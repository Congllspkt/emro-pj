document.addEventListener('keydown',function(e){
	var ele = e.srcElement ? e.srcElement : e.target,
		rx = /INPUT|SELECT|TEXTAREA/i,
	    k = e.which || e.keyCode;
	if((!ele.type || !rx.test(e.target.tagName)|| (ele.readOnly || ele.disabled )) && ["true", ""].indexOf(ele.getAttribute("contenteditable")) === -1 ) {
    	if(k == 8){
    		e.preventDefault();
     	}
     }
},true);

var agent = navigator.userAgent.toLowerCase();
var pdfViewerScriptNode = document.querySelector("script[src$='/pdf.min.js']");
if (pdfViewerScriptNode === null || pdfViewerScriptNode === undefined) {
	if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
        // ie일 경우
        document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/es5/pdf.min.js"><\/script>');
        document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/es5/pdf_viewer.js"><\/script>');
    } else{
        // ie가 아닐 경우
        document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/pdf.min.js"><\/script>');
        document.write('<script src="bower_components/template-designer/js/pdf_form/pdfjs/pdf_viewer.js"><\/script>');
    }
}
