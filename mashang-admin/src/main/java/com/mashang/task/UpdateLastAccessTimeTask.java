package com.mashang.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mashang.useraccount.service.LoginLogService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateLastAccessTimeTask {

	@Autowired
	private LoginLogService loginLogService;

	@Scheduled(fixedRate = 60000)
	public void execute() {
		try {
			log.info("更新最后访问时间定时任务start");
			loginLogService.updateLastAccessTime();
			log.info("更新最后访问时间定时任务end");
		} catch (Exception e) {
			log.error("更新最后访问时间定时任务", e);
		}
	}

}
