<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="java.util.Enumeration"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta http-equiv="Cache-control" content="no-cache" />
	    <meta http-equiv="Pragma" content="no-cache" />
	    <meta http-equiv="Expires" content="0"/>
	    <meta name="robots" content="noindex, nofollow">
	    <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
	    <meta name="${_passwordSaltSource.name}" salt="${_passwordSaltSource.salt}" iteration="${_passwordSaltSource.iterationCount}"/>
	    <meta name="${_aesCipherKey.name}" passphrase="${_aesCipherKey.passPhrase}" key="${_aesCipherKey.key}" iteration="${_aesCipherKey.iterationCount}" iv="${_aesCipherKey.iv}">

	    <meta name="smartsuiteVariables" cache_bust="${_cacheBust}"
	    								 session_timeout="${pageContext.session.maxInactiveInterval}"
	    								 is_sso="${isSso}" locale="${pageContext.response.locale}"
	    								 use_error_user_message="${useErrorUserMessage}"
	    								 is_parameter_encrypt="${_secParam}"
	    								 session_usr_id="${session_usr_id}" />

	    <title>SMARTsuite 10.0</title>
	    <sec:csrfMetaTags />
		<%
		Map<String, String> moduleMap = new HashMap() {{
			put("em-sp-reg-vendor", "ui/sp/vendorMaster/vendorReg/em-sp-reg-vendor.html");
		}};

		Map<String, String> queryParamMap = new HashMap();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()){
			String paramName = paramNames.nextElement();
			queryParamMap.put(paramName, request.getParameter(paramName));
		}

		String moduleLink = moduleMap.get(queryParamMap.get("module"));
		String parameter = new ObjectMapper().writeValueAsString(queryParamMap);
		System.out.println("parameter : "+parameter);
		System.out.println("parameter : "+parameter);
%>

		<style type="text/css">
		    /** JSP Body Loading Bar Image */
			body.body-ready
			{
				background-image: url(ui/assets/img/body/body_loading.gif);
			    background-repeat: no-repeat;
			    background-position: center;
			}
		</style>

	</head>
	<body class="body-ready">

		<div class="top_progress"></div>
		<!-- logout form -->
		<form id="logoutForm" action="<c:url value='/logoutProcess.do' />" method="POST" hidden>
			<sec:csrfInput/>
			<input hidden="true" id="locale" name="locale" value="${pageContext.response.locale}"/>
		</form>

		<script src="license.do"></script>

		<!-- mdi 태그 선언 -->
		<link rel="import" href="ui/smartsuite/sdi/sc-sdi.html">
		<sc-sdi id="sdiMain" menu-url='<%=moduleLink%>' parameter='<%=parameter%>'></sc-sdi>

		<script src="ui/smartsuite/standard-loader.js"></script>
		<script src="ui/smartsuite/standard-resources.js"></script>
	  	<script src="ui/smartsuite/standard-setup.js"></script>
	  	<script src="ui/smartsuite/standard-util.js"></script>

	</body>
</html>