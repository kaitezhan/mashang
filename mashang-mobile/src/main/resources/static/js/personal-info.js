var personalInfoVM = new Vue({
	el : '#personalInfo',
	data : {
		global : GLOBAL,
		accountInfo : {},
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.getMemberAccountInfo();
	},
	methods : {

		getMemberAccountInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getMemberAccountInfo').then(function(res) {
				that.accountInfo = res.body.data;
			});
		}
	}
});