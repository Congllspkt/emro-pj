var StandardResources = {
	
	/** [Step 1] Smartsuite UI Framework Shim(a piece of code), CSS, Components */
	'smartsuite-core' : {
		async: [
			{
				sync: [
					'bower_components/webcomponentsjs/webcomponents-standard.min.js',
					'bower_components/sc-component/sc-elements.std.html',
					'ui/override.html',
					'ui/smartsuite/preloader/sc-preloader.html',
					'ui/smartsuite/mdi/sc-mdi.html',
					'ui/smartsuite/sdi/sc-sdi.html'
				]
			},
			{
				sync: [
					'bower_components/sc-component/sc-elements.std.css',
					'ui/smartsuite/mdi/css/mdi.css',
					'ui/assets/css/common.css',
					'ui/assets/css/basic.design.css',
					'ui/assets/css/common.design.css',
					'ui/assets/css/rfx.config.css'
				]
			}
		]
	},
	
	/** [Step 2] Smartsuite UI Framework Preloader & Commons */
	'smartsuite-modules' : {
		/** [Step 2-1] Main Module(MDI/SDI) 생성 전(공통 서비스) */
		preinitialize: {
			sync: [
				{
					async: [
						/** 필수 라이브러리 */
						{
							sync: [
								"js/jquery.min.js",
								"ui/lib/jsTree/jstree.min.js"
							]
						},
						"ui/lib/timeShift/timeShift.js",
						"ui/smartsuite/custom/js/util.js"
					]
				},
				/** Manager(sc-*-manager) 추상화 */
				"ui/smartsuite/common/sc-abstract-manager.js",
				/** 공통 필수 서비스(다국어) */
				"ui/smartsuite/common/sc-i18n-manager.js",
				{
					async: [
						/** 공통 필수 서비스(세션, 권한) */
						"ui/smartsuite/common/sc-session-manager.js",
						"ui/smartsuite/common/sc-role-manager.js",
						/** 공통 서비스(메뉴, 언어, 즐겨찾기) */
						"ui/smartsuite/common/sc-menu-manager.js",
						"ui/smartsuite/common/sc-locale-manager.js",
						/** 공통 서비스(Standard 모듈) */
						"ui/smartsuite/common/sc-module-manager.js"
					]
				}
			]
		},
		
		/** [Step 2-2] Main Module(MDI/SDI) 생성 중(업무화면 생성 전) */
		initialize: {
			async: [
				"ui/lib/big-number/lib/bignumber.min.js",
				"ui/smartsuite/custom/js/def.js",
				"ui/smartsuite/custom/js/formatters.js",
				"ui/smartsuite/custom/js/validators.js",
				"bower_components/cipher-util/cipher-util.min.js",
				"bower_components/password-encryptor/password-encryptor.min.js",
				{
					sync: [
						"bower_components/smartsuite/excel/smartsuite-excel-export.min.js",
						"ui/smartsuite/excel/export-override.js"
					]
				}
			]
		},
		
		/** [Step 2-3] Main Module(MDI/SDI) 생성 완료 */
		applicationComplete: {
			async: [
				{
					sync: [
						/** isAuthenticated(로그인인증 시에만 호출) */
						"ui/smartsuite/smartchart/smartchart-lic.js?isAuthenticated=true",
						"ui/smartsuite/smartchart/smart-charting.min.js?isAuthenticated=true"
					]
				},
				'bower_components/template-designer/js/pdf_form/dist/polly.bundle.js'
			]
		}
	}
	
};

