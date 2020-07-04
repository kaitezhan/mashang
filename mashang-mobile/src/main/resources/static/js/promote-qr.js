var promoteQrVM = new Vue({
	el : '#promoteQr',
	data : {
		homePageUrl : '',
		qrLink : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.getHomePageUrl();
		new ClipboardJS('.copy-qr-link', {
			text : function(trigger) {
				return trigger.getAttribute('data-clipboard-text');
			}
		}).on('success', function(e) {
			that.$notify({
				type : 'success',
				message : '复制成功'
			});
		});
	},
	methods : {

		getHomePageUrl : function() {
			var that = this;
			that.$http.get('/masterControl/getSystemSetting').then(function(res) {
				that.homePageUrl = res.body.data.homePageUrl;
				that.getUserAccountInfo();
			});
		},

		getUserAccountInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getUserAccountInfo').then(function(res) {
				that.id = res.body.data.id;
				that.qrLink = that.homePageUrl + '/register?inviterId=' + that.id
				that.generateQrCode();
			});
		},

		generateQrCode : function() {
			var that = this;
			that.$nextTick(function() {
				$('.code-content').html('');
				jQuery('.code-content').qrcode({
					width : 200,
					height : 200,
					text : that.qrLink
				});
			});
		},

	}
});