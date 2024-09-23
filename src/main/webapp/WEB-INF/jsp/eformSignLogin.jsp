<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="google" content="notranslate" />
    <meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}" iteration="${_passwordSaltSource.iterationCount}"/>
    <meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
    <sec:csrfMetaTags />   
    
    <title>AnySignCloud</title>
    
    <link rel="stylesheet" type="text/css" href="css/login.css" />
    <script src="../../bower_components/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript" src="../../../js/main/login.js"></script>
    <script type="text/javascript" src="../../../js/main/browser.js"></script>
    <script type="text/javascript" src="bower_components/crypto-js/crypto-js.js"></script>
    <script type="text/javascript" src="bower_components/cipher-util/cipher-util.min.js"></script>
    <script type="text/javascript" src="bower_components/password-encryptor/password-encryptor.min.js"></script>
    
    <style>
        sc-window>div.content-wrap>div.content {
            position: absolute;
            background-color: #fff;
            border-radius: 3px;
            overflow: hidden;
        }
    </style>
</head>
<body style="background: #002c5f;" onload="document.loginForm.username.focus();">
    <div id="wrap">
        <form autocomplete="off" name="loginForm" action="<c:url value='/loginProcess.do' />" method="POST">
            <section class="wrap-conts-02">
                <section class="login-conts">
                    <section class="sp_login-box">
                        <h1></h1>
                        <h2 class="mgb-40">전자서명플랫폼</h2>
                        <section class="input-box">
                            <div class="user_id"><input type="text" name="username" onchange="this.value=this.value.toUpperCase();" placeholder="ID" onkeypress="keyPress();" required="true"></div>
                            <div class="user_pw"><input type="password" name="password"  placeholder="Password" onkeypress="keyPress();" required="true"/>
                                                 <input name="exceptionIfMaximumExceeded"  type="hidden" value="true">
                                                 <input type="hidden" name="usrCls" value="${usrCls}"/>
                                                 <input type="hidden" name="menuId" value="${menuId}" /> 
                                                 <input type="hidden" name="appId" value="${appId}" />
                                                 <input type="hidden" name="mobileYn" value="N" />
                                                 <input type="hidden" name="param" value="${param}" /></div>
                        </section>
                        <a href="#" onclick="login();" class="btn-login" alt="Login">로그인</a>
                        <sec:csrfInput/>
                    </section>
                </section>
            </section>
        </form>
    </div>
    
    <script type="text/javascript">
    //onload 함수
        if(window.addEventListener) {    //IE 9+
            window.addEventListener("load", init, false);
        } else if(window.attachEvent) {    //Low IE
            window.attachEvent("onload", init);
        } else {    //etc 
            window.onload = init;
        }
    window.Polymer = {
        license : 'NmAzsC+gW2Ymtsh1Ec52wJA/EkfMO6QkP0ayljlsuVCdZ6ENfskkHDyGNT0rXrWSw3Be3CVIj7ObV5l6/uWE4l3/NWqMfxuowiNeF0r0wFw='
    }; 
    window.isParameterEncypt = ('${_secParam}' === "skip") ? false: true; //파라미터 암호화 처리 여부
    
    function init () {
            //Browser의 호환성을 체크 
            //프로젝트별 브라우저 및 운영체제 설정은
            //browser.js에서 확인할 수 있음
            //BROWSER.browserValid : 브라우저 검증 Boolean 
            if(!BROWSER.browserValid && document.documentMode != 11) {
                location.href = "../../resources/portal/supportbrowser.html";
            }

            
             var id = getCookie("id");
            
            // getCookie함수로 id라는 이름의 쿠키를 불러와서 있을경우
            if(id) { 
                document.loginForm.username.value = id; //input 이름이 id인곳에 getCookie("id")값을 넣어줌
                document.loginForm.save_id.checked = true; // 체크는 체크됨으로
            }
            
            var part = location.hostname.split('.');
            var subdomains = part.shift();

            
        }

        function login() {
        	// // 강제 로그인 기능을 부여하려면 로그인 form 태그의 exceptionIfMaximumExceeded input 태그의 값 false 로 변경하면 됩니다.
        	document.loginForm.exceptionIfMaximumExceeded.value = false;
            document.loginForm.password.value = PasswordEncryptor.encryptpw(document.loginForm.password.value.trim());
            document.loginForm.submit();    
        }
        function keyPress(){
            if(event.keyCode == "13"){
                login();
            }
        }
        // 쿠키 불러오는 함수
        function getCookie(name) { 
            var search = name + "=";
            
            if (document.cookie.length > 0) { // if there are any cookies
                var offset = document.cookie.indexOf(search);
            
                if (offset != -1) { // if cookie exists
                    offset += search.length; // set index of beginning of value
                    end = document.cookie.indexOf(";", offset); // set index of end of cookie value
                    
                    if(end == -1){
                        end = document.cookie.length;
                    }
                    
                    return unescape(document.cookie.substring(offset, end));
                }
            }
        }
        // 쿠키 저장함수
        function setCookie(name, value, expiredays){
            var todayDate = new Date();
            todayDate.setDate(todayDate.getDate() + expiredays);
            
            document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";"
        }
    </script>
    <c:if test="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] != null}">
        <!-- 중복 로그인 처리 -->
        <spring:eval var="sessionMaximumExceeded" expression="sessionScope['SPRING_SECURITY_LAST_EXCEPTION'] instanceof T(org.springframework.security.web.authentication.session.SessionAuthenticationException)" />
        <c:if test="${sessionMaximumExceeded}">
            <script type="text/javascript">
                //사용자에게 오류 메세지 전달 후 강제 로그인 기능을 부여하려면 로그인 form 태그의 exceptionIfMaximumExceeded input 태그의 값 false 로 변경하면 됩니다.
                if(confirm("로그인된 사용자가 존재합니다!\n기존 로그인을 로그아웃 시키겠습니까?") ){
                    document.loginForm.exceptionIfMaximumExceeded.value = false;
                    alert("다시 로그인을 진행 해 주십시요.");
                }
            </script>
        </c:if>
        <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
        <c:remove var="AUTHENTICATION_FAILURE_MESSAGE" scope="session" />
    </c:if>
</body>
</html>