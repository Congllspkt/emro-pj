/**
 * @public
 * @class
 * @description 인증서 관리를 위한 class
 */
var CertManagement = (function () {
    var NXPWD = '',
        NEW_NXPWD = '',
        NEW_NXPWD_CONFIRM = '',
        NXSSN = '';

    var init = function () {
        NXPWD = '';
        NEW_NXPWD = '';
        NEW_NXPWD_CONFIRM = '';
        NXSSN = '';
    };

    var setPwd = function (p) {
        NXPWD = p;
    };

    var setNEW_NXPWD = function (np) {
        NEW_NXPWD = np;
    };

    var setNEW_NXPWD_CONFIRM = function (npc) {
        NEW_NXPWD_CONFIRM = npc;
    };

    var setNXSSN = function (s) {
        NXSSN = s;
    };

    /**
    * @public
    * @memberof CertManagement
    * @method copyCert
    * @description 
    * @param 
    */
    var copyCert = function (mediaType, extraValue, overWrite) {
        if ((InsertNullCheck(mediaType) === false) &&
            (InsertNullCheck(extraValue) === false)) {

            var certID = certListInfo.getCertID();
            var cmd = "CertManagement.copyCert";
            var Data = {
                'mediaType': mediaType,
                'extraValue': extraValue,
                'password': NXPWD,
                'overWrite': overWrite,
                'certID': certID
            };

            CertManagement.init();
            var param = JSON.stringify(Data);
            secukitnxInterface.SecuKitNX(cmd, param);
        } else {
            hideNXBlockLayer(); KICA_Error.init();
            var location = 'CertManagement.copyCert';
            var reason = '';
            var errorcode = '';

            KICA_ERROR_RESOURCE.ErrorMessage(location, reason, errorcode);
            var ScriptErrorMessage = KICA_Error.getScriptError();
            alert(ScriptErrorMessage);
        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method copyCertCallback
     * @description copyCert 콜백함수
     * @param reply 콜백 데이터
     * @returns 
     */
    var copyCertCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {
            var flag = reply.copyCert;

            if (flag === 'true') {
                // 복사에 사용된 데이터 삭제
                CertManagement.init();

                // 결과 저장
                NX_MANAGEMENT_Result.init();
                NX_MANAGEMENT_Result.setResult(true);

                if (MANAGEMENT_ALERT_UI_SHOW === true) {

                    //복사 성공 창 표시
                    $('.nx-issue-success-alert-head-msg').remove();
                    var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                    headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_22 + '</h1>';
                    headMessage += '</div>';
                    $('#nx-issue-success-alert-head').append(headMessage);

                    $('.nx-issue-success-alert-msg').remove();
                    var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                    alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                    alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_22 + '</span></p></div>';
                    alertMessage += '</div>';
                    $('#nx-issue-succes-alert-box').append(alertMessage);

                    setTimeout(function () {
                        $('#nx-issue-success-alert').show();
                    }, 200);

                    issueFuncName.init();
                    issueFuncName.setFuncName("copyCert");

                }
                else {
                    hideNXBlockLayer();
                    SecuKitNX_Result("copyCert");
                }

            }
            else if (flag === 'exist') {
                //덮어쓰기 유무 확인창 표시
                $('#nx-cert-copy-duplication').show();
            }
        }
        else {
            // 복사에 사용된 데이터 삭제
            CertManagement.init();

            // 결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_1 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_2;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("copyCert");

            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("copyCert");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method deleteCertificate
     * @description 인증서 삭제 기능 함수
     * @param id => 사용안함
     * @param password => 사용안함
     * @param certID 삭제할 인증서 CertID
     */
    var deleteCertIssue = function (id, pwd, certID) {
        var cmd = "CertManagement.deleteCertIssue";
        var Data = {
            'ID': id,
            'password': pwd,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
    * @public
    * @memberof CertManagement
    * @method deleteCertificateCallback
    * @description 인증서 삭제 콜백 기능 함수
    * @param 
    */
    var deleteCertIssueCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {
            if (reply.deleteCertificate === 'true') {

                // 결과 저장
                NX_MANAGEMENT_Result.init();
                NX_MANAGEMENT_Result.setResult(true);

                if (MANAGEMENT_ALERT_UI_SHOW === true) {
                    $('.nx-issue-success-alert-head-msg').remove();
                    var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                    headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_3 + '</h1>';
                    headMessage += '</div>';
                    $('#nx-issue-success-alert-head').append(headMessage);

                    $('.nx-issue-success-alert-msg').remove();
                    var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                    alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_revoke.png" alt=\"인증서 폐지 아이콘\" /></div>';
                    alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_3 + '</span></p></div>';
                    alertMessage += '</div>';
                    $('#nx-issue-succes-alert-box').append(alertMessage);

                    setTimeout(function () {
                        $('#nx-issue-success-alert').show();
                    }, 200);

                    issueFuncName.init();
                    issueFuncName.setFuncName("deleteCert");
                }
                else {
                    hideNXBlockLayer();
                    SecuKitNX_Result("deleteCert");
                }

            }
        }
        else {

            // 결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_4 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_revoke.png" alt=\"인증서 폐지 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_5;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("deleteCert");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("deleteCert");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method checkPassword
     * @description 인증서 암호 확인 기능 함수
     * @param 
     */
    var checkPassword = function (certListIndex, certID) {
        var cmd = "CertManagement.checkPassword";
        var Data = {
            'certListIndex': certListIndex,
            'password': NXPWD,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
     * @public
     * @memberof CertManagement
     * @method checkPasswordCallback
     * @description 인증서 암호 확인 콜백 기능 함수
     * @param 
     */
    var checkPasswordCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //기존 형상을 위해 남겨둠 : 결과값 전달
            NX_ISSUE_Result.init();
            NX_ISSUE_Result.setValidPwdRes(true);

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                //성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_23 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                $('.nx-issue-success-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_23 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("checkPassword");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("checkPassword");
            }

        }
        else {
            //기존 형상을 위해 남겨둠 : 결과값 전달
            NX_ISSUE_Result.init();
            NX_ISSUE_Result.setValidPwdRes(false);

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_6 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_7;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("checkPassword");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("checkPassword");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method changePassword
     * @description 인증서 암호 변경 기능 함수
     * @param 
     */
    var changePassword = function () {
        var cmd = "CertManagement.changePassword";
        var certID = certListInfo.getCertID();
        var Data = {
            'newPassword': NEW_NXPWD,
            'newConfirmPassword': NEW_NXPWD_CONFIRM,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
     * @public
     * @memberof CertManagement
     * @method changePasswordCallback
     * @description 인증서 암호 변경 콜백 기능 함수
     * @param 
     */
    var changePasswordCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                //성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_24 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                $('.nx-issue-success-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_24 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("changePassword");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("changePassword");
            }

        }
        else {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_8 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_9;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("changePassword");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("changePassword");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method verifyIdentity
     * @description 신원확인 기능 함수
     * @param 
     */
    var verifyIdentity = function () {
        var cmd = "CertManagement.verifyIdentity";
        var certID = certListInfo.getCertID();
        var Data = {
            'ssn': NXSSN,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
     * @public
     * @memberof CertManagement
     * @method verifyIdentityCallback
     * @description 신원확인 콜백 기능 함수
     * @param 
     */
    var verifyIdentityCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {
            if (reply.verifyID === 'true') {

                //결과 저장
                NX_MANAGEMENT_Result.init();
                NX_MANAGEMENT_Result.setResult(true);

                if (MANAGEMENT_ALERT_UI_SHOW === true) {

                    //성공 창 표시
                    $('.nx-issue-success-alert-head-msg').remove();
                    var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                    headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_25 + '</h1>';
                    headMessage += '</div>';
                    $('#nx-issue-success-alert-head').append(headMessage);

                    $('.nx-issue-success-alert-msg').remove();
                    var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                    alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                    alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_25 + '</span></p></div>';
                    alertMessage += '</div>';
                    $('#nx-issue-succes-alert-box').append(alertMessage);

                    setTimeout(function () {
                        $('#nx-issue-success-alert').show();
                    }, 200);

                    issueFuncName.init();
                    issueFuncName.setFuncName("verifyIdentity");
                }
                else {
                    hideNXBlockLayer();
                    SecuKitNX_Result("verifyIdentity");
                }

            }
        }
        else {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_10 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_11;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("verifyIdentity");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("verifyIdentity");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method showCert
     * @description 인증서 출력(DN), 가입자 정보 수정 기능 함수
     * @param 
     */
    var showCert = function (certType, sourceString, algorithm, certID) {
        var cmd = "CertManagement.showCert";
        var Data = {
            'certType': certType,
            'sourceString': sourceString,
            'algorithm': algorithm,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
     * @public
     * @memberof CertManagement
     * @method showCertCallback
     * @description 인증서 출력(DN), 가입자 정보 수정 콜백 기능 함수
     * @param 
     */
    var showCertCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장해야함~!!!!
            NX_CertINFO_Result.init();
            NX_CertINFO_Result.setUserDN(reply.userDN);
            NX_CertINFO_Result.setSignCert(reply.signCert);
            NX_CertINFO_Result.setSigendData(reply.signedData);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                //성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_26 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                $('.nx-issue-success-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_26 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("ShowCert");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("ShowCert");
            }

        }
        else {

            //결과 저장해야함~!!!!
            NX_CertINFO_Result.init();

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_27 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_13;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("ShowCert");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("ShowCert");
            }

        }
    };

    /**
     * @public
     * @memberof CertManagement
     * @method recoverIdentity
     * @description 인증서 신원변환(신원복원) 기능 함수
     * @param 
     */
    var recoverIdentity = function () {
        var cmd = "CertManagement.recoverIdentity";
        var certID = certListInfo.getCertID();
        var Data = {
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
     * @public
     * @memberof CertManagement
     * @method recoverIdentityCallback
     * @description 인증서 신원변환(신원복원) 콜백 기능 함수
     * @param 
     */
    var recoverIdentityCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                //성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_28 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                $('.nx-issue-success-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_28 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("recoverIdentity");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("recoverIdentity");
            }

        }
        else {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_14 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_15;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("recoverIdentity");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("recoverIdentity");
            }

        }
    };

    /**
    * @public
    * @memberof CertManagement
    * @method exportP12
    * @description pfx 파일 내보내기 기능 함수
    * @param 
    */
    var exportP12 = function (certType, filePath, fileName, certID) {
        var cmd = "CertManagement.exportP12";
        var Data = {
            'certType': certType,
            'filePath': filePath,
            'fileName': fileName,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
    * @public
    * @memberof CertManagement
    * @method exportP12Callback
    * @description pfx 파일 내보내기 콜백 기능 함수
    * @param 
    */
    var exportP12Callback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                $('.nx-issue-success-alert-msg').remove();

                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_16 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                var alertMessage = '<div class=\"nx-issue-success-alert-msg\" id=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_16 + '</span></p><span class=\"inline-tit2\">' + NX_WEBUI_EX_PFX_FILE_TEXT_8 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("exportP12");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("exportP12");
            }

        }
        else {
            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_17 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_18;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("exportP12");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("exportP12");
            }

        }
    };

    /**
    * @public
    * @memberof CertManagement
    * @method importP12
    * @description pfx 파일 가져오기 기능 함수
    * @param 
    */
    var importP12 = function (filePath, fileName, password, certID) {
        var cmd = "CertManagement.importP12";
        var Data = {
            'filePath': filePath,
            'fileName': fileName,
            'password': password,
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };

    /**
    * @public
    * @memberof CertManagement
    * @method importP12Callback
    * @description pfx 파일 가져오기 콜백 기능 함수
    * @param 
    */
    var importP12Callback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                $('.nx-issue-success-alert-msg').remove();

                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_19 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                var alertMessage = '<div class=\"nx-issue-success-alert-msg\" id=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_19 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("importP12");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("importP12");
            }

        }
        else {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {
                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_20 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"실패 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_21;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("importP12");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("importP12");
            }

        }
    };

    /**
    * @public
    * @memberof CertManagement
    * @method importP12
    * @description pfx 파일 가져오기 기능 함수
    * @param 
    */
    var validateCertIssue = function (certID) {
        var cmd = "CertManagement.validateCertIssue";
        var Data = {
            'certID': certID
        };

        CertManagement.init();

        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX(cmd, param);
    };
    /**
    * @public
    * @memberof CertManagement
    * @method importP12Callback
    * @description pfx 파일 가져오기 콜백 기능 함수
    * @param 
    */
    var validateCertIssueCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(true);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {

                //성공 창 표시
                $('.nx-issue-success-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-success-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_33 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-success-alert-head').append(headMessage);

                $('.nx-issue-success-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-success-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + NX_CERT_MANAGEMENT_TEXT_34 + '</span></p></div>';
                alertMessage += '</div>';
                $('#nx-issue-succes-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-success-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("validateCertIssue");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("validateCertIssue");
            }

        } else {

            //결과 저장
            NX_MANAGEMENT_Result.init();
            NX_MANAGEMENT_Result.setResult(false);

            if (MANAGEMENT_ALERT_UI_SHOW === true) {

                // 실패 창 표시
                $('.nx-issue-fail-alert-head-msg').remove();
                var headMessage = '<div class=\"nx-issue-fail-alert-head-msg\">';
                headMessage += '<h1>' + NX_CERT_MANAGEMENT_TEXT_33 + '</h1>';
                headMessage += '</div>';
                $('#nx-issue-fail-alert-head').append(headMessage);

                // 실패 알림창 호출
                $('.nx-issue-fail-alert-msg').remove();
                var alertMessage = '<div class=\"nx-issue-fail-alert-msg\">';
                alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"성공 아이콘\" /></div>';
                alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + NX_CERT_MANAGEMENT_TEXT_35;
                alertMessage += '<br />';
                alertMessage += '<br />';
                alertMessage += '[ ErrorCode ] : ' + reply.ERROR_CODE;
                alertMessage += '<br />';
                alertMessage += '[ ErrorMessage ] : ' + reply.ERROR_MESSAGE;
                alertMessage += '</span></p></div>';
                alertMessage += '</div>';

                $('#nx-issue-fail-alert-box').append(alertMessage);

                setTimeout(function () {
                    $('#nx-issue-fail-alert').show();
                }, 200);

                issueFuncName.init();
                issueFuncName.setFuncName("validateCertIssue");
            }
            else {
                hideNXBlockLayer();
                SecuKitNX_Result("validateCertIssue");
            }

        }

    };


    return {

        init: init,
        setPwd: setPwd,
        setNEW_NXPWD: setNEW_NXPWD,
        setNEW_NXPWD_CONFIRM: setNEW_NXPWD_CONFIRM,
        setNXSSN: setNXSSN,

        //인증서 복사
        copyCert: copyCert,
        copyCertCallback: copyCertCallback,

        //인증서 삭제
        deleteCertIssue: deleteCertIssue,
        deleteCertIssueCallback: deleteCertIssueCallback,

        //인증서 암호 확인
        checkPassword: checkPassword,
        checkPasswordCallback: checkPasswordCallback,

        //인증서 암호 변경
        changePassword: changePassword,
        changePasswordCallback: changePasswordCallback,

        //신원 확인
        verifyIdentity: verifyIdentity,
        verifyIdentityCallback: verifyIdentityCallback,

        //인증서 출력, 가입자 정보 수정
        showCert: showCert,
        showCertCallback: showCertCallback,

        //신원변환(신원복원)
        recoverIdentity: recoverIdentity,
        recoverIdentityCallback: recoverIdentityCallback,

        //PFX 내보내기
        exportP12: exportP12,
        exportP12Callback: exportP12Callback,

        //PFX 가져오기
        importP12: importP12,
        importP12Callback: importP12Callback,

        //인증서 유효성 검증
        validateCertIssue: validateCertIssue,
        validateCertIssueCallback: validateCertIssueCallback
    };
})();