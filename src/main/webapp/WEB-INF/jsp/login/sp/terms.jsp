<%@ page import="freemarker.debug.Debugger" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
  String tenant = (String)request.getParameter("tenant");
  String locale = (String)request.getParameter("locale");
  String type = (String)request.getParameter("type");
  if(tenant != null && !tenant.isEmpty()) {
      tenant = "'".concat(tenant).concat("'");
  }
  if(locale != null && !locale.isEmpty()) {
      locale = "'".concat(locale).concat("'");
  }
  if(type != null && !type.isEmpty()) {
      type = "'".concat(type).concat("'");
  }
%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta http-equiv="Cache-control" content="no-cache" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />
  <meta name="robots" content="noindex, nofollow">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1, user-scalable=yes">
  <title>SMARTsuite 10.0</title>
  <sec:csrfMetaTags />

  <link rel="stylesheet" type="text/css" href="bower_components/sc-component/sc-elements.std.css">
  <link rel="stylesheet" type="text/css" href="ui/assets/css/common.css" />
  <!-- Design Team For Smartsuite 10 -->
  <link rel="stylesheet" type="text/css" href="ui/assets/css/basic.design.css"/>
  <link rel="stylesheet" type="text/css" href="ui/assets/css/common.design.css"/>
  <script src="ui/smartsuite/custom/js/util.js"></script>
  <link href="css/popup.css" type="text/css" rel="stylesheet">

  <script>
    window.Polymer = {
      //Polymer 엘리먼트 로드 완료 이벤트 설정
      polymerReady: true,
      //다국어 처리 프로세서 비활성화
      useI18nParser: false,
      //캐시 버스트
      cacheBust: ('${_cacheBust}' || Date.now()),
      //지연로딩 활성화
      useLazyLoader: true,
      //지연등록 활성화
      useLazyRegister: true,
      //dom-module 의 whitespace를 지워주는 기능 활성화
      useStripWhiteSpace: true,
      //소멸자 비동기 활성화
      useTerminatorAsyncTask: true,

      //※ sc-component-1.1.8 버전에 신규로 추가된 설정
      //sc-component-1.1.8 이전 버전에 대한 dom-module 호환성 제거
      useDomModuleCompatibility: false,
      //엘리먼트 최소화
      detachedOnElementHided: true,
      //노드탐색 최적화
      useUpgradeWithDefinition: true,
      //Date 객체변환 최적화
      useISO8601TimezoneDesignatorWithColonSeperator: true,
      //SCButtonState mousedown, mouseup, click 이벤트 비활성화
      useButtonStateMouseEvent: false,
      //엘리먼트 공유 스타일 캐시 사용
      useGlobalStyleCache: true,
      //프로퍼티 이펙트 최소화
      usePropertyEffectFilter: true,
      //sc-code 로딩순서 변경
      useCodePrefetch: true,
      //sc-link-lazy-mask target 설정 비활성화
      useLinkLazyMaskTarget: false,
      //컨테이너 엘리먼트의 지연렌더링 기능 비활성화
      useContainerTemplatizedRenderer: false
      //ModuleBehavior 활성화
      , useModuleBehavior: true
      //팝업 자동소멸
      , usePopupAutoDestroy: true

      , ccModuleBehaviorReady: true
    };

    var ccModuleBehaviorHandler = function (e) {
      document.removeEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);
      Polymer.Base.importLink('ui/sp/login/ep-sp-terms-info.html', function (moduleId) {
        var module = document.createElement(moduleId);
        module.tenant = <%=tenant %>;
        module.locale = <%=locale %>;
        module.tcClsCd = <%=type %>;
        Polymer.dom(document.body).appendChild(module);
        Polymer.dom.flush();
      });
    }

    document.addEventListener('ccModuleBehaviorReady', ccModuleBehaviorHandler);

    document.addEventListener("terms-close", function () {
      if (window.fire) {
        window.fire("close");
      } else {
        window.close();
      }
    });
  </script>
  <script src="bower_components/template-designer/js/pdf_form/dist/polly.bundle.js"></script>
  <script src="bower_components/webcomponentsjs/webcomponents-lite.min.js"></script>
</head>

<body>
  <div class="top_progress"></div>
  <script src="license.do"></script>
  <link rel="import" href="bower_components/sc-component/sc-elements.std.html">
  <link rel="import" href="ui/override.html">
  <link rel="import" href="ui/smartsuite/preloader/sc-preloader.html"
        locale="${pageContext.response.locale.language}_${pageContext.response.locale.country}" anonymous="true">
</body>

</html>