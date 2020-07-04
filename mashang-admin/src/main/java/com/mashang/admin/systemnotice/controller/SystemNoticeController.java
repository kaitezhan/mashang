package com.mashang.admin.systemnotice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.systemnotice.param.AddOrUpdateSystemNoticeParam;
import com.mashang.systemnotice.param.SystemNoticeQueryCondParam;
import com.mashang.systemnotice.service.SystemNoticeService;

@Controller
@RequestMapping("/systemNotice")
public class SystemNoticeController {

	@Autowired
	private SystemNoticeService systemNoticeService;

	@GetMapping("/findSystemNoticeById")
	@ResponseBody
	public Result findSystemNoticeById(String id) {
		return Result.success().setData(systemNoticeService.findSystemNoticeById(id));
	}

	@OperLog(system = "后台管理", module = "系统公告", operate = "删除公告")
	@GetMapping("/delSystemNoticeById")
	@ResponseBody
	public Result delSystemNoticeById(String id) {
		systemNoticeService.delSystemNoticeById(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "系统公告", operate = "添加或修改公告")
	@PostMapping("/addOrUpdateSystemNotice")
	@ResponseBody
	public Result addOrUpdateSystemNotice(AddOrUpdateSystemNoticeParam param) {
		systemNoticeService.addOrUpdateSystemNotice(param);
		return Result.success();
	}

	@GetMapping("/findSystemNoticeByPage")
	@ResponseBody
	public Result findSystemNoticeByPage(SystemNoticeQueryCondParam param) {
		return Result.success().setData(systemNoticeService.findSystemNoticeByPage(param));
	}

}
