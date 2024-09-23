<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="google" content="notranslate" />
<title>SMARTsuite 10.0</title>
<link href="css/login.css" rel="stylesheet" type="text/css" />
<%-- <spring:theme code="localization.javascript"/> --%>
    <script type="text/javascript">
		setTimeout(function(){
			redirectPage();
		}, 3000);
		function redirectPage(){
			// document.referrer 이 존재하고, loginFailure.do 를 통한 호출이 되었을 경우에만 이전 요청 url 로 redirect 한다.
			if(document.referrer && (window.location.pathname.indexOf("loginFailure.do") > -1) ){
				top.location.href = document.referrer;
			} else {
    			top.location.href = 'spLogin.do';
			}
		}
	</script>
</head>

<body>
	<div class="wrap_msg">
		<div class="container">
			<div class="section_upper">
				<div class="message fail">
					<b class="icon"></b>
					<b>로그인에 실패하였습니다.</b>
				</div>
				<div class="explan">아이디 또는 비밀번호를 다시 확인하시기 바랍니다.<br/>
					등록되지 않은 아이디이거나, 비밀번호를 잘못 입력하셨습니다.
				</div>
			</div>
			<div class="section_under">
				<p class="commt"><b>3초 후</b>에 로그인 페이지로 이동합니다.</p>
				<p class="btn_cnt"><input class="btn_msg" type="button" value="로그인 페이지 이동" onclick="redirectPage()"/></p>
			</div>
		</div>
	</div>
</body>
</html>