package com.mashang.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;
import com.mashang.common.vo.Result;
import com.mashang.constants.Constant;
import com.mashang.useraccount.service.LoginLogService;
import com.mashang.useraccount.service.UserAccountService;
import com.mashang.useraccount.vo.UserAccountDetails;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;

/**
 * 登录成功处理类
 * 
 * @author zohar
 * @date 2019年1月23日
 *
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		loginLogService.recordLoginLog(RequestContextHolder.currentRequestAttributes().getSessionId(),
				user.getUsername(), Constant.系统_后台管理, Constant.登录状态_成功, Constant.登录提示_登录成功,
				HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
		userAccountService.updateLatelyLoginTime(user.getUserAccountId());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.success().setMsg(Constant.登录提示_登录成功)));
		out.flush();
		out.close();
	}
}
