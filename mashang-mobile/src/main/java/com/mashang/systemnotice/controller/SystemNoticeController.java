package com.mashang.systemnotice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.vo.Result;
import com.mashang.systemnotice.service.SystemNoticeService;
import com.mashang.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/systemNotice")
public class SystemNoticeController {

	@Autowired
	private SystemNoticeService systemNoticeService;

	@GetMapping("/findTop10PublishedNotice")
	@ResponseBody
	public Result findTop10PublishedNotice() {
		return Result.success().setData(systemNoticeService.findTop10PublishedNotice());
	}

	@GetMapping("/getLatestNotice")
	@ResponseBody
	public Result getLatestNotice() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(systemNoticeService.getLatestNotice(user.getUserAccountId()));
	}

	@GetMapping("/markRead")
	@ResponseBody
	public Result markRead(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		systemNoticeService.markRead(id, user.getUserAccountId());
		return Result.success();
	}

}
