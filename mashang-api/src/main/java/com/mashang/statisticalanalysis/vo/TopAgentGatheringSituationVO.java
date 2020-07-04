package com.mashang.statisticalanalysis.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.mashang.statisticalanalysis.domain.TopAgentGatheringSituation;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class TopAgentGatheringSituationVO {

	private String id;

	private String userName;

	private Double totalTradeAmount;

	private Double totalBounty;

	private Double totalRebateAmount;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double monthTradeAmount;

	private Double monthBounty;

	private Double monthRebateAmount;

	private Long monthPaidOrderNum;

	private Long monthOrderNum;

	private Double monthSuccessRate;

	private Double yesterdayTradeAmount;

	private Double yesterdayBounty;

	private Double yesterdayRebateAmount;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Double todayBounty;

	private Double todayRebateAmount;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	public static List<TopAgentGatheringSituationVO> convertFor(List<TopAgentGatheringSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<TopAgentGatheringSituationVO> vos = new ArrayList<>();
		for (TopAgentGatheringSituation situation : situations) {
			vos.add(convertFor(situation));
		}
		return vos;
	}

	public static TopAgentGatheringSituationVO convertFor(TopAgentGatheringSituation situation) {
		if (situation == null) {
			return null;
		}
		TopAgentGatheringSituationVO vo = new TopAgentGatheringSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}

}
