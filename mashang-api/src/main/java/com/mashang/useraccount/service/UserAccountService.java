package com.mashang.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashang.common.auth.GoogleAuthInfoVO;
import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.common.exception.BizError;
import com.mashang.common.exception.BizException;
import com.mashang.common.utils.ThreadPoolUtils;
import com.mashang.common.valid.ParamValid;
import com.mashang.common.vo.PageResult;
import com.mashang.constants.Constant;
import com.mashang.mastercontrol.domain.RegisterSetting;
import com.mashang.mastercontrol.repo.RegisterSettingRepo;
import com.mashang.useraccount.domain.UserAccount;
import com.mashang.useraccount.param.AddUserAccountParam;
import com.mashang.useraccount.param.Level1MemberQueryCondParam;
import com.mashang.useraccount.param.ModifyLoginPwdParam;
import com.mashang.useraccount.param.ModifyMoneyPwdParam;
import com.mashang.useraccount.param.UserAccountEditParam;
import com.mashang.useraccount.param.UserAccountQueryCondParam;
import com.mashang.useraccount.param.UserAccountRegisterParam;
import com.mashang.useraccount.repo.TeamNumberOfPeopleRepo;
import com.mashang.useraccount.repo.UserAccountRepo;
import com.mashang.useraccount.vo.Level1MemberVO;
import com.mashang.useraccount.vo.LoginAccountInfoVO;
import com.mashang.useraccount.vo.MemberAccountInfoVO;
import com.mashang.useraccount.vo.UserAccountDetailsInfoVO;
import com.mashang.useraccount.vo.UserAccountInfoVO;
import com.zengtengpeng.annotation.Lock;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class UserAccountService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private RegisterSettingRepo registerSettingRepo;

	@Autowired
	private TeamNumberOfPeopleRepo teamNumberOfPeopleRepo;

	@ParamValid
	@Transactional(readOnly = true)
	public PageResult<Level1MemberVO> findLevel1MemberByPage(Level1MemberQueryCondParam param) {
		Specification<UserAccount> spec = new Specification<UserAccount>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<UserAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				Predicate predicate2 = builder.equal(root.get("inviterId"), param.getCurrentAccountId());
				predicates.add(predicate2);
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.like(root.get("userName"), "%" + param.getUserName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<UserAccount> result = userAccountRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.asc("registeredTime"))));
		PageResult<Level1MemberVO> pageResult = new PageResult<>(Level1MemberVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	public void bindGoogleAuth(String id, String googleSecretKey, String googleVerCode) {
		if (!GoogleAuthenticator.checkCode(googleSecretKey, googleVerCode, System.currentTimeMillis())) {
			throw new BizException(BizError.谷歌验证码不正确);
		}
		UserAccount userAccount = userAccountRepo.getOne(id);
		userAccount.bindGoogleAuth(googleSecretKey);
		userAccountRepo.save(userAccount);
	}

	public void unBindGoogleAuth(String id, String googleVerCode) {
		UserAccount userAccount = userAccountRepo.getOne(id);
		if (!GoogleAuthenticator.checkCode(userAccount.getGoogleSecretKey(), googleVerCode,
				System.currentTimeMillis())) {
			throw new BizException(BizError.谷歌验证码不正确);
		}
		userAccount.unBindGoogleAuth();
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public GoogleAuthInfoVO getGoogleAuthInfo(@NotBlank String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		return GoogleAuthInfoVO.convertFor(userAccount.getUserName(), userAccount.getGoogleSecretKey(),
				userAccount.getGoogleAuthBindTime());
	}

	@Lock(keys = "'receiveOrderState_' + #userAccountId")
	@Transactional
	public void updateReceiveOrderStateWithMember(@NotBlank String userAccountId, @NotBlank String receiveOrderState) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		if (Constant.接单状态_禁止接单.equals(userAccount.getReceiveOrderState())) {
			throw new BizException(BizError.账号已被禁止接单);
		}
		updateReceiveOrderStateInner(userAccountId, receiveOrderState);
	}

	/**
	 * 更新接单状态
	 * 
	 * @param userAccountId
	 * @param receiveOrderState
	 */
	@Lock(keys = "'receiveOrderState_' + #userAccountId")
	@Transactional
	public void updateReceiveOrderStateInner(@NotBlank String userAccountId, @NotBlank String receiveOrderState) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		if (Constant.接单状态_正在接单.equals(receiveOrderState)) {
			if (Constant.账号状态_禁用.equals(userAccount.getState())) {
				throw new BizException(BizError.账号已被禁用);
			}
			Double cashDeposit = userAccount.getCashDeposit();
			if (cashDeposit != null && cashDeposit <= 0) {
				throw new BizException(BizError.保证金不足无法接单);
			}
		}
		userAccount.setReceiveOrderState(receiveOrderState);
		userAccountRepo.save(userAccount);

		if (Constant.接单状态_正在接单.equals(receiveOrderState)) {
		} else {
		}
	}

	/**
	 * 更新最近登录时间
	 */
	@Transactional
	public void updateLatelyLoginTime(String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setLatelyLoginTime(new Date());
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void updateUserAccount(UserAccountEditParam param) {
		UserAccount existUserAccount = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (existUserAccount != null && !existUserAccount.getId().equals(param.getId())) {
			throw new BizException(BizError.账号已存在);
		}
		UserAccount userAccount = userAccountRepo.getOne(param.getId());
		BeanUtils.copyProperties(param, userAccount);
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public UserAccountDetailsInfoVO findUserAccountDetailsInfoById(String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		return UserAccountDetailsInfoVO.convertFor(userAccount);
	}

	@Transactional(readOnly = true)
	public PageResult<UserAccountDetailsInfoVO> findUserAccountDetailsInfoByPage(UserAccountQueryCondParam param) {
		Specification<UserAccount> spec = new Specification<UserAccount>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<UserAccount> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (CollectionUtil.isNotEmpty(param.getAccountTypes())) {
					predicates.add(root.get("accountType").in(param.getAccountTypes()));
				}
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.like(root.get("userName"), "%" + param.getUserName() + "%"));
				}
				if (StrUtil.isNotEmpty(param.getRealName())) {
					predicates.add(builder.like(root.get("realName"), "%" + param.getRealName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<UserAccount> result = userAccountRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1,
				param.getPageSize(), Sort.by(Direction.fromString(param.getDirection()), param.getPropertie())));
		PageResult<UserAccountDetailsInfoVO> pageResult = new PageResult<>(
				UserAccountDetailsInfoVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@ParamValid
	@Transactional
	public void modifyLoginPwd(ModifyLoginPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldLoginPwd(), userAccount.getLoginPwd())) {
			throw new BizException(BizError.旧的登录密码不正确);
		}
		modifyLoginPwd(param.getUserAccountId(), param.getNewLoginPwd());
	}

	@Transactional
	public void modifyLoginPwd(@NotBlank String userAccountId, @NotBlank String newLoginPwd) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setLoginPwd(new BCryptPasswordEncoder().encode(newLoginPwd));
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void modifyMoneyPwd(ModifyMoneyPwdParam param) {
		UserAccount userAccount = userAccountRepo.getOne(param.getUserAccountId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldMoneyPwd(), userAccount.getMoneyPwd())) {
			throw new BizException(BizError.旧的资金密码不正确);
		}
		String newMoneyPwd = pwdEncoder.encode(param.getNewMoneyPwd());
		userAccount.setMoneyPwd(newMoneyPwd);
		userAccountRepo.save(userAccount);
	}

	@ParamValid
	@Transactional
	public void modifyMoneyPwd(@NotBlank String userAccountId, @NotBlank String newMoneyPwd) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.setMoneyPwd(new BCryptPasswordEncoder().encode(newMoneyPwd));
		userAccountRepo.save(userAccount);
	}

	@Transactional(readOnly = true)
	public LoginAccountInfoVO getLoginAccountInfo(String userName) {
		return LoginAccountInfoVO.convertFor(userAccountRepo.findByUserNameAndDeletedFlagIsFalse(userName));
	}

	@Transactional(readOnly = true)
	public MemberAccountInfoVO getMemberAccountInfo(String userAccountId) {
		return MemberAccountInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@Transactional(readOnly = true)
	public UserAccountInfoVO getUserAccountInfo(String userAccountId) {
		return UserAccountInfoVO.convertFor(userAccountRepo.getOne(userAccountId));
	}

	@ParamValid
	@Transactional
	public void addUserAccount(AddUserAccountParam param) {
		UserAccount userAccount = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (userAccount != null) {
			throw new BizException(BizError.账号已存在);
		}
		String encodePwd = new BCryptPasswordEncoder().encode(param.getLoginPwd());
		param.setLoginPwd(encodePwd);
		UserAccount newUserAccount = param.convertToPo();
		if (StrUtil.isNotBlank(param.getInviterUserName())) {
			UserAccount inviter = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getInviterUserName());
			if (inviter == null) {
				throw new BizException(BizError.邀请人不存在);
			}
			newUserAccount.setInviterId(inviter.getId());
			newUserAccount.setAccountLevel(inviter.getAccountLevel() + 1);
			newUserAccount.setAccountLevelPath(inviter.getAccountLevelPath() + "." + newUserAccount.getId());
		}
		userAccountRepo.save(newUserAccount);
	}

	@Lock(keys = "'userName_' + #param.userName")
	@ParamValid
	@Transactional
	public void register(UserAccountRegisterParam param) {
		RegisterSetting setting = registerSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (!setting.getRegisterFun()) {
			throw new BizException(BizError.未开放注册功能);
		}
		UserAccount existAccount = userAccountRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (existAccount != null) {
			throw new BizException(BizError.账号已存在);
		}
		param.setLoginPwd(new BCryptPasswordEncoder().encode(param.getLoginPwd()));
		param.setMoneyPwd(new BCryptPasswordEncoder().encode(param.getMoneyPwd()));
		UserAccount newUserAccount = param.convertToPo();
		if (setting.getInviteRegisterMode()) {
			if (StrUtil.isBlank(param.getInviterId())) {
				throw new BizException(BizError.业务异常.getCode(), "邀请id不能为空");
			}
			UserAccount inviter = userAccountRepo.findByIdAndDeletedFlagIsFalse(param.getInviterId());
			newUserAccount.updateInviteInfo(inviter);
		}
		userAccountRepo.save(newUserAccount);
	}

	@Transactional
	public void delUserAccount(@NotBlank String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		userAccount.deleted();
		userAccountRepo.save(userAccount);
		ThreadPoolUtils.getLoginLogPool().schedule(() -> {
			redisTemplate.opsForList().leftPush(Constant.已删除账号ID, userAccountId);
		}, 600, TimeUnit.MILLISECONDS);
	}

	@ParamValid
	@Transactional
	public void adjustCashDeposit(@NotBlank String userAccountId,
			@NotNull @DecimalMin(value = "0", inclusive = true) Double cashDeposit, @NotBlank String higherLevelId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		double changeAmount = cashDeposit - userAccount.getCashDeposit();
		if (changeAmount == 0) {
			return;
		}
		UserAccount higherLevelAccount = userAccountRepo.getOne(higherLevelId);
		userAccount.setCashDeposit(NumberUtil.round(cashDeposit, 2).doubleValue());
		userAccountRepo.save(userAccount);
	}

}
