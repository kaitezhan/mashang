package com.mashang.admin.dataclean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.dataclean.param.DataCleanParam;
import com.mashang.dataclean.service.DataCleanService;

@Controller
@RequestMapping("/dataClean")
public class DataCleanController {

	@Autowired
	private DataCleanService dataCleanService;

	@OperLog(system = "后台管理", module = "数据清理", operate = "数据清理")
	@PostMapping("/dataClean")
	@ResponseBody
	public Result clean(@RequestBody DataCleanParam param) {
		dataCleanService.dataClean(param);
		return Result.success();
	}

}
