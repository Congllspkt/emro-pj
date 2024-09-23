<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Cache-control" content="no-cache" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0"/>
	<meta name="robots" content="noindex, nofollow">
	<meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
	
	<meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}" iteration="${_passwordSaltSource.iterationCount}"/>
	<meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
	<meta name="smartsuiteVariables" cache_bust="${_cacheBust}"
									 session_timeout="3600"
									 is_sso="false" 
									 locale="${pageContext.response.locale}"
									 use_error_user_message="false"
									 is_parameter_encrypt="${_secParam}"
									 session_usr_id="GUEST" />
									 
	<title>SMARTsuite 10.0</title>
	<sec:csrfMetaTags />
	
	<link rel="stylesheet" type="text/css" href="bower_components/sc-component/sc-elements.std.css">
	<link rel="stylesheet" type="text/css" href="ui/assets/css/common.css"/>
	<style type="text/css">
		#sign_container{
			margin:10px;
			height:95%
		}
		/** JSP Body Loading Bar Image */
		body.body-ready
		{
			background-image: url(ui/assets/img/body/body_loading.gif);
			background-repeat: no-repeat;
			background-position: center;
		}
	</style>
	
   	<script type="text/javascript">
		window.Polymer = {
			ccModuleBehaviorReady: true
		}
		window.anonymous = true;
		/*window.Polymer = {
   			//Polymer 엘리먼트 로드 완료 이벤트 설정
			polymerReady : true,
			//#다국어 처리 프로세서 비활성화
			useI18nParser : false,
			//캐시 버스트
			cacheBust : ('${_cacheBust}' || Date.now()),
			//lazy loader 활성화
			useLazyLoader : true,
			//lazy register 활성화
			useLazyRegister : true,
			//true 값이면 윈도우 숨김처리시 document.body 에서 윈도우 엘리먼트가 자동으로 제거
			detachedOnSCWindowHided : false,
			//소스 내 whitespace를 지워주는 기능 할성화
			useStripWhiteSpace : true,
			//cc-module-behavior 모듈 로드 완료 이벤트 설정
			ccModuleBehaviorReady : true,
			//ModuleBehavior 활성화
			useModuleBehavior : true
			
		};
		window.isParameterEncypt = ('${_secParam}' === "skip") ? false: true; //파라미터 암호화 처리 여부
		*/
		document.addEventListener('keydown',function(e){
			var ele = e.srcElement ? e.srcElement : e.target,
				rx = /INPUT|SELECT|TEXTAREA/i,
				k = e.which || e.keyCode;
			if(!ele.type || !rx.test(e.target.tagName)|| (ele.readOnly || ele.disabled )) {
				if(k == 8){
					e.preventDefault();
				}
			}
		},true);
		
		// 게시판 화면은 list, detail 화면이 lazy 동작으로 수행되므로 cc-module-behavior 가 필수 로딩 되어야 정상 동작합니다.
		// 따라서 importLink 시점을 cc-module-behavior 가 로딩 된 시점 이후에 수행합니다.
		// (ccModuleBehaviorReady 가 `true` 일 경우에만 `ccModuleBehaviorReady` 이벤트를 dispatch 합니다.)
		var ccModuleBehaviorHandler = function(e){
			document.removeEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);
			Polymer.Base.importLink('ui/bp/eform/popup/ep-eform-sign-mail.html', function(moduleId) {
				var module = document.createElement(moduleId);
				var params = {
						cntr_sgndusr_uuid : "${contractor_no}",
						mobileYn : "${mobileYn}",
				};
				module.params = params;
				Polymer.dom(document.querySelector('#sign_container')).appendChild(module);
				Polymer.dom.flush();
			});
		}
		
		document.addEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);
		
		document.addEventListener("close", function() {
			if(window.fire){
				window.fire("close");
			}else{
				window.close();
			}
		});
		
		var agent = navigator.userAgent.toLowerCase();
		var pdfViewerScriptNode = document.querySelector("script[src$='/pdf.min.js']");
		if (pdfViewerScriptNode === null || pdfViewerScriptNode === undefined) {
			if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
				// ie일 경우
				document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/es5/pdf.min.js"><\/script>');
				document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/es5/pdf_viewer.js"><\/script>');
			} else{
				// ie가 아닐 경우
				document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/pdf.min.js"><\/script>');
				document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/pdf_viewer.js"><\/script>');
			}
		}
	</script>
	<script src="bower_components/template-designer/js/pdf_form/dist/polly.bundle.js"></script>
	<script src="bower_components/webcomponentsjs/webcomponents-standard.min.js"></script>
</head>
<body class="body-ready">
	<div class="top_progress"></div>
	<script src="license.do"></script>
	<div id="sign_container"></div>
	<script src="ui/smartsuite/standard-loader.js"></script>
	<script src="ui/smartsuite/standard-resources.js"></script>
	<script src="ui/smartsuite/standard-setup.js"></script>
	<script src="ui/smartsuite/standard-util.js"></script>
</body>
</html>