package com.mashang;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/my-team")
	public String myTeam() {
		return "/my-team";
	}

	@GetMapping("/promote-qr")
	public String promoteQr() {
		return "promote-qr";
	}

	@GetMapping("/bind-google-auth")
	public String bindGoogleAuth() {
		return "bind-google-auth";
	}

	@GetMapping("/modify-money-pwd")
	public String modifyMoneyPwd() {
		return "modify-money-pwd";
	}

	@GetMapping("/modify-login-pwd")
	public String modifyLoginPwd() {
		return "modify-login-pwd";
	}

	@GetMapping("/bank-card")
	public String bankCard() {
		return "bank-card";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/")
	public String home() {
		return "my-home-page";
	}

	@GetMapping("/my-home-page")
	public String myHomePage() {
		return "my-home-page";
	}

	@GetMapping("/personal-info")
	public String personalInfo() {
		return "personal-info";
	}

	@GetMapping("/personal-account-change")
	public String personalAccountChange() {
		return "personal-account-change";
	}

	@GetMapping("/recharge")
	public String recharge() {
		return "recharge";
	}

	@GetMapping("/withdraw")
	public String withdraw() {
		return "withdraw";
	}

	@GetMapping("/gathering-code")
	public String gatheringCode() {
		return "gathering-code";
	}

	@GetMapping("/receive-order")
	public String receiveOrder() {
		return "receive-order";
	}

	@GetMapping("/audit-order")
	public String auditOrder() {
		return "audit-order";
	}

	@GetMapping("/receive-order-record")
	public String receiveOrderRecord() {
		return "receive-order-record";
	}

	@GetMapping("/customer-service")
	public String customerService() {
		return "customer-service";
	}

}
