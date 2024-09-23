(function() {
	var debug = Polymer.Settings.debug === true;
	var modules = new Polymer.Collection([ 'em', 'es', 'ep', 'smartsuite' ]);
	
	if(Polymer.Settings.ccModuleBehaviorReady){
		document.dispatchEvent(new Event('ccModuleBehaviorReady'));
	}

	/**
	 * em, es, ep 모듈 lazy mode behavior
	 */
	Polymer.CCModuleBehavior = {

		_isCCModuleBehavior: true, //cc-module-behavior 여부를 확인하기 위한 프로퍼티
		
		//초기화완료
		initialized : function() {
			this.resetOriginalValue();
			if(debug) {
				console.log(this.tagName.toLowerCase() + ' initialized 완료');
			}
			
			// 축약어 설정
			if(UT.multiLocaleCheck()) {
				var targetElement = this.querySelector('cc-page-title-bar');
				
				if(UT.isNotEmpty(targetElement)) {
					targetElement.getAbbrInfo();
				}
			}
		},
		
		//소멸완료
		destroyed : function() {
			if(SCUtil.isFunction(this.destroyProperties)) {
				this.destroyProperties();
			}
			if(debug) {
				console.log(this.tagName.toLowerCase() + ' 소멸 완료.');
			}
		},
		
		_validateItems: [],

		validate : function(vGroupName) {
			var targetEl = this;
			if (SCUtil.isElement(vGroupName)) {
				targetEl = [vGroupName];
			} else if (vGroupName) {
				targetEl = targetEl.querySelectorAll('[validation-group="'
						+ vGroupName + '"]');
			} else{
				targetEl = targetEl.querySelectorAll('[field],sc-grid');
			} 

			if (!targetEl || UT.isEmpty(targetEl)) {
				console.warn("there is no target element to validate");
				return true;
			}
			
			var result = this.validateElements(targetEl); 

			if(this.firstFailedEl != null){
				// 2019.07.04 SMARTNINE-3138 신규업체업체 등록, newVendor.jsp 사용으로 인해 추가.
            	if (MDIUT.getMdi(document)){
            		this.firstFailedEl.scrollIntoView({ block: "center", inline: "nearest" });
            	}
				
				if(document.querySelector("#containerWrap") != null){
					document.querySelector("#containerWrap").scrollTop = 0;
				}
				this.firstFailedEl = null;
			}
			
			//this.viewValidateGroup(this._validateItems);
			return result;
		},
		
		firstFailedEl: null,

		validateElements : function(elements) {
			var validCnt = 0;
			for (var i = 0, len = elements.length, field, element; i < len; i++) {
				element = elements[i];
				//                    if (field.isField || element.isGrid()) {
				if (!UT.closest(element, "[hidden]") && (element.isField || element._gridContainer) && (element.getClientRects().length > 0)) {
					if (!element.validate()){
						this.firstFailedEl = this.firstFailedEl == null ? element : this.firstFailedEl;
						validCnt++;
						
						var el = this.findRecursiveElement(element, "cc-fieldset");
						this._validateItems.push(el);
					}
				} else if (!this.validateElements(element
						.querySelectorAll('[field],sc-grid'))) {
					validCnt++;
				}
			}
			return !(validCnt > 0);
		},
		
		findRecursiveElement(element, tag) {
			var lastPrefixTags = ["em-", "es-", "ep-", "smartsuite-"];
			var tagName = Polymer.dom(element).node.nodeName.toLowerCase();
			
			if(tagName === tag) {
				return element;
			}
			if(lastPrefixTags.indexOf(tagName.substring(0, tagName.indexOf('-') + 1)) < 0) {
				return this.findRecursiveElement(element.parentElement, tag);
			}
			return null;
		},
		
		viewValidateGroup: function(validateItems) {
			validateItems.forEach(function(item) {
				item.tabIndex = 0;
			});
			
			/*<div className="vbox" style="height: 100px;width: 100px;align-items: center;">
				<div style="height: 50px;width: 50px;border-radius: 50%;background-color: #b40404"></div>
				<div>으하하하하</div>
			</div>*/
			
			if(validateItems.length > 0) {
				var div = document.createElement("div");
				div.className = "vbox";
				div.style = "height: 100px;width: 100px;align-items: center;";
				
				var circle = document.createElement("div");
				circle.style = "height: 50px;width: 50px;border-radius: 50%;background-color: #b40404;margin-top: 15px;";
				
				var label = document.createElement("div");
				label.innerText = "하하하하";
				
				div.appendChild(circle);
				div.appendChild(label);
				
				var summary = document.createElement("cc-validation-summary");
				summary.appendChild(div);
				
				this.insertBefore(summary, this.firstChild);
			}
		},
		
		resetValidateGroup: function(validateItems) {
			validateItems.forEach(function(item) {
				if(item !== null){
					item.tabIndex = -1;
				}
			});
			validateItems = [];
		},

		resetOriginalValue : function() {
			//sc-form-panel 사용여부에 따라 달라질 수 있음
			var fields = this.querySelectorAll('[field]:not([reset="false"])');
			for (var i = 0, len = fields.length, field; i < len; i++) {
				field = fields[i];
				if (field.resetOriginalValue) {
					field.resetOriginalValue();
				}
			}
		},

		//데이터 초기화
		reset : function(targetEle) {
			var targetEle = targetEle || this;
			//sc-form-panel 사용여부에 따라 달라질 수 있음
			var containers = Polymer.dom(targetEle.root || targetEle)
					.querySelectorAll('sc-tab-navigation,sc-pages');
			var fields = targetEle.querySelectorAll('[field]:not([reset="false"])');
			var grids = targetEle.querySelectorAll('sc-grid:not([reset="false"])');
			var editors = targetEle.querySelectorAll('sc-editor:not([reset="false"])');
			var echarts = targetEle.querySelectorAll('cc-echart:not([reset="false"])');
			var uploaders = targetEle.querySelectorAll('sc-upload');
			var boxes = targetEle.querySelectorAll('.flex,.vbox,.hbox,.page,.fit');
            var resetableEles = targetEle.querySelectorAll('*[resetable]');

			if (targetEle.resetProperties) {
				targetEle.resetProperties();
			}
			for (var j = 0, len = containers.length, container; j < len; j++) {
				container = containers[j];
				for(var j2=0, len2=container.items.length; j2<len2; j2++) {
					var item = container.items[j2];
					if (item.resetProperties) {
						item.resetProperties();
					}
				}
				var selected = container.getAttribute("selected-index") != null ? Number(container
						.getAttribute("selected-index"))
						: 0;
				container.selectedIndex = selected;
			}
			for (var i = 0, len = fields.length, field; i < len; i++) {
				field = fields[i];
				if (SCUtil.isFunction(field.reset)) {
					field.reset();
				}
				else {
					console.warn(field.tagName.toLocaleLowerCase() + '["' + field.id + '"].reset is not function')
				}
			}
			for (var z = 0, len = grids.length, grid; z < len; z++) {
				grid = grids[z];
				if (grid && UT.isNotEmpty(grid.getDataProvider())) {
					grid.getDataProvider().removeAll(false);
					if(SCUtil.isFunction(grid.getDataProvider().clearRemoveItems)){
						grid.getDataProvider().clearRemoveItems();
					}
					this.resetCheckedHeader(grid.getColumns());
				}
			}
			
			for(var f = 0,len = uploaders.length; f < len; f++){
				var uploader = uploaders[f];
				if (SCUtil.isFunction(uploader.reset)) {
					uploader.reset();
				}
			}
			
			for(var p = 0,len = boxes.length; p < len; p++){
				var box = boxes[p];
				if (box.scrollTop) {
					box.scrollTop = 0;
				}
			}
			
			for(var k = 0,len = editors.length; k < len; k++){
				var editor = editors[k];
				if (editor && SCUtil.isFunction(editor.setEditorMode)) {
					editor.syncValue();
					editor.setEditorMode("editor");
				}
			}
			
			for(var k = 0,len = echarts.length; k < len; k++) {
				var echart = echarts[k];
				if (SCUtil.isFunction(echart.reset)) {
					echart.reset();
				}
			}

            for(var k = 0,len = resetableEles.length; k < len; k++){
                var resetableEle = resetableEles[k];
                if (SCUtil.isFunction(resetableEle.reset)) {
                    resetableEle.reset();
                }
            }

			if(targetEle._reset)
                targetEle._reset();
			
			this.resetValidateGroup(this._validateItems);
		},
		
		resetCheckedHeader : function(columns) {
			for(var i = 0; i < columns.length; i++){
				if(columns[i].isCheckBoxColumn){
					columns[i].gridColumn._checked = false;
				}
			}
        },

		_destroyed : function() {
			if (this.isDestroyed) {
				return;
			}
			this.isDestroyed = true;
			this.destroyed();
		},
		/* cc-toogle-container에서 사용하는 resizer */
        listeners: {
            "area-resize": "_onResizeListener"
        },
        /* cc-toogle-container에서 사용하는 resizer */
        _onResizeListener: function() {
            var me = this;
            var elements = this.querySelectorAll('sc-grid');
            for (var i = 0, len = elements.length, field; i < len; i++) {
				var element = elements[i];
				element.doContentElementResize();
            }
        }
	};

	Polymer.Base._registerBeforeChainObjectProcessor(function(object,
			inherited) {
		if(isCCModuleBehavior(object.is)){
			object.behaviors = (object.behaviors || []);
			object.behaviors.push(Polymer.CCModuleBehavior);
			Polymer.CCModuleBehavior = [ Polymer.CCModuleBehavior, Polymer.ModuleBehavior ]
		}
	});
	
	// cc-module-behaivor 판단 Function
    function isCCModuleBehavior(elementName) {
        return SCUtil.isString(elementName) ? modules.getKey(elementName.split('-')[0]) : false;
    }
}());