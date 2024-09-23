﻿﻿<%@page import="smartsuite.app.common.stateful.service.StatefulService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
	ServletContext ctx = pageContext.getServletContext();
	WebApplicationContext wac =
	      WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);
	StatefulService statefulService = wac.getBean(StatefulService.class);
	
    String tenant = (String)request.getParameter("tenant");
    String locale = (String)request.getParameter("locale");
    if(tenant != null && !tenant.isEmpty()) {
        tenant = tenant.replaceAll("<", "&lt;")
                       .replaceAll("script", "")
                       .replaceAll("iframe", "")
                       .replaceAll("embed", "")
                       .replaceAll("alert", "");

        tenant = "'".concat(tenant).concat("'");
    }
    if(locale != null && !locale.isEmpty()) {
        locale = locale.replaceAll("<", "&lt;")
                       .replaceAll("script", "")
                       .replaceAll("iframe", "")
                       .replaceAll("embed", "")
                       .replaceAll("alert", "");

        locale = "'".concat(locale).concat("'");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Cache-control" content="no-cache" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0"/>
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
    
    <meta name="smartsuiteVariables" cache_bust="<%=statefulService.findCacheBustValue()%>"
	    							 session_timeout="3600" 
	    							 is_sso="false" 
	    							 locale="<%=request.getParameter("locale")%>"
	    							 use_error_user_message="false"
	    							 is_parameter_encrypt="false" 
	    							 session_usr_id="GUEST" />
    
    <title>SMARTsuite 10.0</title>
    <sec:csrfMetaTags />
    
    <style type="text/css">
        html,body {
            height: 100%;
        }
        
        body {
            margin : 0;
            padding: 0;
            border: 0 none;
            overflow: hidden;
            height: 100%;
            position: static;
        }
        
        /** JSP Body Loading Bar Image */
		body.body-ready
		{
			background-image: url(ui/assets/img/body/body_loading.gif);
		    background-repeat: no-repeat;
		    background-position: center;
		}
    </style>
    <script>
    	
	    window.Polymer = {
	    	ccModuleBehaviorReady: true
	    }
    	window.anonymous = true;
	    
	    var ccModuleBehaviorHandler = function(e){
			document.removeEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);
			Polymer.Base.importLink('ui/sp/vendorMaster/vendorReg/em-sp-reg-vendor.html', function(moduleId) {
				var module = document.createElement(moduleId);
				module.tenant = <%=tenant%>;
				module.locale = <%=locale%>;
	            Polymer.dom(document.body).appendChild(module);
	            Polymer.dom.flush();
			});
		}
	    
		document.addEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);
		
		// grid 2.1.7 이전 버전일 경우 grid 초기화 시 sc-session-manager참조가 필수 이기 때문에 임의 구현하였습니다.
		// 해당 이후 버전일 경우 하위 로직 삭제하여도 됩니다.
		SCSessionManager = new(function() {
			function SessionManagerImpl() {
		  		this.userDetails = {
					userInfo : {usr_id : "GUEST"}
		  		};
		  		this.currentUser = this.userDetails.userInfo;
		  	}
		  	
		  	SessionManagerImpl.prototype.getCurrentUser = function() {
			  	return this.userDetails.userInfo;
		  	};
		  	return SessionManagerImpl;
		}());
		
	    document.addEventListener("new-vendor-close", function() {
	    	if(window.fire){
	    		window.fire("close");
	    	}else{
	    		window.close();
	    	}
	    });
    </script>
    
</head>
<body class="body-ready">
	<div class="top_progress"></div>
	<script src="license.do"></script>
	
	<script src="ui/smartsuite/standard-loader.js"></script>
	<script src="ui/smartsuite/standard-resources.js"></script>
  	<script src="ui/smartsuite/standard-setup.js"></script>
  	<script src="ui/smartsuite/standard-util.js"></script>
	
</body>
</html>