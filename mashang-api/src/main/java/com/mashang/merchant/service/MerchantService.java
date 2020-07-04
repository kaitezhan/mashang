package com.mashang.merchant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashang.common.auth.GoogleAuthInfoVO;
import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.common.exception.BizError;
import com.mashang.common.exception.BizException;
import com.mashang.common.valid.ParamValid;
import com.mashang.common.vo.PageResult;
import com.mashang.constants.Constant;
import com.mashang.merchant.domain.Merchant;
import com.mashang.merchant.domain.MerchantBankCard;
import com.mashang.merchant.param.AddMerchantParam;
import com.mashang.merchant.param.AddOrUpdateMerchantBankCardParam;
import com.mashang.merchant.param.AdjustWithdrawableAmountParam;
import com.mashang.merchant.param.MerchantEditParam;
import com.mashang.merchant.param.MerchantQueryCondParam;
import com.mashang.merchant.param.ModifyLoginPwdParam;
import com.mashang.merchant.param.ModifyMoneyPwdParam;
import com.mashang.merchant.repo.MerchantBankCardRepo;
import com.mashang.merchant.repo.MerchantRepo;
import com.mashang.merchant.vo.LoginMerchantInfoVO;
import com.mashang.merchant.vo.MerchantBankCardVO;
import com.mashang.merchant.vo.MerchantVO;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class MerchantService {

	@Autowired
	private MerchantRepo merchantRepo;

	@Autowired
	private MerchantBankCardRepo merchantBankCardRepo;


	@Transactional
	public void bindGoogleAuth(String id, String googleSecretKey, String googleVerCode) {
		if (!GoogleAuthenticator.checkCode(googleSecretKey, googleVerCode, System.currentTimeMillis())) {
			throw new BizException(BizError.谷歌验证码不正确);
		}
		Merchant merchant = merchantRepo.getOne(id);
		merchant.bindGoogleAuth(googleSecretKey);
		merchantRepo.save(merchant);
	}

	public GoogleAuthInfoVO getGoogleAuthInfo(String id) {
		Merchant merchant = merchantRepo.getOne(id);
		return GoogleAuthInfoVO.convertFor(merchant.getUserName(), merchant.getGoogleSecretKey(),
				merchant.getGoogleAuthBindTime());
	}

	@ParamValid
	@Transactional
	public void adjustWithdrawableAmount(AdjustWithdrawableAmountParam param) {
		Merchant merchant = merchantRepo.getOne(param.getMerchantId());
		if (Constant.商户账变日志类型_手工增可提现金额.equals(param.getAccountChangeTypeCode())) {
			Double withdrawableAmount = NumberUtil
					.round(merchant.getWithdrawableAmount() + param.getAccountChangeAmount(), 2).doubleValue();
			merchant.setWithdrawableAmount(withdrawableAmount);
			merchantRepo.save(merchant);
		} else if (Constant.商户账变日志类型_手工减可提现金额.equals(param.getAccountChangeTypeCode())) {
			Double withdrawableAmount = NumberUtil
					.round(merchant.getWithdrawableAmount() - param.getAccountChangeAmount(), 2).doubleValue();
			if (withdrawableAmount < 0) {
				throw new BizException(BizError.可提现金额不足无法手工减金额);
			}
			merchant.setWithdrawableAmount(withdrawableAmount);
			merchantRepo.save(merchant);
		}
	}

	@Transactional(readOnly = true)
	public MerchantBankCardVO findMerchantBankCardById(@NotBlank String id) {
		return MerchantBankCardVO.convertFor(merchantBankCardRepo.getOne(id));
	}

	public void deleteMerchantBankCard(String merchantBankCardId, String merchantId) {
		MerchantBankCard merchantBankCard = merchantBankCardRepo.getOne(merchantBankCardId);
		if (!merchantBankCard.getMerchantId().equals(merchantId)) {
			throw new BizException(BizError.参数异常);
		}
		merchantBankCard.delete();
		merchantBankCardRepo.save(merchantBankCard);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateMerchantBankCard(AddOrUpdateMerchantBankCardParam param, String merchantId) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			MerchantBankCard merchantBankCard = param.convertToPo(merchantId);
			merchantBankCardRepo.save(merchantBankCard);
		}
		// 修改
		else {
			MerchantBankCard merchantBankCard = merchantBankCardRepo.getOne(param.getId());
			if (!merchantBankCard.getMerchantId().equals(merchantId)) {
				throw new BizException(BizError.无权修改商户银行卡信息);
			}
			BeanUtils.copyProperties(param, merchantBankCard);
			merchantBankCardRepo.save(merchantBankCard);
		}
	}

	@Transactional(readOnly = true)
	public List<MerchantBankCardVO> findMerchantBankCardByMerchantId(@NotBlank String merchantId) {
		return MerchantBankCardVO.convertFor(merchantBankCardRepo.findByMerchantIdAndDeletedFlagIsFalse(merchantId));
	}

	@Transactional
	public void modifyLowerLevelLoginPwd(@NotBlank String id, @NotBlank String newLoginPwd,
			@NotBlank String inviterId) {
		MerchantVO vo = findMerchantById(id);
		if (!inviterId.equals(vo.getInviterId())) {
			throw new BizException(BizError.无权操作);
		}
		modifyLoginPwd(id, newLoginPwd);
	}

	@ParamValid
	@Transactional
	public void modifyLoginPwd(ModifyLoginPwdParam param) {
		Merchant merchant = merchantRepo.getOne(param.getMerchantId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldLoginPwd(), merchant.getLoginPwd())) {
			throw new BizException(BizError.旧的登录密码不正确);
		}
		modifyLoginPwd(merchant.getId(), param.getNewLoginPwd());
	}

	@Transactional
	public void modifyLowerLevelMoneyPwd(@NotBlank String id, @NotBlank String newMoneyPwd,
			@NotBlank String inviterId) {
		MerchantVO vo = findMerchantById(id);
		if (!inviterId.equals(vo.getInviterId())) {
			throw new BizException(BizError.无权操作);
		}
		modifyMoneyPwd(id, newMoneyPwd);
	}

	@ParamValid
	@Transactional
	public void modifyMoneyPwd(ModifyMoneyPwdParam param) {
		Merchant merchant = merchantRepo.getOne(param.getMerchantId());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		if (!pwdEncoder.matches(param.getOldMoneyPwd(), merchant.getMoneyPwd())) {
			throw new BizException(BizError.旧的资金密码不正确);
		}
		modifyMoneyPwd(merchant.getId(), param.getNewMoneyPwd());
	}

	@Transactional(readOnly = true)
	public List<MerchantVO> findAllMerchant() {
		return MerchantVO.convertFor(merchantRepo.findByDeletedFlagIsFalse());
	}

	@Transactional(readOnly = true)
	public List<MerchantVO> findAllMerchantAgent() {
		return MerchantVO.convertFor(merchantRepo.findByAccountTypeAndDeletedFlagIsFalse(Constant.商户账号类型_商户代理));
	}

	@Transactional(readOnly = true)
	public MerchantVO getMerchantInfo(String id) {
		return MerchantVO.convertFor(merchantRepo.getOne(id));
	}

	/**
	 * 更新最近登录时间
	 */
	@Transactional
	public void updateLatelyLoginTime(String id) {
		Merchant merchant = merchantRepo.getOne(id);
		merchant.setLatelyLoginTime(new Date());
		merchantRepo.save(merchant);
	}

	@Transactional(readOnly = true)
	public LoginMerchantInfoVO getLoginMerchantInfo(String userName) {
		return LoginMerchantInfoVO.convertFor(merchantRepo.findByUserNameAndDeletedFlagIsFalse(userName));
	}

	@Transactional
	public void modifyLoginPwd(@NotBlank String id, @NotBlank String newLoginPwd) {
		Merchant merchant = merchantRepo.getOne(id);
		merchant.setLoginPwd(new BCryptPasswordEncoder().encode(newLoginPwd));
		merchantRepo.save(merchant);
	}

	@Transactional
	public void modifyMoneyPwd(@NotBlank String id, @NotBlank String newMoneyPwd) {
		Merchant merchant = merchantRepo.getOne(id);
		merchant.setMoneyPwd(new BCryptPasswordEncoder().encode(newMoneyPwd));
		merchantRepo.save(merchant);
	}

	@Transactional(readOnly = true)
	public MerchantVO findLowerLevelMerchantById(@NotBlank String id, @NotBlank String inviterId) {
		MerchantVO vo = findMerchantById(id);
		if (!inviterId.equals(vo.getInviterId())) {
			throw new BizException(BizError.无权操作);
		}
		return vo;
	}

	@Transactional(readOnly = true)
	public MerchantVO findMerchantById(@NotBlank String id) {
		return MerchantVO.convertFor(merchantRepo.getOne(id));
	}

	@Transactional
	public void delLowerLevelMerchantById(@NotBlank String id, @NotBlank String inviterId) {
		Merchant merchant = merchantRepo.getOne(id);
		if (!inviterId.equals(merchant.getInviterId())) {
			throw new BizException(BizError.无权操作);
		}
		delMerchantById(id);
	}

	@Transactional
	public void delMerchantById(@NotBlank String id) {
		Merchant merchant = merchantRepo.getOne(id);
		merchant.setDeletedFlag(true);
		merchantRepo.save(merchant);
	}

	@ParamValid
	@Transactional
	public void addLowerLevelMerchant(AddMerchantParam param, @NotBlank String inviterId) {
		param.setInviterId(inviterId);
		param.setAccountType(Constant.商户账号类型_普通商户);
		addMerchant(param);
	}

	@ParamValid
	@Transactional
	public void addMerchant(AddMerchantParam param) {
		if (Constant.商户账号类型_商户代理.equals(param.getAccountType())) {
			param.setInviterId(null);
		}
		Merchant merchantWithUserName = merchantRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (merchantWithUserName != null) {
			throw new BizException(BizError.用户名已使用);
		}
		Merchant merchantWithMerchantNum = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(param.getMerchantNum());
		if (merchantWithMerchantNum != null) {
			throw new BizException(BizError.商户号已使用);
		}
		Merchant merchantWithName = merchantRepo.findByMerchantNameAndDeletedFlagIsFalse(param.getMerchantName());
		if (merchantWithName != null) {
			throw new BizException(BizError.商户名称已使用);
		}
		param.setLoginPwd(new BCryptPasswordEncoder().encode(param.getLoginPwd()));
		Merchant merchant = param.convertToPo();
		merchantRepo.save(merchant);
	}

	@ParamValid
	@Transactional
	public void updateLowerLevelMerchant(MerchantEditParam param, @NotBlank String inviterId) {
		Merchant merchant = merchantRepo.getOne(param.getId());
		if (!inviterId.equals(merchant.getInviterId())) {
			throw new BizException(BizError.无权操作);
		}
		updateMerchant(param);
	}

	@ParamValid
	@Transactional
	public void updateMerchant(MerchantEditParam param) {
		Merchant merchantWithUserName = merchantRepo.findByUserNameAndDeletedFlagIsFalse(param.getUserName());
		if (merchantWithUserName != null && !merchantWithUserName.getId().equals(param.getId())) {
			throw new BizException(BizError.用户名已使用);
		}
		Merchant merchantWithMerchantNum = merchantRepo.findByMerchantNumAndDeletedFlagIsFalse(param.getMerchantNum());
		if (merchantWithMerchantNum != null && !merchantWithMerchantNum.getId().equals(param.getId())) {
			throw new BizException(BizError.商户号已使用);
		}
		Merchant merchantWithName = merchantRepo.findByMerchantNameAndDeletedFlagIsFalse(param.getMerchantName());
		if (merchantWithName != null && !merchantWithName.getId().equals(param.getId())) {
			throw new BizException(BizError.商户名称已使用);
		}
		Merchant merchant = merchantRepo.getOne(param.getId());
		BeanUtils.copyProperties(param, merchant);
		merchantRepo.save(merchant);
	}

	@Transactional(readOnly = true)
	public PageResult<MerchantVO> findMerchantByPageWithInviter(MerchantQueryCondParam param) {
		return findMerchantByPage(param);
	}

	@Transactional(readOnly = true)
	public PageResult<MerchantVO> findMerchantByPage(MerchantQueryCondParam param) {
		Specification<Merchant> spec = new Specification<Merchant>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Merchant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotBlank(param.getMerchantName())) {
					predicates.add(builder.equal(root.get("merchantName"), param.getMerchantName()));
				}
				if (StrUtil.isNotBlank(param.getInviterId())) {
					predicates.add(builder.equal(root.get("inviterId"), param.getInviterId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<Merchant> result = merchantRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<MerchantVO> pageResult = new PageResult<>(MerchantVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
