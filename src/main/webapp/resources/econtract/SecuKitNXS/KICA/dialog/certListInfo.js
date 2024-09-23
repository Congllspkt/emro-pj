/**
 * @public
 * @class
 * @description  인증서 선택창의 인증서 목록을 관리하는 함수 정의 class
 */
var certListInfo = (function () {
    var certListInfo;
    var certListIndex = '';
    var certId = '';

    var init = function () {
        certId = '';
        certListInfo = '';
        certListIndex = '';
    };

    var setCertListInfo = function (listObj) {
        certListInfo = '';
        certListInfo = listObj;
    };
    var getCertListInfo = function () {
        return certListInfo;
    };
    var setCertListIndex = function (index) {
        certListIndex = index;
    };
    var getCertListIndex = function () {
        return certListIndex;
    };
    var setCertID = function (id) {
        certId = id;
    };
    var getCertID = function () {
        return certId;
    };

    return {
        init: init,
        setCertListInfo: setCertListInfo,
        getCertListInfo: getCertListInfo,
        setCertListIndex: setCertListIndex,
        getCertListIndex: getCertListIndex,
        setCertID: setCertID,
        getCertID: getCertID
    };
})();