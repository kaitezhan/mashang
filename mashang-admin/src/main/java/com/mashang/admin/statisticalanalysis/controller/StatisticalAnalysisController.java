package com.mashang.admin.statisticalanalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.vo.Result;
import com.mashang.statisticalanalysis.param.MerchantOrderAnalysisCondParam;
import com.mashang.statisticalanalysis.service.MerchantStatisticalAnalysisService;
import com.mashang.statisticalanalysis.service.StatisticalAnalysisService;

@Controller
@RequestMapping("/statisticalAnalysis")
public class StatisticalAnalysisController {

	@Autowired
	private StatisticalAnalysisService statisticalAnalysisService;

	@Autowired
	private MerchantStatisticalAnalysisService merchantStatisticalAnalysisService;
	
	@GetMapping("/findPlatformIncome")
	@ResponseBody
	public Result findPlatformIncome() {
		return Result.success().setData(statisticalAnalysisService.findPlatformIncome());
	}
	
	@GetMapping("/findChannelUseSituation")
	@ResponseBody
	public Result findChannelUseSituation() {
		return Result.success().setData(statisticalAnalysisService.findChannelUseSituation());
	}

	@GetMapping("/findTopAgentGatheringSituation")
	@ResponseBody
	public Result findTopAgentGatheringSituation() {
		return Result.success().setData(statisticalAnalysisService.findTopAgentGatheringSituation());
	}

	@GetMapping("/findAdjustCashDepositSituation")
	@ResponseBody
	public Result findAdjustCashDepositSituation() {
		return Result.success().setData(statisticalAnalysisService.findAdjustCashDepositSituation());
	}

	@GetMapping("/findWithdrawSituation")
	@ResponseBody
	public Result findWithdrawSituation() {
		return Result.success().setData(statisticalAnalysisService.findWithdrawSituation());
	}

	@GetMapping("/findRechargeSituation")
	@ResponseBody
	public Result findRechargeSituation() {
		return Result.success().setData(statisticalAnalysisService.findRechargeSituation());
	}

	@GetMapping("/findTodayTop10BountyRank")
	@ResponseBody
	public Result findTodayTop10BountyRank() {
		return Result.success().setData(statisticalAnalysisService.findTodayTop10BountyRank());
	}

	@GetMapping("/findTotalTop10BountyRank")
	@ResponseBody
	public Result findTotalTop10BountyRank() {
		return Result.success().setData(statisticalAnalysisService.findTotalTop10BountyRank());
	}

	@GetMapping("/findCashDepositBounty")
	@ResponseBody
	public Result findCashDepositBounty() {
		return Result.success().setData(statisticalAnalysisService.findCashDepositBounty());
	}

	@GetMapping("/findTradeSituation")
	@ResponseBody
	public Result findTradeSituation() {
		return Result.success().setData(statisticalAnalysisService.findTradeSituation());
	}

}
