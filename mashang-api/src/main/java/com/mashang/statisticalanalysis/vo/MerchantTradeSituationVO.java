package com.mashang.statisticalanalysis.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.mashang.statisticalanalysis.domain.merchant.MerchantTradeSituation;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MerchantTradeSituationVO {

	private String id;

	private String merchantName;

	private Double tradeAmount;

	private Double poundage;

	private Double actualIncome;

	private Long paidOrderNum;

	private Long orderNum;

	private Double successRate;

	private Double withdrawAmount;

	private Double totalTradeAmount;

	private Double totalPoundage;

	private Double totalActualIncome;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double totalWithdrawAmount;

	private Double monthTradeAmount;

	private Double monthPoundage;

	private Double monthActualIncome;

	private Long monthPaidOrderNum;

	private Long monthOrderNum;

	private Double monthSuccessRate;

	private Double monthWithdrawAmount;

	private Double yesterdayTradeAmount;

	private Double yesterdayPoundage;

	private Double yesterdayActualIncome;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double yesterdayWithdrawAmount;

	private Double todayTradeAmount;

	private Double todayPoundage;

	private Double todayActualIncome;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	private Double todayWithdrawAmount;

	public static List<MerchantTradeSituationVO> convertFor(List<MerchantTradeSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<MerchantTradeSituationVO> vos = new ArrayList<>();
		for (MerchantTradeSituation situation : situations) {
			vos.add(convertFor(situation));
		}
		return vos;
	}

	public static MerchantTradeSituationVO convertFor(MerchantTradeSituation situation) {
		if (situation == null) {
			return null;
		}
		MerchantTradeSituationVO vo = new MerchantTradeSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}

	public static MerchantTradeSituationVO build(String id, String merchantName) {
		MerchantTradeSituationVO vo = new MerchantTradeSituationVO();
		vo.setId(id);
		vo.setMerchantName(merchantName);
		vo.setTradeAmount(0d);
		vo.setPoundage(0d);
		vo.setActualIncome(0d);
		vo.setPaidOrderNum(0L);
		vo.setOrderNum(0L);
		vo.setSuccessRate(0d);
		vo.setWithdrawAmount(0d);
		return vo;
	}

}
