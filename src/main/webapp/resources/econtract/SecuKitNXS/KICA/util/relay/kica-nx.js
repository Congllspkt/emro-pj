var NxRelayCert 	= function() {
    this.address 	= new RelayAddress();
};

var RelayAddress 	= function() {
	
    // local
    this.authCode 	= "http://192.168.110.181:8443/web-relay/cert/sid.sg";
    this.certUp 	= "http://192.168.110.181:8443/web-relay/cert/cert_up.sg";
    this.certDown 	= "http://192.168.110.181:8443/web-relay/cert/cert_dn.sg";
    this.confirm 	= "http://192.168.110.181:8443/web-relay/cert/confirm.sg";

    // dev
    //this.authCode 	= "https://dev.signgate.com:2443/web-relay/cert/sid.sg";
    //this.certUp 	= "https://dev.signgate.com:2443/web-relay/cert/cert_up.sg";
    //this.certDown 	= "https://dev.signgate.com:2443/web-relay/cert/cert_dn.sg";
    //this.confirm 	= "https://dev.signgate.com:2443/web-relay/cert/confirm.sg";

    // real
    //this.authCode 	= "https://www.signgate.com/web-relay/cert/sid.sg";
    //this.certUp 	= "https://www.signgate.com/web-relay/cert/cert_up.sg";
    //this.certDown 	= "https://www.signgate.com/web-relay/cert/cert_dn.sg";
    //this.confirm 	= "https://www.signgate.com/web-relay/cert/confirm.sg";

};

NxRelayCert.prototype = {
    /**
     * 서버에게 인증코드를 요청한다
     */
    getAuthCode : function() {
        var resultValue 	= new Object();
        var innerHtml 		= "";
        $.ajax({
            url: this.address.authCode
            , crossDomain: true
            , async: false
            , dataType: "text"
            , type: 'GET'
            , data: ""
            , success: 	function(response, status, request) {
                innerHtml = request.responseText;
                var responseMap 	= getParameterMap(innerHtml);
                var resultCode 		= responseMap["resultCode"];
                var message 		= responseMap["message"];
                var validTime 		= responseMap["validTime"];
                var authcode 		= responseMap["authcode"];

                if (resultCode == "2000") {
                    resultValue["authcode"] 	= authcode;
                    resultValue["validTime"] 	= validTime;
                } else {
                    alert(message);
                }
            }
            , error: function(request, status, error) {
                alert("error:" + error + ":status:" + status + ", 중개서버에 요청하는 도중 일시적인 문제가 발생했습니다. 다시 시도하세요");
            }
        });

        //return this.message;
        return resultValue;
    },

    /**
     * 서버에 인증서를 반환한다
     */
    setCert : function(authcode, p12, signature, kmsCertificate) {        
        var innerHtml 		= "";
        var resultValue 	= "";
        $.ajax({
            url: this.address.certUp
            , crossDomain: true
            , async: false
            , dataType: "text"
            , type: 'POST'
            , data: "authcode=" + encodeURIComponent(authcode) + "&pkcs12=" + encodeURIComponent(p12) + "&signature=" + encodeURIComponent(signature) + "&certificate=" + encodeURIComponent(kmsCertificate)
            , success: 	function(response, status, request) {
                innerHtml = request.responseText;
                var responseMap 	= getParameterMap(innerHtml);
                var resultCode 		= responseMap["resultCode"];
                var message 		= responseMap["message"];
                if (resultCode == "2000") {
                    resultValue 	= resultCode;
                    sendPC2Phone_OK();
                } else {
                    alert(message);
                }
            }
            , error: function(request, status, error) {
                alert("error:" + error + ":status:" + status + "중개서버에 요청하는 도중 일시적인 문제가 발생했습니다. 다시 시도하세요");
            }
        });

        return resultValue;
    },

    /**
     * authcode에 대응되는 p12인증서를 중개서버로 부터 갖고온다
     *
     */
    getCert: function(authcode) {
        var innerHtml 		= "";
        var resultValue 	= "";
        $.ajax({
            url: this.address.certDown
            , crossDomain: true
            , async: false
            , dataType: "text"
            , type: 'POST'
            , data: "authcode=" + encodeURI(authcode)
            , success: 	function(response, status, request) {
                innerHtml 			= request.responseText;
                var responseMap 	= getParameterMap(innerHtml);
                var resultCode 		= responseMap["resultCode"];
                var message 		= responseMap["message"];
                var pkcs12 			= responseMap["pkcs12"];
                if (resultCode == "2000") {
                    resultValue 	= pkcs12;
                } else {
                    alert(message);
                }
            }
            , error: function(request, status, error) {
                alert("중개서버에 요청하는 도중 일시적인 문제가 발생했습니다. 다시 시도하세요");
            }
        });

        return resultValue;
    },

    /**
     * 중개서버에게 인증서를 삭제할 것을 요청한다
     */
    confirm: function(p12HashValue) {
        var innerHtml 		= "";
        var resultValue 	= "";

        $.ajax({
            url: this.address.confirm
            , crossDomain: true
            , async: false
            , dataType: "text"
            , type: 'POST'
            , data: "hash=" + encodeURI(p12HashValue)
            , success: 	function(response, status, request) {
                innerHtml 			= request.responseText;
                var responseMap 	= getParameterMap(innerHtml);
                var resultCode 		= responseMap["resultCode"];
                var message 		= responseMap["message"];
                if (resultCode == "2000") {
                    resultValue 	= resultCode;
                } else {
                    alert(message);
                }
            }
            , error: function(request, status, error) {
                alert("중개서버에 요청하는 도중 일시적인 문제가 발생했습니다. 다시 시도하세요");
            }
        });

        return resultValue;
    }
};

var nxrelay 		= new NxRelayCert();

/**
 * 메시지 형식 : {authcode=2000}{message=연습 성공 테스트2}{authcode=974158669309}\n\n{validTime=300000}
 * 받을 메시지를 KEY/VALUE 형태로 반환
 *
 */
function getParameterMap(message) {
    var parameters 		= new Object();
    var pattern 		= new RegExp("(\\w+)=([^}]*)", "mg");
    var matchArray;
    while ((matchArray = pattern.exec(message)) != null) {
        parameters[matchArray[1]] = matchArray[2];
    }

    return parameters;
}