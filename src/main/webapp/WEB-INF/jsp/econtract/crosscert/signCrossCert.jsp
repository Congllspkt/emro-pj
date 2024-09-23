<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="java.util.*" %>

    <%
       Map<String,String> paramMap = (Map<String,String>)request.getAttribute("signContentInfo");
	   String serverCls = (String)request.getAttribute("serverCls");

       // 로컬, 개발일경우 
       if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
	       // 테스트일 경우 입력받은 사업자번호로 셋팅
    	   if("local_test".equals(paramMap.get("testMethod"))){
	    	   paramMap.put("bizregno", paramMap.get("bizregno"));
    	   } else {
	    	   paramMap.put("bizregno", "1234567890");
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
                var sign_source = document.signForm.hash_value.value;
                if(sign_source == null || sign_source == "") {
                    alert("[전자서명 실패]\n서명 원본이 없습니다.");
                    this.close();
                    return;
                } else {
                	unisign.SignDataNonEnveloped( sign_source, null, "", function( resultObject ) 
                			{
                				//document.frm.signedText.value = resultObject.signedData; //결과출력
                				document.signForm.sign_value.value = resultObject.signedData; //결과출력
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
                				GetRValueFromKey(resultObject.certAttrs.subjectName); //R값 추출 확인
                				//alert('전자서명에 성공했습니다.');
                			});
                    
                }
                //툴킷 검은색 배경화면을 흰색으로 변경
                var div = $("body > div").get(0);
                div.setAttribute('style','z-index: 10000; background-image: none; margin: 0px; cursor: auto; display: block; position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(255, 255, 255); opacity: 0.5;');
                var usDivCertSelect = document.getElementById('us-div-cert-select');
                usDivCertSelect.setAttribute('style','z-index: 10001; position: fixed; top: -10px; left: 0px;');
            }
            
            function send()
            {
                if (document.signForm.sign_value.value == "")
                {
                    alert("[전자서명 실패]\n전자서명값이 없습니다.");
                    this.close();
                }
                //document.signForm.method = "post";
                //document.signForm.action = "/sp/edoc/contract/saveVdSignValue.do";
                //document.signForm.target = "work";
                
                document.signForm.submit();
            }
            //R값 생성 함수
            function GetRValueFromKey(userDN) {
            	unisign.GetRValueFromKey(userDN, "", function( resultObject ) {

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
            // 공동인증서 callback 함수
            function certLoginSuccessCallback(signValue){
            	opener.certLoginResult("S", signValue);
            }
         	// 공동인증서 callback 함수
            function certLoginFailCallback(resultMessage){
            	opener.certLoginResult("E", resultMessage);
            }
        </script>
    </head>
    
    <body onload="signDataNVerifyVID();">
    
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
            <sec:csrfInput/>
            <textarea id="sign_value" name="sign_value" style="display:none; word-wrap: normal; overflow:auto;"></textarea>
            <textarea id="rvalue" name="rvalue" rows="5" cols="40" style="display:none; word-wrap: normal; overflow:auto;"></textarea> 
        </form>
        <iframe name="work" src="about:blank" width="0" height="0"></iframe>
    </body>
</html>