/**
 * @public
 * @class
 * @description  BioHSM 정보 추출 함수 정의 class
 */
var BioHSM = (function () {

    var b_selectBioHSM = false;

    var BioHSM_DeviceCompanyCode = '',   //지문보안HSM 장치의 제조사 코드 
        BioHSM_DeviceSerial = '',   //지문보안HSM 장치의 일련번호 
        BioHSM_UserCode = '',   //지문보안HSM 장치의 사용자 번호 
        BioHSM_CompanyCode = '',   //지문보안HSM 장치의 법인 사업자 번호 
        BioHSM_DeviceAuthValue = '';   //지문보안HSM 장치의 장치 인증값 

    /**
    * @public
    * @memberof BioHSM
    * @method init
    * @description BioHSM 정보를 초기화 한다.
    * 초기화 정보 : 제조사 코드, 장치 일련번호, 사용자 번호, 법인 사업자 번호, 장치 인증값
    */
    var init = function () {
        //정보 초기화
        BioHSM_DeviceCompanyCode = '';
        BioHSM_DeviceSerial = '';
        BioHSM_UserCode = '';
        BioHSM_CompanyCode = '';
        BioHSM_DeviceAuthValue = '';
        selectBioHSM = false;
    };

    var setb_SelectBioHSM = function (flag) {
        b_selectBioHSM = flag;
    };

    var getb_SelectBioHSM = function () {
        return b_selectBioHSM;
    };

    /**
 * @public
 * @memberof BioHSM
 * @method getBioHSM_DeviceCompanyCode
 * @description BioHSM 제조사 코드 정보를 리턴한다.
 */
    var setBioHSM_DeviceCompanyCode = function (res) {
        BioHSM_DeviceCompanyCode = res;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_DeviceSerial
    * @description BioHSM 장치 일련번호를 리턴한다.
    */
    var setBioHSM_DeviceSerial = function (res) {
        BioHSM_DeviceSerial = res;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_UserCode
    * @description BioHSM 사용자 번호를 리턴한다.
    */
    var setBioHSM_UserCode = function (res) {
        BioHSM_UserCode = res;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_CompanyCode
    * @description BioHSM 사업자 번호를 리턴한다.
    */
    var setBioHSM_CompanyCode = function (res) {
        BioHSM_CompanyCode = res;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_DeviceAuthValue
    * @description BioHSM 장치 검증 값을 리턴한다.
    */
    var setBioHSM_DeviceAuthValue = function (res) {
        BioHSM_DeviceAuthValue = res;
    };
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_DeviceCompanyCode
    * @description BioHSM 제조사 코드 정보를 리턴한다.
    */
    var getBioHSM_DeviceCompanyCode = function () {
        //console.log(BioHSM_DeviceCompanyCode);
        return BioHSM_DeviceCompanyCode;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_DeviceSerial
    * @description BioHSM 장치 일련번호를 리턴한다.
    */
    var getBioHSM_DeviceSerial = function () {
        //console.log(BioHSM_DeviceSerial);
        return BioHSM_DeviceSerial;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_UserCode
    * @description BioHSM 사용자 번호를 리턴한다.
    */
    var getBioHSM_UserCode = function () {
        //console.log(BioHSM_UserCode);
        return BioHSM_UserCode;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_CompanyCode
    * @description BioHSM 사업자 번호를 리턴한다.
    */
    var getBioHSM_CompanyCode = function () {
        //console.log(BioHSM_CompanyCode);
        return BioHSM_CompanyCode;
    };

    /**
    * @public
    * @memberof BioHSM
    * @method getBioHSM_DeviceAuthValue
    * @description BioHSM 장치 검증 값을 리턴한다.
    */
    var getBioHSM_DeviceAuthValue = function () {
        //console.log(BioHSM_DeviceAuthValue);
        return BioHSM_DeviceAuthValue;
    };


    return {
        init: init,

        setb_SelectBioHSM: setb_SelectBioHSM,
        getb_SelectBioHSM: getb_SelectBioHSM,

        setBioHSM_DeviceCompanyCode: setBioHSM_DeviceCompanyCode,
        setBioHSM_DeviceSerial: setBioHSM_DeviceSerial,
        setBioHSM_UserCode: setBioHSM_UserCode,
        setBioHSM_CompanyCode: setBioHSM_CompanyCode,
        setBioHSM_DeviceAuthValue: setBioHSM_DeviceAuthValue,

        getBioHSM_DeviceCompanyCode: getBioHSM_DeviceCompanyCode,
        getBioHSM_DeviceSerial: getBioHSM_DeviceSerial,
        getBioHSM_UserCode: getBioHSM_UserCode,
        getBioHSM_CompanyCode: getBioHSM_CompanyCode,
        getBioHSM_DeviceAuthValue: getBioHSM_DeviceAuthValue
    };
})();