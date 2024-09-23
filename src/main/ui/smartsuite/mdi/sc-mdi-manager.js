/**
 * SCMdiManager
 */
SCMdiManager = new(function() {

	/** 생성자 */
	function MdiManagerImpl() {
		this._callbacks = [];
		this.locale = SCI18nManager.getLocale();
		SCPreloader.onInitialize(this.initialize.bind(this));
	};
	
	MdiManagerImpl.prototype = {
		
		get mdi() {
			return document.querySelector('#mdiMain');
		},
		
		get mdiHead() {
			return this.mdi && this.mdi.$.mdiHead;
		},
		
		get startPage() {
			return SCMenuManager.startPage;
		},
		
		get userModel() {
			return SCSessionManager.model;
		},
		
		get menuModel() {
			return SCMenuManager.model;
    	},
    	
    	get currentUser() {
    		return SCSessionManager.getCurrentUser();
		},

		get currentLocale() {
			return SCLocaleManager.getLocale();
		},

		get avaiableLocales() {
            return SCLocaleManager.getAvaiableLocales();
        },
		
		initialize : function(){
			this.commonService();
		},
		
		/** 공통 서비스 */
		commonService: function(){
			this.localeService();
			this.menuService();
			this.favoriteService();
		},
		
		getStartPage : function(){
			return this.startPage;
		},
		
		/** 오버라이드 */
		extends : function(target, source){
			return Object.assign(target, source);
		}
		
	};
	
	/** 사용 언어 목록 */
	MdiManagerImpl.prototype.localeService = function(){
		SCLocaleManager.service(function(localeData){
			this.availableLocales = localeData;
			this.localeReady = true;
			this._complete();
		}.bind(this));
	}; 
	
	/** 사용자 메뉴 목록 */
	MdiManagerImpl.prototype.menuService = function(){
		SCMenuManager.service(function(menuData){
			this.menuList = menuData;
			this.menuReady = true;
			this._complete();
		}.bind(this));
	}; 
	
	/** 사용자 즐겨찾기 목록 */
	MdiManagerImpl.prototype.favoriteService = function(){
		if(false){
			SCFavoriteManager.service(function(favoriteData){
				this.favoritList = favoriteData;
				this.favoriteReady = true;
				this._complete();
			}.bind(this));
		}else{
			this.favoriteReady = true;
		}
	};
	
	MdiManagerImpl.prototype._complete = function() {
		var callbacks = this._callbacks,
			fn;

		if(this.localeReady && this.menuReady && this.favoriteReady){
			this._readied = true;
			while((fn = callbacks.shift(0))) {
				fn()
			}
		}
	};

	MdiManagerImpl.prototype.onReady = function(callback) {
		if(this._readied) {
			return callback();
		}
		this._callbacks.push(callback);
	};
	MdiManagerImpl.prototype.isReady = function() {
		return !!this._readied;
	};

	MdiManagerImpl.prototype.createWindow = function(menuId, menuName, menuUrl, options) {
		return this.mdi.$.mdiContent.createWindow(menuId, menuName, menuUrl, options);
	};

	MdiManagerImpl.prototype.removeWindow = function(menuId) {
		return this.mdi.$.mdiContent.removeWindow(menuId);
	};

	MdiManagerImpl.prototype.activateWindow = function(menuId) {
		return this.mdi.$.mdiContent.activateWindow(menuId);
	};

	MdiManagerImpl.prototype.activatedWindow = function() {
		return this.mdi && this.mdi.__attached ? this.mdi.$.mdiContent.activatedWindow : null;
	};
	
	MdiManagerImpl.prototype.toogleFavorite = function(menuCd) {
		this.mdi.toogleFavorite(menuCd);
	};

	MdiManagerImpl.prototype.doSessionTimer = function() {
		this.mdiHead && this.mdiHead.__attached && this.mdiHead.doSessionTimer();
	};
	
	MdiManagerImpl.prototype.logout = function() {
		return this.mdiHead.logout();
	};

	MdiManagerImpl.prototype.findScMdiWindow = function(element) {
		while(!Polymer.DomApi.matchesSelector.call(element, 'sc-mdi-window,sc-window') && element.parentElement) {
			element = element.parentElement;
		}
		if(!element.isMdiWindow && Polymer.DomApi.matchesSelector.call(element, 'sc-window')) {
			element = element.windowParentElement && this.findScMdiWindow(element.windowParentElement);
		}
		return element;
	};

	MdiManagerImpl.prototype.getCurrentMenuId = function(element) {
		element = this.findScMdiWindow(element);
		if(element && element.isAttached) {
			return element.getMenuId();
		}
		else if((element = this.activatedWindow())) {
			return element.getMenuId();
		}
		else if(element && !Polymer.DomApi.matchesSelector.call(element, 'sc-code-manager,sc-i18n-provider')) {
			console.error(' 메뉴코드를 가져오지 못하였습니다.')
		}
	};

	MdiManagerImpl.prototype.getCurrentMenuNm = function(element) {
		element = this.findScMdiWindow(element);
		if(element && element.isAttached) {
			return element.getMenuNm();
		}
		else if((element = this.activatedWindow())) {
			return element.getMenuNm();
		}
		else if(element && !Polymer.DomApi.matchesSelector.call(element, 'sc-code-manager,sc-i18n-provider')) {
			console.error(' 메뉴코드를 가져오지 못하였습니다.')
		}
	};

	MdiManagerImpl.prototype.destroy = function() {
		this.startPage = null;
		this.locale = null;
		this._callbacks = null;
		this.menuList = null;
		this.availableLocales = null;
		this.favoritList = null;
		this.mdi = null;
	};

	return MdiManagerImpl;

}());
