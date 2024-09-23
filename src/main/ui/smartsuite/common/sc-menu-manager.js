/**
 * SCMenuManager
 */
(function(window) {

	function SCMenuManagerImpl(){}
	
	SCMenuManagerImpl.prototype = Object.assign({
	
		ADMIN_MENU_CD_PREFIX_REGEXP : new RegExp("ADM\\d+"),
	
		model : {
			menu_id: "menu_id",
			menu_nm: "menu_nm",
			menu_sort: "menu_sort",
			menu_url: "menu_url",
			parent_menu_id: "parent_menu_id",
			ml_menu_cd: "ml_menu_cd",
			tree: "tree",
			tree_nm: "tree_nm",
			ten_id: "ten_id",
			lang_ccd: "lang_ccd",
			usr_typ_ccd: "usr_typ_ccd"
		},
		
		isLoading : false,
	
		url : 'findListMenu.do',
		
		startPage : {
			menu_id: "HOME",
			menu_nm: "HOME",
			menu_url: "ui/bp/portal/em-portal.html"
		},
		
		allowedMenuIdsWithoutRole : [
			"HOME", "CAL50100"
		],
		
		defaultMenuData : null
	}, SCAbstractManager);
	
	SCMenuManagerImpl.prototype.service = function(callbackFn){
		if((this.defaultMenuData && this.defaultMenuData.length > 0)){
			this.menuData = this.generateCustomMenu(this.defaultMenuData);
			callbackFn && callbackFn(this.menuData);
			return true;
		}else{
			if(this.menuData && this.menuData.length > 0){
				callbackFn && callbackFn(this.menuData);
				return true;
			}else{
				return this.ajaxService(this.url, {}, function onSuccess(response){
					this.menuData = response;
					this.isLoading = false;
					callbackFn && callbackFn(this.menuData);
					this.onCompleted();
				}.bind(this)).fail(function() {
					this.menuData = [];
					console.error("메뉴 데이터 조회중 오류가 발생하였습니다.");
				});
			}
		}
	};
	
	/** Ajax Service */
	SCMenuManagerImpl.prototype.ajaxService = function(url, parameter, callbackFn){
		if(typeof parameter == "object"){
			parameter = JSON.stringify(parameter);
		}
		return $.ajax(url, $.extend(SCPreloader.ajaxSettings(), {
			data : parameter	
		})).then(function(response) {
			callbackFn && (callbackFn(response));
		});
	}
	
	SCMenuManagerImpl.prototype.generateCustomMenu = function(menuList){
		for(var i=0; i<menuList.length; i++){
			menuList[i] = {
			    "menu_typ": null,
			    "menu_url": menuList[i].menu_url,
			    "tree": menuList[i].menu_id,
			    "md_cls": null,
			    "parent_menu_id": menuList[i].parent_menu_id || "ROOT",
			    "level": menuList[i].level || 0,
			    "sys_id": "EMRO",
			    "sort_ord": (i+1),
			    "usr_cls": "B",
			    "lang_cd": "ko_KR",
			    "ml_menu_cd": menuList[i].menu_id,
			    "tree_nm": menuList[i].menu_nm,
			    "menu_nm": menuList[i].menu_nm,
			    "menu_id": menuList[i].menu_id,
			    "popup": menuList[i].popup,
			}
			if(!menuList[i].menu_url && menuList[i].children && menuList[i].children.length > 0){
				
			}
		}
		return menuList;
	}
	
	SCMenuManagerImpl.prototype.getMenuList = function() {
		return this.menuData;
	};

	SCMenuManagerImpl.prototype.setMenuList = function(menuData) {
		this.menuData = menuData;
	};
	
	SCMenuManagerImpl.prototype.isAdminMenu = function(windowId) {
		return this.ADMIN_MENU_CD_PREFIX_REGEXP.test(windowId);
	};

	SCMenuManagerImpl.prototype.setMenuNodeMap = function(menuNodeMap){
		this.menuNodeMap = menuNodeMap;
	},

	SCMenuManagerImpl.prototype.getMenuNodeMap = function(){
		return this.menuNodeMap;
	},

	SCMenuManagerImpl.prototype.setTreeMenuList = function(treeMenuList){
		this.treeMenuList = treeMenuList;
	},

	SCMenuManagerImpl.prototype.getTreeMenuList = function(){
		return this.treeMenuList;
	},

	SCMenuManagerImpl.prototype.getRootMenuNode= function(menuId){
		if(!menuId){
			return;
		}

		var menu = this.getMenuNode(menuId);

		if(!menu){
			return;
		}

		return (menu.menu_id === menu.root_menu_id ? menu : this.getMenuNode(menu.root_menu_id));
	};

	SCMenuManagerImpl.prototype.getMenuNode= function(menuId){
		var menuNode = this.menuNodeMap[menuId];
		if(menuNode){
			return menuNode;
		}
		var getNode = function(nodes, id, keyName, childKeyName){
			var result;
			for(var i=0, child; child = nodes[i]; i++) {
				if(child[keyName] === id) {
					return child;
				}
				else{
					if(!child[childKeyName]){
						continue;
					}
					result = arguments.callee(child[childKeyName], id, keyName, childKeyName) || result;
					if(result){
						return result;
					}
				}
			}
			return result;
		};

		return getNode(this.getTreeMenuList() || [], menuId, "menu_id", "sub_menu_list");
	};
	
	SCMenuManagerImpl.prototype.getCurrentMenuId = function(element) {
			var mainModule = UT.getMainModule(element);
			return this.getMenuNodeByDom(mainModule).menu_id;
		};
	
	SCMenuManagerImpl.prototype.getMenuNodeByDom = function(moduleName){
		for(var i=0, menu; i<this.menuData.length; i++){
			menu = this.menuData[i];
			if(menu.menu_url && menu.menu_url.indexOf(moduleName) >= 0){
				return menu; 
			}
		}
		return {};
	};
	
	SCMenuManagerImpl.prototype.getWindow = function(){
		return MDIUT.activatedWindow();
	};
	
	/** SCPreloader 인스턴스 생성(on preloader-behavior.js) */
	window.SCMenuManager = new SCMenuManagerImpl();

}(window));