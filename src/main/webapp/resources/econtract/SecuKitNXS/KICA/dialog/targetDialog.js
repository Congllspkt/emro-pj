/***********************************************
    인증서 선택창 미디어 세팅
************************************************/
function SelectDialogSet() {
    var length = NX_SELECT_CERT_MEDIA.length;
    var res = '';

    var media = '';
    var order = '';
    var able = '';
    var Upper_able = '';

    var j = 0;

    // 기존에 붙어 있던 저장매체 영역 삭제
    $('.cert-location-area-select-media ul').remove();

    //var cnt = 0;    // 저장매체 공간 체크
    res = '<ul id="MediaSet_1">';
    var i;
    for (i = 1; i <= length; i++) {
        var index = 0;
        while (true) {
            media = NX_SELECT_CERT_MEDIA[index].MEDIA;
            order = NX_SELECT_CERT_MEDIA[index].ORDER;
            able = NX_SELECT_CERT_MEDIA[index].ABLE;
            index++;

            if (order === i) {
                break;
            }
        }

        Upper_able = able.toUpperCase();

        // 확장 매체 시작시
        if (i === 5) {

            if (NX_SELECT_CERT_MEDIA[5].ABLE === 'able') {
                res += "<li><button tabindex=\"0\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "MEDIA_LIST.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow();\">" + NX_TARGET_DIALOG_TEXT_1 + "</button>";
                res += "<div class='sub-layer'>";
                res += "<div class='inner' id='MediaSet_1-sub'>";
            } else {
                // 확장 매체 disable
                res += "<li><button disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "MEDIA_LIST.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow();\">" + NX_TARGET_DIALOG_TEXT_1 + "</button>";
                res += "<div class='sub-layer'>";
                res += "<div class='inner' id='MediaSet_1-sub'>";
            }
        }

        if (i >= 5) {
            // 추가확장매체에 표시시
            switch (media) {
                case 'HDD':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_HDD_EX\" name=\"NX_MEDIA_HDD_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\" onclick=\"NX_SetMediaHDD();\">" + NX_TARGET_DIALOG_TEXT_2 + "</button> ";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_HDD_EX\" name=\"NX_MEDIA_HDD_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\">" + NX_TARGET_DIALOG_TEXT_2 + "</button>";
                    }
                    break;

                case 'USB':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_USB_EX\" name=\"NX_MEDIA_USB_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2();\">" + NX_TARGET_DIALOG_TEXT_3 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_USB_EX\" name=\"NX_MEDIA_USB_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\">" + NX_TARGET_DIALOG_TEXT_3 + "</button> ";
                    }
                    break;

                case 'USIM':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_USIM_EX\" name=\"NX_MEDIA_USIM_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('USIM','NULL');\">" + NX_TARGET_DIALOG_TEXT_4 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_USIM_EX\" name=\"NX_MEDIA_USIM_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\">" + NX_TARGET_DIALOG_TEXT_4 + "</button>";
                    }
                    break;

                case 'HSM':
                case 'SECURE_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_HSM_EX\" name=\"NX_MEDIA_HSM_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('HSM','NULL');\">" + NX_TARGET_DIALOG_TEXT_5 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_HSM_EX\" name=\"NX_MEDIA_HSM_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_5 + "</button>";
                    }
                    break;

                case 'BIOHSM':
                case 'BIO_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_BIOHSM_EX\" name=\"NX_MEDIA_BIOHSM_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('BIOHSM','" + BioTokenP7Message + "');\">" + NX_TARGET_DIALOG_TEXT_6 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_BIOHSM_EX\" name=\"NX_MEDIA_BIOHSM_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_6 + "</button>";
                    }
                    break;

                case 'SECUREDISK':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_SECUREDISK_EX\" name=\"NX_MEDIA_SECUREDISK_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECUREDISK.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('SECUREDISK','NULL');\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_SECUREDISK_EX\" name=\"NX_MEDIA_SECUREDISK_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECUREDISK.png')\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    break;

                case 'PHONE': //유비키
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_PHONE_EX\" name=\"NX_MEDIA_PHONE_EX\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "PHONE.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('PHONE','NULL');\">" + "휴대폰저장소" + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_PHONE_EX\" name=\"NX_MEDIA_PHONE_EX\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "PHONE.png')\">" + "휴대폰저장소" + "</button>";
                    }
                    break;
            }//end switch

        }
        else {
            // 앞단에 표시시
            switch (media) {
                case 'HDD':
                    if (Upper_able === 'ABLE') {
                        var flag = NX_SELECT_CERT_MEDIA[0].DEFAULT;
                        if (flag === 'able') {
                            res += "<li> <button id=\"NX_MEDIA_HDD\" name=\"NX_MEDIA_HDD\" class=\"active\" style=\"background-image:url(\'" + NX_MEDIA_IMG_URL + "HDD.png\')\" onclick=\"NX_SetMediaHDD();\">" + NX_TARGET_DIALOG_TEXT_2 + "</button></li> ";
                        } else {
                            res += "<li> <button id=\"NX_MEDIA_HDD\" name=\"NX_MEDIA_HDD\" style=\"background-image:url(\'" + NX_MEDIA_IMG_URL + "HDD.png\')\" onclick=\"NX_SetMediaHDD();\">" + NX_TARGET_DIALOG_TEXT_2 + "</button></li> ";
                        }
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_HDD\" name=\"NX_MEDIA_HDD\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\">" + NX_TARGET_DIALOG_TEXT_2 + "</button></li> ";
                    }
                    break;

                case 'USB':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"NX_MEDIA_USB\" name=\"NX_MEDIA_USB\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('USB', 'NULL');\">" + NX_TARGET_DIALOG_TEXT_3 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_USB\" name=\"NX_MEDIA_USB\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\">" + NX_TARGET_DIALOG_TEXT_3 + "</button></li> ";
                    }
                    break;

                case 'USIM':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"NX_MEDIA_USIM\" name=\"NX_MEDIA_USIM\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('USIM','NULL');\">" + NX_TARGET_DIALOG_TEXT_4 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_USIM\" name=\"NX_MEDIA_USIM\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\">" + NX_TARGET_DIALOG_TEXT_4 + "</button></li> ";
                    }
                    break;

                case 'HSM':
                case 'SECURE_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"NX_MEDIA_HSM\" name=\"NX_MEDIA_HSM\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('HSM','NULL');\">" + NX_TARGET_DIALOG_TEXT_5 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_HSM\" name=\"NX_MEDIA_HSM\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_5 + "</button></li> ";
                    }
                    break;

                case 'BIOHSM':
                case 'BIO_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"NX_MEDIA_BIOHSM\" name=\"NX_MEDIA_BIOHSM\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('BIOHSM', '" + BioTokenP7Message + "');\">" + NX_TARGET_DIALOG_TEXT_6 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_BIOHSM\" name=\"NX_MEDIA_BIOHSM\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_6 + "</button></li> ";
                    }
                    break;

                case 'SECUREDISK':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"NX_MEDIA_SECUREDISK\" name=\"NX_MEDIA_SECUREDISK\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECUREDISK.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('SECUREDISK','NULL');\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    else {
                        res += "<button id=\"NX_MEDIA_SECUREDISK\" name=\"NX_MEDIA_SECUREDISK\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECUREDISK.png')\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    break;

                case 'PHONE': //유비키
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"NX_MEDIA_PHONE\" name=\"NX_MEDIA_PHONE\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "PHONE.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow2('PHONE', 'NULL');\">" + "휴대폰저장소" + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"NX_MEDIA_PHONE\" name=\"NX_MEDIA_PHONE\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "PHONE.png')\">" + "휴대폰저장소" + "</button></li> ";
                    }

                    break;
            }//end switch
        }

        //확장매체 종료
        if (i === length) {
            res += '<a href="javascript:void(0)" class="btn-close" onclick="NX_Issue_pubUi.moreSaveMediaHide(); NX_Issue_pubUi.moreSaveMediaHideFocusMediaList(); return false;">' + NX_TARGET_DIALOG_TEXT_7 + '</a> </div></div></li>';
        }

    }//end for          

    res += '</ul>';

    $('.cert-location-area-select-media').append(res);
}


/**
  * @public
  * @memberof Dialog
  * @method selectStorage
  * @description 선택된 미디어 인증서 전체 리스트를 가져온다
  * @param {String} mediaType 저장매체 종류(HDD, HSM, USB등)
  * @param {String} extraValue HDD 선택시에는 NULL, HSM 선택시에는 NULL 또는 Drive명
  */
function NXselectStorageIssue(mediaType, extraValue) {
    if (InsertNullCheck(mediaType) === false) {
        // 선택된 저장매체가 USIM인 경우 사이트 코드를 할당한다.
        if (mediaType === "USIM" || mediaType === "usim") {
            extraValue = USIM_SITECODE;
        }

        var cmd = 'NXselectStorageIssue.selectStorageIssue';
        var Data = {
            'mediaType': mediaType,
            'extraValue': extraValue
        };
        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX_EX(cmd, param);
    } else {
        hideNXBlockLayer(); KICA_Error.init();
        var location = 'NXselectStorageIssue';
        var reason = 'mediaType';
        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
        var ScriptErrorMessage = KICA_Error.getScriptError();
        alert(ScriptErrorMessage);
    }
}

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
function NXselectStorageIssueCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        //console.log(err);
    }

    if (errorCheck === undefined) {


    }
    else if (reply.ERROR_CODE) {
        hideNXBlockLayer(); KICA_Error.init();
        if (reply.ERROR_CODE === '357236736') {
            //console.log('');
        } else {
            KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
            var errorMsg = KICA_Error.getError();
            alert(errorMsg);
        }
    }
}

/***********************************************
    발급&재발급&복사 : 인증서 저장할 미디어창 세팅 
************************************************/
function TargetDialogSet() {
    var length = NX_TARGET_MEDIA.length;
    var res = '';

    var media = '';
    var order = '';
    var able = '';
    var Upper_able = '';

    var j = 0;

    // 기존에 붙어 있던 저장매체 영역 삭제
    $('.cert-location-area-targetMedia ul').remove();

    //var cnt = 0;    // 저장매체 공간 체크
    res = '<ul id="MediaSet_2">';
    for (var i = 1; i <= length; i++) {
        var index = 0;
        while (true) {
            media = NX_TARGET_MEDIA[index].MEDIA;
            order = NX_TARGET_MEDIA[index].ORDER;
            able = NX_TARGET_MEDIA[index].ABLE;
            index++;

            if (order === i) {
                break;
            }
        }

        Upper_able = able.toUpperCase();

        // 확장 매체 시작시
        if (i === 5) {
            if (NX_TARGET_MEDIA[5].ABLE === 'able') {
                res += "<li><button style=\"background-image:url('" + NX_MEDIA_IMG_URL + "MEDIA_LIST.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow();\">" + NX_TARGET_DIALOG_TEXT_1 + "</button>";
                res += "<div class='sub-layer'>";
                res += "<div class='inner' id='MediaSet_1-sub'>";
            } else {
                // 확장 매체 disable
                res += "<li><button disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "MEDIA_LIST.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow();\">" + NX_TARGET_DIALOG_TEXT_1 + "</button>";
                res += "<div class='sub-layer'>";
                res += "<div class='inner' id='MediaSet_1-sub'>";
            }
        }

        if (i >= 5) {
            // 추가확장매체에 표시시
            switch (media) {
                case 'HDD':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"HDD\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\" onclick=\"NX_SetMediaHDD2();\">" + NX_TARGET_DIALOG_TEXT_2 + "</button> ";
                    }
                    else {
                        res += "<button id=\"HDD\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\">" + NX_TARGET_DIALOG_TEXT_2 + "</button>";
                    }
                    break;

                case 'USB':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"USB\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3();\">" + NX_TARGET_DIALOG_TEXT_3 + "</button>";
                    }
                    else {
                        res += "<button id=\"USB\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\">" + NX_TARGET_DIALOG_TEXT_3 + "</button> ";
                    }
                    break;

                case 'USIM':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"USIM\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('USIM','NULL');\">" + NX_TARGET_DIALOG_TEXT_4 + "</button>";
                    }
                    else {
                        res += "<button id=\"USIM\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\">" + NX_TARGET_DIALOG_TEXT_4 + "</button>";
                    }
                    break;

                case 'HSM':
                case 'SECURE_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"SECURE_TOKEN\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('HSM','NULL');\">" + NX_TARGET_DIALOG_TEXT_5 + "</button>";
                    }
                    else {
                        res += "<button id=\"SECURE_TOKEN\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_5 + "</button>";
                    }
                    break;

                case 'BIOHSM':
                case 'BIO_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<button id=\"BIO_TOKEN\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('BIOHSM','" + BioTokenP7Message + "');\">" + NX_TARGET_DIALOG_TEXT_6 + "</button>";
                    }
                    else {
                        res += "<button id=\"BIO_TOKEN\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_6 + "</button>";
                    }
                    break;

                case 'SECUREDISK':
                    if (Upper_able === 'ABLE') {
                        res += "<button style=\"background-image:url('" + NX_MEDIA_IMG_URL + "IC.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('SECUREDISK','NULL');\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    else {
                        res += "<button disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "IC.png')\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    break;
            }//end switch

        }
        else {
            // 앞단에 표시시
            switch (media) {
                case 'HDD':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"HDD\" style=\"background-image:url(\'" + NX_MEDIA_IMG_URL + "HDD.png\')\" onclick=\"NX_SetMediaHDD2();\">" + NX_TARGET_DIALOG_TEXT_2 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"HDD\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "HDD.png')\">" + NX_TARGET_DIALOG_TEXT_2 + "</button></li> ";
                    }
                    break;

                case 'USB':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"USB\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('USB', 'NULL');\">" + NX_TARGET_DIALOG_TEXT_3 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"USB\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USB.png')\">" + NX_TARGET_DIALOG_TEXT_3 + "</button></li> ";
                    }
                    break;

                case 'USIM':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"USIM\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('USIM','NULL');\">" + NX_TARGET_DIALOG_TEXT_4 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"USIM\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "USIM.png')\">" + NX_TARGET_DIALOG_TEXT_4 + "</button></li> ";
                    }
                    break;

                case 'HSM':
                case 'SECURE_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"SECURE_TOKEN\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('HSM','NULL');\">" + NX_TARGET_DIALOG_TEXT_5 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"SECURE_TOKEN\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "SECURE_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_5 + "</button></li> ";
                    }
                    break;

                case 'BIOHSM':
                case 'BIO_TOKEN':
                    if (Upper_able === 'ABLE') {
                        res += "<li> <button id=\"BIO_TOKEN\" style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('BIOHSM','" + BioTokenP7Message + "');\">" + NX_TARGET_DIALOG_TEXT_6 + "</button></li> ";
                    }
                    else {
                        res += "<li> <button id=\"BIO_TOKEN\" disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "BIO_TOKEN.png')\">" + NX_TARGET_DIALOG_TEXT_6 + "</button></li> ";
                    }
                    break;

                case 'SECUREDISK':
                    if (Upper_able === 'ABLE') {
                        res += "<button style=\"background-image:url('" + NX_MEDIA_IMG_URL + "IC.png')\" onclick=\"NX_Issue_pubUi.moreSaveMediaShow3('SECUREDISK','NULL');\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    else {
                        res += "<button disabled style=\"background-image:url('" + NX_MEDIA_IMG_URL + "IC.png')\">" + NX_TARGET_DIALOG_TEXT_14 + "</button>";
                    }
                    break;
            }//end switch
        }

        //확장매체 종료
        if (i === length) {
            res += '<a href="javascript:void(0)" class="btn-close" onclick="NX_Issue_pubUi.moreSaveMediaHide(); NX_Issue_pubUi.moreSaveMediaHideFocusMediaList(); return false;">' + NX_TARGET_DIALOG_TEXT_7 + '</a> </div></div></li>';
        }

    }//end for          

    res += '</ul>';

    $('.cert-location-area-targetMedia').append(res);
}

function NX_selectStorage(mediaType, extraValue) {

    if (InsertNullCheck(mediaType) === false) {
        // 선택된 저장매체가 USIM인 경우 사이트 코드를 할당한다.
        if (mediaType === "USIM" || mediaType === "usim") {
            extraValue = USIM_SITECODE;
        }

        //로직 구분
        var Logic_flag = processLogic.getProcessLogic();
        var cmd;
        var targetMediaF = targetMediaFlag.getFlag();
        if (targetMediaF === true) {
            // 발급 & 재발급인 경우
            // 타켓 미디어 정보만 필요한 경우
            cmd = 'NX_selectStorage.selectStorage2Issue';
        }
        else {
            if ((Logic_flag.indexOf('ISSUE') !== -1) ||
                (Logic_flag.indexOf('MANAGEMENT') !== -1)) {

                // 갱신인 경우 
                // 갱신 대상 인증서 정보도 가져와야 하기 때문에 인증서 리스트 호출
                cmd = 'NX_selectStorage.selectStorageIssue';
            } else {
                // 그외 인증서 이용시 인증서 리스트 호출 
                cmd = 'NX_selectStorage.selectStorage';
            }
        }

        var Data = {
            'mediaType': mediaType,
            'extraValue': extraValue
        };
        var param = JSON.stringify(Data);
        secukitnxInterface.SecuKitNX_EX(cmd, param);
    } else {
        hideNXBlockLayer(); KICA_Error.init();
        var location = 'NX_selectStorage';
        var reason = 'mediaType';
        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_SCRIPT_DIALOG_INPUT');
        var ScriptErrorMessage = KICA_Error.getScriptError();
        alert(ScriptErrorMessage);
    }
}

function NX_selectStorageCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        //console.log(err);
    }

    if (errorCheck === undefined) {

        var targetMediaF = targetMediaFlag.getFlag();
        if (targetMediaF === true) {

            $('.pki-wrap4 #pki-extra-media-box-contents4').remove();

            var mediaType = TargetMediaInfo.getMediaType();
            var extraMsg = '';
            if (mediaType === 'USB') {
                extraMsg = NX_TARGET_DIALOG_TEXT_3;
            }
            if (mediaType === 'HSM') {
                extraMsg = NX_TARGET_DIALOG_TEXT_5;

                // 보안토큰 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }
            if (mediaType === 'BIOHSM') {
                extraMsg = NX_TARGET_DIALOG_TEXT_6;

                // 보안토큰 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }

            if (mediaType === 'PHONE') {
                extraMsg = NX_TARGET_DIALOG_TEXT_15;

                // 휴대폰저장소 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }

            if (mediaType === 'SECUREDISK') {
                // 안전디스크 설치 유무 판단
                var SecureDISK_Chk = false;

                // 안전디스크 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
                for (var i = 1; i <= reply.size; i++) {
                    if (reply[i].diskName.indexOf('SecureDisk') !== -1) {
                        TargetMediaInfo.setExtraValue(reply[i].diskName);
                        SecureDISK_Chk = true;
                        break;
                    }
                }

                if (!SecureDISK_Chk) {
                    // 안전디스크 설치 안된 경우
                    // @Todo KISA 검증 체크 추가
                    KICA_Error.init();
                    var location = '';
                    var reason = '';
                    KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_CLIENT_NO_INSTALL_SECUREDISK');
                    var ScriptErrorMessage = KICA_Error.getScriptError();
                    alert(ScriptErrorMessage);
                    // 안전디스크 다운로드
                    NXdownClientURL_SecureDisk();
                }

                NX_Issue_pubUi.moreSaveMediaHide();
                NX_Issue_pubUi.moreSaveMediaHide3();
            }
            else {
                // 안전디스크를 선택하지 않은 경우
                var sublayer = '<div id=\"pki-extra-media-box-contents4\">';
                sublayer += '<div class=\"pki-head4\"><h1>' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_8 + '</h1><a href=\"#\" class=\"btn-close1\" onclick=\"NX_Issue_pubUi.moreSaveMediaHide5();return false;\">' + NX_TARGET_DIALOG_TEXT_9 + '</a></div>';
                sublayer += '<div class=\"pki-body4\">';
                sublayer += '<div class=\"titwrap\">';
                sublayer += '<h2 class=\"tit1\">' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_10 + '</h2>';
                sublayer += '<div class=\"cert-select-area4\" id=\"cert-select-area4\">';

                sublayer += '<input type=\"hidden\" id=\"NX_EX_TargetMediaIndex\" value=\"\">';
                sublayer += '<table id=\"divtable2\" cellpadding=\"0\" cellspacing=\"0\" class=\"js-selrow\">';

                var listIndex = 1;
                var tokenNameTmp = '';
                for (var i = 1; i <= reply.size; i++) {
                    // 보안토큰 클릭시 모바일 드라이브 목록 출력 안되도록 수정 - 160406
                    if (reply[i].diskName.indexOf('Mobile') === -1 && reply[i].diskName.indexOf('SecureDisk') === -1) {

                        if (mediaType === 'BIOHSM') {
                            var Tmp_BioTokenInfo = '';
                            Tmp_BioTokenInfo = reply[i].diskName.split('|');
                            tokenNameTmp = Tmp_BioTokenInfo[0] + ' | ' + Tmp_BioTokenInfo[3];
                        }
                        else {
                            tokenNameTmp = reply[i].diskName;
                        }

                        sublayer += '<tr tabindex="0"  id="' + reply[i].diskName + '" onclick="NX_Issue_pubUi.IssueExtraValueIdReturn(this); NX_Issue_pubUi.certSelectRow(' + listIndex + '); ">';
                        sublayer += '<td>' + tokenNameTmp + '</td></tr>';
                        ++listIndex;
                        tokenNameTmp = '';
                    }
                }
                sublayer += '</table>';
                sublayer += '</div></div></div>';
                sublayer += '<div class=\"pki-bottom4\">';
                if (mediaType === 'BIOHSM' || mediaType === 'HSM') {
                    sublayer += '<div class=\"gray-box2 mb10\"> ' + '<a target="_blank" href="http://rootca.or.kr/">토큰 드라이브 다운로드</a>' + ' </div>';
                } else {
                    sublayer += '<div class=\"gray-box2 mb10\"> ' + extraMsg + '' + NX_TARGET_DIALOG_TEXT_11 + ' </div>';
                }
                sublayer += '<button class=\"btn-ok\" onclick="NX_Issue_pubUi.moreSaveMediaHide3();return false;">' + NX_TARGET_DIALOG_TEXT_12 + '</button>';
                sublayer += '</div><div>';

                $('.pki-wrap4').find('#pki-extra-media-box4').append(sublayer);
                // 추가매체 화면 표시
                $('.cert-location-area').find('.pki-wrap4').fadeIn('fast');
            }
            // 안전디스크 끝
            $('.pki-wrap4 tr:first-child').trigger('click');
            $('.pki-wrap4 tbody tr:first-child').focus();

            $('.pki-wrap4 tbody tr').on('keydown', function(event) {
                if (event.keyCode == 13 || event.which == 13) {
                    $(this).trigger('click');
                }
            });

        } else {
            // 인증서 리스트 포함인 경우
            $('.pki-wrap3 #pki-extra-media-box-contents3').remove();

            var mediaType = SelectMediaInfo.getMediaType();
            var extraMsg = '';
            if (mediaType === 'USB') {
                extraMsg = NX_TARGET_DIALOG_TEXT_3;
            }

            if (mediaType === 'HSM') {
                extraMsg = NX_TARGET_DIALOG_TEXT_5;

                // 보안토큰 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }

            if (mediaType === 'BIOHSM') {
                extraMsg = NX_TARGET_DIALOG_TEXT_6;

                // 보안토큰 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }

            if (mediaType === 'SECUREDISK') {
                extraMsg = NX_TARGET_DIALOG_TEXT_14;

                // 안전디스크 정보 저장
                TokenMediaInfo.init();
                TokenMediaInfo.setTokenMediaObj(reply);
            }

            if (mediaType === 'USIM' && reply.extraValue === 'NULL') {
                alert(NX_WEBUI_SELECT_TEXT_12);
                $("#MediaSet_1>li>button.active").removeClass("active");
                if (USIM_DOWNLOAD_EXE !== '') {
                    //USIM 설치파일 직접 다운로드
                    window.open(USIM_DOWNLOAD_EXE, '', 'width="500", height="300", menubar=no, status=no, toolbar=no');
                } else {
                    //다운로드 사이트 팝업
                    var PopUPWindow = window.open(USIM_DOWNLOAD_URL);
                }
            } else if (mediaType === 'USIM') {
                if (reply.verify !== 'Y') {
                    alert(NX_ISSUE_PUB_TEXT_32);
                }
                Dialog.selectCertificate("1", "USIM", "NULL");
            } else {

                if (mediaType === 'SECUREDISK') {
                    // 안전디스크 설치 유무 판단
                    var SecureDISK_Chk = false;
                    var SecureDisk_Version_Chk = true;

                    for (var i = 1; i <= reply.size; i++) {
                        if (reply[i].diskName.indexOf('SecureDisk') !== -1) {
                            // 안전디스크 설치 된 경우
                            // 버전 체크 : npki_pkcs11.cnf 파일의 info 필드 값으로 버전 체크
                            if (reply[i].diskName === SECUREDISK_DRIVE_NAME) {
                                SecureDISK_Chk = true;
                                SecureDisk_Version_Chk = true;
                                Dialog.selectStorage("SECUREDISK", reply[i].diskName);
                            }
                            else {
                                SecureDISK_Chk = false;
                                SecureDisk_Version_Chk = false;
                            }
                        }
                    }

                    if (!SecureDISK_Chk && SecureDisk_Version_Chk) {
                        // 안전디스크 설치 안된 경우
                        KICA_Error.init();
                        var location = '';
                        var reason = '';
                        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_CLIENT_NO_INSTALL_SECUREDISK');
                        var ScriptErrorMessage = KICA_Error.getScriptError();
                        alert(ScriptErrorMessage);
                        // 안전디스크 다운로드
                        NXdownClientURL_SecureDisk();
                    }

                    if (!SecureDISK_Chk && !SecureDisk_Version_Chk) {
                        // 안전디스크 버전이 하위버전인 경우
                        KICA_Error.init();
                        var location = '';
                        var reason = '';
                        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_CLIENT_RELEASE_VERSION_SECUREDISK');
                        var ScriptErrorMessage = KICA_Error.getScriptError();
                        alert(ScriptErrorMessage);
                        // 안전디스크 다운로드
                        NXdownClientURL_SecureDisk();
                    }

                }
                else if (mediaType === 'PHONE' && reply.extraValue === 'UBIKEY') {
                    alert(NX_WEBUI_SELECT_TEXT_12);
                    $("#MediaSet_1>li>button.active").removeClass("active");

                    //if (USIM_DOWNLOAD_EXE !== '') {
                    if (mediaType === 'PHONE') {
                        //USIM 설치파일 직접 다운로드
                        window.location = "http://www.ubikey.co.kr/infovine/UBIKey.exe";
                    } else {

                        //다운로드 사이트 팝업
                        var PopUPWindow = window.open("http://www.ubikey.co.kr/services/service-join.asp");

                    }

                }
                else if (mediaType === 'PHONE' && reply.extraValue === '') {
                    extraMsg = NX_TARGET_DIALOG_TEXT_15;

                    var sublayer = '<div id=\"pki-extra-media-box-contents3\">';
                    sublayer += '<div class=\"pki-head3\"><h1>' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_8 + '</h1><a href=\"javascript:void(0)\" class=\"btn-close1\" onclick=\"NX_Issue_pubUi.moreSaveMediaHide5();return false;\">' + NX_TARGET_DIALOG_TEXT_9 + '</a></div>';
                    sublayer += '<div class=\"pki-body3\">';
                    sublayer += '<div class=\"titwrap\">';
                    sublayer += '<h2 class=\"tit1\">' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_10 + '</h2>';
                    sublayer += '<div class=\"cert-select-area3\" id=\"cert-select-area3\">';

                    sublayer += '<input type=\"hidden\" id=\"NX_EX_SelectMediaIndex\" value=\"\">';
                    sublayer += '<table cellpadding=\"0\" cellspacing=\"0\" class=\"js-selrow\">';

                    var i = 1;
                    sublayer += '<tr tabindex="0"  id=\"' + "phoneUbikey" + '\" onkeydown="NX_Issue_pubUi.EnterOnSublayerList(event)" onclick=\"NX_Issue_pubUi.extraValueIdReturn(this); NX_Issue_pubUi.certSelectRow(' + i + ');\">';
                    sublayer += '<td>' + "휴대폰 인증서 저장 서비스 (유비키)" + '</td></tr>';

                    sublayer += '</table>';
                    sublayer += '</div></div></div>';
                    sublayer += '<div class=\"pki-bottom3\">';
                    sublayer += '<div class=\"gray-box2 mb10\"> ' + extraMsg + '' + NX_TARGET_DIALOG_TEXT_11 + ' </div>';
                    sublayer += '<button class=\"btn-ok\" onclick="NX_Issue_pubUi.selectPhoneSevice();return false;">' + NX_TARGET_DIALOG_TEXT_12 + '</button>';
                    sublayer += '</div></div>';

                    $('.pki-wrap3 #pki-extra-media-box3').append(sublayer);
                    // 추가매체 화면 표시
                    $('.cert-location-area .pki-wrap3').fadeIn('fast');

                }
                else {
                    var sublayer = '<div id=\"pki-extra-media-box-contents3\">';
                    sublayer += '<div class=\"pki-head3\"><h1>' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_8 + '</h1><a href=\"#\" class=\"btn-close1\" onclick=\"NX_Issue_pubUi.moreSaveMediaHide5();return false;\">' + NX_TARGET_DIALOG_TEXT_9 + '</a></div>';
                    sublayer += '<div class=\"pki-body3\">';
                    sublayer += '<div class=\"titwrap\">';
                    sublayer += '<h2 class=\"tit1\">' + extraMsg + ' ' + NX_TARGET_DIALOG_TEXT_10 + '</h2>';
                    sublayer += '<div class=\"cert-select-area3\" id=\"cert-select-area3\">';

                    sublayer += '<input type=\"hidden\" id=\"NX_EX_SelectMediaIndex\" value=\"\">';
                    sublayer += '<table cellpadding=\"0\" cellspacing=\"0\" class=\"js-selrow\">';

                    var listIndex = 1;
                    var tokenNameTmp = '';
                    for (var i = 1; i <= reply.size; i++) {
                        // 보안토큰 클릭시 모바일 드라이브 목록 출력 안되도록 수정 - 160406
                        if (reply[i].diskName.indexOf('Mobile') === -1 && reply[i].diskName.indexOf('SecureDisk') === -1) {

                            if (mediaType === 'BIOHSM') {
                                var Tmp_BioTokenInfo = '';
                                Tmp_BioTokenInfo = reply[i].diskName.split('|');
                                tokenNameTmp = Tmp_BioTokenInfo[0] + ' | ' + Tmp_BioTokenInfo[3];
                            }
                            else {
                                tokenNameTmp = reply[i].diskName;
                            }

                            sublayer += '<tr tabindex="0"  id=\"' + reply[i].diskName + '\" onkeydown="NX_Issue_pubUi.EnterOnSublayerList(event)" onclick=\"NX_Issue_pubUi.extraValueIdReturn(this); NX_Issue_pubUi.certSelectRow(' + listIndex + ');\">';
                            sublayer += '<td>' + tokenNameTmp + '</td></tr>';
                            ++listIndex;
                            tokenNameTmp = '';
                        }
                    }

                    sublayer += '</table>';
                    sublayer += '</div></div></div>';
                    sublayer += '<div class=\"pki-bottom3\">';
                    if (mediaType === 'BIOHSM' || mediaType === 'HSM') {
                        sublayer += '<div class=\"gray-box2 mb10\"> ' + '<a target="_blank" href="http://rootca.or.kr/">토큰 드라이브 다운로드</a>' + ' </div>';
                    } else {
                        sublayer += '<div class=\"gray-box2 mb10\"> ' + extraMsg + '' + NX_TARGET_DIALOG_TEXT_11 + ' </div>';
                    }
                    sublayer += '<button class=\"btn-ok\" onclick="NX_Issue_pubUi.moreSaveMediaHide2();return false;">' + NX_TARGET_DIALOG_TEXT_12 + '</button>';
                    sublayer += '</div></div>';

                    $('.pki-wrap3').find('#pki-extra-media-box3').append(sublayer);
                    $('.pki-wrap3 #pki-extra-media-box3 tbody tr:first-child').trigger('click');
                    $('.pki-wrap3 #pki-extra-media-box3 tbody tr:first-child').focus();

                    $('.pki-wrap3 #pki-extra-media-box3 tbody tr').on('keydown', function(event) {
                        if (event.keyCode == 13 || event.which == 13) {
                            $(this).trigger('click');
                        }
                    });

                    // 추가매체 화면 표시
                    $('.cert-location-area').find('.pki-wrap3').fadeIn('fast');

                    if (NX_DIALOG_MOVE === true) {
                        try {
                            //추가 미디어 창 커서 이동을 위해 추가
                            $(".pki-wrap3").draggable({ handle: ".pki-head3", cursor: "move" });
                        } catch (e) { //console.log(e);}
                        }
                    }
                }


            }
        }
    }
    else {
        $('.cert-location-area .pki-wrap3').hide();
        $("#MediaSet_1>li>button.active").removeClass("active");
        hideNXBlockLayer(); KICA_Error.init();
        KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
        var errorMsg = KICA_Error.getError();
        alert(errorMsg);
    }
}

// 선택한 미디어 정보를 가지고 있는 함수
var TargetMediaInfo = (function () {
    var mediaType = '';
    var extraValue = '';

    var init = function () {
        mediaType = '';
        extraValue = '';
    };

    var setMediaType = function (media) {
        mediaType = media;
    };
    var getMediaType = function () {
        return mediaType;
    };

    var setExtraValue = function (extra) {
        extraValue = extra;
    };
    var getExtraValue = function () {
        return extraValue;
    };

    return {
        init: init,
        setMediaType: setMediaType,
        getMediaType: getMediaType,
        setExtraValue: setExtraValue,
        getExtraValue: getExtraValue
    };
})();

// 선택한 미디어 정보를 가지고 있는 함수
var SelectMediaInfo = (function () {
    var mediaType = '';
    var extraValue = '';

    var init = function () {
        mediaType = '';
        extraValue = '';
    };

    var setMediaType = function (media) {
        mediaType = media;
    };
    var getMediaType = function () {
        return mediaType;
    };

    var setExtraValue = function (extra) {
        extraValue = extra;
    };
    var getExtraValue = function () {
        return extraValue;
    };

    return {
        init: init,
        setMediaType: setMediaType,
        getMediaType: getMediaType,
        setExtraValue: setExtraValue,
        getExtraValue: getExtraValue
    };
})();

// 보안토큰 및 지문보안토큰에 대한 리스트 저장
// KISA 검증 여부를 확인 하기 위한 객체
var TokenMediaInfo = (function () {
    var TokenMediaObj = '';

    var init = function () {
        TokenMediaObj = '';
    };

    var setTokenMediaObj = function (obj) {
        TokenMediaObj = obj;
    };

    var getTokenMediaObj = function () {
        return TokenMediaObj;
    };

    return {
        init: init,
        setTokenMediaObj: setTokenMediaObj,
        getTokenMediaObj: getTokenMediaObj
    };
})();


// 하드디스크인 경우 MediaType 및 ExtraValue Set 함수
function NX_SetMediaHDD() {
    //인증서 리스트 초기화
    NX_Issue_pubUi.certListReset();

    // 확장매체 표시 제거
    NX_Issue_pubUi.moreSaveMediaHide();

    NX_Issue_pubUi.removeMediaExtraDialog();

    TargetMediaInfo.setMediaType('HDD');
    TargetMediaInfo.setExtraValue('NULL');
    SelectMediaInfo.setMediaType('HDD');
    SelectMediaInfo.setExtraValue('NULL');

    // 로직 구분
    var Logic_flag = processLogic.getProcessLogic();

    if (Logic_flag === 'KICA.ISSUE.IssueCert' ||
        Logic_flag === 'KICA.ISSUE.ReissueCertificate') {
        var selectMedia_noti = NX_TARGET_DIALOG_TEXT_13 + ' : ';
        document.getElementById("targetMedia_noti").innerHTML = selectMedia_noti;

    } else {

        if (Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
            var selectMedia_noti = NX_TARGET_DIALOG_TEXT_13 + ' : ';
            document.getElementById("targetMedia_noti").innerHTML = selectMedia_noti;
        }

        Dialog.selectStorage('HDD', 'NULL');
        //NX_selectStorage('HDD', 'NULL');
    }

}

// 하드디스크인 경우 MediaType 및 ExtraValue Set 함수
function NX_SetMediaHDD2() {

    TargetMediaInfo.setMediaType('HDD');
    TargetMediaInfo.setExtraValue('NULL');
    SelectMediaInfo.setMediaType('HDD');
    SelectMediaInfo.setExtraValue('NULL');

    var selectMedia_noti = NX_TARGET_DIALOG_TEXT_13 + ' : ' + NX_TARGET_DIALOG_TEXT_2;
    document.getElementById("targetMedia_noti").innerHTML = selectMedia_noti;
}