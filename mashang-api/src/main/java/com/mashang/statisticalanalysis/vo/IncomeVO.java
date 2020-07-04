package com.mashang.statisticalanalysis.vo;

import org.springframework.beans.BeanUtils;

import com.mashang.statisticalanalysis.domain.PlatformIncome;

import lombok.Data;

@Data
public class IncomeVO {

	private Double totalIncome;

	private Double monthIncome;;

	private Double yesterdayIncome;

	private Double todayIncome;
	
	public static IncomeVO convertForPlatform(PlatformIncome income) {
		if (income == null) {
			return null;
		}
		IncomeVO vo = new IncomeVO();
		BeanUtils.copyProperties(income, vo);
		return vo;
	}

}
