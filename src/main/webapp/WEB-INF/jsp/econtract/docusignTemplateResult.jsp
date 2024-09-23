<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String uiId = (String)request.getAttribute("uiId");
String event = (String)request.getAttribute("event");
%>
<script language="javascript" charset='UTF-8'>

var agent = navigator.userAgent.toLowerCase();
var browser = "";
if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
 	browser = "IE";
}
if(browser === "IE"){
	if('<%=event%>'=== 'Save'){
		
	}else if('<%=event%>'=== 'Cancel'){
		alert("삭제하였습니다.\n IE 브라우저는 재조회를 해야 변경된 진행상태를 확인 할 수 있습니다.");
	}
	this.close();
}else{
	var parentNode = this.opener.document.querySelector('<%=uiId%>');
	parentNode.docusignTemplateCallBack('<%=event%>');
	this.close();	
}
</script>
