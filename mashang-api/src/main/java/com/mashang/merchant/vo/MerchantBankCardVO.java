package com.mashang.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mashang.merchant.domain.MerchantBankCard;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MerchantBankCardVO {

	private String id;

	private String openAccountBank;

	private String accountHolder;

	private String bankCardAccount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bankInfoLatelyModifyTime;

	public static List<MerchantBankCardVO> convertFor(List<MerchantBankCard> merchantBankCards) {
		if (CollectionUtil.isEmpty(merchantBankCards)) {
			return new ArrayList<>();
		}
		List<MerchantBankCardVO> vos = new ArrayList<>();
		for (MerchantBankCard merchantBankCard : merchantBankCards) {
			vos.add(convertFor(merchantBankCard));
		}
		return vos;
	}

	public static MerchantBankCardVO convertFor(MerchantBankCard merchantBankCard) {
		if (merchantBankCard == null) {
			return null;
		}
		MerchantBankCardVO vo = new MerchantBankCardVO();
		BeanUtils.copyProperties(merchantBankCard, vo);
		return vo;
	}

}
