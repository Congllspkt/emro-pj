/**
 * Util
 *
 * @class UT
 */

var UT = {
	/**
	 * 날짜 객체의 복사
	 * @method dateCopy
	 * @param {Date} dateInfo - 복사하려는 날짜 객체
	 * @returns {Date} 복사된 날짜 객체
	 */
	dateCopy: function(dateInfo) {
		var newDate = new Date();
		newDate.setTime(dateInfo.getTime());
		return newDate;
	},
	
	/**
	 * 상위 노드에서 화면 삭제
	 *
	 * @method removeModule
	 * @param {object} parentNode 삭제할 객체의 상위 객체
	 * @param {object} event      이벤트 객체
	 * @example
	 *     someFunction: function(e) {
	 *         var me = this;
	 *         UT.removeModule(me.$.pages, e);
	 *     }
	 */
	removeModule: function(parentNode, event) {
		Polymer.dom(parentNode).removeChild(event.currentTarget);
		
		// 메모리 해제
		event.currentTarget.destroy();
	},

	multiLocaleCheck : function (){
		return (SCLocaleManager.getLocale() != 'ko_KR');
	},

	/**
	 * alert dialog
	 *
	 * @method alert
	 * @param {string}   message      출력할 메시지
	 * @param {function} [okCallback] ok 버튼 클릭 후 콜백 함수
	 * @param {Boolean} 다국어 처리 비활성화 여부 (기본 false:활성화)
	 * @async
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.alert("Hello World!");
	 *     }
	 */
	alert: function(message, okCallback, i18nDisabled, options) {
		var options = options || {};
		options.autoGrowMessageBox = options.autoGrowMessageBox || true;
		SCAlert._messagebox.autoGrowMessageBox = options.autoGrowMessageBox;
		i18nDisabled = typeof i18nDisabled !== 'undefined' ? i18nDisabled : false;
		return SCAlert.show(i18nDisabled ? I18N.translate("STD.N1000") : "STD.N1000", message, function(btn) {
			if(UT.isFunction(okCallback)) {
				okCallback.call(this);
			}
		}, null, i18nDisabled);
//        options.titleText = "STD.N1000";
//        return UT.messageBox(message, {"ok": okCallback}, i18nDisabled, options);
	},


	completeAlert: function(appendMessage, okCallback, i18nDisabled, options) {
		if(UT.isEmpty(appendMessage)) {
			appendMessage = "STD.N1500";
		}else {
			appendMessage = "[" + I18N.translate(appendMessage,{"i18nType":"button"}) + "]" +  " " + I18N.translate("STD.N1500",{"i18nType":"label"});
			i18nDisabled = true;
		}

		this.alert(appendMessage, okCallback, i18nDisabled, options)
	},

	/**
	 * confirm dialog
	 *
	 * @method confirm
	 * @param {string}   message       출력할 메시지
	 * @param {function} [yesCallback] ok 버튼 클릭 후 콜백 함수
	 * @param {function} [noCallback]  no 버튼 클릭 후 콜백 함수
	 * @param {Boolean} 다국어 처리 비활성화 여부 (기본 false:활성화)
	 * @async
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.confirm("Hello World!", function() {
	 *             // yes
	 *             ...
	 *         }, function() {
	 *             // no
	 *             ...
	 *         });
	 *     }
	 */
	confirm: function(message, yesCallback, noCallback, i18nDisabled, options) {
		var options = options || {};
		options.autoGrowMessageBox = options.autoGrowMessageBox || true;
		SCAlert._messagebox.autoGrowMessageBox = options.autoGrowMessageBox;
		i18nDisabled = typeof i18nDisabled !== 'undefined' ? i18nDisabled : false;
		return SCAlert.confirm(i18nDisabled ? I18N.translate("STD.N1100") : "STD.N1100", message, function(btn) {
			if(btn === "yes") {
				if(UT.isFunction(yesCallback)) {
					yesCallback.call(this);
				}
			} else { // no
				if(UT.isFunction(noCallback)) {
					noCallback.call(this);
				}
				
				// 2019.07.04 SMARTNINE-3138 신규업체업체 등록, newVendor.jsp 사용으로 인해 추가.
				if(MDIUT.hasMdi()) {
					MDIUT.goHistory();
				}
			}
		}, null, i18nDisabled);
//        options.titleText = "STD.N1100";
//        return UT.messageBox(message, {"yes": yesCallback, "no": noCallback}, i18nDisabled, options);
	},
	
	/**
	 * custom multi button messagebox dialog
	 *
	 * @method messageBox
	 * @param {string}  [message]                           출력할 메시지
	 * @param {function}[callback]                          function(id, text, type)
	 * @param {Boolean} [i18nDisabled=false]                다국어 처리 비활성화 여부 (기본 false:활성화)
	 * @param {object}  [options]                           팝업 설정 옵션
	 * @param {string}  [options.buttons='yes,no,test']     버튼 (기본 'ok')
	 * @param {string}  [options.titleText]                 툴바 타이틀
	 * @param {string}  [options.iconCls]                   icon cls : 'info', 'error' 등..
	 * @param {boolean} [options.autoGrowMessageBox=true]  메시지 박스 높이 자동 조정 여부
	 * @example
	 *      someFunction: function() {
	 *          var me = this;
	 *          ...
	 *          UT.messageBox("Hello World!", function(id, text, type){
	 *              switch (id) {
	 *                  case "yes" :   // id:'yes', text:'예', type:'yes'
	 *                      console.log(id);
	 *                      break;
	 *                  case "no" :   // id:'no', text:'아니오', type:'no'
	 *                      console.log(id);
	 *                      break;
	 *                  case "ok" :   // id:'ok', text:'확인', type:'ok'
	 *                      console.log(id);
	 *                      break;
	 *                  case "custom" :   // id:'custom', text:'custom', type:'custom'
	 *                      console.log(id);
	 *                      break;
	 *                  case "test" :   // id:'test', text:'test', type:'custom'
	 *                      console.log(id);
	 *                      break;
	 *                  default :
	 *                     break;
	 *             }
	 *         }, false, {buttons:'yes,no,ok,custom,test,cancel', titleText:"messagebox!!", autoGrowMessageBox: false, iconCls:'info'});
	 *     }
	 */
	messageBox: function(message, callback, i18nDisabled, options) {
		i18nDisabled = UT.isBoolean(i18nDisabled) ? i18nDisabled : false;
		options = options || {};
		
		var messageBox = SCAlert._messagebox;
		var buttons = options.buttons || "ok";                          // 버튼 (기본값:ok)
		var titleText = options.titleText || "STD.N1000";               // 툴바 타이틀 (기본값:알림)
		var iconCls = options.iconCls ? options.iconCls : null;         // 아이콘 설정 (기본값:null)
		var autoGrowMessageBox = options.autoGrowMessageBox || true;   // 높이 자동 증가 여부 (기본값:true)

		var handler = function(type, text) {    // 메시지 박스의 버튼 클릭 핸들러 함수 정의
			// buttonIds, buttonTexts에 정의된 버튼의 경우 => yes={type:'yes', text:'예'} 또는 ok={type:'ok', text:'확인'}
			// 커스텀 버튼의 경우 => customBtn={type:'custom', text:'customBtn'} 또는 testBtn={type:'custom', text:'testBtn'}
			// 콜백 함수에서 첫 번째 인자를 사용하여 클릭한 버튼을 식별하기 위해 id 정의(기본: type, 커스텀 버튼인 경우: text)
			var id = type == "custom" ? text : type;
			if(UT.isFunction(callback)) {
				callback.call(this, id, text, type); // 기존에 type, text 순서로 전달했기 때문에 id, text, type 순으로 전달
			}
		};
		
		// 기존에 열려있는 메시지 박스가 있으면 닫고 다시 연다.
		if(messageBox.isShowing()) {
			messageBox.close();
		}
		
		return messageBox.show({
			titleText: i18nDisabled ? I18N.translate(titleText) : titleText,
			message: message,
			modal: true,
			buttons: buttons,
			handler: handler,
			iconCls: iconCls,
			i18nDisabled: i18nDisabled,
			autoGrowMessageBox: autoGrowMessageBox
		});
	},
	
	/**
	 * ajax 요청 (SCLoadMask 사용 및 에러 핸들링을 위함)
	 *
	 * @method request
	 * @param {object}  ajax                      sc-ajax 객체
	 * @param {function}[responseCallback(res)]   삭제 완료 후 콜백 함수
	 * @param {object}  responseCallback(res).res 처리 결과 정보
	 * <pre>
	 * &lt;sc-ajax url="url" on-response="func">&lt;/sc_ajax>
	 * on-response 에 함수를 지정하지 않은 경우 : responseCallback 을 정의하여 response를 받을 수 있다
	 * </pre>
	 * @async
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.request(me.$.findInfo);
	 *     }
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.request(me.$.findInfo, function(e, res) {
	 *             ...
	 *         });
	 *     }
	 */
	_requestedCount: 0,
	
	request: function(ajax, responseCallback) {
		if(UT._requestedCount === 0) {
			SCLoadMask.show('__utrequest__');
		}
		UT._requestedCount++;
		
		var listener = function(e) {
			UT._requestedCount--;
			if(UT._requestedCount === 0) {
				SCLoadMask.hide('__utrequest__');
			}
			ajax.removeEventListener("response", listener);
			ajax.removeEventListener("error", listener);
			if(UT.isFunction(responseCallback)) {
				var result;
				if(e.detail.error) {
					result = e.detail;
				} else {
					result = e.detail.parseResponse();
				}
				responseCallback.call(this, e, {response: result});
			}
		};
		ajax.addEventListener("response", listener);
		ajax.addEventListener("error", listener);
		if(SCUtil.isFunction(ajax.request)) {
			ajax.request();
		} else {
			ajax.generateRequest();
		}
	},
	
	requestNotUseLoadMask: function(ajax, responseCallback) {
		var listener = function(e) {
			ajax.removeEventListener("response", listener);
			ajax.removeEventListener("error", listener);
			if(UT.isFunction(responseCallback)) {
				var result;
				if(e.detail.error) {
					result = e.detail;
				} else {
					result = e.detail.parseResponse();
				}
				responseCallback.call(this, e, {response: result});
			}
		};
		ajax.addEventListener("response", listener);
		ajax.addEventListener("error", listener);
		if(SCUtil.isFunction(ajax.request)) {
			ajax.request();
		} else {
			ajax.generateRequest();
		}
	},
	
	
	isJson: function(obj) {
		if(typeof obj == "object") {
			obj = JSON.stringify(obj);
		}
		
		if(typeof obj == "string" && obj.length > 0) {
			let json = "";
			let isValid = false;
			try {
				json = JSON.parse(obj);
				isValid = true;
			} catch(e) {
				isValid = false;
			}
			if(!json || typeof json !== "object") isValid = false;
			return isValid;
		}
	},
	/*
		jsonToString : function(object){
			var results = [];
			for (var property in object) {
				var value = object[property];
				if (value)
					results.push(property.toString() + ': ' + value);
				}
				
				return '{' + results.join(', ') + '}';
		},
	*/
	
	/**
	 * clone object
	 *
	 * @method copy
	 * @param  {object} object 복사할 대상 object
	 * @return {object} new object
	 * @example
	 *     someFunction: function(param) {
	 *         var me = this;
	 *         me.set("findInfo.param", UT.copy(param));
	 *     }
	 */
	copy: function(object) {
		if(!UT.isObject(object)) {
			return object;
		}
		if(SCUtil.isDate(object)) {
			var copy = new Date();
			copy.setTime(object.getTime());
			return copy;
		}
		if(Array.isArray(object)) {
			return object.concat();
		}
		var copy = {};
		for(var key in object) {
			if(object.hasOwnProperty(key)) {
				copy[key] = UT.copy(object[key]);
			}
		}
		return copy;
	},
	
	/**
	 * uuid 만들기
	 *
	 * @method generateUUID
	 * @return {string} uuid
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         var uuid = UT.generateUUID();
	 *         ...
	 *     }
	 */
	generateUUID: function() {
		return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
			var r = UT.mathRandom() * 16 | 0;
			var v = (c === "x") ? r : (r & 3 | 8);
			return v.toString(16);
		});
	},
	
	mathRandom: function() {
		var v = null;
		var _crypto = window.crypto || window.msCrypto; // for IE 11
		if(_crypto && _crypto.getRandomValues)
			v = _crypto.getRandomValues(new Uint32Array(1))[0] / Math.pow(2, 32);
		else
			v = Math.random();
		return v;
	},
	
	/**
	 * 팝오버
	 *
	 * @method popover
	 * @param {string} popoverTagName              팝오버로 사용할 tag name
	 * @param {object} element                     element
	 * @param {number} width                       가로크기
	 * @param {number} height                      세로크기
	 * @param {object} [events]                    팝오버에서 발생되는 event의 리스너 목록,
	 * @param {object} [options]                   팝오버 설정 옵션
	 * @param {object} [options.modal=true]        모달 여부
	 * @param {object} [options.resizable=false]   리사이징 여부
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         UT.popover("ep-approval", me, 900, 800, {
	 *             "attached": function(popover, e) {
	 *                 popover.getContent().setParam({
	 *                     task_uuid: param.task_uuid || "",
	 *                     apvl_uuid: param.apvl_uuid || "",
	 *                     apvl_typ_ccd: param.apvl_typ_ccd || ""
	 *                 });
	 *             },
	 *             "saved-approval": function(popover, e) {
	 *                 if (UT.isFunction(savedCallback)) {
	 *                     savedCallback.call(this, e.detail);
	 *                 }
	 *             }
	 *         }, {modal: true});
	 *     }
	 */
	popover: function(popoverTagName, element, width, height, events, options) {
		options = options || {};
		var modal = UT.isBoolean(options.modal) ? options.modal : false; // 기본값: false
		var resizable = UT.isBoolean(options.resizable) ? options.resizable : false;
		var i18nDisabled = UT.isString(options.i18nDisabled) ? options.i18nDisabled : false;
		var destroyOnHided = UT.isBoolean(options.destroyOnHided) ? options.destroyOnHided : false;	// 협력사,품목,결재 등 제외하고 destroy한다.
		//alwaysNew 옵션이 true 이면 sc-component 내부적으로 destroyOnHided 옵션을 true로 지정하게 돼있다
		if(options.alwaysNew === true && !UT.isBoolean(options.destroyOnHided)){
			destroyOnHided = undefined;
		}
		var alwaysNew =  UT.isBoolean(options.alwaysNew) ? options.alwaysNew : false; //항상 새로 생성
		
		var popover = SCPopoverManager.popover(popoverTagName, element, width, height, {
			modal : modal,
			closable : false,
			maximizable : false,
			draggable : false,
			resizable : resizable,
			collapsible : false,
			i18nDisabled : i18nDisabled,
			resizeOnBrowserResize : false, //브라우저 리사이즈시 팝오버 리사이즈
			refitOnBrowserResize : false, //브라우저 리사이즈시 팝오버 위치 변경
			destroyOnHided: destroyOnHided,
			alwaysNew: alwaysNew
		});
		
		popover.classList.add(popoverTagName);
		
		var content = popover.getWindowContent();
		
		var fn = {};
		if (UT.isObject(events)) {
			for (var eventName in events) {
				if (UT.isFunction(events[eventName])) {
					fn[eventName] = events[eventName].bind(null, popover);
					content.addEventListener(eventName, fn[eventName]);
				}
			}
		}
		
		//팝오버이 숨겨질 때 Reset을 호출 , removeEvent호출
		var scWindowHidedHandler = function(e) {
			if(e.target.__isDestroying__){
				return;
			}
			if(content.reset)
				content.reset();
			
			if (UT.isObject(events) && UT.isFunction(events["sc-popover-hided"])) {
				// 팝오버 윈도우가 닫힐 때 파라미터 전달용 함수 호출
				if(UT.isFunction(content["onScWindowHided"])) {
					e.detail = content["onScWindowHided"].call(content);
				}
				
				events["sc-popover-hided"].call(this, popover, e);
			}
			popover._removeEvent();
		};
		popover.addEventListener("sc-popover-hided", scWindowHidedHandler);
		
		//팝오버 컨텐츠 리사이즈 - 팝오버내 차트는 별도 리사이즈 해주어야 함
		var resizeHandler = function(e) {
			if(e.target.__isDestroying__){
				return;
			}
			content.fire('content-resize', e);
		};
		popover.addEventListener("interact-resize-end", resizeHandler);
		
		popover._removeEvent = function() {
			
			popover.removeEventListener("sc-popover-hided", scWindowHidedHandler);
			popover.removeEventListener("interact-resize-end", resizeHandler);
			
			if (UT.isObject(events)) {
				for (var eventName in events) {
					if (UT.isFunction(events[eventName])) {
						content.removeEventListener(eventName, fn[eventName]);
					}
				}
			}
		};
		//popover.$_destroy = popover.removeEvent;
		
		return popover;
	},
	
	/**
	 * 팝업
	 * <br>
	 * 팝업의 close 이벤트는 기본적으로 설정된다.
	 *
	 * @method popup
	 * @param {string} popupTagName                팝업으로 사용할 tag name
	 * @param {object} element                     element
	 * @param {number} width                       가로크기
	 * @param {number} height                      세로크기
	 * @param {object} [events]                    팝업에서 발생되는 event의 리스너 목록,
	 * @param {object} [options]                   팝업 설정 옵션
	 * @param {object} [options.modal=true]        모달 여부
	 * @param {object} [options.maximizable=false] 최대화 여부
	 * @param {object} [options.draggable=true]    이동 여부
	 * @param {object} [options.resizable=false]   리사이징 여부
	 * @param {object} [options.collapsible=false] 접기/펴기 여부
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         UT.popup("ep-approval", me, 900, 800, {
	 *             "attached": function(popup, e) {
	 *                 popup.getContent().setParam({
	 *                     task_uuid: param.task_uuid || "",
	 *                     apvl_uuid: param.apvl_uuid || "",
	 *                     apvl_typ_ccd: param.apvl_typ_ccd || ""
	 *                 });
	 *             },
	 *             "saved-approval": function(popup, e) {
	 *                 if (UT.isFunction(savedCallback)) {
	 *                     savedCallback.call(this, e.detail);
	 *                 }
	 *             }
	 *         }, {maximizable: true});
	 *     }
	 */
	popup: function(popupTagName, element, width, height, events, options) {
		options = options || {};
		var modal = UT.isBoolean(options.modal) ? options.modal : true;
		var maximizable = UT.isBoolean(options.maximizable) ? options.maximizable : false;
		var draggable = UT.isBoolean(options.draggable) ? options.draggable : true;
		var resizable = UT.isBoolean(options.resizable) ? options.resizable : true;
		var closable = UT.isBoolean(options.closable) ? options.closable : true;
		var collapsible = UT.isBoolean(options.collapsible) ? options.collapsible : false;
		var titleText = UT.isString(options.titleText) ? options.titleText : "";
		var i18nDisabled = UT.isBoolean(options.i18nDisabled) ? options.i18nDisabled : false;
		var resizeOnBrowserResize = UT.isBoolean(options.resizeOnBrowserResize) ? options.resizeOnBrowserResize : true;
		var refitOnBrowserResize = UT.isBoolean(options.refitOnBrowserResize) ? options.refitOnBrowserResize : true;
		var destroyOnHided = UT.isBoolean(options.destroyOnHided) ? options.destroyOnHided : false;	// 협력사,품목,결재 등 제외하고 destroy한다.
		//alwaysNew 옵션이 true 이면 sc-component 내부적으로 destroyOnHided 옵션을 true로 지정하게 돼있다
		if(options.alwaysNew === true && !UT.isBoolean(options.destroyOnHided)) {
			destroyOnHided = undefined;
		}
		var alwaysNew = UT.isBoolean(options.alwaysNew) ? options.alwaysNew : false; //항상 새로 생성
		
		var popup = SCPopupManager.popup(popupTagName, element, width, height, {
			modal: modal,
			closable: closable,
			maximizable: maximizable,
			draggable: draggable,
			resizable: resizable,
			collapsible: collapsible,
			i18nDisabled: i18nDisabled,
			resizeOnBrowserResize: resizeOnBrowserResize, //브라우저 리사이즈시 팝업 리사이즈
			refitOnBrowserResize: refitOnBrowserResize, //브라우저 리사이즈시 팝업 위치 변경
			destroyOnHided: destroyOnHided,
			alwaysNew: alwaysNew
		});
		
		popup.classList.add(popupTagName);
		
		var content = popup.getWindowContent();
		
		if(UT.isNotEmpty(titleText)) {
			popup.titleText = titleText;
		} else if(UT.isNotEmpty(content.titleText) && UT.isEmpty(popup.titleText)) {
			popup.titleText = content.titleText;
		}
		
		var fn = {};
		if(UT.isObject(events)) {
			for(var eventName in events) {
				if(UT.isFunction(events[eventName]) && eventName !== "close") { //close 이벤트는 별도로 제어
					fn[eventName] = events[eventName].bind(null, popup);
					content.addEventListener(eventName, fn[eventName]);
				}
			}
		}
		
		//close 이벤트가 발생하면 팝업을 닫아줌
		var closeHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			popup.close();
			if(UT.isObject(events) && UT.isFunction(events["close"])) {
				events["close"].call(this, popup, e);
			}
		};
		popup.addEventListener("close", closeHandler);
		
		//팝업이 보여질 때 scroll이 내려가 있는 경우 맨위로 변경
		var scWindowShowedHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			var eles = e.target.querySelectorAll('.flex,.vbox,.hbox,.page,.fit') || [];
			for(var i = 0; i < eles.length; i++) {
				var d = eles[i];
				if(d.scrollTop) {
					d.scrollTop = 0;
				}
			}
		};
		popup.addEventListener("sc-window-showed", scWindowShowedHandler);
		
		//팝업이 숨겨질 때 Reset을 호출 , removeEvent호출
		var scWindowHidedHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			if(content.reset)
				content.reset();
			
			if(UT.isObject(events) && UT.isFunction(events["sc-window-hided"])) {
				// 팝업 윈도우가 닫힐 때 파라미터 전달용 함수 호출
				if(UT.isFunction(content["onScWindowHided"])) {
					e.detail = content["onScWindowHided"].call(content);
				}
				
				events["sc-window-hided"].call(this, popup, e);
			}
			popup._removeEvent();
		};
		popup.addEventListener("sc-window-hided", scWindowHidedHandler);
		
		//팝업 컨텐츠 리사이즈 - 팝업내 차트는 별도 리사이즈 해주어야 함
		var resizeHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			content.fire('content-resize', e);
		};
		popup.addEventListener("interact-resize-end", resizeHandler);
		
		popup._removeEvent = function() {
			
			popup.removeEventListener("close", closeHandler);
			popup.removeEventListener("sc-window-hided", scWindowHidedHandler);
			popup.removeEventListener("sc-window-showed", scWindowShowedHandler);
			popup.removeEventListener("interact-resize-end", resizeHandler);
			
			if(UT.isObject(events)) {
				for(var eventName in events) {
					if(UT.isFunction(events[eventName]) && eventName !== "close") { //close 이벤트는 별도로 제어
						content.removeEventListener(eventName, fn[eventName]);
					}
				}
			}
		};
		//popup.$_destroy = popup.removeEvent;
		
		return popup;
	},
	
	/**
	 * 팝업
	 * <br>
	 * scpopup에 iframe을 이용하여 jsp, html등 파일을 호출한다.
	 * 호출된 jsp에서 팝업창을 제어할 수 있도록 closePopup 펑션 추가
	 *
	 * @method popupJsp
	 * @param {string} url                           호출할 파일 url
	 * @param {object} element                     element
	 * @param {number} width                       가로크기
	 * @param {number} height                      세로크기
	 * @param {object} [events]                    팝업에서 발생되는 event의 리스너 목록,
	 * @param {object} [options]                   팝업 설정 옵션
	 * @param {object} [options.modal=true]        모달 여부
	 * @param {object} [options.maximizable=false] 최대화 여부
	 * @param {object} [options.draggable=true]    이동 여부
	 * @param {object} [options.resizable=false]   리사이징 여부
	 * @param {object} [options.collapsible=false] 접기/펴기 여부
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         UT.popupJsp("test.jsp", me, 900, 800, {maximizable: true});
	 *     }
	 */
	
	popupJsp: function(url, element, width, height, events, options) {
		options = options || {};
		events = events || {};
		var modal = UT.isBoolean(options.modal) ? options.modal : true;
		var maximizable = UT.isBoolean(options.maximizable) ? options.maximizable : false;
		var draggable = UT.isBoolean(options.draggable) ? options.draggable : true;
		var resizable = UT.isBoolean(options.resizable) ? options.resizable : true;
		var closable = UT.isBoolean(options.closable) ? options.closable : true;
		var collapsible = UT.isBoolean(options.collapsible) ? options.collapsible : false;
		var titleText = UT.isString(options.titleText) ? options.titleText : "";
		var footerable = UT.isBoolean(options.footerable) ? options.footerable : false;
		
		var popup = SCPopupManager.create("poupJspIFrame", element, width, height, modal, closable, maximizable, draggable, resizable, collapsible);
		popup.titleText = titleText;
		
		var iframe = document.createElement('iframe');
		iframe.id = "poupJspIFrame";
		iframe.src = encodeURI(url);
		iframe.frameborder = 0;
		iframe.scrolling = UT.isString(options.scrolling) ? options.scrolling : 'yes';
		iframe.width = '100%';
		iframe.height = '100%';
		
		if(footerable) {
			iframe.height = '98%';
			popup._contentElement.appendChild(iframe);
			popup._contentElement.appendChild(options.footertemplate);
		} else {
			popup._contentElement.appendChild(iframe);
		}
		
		popup.show();
		
		var content = iframe.contentWindow;
		iframe.onload = function() {
			iframe.contentWindow.fire = function(event, detail) {
				popup.fire(event, detail);
			};
		};
		
		var fn = {};
		if(UT.isObject(events)) {
			for(var eventName in events) {
				if(UT.isFunction(events[eventName]) && (eventName !== "close" && eventName !== "sc-window-hided")) {
					fn[eventName] = events[eventName].bind(null, popup);
					popup.addEventListener(eventName, fn[eventName]);
				}
			}
		}
		
		var closeHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			popup.close();
			if(UT.isFunction(events["close"])) {
				events["close"].call(this, iframe.contentWindow, e);
			}
		};
		popup.addEventListener("close", closeHandler);
		
		var scWindowHidedHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			if(UT.isObject(events) && UT.isFunction(events["sc-window-hided"])) {
				events["sc-window-hided"].call(this, popup, e);
			}
			popup._removeEvent();
		};
		popup.addEventListener("sc-window-hided", scWindowHidedHandler);
		
		var resizeAndDragStartHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			var contents = document.getElementById("poupJspIFrame");
			contents.style.visibility = "hidden";
		};
		popup.addEventListener("interact-resize-start", resizeAndDragStartHandler);
		popup.addEventListener("interact-drag-start", resizeAndDragStartHandler);
		
		var resizeAndDragEndHandler = function(e) {
			var contents = document.getElementById("poupJspIFrame");
			contents.style.visibility = "visible";
		};
		popup.addEventListener("interact-resize-end", resizeAndDragEndHandler);
		popup.addEventListener("interact-drag-end", resizeAndDragEndHandler);
		
		
		//popup.$_destroy = popup._destroy;
		popup._removeEvent = function() {
			//popup.$_destroy();
			popup.removeEventListener("close", closeHandler);
			popup.removeEventListener("sc-window-hided", scWindowHidedHandler);
			popup.removeEventListener("interact-resize-start", resizeAndDragStartHandler);
			popup.removeEventListener("interact-drag-start", resizeAndDragStartHandler);
			popup.removeEventListener("interact-resize-end", resizeAndDragEndHandler);
			popup.removeEventListener("interact-drag-end", resizeAndDragEndHandler);
			
			if(UT.isObject(events)) {
				for(var eventName in events) {
					if(UT.isFunction(events[eventName]) && eventName !== "close") { //close 이벤트는 별도로 제어
						popup.removeEventListener(eventName, fn[eventName]);
					}
				}
			}
		};
		
		content.focus();
		
		return iframe.contentWindow;
	},
	
	/**
	 * 팝업
	 * <br>
	 * scpopup에 iframe을 이용하여 jsp, html등 파일을 호출한다.
	 * post 방식으로 호출된 jsp에서 팝업창을 제어할 수 있도록 closePopup 펑션 추가
	 *
	 * @method popupJsp
	 * @param {string} url                           호출할 파일 url
	 * @param {}   	   param                       전달받을 Object
	 * @param {object} element                     element
	 * @param {number} width                       가로크기
	 * @param {number} height                      세로크기
	 * @param {object} [events]                    팝업에서 발생되는 event의 리스너 목록,
	 * @param {object} [options]                   팝업 설정 옵션
	 * @param {object} [options.modal=true]        모달 여부
	 * @param {object} [options.maximizable=false] 최대화 여부
	 * @param {object} [options.draggable=true]    이동 여부
	 * @param {object} [options.resizable=false]   리사이징 여부
	 * @param {object} [options.collapsible=false] 접기/펴기 여부
	 */
	popupJspByPost: function(url, param, element, width, height, events, options) {
		options = options || {};
		events = events || {};
		var modal = UT.isBoolean(options.modal) ? options.modal : true;
		var maximizable = UT.isBoolean(options.maximizable) ? options.maximizable : false;
		var draggable = UT.isBoolean(options.draggable) ? options.draggable : true;
		var resizable = UT.isBoolean(options.resizable) ? options.resizable : true;
		var closable = UT.isBoolean(options.closable) ? options.closable : true;
		var collapsible = UT.isBoolean(options.collapsible) ? options.collapsible : false;
		var titleText = UT.isString(options.titleText) ? options.titleText : "";
		
		var popup = SCPopupManager.create("popupJspByPost", element, width, height, modal, closable, maximizable, draggable, resizable, collapsible);
		popup.titleText = titleText;
		
		var iframe = document.createElement('iframe');
		//iframe.src = encodeURI(url);
		iframe.id = 'poupJspIFrame';
		iframe.name = 'poupJspIFrame';
		iframe.frameborder = 0;
		iframe.scrolling = UT.isString(options.scrolling) ? options.scrolling : 'yes';
		iframe.width = '100%';
		iframe.height = '100%';
		
		var form = document.createElement('form');
		form.method = 'POST';
		form.action = url;
		form.target = iframe.name;
		form.style.display = 'none';
		
		var csrfObj = this.csrf();
		csrfObj._csrf = csrfObj.csrf;
		
		//CSRF 
		var input = document.createElement("input");
		input.name = csrfObj.csrfParameter;
		input.value = csrfObj.csrf;
		input.type = "hidden";
		
		form.appendChild(input);
		
		for(var i = 0; i < param.length; i++) {
			var key = Object.keys(param[i]).slice();
			
			for(var j = 0; j < key.length; j++) {
				input = document.createElement('input');
				input.setAttribute('name', key[j]);
				input.setAttribute('value', param[i][key[j]]);
				form.appendChild(input);
			}
		}
		
		popup._contentElement.appendChild(iframe);
		document.body.appendChild(form);
		popup.show();
		form.submit();
		document.body.removeChild(form);
		
		var content = iframe.contentWindow;
		iframe.onload = function() {
			iframe.contentWindow.fire = function(event, detail) {
				popup.fire(event, detail);
			};
		};
		var fn = {};
		if(UT.isObject(events)) {
			for(var eventName in events) {
				if(UT.isFunction(events[eventName]) && eventName !== "close") {
					fn[eventName] = events[eventName].bind(null, popup);
					popup.addEventListener(eventName, fn[eventName]);
				}
			}
		}
		
		var closeHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			popup.close();
			if(UT.isFunction(events["close"])) {
				events["close"].call(this, iframe.contentWindow, e);
			}
		};
		popup.addEventListener("close", closeHandler);
		
		var scWindowHidedHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			if(UT.isObject(events) && UT.isFunction(events["sc-window-hided"])) {
				events["sc-window-hided"].call(this, popup, e);
			}
			popup._removeEvent();
		};
		popup.addEventListener("sc-window-hided", scWindowHidedHandler);
		
		var resizeAndDragStartHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			var contents = document.getElementById("poupJspIFrame");
			contents.style.visibility = "hidden";
		};
		popup.addEventListener("interact-resize-start", resizeAndDragStartHandler);
		popup.addEventListener("interact-drag-start", resizeAndDragStartHandler);
		
		var resizeAndDragEndHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			var contents = document.getElementById("poupJspIFrame");
			contents.style.visibility = "visible";
		};
		popup.addEventListener("interact-resize-end", resizeAndDragEndHandler);
		popup.addEventListener("interact-drag-end", resizeAndDragEndHandler);
		
		content.focus();
		
		//popup.$_destroy = popup._destroy;
		popup._removeEvent = function() {
			//popup.$_destroy();
			popup.removeEventListener("close", closeHandler);
			popup.removeEventListener("sc-window-hided", scWindowHidedHandler);
			popup.removeEventListener("interact-resize-start", resizeAndDragStartHandler);
			popup.removeEventListener("interact-drag-start", resizeAndDragStartHandler);
			popup.removeEventListener("interact-resize-end", resizeAndDragEndHandler);
			popup.removeEventListener("interact-drag-end", resizeAndDragEndHandler);
			
			if(UT.isObject(events)) {
				for(var eventName in events) {
					if(UT.isFunction(events[eventName]) && eventName !== "close") {
						popup.removeEventListener(eventName, fn[eventName]);
					}
				}
			}
		}
		
		return iframe.contentWindow;
	},
	
	/**
	 * 브라우저 팝업
	 * 브라우저 팝업 생성하는 API
	 *
	 * @method popupJsp
	 * @param {string} url                         호출할 url (ex:http://www.emro.co.kr)
	 * @param {string} target                      팝업을 생성할 window (ex:_self, _blank) default: _blank
	 * @param {string} option                      팝업 옵션
	 */
	popupWindow: function(url, target, option) {
		var options = "";
		target = typeof target !== 'undefined' ? target : "_blank";
		
		if(url == "") {
			console.error('url is empty.');
			return;
		}
		
		// 새창 사이즈--> width=100,height=100,scrollbars=no
		if(option != "") options = option + ",";
		
		options = options + "toolbar=no,location=no,menubar=no,status=no";
		
		if(target == "_self")
			window.open(url, target);
		else
			window.open(url, target, options);
	},
	
	/**
	 * 결재 팝업
	 *
	 * @method popupApproval
	 * @param {object}   element                    element
	 * @param {object}   param                      결재 팝업으로 전달할 파라미터
	 * @param {string}   [param.task_uuid]             해당 모듈의 APP 아이디(신규작성시)
	 * @param {string}   [param.apvl_typ_ccd]         결재 유형코드(신규작성시)
	 * @param {string}   [param.apvl_uuid]            결재 아이디
	 * @param {function} [savedCallback(stsCd)]     결재 작성시(resultView=false) 결재정보가 저장된 후 콜백 함수
	 * @param {string}   savedCallback(stsCd).stsCd 결재상태코드
	 * @async
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.popupApproval(me, {apvl_uuid: data.apvl_uuid}, function() {
	 *             ...
	 *         });
	 *     }
	 */
	popupApproval: function(element, param, savedCallback, isViewMode) {
		var loadParam = {
			task_uuid: param.task_uuid || "",
			apvl_uuid: param.apvl_uuid || "",
			apvl_sts_ccd: param.apvl_sts_ccd || "",
			apvl_typ_ccd: param.apvl_typ_ccd || "",
			tmp_param: param.tmp_param || {},
			aprv_incharge_usr_id: param.aprv_incharge_usr_id || "",	//결재라인생성용
			appData: param.appData || {},
			appAmt: param.appAmt,
			is_view_mode: isViewMode === true,
			apvl_tit: UT.isNotEmpty(param.apvl_tit) ? param.apvl_tit : "",
			dpty_aprv_yn: param.dpty_aprv_yn || "",									//대리결재여부
			apvr_id: param.apvr_id || ""										//대리결재 시 결재자 ID
		};
		
		var approvalPopup = {};
		
		var savedApprovalHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			approvalPopup.removeEventListener('saved-approval', savedApprovalHandler);
			approvalPopup.removeEventListener('sc-window-hided', closeHandler);
			if(UT.isFunction(savedCallback)) {
				savedCallback.call(this, e.detail, approvalPopup);
			}
			approvalPopup.getWindowContent().reset();
			approvalPopup.close();
		};
		
		//팝업이 보여질 때 scroll이 내려가 있는 경우 맨위로 변경
		var scWindowShowedHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			var eles = e.target.querySelectorAll('.flex,.vbox,.hbox,.page,.fit') || [];
			
			for(var i = 0; i < eles.length; i++) {
				var d = eles[i];
				if(d.scrollTop) {
					d.scrollTop = 0;
				}
			}
		};
		
		var closeHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			approvalPopup.removeEventListener('saved-approval', savedApprovalHandler);
			approvalPopup.removeEventListener('sc-window-hided', closeHandler);
			// 결재팝업에서 아무 처리 없이 닫기를 했을 때는 savedCallback 을 호출한다.
			// saved-approval 이벤트 발생후 close되는 것과의 구별을 위함. savedCallback 이 두번 호출되는 것 방지
			if(e.detail === "callback" && UT.isFunction(savedCallback)) {
				savedCallback.call(this, "", approvalPopup);
			}
			approvalPopup.getWindowContent().reset();
			approvalPopup.close();
		};
		
		if(!SCLinkManager.isRegistered('ep-approval')) {
			SCLinkManager.register('ui/bp/shared/ep-approval.html');
		}
		
		approvalPopup = SCPopupManager.popup("ep-approval", element, 1100, "95%", {
			modal: true,
			maximizable: true,
			draggable: true,
			//브라우저 리사이즈시 팝업 리사이즈
			resizeOnBrowserResize: true,
			//브라우저 리사이즈시 팝업 위치이동
			refitOnBrowserResize: true
		});
		
		approvalPopup.titleText = "결재";
		
		approvalPopup.addEventListener("_resetAuthCheck", function(e) {
			var authChecker = approvalPopup.getWindowContent().querySelector('cc-auth-checker');
			if(authChecker) {
				authChecker.applyAuthorization();
			}
		});
		
		approvalPopup.addEventListener("saved-approval", savedApprovalHandler);
		approvalPopup.addEventListener("sc-window-hided", closeHandler);
		approvalPopup.addEventListener("sc-window-showed", scWindowShowedHandler);
		
		approvalPopup.show();
		approvalPopup.getWindowContent().load(loadParam);
	},
	
	/**
	 * 첨부파일 업로드 팝업
	 * <br>
	 * 콜백함수로부터 첨부파일 그룹코드와 파일 개수가 전달된다.
	 *
	 * @method popupAttach
	 * @param {object}   element                                 element
	 * @param {string}   grpCd                                   첨부파일 그룹코드
	 * @param {function} [savedCallback(result)]                 파일 저장 후 콜백 함수
	 * @param {object}   savedCallback(result).result            콜백함수로부터 전달 되는 파라미터 - (예) {grp_cd: "xxx", file_count: 2}
	 * @param {string}   savedCallback(result).result.grp_cd     첨부파일 그룹코드
	 * @param {number}   savedCallback(result).result.file_count 업로드된 파일 총 수
	 * @param {object}   [options]                               업로더 설정 옵션
	 * @param {number}   [options.maxTotalFileCount=0]           객최대 파일 개수, 0 : 제한없음.
	 * @param {string}   [options.maxTotalFileSize=0]            업로드 될 파일의 총 용량. 단위 - B(byte), KB(kilobyte), MB(megabyte), GB(gigabyte)
	 * @async
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         me.gridView.onDataCellClicked = function(grid, cell) {
	 *             var fieldName = cell.fieldName;
	 *             if(fieldName == "field1"){
	 *                 UT.popupAttach(me, me.data.field2, function(result) {
	 *                     grid.setValues(cell.itemIndex, {
	 *                         field2: result.grp_cd,
	 *                         field1: result.file_count
	 *                     }, true);
	 *                 });
	 *             }
	 *         };
	 *     }
	 */
	popupAttach: function(element, athg_uuid, savedCallback, options) {
		var param = {
			athg_uuid: athg_uuid || "",
			options: options
		};
		var titleText = UT.isString(options.titleText) ? options.titleText : "";
		
		var attachPopup = {};
		
		var savedAttachHandler = function(e) {
			if(e.target.__isDestroying__) {
				return;
			}
			attachPopup.removeEventListener("saved-attach", savedAttachHandler);
			attachPopup.removeEventListener("sc-window-hided", closeHandler);
			if(UT.isFunction(savedCallback)) {
				savedCallback.call(this, e.detail);
			}
			attachPopup.getWindowContent().reset();
			attachPopup.close();
		};
		
		var closeHandler = function() {
			attachPopup.removeEventListener("saved-attach", savedAttachHandler);
			attachPopup.removeEventListener("sc-window-hided", closeHandler);
			attachPopup.getWindowContent().reset();
			attachPopup.close();
		};
		
		if(!SCLinkManager.isRegistered('ep-attach')) {
			SCLinkManager.register('ui/bp/shared/ep-attach.html');
		}
		
		attachPopup = SCPopupManager.popup("ep-attach", element, 800, 400, {
			modal: true,
			maximizable: true
		});
		
		var content = attachPopup.getWindowContent();
		
		if(UT.isNotEmpty(titleText)) {
			attachPopup.titleText = titleText;
		} else if(UT.isNotEmpty(content.titleText) && UT.isEmpty(attachPopup.titleText)) {
			attachPopup.titleText = content.titleText;
		}
		
		attachPopup.addEventListener("saved-attach", savedAttachHandler);
		attachPopup.addEventListener("sc-window-hided", closeHandler);
		
		attachPopup.show();
		attachPopup.getWindowContent().load(param);
	},


	popupModuleAttach: function(element, moduleCd, savedCallback, options) {
		var param = {
			modl_athf_cd: moduleCd || "",
			options: options
		};
		var titleText = UT.isString(options.titleText) ? options.titleText : "";

		var attachPopup = {};

		var savedAttachHandler = function(e){
			if(e.target.__isDestroying__){
				return;
			}
			attachPopup.removeEventListener("saved-attach", savedAttachHandler);
			attachPopup.removeEventListener("sc-window-hided", closeHandler);
			if (UT.isFunction(savedCallback)) {
				savedCallback.call(this, e.detail);
			}
			attachPopup.getWindowContent().reset();
			attachPopup.close();
		};

		var closeHandler = function(){
			attachPopup.removeEventListener("saved-attach", savedAttachHandler);
			attachPopup.removeEventListener("sc-window-hided", closeHandler);
			attachPopup.getWindowContent().reset();
			attachPopup.close();
		};

		if(!SCLinkManager.isRegistered('ep-module-attach')) {
			SCLinkManager.register('ui/bp/shared/ep-module-attach.html');
		}

		attachPopup = SCPopupManager.popup("ep-module-attach", element, 800, 400,{
			modal: true,
			maximizable: true
		});

		var content = attachPopup.getWindowContent();

		if(UT.isNotEmpty(titleText)){
			attachPopup.titleText = titleText;
		}
		else if(UT.isNotEmpty(content.titleText) && UT.isEmpty(attachPopup.titleText)){
			attachPopup.titleText = content.titleText;
		}

		attachPopup.addEventListener("saved-attach", savedAttachHandler);
		attachPopup.addEventListener("sc-window-hided", closeHandler);

		attachPopup.show();
		attachPopup.getWindowContent().load(param);
	},
	
	/**
	 * 이미지 뷰어를 불러온다.
	 * @param {Object} param 이미지 뷰어를 불러올 때 설정
	 * @param {Array} param.items 이미지 정보를 담고 있는 배열
	 * @param {Number} param.index 초기 선택 인덱스
	 */
	showImageViewerPopup: function(params) {
		// overlay와 imageviewer를 생성하고 기초 설정을 수행
		var overlay = document.createElement('sc-overlay');
		var imageViewer = document.createElement('sc-image-viewer');
		imageViewer.setAttribute('allow-zoom', true);
		imageViewer.setAttribute('show-controller', true);
		imageViewer.setAttribute('info-field', 'desc');
		imageViewer.setAttribute('thumbnail-size', (params.thumbnailSize) ? params.thumbnailSize : 50);
		overlay.classList.add('imageviewer-overlay');
		
		// 빈영역 클릭시 삭제하는 동작 수행
		var closeButtonEl = document.createElement('button');
		closeButtonEl.classList.add('image-viewer-close-button');
		closeButtonEl.innerHTML = 'X';
		var closeTargetEl = (params.useCloseButton) ? closeButtonEl : overlay;
		var clickHandler = function(event) {
			var targetEl = event.currentTarget;
			if(targetEl.className === 'image-viewer-close-button') targetEl = targetEl.parentElement;
			targetEl.close();
			targetEl.removeEventListener('click', clickHandler);
			var viewer = targetEl.querySelector('sc-image-viewer');
			viewer.destroy();
			Polymer.dom(document.body).removeChild(targetEl);
			targetEl.destroy();
		};
		closeTargetEl.addEventListener('click', clickHandler);
		
		// 보여줄 데이터 초기화
		overlay.appendChild(imageViewer);
		if(params.useCloseButton) {
			overlay.appendChild(closeButtonEl);
		}
		Polymer.dom(document.body).appendChild(overlay);
		imageViewer.initData(params.items, params.index);
		
		// 보여주기
		overlay.show();
		
		return overlay;
	},
	
	/**
	 * 이미지 뷰어를 제거한다.
	 * @param {Element} popup 제거하려는 팝업 엘리먼트
	 */
	hideImageViewerPopup: function(popup) {
		Polymer.dom(document.body).removeChild(popup);
	},
	
	/**
	 * date를 format의 형태로 출력한다.
	 *
	 * @method  formatDate
	 * @param   {date|number} date 날짜 객체 | 날짜의 time값
	 * @param   {string} [format=DEF.styles.formatDate.datetimeFormat] 변환할 format
	 * @return  {string} 변환된 문자열
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.formatDate(new Date(), "yyyy/MM/dd"); // 2016/04/20
	 *     }
	 */
	formatDate: function(date, format) {
		if(UT.isNumber(date)) {
			date = new Date(date);
		}
		if(!(SCUtil.isDate(date))) {
			return date;
		}
		var me = date;
		
		if(format == null) {
			return SCFormatter.format('date', date);
		} else {
			return format.replace(/(yyyy|yy|MM|M|dd|d|HH|H|mm|m|ss|s)/g, function(match) {
				switch(match) {
					case "yyyy":
						return me.getFullYear();
					case "yy":
						var v = me.getFullYear() % 100;
						return v > 10 ? v : "0" + v;
					case "MM":
					case "M":
						var v = me.getMonth() + 1;
						return match.length === 1 || v >= 10 ? v : "0" + v;
					case "dd":
					case "d":
						var v = me.getDate();
						return match.length === 1 || v >= 10 ? v : "0" + v;
					case "HH":
					case "H":
						var v = me.getHours();
						return match.length === 1 || v >= 10 ? v : "0" + v;
					case "mm":
					case "m":
						var v = me.getMinutes();
						return match.length === 1 || v >= 10 ? v : "0" + v;
					case "ss":
					case "s":
						var v = me.getSeconds();
						return match.length === 1 || v >= 10 ? v : "0" + v;
					default:
						return match;
				}
			});
		}
	},
	
	/**
	 * string에 values를 replace 한다
	 *
	 * @method formatString
	 * @param  {string}       string 대상 문자열
	 * @param  {string|array} values 바꿀 문자열
	 * @return {string}              replace 된 문자열
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.formatString("{0} / {1}", "a", "b");                       // a / b
	 *         UT.formatString("{0} / {1}", ["a", "b"]);                     // a / b
	 *         UT.formatString("{name} / {value}", {name: "a", value: "b"}); // a / b
	 *     }
	 */
	formatString: function(string, values) {
		string = I18N.translate(string); //formatString의 key값을 translate
		values = UT.isObject(values) ? values : Array.prototype.slice.call(arguments, 1);
		return string.replace(/\{([^{}]+)\}/gm, function(matched, key) {
			return typeof values[key] === "undefined" ? matched : values[key];
		});
	},
	
	/**
	 * 숫자형으로.
	 *
	 * @method toNumber
	 * @param  {string} s
	 * @return {number} n : param 이 숫자형이 아니면 0
	 */
	toNumber: function(s) {
		if(UT.isString(s)) {
			s = s.replace(/[\,]/g, "");
		}
		return isNaN(s) ? 0 : Number(s);
	},
	
	/**
	 * 숫자인 경우 문자열로 변환
	 *
	 * @private
	 * @method toString
	 * @param  {object} v
	 * @return {string}
	 */
	toString: function(v) {
		if(UT.isNumber(v)) {
			return v.toString();
		}
		return v;
	},
	
	/**
	 * 날짜의 time 값으로 변환
	 *
	 * @private
	 * @method toTime
	 * @param  {object} v
	 * @return {number}
	 */
	toTime: function(v) {
		if(UT.isDate(v)) {
			return v.getTime();
		}
		return null;
	},
	
	passwordRules: function() {
		var rules = SCSessionManager.getPasswordRules();
		var alphabetRegExp = '(?=.*?[a-z])';
		var digitRegExp = '(?=.*?[0-9])';
		var specialRegExp = '(?=.*[ !"#$%&\'()*+,-./:;<=>?@\[\\\]^_`{|}~])';
		var letterRegExp = '';
		if(rules.alphabetCharacterRule > 0) {
			letterRegExp += alphabetRegExp;
		}
		if(rules.digitCharacterRule > 0) {
			letterRegExp += digitRegExp;
		}
		if(rules.specialCharacterRule > 0) {
			letterRegExp += specialRegExp
		}
		return {
			lengthRe: new RegExp("^.{" + rules.minLengthRule + "," + rules.maxLengthRule + "}$"),	//길이 8~20자
			letterRe: new RegExp("^" + letterRegExp, "i"), //영문, 특수문자, 숫자 포함 여부
			repeatRe: new RegExp("(.)\\1{" + (rules.repeatCharacterRule - 1) + ",}")	// 4번 이상연속된 문자
		};
	},
	
	
	/**
	 * 패스워드 정규표현식 반환
	 *
	 * @private
	 * @method passwordRegExp
	 * @return {object}
	 */
	passwordRegExp: function() {
		var regExp = {};
		
		regExp.lengthRe = new RegExp("^.{8,20}$");	//길이 8~20자
		regExp.letterRe = new RegExp("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-])", "i"); //영문, 특수문자, 숫자 포함 여부
		regExp.repeatRe = new RegExp("(.)\\1{3,}");	// 4번 이상연속된 문자
		
		return regExp;
	},
	
	/**
	 * 이메일 정규표현식 반환
	 *
	 * @private
	 * @method emailRegExp
	 * @return {object}
	 */
	emailRegExp: function() {
		var regExp = new RegExp("^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$", "i");	//길이 8~20자
		return regExp;
	},
	
	/**
	 * format 형태의 string을 date 로 변환한다.
	 *
	 * @method toDate
	 * @param  {string} str 날짜 string
	 * @param  {string} [format=DEF.styles.formatDate.datetimeFormat] str의 format
	 * @return {date} 변환된 date
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         UT.toDate("20160420", "yyyyMMdd"); // 2016/04/20
	 *     }
	 */
	toDate: function(str, format) {
		format = format || "yyyyMMdd";
		
		if(UT.isDate(str)) {
			return str;
		}
		if(UT.isString(str)) {
			str = str.replace(/\//g, "");
		}
		if(!UT.isString(str) || str.length !== format.length) {
			return null;
		}
		
		var y = 0, m = 0, d = 0, h = 0, mi = 0, s = 0;
		format.replace(/(yyyy|yy|MM|M|dd|d|HH|H|mm|m|ss|s)/g, function(match) {
			var offset = arguments[arguments.length - 2];
			switch(match) {
				case "yyyy":
				case "yy":
					y = parseInt(str.substring(offset, offset + match.length), 10);
					break;
				case "MM":
				case "M":
					m = parseInt(str.substring(offset, offset + match.length), 10) - 1;
					break;
				case "dd":
				case "d":
					d = parseInt(str.substring(offset, offset + match.length), 10);
					break;
				case "HH":
				case "H":
					h = parseInt(str.substring(offset, offset + match.length), 10);
					break;
				case "mm":
				case "m":
					mi = parseInt(str.substring(offset, offset + match.length), 10);
					break;
				case "ss":
				case "s":
					s = parseInt(str.substring(offset, offset + match.length), 10);
					break;
				default:
					break;
			}
		});
		return new Date(y, m, d, h, mi, s);
	},
	
	/**
	 * Array 여부
	 *
	 * @method isArray
	 * @param  {object}  object
	 * @return {boolean} true is array
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (UT.isArray(dataRows)) {
	 *             ...
	 *         }
	 *     }
	 */
	isArray: function(object) {
		return Array.isArray(object);
		// return Object.prototype.toString.call(object) === "[object Array]";
	},
	
	/**
	 * String 여부
	 *
	 * @method isString
	 * @param  {object}  object
	 * @return {boolean} true is string
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (UT.isString(usrId)) {
	 *             ...
	 *         }
	 *     }
	 */
	isString: function(object) {
		return "string" === typeof object;
	},
	
	/**
	 * Boolean 여부
	 *
	 * @method isBoolean
	 * @param  {object}  object
	 * @return {boolean} true is boolean
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         me.set("singleSelect", UT.isBoolean(options.singleSelect) ? options.singleSelect : false);
	 *     }
	 */
	isBoolean: function(object) {
		return "boolean" === typeof object;
	},
	
	/**
	 * Number 여부
	 *
	 * @method isNumber
	 * @param  {object}  object
	 * @return {boolean} true is number
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (UT.isNumber(count)) {
	 *             ...
	 *         }
	 *     }
	 */
	isNumber: function(object) {
		return "number" === typeof object;
	},
	
	/**
	 * Function 여부
	 *
	 * @method isFunction
	 * @param  {object}  object
	 * @return {boolean} true is function
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         if (UT.isFunction(callback)) {
	 *             callback.call(this);
	 *         }
	 *     }
	 */
	isFunction: function(object) {
		return "function" === typeof object;
	},
	
	/**
	 * Object 여부
	 * <br>
	 * (주의: null 도 object이지만, 편의상 null 은 object에서 제외 한다)
	 *
	 * @method isObject
	 * @param  {object}  object
	 * @return {boolean} true is object
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (UT.isObject(options.defaultParam)) {
	 *             me.set("findList.param", options.defaultParam);
	 *         }
	 *     }
	 */
	isObject: function(object) {
		return object !== null && "object" === typeof object;
	},
	
	/**
	 * Date 여부
	 *
	 * @method isDate
	 * @param  {object}  object
	 * @return {boolean} true is date
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (UT.isDate(param)) {
	 *             ...
	 *         }
	 *     }
	 */
	isDate: function(object) {
		return UT.isObject(object) && typeof object.getTime === "function";
	},
	
	/**
	 * 데이터의 empty 여부
	 * <br>
	 * string일 경우 trim 후 empty 검사한다
	 *
	 * @method isEmpty
	 * @param  {string|array} object
	 * @return {boolean}      true is empty
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *         ...
	 *         if (!UT.isEmpty(item)) {
	 *             ...
	 *         }
	 *     }
	 */
	isEmpty: function(object) {
		return object === null || "undefined" === typeof object || (UT.isObject(object) && !Object.keys(object).length && !UT.isDate(object)) || (UT.isString(object) && object.trim() === "") || (UT.isArray(object) && object.length === 0);
	},
	
	/**
	 * !UT.isEmpty(object)
	 *
	 * @method isNotEmpty
	 * @param  {string|array} object
	 * @return {boolean}      true is not empty
	 */
	isNotEmpty: function(object) {
		return !UT.isEmpty(object);
	},
	
	/**
	 * input element에서 enter key가 눌렸을 때의 이벤트
	 *
	 * @method enter
	 * @param {object} element
	 * @param {function} callback enter key가 눌린 후 콜백 함수
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	enter: function(element, callback) {
		$(element).find("input").keypress(function(key) {
			if(key.keyCode === 13) {
				if(UT.isFunction(callback)) {
					callback.call(this);
				}
			}
		});
	},
	
	/**
	 * 부모를 탐색하며 selector와 동일한 엘리먼트를 찾아줌
	 *
	 * @method closest
	 * @param {object} element
	 * @param {string} selector
	 * @return {object}
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	closest: function(element, selector) {
		while(element) {
			if(Polymer.DomApi.matchesSelector.call(element, selector)) {
				return element;
			} else {
				element = element.parentElement;
			}
		}
	},
	
	/**
	 * 현재 화면의 MenuId를 return
	 *
	 * @method getMenuId
	 * @param {object} element
	 * @return {string} menuId
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	getMenuId: function(element) {
		return SCMdiManager.getCurrentMenuId(element);
	},
	
	/**
	 * 현재 화면의 authList를 return
	 *
	 * @method getAuthList
	 * @param {object} element
	 * @return {array} authList
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	getAuthList: function(element) {
		var menuId = UT.getMenuId(element);
		if(menuId) {
			return SCRoleManager.getUserFuncs(menuId);
		}
	},
	
	/**
	 * Array 에 특정  필드에 특정 값이 필터값과 같으면 Array를 재생성함.
	 * @method : arrayFilterChange
	 * @param : {Array} array
	 * @param : {Object} filter : {key : filter처리할키  , value : 값}
	 * @example
	 *             UT.arrayFilterChange(me.codes.prcStlTypCd,{key : "cstr_cnd_val",value : me.rfxData.rfx_typ_ccd});
	 *
	 */
	arrayFilterChange: function(array, filter) {
		//배열에서 자료추출
		if(UT.isArray(array)) {
			var datas = array.filter(function(element) {
				if(element[filter.key] === filter.value) {
					return true;
				}
			});
			return datas;
		} else {
			return array;
		}
	},
	getArrayValuesByKey: function(array, searchKey) {
		var values = [];
		if(UT.isArray(array)) {
			array.forEach(function(data) {
				var val = data[searchKey];
				if(UT.isNotEmpty(val) && values.indexOf(val) === -1) {
					values.push(data[searchKey]);
				}
			});
		}
		return values;
	},
	getHour: function() {
		var array = [];
		for(var i = 0; i < 24; i++) {
			var label = (i < 10) ? ("0" + i) : i;
			array.push({data: i, label: label});
		}
		return array;
	},
	getMin: function() {
		var array = [];
		for(var i = 0; i < 60; i++) {
			var label = (i < 10) ? ("0" + i) : i;
			array.push({data: i, label: label});
		}
		return array;
	},
	/**
	 * TODO : 날짜,시간,분이 있는 component의 값을 date로
	 * dt.hour.min이 있는 data의 경우 dt로 date를 만들어서 셋팅
	 *
	 * ynkim
	 */
	convertDayHourMinToDt: function(obj) {
		for(var key in obj) {
			if(obj.hasOwnProperty(key) && /_dttm$/.test(key)) {
				var val = obj[key];
				if(Date.parse(val)) {
					var day  = obj[key],
						hour = obj[key + "_hour"],
						min  = obj[key + "_min"];
					
					if((UT.isDate(day) || !UT.isEmpty(day)) && !UT.isObject(hour) && !UT.isObject(min)) {
						var date = new Date(day);
						date.setHours(!!hour ? hour : 0, !!min ? min : 0, 0, 0);
						obj[key] = date;
					}
				}
			}
		}
		return obj;
	},
	/**
	 * TODO: 추가개발필요 )))) date의 값을 날짜,시간,분이 있는 값으로 셋팅
	 * data에 dt가 있는  데이타의 경우 dt,hour,min data를 만들어서 셋팅
	 *
	 * ynkim
	 */
	convertDtToDayHourMin: function(obj) {
		for(var key in obj) {
			if(obj.hasOwnProperty(key) && /_dttm$/.test(key)) {
				var val = obj[key];
				if(Date.parse(val)) {
					var newKey = key + "_dttm";
					obj[newKey] = val;
					newKey = key + "_hour";
					obj[newKey] = val.getHours();
					newKey = key + "_min";
					obj[newKey] = val.getMinutes();
				}
			}
		}
		return obj;
	},
	
	//트리 변환 메서드
	listToTree: function(arrayList, idKey, parentIdKey) {
		var rootNodes = [];
		var traverse = function(nodes, item, index) {
			if(nodes instanceof Array) {
				return nodes.some(function(node) {
					if(node[idKey] === item[parentIdKey]) {
						node.children = node.children || [];
						return node.children.push(arrayList.splice(index, 1)[0]);
					}
					
					return traverse(node.children, item, index);
				});
			}
		};
		
		while(arrayList.length > 0) {
			arrayList.some(function(item, index) {
				if(item[parentIdKey] === "ROOT") {
					return rootNodes.push(arrayList.splice(index, 1)[0]);
				}
				
				return traverse(rootNodes, item, index);
			});
		}
		
		return rootNodes;
	},
	
	/**
	 * 협력사 profile 팝업 호출
	 * @param element
	 * @param param
	 * @param savedCallback
	 */
	popupVendor: function(element, param, savedCallback) {
		var vendorPopup = {};
		
		var completeSaveHandler = function(e) {
			if(UT.isFunction(savedCallback)) {
				savedCallback.call(this, e.detail);
			}
		};
		
		var closeHandler = function(e) {
			vendorPopup.removeEventListener("complete-save", completeSaveHandler);
			vendorPopup.removeEventListener("sc-window-hided", closeHandler);
			vendorPopup.getWindowContent().reset();
			vendorPopup.close();
		};
		
		if(!SCLinkManager.isRegistered('ep-vendor-profile-tab')) {
			SCLinkManager.register('ui/bp/vendorMaster/vendorInfo/ep-vendor-profile-tab.html');
		}
		
		var locale = SCLocaleManager.getLocale();
		var titleText= "Vendor Profile" + ((locale != 'ko_KR' && UT.isNotEmpty(param.vd_nm_en)) ? " - " + param.vd_nm_en : " - " + param.vd_nm);
		vendorPopup = SCPopupManager.popup("ep-vendor-profile-tab", element, "90%", "90%", {
			modal: true,
			maximizable: true,
			//브라우저 리사이즈시 팝업 리사이즈
			resizeOnBrowserResize: true,
			//브라우저 리사이즈시 팝업 위치이동
			refitOnBrowserResize: true,
			titleText: titleText,
			i18nDisabled: true
		});
		
		vendorPopup.addEventListener("complete-save", completeSaveHandler);
		vendorPopup.addEventListener("sc-window-hided", closeHandler);
		
		vendorPopup.show();
		vendorPopup.getWindowContent().load(param);
	},
	
	/**
	 * 품목 검색 팝업 호출
	 * @param element
	 * @param param
	 * @param selectedCallback
	 */
	popupItemSearch: function(element, param, selectedCallback) {
		var itemPopup = {};
		
		var selectedItemsHandler = function(e) {
			param = param || {};
			var singleSelect = UT.isBoolean(param.singleSelect) ? param.singleSelect : false;
			if(UT.isFunction(selectedCallback)) {
				selectedCallback.call(this, e.detail);
			}
			if(singleSelect) {
				itemPopup.removeEventListener("selected-items", selectedItemsHandler);
				itemPopup.removeEventListener("sc-window-hided", closeHandler);
				itemPopup.getWindowContent().reset();
				itemPopup.close();
			}
		};
		
		var closeHandler = function() {
			itemPopup.removeEventListener("selected-items", selectedItemsHandler);
			itemPopup.removeEventListener("sc-window-hided", closeHandler);
			itemPopup.getWindowContent().reset();
			itemPopup.close();
		};
		
		if(!SCLinkManager.isRegistered('ep-item-search-list')) {
			SCLinkManager.register('ui/bp/pro/shared/ep-item-search-list.html');
		}
		
		itemPopup = SCPopupManager.popup("ep-item-search-list", element, "80%", "95%", {
			modal: true,
			maximizable: true,
			//브라우저 리사이즈시 팝업 리사이즈
			resizeOnBrowserResize: true,
			//브라우저 리사이즈시 팝업 위치이동
			refitOnBrowserResize: true,
			//팝업타이틀
			titleText: "품목 검색",
			draggable: true
		});
		
		itemPopup.addEventListener("selected-items", selectedItemsHandler);
		itemPopup.addEventListener("sc-window-hided", closeHandler);
		
		itemPopup.show();
		itemPopup.getWindowContent().load(param);
	},
	
	popupItemSearchForRfx: function(element, param, selectedCallback) {
		var itemPopup = {};
		
		var selectedItemsHandler = function(e) {
			param = param || {};
			var singleSelect = UT.isBoolean(param.singleSelect) ? param.singleSelect : false;
			if(UT.isFunction(selectedCallback)) {
				selectedCallback.call(this, e.detail);
			}
			if(singleSelect) {
				itemPopup.removeEventListener("selected-items", selectedItemsHandler);
				itemPopup.removeEventListener("sc-window-hided", closeHandler);
				itemPopup.getWindowContent().reset();
				itemPopup.close();
			}
		};
		
		var closeHandler = function() {
			itemPopup.removeEventListener("selected-items", selectedItemsHandler);
			itemPopup.removeEventListener("sc-window-hided", closeHandler);
			itemPopup.getWindowContent().reset();
			itemPopup.close();
		};
		
		if(!SCLinkManager.isRegistered('ep-item-search-list-for-rfx')) {
			SCLinkManager.register('ui/bp/rfx/shared/ep-item-search-list-for-rfx.html');
		}
		
		itemPopup = SCPopupManager.popup("ep-item-search-list-for-rfx", element, "95%", "90%", {
			modal: true,
			maximizable: true,
			//브라우저 리사이즈시 팝업 리사이즈
			resizeOnBrowserResize: true,
			//브라우저 리사이즈시 팝업 위치이동
			refitOnBrowserResize: true,
			//팝업타이틀
			titleText: "품목 검색",
			draggable: true
		});
		
		itemPopup.addEventListener("selected-items", selectedItemsHandler);
		itemPopup.addEventListener("sc-window-hided", closeHandler);
		
		itemPopup.show();
		itemPopup.getWindowContent().load(param);
	},
	
	/**
	 * moment.js의 date를 javascript date object로 변환
	 * @param {object} momentDate 변경하려는 momentjs 객체
	 * @returns {Date} 변환된 데이트 객체
	 */
	moment2Date: function(momentDate) {
		
		var targetDate = new Date();
		targetDate.setYear(momentDate.year());
		targetDate.setMonth(momentDate.month());
		targetDate.setDate(momentDate.date());
		targetDate.setHours(momentDate.hour());
		targetDate.setMinutes(momentDate.minute());
		
		return targetDate;
	},
	
	/**
	 * 부동소수 수량의 곱 (소수점 3자리까지 입력가능. 결과는 소수점 6자리까지 나올수 있음)
	 * Round 값을 넣지 않으면 Scale 값은 무시
	 * @param _numberPrc 계산할 숫자1
	 * @param _numberQty 계산할 숫자2
	 * @param Round (ROUND: 반올림, CEIL: 올림, FLOOR: 내림, 입력안함: 전부표시)
	 * @param Scale 소수점 몇째자리에서 처리
	 * @returns {String}
	 */
	toFixedMultiply: function(_numberPrc, _numberQty, Round, Scale) {
		var _divisor = Math.pow(10, 8);
		var sign = 1;
		var _multip, _multipPrc, _multipQty;
		var _idxPrc, _idxQty;
		
		if(Round === null || typeof Round === 'undefined') {
			Round = "NONE";
		}
		if(Scale === null || typeof Scale === 'undefined') {
			Scale = 4;
		}
		
		if(_numberPrc < 0) {
			sign *= -1;
			_multipPrc = (_numberPrc * -1).toString();
		} else {
			_multipPrc = _numberPrc.toString();
		}
		
		if(_numberQty < 0) {
			sign *= -1;
			_multipQty = (_numberQty * -1).toString();
		} else {
			_multipQty = _numberQty.toString();
		}
		
		_idxPrc = _multipPrc.indexOf(".");
		_idxQty = _multipQty.indexOf(".");
		
		if(_idxPrc == -1) {
			_multipPrc = (_multipPrc + "0000");
		} else {
			_multipPrc = (_multipPrc + "0000");
			_multipPrc = _multipPrc.substring(0, _idxPrc) + _multipPrc.substring(_idxPrc + 1, _idxPrc + 5);
		}
		
		if(_idxQty == -1) {
			_multipQty = (_multipQty + "0000");
		} else {
			_multipQty = (_multipQty + "0000");
			_multipQty = _multipQty.substring(0, _idxQty) + _multipQty.substring(_idxQty + 1, _idxQty + 5);
		}
		
		switch(Round) {
			case "ROUND" :
				_multip = ((Number(_multipPrc) * Number(_multipQty) + (5 * Math.pow(10, 8 - Scale))) / _divisor * sign).toString();
				_multip = (_multip.indexOf(".") == -1) ? _multip : _multip.substring(0, _multip.indexOf(".") + Scale);
				break;
			case "CEIL":
				_multip = ((Number(_multipPrc) * Number(_multipQty) + (Math.pow(10, 9 - Scale) - 1)) / _divisor * sign).toString();
				_multip = (_multip.indexOf(".") == -1) ? _multip : _multip.substring(0, _multip.indexOf(".") + Scale);
				break;
			case "FLOOR":
				_multip = ((Number(_multipPrc) * Number(_multipQty)) / _divisor * sign).toString();
				_multip = (_multip.indexOf(".") == -1) ? _multip : _multip.substring(0, _multip.indexOf(".") + Scale);
				break;
			default:
				_multip = ((Number(_multipPrc) * Number(_multipQty)) / _divisor * sign).toString();
				break;
		}
		
		return _multip;
	},
	
	/**
	 * 부동소수 수량의 나눗셈 (소수점 3자리까지 입력가능. 결과가 소수점 무한대로 나올수있으므로 Round 파라미터 입력필요 )
	 * Round 값을 넣지 않으면 Scale 값은 무시
	 * @param _number1 계산할 숫자1
	 * @param _number2 계산할 숫자2
	 * @param Round (ROUND: 반올림, CEIL: 올림, FLOOR: 내림, 입력안함: 전부표시)
	 * @param Scale 소수점 몇째자리에서 처리
	 * @returns {String}
	 */
	toFixedDivide: function(_number1, _number2, Round, Scale) {
		var _multip1 = _number1.toString();
		var _multip2 = _number2.toString();
		var _idx1, _idx2;
		
		if(Round === null || typeof Round === 'undefined') {
			Round = "NONE";
		}
		if(Scale === null || typeof Scale === 'undefined') {
			Scale = 4;
		}
		
		// 소수점 몇째자리까지 있는지 확인
		_idx1 = _multip1.indexOf(".");
		_idx2 = _multip2.indexOf(".");
		
		//정수화된 값
		var _multip1Num = "";
		var _multip2Num = "";
		
		//소수점 길이
		var _multip1Len = 0;
		var _multip2Len = 0;
		if(_idx1 != -1)
			_multip1Len = _multip1.length - _idx1 - 1;
		if(_idx2 != -1)
			_multip2Len = _multip2.length - _idx2 - 1;
		
		// 각 수 정수로 바꾸기
		if(_multip1Len == _multip2Len) {
			// 둘다 정수일때
			if(_multip1Len == 0) {
				_multip1Num = _multip1;
				_multip2Num = _multip2;
			} else {
				_multip1Num = _multip1.substring(0, _idx1) + _multip1.substring(_idx1 + 1, _multip1.length);
				_multip2Num = _multip2.substring(0, _idx2) + _multip2.substring(_idx2 + 1, _multip2.length);
			}
		} else if(_multip1Len > _multip2Len) {
			// 소수점뒤 길이가 긴건 소수점만 없애고 짧은건 짧은 수만큼 0 붙이기
			var powLen = Math.pow(10, _multip1Len - _multip2Len).toString();
			_multip1Num = _multip1.substring(0, _idx1) + _multip1.substring(_idx1 + 1, _multip1.length);
			if(_multip2Len == 0) {
				_multip2Num = _multip2 + powLen.substring(1, powLen.length);
			} else {
				_multip2Num = _multip2.substring(0, _idx2) + _multip2.substring(_idx2 + 1, _multip2.length) + powLen.substring(1, powLen.length);
			}
		} else {
			// 소수점뒤 길이가 긴건 소수점만 없애고 짧은건 짧은 수만큼 0 붙이기
			var powLen = Math.pow(10, _multip2Len - _multip1Len).toString();
			_multip2Num = _multip2.substring(0, _idx2) + _multip2.substring(_idx2 + 1, _multip2.length);
			if(_multip1Len == 0) {
				_multip1Num = _multip1 + powLen.substring(1, powLen.length);
			} else {
				_multip1Num = _multip1.substring(0, _idx1) + _multip1.substring(_idx1 + 1, _multip1.length) + powLen.substring(1, powLen.length);
			}
		}
		
		// 결과값
		var _result = Number(_multip1Num) / Number(_multip2Num);
		
		//반올림 함수 호출부분
		return this.toFixedRound(_result, Round, Scale);
	},
	
	/**
	 * 반올림 올림 내림 함수
	 * @param _number1 계산할 숫자
	 * @param Round (ROUND: 반올림, CEIL: 올림, FLOOR: 내림, 입력안함: 전부표시)
	 * @param Scale 소수점 몇째자리에서 처리
	 * @returns {String}
	 */
	toFixedRound: function(_number1, Round, Scale) {
		
		if(Scale === 0) {
			return null;
		}
		// String 변환
		var _num = _number1.toString();
		
		var sign = "";
		
		if(Round === null || typeof Round === 'undefined') {
			Round = "NONE";
		}
		if(Scale === null || typeof Scale === 'undefined') {
			Scale = 4;
		}
		
		if(_number1 < 0) {
			sign = "-";
			_num = _num.substring(1, _num.length);
		}
		//소수점 위치
		var indexdot = _num.indexOf(".");
		
		// 곱한 10의 n 승
		var muxcnt = _num.length - indexdot - 1;
		
		// 정수가 들어왔거나 소수점처리 안한다면 그냥 리턴
		if(indexdot == -1 || Round == "NONE" || muxcnt < Scale)
			return _number1.toString();
		
		// 소수점 제거된 입력값
		var _numX = Number(_num.substring(0, indexdot) + _num.substring(indexdot + 1, _num.length));
		
		var _numS = "";
		
		switch(Round) {
			case "ROUND" :
				_numS = ((Number(_numX) + (5 * Math.pow(10, muxcnt - Scale))) / Math.pow(10, muxcnt)).toString();
				_numS = (_numS.indexOf(".") == -1) ? _numS : _numS.substring(0, _numS.indexOf(".") + Scale);
				break;
			case "CEIL":
				_numS = ((Number(_numX) + (Math.pow(10, muxcnt + 1 - Scale) - 1)) / Math.pow(10, muxcnt)).toString();
				_numS = (_numS.indexOf(".") == -1) ? _numS : _numS.substring(0, _numS.indexOf(".") + Scale);
				break;
			case "FLOOR":
				_numS = (Number(_numX) / Math.pow(10, muxcnt)).toString();
				_numS = (_numS.indexOf(".") == -1) ? _numS : _numS.substring(0, _numS.indexOf(".") + Scale);
				break;
			default: //아무것도 아닐 경우
				return _number1.toString();
				break;
		}
		
		return Number(sign + _numS).toString();
	},
	
	/**
	 * 부동소수 수량의 합. (소수점 4째자리까지 입력 가능)
	 * @param _numQtyA 계산할 숫자1
	 * @param _numQtyB 계산할 숫자2
	 * @returns {String}
	 */
	toFixedAdd: function(_numQtyA, _numQtyB) {
		var _divisor = Math.pow(10, 4);
		var _addition;
		
		var _addQtyA = _numQtyA.toString();
		var _addQtyB = _numQtyB.toString();
		
		var _idxQtyA = _addQtyA.indexOf(".");
		var _idxQtyB = _addQtyB.indexOf(".");
		
		if(_idxQtyA == -1)
			_addQtyA = (_addQtyA + "0000");
		else {
			_addQtyA = (_addQtyA + "0000");
			_addQtyA = _addQtyA.substring(0, _idxQtyA) + _addQtyA.substring(_idxQtyA + 1, _idxQtyA + 5);
		}
		
		if(_idxQtyB == -1)
			_addQtyB = (_addQtyB + "0000");
		else {
			_addQtyB = (_addQtyB + "0000");
			_addQtyB = _addQtyB.substring(0, _idxQtyB) + _addQtyB.substring(_idxQtyB + 1, _idxQtyB + 5);
		}
		
		
		_addition = ((Number(_addQtyA) + Number(_addQtyB)) / _divisor).toString();
		
		return _addition;
	},
	
	/**
	 * 부동소수 수량의 차. (소수점 4째자리까지 입력 가능)
	 * @param _numQtyA 계산할 숫자1
	 * @param _numQtyB 계산할 숫자2
	 * @returns {String}
	 */
	toFixedSub: function(_numQtyA, _numQtyB) {
		var _divisor = Math.pow(10, 4);
		var _addition;
		
		var _addQtyA = _numQtyA.toString();
		var _addQtyB = _numQtyB.toString();
		
		var _idxQtyA = _addQtyA.indexOf(".");
		var _idxQtyB = _addQtyB.indexOf(".");
		
		if(_idxQtyA == -1) {
			_addQtyA = (_addQtyA + "0000");
		} else {
			_addQtyA = (_addQtyA + "0000");
			_addQtyA = _addQtyA.substring(0, _idxQtyA) + _addQtyA.substring(_idxQtyA + 1, _idxQtyA + 5);
		}
		
		if(_idxQtyB == -1) {
			_addQtyB = (_addQtyB + "0000");
		} else {
			_addQtyB = (_addQtyB + "0000");
			_addQtyB = _addQtyB.substring(0, _idxQtyB) + _addQtyB.substring(_idxQtyB + 1, _idxQtyB + 5);
		}
		
		_addition = ((Number(_addQtyA) - Number(_addQtyB)) / _divisor).toString();
		
		return _addition;
	},
	
	csrf: function() {
		var doc  = document,
			meta = doc.querySelector('meta[name=_csrf]'), _csrf;
		
		if(meta) {
			_csrf = {
				csrf: meta.content,
				csrfHeader: doc.querySelector('meta[name=_csrf_header]').content,
				csrfParameter: doc.querySelector('meta[name=_csrf_parameter]').content
			}
		}
		return _csrf;
	},
	
	/**
	 * 큰 숫자 정렬을 위한 함수
	 * @param val1
	 * @param val2
	 * @returns val1가 크면 1, 같으면 0, 작으면 -1
	 */
	sortBigNumber: function(val1, val2) {
		if(val1 === undefined || val1 === null) {
			return (val2 === undefined || val2 === null) ? 0 : -1;
		}
		if(val2 === undefined || val2 === null) {
			return 1;
		}
		
		var bigVal1 = new BigNumber(val1);
		var bigVal2 = new BigNumber(val2);
		
		return bigVal1.comparedTo(bigVal2);
	},
	
	/**
	 * 큰 숫자 합계를 위한 함수
	 * @param dataField
	 * @param datas
	 * @returns val1가 크면 1, 같으면 0, 작으면 -1
	 */
	plusBigNumber: function(dataField, datas) {
		var result = new BigNumber('0');
		
		for(var i = 0; i < datas.length; ++i) {
			result = result.plus(new BigNumber(datas[i][dataField] || 0));
		}
		return result.toFixed();
	},
	plusPrecBigNumber: function(dataField, datas) {
		var result = new BigNumber('0');
		var amtFormatType = this.amtFormatType;
		
		for(var i = 0; i < datas.length; ++i) {
			result = result.plus(new BigNumber(datas[i][dataField] || 0));
		}
		return SCFormatter.format("prec_" + amtFormatType, result);
	},
	templateColumns: function(scope, templateId, columnsId) {
		var me       = scope, columnsCt, columns,
			template = me.querySelector(templateId);
		
		me.templatize(template);
		instance = me.stamp(null);
		me.appendChild(instance.root);
		columnsCt = me.querySelector(columnsId);
		columns = Array.prototype.slice.call(columnsCt.children);
		
		me.removeChild(columnsCt);
		return columns;
	},
	
	/**
	 * keydown event 입력 key의 특수키 여부(일반 문자 입력 제외) 반환
	 *
	 * key codes
	 * Tab:9, Enter:13, Shift:16, Ctrl:17, Alt:18, Pause break:19, Caps Lock: 20,
	 * 한/영(Alt(R)):21, 한자(Ctrl(R)):25, ESC:27, Page Up:33, Page Down:34, End:35, Home:36,
	 * ←:37, ↑:38, →:39, ↓:40, Insert:45, window(L):91, window(R):92, Fn:93,
	 * F1 :112, F2:113, F3:114, F4:115, F5:116, F6:117, F7:118, F8:119, F9:120, F10:121, F11:122, F12:123,
	 * Num Lock:144, Scroll Lock:145
	 *
	 * @method isSpecialKeyDown
	 * @param  {object} event      이벤트 객체
	 * @return {boolean}      true is special key down
	 */
	isSpecialKeyDown: function(event) {
		var keyCode = event.keyCode ? event.keyCode : event.which;
		var specialKeyCodes = [
			9, 13, 16, 17, 18, 19, 20, 25, 27, 33, 34, 35, 36, 37, 38, 39, 40, 45,
			91, 92, 93, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 144, 145
		]; // Back Space(8), Delete(46) 제외
		
		return SCUtil.Array.contains(specialKeyCodes, keyCode);
	},
	
	/**
	 * 암호화된 첨부파일 아이디로 파일 다운로드 하기 위한 함수
	 * @param attId 암호화 된 첨부파일 아이디
	 */
	fileDownloadByAttId: function(attId) {
		var iFrame = document.createElement('iframe');
		iFrame.id = 'sc-download-' + Polymer.SCAutoGenIdBehavior.generateId(true);
		iFrame.style.display = 'none';
		document.body.appendChild(iFrame);
		var iframeDoc = iFrame.contentDocument;
		var form      = iframeDoc.createElement('form'),
			input     = iframeDoc.createElement('input'),
			csrfInput = iframeDoc.createElement('input');
		// 1. 첨부파일 아이디
		input.name = 'id';
		input.value = attId;
		form.appendChild(input);
		
		// 2. CSRF
		var csrfObj = this.csrf();
		csrfObj._csrf = csrfObj.csrf;
		csrfInput.name = csrfObj.csrfParameter;
		csrfInput.value = csrfObj.csrf;
		csrfInput.type = "hidden";
		form.appendChild(csrfInput);
		
		// 3. form 정의
		form.method = 'POST';
		form.action = 'attachment/download.do';
		form.style.display = "none";
		iframeDoc.body.appendChild(form);
		form.submit();
		iframeDoc.body.removeChild(form);
	},
	
	/**
	 * IE 브라우저 && Adobe Reader 설치 여부를 체크
	 *
	 * IE 브라우저 이면서 Adobe Reader 설치 가 안된 경우 false 를 리턴
	 * @return {boolean}
	 */
	checkAdobeReader: function() {
		if(Polymer.isIE) {
			// IE일 경우만 체크
			var getActiveXObject = function(name) {
				try {
					return new ActiveXObject(name);
				} catch(e) {
					console.error(e);
				} // 미존재시 error
			}
			var getPDFPlugin = function() {
				// AcroPDF.PDF 7이상 버전, PDF.PdfCtrl 6이하버전  (load activeX control)
				return getActiveXObject("AcroPDF.PDF") || getActiveXObject("PDF.PdfCtrl");
			}
			return !!getPDFPlugin();
		} else {
			return true;
		}
	},
	
	/**
	 * 코드 명칭 가져오기
	 *
	 * @return {String}
	 */
	findCodeName: function(codeArray, value, displayField, valueField) {
		
		var displayField = UT.isString(displayField) ? displayField : "label",
			valueField   = UT.isString(valueField) ? valueField : "data";
		
		var filter = codeArray.filter(function(item) {
			return item[valueField] === value;
		});
		return filter.length > 0 ? filter[0][displayField] : "";
		
		
	},
	
	/**
	 * UBIFORM Viewer 열기 (출력물 솔루션)
	 * @param projectName	//프로젝트명
	 * @param formName		//문서 아이디 (서식명)
	 * @param datasetList	//dataset
	 * @param url			//UBIFORM Viewer url
	 */
	openUBIFORM: function(projectName, formName, datasetList, url) {
		var projectName = projectName,
			formName    = formName,
			datasetList = datasetList;
		
		
		var _params = {
			projectName: projectName,  // 프로젝트명
			formName: formName, // 문서 아이디 (서식명)
		};
		
		if(datasetList) {
			for(var datasetValue in datasetList) {
				_params[datasetValue] = encodeURIComponent(datasetList[datasetValue]);
			}
		}
		
		var _url = url;
		
		var date = new Date();
		var now = date.getTime();
		var name = "UBF_" + now;
		var windowoption = 'location=0, directories=0, resizeable=0, status=0, toolbar=0, menubar=0, width=1280px, height=650px, left=0, top=0, scrollbars=0';
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", _url);
		
		for(var i in _params) {
			if(_params.hasOwnProperty(i)) {
				var param = document.createElement('input');
				param.type = 'hidden';
				param.name = i;
				param.value = encodeURI(_params[i]);
				form.appendChild(param);
			}
		}
		
		document.body.appendChild(form);
		form.setAttribute("target", name);
		window.open("", name, windowoption);
		form.submit();
		document.body.removeChild(form);
	},
	
	/** Dom Sub Module을 통해, Main Module을 찾는다 */
	getMainModule: function(element) {
		var domName = element.is;
		if(domName.indexOf("em-") == 0) {
			return domName;
		} else if(domName.indexOf("smartsuite-") == 0) {
			var parentDomHost = element.domHost;
			if(!parentDomHost || (parentDomHost.is.indexOf("em-") < 0 && parentDomHost.is.indexOf("smartsuite-") < 0)) {
				return element;
			} else {
				return this.getMainModule(parentDomHost);
			}
		} else {
			if(element.domHost) {
				return this.getMainModule(element.domHost);
			} else {
				return null;
			}
		}
	},
	
	convertBizRegNoFn: function(data, item) {
		var value = data[item.dataField];
		if(!!!value || value.length !== 10) {
			return value;
		} else {
			return value.substr(0, 3) + "-" + value.substr(3, 2) + "-" + value.substr(5, 5);
		}
	},
	
	importLinks: function(linkUrls, callbackFn){
		for(var i=0, linkUrl, total=linkUrls.length; i<total; i++){
			linkUrl = linkUrls[i];
			Polymer.Base.importLink(linkUrl, function(){
				total -= 1;
				if(total == 0){
					callbackFn.call(this);
				}
			}.bind(this));
		}
	}
};//UT end~~~

/**
 * 다국어 변환
 *
 * @class I18N
 */
var I18N = {
	/**
	 * 다국어 처리 instance
	 */
	instance: null,
	
	/**
	 * 다국어 변환
	 *
	 * @private
	 * @method translate
	 * @param  {string}
	 * @return {string}
	 */
	translate: function() {
		var me = this;
		if(me.instance === null) {
			me.instance = new SCI18n();
		}
		return me.instance.translate.apply(this, arguments);
	}
};

/** MDI 종속적인 유틸 펑션 */
MDIUT = {
	
	getMdi: function(doc) {
		var thisDoc = doc || document;
		return thisDoc.querySelector('sc-mdi');
	},
	
	hasMdi: function(doc) {
		var scMdi = this.getMdi();
		if(scMdi) {
			return true;
		} else {
			return false;
		}
	},
	
	goHistory: function(doc) {
		var thisDoc = doc || document; // 추가
		var scMdi = thisDoc.querySelector('sc-mdi');
		if(scMdi && SCMdiHistory.useHistory && SCMdiHistory.getHistorySts()) {
			SCMdiHistory.goHistory();
		}
	},
	
	/**
	 * menuId를 통해 메뉴생성
	 *
	 * @method createWindowByMenuId
	 * @param {string} menuId 메뉴아이디
	 * @param {boolean} 메뉴 재 생성 여부
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	createWindowByMenuId: function(menuId, callback, regeneration) {
		var regeneration = UT.isBoolean(regeneration) ? regeneration : true;
		var menuList = SCMenuManager.getMenuList();
		var windowIndex = SCMdiManager.mdi.$.mdiContent.createdWindows.indexOf(menuId);
		var moduleWindow = null;
		
		if(windowIndex > -1 && regeneration) {
			SCMdiManager.removeWindow(menuId);
		}
		
		for(idx in menuList) {
			if(menuId == menuList[idx].menu_id) {
				SCMdiManager.createWindow(menuList[idx].menu_id, menuList[idx].menu_nm, menuList[idx].menu_url, {callback: callback});
				break;
			}
		}
	},
	
	/**
	 * 메뉴 생성
	 *
	 * @method createWindow
	 * @param {string} menuId    메뉴아이디
	 * @param {string} menuName  메뉴명
	 * @param {string} menuUrl   메뉴URL
	 * @example
	 *     someFunction: function() {
	 *         var me = this;
	 *     }
	 */
	createWindow: function(menuId, menuName, menuUrl, callback) {
		var windowIndex = SCMdiManager.mdi.$.mdiContent.createdWindows.indexOf(menuId);
		var moduleWindow = null;
		
		if(windowIndex > -1) {
			SCMdiManager.removeWindow(menuId);
		}
		SCMdiManager.createWindow(menuId, menuName, menuUrl, {callback: callback});
	},
	
	/**
	 * 메뉴ID로 메뉴 활성화
	 *
	 * @method activateWindowByMenuId
	 * @param {String} menuId
	 * @param {Function} initialized 호출되는 함수, 함수의 인자로 해당 모듈이 설정
	 * @example
	 *     var callbackFn = function(module) {
	 *          ....
	 *     }
	 *     MDIUT.activateMdiWindowByMenuId(menuId, callbackFn);
	 */
	activateMdiWindowByMenuId: function(menuId, callback) {
		var moduleWindow = SCMdiManager.activateWindow(menuId);
		
		if(!moduleWindow) {
			return;
		}
		
		if(moduleWindow.module) {
			callback.call(moduleWindow.module, moduleWindow.module);
		} else {
			var loadedHandler = function(e) {
				callback.call(moduleWindow.module, moduleWindow.module);
				moduleWindow.removeEventListener("window-module-loaded", loadedHandler);
			};
			moduleWindow.addEventListener("window-module-loaded", loadedHandler);
		}
		return moduleWindow;
	},
	
	showMdiCalendar: function() {
		SCMdiManager.createWindow('ADM50000', 'Calendar', 'bower_components/smartsuite/calendar/smartsuite-calendar-mgt.html');
	},
	
	activatedWindow: function() {
		var scMdi = this.getMdi();
		if(scMdi) {
			return scMdi.$.mdiContent.activatedWindow;
		} else {
			return null;
		}
	}
}; //MDIUT end~~~

/**
 * ui 제어 변수
 * */
var docusign_use_yn = "Y";

/**
 * 파일 다운로드 관련
 *
 * @class DL
 */
var DL = {
	loadHandler: function(event) {
		var target = event.target;
		
		if(target.hasAttribute('created')) {
			target.removeAttribute('created');
			return;
		}
		
		SCAlert.show(target.dataset.alertTitleText, target.dataset.downloadErrorText, null, null, false);
		target.removeEventListener('load', DL.loadHandler);
		document.body.removeChild(target);
		iframe = DL.createIframeElement(true);
	},
	
	createIframeElement: function(refreshOp) {
		var iframe = document.createElement('iframe');
		iframe.id = 'sc-download-' + Polymer.SCAutoGenIdBehavior.generateId(true);
		iframe.style.display = 'none';
		if(refreshOp || window.Polymer.isChrome) {
			iframe.setAttribute('created', true);
		}
		iframe.addEventListener('load', DL.loadHandler);
		document.body.appendChild(iframe);
		return iframe;
	},
	
	downloadByFileId: function(encryptId) {
		var iframe = DL.createIframeElement();
		//다운로드 실패시 오류메세지 전달
		iframe.dataset.alertTitleText = "STD.N1000"; //알림
		iframe.dataset.downloadErrorText = "STD.E9999"; //오류가 발생하였습니다.<br/>관리자에게 문의하세요.
		
		var document = iframe.contentDocument;
		var form = document.createElement('form');
		
		//id setting
		var input = document.createElement('input');
		input.name = 'id';
		input.value = encryptId;
		form.appendChild(input);
		
		//csrf setting
		var csrfObj = SCPreloader.csrf();
		csrfObj._csrf = csrfObj.csrf;
		if(csrfObj) {
			input = document.createElement('input');
			input.name = csrfObj.csrfParameter;
			input.value = csrfObj.csrf;
			form.appendChild(input);
		}
		
		form.method = 'POST';
		form.action = 'attachment/download.do';
		form.style.display = 'none';
		
		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	},
	
	/**
	 * 해당 파일 그룹 코드의 파일이 1개일 때 다운로드 가능
	 * 전자계약 PDF 다운로드 / 첨부파일 다운로드
	 */
	downloadByGrpCd: function(grpCd) {
		// list.do 호출 하여 fileId의 암호화 값을 추출
		var scAjax = document.createElement('sc-ajax');
		scAjax.url = 'attachment/list.do';
		scAjax.body = {
			_csrf: SCPreloader.csrf().csrf,
			groupId: grpCd
		}
		scAjax.request().then(function(e) {
			if(e.response.length !== 1) {
				SCAlert.show("STD.N1000", "STD.E9999", null, null, false); //오류가 발생하였습니다.<br/>관리자에게 문의하세요.
				return;
			}
			DL.downloadByFileId(e.response[0].id); // fileId로 download.do 호출
		}.bind(this));
	},
	
	downloadByUrl: function(url, paramObj, iframeId) {
		var id = UT.isEmpty(iframeId) ? "custom-downloader" : iframeId;
		var iframe = document.getElementById(id);
		if(!iframe) {
			iframe = document.createElement("iframe");
			iframe.id = id;
			document.body.appendChild(iframe);
		}
		
		var form = document.createElement('form');
		form.method = 'GET';
		form.action = url;
		form.style.display = 'none';
		
		//csrf setting
		var csrfObj = SCPreloader.csrf();
		csrfObj._csrf = csrfObj.csrf;
		if(csrfObj) {
			var input = document.createElement('input');
			input.name = csrfObj.csrfParameter;
			input.value = csrfObj.csrf;
			input.type = "hidden";
			form.appendChild(input);
		}
		
		//Parameter
		for(var prop in paramObj) {
			var input = document.createElement("input");
			input.name = prop;
			input.value = paramObj[prop];
			input.type = "hidden";
			form.appendChild(input);
		}
		;
		
		var iframeDocument = iframe.contentDocument;
		iframeDocument.body.appendChild(form);
		form.submit();
		iframeDocument.body.removeChild(form);
	}
};

UT.popupMemo = function() {
	var me      = this,
		handler = function() {
			var memoPopup = UT.popup("smartsuite-memo-list", me, 930, 520, {
					"close": function() {
						memoPopup.classList.remove('smartsuite-memo-popup-super');
					}
				},
				{
					title: "SMART MEMO",
					resizable: true,
					collapsible: true,
					modal: false
				});
		
			memoPopup.classList.add('smartsuite-memo-popup-super');
			memoPopup.addEventListener('interact-resize-end', me._updateColumnCount);
		
			memoPopup.show();
			memoPopup.getWindowContent().load();
		};
	
	if(!SCLinkManager.isLoaded('sc-grid')) {
		SCLinkManager.load('sc-grid').then(handler);
	} else {
		handler();
	}
}

UT.showCalendar = function() {
	SCMdiManager.createWindow('CAL50100', 'Calendar', 'bower_components/smartsuite/calendar/smartsuite-calendar-mgt.html');
}

UT.isParentElement = function(candidate, parent) {
	while(candidate.parentElement) {
		if(candidate == parent) {
			return true;
		}
		candidate = candidate.parentElement;
	}
	return false;
}

UT.isNotParentElement = function(parent, child) {
	return !UT.isParentElement(parent, child);
}
