/**
 * SCMdiPopupManager
 *
 * 1. MDI내에서 사용하는 Popup의 Scheme 관리
 * 2. 일관된 팝업 호출 패턴 정의
 */
SCMdiPopupManager = new(function() {

	/** 생성자 */
	function SCMdiPopupManagerImpl() {
	};
	
	SCMdiPopupManagerImpl.prototype = {
		
		/** 오버라이드 */
		extends : function(target, source){
			return Object.assign(target, source);
		},
		
		get userModel() {
   			return this.mdi.userModel;
   		},
   		
   		get menuModel() {
   			return this.mdi.menuModel;
       	},
       	
       	get currentUser() {
       		return this.mdi.currentUser;
   		},
   		
   		get mdi() {
   			if(!this._mdi){
   				this._mdi = document.querySelector("sc-mdi");
   			}
   			return this._mdi;
   		},
   		
   		get translate() {
   			return this.mdi.translate;
   		},
   		
   		get importLink() {
   			return this.mdi.importLink.bind(this.mdi);
   		}
		
	};
	
	/** Popup 호출 */
	SCMdiPopupManagerImpl.prototype.importLinkPopup = function(scope, popupScheme, events){
    	this.importLink(popupScheme.linkUrl, function(popupScheme, _events, tagName) {
			var popup = UT.popup(
            		tagName, 
            		this, 
            		popupScheme.width,
            		popupScheme.height, 
            		_events || {}, 
            		this.extends(popupScheme.options, {
            			"titleText": this.translate(popupScheme.options.titleText)
            		}));
            popup.show();
            popup.getWindowContent().load && popup.getWindowContent().load();
        }.bind(scope, popupScheme, events || popupScheme.events));
    },
    
    /** Dialog 기반의 Popup 호출 */
    SCMdiPopupManagerImpl.prototype.importLinkDialog = function(scope, popupScheme, okCallbackFn){
    	this.importLink(popupScheme.linkUrl, function(popupScheme, _okCallbackFn, tagName) {
   			var instance = document.createElement(tagName);
   			var dialog = document.createElement("sc-dialog");
   			dialog.titleText = popupScheme.options.titleText;
   			dialog.style.width = popupScheme.width+"px";
   			dialog.style.height = popupScheme.height+"px";
   			dialog.titleAlign = "left";
   			dialog.modal = true;
   			dialog.closable = false;
   			dialog._contentElement.appendChild(instance);
   			this.mdi.appendChild(dialog);
			dialog.show({
				buttons: 'ok',
				handler: _okCallbackFn.bind(this)
			});
        }.bind(scope, popupScheme, okCallbackFn || popupScheme.okCallbackFn));
    },
    
	/** 공지사항 팝업 */
	SCMdiPopupManagerImpl.prototype.startNoticeStep = function() {
      	/** 1. 공지사항 서비스 */
      	this.importLink("ui/smartsuite/mdi/popup/sc-mdi-notice-service.html", function(moduleId) {
      		this.SCMdiNoticeService = document.createElement(moduleId);
      		this.SCMdiNoticeService.addEventListener('notice-list-changed', function(event) {
			this.SCMdiNoticeService.removeEventListener('notice-list-changed', arguments.callee);
				var noticeList = event.detail.value;
				if(noticeList.length > 0){
					/** 2. 공지사항 팝업 호출 */
					this.noticeList = noticeList;
					this.createNoticeListPopup(noticeList);
				}
			}.bind(this));
		}.bind(this))
	},
	
	/** 사용자 정보 팝업 */
	SCMdiPopupManagerImpl.prototype.changeUserInfoPopup = function(){
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-user-popup.html",
			width: 850,
			height: 690,
			options: {
				titleText : "사용자 정보 변경"
			}
		});
	},
	
	/** 로그인 정보 */
	SCMdiPopupManagerImpl.prototype.loginInfoPopup = function(callbackFn){
		this.importLinkDialog(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-login-info-popup.html",
			width: 400,
			height: 210,
			options: {
				titleText: "알람"
			},
			okCallbackFn: function(){
				callbackFn();
				if(this.currentUser[this.userModel.usr_cls] === 'VD'){
					var request = document.createElement("sc-ajax");
					request.url = 'existChargeTermsAgreeInfoByTermsConditionsAgreeId.do';
					request.addEventListener("response", (function(event) {
						var response = event.detail.response;
						if(response.terms_popup_yn === "Y"){
							SCMdiPopupManager.createTermsPopup();
						}else{
							SCMdiPopupManager.startNoticeStep();
						}
					}).bind(this));
					request.generateRequest();
				}else{
					SCMdiPopupManager.startNoticeStep();
				}
			}
		});
	},
			
	/** 팝업 */
	SCMdiPopupManagerImpl.prototype.createTermsPopup = function() {
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-terms-popup.html",
			width: 800,
			height: "90%",
			options: {
				maximizable : true, 
				titleText : "이용약관 동의", 
				closable : false
			}, 
			events: {
				"agree-terms":	function(popup, e) {
					popup.close();
					SCMdiPopupManager.startNoticeStep();
				}
			}
		});
	},

	/** 공지 팝업 */
	SCMdiPopupManagerImpl.prototype.createNoticeListPopup = function(noticeList){
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-notice-popup.html",
			width: 800,
			height: "90%",
			options: {
				maximizable : true, 
				titleText : "공지사항" 
			},
			events: {
				"attached": function(popup, e){
					popup.getWindowContent().setNoticeList(noticeList);
				},
				"notice-hidden": function(popup, e){
					SCMdiPopupManager.SCMdiNoticeService.setCookie(e.detail.pst_no, e.detail.pst_no, 1);
				}
			}
		});
	},
            
	/** 계정 알림 팝업 */
	SCMdiPopupManagerImpl.prototype.accountNotifyPopup = function() {
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-account-notify.html",
			width: 600,
			height: 400,
			options: {
				titleText : "공지",
				closable: true
			}
		});
	},
            
	/** 비밀번호 변경 팝업 */
	SCMdiPopupManagerImpl.prototype.changePasswordPopup = function() {
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-pw-popup.html",
			width: 490,
			height: 640,
			options: {
				titleText: "비밀번호 변경",
				closable: true
			},
			events: {
				"logout": function() {
					//SCMdiPopupManager.mdi.logout();
					SCMdiManager.logout();
				}
			}
		});
	},
	
	/** 이메일 수신대상 팝업 */
	SCMdiPopupManagerImpl.prototype.selectReceptionMailPopup = function(){
		this.importLinkPopup(this, {
			linkUrl:"ui/smartsuite/mdi/popup/sc-mdi-select-mail-popup.html",
			width: 1350,
			height: 700,
			options: {
				titleText : "이메일 수신 제외"
			}
		});
	},
	SCMdiPopupManagerImpl.prototype.destroy = function() {
		this.scheme = null;
	};

	return SCMdiPopupManagerImpl;

}());
