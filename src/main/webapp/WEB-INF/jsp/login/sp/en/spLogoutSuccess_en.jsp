<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="google" content="notranslate" />
	<title>SMARTsuite 10.0 | Vendor</title>
	<link href="css/login.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		setTimeout(function(){
			redirectPage();
		}, 3000);
		function redirectPage(){
			top.location.href='spLoginEn.do';
		}
	</script>
</head>
<body>
	<div class="wrap_msg">
		<div class="container">
			<div class="section_upper">
				<div class="message success">
					<b class="icon"></b>
					<b>You are successfully <br/>logged out.</b>
				</div>
			</div>
			<div class="section_under">
				<p class="commt">Go to the login page in <b>3</b> seconds.</p>
				<p class="btn_cnt"><input class="btn_msg" type="button" value="Move Login Page" onclick="redirectPage()"/></p>
			</div>
		</div>
	</div>
</body>
</html>