var _copyMedia = null;

function NX_branchLogic_ISSUE() {
    var Logic_flag = processLogic.getProcessLogic();
    if (Logic_flag.indexOf('ISSUE') !== -1) {
        //issueCertificate.branchLogic(Logic_flag);

        if (Logic_flag === 'KICA.ISSUE.RenewCertificateInfo') {
            hideNXBlockLayer();
            SecuKitNX_Result('updateCertInfo');
            // 인증서 갱신 전 인증서 정보 추출인 경우
        }

        if (Logic_flag === 'KICA.ISSUE.RenewCertificate') {
            // 인증서 갱신 인 경우

            // 진행중 창 표시
            $('.nx-issue-ing-alert-head-msg').remove();
            var headMessage = '';
            var alertMessage = '';
            headMessage += '<div class=\"nx-issue-ing-alert-head-msg\">';
            headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_28 + '</h>';
            alertMessage = '<div id=\"issue-ing-alert-message\" class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_ISSUE_PUB_TEXT_29 + '<br><br>' + NX_ISSUE_PUB_TEXT_35 + '<br><br>' + NX_ISSUE_PUB_TEXT_36 + '</span></p></div>';
            headMessage += '</div>';
            $('#nx-issue-ing-alert-head').append(headMessage);
            $('#issue-ing-alert-message').remove();
            $('#nx-issue-ing-alert-box').append(alertMessage);

            setTimeout(function () {
                $('#nx-issue-ing-alert').show();
            }, 100);

            setTimeout(function () { issueCertificate.updateCert(); }, 200);

        }

        // RevokeCert
        if (Logic_flag === 'KICA.ISSUE.RevokeCert') {

            // 진행중 창 표시
            $('.nx-issue-ing-alert-head-msg').remove();
            var headMessage = '';
            var alertMessage = '';
            headMessage += '<div class=\"nx-issue-ing-alert-head-msg\">';
            headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_46 + '</h>';
            alertMessage = '<div id=\"issue-ing-alert-message\" class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_ISSUE_PUB_TEXT_47 + '<br><br>' + NX_ISSUE_PUB_TEXT_35 + '<br><br>' + NX_ISSUE_PUB_TEXT_36 + '</span></p></div>';
            headMessage += '</div>';
            $('#nx-issue-ing-alert-head').append(headMessage);
            $('#issue-ing-alert-message').remove();
            $('#nx-issue-ing-alert-box').append(alertMessage);

            setTimeout(function () {
                $('#nx-issue-ing-alert').show();
            }, 100);

            setTimeout(function () {
                //인증서 폐지 실행
                issueCertificate.revokeCert();
            }, 200);

        }

        // HoldCert
        if (Logic_flag === 'KICA.ISSUE.HoldCert') {

            // 진행중 창 표시
            $('.nx-issue-ing-alert-head-msg').remove();
            var headMessage = '';
            var alertMessage = '';
            headMessage += '<div class=\"nx-issue-ing-alert-head-msg\">';
            headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_44 + '</h>';
            alertMessage = '<div id=\"issue-ing-alert-message\" class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_ISSUE_PUB_TEXT_45 + '<br><br>' + NX_ISSUE_PUB_TEXT_35 + '<br><br>' + NX_ISSUE_PUB_TEXT_36 + '</span></p></div>';
            headMessage += '</div>';
            $('#nx-issue-ing-alert-head').append(headMessage);
            $('#issue-ing-alert-message').remove();
            $('#nx-issue-ing-alert-box').append(alertMessage);

            setTimeout(function () {
                $('#nx-issue-ing-alert').show();
            }, 100);

            setTimeout(function () {
                //인증서 효력정지 실행
                issueCertificate.holdCert();
            }, 200);

        }

        // IssueKmCert
        if (Logic_flag === 'KICA.ISSUE.IssueKmCert') {
            // 진행중 창 표시
            $('.nx-issue-ing-alert-head-msg').remove();
            var headMessage = '';
            var alertMessage = '';
            headMessage += '<div class=\"nx-issue-ing-alert-head-msg\">';
            headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_42 + '</h>';
            alertMessage = '<div id=\"issue-ing-alert-message\" class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_ISSUE_PUB_TEXT_43 + '<br><br>' + NX_ISSUE_PUB_TEXT_35 + '<br><br>' + NX_ISSUE_PUB_TEXT_36 + '</span></p></div>';
            headMessage += '</div>';
            $('#nx-issue-ing-alert-head').append(headMessage);
            $('#issue-ing-alert-message').remove();
            $('#nx-issue-ing-alert-box').append(alertMessage);

            setTimeout(function () {
                $('#nx-issue-ing-alert').show();
            }, 100);

            setTimeout(function () { issueCertificate.issueKmCert(); }, 200);

        }
    }

    if (Logic_flag.indexOf('MANAGEMENT') !== -1) {
        //delete cert
        if ((Logic_flag === 'KICA.MANAGEMENT.DeleteCertificate')) {
            var mediaType = SelectMediaInfo.getMediaType();
            var mediaTypeString = '';

            if (mediaType === 'HDD') {
                mediaTypeString = NX_TARGET_DIALOG_TEXT_2;
            }
            if (mediaType === 'USB') {
                mediaTypeString = NX_TARGET_DIALOG_TEXT_3;
            }
            if (mediaType === 'HSM') {
                mediaTypeString = NX_TARGET_DIALOG_TEXT_5;
            }
            if (mediaType === 'BIOHSM') {
                mediaTypeString = NX_TARGET_DIALOG_TEXT_6;
            }
            if (mediaType === 'SECUREDISK') {
                mediaTypeString = NX_TARGET_DIALOG_TEXT_14;
            }

            //cert cn 
            var selectCertIndex = certListInfo.getCertListIndex();
            var certListObj = certListInfo.getCertListInfo();
            var certName = certListObj[selectCertIndex].cn;

            $('#nx-cert-delete-box-msg').remove();

            var deleteMsg = '<div id=\"nx-cert-delete-box-msg\">';
            deleteMsg += '<p>' + NX_ISSUE_TEXT_20 + mediaType + NX_ISSUE_TEXT_21 + '<br />';
            deleteMsg += NX_ISSUE_TEXT_22 + certName + NX_ISSUE_TEXT_23 + '<br />';
            deleteMsg += '<strong>' + NX_ISSUE_TEXT_19 + '</strong></p>';
            deleteMsg += '</div>';

            $('#nx-cert-delete-box').append(deleteMsg);
            $('#nx-cert-select').hide();
            $('#nx-cert-delete').show();
        }

        // export pfx
        if (Logic_flag === 'KICA.MANAGEMENT.EXPORT_PFX') {
            var certType = '',  //서명용 인증서만 저장할지 암호화용과 함께 저장할지 여부 (SignCert, EncryptCert)
                filePath = '',  //빈 문자일 경우 읽어온 인증서 경로에 저장
                fileName = '',  //PFX 파일 이름
                certID = '';    //내보내기 할 인증서 CertID

            // 내보내기 파일명 
            fileName = $('#exportP12Name').val();
            $('#exportP12Name').val('');

            // with km or not
            var flag_WithKm = document.getElementById('exportWithKm');
            var flag_WithOutKm = document.getElementById('exportWithoutKm');

            if (flag_WithKm.checked) {
                 // with KmCert
                certType = 'EncryptCert';
            }

            if (flag_WithOutKm.checked) {
                // without KmCert
                certType = 'SignCert';
            }

            // certID
            certID = certListInfo.getCertID();

            // export p12
            CertManagement.exportP12(certType, filePath, fileName, certID);
        }

        // import pfx
        if (Logic_flag === 'KICA.MANAGEMENT.IMPORT_PFX') {
            var fullfilePath = '',  // 가져오기 할 인증서 경로
                fileName = '',      // 가져오기 할 인증서 이름
                pfxpwd = '',        // pfx 암호
                certID = '';        // 가져온 pfx파일이 인증서로 저장된 이후의 인증서의 certID

            fullfilePath = $('#importPFXFileName').val();
            fileName = fullfilePath.replace(/^.*(\\|\/|\:)/, '');
            pfxpwd = $('#importPFXPwd').val();
            $('#importPFXPwd').val('');

            var tmp = fullfilePath.indexOf(fileName);
            var filePath = fullfilePath.substring(0, tmp);

            // import p12
            CertManagement.importP12(filePath, fileName, pfxpwd, certID);
        }

        // copy cert
        if (Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
            // 동일한 저장매체 선택 시 처리 로직 추가
            var media = SelectMediaInfo.getMediaType();
            _copyMedia = document.getElementById(media);
            _copyMedia.disabled = true;

            // target media box
            $('#nx-targetMedia-select').show();
            $(".nx-pki-ui-wrapper").draggable({ handle: ".pki-head", cursor: "move" });
        }

        // CheckPassword
        if (Logic_flag === 'KICA.MANAGEMENT.CheckPassword') {
            var certListIndex = certListInfo.getCertListIndex();
            var certID = certListInfo.getCertID();
            CertManagement.checkPassword(certListIndex, certID);
        }

       // ShowCert
        if (Logic_flag === 'KICA.MANAGEMENT.ShowCert') {
            var certType = 'SignCert';
            var sourceString = 'test1234567890!@#$%^&*()Test';
            var algorithm = 'SHA256';
            var certID = certListInfo.getCertID();
            CertManagement.showCert(certType, sourceString, algorithm, certID);
        }

        // AuthIdentify
        if (Logic_flag === 'KICA.MANAGEMENT.AuthIdentify') {
            CertManagement.recoverIdentity();
        }

        // verify identity
        if (Logic_flag === 'KICA.MANAGEMENT.VerifyIdentify') {
            // show input ssn
            $('#nx-cert-VerifyIdentify').show();
        }

        // ChangePassword
        if (Logic_flag === 'KICA.MANAGEMENT.ChangePassword') {
            // show input password
            $('#nx-pwd-insert').show();

            setTimeout(function () {
                $("#nx_issue_cert_pw").attr("tabindex", -1).focus();
            }, 100);
        }

        // CertValidation
        if (Logic_flag === 'KICA.MANAGEMENT.CertValidation') {
            var certID = certListInfo.getCertID();
            CertManagement.validateCertIssue(certID);
        }
    }

    if (Logic_flag === '') {
        hideNXBlockLayer();
    }
}


//****************************
// 인증서 발급&재발급&갱신&폐지 관련 결과
// result of issue/reissue/renew/revoke
//****************************
var NX_ISSUE_Result = (function () {
    var ISSUE_RES = false,            // 인증서 발급 정상 발급 유무
        ISSUE_USERNAME = '',            // 발급된 인증서 NAME
        ISSUE_DN = '',                  // 발급된 인증서 DN
        ISSUE_Vaildate_From = '',       // 발급된 인증서 시작 날짜
        ISSUE_Vaildate_To = '',         // 발급된 인증서 만료 날짜
        ISSUE_Valid_PWD = false,        // 인증서 비밀번호 체크 
		ISSUE_certPath = '',
		ISSUE_certPem = '',
		ISSUE_SERIAL = '',
		ISSUE_PUBLICKEY = '';

    var init = function () {
        ISSUE_RES = false;
		ISSUE_certPath = '';
        ISSUE_certPem = '';
        ISSUE_USERNAME = '';
        ISSUE_DN = '';
        ISSUE_SERIAL = '';
        ISSUE_PUBLICKEY = '';
        ISSUE_Vaildate_From = '';
        ISSUE_Vaildate_To = '';
        ISSUE_Valid_PWD = false;
    };

    var setResult = function (res) {
        ISSUE_RES = res;
    };

    var getResult = function () {
        return ISSUE_RES;
    };

    var setIssueCertPath = function (p_certPath) {
    	ISSUE_certPath = p_certPath;
    };

    var getIssueCertPath = function () {
        return ISSUE_certPath;
    };
    
    var setIssueCertPem = function (p_certPem) {
    	ISSUE_certPem = p_certPem;
    };

    var getIssueCertPem = function () {
        return ISSUE_certPem;
    };

    var setIssueUserName = function (name) {
        ISSUE_USERNAME = name;
    };

    var getIssueUserName = function () {
        return ISSUE_USERNAME;
    };

    var setIssueCertDN = function (dn) {
        ISSUE_DN = dn;
    };

    var getIssueCertDN = function () {
        return ISSUE_DN;
    };

    var setIssueCertSerial = function (serial) {
        ISSUE_SERIAL = serial;
    };

    var getIssueCertSerial = function () {
        return ISSUE_SERIAL;
    };

    var setIssueCertPublicKey = function (publickey) {
        ISSUE_PUBLICKEY = publickey;
    };

    var getIssueCertPublicKey = function () {
        return ISSUE_PUBLICKEY;
    };

    var setIssueCertVaildateFrom = function (f) {
        ISSUE_Vaildate_From = f;
    };

    var getIssueCertVaildateFrom = function () {
        return ISSUE_Vaildate_From;
    };

    var setIssueCertVaildateTo = function (t) {
        ISSUE_Vaildate_To = t;
    };

    var getIssueCertVaildateTo = function () {
        return ISSUE_Vaildate_To;
    };

    var setValidPwdRes = function (res) {
        ISSUE_Valid_PWD = res;
    };

    var getValidPwdRes = function () {
        return ISSUE_Valid_PWD;
    };

    return {
        init: init,
        setResult: setResult,
        getResult: getResult,

        setIssueCertPath: setIssueCertPath,
        getIssueCertPath: getIssueCertPath,

        setIssueCertPem: setIssueCertPem,
        getIssueCertPem: getIssueCertPem,
        setIssueUserName: setIssueUserName,
        getIssueUserName: getIssueUserName,

        setIssueCertDN: setIssueCertDN,
        getIssueCertDN: getIssueCertDN,

        setIssueCertSerial: setIssueCertSerial,
        getIssueCertSerial: getIssueCertSerial,

        setIssueCertPublicKey: setIssueCertPublicKey,
        getIssueCertPublicKey: getIssueCertPublicKey,
        setIssueCertVaildateFrom: setIssueCertVaildateFrom,
        getIssueCertVaildateFrom: getIssueCertVaildateFrom,

        setIssueCertVaildateTo: setIssueCertVaildateTo,
        getIssueCertVaildateTo: getIssueCertVaildateTo

    };
})();

//****************************
// result of show cert
//****************************
var NX_CertINFO_Result = (function () {
    var USERDN = '',
        SIGNCERT = '',
        SINGED_DATA = '';

    var init = function () {
        USERDN = '';
        SIGNCERT = '';
        SINGED_DATA = '';
    };

    var setUserDN = function (dn) {
        USERDN = dn;
    };

    var getUserDN = function () {
        return USERDN;
    };

    var setSignCert = function (sign) {
        SIGNCERT = sign;
    };

    var getSignCert = function () {
        return SIGNCERT;
    };

    var setSigendData = function (data) {
        SINGED_DATA = data;
    };

    var getSignedData = function () {
        return SINGED_DATA;
    };

    return {
        init: init,
        setUserDN: setUserDN,
        getUserDN: getUserDN,
        setSignCert: setSignCert,
        getSignCert: getSignCert,
        setSigendData: setSigendData,
        getSignedData: getSignedData
    };
})();


//****************************
// 인증서 관리 결과 저장
//****************************
var NX_MANAGEMENT_Result = (function () {
    var result = false;

    var init = function () {
        result = false;
    };

    var setResult = function (flag) {
        result = flag;
    };

    var getResult = function () {
        return result;
    };

    return {
        init: init,
        setResult: setResult,
        getResult: getResult
    };

})();

//****************************
// SecuKitNX_Result 함수를 호출 하기 위해 함수이름을 가지고 있는 함수
//****************************
var issueFuncName = (function () {
    var funName = "";

    var init = function () {
        funName = "";
    };

    var setFuncName = function (fn) {
        funName = fn;
    };

    var getFuncName = function () {
        return funName;
    };

    return {
        init: init,
        setFuncName: setFuncName,
        getFuncName: getFuncName
    };

})();