package com.mashang.mastercontrol.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashang.common.valid.ParamValid;
import com.mashang.mastercontrol.domain.CustomerServiceSetting;
import com.mashang.mastercontrol.domain.RegisterSetting;
import com.mashang.mastercontrol.domain.SystemSetting;
import com.mashang.mastercontrol.param.UpdateCustomerServiceSettingParam;
import com.mashang.mastercontrol.param.UpdateRegisterSettingParam;
import com.mashang.mastercontrol.param.UpdateSystemSettingParam;
import com.mashang.mastercontrol.repo.CustomerServiceSettingRepo;
import com.mashang.mastercontrol.repo.RegisterSettingRepo;
import com.mashang.mastercontrol.repo.SystemSettingRepo;
import com.mashang.mastercontrol.vo.CustomerServiceSettingVO;
import com.mashang.mastercontrol.vo.RegisterSettingVO;
import com.mashang.mastercontrol.vo.SystemSettingVO;

import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@Slf4j
public class MasterControlService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	
	@Autowired
	private RegisterSettingRepo registerSettingRepo;

	@Autowired
	private CustomerServiceSettingRepo customerServiceSettingRepo;

	@Autowired
	private SystemSettingRepo systemSettingRepo;
	
	
	@Transactional(readOnly = true)
	public SystemSettingVO getSystemSetting() {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		return SystemSettingVO.convertFor(setting);
	}

	@ParamValid
	@Transactional
	public void updateSystemSetting(UpdateSystemSettingParam param) {
		SystemSetting setting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = SystemSetting.build();
		}
		BeanUtils.copyProperties(param, setting);
		setting.setLatelyUpdateTime(new Date());
		systemSettingRepo.save(setting);
	}

	@Transactional(readOnly = true)
	public RegisterSettingVO getRegisterSetting() {
		RegisterSetting setting = registerSettingRepo.findTopByOrderByLatelyUpdateTime();
		return RegisterSettingVO.convertFor(setting);
	}

	@ParamValid
	@Transactional
	public void updateRegisterSetting(UpdateRegisterSettingParam param) {
		RegisterSetting setting = registerSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = RegisterSetting.build();
		}
		BeanUtils.copyProperties(param, setting);
		setting.setLatelyUpdateTime(new Date());
		registerSettingRepo.save(setting);
	}

	@Transactional(readOnly = true)
	public CustomerServiceSettingVO getCustomerServiceSetting() {
		CustomerServiceSetting setting = customerServiceSettingRepo.findTopByOrderByLatelyUpdateTime();
		return CustomerServiceSettingVO.convertFor(setting);
	}

	@Transactional
	public void updateCustomerServiceSetting(UpdateCustomerServiceSettingParam param) {
		CustomerServiceSetting setting = customerServiceSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null) {
			setting = CustomerServiceSetting.build();
		}
		BeanUtils.copyProperties(param, setting);
		setting.setLatelyUpdateTime(new Date());
		customerServiceSettingRepo.save(setting);
	}

	public void refreshCache(@NotEmpty List<String> cacheItems) {
		List<String> deleteSuccessKeys = new ArrayList<>();
		List<String> deleteFailKeys = new ArrayList<>();
		for (String cacheItem : cacheItems) {
			Set<String> keys = redisTemplate.keys(cacheItem);
			for (String key : keys) {
				Boolean flag = redisTemplate.delete(key);
				if (flag) {
					deleteSuccessKeys.add(key);
				} else {
					deleteFailKeys.add(key);
				}
			}
		}
		if (!deleteFailKeys.isEmpty()) {
			log.warn("以下的缓存删除失败:", deleteFailKeys);
		}
	}

}
