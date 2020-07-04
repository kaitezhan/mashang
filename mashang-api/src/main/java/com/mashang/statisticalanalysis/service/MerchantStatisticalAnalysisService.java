package com.mashang.statisticalanalysis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashang.common.valid.ParamValid;
import com.mashang.statisticalanalysis.domain.merchant.MerchantChannelTradeSituation;
import com.mashang.statisticalanalysis.domain.merchant.MerchantEverydayStatistical;
import com.mashang.statisticalanalysis.domain.merchant.MerchantTradeSituation;
import com.mashang.statisticalanalysis.param.MerchantIndexQueryParam;
import com.mashang.statisticalanalysis.repo.merchant.MerchantChannelTradeSituationRepo;
import com.mashang.statisticalanalysis.repo.merchant.MerchantEverydayStatisticalRepo;
import com.mashang.statisticalanalysis.repo.merchant.MerchantTradeSituationRepo;
import com.mashang.statisticalanalysis.vo.IndexStatisticalVO;
import com.mashang.statisticalanalysis.vo.MerchantChannelTradeSituationVO;
import com.mashang.statisticalanalysis.vo.MerchantTradeSituationVO;

import cn.hutool.core.date.DateUtil;

@Validated
@Service
public class MerchantStatisticalAnalysisService {

	@Autowired
	private MerchantTradeSituationRepo merchantTradeSituationRepo;

	@Autowired
	private MerchantEverydayStatisticalRepo everydayStatisticalRepo;

	@Autowired
	private MerchantChannelTradeSituationRepo merchantChannelTradeSituationRepo;

	@Transactional(readOnly = true)
	public List<MerchantChannelTradeSituationVO> findMerchantChannelTradeSituationByMerchantId(String merchantId) {
		List<MerchantChannelTradeSituation> tradeSituations = merchantChannelTradeSituationRepo
				.findByMerchantId(merchantId);
		return MerchantChannelTradeSituationVO.convertFor(tradeSituations);
	}

	@Transactional(readOnly = true)
	public MerchantTradeSituationVO findMerchantTradeSituationById(String merchantId) {
		return MerchantTradeSituationVO.convertFor(merchantTradeSituationRepo.getOne(merchantId));
	}

	@Transactional(readOnly = true)
	public List<MerchantTradeSituationVO> findMerchantTradeSituation() {
		List<MerchantTradeSituation> merchantTradeSituations = merchantTradeSituationRepo.findAll();
		return MerchantTradeSituationVO.convertFor(merchantTradeSituations);
	}

	@ParamValid
	@Transactional(readOnly = true)
	public List<IndexStatisticalVO> findEverydayStatistical(MerchantIndexQueryParam param) {
		List<MerchantEverydayStatistical> statisticals = everydayStatisticalRepo
				.findByMerchantIdAndEverydayGreaterThanEqualAndEverydayLessThanEqualOrderByEveryday(
						param.getMerchantId(), DateUtil.beginOfDay(param.getStartTime()),
						DateUtil.beginOfDay(param.getEndTime()));
		return IndexStatisticalVO.convertForEvery(statisticals);
	}

}
