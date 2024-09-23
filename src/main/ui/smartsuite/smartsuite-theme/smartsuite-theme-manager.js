var EventUtil = {
    addHandler : function(element, type, handler) {
        if( element.addEventListener) {
            element.addEventListener(type, handler, false);
        } else if(element.attachEvent) {
            element.attachEvent("on"+type, handler);
        } else {
            element["on"+type] = handler;
        }
    },

    removeHandler : function(element, type, handler) {
        if( element.removeEventListener) {
            element.removeEventListener(type, handler, false);
        } else if(element.detachEvent) {
            element.detachEvent("on"+type, handler);
        } else {
            element["on"+type] = null;
        }
    }
};


SmartsuiteTheme = new(function() {

    /**
     * 테마 관련 설정 값을 정의합니다.
     */
    var themeConfig = {
        /**
         * 테마에 사용되는 스타일시트 및 스크립트 파일의 경로를 지정합니다.
         */
        path: {
            mdi : "ui/lib/smartsuite-theme/css/mdi-css/",
            common : "ui/lib/smartsuite-theme/css/common-css/",
            grid : "ui/lib/smartsuite-theme/css/grid-css/"
        },
        /**
         * 스타일시트 및 스크립트 파일의 prefix 를 정의 합니다.
         */
        prefix: {
            mdi : "mdi-",
            common : "common-",
            grid : "grid-"
        },
        /**
         * 현재 적용된 테마를 지정합니다.
         */
        themeStyleName: null,
        /**
         * 그리드에서 기본으로 사용하는 테마 변수에 최초 대상 테마 반영여부를 반환합니다.
         */
        gridReflect: false,
        /**
         * 기본으로 사용할 테마를 지정할 수 있습니다.
         */
        defaultThemeStyle: null,
        /**
         * 테마스타일에 따른 그리드 테마 적용 변수이름을 명시합니다.
         */
        gridVariable: {
            "gray-theme" : {
                name : "gridGrayTheme"
            },
            "red-theme" : {
                name : "gridRedTheme"
            },
            "white-theme" : {
                name : "gridWhiteTheme"
            },
            "default-theme" : {
                name : "gridDefaultTheme"
            }
        }
    };

    function SmartsuiteThemeImpl () {

        // 로컬스토리지에 저장 된 테마 호출
        var localStorageTheme = this.getLocalStorageTheme();
        if(localStorageTheme) {
            this.applySmartsuiteTheme(localStorageTheme);
        } else if (themeConfig.defaultThemeStyle){
            this.applySmartsuiteTheme(themeConfig.defaultThemeStyle);
        }
    }

    /**
     * 로컬스토리지에 저장된 테마를 반환합니다.
     */
    SmartsuiteThemeImpl.prototype.getLocalStorageTheme = function() {
        return localStorage.getItem('[' + window.session_usr_id + ']storageTheme');
    };

    /**
     * 로컬스토리지에 선택된 테마를 저장합니다.
     */
    SmartsuiteThemeImpl.prototype.setLocalStorageTheme = function(themeStyleName) {
        localStorage.setItem('['+SCSessionManager.currentUser.usr_id+']storageTheme', themeStyleName);
    };

    /**
     * 로컬스토리지에서 테마 개인화 정보를 삭제합니다.
     */
    SmartsuiteThemeImpl.prototype.removeLocalStorageTheme = function() {
        localStorage.removeItem('['+SCSessionManager.currentUser.usr_id+']storageTheme');
    };

    /**
     * 현재 지정된 테마 이름을 반환합니다.
     */
    SmartsuiteThemeImpl.prototype.getThemeStyleName = function() {
        return themeConfig.themeStyleName;
    };

    /**
     * 그리드 테마 최초 반영 여부를 반환합니다.
     */
    SmartsuiteThemeImpl.prototype.getGridReflect = function() {
        return themeConfig.gridReflect;
    };

    /**
     * 테마 이름에 따른 그리드 테마 객체를 반환합니다.
     */
    SmartsuiteThemeImpl.prototype.getGridTheme = function(themeStyleName) {
        return window[themeConfig.gridVariable[themeStyleName].name];
    };

    /**
     * 그리드 기본 테마 객체를 저장합니다.
     */
    SmartsuiteThemeImpl.prototype.setGridDefaultTheme = function(obj) {
        window[themeConfig.gridVariable["default-theme"].name] = obj;
    };
    /**
     * 그리드 기본 테마 객체를 반환합니다.
     */
    SmartsuiteThemeImpl.prototype.getGridDefaultTheme = function() {
        return window[themeConfig.gridVariable["default-theme"].name];
    };

    /**
     * 스타일 시트 및 스크립트를 동적으로 추가합니다.
     * @private
     * @param {string} themeStyleName 테마 이름
     * @param {string} targetType     테마 구분 ( 'mdi', 'common', 'grid' )
     * @param {string} fileType       파일 타입 ( 'css', 'js' )
     */
    SmartsuiteThemeImpl.prototype._createElementTag = function(themeStyleName, targetType, fileType) {
        var head = document.getElementsByTagName('head')[0], fileRef,
            filePath = themeConfig.path[targetType] + themeConfig.prefix[targetType] +themeStyleName+"." + fileType,
            tagId = themeConfig.prefix[targetType] + themeStyleName;

        if(fileType === "css") {
            fileRef = document.createElement('link');
            fileRef.setAttribute("id", tagId);
            fileRef.setAttribute("type", "text/css");
            fileRef.setAttribute("rel", "stylesheet");
            fileRef.setAttribute("href", filePath);
        } else if (fileType === "js") {
            fileRef = document.createElement('script');
            fileRef.setAttribute("id", tagId);
            fileRef.setAttribute("type","text/javascript");
            fileRef.setAttribute("src", filePath);
            EventUtil.addHandler(fileRef, "load", function _listener() {
                EventUtil.removeHandler(fileRef, "load", _listener);
                this._reflectGridScript(themeStyleName);
            }.bind(this));
        }
        head.appendChild(fileRef);
    };



    /**
     * 그리드 스타일을 반영합니다.
     * @private
     */
    SmartsuiteThemeImpl.prototype._reflectGridScript= function(themeStyleName) {
        var gridThemeObj = this.getGridTheme(themeStyleName);
        if(typeof gridThemeObj === "undefined") {
            return;
        }

        if(typeof blueSkySkin !== "undefined"){
            blueSkySkin = gridThemeObj;
            themeConfig.gridReflect = true;
        }

        var grids = document.querySelectorAll('sc-grid');
        for(var i=0, len=grids.length, grid; i<len; i++) {
            grid = grids[i];
            grid.loadStyles(blueSkySkin);
        }

    };

    /**
     * 스타일 시트를 활성화 합니다.
     * @private
     * @param {string} themeStyleName 테마 이름
     * @param {string} targetType     테마 구분 ( 'mdi', 'common' )
     */
    SmartsuiteThemeImpl.prototype._activationStyle = function(themeStyleName, targetType) {
        var styles = document.styleSheets,
            href = "",
            oldFileName = themeConfig.prefix[targetType] + themeConfig.themeStyleName +".css",
            newFileName = themeConfig.prefix[targetType] + themeStyleName+".css";

        var cnt = 0;
        for(var i = 0, len=styles.length; i < len; i++) {

            if(cnt === 2) {
                break;
            }
            href = styles[i].href;
            if(!href){
                continue;
            }
            href = href.split("/");
            href = href[href.length-1];

            if(href === oldFileName) {
                styles[i].disabled = true;
                cnt++;
            }
            if(href === newFileName) {
                styles[i].disabled = false;
                cnt++;
            }
        }
    };

    /**
     * 테마를 적용합니다.
     * @private
     * @param {string} themeStyleName 테마 이름
     * @param {string} targetType     테마 구분 ( 'mdi', 'common', 'grid' )
     * @param {string} fileType       파일 타입 ( 'css', 'js' )
     */
    SmartsuiteThemeImpl.prototype._applyTheme = function(themeStyleName, targetType, fileType) {
        var targetTag = document.getElementById(themeConfig.prefix[targetType] + themeStyleName);
        if(!targetTag) {
            this._createElementTag(themeStyleName, targetType, fileType);
        }

        if(fileType === 'css'){
            this._activationStyle(themeStyleName, targetType);
        } else if (fileType === "js") {
            this._reflectGridScript(themeStyleName);
        }
    };



    /**
     * 스타일 시트를 제거합니다.
     * @private
     * @param {string} themeStyleName 테마 이름
     * @param {string} targetType     테마 구분 ( 'mdi', 'common' )
     */
    SmartsuiteThemeImpl.prototype._removeStyleSheet = function(themeStyleName, targetType) {
        var elementTag = document.getElementById(themeConfig.prefix[targetType] + themeStyleName);
        if(elementTag){
            elementTag.parentNode.removeChild(elementTag);
        }
    };


    /**
     * 'mdi', 'common', 'grid' 영역에 대해서 테마를 적용합니다.
     *
     * @param {string} themeStyleName 테마 이름
     */
    SmartsuiteThemeImpl.prototype.applySmartsuiteTheme = function(themeStyleName) {
        this._applyTheme(themeStyleName, "mdi", "css");
        this._applyTheme(themeStyleName, "common", "css");
        this._applyTheme(themeStyleName, "grid", "js");
        this.setLocalStorageTheme(themeStyleName);

        themeConfig.themeStyleName = themeStyleName;
    };


    /**
     * 'mdi', 'common', 'grid' 영역에 대해 적용된 테마를 제거합니다.
     */
    SmartsuiteThemeImpl.prototype.removeSmartsuiteTheme = function() {
        var themeStyleName = themeConfig.themeStyleName;
        this._removeStyleSheet(themeStyleName, "mdi");
        this._removeStyleSheet(themeStyleName, "common");
        this._reflectGridScript("default-theme");
        this.removeLocalStorageTheme();
        themeConfig.themeStyleName = null;
    }

    return SmartsuiteThemeImpl;
}());