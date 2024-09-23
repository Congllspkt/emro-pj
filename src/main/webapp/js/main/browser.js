/**
 * 사용자 브라우저의 유효성을 검사  
 * 2016.09.06, Choi JongHyeok
 */
(function(window) {
	
var BROWSER = {},
	BROWSER_UTIL = {};

	window.BROWSER = BROWSER = {
		browserInfo		: {},  	/*-- Client Browser Info --*/
		browserValid	: new Boolean(), /*-- Browser Support Y/N --*/
		valid			: new Boolean(), /*-- Browser + OS Support Y/N --*/
		supportBrowsers 	: [ /*-- Base Support Browser Object List--*/
		 	{ browser: "MSIE"  	, version: "11" },
		 	{ browser: "Chrome"	, version: "52" }, 
		 	{ browser: "Edge"	, version: "ALL"},
		 	{ browser: "Firefox", version: "48" },
		 	{ browser: "Safari"	, version: "7" }
	    ],
	    setSupportBrowsers : function (supportBrowsers) {  //setter, method chaining
	    	this.supportBrowsers = supportBrowsers;
	    	BROWSER_UTIL.syncValidation();
	    	return this;
	    }
	};
	
	BROWSER_UTIL = {
		getBrowserInfo : function(){
			var
				tp = navigator.platform,
				N= navigator.appName,
				ua= navigator.userAgent,
				tem;
			var result, M;
			
			// if Edge
			M = ua.match(/(edge)\/?\s*(\.?\d+(\.\d+)*)/i);
			M = M ? {"browser":"Edge", "version":M[2]} : M;
			
			// if opera
			if(!M){
				M = ua.match(/(opera|opr)\/?\s*(\.?\d+(\.\d+)*)/i);
				if(M && (tem = ua.match(/version\/([\.\d]+)/i)) != null) M[2] = tem[1];
				M = M ? {"browser":"Opera", "version":M[2]} : M;
			}
			
			// if IE7 under
			if(!M) {
				M = ua.match(/MSIE ([67].\d+)/);
				if(M) M = {"browser":"MSIE", "version":M[1]};
			}
			
			// others
			if(!M) {
				M = ua.match(/(msie|trident|chrome|safari|firefox)\/?\s*(\.?\d+(\.\d+)*)/i);
				if(M){
					if((tem = ua.match(/rv:([\d]+)/)) != null) {
						M[2] = tem[1];
					} else if((tem = ua.match(/version\/([\.\d]+)/i)) != null) {
						M[2] = tem[1];
					}
					if(M[1] == "Trident") M[1] = "MSIE";
					M = M? {"browser":M[1], "version":M[2]} : {"browser":N, "version1":navigator.appVersion,"other":'-?'};
				}
			}
			
			if(!M){
				return {"browser":"UNDEFINED", "version":""};
			}
			
			if(M.version){
				var verArr = (M.version).split(".");
				M.version = verArr[0];
			}
			
			if(M.browser == "MSIE" || M.browser == "Edge") {
				if(tp == "Win32"){
					M.bit = "32";
				} else if (tp == "Win64"){
					M.bit = "64";
				}
			}
			
			result = M;
			
			return result;
		},
		getBrowserVer : function() {
			var browserInfo = BROWSER.browserInfo;
			if(!browserInfo) browserInfo = BROWSER_UTIL.getBrowserInfo();
			return browserInfo.version;
		},
		browserValid : function(currBrowser, supportBrowsers){

			var browser = currBrowser.browser;
			var version = parseInt(currBrowser.version);
			var valid = false;
  
			for(var i=0; i<supportBrowsers.length; i++) {

				var item = supportBrowsers[i];

				if(browser.toUpperCase() === item.browser.toUpperCase()) {
				 
				  if(item.version.toUpperCase() === "ALL" 
								|| version >= parseInt(item.version)) {
					  valid = true;
				  }
				  
				  break;
				}
			}
      
			return valid;
		},
		syncValidation : function() {
			BROWSER.browserInfo = BROWSER_UTIL.getBrowserInfo();
			BROWSER.browserValid = BROWSER_UTIL.browserValid(BROWSER.browserInfo, BROWSER.supportBrowsers);
			if(BROWSER.browserValid) {
				BROWSER.valid = true;
			} else {
				BROWSER.valid = false;
			}
			
			return BROWSER; 
		}
	};
	
	//Init Function 
	(function(){
		BROWSER_UTIL.syncValidation(); 
	})();

})(window);