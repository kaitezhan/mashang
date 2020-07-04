package com.mashang.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mashang.common.valid.PermissionValid;

@Controller
public class PageController {
	
	@GetMapping("/permission-fail")
	public String permissionFail() {
		return "permission-fail";
	}
	
	@PermissionValid
	@GetMapping("/role-manage")
	public String roleManage() {
		return "role-manage";
	}
	
	@PermissionValid
	@GetMapping("/menu-manage")
	public String menuManage() {
		return "menu-manage";
	}
	
	@PermissionValid
	@GetMapping("/real-time-queue-record")
	public String realTimeQueueRecord() {
		return "real-time-queue-record";
	}
	
	@PermissionValid
	@GetMapping("/credit-member")
	public String creditMember() {
		return "credit-member";
	}
	
	@PermissionValid
	@GetMapping("/member")
	public String member() {
		return "member";
	}
	
	@PermissionValid
	@GetMapping("/agent")
	public String agent() {
		return "agent";
	}

	@PermissionValid
	@GetMapping("/background-account")
	public String backgroundAccount() {
		return "background-account";
	}

	@PermissionValid("/statistical-analysis")
	@GetMapping("/")
	public String index() {
		return "statistical-analysis";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PermissionValid
	@GetMapping("/merchant-account-change-log")
	public String merchantAccountChangeLog() {
		return "merchant-account-change-log";
	}

	@PermissionValid
	@GetMapping("/account-change-log")
	public String accountChangeLog() {
		return "account-change-log";
	}

	@PermissionValid
	@GetMapping("/recharge-order")
	public String rechargeOrder() {
		return "recharge-order";
	}

	@PermissionValid
	@GetMapping("/withdraw-record")
	public String withdrawRecord() {
		return "withdraw-record";
	}

	@PermissionValid
	@GetMapping("/dict-manage")
	public String dictManage() {
		return "dict-manage";
	}

	@PermissionValid
	@GetMapping("/master-control-room")
	public String masterControlRoom() {
		return "master-control-room";
	}

	@PermissionValid
	@GetMapping("/merchant-order")
	public String merchantOrder() {
		return "merchant-order";
	}

	@PermissionValid
	@GetMapping("/merchant")
	public String merchant() {
		return "merchant";
	}

	@PermissionValid
	@GetMapping("/statistical-analysis")
	public String statisticalAnalysis() {
		return "statistical-analysis";
	}

	@PermissionValid
	@GetMapping("/gathering-code")
	public String gatheringCode() {
		return "gathering-code";
	}

	@PermissionValid
	@GetMapping("/recharge-channel")
	public String rechargeChannel() {
		return "recharge-channel";
	}

	@PermissionValid
	@GetMapping("/login-log")
	public String loginLog() {
		return "login-log";
	}


	@PermissionValid
	@GetMapping("/gathering-channel")
	public String gatheringChannel() {
		return "gathering-channel";
	}

	@PermissionValid
	@GetMapping("/online-account")
	public String onlineAccount() {
		return "online-account";
	}

	@PermissionValid
	@GetMapping("/merchant-settlement-record")
	public String merchantSettlementRecord() {
		return "merchant-settlement-record";
	}

	@PermissionValid
	@GetMapping("/data-clean")
	public String dataClean() {
		return "data-clean";
	}

	@PermissionValid
	@GetMapping("/system-notice")
	public String systemNotice() {
		return "system-notice";
	}

	@PermissionValid
	@GetMapping("/oper-log")
	public String operLog() {
		return "oper-log";
	}

}
