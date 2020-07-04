package com.mashang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/merchant-account-change-log")
	public String merchantAccountChangeLog() {
		return "merchant-account-change-log";
	}
	
	@GetMapping("/lower-level-merchant-order")
	public String lowerLevelMerchantOrder() {
		return "lower-level-merchant-order";
	}
	
	@GetMapping("/lower-level-merchant")
	public String lowerLevelMerchant() {
		return "lower-level-merchant";
	}

	@GetMapping("/")
	public String index() {
		return "statistical-analysis";
	}

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * 商户订单
	 * 
	 * @return
	 */
	@GetMapping("/merchant-order")
	public String merchantOrder() {
		return "merchant-order";
	}

	/**
	 * 统计分析
	 * 
	 * @return
	 */
	@GetMapping("/statistical-analysis")
	public String statisticalAnalysis() {
		return "statistical-analysis";
	}

	/**
	 * 申诉记录
	 * 
	 * @return
	 */
	@GetMapping("/appeal-record")
	public String appealRecord() {
		return "appeal-record";
	}

	@GetMapping("/appeal-details")
	public String appealDetails() {
		return "appeal-details";
	}

	@GetMapping("/merchant-info")
	public String merchantInfo() {
		return "merchant-info";
	}

	@GetMapping("/rate-details")
	public String rateDetails() {
		return "rate-details";
	}

	@GetMapping("/apply-settlement")
	public String applySettlement() {
		return "apply-settlement";
	}

}
