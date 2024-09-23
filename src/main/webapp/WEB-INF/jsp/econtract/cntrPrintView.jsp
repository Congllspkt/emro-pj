<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    response.setHeader("Pragma","no-cache"); 
    response.setDateHeader("Expires",0); 
    response.setHeader("Cache-Control","no-store"); 
%>  
<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%@ page import="java.net.*,java.io.*" %>
<%
	Map result = (Map)request.getAttribute("result");
	List appList = (List)request.getAttribute("appList");
	String cntr_no = String.valueOf(result.get("cntr_no"));
	String cntr_rev = String.valueOf(result.get("cntr_rev"));
	String cntr_prog_sts = String.valueOf(result.get("cntr_prog_sts"));
	String content = String.valueOf(result.get("content"));
	String vd_nm = String.valueOf(result.get("vd_nm"));
	String print_yn = String.valueOf(result.get("print_yn"));
%>

<HTML>

<HEAD>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<TITLE>계약 번호 : <c:out value="${result.cntr_no}"></c:out> 차수 : <c:out value="${result.cntr_rev}"></c:out></TITLE>

	<link rel="stylesheet" type="text/css" href="/resources/econtract/common/css/print.css" >
	<style type="text/css" media="print">
  	table.breakpoint{
    	page-break-after: always;
    	page-break-inside: avoid;
  	}
	</style>

	<script language="javascript"> 
    	function printWindow(){
    		var printYn = '<c:out value="${result.print_yn}"/>';
    		if(printYn === "Y"){
    			this.print();
    		}else{
    			window.parent.frames['poupJspIFrame'].focus();
				window.parent.frames['poupJspIFrame'].print();
    		}
    	}
	</script>
	</HEAD>

	<body topmargin="40" leftmargin="20" rightmargin="20" bottommargin="40" bgcolor="#FFFFFF" text="#000000" onload="printWindow();">
		<div id="printArea">
			<table width="100%" align=center class="breakpoint">
				<tr>
					<td>
						<c:out value="${result.content}" escapeXml="false"></c:out>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<c:choose>
					<c:when test="${result.cntr_prog_sts eq 'CP'}">
						<tr>
							<td>
							<b> 
							&nbsp;&nbsp;본 계약서는 전자서명법 제3조 및 전자거래기본법 제5조,제6조에 근거하여 (주)엠로와  <c:out value="${result._vd_nm}"></c:out>에서 
						 	&nbsp;&nbsp;전자서명으로 체결된 전자계약서 입니다. 
							&nbsp;&nbsp;본 계약서에 대한 문의는 (주)엠로 담당부서에 문의하시기 바랍니다.
							</b>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
							<b> 
							&nbsp;&nbsp;본 계약서는 (주)엠로 전자계약 시스템에서 작성된 전자문서입니다.
							&nbsp;&nbsp;전자서명이 이루어지지 않은 상태로 적법한 문서로써 유효하지 않습니다.
							&nbsp;&nbsp;본 계약서에 대한 문의는 (주)엠로 담당부서에 문의하시기 바랍니다.
							</b>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
			<c:forEach items="${appList}" var="item" varStatus="status">
			
				<c:choose>
					<c:when test="${status.last}">
						<table width="100%" align="center">
					</c:when>
					<c:otherwise>
						<table width="100%" align="center" class="breakpoint">			
					</c:otherwise>
				</c:choose>
				<tr>
					<td align="left">
						<c:out value="${item.app_content}" escapeXml="false"></c:out>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<c:choose>
					<c:when test="${result.cntr_prog_sts eq 'CP'}">
						<tr>
							<td>
							<b> &nbsp;&nbsp;본 문서는 전자서명법 제3조 및 전자거래기본법 제5조,제6조에 근거하여 (주)엠로와  <c:out value="${result._vd_nm}"></c:out>에서 
						 		&nbsp;&nbsp;전자서명으로 체결된 전자계약서의 첨부문서입니다. 
								&nbsp;&nbsp;본 계약 첨부서류에 대한 문의는 (주)엠로 담당부서에 문의하시기 바랍니다.</b>
							</td>
						</tr>	
					</c:when>
					<c:otherwise>
						<tr>
							<td>
							<b> &nbsp;&nbsp;본 문서는 (주)엠로 전자계약 시스템에서 작성된 전자문서입니다.
								&nbsp;&nbsp;전자서명이 이루어지지 않은 상태로 적법한 문서로써 유효하지 않습니다.
								&nbsp;&nbsp;본 계약 첨부서류에 대한 문의는 (주)엠로 담당부서에 문의하시기 바랍니다.</b>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
				</table>
			</c:forEach>
		</div>
	</body>
</html>