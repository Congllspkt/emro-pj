<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%
	Map tmpInfo = (Map)request.getAttribute("tmpInfo");

	String tmpCont  = "";
	String tmpTitle = "";
	
	if(tmpInfo != null){
		tmpCont  = (String)tmpInfo.get("basc_ctmpl_cont");
		tmpTitle = (String)tmpInfo.get("basc_ctmpl_nm");
	}else{
		tmpCont = "템플릿이 존재하지 않습니다.";
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="google" content="notranslate" />
	<title>SMARTsuite 10.0</title>
	<link rel="stylesheet" type="text/css" href="css/login.css" />
    <style type="text/css">
        textarea,input{font-size:13px;}
		html{overflow:auto}
		textarea{
			width:95%;
		}
    </style>
	<link rel="stylesheet" type="text/css" href="/css/popup/text_popup.css" />
</head>
<body>
    <div id="wrap">
    	<div id="printArea" style="display:inline;">
	    	<div class="title"><%=tmpTitle %></div>
	    	<div class="content"><%=tmpCont %></div>
    	</div>
    	<!-- <div class="base_btn nonangulate noPrint"><a href="javascript:close()">닫기</a></div> -->
    	<div class="base_btn nonangulate noPrint"><a href="javascript:pagePrint()">인쇄</a></div>
    </div>
    
    <script>
    function close() {
    	this.fire("close");
    }
    function pagePrint() {
    	window.print();
    }
    </script>
</body>
</html>