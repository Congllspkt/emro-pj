/**
 * @public
 * @class
 * @description   WebUI 상호연동 함수 정의 class
 */
var Dialog = (function () {

    var initDialog = function () {
        //인증서 선택창 리스트 초기화 필요
        NX_Issue_pubUi.certListReset();
    };

    /**
   * @public
   * @memberof Dialog
   * @method selectStorage
   * @description 선택된 미디어 인증서 전체 리스트를 가져온다
   * @param {String} mediaType 저장매체 종류(HDD, HSM, USB등)
   * @param {String} extraValue HDD 선택시에는 NULL, HSM 선택시에는 NULL 또는 Drive명
   */
    var selectStorage = function (mediaType, extraValue) {
        if (InsertNullCheck(mediaType) === false) {
            // 선택된 저장매체가 USIM인 경우 사이트 코드를 할당한다.
            if (mediaType === "USIM" || mediaType === "usim") {
                extraValue = USIM_SITECODE;
            }

            var logicFlag = processLogic.getProcessLogic();
            var cmd;
            if ((logicFlag.indexOf('ISSUE') !== -1) || (logicFlag.indexOf('MANAGEMENT') !== -1)) {
                // 발급모듈에서 진행한 경우
                cmd = 'Dialog.selectStorageIssue';
            } else {
                // 이용모듈에서 진행한 경우
                cmd = 'Dialog.selectStorage';
            }
            var Data = {
                'mediaType': mediaType,
                'extraValue': extraValue
            };
            var param = JSON.stringify(Data);
            secukitnxInterface.SecuKitNX(cmd, param);
        } else {
            $('.nx-cert-select').hide(); hideNXBlockLayer(); KICA_Error.init();
            var location = 'Dialog.selectStorage';
            var reason = 'mediaType';
            KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
            var ScriptErrorMessage = KICA_Error.getScriptError();
            alert(ScriptErrorMessage);
        }
    };

    /**
     * @public
     * @memberof Dialog
     * @method selectStorageCallback
     * @description selectStorage 콜백함수
     * @param reply 콜백 데이터
     * @returns 
     *          mediaType HDD 입력 : 인증서 리스트
     *          mediaType HSM 입력 : 장치목록 (Driver 목록), HSM & Driver명 입력 : 인증서 리스트 
     */
    var selectStorageCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {
        	//비밀번호 횟수 초기화
        	NX_PW_COUNT_GET = NX_PW_COUNT_SET;

            //인증서 리스트 정보 저장
            certListInfo.setCertListInfo(reply);

            //하드디스크
            if (reply.mediaType === 'HDD') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // 비밀번호 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_8;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_9;
            }

            //이동식 디스크
            if (reply.mediaType === 'USB') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // 비밀번호 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_8;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_9;
            }

            //USIM
            if (reply.mediaType === 'USIM') {
            }

            //보안토큰
            if (reply.mediaType === 'HSM') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // PIN 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_10;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_11;

                $('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가

                setTimeout(function () {
                    try {
                        $("#nx_cert_pin").focus();
                    } catch (e) { }

                }, 500);
            }

            //지문보안토큰
            if (reply.mediaType === 'BIOHSM') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // PIN 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_10;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_11;

                $('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가

                setTimeout(function () {
                    try {
                        $("#nx_cert_pin").focus();
                    } catch (e) { }

                }, 500);
            }

            //안전디스크
            if (reply.mediaType === 'SECUREDISK') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // 비밀번호 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_8;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_9;
            }

            //휴대폰 인증저장 (유비키)
            if (reply.mediaType === 'PHONE') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // 비밀번호 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_8;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_9;
            }
            if (reply.mediaType === 'PHONE' && reply.extraValue === "NONE_UBIKEY") {
                alert(NX_WEBUI_SELECT_TEXT_12);
                $("#MediaSet_1>li>button.active").removeClass("active");
                // 휴대폰 저장소 다운로드 팝업 정보
                var PopUPWindow = window.open(INFOVINE_UBYKEY_URL, "", "width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes");
                return;
            }

            var pwdObj = document.getElementById("certPwd");
            if ((reply.mediaType === 'USIM') || (reply.mediaType === 'HSM') || (reply.mediaType === 'BIOHSM')) {

                pwdObj.disabled = true;
                pwdObj.style.backgroundColor = "#CCCCCC";

            }
            else {
                // 스마트인증 & 보안토큰 & 지문보안토큰이 아닌 경우 비밀번호 입력창 활성화
                pwdObj.disabled = false;
                pwdObj.style.backgroundColor = "#ffffff";

            }

            // 초기화
            initTextAll();

            // 첫번째 인증서 default로 선택 될 수 있도록 코드 추가 - 160414
            NX_Issue_pubUi.certSelectRow_CertList(1);
            NX_SELECTED_ROW = 1;
            // gpps에서 백업 인증서 설정이 된 경우 첫번째 인증서 설정하는 부분은 Skip
            var tmpGpps = backupIssueCertDN.getCertDN();
            if (tmpGpps === "")
            {
                certListInfo.setCertListIndex(reply[1].index);
                NX_SELECTED_ROW = reply[1].index;
            }

            // 인증서 리스트 테이블 조정
            $("#divtable").colResizable({ liveDrag: true });

            setTimeout(function () {
                try {
                    $("#certPwd").focus();
                } catch (e) { }
            }, 500);

        }
        else if (reply.ERROR_CODE) {
            KICA_Error.init();
            var mediaType = SelectMediaInfo.getMediaType();
            if ((reply.ERROR_CODE === '357236736') && (mediaType === 'BIOHSM')) {
                var location = '';
                var reason = '';
                KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_BIOHSM_PIN_LOCK');
                var ScriptErrorMessage = KICA_Error.getScriptError();
                alert(ScriptErrorMessage);

                // 인증서 선택창이 사라져...임시로 넣어둠..
                $('#nx-pki-ui-wrapper').show();
                $('#nx-cert-select').show();

            }else {
                KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
                var errorMsg = KICA_Error.getError();
                alert(errorMsg);
            }
        }
    };

    /**
     * @public
     * @memberof Dialog
     * @method selectStorageCallback
     * @description selectStorage 콜백함수
     * @param reply 콜백 데이터
     * @returns 
     *          mediaType HDD 입력 : 인증서 리스트
     *          mediaType HSM 입력 : 장치목록 (Driver 목록), HSM & Driver명 입력 : 인증서 리스트 
     */
    var selectStorageIssueCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }

        if (errorCheck === undefined) {

            //인증서 리스트 정보 저장
            certListInfo.setCertListInfo(reply);

            //하드디스크
            if (reply.mediaType === 'HDD') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);
            }

            //이동식 디스크
            if (reply.mediaType === 'USB') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

            }

            //USIM
            if (reply.mediaType === 'USIM') {
            }

            //보안토큰
            if (reply.mediaType === 'HSM') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // PIN 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_10;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_11;

                $('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가

                setTimeout(function () {
                    try {
                        $("#nx_cert_pin").focus();
                    } catch (e) { }

                }, 500);
            }

            //지문보안토큰
            if (reply.mediaType === 'BIOHSM') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

                // PIN 입력 안내
                document.getElementById("pwdnoti_1").innerHTML = NX_WEBUI_SELECT_TEXT_10;
                document.getElementById("pwdnoti_2").innerHTML = NX_WEBUI_SELECT_TEXT_11;

                $('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가

                setTimeout(function () {
                    try {
                        $("#nx_cert_pin").focus();
                    } catch (e) { }

                }, 500);
            }

            //안전디스크
            if (reply.mediaType === 'SECUREDISK') {
                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

            }

            var pwdObj = document.getElementById("certPwd");
            if ((reply.mediaType === 'USIM') || (reply.mediaType === 'HSM') || (reply.mediaType === 'BIOHSM')) {

                pwdObj.disabled = true;
                pwdObj.style.backgroundColor = "#CCCCCC";

            }
            else {
                // 스마트인증 & 보안토큰 & 지문보안토큰이 아닌 경우 비밀번호 입력창 활성화
                pwdObj.disabled = false;
                pwdObj.style.backgroundColor = "#ffffff";

            }

            // 초기화
            initTextAll();

            // 첫번째 인증서 default로 선택 될 수 있도록 코드 추가 - 160414
            NX_Issue_pubUi.certSelectRow_CertList(1);
            NX_SELECTED_ROW = 1;
            // gpps에서 백업 인증서 설정이 된 경우 첫번째 인증서 설정하는 부분은 Skip
            var tmpGpps = backupIssueCertDN.getCertDN();
            if (tmpGpps === "") {
                certListInfo.setCertListIndex(reply[1].index);
                NX_SELECTED_ROW = reply[1].index;
            }

            // 인증서 리스트 테이블 조정
            $("#divtable").colResizable({ liveDrag: true });

            setTimeout(function () {
                try {
                    $("#certPwd").focus();
                } catch (e) { }

            }, 500);
        }
        else if (reply.ERROR_CODE) {
            KICA_Error.init();
            var mediaType = SelectMediaInfo.getMediaType();
            if (reply.ERROR_CODE === '0') {
                //console.log(NX_MEMBER_OF_DIALOG_TEXT_1);

                var listObj = new MakeCertList();
                listObj.init();
                var data = listObj.makeCertListHTML(reply);
                $('#select-cert-list #select-cert-list-area').remove();
                $('#select-cert-list').append(data);

            }
            else if ((reply.ERROR_CODE === '357236736') && (mediaType === 'BIOHSM')) {
                var location = '';
                var reason = '';
                KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_BIOHSM_PIN_LOCK');
                var ScriptErrorMessage = KICA_Error.getScriptError();
                alert(ScriptErrorMessage);

                // 인증서 선택창이 사라져...임시로 넣어둠..
                $('#nx-pki-ui-wrapper').show();
            }
            else {
                KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
                var errorMsg = KICA_Error.getError();
                alert(errorMsg);
            }
        }
    };

    /**
   * @public
   * @memberof Dialog
   * @description 인증서 선택 및 비밀번호 입력 후 단계 
   * @param {String}  ID 인증서 DN, Serial   
   * @param {String}  password 인증서 비밀번호
   * @param {String} certID  CertID 또는 Null
   */
    var selectCertificate = function (ID, password, certID) {
        if ((InsertNullCheck(ID) === false) && (InsertNullCheck(password) === false)) {
            var logicFlag = processLogic.getProcessLogic();
            var cmd;
            if ((logicFlag.indexOf('ISSUE') !== -1) || (logicFlag.indexOf('MANAGEMENT') !== -1)) {
                // 발급모듈에서 진행한 경우
                cmd = 'Dialog.selectCertificateIssue';
            } else {
                // 이용모듈에서 진행한 경우
                cmd = 'Dialog.selectCertificate';
            }
            var Data = {
                'ID': ID,
                'password': password,
                'certID': certID
            };
            var param = JSON.stringify(Data);
            secukitnxInterface.SecuKitNX(cmd, param);
        } else {
            KICA_Error.init();
            var location = 'Dialog.selectCertificate';
            var reason = '';

            if (InsertNullCheck(ID)) {
                reason = 'ID';
            }

            if (InsertNullCheck(password)) {
                reason = 'password';
            }

            KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
            var ScriptErrorMessage = KICA_Error.getScriptError();
            alert(ScriptErrorMessage);
        }
    };

    /**
     * @public
     * @memberof GetCertInfo
     * @description 인증서 선택 및 비밀번호 입력 후 선택된 인증서의 CertID를 콜백 받는 함수
     * @param reply 콜백 데이터
     * @returns
     *       성공 : {String} CertId   실패 : false
     *       
     *       인증서 선택창에서 인증서 선택 후 사용자가 해당 저장매체에서 인증서를 삭제한 경우 에러 처리 
     *       ex)'선택된 인증서가 없습니다.' 
     */
    var selectCertificateCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }
        
        // 초기화
        initTextAll();
        if (errorCheck === undefined) {
            certListInfo.setCertID(reply.certID);
            var logic = processLogic.getProcessLogic();
            SecuKitNXS_RESULT(logic, reply.certID);
            // WebUI 백그라운드 화면 버튼 클릭 차단 해제
            hideNXBlockLayer();
        }
        else if (reply.ERROR_CODE) {
            if (RAON_F === true) {
                TK_Clear("formRe", "certPwd"); // 라온시큐어 키보드보안 메모리 해제
            }

            KICA_Error.init();
            KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
            var errorMsg = KICA_Error.getError();
            alert(errorMsg);
            
            if(reply.ERROR_CODE === '338821120' || reply.ERROR_CODE === '357105664' || 
            		reply.ERROR_CODE === '346234880' || reply.ERROR_CODE === '346304512'){
            	if(NX_PW_COUNT){
            		NX_PW_COUNT_GET = NX_PW_COUNT_GET - 1;
            		
            		if(NX_PW_COUNT_GET > 0){
            			alert('인증서 비밀번호 입력 가능 횟수가 ' + NX_PW_COUNT_GET + '회 남았습니다.');

            		}else{
            			alert('인증서 비밀번호를 ' + NX_PW_COUNT_SET + '회 틀리셨습니다. 확인 후 다시 시도해 주십시오.');
            			certListInfo.setCertListIndex('');
            			//갱신창이 열려있는 경우 닫기
            			$('#nx-error-cert-update').fadeOut('fast');
            			initTextAll();
            			hideNXBlockLayer();
            			//비밀번호 횟수 초기화
            			NX_PW_COUNT_GET = NX_PW_COUNT_SET;
            		}
            		
            	}
            	
            	if (reply.ERROR_CODE === '346234880' || reply.ERROR_CODE === '346304512') {
            		$('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가
            		
            		setTimeout(function () {
            			try {
            				$("#nx_cert_pin").focus();
            			} catch (e) { }
            			
            		}, 500);
            	}
            }
        }
    };

    /**
     * @public
     * @memberof GetCertInfo
     * @description 인증서 선택 및 비밀번호 입력 후 선택된 인증서의 CertID를 콜백 받는 함수
     * @param reply 콜백 데이터
     * @returns
     *       성공 : {String} CertId   실패 : false
     *       
     *       인증서 선택창에서 인증서 선택 후 사용자가 해당 저장매체에서 인증서를 삭제한 경우 에러 처리 
     *       ex)'선택된 인증서가 없습니다.' 
     */
    var selectCertificateIssueCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            //console.log(err);
        }
        
        // 초기화
        initTextAll();
        if (errorCheck === undefined) {
            certListInfo.setCertID(reply.certID);

            //인증서 정보 추출 함수 호출
            var certType = 'SignCert';
            var certID = certListInfo.getCertID();

            var Logic_flag = processLogic.getProcessLogic();
            if (Logic_flag.indexOf('RenewCertificateInfo') !== -1) {
                Dialog.viewCertInfomationWithVID(certType, certID);
            }
            else {
                NX_branchLogic_ISSUE();
                $('.nx-cert-select').hide();
            }
        }
        else {
            if (RAON_F === true) {
                TK_Clear("formRe", "certPwd"); // 라온시큐어 키보드보안 메모리 해제
            }

            KICA_Error.init();
            KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
            var errorMsg = KICA_Error.getError();
            alert(errorMsg);

            if(reply.ERROR_CODE === '338821120' || reply.ERROR_CODE === '357105664' ||
            		reply.ERROR_CODE === '346234880' || reply.ERROR_CODE === '346304512'){
            	if(NX_PW_COUNT){
            		NX_PW_COUNT_GET = NX_PW_COUNT_GET - 1;
            		
            		if(NX_PW_COUNT_GET > 0){
            			alert('인증서 비밀번호 입력 가능 횟수가 ' + NX_PW_COUNT_GET + '회 남았습니다.');
            			
            		}else{
            			alert('인증서 비밀번호를 ' + NX_PW_COUNT_SET + '회 틀리셨습니다. 확인 후 다시 시도해 주십시오.');
            			certListInfo.setCertListIndex('');
            			//갱신창이 열려있는 경우 닫기
            			$('#nx-error-cert-update').fadeOut('fast');
            			initTextAll();
            			hideNXBlockLayer();
            			//비밀번호 횟수 초기화
            			NX_PW_COUNT_GET = NX_PW_COUNT_SET;
            		}
            	}
            	
            	if (reply.ERROR_CODE === '346234880' || reply.ERROR_CODE === '346304512') {
            		$('.cert-location-area .pki-wrap6').fadeIn('fast');			// 코드 추가
            	}
            }
        }
    };

    /**
* @public-
* @memberof Dialog
* @description 인증서 선택 후 세부 정보 리스트를 요청하는 함수
* @param {String} ID 인증서 리스트상의 순번(1부터 시작)
*/
    viewCertInfomationWithVID = function (certType, certID) {
        var isViewVID = '0';    // 0 : VID 추출 안함,  1 : VID 추출
        if ((InsertNullCheck(certID) === false)) {
            var logicFlag = processLogic.getProcessLogic();
            if ((logicFlag.indexOf('ISSUE') !== -1) || (logicFlag.indexOf('MANAGEMENT') !== -1)) {
                // 발급모듈에서 진행한 경우
                var cmd = 'Dialog.viewCertInfomationWithVIDIssue';
            } else {
                // 이용모듈에서 진행한 경우
                var cmd = 'Dialog.viewCertInfomationWithVID';
            }

            var Data = {
                'certType': certType,
                'certID': certID,
                'isViewVID': isViewVID
            };
            var param = JSON.stringify(Data);
            secukitnxInterface.SecuKitNX(cmd, param);
        } else {
            KICA_Error.init();
            var location = 'Dialog.viewCertInfomationWithVID';
            var reason = 'certID';
            KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
            var ScriptErrorMessage = KICA_Error.getScriptError();
            alert(ScriptErrorMessage);
        }
    };

    /**
    * @public
    * @memberof Dialog
    * @description 인증서 세부 정보 리스트를 콜백 받는 함수
    * @param reply 콜백 데이터
    * @returns
    *       성공 : {Json String} 인증서 세부 정보 리스트   실패 : ERROR_CODE:에러코드, ERROR_MESSAGE:에러메시지
    */
    viewCertInfomationWithVIDIssueCallback = function (reply) {
        var errorCheck = -1;
        try {
            errorCheck = reply.ERROR_CODE;
        } catch (err) {
            console.log(err);
        }

        if (errorCheck === undefined) {
            //인증서 정보 저장
            CertInfo.init();
            CertInfo.setCertInfo(reply);

            NX_branchLogic_ISSUE();
            $('.nx-cert-select').hide();
        }
        else {
            $('#certPwd').val('');
            KICA_Error.init();
            KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
            var errorMsg = KICA_Error.getError();
            alert(errorMsg);
        }
    };

    return {
        initDialog: initDialog,

        selectStorage: selectStorage,
        selectStorageCallback: selectStorageCallback,
        selectStorageIssueCallback: selectStorageIssueCallback,

        selectCertificate: selectCertificate,
        selectCertificateCallback: selectCertificateCallback,
        selectCertificateIssueCallback: selectCertificateIssueCallback,

        viewCertInfomationWithVID: viewCertInfomationWithVID,
        viewCertInfomationWithVIDIssueCallback: viewCertInfomationWithVIDIssueCallback
    };

})();

/**
 * @public
 * @memberof Dialog
 * @description 인증서 선택 후 세부 정보 리스트를 요청하는 함수
 * @param {String} ID 인증서 리스트상의 순번(1부터 시작)
 */
function NXviewCertDetailInfomation(ID) {
    if (InsertNullCheck(ID) === false) {
        var logicFlag = processLogic.getProcessLogic();
        var cmd;
        if ((logicFlag.indexOf('ISSUE') !== -1) || (logicFlag.indexOf('MANAGEMENT') !== -1)) {
            // 발급모듈에서 진행한 경우
            cmd = 'NXviewCertDetailInfomation.viewCertDetailInfomationIssue';
        } else {
            // 이용모듈에서 진행한 경우
            cmd = 'NXviewCertDetailInfomation.viewCertDetailInfomation';
        }

        var Data = {
            'ID': ID
        };
        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX_EX(cmd, param);
    } else {
        KICA_Error.init();
        var location = 'Dialog.viewCertDetailInfomation';
        var reason = 'ID';
        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
        var ScriptErrorMessage = KICA_Error.getScriptError();
        alert(ScriptErrorMessage);
    }
}

/**
 * @public
 * @memberof Dialog
 * @description 인증서 세부 정보 리스트를 콜백 받는 함수
 * @param reply 콜백 데이터
 * @returns
 *       성공 : {Json String} 인증서 세부 정보 리스트   실패 : ERROR_CODE:에러코드, ERROR_MESSAGE:에러메시지
 *
 * CertInfo = {
    *       CertString : 서명용 인증서 ex) MIIFzzCCBLegAwIBAgIEArGBxDANBgkqhkiG9w0BAQsFADBKMQswCQYDVQQGEwJLUjENMA...,
    *       UserDN : DN ex) cn=테스트(법인-A),ou=RA센터,ou=TEST인증서,ou=TEST발급용,ou=licensedCA,o=KICA,c=KR,
    *       SignatureAlgorithmID : 서명 알고리즘 정보 ex) sha256WithRSAEncryption,
    *       Version : 인증서 버전 정보 ex) Version 3,
    *       Serial : 시리얼 정보,
    *       IssueDN : 발행기관 DN  ex) cn=signGATE CA4,ou=AccreditedCA,o=KICA,c=KR,
    *       Issuer : 발행기관 ex) KICA,
    *       UserPublicKeyAlgorithmID : ex) rsaEncryption,
    *       ValidateFrom : 인증서 유효기간 ex) 2014-06-23 11:32:00,
    *       ValidateTo : 인증서 유효기간 ex) 2015-07-19,
    *       UserName : ex) 테스트(법인-A),
    *       Usage : ex) digitalSignature,nonRepudiation,
    *       Policy : ex) 1.2.410.200004.5.2.1.1,
    *       AuthorityKeyID :  ex) ae52fd0e0e01f83086377e...,
    *       UserKeyID : ex) bf83c19c48c310ee47473ec38696...,
    *       PublicKey : ex) 3082010a0282010100cd344ba481a86876a7a3e52681f4f34e287e1695657ae308031725fafe8af56dda1c...,
    *       PublicKeySize: ex) 2160
    *       }
    */
function NXviewCertDetailInfomationCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        //console.log(err);
    }

    if (errorCheck === undefined) {
        var makeCertListObj = new MakeCertList();
        var data = makeCertListObj.makeCertDetailInfoHTML(reply);
        $('#cert-detail-info').remove();
        $('#certContent_01_box').append(data);
        //인증서 선택창 hide
        $('#nx-cert-select').hide();
        //인증서 정보창 show
        $('#nx-cert-detail-info').show();
    }
    else {
        KICA_Error.init();
        KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
        var errorMsg = KICA_Error.getError();
        alert(errorMsg);
    }
}

//인증서 선택창(Web UI) 노출을 위한 환경 세팅 함수 호출
//기존 인증서 선택창 호출 함수와 호환을 위해 함수명은 그대로 사용...
function NX_ShowDialog() {

    if ($('#nx-pki-ui-wrapper').length === 0) {
        // KICA WebUI append
        $('#KICA_SECUKITNXDIV_ID').append(KICA_SECUKITNXDIV);
    }

    if (SECUKITNX_CONST.load) {
        try {
            // 환경 설정 함수 호출
            var flag = processLogic.getProcessLogic();
            if ((flag.indexOf('ISSUE') !== -1) || (flag.indexOf('MANAGEMENT') !== -1)) {
                setTimeout(function () {  // 환경설정 Set
                    NXIssueConfigSetDialog();
                }, 100);
            } else {
                setTimeout(function () {  // 환경설정 Set
                    NXConfigSetDialog();
                }, 100);
            }
        } catch (e) { }
        
        //키보드 보안 사용 확인
        if(CUSTENC === true){
        	NX_Issue_pubUi.custEnc();
        }
    } else {
        KICA_Error.init();
        var location = '';
        var reason = '';
        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_CLIENT_NO_LOADING');
        var ScriptErrorMessage = KICA_Error.getScriptError();
        alert(ScriptErrorMessage);
    }
}

/***********************************************
     인증서 발급&재발급 등이 실행되기전 환경 세팅 함수
************************************************/
function NXIssueConfigSetDialog() {

    // CA IP & PORT 설정
    NXsetCommonInfoDialog();

    // 저장매체 정보를 저장하는 함수 초기화
    TargetMediaInfo.init();

    // 저장매체 환경 Set에 맞게 저장위치 생성
    NXsetBanner_TargetMedia();
    TargetDialogSet();
    NXStopMediaSelect2();

    // 인증서 선택창 미디어 세팅
    SelectDialogSet();
    NXStopMediaSelect();

    var msg = NX_MEDIA_SELECT_TEXT_4;
    document.getElementById("targetMedia_noti").innerHTML = msg;
}

/***********************************************
     인증서 선택창 실행 전 환경 세팅 함수
************************************************/
function NXConfigSetDialog() {

    // CA IP & PORT 설정
    NXsetCommonInfoDialog();

    // 저장매체 정보를 저장하는 함수 초기화
    TargetMediaInfo.init();

    // 저장매체 환경 Set에 맞게 저장위치 생성
    NXsetBanner_TargetMedia();
    TargetDialogSet();
    NXStopMediaSelect2();

    // 인증서 선택창 미디어 세팅
    SelectDialogSet();
    NXStopMediaSelect();
}

/***********************************************
환경 설정 변수 SET 함수 모음
************************************************/
function NXsetCommonInfoDialog() {
    // 인증서 선택창 최초 실행시 cmd 목록 초기화
    //SECUKITNX_CONST.cmdCnt = [];

    var logicFlag = processLogic.getProcessLogic();
    var cmd;
    if ((logicFlag.indexOf('ISSUE') !== -1) || (logicFlag.indexOf('MANAGEMENT') !== -1)) {
        // 발급모듈에서 진행한 경우
        cmd = 'NXsetCommonInfoDialog.setCommonInfoIssue';
    } else {
        // 이용모듈에서 진행한 경우
        cmd = 'NXsetCommonInfoDialog.setCommonInfo';
    }
    var Data = {
        'caIP': NX_CA_IP,
        'caCmpPort': NX_CA_PORT,
        'anyPolicy': NX_AnyPolicy,
        'policies': NX_POLICIES

    };
    var param = JSON.stringify(Data);
    secukitnxInterface.SecuKitNX_EX(cmd, param);
}

function NXsetCommonInfoDialogCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        nxlog(err);
    }

    if (errorCheck === undefined) {

        //인증서 선택창 WebUI 노출
        Dialog.initDialog();
        NX_Issue_pubUi.certListReset();
        setTimeout(function () {
            $('#nx-background-block-layer').show();
            $('#nx-pki-ui-wrapper').show();
            $('#nx-cert-select').show();
            try {
                $("#certPwd").attr("tabindex", -1).focus();
            } catch (e) { }

            //디폴트로 하드디스크 저장매체 선택 표시시
            var flag = NX_SELECT_CERT_MEDIA[0].DEFAULT;
            var flag2 = NX_SELECT_CERT_MEDIA[0].ABLE;
            if (flag == 'able' && flag2 =='able') {
                NX_SetMediaHDD();
            }

        }, 100);

        if (NX_DIALOG_MOVE === true) {
            try {
                //인증서 선택창 커서 이동을 위해 삽입
                //$(".nx-pki-ui-wrapper").draggable({ handle: ".pki-head2", cursor: "move" });
            } catch (e) { }
        }

    }
    else {
        KICA_Error.init();
        KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
        var errorMsg = KICA_Error.getError();
        alert(errorMsg);
    }
}