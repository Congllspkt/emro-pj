<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/popup.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
	function onShowList(){
        var form = document.notiInfoForm;
        form.submit();
	}

	function downloadAttFile(event){
        var form = document.downloadAttFileForm;
        var attId = event.dataset.arg;
        if(attId == null || attId ==''){
            return;
        }
        form.id.value = attId;
        form.submit();
    }
    function downloadZipFile(event){
        var form = document.downloadZipFileForm;
        var attList = document.querySelectorAll('#att_list > li > a');

        if(attList == null || attList.length < 1){
            return;
        }else if(attList.length == 1){
            downloadAttFile(attList[0]);
            return;
        }

        Array.prototype.forEach.call(attList,function(item){
            var input = document.createElement("input");
            input.name = "id";
            input.value = item.dataset.arg;
            input.type = "hidden";
            form.appendChild(input);
        });

        form.submit();
    }
</script>
</head>
<body>
	${content}
	<form name="notiInfoForm" action="noticeLink.do" method="post" >
        <input name="bbd_uuid" value="${boardId}" type="hidden"/>
        <input name="page" value="${page}" type="hidden"/>
        <sec:csrfInput/>
    </form>
    <form name="downloadAttFileForm" action="upload/download.do" method="post" >
        <input name="id" type="hidden"/>
        <sec:csrfInput/>
    </form>
	<form name="downloadZipFileForm" action="upload/downloadZip.do" method="post" >
        <sec:csrfInput/>
    </form>
</body>
</html>