<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="smartsuite.app.common.cert.util.DocumentProperties"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.*,java.net.*,java.io.*" %>
	<%
    
		Map<String,String> signContentInfo = (Map<String,String>)request.getAttribute("signContentInfo");
		List<Map<String,String>> signAppTextInfo = (List<Map<String,String>>)request.getAttribute("signAppTextInfo");
    	List<Map<String,String>> signAppFileInfo = (List<Map<String,String>>)request.getAttribute("signAppFileInfo");
    	String bizregno = (String)request.getAttribute("bizregno");
    
		String serverCls = DocumentProperties.get("server.cls");
       
	 	// 로컬, 개발일경우 
	 	if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
		 	// 테스트일 경우 입력받은 사업자번호로 셋팅
	 		if("multi_sign".equals(signContentInfo.get("testMethod"))) {
		 		signContentInfo.put("bizregno", signContentInfo.get("bizregno"));
	 		} else {
		 		signContentInfo.put("bizregno", "1234567890");
	 		}
	 	}
       
    %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
		<meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
		<script src="${pageContext.request.contextPath}/bower_components/jquery/dist/jquery.min.js"></script>
		<title></title>
		<!-- 전자인증 모듈 설정 //-->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/econtract/crosscert_home/unisignweb/rsrc/css/certcommon.css?v=1" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/econtract/crosscert_home/unisignweb/js/unisignwebclient.js?v=1"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/econtract/crosscert_home/UniSignWeb_Multi_Init_Nim.js?v=1&context=${pageContext.request.contextPath}"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/bower_components/crypto-js/crypto-js.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/bower_components/cipher-util/cipher-util.min.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath}/bower_components/password-encryptor/password-encryptor.min.js"></script>

		<!-- 전자인증 모듈 설정 //-->
		<script type="text/javascript">							
			function signDataNVerifyVID() 
	        {
	            //debugger;
	             //window.resizeTo(610, 652);
	            
	            /*** Hash값 검증 및 신원확인 ***/
	            var sign_source = document.signForm.hash_value[0].value;
	            if(sign_source == null || sign_source == "") {
	                alert("[전자서명 실패]\n서명 원본이 없습니다.");
	                this.close();
	                return;
	            } else {
	            	unisign.SignDataNonEnveloped( sign_source, null, "", function( resultObject ) 
	            			{
	            				//document.frm.signedText.value = resultObject.signedData; //결과출력
	            				//document.signForm.sign_value.value = resultObject.signedData; //결과출력
	            				if( !resultObject || resultObject.resultCode !=0 )
	            				{
	            					
	            					var resultStatus = "E";
	            					if(resultObject.resultCode == 999){ //사용자 취소
	                                    resultStatus = "CANCEL";
	            					}else{
	            						alert( resultObject.resultMessage + "\n오류코드 : " + resultObject.resultCode );
	            					}
	            					callback(resultStatus);
	            					return;
	            				}
	            				console.log("DN:"+resultObject.certAttrs.subjectName +'인증서의 신원확인 검증에 성공하였습니다.');
	            				document.signForm.dn.value = resultObject.certAttrs.subjectName;
	            				if(document.signForm.hash_value.length > 1){
	            					document.signForm.sign_value[0].value = resultObject.signedData;
	            					listSign(resultObject.signedData);
	            				}else{
	            					document.signForm.sign_value[0].value = resultObject.signedData;
	            					GetRValueFromKey(); //R값 추출 확인
	            				}
	            			});
	                
	            }
	            //툴킷 검은색 배경화면을 흰색으로 변경
	            var div = $("body > div").get(0);
	            div.setAttribute('style','z-index: 10000; background-image: none; margin: 0px; cursor: auto; display: block; position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(255, 255, 255); opacity: 0.5;');
	            var usDivCertSelect = document.getElementById('us-div-cert-select');
	            usDivCertSelect.setAttribute('style','z-index: 10001; position: fixed; top: -10px; left: 0px;');
	        }
			var cnt = 1;
			function addSign(sign_source, index){
				
				unisign.SignDataNonEnveloped( sign_source, document.signForm.dn.value,'',
						function(resultObject) 
						{ 

							if( !resultObject || resultObject.resultCode !=0 )
							{
								alert( resultObject.resultMessage + "\n오류코드 : " + resultObject.resultCode );
								return;
							}
							else
							{
								console.log(index + "번째 서명완료");
								document.signForm.sign_value[index].value = resultObject.signedData;
								cnt++;
								if(document.signForm.sign_value.length == cnt){ //리스트 서명완료
									GetRValueFromKey(); //R값 추출 확인
								}
							}				 
						} 					 
					);
			}
			function listSign(){
				
				
				for(var i = 1; i < document.signForm.hash_value.length; i++){
					if(document.signForm.hash_value[i].value == null || document.signForm.hash_value[i].value == "") {
		                alert("[전자서명 실패]\n서명 원본이 없습니다.");
		                this.close();
		                return;
		            }
					console.log(i + "번째 서명 시작");
					document.signForm.sign_value[i].value = addSign(document.signForm.hash_value[i].value, i);	
				}
			}
			
			function send()
			{
				if(document.signForm.sign_value.length > 1){
					for(var k=0 ; k < document.signForm.sign_value.length ; k++){
						if(document.signForm.sign_value[k].value == ""){
							alert("[전자서명 실패]\n전자서명값이 없습니다.");
		 					this.close();
		 					callback("E");
							return;
						}
					}
				} else {
					if (document.signForm.sign_value.value == "") {
	 					alert("[전자서명 실패]\n전자서명값이 없습니다.");
	 					this.close();
	 					callback("E");
						return;
	 				}
				}
				document.signForm.submit();
			}
			  //R값 생성 함수
            function GetRValueFromKey() {
            	
            	unisign.GetRValueFromKey(document.signForm.dn.value, "", function( resultObject ) {

    				var rValueBox = document.getElementById('rvalue');

    				if( !resultObject || resultObject.resultCode != 0 ){
    					alert( resultObject.resultMessage + "\n오류코드 : " + resultObject.resultCode );
    					return;
    				}
    				rValueBox.value = CipherUtil.encrypt(resultObject.RValue);  // R 값 생성
    				
    				send();
    			})
    		}
			// callback 함수 (S : 성공, E : 실패)
			function callback(result_status) {
				this.fire("close", result_status);
			}
		</script>
	</head>
	<body onload="javascript:signDataNVerifyVID();">
	
		<form name="signForm" method="post" action="${signContentInfo.callbackUrl}" target="work">
			
			<%
			if(signContentInfo != null){
				%>
				<input type="hidden" name="dn"/>
				<input type="hidden" id="rvalue" name="rvalue"/>
				<input type="hidden" name="testMethod" value="<%=signContentInfo.get("testMethod")%>" />
				<input type="hidden" name="ssn" value="<%=signContentInfo.get("bizregno")%>" />
				<input type="hidden" name="cntr_no" value="<%=signContentInfo.get("cntr_no")%>" />
				<input type="hidden" name="cntr_rev" value="<%=signContentInfo.get("cntr_rev")%>" />
				<input type="hidden" name="bizregno" value="<%=bizregno%>" />
				<input type="hidden" name="hash_value" value="<%=signContentInfo.get("hash_value")%>" />
				<input type="hidden" name="sign_target" value="<%=signContentInfo.get("sign_target")%>" />
				<input type="hidden" name="doc_type" value="<%=signContentInfo.get("doc_type")%>" />
				<textarea name="sign_value" style='display:none; word-wrap: normal; overflow:auto;'></textarea>
				<%
			}
			%>
		
			<div id="list" style="visibility:hidden">
				<%
			if(signAppTextInfo != null){
				for(int i = 0; i < signAppTextInfo.size(); i++){
					Map item = signAppTextInfo.get(i);
				%>
				<input type="hidden" name="hash_value" value="<%=item.get("hash_value")%>" />
				<input type="hidden" name="sign_target" value="<%=item.get("sign_target")%>" />
				<input type="hidden" name="doc_type" value="<%=item.get("doc_type")%>" />
				<textarea name="sign_value" style='display:none; word-wrap: normal; overflow:auto;'></textarea>
				<%
				}
			}
			%>
				<%
			if(signAppFileInfo != null){
				for(int i = 0; i < signAppFileInfo.size(); i++){
					Map item = signAppFileInfo.get(i);
				%>
				<input type="hidden" name="hash_value" value="<%=item.get("hash_value")%>" />
				<input type="hidden" name="sign_target" value="<%=item.get("sign_target")%>" />
				<input type="hidden" name="doc_type" value="<%=item.get("doc_type")%>" />
				<textarea name="sign_value" style='display:none; word-wrap: normal; overflow:auto;'></textarea>
				<%
				}
			}
			%>
				
			</div>
			
			
            <sec:csrfInput/>
        </form>
        <iframe name="work" src="about:blank" width="0" height="0"></iframe>
	</body>
</html>