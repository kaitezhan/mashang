package com.mashang.admin.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.merchant.param.AddMerchantParam;
import com.mashang.merchant.param.AdjustWithdrawableAmountParam;
import com.mashang.merchant.param.MerchantEditParam;
import com.mashang.merchant.param.MerchantQueryCondParam;
import com.mashang.merchant.service.MerchantService;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;
	
	
	@OperLog(system = "后台管理", module = "商户管理", operate = "调整可提现金额")
	@PostMapping("/adjustWithdrawableAmount")
	@ResponseBody
	public Result adjustWithdrawableAmount(AdjustWithdrawableAmountParam param) {
		merchantService.adjustWithdrawableAmount(param);
		return Result.success();
	}

	@GetMapping("/findAllMerchant")
	@ResponseBody
	public Result findAllMerchant() {
		return Result.success().setData(merchantService.findAllMerchant());
	}

	@GetMapping("/findAllMerchantAgent")
	@ResponseBody
	public Result findAllMerchantAgent() {
		return Result.success().setData(merchantService.findAllMerchantAgent());
	}
	
	@OperLog(system = "后台管理", module = "商户管理", operate = "修改登陆密码")
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String id, String newLoginPwd) {
		merchantService.modifyLoginPwd(id, newLoginPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "修改资金密码")
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String id, String newMoneyPwd) {
		merchantService.modifyMoneyPwd(id, newMoneyPwd);
		return Result.success();
	}

	@GetMapping("/generateSecretKey")
	@ResponseBody
	public Result generateSecretKey() {
		return Result.success().setData(SecureUtil.md5(UUID.fastUUID().toString()));
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "添加商户")
	@PostMapping("/addMerchant")
	@ResponseBody
	public Result addMerchant(AddMerchantParam param) {
		merchantService.addMerchant(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "修改商户信息")
	@PostMapping("/updateMerchant")
	@ResponseBody
	public Result updateMerchant(MerchantEditParam param) {
		merchantService.updateMerchant(param);
		return Result.success();
	}

	@GetMapping("/findMerchantById")
	@ResponseBody
	public Result findMerchantById(String id) {
		return Result.success().setData(merchantService.findMerchantById(id));
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "删除商户")
	@GetMapping("/delMerchantById")
	@ResponseBody
	public Result delMerchantById(String id) {
		merchantService.delMerchantById(id);
		return Result.success();
	}

	@GetMapping("/findMerchantByPage")
	@ResponseBody
	public Result findPlatformOrderByPage(MerchantQueryCondParam param) {
		return Result.success().setData(merchantService.findMerchantByPage(param));
	}

}
