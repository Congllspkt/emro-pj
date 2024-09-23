/**
* SCRoleManager
*/
SCRoleManager = new(function() {

	var AJAXSETTINGS = SCPreloader.ajaxSettings(),
		IS_AUTHENTICATED = SCPreloader.isAuthenticated(); // 로그인 인증 여부(추가)

	function RoleManagerImpl() {
		this.userRoles = [];
		this._callbacks = [];

		if(IS_AUTHENTICATED){ // 로그인 인증 여부(추가)
			$.ajax('findListUserRole.do', AJAXSETTINGS).done(function(roles) {
				this.userRoles = roles;
				this._complete();
			}.bind(this)).fail(function() {
				console.error("사용자 롤정보 조회중 오류가 발생하였습니다.");
			});
		}
	}

	/*var AJAXSETTINGS = SCPreloader.ajaxSettings();
		
	function RoleManagerImpl() {
		this.userRoles = [];
		this._callbacks = [];
		$.ajax('findListUserRole.do', AJAXSETTINGS).done(function(roles) {
			this.userRoles = roles;
			this._complete();
		}.bind(this)).fail(function() {
			console.error("사용자 롤정보 조회중 오류가 발생하였습니다.");
		});
	}*/
	RoleManagerImpl.prototype._complete = function(callback) {
		var callbacks = this._callbacks,
			fn;
		
		this._readied = true;
		//callback run
		while((fn = callbacks.shift(0))) {
			fn()
		}
	};
	RoleManagerImpl.prototype.onReady = function(callback) {
		if(this._readied) {
			return callback();
		}
		this._callbacks.push(callback);
	};
	RoleManagerImpl.prototype.isReady = function() {
		return !!this._readied;
	};
	RoleManagerImpl.prototype.findListUserRole = function() {
	  	return this.userRoles;
	};
	
	RoleManagerImpl.prototype.getAllowedMenuIdsWithoutRole = function() {
		return SCMenuManager.allowedMenuIdsWithoutRole;
	}
	
	RoleManagerImpl.prototype.getUserFuncs = function(menuCd) {
		// userRole array 에서 menu_cd 별 act_cd 를 조회, array 에 담아서 리턴
		var funcs = [];
		var compareMenuCd = "";
     	this.userRoles.forEach(function(element, index, array){
     		if(compareMenuCd !== "" && compareMenuCd !== element.menu_cd)
     			return funcs;
      		if(element.menu_cd == menuCd){
				compareMenuCd = menuCd;
				funcs.push(element.act_cd);
       		}
		});		
		return funcs;
	};	  
	
	RoleManagerImpl.prototype.hasRole = function(windowUrl, windowId, args) {
		var link = args[1],
			splitedWindowUrl = windowUrl.split('?')[0],
			url = link.href.split(SCPreloader.contextPath())[1].split('?')[0],
			menu = SCMenuManager.menuNodeMap[windowId] ||
				(SCMenuManager.startPage.menu_id === windowId && SCMenuManager.startPage);

		// 권한 없이 허용되는 메뉴 아이디 목록 확인
		if (this.getAllowedMenuIdsWithoutRole().indexOf(windowId) > -1) {
			return true;
		}

		if(!menu) {
			console.warn("권한없는 메뉴호출");
			return false;
		}
		//windowUrl.indexOf("&widget=1") < 0 추가 포틀릿에서 파라미터 넘길경우 예외처리
		else if(menu.menu_url.indexOf(url) == -1 || (windowUrl.indexOf("&widget=1") < 0 && menu.menu_url != windowUrl && menu.menu_url != splitedWindowUrl)) {
			console.warn("잘못된 메뉴 url 호출");
			return false;
		}
		return true;
	};
	
	RoleManagerImpl.prototype.destroy = function() {
		this.userRoles = null;
	};
	return RoleManagerImpl;
}());