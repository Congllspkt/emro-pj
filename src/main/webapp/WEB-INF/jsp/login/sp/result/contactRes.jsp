<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="google" content="notranslate" />
	<title>SMARTsuite 10.0 Login</title>

	<link rel="stylesheet" type="text/css" href="css/popup/find_popup.css" />
</head>

<body>
	<div id="wrap">
	
	<div class="sel_box">
		<ul class="sel_list_1">
			<li class="active" data-item="1"><a class="menu_link">임시패스워드 발급 실패</a></li>
		</ul>
		<div class="sel_table" data-item="1">
			<div class="sel_cell">
				<div class="medium_txt">현재 회원님은 이메일 전송에 동의하지 않아 임시 패스워드 발급이 불가능합니다.<br>
					휴대폰 또는 아이핀(Ipin)를 이용하여 패스워드 재설정 또는 관리자에게 문의바랍니다.<br>
				</div>
				
				<div class="base_btn"><a id="return_btn" href="javascript:redirectFindPw()">돌아가기</a></div>
			</div>
		</div>
	</div>
	</div>

	<script>
	(function (){
		document.getElementById("return_btn").focus();
	})();
	</script>	
	<script type="text/javascript" src="/js/main/login.js"></script>
</body>

</html>