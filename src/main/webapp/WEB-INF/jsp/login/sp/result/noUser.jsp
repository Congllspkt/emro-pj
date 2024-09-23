<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="google" content="notranslate" />
	<title>SMARTsuite 10.0 Login</title>

	<link rel="stylesheet" type="text/css" href="../../../../../css/base.css" />
	<link rel="stylesheet" type="text/css" href="../../../../../css/popup.css" />
	<script type="text/javascript" src="/js/main/login.js"></script>
</head>

<body>

	<!--임시패스워드 발급 실패-->
	<div class="modal-body pw_serach">
		<h2>임시패스워드 발급 실패</h2>
		<div class="content pw_result">
			<p>입력하신 정보와 일치하는 회원이 존재하지 않습니다.</p>
			<p>관리자에게 문의바랍니다.</p>
		</div>
		<div class="modal-footer">
			<a id="return_btn" href="javascript:redirectFindPw()" alt="돌아가기">돌아가기</a>
		</div>
	</div>

	<script>
	(function (){
		document.getElementById("return_btn").focus();
	})();
	</script>

</body>

</html>