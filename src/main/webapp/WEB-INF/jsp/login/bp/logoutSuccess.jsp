<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="google" content="notranslate" />
<title>SMARTsuite 10.0</title>
<link href="css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	setTimeout(function(){
		redirectPage();
	}, 3000);
	function redirectPage(){
		top.location.href='login.do';
	}
</script>

</head>
<body>
	<div class="wrap_msg">
		<div class="container">
			<div class="section_upper">
				<div class="message success">
					<b class="icon"></b>
					<b>정상적으로 <br/>로그아웃 되었습니다.</b>
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