package com.mashang.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mashang.statisticalanalysis.domain.CashDepositBounty;

public interface CashDepositBountyRepo
		extends JpaRepository<CashDepositBounty, String>, JpaSpecificationExecutor<CashDepositBounty> {
	
	CashDepositBounty findTopBy();

}
