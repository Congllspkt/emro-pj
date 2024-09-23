/***********************************************
    라이센스 정보
************************************************/
var NXS_LICENSE = '';
if (document.location.hostname.indexOf('domain') >= 0) {
    // 도메인 라이센스 :
    NXS_LICENSE = '';
}
else {
    // 개발자 라이센스 만료일 : 2021-09-29
    NXS_LICENSE = 'bmpNdjBRWlNZd1lncWprTUZFZkUrMkRZaGFSTlFLZW80Z1d4Vzhid2N3dGhUQ2krc0Q0L1djOHVETUlhckF5S2ZqaU1lRHVpZHZ4WGhyRUpmUmt0NUE9PVwwS21GK2RZN2dWOD1cbWtPUXZrU2hoSStmMEFMR1h3TFdZWHlWOS9zPQ=='; 
}


/***********************************************
   내부 참조 JavaScript 경로 설정 : SecuKitTAX/KICA/config/LoadSecuKitNX.js
************************************************/
var secukitnxBaseDir = "/resources/econtract/SecuKitTAX/";


/***********************************************
    이미지 상대경로 설정
************************************************/
var NX_MEDIA_IMG_URL = '/resources/econtract/SecuKitTAX/WebUI/images/media/';
var NX_DEFAULT_IMG_URL = '/resources/econtract/SecuKitTAX/WebUI/images/';


/***********************************************
    배너 이미지 URL : 가로 : 410px 세로 : 93px
************************************************/
var NX_Banner_IMG_URL = NX_DEFAULT_IMG_URL + 'banner/default_banner.png';


/***********************************************
    SecuKit NX 설치페이지 URL 경로
************************************************/
var NX_INSTALL_FLAG = false;                                // 설치페이지를 별도로 운영할지 여부 : FALSE 인 경우 업무페이지에서 설치파일 다운로드 실행
var NX_INSTALL_PAGENAME = 'install';                        // 설치페이지 명 : 설치페이지 명으로 내부에서 분기처리하기 때문에 NX_INSTALL_PAGE 경로의 페이지 명을 넣어주면 된다.
var NX_INSTALL_PAGE = './install.html';                     // 설치페이지 URL


/***********************************************
    SecuKit NX 다운로드 경로
************************************************/
var _OSName = "Unknown";
if (window.navigator.userAgent.indexOf("Mac") != -1) _OSName = "Mac/iOS";
if (window.navigator.userAgent.indexOf("X11") != -1) _OSName = "UNIX";
if (window.navigator.userAgent.indexOf("Linux") != -1) {
    if (window.navigator.userAgent.indexOf("Ubuntu") != -1) {
        _OSName = "Linux/Ubuntu";
    }
    else {
        _OSName = "Linux";
    }
}

var NXClient_DownLoad_URL = '';

if (_OSName === 'Mac/iOS')
{
    // MacOS
    NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuTaxNXS.exe';    /** Client 설치파일**/
}
else if (_OSName === 'Linux/Ubuntu')
{
    // Linux/Ubuntu
    NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuTaxNXS.exe';    /** Client 설치파일**/
}
else
{
    // Windows
    NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuTaxNXS.exe';    /** Client 설치파일**/
}


/***********************************************
    Client 버전 정보
************************************************/
var Module_NX_Ver = '1.0.5.1';
var Module_KPMCNT_Ver = '1.1.5.0';
var Module_KPMSVC_Ver = '1.1.5.0';

// 멀티OS Client 버전 정보
var MULTIOS_MacOS_Ver = '0.0.2.0';
var MULTIOS_Ubuntu_Ver = '0.0.2.0';

/***********************************************
    Console log 사용 유무
************************************************/
var ConsoleLogFlag = true;


/***********************************************
    모듈 로딩 전 화면 블락 처리
************************************************/
var NXSBlockWrapLayer = true;


/***********************************************
    인증서 선택창 이동 활성화
************************************************/
var NX_DIALOG_MOVE = true;


/***********************************************
    로케일 설정  ex: KR, EN, FR
************************************************/
var NXLOCALE = 'KR';


/***********************************************
    SecuKitNX NX CharSet 설정 : 내부 통신시 인코딩 set
************************************************/
var inCharset = 'UTF-8';
var outCharset = 'UTF-8';


/***********************************************
    정책 설정 환경 변수
************************************************/
var NX_AnyPolicy = 'Y';
var NX_POLICIES = '1.2.410.200004.5.2.1.2';


/***********************************************
    인증서 선택창 미디어 세팅
************************************************/
var NX_SELECT_CERT_MEDIA = [
{
    MEDIA: 'HDD',
    ORDER: 2,           // 표시 순서
    ABLE: 'able',       // 선택창 활성화 유무 (able / disable)
    DEFAULT: 'able'  // 하드디스크를 기본으로 먼저 표시하려고 할때 (able / disable)
},
{
    MEDIA: 'USB',
    ORDER: 1,
    ABLE: 'able'
},
{
    MEDIA: 'HSM',
    ORDER: 3,
    ABLE: 'able'
},
{
    MEDIA: 'BIOHSM',
    ORDER: 4,
    ABLE: 'able'
},
{
    MEDIA: 'USIM',
    ORDER: 5,
    ABLE: 'able'
},
{
    MEDIA: 'EXTENSION',
    ORDER: 6,           // 수정 금지
    ABLE: 'able'
},
{
    MEDIA: 'SECUREDISK',
    ORDER: 7,
    ABLE: 'able'
},
{
    MEDIA: 'PHONE',
    ORDER: 8,
    ABLE: 'able'
}

];


/***********************************************
    발급 관련 환경 변수
************************************************/
var NX_CA_IP = 'catest.signgate.com',
    NX_CA_PORT = '4502';


/***********************************************
     인증서 선택창 관리창 활성화 (발급 모듈)
************************************************/
var CERTMGR_F = true;

/***********************************************
    발급/관리 성공&실패 UI 표시 유무
************************************************/
var ISSUE_ALERT_UI_SHOW = true;
var MANAGEMENT_ALERT_UI_SHOW = true;

/***********************************************
    타켓 미디어 세팅 : 발급&재발급 등에서 사용되는 미디어 선택창
************************************************/
var NX_TARGET_MEDIA = [
{
    MEDIA: 'HDD',
    ORDER: 2, // 표시 순서
    ABLE: 'able'   // 선택창 활성화 유무 (able / disable)
},
{
    MEDIA: 'USB',
    ORDER: 1,
    ABLE: 'able'
},
{
    MEDIA: 'HSM',
    ORDER: 3,
    ABLE: 'able'
},
{
    MEDIA: 'BIOHSM',
    ORDER: 4,
    ABLE: 'able'
},
{
    MEDIA: 'USIM',
    ORDER: 5,
    ABLE: 'able'
},
{
    MEDIA: 'EXTENSION',
    ORDER: 6,           // 수정 금지
    ABLE: 'able'
},
{
    MEDIA: 'SECUREDISK',
    ORDER: 7,
    ABLE: 'able'
}

];


/***********************************************
     USIM 관련
************************************************/
//USIM 스마트 인증 DLL명
var USIMDRIVE_NAME = {
    RAON: "USIMCert.dll",         // 라온시큐리티
    DREAM: "USIMDream.dll"        // 드림시큐리티
};

//USIM 다운로드 기본 URL
var USIM_DOWNLOAD_URL = "http://center.smartcert.co.kr/";   //드림시큐리티 쪽
var USIM_OPEN_SIZE_W = '';
var USIM_OPEN_SIZE_H = '';
var USIM_DOWNLOAD_EXE = "http://ids.smartcert.kr/";

//USIM 사이트 코드
var USIM_SITECODE = "000000000";


/***********************************************
     키보드보안 관련
************************************************/
var NOS_F       = false;	// 잉카 키보드 보안 모듈 동작시 
var RAON_F      = false;    // 라온시큐어 키보드 보안 모듈 동작시
var AHNLAB_F    = false;    // 안랩 키보드 보안 모듈 동작시
var CUSTENC		= false;	// 소프트일레븐 키보드 보안 모듈 동작시


/***********************************************
     안전디스크 관련
************************************************/
var SECUREDISK_DRIVE_NAME = "SecureDisk:1.5.6.0";
var SECUREDISK_DOWNLOAD_URL = secukitnxBaseDir + 'Install/PKCS11Setup.zip';


/***********************************************
    KMS & XML 다운로드 경로
************************************************/
var NXClient_DownLoad_URL_XML = secukitnxBaseDir + 'Install/SecuTaxNXS.exe';   /** Client 설치파일**/
var NXClient_DownLoad_URL_KMS = secukitnxBaseDir + 'Install/KMS.exe';       /** Client 설치파일**/


/***********************************************
    KMS & XML Client 버전 정보
************************************************/
var Module_XML_Ver = '1.0.0.15';
var Module_KMS_Ver = '1.0.0.30';
var Module_EDS_Ver = '1.0.5.3'; 

/***********************************************
    KMS Info
************************************************/
var DEF_KMS_COUNT = "1";
var DEF_KMS_INFO = [{ "ip": "", "port": "", "path": "", "protocol": "", "kmsNumber": "" }];


/***********************************************
    바이오보안토큰 PKCS#7 
************************************************/
var BioTokenP7Message = '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';


/***********************************************
    유비키 설치URL
************************************************/
var INFOVINE_UBYKEY_URL = "http://tech.signgate.com"; // 인포바인 확인 후 URL 수정 필요, 팝업 설정은 dialog.js 확인


/***********************************************
비밀번호 횟수 제한
***********************************************/
var NX_PW_COUNT = false;		//비밀번호 카운트 사용시 true
var NX_PW_COUNT_SET = 3;	//비밀번호가 틀리면 감소     횟수만큼 사용 가능
var NX_PW_COUNT_GET = 3;	//바뀌지 않는 count 횟수  횟수만큼 사용 가능
var NX_SELECTED_ROW = 0;	//현재 선택된 인증서와 같은 인증서 인지 아닌지 구분, 항상 0으로 세팅