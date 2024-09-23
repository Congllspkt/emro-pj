<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!--
2021.05.21 nami Kim
AnySignCloud 모바일 : 로그인
 -->
<!DOCTYPE HTML>
<html>
<head>
	<meta charset="UTF-8">
	<!-- Mobile Metas -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=medium-dpi">
	<!-- Mobile 자동전화 방지 -->
	<meta name="format-detection" content="telephone=no" />
    <meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}" iteration="${_passwordSaltSource.iterationCount}"/>
    <meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
	<title>SMARTsuite 10.0</title>
	<link href="/resources/mobile/css/common.css" type="text/css" rel="stylesheet">
	<link href="/resources/mobile/css/mobile.css" type="text/css" rel="stylesheet">
	<link href="/resources/mobile/css/modal.css"  type="text/css" rel="stylesheet">
	<!-- Scripts -->
	<script src="/resources/mobile/assets/js/jquery.min.js"></script>
	<script src="/resources/mobile/assets/js/jquery.scrolly.min.js"></script>
	<script src="/resources/mobile/assets/js/jquery.dropotron.min.js"></script>
	<script src="/resources/mobile/assets/js/jquery.scrollex.min.js"></script>
	<script src="/resources/mobile/assets/js/skel.min.js"></script>
	<script src="/resources/mobile/assets/js/util.js"></script>
	<!--[if lte IE 8]><script src="/resources/mobile/assets/js/ie/respond.min.js"></script><![endif]-->
	<script src="/resources/mobile/assets/js/main.js"></script>

	<script src="/resources/mobile/assets/js/jquery.validate.js"></script>
	<script src="/resources/mobile/assets/js/core.common.js"></script>
	<script src="/resources/mobile/assets/js/core.form.js"></script>

	<script type="text/javascript" src="/bower_components/crypto-js/crypto-js.js"></script>
	<script type="text/javascript" src="/bower_components/password-encryptor/password-encryptor.min.js"></script>
	
 	<script type="text/javascript">
 	
 		var validator = null;
 		$(document).ready(function(){
 			validator = coreForm.validate('loginForm');
 			$('#userId').focus();
 			window.isParameterEncypt = ('${_secParam}' === "skip") ? false: true; //파라미터 암호화 처리 여부
 		});
 		
 		//인증요청
 		function submitLogin() {
 			var userId = $('#userId').val();
 			var userPwd = $('#userPwd').val();
 			
 			$('#userId').val($('#userId').val().toUpperCase());
 			
 			//아이디 대문자치환
 			$('#username').val(userId.toUpperCase());
 			//암호화
 			$('#password').val(PasswordEncryptor.encryptpw(userPwd));
 			return true;
 		}
 		
 		//인증요청
 		function loginProcess() {
 			// 강제 로그인 기능을 부여하려면 로그인 form 태그의 exceptionIfMaximumExceeded input 태그의 값 false 로 변경하면 됩니다.
 			//document.loginForm.exceptionIfMaximumExceeded.value = false;
 			$('#loginForm').submit();
 		}
 	</script>
</head>
	<body class="login_bg">
		<form id="loginForm" action="/loginProcess.do" method="post" onsubmit="return submitLogin();">
			<input type="hidden" name="exceptionIfMaximumExceeded" value="false">
			<input type="hidden" name="username" id="username"/>
			<input type="hidden" name="password" id="password"/>
			<input type="hidden" name="currentDevice" value="mobile"/>
			<input type="hidden" name="<c:out value='${_csrf.parameterName}' />" value="<c:out value='${_csrf.token}' />" />
            <input type="hidden" name="menuId" value="${menuId}" /> 
            <input type="hidden" name="appId" value="${appId}" />
            <input type="hidden" name="usrCls" value="${usrCls}"/>
            <input type="hidden" name="param" value="${param}" />
            <input type="hidden" name="mobileYn" value="Y" />
		</form>
		<div id="page-wrapper">
			<!-- login -->
			<section id="banner">
				<div class="content">
					<header>
						<h2><img src="/resources/mobile/images/ci.png" /></h2>
						<h3>간편서명</h3>
						<ul class="login_b">
                          <li><input type="text" name="userId" id="userId" value="" placeholder="ID" maxlength="40" class="cv-required" cv-required-msg="아이디는 필수입력항목입니다." onkeydown="coreCommon.onKeyId(this);" ></li>
                          <li><input type="password" name="userPwd" id="userPwd" value="" placeholder="Password" maxlength="40" class="cv-required" cv-required-msg="비밀번호는 필수입력항목입니다."></li>
		                  <li class="btnLog"><a class="btnLogin" href="javascript:loginProcess();">로그인</a></li>
		               </ul>
					</header>
				</div>
			</section>
		</div>
	    <c:if test="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] != null}">
	        <!-- 중복 로그인 처리 -->
	        <spring:eval var="sessionMaximumExceeded" expression="sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] instanceof T(org.springframework.security.web.authentication.session.SessionAuthenticationException)" />
	        <c:if test="${sessionMaximumExceeded}">
	            <script type="text/javascript">
	                //사용자에게 오류 메세지 전달 후 강제 로그인 기능을 부여하려면 로그인 form 태그의 exceptionIfMaximumExceeded input 태그의 값 false 로 변경하면 됩니다.
	                if(confirm("로그인된 사용자가 존재합니다!\n기존 로그인을 로그아웃 시키겠습니까?") ){
	                    document.loginForm.exceptionIfMaximumExceeded.value = false;
	                    alert("다시 로그인을 진행 해 주십시요.");
	                }
	            </script>
	        </c:if>
	        <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
	        <c:remove var="AUTHENTICATION_FAILURE_MESSAGE" scope="session" />
	    </c:if>
	</body>
</html>
