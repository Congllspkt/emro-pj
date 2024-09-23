
/**
 * SCSdiManager
 */
SCSdiManager = new(function() {

	var ADMIN_MENU_CD_PREFIX_REGEXP = new RegExp("ADM\\d+"),
		CONTEXT_PATH = (function() {
			 var pathname = location.pathname;
			 if(pathname.indexOf('.do') > -1) {
				 pathname = pathname.substring(0, pathname.lastIndexOf('/') + 1);
			 }
			 return location.host + pathname;
		}());

	function SdiManagerImpl() {
		this.locale = SCI18nManager.getLocale();
		CSRF = SCPreloader.csrf();
		AJAXSETTINGS = SCPreloader.ajaxSettings();
	};
	
	/** 사용 가능한 로케일 */
	SdiManagerImpl.prototype._locale = function(fn) {
		return $.ajax('i18n/getAvailableLocalizedLocales.do', AJAXSETTINGS).done(function(availableLocales) {
			this.availableLocales = availableLocales;
			fn && (fn(this.availableLocales));
		}.bind(this)).fail(function() {
			this.availableLocales = [{locale : 'ko_KR', displayName : '한국어'}];
			console.error("언어목록 조회중 오류가 발생하였습니다.");
		}.bind(this));
	};
	
	/** 메뉴 정보 */
	SdiManagerImpl.prototype._menu = function(fn) {
		return SCMenuManager.menuService(function(menuList){
			this.menuList = menuList;
		}.bind(this));
	};
	
	SdiManagerImpl.prototype.getLocale = function() {
		return this.locale;
	};
	
	SdiManagerImpl.prototype.getAvaiableLocales = function(availableLocales) {
		return this.availableLocales;
	};
	
	SdiManagerImpl.prototype.getMenuList = function() {
		return this.menuList;
	};
	
    SdiManagerImpl.prototype.destroy = function() {
        this.locale = null;
        this.menuList = null;
        this.availableLocales = null;
    };
	
	return SdiManagerImpl;
	
}());
