package com.mashang.statisticalanalysis.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alicp.jetcache.anno.Cached;
import com.mashang.statisticalanalysis.domain.AdjustCashDepositSituation;
import com.mashang.statisticalanalysis.domain.CashDepositBounty;
import com.mashang.statisticalanalysis.domain.RechargeSituation;
import com.mashang.statisticalanalysis.domain.TodayAccountReceiveOrderSituation;
import com.mashang.statisticalanalysis.domain.TopAgentGatheringSituation;
import com.mashang.statisticalanalysis.domain.TotalAccountReceiveOrderSituation;
import com.mashang.statisticalanalysis.domain.TradeSituation;
import com.mashang.statisticalanalysis.domain.WithdrawSituation;
import com.mashang.statisticalanalysis.repo.AdjustCashDepositSituationRepo;
import com.mashang.statisticalanalysis.repo.CashDepositBountyRepo;
import com.mashang.statisticalanalysis.repo.ChannelUseSituationRepo;
import com.mashang.statisticalanalysis.repo.PlatformIncomeRepo;
import com.mashang.statisticalanalysis.repo.RechargeSituationRepo;
import com.mashang.statisticalanalysis.repo.TodayAccountReceiveOrderSituationRepo;
import com.mashang.statisticalanalysis.repo.TopAgentGatheringSituationRepo;
import com.mashang.statisticalanalysis.repo.TotalAccountReceiveOrderSituationRepo;
import com.mashang.statisticalanalysis.repo.TradeSituationRepo;
import com.mashang.statisticalanalysis.repo.WithdrawSituationRepo;
import com.mashang.statisticalanalysis.vo.AccountReceiveOrderSituationVO;
import com.mashang.statisticalanalysis.vo.AdjustCashDepositSituationVO;
import com.mashang.statisticalanalysis.vo.BountyRankVO;
import com.mashang.statisticalanalysis.vo.CashDepositBountyVO;
import com.mashang.statisticalanalysis.vo.ChannelUseSituationVO;
import com.mashang.statisticalanalysis.vo.IncomeVO;
import com.mashang.statisticalanalysis.vo.RechargeSituationVO;
import com.mashang.statisticalanalysis.vo.TopAgentGatheringSituationVO;
import com.mashang.statisticalanalysis.vo.TradeSituationVO;
import com.mashang.statisticalanalysis.vo.WithdrawSituationVO;

@Validated
@Service
public class StatisticalAnalysisService {

	@Autowired
	private TotalAccountReceiveOrderSituationRepo totalAccountReceiveOrderSituationRepo;

	@Autowired
	private TodayAccountReceiveOrderSituationRepo todayAccountReceiveOrderSituationRepo;

	@Autowired
	private CashDepositBountyRepo cashDepositBountyRepo;

	@Autowired
	private TradeSituationRepo tradeSituationRepo;

	@Autowired
	private WithdrawSituationRepo withdrawSituationRepo;

	@Autowired
	private RechargeSituationRepo rechargeSituationRepo;

	@Autowired
	private AdjustCashDepositSituationRepo adjustCashDepositSituationRepo;

	@Autowired
	private TopAgentGatheringSituationRepo topAgentGatheringSituationRepo;
	
	@Autowired
	private ChannelUseSituationRepo channelUseSituationRepo;
	
	@Autowired
	private PlatformIncomeRepo platformIncomeRepo;
	
	@Transactional(readOnly = true)
	public IncomeVO findPlatformIncome() {
		return IncomeVO.convertForPlatform(platformIncomeRepo.findTopBy());
	}
	
	@Transactional(readOnly = true)
	public List<ChannelUseSituationVO> findChannelUseSituation() {
		return ChannelUseSituationVO.convertFor(channelUseSituationRepo.findAll());
	}

	@Transactional(readOnly = true)
	public List<TopAgentGatheringSituationVO> findTopAgentGatheringSituation() {
		List<TopAgentGatheringSituation> situations = topAgentGatheringSituationRepo.findAll();
		return TopAgentGatheringSituationVO.convertFor(situations);
	}

	@Transactional(readOnly = true)
	public AdjustCashDepositSituationVO findAdjustCashDepositSituation() {
		AdjustCashDepositSituation situation = adjustCashDepositSituationRepo.findTopBy();
		return AdjustCashDepositSituationVO.convertFor(situation);
	}

	@Transactional(readOnly = true)
	public WithdrawSituationVO findWithdrawSituation() {
		WithdrawSituation situation = withdrawSituationRepo.findTopBy();
		return WithdrawSituationVO.convertFor(situation);
	}

	@Transactional(readOnly = true)
	public RechargeSituationVO findRechargeSituation() {
		RechargeSituation situation = rechargeSituationRepo.findTopBy();
		return RechargeSituationVO.convertFor(situation);
	}

	@Transactional(readOnly = true)
	public AccountReceiveOrderSituationVO findMyTodayReceiveOrderSituation(@NotBlank String userAccountId) {
		return AccountReceiveOrderSituationVO
				.convertForToday(todayAccountReceiveOrderSituationRepo.findByReceivedAccountId(userAccountId));
	}

	@Transactional(readOnly = true)
	public AccountReceiveOrderSituationVO findMyTotalReceiveOrderSituation(@NotBlank String userAccountId) {
		return AccountReceiveOrderSituationVO
				.convertForTotal(totalAccountReceiveOrderSituationRepo.findByReceivedAccountId(userAccountId));
	}

	@Cached(name = "totalTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTotalTop10BountyRank() {
		List<TotalAccountReceiveOrderSituation> receiveOrderSituations = totalAccountReceiveOrderSituationRepo
				.findTop10ByOrderByBountyDesc();
		return BountyRankVO.convertFor(receiveOrderSituations);
	}

	@Cached(name = "todayTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTodayTop10BountyRank() {
		List<TodayAccountReceiveOrderSituation> todayReceiveOrderSituations = todayAccountReceiveOrderSituationRepo
				.findTop10ByOrderByBountyDesc();
		return BountyRankVO.convertForToday(todayReceiveOrderSituations);
	}

	@Transactional(readOnly = true)
	public CashDepositBountyVO findCashDepositBounty() {
		CashDepositBounty cashDepositBounty = cashDepositBountyRepo.findTopBy();
		return CashDepositBountyVO.convertFor(cashDepositBounty);
	}

	@Transactional(readOnly = true)
	public TradeSituationVO findTradeSituation() {
		TradeSituation tradeSituation = tradeSituationRepo.findTopBy();
		return TradeSituationVO.convertFor(tradeSituation);
	}

}
