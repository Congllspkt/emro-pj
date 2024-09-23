<%@ page contentType="text/html; charset=UTF-8" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="google" content="notranslate" />
	<title>SMARTsuite 10.0</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/login.css" />
	<%-- <spring:theme code="localization.javascript"/> --%>
	<script type="text/javascript">
		setTimeout(function () {
			redirectPage();
		}, 3000);
		function redirectPage() {
			if(document.referrer && (window.location.pathname.indexOf("spLoginProcess.do") > -1) ){
				top.location.href = document.referrer;
			} else {
				top.location.href = 'login.do';
			}
		}	
	</script>
</head>

<body>
	<div class="wrap_msg">
		<div class="container width_540">
			<div class="section_upper">
				<div class="message error">
					<b>400</b><b>에러<br/>Bad Request</b>
				</div>
				<div class="explan">서버가 클라이언트 오류로 인해 요청을 처리 할 수 없거나 처리하지 않습니다.<br/>
					The server cannot or will not process the request, due to a client error.
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