/**
* SCI18NManager
*/
(function() {
	
	var win = window,
		locale = SCI18nManager.getLocale(),
		ajaxsettings = SCPreloader.ajaxSettings(),
		csrf =  SCPreloader.csrf(),
		convert = SCPreloader.convert;

	$.ajax('i18n/getByLastUpdated.do', $.extend(false, ajaxsettings, {
		data : JSON.stringify({
			locale : locale,
			lastUpdated : new Date(Number(localStorage.getItem('i18n-' + locale + '.lastUpdated') || '-2209107600000')) 
		})	
	})).then(function(response) {
		return convert(response);	
	}).then(function(i18n) {
		if(win.Polymer.Settings) {
			win.Polymer.Settings.i18nResponse = i18n;
		}
		else {
			win.Polymer.i18nResponse = i18n;
		}
		//i18nmanager
		if(csrf) {
    		var i18nHeader = SCI18nProvider.prototype.properties.requestHeaders.value();
     		SCI18nProvider.prototype.properties.requestHeaders.value = function() {
     			i18nHeader[csrf.csrfHeader] = csrf.csrf;
     			return i18nHeader;
     		};
    	}
		SCI18nManager.setProvider(new SCI18nProvider({ byKeysUrl : 'i18n/getByKeys.do', byLastUpdatedUrl : 'i18n/getByLastUpdated.do' }));
	}.bind(this)).fail(function() {
		console.error("다국어 메세지 조회중 오류가 발생하였습니다.");
	});
	
}());