/**
* SCSessionManager
*/
SCSessionManager = new(function() {
	
	var AJAXSETTINGS = SCPreloader.ajaxSettings(),
		CONVERT = SCPreloader.convert,
		IS_AUTHENTICATED = SCPreloader.isAuthenticated(); // 로그인 인증 여부 추가
	
  	function SessionManagerImpl() {
  		this._session();
  	}
  	
  	SessionManagerImpl.prototype = {
  		
  		model : {
			last_login_dt: "last_login_dttm",
			last_login_ip: "last_login_ip",
			usr_cls: "usr_typ_ccd",
			usr_id: "usr_id",
			usr_nm: "usr_nm",
			usr_en_nm: "usr_en_nm",
			comp_nm: "comp_nm",
			dept_nm: "dept_nm"
		},
  		
  		userDetails : {
  			userInfo : {}
  		},
  		
  		_callbacks : []
  		
  	};
  	
	/** 로그인 미인증 시, 디폴트 값 */
	SessionManagerImpl.prototype._anonymousSession = function() {
		this.userDetails = {
			userInfo : {usr_id : "GUEST"}
		};
		this.currentUser = this.userDetails.userInfo;
		this._complete();
		return true;
	};
	/** 로그인 인증 시, 공통 서비스(세션) 호출 */
	SessionManagerImpl.prototype._authenticatedSession = function() {
		return $.ajax('getSessionUser.do', AJAXSETTINGS).then(function(response) {
			return CONVERT(response);
		}).done(function(userDetails) {
			this.userDetails = userDetails;
			//타임존 설정
			var currentUser = userDetails.userInfo;
			if(currentUser != null && "undefined" != typeof currentUser ){
				var tmz_ccd = currentUser.tmz_ccd;
				if("string" === typeof tmz_ccd){
					window.Date = TimeShift.Date;   // 전체 타임존을 사용할경우 적용
					SCSessionManager.currentUser.last_login_dttm = new Date(SCSessionManager.currentUser.last_login_dttm);
					SCSessionManager.currentUser.pw_mod_dt = new Date(SCSessionManager.currentUser.last_login_dttm);
					// 분단위 전환
					var timezoneOffset = tmz_ccd * 60;
					TimeShift.setTimezoneOffset(-timezoneOffset);
				}
			}
			this._complete();
		}.bind(this)).fail(function() {
			console.error("사용자 정보 조회중 오류가 발생하였습니다.");
		});
	};

	SessionManagerImpl.prototype._session = IS_AUTHENTICATED ?
		SessionManagerImpl.prototype._authenticatedSession : SessionManagerImpl.prototype._anonymousSession;
	/*SessionManagerImpl.prototype._session = function() {
  		return $.ajax('getSessionUser.do', AJAXSETTINGS).then(function(response) {
			return CONVERT(response);
		}).done(function(userDetails) {
			this.userDetails = userDetails;
			//타임존 설정
			var currentUser = userDetails.userInfo;
  			var tmz_ccd = currentUser.tmz_ccd;
  			if("string" === typeof tmz_ccd){
  				window.Date = TimeShift.Date;   // 전체 타임존을 사용할경우 적용
  				SCSessionManager.currentUser.last_login_dttm = new Date(SCSessionManager.currentUser.last_login_dttm);
  				SCSessionManager.currentUser.pw_mod_dt = new Date(SCSessionManager.currentUser.last_login_dttm);
  				// 분단위 전환
  				var timezoneOffset = tmz_ccd * 60;
  				TimeShift.setTimezoneOffset(-timezoneOffset);
  			}
			this._complete();
		}.bind(this)).fail(function() {
			console.error("사용자 정보 조회중 오류가 발생하였습니다.");
		});
  	};*/
  	
  	SessionManagerImpl.prototype._complete = function(callback) {
		var callbacks = this._callbacks,
			fn;
		
		this._readied = true;
		//callback run
		while((fn = callbacks.shift(0))) {
			fn()
		}
	};
	
	SessionManagerImpl.prototype.loadSession = SessionManagerImpl.prototype._session; 
	
	SessionManagerImpl.prototype.onReady = function(callback) {
		if(this._readied) {
			return callback();
		}
		this._callbacks.push(callback);
	};
	
	SessionManagerImpl.prototype.isReady = function() {
		return !!this._readied;
	};
	
  	/**
   	* 'SCSessionManager' 의 내부 변수인 user에 입력된 데이터를 조회 합니다. 
   	* @return {Object} user
   	*/
  	SessionManagerImpl.prototype.getCurrentUser = function() {
	  	return this.userDetails.userInfo;
  	};
  	/**
   	* 'SCSessionManager' 의 내부 변수인 user에 입력된 데이터를 삭제 합니다. 
   	*/
  	SessionManagerImpl.prototype.destroy = function() {
  		this.userDetails = null;
  	};
  	/**
   	* 사용자의 패스워드 만료상태를 반환합니다. 
   	*/
  	SessionManagerImpl.prototype.isCredentialsNonExpired = function() {
  		return this.userDetails.credentialsNonExpired;
  	};
  	/**
  	* 사용자의 패스워드 초기화상태를 반환합니다.
  	*/
  	SessionManagerImpl.prototype.isCredentialsNonInitialized = function() {
  		return this.userDetails.credentialsNonInitialized;
  	};
  	/**
  	* 사용자의 패스워드 변경주기를 반환합니다.
  	*/
  	SessionManagerImpl.prototype.getPasswordExpiredPeriod = function() {
  		return this.userDetails.accountSettings.passwordExpiredPeriod;
  	};
  	/**
  	* 패스워드 규칙을 반환합니다.
  	*/
  	SessionManagerImpl.prototype.getPasswordRules = function() {
  		var accountSettings = this.userDetails.accountSettings;
  		
  		return {
  			minLengthRule : accountSettings.passwordMinLengthRule,
  			maxLengthRule : accountSettings.passwordMaxLengthRule,
  			alphabetCharacterRule : accountSettings.passwordAlphabetCharacterRule,
  			digitCharacterRule : accountSettings.passwordDigitCharacterRule,
  			sequenceCharacterRule : accountSettings.passwordSequenceCharacterRule,
  			specialCharacterRule : accountSettings.passwordSepecialCharacterRule,
  			repeatCharacterRule : accountSettings.passwordRepeatCharacterRule,
  			phoneNoDuplicateRule : accountSettings.passwordPhoneNoDuplicateRule
  		}
  	};
  	
  	SessionManagerImpl.prototype.isRoleAdmin = function() {
  		return (this.userDetails.authorities && Polymer.$Array.find(this.userDetails.authorities, function(role) {
  			return role.authority == 'ROLE_ADMIN';
  		}) != null);
  	};
  	
  	SessionManagerImpl.prototype.isAdminAdditionalAuthentication = function() {
  		return this.userDetails.accountSettings.adminAdditionalAuthentication;
  	};
  	
  	SessionManagerImpl.prototype.isDisableOnSpecifiedDate = function() {
  		return this.userDetails.accountSettings.disableOnSpecifiedDate;
  	};
  	
  	SessionManagerImpl.prototype.getAccountDisableUserNotifyDate = function() {
  		return this.userDetails.accountSettings.accountDisableUserNotifyDate;
  	};
  	
  	SessionManagerImpl.prototype.getAccountDisableForSpecifiedDate = function() {
  		return this.userDetails.accountSettings.accountDisableForSpecifiedDate;
  	};
  	
  	SessionManagerImpl.prototype.getAccountSettings = function() {
  		return this.userDetails.accountSettings;
  	};
  	
  	/**
   	* 'SCSessionManager' 의 내부 변수인 user에 입력된 데이터의 입력 및 조회를 합니다.
   	*/
  	Object.defineProperty(SessionManagerImpl.prototype, "currentUser", {
    	get: function() {
	     	return this.userDetails.userInfo;
    	}
  	});
  	return SessionManagerImpl;
}());