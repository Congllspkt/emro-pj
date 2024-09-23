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
          <link href="${pageContext.request.contextPath}/css/base.css" type="text/css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/popup.css" type="text/css" rel="stylesheet">

        <script type="text/javascript">
        function onClickPaging (event) {
            var form = document.notiInfoForm;
            var pageNum = event.dataset.page;
            form.page.value= pageNum;
            form.bbd_uuid.value = "${boardId}";
            form.submit();
        }
        function noticeLink(event){
            var form = document.notiInfoForm;
            var boardId = event.dataset.id;
            var postNo = event.dataset.no;
            form.bbd_uuid.value = boardId;
            form.pst_no.value = postNo;
            form.page.value = "${_page.page}"
            form.submit();
        }
        </script>
    </head>
    <body>
        <div class="modal-body">
            <div class="notice_list">
                <h2>목록</h2>
                <div class="notice_box">
                    <table class="notice">
                        <caption>공지사항 테이블</caption>
                        <colgroup>
                            <col width="80px">
                            <col width="440px">
                            <col width="100px">
                            <col width="120px">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>제목</th>
                                <th>글쓴이</th>
                                <th>작성일</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" varStatus="status" items="${boardList}" begin="0" end="9">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td><a data-id="${row.bbd_uuid}" data-no="${row.pst_no}" onclick="javascript:noticeLink(this)">${row.pst_tit}</a></td>
                                    <td>${row.crtr_nm}</td>
                                    <td>${row.post_reg_dttm}</td>
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
                        <a alt="${pageNum}" class="on" data-page="${pageNum}" onclick="onClickPaging(this)">${pageNum}</a>
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
        <form name="notiInfoForm" action="noticeLink.do" method="post" >
            <input name="bbd_uuid" type="hidden"/>
            <input name="pst_no" type="hidden"/>
            <input name="page" type="hidden"/>
            <sec:csrfInput/>
        </form>
    </body>
</html>