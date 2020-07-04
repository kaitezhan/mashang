package com.mashang.admin.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.useraccount.param.LoginLogQueryCondParam;
import com.mashang.useraccount.service.LoginLogService;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController {

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/findLoginLogByPage")
	@ResponseBody
	public Result findLoginLogByPage(LoginLogQueryCondParam param) {
		return Result.success().setData(loginLogService.findLoginLogByPage(param));
	}

	@GetMapping("/findOnlineAccountByPage")
	@ResponseBody
	public Result findOnlineAccountByPage(LoginLogQueryCondParam param) {
		return Result.success().setData(loginLogService.findOnlineAccountByPage(param));
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "强制退出登陆")
	@GetMapping("/logout")
	@ResponseBody
	public Result logout(String sessionId) {
		loginLogService.logout(sessionId);
		return Result.success();
	}

}
