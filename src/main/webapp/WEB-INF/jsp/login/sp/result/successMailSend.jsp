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
</head>
<body>
	<!--메일 전송 완료-->
	<div class="modal-body pw_serach">
		<h2>메일 전송 완료</h2>
		<div class="content pw_result">
			<p>이메일 주소로 임시 패스워드가 발급되었습니다.</p>
			<p>로그인 후 비밀번호를 재설정하십시오.</p>
		</div>
		<div class="modal-footer">
			<a href="javascript:popupClose(this)" alt="닫기">닫기</a>
		</div>
	</div>

	<script type="text/javascript" src="/js/main/login.js"></script>
</body>

</html>