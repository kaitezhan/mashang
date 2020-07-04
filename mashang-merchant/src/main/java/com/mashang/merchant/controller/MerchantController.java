package com.mashang.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.common.vo.Result;
import com.mashang.merchant.param.AddMerchantParam;
import com.mashang.merchant.param.AddOrUpdateMerchantBankCardParam;
import com.mashang.merchant.param.MerchantEditParam;
import com.mashang.merchant.param.MerchantQueryCondParam;
import com.mashang.merchant.param.ModifyLoginPwdParam;
import com.mashang.merchant.param.ModifyMoneyPwdParam;
import com.mashang.merchant.service.MerchantService;
import com.mashang.merchant.vo.MerchantBankCardVO;
import com.mashang.merchant.vo.MerchantVO;
import com.mashang.useraccount.vo.MerchantAccountDetails;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;
	
	@PostMapping("/modifyLowerLevelLoginPwd")
	@ResponseBody
	public Result modifyLowerLevelLoginPwd(String id, String newLoginPwd) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.modifyLowerLevelLoginPwd(id, newLoginPwd, user.getMerchantId());
		return Result.success();
	}

	@PostMapping("/modifyLowerLevelMoneyPwd")
	@ResponseBody
	public Result modifyLowerLevelMoneyPwd(String id, String newMoneyPwd) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.modifyLowerLevelMoneyPwd(id, newMoneyPwd, user.getMerchantId());
		return Result.success();
	}
	
	@GetMapping("/generateSecretKey")
	@ResponseBody
	public Result generateSecretKey() {
		return Result.success().setData(SecureUtil.md5(UUID.fastUUID().toString()));
	}

	@GetMapping("/delLowerLevelMerchantById")
	@ResponseBody
	public Result delLowerLevelMerchantById(String id) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.delLowerLevelMerchantById(id, user.getMerchantId());
		return Result.success();
	}
	
	@PostMapping("/addLowerLevelMerchant")
	@ResponseBody
	public Result addLowerLevelMerchant(AddMerchantParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.addLowerLevelMerchant(param, user.getMerchantId());
		return Result.success();
	}

	@PostMapping("/updateLowerLevelMerchant")
	@ResponseBody
	public Result updateLowerLevelMerchant(MerchantEditParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.updateLowerLevelMerchant(param, user.getMerchantId());
		return Result.success();
	}
	
	@GetMapping("/findLowerLevelMerchantById")
	@ResponseBody
	public Result findLowerLevelMerchantById(String id) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(merchantService.findLowerLevelMerchantById(id, user.getMerchantId()));
	}

	@GetMapping("/findMerchantByPageWithInviter")
	@ResponseBody
	public Result findMerchantByPageWithInviter(MerchantQueryCondParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setInviterId(user.getMerchantId());
		return Result.success().setData(merchantService.findMerchantByPageWithInviter(param));
	}

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result bindGoogleAuth(String googleSecretKey, String googleVerCode) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.bindGoogleAuth(user.getMerchantId(), googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result getGoogleAuthInfo() {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(merchantService.getGoogleAuthInfo(user.getMerchantId()));
	}

	@GetMapping("/downloadInterfaceDemo")
	public ResponseEntity<Resource> downloadInterfaceDemo() {
		Resource file = new ClassPathResource("Demo.java");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}

	@GetMapping("/downloadInterfaceDoc")
	public ResponseEntity<Resource> downloadInterfaceDoc() {
		Resource file = new ClassPathResource("interfaceDoc.doc");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}
	
	@GetMapping("/getMerchantInfo")
	@ResponseBody
	public Result getMerchantInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		MerchantAccountDetails user = (MerchantAccountDetails) principal;
		MerchantVO merchantInfo = merchantService.getMerchantInfo(user.getMerchantId());
		return Result.success().setData(merchantInfo);
	}

	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(ModifyLoginPwdParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		merchantService.modifyLoginPwd(param);
		return Result.success();
	}

	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(ModifyMoneyPwdParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		merchantService.modifyMoneyPwd(param);
		return Result.success();
	}

	@GetMapping("/findMerchantBankCardById")
	@ResponseBody
	public Result findMerchantBankCardById(String id) {
		MerchantBankCardVO merchantBankCard = merchantService.findMerchantBankCardById(id);
		return Result.success().setData(merchantBankCard);
	}

	@GetMapping("/findMerchantBankCardByMerchantId")
	@ResponseBody
	public Result findMerchantBankCardByMerchantId(String merchantId) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(merchantService.findMerchantBankCardByMerchantId(user.getMerchantId()));
	}

	@PostMapping("/addOrUpdateMerchantBankCard")
	@ResponseBody
	public Result addOrUpdateMerchantBankCard(AddOrUpdateMerchantBankCardParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.addOrUpdateMerchantBankCard(param, user.getMerchantId());
		return Result.success();
	}

	@GetMapping("/deleteMerchantBankCard")
	@ResponseBody
	public Result deleteMerchantBankCard(String merchantBankCardId) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.deleteMerchantBankCard(merchantBankCardId, user.getMerchantId());
		return Result.success();
	}

}
