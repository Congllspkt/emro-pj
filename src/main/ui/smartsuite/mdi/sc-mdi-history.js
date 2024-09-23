/**
 * SCMdiHistory 는 리스트, 상세 화면 단위로 뒤로가기 및 앞으로 가기 기능 (히스토리 기능) 을 담당합니다.
 * useHistory 값으로 history 기능 사용 여부를 변경 할수 있으며, SCMdiHistory.setHistory 를 호출 하여 사용합니다.
 *
 * @example em-rfx.html
 *
 * // 상세 화면 호출 시
 * onShowRfxDetail : function() {
 *      var me = this;
 *      me.$.pages.selectItem(me.$.rfxDetail);
 *      SCMdiHistory.setHistory('detail', arguments.callee.name, data);
 *      me.$.rfxDetail.load(data);
 * }
 *
 * // 리스트 화면 호출 시
 * onShowList : function() {
 *      var me = this;
 *      me.$.pages.selectItem(me.$.list);
 *      SCMdiHistory.setHistory();
 * }
 *
 */
var SCMdiHistory = (function() {
    var useHistory = false, // history 사용 여부
        pageIdx = 0, // 히스토리 엔트리 순번
        isForward = false, // 뒤로 가기 버튼과 앞으로 가기 버튼의 구분
        preventHistory = false, // 히스토리 방지
        historySts = false; // 히스토리 기능에 의한 동작 여부


    // 히스토리 기능 을 사용 하지 않을 경우, 뒤로가기 기능을 방지 합니다.
    if(!useHistory){
        history.pushState(null, null, location.href);
        window.onpopstate = function() {
            //history.go(1);
        	history.pushState(null, null, location.href);
        };
        return {
            // public
            useHistory : useHistory
        }
    }

    // 히스토리 기능 을 사용 할 경우
    if (!history.state) {
        history.replaceState('default', null);
    }

    // 새로고침 시 기존 히스토리 엔트리를 초기화 합니다.
    if (history.state && history.state.pageIdx) {
        var len = history.state.pageIdx;
        if (len > 0) {
            history.go(-len);
        }
    }

    // 히스토리 엔트리의 순번을 관리합니다.
    window.onhashchange = function () {
        if (history.state && history.state.pageIdx) {
            pageIdx = history.state.pageIdx;
        }
    };

    window.onpopstate = function () {

        isForward = pageIdx < history.state.pageIdx;

        // popup, messagebox 가 띄어진 상태 (backdrop 이 존재할 경우) 뒤로가기가 동작하지 않도록 제어합니다.
        var overlayEl = document.querySelector('sc-overlay-backdrop');
        if (overlayEl && overlayEl.style.display === "block") {
            if (!preventHistory) {
                if (pageIdx < history.state.pageIdx) {
                    history.go(-1);
                } else {
                    history.go(1);
                }
                preventHistory = true;
            } else {
                preventHistory = false;
            }
            return;
        }

        // 최초화면 일 경우 로그아웃 확인 메세지를 띄워줍니다.
        if (history.state === null || (SCMdiManager.activatedWindow() && history.state === 'default')) {
            UT.confirm("STD.MDI1001", function () {
                document.getElementById('logoutForm').submit();
            }, function () {
                history.go(1);
                preventHistory = true;
            });
            return;
        }

        //활성화된 메뉴 없을 경우 return
        if (!SCMdiManager.activatedWindow()) {
            return;
        }

        // 다른 메뉴를 띄워줘야 할 경우
        if (SCMdiManager.activatedWindow().id !== history.state.menuId) {

            var handler = function () {
                var callback;
                // 상세화면 이동 일 경우, 메뉴 활성화 혹은 생성 이후 상세 화면으로 전환
                if (history.state.module === "detail") {
                    callback = function () {
                        historyToDetail(history.state);
                    }

                }
                setHistorySts(true);
                // HOME 화면 일 경우, 활성화
                if(history.state.menuId === "HOME"){
                    SCMdiManager.activateWindow(history.state.menuId);
                }else { // 다른 메뉴 일 경우, 메뉴 활성화 혹은 생성
                    MDIUT.createWindowByMenuId(history.state.menuId, callback, false);
                }

            };
            handler();
            return;
        }
        if (preventHistory) {
            preventHistory = false;
            return;
        }

        // 같은 메뉴 내 리스트, 상세 이동 일 경우
        if (history.state.module === "list") {
            historyToList();
        } else if (history.state.module === "detail") {
            historyToDetail(history.state);
        }
    };

    /**
     * 리스트화면으로 이동 할 경우 호출됩니다.
     */
    var historyToList = function() {
        var scPage = SCMdiManager.activatedWindow().querySelector('sc-pages');
        if(!scPage) {
            return;
        }

        // 상세 -> 리스트 일 경우 onClose 함수를 호출합니다.
        var selectedIndex = scPage.selectedIndex;
        if(selectedIndex > 0){
            setHistorySts(true);
            scPage.selectedItem.onClose();
        }
    };

    /**
     *  상세화면으로 이동 할 경우 호출됩니다.
     */
    var historyToDetail = function(obj) {
        setHistorySts(true);
        var data = obj.data;
        SCMdiManager.activatedWindow().firstChild[obj.funcName](event, JSON.parse(data));
    };

    /**
     *  히스토리 엔트리를 저장합니다.
     *  @param{string} menuId 현재 메뉴 id
     *  @param{string} module 상세 화면전환 시 `detail`
     *  @param{string} 상세 화면 전환 시 호출 한 function name
     *  @param{Object} 상세 화면 전환 시 전달 한 data
     */
    var setHistory = function(menuId, module, funcName, data) {
        if(getHistorySts()){
            setHistorySts(false);
            return;
        }

        menuId = menuId || SCMdiManager.activatedWindow().getMenuId();
        module = module || "list";

        var stsObj = history.state;
        if(stsObj && stsObj.menuId === menuId && stsObj.module === module){
            return;
        }
        var pageIdx = history.state === 'default' ? 1 : history.state.pageIdx+1,
            url = module === "detail" && '#'+menuId+'#'+module || '#'+menuId;
        history.pushState({menuId: menuId, module: module, pageIdx: pageIdx, funcName: funcName, data: JSON.stringify(data)}, null, url);
    };

    /**
     *  히스토리 엔트리에서 앞 혹은 뒤로 이동합니다.
     */
    var goHistory = function() {
        preventHistory = true;
        isForward ? history.go(-1) : history.go(1);
    };

    /**
     *  히스토리 기능에 의한 동작 여부를 지정합니다.
     */
    var setHistorySts = function(sts) {
        historySts = sts;
    };

    /**
     *  히스토리 기능에 의한 동작 여부를 반환합니다.
     */
    var getHistorySts = function() {
        return historySts;
    };

    return {
        // public
        useHistory : useHistory,
        setHistory : setHistory,
        goHistory : goHistory,
        getHistorySts : getHistorySts

    }
}());