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
        <title>SMARTsuite 10.0 | Buyer</title>
        <link rel="stylesheet" type="text/css" href="css/login.css" />
    </head>
    <body class="in" onload="document.loginForm.username.focus();">
    <div class="wrap">
        <form name="loginForm" action="<c:url value="/loginProcess.do" />" method="POST" autocomplete="off">
        <div class="lang"><!-- 2023.10.11 pjsuny : change location -->
            <select id="locale" name="locale" onchange="onChangeLocale(this)">
                <option value="en_US">English</option>
                <option value="ko_KR">Korean</option>
                <option value="zh_CN">Chinese</option>
                <option value="ja_JP">Japanese</option>
            </select>
        </div>
        <div class="logo">emro</div>
        <div class="headline">Optimize Relationship</div>
        <!-- Login Area -->
        <div class="container_right flex_center">
            <div class="container">
                <!-- Logo -->
                <div class="logo_system">
                    <h2><img src="<c:url value='/img/logo_system.svg'/>"></h2>
                    <p>emro Intergrated Procurement system</p>
                </div>
                <!-- Login -->
                <div class="login_wrap mgt_100">
                    <!---- ID Login ---->
                    <div class="tab_contents active">
                            <div class="form">
                                <p class="form_row">
                                    <input id="principal" type="text" name="username" value="" onchange="this.value=this.value.toUpperCase();" placeholder="ID">
                                    <label for="principal" class="input_label">ID</label>
                                </p>
                                <p class="form_row">
                                    <input id="credential" type="password" name="password"  value="" placeholder="Password" onkeypress="keyPress();" autocomplete="off"/>
                                    <label for="credential" class="input_label">Password</label>
                                </p>
                                <p class="form_row" hidden="true">
                                    <select id="tenant" name="tenant" >
                                        <option value="EMRO">EMRO</option>
                                        <%--<option value="QA">QA</option>--%>
                                    </select>
                                </p>
                            </div>
                            <div class="login_more">
                                <sec:csrfInput/>
                                <div class="saveid">
                                    <input type="checkbox" id="id_save">
                                    <label for="id_save">ID Save</label>
                                </div>
                            </div>
                            <div class="btn_login">
                                <a value="Login" onclick="login();" title="Login" class="btn">LOGIN</a>
                            </div>
                    </div>
                </div>
                <!-- Footer -->
                <div class="footer">
                    <p class="copyright">ⓒ emro All Right Reserved.</p>
                </div>
            </div>
            </form>
        </div>

        <script type="text/javascript" src="bower_components/crypto-js/crypto-js.js"></script>
        <script type="text/javascript" src="bower_components/password-encryptor/password-encryptor.min.js"></script>

        <script src="license.do"></script>

        <script src="bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
        <link rel="import" href="bower_components/sc-component/sc-elements.build.html"/>
        <link rel="stylesheet" type="text/css" href="bower_components/sc-component/sc-elements.build.css"/>
        <script type="text/javascript">
            //onload 함수
            if(window.addEventListener) {    //IE 9+
                window.addEventListener("load", init, false);
            } else if(window.attachEvent) {    //Low IE
                window.attachEvent("onload", init);
            } else {    //etc
                window.onload = init;
            }
            function storageAvailable(type) {
                var storage;
                try {
                    storage = window[type];
                    var x = '__storage_test__';
                    storage.setItem(x, x);
                    storage.removeItem(x);
                    return true;
                }
                catch(e) {
                    return e instanceof DOMException && (
                        // Firefox를 제외한 모든 브라우저
                        e.code === 22 ||
                        // Firefox
                        e.code === 1014 ||
                        // 코드가 존재하지 않을 수도 있기 떄문에 이름 필드도 확인합니다.
                        // Firefox를 제외한 모든 브라우저
                        e.name === 'QuotaExceededError' ||
                        // Firefox
                        e.name === 'NS_ERROR_DOM_QUOTA_REACHED') &&
                        // 이미 저장된 것이있는 경우에만 QuotaExceededError를 확인하십시오.
                        (storage && storage.length !== 0);
                }
            }
            // localStorage 불러오는 함수
            function getLocalStorage(name) {
                //localStorage 사용가능 검사
                if(storageAvailable('localStorage') && document.location.href === localStorage.getItem('href')){
                    var value = localStorage.getItem(name);
                }
                return value? value : null;
            }
            // localstorage 저장함수
            function setLocalStorage(name, value, checked) {
                //localStorage 사용가능 검사
                if(storageAvailable('localStorage')){
                    if(checked){
                        localStorage.setItem(name, value);
                        localStorage.setItem('href',document.location.href);
                    }else{
                        localStorage.removeItem(name);
                        localStorage.removeItem('href');
                    }
                }
            }
            function init() {
                var id = getLocalStorage('id');

                if(id){
                    document.loginForm.principal.value = id; //input 이름이 id인곳에 getLocalStorage("id")값을 넣어줌
                    document.loginForm.credential.value = "";
                    document.loginForm.id_save.checked = true; // 체크는 체크됨으로
                    document.loginForm.credential.focus();
                }else{
                     document.loginForm.principal.focus();
                     document.loginForm.autocomplete = "on";
                }
            }
            function login() {
                // 아이디 저장을 체크 하였을때
                if (document.loginForm.id_save.checked == true) {
                    setLocalStorage("id", document.loginForm.principal.value, true);
                // 아이디 저장을 체크 하지 않았을때
                } else {
                    setLocalStorage("id");
                }

                document.loginForm.password.value = PasswordEncryptor.encryptpw(document.loginForm.password.value);
                document.loginForm.submit();
            }
            function keyPress(){
                if(event.keyCode == "13"){
                    login();
                }
            }
            function onChangeLocale(e){
                if(e.value == "ko_KR"){
                    location.href="/loginKr.do"
                }else{
                    location.href="/loginEn.do"
                }
            }
        </script>
    </body>
</html>