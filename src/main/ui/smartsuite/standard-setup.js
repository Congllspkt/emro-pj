

/** 전역설정 */
var smartsuiteVariables = document.querySelector("meta[name=smartsuiteVariables]"); /** JSP의 Meta 태그 */
/** 세션 타임아웃 설정 		*/ window.SESSIONTIMEOUT = Number(smartsuiteVariables.getAttribute("session_timeout")) * 1000; 
/** SSO 여부 				*/ window.isSso = (smartsuiteVariables.getAttribute("is_sso") === "true") ? true: false; 
/** 사용자 에러 정보 입력 	*/ window.useErrorUserMessage = (smartsuiteVariables.getAttribute("use_error_user_message") === "true") ? true: false;  
/** 파라미터 암호화 처리 		*/ window.isParameterEncypt = (smartsuiteVariables.getAttribute("is_parameter_encrypt") === "true")? true : false;
/** 세션 User 정보 		*/ window.session_usr_id = smartsuiteVariables.getAttribute("session_usr_id"); 
/** 세션 Locale 정보 		*/ window.session_locale = smartsuiteVariables.getAttribute("locale"); 


/** Polymer 설정 */
window.Polymer = Object.assign({
    /** 다국어 프로세서 활성화 	*/ useI18nParser : false,
 	/** 캐시 버스트 			*/ cacheBust : ((smartsuiteVariables && smartsuiteVariables.getAttribute("cache_bust")) || Date.now()), 
    /** 지연로딩 활성화 		*/ useLazyLoader : true,
    /** 지연등록 활성화			*/ useLazyRegister : true,
    /** 소멸자 비동기 활성화		*/ useTerminatorAsyncTask : true,
    /** 엘리먼트 최소화			*/ detachedOnElementHided : true,
    /** Date 객체변환 최적화	*/ useISO8601TimezoneDesignatorWithColonSeperator : true,
    /** 엘리먼트 스타일 공유 캐시	*/ useGlobalStyleCache : true,
    /** 프로퍼티 이펙트 최소화	*/ usePropertyEffectFilter : true,
    /** sc-code 로딩순서 변경	*/ useCodePrefetch : true,
    /** ModuleBehavior 활성화	*/ useModuleBehavior : true,
    /** 팝업 자동소멸			*/ usePopupAutoDestroy: true,
    /** 모든 버튼 사이즈 고정		*/ button : {
									// fixedSizeWidth: '100px',
									fixedSizeHeight : '28px'
								},
	/** 다국어 로컬 스토리지 리셋 */ i18nLocalStorageClearDateTime : '2023-12-06 15:12:00' 
}, window.Polymer);


/** UI Framework Resource Load */
StandardLoader.ready(StandardResources['smartsuite-core'], function(){
	console.info("Smartsuite UI Framework Core Loaded.")
});