package com.mashang.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.mashang.common.utils.IdUtils;
import com.mashang.merchant.domain.MerchantBankCard;

import lombok.Data;

@Data
public class AddOrUpdateMerchantBankCardParam {

	private String id;

	@NotBlank
	private String openAccountBank;

	@NotBlank
	private String accountHolder;

	@NotBlank
	private String bankCardAccount;

	public MerchantBankCard convertToPo(String merchantId) {
		MerchantBankCard po = new MerchantBankCard();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		po.setBankInfoLatelyModifyTime(po.getCreateTime());
		po.setMerchantId(merchantId);
		return po;
	}

}
