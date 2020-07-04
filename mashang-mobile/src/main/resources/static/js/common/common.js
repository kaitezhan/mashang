Vue.http.interceptors.push(function(request) {
	return function(response) {
		if (response.body.code == 999) {
			this.$notify({
				type : 'danger',
				message : response.body.msg,
				onClose : function() {
					window.location.href = '/login';
				}
			});
		} else if (response.body.code != 200) {
			response.ok = false;
			this.$notify({
				type : 'danger',
				message : response.body.msg
			});
		}
	};
});

var GLOBAL = {
	systemSetting : {
		currencyUnit : ''
	},
	pathname : '',
	receiveOrderState : '',
	autoReceiveOrderFlag : sessionStorage.getItem('autoReceiveOrderFlag') == null ? false : sessionStorage.getItem('autoReceiveOrderFlag') == 'true',
	dispatchMode : false,
	receiveOrderSuccessToAuditPage : false,
	receiveOrderOrDispatchOrderInterval : null
};
initGlobal();
//receiveOrderOrDispatchOrder();

function initGlobal() {
	var systemSetting = sessionStorage.getItem('systemSetting');
	if (systemSetting != null) {
		GLOBAL.systemSetting = JSON.parse(systemSetting);
	} else {
		loadSystemSetting();
	}
}

function loadSystemSetting() {
	var that = this;
	Vue.http.get('/masterControl/getSystemSetting').then(function(res) {
		GLOBAL.systemSetting = res.body.data;
		sessionStorage.setItem('systemSetting', JSON.stringify(GLOBAL.systemSetting));
	});
}

function receiveOrderOrDispatchOrder() {
	var pathname = window.location.pathname;
	if (pathname == '/receive-order') {
		return;
	}
	GLOBAL.pathname = pathname;
	var that = this;
	Vue.http.get('/userAccount/getUserAccountInfo').then(function(res) {
		if (res.body.data == null) {
			return;
		}
		GLOBAL.receiveOrderState = res.body.data.receiveOrderState;
		if (GLOBAL.receiveOrderState != '1') {
			return;
		}
		Vue.http.get('/masterControl/getReceiveOrderSetting').then(function(res) {
			GLOBAL.dispatchMode = res.body.data.dispatchMode;
			GLOBAL.receiveOrderSuccessToAuditPage = res.body.data.receiveOrderSuccessToAuditPage;
			GLOBAL.noInReceiveOrderPageStopReceiveOrder = res.body.data.noInReceiveOrderPageStopReceiveOrder;
			if (GLOBAL.noInReceiveOrderPageStopReceiveOrder) {
				updateReceiveOrderStateToStop();
				return;
			}
			receiveOrderOrDispatchOrderInner();
		});
	});
}

function updateReceiveOrderStateToStop() {
	var that = this;
	Vue.http.post('/userAccount/updateReceiveOrderState', {
		receiveOrderState : '2'
	}, {
		emulateJSON : true
	}).then(function(res) {
	});
}

function receiveOrderOrDispatchOrderInner() {
	if (GLOBAL.dispatchMode) {
		dispatchOrderTip();
		GLOBAL.receiveOrderOrDispatchOrderInterval = window.setInterval(function() {
			checkStopReceiveOrderState();
			dispatchOrderTip();
		}, 5000);
	} else {
		if (!GLOBAL.autoReceiveOrderFlag) {
			return;
		}
		loadMerchantOrder();
		GLOBAL.receiveOrderOrDispatchOrderInterval = window.setInterval(function() {
			checkStopReceiveOrderState();
			loadMerchantOrder();
		}, 6000);
	}
}

function checkStopReceiveOrderState() {
	var that = this;
	Vue.http.get('/userAccount/getUserAccountInfo').then(function(res) {
		GLOBAL.receiveOrderState = res.body.data.receiveOrderState;
		if (GLOBAL.receiveOrderState == '2') {
			window.clearInterval(GLOBAL.receiveOrderOrDispatchOrderInterval);
			GLOBAL.receiveOrderOrDispatchOrderInterval = null;
		}
	});
}

function loadMerchantOrder() {
	var that = this;
	Vue.http.get('/merchantOrder/findMyWaitReceivingOrder').then(function(res) {
		GLOBAL.waitReceivingOrders = res.body.data;
		if (GLOBAL.waitReceivingOrders == null || GLOBAL.waitReceivingOrders.length == 0) {
			return;
		}
		var randomOrder = GLOBAL.waitReceivingOrders[Math.floor(Math.random() * GLOBAL.waitReceivingOrders.length + 0)];
		receiveOrder(randomOrder.id);
	});
}

function receiveOrder(orderId) {
	Vue.http.get('/merchantOrder/receiveOrder', {
		params : {
			orderId : orderId
		}
	}).then(function(res) {
		layer.alert('接单成功,请及时审核', {
			icon : 1,
			time : 2000,
			shade : false,
			end : function() {
				if (GLOBAL.receiveOrderSuccessToAuditPage && GLOBAL.pathname != '/audit-order') {
					window.location.href = '/audit-order';
				}
			}
		});
	});
}

function dispatchOrderTip() {
	Vue.http.get('/merchantOrder/dispatchOrderTip').then(function(res) {
		var tip = res.body.data;
		if (tip == null) {
			return;
		}
		dispatchOrderTipMarkRead();
		layer.alert('派单成功,请及时审核', {
			icon : 1,
			time : 2000,
			shade : false,
			end : function() {
				if (GLOBAL.receiveOrderSuccessToAuditPage  && GLOBAL.pathname != '/audit-order') {
					window.location.href = '/audit-order';
				}
			}
		});
	});
}

function dispatchOrderTipMarkRead() {
	var that = this;
	Vue.http.get('/merchantOrder/dispatchOrderTipMarkRead').then(function(res) {
	});
}

/**
 * 由于js存在精度丢失的问题,需要对其进行四舍五入处理
 * 
 * @param num
 * @param digit
 *            小数位数, 不填则默认4为小数
 * @returns
 */
function numberFormat(num, digit) {
	if (digit == null) {
		digit = 4;
	}
	return parseFloat(Number(num).toFixed(digit));
}

/**
 * 获取url参数
 * 
 * @param name
 * @returns
 */
function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
