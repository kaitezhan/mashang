package com.mashang.task.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mashang.useraccount.service.LoginLogService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateReceiveOrderStateToStopTask {

	@Autowired
	private LoginLogService loginLogService;

	@Scheduled(fixedRate = 20000)
	public void execute() {
		try {
			log.info("定时更新状态为停止接单start");
			log.info("定时更新状态为停止接单end");
		} catch (Exception e) {
			log.error("定时更新状态为停止接单", e);
		}
	}

}
