<%@ page contentType="text/html;charset=utf-8"%>
<%@ page language="java" import="java.util.*"%>
<%
    Object resultInfo = request.getAttribute("resultInfo");
    
    String tmpCon = "";
    
    if(resultInfo != null) {
        tmpCon = (String)((Map)resultInfo).get("tmpCon");
    }
    else {
        tmpCon = "데이터가 존재하지 않습니다.";
    }
    
    String browser = "";
    String userAgent = request.getHeader("User-Agent");
    
    if (userAgent.indexOf("Trident") > 0 || userAgent.indexOf("MSIE") > 0) {
        browser = "IE";
    }
    else {
        browser = "ETC";
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <script language="javascript"></script>
    </head>
    <body onload="onPrint()">
        <%=tmpCon%>
    </body>
    
    <script>
        function onPrint() {
                
            var browser = "<%=browser%>";
            
            if(browser == "IE") {
                
                var w = window.open('about:blank','printWin','width=660,height=440,scrollbars=yes');
                var wdoc = w.document;
                
                wdoc.open();
                wdoc.writeln(document.body.innerHTML);
                wdoc.close();
                
                w.print();
                
                w.close();
            }
            else {
                window.print();
            }
            
        }
    </script>
</html>