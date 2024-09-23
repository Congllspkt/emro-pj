<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="smartsuite.exception.CommonException"%>
<%@ page import="java.io.*" %>
<%
	File pdfFile = null;
	byte[] pdfByteArray = null;
	OutputStream outputStream = null;
	Writer writer = null;
	
	try{
		Map manualInfo = (Map)request.getAttribute("manualInfo");
		pdfByteArray = (byte[])manualInfo.get("pdtByteArray");
		
		if(pdfByteArray !=null){
			out.clear();
			out=pageContext.pushBody();
			
			response.setContentType("application/pdf");
			outputStream = response.getOutputStream();
			outputStream.write(pdfByteArray);
			outputStream.flush();
		}else{
			response.setContentType("text/html");
			writer = response.getWriter();
			writer.write("게시된 매뉴얼이 존재하지 않습니다.");
			writer.flush();
		}
	}catch(Exception e){
		throw new CommonException(this.getClass().getName());
	}finally{
		if(outputStream != null){
			outputStream.close();
		}
		
		if(writer != null){
			writer.close();
		}
	}
	
%>

<HTML>

<HEAD>
	<script language="javascript"></script>
</HEAD>

<body>
</body>
</html>
