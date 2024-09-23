<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String uiId = (String)request.getAttribute("uiId");
String event = (String)request.getAttribute("event");
String procType = (String)request.getAttribute("procType");
String usrCls = (String)request.getAttribute("usrCls");
String cntrId = (String)request.getAttribute("cntrId");
%>
<script language="javascript">
var agent = navigator.userAgent.toLowerCase();
var browser = "";
if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
	browser = "IE";
}
if(browser === "IE"){
	if('<%=event%>'=== "viewing_complete"){
	}else if('<%=event%>' === "decline"){
		alert("거부 하였습니다.\n IE 브라우저는 재조회를 해야 변경된 진행상태를 확인 할 수 있습니다.");
	}else if('<%=event%>' === "signing_complete"){
		if('<%=usrCls%>' === "BUYER"){
			if('<%=procType%>' === "VD_BUYER"){
				alert("최종서명이 완료되었습니다.\n IE 브라우저는 재조회를 해야 변경된 진행상태를 확인 할 수 있습니다.");
			}else if('<%=procType%>' === "BUYER_VD"){
				alert("서명완료하였고, 계약서를 협력사에게 발송하였습니다.\n IE 브라우저는 재조회를 해야 변경된 진행상태를 확인 할 수 있습니다.");
			}	
		}else if('<%=usrCls%>' === "VD"){
			alert("서명 완료하였습니다.\n IE 브라우저는 재조회를 해야 변경된 진행상태를 확인 할 수 있습니다.");
		}
		
	}
	this.close();
}else{
	var parentNode = this.opener.document.querySelector('<%=uiId%>');
	parentNode.docusignCallBack('<%=event%>','<%=procType%>','<%=cntrId%>');
	this.close();	
}
</script>
