package com.mashang.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.mashang.common.utils.IdUtils;
import com.mashang.constants.Constant;
import com.mashang.merchant.domain.Merchant;

import lombok.Data;

@Data
public class AddMerchantParam {

	@NotBlank
	private String userName;

	@NotBlank
	private String loginPwd;

	@NotBlank
	private String merchantNum;

	@NotBlank
	private String merchantName;

	private String accountType;

	private String inviterId;

	@NotBlank
	private String secretKey;

	private String notifyUrl;

	private String returnUrl;

	public Merchant convertToPo() {
		Merchant po = new Merchant();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		po.setWithdrawableAmount(0D);
		po.setMoneyPwd(po.getLoginPwd());
		po.setState(Constant.账号状态_启用);
		return po;
	}

}
