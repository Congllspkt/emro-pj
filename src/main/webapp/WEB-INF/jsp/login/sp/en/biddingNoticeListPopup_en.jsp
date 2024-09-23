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
        <meta http-equiv="Expires" content="0" />
        <meta name="robots" content="noindex, nofollow">
        <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
        <sec:csrfMetaTags />
        <link href="../../../css/base.css" type="text/css" rel="stylesheet">
        <link href="../../../css/login.css" type="text/css" rel="stylesheet">
        <link href="../../../css/popup.css" type="text/css" rel="stylesheet">

        <script type="text/javascript">
        function onClickPaging (event) {
            var form = document.biddingNotiInfoForm;
            var pageNum = event.dataset.page;
            form.page.value= pageNum;
            form.submit();
        }
        function biddingNoticeLink(event){
            var form = document.biddingNotiInfoForm;
            var rfxId = event.dataset.id;
            form.rfx_uuid.value = rfxId;
            form.page.value = "${_page.page}"
            form.submit();
        }
        </script>
    </head>
    <body>
        <div class="modal-body">
        	<div class="notice_list">
                <h2>List</h2>
                <div class="notice_box">
                    <table class="notice">
                        <caption>Table</caption>
                        <colgroup>
                            <col width="45px">
                            <col width="90px">
                            <col width="45px">
                            <col width="230px">
                            <col width="70px">
                            <col width="70px">
                            <col width="70px">
                            <col width="250px">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>No</th>
                                <th>RFxNo</th>
                                <th>Rev</th>
                                <th>RFxName</th>
                                <th>ProgSts</th>
                                <th>PRPurp</th>
                                <th>RFxType</th>
                                <th>RFxDate</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" varStatus="status" items="${biddingList}" begin="0" end="9">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td style="text-align: center;">${row.rfx_no}</td>
                                    <td>${row.rfx_rnd}</td>
                                    <td style="text-align: left"><a data-id="${row.rfx_uuid}" onclick="biddingNoticeLink(this)">${row.rfx_tit}</a></td>
                                    <td>${row.rfx_prog_sts_nm}</td>
                                    <td>${row.purc_typ_nm}</a></td>
                                    <td>${row.rfx_typ_nm}</td>
                                    <td>${row.rfx_period}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="paging">
            <c:if test="${(_page.page > 0) && (_page.page ne 1)}">
                <a alt="맨 앞으로" class="btnFro" data-page="1" onclick="onClickPaging(this)"></a>
            </c:if>
            <c:if test="${(_page.page > 0) && (_page.page ne 1)}">
                <a alt="앞으로" class="btnPre" data-page="${_page.page - 1}" onclick="onClickPaging(this)"></a>
            </c:if>
            <c:forEach var="pageNum" begin="${_page.startPage}" end="${_page.endPage}">
                <c:choose>
                    <c:when test="${(pageNum > 0) && (pageNum eq _page.page)}">
                        <a alt="${pageNum}" data-page="${pageNum}" class="on" onclick="onClickPaging(this)">${pageNum}</a>
                    </c:when>
                    <c:when test="${(pageNum > 0)}">
                        <a alt="${pageNum}" data-page="${pageNum}" onclick="onClickPaging(this)">${pageNum}</a>
                    </c:when>
                </c:choose>
            </c:forEach>
            <c:if test="${(_page.totalPage > 1) && (_page.page ne _page.totalPage)}">
                <a alt="뒤로" class="btnNex" data-page="${_page.page + 1}" onclick="onClickPaging(this)"></a>
            </c:if>
            <c:if test="${(_page.totalPage > 1) && (_page.page ne _page.totalPage)}">
                <a alt="맨 뒤로" class="btnEnd" data-page="${_page.totalPage}" onclick="onClickPaging(this)"></a>
            </c:if>
        </div>
        <form name="biddingNotiInfoForm" action="biddingNoticeLink.do" method="post" >
            <input name="rfx_uuid" type="hidden"/>
            <input name="page" type="hidden"/>
            <input name="locale" value="en_US" type="hidden"/>
            <sec:csrfInput/>
        </form>
    </body>
</html>