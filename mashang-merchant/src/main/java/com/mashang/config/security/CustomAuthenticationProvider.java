package com.mashang.config.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mashang.common.auth.GoogleAuthenticator;
import com.mashang.constants.Constant;
import com.mashang.mastercontrol.service.MasterControlService;
import com.mashang.mastercontrol.vo.SystemSettingVO;
import com.mashang.merchant.service.MerchantService;
import com.mashang.merchant.vo.LoginMerchantInfoVO;
import com.mashang.useraccount.service.LoginLogService;
import com.mashang.useraccount.vo.MerchantAccountDetails;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private LoginLogService loginLogService;
	
	@Autowired
	private MasterControlService masterControlService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();
		LoginMerchantInfoVO loginMerchantInfo = merchantService.getLoginMerchantInfo(username);
		if (loginMerchantInfo == null) {
			loginLogService.recordLoginLog(null, username,
					Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_用户名不存在, HttpUtil.getClientIP(request),
					UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException("用户名或密码不正确");
		}
		if (!new BCryptPasswordEncoder().matches(token.getCredentials().toString(), loginMerchantInfo.getLoginPwd())) {
			loginLogService.recordLoginLog(null,
					loginMerchantInfo.getUserName(), Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_用户名或密码不正确,
					HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_用户名或密码不正确);
		}
		if (Constant.账号状态_禁用.equals(loginMerchantInfo.getState())) {
			loginLogService.recordLoginLog(null,
					loginMerchantInfo.getUserName(), Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_账号已被禁用,
					HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_账号已被禁用);
		}
		SystemSettingVO systemSetting = masterControlService.getSystemSetting();
		if (systemSetting.getMerchantLoginGoogleAuth()) {
			if (StrUtil.isNotBlank(loginMerchantInfo.getGoogleSecretKey())) {
				String googleVerCode = request.getParameter("googleVerCode");
				if (StrUtil.isBlank(googleVerCode) || !GoogleAuthenticator
						.checkCode(loginMerchantInfo.getGoogleSecretKey(), googleVerCode, System.currentTimeMillis())) {
					loginLogService.recordLoginLog(null, username, Constant.系统_商户端, Constant.登录状态_失败,
							Constant.登录提示_谷歌验证码不正确, HttpUtil.getClientIP(request),
							UserAgentUtil.parse(request.getHeader("User-Agent")));
					throw new AuthenticationServiceException(Constant.登录提示_谷歌验证码不正确);
				}
			}
		}
		MerchantAccountDetails merchantAccountDetails = new MerchantAccountDetails(loginMerchantInfo);
		return new UsernamePasswordAuthenticationToken(merchantAccountDetails, merchantAccountDetails.getPassword(),
				Arrays.asList(new SimpleGrantedAuthority("MERCHANT")));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
