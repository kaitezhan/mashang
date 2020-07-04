package com.mashang.config.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Autowired
	private AuthenticationFailHandler failHandler;

	@Autowired
	private LogoutHandler logoutHandler;
	
	@Autowired
	private RememberMeUserDetailsService rememberMeUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
		.authenticationEntryPoint(customAuthenticationEntryPoint)
		.and()
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.authorizeRequests()
		.antMatchers("/pay-test").permitAll()
		.antMatchers("/alipay-id-fixed-code").permitAll()
		.antMatchers("/alipay-id-fixed-code-inner").permitAll()
		.antMatchers("/xqd-inner").permitAll()
		.antMatchers("/pc-cashier").permitAll()
		.antMatchers("/cashier").permitAll()
		.antMatchers("/captcha").permitAll()
		.antMatchers("/pay").permitAll()
		.antMatchers("/api/**").permitAll()
		.antMatchers("/paySuccessNotice").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/masterControl/**").permitAll()
		.antMatchers("/userAccount/register").permitAll()
		.antMatchers("/userAccount/getUserAccountInfo").permitAll()
		.antMatchers("/storage/fetch/**").permitAll()
		.antMatchers("/gatheringChannel/findAllGatheringChannel").permitAll()
		.anyRequest().hasAuthority("MOBILE")
		.and().formLogin().loginPage("/login").loginProcessingUrl("/login")
		.successHandler(successHandler).failureHandler(failHandler).permitAll()
		.and().rememberMe()
		.rememberMeParameter("rememberMe")
		.tokenValiditySeconds(3600 * 24 * 7)
		.userDetailsService(rememberMeUserDetailsService)
		.and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler).permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/plugins/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(customAuthenticationProvider);
	}
	
	@Bean
	public FilterRegistrationBean<Filter> updateLastAccessTimeFilter() {
		FilterRegistrationBean<Filter> updateLastAccessTimeFilter = new FilterRegistrationBean<Filter>(
				new UpdateLastAccessTimeFilter());
		updateLastAccessTimeFilter.setOrder(1);
		updateLastAccessTimeFilter.setEnabled(true);
		updateLastAccessTimeFilter.addUrlPatterns("/*");
		return updateLastAccessTimeFilter;
	}

}
