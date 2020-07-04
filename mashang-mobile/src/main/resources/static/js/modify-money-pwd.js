var modifyMoneyPwdVM = new Vue({
	el : '#modifyMoneyPwd',
	data : {
		global : GLOBAL,
		oldMoneyPwd : '',
		newMoneyPwd : '',
		confirmMoneyPwd : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
	},
	methods : {

		modifyMoneyPwd : function() {
			var that = this;
			if (that.oldMoneyPwd == null || that.oldMoneyPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请输入当前密码'
				});
				return;
			}
			if (that.newMoneyPwd == null || that.newMoneyPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请输入新密码'
				});
				return;
			}
			if (that.confirmMoneyPwd == null || that.confirmMoneyPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请确认密码'
				});
				return;
			}
			if (that.newMoneyPwd != that.confirmMoneyPwd) {
				this.$notify({
					type : 'warning',
					message : '密码不一致'
				});
				return;
			}
			that.$http.post('/userAccount/modifyMoneyPwd', {
				oldMoneyPwd : that.oldMoneyPwd,
				newMoneyPwd : that.newMoneyPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				that.$toast.success({
					message : '密码修改成功!',
					forbidClick : true,
					onClose : function() {
						window.location.href = '/';
					}
				});
			});
		},
	}
});