Polymer.CCTitleBarBehavior = {
        properties: {
            /**
             *  타이틀을 보여주기 위한 속성입니다.
             *  _getTitle 함수의 return 값을 value 로 사용합니다.
             *
             * @type {String}
             */
            _title: {
                type: String,
                value: function(){
                	return "";
                }

            },

            /**
             *  페이지의 타이틀 문자열을 지정합니다.
             *  명시하지 않을 경우 메뉴의 이름을 가져옵니다.
             *
             * @type {String}
             */
            titleText: {
                type: String,
                value: function(){
                    return "";
                }
            },

            /**
             *  페이지의 타이틀 문자열을 지정합니다.
             *  `pageTitle` 프로퍼티는 deprecated 되었습니다. `titleText`를 사용하시기 바랍니다.
             *
             * @deprecated
             * @type {String}
             */
            pageTitle:{
                type: String,
                value: function(){
                    return "";
                }
            },

            /**
             * 부모 element 입니다.
             *
             * @type {Element}
             */
            _parentWindow:{
                type:Object,
                value: function(){
                    return {};
                }
            },

            /**
             * 'true'일 경우 다국어 변환을 하지 않습니다.
             *
             * @type {Boolean}
             */
            i18nDisabled:{
                type: Boolean,
                value: false,
                reflectToAttribute: true
            }
        },

        observers: [
        	'_getTitle(titleText, pageTitle,i18nDisabled)'
        ],


        /**
         * `titleText` 를 명시한 경우 지정한 text 를 페이지 타이틀로 설정합니다.
         *  명시 하지 않으면 메뉴의 이름으로 설정합니다.
         *
         * @private
         */
        _setPageTitle: function(){
            if(!this._getTitle(this.titleText, this.pageTitle) && this._parentWindow instanceof  SCMdiWindow && !(this._parentWindow instanceof  SCDialog)){
                this.set('i18nDisabled', true); //SMARTNINE-6068 영어 메뉴명 수집 X
                this.set('titleText', UT.closest(this, 'sc-mdi-window').getMenuNm());
            }
        },

        /**
         * 페이지 타이틀을 가져옵니다.
         *
         * @private
         * @return {String}
         */
        _getTitle: function (titleText, pageTitle) {
            var title = titleText || pageTitle;
            this.set('_title', this.i18nDisabled ?  title : this.translate(title));
            return this.i18nDisabled ?  title : this.translate(title);
        },

        /**
         * 즐겨찾기 등록 및 삭제 시 호출되며, isFavorite Formula 를 수행합니다.
         *
         * @private
         */
        setFavorite: function(){
            this.applyFormula("isFavorite");
        },

        /**
         * 즐겨찾기 아이콘이 클릭 되면 호출되며, 즐겨찾기 list 에 추가하거나 삭제합니다.
         *
         * @private
         */
        _onFavorite : function() {
            var me = this,
                activatedWindow = SCMenuManager.getWindow();
            if(activatedWindow) {
                SCMdiManager.toogleFavorite(activatedWindow.windowId);
            }
        }
};

