
/**
 *  1. SCPreloader
 *  2. PreloaderLifeCycle, PreloaderLifeCycleCallback
 *  3. RequestSettings
 *  4. ModuleLoader
 *  5. ScriptLoader
 */

/** 1. SCPreloader의 인스턴스 생성 */
window.newPreloaderInstance = function(preloader, thisDoc, thatDoc){

	preloaderDebugMode = preloader.debugMode;
	
	/** sc-preloader내의 document */
	preloader.thisDoc = thisDoc;
	
	/** sc-preloader가 선언된 document */
	preloader.thatDoc = thatDoc;
	
	/** Main Module 리소스 준비 (MDI, SDI) */
	preloader.preinitializeScripts = StandardResources['smartsuite-modules'].preinitialize;
	
	/** Business Module 리소스 준비 (EM-, ES-) */
	preloader.initializeScripts = StandardResources['smartsuite-modules'].initialize;
	
	/** Application 사용 준비 완료 */
	preloader.applicationCompleteScripts = StandardResources['smartsuite-modules'].applicationComplete;
	
	preloader._callbacks = [];
	preloader._moduleCallbacks = [];
	preloader._moduleMappging = {};
	
	/** [로그인 인증 여부] 인증을 통한, 공통 서비스 호출 여부 판단시 사용 */
	preloader.isAuthenticated = function(){
		return !(window.anonymous == true);
	};

	preloader.get = function(attrKey){
		return this.link.getAttribute(attrKey);
	};

	preloader.onReady = function(callback) {
		if(this._readied && this._readiedAppManager) {
			return callback();	
		}
		this._callbacks.push(callback);
	};
	
	/** 모듈(메뉴) 호출 시 */
	preloader.onModuleReady = function(callback) {
		if(this._moduleReadied) {
			return callback();
		}
		this._moduleCallbacks.push(callback);
	};
	
	preloader.onCommonServiceReady = function(callback) {
		if(window.SCLocaleManager && window.SCMenuManager && window.SCFavoriteManager) {
			return callback();
		}else{
			setTimeout(function(_callback){
				this.onCommonServiceReady(_callback);
			}.bind(this, callback), 100);
		}
	};
	
	return new(document.registerElement('sc-preloader', {
		prototype : Object.assign(preloader,
						window.RequestSettings, 
						window.PreloaderLifeCycle,
						window.ModuleLoader)
	}));
}

/** 2. SCPreloader 라이프사이클 펑션 */
window.PreloaderLifecycleCallback = {

	isReadied : {
		preinitialize: false,
		initialize: false,
		applicationComplete: false
	},
	
	/** [라이프사이클] 호출 콜백 함수 */
	lifeCycleCallback : {
		preinitializeFns : [],
		initializeFns : [],
		applicationCompleteFns : []
	},
	
	/** 콜백 함수 등록(preinitialize) */
	onPreinitialize: function(callbackFn){
		this.isReadied.preinitialize ? 
			callbackFn() : 
			(this.lifeCycleCallback.preinitializeFns.push(callbackFn))
	},
	
	/** 콜백 함수 등록(initialize) */
	onInitialize: function(callbackFn){
		this.isReadied.initialize ? 
			callbackFn() : 
			(this.lifeCycleCallback.initializeFns.push(callbackFn))
	},
	
	/** 콜백 함수 등록(applicationComplete) */
	onApplicationComplete: function(callbackFn){
		this.isReadied.applicationComplete ? 
			callbackFn() : 
			(this.lifeCycleCallback.applicationCompleteFns.push(callbackFn)) 
	},
	
	/** 콜백 함수 호출(preinitialize) */
	executePreinitializeCallback: function(){
		this.executeCallbacks(this.lifeCycleCallback.preinitializeFns);
		this.lifeCycleCallback.preinitializeFns = [];
	},
	
	/** 콜백 함수 호출(initialize) */
	executeInitializeCallback: function(){
		this.executeCallbacks(this.lifeCycleCallback.initializeFns);
		this.lifeCycleCallback.initializeFns = [];
	},
	
	/** 콜백 함수 호출(applicationComplete) */
	executeApplicationCompleteCallback: function(){
		this.executeCallbacks(this.lifeCycleCallback.applicationCompleteFns);
		this.lifeCycleCallback.applicationCompleteFns = [];
	},
	
	executeCallbacks: function(fns){
		for(var i=0; i<fns.length; i++){
			fns[i]();
		}
	},
	
	preinitializeCompleted: function(){
		this.isReadied.preinitialize = true;
		this.executePreinitializeCallback();
	},
	
	initializeCompleted: function(){
		this.isReadied.initialize = true;
		this.executeInitializeCallback();
	},
	
	applicationCompleted: function(){
		this.isReadied.applicationComplete = true;
		this.executeApplicationCompleteCallback();
	}
};
window.PreloaderLifeCycle = Object.assign({

	mainModuleDelimeter: "[main-module]",

	/** (createdCallback:예약어) sc-preloader 커스텀 엘리먼트 준비 */
	createdCallback: function(){
		this.preinitialize.call(this);
	},
	
	initializeMain: function(){
		this.preinitializeCompleted();
		$.when(true)
			.then(this.initialize.bind(this))
			.then(this.initializeSucceed.bind(this))
			.fail(this.initializeFailed.bind(this));
	},
	
	/** 초기화 성공 */
	initializeSucceed: function(){
		console.info("[Step 2/3] Lazy Modules Register Completed");
		this.dispatchEvent(new Event('preloader completed'));
	},
	
	/** 초기화 실패 */
	initializeFailed: function(){
		console.info("Preload Failed");
		alert("There is an error on login. Contact to administrator.");
		Polymer.Base.reload();
	},
	
	/** 초기화 과정(Main Module 리소스) */
	readyMainModuleResources: function(){
		this.link = this.thatDoc.querySelector('link[href*="sc-preloader.html"]');
		SCI18nManager.setLocale(window.session_locale);
		StandardLoader.ready(
			this.preinitializeScripts, 
			function(){
				console.info("[Step 1/3] Common Service(Language, Session, Role, Menu) Completed");
				this.initializeMain.call(this);
			}.bind(this));
	},
	
	/** 초기화 과정(Business Module 리소스) */
	readyBusinessModuleResources: function() {
		CCHierachicalData = SCHierachicalData;
		this._readied = true;
		setTimeout(function(){
			StandardLoader.ready(
				this.initializeScripts, 
				function(){
					this.initializeCompleted();
					this.loadLazyScriptComplete();
				}.bind(this));
			this.lazyLink.call(this, this.thisDoc);
		}.bind(this), 1);
		
	},
	
	loadLazyScriptComplete: function(){
		var callbacks = this._callbacks, fn;
		while((fn = callbacks.shift(0))) {
			fn()
		}
		setTimeout(function(){
			this.waitingApplicationComplete();
		}.bind(this), 100);
	},
	
	/** 초기화 완료 후, Application Complete 호출 */
	waitingApplicationComplete: function(){
		var mainModule = document.querySelector(this.mainModuleDelimeter);
		
		/** Main Module(MDI / SDI) */
		if(mainModule && this.applicationCompleteScripts){
			if(!mainModule.__attached){
				setTimeout(function(){
					this.waitingApplicationComplete();
				}.bind(this), 100);
			}else{
				this.asyncScript.call(this, this.applicationCompleteScripts, function(){
					SCSessionManager.onReady(
						SCMenuManager.onReady.bind(this, function(){
							this.applicationComplete && this.applicationComplete();
							this.applicationCompleted();
							console.info("[Step 3/3] Create Application Completed");
						}.bind(this))
					);
				}.bind(this));
			}
		}else{
			console.warn("There is no 'main-module' attribute. Check the properties of 'sc-mdi' or 'sc-sdi'");
		}
	}
}, window.PreloaderLifecycleCallback);

/** 3. 서비스 요청 설정 */
window.RequestSettings = {
	
	/** (Date Type) String 객체 -> Date 객체 */
	convert: function(value){
		var type = typeof value;
		switch(type){
			case 'object':
				for(var key in value){
				value[key] = RequestSettings.convert(value[key]);
			}
			break;
			case 'string':
			if(value.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}(Z|[+-](\d{2}:?\d{2}))?$/)){
				value = value.replace(/([+-])(\d{2})(\d{2})/, function(m,p1,p2,p3){
				 	return p1+p2+":"+p3;
				});
				value = new Date(Date.parse(value));   	  				
			}
			break;
		}
		return value;
	},
	
	/** 서버 요청에 사용될 CSRF 토큰 */
	csrf: function(){
		var doc = document,
			meta = doc.querySelector('meta[name=_csrf]'), _csrf;
		
		if(meta) {
			_csrf = {
				csrf : meta.content,
				csrfHeader : doc.querySelector('meta[name=_csrf_header]').content,
				csrfParameter : doc.querySelector('meta[name=_csrf_parameter]').content
			}
		}
		return _csrf;
	},
	
	/** (sc-ajax 생성 전) jquery를 활용한 ajax요청 시 사용 될 공통옵션(헤더) */
	ajaxSettings: function(){
		var settings = {
			method : 'POST',
			dataType : 'json',
			contentType: "application/json",
			headers : {}
		};
		if(csrf) {
			settings.headers[csrf.csrfHeader] = csrf.csrf;
		}
		return settings;
	},
	
	contextPath: function(){
		var pathname = location.pathname;
		 if(pathname.indexOf('.do') > -1) {
			 pathname = pathname.substring(0, pathname.lastIndexOf('/') + 1);
		 }
		 return location.host + pathname;
	}
}

/** 4. sc-preloader의 선언되어 있는 sc-link와 script Lazy 호출합니다. */
window.ModuleLoader = {
	asyncScript: function(scriptScheme, onLoad){
		StandardLoader.ready(scriptScheme, onLoad);
	}, lazyLink : function(thisDoc) {
		var mdi = document.querySelector('#mdiMain'),
			load = (function(mdi) {
				var progress = document.querySelector('div.top_progress'),
					progressHandler = function() {
						count++;
						this.style.transform = this.style.webkitTrans = 'scaleX(' + (count / total) + ')';
					}.bind(progress),
					taskList = (function() {
						var portalType = this.link.getAttribute('portal-type'),
							links = thisDoc.querySelectorAll('sc-link:not([lazy])'),
							scripts = Polymer.dom(thisDoc.body).queryDistributedElements('sc-script');
							tasks = [];
							for(var i=0,len=scripts.length; i<len; i++) {
								if(scripts[i].hasAttribute('vue') && portalType !== 'vueGrid') {
									continue;
								}
								tasks.push(function(script) {
									return function() {
										script.load().then(progressHandler);
									}
								}(scripts[i]));
							}
							for(var i=0,len=links.length; i<len; i++) {
								tasks.push(function(link) {
									return function() {
										link.load().then(progressHandler);
									}
								}(links[i]));
							}
						return tasks;
					}.bind(this)()),
					total = taskList.length,
					count = 0;
				
				function processTaskList() {
					var taskStartTime = window.performance.now();
					var taskFinishTime;
					do {
						var nextTask = taskList.shift();
						if(nextTask) {
							nextTask();
						}
						taskFinishTime = window.performance.now();
					}
					while(taskFinishTime - taskStartTime < 3);
					if(taskList.length > 0) {
						requestAnimationFrame(processTaskList.bind(this));
					}
					else {
						var callbacks = this._moduleCallbacks;
						this._moduleReadied = true;
						while((fn = callbacks.shift(0))) {
							fn()
						}
						requestAnimationFrame(function() {
							this.style.opacity = '0';
						}.bind(progress));
					}
				};
				requestAnimationFrame(processTaskList.bind(this));
			}.bind(this, mdi));
			
		if(!mdi || (mdi && mdi.mdiInitialized)) {
			load();
		}
		else {
			var mdiInitializedHandler = function(event) {
				event.target.removeEventListener('mdi-initialized', mdiInitializedHandler);
				load();
				mdiInitializedHandler = null;
			};
			mdi.addEventListener('mdi-initialized', mdiInitializedHandler);
		}
	}
}

