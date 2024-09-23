<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Cache-control" content="no-cache" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0"/>
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
    <meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}" iteration="${_passwordSaltSource.iterationCount}"/>
    <meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
    <title>SMARTsuite 10.0</title>
    <sec:csrfMetaTags />
    <link rel="stylesheet" type="text/css" href="bower_components/sc-component/sc-elements.build.css">
    <link rel="stylesheet" type="text/css" href="ui/ops/assets/css/ops.css"/>
    <link rel="stylesheet" type="text/css" href="ui/assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="ui/assets/css/external-common.css"/>
    <script type="text/javascript">
    	window.Polymer = {
    			//Polymer 엘리먼트 로드 완료 이벤트 설정
    		    polymerReady : true,
    		    //다국어 처리 프로세서 비활성화
    		    useI18nParser : false,
    		    //캐시 버스트
    		    cacheBust : ('${_cacheBust}' || Date.now()),   
    		    //지연로딩 활성화
    		    useLazyLoader : true,   
    		    //지연등록 활성화
    		    useLazyRegister : true,
    		    //dom-module 의 whitespace를 지워주는 기능 활성화
    		    useStripWhiteSpace : true,
    		    //소멸자 비동기 활성화
    		    useTerminatorAsyncTask : true,
    		 
    		    //※ sc-component-1.1.8 버전에 신규로 추가된 설정
    		    //sc-component-1.1.8 이전 버전에 대한 dom-module 호환성 제거
    		    useDomModuleCompatibility : false,
    		    //엘리먼트 최소화
    		    detachedOnElementHided : true,
    		    //노드탐색 최적화
    		    useUpgradeWithDefinition : true,   
    		    //Date 객체변환 최적화
    		    useISO8601TimezoneDesignatorWithColonSeperator : true,
    		    //SCButtonState mousedown, mouseup, click 이벤트 비활성화
    		    useButtonStateMouseEvent : false,   
    		    //엘리먼트 공유 스타일 캐시 사용
    		    useGlobalStyleCache : true,
    		    //프로퍼티 이펙트 최소화
    		    usePropertyEffectFilter : true,
    		    //sc-code 로딩순서 변경
    		    useCodePrefetch : true,
    		    //sc-link-lazy-mask target 설정 비활성화
    		    useLinkLazyMaskTarget : false,
    		    //컨테이너 엘리먼트의 지연렌더링 기능 비활성화
    		    useContainerTemplatizedRenderer : false
    		    //ModuleBehavior 활성화
    		    ,useModuleBehavior : true
    		    //팝업 자동소멸
    		    ,usePopupAutoDestroy: true
    	};
    	window.useErrorUserMessage = ('${useErrorUserMessage}' === "true") ? true: false; //에러 발생 시 사용자가 에러 정보를 입력하도록 할 것인지
		window.isParameterEncypt = ('${_secParam}' === "skip") ? false: true; //파라미터 암호화 처리 여부
  	</script>
  	<script src="bower_components/webcomponentsjs/webcomponents-standard.min.js"></script>
</head>
<body>
	<div class="top_progress"></div>
	<script src="license.do"></script>
	
	<!-- mdi 태그 선언 -->
	<sc-ops id="mdiMain"></sc-ops>
	
	<script type="text/javascript">
		var mdiMain = document.getElementById('mdiMain'),
		
		SCSessionManager = new(function() {
			function SessionManagerImpl() {
		  		this.userDetails = {
					userInfo : {usr_id : "GUEST"}
		  		};
		  		this.currentUser = {};
		  	}
		  	
		  	SessionManagerImpl.prototype.getCurrentUser = function() {
			  	return this.userDetails.userInfo;
		  	};
		  	
		  	return SessionManagerImpl;
		}());
		
		mdiMainCompletedHandler = function(e) {
			mdiMain.removeEventListener('mdi-manager-initialized', mdiMainCompletedHandler);
			mdiMain.mdiMainCompleted = true;
		};
		mdiMain.addEventListener('mdi-manager-initialized', mdiMainCompletedHandler);
		
		document.addEventListener('keydown',function(e){
			var ele = e.srcElement ? e.srcElement : e.target,
				rx = /INPUT|SELECT|TEXTAREA/i,
			    k = e.which || e.keyCode;
			if((!ele.type || !rx.test(e.target.tagName)|| (ele.readOnly || ele.disabled )) && ["true", ""].indexOf(ele.getAttribute("contenteditable")) === -1 ) {
	 	    	if(k == 8){
	 	    		e.preventDefault();
	 	     	}
	 	     }
		},true);
	    
	</script>
	<link rel="import" href="bower_components/sc-component/sc-elements.std.html">
	<link rel="import" href="ui/override.html">
	<link rel="import" href="ui/smartsuite/preloader/sc-preloader.html" locale="${pageContext.response.locale}" anonymous="true">
	<link rel="import" href="ui/ops/sc-ops.html">
</body>
</html>