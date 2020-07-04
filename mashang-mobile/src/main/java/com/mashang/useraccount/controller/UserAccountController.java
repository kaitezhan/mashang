package com.mashang.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.common.vo.Result;
import com.mashang.useraccount.param.Level1MemberQueryCondParam;
import com.mashang.useraccount.param.ModifyLoginPwdParam;
import com.mashang.useraccount.param.ModifyMoneyPwdParam;
import com.mashang.useraccount.param.UserAccountRegisterParam;
import com.mashang.useraccount.service.UserAccountService;
import com.mashang.useraccount.vo.UserAccountDetails;
import com.mashang.useraccount.vo.UserAccountInfoVO;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@GetMapping("/findLevel1MemberByPage")
	@ResponseBody
	public Result findLevel1MemberByPage(Level1MemberQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(userAccountService.findLevel1MemberByPage(param));
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result getGoogleAuthInfo() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(userAccountService.getGoogleAuthInfo(user.getUserAccountId()));
	}

	@PostMapping("/unBindGoogleAuth")
	@ResponseBody
	public Result unBindGoogleAuth(String googleVerCode) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.unBindGoogleAuth(user.getUserAccountId(), googleVerCode);
		return Result.success();
	}

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result bindGoogleAuth(String googleSecretKey, String googleVerCode) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.bindGoogleAuth(user.getUserAccountId(), googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@PostMapping("/updateReceiveOrderState")
	@ResponseBody
	public Result updateReceiveOrderState(String receiveOrderState) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.updateReceiveOrderStateWithMember(user.getUserAccountId(), receiveOrderState);
		return Result.success();
	}

	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(ModifyLoginPwdParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		userAccountService.modifyLoginPwd(param);
		return Result.success();
	}

	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(ModifyMoneyPwdParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		userAccountService.modifyMoneyPwd(param);
		return Result.success();
	}

	@PostMapping("/register")
	@ResponseBody
	public Result register(UserAccountRegisterParam param) {
		userAccountService.register(param);
		return Result.success();
	}

	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		return Result.success().setData(userAccountInfo);
	}

	@GetMapping("/getMemberAccountInfo")
	@ResponseBody
	public Result getMemberAccountInfo() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(userAccountService.getMemberAccountInfo(user.getUserAccountId()));
	}

}
