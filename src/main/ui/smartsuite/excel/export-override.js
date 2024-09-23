
(function(){
	/*
	 * 엑셀 다운로드 CSRF 로직
	 */
	var csrf = document.querySelector('meta[name=_csrf]');
	
	if(window.Export_Downloader && csrf){
		
		//$download 함수가 있을 경우 csrf가 적용되었다고 판단한다. 
		if(Export_Downloader.prototype.$download){
			return;
		}
		
		Export_Downloader.prototype.$download = Export_Downloader.prototype.download;
		
		Export_Downloader.prototype.download = function(params){
			
			
			var csrfHeader = document.querySelector('meta[name=_csrf_parameter]').content;
			var value = csrf.content;
			
			params[csrfHeader] = value;
			
			Export_Downloader.prototype.$download.apply(this,[params]);
		}
	}
}());
