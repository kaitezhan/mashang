var roleManageVM = new Vue({
	el : '#role-manage',
	data : {
		menuTrees : [],
		roles : [],

		addOrUpdateRoleFlag : false,
		roleActionTitle : '',
		editRole : {},

		assignMenuFlag : false,
		selectedRoleId : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		jQuery.browser = {};
		(function() {
			jQuery.browser.msie = false;
			jQuery.browser.version = 0;
			if (navigator.userAgent.match(/MSIE ([0-9]+)./)) {
				jQuery.browser.msie = true;
				jQuery.browser.version = RegExp.$1;
			}
		})();
		this.findMenuTree();
		this.findAllRole();
	},
	methods : {

		findMenuTree : function() {
			var that = this;
			that.$http.get('/rbac/findMenuTree').then(function(res) {
				that.menuTrees = res.body.data;
			});
		},

		assignMenu : function() {
			var that = this;
			var menuIds = [];
			var selectedMenus = $.fn.zTree.getZTreeObj('menuTree').getCheckedNodes(true);
			for (var i = 0; i < selectedMenus.length; i++) {
				menuIds.push(selectedMenus[i].id);
			}
			that.$http.post('/rbac/assignMenu', {
				roleId : that.editRole.id,
				menuIds : menuIds
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.assignMenuFlag = false;
				that.findAllRole();
			});
		},

		showAssignMenuModal : function(role) {
			var that = this;
			that.editRole = role;
			that.$http.get('/rbac/findMenuByRoleId', {
				params : {
					roleId : role.id
				}
			}).then(function(res) {
				that.assignMenuFlag = true;
				that.buildTree(res.body.data);
			});
		},

		validIsSelected : function(selectedMenus, menu) {
			for (var i = 0; i < selectedMenus.length; i++) {
				if (selectedMenus[i].id == menu.id) {
					return true;
				}
			}
			return false;
		},

		buildTree : function(selectedMenus) {
			var that = this;
			for (var i = 0; i < that.menuTrees.length; i++) {
				var menu1 = that.menuTrees[i];
				menu1.checked = that.validIsSelected(selectedMenus, menu1);
				for (var j = 0; j < menu1.subMenus.length; j++) {
					var subMenu = menu1.subMenus[j];
					subMenu.checked = that.validIsSelected(selectedMenus, subMenu);
					for (var k = 0; k < subMenu.subMenus.length; k++) {
						var btn = subMenu.subMenus[k];
						btn.checked = that.validIsSelected(selectedMenus, btn);
					}
				}
			}
			var setting = {
				check : {
					enable : true
				},
				data : {
					key : {
						name : 'name',
						children : 'subMenus',
						checked : 'checked',
						url : 'xUrl'
					}
				},
				callback : {}
			};
			that.$nextTick(function() {
				$.fn.zTree.init($('#menuTree'), setting, that.menuTrees);
			});
		},

		delRole : function(id) {
			var that = this;
			that.$http.get('/rbac/delRole', {
				params : {
					id : id
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.findAllRole();
			});
		},

		findAllRole : function() {
			var that = this;
			that.$http.get('/rbac/findAllRole').then(function(res) {
				that.roles = res.body.data;
			});
		},

		openAddRoleModal : function() {
			var that = this;
			that.addOrUpdateRoleFlag = true;
			this.roleActionTitle = '新增角色';
			this.editRole = {
				name : ''
			};
		},

		showEditRoleModal : function(id) {
			var that = this;
			that.$http.get('/rbac/findRoleById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editRole = res.body.data;
				that.addOrUpdateRoleFlag = true;
				that.roleActionTitle = '编辑角色';
			});
		},

		addOrUpdateRole : function() {
			var that = this;
			var editRole = that.editRole;
			if (editRole.name == null || editRole.name == '') {
				layer.alert('请输入角色名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/rbac/addOrUpdateRole', editRole).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateRoleFlag = false;
				that.findAllRole();
			});
		},

	}
});