var tmpAuthCode = "";

var KICA_RELAY = {
	exportP12ToData: function() {
		var certType = 'KmCert';                                                 // 서명용 : SignCert, 암호화용 : EncryptCert
		var certID = certListInfo.getCertID();                                   // 선택된 인증서 ID

		var cmd = 'KICA_RELAY.exportP12ToData';
		var Data = {
			'certType': certType,
			'certID': certID,
		};
		var param = JSON.stringify(Data);
		secukitnxInterface.SecuKitNX(cmd, param);

	},

	exportP12ToDataCallback: function(res) {
		if (!_errorCheck(res)) {
			return;
		}

		var p12 = res.exportP12toData;
		var signValue = res.signature;
		nxPC2Phone.setP12(p12);
		nxPC2Phone.setSignature(signValue);
		sendPC2Phone();
	},

	importP12FromData: function() {
		$('#nx-targetMedia-select').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();

		var authCodeTmp = nxPhone2PC.getAuthCode();
		var phone2pc_pfxPem = nxrelay.getCert(tmpAuthCode);
		//var phone2pc_pfxPem = document.getElementById("pc2phone_p12").value;
		if (phone2pc_pfxPem === "") {
			// 인증코드가 없는 경우
			hideNXBlockLayer();
			return;
		}

		var password = nxPhone2PC.getPhone2PCPwd();
		var mediaType = TargetMediaInfo.getMediaType();
		var extraValue = TargetMediaInfo.getExtraValue();
		var certID = "";

		var cmd = 'KICA_RELAY.importP12FromData';
		var Data = {
			'data': phone2pc_pfxPem,
			'password': password,
			'mediaType': mediaType,
			'extraValue': extraValue,
			'certID': certID
		};
		var param = JSON.stringify(Data);
		secukitnxInterface.SecuKitNX(cmd, param);
	},

	importP12FromDataCallback: function(res) {
		if (!_errorCheck(res)) {
			return;
		}

		// 인증서 이동복사 성공 창 표시
		$('.nx-relay-success-alert-head-msg').remove();
		$('.nx-relay-success-alert-msg').remove();

		var headMessage = '<div class=\"nx-relay-success-alert-head-msg\">';
		headMessage += '<h1>' + "인증서 이동복사" + '</h1>';
		headMessage += '</div>';
		$('#nx-relay-success-alert-head').append(headMessage);


		var alertMessage = '<div class=\"nx-relay-success-alert-msg\" id=\"nx-relay-success-alert-msg\">';
		alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"\" /></div>';
		alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + "스마트폰에서 PC로 이동복사가 완료되었습니다." + '</span></p></div>';
		alertMessage += '</div>';
		$('#nx-relay-succes-alert-box').append(alertMessage);

		setTimeout(function() {
			$('#nx-pki-ui-wrapper').show();
			$('#nx-relay-success-alert').show();
		}, 200);
	},

}

function _errorCheck(res) {
	var Err = 999;
	try {
		// Error Code가 포함되었는지 확인
		Err = res.ERROR_CODE;
	} catch (e) {
		console.log(e);
	}

	if (Err === undefined) {
		return true;
	}

	var logic = processLogic.getProcessLogic();

	if (logic === "KICA.MANAGEMENT.PHONE2PC") {
		// 인증서 이동복사 (PHONE2PC) 실패창 표시
		$('.nx-relay-fail-alert-head-msg').remove();
		var headMessage = '<div class=\"nx-relay-fail-alert-head-msg\">';
		headMessage += '<h1>' + "인증서 이동복사" + '</h1>';
		headMessage += '</div>';
		$('#nx-relay-fail-alert-head').append(headMessage);

		$('.nx-relay-fail-alert-msg').remove();
		var alertMessage = '<div class=\"nx-relay-fail-alert-msg\">';
		alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_fail.png" alt=\"\" /></div>';
		alertMessage += '<div class=\"gray-box2\"><p class=\"txt-l\"><span class=\"inline-tit2\">' + "스마트폰에서 PC로 이동복사가 실패하였습니다.";
		alertMessage += '<br />';
		alertMessage += '<br />';
		alertMessage += '[ ErrorCode ] : ' + res.ERROR_CODE;
		alertMessage += '<br />';
		alertMessage += '[ ErrorMessage ] : ' + res.ERROR_MESSAGE;
		alertMessage += '</span></p></div>';
		alertMessage += '</div>';

		$('#nx-relay-fail-alert-box').append(alertMessage);

		setTimeout(function() {
			$('#nx-pki-ui-wrapper').show();
			$('#nx-relay-fail-alert').show();
		}, 200);
	} else {

		// Error Message 출력
		hideNXBlockLayer(); KICA_Error.init();
		KICA_Error.setError(res.ERROR_CODE, res.ERROR_MESSAGE);
		var errorMsg = KICA_Error.getError();
		alert(errorMsg);
	}

	return false;
}
// PC에서 폰으로 인증서 내보내기시 전달할 값들 저장
var nxPC2Phone = (function() {
	var authcode = "";
	var p12 = "";
	var signature = "";
	var userSignCert = "";

	var init = function() {
		authcode = "";
		p12 = "";
		signature = "";
		userSignCert = "";
	};

	var setAuthCode = function(param) {
		authcode = param;
	};

	var getAuthCode = function() {
		return authcode;
	};

	var setP12 = function(param) {
		p12 = param;
	};

	var getP12 = function() {
		return p12;
	};

	var setSignature = function(param) {
		signature = param;
	};

	var getSignature = function() {
		return signature;
	};

	var setUserSignCert = function(param) {
		userSignCert = removePEMHeader(removeCRLF(param));
	};

	var getUserSignCert = function() {
		return userSignCert;
	};


	return {
		init: init,
		setAuthCode: setAuthCode,
		getAuthCode: getAuthCode,
		setP12: setP12,
		getP12: getP12,
		setSignature: setSignature,
		getSignature: getSignature,
		setUserSignCert: setUserSignCert,
		getUserSignCert: getUserSignCert
	};

})();

// 인증서 이동복사 (PC2Phone) 결과 및 중계서버 전송부분
function sendPC2Phone() {
	var authcode = nxPC2Phone.getAuthCode();
	var p12 = nxPC2Phone.getP12();
	var signature = nxPC2Phone.getSignature();
	var kmsCertificate = nxPC2Phone.getUserSignCert();

	nxrelay.setCert(authcode, p12, signature, kmsCertificate);
}

//  PC에서 스마트폰으로 인증서 내보내기 성공시 WebUI 표시
function sendPC2Phone_OK() {
	// 인증서 이동복사 성공 창 표시
	$('.nx-relay-success-alert-head-msg').remove();
	$('.nx-relay-success-alert-msg').remove();

	var headMessage = '<div class=\"nx-relay-success-alert-head-msg\">';
	headMessage += '<h1>' + "인증서 이동복사" + '</h1>';
	headMessage += '</div>';
	$('#nx-relay-success-alert-head').append(headMessage);


	var alertMessage = '<div class=\"nx-relay-success-alert-msg\" id=\"nx-relay-success-alert-msg\">';
	alertMessage += '<div class=\"bg-img-area\"><img src=\"' + NX_DEFAULT_IMG_URL + 'img_issue_success.png" alt=\"\" /></div>';
	alertMessage += '<div class=\"gray-box2\"><p class=\"txt-c\"><span class=\"inline-tit\">' + "인증서 내보내기가 완료 되었습니다." + '</span></p></div>';
	alertMessage += '</div>';
	$('#nx-relay-succes-alert-box').append(alertMessage);

	setTimeout(function() {
		$('#nx-pki-ui-wrapper').show();
		$('#nx-relay-success-alert').show();
	}, 200);
}

function phone2pcAuthCode(authCode) {
	nxPhone2PC.init();
	nxPhone2PC.setAuthCode(authCode);

	processLogic.init();
	processLogic.setProcessLogic('KICA.MANAGEMENT.PHONE2PC');

	tmpAuthCode = authCode;

	var auth1 = authCode.substring(0, 4);
	var auth2 = authCode.substring(4, 8);
	var auth3 = authCode.substring(8, 12);

	document.getElementById("phone2pc_auth_1").value = auth1;
	document.getElementById("phone2pc_auth_2").value = auth2;
	document.getElementById("phone2pc_auth_3").value = auth3;

	$('#nx-background-block-layer').show();
	$('#nx-pki-ui-wrapper').show();
	$('#nx-cert-phone2pc-authcode').show();
}


var nxPhone2PC = (function() {
	var pwd = "";
	var authCode = "";

	var init = function() {
		pwd = "";
		authCode = "";

		setTimeout(function() {  // 환경설정 Set : 복사할 타겟 미디어 UI Set
			// 저장매체 정보를 저장하는 함수 초기화
			TargetMediaInfo.init();

			// 저장매체 환경 Set에 맞게 저장위치 생성
			NXsetBanner_TargetMedia();
			TargetDialogSet();
			NXStopMediaSelect2();
		}, 100);
	};

	var setPhone2PCPwd = function(param) {
		pwd = param;
	};

	var getPhone2PCPwd = function() {
		return pwd;
	};

	var setAuthCode = function(param) {
		authCode = param;
	};

	var getAuthCode = function() {
		return authCode;
	};

	return {
		init: init,
		setPhone2PCPwd: setPhone2PCPwd,
		getPhone2PCPwd: getPhone2PCPwd,
		setAuthCode: setAuthCode,
		getAuthCode: getAuthCode
	};
})();


function initNXPC2Phone() {
	document.getElementById("pc2phone_auth_1").value = "";
	document.getElementById("pc2phone_auth_2").value = "";
	document.getElementById("pc2phone_auth_3").value = "";

	nxPC2Phone.init();
}

function isNumberKey(evt) {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;
	return true;
}