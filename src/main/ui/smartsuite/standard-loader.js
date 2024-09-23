var StandardLoader = {
	
	debugMode: false,
	
	authKey: "isAuthenticated=true",
	
	/** sync가 아니면, 무조건 async */
	syncKey: "sync",
	
	isString: function(value){
		return typeof value == "string";
	},
	
	isObject: function(value){
		return value !== null && "object" === typeof value;
	},
	
	ready: function(scriptScheme, onLoad){
		if(scriptScheme.hasOwnProperty(StandardLoader.syncKey)){
			StandardLoader.sync(scriptScheme.sync, onLoad);
		}else{
			StandardLoader.async(scriptScheme.async || scriptScheme, onLoad);
		}
	},
	
	/** 순차적 동기 호출 후, 콜백 */
	sync: function(srcArr, onLoad, currentIdx){
		var idx = srcArr.length;
		if(!idx){
			onLoad && onLoad();
			return;
		}
		
		if(!currentIdx){
			currentIdx = 0;
		}
		var src = srcArr[currentIdx];	
		if((idx-1) == currentIdx){
			StandardLoader.loadWaiting(src, onLoad);	
		}else{
			currentIdx += 1;
			StandardLoader.loadWaiting(src, StandardLoader.sync.bind(this, srcArr, onLoad, currentIdx));	
		}
	},
	
	/** 비동기 호출 후, 콜백 */
	async: function(srcArr, onLoad){
		var totalCount = srcArr.length;
		if(!totalCount){
			onLoad && onLoad();
			return;
		}
		
		var onSyncLoad = function(_onLoad){
			totalCount = totalCount - 1;
			if(totalCount == 0){
				_onLoad && _onLoad();
			}
		}
		
		for(var i=0; i<srcArr.length; i++){
			StandardLoader.loadWaiting(srcArr[i], onSyncLoad.bind(this, onLoad));
		}
	},
	
	/** 일반 호출 후, 콜백 */
	loadWaiting: function(src, onLoad){
		if(this.isString(src)){
			var modifiedSrc = this.loadAuthFilter(src);
			if(modifiedSrc){
				var length = modifiedSrc.length;
				if(length == (modifiedSrc.indexOf(".html")+5)){
					this.loadHtml(modifiedSrc, onLoad);	
				}else if(length == (modifiedSrc.indexOf(".css")+4)){
					this.loadCss(modifiedSrc, onLoad);
				}else{
					this.loadJs(modifiedSrc, onLoad);
				}
			}else{
				console.warn("Didn't Request For URL("+src+") While 'SCPreloader Script Request'");
				onLoad && onLoad();
			}
		}else{
			if(this.isObject(src)){
				StandardLoader.ready(src, onLoad);
			}else{
				console.error("Not Supported Type While 'SCPreloader Script Request'");
			}
		}
	},
	
	loadCss: function(src, onLoad){
		var linkEl = document.createElement('link');
		linkEl.setAttribute('type', 'text/css');
		linkEl.setAttribute('rel', 'stylesheet');
		linkEl.setAttribute('href', src);
		this.load(linkEl, onLoad);
	},
	
	loadHtml: function(src, onLoad){
		var linkEl = document.createElement('link');
		linkEl.setAttribute('rel', 'import');
		linkEl.setAttribute('href', src);
		this.load(linkEl, onLoad);
	},
	
	/** 스크립트 호출 */
	loadJs: function(src, onLoad){
		var scriptEl = document.createElement('script');
		scriptEl.setAttribute('type', 'text/javascript');
		scriptEl.setAttribute('defer', 'true');
		scriptEl.setAttribute('src', src);
		this.load(scriptEl, onLoad);
	},
	
	load: function(element, onLoad){
		if (onLoad != null)
		{
			var readied = false;
			element.onload = element.onreadystatechange = function() {
				if (!readied && (!this.readyState || this.readyState == 'complete'))
				{
					if(this.debugMode){
						console.log((element.src || element.href) + " (Load Completed)");
					}
		      		readied = true;
		      		onLoad();
				}
		  	}.bind(this);
		}
	  	var headEl = document.querySelector('head');
	  	if (headEl != null)
	  	{
	  		headEl.appendChild(element);
	  	}
	},
	
	/** URL의 QueryString을 통해, 리소스 호출 여부 판단 */	
	loadAuthFilter: function(src){
		var originUrl = src.split("?")[0];
		var queryString = src.split("?")[1];
		if(queryString){
			var paramKeys = queryString.split(",");
			for(var i=0, key; i<paramKeys.length; i++){
				key = paramKeys[i];
				if(key == this.authKey && SCPreloader.isAuthenticated()){
					return originUrl;
				}else{
					return null;
				}
			}
			return originUrl;
		}else{
			return originUrl;
		}
	}
	
};






