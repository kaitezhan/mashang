package com.mashang.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashang.common.ip.IpInfoVO;
import com.mashang.common.ip.IpUtils;
import com.mashang.common.utils.IdUtils;
import com.mashang.common.utils.ThreadPoolUtils;
import com.mashang.common.vo.PageResult;
import com.mashang.useraccount.domain.LoginLog;
import com.mashang.useraccount.domain.UserAccount;
import com.mashang.useraccount.param.LoginLogQueryCondParam;
import com.mashang.useraccount.repo.LoginLogRepo;
import com.mashang.useraccount.repo.MemberLatelyLoginLogRepo;
import com.mashang.useraccount.repo.UserAccountRepo;
import com.mashang.useraccount.vo.LoginLogVO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;

@Validated
@Service
public class LoginLogService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Autowired
	private MemberLatelyLoginLogRepo memberLatelyLoginLogRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

	@Autowired
	private UserAccountService userAccountService;

	@Transactional
	public void logoutWithUserAccountId(@NotBlank String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		Map<String, ? extends Session> sessionMap = sessionRepository.findByPrincipalName(userAccount.getUserName());
		for (Entry<String, ? extends Session> entry : sessionMap.entrySet()) {
			logout(entry.getKey());
		}
	}

	@Transactional
	public void logout(@NotBlank String sessionId) {
		sessionRepository.deleteById(sessionId);
	}

	@Transactional
	public List<String> getSessionId() {
		List<String> sessionIds = new ArrayList<>();
		String prefix = "spring:session:index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":";
		Set<String> keys = redisTemplate.keys(prefix + "*");
		for (String key : keys) {
			String principalName = key.split(prefix)[1];
			Map<String, ? extends Session> sessionMap = sessionRepository.findByPrincipalName(principalName);
			for (Entry<String, ? extends Session> entry : sessionMap.entrySet()) {
				sessionIds.add(entry.getKey());
			}
		}
		return sessionIds;
	}

	@Transactional(readOnly = true)
	public PageResult<LoginLogVO> findOnlineAccountByPage(LoginLogQueryCondParam param) {
		param.setSessionIds(getSessionId());
		return findLoginLogByPage(param);
	}

	public void updateLastAccessTimeWithRedis(String sessionId) {
		redisTemplate.opsForValue().set("updateLastAccessTime_" + sessionId,
				DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN), 90, TimeUnit.SECONDS);
	}

	@Transactional
	public void updateLastAccessTime() {
		String prefix = "updateLastAccessTime_";
		Set<String> keys = redisTemplate.keys(prefix + "*");
		for (String key : keys) {
			String sessionId = key.split(prefix)[1];
			updateLastAccessTime(sessionId,
					DateUtil.parse(redisTemplate.opsForValue().get(key), DatePattern.NORM_DATETIME_PATTERN));
		}
	}

	@Transactional
	public void updateLastAccessTime(String sessionId, Date lastAccessTime) {
		LoginLog loginLog = loginLogRepo.findTopBySessionIdOrderByLoginTime(sessionId);
		if (loginLog != null) {
			if (loginLog.getLastAccessTime() == null
					|| loginLog.getLastAccessTime().getTime() != lastAccessTime.getTime()) {
				loginLog.setLastAccessTime(lastAccessTime);
				loginLogRepo.save(loginLog);
			}
		}
	}

	@Transactional(readOnly = true)
	public PageResult<LoginLogVO> findLoginLogByPage(LoginLogQueryCondParam param) {
		Specification<LoginLog> spec = new Specification<LoginLog>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<LoginLog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getIpAddr())) {
					predicates.add(builder.equal(root.get("ipAddr"), param.getIpAddr()));
				}
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.equal(root.get("userName"), param.getUserName()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("loginTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("loginTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				if (CollectionUtil.isNotEmpty(param.getSessionIds())) {
					predicates.add(root.get("sessionId").in(param.getSessionIds()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<LoginLog> result = loginLogRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("loginTime"))));
		PageResult<LoginLogVO> pageResult = new PageResult<>(LoginLogVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void recordLoginLog(String sessionId, String userName, String system, String state, String msg,
			String ipAddr, UserAgent userAgent) {
		ThreadPoolUtils.getLoginLogPool().schedule(() -> {
			Date now = new Date();
			String loginLocation = null;
			IpInfoVO ipInfo = IpUtils.getIpInfo(ipAddr);
			if (ipInfo != null) {
				loginLocation = ipInfo.getProvince() + ipInfo.getCity();
			}
			LoginLog loginLog = new LoginLog();
			loginLog.setId(IdUtils.getId());
			loginLog.setSessionId(sessionId);
			loginLog.setUserName(userName);
			loginLog.setSystem(system);
			loginLog.setState(state);
			loginLog.setMsg(msg);
			loginLog.setIpAddr(ipAddr);
			loginLog.setLoginLocation(loginLocation);
			loginLog.setLoginTime(now);
			loginLog.setLastAccessTime(now);
			loginLog.setBrowser(userAgent.getBrowser().getName());
			loginLog.setOs(userAgent.getOs().getName());
			loginLogRepo.save(loginLog);

		}, 10, TimeUnit.MILLISECONDS);
	}

}
