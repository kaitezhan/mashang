package com.mashang.useraccount.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.BeanUtils;

import com.mashang.common.utils.IdUtils;
import com.mashang.constants.Constant;
import com.mashang.useraccount.domain.UserAccount;

import lombok.Data;

@Data
public class UserAccountRegisterParam {

	private String inviterId;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{2,11}$")
	private String userName;
	
	@NotBlank
	private String realName;
	
	@NotBlank
	private String mobile;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{5,14}$")
	private String loginPwd;

	@NotBlank
	private String moneyPwd;

	public UserAccount convertToPo() {
		UserAccount po = new UserAccount();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setState(Constant.账号状态_启用);
		po.setDeletedFlag(false);
		po.setRegisteredTime(new Date());
		po.setAccountType(Constant.账号类型_会员);
		po.setReceiveOrderState(Constant.接单状态_停止接单);
		po.setCashDeposit(0d);
		po.setAccountLevel(1);
		po.setAccountLevelPath(po.getId());
		return po;
	}

}
