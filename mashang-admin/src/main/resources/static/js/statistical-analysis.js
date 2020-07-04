var totalBountyRankChart = null;
var todayBountyRankChart = null;
var statisticalAnalysisVM = new Vue({
	el : '#statistical-analysis',
	data : {
		global : GLOBAL,
		platformIncome : {},
		cashDepositBounty : {},
		tradeSituation : {},
		rechargeSituation : {},
		withdrawSituation : {},
		adjustCashDepositSituation : {},
		merchantTradeSituations : [],
		channelTradeSituations : [],
		topAgentGatheringSituations : [],
		creditMemberGatheringSituations : [],
		queryScope : 'today',
		topAgentQueryScope : 'today',
		creditMemberQueryScope : 'today',
		merchantStartTime : dayjs().format('YYYY-MM-DD'),
		merchantEndTime : dayjs().format('YYYY-MM-DD'),
		channelStartTime : dayjs().format('YYYY-MM-DD'),
		channelEndTime : dayjs().format('YYYY-MM-DD'),
		channelUseSituations : []
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadPlatformIncome();
		that.loadCashDepositBounty();
		that.loadTradeSituation();
		that.loadMerchantTradeSituation();
		that.loadChannelTradeSituation();
		that.loadRechargeSituation();
		that.loadWithdrawSituation();
		that.loadAdjustCashDepositSituation();
		that.loadChannelUseSituation();
	},
	methods : {

		setMerchantQueryCond : function(queryScope) {
			var merchantStartTime = 'all';
			var merchantEndTime = 'all';
			if (queryScope == 'all') {
				merchantStartTime = null;
				merchantEndTime = null;
			} else if (queryScope == 'month') {
				merchantStartTime = dayjs().startOf('month').format('YYYY-MM-DD');
				merchantEndTime = dayjs().endOf('month').format('YYYY-MM-DD');
			} else if (queryScope == 'yesterday') {
				merchantStartTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
				merchantEndTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
			} else if (queryScope == 'today') {
				merchantStartTime = dayjs().format('YYYY-MM-DD');
				merchantEndTime = dayjs().format('YYYY-MM-DD');
			}
			this.merchantStartTime = merchantStartTime;
			this.merchantEndTime = merchantEndTime;
			this.loadMerchantTradeSituation();
		},

		setChannelQueryCond : function(queryScope) {
			var channelStartTime = 'all';
			var channelEndTime = 'all';
			if (queryScope == 'all') {
				channelStartTime = null;
				channelEndTime = null;
			} else if (queryScope == 'month') {
				channelStartTime = dayjs().startOf('month').format('YYYY-MM-DD');
				channelEndTime = dayjs().endOf('month').format('YYYY-MM-DD');
			} else if (queryScope == 'yesterday') {
				channelStartTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
				channelEndTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
			} else if (queryScope == 'today') {
				channelStartTime = dayjs().format('YYYY-MM-DD');
				channelEndTime = dayjs().format('YYYY-MM-DD');
			}
			this.channelStartTime = channelStartTime;
			this.channelEndTime = channelEndTime;
			this.loadChannelTradeSituation();
		},
		
		loadPlatformIncome : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findPlatformIncome').then(function(res) {
				that.platformIncome = res.body.data;
			});
		},

		loadTopAgentGatheringSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTopAgentGatheringSituation').then(function(res) {
				that.topAgentGatheringSituations = res.body.data;
			});
		},

		loadCreditMemberGatheringSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findCreditMemberGatheringSituation').then(function(res) {
				that.creditMemberGatheringSituations = res.body.data;
			});
		},

		loadRechargeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findRechargeSituation').then(function(res) {
				that.rechargeSituation = res.body.data;
			});
		},

		loadWithdrawSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findWithdrawSituation').then(function(res) {
				that.withdrawSituation = res.body.data;
			});
		},

		loadAdjustCashDepositSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findAdjustCashDepositSituation').then(function(res) {
				that.adjustCashDepositSituation = res.body.data;
			});
		},

		toMerchantOrderPageWithMerchant : function(merchantId, orderState) {
			window.location.href = 'merchant-order?merchantId=' + merchantId + '&orderState=' + orderState + '&submitStartTime=' + this.merchantStartTime + '&submitEndTime=' + this.merchantEndTime;
		},

		toMerchantOrderPageWithChannel : function(channelId, orderState) {
			window.location.href = 'merchant-order?channelId=' + channelId + '&orderState=' + orderState + '&submitStartTime=' + this.channelStartTime + '&submitEndTime=' + this.channelEndTime;
		},

		loadMerchantTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMerchantTradeSituation', {
				params : {
					startTime : that.merchantStartTime,
					endTime : that.merchantEndTime
				}
			}).then(function(res) {
				that.merchantTradeSituations = res.body.data;
			});
		},

		loadChannelTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findChannelTradeSituation', {
				params : {
					startTime : that.channelStartTime,
					endTime : that.channelEndTime
				}
			}).then(function(res) {
				that.channelTradeSituations = res.body.data;
			});
		},

		loadChannelUseSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findChannelUseSituation').then(function(res) {
				that.channelUseSituations = res.body.data;
			});
		},

		loadCashDepositBounty : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findCashDepositBounty').then(function(res) {
				that.cashDepositBounty = res.body.data;
			});
		},

		loadTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTradeSituation').then(function(res) {
				that.tradeSituation = res.body.data;
			});
		},

		initTotalBountyRankChart : function() {
			var that = this;
			option = {
				title : {
					text : '累计奖励金排行榜前十'
				},
				color : [ '#26dad0' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				grid : {
					show : true,
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true,
					backgroundColor : '#2c3448'
				},
				xAxis : {
					type : 'category',
					data : [],
					axisTick : {
						alignWithLabel : true
					}
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					name : '奖励金',
					type : 'bar',
					barWidth : '60%',
					data : []
				} ]
			};
			totalBountyRankChart = echarts.init(document.getElementById('total-bounty-rank-chart'));
			totalBountyRankChart.setOption(option);
		},

		initTodayBountyRankChart : function() {
			var that = this;
			option = {
				title : {
					text : '今日奖励金排行榜前十'
				},
				color : [ '#26dad0' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				grid : {
					show : true,
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true,
					backgroundColor : '#2c3448'
				},
				xAxis : {
					type : 'category',
					data : [],
					axisTick : {
						alignWithLabel : true
					}
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					name : '奖励金',
					type : 'bar',
					barWidth : '60%',
					data : []
				} ]
			};
			todayBountyRankChart = echarts.init(document.getElementById('today-bounty-rank-chart'));
			todayBountyRankChart.setOption(option);
		},

		loadTotalTop10BountyRankData : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTotalTop10BountyRank').then(function(res) {
				var xAxisDatas = [];
				var seriesDatas = [];
				var top10BountyRanks = res.body.data;
				for (var i = 0; i < top10BountyRanks.length; i++) {
					xAxisDatas.push(top10BountyRanks[i].userName);
					seriesDatas.push(top10BountyRanks[i].bounty);
				}
				totalBountyRankChart.setOption({
					xAxis : {
						data : xAxisDatas
					},
					series : [ {
						data : seriesDatas
					} ]
				});
			});
		},

		loadTodayTop10BountyRankData : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTodayTop10BountyRank').then(function(res) {
				var xAxisDatas = [];
				var seriesDatas = [];
				var top10BountyRanks = res.body.data;
				for (var i = 0; i < top10BountyRanks.length; i++) {
					xAxisDatas.push(top10BountyRanks[i].userName);
					seriesDatas.push(top10BountyRanks[i].bounty);
				}
				todayBountyRankChart.setOption({
					xAxis : {
						data : xAxisDatas
					},
					series : [ {
						data : seriesDatas
					} ]
				});
			});
		}
	}
});