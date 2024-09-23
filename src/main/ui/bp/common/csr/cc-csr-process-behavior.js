(function () {
    Polymer.CSRProcessBehavior = {

        get editorPanel() {
            if(!this._editorPanel) {
                this._editorPanel = document.createElement('sc-editor');
            }
            return this._editorPanel;
        },
        // 요청 내용 에디터 생성
        createEditorPanel: function() {
            var editorPanel = this.editorPanel;
            editorPanel.id = "editorPanel";
            editorPanel.classList.add("h-400");
            editorPanel.style.width = "94%";
            Polymer.dom(this.$.editorContainer).appendChild(editorPanel);
            Polymer.dom.flush();
        },

        // 요청 내용 에디터 설정
        configureEditorPanel: function(data) {
            var editorPanel = this.editorPanel;
            editorPanel.value = data.csr_cont;
            editorPanel.editable = this.formula('isEditable');
        },

        // 처리 이력 생성
        createProcessingList: function(data) {
            if(data.length > 0){
                this.importLink("ui/bp/common/csr/es-csr-process-detail.html", function() {
                    var el = document.createElement('es-csr-process-detail');
                    el.processHistoryList = data;
                    el._parentElement = this;
                    Polymer.dom(this.$.contents).appendChild(el);
                    Polymer.dom.flush();
                });

            }
        },

        // 처리이력 초기화
        resetProcessHistory: function() {
            var processHistory = this.querySelector('es-csr-process-detail');
            if(processHistory) {
                processHistory.destroy();

            }
        },

        // 에디터 삭제
        removeEditorPanel: function() {
            var editors = this.querySelectorAll('sc-editor');
            for (var i=0,len=editors.length; i<len; i++) {
                editors[i].destroy();
            }
            this._editorPanel = null;
        }

    }
}());