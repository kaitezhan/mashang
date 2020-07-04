package com.mashang.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mashang.common.utils.SpringUtils;
import com.mashang.useraccount.service.LoginLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateLastAccessTimeFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getServletPath();
		String[] exclusionsUrls = { ".js", ".gif", ".jpg", ".png", ".css", ".ico" };
		for (String str : exclusionsUrls) {
			if (path.contains(str)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		if (path.startsWith("/api")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			LoginLogService loginLogService = SpringUtils.getBean(LoginLogService.class);
			loginLogService.updateLastAccessTimeWithRedis(RequestContextHolder.currentRequestAttributes().getSessionId());
		} catch (Exception e) {
			log.warn("更新最后访问时间异常", e);
		}
		filterChain.doFilter(request, response);
	}

}
