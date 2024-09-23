<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}"
        iteration="${_passwordSaltSource.iterationCount}" />
    <sec:csrfMetaTags />
    <title>SMARTsuite 10.0 | Vendor</title>
    <script src="bower_components/jquery/dist/jquery.min.js"></script>
    <script src="bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
    <link rel="stylesheet" type="text/css" href="bower_components/sc-component/sc-elements.build.css" />
    <link href="css/login.css" type="text/css" rel="stylesheet">
    <link href="css/popup.css" type="text/css" rel="stylesheet">
    <script src="ui/smartsuite/custom/js/util.js" type="text/javascript"></script>

    <script type="text/javascript">
        //onload 함수
        if (window.addEventListener) {    //IE 9+
            window.addEventListener("load", init, false);
        } else if (window.attachEvent) {    //Low IE
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
            catch (e) {
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
		function getQueryStringParam(key) {
			return new URL(location.href).searchParams.get(key);
        }
        function init() {

            //Browser의 호환성을 체크
            //프로젝트별 브라우저 및 운영체제 설정은
            //browser.js에서 확인할 수 있음
            //1) BROWSER.valid : 운영체제 & 브라우저 검증 Boolean
            //2) BROWSER.OSValid : 운영체제 검증 Boolean
            //3) BROWSER.browserValid : 브라우저 검증 Boolean
            /* if(!BROWSER.OSValid) {
                    location.href = "resources/portal/osbrowser.html";
                     */
            if (!BROWSER.browserValid && document.documentMode != 11) {
                location.href = "resources/portal/supportbrowser.html";
            }

            var id = getLocalStorage('id');
            var sysId = getQueryStringParam('sysId');

            if(sysId != null && sysId !=''){
                //tenant setting
                Array.prototype.forEach.call(document.getElementsByName('tenant'),function(item){
                    item.value = sysId;
                });
            }
            // localStorage에서 id를 불러와서 있을경우
            if (id) {
                document.loginForm.id.value = id; //input 이름이 id인곳에 getCookie("id")값을 넣어줌
                document.loginForm.pw.value = "";
                document.loginForm.id_save.checked = true; // 체크는 체크됨으로
                document.loginForm.pw.focus();
            } else {
                document.loginForm.id.focus();
                document.loginForm.autocomplete = "on";
            }
        }
        // localStorage 불러오는 함수
        function getLocalStorage(name) {
            //localStorage 사용가능 검사
            if (storageAvailable('localStorage') && document.location.href === localStorage.getItem('href')) {
                var value = localStorage.getItem(name);
            }
            return value ? value : null;
        }
        // localstorage 저장함수
        function setLocalStorage(name, value, checked) {
            //localStorage 사용가능 검사
            if (storageAvailable('localStorage')) {
                if (checked) {
                    localStorage.setItem(name, value);
                    localStorage.setItem('href',document.location.href);
                } else {
                    localStorage.removeItem(name);
                    localStorage.removeItem('href');
                }
            }
        }
        // 로그인 함수
        function onLogin() {
            // 아이디 저장을 체크 하였을때
            if (document.loginForm.id_save.checked == true) {
                setLocalStorage("id", document.loginForm.id.value, true); //id로 아이디입력필드값을 저장
                // 아이디 저장을 체크 하지 않았을때
            } else {
                setLocalStorage("id");
            }
            document.loginForm.password.value = PasswordEncryptor.encryptpw(document.loginForm.password.value);
            document.loginForm.submit();
        }
        function onCertLogin(){
			var certId = document.getElementById('certId');

			if(certId.value == null || certId.value == ""){
				alert("ID를 입력해주세요.");
				certId.focus();
				return;
			}
			popupCertLogin();

		}
		var windowCertPopup;
		function popupCertLogin(){
			var cw = 598;
			var ch = 620;
			var sw = screen.availWidth;
			var sh = screen.availHeight;
			var px = (sw-cw)/2;
			var py=(sh-ch)/2;
			var option = "top="+py+",left="+px+",width="+cw+",height="+ch;
			var certId = document.getElementById("certId");

			windowCertPopup = window.open("about:blank","windowCertPopup",option);
			var form = document.certLoginCheckForm;
			form.action="openCertSelectionPage.do";
			form.target="windowCertPopup";

			var username = document.certLoginCheckForm.username;
			username.value = certId.value;
			var tenantInput = document.certLoginCheckForm.tenant;
			var tenant = document.getElementById("tenant").value;

			tenantInput.value = tenant;

			form.submit();
		}
		function failCheckBizRegNo(resultStatus, resultMessage){

			if(resultStatus === "E"){
				windowCertPopup.self.close();
				setTimeout(function(){alert("error : " + resultMessage);},10);
			}
		}
		function certLoginResult(resultStatus, content){
			if(resultStatus === "S"){
				windowCertPopup.self.close();
				setTimeout(completeCertLogin(content),10);
			}else if(resultStatus === "E"){
				windowCertPopup.self.close();
				setTimeout(function(){alert("error : " + content);},10);
			}
		}
		function completeCertLogin(hashValue){

			var form = document.getElementById("certLoginForm");

			var hashValueInput = form.hash_value;
			var usernameInput = form.username;
			var certId = document.getElementById("certId");
			var locale = document.getElementById('lang').value;
			var localeInput = document.getElementsByName("locale");
			var tenant = document.getElementById("tenant").value;
			var tenantInput = document.getElementsByName("cert_login_tenant");

			hashValueInput.value = hashValue;
			usernameInput.value = certId.value;
			localeInput[0].value = locale;
			tenantInput[0].value = tenant;
			form.submit();

		}
        function onRegNewVendor() {
            var me = this;
            var locale = document.getElementById('lang').value;
            var tenant = document.getElementById('tenant').value;
            var param = [
                {
                    locale : locale,
                    tenant : tenant
                }
            ]
            UT.popupJspByPost("newVendor.do",param,null, 1200, 760, {
                "sc-window-hided": function () {
                    location.href = "spLogin.do";
                },
                "close": function (e) {
                    location.href = "spLogin.do";
                }
            }, { maximizable: true, titleText: "신규업체등록" });
        }
        function noticeLink(pst_no) {
            var param = [
                {
                    bbd_uuid: '${boardId}',
                    pst_no: pst_no
                }
            ];

            UT.popupJspByPost("noticeLink.do", param, null, 1000, 650, "", { maximizable: true, titleText: "공지사항" });
        }
        function biddingNoticeLink(rfx_uuid) {
            var param = [
                { rfx_uuid: rfx_uuid }
            ];

            UT.popupJspByPost("sp/login/bidding/biddingNoticeLink.do", param, null, 1000, 650, "", { maximizable: true, titleText: "입찰공고" });
        }
        function morePopup() {
            if (document.getElementsByClassName('active')['tab3']) {
                var param = [{ bbd_uuid: "${boardId}" }];
                UT.popupJspByPost("noticeLink.do", param, null, 1000, 650, "", { maximizable: true, titleText: "공지사항" });
            } else {
                UT.popupJspByPost("sp/login/bidding/biddingNoticeLink.do", "", null, 1000, 650, "", { maximizable: true, titleText: "입찰공고" });
            }
        }
        function popupTerms(termType) {
            var me = this;
            var titleText = termType == 'SYS_TERMCND' ? '구매포탈 이용약관' : '개인정보처리방침';
            var locale = document.getElementById('lang').value;
            var tenant = document.getElementById('tenant').value;
            UT.popupJsp("findTerms.do?locale=" + locale + "&tenant=" + tenant + "&type=" + termType, null, 900, 660, null, { maximizable: true, titleText: titleText });
        }

        function popupHelpDesk() {
            var me = this;
            UT.popupJsp('resources/login/contents/help_desk.html', me, 500, 273, null, { titleText: 'Helpdesk', resizable: false });
        }
        function popupEthical() {
            var me = this;
            UT.popupJsp('resources/login/contents/ethical.html', me, 500, 235, null, { titleText: '윤리경영', resizable: false });
        }
        function popupCredit() {
            var me = this;
            UT.popupJsp('resources/login/contents/credit_info.html', me, 650, 500, null, { titleText: '신용평가', resizable: false });
        }
        function onChangeTenant(e){
        	var form = document.getElementById('spLoginForm');
            form.sysId.value = e.value;
            form.submit();
        }
    </script>
    <style>
        sc-window>div.content-wrap>div.content {
            position: absolute;
            background-color: #fff;
            border-radius: 3px;
            overflow: hidden;
        }
    </style>
</head>

<body onload="document.loginForm.username.focus();">
<div class="wrap">
    <div class="logo">emro</div>
    <div class="headline">Optimize Relationship</div>
    <!-- Login Area -->
    <div class="container_right flex_center">
        <div class="container">
            <!-- Logo -->
            <!-- Language --><!-- 2024.02.01 pjsuny -->
            <div class="lang">
                <select id="lang" name="lang" onchange="onChangeLocale(this)">
                    <option value="en_US">English</option>
                    <option value="ko_KR">Korean</option>
                    <option value="zh_CN">Chinese</option>
                </select>
            </div>
            <div class="logo_system">
                <h2><img src="<c:url value='/img/logo_system.svg'/>" art="Login" title="Login"></h2>
                <p>emro Intergrated Procurement system</p>
            </div>
            <!-- Login -->
            <div class="login_wrap">
                <!---- Login Tab ---->
                <div class="login_tab_bar">
                    <div data-tab="tab1" class="tab active"><a>아이디</a></div>
                    <div data-tab="tab2" class="tab"><a>공동인증서</a></div>
                </div>
                <!---- ID Login ---->
                <div id="tab1" class="tab_contents active">
                    <form name="loginForm" action="<c:url value='/spLoginProcess.do'/>" method="post">
                        <div class="form">
                            <p class="form_row">
                                <input type="text" id="id" name="username" value=""
                                       onchange="this.value=this.value.toUpperCase();"
                                       onkeypress="if(event.keyCode==13){ onLogin(); return false;}" placeholder="ID" />
                                <label for="id" class="input_label">ID</label>
                            </p>
                            <p class="form_row">
                                <input type="password" id="pw" name="password"
                                       onkeypress="if(event.keyCode==13){ onLogin(); return false;}"
                                       placeholder="PASSWORD" autocomplete="off">
                                <label for="password" class="input_label">Password</label>
                            </p>
                            <p class="form_row" hidden="true">
                                <select id="tenant" name="tenant" onchange="onChangeTenant(this)">
                                    <option value="EMRO">EMRO</option>
                                    <%--<option value="QA">QA</option>--%>
                                </select>
                            </p>
                            <p class="form_row">
                                <select id="locale" name="locale">
                                    <option value="ko_KR">KOREAN</option>
                                    <option value="en_US">English</option>
                                    <option value="zh_CN">Chinese</option>
                                </select>
                            </p>
                        </div>
                        <div class="login_more">
                            <div class="saveid">
                                <input type="checkbox" id="id_save">
                                <label for="id_save">ID 저장</label>
                            </div>
                            <div class="login_search">
                                <a href="#" onclick="popupFindId()" href="">ID 찾기</a>
                                <span>/</span>
                                <a href="#" onclick="popupFindPw()" href="">Password 찾기</a>
                            </div>
                        </div>
                        <div class="btn_login">
                            <a onclick="onLogin()" title="아이디 로그인" class="btn">LOGIN</a>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <sec:csrfInput />
                    </form>
                </div>
                <!---- Certificate Login ---->
                <div id="tab2" class="tab_contents">
                    <form>
                        <div class="form">
                            <p class="form_row">
                                <input type="text" id="certId" name="username" value=""
                                       onchange="this.value=this.value.toUpperCase();"
                                       onkeypress="if(event.keyCode==13){this.value=this.value.toUpperCase(); onCertLogin(); return false;}" placeholder="ID" />
                                <label for="certId" class="input_label">ID</label>
                            </p>
                            <p class="form_row">
                                <select id="certLocale" name="locale">
                                    <option value="ko_KR">Korean</option>
                                    <option value="en_US">English</option>
                                    <option value="zh_CN">Chinese</option>
                                </select>
                            </p>
                        </div>
                        <div class="btn_login">
                            <a onclick="onCertLogin()" title="공동인증서 로그인" class="btn">LOGIN</a>
                        </div>
                    </form>
                </div>
            </div>
            <!-- Banner -->
            <div class="banner_wrap">
                <a href="#" onclick="onRegNewVendor()" href="" class="banner"><b>신규업체등록</b></a>
                <a href="https://raadmin.crosscert.com/customer/emro/index.html" target="_blank"class="banner"><b>공동인증서안내</b></a>
                <a href="#" onclick="popupEthical()" href="" class="banner"><b>윤리경영</b></a>
            </div>
            <!-- Footer -->
            <div class="footer">
                <div class="footer_link">
                    <a href="#" onclick="popupTerms('PRSN_INFO_COLL_USG_AGT')" href="">개인정보처리방침</a>
                    <a href="#" onclick="popupTerms('SYS_TERMCND')" href="">이용약관</a>
                    <a href="#" onclick="popupHelpDesk()" href="">Helpdesk</a>
                </div>
                <p class="copyright">ⓒ emro All Right Reserved.</p>
            </div>
        </div>
    </div>
    <!--tabs-->
    <script>
        $(function () {
            $('.login_tab_bar>div').click(function () {
                var activeTab = $(this).attr('data-tab');
                $('.login_tab_bar>div').removeClass('active');
                $('.tab_contents').removeClass('active');
                $(this).addClass('active');
                $('#' + activeTab).addClass('active');
                if (activeTab === 'tab1') {
                    document.getElementById('id').value = document.getElementById('certId').value
                } else if (activeTab === 'tab2') {
                    document.getElementById('certId').value = document.getElementById('id').value
                }
            });
            $('.login_tab_bar>div').click(function () {
                var activeTab = $(this).attr('data-tab');
                $('.login_tab_bar>div').removeClass('active');
                $('.tab_contents').removeClass('active');
                $(this).addClass('active');
                $('#' + activeTab).addClass('active');
            });
        });
    </script>

    <form name="certLoginCheckForm" target="work" method="post" >
		<input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden"/>
		<input name="username" type="hidden"/>
		<input name="callbackUrl" value="openCertSelectionPage.do" type="hidden"/>
		<input name="installStatus" value="BEFORE" type="hidden"/>
		<input name="tenant" value="" type="hidden"/>
    </form>

    <form id="certLoginForm" action="certificateLoginProcess.do" method="POST">
    	<input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden"/>
		<input type="hidden" name="login_type" value="certLogin" />
		<input type="hidden" name="hash_value" value="" />
		<input type="hidden" name="username" value="" />
		<input type="hidden" name="locale" value="" />
		<input type="hidden" name="cert_login_tenant" value="" />
  	</form>

    <form id="spLoginForm" action="spLogin.do" method="post">
    	<input type="hidden" name="sysId"/>
    	<sec:csrfInput/>
    </form>

    <script src="license.do"></script>
    <link rel="import" href="bower_components/sc-component/sc-elements.build.html" />
    <script type="text/javascript" src="js/main/login.js"></script>
    <script type="text/javascript" src="js/main/browser.js"></script>
    <script type="text/javascript" src="bower_components/crypto-js/crypto-js.js"></script>
    <script type="text/javascript" src="bower_components/password-encryptor/password-encryptor.min.js"></script>
    <script type="text/javascript" src="js/jquery.min.js"></script>
</body>

</html>