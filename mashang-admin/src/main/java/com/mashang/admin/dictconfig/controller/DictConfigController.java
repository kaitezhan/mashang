package com.mashang.admin.dictconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.dictconfig.DictHolder;
import com.mashang.dictconfig.param.AddOrUpdateDictTypeParam;
import com.mashang.dictconfig.param.DictTypeQueryCondParam;
import com.mashang.dictconfig.param.UpdateDictDataParam;
import com.mashang.dictconfig.service.DictService;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {

	@Autowired
	private DictService dictService;

	@OperLog(system = "后台管理", module = "字典配置项", operate = "更新字典项")
	@PostMapping("/updateDictData")
	@ResponseBody
	public Result updateDictData(@RequestBody UpdateDictDataParam param) {
		dictService.updateDictData(param);
		return Result.success();
	}

	@GetMapping("/findDictItemByDictTypeId")
	@ResponseBody
	public Result findDictItemByDictTypeId(String dictTypeId) {
		return Result.success().setData(dictService.findDictItemByDictTypeId(dictTypeId));
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "删除字典类型")
	@GetMapping("/delDictTypeById")
	@ResponseBody
	public Result delDictTypeById(String id) {
		dictService.delDictTypeById(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "添加或修改字典类型")
	@PostMapping("/addOrUpdateDictType")
	@ResponseBody
	public Result addOrUpdateDictType(@RequestBody AddOrUpdateDictTypeParam param) {
		dictService.addOrUpdateDictType(param);
		return Result.success();
	}

	@GetMapping("/findDictTypeById")
	@ResponseBody
	public Result findDictTypeById(String id) {
		return Result.success().setData(dictService.findDictTypeById(id));
	}

	@GetMapping("/findDictTypeByPage")
	@ResponseBody
	public Result findDictTypeByPage(DictTypeQueryCondParam param) {
		return Result.success().setData(dictService.findDictTypeByPage(param));
	}

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result findDictItemInCache(String dictTypeCode) {
		return Result.success().setData(DictHolder.findDictItem(dictTypeCode));
	}

}
