package com.mashang.admin.mastercontrol.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.mastercontrol.param.UpdateCustomerServiceSettingParam;
import com.mashang.mastercontrol.param.UpdateMerchantSettlementSettingParam;
import com.mashang.mastercontrol.param.UpdateReceiveOrderRiskSettingParam;
import com.mashang.mastercontrol.param.UpdateReceiveOrderSettingParam;
import com.mashang.mastercontrol.param.UpdateRechargeSettingParam;
import com.mashang.mastercontrol.param.UpdateRegisterSettingParam;
import com.mashang.mastercontrol.param.UpdateSystemSettingParam;
import com.mashang.mastercontrol.param.UpdateWithdrawSettingParam;
import com.mashang.mastercontrol.service.MasterControlService;

@Controller
@RequestMapping("/masterControl")
public class MasterControlController {

	@Autowired
	private MasterControlService service;
	
	@GetMapping("/getSystemSetting")
	@ResponseBody
	public Result getSystemSetting() {
		return Result.success().setData(service.getSystemSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新系统设置")
	@PostMapping("/updateSystemSetting")
	@ResponseBody
	public Result updateSystemSetting(UpdateSystemSettingParam param) {
		service.updateSystemSetting(param);
		return Result.success();
	}

	@GetMapping("/getRegisterSetting")
	@ResponseBody
	public Result getRegisterSetting() {
		return Result.success().setData(service.getRegisterSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新注册设置")
	@PostMapping("/updateRegisterSetting")
	@ResponseBody
	public Result updateRegisterSetting(UpdateRegisterSettingParam param) {
		service.updateRegisterSetting(param);
		return Result.success();
	}

	@GetMapping("/getCustomerServiceSetting")
	@ResponseBody
	public Result getCustomerServiceSetting() {
		return Result.success().setData(service.getCustomerServiceSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新客服设置")
	@PostMapping("/updateCustomerServiceSetting")
	@ResponseBody
	public Result updateCustomerServiceSetting(UpdateCustomerServiceSettingParam param) {
		service.updateCustomerServiceSetting(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "刷新缓存")
	@PostMapping("/refreshCache")
	@ResponseBody
	public Result refreshCache(@RequestBody List<String> cacheItems) {
		service.refreshCache(cacheItems);
		return Result.success();
	}

}
