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
    
    <title>SMARTsuite 10.0</title>
    
    <link rel="stylesheet" type="text/css" href="/resources/mobile/css/login.css" />
    <script src="../bower_components/jquery/dist/jquery.min.js"></script>
    <script type="text/javascript" src="../../js/main/login.js"></script>
    <script type="text/javascript" src="../../js/main/browser.js"></script>
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
<body style="background: #002c5f;" onload="document.loginForm.password.focus();">
    <div id="wrap">
        <form autocomplete="off" name="loginForm" action="<c:url value='/eformSignPwLoginProcess.do' />" method="POST">
            <section class="wrap-conts-02">
                <section class="login-conts">
                    <section class="sp_login-box">
                        <h1>SMARTsuite 10.0</h1>
                        <h2 class="mgb-40">간편서명</h2>
                        <section class="input-box">
                            <div class="user_pw"><input id="password" type="password" name="password"  placeholder="Password" onkeypress="keyPress();" required="true"/>
                                                 <input type="hidden" name="mobileYn" value="N" />
                                                 <input type="hidden" name="param" value="${eformSignParam}" /></div>
                        </section>
                        <a href="#" onclick="login();" class="btn-login" alt="Login">인증하기</a>
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
                location.href = "/resources/portal/supportbrowser.html";
            }
            
            var part = location.hostname.split('.');
            var subdomains = part.shift();
        }

        function login() {
            //document.loginForm.password.value = CipherUtil.encrypt(document.loginForm.password.value.trim())
            document.loginForm.password.value = PasswordEncryptor.encryptpw(document.loginForm.password.value.trim());
            document.loginForm.submit();    
        }
        function keyPress(){
            if(event.keyCode == "13"){
                login();
            }
        }
    </script>
</body>
</html>