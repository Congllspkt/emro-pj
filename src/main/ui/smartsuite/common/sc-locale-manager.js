/**
 * SCLocaleManager
 */
(function(window) {

	function SCLocaleManagerImpl(callbackFns){
		this._callbacks = callbackFns || [];
	}
	
	SCLocaleManagerImpl.prototype = Object.assign({
	
		url : 'i18n/getAvailableLocalizedLocales.do',
		
		defaultLocale : {
			locale : 'ko_KR', 
			displayName : '한국어'
		},
		
		errorMessage : "언어목록 조회중 오류가 발생하였습니다."
		
	}, SCAbstractManager);
	
	SCLocaleManagerImpl.prototype.service = function(callbackFn){
		if(this.availableLocales){
			callbackFn && callbackFn(this.availableLocales);
			return true;
		}
		return this.ajaxService(this.url, {}, function onSuccess(response){
			this.availableLocales = response;
			callbackFn && callbackFn(this.availableLocales);
			this.onCompleted();
		}.bind(this)).fail(function() {
			this.availableLocales = [this.defaultLocale];
			callbackFn && callbackFn(this.availableLocales);
			console.warn(this.errorMessage);
		}.bind(this));
	}
	
	/** Ajax Service */
	SCLocaleManagerImpl.prototype.ajaxService = function(url, parameter, callbackFn){
		if(typeof parameter == "object"){
			parameter = JSON.stringify(parameter);
		}
		return $.ajax(url, $.extend(SCPreloader.ajaxSettings(), {
			data : parameter	
		})).then(function(response) {
			callbackFn && (callbackFn(response));
		});
	};
	
	SCLocaleManagerImpl.prototype.setLocale = function(locale) {
		this.locale = locale;
	};

	SCLocaleManagerImpl.prototype.getLocale = function() {
		return this.locale || SCI18nManager.getLocale();
	};
	
	SCLocaleManagerImpl.prototype.setAvaiableLocales = function(availableLocales) {
		this.availableLocales = availableLocales;
	};

	SCLocaleManagerImpl.prototype.getAvaiableLocales = function(availableLocales) {
		return this.availableLocales;
	};
	
	/** SCPreloader 인스턴스 생성(on preloader-behavior.js) */
	window.SCLocaleManager = new SCLocaleManagerImpl();

}(window));
