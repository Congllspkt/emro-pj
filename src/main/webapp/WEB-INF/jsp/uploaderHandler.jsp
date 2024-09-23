<%@ page contentType="text/html;charset=utf-8"%><%
    String result = (String)request.getAttribute("result");
	out.clear();
    if(result != null && !result.equals("")) {
		out.print(result);
	} else {
		out.clear();
	}
%>