package com.mashang.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mashang.statisticalanalysis.domain.TopAgentGatheringSituation;

public interface TopAgentGatheringSituationRepo extends JpaRepository<TopAgentGatheringSituation, String>,
		JpaSpecificationExecutor<TopAgentGatheringSituation> {

}
