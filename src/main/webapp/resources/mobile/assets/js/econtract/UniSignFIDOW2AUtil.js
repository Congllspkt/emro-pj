////////////////////////////////////////////////////////////////////////////////
//	JavaScript Utility
//	Only use for AAAS for SmartPhone
////////////////////////////////////////////////////////////////////////////////

// Message for Web to App Process
var _USMessage = {
    NoticeDownload : "앱이 설치되어 있지 않을 경우\n설치페이지로 이동합니다.",
    ConfirmDownload : "앱 설치 후 이용 가능합니다.\n설치 페이지로 이동하시겠습니까?",
    NoticeUnsupportedOS : "지원하지 않는 운영체제입니다."
};

var _test = {
    countWindows : function() {
        var myWins = safari.application.browserWindows;
        alert('windows count : ' + myWins);
    }
};

// API for UniSign
var UniSignW2A = {
    Scheme : {
        //iOS : "CCFIDOForOCU",
		iOS : "unisign-app",
        Android : "crosscert"
    },

    Package : {
        Android : "com.crosscert.android"
    },

    DownloadURL : {
    //     iOS :"https://itunes.apple.com/kr/app/%ED%95%9C%EA%B5%AD%EC%A0%84%EC%9E%90%EC%9D%B8%EC%A6%9Dfido/id1275327576?mt=8",
		 iOS :"https://itunes.apple.com/kr/app/gong-in-injeungsenteo/id426081742?mt=8",
        Android : "market://details?id=com.crosscert.android"
    },

    AutoCheckInstallation : true,
    UseTopLocation : false,

   checkCrossCert : function(uri) {
   
        if (_USUtil.OS.isAndroid()) {
            alert(_USMessage.NoticeDownload);
            top.location.href = _USUtil.makeIntent(this.Scheme.Android, "init",
                    "isInit", "true");
        } 
		
		else if (_USUtil.OS.isiOS()) {
            if (false == this.AutoCheckInstallation && null != uri
                    && 0 != uri.length) {
                top.location.href = uri;
                return;
            }
  
            if (null == uri || 0 == uri.length) {
                uri = this.Scheme.iOS + '://?cmd=Main&caller_url_scheme='
                        + location.href + '&callback=01';
            }
			var clickedAt = +new Date;
			var visitedAt = (new Date()).getTime(); // 방문 시간

            /** --------------------------------------------
            // 1. 통합인증센터 앱이 설치 되지 않았다고 가정 후 앱스토어로 이동하는 팝업을 띄운다.
            setTimeout(function() 
			{
                if (+new Date - clickedAt < 2000) {
                    var ret = confirm(_USMessage.ConfirmDownload);
                    if(true == ret) {
						UniSignW2A.moveToStore();
					    
                    } else {
						console.debug('confirm cancel');
                    }

                }
            }, 1000);

            //2. 통합인증센터로 이동한다.
		    setTimeout(function(){
                self.location.href = uri;
            }, 0);
            -------------------------------------------- */

            /** --------------------------------------------
            // 사파리에서는 비활성화 된 탭의 경우 timer, interval, worker 등이 동작하지 않음.
            // 유효하지 않은 타이머 코드
            // 1. 사파리에 전자서명 후 여러 탭 생김 방지를 위해 탭을 닫는다.
            setTimeout(function() 
            {
                UniSignW2A.closeWindow();                           
            }, 500);
            // 2. 통합인증센터로 이동            
            self.location.href = uri;
            -------------------------------------------- */

            // 1. 통합인증센터 앱이 설치 되지 않았다고 가정 후 앱스토어로 이동하는 팝업을 띄운다.
            setTimeout(function() 
            {
                if (+new Date - clickedAt < 2000) {
                    var ret = confirm(_USMessage.ConfirmDownload);
                    if(true == ret) {
                        UniSignW2A.moveToStore();
                        
                    } else {
                        console.debug('confirm cancel');
                    }

                }
            }, 1000);

            //2. 통합인증센터로 이동한다.
            setTimeout(function(){
                self.location.href = uri;
            }, 0);


		/*
		var app = window.open(uri,'_self');
			if(app.document && app.document.body) {
			alert('not null2');
				alert(typeof(app));
				self.close();
			} else {
			alert('null');
				alert(app);
			}
		*/
        } 
		else {
            alert(_USMessage.NoticeUnsupportedOS);
        }

		/*
		setTimeout(function() 
		{
			 if ((new Date()).getTime() - visitedAt < 2000) {				
				UniSignW2A.closeWindow();								
			}
        }, 500);
		*/
    },

	closeWindow : function () {
        self.close();
        window.open('','_parent','');
        window.close();
    },
	
    moveToStore : function() {

        if (_USUtil.OS.isAndroid()) {
            self.location.href = this.DownloadURL.Android;
        } else if (_USUtil.OS.isiOS()) {
			self.location.href = this.DownloadURL.iOS;
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    getLicenseInfo : function(retURL) {
	   
        if (_USUtil.OS.isAndroid()) {
		    var intent = _USUtil.makeIntent(this.Scheme.Android, "licenseinfo",
                    "requestCode", "2", "retURL", retURL);
			
            top.location.href = intent;
		
        } else if (_USUtil.OS.isiOS()) {
         
			var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "LicenseInfo", "caller_url_scheme", retURL, "callback",
                    "01");
            this.checkCrossCert(customScheme);
        } else {
		    alert("기타");
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    manageCert : function(retURL, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "mngmt",
                    "requestCode", "1", "retURL", retURL, "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ManageCert", "caller_url_scheme", retURL, "callback",
                    rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    importCert : function(retURL, appkey, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
			//window.crosscert.importCert(retURL, appkey, rtnParam);
            var intent = _USUtil.makeIntent(this.Scheme.Android, "import",
                    "requestCode", "3", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ImportCert", "caller_url_scheme", retURL, "appkey",
                    appkey, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    importCertWithShow : function(retURL, appkey, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "import",
                    "requestCode", "3", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ImportCertEx", "caller_url_scheme", retURL, "appkey",
                    appkey, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    exportCert : function(retURL, appkey, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            //window.crosscert.exportCert(retURL, appkey, rtnParam);
            var intent = _USUtil.makeIntent(this.Scheme.Android, "export",
                    "requestCode", "4", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ExportCert", "caller_url_scheme", retURL, "appkey",
                    appkey, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    exportCertWithShow : function(retURL, appkey, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "export",
                    "requestCode", "4", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ExportCertEx", "caller_url_scheme", retURL, "appkey",
                    appkey, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    issueCert : function(retURL, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "issue",
                    "requestCode", "5", "retURL", retURL, "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "IssueCert", "caller_url_scheme", retURL, "callback",
                    rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    renewCert : function(retURL, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "renew",
                    "requestCode", "6", "retURL", retURL, "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "UpdateCert", "caller_url_scheme", retURL, "callback",
                    rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    disuseCert : function(retURL, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "disuse",
                    "requestCode", "7", "retURL", retURL, "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "RevokeCert", "caller_url_scheme", retURL, "callback",
                    rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    esign : function(retURL, appkey, data, rtnParam, sidOption, sidValue) {
	
        if (_USUtil.OS.isAndroid()) {
            
			//window.crosscert.esign(retURL, appkey, data, rtnParam, sidOption, sidValue);
            var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
          
            if(userAgent != null){
                var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
                        "requestCode", "8", "userAgent", userAgent, "retURL", retURL, "appkey", appkey,
                        "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
                        "sidOption", sidOption, "sidValue", sidValue);
                        
    //                  alert('intent='+intent);
            }else{
                var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
                        "requestCode", "8", "retURL", retURL, "appkey", appkey,
                        "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
                        "sidOption", sidOption, "sidValue", sidValue);

            }
            top.location.href = intent;

        } else if (_USUtil.OS.isiOS()) {

            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "SignedData", "caller_url_scheme", retURL, "appkey",
                    appkey, "data", _USUtil.Base64.encode(data), "callback",
                    rtnParam, "sidOption", sidOption, "sidValue", sidValue);
            this.checkCrossCert(customScheme);
        } else {
			
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },
	
	   
	
    esignWithShow : function(retURL, appkey, data, rtnParam, sidOption, sidValue) {
        if (_USUtil.OS.isAndroid()) {
					
            var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
                    "requestCode", "8", "retURL", retURL, "appkey", appkey,
                    "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
                    "sidOption", sidOption, "sidValue", sidValue);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "SignedDataEx", "caller_url_scheme", retURL, "appkey",
                    appkey, "data", _USUtil.Base64.encode(data), "callback",
                    rtnParam, "sidOption", sidOption, "sidValue", sidValue);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },
	esignWithOID : function(retURL, appkey, data, rtnParam, sidOption, sidValue, OIDs) {
	
        if (_USUtil.OS.isAndroid()) {
		    // === 채은 ===
		    var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
				var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
						"requestCode", "8", "userAgent", userAgent, "retURL", retURL, "appkey", appkey,
						"data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
						"sidOption", sidOption, "sidValue", sidValue, "certOIDs", OIDs);
			}else{
				var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
						"requestCode", "8", "retURL", retURL, "appkey", appkey,
						"data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
						"sidOption", sidOption, "sidValue", sidValue, "certOIDs", OIDs);

			}
			           
            top.location.href = intent;
			
        } else if (_USUtil.OS.isiOS()) {

            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "SignedData", "caller_url_scheme", retURL, "appkey",
                    appkey, "data", _USUtil.Base64.encode(data), "callback",
                    rtnParam, "sidOption", sidOption, "sidValue", sidValue, "certOIDs", OIDs);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },
    vidr : function(retURL, appkey, rtnParam, sidOption, sidValue) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "vidr",
                    "requestCode", "12", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam, "sidOption", sidOption, "sidValue",
                    sidValue);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "VIDR", "caller_url_scheme", retURL, "appkey", appkey,
                    "callback", rtnParam, "sidOption", sidOption, "sidValue",
                    sidValue);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    etax : function(retURL, appkey, data, option, rtnParam) {
        if (_USUtil.OS.isAndroid()) {

            var intent = _USUtil.makeIntent(this.Scheme.Android, "etax",
                    "requestCode", "10", "retURL", retURL, "appkey", appkey,
                    "data", data, "option", option, "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "etax", "caller_url_scheme", retURL, "appkey", appkey,
                    "data", data, "option", option, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },
    
    getCertList : function(retURL, appkey) {
    	
        if (_USUtil.OS.isAndroid()) {
			window.crosscert.getCertList(retURL, appkey);
        } else if (_USUtil.OS.isiOS()) {
			try {
				window.webkit.messageHandlers.getCertList.postMessage({retURL: retURL, appkey: appkey});
			} catch(err){
				alert(err);
			}
        } else {
			
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },
    
    deleteCert : function(retURL, appkey, certIdx) {
    	
        if (_USUtil.OS.isAndroid()) {
			window.crosscert.deleteCert(retURL, appkey, certIdx);
        } else if (_USUtil.OS.isiOS()) {
			try {
				window.webkit.messageHandlers.deleteCert.postMessage({retURL: retURL, appkey: appkey, certIdx: certIdx});
			} catch(err){
				alert(err);
			}
        } else {
			
            alert(_USMessage.NoticeUnsupportedOS);
        }
    }
};

// API for VeriSign
var VeriSign = {
    Scheme : {
        iOS : "unisignv-app",
        Android : "crosscertv"
    },

    Package : {
        Android : "com.crosscertv.android"
    },

    DownloadURL : {
        iOS : "https://itunes.apple.com/kr/app/verisigninjeungsenteo/id495605308?mt=8",
        Android : "market://details?id=com.crosscertv.android"
    },

    AutoCheckInstallation : true,

    checkCrossCert : function(uri) {
        if (_USUtil.OS.isAndroid()) {
            alert(_USMessage.NoticeDownload);
            top.location.href = _USUtil.makeIntent(this.Scheme.Android, "init",
                    "isInit", "true");
        } else if (_USUtil.OS.isiOS()) {
            if (false == this.AutoCheckInstallation && null != uri
                    && 0 != uri.length) {
                top.location.href = uri;
                return;
            }

            if (null == uri || 0 == uri.length) {
                uri = this.Scheme.iOS + '://?cmd=Main&caller_url_scheme='
                        + location.href + '&callback=01';
            }

            var clickedAt = +new Date;

            // var checkTimer =
            setTimeout(function() {
                if (+new Date - clickedAt < 1600) {
                    if (confirm(_USMessage.ConfirmDownload)) {
                        VeriSign.moveToStore();
                    } else {
                        console.debug('confirm cancel');
                    }
                }
            }, 1500);

            top.location.href = uri;
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    moveToStore : function() {
	
        if (_USUtil.OS.isAndroid()) {
            top.location.href = this.DownloadURL.Android;
        } else if (_USUtil.OS.isiOS()) {
            top.location.href = this.DownloadURL.iOS;
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    getLicenseInfo : function(retURL) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "licenseinfo",
                    "requestCode", "2", "retURL", retURL);
					
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "LicenseInfo", "caller_url_scheme", retURL, "callback",
                    "01");
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    importCert : function(retURL, appkey, rtnParam) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "import",
                    "requestCode", "3", "retURL", retURL, "appkey", appkey,
                    "rtnParam", rtnParam);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "ImportCert", "caller_url_scheme", retURL, "appkey",
                    appkey, "callback", rtnParam);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    esign : function(retURL, appkey, data, rtnParam, sidOption, sidValue) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeIntent(this.Scheme.Android, "esign",
                    "requestCode", "8", "retURL", retURL, "appkey", appkey,
                    "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam,
                    "sidOption", sidOption, "sidValue", sidValue);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS,
                    "SignedData", "caller_url_scheme", retURL, "appkey",
                    appkey, "data", _USUtil.Base64.encode(data), "callback",
                    rtnParam, "sidOption", sidOption, "sidValue", sidValue);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    }
};

// API for FIDO
var FIDOW2A = {
    Scheme : {
       //iOS : "CCFIDOForOCU",
	   iOS : "unisign-app",
       Android : "crosscert"
    },

    Package : {
        Android : "com.crosscert.android"
    },

    DownloadURL : {
        // iOS :"https://itunes.apple.com/kr/app/%ED%95%9C%EA%B5%AD%EC%A0%84%EC%9E%90%EC%9D%B8%EC%A6%9Dfido/id1275327576?mt=8",
		iOS :"https://itunes.apple.com/kr/app/gong-in-injeungsenteo/id426081742?mt=8",
        Android : "market://details?id=com.crosscert.android"
    },

    AutoCheckInstallation : true,
    UseTopLocation : false,

    checkCrossCert : function(uri) {
        if (_USUtil.OS.isAndroid()) {
            alert(_USMessage.NoticeDownload);
            top.location.href = _USUtil.makeIntent2(this.Scheme.Android,
                    "init", "isInit", "true");
        } else if (_USUtil.OS.isiOS()) {
		
            if (false == this.AutoCheckInstallation && null != uri
                    && 0 != uri.length) {
                top.location.href = uri;
                return;
            }

            if (null == uri || 0 == uri.length) {
                uri = this.Scheme.iOS + '://?cmd=Main&caller_url_scheme='
                        + location.href + '&callback=01';
            }
			
            var clickedAt = +new Date;

            // var checkTimer =
            setTimeout(function() {
                if ((+new Date - clickedAt) < 1600) {
				
				 alert('FIDOW2A checkCrossCert : ');
                    // var ret = confirm(_USMessage.ConfirmDownload);
                    // console.log('confirm result : ' + ret);

                    // if(true == ret) {
                    // if(confirm(_USMessage.ConfirmDownload)) {
                    //    UniSignW2A.moveToStore();
                    // } else {
                    // console.debug('confirm cancel');
                    // }
                }
            }, 1500);
			
            top.location.href = uri;
        } else {
			
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    reg : function(caller_url_scheme, appkey, jsonData) {
	    if (_USUtil.OS.isAndroid()) {
		
			var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
		
			alert("debug = " + intent);
			top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeFIDOScheme(this.Scheme.iOS, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
					
            this.checkCrossCert(customScheme);
        } else {
		    alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    auth : function(caller_url_scheme, appkey, jsonData) {
        if (_USUtil.OS.isAndroid()) {
		
			var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
           
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeFIDOScheme(this.Scheme.iOS, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    /*tc : function(caller_url_scheme, jsonData) {
        if (_USUtil.OS.isAndroid()) {
            var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "json_protocol", jsonData);
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeFIDOScheme(this.Scheme.iOS, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },*/

    dereg : function(caller_url_scheme, appkey, jsonData) {
        if (_USUtil.OS.isAndroid()) {
			
			
			var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makeFIDOIntent(this.Scheme.Android, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
		
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makeFIDOScheme(this.Scheme.iOS, "FIDO",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    pinReg : function(caller_url_scheme, appkey, jsonData) {
        if (_USUtil.OS.isAndroid()) {
		
			var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
		
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makePINPADScheme(this.Scheme.iOS, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    pinModify : function(caller_url_scheme, appkey, jsonData) {
        if (_USUtil.OS.isAndroid()) {
		
			var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
		
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makePINPADScheme(this.Scheme.iOS, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    },

    pinDereg : function(caller_url_scheme, appkey, jsonData) {
        if (_USUtil.OS.isAndroid()) {
            var userAgent = _USUtil.Base64.encode(navigator.userAgent.toLowerCase());
		  
		    if(userAgent != null){
						
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), "userAgent", userAgent, 
					"language", "en", "json_protocol", jsonData);
			}else{
			
				var intent = _USUtil.makePINPADIntent(this.Scheme.Android, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
			}
		
            top.location.href = intent;
        } else if (_USUtil.OS.isiOS()) {
            var customScheme = _USUtil.makePINPADScheme(this.Scheme.iOS, "FIDO_PINPAD",
                    "caller_url_scheme", caller_url_scheme, "appkey", _USUtil.encodeUrl(appkey), 
					"language", "en", "json_protocol", jsonData);
            this.checkCrossCert(customScheme);
        } else {
            alert(_USMessage.NoticeUnsupportedOS);
        }
    }
};

// Utility for UniSign and VeriSign
var _USUtil = {
    OS : {
        UserAgent : {
            Android_Phone : "Android",
            Android_Pad : "Android",
            iPhone : "iPhone",
            iPad : "iPad"
        },

        isAndroid : function() {
            if (-1 < navigator.userAgent.indexOf(this.UserAgent.Android_Phone)) {
                return true;
            } else {
                return false;
            }
        },

        isiOS : function() {
            if ((-1 < navigator.userAgent.indexOf(this.UserAgent.iPhone))
                    || -1 < navigator.userAgent.indexOf(this.UserAgent.iPad)) {
                return true;
            } else {
                return false;
            }
        },

        isSupported : function() {
            if (this.isAndroid() || this.isiOS()) {
                return true;
            } else {
                return false;
            }
        }
    },

    makeIntent : function() {
        if (false === this.OS.isAndroid()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var intentScheme = "intent://" + arguments[1];
        var intentQuery = "";

        for (var i = 1; i * 2 < arguments.length; i++) {
            if (0 >= intentQuery.length) {
                intentQuery += "?" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            } else {
                intentQuery += "&" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            }
        }

        var intentAttribute = "";

        if (UniSignW2A.Scheme.Android === arguments[0]) {
            // intentAttribute = "#Intent;scheme=" + UniSignW2A.Scheme.Android +
            // ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
            // + UniSignW2A.Package.Android + ";end";
            intentAttribute = "#Intent;";
            intentAttribute = intentAttribute + "package="
                    + UniSignW2A.Package.Android + ";";
            intentAttribute = intentAttribute
                    + "action=android.intent.action.VIEW;";
            intentAttribute = intentAttribute
                    + "category=android.intent.category.BROWSABLE;";
//            intentAttribute = intentAttribute
//                    + "component="
//                    + "com.crosscert.android/com.crosscert.android.fido.FidoActivity"
//                    + ";";
            intentAttribute = intentAttribute + "scheme="
                    + UniSignW2A.Scheme.Android + ";";
            intentAttribute = intentAttribute + "end;";
        } else if (VeriSign.Scheme.Android === arguments[0]) {
            intentAttribute = "#Intent;scheme="
                    + VeriSign.Scheme.Android
                    + ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
                    + VeriSign.Package.Android + ";end";
        } else {
            return;
        }

        return intentScheme + intentQuery + intentAttribute;
    },

    makeFIDOIntent : function() {
        if (false === this.OS.isAndroid()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var intentScheme = "intent://" + arguments[1];
        var intentQuery = "";

        for (var i = 1; i * 2 < arguments.length; i++) {
            if (0 >= intentQuery.length) {
                intentQuery += "?" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            } else {
                intentQuery += "&" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            }
        }
        
        var intentAttribute = "";

        if (UniSignW2A.Scheme.Android === arguments[0]) {
            // intentAttribute = "#Intent;scheme=" + UniSignW2A.Scheme.Android +
            // ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
            // + UniSignW2A.Package.Android + ";end";
            intentAttribute = "#Intent;";
            intentAttribute = intentAttribute + "package="
                    + UniSignW2A.Package.Android + ";";
            intentAttribute = intentAttribute
                    + "action=android.intent.action.VIEW;";
            intentAttribute = intentAttribute
                    + "category=android.intent.category.BROWSABLE;";
//            intentAttribute = intentAttribute
//                    + "component="
//                    + "com.crosscert.android/com.crosscert.android.fido.FidoActivity"
//                    + ";";
            intentAttribute = intentAttribute + "scheme="
                    + UniSignW2A.Scheme.Android + ";";
            intentAttribute = intentAttribute + "end;";
			
        } else if (VeriSign.Scheme.Android === arguments[0]) {
            intentAttribute = "#Intent;scheme="
                    + VeriSign.Scheme.Android
                    + ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
                    + VeriSign.Package.Android + ";end";
        } else {
            return;
        }
       
        return intentScheme + intentQuery + intentAttribute;
    },

    makePINPADIntent : function() {
        if (false === this.OS.isAndroid()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var intentScheme = "intent://FIDO";
        var intentQuery = "";

        for (var i = 1; i * 2 < arguments.length; i++) {
            if (0 >= intentQuery.length) {
                intentQuery += "?product_type=" + arguments[1] + "&" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            } else {
                intentQuery += "&" + arguments[i * 2] + "="
                        + arguments[i * 2 + 1];
            }
        }
        
        var intentAttribute = "";

        if (UniSignW2A.Scheme.Android === arguments[0]) {
            // intentAttribute = "#Intent;scheme=" + UniSignW2A.Scheme.Android +
            // ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
            // + UniSignW2A.Package.Android + ";end";
            intentAttribute = "#Intent;";
            intentAttribute = intentAttribute + "package="
                    + UniSignW2A.Package.Android + ";";
            intentAttribute = intentAttribute
                    + "action=android.intent.action.VIEW;";
            intentAttribute = intentAttribute
                    + "category=android.intent.category.BROWSABLE;";
//            intentAttribute = intentAttribute
//                    + "component="
//                    + "com.crosscert.android/com.crosscert.android.fido.FidoActivity"
//                    + ";";
            intentAttribute = intentAttribute + "scheme="
                    + UniSignW2A.Scheme.Android + ";";
            intentAttribute = intentAttribute + "end;";
        } else if (VeriSign.Scheme.Android === arguments[0]) {
            intentAttribute = "#Intent;scheme="
                    + VeriSign.Scheme.Android
                    + ";action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package="
                    + VeriSign.Package.Android + ";end";
        } else {
            return;
        }
        
        return intentScheme + intentQuery + intentAttribute;
    },

    makeCustomScheme : function() {
        if (false === _USUtil.OS.isiOS()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var customScheme = arguments[0] + "://?cmd=" + arguments[1];
        var customSchemeQuery = "";
        // alert('customScheme : '+customScheme);
        for (var i = 1; i * 2 < arguments.length; i++) {
            customSchemeQuery += "&" + arguments[i * 2] + "="
                    + arguments[i * 2 + 1];
        }

        var customSchemeAttribute = "";

        return customScheme + customSchemeQuery + customSchemeAttribute;
    },

    makeFIDOScheme : function() {
        if (false === _USUtil.OS.isiOS()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var customScheme = arguments[0] + "://?product_type=" + arguments[1];
        var customSchemeQuery = "";
        // alert('customScheme : '+customScheme);
        for (var i = 1; i * 2 < arguments.length; i++) {
            customSchemeQuery += "&" + arguments[i * 2] + "="
                    + arguments[i * 2 + 1];
        }

        var customSchemeAttribute = "";

        return customScheme + customSchemeQuery + customSchemeAttribute;
    },

    makePINPADScheme : function() {
        if (false === _USUtil.OS.isiOS()) {
            return;
        }

        if (4 > arguments.length) {
            return;
        }

        var customScheme = arguments[0] + "://?product_type=" + arguments[1];
        var customSchemeQuery = "";
        // alert('customScheme : '+customScheme);
        for (var i = 1; i * 2 < arguments.length; i++) {
            customSchemeQuery += "&" + arguments[i * 2] + "="
                    + arguments[i * 2 + 1];
        }

        var customSchemeAttribute = "";

        return customScheme + customSchemeQuery + customSchemeAttribute;
    },

    getQueryVariable : function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            if (0 < vars[i].indexOf("=")) {
                if (variable == vars[i].substring(0, vars[i].indexOf("="))) {
                    return vars[i].substring(vars[i].indexOf("=") + 1,
                            vars[i].length);
                }
            }
        }
        return null;
    },

    encodeUrl : function(str) {
        return str.replace(/\+/g, '-').replace(/\//g, '_').replace(/\=+$/, '');
    },

    Base64 : {
        // private property
        _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
        // public method for encoding
        encode : function(input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = this._utf8_encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output + this._keyStr.charAt(enc1)
                        + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3)
                        + this._keyStr.charAt(enc4);
            }
            return output;
        },
        // public method for decoding
        decode : function(input) {
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
            while (i < input.length) {
                enc1 = this._keyStr.indexOf(input.charAt(i++));
                enc2 = this._keyStr.indexOf(input.charAt(i++));
                enc3 = this._keyStr.indexOf(input.charAt(i++));
                enc4 = this._keyStr.indexOf(input.charAt(i++));
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
                output = output + String.fromCharCode(chr1);
                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }
            }
            output = this._utf8_decode(output);
            return output;
        },
        // private method for UTF-8 encoding
        _utf8_encode : function(string) {
            string = string.replace(/\r\n/g, "\n");
            var utftext = "";
            for (var n = 0; n < string.length; n++) {
                var c = string.charCodeAt(n);
                if (c < 128) {
                    utftext += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                } else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
            }
            return utftext;
        },
        // private method for UTF-8 decoding
        _utf8_decode : function(utftext) {
            var string = "";
            var i = 0;
            var c = c1 = c2 = 0;
            while (i < utftext.length) {
                c = utftext.charCodeAt(i);
                if (c < 128) {
                    string += String.fromCharCode(c);
                    i++;
                } else if ((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i + 1);
                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                } else {
                    c2 = utftext.charCodeAt(i + 1);
                    c3 = utftext.charCodeAt(i + 2);
                    string += String.fromCharCode(((c & 15) << 12)
                            | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }
            }
            return string;
        }
    }
};
