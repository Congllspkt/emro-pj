var contextPathUseYn = "N";

function getWebRootPath(){
	if(contextPathUseYn === "Y"){
		var hostIndex = location.href.indexOf(location.host) + location.host.length;
		var contextPath = location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1));
		console.log("contextPath:" + contextPath);
		return contextPath;	
	}else if(contextPathUseYn === "N"){
		return "/";
	}
}