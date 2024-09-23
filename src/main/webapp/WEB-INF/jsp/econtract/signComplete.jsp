<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
if(parent.callback != null){
    parent.callback('${result_status}');
}else{
	// signContract.jsp 가기전에 오류가 났을 경우 sc-window를 찾아 팝업을 닫아준다.
	var windowList = parent.document.querySelectorAll("sc-window");
	for(var index =0; index<windowList.length; index++){
		var scWindow = windowList[index];
		var jspIframe = scWindow.querySelector("#poupJspIFrame");
		if(jspIframe && jspIframe.contentWindow == this){
			scWindow.fire("close", "${result_status}");
		}
	}
}
console.log('${result_status}');
// 성공 : S, 실패 : E
</script>