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
	<title>AnySignCloud :: 간편서명플랫폼</title>
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
//  			var userId = $('#userId').val();
 			var userPwd = $('#userPwd').val();
 			
//  			$('#userId').val($('#userId').val().toUpperCase());
 			
 			//아이디 대문자치환
//  			$('#username').val(userId.toUpperCase());
 			
 			//암호화
 			$('#password').val(PasswordEncryptor.encryptpw(userPwd));
 			return true;
 		}
 		
 		//인증요청
 		function loginProcess() {
 			$('#loginForm').submit();
 		}
 	</script>
</head>
	<body class="login_bg">
		<form id="loginForm" action="/eformSignPwLoginProcess.do" method="post" onsubmit="return submitLogin();">
			<input type="hidden" name="password" id="password"/>
			<input type="hidden" name="currentDevice" value="mobile"/>
			<input type="hidden" name="param" value="${eformSignParam}" />
			<input type="hidden" name="mobileYn" value="Y" />
			<input type="hidden" name="<c:out value='${_csrf.parameterName}' />" value="<c:out value='${_csrf.token}' />" />
			<input name="exceptionIfMaximumExceeded"  type="hidden" value="true">
		</form>
		<div id="page-wrapper">
			<!-- login -->
			<section id="banner">
				<div class="content">
					<header>
						<h2><img src="/resources/mobile/images/ci.png" /></h2>
						<h3>전자서명플랫폼</h3>
						<ul class="login_b">
                          <li><input type="password" name="userPwd" id="userPwd" value="" placeholder="Password" maxlength="40" class="cv-required" cv-required-msg="비밀번호는 필수입력항목입니다."></li>
		                  <li class="btnLog"><a class="btnLogin" href="javascript:loginProcess();">인증하기</a></li>
		               </ul>
					</header>
				</div>
			</section>
		</div>
	</body>
</html>
