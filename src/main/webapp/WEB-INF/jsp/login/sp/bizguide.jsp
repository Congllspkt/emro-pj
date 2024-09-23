<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="google" content="notranslate" />
<title>SMARTsuite 10.0.0 Login</title>
<link rel="stylesheet" type="text/css" href="css/login.css" />
    <style type="text/css">
        textarea,input{font-size:13px;}
		html{overflow:auto}
		textarea{
			width:95%;
		}
    </style>
<link rel="stylesheet" type="text/css" href="css/popup/text_popup.css" />
    
</head>
<body>
    <div id="wrap">
    <div class="title">약관동의</div>
    <textarea rows="25" cols="120">${content}</textarea>
            <div >
            	<div class="base_btn gray nonangulate"><a href="javascript:moveLogin()">미동의</a></div>
            	<div class="base_btn nonangulate"><a href="javascript:moveNewVendor()" tab-index="0">동의</a></div>
            </div>
        
        <!-- <div class="footer">
	        <img src="/img/copyright.png" alt="copyright ⓒ emro. All rights reserved."/>
	    </div> -->
    </div>
    
    
    <script type="text/javascript">
	    function moveNewVendor() {
	        location.href="newVendor.do";
	    }
	    
	    function moveLogin() {
	    	this.fire("close");
	    }
    </script>
</body>
</html>