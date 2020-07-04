package com.mashang.admin.useraccount.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.constants.Constant;
import com.mashang.useraccount.param.AddUserAccountParam;
import com.mashang.useraccount.param.UserAccountEditParam;
import com.mashang.useraccount.param.UserAccountQueryCondParam;
import com.mashang.useraccount.service.UserAccountService;
import com.mashang.useraccount.vo.UserAccountDetails;
import com.mashang.useraccount.vo.UserAccountInfoVO;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@OperLog(system = "后台管理", module = "账号管理", operate = "开放该账号接单")
	@GetMapping("/openReceiveOrder")
	@ResponseBody
	public Result updateReceiveOrderState(String userAccountId) {
		userAccountService.updateReceiveOrderStateInner(userAccountId, Constant.接单状态_停止接单);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "禁止该账号接单")
	@GetMapping("/disableReceiveOrder")
	@ResponseBody
	public Result disableReceiveOrder(String userAccountId) {
		userAccountService.updateReceiveOrderStateInner(userAccountId, Constant.接单状态_禁止接单);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "退出排队队列")
	@GetMapping("/updateReceiveOrderStateToStop")
	@ResponseBody
	public Result updateReceiveOrderStateToStop(String userAccountId) {
		userAccountService.updateReceiveOrderStateInner(userAccountId, Constant.接单状态_停止接单);
		return Result.success();
	}

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result bindGoogleAuth(String userAccountId, String googleSecretKey, String googleVerCode) {
		userAccountService.bindGoogleAuth(userAccountId, googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result getGoogleAuthInfo(String userAccountId) {
		return Result.success().setData(userAccountService.getGoogleAuthInfo(userAccountId));
	}

	@GetMapping("/findUserAccountDetailsInfoById")
	@ResponseBody
	public Result findUserAccountDetailsInfoById(String userAccountId) {
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoById(userAccountId));
	}

	@GetMapping("/findMemberByPage")
	@ResponseBody
	public Result findMemberByPage(UserAccountQueryCondParam param) {
		param.setAccountTypes(Arrays.asList(Constant.账号类型_会员));
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	@GetMapping("/findBackgroundAccountByPage")
	@ResponseBody
	public Result findBackgroundAccountByPage(UserAccountQueryCondParam param) {
		param.setAccountTypes(Arrays.asList(Constant.账号类型_管理员));
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改登陆密码")
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String userAccountId, String newLoginPwd) {
		userAccountService.modifyLoginPwd(userAccountId, newLoginPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改资金密码")
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String userAccountId, String newMoneyPwd) {
		userAccountService.modifyMoneyPwd(userAccountId, newMoneyPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改账号信息")
	@PostMapping("/updateUserAccount")
	@ResponseBody
	public Result updateUserAccount(UserAccountEditParam param) {
		userAccountService.updateUserAccount(param);
		return Result.success();
	}

	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		return Result.success().setData(userAccountInfo);
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "删除账号")
	@GetMapping("/delUserAccount")
	@ResponseBody
	public Result delUserAccount(String userAccountId) {
		userAccountService.delUserAccount(userAccountId);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "新增后台账号")
	@PostMapping("/addBackgroundAccount")
	@ResponseBody
	public Result addBackgroundAccount(AddUserAccountParam param) {
		param.setAccountType(Constant.账号类型_管理员);
		userAccountService.addUserAccount(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "新增会员账号")
	@PostMapping("/addMember")
	@ResponseBody
	public Result addMember(AddUserAccountParam param) {
		param.setAccountType(Constant.账号类型_会员);
		userAccountService.addUserAccount(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "调整保证金")
	@PostMapping("/adjustCashDeposit")
	@ResponseBody
	public Result adjustCashDeposit(String id, Double cashDeposit) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.adjustCashDeposit(id, cashDeposit, user.getUserAccountId());
		return Result.success();
	}

}
