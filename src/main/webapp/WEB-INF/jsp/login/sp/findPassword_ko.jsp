<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="google" content="notranslate" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>SMARTsuite 10.0</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/popup.css" type="text/css" rel="stylesheet">
</head>

<body>
	<div class="modal-body pw_serach">
		<h2>비밀번호 초기화</h2>
		<p>아이디, 이메일을 입력한 후 [전송]버튼을 클릭하면 해당 이메일로 초기 비밀번호가 전송됩니다.</p>
		<form name="userForm" >
		<div class="content">
			<ul class="style">
				<li class="form_1">
					<label for="number1">시스템</label>
					<span>
						<select id="tenant" name="tenant">
							<option value="EMRO">EMRO</option>
							<option value="QA">QA</option>
						</select>
					</span>
				</li>
				<li class="form_2"><label for="number1">아이디</label><span><input type="text" name="username" onchange="this.value=this.value.toUpperCase();" onkeypress="if(event.keyCode==13){ initPwByUserInfo(); return false;}"/></span></li>
				<li class="form_3"><label for="eml">E-mail</label><span><input type="text" name="eml" onkeypress="if(event.keyCode==13){ initPwByUserInfo(); return false;}"/></span></li>
			</ul>
		</div>
		<sec:csrfInput/>
		</form>
		<div class="modal-footer">
			<a href="javascript:initPwByUserInfo()" alt="전송하기">전송</a>
		</div>
	</div>

 	<!-- 추가적인 인증 방법 사용시 사용
		 <div class="sel_box">

		<form name="phoneForm" action="" method="post">
		<div class="sel_content" data-item="2" hidden="hidden">
			<p class="help_txt">휴대폰 정보로 찾기입니다.<br>가입할 때 등록하신 아이디를 입력하세요.</p>
			<ul>
				<li><span class="id_txt">이름</span><input type="text" onchange="this.value=this.value.toUpperCase();"/></li>
				<li><span class="id_txt">아이디</span><input type="text" onchange="this.value=this.value.toUpperCase();"/></li>
			</ul>
			<div class="base_btn"><a href="">휴대폰 인증</a></div>
		</div>
		<sec:csrfInput/>
		</form>

		<form name="ipinForm" action="" method="post">
		<div class="sel_content" data-item="3" hidden="hidden">
			<p class="help_txt">아이핀(I-PIN) 정보로 찾기입니다.<br>가입할 때 등록하신 아이디를 입력하세요.</p>
			<ul>
				<li><span class="id_txt">이름</span><input type="text" onchange="this.value=this.value.toUpperCase();"/></li>
				<li><span class="id_txt">아이디</span><input type="text" onchange="this.value=this.value.toUpperCase();"/></li>
			</ul>
			<div class="base_btn"><a href="">아이핀 인증</a></div>
		</div>
		<sec:csrfInput/>
		</form>
	</div> -->

	<script src="/bower_components/jquery/dist/jquery.min.js"></script>
	<script>
		(function () {
			/* document.userForm.username.focus(); */  //Init Focus(JavaScript)
			$('.sel_content').first().find('input').first().focus(); //Init Focus
		})() ;

		function initPwByUserInfo() {
		    if(!document.userForm.username.value || !document.userForm.eml.value) {
		    	alert("아이디와 이메일을 입력하십시오.");
		    	return;
		    }

			document.userForm.action = "initPassword.do";
			document.userForm.method = "post"
			document.userForm.submit();
		}

		//Select MENU 제어하기
		$('.sel_box > ul > li').click(function(){
			var item = $(this).attr('data-item');

			//Content 제어
			$('.sel_content').hide();
			$('.sel_content[data-item='+item+']').show();
			$('.sel_content[data-item='+item+']').find('input').first().focus();

			//MENU 제어
			$('.sel_box > ul > li').removeClass('active');
			$(this).addClass('active');
		});
	</script>
</body>

</html>
