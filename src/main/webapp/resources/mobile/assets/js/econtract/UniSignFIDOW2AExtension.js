////////////////////////////////////////////////////////////////////////////////
//	JavaScript Utility
//	Only use for UniSign for SmartPhone or UniSign for VeriSign
////////////////////////////////////////////////////////////////////////////////

// Extension API for UniSign
UniSignW2A.getLicenseInfoWithProcURL = function (retURL, procURL) {
	if (_USUtil.OS.isAndroid()) {
		var intent = _USUtil.makeIntent(this.Scheme.Android, "licenseinfo", "requestCode", "2", "retURL", retURL, "procURL", procURL);
		top.location.href = intent;
	} else if (_USUtil.OS.isiOS()) {
		var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "LicenseInfo", "caller_url_scheme", retURL, "callback", "01", "procURL", procURL);
		this.checkCrossCert(customScheme);
	} else {
		alert(_USMessage.NoticeUnsupportedOS);
	}
};

UniSignW2A.esignWithProcURL = function (retURL, appkey, data, rtnParam, sidOption, sidValue, procURL) {
	if (_USUtil.OS.isAndroid()) {
		var intent = _USUtil.makeIntent(this.Scheme.Android, "esign", "requestCode", "8", "retURL", retURL, "appkey", appkey, "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam, "sidOption", sidOption, "sidValue", sidValue, "procURL", procURL);
		top.location.href = intent;
	} else if (_USUtil.OS.isiOS()) {
		var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "SignedData", "caller_url_scheme", retURL, "appkey", appkey, "data", _USUtil.Base64.encode(data), "callback", rtnParam, "sidOption", sidOption, "sidValue", sidValue, "procURL", procURL);
		this.checkCrossCert(customScheme);
	} else {
		alert(_USMessage.NoticeUnsupportedOS);
	}
};

UniSignW2A.esignWithEncVID = function (retURL, data, rtnParam) {
	if (_USUtil.OS.isAndroid()) {
		var intent = _USUtil.makeIntent(this.Scheme.Android, "esignWithEncVID", "requestCode", "9", "retURL", retURL, "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam);
		top.location.href = intent;
	} else if (_USUtil.OS.isiOS()) {
		var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "SignedDataWithEncVID", "caller_url_scheme", retURL, "data", _USUtil.Base64.encode(data), "callback", rtnParam);
		this.checkCrossCert(customScheme);
	} else {
		alert(_USMessage.NoticeUnsupportedOS);
	}
};

UniSignW2A.backupStore = function (retURL, caller, p12, p12key, option, rtnParam) {
	if (_USUtil.OS.isAndroid()) {
		var intent = _USUtil.makeIntent(this.Scheme.Android, "backupStore", "requestCode", "8", "retURL", retURL, "caller", caller, "p12", p12, "p12key", p12key, "option", option, "rtnParam", rtnParam);
		top.location.href = intent;
	} else if (_USUtil.OS.isiOS()) {
		var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "BackupStore", "caller_url_scheme", retURL, "caller", caller, "p12", p12, "p12key", p12key, "option", option, "callback", rtnParam);
		this.checkCrossCert(customScheme);
	} else {
		alert(_USMessage.NoticeUnsupportedOS);
	}
};

UniSignW2A.backupLoad = function (retURL, caller, option, rtnParam) {
	if (_USUtil.OS.isAndroid()) {
		var intent = _USUtil.makeIntent(this.Scheme.Android, "backupLoad", "requestCode", "8", "retURL", retURL, "caller", caller, "option", option, "rtnParam", rtnParam);
		top.location.href = intent;
	} else if (_USUtil.OS.isiOS()) {
		var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "BackupLoad", "caller_url_scheme", retURL, "caller", caller, "option", option, "callback", rtnParam);
		this.checkCrossCert(customScheme);
	} else {
		alert(_USMessage.NoticeUnsupportedOS);
	}
};


//API for VeriSign
var UniSignFP = {
		Scheme : {
			iOS : "unisign-app",
			Android : "crosscert"
		},
		
		Package : {
			Android : "com.crosscert.androidfp"
		},

		DownloadURL : {
			iOS : "https://itunes.apple.com/kr/app/gong-in-injeungsenteo/id426081742?mt=8",
			Android : "market://details?id=com.crosscert.android"
		},
		
		AutoCheckInstallation : true,
		
		checkCrossCert : function (uri) {
			if(_USUtil.OS.isAndroid()) {
				alert(_USMessage.NoticeDownload);
				top.location.href = _USUtil.makeIntent(this.Scheme.Android, "init", "isInit", "true");
			} else if(_USUtil.OS.isiOS()) {
				if(false == this.AutoCheckInstallation && null != uri && 0 != uri.length) {
					top.location.href = uri;
					return;
				}

				if(null == uri || 0 == uri.length) {
					uri = this.Scheme.iOS + '://?cmd=Main&caller_url_scheme=' + location.href + '&callback=01';
				}

				var clickedAt = +new Date;

//				var checkTimer = 
				setTimeout(function() {
					if((+new Date - clickedAt) < 1600) {
//						var ret = confirm(_USMessage.ConfirmDownload);
//						console.log('confirm result : ' + ret);

//						if(true == ret) {
//						if(confirm(_USMessage.ConfirmDownload)) {
							UniSign.moveToStore();
//						} else {
							
//							console.debug('confirm cancel');
//						}
					}
				}, 1500);

				top.location.href = uri;
			} else {
				alert(_USMessage.NoticeUnsupportedOS);
			}
		},
		
		moveToStore : function () {
			if(_USUtil.OS.isAndroid()) {
				top.location.href = this.DownloadURL.Android;
			} else if(_USUtil.OS.isiOS()) {
				top.location.href = this.DownloadURL.iOS;
			} else {
				alert(_USMessage.NoticeUnsupportedOS);
			}
		},

		getLicenseInfo : function (retURL) {
			if(_USUtil.OS.isAndroid()) {
				var intent = _USUtil.makeIntent(this.Scheme.Android, "licenseinfo", "requestCode", "2", "retURL", retURL);
				console.debug('getLicenseInfo : ' + intent);
				top.location.href = intent;
			} else if(_USUtil.OS.isiOS()) {
				var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "LicenseInfo", "caller_url_scheme", retURL, "callback", "01");
				console.debug('getLicenseInfo : ' + customScheme);
				this.checkCrossCert(customScheme);
			} else {
				alert(_USMessage.NoticeUnsupportedOS);
			}
		},
		
		manageCert : function(retURL, rtnParam) {
			if(_USUtil.OS.isAndroid()) {
				var intent = _USUtil.makeIntent(this.Scheme.Android, "mngmt", "requestCode", "1", "retURL", retURL, "rtnParam", rtnParam);
				console.debug('manageCert : ' + intent);
				top.location.href = intent;
			} else if(_USUtil.OS.isiOS()) {
				var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "ManageCert", "caller_url_scheme", retURL, "callback", rtnParam);
				console.debug('manageCert : ' + customScheme);
				this.checkCrossCert(customScheme);
			} else {
				alert(_USMessage.NoticeUnsupportedOS);
			}
		},

		esign : function(retURL, appkey, data, rtnParam, sidOption, sidValue) {
			if(_USUtil.OS.isAndroid()) {
				var intent = _USUtil.makeIntent(this.Scheme.Android, "esign", "requestCode", "8", "retURL", retURL, "appkey", appkey, "data", _USUtil.Base64.encode(data), "rtnParam", rtnParam, "sidOption", sidOption, "sidValue", sidValue);
				console.debug('esign : ' + intent);
				top.location.href = intent;
			} else if(_USUtil.OS.isiOS()) {
				var customScheme = _USUtil.makeCustomScheme(this.Scheme.iOS, "SignedData", "caller_url_scheme", retURL, "appkey", appkey, "data", _USUtil.Base64.encode(data), "callback", rtnParam, "sidOption", sidOption, "sidValue", sidValue );
				console.debug('esign : ' + customScheme);
				this.checkCrossCert(customScheme);
			} else {
				alert(_USMessage.NoticeUnsupportedOS);
			}
		}
};