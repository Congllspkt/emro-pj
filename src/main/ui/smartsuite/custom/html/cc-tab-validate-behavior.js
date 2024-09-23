(function () {
    Polymer.CCTabValidateBehavior = {
        properties: {
            //반환되는 Valdation 메시지
            validateResults: {
                type: Array,
                value: function () {
                    return [];
                }
            },
            //true일 경우 Validation 실패할 경우 다음 Validation을 하지 않음
            stopValidateWhenFail: {
                type: Boolean,
                value: true
            }
        },

        firstFailedTab: null,

        //외부 비지니스 로직에서 실행할 validate 함수 (loadmask 및 초기화)
        validateTabNavigation: function (targetTabNavigation, targetTabEls, loadParam, validParam, callback) {
            var me = this;
            UT._requestedCount++;
            me.validateResults = [];
            SCLoadMask.show();
            me.validateTabs(targetTabNavigation, targetTabEls, loadParam, validParam, callback);
        },

        //재귀적으로 validateTabs 호출 (화면선택, 데이터 로딩, 결과 전달)
        validateTabs: function (targetTabNavigation, targetTabEls, loadParam, validParam, callback) {
            var me = this;
            targetTabNavigation.selectItem(targetTabEls[0]);
            var targetTab = targetTabNavigation.selectedItem;
            if(targetTabEls.length === 0) {// 모든 탭 validate 종료 시
                var result = true;
                if(me.firstFailedTab !== null) {
                    targetTabNavigation.selectItem(me.firstFailedTab);
                    me.firstFailedTab = null;
                    result = false;
                }
                else {
                    targetTabNavigation.selectIndex(0);
                }
                callback.call(me, result, me.validateResults);
                UT._requestedCount--;
                SCLoadMask.hide();
                return;
            }
            if(targetTab.isLoaded === true) { //target Tab이 loaded 되었는지 검사. 안되었으면 재귀 호출
                // validation 수행
                var validResult = targetTab.isValidFn(validParam);
                me.validateResults.push({targetTab: targetTab, result: validResult});
                if(validResult !== true) { //true가 아닌 모든 값은 실패로 취급
                    me.firstFailedTab = me.firstFailedTab === null ? targetTab : me.firstFailedTab;
                    if(me.stopValidateWhenFail) {
                        targetTabEls = []; //stopValidateWhenFail 프로퍼티가 true일 때, 실패할 경우 targetTabEls 를 빈배열로 설정하여 재귀 종료
                    }
                }
                me.validateTabs(targetTabNavigation, targetTabEls.slice(1), loadParam, validParam, callback);
            } else {
                if(!targetTab.isLoading) {
                    UT.isFunction(targetTab.load) && targetTab.load(loadParam);
                    targetTab.isLoading = true;
                }
                me.async(function () {
                    me.validateTabs(targetTabNavigation, targetTabEls, loadParam, validParam, callback);
                }, 10);
            }
        }
    }
}());