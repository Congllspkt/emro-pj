<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="smartsuite.app.common.cert.util.DocumentProperties"%>
<%@page language="java" import="java.util.*"%>
	<%
	Map<String,Object> signContentInfo = (Map<String,Object>)request.getAttribute("signContentInfo");
	List<Map<String,Object>> taxXmlDataList = (ArrayList<Map<String,Object>>)signContentInfo.get("tax_xml_data_list");
	
	String serverCls = DocumentProperties.get("server.cls");
	
	// 로컬, 개발일경우 bizregno, 1234567890으로 셋팅
	if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
		signContentInfo.put("bizregno", "1111111119");
	}
	
	String signTargetName = "tax_xml_data";
	String signedXmlName = "signed_xml";

	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <title>한국정보인증(주) SecuKit NX Sample - PKCS7</title>

    <!-- KICA SecuKit NXS -->
    <link rel="stylesheet" type="text/css" href="/resources/econtract/SecuKitTAX/WebUI/css/base.css" />
    <script type="text/javascript" src="/resources/econtract/SecuKitTAX/WebUI/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="/resources/econtract/SecuKitTAX/KICA/config/nx_config.js"></script>
    <script type="text/javascript" src="/resources/econtract/SecuKitTAX/KICA/config/LoadSecukitnx.js"></script>
	
	<script type="text/javascript">
		window.onload = function () {
	        // KICA WebUI append
	        $('#KICA_SECUKITNXDIV_ID').append(KICA_SECUKITNXDIV);
	        secunx_Loading();
	        //debugger;
	        //document.test22.submit();
	        //document.signForm.submit();
	    };
	
	    function SecuKitNX_Ready(res) {
	        if (res) {
	            //alert('SecuKitTAX Ready');
	            documentTax();
	        }
	    }
	</script>
	<!-- //KICA SecuKit NXS -->

    <script type="text/javascript">
    	
    	var index = 0;
    	
        // 함수 호출 결과값 리턴
        function SecuKitNXS_RESULT(cmd, res) {
        	
        	var targetXmlDataSize = document.getElementsByName('tax_xml_data').length;
        	
            // Error Check
            var Err = 999;
            try {
                // Error Code가 포함되었는지 확인
                Err = res.ERROR_CODE;
            } catch (e) {
                console.log(e);
            }

            if (Err === undefined) {
                var val = null;
               	switch (cmd) {
                   
					case 'DocForTax':

                   	var cmd ="DocForTaxRes.signXMLDocumentForTax";
                   	var originalText = document.getElementsByName('tax_xml_data')[index];
                   	var Data = {
                   			'originalText':originalText.value,
                   			'certID':certListInfo.getCertID()
                   	};
                   	var param = JSON.stringify(Data);
                   	secukitnxInterface.SecuKitNXS(cmd, param);
                   	break;
                   	
                   case 'DocForTaxRes':
                   	document.getElementsByName('signed_xml')[index].value = res.signXMLDocumentForTax;
                   	
                   	var certString = document.getElementById('base64kmcert').value;
                   	var cmd ="GetKeyRandomRes.getKeyRandom";
                   	var Data ={
                   			'certString':removePEMHeader(removeCRLF(certString)),
                   			'certID':certListInfo.getCertID()
                   	};
                   	var param = JSON.stringify(Data);
                   	secukitnxInterface.SecuKitNXS(cmd, param);
                   	
                   	break;
                   	
                   	
                   	// random키는 서버인증서로 암호화 되서 reutn이 된다. 
                   	// return된 값은 서버에서 복호화를 한후 new String()으로 형 변환 한다.
                   case 'GetKeyRandomRes':
                   	document.getElementById('rvalue').value = res.getKeyRandom;
                   	
                   	 var certType = 'SignCert';                                               // 서명용 : SignCert, 암호화용 : EncryptCert
                        var certID = certListInfo.getCertID();                                   // 선택된 인증서 ID

                        var isViewVID = '1';													 // 0 : VID 추출 안함,  1 : VID 추출
                        var cmd = 'Get_CertInfo_Result.viewCertInfomationWithVID';
                        var Data = {
                            'certType': certType,
                            'certID': certID,
                            'isViewVID': isViewVID
                        };
                        var param = JSON.stringify(Data);
                        secukitnxInterface.SecuKitNXS(cmd, param);
                   	
                   	break;
                    
                   case 'Get_CertInfo_Result' : 
                   	//document.getElementById('certdn').value = res.userDN;
                   	//document.getElementById('validateto').value =  res.validateTo;
                   	//document.getElementById('random').value =  res.vidRandom;
   					document.getElementById('certPerm').value = res.certPEM;
   					document.signForm.rvalue.value = res.vidRandom;

                   	var ssn = document.signForm.ssn.value;     // 신원확인 정보 ( 개인 : 주민등록번호, 사업자 : 사업자번호)
                       var certID = certListInfo.getCertID();              // 선택된 인증서 ID
                        
                       var cmd = 'Check_SSN_Result.verifyVID';
                       var Data = {
   						'ssn': ssn,
                           'certID': certID
                       };
                        var param = JSON.stringify(Data);
                        secukitnxInterface.SecuKitNXS(cmd, param);
                        break;
                        
                   case 'Check_SSN_Result' :
                   	if(res.verifyVID == false){
                   		alert("신원확인 실패");
                   	}else{
                   		index++;
                   		if(index < targetXmlDataSize){
                   			SecuKitNXS_RESULT("DocForTax",{});
                   		}else{
                   			document.signForm.submit();	
                   		}
   						
                   	}
                       break;
                   	
                       default: break;
                   }
            } else {
                // Error Message 출력
                hideNXBlockLayer(); KICA_Error.init();
                KICA_Error.setError(res.ERROR_CODE, res.ERROR_MESSAGE);
                var errorMsg = KICA_Error.getError();
                alert(errorMsg);
            }
        }

        function documentTax(){
        	//로직 구분
            processLogic.init();
            processLogic.setProcessLogic('DocForTax');

            // 인증서 선택창 호출
            NX_ShowDialog();
        }
        
        /*
        function keyRandom(){
        	//로직 구분
            processLogic.init();
            processLogic.setProcessLogic('GetKeyRandom');

            // 인증서 선택창 호출
            NX_ShowDialog();
        }
        function certinfo(){
        	//로직 구분
            processLogic.init();
            processLogic.setProcessLogic('CertInfo');

            // 인증서 선택창 호출
            NX_ShowDialog();
        }
*/
		function callback(result_status){
			this.fire("close", result_status);
		}
		
    </script>
</head>
<body>
	<!-- 한국정보인증 WebUI DIV 영역 -->
    <div id="KICA_SECUKITNXDIV_ID"></div>
    <form name="test22" method="post" action="bp/edoc/contract/cert/cert/testTaxMultiSignComplete.do" target="work">
    	<input type="hidden" name="test1" value="1111">
    	<sec:csrfInput/>
    </form>
    
    <form name="signForm" method="post" action="${signContentInfo.callbackUrl}" target="work">
		<input type="hidden" name="ssn" value="${signContentInfo.bizregno }">
			<%
			Set<Map.Entry<String,Object>> entrySet = signContentInfo.entrySet();
			Iterator<Map.Entry<String,Object>> iter = entrySet.iterator();
			
			while(iter.hasNext()){
				Map.Entry<String,Object> entry = iter.next();
				String key = (String)entry.getKey();
				String value = String.valueOf(entry.getValue());
				if( "kmCert".equals(key) || "tax_xml_data_list".equals(key)){
					continue;
				}
				%>
					<input type="hidden" name="<%=key%>" value="<%=value%>">
				<%
			}
			%>
			
			<%
			for(Map<String,Object> row : taxXmlDataList){
				entrySet = row.entrySet();
				iter = entrySet.iterator();
				while(iter.hasNext()){
					Map.Entry<String,Object> entry = iter.next();
					String key = (String)entry.getKey();
					String value = String.valueOf(entry.getValue());
					if(signTargetName.equals(key)){
						%>
						<textarea name="<%=signTargetName%>" style="display:none;"><%=value%></textarea>
						<textarea name="<%=signedXmlName%>" style="display:none;"></textarea>
						<%
					}else{
						%>
						<input type="hidden" name="<%=key%>" value="<%=value%>">
						<%
					}
				}
			}
			%>
        <sec:csrfInput/>
        <textarea id="rvalue" name="rvalue" style="display:none;"></textarea>
        <textarea id="certPerm" name="rvalue" style="display:none;"></textarea>
		<input id="subject" type="hidden" name="subject" value=""></input>
	</form>
	<textarea id="base64kmcert" name="base64kmcert" style="display:none;">${signContentInfo.kmCert}</textarea>
    <iframe name="work" src="about:blank" width="0" height="0"></iframe>
	</body>
</html>