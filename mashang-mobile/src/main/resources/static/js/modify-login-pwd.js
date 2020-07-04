var modifyLoginPwdVM = new Vue({
	el : '#modifyLoginPwd',
	data : {
		global : GLOBAL,
		oldLoginPwd : '',
		newLoginPwd : '',
		confirmLoginPwd : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
	},
	methods : {

		modifyLoginPwd : function() {
			var that = this;
			if (that.oldLoginPwd == null || that.oldLoginPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请输入当前密码'
				});
				return;
			}
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请输入新密码'
				});
				return;
			}
			if (that.confirmLoginPwd == null || that.confirmLoginPwd == '') {
				this.$notify({
					type : 'warning',
					message : '请确认密码'
				});
				return;
			}
			if (that.newLoginPwd != that.confirmLoginPwd) {
				this.$notify({
					type : 'warning',
					message : '密码不一致'
				});
				return;
			}
			var passwordPatt = /^[A-Za-z][A-Za-z0-9]{5,14}$/;
			if (!passwordPatt.test(that.newLoginPwd)) {
				this.$notify({
					type : 'warning',
					message : '请输入以字母开头,长度为6-15个字母和数字的密码'
				});
				return;
			}
			that.$http.post('/userAccount/modifyLoginPwd', {
				oldLoginPwd : that.oldLoginPwd,
				newLoginPwd : that.newLoginPwd
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