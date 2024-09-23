/**
 * @public
 * @class
 * @description  인증서 선택창 내 리스트(인증서 목록, 인증서 정보)를 관리하는 함수 정의 class
 */
var MakeCertList = function () {

    var data = '',
        detailData = '',
        generalData = '';

    /**
     * @public
     * @memberof makeCertList
     * @method init
     * @description 인증서 선택창 내 리스트 정보를 담는 변수를 초기화한다.
     */
    var init = function () {
        data = '';
        detailData = '';
        generalData = '';
    };

    /**
     * @public
     * @memberof makeCertList
     * @method makeCertListHTML
     * @description 인증서 선택창의 인증서 목록을 출력하기 위해 리스트 정보를 가공한다.
     * @param certListValue 인증서 리스트 정보를 가지고 있는 객체
     */
    var makeCertListHTML = function (certListValue) {
        //var certID = 'certificate';
        var d = new Date();
        var NowYear = ZerosDate(d.getFullYear(), 4);
        var NowMonth = ZerosDate(d.getMonth() + 1, 2);
        var NowDate = ZerosDate(d.getDate(), 2);

        if (certListValue.size === undefined) {
            data = KICA_CERTLISTEMPTY;
        } else {

            data += '<div id="select-cert-list-area" class="js-selrow-certlist">';
            data += '<table class=\"js-selrow\" id=\"divtable\" cellpadding=\"0\" cellspacing=\"0\">';
            data += '<caption>';
            data += '</caption>';
            data += '<colgroup>';
            data += '<col style="width:25%" />';
            data += '<col style="width:auto" />';
            data += '<col style="width:20%" />';
            data += '<col style="width:20%" />';
            data += '</colgroup>';
            data += '<thead>';
            data += '<tr>';
            data += '<th scope="col"><div>' + NX_MAKE_CERT_LIST_TEXT_1 + '</div></th>';
            data += '<th scope="col"><div>' + NX_MAKE_CERT_LIST_TEXT_2 + '</div></th>';
            data += '<th scope="col"><div>' + NX_MAKE_CERT_LIST_TEXT_3 + '</div></th>';
            data += '<th scope="col"><div>' + NX_MAKE_CERT_LIST_TEXT_4 + '</div></th>';
            data += '</tr>';
            data += '</thead>';
            data += '<tbody id="NXcertList">';

            if (backupIssueCertDN.getCertDN() !== "") {
                // 공고 게시 후 발급 된 인증서만 리스트에 보이도록 추가 됨 - 170324
                for (var i = 1; i <= certListValue.size; i++) {

                    if (certListValue[i].userDN === backupIssueCertDN.getCertDN()) {

                        certListInfo.setCertListIndex(certListValue[i].index);

                        data += '<tr id="' + certListValue[i].index + '" onclick="NX_Issue_pubUi.certIdReturn(this); NX_Issue_pubUi.certSelectRow_CertList(' + i + ');" alt=""';
                        if (i % 2 !== 0) {
                            data += ' class="even" >';
                        } else {
                            data += '>';
                        }

                        var fullCertTodate = certListValue[i].todate;
                        var certTodate = fullCertTodate.split('-');
                        //certTodate[0] 년
                        //certTodate[1] 월
                        //certTodate[2] 일
                        if (certTodate[0] < NowYear) {
                            data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                        }

                        else if ((certTodate[0] === NowYear) && (certTodate[1] < NowMonth)) {
                            data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                        }

                        else if ((certTodate[0] === NowYear) && (certTodate[1] === NowMonth) && (certTodate[2] < NowDate)) {
                            data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                        }
                        else {
                            data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert2.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                        }

                        data += '<td><div>' + certListValue[i].cn + '</div></td>';
                        data += '<td class="txt-c"><div>' + certListValue[i].todate + '</div></td>';
                        data += '<td class="txt-c"><div>' + certListValue[i].issuerToName + '</div></td>';
                        data += '<td style="display:none;" id=\"realCertListIndex\"><div>' + + '</div></td>';
                        data += '</tr>';
                    }

                }

            }
            else {

                for (var i = 1; i <= certListValue.size; i++) {

                    data += '<tr id="' + certListValue[i].index + '" onclick="NX_Issue_pubUi.certIdReturn(this); NX_Issue_pubUi.certSelectRow_CertList(' + i + ');" alt=""';
                    if (i % 2 !== 0) {
                        data += ' class="even" >';
                    } else {
                        data += '>';
                    }

                    var fullCertTodate = certListValue[i].todate;
                    var certTodate = fullCertTodate.split('-');
                    //certTodate[0] 년
                    //certTodate[1] 월
                    //certTodate[2] 일
                    if (certTodate[0] < NowYear) {
                        data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                    }

                    else if ((certTodate[0] === NowYear) && (certTodate[1] < NowMonth)) {
                        data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                    }

                    else if ((certTodate[0] === NowYear) && (certTodate[1] === NowMonth) && (certTodate[2] < NowDate)) {
                        data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert1.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                    }
                    else {
                        data += '<td><div><img src="' + NX_DEFAULT_IMG_URL + 'ico_cert2.png" class="vm" alt="">' + ' ' + certListValue[i].usageToName + '</div></td>';
                    }

                    data += '<td><div>' + certListValue[i].cn + '</div></td>';
                    data += '<td class="txt-c"><div>' + certListValue[i].todate + '</div></td>';
                    data += '<td class="txt-c"><div>' + certListValue[i].issuerToName + '</div></td>';
                    data += '<td style="display:none;" id=\"realCertListIndex\"><div>' + + '</div></td>';
                    data += '</tr>';
                }

            }

            data += '</tbody>';
            data += '</table>';
            data += '</div>';
        }

        return data;
    };

    /**
     * @public
    * @memberof makeCertList
    * @method makeCertDetailInfoHTML
    * @description 인증서 선택창의 인증서정보를 출력하기 위해 리스트 정보를 가공한다.
    * @param certInfoValue 인증서 리스트 정보를 가지고 있는 객체
    */
    var makeCertDetailInfoHTML = function (certDetailInfoValue) {
        var mediaType = SelectMediaInfo.getMediaType();
        var mediaTypeString = '';
        if (mediaType === 'HDD') {
            mediaTypeString = NX_MAKE_CERT_LIST_TEXT_5;
        }
        if (mediaType === 'USB') {
            mediaTypeString = NX_MAKE_CERT_LIST_TEXT_6;
        }
        if (mediaType === 'HSM') {
            mediaTypeString = NX_MAKE_CERT_LIST_TEXT_7;
        }
        if (mediaType === 'BIOHSM') {
            mediaTypeString = NX_MAKE_CERT_LIST_TEXT_8;
        }

        var d = new Date();
        var NowYear = ZerosDate(d.getFullYear(), 4);
        var NowMonth = ZerosDate(d.getMonth() + 1, 2);
        var NowDate = ZerosDate(d.getDate(), 2);

        var fullCertTodate = certDetailInfoValue.validateTo;
        var certTodate = fullCertTodate.split('-');

        detailData += '<div id=\"cert-detail-info\">';
        detailData += '<dl><dt><img src=\"' + NX_DEFAULT_IMG_URL + 'certimg.png\" align="middle" alt="CertificateInfo" height="24" width="32">';
        detailData += '&nbsp&nbsp' + certDetailInfoValue.username + NX_MAKE_CERT_LIST_TEXT_9 + '</dt>';

        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_10 + '</b></dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_11 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.userDN + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_12 + '</b></dd>';
        detailData += '<dd>' + mediaTypeString + '</dd>';
        if (!InsertNullCheck(certDetailInfoValue.certDirPath)) {
            detailData += '<dd>' + certDetailInfoValue.certDirPath + '</dd>';
        }
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_13 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.validateFrom + '~' + certDetailInfoValue.validateTo + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_14 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.issuerToString + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_15 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.issueDN + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_16 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.policy + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_17 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.policyToName + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_18 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.signatureAlgorithmID + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_19 + '</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.version + '</dd>';
        detailData += '<dd><br /></dd>';
        detailData += '<dd><b>Key Size</b></dd>';
        detailData += '<dd>' + certDetailInfoValue.keySize + '</dd>';
        detailData += '<dd><br /></dd>';

        //certTodate[0] 년
        //certTodate[1] 월
        //certTodate[2] 일
        if (certTodate[0] < NowYear) { detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_21 + '</b></dd>'; }
        else if ((certTodate[0] === NowYear) && (certTodate[1] < NowMonth)) { detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_21 + '</b></dd>'; }
        else if ((certTodate[0] === NowYear) && (certTodate[1] === NowMonth) && (certTodate[2] < NowDate)) { detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_21 + '</b></dd>'; }
        else { detailData += '<dd><b>' + NX_MAKE_CERT_LIST_TEXT_20 + '</b></dd>'; }

        detailData += '<dd><br /></dd>';
        detailData += '<dd></dd>';
        detailData += '<dd></dd>';
        detailData += '<dd></dd>';
        detailData += '<dd></dd>';
        detailData += '<dd></dd>';
        detailData += '<dd></dd>';
        detailData += '</dl>';

        return detailData;
    };

    return {
        init: init,
        makeCertListHTML: makeCertListHTML,
        makeCertDetailInfoHTML: makeCertDetailInfoHTML
    };
};

function ZerosDate(n, digits) {
    var zero = '';
    n = n.toString();

    if (n.length < digits) {
        for (i = 0; i < digits - n.length; i++) {
            zero += '0';
        }
    }
    return zero + n;
}

// gpps backupCertDN
var backupIssueCertDN = (function () {
    var issueCertDN = "";

    var init = function () {
        issueCertDN = "";
    };

    var setCertDN = function (dn) {
        issueCertDN = dn;
    };
    var getCertDN = function () {
        return issueCertDN;
    };

    return {
        init: init,
        setCertDN: setCertDN,
        getCertDN: getCertDN
    };
})();