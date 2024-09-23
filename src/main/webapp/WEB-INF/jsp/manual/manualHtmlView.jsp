<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%
	Object manualInfo = request.getAttribute("manualInfo");

	String mnlCont = "";
	
	if(manualInfo != null){
		mnlCont = (String)((Map)manualInfo).get("mnl_cont");
	}else{
		mnlCont = "게시된 매뉴얼이 존재하지 않습니다.";
	}
%>

<HTML>

<HEAD>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<script language="javascript"></script>
</HEAD>

<body>
	<div style="padding-top:20px;">
		<%=mnlCont%>
	</div>
</body>
</html>
