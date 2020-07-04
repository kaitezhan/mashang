package com.mashang.statisticalanalysis.vo;

import org.springframework.beans.BeanUtils;

import com.mashang.statisticalanalysis.domain.CashDepositBounty;

import lombok.Data;

@Data
public class CashDepositBountyVO {

	private Double totalCashDeposit;

	private Double totalBounty;

	private Double monthBounty;

	private Double yesterdayBounty;

	private Double todayBounty;

	public static CashDepositBountyVO convertFor(CashDepositBounty cashDepositBounty) {
		if (cashDepositBounty == null) {
			return null;
		}
		CashDepositBountyVO vo = new CashDepositBountyVO();
		BeanUtils.copyProperties(cashDepositBounty, vo);
		return vo;
	}

}
