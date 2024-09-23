<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Map result = (Map)request.getAttribute("result");
String resultStatus = (String)result.get("result_status");
String resultMessage = (String)result.get("result_message");
String hashValue = "";

if("S".equals(resultStatus)){
	hashValue = (String)result.get("hash_value");	
}

%>
<script type="text/javascript">
if('<%=resultStatus%>' === "S"){
	parent.certLoginSuccessCallback('<%=hashValue%>');
}else{
	parent.certLoginFailCallback('에러가 발생하였습니다. 관리자에게 문의 바랍니다.');	
}

// 성공 : S, 실패 : E
</script>