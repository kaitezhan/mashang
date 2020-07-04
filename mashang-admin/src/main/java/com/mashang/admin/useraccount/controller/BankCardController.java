package com.mashang.admin.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.vo.Result;
import com.mashang.useraccount.service.BankCardService;

@Controller
@RequestMapping("/bankCard")
public class BankCardController {

	@Autowired
	private BankCardService bankCardService;

	@GetMapping("/findBankCardByUserAccountId")
	@ResponseBody
	public Result findBankCardByUserAccountId(String userAccountId) {
		return Result.success().setData(bankCardService.findBankCard(userAccountId));
	}

}
