(function () {
    Polymer.CCSearchFieldBehavior = {
        properties: {
            // cc-search-field-behavior 여부를 확인하기 위한 프로퍼티
            _isCCSearchFieldBehavior: {
                type: Boolean,
                value : true
            },

            disabled: {
                type: Object,
                value: function () {
                    return false;
                }
            },
            required: {
                type: Object,
                value: function () {
                    return false;
                }
            },
            readonly: {
                type: Object,
                value: function () {
                    return false;
                }
            },
            // Trigger Hidden Flag
            hideTrigger : {
                type: Object,
                value : function() {
                    return false;
                }
            },
            // Trigger Disabled Flag
            disabledTrigger : {
                type : Object,
                value : function() {
                    return false;
                }
            },
            // result-Value Hidden Flag
            resultHidden : {
                type : Object,
                value : function() {
                    return false;
                }
            } ,
            // Value Width
            valueClass: {
                type: String,
            },
            // Result Width
            resultClass: {
                type: String,
                value: "w-130"
            },
            // Value Placeholder
            valuePlaceholder : {
                type: String,
                value: "코드"
            },
            // Result Placeholder
            resultPlaceholder : {
                type: String,
                value: "코드 명"
            },
            // Code(코드) Property
            value: {
                type: Object,
                notify: true,
                observer: 'valueChanged'
            },
            // Label(명) Property
            resultValue: {
                type: Object,
                notify: true
            },
            // Clear Flag
            clearFlag: {
                type: Boolean,
                value: false
            },
        },

        attached : function() {
        	if(!this._attachedCalled) {
                this._attachedCalled = true;
                //한번만 호출되도록 로직작성
                
                var me = this;
                var target = me.querySelector(".value-field");
                if(target && !me.mouseClickListener){
                	me.mouseClickListener = me.mouseClickFn.bind(me);
                    target.addEventListener("mousedown", me.mouseClickListener);
                }
            }
        },

        _destroy : function() {
            var me = this;
            var target = me.querySelector(".value-field");
            if(target && me.mouseClickListener){
                target.removeEventListener("mousedown", me.mouseClickListener);
            }
        },

        // Mouse Click Event
        mouseClickFn : function(event) {
            // Right Click
            if (event.button == 2 || event.which == 3 || event.target.classList.contains("input-clear")) {
                this.clearFlag = true;
            }
        },

        // Field ValueChaged
        valueChanged: function () {
            if (this.clearFlag) {
                this.set("resultValue", "");
            }
            this.clearFlag = false;
        },

        // KeyDown Event
        onKeyDown: function () {
            this.clearFlag = true;
        }
    }
}());