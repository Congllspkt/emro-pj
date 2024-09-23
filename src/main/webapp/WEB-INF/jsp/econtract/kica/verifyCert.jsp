<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page language="java" import="java.util.*"%>
<%@page language="java" import="java.net.URLEncoder"%>
<%@page language="java" import="signgate.crypto.util.CertUtil"%>
<%@page language="java" import="org.json.JSONObject"%>
<%
//#####################################################################//
	String CRL_PATH = "d:\\temp";  // 경로 셋팅 필요(WAS Start 계정 역할 필요)
//#####################################################################//
	String msg = new String();
	String result = "false";
	JSONObject json = new JSONObject();
	
	String param = request.getParameter("param").toString();
	
	try{
		CertUtil cu = new CertUtil(param.getBytes());
		boolean isvalid = cu.isValid(true, CRL_PATH);
		if(!isvalid){
			msg = cu.getErrorMsg();				
			result = "false";
			System.out.println("인증서 검증 실패 : " + msg);
		}else{
			result = "success";
		}
	
		result = URLEncoder.encode(result, "UTF-8");
		msg = URLEncoder.encode(msg, "UTF-8");
	
		json.put("result", result);
		json.put("resultmsg", msg);
	
	}catch(Exception e){
		System.out.println("result : " + result + ", resultmsg : " + msg);
	}finally{
	
	}
%>
<%= json.toString() %>