package com.mashang.admin.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashang.common.operlog.OperLog;
import com.mashang.common.vo.Result;
import com.mashang.useraccount.param.AssignMenuParam;
import com.mashang.useraccount.param.AssignRoleParam;
import com.mashang.useraccount.param.MenuParam;
import com.mashang.useraccount.param.RoleParam;
import com.mashang.useraccount.service.RbacService;
import com.mashang.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/rbac")
public class RbacController {

	@Autowired
	private RbacService rbacService;

	@GetMapping("/findMyMenuTree")
	@ResponseBody
	public Result findMyMenuTree() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(rbacService.findMenuTreeByUserAccountId(user.getUserAccountId()));
	}

	@PostMapping("/assignRole")
	@ResponseBody
	public Result assignRole(@RequestBody AssignRoleParam param) {
		rbacService.assignRole(param);
		return Result.success();
	}

	@GetMapping("/findRoleByUserAccountId")
	@ResponseBody
	public Result findRoleByUserAccountId(String userAccountId) {
		return Result.success().setData(rbacService.findRoleByUserAccountId(userAccountId));
	}

	@PostMapping("/assignMenu")
	@ResponseBody
	public Result assignMenu(@RequestBody AssignMenuParam param) {
		rbacService.assignMenu(param);
		return Result.success();
	}

	@GetMapping("/findMenuByRoleId")
	@ResponseBody
	public Result findMenuByRoleId(String roleId) {
		return Result.success().setData(rbacService.findMenuByRoleId(roleId));
	}

	@GetMapping("/findAllRole")
	@ResponseBody
	public Result findAllRole() {
		return Result.success().setData(rbacService.findAllRole());
	}

	@OperLog(system = "后台管理", module = "rbac", operate = "添加或修改角色")
	@PostMapping("/addOrUpdateRole")
	@ResponseBody
	public Result addOrUpdateRole(@RequestBody RoleParam param) {
		rbacService.addOrUpdateRole(param);
		return Result.success();
	}

	@GetMapping("/delRole")
	@ResponseBody
	public Result delRole(String id) {
		rbacService.delRole(id);
		return Result.success();
	}

	@GetMapping("/findRoleById")
	@ResponseBody
	public Result findRoleById(String id) {
		return Result.success().setData(rbacService.findRoleById(id));
	}

	@GetMapping("/findMenuById")
	@ResponseBody
	public Result findMenuById(String id) {
		return Result.success().setData(rbacService.findMenuById(id));
	}

	@GetMapping("/delMenu")
	@ResponseBody
	public Result delMenu(String id) {
		rbacService.delMenu(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "rbac", operate = "添加或修改菜单")
	@PostMapping("/addOrUpdateMenu")
	@ResponseBody
	public Result addOrUpdateMenu(@RequestBody MenuParam param) {
		rbacService.addOrUpdateMenu(param);
		return Result.success();
	}

	@GetMapping("/findMenuTree")
	@ResponseBody
	public Result findMenuTree() {
		return Result.success().setData(rbacService.findMenuTree());
	}

}
