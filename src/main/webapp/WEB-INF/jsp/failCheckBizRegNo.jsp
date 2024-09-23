<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Map result = (Map)request.getAttribute("result");
String resultStatus = (String)result.get("result_status");
String resultMessage = (String)result.get("result_message");
%>
<script type="text/javascript">

	opener.failCheckBizRegNo('<%=resultStatus%>','<%=resultMessage%>');	

// 성공 : S, 실패 : E
</script>