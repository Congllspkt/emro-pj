// Object Define
var SECUKITNX;

// Info Set
var secukitnxInfo = {
    "exPluginCallName": "SECUKITNX",
    "exPluginName": "SECUKITNX",		                    // exinterface.js 최상단에 정의한 객체명
    "exPluginInfo": "secukitnxInfo",	                    // exinterface.js 정의한 protocolInfo 객체명
    "exModuleName": "secukitnx",		                    // 모듈명

    // Module Info, 플러그인 설치파일 경로
    "moduleInfo": {
        "exNxClient_kpmcnt_exe": Module_KPMCNT_Ver,
        "exNxClient_kpmsvc_exe": Module_KPMSVC_Ver,

        "exNxClient_secukitNX_dll": Module_NX_Ver,
        "exNxClient_secuXML_dll": Module_XML_Ver,
        "exNxClient_kms_dll":Module_KMS_Ver
    },
    "exServiceInfo": {
        "isUse": true,
        "localhost": "https://127.0.0.1",
        "localhost2" : "http://127.0.0.1",
        "PortNum": 14315,
        "PortNum2": 14319
    },
    "checkSpec": true,
    "reqSpec": {
        "OS": {
            "WINDOWS": "5.1"  // XP=5.1, VISTA=6.0, Win7=6.1, Win8=6.2, Win8.1=6.3, Win10=6.4/10.0
        },
        "Browser": {
            "MSIE": "6",
            "EDGE": "ALL",
            "CHROME": "38",
            "FIREFOX": "36"
        }
    },
    "license": NXS_LICENSE
};

// Interface Define
var secukitnxInterface = {
    CustomEX: function (cmd, params) {
        var exCallback = "secukitnxInterface.CustomEXCallback";
        if (!cmd) {
            nxalert("secukitnxInterface.CustomEX", NX_EX_INTERFACE_TEXT_6);
            return;
        }

        try {
            var obj = JSON.parse(params);
            params = obj;
        } catch (e) { }

        SECUKITNX.Invoke(cmd, [params], exCallback);
    },

    CustomEXCallback: function (result) {
        try {
            var strSerial = JSON.stringify(result);
            nxlog("secukitnxInterface.CustomEXCallback", result);

            var resultArea = document.getElementById("resultArea");
            if (resultArea) {
                resultArea.value = strSerial;
            } else {
                alert(strSerial);
            }
        } catch (e) {
            nxlog("secukitnxInterface.CustomEXCallback [exception] result", result);
            nxalert("secukitnxInterface.CustomEXCallback", NX_EX_INTERFACE_TEXT_4 + "\n" + "result : " + result + "\nexception : " + e);
        }
    },

    SecuKitNX: function (cmd, params) {
        var start = cmd.indexOf(".");
        var dllCmd = cmd.substring(start + 1, cmd.length);
        var callback = cmd + "Callback";
        var exCallback = "secukitnxInterface.SecuKitNXCallBack";

        if (!cmd) {
            nxalert("secukitnxInterface.SecuKitNX", NX_EX_INTERFACE_TEXT_6);
            return;
        }

        nxlog("secukitnxInterface.SecuKitNX Command Message : ", params);

        try {

            var obj = JSON.parse(params);
            params = obj;
        } catch (e) { }

        SECUKITNX.Invoke(dllCmd, [params], exCallback, callback);

    },

    SecuKitNXCallBack: function (result) {
        try {
            var strSerial = JSON.stringify(result);
            if (result.reply.ERROR_CODE !== undefined) {
                KICA_Error.init();
                KICA_Error.setError(result.reply.ERROR_CODE, result.reply.ERROR_MESSAGE);
            }

            nxlog("secukitnxInterface.SecuKitNXCallBack", result);

            try {
                eval(result.callback)(result.reply);
            } catch (e) { }

        } catch (e) {
            nxlog("secukitnxInterface.SecuKitNXCallBack [exception] result", result);
            nxlog("secukitnxInterface.SecuKitNXCallBack", NX_EX_INTERFACE_TEXT_4 + "\n" + "result : " + result + "\nexception : " + e);
        }
    },

    SecuKitNX_EX: function (cmd, params) {
        var start = cmd.indexOf(".");
        var dllCmd = cmd.substring(start + 1, cmd.length);
        var funcName = cmd.substring(0, start);
        var callback = funcName + "Callback";
        var exCallback = "secukitnxInterface.SecuKitNX_EXCallBack";


        nxlog("secukitnxInterface.SecuKitNX_EX Command Message : ", params);

        if (!cmd) {
            nxalert("secukitnxInterface.SecuKitNX_EX", NX_EX_INTERFACE_TEXT_6);
            return;
        }

        try {
            var obj = JSON.parse(params);
            params = obj;
        } catch (e) { }

        SECUKITNX.Invoke(dllCmd, [params], exCallback, callback);

    },

    SecuKitNX_EXCallBack: function (result) {
        try {
            var strSerial = JSON.stringify(result);
            if (result.reply.ERROR_CODE !== undefined) {
                KICA_Error.init();
                KICA_Error.setError(result.reply.ERROR_CODE, result.reply.ERROR_MESSAGE);
            }

            nxlog("secukitnxInterface.SecuKitNX_EXCallBack", result);

            try {
                eval(result.callback)(result.reply);
            } catch (e) { }

        } catch (e) {
            nxlog("secukitnxInterface.SecuKitNXCallBack [exception] result", result);
            nxlog("secukitnxInterface.SecuKitNXCallBack", NX_EX_INTERFACE_TEXT_4 + "\n" + "result : " + result + "\nexception : " + e);
        }
    },

    SecuKitNXS: function (cmd, params) {
        var start = cmd.indexOf(".");
        var dllCmd = cmd.substring(start + 1, cmd.length);
        var funcName = cmd.substring(0, start);
        var callback = funcName + "Callback";
        var exCallback = "secukitnxInterface.SecuKitNXSCallBack";

        if (!cmd) {
            nxalert("secukitnxInterface.SecuKitNXS", NX_EX_INTERFACE_TEXT_6);
            return;
        }

        nxlog("secukitnxInterface.SecuKitNXS Command Message : ", params);

        try {
            var obj = JSON.parse(params);
            params = obj;
        } catch (e) { }

        SECUKITNX.Invoke(dllCmd, [params], exCallback, callback);
    },

    SecuKitNXSCallBack: function (result) {
        try {
            var strSerial = JSON.stringify(result);
            if (result.reply.ERROR_CODE !== undefined) {
                KICA_Error.init();
                KICA_Error.setError(result.reply.ERROR_CODE, result.reply.ERROR_MESSAGE);
            }

            nxlog("secukitnxInterface.SecuKitNXSCallBack", result);

            try {
                var funNameCallback = result.callback;
                var funName = funNameCallback.replace('Callback', '');

                eval('SecuKitNXS_RESULT')(funName, result.reply);
            } catch (e) { }

        } catch (e) {
            nxlog("secukitnxInterface.SecuKitNXSCallBack [exception] result", result);
            nxlog("secukitnxInterface.SecuKitNXSCallBack", NX_EX_INTERFACE_TEXT_4 + "\n" + "result : " + result + "\nexception : " + e);
        }
    }
};


/*
 * SECUKITNX_CONST Define
 */
var SECUKITNX_CONST = {
    "debug": ConsoleLogFlag,	            // nxlog 출력 여부
    "debugAlert": true,	                    // exproto_nxclient에서 발생하는 알림 여부
    "exEXObjName": "SECUKITNX",
    "frameName": "",
    "browserInfo": "",
    "OSInfo": "",
    "pluginInfo": [],
    "extabid": "",
    "pluginCount": 0,
    "isInstalled": false,
    "nextVerCheck":false,
    "licenseChk" : false,
    "load": false,
    "json2Path": secukitnxBaseDir + "KICA/config/3rd/json2.js",
    "cmdCnt": [],
    "intervalCheckCnt": 0,
    "serviceType": 1,                       // XML & KMS 이용시  : nx 1,              xml 2,              kms 3
    "serviceToken": "empty",                // XML & KMS 이용시  : nx empty,          xml 1234&OP=1,      kms ???
    "multiOS": false,
    "multiOSdownloadCnt" : false
};

var _OSName = "Unknown";
if (window.navigator.userAgent.indexOf("Mac") != -1) _OSName = "Mac/iOS";
if (window.navigator.userAgent.indexOf("X11") != -1) _OSName = "UNIX";
if (window.navigator.userAgent.indexOf("Linux") != -1)
{
    if(window.navigator.userAgent.indexOf("Ubuntu") != -1)
    {
        _OSName = "Linux/Ubuntu";
    }
    else {
        _OSName = "Linux";
    }
}

if (_OSName !== "Unknown")
{
    SECUKITNX_CONST.multiOS = true;
}

// IE 8 under JSON Object Set
if (typeof JSON !== "object" || navigator.userAgent.match(/msie 8/i)) {
    try {
        JSON = {};
        document.write("<script type='text/javascript' src='" + SECUKITNX_CONST.json2Path + "'></script>");
    } catch (e) {
        alert("json2.js load error");
    }
}

/*
 * SECUKITNXS Loading
 */
var SECUKITNX_LOADING = function (callback) {

    if (SECUKITNX_CONST.pluginInfo.length <= 0) {
        alert(NX_EXPROTO_TEXT_1);
        return;
    } else {
        if (SECUKITNX_CHECK.chkInfoStatus.status) {
            var chk = true;
            for (var i = 0; i < SECUKITNX_CONST.pluginInfo.length; i++) {
                var pluginInfo = SECUKITNX_CONST.pluginInfo[i];
                try {
                    eval(pluginInfo.exPluginName + " = new " + SECUKITNX_CONST.exEXObjName + "_EX(pluginInfo);");
                } catch (e) {
                    nxlog("_LOADING [exception]", e);
                    nxalert("_LOADING [exception]", e);
                    chk = false;
                    break;
                }
            }
            SECUKITNX_CONST.isInstalled = true;
            if (callback) {
                setTimeout(function () { eval(callback)(chk); }, 5);
            }

        } else {
            alert(NX_EXPROTO_TEXT_2);
            if (callback) eval(callback)(false);
        }
    }
};

// 버전체크 후 결과를 리턴 받지 못하는 경우를 대비해서
// interval 함수를 사용하여 체크를 한다.
var _IntervalChk = "";
var _requestMsg = "";
/*
 * SECUKITNXS InstallCheck
 */
var SECUKITNX_CHECK = {

    chkCallback: "",
    chkInfoStatus: { "status": false, info: [] },
    chkCurrPluginCnt: 0,
    //_IntervalChk : "",
    chkInterValChk:false,

    reChkModule: false,    // If Install Check is false, ReCheck Module install.


    check: function (pluginInfoArr, callback) {
        _requestMsg = "";

        // SECUKITNX_CONST Initialize
        SECUKITNX_CONST.frameName = SECUKITNX_UTIL.findPath(self);
        SECUKITNX_CONST.OSInfo = SECUKITNX_UTIL.getOSInfo();
        SECUKITNX_CONST.browserInfo = SECUKITNX_UTIL.getBrowserInfo();
        nxlog("_CONST.frameName", SECUKITNX_CONST.frameName);
        nxlog("_CONST.OSInfo", SECUKITNX_CONST.OSInfo);
        nxlog("_CONST.browserInfo", SECUKITNX_CONST.browserInfo);

        // generate IE tab id
        if (!SECUKITNX_CONST.extabid) {
            SECUKITNX_CONST.extabid = SECUKITNX_UTIL.createId();
        }

        nxlog("_CHECK.check", pluginInfoArr);
        this.chkInfoStatus = { "status": false, info: [] };
        this.chkCurrPluginCnt = 0;

        if (pluginInfoArr && pluginInfoArr.length > 0) {
            var infoCnt = pluginInfoArr.length;
            SECUKITNX_CONST.pluginInfo = [];

            for (var i = 0; i < infoCnt; i++) {
                SECUKITNX_CONST.pluginInfo.push(pluginInfoArr[i]);
                var tmpInfo = { "name": pluginInfoArr[i].exPluginName };
                this.chkInfoStatus.info.push(tmpInfo);
            }
        } else {
            if (SECUKITNX_CONST.pluginInfo.length <= 0) {
                alert(NX_EXPROTO_TEXT_3);
                return;
            }
        }

        if (callback) {
            this.chkCallback = callback;
        } else {
            alert(NX_EXPROTO_TEXT_4);
            return;
        }

        SECUKITNX_CONST.pluginCount = SECUKITNX_CONST.pluginInfo.length;

        this.moduleCheck(0);

    },

    moduleCheck: function (currPluginCnt) {
        // generate IE tab id
        if (!SECUKITNX_CONST.extabid) {
            SECUKITNX_CONST.extabid = SECUKITNX_UTIL.createId();
        }

        SECUKITNX_CHECK.chkCurrPluginCnt = currPluginCnt;

        if (currPluginCnt >= SECUKITNX_CONST.pluginCount) {
            var chk = true;
            for (var i = 0; i < SECUKITNX_CONST.pluginCount; i++) {
                var currInstalled = true;
                var currStatus = SECUKITNX_CHECK.chkInfoStatus.info[i];

                if (!SECUKITNX_CONST.multiOS) {

                    if (SECUKITNX_CONST.serviceType === 1) {
                        if (!currStatus.nx) {
                            chk = false;
                            currInstalled = false;
                        }
                    }

                    if (SECUKITNX_CONST.serviceType === 2) {
                        if (!currStatus.xml) {
                            chk = false;
                            currInstalled = false;
                        }
                    }

                    if (SECUKITNX_CONST.serviceType === 3) {
                        if (!currStatus.kms) {
                            chk = false;
                            currInstalled = false;
                        }
                    }

                    if (!currStatus.kpmcnt) {
                        chk = false;
                        currInstalled = false;
                    }

                    if (!currStatus.kpmsvc) {
                        chk = false;
                        currInstalled = false;
                    }
                } else {
                    //멀티 OS인 경우
                    if (!SECUKITNX_CONST.isInstalled) {
                        chk = false;
                        currInstalled = false;
                    }
                }
                currStatus.isInstalled = currInstalled;
            }
            if (!SECUKITNX_CONST.multiOS) {
                if (chk) {
                    // 설치 확인 성공
                    SECUKITNX_CHECK.chkInfoStatus.status = chk;
                    nxlog("_SERVICE.moduleCheck.chkInfoStatus", SECUKITNX_CHECK.chkInfoStatus);
                    eval(SECUKITNX_CHECK.chkCallback)(SECUKITNX_CHECK.chkInfoStatus);
                    return;
                }
                else if (this.reChkModule && !this.chkInterValChk) {
                    // 설치 확인이 안된 상태이며, 모듈 재체크 및 모듈 설치 결과를 전달하지 않은 경우
                    SECUKITNX_CHECK.chkInfoStatus.status = chk;
                    nxlog("_SERVICE.moduleCheck.chkInfoStatus", SECUKITNX_CHECK.chkInfoStatus);
                    eval(SECUKITNX_CHECK.chkCallback)(SECUKITNX_CHECK.chkInfoStatus);
                    return;
                }
                else if (this.chkInterValChk) {
                    // 설치 결과를 외부 스크립트 (secuKitnx.js)에서 확인 후 다시 모듈 체크 로직을 태운 경우
                    this.moduleCheck(0);
                    return;
                }
                else {
                    // 설치 확인이 안된 상태에서 모듈 재 체크 시도
                    this.reChkModule = true;
                    nxlog("_SERVICE.moduleCheck.chkInfoStatus- reChkModule : ", chk + "|" + this.reChkModule);
                    this.moduleCheck(0);
                    return;
                }
            }
            else {
                //멀티 OS인 경우
                // 설치 확인 성공
                SECUKITNX_CHECK.chkInfoStatus.status = chk;
                nxlog("_SERVICE.moduleCheck.chkInfoStatus", SECUKITNX_CHECK.chkInfoStatus);
                eval(SECUKITNX_CHECK.chkCallback)(SECUKITNX_CHECK.chkInfoStatus);
                return;
            }

        }

        var pluginInfo = SECUKITNX_CONST.pluginInfo[currPluginCnt];

        var checkCallback = "SECUKITNX_CHECK.serviceVersionCheck";
        var request = {};
        request.tabid = SECUKITNX_CONST.extabid;
        request.init = "get_versions";

        request.m = pluginInfo.exModuleName;
        request.origin = location.origin;
        //request.lic = pluginInfo.lic;
        request.callback = "";

        try {
            var params = {
                "TOKEN": "Getversion"
            };
            this.GetversionInvoke("GetVersion", [params], checkCallback, checkCallback);
        } catch (e) {
            nxalert("_SERVICE.moduleCheck.portCheck", e);
            SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
            return;
        }
    },
    req: null,
    pageCallbackTmp: null,
    //callBackFuncName: null,
    GetversionInvoke: function (fname, args, exCallback, pageCallback) {

        SECUKITNX_CONST.cmdCnt.push(pageCallback);

        var ieVersion, trident, reg;
        var agent = window.navigator.userAgent.toLowerCase();

        //ie 버전 구하기
        reg = new RegExp("trident/([0-9]{1,}[\.0-9]{0,})" );
        if(reg.exec( agent ) != null){
            trident = parseFloat(RegExp.$1);
        }

        reg = new RegExp("msie ([0-9]{1,}[\.0-9]{0,})" );
        if(reg.exec( agent ) != null){
            ieVersion = parseFloat(RegExp.$1);
        }

        nxlog("ieVersion " + ieVersion + ", trident " + trident);
        if(trident < 6 || ieVersion < 10){
            //ie 9이하
            if (window.XDomainRequest) { // IE 8, 9, 10
                req = new XDomainRequest();
                //pageCallbackTmp = pageCallback;
            } else if (window.ActiveXObject) {  // IE 5, 6, 7
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else if (window.XMLHttpRequest) { // Etc.
                req = new XMLHttpRequest();
            }
        }

        // JSON Build
        var request = new Object;
        request.callback = exCallback;
        callBackFuncName = exCallback ? exCallback : null;

        var tmp_TOKEN = args[0].TOKEN;
        if (!tmp_TOKEN || tmp_TOKEN === null) {
            tmp_TOKEN = "empty";
        }
        tmp_serviceType = SECUKITNX_CONST.serviceType;      // XML & KMS 이용시  : nx 1,              xml 2,              kms 3

        var exfunc = new Object;
        exfunc.fname = fname;
        exfunc.args = args;
        request.exfunc = exfunc;

        var strRequest = JSON.stringify(request);
        var methodType = "";
        var ajaxUrl = "";
        var browserInfo = SECUKITNX_UTIL.getBrowserInfo();
        var urlTmp = document.URL;

        if (urlTmp.indexOf("https") !== -1) {
            ajaxUrl = secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType;
        }else{
            ajaxUrl = secukitnxInfo.exServiceInfo.localhost2 + ":" + secukitnxInfo.exServiceInfo.PortNum2 + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType;
        }

        if (!SECUKITNX_CONST.multiOS) {
            methodType = "GET";
        } else {
            //멀티OS인 경우
            methodType = 'POST';
        }

        var versionFlag = (trident === undefined)?((ieVersion === undefined)?true:false):((trident >= 6)?((ieVersion === undefined)?true:((ieVersion >= 10)?true:false)):false);
        if(versionFlag){
            //ie 10 이상 or ie 외 브라우저
            $.ajax({
                url : ajaxUrl,
                crossDomain: true,
                type: methodType,
                data : strRequest,
                dataType : "JSON",
                success : function(data){
                    var tmp_msg = "";

                    if(data !== ""){
                        tmp_msg = data;
                        _requestMsg = tmp_msg;

                        tmp_msg.callback = SECUKITNX_CONST.cmdCnt[0];
                        SECUKITNX_CONST.cmdCnt.shift();
                        eval(callBackFuncName)(tmp_msg);
                    }
                },
                error : function(req, status, err){
                    nxlog("version check failed.");
                    nxlog("status:" + status + " error : " + err + "response : " + req.responseText);
                    SECUKITNX_CONST.cmdCnt.shift();
                    SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
                }
            });
        }else if (window.XDomainRequest) {
            req.onerror = this.resPonseERRXDR;
            req.onload = this.resPonseXDR;
            req.ontimeout = this.responseTimeOutXDR;
            req.onprogress = this.responseProgressXDR;

            // domain url Check
            // http 인지 https 인지..
            var browserInfo = SECUKITNX_UTIL.getBrowserInfo();
            var urlTmp = document.URL;

            if (urlTmp.indexOf("https") !== -1) {
                if (!SECUKITNX_CONST.multiOS) {
                    // domain url이 https 인 경우
                    req.open("GET", secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    req.send(strRequest);
                }
                else
                {
                    //멀티OS인 경우
                    req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    req.send(strRequest);
                }

            } else if ((urlTmp.indexOf("https") === -1) && (((browserInfo.browser === "MSIE") && (browserInfo.version === "11")) || (browserInfo.browser === "Edge"))) {
                if (!SECUKITNX_CONST.multiOS) {
                    // https는 아니지만, IE11 이상인 경우
                    req.open("GET", secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    req.send(strRequest);
                }
                else
                {
                    //멀티OS인 경우
                    req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    req.send(strRequest);
                }

            } else {
                if (!SECUKITNX_CONST.multiOS) {
                    // domain url이 http 인 경우 IE10 이하
                    req.open("GET", secukitnxInfo.exServiceInfo.localhost2 + ":" + secukitnxInfo.exServiceInfo.PortNum2 + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    setTimeout(function () {
                        try {
                            req.send(strRequest);
                        } catch (e) { nxlog(e); }
                    }, 5);
                }
                else
                {
                    //멀티OS인 경우
                    // domain url이 http 인 경우 IE10 이하
                    req.open('POST', secukitnxInfo.exServiceInfo.localhost2 + ":" + secukitnxInfo.exServiceInfo.PortNum2 + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                    setTimeout(function () {
                        try {
                            req.send(strRequest);
                        } catch (e) { nxlog(e); }
                    }, 5);
                }
            }

        } else if(window.ActiveXObject){
            req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType, true);

            req.setRequestHeader("Accept", "application/JSON");
            req.setRequestHeader("Content-Type", "text/plain");

            req.onreadystatechange = function (aEvt) {
                if (req.readyState === 4) {
                    if (req.status === 200) {
                        if (req.responseText !== "") {
                            var tmp_msg = JSON.parse(req.responseText);
                            _requestMsg = tmp_msg;
                            tmp_msg.callback = SECUKITNX_CONST.cmdCnt[0];
                            SECUKITNX_CONST.cmdCnt.shift();
                            eval(callBackFuncName)(tmp_msg);
                        }
                    }
                    else {
                        nxlog("Error loading page\n");
                        //SECUKITNX_CHECK.setServiceStatus("", "", false, true);
                        SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
                        SECUKITNX_CONST.cmdCnt.shift();
                    }
                }
            };

            req.send(strRequest);
        }
    },

    resPonseXDR: function () {
        var tmp_msg = "";
        if (req.responseText !== "")
        {
            tmp_msg = JSON.parse(req.responseText);
            _requestMsg = tmp_msg;

            tmp_msg.callback = SECUKITNX_CONST.cmdCnt[0];
            SECUKITNX_CONST.cmdCnt.shift();
            eval(callBackFuncName)(tmp_msg);
        }
    },

    resPonseERRXDR: function () {
        SECUKITNX_CONST.cmdCnt.shift();
        //SECUKITNX_CHECK.setServiceStatus("", "", false, true);
        SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
    },

    responseTimeOutXDR: function () {
        SECUKITNX_CONST.cmdCnt.shift();
        //SECUKITNX_CHECK.setServiceStatus("", "", false, true);
        SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
    },

    responseProgressXDR: function () {
    },

    serviceVersionCheck: function (updateInfo) {

        var pluginInfo = SECUKITNX_CONST.pluginInfo[SECUKITNX_CHECK.chkCurrPluginCnt];

        if (updateInfo) {
            //nxlog("_SERVICE.serviceVersionCheck.updateInfo", updateInfo);
            if (updateInfo === "-1") {
                //SECUKITNX_CHECK.setServiceStatus("", "", false, true);
                SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
                return;
            }

            if (!SECUKITNX_CONST.multiOS) {

                if (updateInfo.reply.result && updateInfo.reply.result.kpmsvc) {
                    try {
                        var tmpINSTALLVer = "";
                        // check NX service version
                        tmpINSTALLVer = pluginInfo.moduleInfo.exNxClient_kpmsvc_exe;
                        if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.result.kpmsvc, tmpINSTALLVer) === false) {
                            SECUKITNX_CHECK.setServiceStatus("kpmsvc", updateInfo.reply.result.kpmsvc, false, true);
                        }
                        else {
                            SECUKITNX_CHECK.setServiceStatus("kpmsvc", updateInfo.reply.result.kpmsvc, true, true);
                        }


                        // check NX controller version
                        tmpINSTALLVer = pluginInfo.moduleInfo.exNxClient_kpmcnt_exe;
                        if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.result.kpmcnt, tmpINSTALLVer) === false) {
                            SECUKITNX_CHECK.setServiceStatus("kpmcnt", updateInfo.reply.result.kpmcnt, false, true);
                        }
                        else {
                            SECUKITNX_CHECK.setServiceStatus("kpmcnt", updateInfo.reply.result.kpmcnt, true, true);
                        }


                        // check NX module version
                        if (SECUKITNX_CONST.serviceType === 1) {
                            tmpINSTALLVer = pluginInfo.moduleInfo.exNxClient_secukitNX_dll;
                            if (updateInfo.reply.result.secukitNX) {
                                if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.result.secukitNX, tmpINSTALLVer) === false) {
                                    SECUKITNX_CHECK.setServiceStatus("NX", updateInfo.reply.result.secukitNX, false, true);
                                }
                                else {
                                    SECUKITNX_CHECK.setServiceStatus("NX", updateInfo.reply.result.secukitNX, true, true);
                                }
                            }
                            else {
                                SECUKITNX_CHECK.setServiceStatus("NX", "", false, true);
                            }
                        }

                        // check XML module version
                        if (SECUKITNX_CONST.serviceType === 2) {
                            tmpINSTALLVer = pluginInfo.moduleInfo.exNxClient_secuXML_dll;
                            if (updateInfo.reply.result.secuXML) {
                                if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.result.secuXML, tmpINSTALLVer) === false) {
                                    SECUKITNX_CHECK.setServiceStatus("xml", updateInfo.reply.result.secuXML, false, true);
                                }
                                else {
                                    SECUKITNX_CHECK.setServiceStatus("xml", updateInfo.reply.result.secuXML, true, true);
                                }
                            }
                            else {
                                SECUKITNX_CHECK.setServiceStatus("xml", "", false, true);
                            }
                        }

                        // check KMS module version
                        if (SECUKITNX_CONST.serviceType === 3) {
                            tmpINSTALLVer = pluginInfo.moduleInfo.exNxClient_kms_dll;
                            if (updateInfo.reply.result.kms) {
                                if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.result.kms, tmpINSTALLVer) === false) {
                                    SECUKITNX_CHECK.setServiceStatus("kms", updateInfo.reply.result.kms, false, true);
                                }
                                else {
                                    SECUKITNX_CHECK.setServiceStatus("kms", updateInfo.reply.result.kms, true, true);
                                }
                            }
                            else {
                                SECUKITNX_CHECK.setServiceStatus("kms", "", false, true);
                            }
                        }

                        SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
                    } catch (ex_nx1) {
                        nxlog("_SERVICE.serviceVersionCheck", "NxClient version check exception");
                    }
                }
                else {
                    SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
                }
            }
            else
            {
                //멀티 OS인 경우
                if (_OSName === "Mac/iOS")
                {
                    // MacOS인 경우
                    if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.version, MULTIOS_MacOS_Ver) === true) {
                        SECUKITNX_CONST.isInstalled = true;
                    }
                    else {
                        SECUKITNX_CONST.isInstalled = false;
                    }
                }
                else if (_OSName === "Linux/Ubuntu")
                {
                    // Linux/Ubuntu
                    if (SECUKITNX_UTIL.diffVersion(updateInfo.reply.version, MULTIOS_Ubuntu_Ver) === true) {
                        SECUKITNX_CONST.isInstalled = true;
                    }
                    else {
                        SECUKITNX_CONST.isInstalled = false;
                    }

                }

                SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
            }

        } else {
            nxlog("_SERVICE.serviceVersionCheck", pluginInfo.exPluginName + " updateInfo Error");
            SECUKITNX_CHECK.moduleCheck(SECUKITNX_CHECK.chkCurrPluginCnt + 1);
        }
    },
    setServiceStatus: function (type, localVer, status, isNext) {

        switch(type)
        {
            case 'kpmsvc':
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kpmsvcVer = localVer;
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kpmsvc = status;
                break;
            case 'kpmcnt':
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kpmcntVer = localVer;
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kpmcnt = status;
                break;
            case 'NX':
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].nxVer = localVer;
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].nx = status;
                break;
            case 'kms':
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kmsVer = localVer;
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].kms = status;
                break;
            case 'xml':
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].xmlVer = localVer;
                SECUKITNX_CHECK.chkInfoStatus.info[SECUKITNX_CHECK.chkCurrPluginCnt].xml = status;
                break;
        }
    }
};

function nxsModuleChk() {
    var Data = { 'license': secukitnxInfo.license };
    SECUKITNX.Invoke('checkLicense', [Data], 'secukitnxInterface.SecuKitNX_EXCallBack', 'nxsModuleChkCallback');
}

function nxsModuleChkCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        nxlog('_nxsModuleChkCallback : ', err);
    }

    // 화면 블락 해제
    if (NXSBlockWrapLayer === true) {
        try {
            $('.nxs_loading_block').hide();
        } catch (e) { console.log(e); }
    }

    if (errorCheck === undefined) {
        if (reply.checkLicense === "Y") {
            NXsetEnvironment();
            SECUKITNX_CONST.load = true;
            SECUKITNX_CONST.licenseChk = true;
            SecuKitNX_Ready(true);
            nxlog("_nxsModuleChkCallback - nxLicense : ", reply.checkLicense);
        } else {
            SECUKITNX_CONST.load = true;
            SECUKITNX_CONST.licenseChk = false;
            SecuKitNX_Ready(false);
            nxlog("_nxsModuleChkCallback - nxLicense : ", reply.checkLicense);
            KICA_Error.init();
            var location = 'NXS_LICENSE ERROR';
            var reason = 'LICENSE ERR';
            KICA_ERROR_RESOURCE.ErrorMessage(location, reason, 'ERR_CLIENT_LICENSE');
            var ScriptErrorMessage = KICA_Error.getScriptError();
            alert(ScriptErrorMessage);
        }
    } else {
        // 모듈은 로드 됐지만.. 라이센스 체크는 안된 상태!!
        SECUKITNX_CONST.load = true;
        SECUKITNX_CONST.licenseChk = false;
        nxlog("_nxsModuleChkCallback - nxLicense: ", reply.ERROR_MESSAGE);
        SecuKitNX_Ready(false);

        KICA_Error.init();
        $('.nx-cert-select').hide(); $('#nx-pki-ui-wrapper').hide(); KICA_Error.init();
        KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
        var errorMsg = KICA_Error.getError();
        alert(errorMsg);
    }
}


/*
 * SECUKITNX_EX
 */
var SECUKITNX_EX = function (property) {

    this.isInstalled = property.isInstalled;
    this.exPluginName = property.exPluginName;
    this.exModuleName = property.exModuleName ? property.exModuleName : property.exProtocolName;
    this.exDefaultCallbackName = property.exDefaultCallback ? property.exDefaultCallback : property.exPluginName + ".exDefaultCallback";

    hostid = property.hostid ? property.hostid : location.host;

    this.initEXInfoArr = [];
    this.exInterfaceArr = [];
    this.exEcho = false;
    this.setEcho = function (status) {
        this.exEcho = status;
    };

    var req = null;
    var pageCallbackTmp = null;
    var callBackFuncName = null;
    /*
     * Invoke(NxClient)
     */
    this.Invoke = function (fname, args, exCallback, pageCallback)
    {
        pageCallbackTmp = null;
        //SECUKITNX_CONST.cmdCnt.push(pageCallback);
        pageCallbackTmp = pageCallback;

        var ieVersion, trident, reg;
        var agent = window.navigator.userAgent.toLowerCase();

        //ie 버전 구하기
        reg = new RegExp("trident/([0-9]{1,}[\.0-9]{0,})" );
        if(reg.exec( agent ) != null){
            trident = parseFloat(RegExp.$1);
        }

        reg = new RegExp("msie ([0-9]{1,}[\.0-9]{0,})" );
        if(reg.exec( agent ) != null){
            ieVersion = parseFloat(RegExp.$1);
        }

        if(trident < 6 || ieVersion < 10){
            //ie 9이하, 다른 브라우저
            if (window.XDomainRequest) { // IE 8, 9, 10
                req = new XDomainRequest();
                //pageCallbackTmp = pageCallback;
            } else if (window.ActiveXObject) {  // IE 5, 6, 7
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } else if (window.XMLHttpRequest) { // Etc.
                req = new XMLHttpRequest();
            }
        }

        // JSON Build
        var request = new Object;
        request.callback = exCallback;
        callBackFuncName = exCallback ? exCallback : null;

        tmp_serviceType = SECUKITNX_CONST.serviceType;      // XML & KMS 이용시  : nx 1,              xml 2,              kms 3

        switch(tmp_serviceType)
        {
            // XML & KMS 이용시  : nx empty,          xml 1234&OP=1,      kms GetVersion ???
            case 1: SECUKITNX_CONST.serviceToken = 'empty';
                break;
            case 2: SECUKITNX_CONST.serviceToken = '1234&OP=1';
                break;
            case 3: SECUKITNX_CONST.serviceToken = 'GetVersion';
                break;
        }

        tmp_TOKEN = SECUKITNX_CONST.serviceToken;

        var exfunc = new Object;
        exfunc.fname = fname;
        exfunc.args = args;

        request.exfunc = exfunc;

        var strRequest = JSON.stringify(request);
        var ajaxUrl = "";
        var methodType = 'POST';

        var browserInfo = SECUKITNX_UTIL.getBrowserInfo();
        var urlTmp = document.URL;

        if(urlTmp.indexOf('https') !== -1){
            ajaxUrl = secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType;
        }else{
            ajaxUrl = secukitnxInfo.exServiceInfo.localhost2 + ":" + secukitnxInfo.exServiceInfo.PortNum2 + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType
        }

        var versionFlag = (trident === undefined)?((ieVersion === undefined)?true:false):((trident >= 6)?((ieVersion === undefined)?true:((ieVersion >= 10)?true:false)):false);
        if(versionFlag){
            if(strRequest.indexOf("??")<1){
                //ie 10 이상
                $.ajax({
                    crossOrigin : true, 				//@add nayagdkim 180313
                    url : ajaxUrl,
                    crossDomain: true,
                    type: methodType,
                    data : strRequest,
                    dataType : "JSON",
                    contextType : "application/json; charset=utf-8",
                    success : function(data){
                        var tmp_msg = data;
                        if(data !== ""){
                            tmp_msg.callback = pageCallbackTmp;
                            eval(callBackFuncName)(tmp_msg);
                        }
                    },
                    error : function(req, status, error){
                        nxlog("Error loading NX Module\n");
                        var tmp_msg = JSON.parse("{\"status\":\"" + req.status + "\", \"reply\" : {\"ERROR_CODE\":\"00000000\", \"ERROR_MESSAGE\" : \"Invoke SecuKitNX Module ajax Network connection response error. [" + error.Error + "] " + req.responseText + "\"}}");
                        nxlog("network response result :" + tmp_msg);
                        tmp_msg.callback = pageCallbackTmp;
                        eval(callBackFuncName)(tmp_msg);
                    }
                });
            } else{
                $.ajax({
                    crossOrigin : true, 				//@add nayagdkim 180313
                    url : ajaxUrl,
                    crossDomain: true,
                    type: methodType,
                    data : strRequest,
                    contextType : "application/json; charset=utf-8",
                    success : function(data){
                        var tmp_msg = JSON.parse(data);
                        if(data !== ""){
                            tmp_msg.callback = pageCallbackTmp;
                            eval(callBackFuncName)(tmp_msg);
                        }
                    },
                    error : function(req, status, error){
                        nxlog("Error loading NX Module\n");
                        var tmp_msg = JSON.parse("{\"status\":\"" + req.status + "\", \"reply\" : {\"ERROR_CODE\":\"00000000\", \"ERROR_MESSAGE\" : \"Invoke SecuKitNX Module ajax Network connection response error. [" + error.Error + "] " + req.responseText + "\"}}");
                        nxlog("network response result :" + tmp_msg);
                        tmp_msg.callback = pageCallbackTmp;
                        eval(callBackFuncName)(tmp_msg);
                    }
                });
            }
        }else if (window.XDomainRequest) {
            //MSIE 10이하
            req.onerror = this.resPonseERRXDR;
            req.onload = this.resPonseXDR;
            req.ontimeout = this.responseTimeOutXDR;
            req.onprogress = this.responseProgressXDR;

            // domain url Check
            // http 인지 https 인지..
            var browserInfo = SECUKITNX_UTIL.getBrowserInfo();
            var urlTmp = document.URL;
            if (urlTmp.indexOf('https') !== -1) {
                // domain url이 https 인 경우
                req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                req.send(strRequest);
            } else if ((urlTmp.indexOf('https') === -1) && (((browserInfo.browser === 'MSIE') && (browserInfo.version === "11")) || (browserInfo.browser === 'Edge'))) {
                // https는 아니지만, IE11 이상인 경우
                req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                req.send(strRequest);
            } else {
                // domain url이 http 인 경우, IE10 이하
                req.open('POST', secukitnxInfo.exServiceInfo.localhost2 + ":" + secukitnxInfo.exServiceInfo.PortNum2 + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType);
                setTimeout(function () { req.send(strRequest); }, 5);
            }

        } else if(window.ActiveXObject){
            req.open('POST', secukitnxInfo.exServiceInfo.localhost + ":" + secukitnxInfo.exServiceInfo.PortNum + "?TOKEN=" + tmp_TOKEN + "&serviceType=" + tmp_serviceType, true);

            if (tmp_serviceType === 2)
            {
                // XML 인 경우
                req.withCredentials = true;
                req.setRequestHeader('Accept', 'application/JSON, OP:0x000001');
            }
            else
            {
                req.setRequestHeader('Accept', 'application/JSON');
            }

            req.setRequestHeader('Content-Type', 'text/plain');

            req.onreadystatechange = function (aEvt) {
                if (req.readyState === 4) {
                    if (req.status === 200) {
                        if (req.responseText !== "")
                        {
                            var tmp_msg = JSON.parse(req.responseText);
                            tmp_msg.callback = pageCallbackTmp;
                            eval(callBackFuncName)(tmp_msg);
                        }
                    }
                    else if (req.status !== 0 && req.status !== 200) {
                        nxlog("Error loading page\n");
                        var tmp_msg = JSON.parse("{\"status\":\"0\", \"reply\" : {\"ERROR_CODE\":\"00000000\", \"ERROR_MESSAGE\" : \"SecuKitNX Module Network connection error. [" + req.status + "]\"}}");
                        tmp_msg.callback = pageCallbackTmp;
                        eval(callBackFuncName)(tmp_msg);
                    }
                }
            };

            req.send(strRequest);
        }

    };

    this.resPonseXDR = function () {
        var tmp_msg = '';
        if (req.responseText !== "")
        {
            tmp_msg = JSON.parse(req.responseText);
            tmp_msg.callback = pageCallbackTmp;
            eval(callBackFuncName)(tmp_msg);
        }
    };

    this.resPonseERRXDR = function () {
        nxlog("Error loading NX Module\n");
        var tmp_msg = JSON.parse("{\"status\":\"0\", \"reply\" : {\"ERROR_CODE\":\"00000000\", \"ERROR_MESSAGE\" : \"SecuKitNX Module Network connection response error. [" + req.status + "]\"}}");
        tmp_msg.callback = pageCallbackTmp;
        eval(callBackFuncName)(tmp_msg);
        //SECUKITNX_CONST.cmdCnt.shift();
    };
    this.responseTimeOutXDR = function () {
        nxlog("Error loading NX Module TimeOut\n");
        var tmp_msg = JSON.parse("{\"status\":\"0\", \"reply\" : {\"ERROR_CODE\":\"00000000\", \"ERROR_MESSAGE\" : \"SecuKitNX Module Network connection TimeOut error. [" + req.status + "]\"}}");
        tmp_msg.callback = pageCallbackTmp;
        eval(callBackFuncName)(tmp_msg);
        //SECUKITNX_CONST.cmdCnt.shift();
    };
    this.responseProgressXDR = function () {
    }

    this.InitEX = function (id, cmd, fname, funcArgsArray, callback) {

        var initUrl = this.createInitEXURL(id, this.exPluginName + ".InitEXCallback");
        var request = {};
        request.id = id;
        request.tabid = SECUKITNX_CONST.extabid;
        request.module = this.exModuleName;
        request.cmd = cmd;
        request.origin = location.origin !== undefined ? location.origin : (location.protocol + "//" + hostid);
        request.exfunc = {};
        request.exfunc.fname = fname;
        request.callback = callback;
        if (this.exEcho) request.echo = true;

        var exfunc = request.exfunc;
        if (funcArgsArray) {
            if (funcArgsArray instanceof Array) {
                exfunc.args = funcArgsArray;
            } else {
                var arr = [funcArgsArray];
                exfunc.args = arr;
            }
        } else {
            exfunc.args = [];
        }
        var initEXInfo = { "id": id, "json": request, "callback": callback };
        this.initEXInfoArr.push(initEXInfo);
    };

    this.InitEXCallback = function (id) {
        var callback,
            reqJSON;

        if (this.initEXInfoArr) {
            for (var i = 0; i < this.initEXInfoArr.length; i++) {
                initExObj = this.initEXInfoArr[i];
                if (initExObj.id === id) {
                    reqJSON = initExObj.json;
                    callback = initExObj.json.callback;
                    this.initEXInfoArr.splice(i, 1);
                    break;
                }
            }
        } else {
            nxlog(this.exPluginName + ".InitEXCallback [exception]", NX_EXPROTO_TEXT_15);
            nxalert(this.exPluginName + ".InitEXCallback [exception]", NX_EXPROTO_TEXT_15);
            return;
        }

        this.InvokeEX(id, callback, reqJSON);
    };

    this.InvokeEX = function (id, callback, reqJSON) {
        var frm = document.getElementById(SECUKITNX_CONST.exFormName);
        frm.target = SECUKITNX_CONST.exIframeName;
        frm.action = this.createEXURL(id, this.exPluginName + ".InvokeCallback");

        if (frm.elements.length > 1) {
            for (var i = 0; i < frm.elements.length;) {
                var chkElement = frm.elements[i];
                var chkId = chkElement.id ? chkElement.id : "";
                var chkName = chkElement.name ? chkElement.name : "";

                if (chkId !== SECUKITNX_CONST.exFormDataName || chkName !== SECUKITNX_CONST.exFormDataName) {
                    nxlog(this.exPluginName + ".InvokeEX.form remove garbage param", chkId + "::" + chkName);
                    chkElement.parentElement.removeChild(chkElement);
                } else {
                    i++;
                }
            }
        }

        var node = frm.elements[0];
        node.value = JSON.stringify(reqJSON);
        nxlog(this.exPluginName + ".InvokeEX.form.action", frm.action);
        nxlog(this.exPluginName + ".InvokeEX." + this.exFormDataName + ".value", node.value);
        frm.submit();
    };

    this.InvokeCallback = function (response) {
        if (response) {
            try {
                nxlog(this.exPluginName + ".InvokeCallback.response", response);
                if (typeof response === "object") {
                    var strSerial = JSON.stringify(response);
                } else if (typeof response === "string") {
                    response = JSON.parse(response);
                }

                var status = response.status;
                if (status === "TRUE") {	// success
                    var id = response.id;
                    var funcInfo = {};
                    for (var i = 0; i < this.exInterfaceArr.length; i++) {
                        if (this.exInterfaceArr[i]) {
                            var arrObj = this.exInterfaceArr[i];
                            if (arrObj.id === id) {
                                funcInfo = arrObj;
                                this.exInterfaceArr.splice(i, 1);
                                break;
                            }
                        }
                    }
                    var callback = funcInfo.EXCallback;
                    var reply = response.reply.reply;

                    // run callback
                    if (callback) {
                        if (reply instanceof Array) {
                            var strReply = {};
                            strReply.callback = funcInfo.pageCallback;
                            var replyArr;
                            replyArr = "[";
                            for (var i in reply) {
                                var str = reply[i];
                                str = str.replace("\\r", "\r");
                                str = str.replace("\\n", "\n");
                                replyArr += "'" + str + "',";
                            }
                            replyArr += "]";
                            strReply.reply = replyArr;
                            callback = callback + "(" + JSON.stringify(strReply) + ");";
                        } else if (typeof reply === 'string') {
                            var strReply = {};
                            strReply.callback = funcInfo.pageCallback;
                            strReply.reply = reply.replace("\\r", "\r").replace("\\n", "\n");
                            callback = callback + "(" + JSON.stringify(strReply) + ");";
                        } else if (typeof reply === 'object') {
                            //reply.id = id;
                            reply.callback = funcInfo.pageCallback;
                            callback = callback + "(" + JSON.stringify(reply) + ");";
                        } else {
                            callback = callback + "()";
                        }
                        setTimeout(function () { eval(callback); }, 5);
                    }

                } else {
                    nxlog(this.exPluginName + ".InvokeCallback", "native response status not TRUE");
                    eval(this.exDefaultCallbackName)(response);
                }
            } catch (e) {
                nxlog(this.exPluginName + ".InvokeCallback [exception]", e);
                nxlog(this.exPluginName + ".InvokeCallback [exception]", "native response process exception");
                eval(this.exDefaultCallbackName)(response);
            }
        } else {
            nxlog(this.exPluginName + ".InvokeCallback", "native call not response");
            eval(this.exDefaultCallbackName)();
        }
    };
};

/*
 * SECUKITNXS_UTIL
 */
var SECUKITNX_UTIL = {

    nxlog: function (func, value) {
        if (SECUKITNX_CONST.debug) {
            var strlog;
            try {
                if (typeof value === "undefined") {
                    strlog = "[undefined]";
                } else {
                    if (typeof value === "function") {
                        strlog = "[function] " + value;
                    } else if (typeof value === "object") {
                        try {
                            strlog = "[json] " + JSON.stringify(value);
                        } catch (e) {
                            strlog = "[object] " + value;
                        }
                    } else if (typeof value === "number") {
                        strlog = "[number] " + value;
                    } else if (typeof value === "string") {
                        strlog = "[string] " + value;
                    } else if (typeof value === "boolean") {
                        strlog = "[boolean] " + value;
                    } else {
                        strlog = "[other] " + value.toString();
                    }
                }
            } catch (e) {
                strlog = " [nxlog exception] " + typeof value;
            }

            if (!window.console) {console = window.console || { log: function () { } };}
            console.log("[nxlog] " + SECUKITNX_CONST.frameName + func + " : " + strlog);
            return;
        }
    },
    nxalert: function (func, value) {
        if (SECUKITNX_CONST.debugAlert) {
            var msg;
            try {
                if (value) {
                    if (typeof value === "object") {
                        msg = JSON.stringify(value);
                    } else if (typeof value === "function") {
                        msg = value;
                    } else if (typeof value === "number") {
                        msg = value;
                    } else if (typeof value === "string") {
                        msg = value;
                    } else if (typeof value === "boolean") {
                        msg = value;
                    } else {
                        msg = value.toString();
                    }
                }
            } catch (e) {
                msg = "[exception] " + value;
            }

            if (func) {
                alert(func + " : " + msg);
            } else {
                alert(msg);
            }
            return;
        }
    },

    findPath: function (currentFrame, tmpPath) {
        if (currentFrame === top) return "";
        var path, pathObj;
        if (!tmpPath) {
            pathObj = top;
            path = "top";
        } else {
            pathObj = eval(tmpPath);
            path = tmpPath;
        }

        var frameCnt = pathObj.frames.length;
        for (var i = 0 ; i < frameCnt; i++) {
            var fr = pathObj.frames[i];
            if (currentFrame === fr) {
                return path + ".frames[" + i + "].";
            }
            if (fr.frames.length > 0) {
                var resultPath = SECUKITNX_UTIL.findPath(currentFrame, path + ".frames[" + i + "]");
                if (resultPath) {
                    return resultPath;
                }
            }
        }
        return;
    },

    getOSInfo: function () {
        var tp = navigator.platform,
            ua = navigator.userAgent,
            tem;

        var result = {};

        // platform
        if (tp === "Win32" || tp === "Win64") {
            if (ua.search("Windows Phone") >= 0) {
                result.platform = "Windows Phone";
                result.name = "Windows Phone";
            } else {
                result.platform = "WINDOWS";
            }
        }
        else {
            result.platform = "UNKNOWN";
        }

        // version, bit
        if (result.platform == "WINDOWS") {
            if (ua.indexOf("Windows NT 5.1") !== -1) { result.version = "5.1"; result.name = "XP"; }
            else if (ua.indexOf("Windows NT 6.0") !== -1) { result.version = "6.0"; result.name = "VISTA"; }
            else if (ua.indexOf("Windows NT 6.1") !== -1) { result.version = "6.1"; result.name = "7"; }
            else if (ua.indexOf("Windows NT 6.2") !== -1) { result.version = "6.2"; result.name = "8"; }
            else if (ua.indexOf("Windows NT 6.3") !== -1) { result.version = "6.3"; result.name = "8.1"; }
            else if (ua.indexOf("Windows NT 6.4") !== -1) { result.version = "6.4"; result.name = "10"; }
            else if (ua.indexOf("Windows NT 10.0") !== -1) { result.version = "10.0"; result.name = "10"; }
            else if (ua.indexOf("Windows NT") !== -1) {
                result.version = "UNKNOWN"; result.name = "UNKNOWN";
            } else {
                result.version = "UNKNOWN"; result.name = "UNKNOWN";
            }

            if (ua.indexOf("WOW64") !== -1 || ua.indexOf("Win64") !== -1) result.bit = "64";
            else result.bit = "32";

        } else {
            result.version = "UNKNOWN"; result.name = "UNKNOWN";
        }

        return result;
    },
    isWin: function () {
        var OSInfo = SECUKITNX_CONST.OSInfo.platform;
        if (!OSInfo) OSInfo = SECUKITNX_UTIL.getOSInfo().platform;
        if (OSInfo === "WINDOWS") return true;
        return false;
    },
    getBrowserInfo: function () {
        var
            tp = navigator.platform,
            N = navigator.appName,
            ua = navigator.userAgent,
            tem;
        var result, M;

        // if Edge
        M = ua.match(/(edge)\/?\s*(\.?\d+(\.\d+)*)/i);
        M = M ? { "browser": "Edge", "version": M[2] } : M;

        // if opera
        if (!M) {
            M = ua.match(/(opera|opr)\/?\s*(\.?\d+(\.\d+)*)/i);
            if (M && (tem = ua.match(/version\/([\.\d]+)/i)) !== null) M[2] = tem[1];
            M = M ? { "browser": "Opera", "version": M[2] } : M;
        }

        // if IE9 under
        if (!M) {
            M = ua.match(/MSIE ([6789].\d+)/);
            if (M) M = { "browser": "MSIE", "version": M[1] };
        }

        // others
        if (!M) {
            M = ua.match(/(msie|trident|chrome|safari|firefox)\/?\s*(\.?\d+(\.\d+)*)/i);
            if (M) {
                if ((tem = ua.match(/rv:([\d]+)/)) !== null) {
                    M[2] = tem[1];
                } else if ((tem = ua.match(/version\/([\.\d]+)/i)) !== null) {
                    M[2] = tem[1];
                }
                if (M[1] === "Trident") M[1] = "MSIE";
                M = M ? { "browser": M[1], "version": M[2] } : { "browser": N, "version1": navigator.appVersion, "other": '-?' };
            }
        }

        if (!M) {
            if (typeof Proxy) {
                M = { "browser": "Edge", "version": "" };
            }
        }

        if (!M) {
            return { "browser": "UNDEFINED", "version": "" };
        }

        if (M.version) {
            var verArr = (M.version).split(".");
            M.version = verArr[0];
        }

        if (M.browser === "MSIE" || M.browser === "Edge") {
            if (tp === "Win32") {
                M.bit = "32";
            } else if (tp === "Win64") {
                M.bit = "64";
            }
        }

        result = M;

        return result;
    },
    getBrowserVer: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        return browserInfo.version;
    },
    getBrowserBit: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        return browserInfo.bit;
    },
    isIE: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if (browserInfo.browser.toLowerCase().indexOf("msie") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    isEdge: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if (browserInfo.browser.toLowerCase().indexOf("edge") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    isChrome: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if (browserInfo.browser.toLowerCase().indexOf("chrome") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    isFirefox: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if (browserInfo.browser.toLowerCase().indexOf("firefox") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    isOpera: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if (browserInfo.browser.toLowerCase().indexOf("opera") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    isSafari: function () {
        var browserInfo = SECUKITNX_CONST.browserInfo;
        if (!browserInfo) { browserInfo = SECUKITNX_UTIL.getBrowserInfo(); }
        if ((browserInfo.browser).toLowerCase().indexOf("safari") !== -1) {
            return true;
        } else {
            return false;
        }
    },
    createId: function () {
        var id = new Date().getTime() + "_" + Math.floor(Math.random() * 10000) + 1;
        //nxlog("SECUKITNX_UTIL.createId.id", id);
        return id;
    },
    /**
     * true : 기존 설치된 모듈이 버전 더 높거나 같음
     * false : 기존 설치된 모듈이 버전이 낮음. 업그레이드가 필요함.
     */
    diffVersion: function (curVersion, svrVersion) {
        var index;
        try {
            index = curVersion.indexOf('version:', 0);
        } catch (e) {
            return false;
        }

        if (index >= 0) {
            curVersion = curVersion.substring(index + 8, curVersion.length);
            var arrayOldVersion = curVersion.split('.');
            if (arrayOldVersion.length < 4) { arrayOldVersion = curVersion.split(','); }
            if (arrayOldVersion.length < 4) { return false; }
            var arrayNewVersion = svrVersion.split('.');

            for (var i = 0; i < 4; i++) {
                if (parseInt(arrayOldVersion[i], 10) > parseInt(arrayNewVersion[i], 10)) {
                    return true;
                } else if (parseInt(arrayOldVersion[i], 10) < parseInt(arrayNewVersion[i], 10)) {
                    SECUKITNX_CONST.nextVerCheck = true;
                    return false;
                }
            }
            return true;
        } else {
            var arrayOldVersion = curVersion.split('.');
            if (arrayOldVersion.length < 4) { arrayOldVersion = curVersion.split(','); }
            if (arrayOldVersion.length < 4) { return false; }
            var arrayNewVersion = svrVersion.split('.');

            for (var i = 0; i < 4; i++) {
                if (parseInt(arrayOldVersion[i], 10) > parseInt(arrayNewVersion[i], 10)) {
                    return true;
                } else if (parseInt(arrayOldVersion[i], 10) < parseInt(arrayNewVersion[i], 10)) {
                    SECUKITNX_CONST.nextVerCheck = true;
                    return false;
                }
            }
            return true;
        }
    }
};

window.nxlog = SECUKITNX_UTIL.nxlog;
window.nxalert = SECUKITNX_UTIL.nxalert;


// Get File Path
function NXGetPFXFilePath() {
    var cmd = 'NXGetPFXFilePath.getFilePath';
    var Data = {
        'fileType': "pfx"
    };

    var param = JSON.stringify(Data);
    secukitnxInterface.SecuKitNX_EX(cmd, param);
}

function NXGetPFXFilePathCallback(reply) {
    var errorCheck = -1;
    try {
        errorCheck = reply.ERROR_CODE;
    } catch (err) {
        console.log(err);
    }

    if (errorCheck === undefined) {
        //결과값
        var res = reply.getFilePath;
        document.getElementById('importPFXFileName').value = res;
    }
    else {
        KICA_Error.init();
        KICA_Error.setError(reply.ERROR_CODE, reply.ERROR_MESSAGE);
        var errorMsg = KICA_Error.getError();
        alert(errorMsg);
    }
}

function secunx_Check() {
    SECUKITNX_CHECK.check([secukitnxInfo], "secunx_CheckCallback");
}

function secunx_CheckCallback(check) {

    if (check.status) {
        SECUKITNX_LOADING("nxsModuleChk");
    } else {
        setTimeout(function () {
            secunx_Check();
        }, 500);
    }
}

function secunx_Loading() {
    var localPage_href = window.document.location.href;
    var localPage = localPage_href.indexOf(NX_INSTALL_PAGENAME);
    if (localPage === -1) {
        // 모듈 로딩 전 화면 블락
        if (NXSBlockWrapLayer === true)
        {
            try {
                $('.nxs_loading_block').show();
            } catch (e) { console.log(e); }
        }
    }

    SECUKITNX_CHECK.check([secukitnxInfo], "secunx_LoadingCallback");
}

function secunx_LoadingCallback(check) {
    nxlog("secunx_LoadingCallback", check);
    if (check.status) {
        SECUKITNX_LOADING("secunx_ModuleInit");
    }
    else {
        $('.nx-cert-select').hide();
        $('#nx-pki-ui-wrapper').hide(); KICA_Error.init();
        var location = NX_SECUKIT_NX_TEXT_1;
        var reason = '';
        var errorcode = '';
        if (SECUKITNX_CONST.nextVerCheck == true)
        {
            switch(SECUKITNX_CONST.serviceType)
            {
                case 1: errorcode = 'ERR_CLIENT_RELEASE_VERSION';
                    break;
                case 2: errorcode = 'ERR_CLIENT_RELEASE_VERSION_XML';
                    break;
                case 3: errorcode = 'ERR_CLIENT_RELEASE_VERSION_KMS';
                    break;
                default: errorcode = 'ERR_CLIENT_RELEASE_VERSION';
                    break;
            }

        } else {

            switch (SECUKITNX_CONST.serviceType) {
                case 1: errorcode = 'ERR_CLIENT_NO_INSTALL';
                    break;
                case 2: errorcode = 'ERR_CLIENT_NO_INSTALL_XML';
                    break;
                case 3: errorcode = 'ERR_CLIENT_NO_INSTALL_KMS';
                    break;
                default: errorcode = 'ERR_CLIENT_NO_INSTALL';
                    break;
            }
        }

        KICA_ERROR_RESOURCE.ErrorMessage(location, reason, errorcode);
        var ScriptErrorMessage = KICA_Error.getScriptError();
        var localPage_href = window.document.location.href;
        var localPage = localPage_href.indexOf(NX_INSTALL_PAGENAME);

        if (NX_INSTALL_FLAG === false) {
            // 설치페이지를 별도로 운영하지 않는 경우
            alert(ScriptErrorMessage);
            NXdownClientURL();
            secunx_Check();
        } else {
            // 설치페이지가 있는 경우
            if (localPage !== -1 && downClientFlag === false) {
                // 호출된 페이지가 설치 페이지이고, 설치파일이 다운로드 안된 경우
                NXdownClientURL();
                secunx_Check();
            } else if (localPage === -1 && downClientFlag === false) {
                // 호출된 페이지가 설치 페이지가 아닌 경우
                alert(ScriptErrorMessage);
                window.location = NX_INSTALL_PAGE;
                return;
            }
        }

        SECUKITNX_CHECK.chkInterValChk = true;
        SECUKITNX_CHECK.moduleCheck(0);

        SecuKitNX_Ready(false);
    }
}

function secunx_ModuleInit(result) {
     if (result) {
        nxsModuleChk();
    } else {
        SecuKitNX_Ready(false);
        SECUKITNX_CONST.load = false;
    }
}
