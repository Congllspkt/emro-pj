<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="smartsuite.app.common.cert.util.DocumentProperties"%>
<%@page language="java" import="java.util.*"%>
<%
	Map<String,String> paramMap = (Map<String,String>)request.getAttribute("signContentInfo");
	String serverCls = DocumentProperties.get("server.cls");
	
	// 로컬, 개발일경우 bizregno, 1234567890으로 셋팅
	if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
	    paramMap.put("ssn", "1234567890");
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- KICA SecuKit NXS -->
    <link rel="stylesheet" type="text/css" href="/resources/econtract/kica_home_daemon/WebUI/css/base.css" />
    <script type="text/javascript" src="/resources/econtract/kica_home_daemon/WebUI/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="/resources/econtract/kica_home_daemon/KICA/config/nx_config.js"></script>
    <script type="text/javascript" src="/resources/econtract/kica_home_daemon/KICA/config/LoadSecukitnx.js"></script>
	
	<script type="text/javascript">
		window.onload = function () {
	        // KICA WebUI append
	        $('#KICA_SECUKITNXDIV_ID').append(KICA_SECUKITNXDIV);
	        secunx_Loading();
	    };
	
	    function SecuKitNX_Ready(res) {
	        if (res) {
	        	  //로직 구분
	            processLogic.init();
	            processLogic.setProcessLogic('KICA_USE_P7Sign');

	            // 인증서 선택창 호출
	            NX_ShowDialog();
	        }
	    }
	</script>
	<script type="text/javascript">
		// 데몬형태 callback
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
						};
						var param = JSON.stringify(Data);
						secukitnxInterface.SecuKitNXS(cmd, param);*/
						// 인증서 정보추출
						var certID = certListInfo.getCertID();
						var certType ='signCert';
						var isViewVID = '1';
						var cmd = 'Get_CertInfo_Result.viewCertInfomationWithVID';
						var Data = {
						'certType': certType,
						'certID': certID,
						'isViewVID' : isViewVID
						};
						var param = JSON.stringify(Data);
						secukitnxInterface.SecuKitNXS(cmd, param);
	                break;
	                
	             	// 인증서 정보 추출
					case 'Get_CertInfo_Result':
						// Version : 인증서 버전 정보 ex) Version 3
						var version = res.version;
						// Serial : 시리얼 정보
						var serial = res.serial;
						// IssueDN : 발행기관 DN ex) cn=signGATE CA4,ou=AccreditedCA,o=KICA,c=KR
						var issueDN = res.issueDN;
						// Issuer : 발행기관 ex) KICA
						var issuer = res.issuer;
						// UserDN : DN ex) cn=테스트(법인-A),ou=RA센터,...
						var userDN = res.userDN;
						// ValidateFrom : 인증서 유효기간 ex) 2014-06-23 11:32:00
						var validateFrom = res.validateFrom;
						// ValidateTo : 인증서 유효기간 ex) 2015-07-19
						var validateTo = res.validateTo;
						// UserName : ex) 테스트(법인-A)
						var username = res.username;
						// Policy : ex) 1.2.410.200004.5.2.1.1,
						var policy = res.policy;
						// VidRandom : 신원확인 정보
						var vidRandom = res.vidRandom;
						
						// 서명용 인증서 정보 : SignCert PEM
						var certPEM = res.certPEM;
						// 암호화용 인증서 정보 : EncryptCert PEM
						var encryptCertPEM = res.encryptCertPEM;
						// 신원검증 확인(VID)
						var cmd = 'Check_SSN_Result.verifyVID';
						var Data = {
							'ssn' : document.signForm.ssn.value,
							'certID' : certListInfo.getCertID()
						};
						var rValueBox = document.getElementById('rvalue');
						rValueBox.value=vidRandom;
						var param = JSON.stringify(Data);
						secukitnxInterface.SecuKitNXS(cmd, param);
	                break;
	                
					// 신원검증 콜백
					case 'Check_SSN_Result':
						var val = res. verifyVID;
						if(val){
							document.signForm.submit();
						}else{
							alert("신원 검증에 실패하였습니다.\nVID(사업자번호, 주민등록번호)를 확인해주세요.");
							callback();
						}
					break;
				}
			}
		}
		
		// callback 함수 (S : 성공, E : 실패)
		function callback(result_status){
			this.fire("close", result_status);
		}
	</script>
</head>
<body>
	<!-- 한국정보인증 WebUI DIV 영역 -->
    <div id="KICA_SECUKITNXDIV_ID"></div>
    <form name="signForm" method="post" action="${signContentInfo.callbackUrl}" target="work">
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