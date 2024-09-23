/**
 * 자동 완성 기능을 추가하는 behavior
 */
Polymer.CCAutocompleteBehavior = {
    properties:{
        autoComplete:{
            type: Boolean,
            value: false
        }
    },
    ready : function() {
        var elements = Polymer.dom(this.root).querySelectorAll('sc-ajax, sc-grid-paging'),
            len = elements.length;

        for(var i=0,element; i<len; i++) {
            element = elements[i];
            if(this.getAjaxUrl() && element.url === this.getAjaxUrl()) {
                this.listen(element, 'response', '_onResponse');
            }
        }
    },

    _onResponse : function(event, response) {
        var me = this,result = "";
        if(event.detail.error){
            result = event.detail;
        } else if (event.currentTarget.tagName === "SC-GRID-PAGING") {
            result = event.detail;
        } else {
            result = event.detail.parseResponse();
        }

        if (me.autoComplete && Array.isArray(result)) { // 배열
            if (result.length === 1) {
                me.fire("selected-items", [result[0]]);
            }
        }
    }
};