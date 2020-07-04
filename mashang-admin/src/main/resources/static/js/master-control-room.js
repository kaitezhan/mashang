var masterControlRoomVM = new Vue({
	el : '#master-control-room',
	data : {

		currentPage : '',

		rechargeExplainEditor : null,
		withdrawExplainEditor : null,
		customerServiceExplainEditor : null,

		/**
		 * 系统设置start
		 */
		websiteTitle : '',
		homePageUrl : '',
		appUrl : '',
		localStoragePath : '',
		currencyUnit : '',
		memberClientSingleDeviceLogin : false,
		merchantLoginGoogleAuth : false,
		backgroundLoginGoogleAuth : false,
		memberClientLoginGoogleAuth : false,

		/**
		 * 注册设置start
		 */
		registerFun : false,
		inviteRegisterMode : false,

		/**
		 * 接单设置start
		 */
		stopStartAndReceiveOrder : false,
		receiveOrderEffectiveDuration : '',
		orderPayEffectiveDuration : '',
		cashDepositMinimumRequire : '',
		cashPledge : '',
		freezeEffectiveDuration : '',
		unconfirmedAutoFreezeDuration : '',

		/**
		 * 接单风控
		 */

		auditGatheringCode : false,
		banReceiveRepeatOrder : false,
		waitConfirmOrderUpperLimit : '',
		noOpsStopReceiveOrder : '',
		gatheringCodeReceiveOrderInterval : '',
		gatheringCodeEverydayUsedUpperLimit : false,
		gatheringCodeUsedUpperLimit : '',
		gatheringCodeEverydayGatheringUpperLimit : false,
		gatheringCodeGatheringUpperLimit : '',
		floatAmountMode : false,
		floatAmountDirection : 'up',
		minFloatAmount : '',
		maxFloatAmount : '',
		continuationGatheringFailOffLine : '',

		/**
		 * 充值start
		 */
		rechargeOrderEffectiveDuration : '',
		rechargeLowerLimit : '',
		rechargeUpperLimit : '',
		quickInputAmount : '',
		rechargeExplain : '',
		cantContinuousSubmit : false,

		/**
		 * 提现start
		 */
		everydayWithdrawTimesUpperLimit : '',
		withdrawLowerLimit : '',
		withdrawUpperLimit : '',
		withdrawExplain : '',

		merchantSettlementRate : '',
		minServiceFee : '',

		/**
		 * 客服设置start
		 */
		customerServiceExplain : '',

		/**
		 * 刷新缓存start
		 */
		refreshConfigItem : true,
		refreshDict : true
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadSystemSetting();
		$('.qrcode-pic').on('filebatchuploadsuccess', function(event, data) {
			that.qrcodeStorageId = data.response.data.join(',');
			that.uploadQrcodeFlag = false;
		});
	},
	methods : {

		loadSystemSetting : function() {
			var that = this;
			that.currentPage = 'systemSetting';
			that.$http.get('/masterControl/getSystemSetting').then(function(res) {
				if (res.body.data != null) {
					that.websiteTitle = res.body.data.websiteTitle;
					that.homePageUrl = res.body.data.homePageUrl;
					that.appUrl = res.body.data.appUrl;
					that.localStoragePath = res.body.data.localStoragePath;
					that.currencyUnit = res.body.data.currencyUnit;
					that.memberClientSingleDeviceLogin = res.body.data.memberClientSingleDeviceLogin;
					that.merchantLoginGoogleAuth = res.body.data.merchantLoginGoogleAuth;
					that.backgroundLoginGoogleAuth = res.body.data.backgroundLoginGoogleAuth;
					that.memberClientLoginGoogleAuth = res.body.data.memberClientLoginGoogleAuth;
				}
			});
		},

		updateSystemSetting : function() {
			var that = this;
			var websiteTitle = that.websiteTitle;
			if (websiteTitle === null || websiteTitle === '') {
				layer.alert('请输入网站标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var homePageUrl = that.homePageUrl;
			if (homePageUrl === null || homePageUrl === '') {
				layer.alert('请输入首页地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var appUrl = that.appUrl;
			var monitorAppUrl = that.monitorAppUrl;
			var localStoragePath = that.localStoragePath;
			if (localStoragePath === null || localStoragePath === '') {
				layer.alert('请输入本地存储对象路径', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var currencyUnit = that.currencyUnit;
			if (currencyUnit === null || currencyUnit === '') {
				layer.alert('请输入货币单位', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var memberClientSingleDeviceLogin = that.memberClientSingleDeviceLogin;
			if (memberClientSingleDeviceLogin === null) {
				layer.alert('请设置是否限制单一设备登录', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var merchantLoginGoogleAuth = that.merchantLoginGoogleAuth;
			if (merchantLoginGoogleAuth === null) {
				layer.alert('请设置商户端是否登陆启用谷歌身份验证', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var backgroundLoginGoogleAuth = that.backgroundLoginGoogleAuth;
			if (backgroundLoginGoogleAuth === null) {
				layer.alert('请设置后台管理是否登陆启用谷歌身份验证', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var memberClientLoginGoogleAuth = that.memberClientLoginGoogleAuth;
			if (memberClientLoginGoogleAuth === null) {
				layer.alert('请设置会员端是否登陆启用谷歌身份验证', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateSystemSetting', {
				websiteTitle : websiteTitle,
				homePageUrl : homePageUrl,
				appUrl : appUrl,
				localStoragePath : localStoragePath,
				currencyUnit : currencyUnit,
				memberClientSingleDeviceLogin : memberClientSingleDeviceLogin,
				merchantLoginGoogleAuth : merchantLoginGoogleAuth,
				backgroundLoginGoogleAuth : backgroundLoginGoogleAuth,
				memberClientLoginGoogleAuth : memberClientLoginGoogleAuth
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadSystemSetting();
			});
		},

		loadRegisterSetting : function() {
			var that = this;
			that.currentPage = 'registerSetting';
			that.$http.get('/masterControl/getRegisterSetting').then(function(res) {
				if (res.body.data != null) {
					that.registerFun = res.body.data.registerFun;
					that.inviteRegisterMode = res.body.data.inviteRegisterMode;
				}
			});
		},

		updateRegisterSetting : function() {
			var that = this;
			var registerFun = that.registerFun;
			if (registerFun === null) {
				layer.alert('请设置是否开放注册功能', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var inviteRegisterMode = that.inviteRegisterMode;
			if (inviteRegisterMode === null) {
				layer.alert('请设置是否启用邀请注册模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateRegisterSetting', {
				registerFun : registerFun,
				inviteRegisterMode : inviteRegisterMode
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRegisterSetting();
			});
		},

		loadReceiveOrderSetting : function() {
			var that = this;
			that.currentPage = 'receiveOrderSetting';
			that.$http.get('/masterControl/getReceiveOrderSetting').then(function(res) {
				if (res.body.data != null) {
					that.stopStartAndReceiveOrder = res.body.data.stopStartAndReceiveOrder;
					that.receiveOrderEffectiveDuration = res.body.data.receiveOrderEffectiveDuration;
					that.orderPayEffectiveDuration = res.body.data.orderPayEffectiveDuration;
					that.cashDepositMinimumRequire = res.body.data.cashDepositMinimumRequire;
					that.cashPledge = res.body.data.cashPledge;
					that.freezeEffectiveDuration = res.body.data.freezeEffectiveDuration;
					that.unconfirmedAutoFreezeDuration = res.body.data.unconfirmedAutoFreezeDuration;
				}
			});
		},

		updateReceiveOrderSetting : function() {
			var that = this;
			var stopStartAndReceiveOrder = that.stopStartAndReceiveOrder;
			if (stopStartAndReceiveOrder === null) {
				layer.alert('请选择是否暂停发单接单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var receiveOrderEffectiveDuration = that.receiveOrderEffectiveDuration;
			if (receiveOrderEffectiveDuration === null || receiveOrderEffectiveDuration === '') {
				layer.alert('请输入接单有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var orderPayEffectiveDuration = that.orderPayEffectiveDuration;
			if (orderPayEffectiveDuration === null || orderPayEffectiveDuration === '') {
				layer.alert('请输入支付有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cashDepositMinimumRequire = that.cashDepositMinimumRequire;
			if (cashDepositMinimumRequire === null || cashDepositMinimumRequire === '') {
				layer.alert('请输入余额最低要求', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cashPledge = that.cashPledge;
			if (cashPledge === null || cashPledge === '') {
				layer.alert('请输入押金', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var freezeEffectiveDuration = that.freezeEffectiveDuration;
			if (freezeEffectiveDuration === null || freezeEffectiveDuration === '') {
				layer.alert('请输入冻结时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var unconfirmedAutoFreezeDuration = that.unconfirmedAutoFreezeDuration;
			if (unconfirmedAutoFreezeDuration === null || unconfirmedAutoFreezeDuration === '') {
				layer.alert('请输入超过多少分钟未确认冻结该订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateReceiveOrderSetting', {
				stopStartAndReceiveOrder : stopStartAndReceiveOrder,
				receiveOrderEffectiveDuration : receiveOrderEffectiveDuration,
				orderPayEffectiveDuration : orderPayEffectiveDuration,
				cashDepositMinimumRequire : cashDepositMinimumRequire,
				cashPledge : cashPledge,
				freezeEffectiveDuration : freezeEffectiveDuration,
				unconfirmedAutoFreezeDuration : unconfirmedAutoFreezeDuration,
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadReceiveOrderSetting();
			});
		},

		loadReceiveOrderRiskSetting : function() {
			var that = this;
			that.currentPage = 'receiveOrderRiskSetting';
			that.$http.get('/masterControl/getReceiveOrderRiskSetting').then(function(res) {
				if (res.body.data != null) {
					that.auditGatheringCode = res.body.data.auditGatheringCode;
					that.banReceiveRepeatOrder = res.body.data.banReceiveRepeatOrder;
					that.waitConfirmOrderUpperLimit = res.body.data.waitConfirmOrderUpperLimit;
					that.noOpsStopReceiveOrder = res.body.data.noOpsStopReceiveOrder;
					that.gatheringCodeReceiveOrderInterval = res.body.data.gatheringCodeReceiveOrderInterval;
					that.gatheringCodeEverydayUsedUpperLimit = res.body.data.gatheringCodeEverydayUsedUpperLimit;
					that.gatheringCodeUsedUpperLimit = res.body.data.gatheringCodeUsedUpperLimit;
					that.gatheringCodeEverydayGatheringUpperLimit = res.body.data.gatheringCodeEverydayGatheringUpperLimit;
					that.gatheringCodeGatheringUpperLimit = res.body.data.gatheringCodeGatheringUpperLimit;
					that.floatAmountMode = res.body.data.floatAmountMode;
					that.floatAmountDirection = res.body.data.floatAmountDirection;
					that.minFloatAmount = res.body.data.minFloatAmount;
					that.maxFloatAmount = res.body.data.maxFloatAmount;
					that.continuationGatheringFailOffLine = res.body.data.continuationGatheringFailOffLine;
				}
			});
		},

		updateReceiveOrderRiskSetting : function() {
			var that = this;
			var auditGatheringCode = that.auditGatheringCode;
			if (auditGatheringCode === null) {
				layer.alert('请设置是否审核收款码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var banReceiveRepeatOrder = that.banReceiveRepeatOrder;
			if (banReceiveRepeatOrder === null) {
				layer.alert('请选择是否禁止接相同金额的订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var noOpsStopReceiveOrder = that.noOpsStopReceiveOrder;
			if (noOpsStopReceiveOrder === null || noOpsStopReceiveOrder === '') {
				layer.alert('请输入超过多少分钟无操作停止接单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var waitConfirmOrderUpperLimit = that.waitConfirmOrderUpperLimit;
			if (waitConfirmOrderUpperLimit === null || waitConfirmOrderUpperLimit === '') {
				layer.alert('请输入待审核订单数量上限', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeReceiveOrderInterval = that.gatheringCodeReceiveOrderInterval;
			if (gatheringCodeReceiveOrderInterval === null || gatheringCodeReceiveOrderInterval === '') {
				layer.alert('请输入单个收款方式接单间隔', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeEverydayUsedUpperLimit = that.gatheringCodeEverydayUsedUpperLimit;
			if (gatheringCodeEverydayUsedUpperLimit === null) {
				layer.alert('请设置是否限制收款码每日收款次数', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeUsedUpperLimit = that.gatheringCodeUsedUpperLimit;
			if (gatheringCodeUsedUpperLimit === null || gatheringCodeUsedUpperLimit === '') {
				layer.alert('请输入收款码收款次数上限数量', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeEverydayGatheringUpperLimit = that.gatheringCodeEverydayGatheringUpperLimit;
			if (gatheringCodeEverydayGatheringUpperLimit === null) {
				layer.alert('请设置是否限制收款码每日收款金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var gatheringCodeGatheringUpperLimit = that.gatheringCodeGatheringUpperLimit;
			if (gatheringCodeGatheringUpperLimit === null || gatheringCodeGatheringUpperLimit === '') {
				layer.alert('请输入收款码收款金额上限', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var floatAmountMode = that.floatAmountMode;
			if (floatAmountMode === null) {
				layer.alert('请设置是否启用浮动金额模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var floatAmountDirection = that.floatAmountDirection;
			if (floatAmountDirection === null) {
				layer.alert('请选择浮动方向', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var minFloatAmount = that.minFloatAmount;
			if (minFloatAmount === null || minFloatAmount === '') {
				layer.alert('请输入浮动最低金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var maxFloatAmount = that.maxFloatAmount;
			if (maxFloatAmount === null || maxFloatAmount === '') {
				layer.alert('请输入浮动最高金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var continuationGatheringFailOffLine = that.continuationGatheringFailOffLine;
			if (continuationGatheringFailOffLine === null || continuationGatheringFailOffLine === '') {
				layer.alert('请输入连续收款失败次数', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateReceiveOrderRiskSetting', {
				auditGatheringCode : auditGatheringCode,
				banReceiveRepeatOrder : banReceiveRepeatOrder,
				waitConfirmOrderUpperLimit : waitConfirmOrderUpperLimit,
				noOpsStopReceiveOrder : noOpsStopReceiveOrder,
				gatheringCodeReceiveOrderInterval : gatheringCodeReceiveOrderInterval,
				gatheringCodeEverydayUsedUpperLimit : gatheringCodeEverydayUsedUpperLimit,
				gatheringCodeUsedUpperLimit : gatheringCodeUsedUpperLimit,
				gatheringCodeEverydayGatheringUpperLimit : gatheringCodeEverydayGatheringUpperLimit,
				gatheringCodeGatheringUpperLimit : gatheringCodeGatheringUpperLimit,
				floatAmountMode : floatAmountMode,
				floatAmountDirection : floatAmountDirection,
				minFloatAmount : minFloatAmount,
				maxFloatAmount : maxFloatAmount,
				continuationGatheringFailOffLine : continuationGatheringFailOffLine
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadReceiveOrderRiskSetting();
			});
		},

		loadRechargeSetting : function() {
			var that = this;
			that.currentPage = 'rechargeSetting';
			that.$http.get('/masterControl/getRechargeSetting').then(function(res) {
				if (res.body.data != null) {
					that.rechargeOrderEffectiveDuration = res.body.data.orderEffectiveDuration;
					that.rechargeLowerLimit = res.body.data.rechargeLowerLimit;
					that.rechargeUpperLimit = res.body.data.rechargeUpperLimit;
					that.quickInputAmount = res.body.data.quickInputAmount;
					that.rechargeExplain = res.body.data.rechargeExplain;
					that.cantContinuousSubmit = res.body.data.cantContinuousSubmit;
				}
				that.initRechargeExplainEditor();
			});
		},

		initRechargeExplainEditor : function() {
			var that = this;
			that.$nextTick(function() {
				that.rechargeExplainEditor = new window.wangEditor('#rechargeExplainEditor');
				that.rechargeExplainEditor.customConfig.menus = [ 'head', // 标题
				'bold', // 粗体
				'fontSize', // 字号
				'fontName', // 字体
				'italic', // 斜体
				'underline', // 下划线
				'strikeThrough', // 删除线
				'foreColor', // 文字颜色
				'backColor', // 背景颜色
				'link', // 插入链接
				'list', // 列表
				'justify', // 对齐方式
				'quote', // 引用
				'emoticon', // 表情
				'table', // 表格
				'code', // 插入代码
				'undo', // 撤销
				'redo' // 重复
				];
				that.rechargeExplainEditor.create();
				that.rechargeExplainEditor.txt.html(that.rechargeExplain);
			});
		},

		updateRechargeSetting : function() {
			var that = this;
			var rechargeOrderEffectiveDuration = that.rechargeOrderEffectiveDuration;
			if (rechargeOrderEffectiveDuration === null || rechargeOrderEffectiveDuration === '') {
				layer.alert('请输入充值订单有效时长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeLowerLimit = that.rechargeLowerLimit;
			if (rechargeLowerLimit === null || rechargeLowerLimit === '') {
				layer.alert('请输入充值最低限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var rechargeUpperLimit = that.rechargeUpperLimit;
			if (rechargeUpperLimit === null || rechargeUpperLimit === '') {
				layer.alert('请输入充值最高限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var quickInputAmount = that.quickInputAmount;
			if (quickInputAmount === null || quickInputAmount === '') {
				layer.alert('请输入快速设置金额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.rechargeExplain = that.rechargeExplainEditor.txt.html();
			var rechargeExplain = that.rechargeExplain;
			if (rechargeExplain === null || rechargeExplain === '') {
				layer.alert('请输入充值说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var cantContinuousSubmit = that.cantContinuousSubmit;
			if (cantContinuousSubmit === null) {
				layer.alert('请选择是否限制不能连续提交充值订单', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateRechargeSetting', {
				orderEffectiveDuration : rechargeOrderEffectiveDuration,
				rechargeLowerLimit : rechargeLowerLimit,
				rechargeUpperLimit : rechargeUpperLimit,
				quickInputAmount : quickInputAmount,
				rechargeExplain : rechargeExplain,
				cantContinuousSubmit : cantContinuousSubmit
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadRechargeSetting();
			});
		},

		loadWithdrawSetting : function() {
			var that = this;
			that.currentPage = 'withdrawSetting';
			that.$http.get('/masterControl/getWithdrawSetting').then(function(res) {
				if (res.body.data != null) {
					that.everydayWithdrawTimesUpperLimit = res.body.data.everydayWithdrawTimesUpperLimit;
					that.withdrawLowerLimit = res.body.data.withdrawLowerLimit;
					that.withdrawUpperLimit = res.body.data.withdrawUpperLimit;
					that.withdrawExplain = res.body.data.withdrawExplain;
				}
				that.initCustomerServiceExplainEditor();
			});
		},

		initWithdrawExplainEditor : function() {
			var that = this;
			that.$nextTick(function() {
				that.withdrawExplainEditor = new window.wangEditor('#withdrawExplainEditor');
				that.withdrawExplainEditor.customConfig.menus = [ 'head', // 标题
				'bold', // 粗体
				'fontSize', // 字号
				'fontName', // 字体
				'italic', // 斜体
				'underline', // 下划线
				'strikeThrough', // 删除线
				'foreColor', // 文字颜色
				'backColor', // 背景颜色
				'link', // 插入链接
				'list', // 列表
				'justify', // 对齐方式
				'quote', // 引用
				'emoticon', // 表情
				'table', // 表格
				'code', // 插入代码
				'undo', // 撤销
				'redo' // 重复
				];
				that.withdrawExplainEditor.create();
				that.withdrawExplainEditor.txt.html(that.withdrawExplain);
			});
		},

		updateWithdrawSetting : function() {
			var that = this;
			var everydayWithdrawTimesUpperLimit = that.everydayWithdrawTimesUpperLimit;
			if (everydayWithdrawTimesUpperLimit === null || everydayWithdrawTimesUpperLimit === '') {
				layer.alert('请输入每日提现次数上限', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var withdrawLowerLimit = that.withdrawLowerLimit;
			if (withdrawLowerLimit === null || withdrawLowerLimit === '') {
				layer.alert('请输入提现最低限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var withdrawUpperLimit = that.withdrawUpperLimit;
			if (withdrawUpperLimit === null || withdrawUpperLimit === '') {
				layer.alert('请输入提现最高限额', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.withdrawExplain = that.withdrawExplainEditor.txt.html();
			var withdrawExplain = that.withdrawExplain;
			if (withdrawExplain === null || withdrawExplain === '') {
				layer.alert('请输入提现说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateWithdrawSetting', {
				everydayWithdrawTimesUpperLimit : everydayWithdrawTimesUpperLimit,
				withdrawLowerLimit : withdrawLowerLimit,
				withdrawUpperLimit : withdrawUpperLimit,
				withdrawExplain : withdrawExplain
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadWithdrawSetting();
			});
		},

		loadMerchantSettlementSetting : function() {
			var that = this;
			that.currentPage = 'merchantSettlementSetting';
			that.$http.get('/masterControl/getMerchantSettlementSetting').then(function(res) {
				if (res.body.data != null) {
					that.merchantSettlementRate = res.body.data.merchantSettlementRate;
					that.minServiceFee = res.body.data.minServiceFee;
				}
			});
		},

		updateMerchantSettlementSetting : function() {
			var that = this;
			var merchantSettlementRate = that.merchantSettlementRate;
			if (merchantSettlementRate === null || merchantSettlementRate === '') {
				layer.alert('请输入按多少比率收取服务费', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var minServiceFee = that.minServiceFee;
			if (minServiceFee === null || minServiceFee === '') {
				layer.alert('请输入单笔最低服务费', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/masterControl/updateMerchantSettlementSetting', {
				merchantSettlementRate : merchantSettlementRate,
				minServiceFee : minServiceFee
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadMerchantSettlementSetting();
			});
		},

		loadCustomerServiceSetting : function() {
			var that = this;
			that.currentPage = 'customerServiceSetting';
			that.$http.get('/masterControl/getCustomerServiceSetting').then(function(res) {
				if (res.body.data != null) {
					that.customerServiceExplain = res.body.data.customerServiceExplain;
				}
				that.initCustomerServiceExplainEditor();
			});
		},

		initCustomerServiceExplainEditor : function() {
			var that = this;
			that.$nextTick(function() {
				that.customerServiceExplainEditor = new window.wangEditor('#customerServiceExplainEditor');
				that.customerServiceExplainEditor.customConfig.menus = [ 'head', // 标题
				'bold', // 粗体
				'fontSize', // 字号
				'fontName', // 字体
				'italic', // 斜体
				'underline', // 下划线
				'strikeThrough', // 删除线
				'foreColor', // 文字颜色
				'backColor', // 背景颜色
				'link', // 插入链接
				'list', // 列表
				'justify', // 对齐方式
				'quote', // 引用
				'emoticon', // 表情
				'table', // 表格
				'code', // 插入代码
				'undo', // 撤销
				'redo' // 重复
				];
				that.customerServiceExplainEditor.create();
				that.customerServiceExplainEditor.txt.html(that.customerServiceExplain);
			});
		},

		updateCustomerServiceSetting : function() {
			var that = this;
			that.customerServiceExplain = that.customerServiceExplainEditor.txt.html();
			var customerServiceExplain = that.customerServiceExplain;
			if (customerServiceExplain === null || customerServiceExplain === '') {
				layer.alert('请输入客服说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/masterControl/updateCustomerServiceSetting', {
				customerServiceExplain : that.customerServiceExplain
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadCustomerServiceSetting();
			});
		},

		refreshCache : function() {
			var that = this;
			var cacheItems = [];
			cacheItems.push('dict*');
			cacheItems.push('findMenuTreeByUserAccountId_*');
			that.$http.post('/masterControl/refreshCache', cacheItems).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		}
	}
});