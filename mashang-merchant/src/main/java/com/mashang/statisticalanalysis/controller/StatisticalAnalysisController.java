package com.mashang.statisticalanalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.vo.Result;
import com.mashang.statisticalanalysis.param.MerchantIndexQueryParam;
import com.mashang.statisticalanalysis.service.MerchantStatisticalAnalysisService;
import com.mashang.useraccount.vo.MerchantAccountDetails;

@Controller
@RequestMapping("/statisticalAnalysis")
public class StatisticalAnalysisController {

	@Autowired
	private MerchantStatisticalAnalysisService statisticalAnalysisService;

	@GetMapping("/findMerchantChannelTradeSituation")
	@ResponseBody
	public Result findMerchantChannelTradeSituation() {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(
				statisticalAnalysisService.findMerchantChannelTradeSituationByMerchantId(user.getMerchantId()));
	}

	@GetMapping("/findTradeSituation")
	@ResponseBody
	public Result findTradeSituation() {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success()
				.setData(statisticalAnalysisService.findMerchantTradeSituationById(user.getMerchantId()));
	}

	@GetMapping("/findEverydayStatistical")
	@ResponseBody
	public Result findEverydayStatistical(MerchantIndexQueryParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		return Result.success().setData(statisticalAnalysisService.findEverydayStatistical(param));
	}

}
