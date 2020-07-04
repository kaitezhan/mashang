package com.mashang.dataclean.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mashang.common.valid.ParamValid;
import com.mashang.dataclean.param.DataCleanParam;
import com.mashang.useraccount.repo.LoginLogRepo;
import com.mashang.useraccount.repo.OperLogRepo;

import cn.hutool.core.date.DateUtil;

@Service
public class DataCleanService {

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Autowired
	private OperLogRepo operLogRepo;

	@ParamValid
	@Transactional
	public void dataClean(DataCleanParam param) {
		List<String> dataTypes = param.getDataTypes();
		Date startTime = DateUtil.beginOfDay(param.getStartTime());
		Date endTime = DateUtil.endOfDay(param.getEndTime());
		if (dataTypes.contains("merchantOrder")) {
		}
		if (dataTypes.contains("merchantSettlementRecord")) {
		}
		if (dataTypes.contains("loginLog")) {
			loginLogRepo.deleteByLoginTimeGreaterThanEqualAndLoginTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("operLog")) {
			operLogRepo.deleteByOperTimeGreaterThanEqualAndOperTimeLessThanEqual(startTime, endTime);
		}
	}

}
