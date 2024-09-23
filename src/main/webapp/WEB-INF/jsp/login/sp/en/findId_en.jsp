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
	<sec:csrfMetaTags />
	<title>SMARTsuite 10.0</title>
<link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/popup.css" type="text/css" rel="stylesheet">
</head>

<body>
	<div class="modal-body pw_serach id_serach">
		<h2>Find ID</h2>
		<form name="userForm">
			<div class="sel_content" data-item="1">
				<p>Find the ID with the business registration number registered at the time of registration. Enter your business registration number.</p>
				<div class="content">
					<ul class="style">
						<li class="form_1">
							<label for="select">System</label>
							<span>
								<select id="tenant" name="tenant">
									<option value="EMRO">EMRO</option>
									<option value="QA">QA</option>
								</select>
							</span>
						</li>
						<li class="form_2">
							<label for="number1">Company Registration Number</label>
							<span>
								<input type="text" id="number1" onkeypress="if(event.keyCode==13){ initPwByUserInfo(); return false;}" maxlength="3" oninput="inputNumber(this)"/>
						   -<input type="text" id="number2" maxlength="2" oninput="inputNumber(this)">-<input type="text" id="number3" maxlength="5" oninput="inputNumber(this)">
								<input type="hidden" id="id" name="id">
							</span>
							</span>
						</li>
					</ul>
				</div>
				<div class="modal-footer">
					<a href="javascript:initPwByUserInfo()" alt="Ok">Ok</a>
				</div>
			</div>
		<sec:csrfInput/>
		</form>
		<form name="resultForm">
			<div class="sel_content" data-item="2" hidden="hidden">
				<p class="help_txt" id="result_header_text"></p>
				<div class="content id_result">
					<ul id="result_userList">
					</ul>
				</div>
				<div class="modal-footer">
					<a href="javascript:moveFindPassword()" alt="FindPw">Find PW</a>
				</div>
			</div>
			<sec:csrfInput/>
		</form>
	</div>

	<script src="/bower_components/jquery/dist/jquery.min.js"></script>
	<script>
		(function () {
			/* document.userForm.username.focus(); */  //Init Focus(JavaScript)
			$('.sel_box').first().find('li').first().addClass('active'); //Init <li> Select
			$('.sel_content').first().find('input').first().focus(); //Init <input> Focus
		})() ;

		function initPwByUserInfo() {
				 var id ="";
				 var inputArr = $('input');
				for(var i = 0, len = inputArr.length; i < len; i++){
					var item = inputArr[i];
					if(item.id.indexOf('number') > -1){
							if( item.value == null || item.value == ""){
								alert("Enter your business registration number.");
		    				return;
							}
							id += item.value;
					 }
					 if(len == i+1){
						document.userForm.id.value = id;
					 }
				}
		    if(!document.userForm.id.value) {
		    	alert("Enter your business registration number.");
		    	return;
		    }

		    var doc = document,
			meta = doc.querySelector('meta[name=_csrf]'), _csrf,
			ajaxSettings = {
	    		method : 'POST',
				dataType : 'json',
				contentType: "application/json",
				headers: {}
	    	},id = $('#id').val(),tenant = $('#tenant').val();

			if(meta) {
				_csrf = {
					csrf : meta.content,
					csrfHeader : doc.querySelector('meta[name=_csrf_header]').content,
					csrfParameter : doc.querySelector('meta[name=_csrf_parameter]').content
				}
				ajaxSettings.headers[_csrf.csrfHeader] = _csrf.csrf;
			}

				$.ajax('findUserIdByBusinessRegistrationNumber.do', $.extend(false, ajaxSettings, {
					data : JSON.stringify({
						id: id,
						tenant: tenant
					})
				})).done(function(result) {
					if(result != null){
						if(result.resultStatus === "S"){
							var list = result.resultData.userList;

							$('#id').val('');
							$('.sel_content').hide();
							$('.sel_content[data-item=2]').show();
							$('.sel_content[data-item=2]').find('input').first().focus();

							$('#result_header_text').empty();
							$('#result_header_text').append(list[0].vd_nm+' Partners <b>Total '+ list.length +'</b>User');
							$('#result_userList').empty();

							for(var i = 0,len = list.length; i<len;i++){
								var item = list[i];
								$('#result_userList').append('<li>User ID <b>'+item.usr_id+'</b> </li>');
							}
						}else{
							alert("The registered information does not exist.");
						}
					}
				}.bind(this)).fail(function() {
					console.error("An error occurred while viewing.");
				}.bind(this));
		}

		function moveFindPassword() {
			document.resultForm.action = "findPasswordEn.do";
			document.resultForm.submit();
		}
		function inputNumber(e){
			var me = this;
			var inputId = $('#'+e.id);
			inputId.val(inputId.val().replace(/[^0-9]/g,''));
			if(parseInt(inputId.attr('maxlength')) <= inputId.val().length){
				var currentNum = parseInt(inputId.selector.replace(/[^0-9]/g,''));
				if(currentNum < 3){
					$('#number' + (currentNum+1)).focus();
				}
			}
		}
		//Select MENU 제어하기
		/* $('.sel_box > ul > li').click(function(){
			var item = $(this).attr('data-item');

			//Content 제어
			$('.sel_content').hide();
			$('.sel_content[data-item='+item+']').show();
			$('.sel_content[data-item='+item+']').find('input').first().focus();

			//MENU 제어
			$('.sel_box > ul > li').removeClass('active');
			$(this).addClass('active');
		}); */
	</script>
</body>

</html>
