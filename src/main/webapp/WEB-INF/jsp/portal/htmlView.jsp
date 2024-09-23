<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%
	Object tmpInfo = request.getAttribute("tmpInfo");

	String tmpCont = "";
	String tmpTitle = "";
	
	if(tmpInfo != null){
		tmpCont = (String)((Map)tmpInfo).get("basc_ctmpl_cont");
		tmpTitle = (String)((Map)tmpInfo).get("basc_ctmpl_nm");
	}else{
		tmpCont = "템플릿이 존재하지 않습니다.";
	}
%>

<%=tmpCont%>
