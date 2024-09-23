<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="smartsuite.app.common.cert.util.DocumentProperties"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.*,java.net.*,java.io.*" %>
	<%
    
		Map<String,Object> signContentInfo = (Map<String,Object>)request.getAttribute("signContentInfo");
		List<Map<String,Object>> taxXmlDataList = (ArrayList<Map<String,Object>>)signContentInfo.get("tax_xml_data_list");
    
		String serverCls = DocumentProperties.get("server.cls");
       
		// 로컬, 개발일경우 bizregno, 1234567890으로 셋팅
		if("LOCAL".equals(serverCls) || "DEV".equals(serverCls)){
			signContentInfo.put("bizregno", "1234567890");
		}
		
		String signTargetName = "tax_xml_data";
		String signedXmlName = "signed_xml";
		
    %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
		<meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">
		<script src="/bower_components/jquery/dist/jquery.min.js"></script>
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
			function getXMLRootNode(data){
				var xmlDoc = getXMLDoc(data);
				if(xmlDoc == null) return null;
				
				var rootNode = xmlDoc.documentElement;
				
				if(rootNode == null || rootNode.getElementsByTagName('parsererror')[0] || (rootNode.firstChild.data != null && rootNode.firstChild.data.trim() != '') ) {
					return null;
				}
				
				return rootNode;
			}
			
			function signXml(){
				
				if(fnXMLCheck()){	//XML확인
					sign();
				}else{
					alert("XML이 아닙니다.");
				}
			}
			
			function getXMLDoc(data){
				var xmlDoc=null;
				try{
					if (window.DOMParser){
						var parser=new DOMParser();
					    xmlDoc=parser.parseFromString(data,"text/xml");
					    xmlDoc.async=false;
					}else if (window.ActiveXObject) {
						xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
						xmlDoc.async=false;
						xmlDoc.loadXML(data);
					} else {
						alert("");
						return null;
					}
				}catch(e){
					return null;
				}
				return xmlDoc;
			}
	
			function fnXMLCheck(){
				var xmlDataList = document.getElementsByName('<%=signTargetName%>');
				var data = "";
				
				for(var i=0; i < xmlDataList.length; i++){
					//console.log("value:" + xmlDataList[i].value);
					var rootNode = getXMLRootNode(xmlDataList[i].value);
					if(!rootNode){
						return false;
					}else{
						return true;
					}
				}
			}
			
			
			function fnTaxAdd(){
				var area = document.getElementById('contentArea');
				var idx = plainElements.length;
				var pid = 'txt-box-plain-' + idx;
				var rid = 'txt-box-result-' + idx;
				
				var html = firstData.replace('txt-box-plain', pid);
				html = html.replace('txt-box-result', rid);
				area.innerHTML += html;
				
				document.getElementById(pid).value = document.getElementById('txt-box-plain').value;		
				
				plainElements.push( pid );
				resultElements.push( rid );
				
				
			}
			
			function sign(){
				var rValueBox = document.getElementById('rvalue');
				var b64KMCert = document.getElementById('base64kmcert');
				
				unisign.MakeTaxXMLDSIG(
						//아래의 함수는 plainElements에 추가된 원문 데이터를 배열로 반환하는 함수
						//함수를 쓰지 않고 [text.value, text2.value] or ['원문 데이터1', '원문 데이터2', '원문 데이터3' ..] 이런식으로 사용하시면 됩니다.
						//문자열의 배열을 추가하면 됩니다.
	
						getPlainText(), null, b64KMCert.value,
						function(retObj){
							
							if ( null == retObj.signedData || '' == retObj.signedData ) {
								unisign.GetLastError(
										function(errCode, errMsg){
											alert('Error code : ' + errCode + '\n\nError Msg : ' + errMsg);
										}
								);
								callback("E_PW");
							}else{
								var subjectBox = document.getElementById('subject');
								subjectBox.value = retObj.certAttrs.subjectName;
								rValueBox.value = retObj.b64RValue;
								
								var signedXmlKeyName = "";
								var signedXmlList = document.getElementsByName('<%=signedXmlName%>');
								
								// signedText는 첫 파라메터와 동일한 순서대로 리턴됩니다.
								// 즉, 넘긴 배열에 해당하는 index에 해당 서명값이 들어있기 때문에 아래와 같이 처리 할수 있습니다.
								// resultElements는 result값이 들어갈 textarea id 배열입니다.
								for(var t in retObj.signedData){
									signedXmlList[t].value = retObj.signedData[t];
									if(null === retObj.signedData[t] || '' === retObj.signedData[t]){
										callback("E");
										return;
									}
								}
								
								send();
							}
						}
				);
				
				 //툴킷 검은색 배경화면을 흰색으로 변경
	            var div = $("body > div").get(0);
	            div.setAttribute('style','z-index: 10000; background-image: none; margin: 0px; cursor: auto; display: block; position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(255, 255, 255); opacity: 0.5;');
	            var usDivCertSelect = document.getElementById('us-div-cert-select');
	            usDivCertSelect.setAttribute('style','z-index: 10001; position: fixed; top: -10px; left: 0px;');
			}
			
			function getPlainText(){
				var ar = new Array();
				var xmlDataList = document.getElementsByName('<%=signTargetName%>');
				
				for(var i = 0; i < xmlDataList.length; i++){
					//window.console.log(document.getElementById(targets[i].id).value);
					ar.push( xmlDataList[i].value );
				}
				window.console.log(ar);
				return ar;
			}
			function send(){
				document.signForm.submit();
			}
			// callback 함수 (S : 성공, E : 실패)
			function callback(result_status) {
				this.fire("close", result_status);
			}
		</script>
	</head>
	<body onload="signXml();">
	
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
            <input id="subject" type="hidden" name="subject" value=""></input>
        </form>
        <textarea id="base64kmcert" name="base64kmcert" style="display:none;">${signContentInfo.kmCert}</textarea>
        <iframe name="work" src="about:blank" width="0" height="0"></iframe>
	</body>
</html>