document.writeln("<iframe  src='https://127.0.0.1:15018/' name='hsmiframe' id='hsmiframe' style='visibility:hidden;position:absolute'></iframe>");

if(document.body){
	var winTarget = document.createElement('div');
	winTarget.id = 'ESignWindow';
	document.body.appendChild( winTarget, document.body.firstChild );
}else{
	document.writeln('<div id="ESignWindow"></div>');
}

var parent = null;

var script = document.getElementsByTagName('script');
var currentSrc = script[script.length-1].src;
var l = currentSrc.split("=");
var contextPath = l[l.length - 1];

// MODE 4 = NIM, nim + webstorage = 6
var unisign = UnisignWeb({
    Mode: 4,
    
    PKI: 'NPKI',
	SRCPath: contextPath+'/resources/econtract/crosscert_home/',
    Language: 'ko-kr',
    TargetObj: document.getElementById('ESignWindow'),
    TabIndex: 1000,
    LimitNumOfTimesToTryToInputPW: 3,
    
    // npk, touchen: 라온, ahnlab - 현재는 touchen만 지원됨. 안랩 - e2e_type="11" , 라온 - data-enc="on"
    // TOUCHEN - NPAPI 스타일이라서 CHROME은 해당 되지 않음
    //SecureKeyboardType: 'ahnlab',
 
    /* // TODO : 저장매체 추가시 수정해야될 부분 */
    //Media: {'defaultdevice':'harddisk', 'list':'removable|sectoken|savetoken|mobilephone|harddisk'},/* plugin mode(Mode:1) media list */
    //Media: {'defaultdevice':'webstorage', 'list':'webstorage|touchsign|smartsign|websectoken|websofttoken'},/* plugin-free mode(Mode:2) media list */
   // NPKI
    Media: {'defaultdevice':'harddisk', 'list':'mobiletoken|sectoken|savetoken|removable|harddisk|mobilephone|securedisk'},	/* all media(Mode:3) list */
   // GPKI
//    Media: {'defaultdevice':'harddisk', 'list':'sectoken|savetoken|removable|harddisk'},/* all media(Mode:3) list */
    
    
	//Policy: '1.2.410.200004.5.2.1.1|1.2.410.200004.5.1.1.7|1.2.410.200005.1.1.5|1.2.410.200004.5.4.1.2|1.2.410.200012.1.1.3',
    //Policy: '1.2.410.200004.5.4.1.2',  Policy: '1.2.410.200004.5.4.1.1',
    
    //ShowExpiredCerts: false,
    CMPIP:  'testca.crosscert.com', // '211.180.234.221', // 'testca.crosscert.com',  //CMP IP // '211.118.38.179',
    //CMPIP: '211.180.234.216',  //CMP IP
    CMPPort: 4502,  //CMP Port			// real 사설 - 3502   // 그외 - 4502
    
    LimitMinNewPWLen: 8,
    LimitMaxNewPWLen: 64,
    LimitNewPWPattern: 2,  //0 : 제한 없음, 1 : 영문,숫자 혼합, 2 : 영문,숫자,특수문자 혼합
	ChangePWByNPKINewPattern: true,
	
	SDInstallURL: 'http://testca.crosscert.com/test/download.html',
	
	// 지문보안토큰에 인증서 밝급 여부 설정
	IssueCertInBIOToken: false,


    Policy: '1.2.410.200004.5.2.1.1|1.2.410.200004.5.1.1.7|1.2.410.200005.1.1.5|1.2.410.200004.5.4.1.2|1.2.410.200012.1.1.3',

	// 파일 명. root 디렉토리
	//License: "License.js"	
});

unisign.SetOptions("popup", "center,overlay=true");
unisign.SetMobileTokenEnvInfo("303010001", "0003", "www.crosscert.com", "service.smartcert.kr", "443", "http://download.smartcert.kr");
unisign.SetUBIKeyEnvInfo("1,3,0,7", "CROSSCERT|http://www.ubikey.co.kr/infovine/download.html", "CROSSCERT|NULL", "http://www.ubikey.co.kr/infovine/download.html");			
	
