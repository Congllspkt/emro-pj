<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="smartsuite.app.common.cert.util.DocumentProperties"%>
<%@page language="java" import="java.util.*"%>
<%
	Map<String,String> paramMap = (Map<String,String>)request.getAttribute("signContentInfo");
	String serverCls = DocumentProperties.get("server.cls");
	
	// 로컬, 개발일경우 bizregno, 1234567890으로 셋팅
	if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
	    paramMap.put("bizregno", "1234567890");
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <title>한국정보인증(주) SecuKit NX Sample - PKCS7</title>

    <!-- KICA SecuKit NXS -->
    <link rel="stylesheet" type="text/css" href="/resources/econtract/SecuKitNXS/WebUI/css/base.css" />
    <script type="text/javascript" src="/resources/econtract/SecuKitNXS/WebUI/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="/resources/econtract/SecuKitNXS/KICA/config/nx_config.js"></script>
    <script type="text/javascript" src="/resources/econtract/SecuKitNXS/KICA/config/LoadSecukitnx.js"></script>
	
	<meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
	<script type="text/javascript" src="/bower_components/crypto-js/crypto-js.js"></script>
	<script type="text/javascript" src="/bower_components/cipher-util/cipher-util.min.js"></script>
    <script type="text/javascript" src="/bower_components/password-encryptor/password-encryptor.min.js"></script>
	
	<script type="text/javascript">
		window.onload = function () {
	        // KICA WebUI append
	        $('#KICA_SECUKITNXDIV_ID').append(KICA_SECUKITNXDIV);
	        secunx_Loading();
	    };
	
	    function SecuKitNX_Ready(res) {
	        if (res) {
	            //alert('SecuKitNX Ready');
	        	processLogic.init();
				processLogic.setProcessLogic('KICA_USE_P7Sign');
				NX_ShowDialog();
	        }
	    }
	</script>
	<script type="text/javascript">
		
		function SecuKitNXS_RESULT(cmd, res){
			
			// Error 체크
			var Err = 999;
			try{
				Err = res.ERROR_CODE;
			}catch(e){
				console.log(e);
			}
			
			if(Err === undefined){
				var val = null;
				switch(cmd){
					// 전자서명				
					case 'KICA_USE_P7Sign' :
						var certType ='signCert';
						var sourceString = document.signForm.hash_value.value;
						var certID = certListInfo.getCertID();
						var cmd = 'KICA_USE_P7Sign_Result.generatePKCS7SignedData';
						var Data = {
							'certType' : certType,
							'sourceString' : sourceString,
							'certID' : certID
						}
						var param = JSON.stringify(Data);
						secukitnxInterface.SecuKitNXS(cmd, param);
	                break;
	                
					// 전자서명 콜백
					case 'KICA_USE_P7Sign_Result':
	                    val = res.generatePKCS7SignedData;      // PKCS#7 Signed Data
	                    document.signForm.sign_value.value = val;
						
						// 신원검증 확인(VID)
						/*var cmd = 'Check_SSN_Result.verifyVID';
						var Data = {
							'ssn' : document.signForm.ssn.value,
							'certID' : certListInfo.getCertID()
						};*/
						var certID = certListInfo.getCertID();  
                        var cmd = 'GetCertInfo.viewCertInfomationWithVID';
						var Data = {
									'certType': 'signCert',
	                                'certID': certID,
									'isViewVID': '1'
								};
						var param = JSON.stringify(Data);
						secukitnxInterface.SecuKitNXS(cmd, param);
						
	                break;
					case 'GetCertInfo':
	                    
	                    if(res.vidRandom){
	                    	console.log("vidRandom : " + res.vidRandom);
	                    	document.signForm.rvalue.value = CipherUtil.encrypt(res.vidRandom);
	                    	console.log("encrypt vidRandom : " + document.signForm.rvalue.value);
							document.signForm.submit();
						}else{
							alert("인증서 정보 추출에 실패하였습니다.");
							callback();
						}

					break;
	                
					/*// 신원검증 콜백  -> 신원확인을 서버단에서 함
					case 'Check_SSN_Result':
						var val = res.verifyVID;
						if(val){
							document.signForm.submit();
						}else{
							alert("신원 검증에 실패하였습니다.\nVID(사업자번호, 주민등록번호)를 확인해주세요.");
							callback();
						}
					break;*/
				}
			}
		}
		
		// callback 함수 (S : 성공, E : 실패)
		function callback(result_status){
			this.fire("close", result_status);
		}
		// 공동인증서 callback 함수
        function certLoginSuccessCallback(hashValue){
        	opener.certLoginResult("S", hashValue);
        }
     	// 공동인증서 callback 함수
        function certLoginFailCallback(resultMessage){
        	opener.certLoginResult("E", resultMessage);
        }
	</script>
</head>
<body>
	<!-- 한국정보인증 WebUI DIV 영역 -->
    <div id="KICA_SECUKITNXDIV_ID"></div>
    <!-- HTML5 제품 웹 DIV 영역 -->
    <div id="KICA_WEBNPKI_DIV"></div>
    <form name="signForm" method="post" action="${signContentInfo.callbackUrl}" target="work">
    	<input type="hidden" name="ssn" value="${signContentInfo.bizregno}">
	 		<%
            // parameter 셋팅
            Set<Map.Entry<String,String>> entrySet = paramMap.entrySet();
            Iterator<Map.Entry<String,String>> iter = entrySet.iterator();
            while(iter.hasNext()){
            	Map.Entry<String,String> entry = iter.next();
            	String key   = entry.getKey();
            	String value = String.valueOf(entry.getValue());
            	
           	%>
           	    <input type="hidden" name="<%=key%>" value="<%=value%>">
           	<%
            }
            %>
	    <textarea id="sign_value" name="sign_value" style="display:none; word-wrap: normal; overflow:auto;"></textarea>
	    <textarea id="rvalue" name="rvalue" rows="5" cols="40" style="display:none; word-wrap: normal; overflow:auto;"></textarea>
	    <sec:csrfInput/>
	</form>
	<iframe name="work" src="about:blank" width="0" height="0"></iframe>
</body>
</html>