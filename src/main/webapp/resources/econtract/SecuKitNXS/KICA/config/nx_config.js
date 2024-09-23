/***********************************************
    라이센스 정보
************************************************/
var NXS_LICENSE = '';
if (document.location.hostname.indexOf('domain') >= 0) {
  // 도메인 라이센스 :
  NXS_LICENSE = '';
} else {
  // 개발자 라이센스 만료일 : 2021.01.17
  NXS_LICENSE =
    'T3RTcE1aZ3FwNC9lK0crZ1c2SFNUc21BMTJPdkptaG41bGtuUHVuc2NlQ2dRYUFaZVNuc3JiTitUamN2NzZEZEVpamFsck1XQmt6WGlSUko3VG5tWGc9PVxIVzg1RHVFOEZhOD1cR3dSZVo4aGRKUmxlUnRRZmNuNEpJSHRPOEZZPQ==';
}

/***********************************************
   내부 참조 JavaScript 경로 설정 : SecuKitNXS/KICA/config/LoadSecuKitNX.js
************************************************/
var secukitnxBaseDir = '/resources/econtract/SecuKitNXS/';

/***********************************************
    이미지 상대경로 설정
************************************************/
var NX_MEDIA_IMG_URL = '/resources/econtract/SecuKitNXS/WebUI/images/media/';
var NX_DEFAULT_IMG_URL = '/resources/econtract/SecuKitNXS/WebUI/images/';

/***********************************************
    배너 이미지 URL : 가로 : 410px 세로 : 93px
************************************************/
var NX_Banner_IMG_URL = NX_DEFAULT_IMG_URL + 'banner/default_banner.png';

/***********************************************
    SecuKit NX 설치페이지 URL 경로
************************************************/
var NX_INSTALL_FLAG = false; // 설치페이지를 별도로 운영할지 여부 : FALSE 인 경우 업무페이지에서 설치파일 다운로드 실행
var NX_INSTALL_PAGENAME = 'install'; // 설치페이지 명 : 설치페이지 명으로 내부에서 분기처리하기 때문에 NX_INSTALL_PAGE 경로의 페이지 명을 넣어주면 된다.
var NX_INSTALL_PAGE = './install.html'; // 설치페이지 URL

/***********************************************
    SecuKit NX 다운로드 경로
************************************************/
var _OSName = 'Unknown';
if (window.navigator.userAgent.indexOf('Mac') != -1) _OSName = 'Mac/iOS';
if (window.navigator.userAgent.indexOf('X11') != -1) _OSName = 'UNIX';
if (window.navigator.userAgent.indexOf('Linux') != -1) {
  if (window.navigator.userAgent.indexOf('Ubuntu') != -1) {
    _OSName = 'Linux/Ubuntu';
  } else {
    _OSName = 'Linux';
  }
}

var NXClient_DownLoad_URL = '';

if (_OSName === 'Mac/iOS') {
  // MacOS
  NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuKitNXS.exe'; /** Client 설치파일**/
} else if (_OSName === 'Linux/Ubuntu') {
  // Linux/Ubuntu
  NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuKitNXS.exe'; /** Client 설치파일**/
} else {
  // Windows
  NXClient_DownLoad_URL = secukitnxBaseDir + 'Install/SecuKitNXS.exe'; /** Client 설치파일**/
}

/***********************************************
    Client 버전 정보
************************************************/
var Module_NX_Ver = '1.0.6.7';
var Module_KPMCNT_Ver = '1.1.5.4';
var Module_KPMSVC_Ver = '1.1.5.2';

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
    - NX_AnyPoilcy : Y => 모든 인증서 노출
    - NX_AnyPolicy : N => NX_POLICIES에 세팅한 인증서만 노출
    - NX_POLICIES : 특정 OID를 세팅하여 필요한 인증서만 노출('|' 로 구분하여 사용)
************************************************/
var NX_AnyPolicy = 'Y';
var NX_POLICIES = '';

var NX_AllowExpired = 'N';

/***********************************************
    인증서 선택창 미디어 세팅
************************************************/
var NX_SELECT_CERT_MEDIA = [
  {
    MEDIA: 'HDD',
    ORDER: 2, // 표시 순서
    ABLE: 'able', // 선택창 활성화 유무 (able / disable)
    DEFAULT: 'able', // 하드디스크를 기본으로 먼저 표시하려고 할때 (able / disable)
  },
  {
    MEDIA: 'USB',
    ORDER: 1,
    ABLE: 'able',
  },
  {
    MEDIA: 'HSM',
    ORDER: 3,
    ABLE: 'able',
  },
  {
    MEDIA: 'BIOHSM',
    ORDER: 4,
    ABLE: 'able',
  },
  {
    MEDIA: 'USIM',
    ORDER: 5,
    ABLE: 'able',
  },
  {
    MEDIA: 'EXTENSION',
    ORDER: 6, // 수정 금지
    ABLE: 'able',
  },
  {
    MEDIA: 'SECUREDISK',
    ORDER: 7,
    ABLE: 'able',
  },
  {
    MEDIA: 'PHONE',
    ORDER: 8,
    ABLE: 'able',
  },
];

/******* 아래 세팅은 인증서 발급을 위한 세팅으로 
  발급 관련된 업무가 없으면 무시해도 됨 ************/

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
    ABLE: 'able', // 선택창 활성화 유무 (able / disable)
  },
  {
    MEDIA: 'USB',
    ORDER: 1,
    ABLE: 'able',
  },
  {
    MEDIA: 'HSM',
    ORDER: 3,
    ABLE: 'able',
  },
  {
    MEDIA: 'BIOHSM',
    ORDER: 4,
    ABLE: 'able',
  },
  {
    MEDIA: 'USIM',
    ORDER: 5,
    ABLE: 'able',
  },
  {
    MEDIA: 'EXTENSION',
    ORDER: 6, // 수정 금지
    ABLE: 'able',
  },
  {
    MEDIA: 'SECUREDISK',
    ORDER: 7,
    ABLE: 'able',
  },
];

/***********************************************
     USIM 관련
************************************************/
//USIM 스마트 인증 DLL명
var USIMDRIVE_NAME = {
  RAON: 'USIMCert.dll', // 라온시큐리티
  DREAM: 'USIMDream.dll', // 드림시큐리티
};

//USIM 다운로드 기본 URL
var USIM_DOWNLOAD_URL = 'http://center.smartcert.co.kr/'; //드림시큐리티 쪽
var USIM_OPEN_SIZE_W = '';
var USIM_OPEN_SIZE_H = '';
var USIM_DOWNLOAD_EXE = 'http://ids.smartcert.kr/';

//USIM 사이트 코드
var USIM_SITECODE = '000000000';

/***********************************************
     키보드보안 관련
************************************************/
var NOS_F = false; // 잉카 키보드 보안 모듈 동작시
var RAON_F = false; // 라온시큐어 키보드 보안 모듈 동작시
var AHNLAB_F = false; // 안랩 키보드 보안 모듈 동작시
var CUSTENC = false; // 소프트일레븐 키보드 보안 모듈 동작시

/***********************************************
     안전디스크 관련
************************************************/
var SECUREDISK_DRIVE_NAME = 'SecureDisk:1.5.6.0';
var SECUREDISK_DOWNLOAD_URL = secukitnxBaseDir + 'Install/PKCS11Setup.zip';

/***********************************************
    KMS & XML 다운로드 경로
************************************************/
var NXClient_DownLoad_URL_XML = secukitnxBaseDir + 'Install/SecuXML.exe'; /** Client 설치파일**/
var NXClient_DownLoad_URL_KMS = secukitnxBaseDir + 'Install/KMS.exe'; /** Client 설치파일**/

/***********************************************
    KMS & XML Client 버전 정보
************************************************/
var Module_XML_Ver = '1.0.0.15';
var Module_KMS_Ver = '1.0.0.30';

/***********************************************
    KMS Info
************************************************/
var DEF_KMS_COUNT = '1';
var DEF_KMS_INFO = [{ ip: '', port: '', path: '', protocol: '', kmsNumber: '' }];

/***********************************************
    바이오보안토큰 PKCS#7 
************************************************/
var BioTokenP7Message =
  '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';

/***********************************************
    유비키 설치URL
************************************************/
var INFOVINE_UBYKEY_URL = 'http://tech.signgate.com'; // 인포바인 확인 후 URL 수정 필요, 팝업 설정은 dialog.js 확인

/***********************************************
비밀번호 횟수 제한
***********************************************/
var NX_PW_COUNT = false; //비밀번호 카운트 사용시 true
var NX_PW_COUNT_SET = 3; //비밀번호가 틀리면 감소     횟수만큼 사용 가능
var NX_PW_COUNT_GET = 3; //바뀌지 않는 count 횟수  횟수만큼 사용 가능
var NX_SELECTED_ROW = 0; //현재 선택된 인증서와 같은 인증서 인지 아닌지 구분, 항상 0으로 세팅
