/**
 * cc-fieldset 간의 상호참조
 */
Polymer.CCPanelMediatorBehavior = {

    properties:{
        
        /** Label 텍스트의 넓이 */
		labelWidth: {
			type: String,
			value: "150",
			observer: "onLabelWidthChanged"
		},
		
		/** Label 텍스트의 좌, 우 배치(left, center, right) */
		labelAlign: {
			type: String,
			value: "left",
			observer: "onLabelAlignChanged"
		},
		
		childrenWrapperSelectors: {
			type: String,
			value: "cc-fieldset"
		},
		
		freeLabelWidth: {
			type: String,
			value: "free"
		},
        
    },
    
    /** Child 엘리먼트 목록 */
	get __contentWrapper() {
		if(!this.__contentWrapperEls || this.__contentWrapperEls.length < 1) {
			this.__contentWrapperEls = this.querySelectorAll(this.childrenWrapperSelectors);
		}
		return this.__contentWrapperEls;
	},

	/** Child 엘리먼트에 이벤트를 통한 값 전달 */
	setClsChildren: function(eventName, cls, oldCls){
		var children = this.__contentWrapper;
		var childrenAttached =  false;
		if(!children || children.length < 1){
			setTimeout(this.setClsChildren.bind(this, eventName, cls, oldCls),100);
		}else{
			for(var i=0; i < children.length; i++){
				if(children[i].__attached){
					if(eventName == "label-width"){
						children[i].onChangedLabelWidth(cls, oldCls);
					}else if(eventName == "label-align"){
						children[i].onChangedLabelAlign(cls, oldCls);
					}
				}else{
					childrenAttached = true;
				}
			}

			if(childrenAttached){
				setTimeout(this.setClsChildren.bind(this, eventName, cls, oldCls),100);
			}
		}
	},
	
	/** 디폴트 Label Width */
	onLabelWidthChanged: function(newValue, oldValue){
		this.setClsChildren("label-width", newValue, oldValue);
    },
    
    /** 디폴트 Label Align(left, center, right) */
    onLabelAlignChanged: function(newValue, oldValue){
    	this.setClsChildren("label-align", newValue, oldValue);
    },
			
};


/** `Polymer.SCFocusableBehavior` 는  SCComponent 의 모든 필드 엘리먼트에 구현되며, 개별 엘리먼트별 다른 스타일을 가져갈 수 있도록 디폴트 CSS 클래스를 적용합니다. */
Polymer.SCCSSClassStyleBehavior = {
		
	properties : {
		
		/** 디폴트 CSS 클래스 적용 시, 다중 등록 가능하도록 하는 구분자 */
		styleClsDelimeter : {
			type : String,
			value: " "
		},
		
		/**
		* Host 엘리먼트의 디폴트 CSS 클래스를 적용
		*/
		hostCls : {
			type : String,
			reflectToAttribute : true,
	        observer: '_hostClsChanged',
	        value: null
		},
		
		/**
		 * 불필요한 CSS 클래스
		 */
		unnecessaryCls : {
			type : String,
			value: "style-scope"
		}
		
	},
	
	isEmpty : function(value){
		if(value instanceof Array){
			if(value.length == 1){
				return Polymer.$Util.isEmpty(value[0]);
			}else if(value.length > 0){
				return false;
			}else{
				return true;
			}
		}else{
			return Polymer.$Util.isEmpty(value);	
		}
	},
	
	isNotEmpty : function(value){
		return !this.isEmpty(value);
	},
	
	addCls : function(element, cls){
		var clsArr = cls.split(this.styleClsDelimeter);
		if(clsArr.length > 0){
			for(var idx=0; idx<clsArr.length; idx++){
				Polymer.$Element.addCls(element || this, clsArr[idx]);
			}
		}
	},
	
	removeCls : function(element, cls){
		var clsArr = cls.split(this.styleClsDelimeter);
		if(clsArr.length > 0){
			var clsArr = cls.split(this.styleClsDelimeter);
			for(var idx=0; idx<clsArr.length; idx++){
				Polymer.$Element.removeCls(element || this, clsArr[idx]);
			}
		}
	},
	
	_hostClsChanged: function(newCls, oldCls){
		if(this.isNotEmpty(newCls)){
			if(this.isNotEmpty(oldCls)){
				this.removeCls(this, oldCls)	
			}
			this.addCls(this, newCls)
		}
	},
	
	attached: function(){
		this.defer(this._removeUnnecessaryCls);
	},
	
	_removeUnnecessaryCls: function(){
		var classDelimeter = ".";
		var unnecessaryClsList = this.querySelectorAll(classDelimeter+this.unnecessaryCls);
		for(var idx = 0; idx<unnecessaryClsList.length; idx++){
			this.removeCls(unnecessaryClsList[idx], this.unnecessaryCls);
		}
	},
	
	combindCssClass: function(...values){
		var mergedCls="";
		for(var index in values){
			mergedCls += values[index] + " ";
		}
		return mergedCls;
	}
};