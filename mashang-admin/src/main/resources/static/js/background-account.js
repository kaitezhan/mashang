var accountManageVM = new Vue({
	el : '#account-manage',
	data : {
		accountStateDictItems : [],
		userName : '',
		realName : '',

		addUserAccountFlag : false,
		accountEditFlag : false,
		selectedAccount : {},
		selectedAccountId : '',

		modifyLoginPwdFlag : false,
		newLoginPwd : '',

		lowerLevelAccountFlag : false,
		selectedLowerLevelAccountId : '',

		bindGoogleAuthFlag : false,
		userNameWithGoogleAuth : '',
		googleSecretKey : null,
		googleAuthBindTime : null,
		googleVerCode : '',

		roles : [],
		assignRoleFlag : false,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.findAllRole();
		that.loadAccountTypeItem();
		that.loadAccountStateItem();
		that.initTable();

		$('.chart-container').on('click', '.node', function() {
			that.selectedLowerLevelAccountId = $(this).find('.title').attr('userAccountId');
		});
	},
	methods : {

		findAllRole : function() {
			var that = this;
			that.$http.get('/rbac/findAllRole').then(function(res) {
				this.roles = res.body.data;
			});
		},

		loadAccountTypeItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountType'
				}
			}).then(function(res) {
				this.accountTypeDictItems = res.body.data;
			});
		},

		loadAccountStateItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountState'
				}
			}).then(function(res) {
				this.accountStateDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.account-manage-table').bootstrapTable({
				classes : 'table table-hover',
				url : '/userAccount/findBackgroundAccountByPage',
				pagination : true,
				sidePagination : 'server',
				height : '50%',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				sortable : true,
				sortName : 'cashDeposit',
				sortOrder : 'desc',
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						propertie : params.sortName,
						direction : params.sortOrder,
						userName : that.userName,
						realName : that.realName
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					title : '账号',
					formatter : function(value, row, index) {
						return row.userName;
					},
					cellStyle : {
						classes : 'user-name'
					}
				}, {
					field : 'realName',
					title : '姓名'
				}, {
					field : 'mobile',
					title : '电话'
				}, {
					field : 'stateName',
					title : '账号状态'
				}, {
					title : '谷歌验证器',
					formatter : function(value, row, index, field) {
						if (row.googleAuthBindTime == null) {
							return '未绑定';
						}
						return '<p>验证器密钥:' + row.googleSecretKey + '</p>' + '<p>绑定时间:' + row.googleAuthBindTime + '</p>';
					}
				}, {
					field : 'registeredTime',
					title : '注册时间',
					sortable : true,
					order : 'asc'
				}, {
					field : 'latelyLoginTime',
					title : '最近登录时间',
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						var html = template('account-action', {
							accountInfo : row
						});
						return html;
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.account-manage-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showAssignRoleModal : function(id) {
			var that = this;
			that.selectedAccountId = id;
			for (var i = 0; i < that.roles.length; i++) {
				that.roles.selectdFlag = false;
			}
			that.$http.get('/rbac/findRoleByUserAccountId', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				var selectedRole = res.body.data;
				for (var i = 0; i < that.roles.length; i++) {
					var role = that.roles[i];
					for (var j = 0; j < selectedRole.length; j++) {
						if (role.id == selectedRole[j].id) {
							role.selectdFlag = true;
							break;
						}
					}
				}
				that.assignRoleFlag = true;
			});
		},

		assignRole : function() {
			var that = this;
			var roleIds = [];
			for (var i = 0; i < that.roles.length; i++) {
				if (that.roles[i].selectdFlag) {
					roleIds.push(that.roles[i].id);
				}
			}
			that.$http.post('/rbac/assignRole', {
				userAccountId : that.selectedAccountId,
				roleIds : roleIds
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.assignRoleFlag = false;
				that.refreshTable();
			});
		},

		toUtf8 : function(str) {
			var out, i, len, c;
			out = '';
			len = str.length;
			for (i = 0; i < len; i++) {
				c = str.charCodeAt(i);
				if ((c >= 0x0001) && (c <= 0x007F)) {
					out += str.charAt(i);
				} else if (c > 0x07FF) {
					out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
					out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
					out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
				} else {
					out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
					out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
				}
			}
			return out;
		},

		showBindGoogleAuthModal : function(id, userName) {
			var that = this;
			that.$http.get('/userAccount/getGoogleAuthInfo', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.bindGoogleAuthFlag = true;
				that.selectedAccountId = id;
				that.userNameWithGoogleAuth = userName;
				that.googleVerCode = null;
				that.googleSecretKey = res.body.data.googleSecretKey;
				that.googleAuthBindTime = res.body.data.googleAuthBindTime;
				if (that.googleAuthBindTime == null) {
					layer.alert('首次绑定,系统自动分配谷歌密钥', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					that.generateGoogleSecretKey();
				} else {
					that.generateGoogleQRcode();
				}
			});
		},

		generateGoogleSecretKey : function() {
			var that = this;
			that.$http.get('/userAccount/generateGoogleSecretKey').then(function(res) {
				that.googleSecretKey = res.body.data;
				that.generateGoogleQRcode();
			});
		},

		generateGoogleQRcode : function() {
			var that = this;
			that.$nextTick(function() {
				$('.code-content').html('');
				jQuery('.code-content').qrcode({
					width : 200,
					height : 200,
					text : 'otpauth://totp/' + that.toUtf8(that.userNameWithGoogleAuth) + '?secret=' + that.googleSecretKey
				});
			});
		},

		bindGoogleAuth : function() {
			var that = this;
			if (that.googleVerCode === null || that.googleVerCode === '') {
				layer.alert('请输入谷歌验证码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindGoogleAuth', {
				userAccountId : that.selectedAccountId,
				googleSecretKey : that.googleSecretKey,
				googleVerCode : that.googleVerCode
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('绑定成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.bindGoogleAuthFlag = false;
				that.refreshTable();
			});
		},

		showLowerLevelAccountModal : function(id) {
			var that = this;
			that.selectedAccountId = id;
			that.selectedLowerLevelAccountId = null;
			that.$http.get('/userAccount/findAllLowerLevelAccount', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				var lowerLevelAccounts = res.body.data;
				that.showLowerLevelAccountInner(lowerLevelAccounts);
			});
		},

		showLowerLevelAccountInner : function(lowerLevelAccounts) {
			var that = this;
			var lowerLevelAccountData = that.buildLowerLevelAccountTree(lowerLevelAccounts);
			var nodeTemplate = function(data) {
				return '<div class="title" userAccountId="' + data.id + '">' + data.accountTypeName + '(' + data.stateName + ')</div><div class="content">' + data.userName + '</div>';
			};
			$('.chart-container').empty();
			var oc = $('.chart-container').orgchart({
				'data' : lowerLevelAccountData,
				'nodeTemplate' : nodeTemplate
			});
			that.lowerLevelAccountFlag = true;
		},

		buildLowerLevelAccountTree : function(lowerLevelAccounts) {
			var lowerLevelAccountData = null;
			for (var i = 0; i < lowerLevelAccounts.length; i++) {
				if (this.selectedAccountId == lowerLevelAccounts[i].id) {
					lowerLevelAccountData = lowerLevelAccounts[i];
					break;
				}
			}
			lowerLevelAccountData.children = [];
			this.buildLowerLevelAccountTreeInner(lowerLevelAccountData, lowerLevelAccounts);
			return lowerLevelAccountData;
		},

		buildLowerLevelAccountTreeInner : function(current, lowerLevelAccounts) {
			for (var i = 0; i < lowerLevelAccounts.length; i++) {
				if (current.id == lowerLevelAccounts[i].inviterId) {
					var lowerLevelAccount = lowerLevelAccounts[i];
					lowerLevelAccount.children = [];
					current.children.push(lowerLevelAccount);
					this.buildLowerLevelAccountTreeInner(lowerLevelAccount, lowerLevelAccounts);
				}
			}
		},

		updateLowerLevelAccountState : function(state) {
			var that = this;
			if (that.selectedLowerLevelAccountId == null || that.selectedLowerLevelAccountId == '') {
				layer.alert('请选择下级账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/updateLowerLevelAccountState', {
				userAccountId : that.selectedLowerLevelAccountId,
				state : state
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showLowerLevelAccountModal(that.selectedAccountId);
				$('.account-manage-table').bootstrapTable('refresh');
			});
		},

		openAddAccountModal : function() {
			this.addUserAccountFlag = true;
			this.selectedAccount = {
				userName : '',
				realName : '',
				mobile : '',
				loginPwd : '',
				accountType : ''
			}
		},

		addUserAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount;
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.realName === null || selectedAccount.realName === '') {
				layer.alert('请输入真实姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				layer.alert('请输入手机号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.loginPwd === null || selectedAccount.loginPwd === '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/addBackgroundAccount', selectedAccount, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addUserAccountFlag = false;
				that.refreshTable();
			});
		},

		delAccount : function(id) {
			var that = this;
			layer.confirm('确定要删除该账号吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/userAccount/delUserAccount', {
					params : {
						userAccountId : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showAccountEditModal : function(id) {
			var that = this;
			that.$http.get('/userAccount/findUserAccountDetailsInfoById', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.accountEditFlag = true;
			});
		},

		updateUserAccount : function() {
			var that = this;
			var selectedAccount = that.selectedAccount
			if (selectedAccount.userName === null || selectedAccount.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.realName === null || selectedAccount.realName === '') {
				layer.alert('请输入真实姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.mobile === null || selectedAccount.mobile === '') {
				layer.alert('请输入手机号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (selectedAccount.state === null || selectedAccount.state === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/updateUserAccount', selectedAccount, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.accountEditFlag = false;
				that.refreshTable();
			});
		},

		showModifyLoginPwdModal : function(id) {
			var that = this;
			that.$http.get('/userAccount/findUserAccountDetailsInfoById', {
				params : {
					userAccountId : id
				}
			}).then(function(res) {
				that.selectedAccount = res.body.data;
				that.newLoginPwd = '';
				that.modifyLoginPwdFlag = true;
			});
		},

		modifyLoginPwd : function() {
			var that = this;
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/modifyLoginPwd', {
				userAccountId : that.selectedAccount.id,
				newLoginPwd : that.newLoginPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.modifyLoginPwdFlag = false;
				that.refreshTable();
			});
		}

	}
});