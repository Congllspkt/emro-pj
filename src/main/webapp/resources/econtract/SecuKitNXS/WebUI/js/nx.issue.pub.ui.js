//JQuery 하위 호환성 문제를 해결하기 위해 $ 재정의
//var NXJQUERY = $.noConflict(false);
if (typeof NX_Issue_pubUi !== 'undefined') {
  alert('NX_Issue_pubUi 변수가 중복사용되고 있습니다');
} else {
  var NX_Issue_pubUi = {};
  /* input placeholder */
  NX_Issue_pubUi.placeholder = function () {
    var input = $('.nx-pki-ui-wrapper .js-placeholder').next('input:enabled'); //활성화된 텍스트박스
    var label = $('.nx-pki-ui-wrapper input:enabled').siblings('.js-placeholder'); //활성화된 텍스트박스의 레이블

    //최초 로딩시 바인딩된 이벤트를 제거
    $('.nx-pki-ui-wrapper .js-placeholder').next('input').off('focus, blur, change');
    $('.nx-pki-ui-wrapper input').siblings('.js-placeholder').off('click');

    //이벤트 할당
    input
      .on({
        focus: function () {
          $(this).prev('.js-placeholder').css('visibility', 'hidden');
        },
        blur: function () {
          if ($(this).val() === '' && $(this).html() === '') {
            $(this).prev('.js-placeholder').css('visibility', 'visible');
          } else {
            $(this).prev('.js-placeholder').css('visibility', 'hidden');
          }
        },
        change: function () {
          if ($(this).val() === '' && $(this).html() === '') {
            $(this).prev('.js-placeholder').css('visibility', 'visible');
          } else {
            $(this).prev('.js-placeholder').css('visibility', 'hidden');
          }
        },
      })
      .blur();
    label.on('click', function () {
      $(this).css('visibility', 'hidden');
      $(this).next('input').focus();
    });
  };

  /***********************************************
    공통 : 추가매체 창 닫기 버튼 클릭시
    ************************************************/
  NX_Issue_pubUi.moreSaveMediaHide5 = function () {
    TargetMediaInfo.setMediaType('');
    TargetMediaInfo.setExtraValue('');

    initTextAll();

    // 비밀번호 입력 안내
    document.getElementById('pwdnoti_1').innerHTML = '<label for="certPwd">' + NX_WEBUI_SELECT_TEXT_8 + '</label>';
    document.getElementById('pwdnoti_2').innerHTML = NX_WEBUI_SELECT_TEXT_9;

    $('#MediaSet_1>li>button.active').focus();
    $('#MediaSet_2>li>button.active').focus();

    $('#MediaSet_1>li>button.active').removeClass('active');
    $('#MediaSet_2>li>button.active').removeClass('active');

    var msg = NX_ISSUE_PUB_TEXT_2 + ' :';
    document.getElementById('targetMedia_noti').innerHTML = msg;

    var pwdObj = document.getElementById('certPwd');
    pwdObj.readOnly = false;
    pwdObj.style.backgroundColor = '#ffffff';

    NX_Issue_pubUi.certListReset();
    $('.cert-location-area .pki-wrap6').fadeOut('fast');
    $('.cert-location-area .pki-wrap4').fadeOut('fast');
    $('.cert-location-area .pki-wrap3').fadeOut('fast');
  };

  NX_Issue_pubUi.removeMediaExtraDialog = function () {
    $('.cert-location-area .pki-wrap6').fadeOut('fast');
    $('.cert-location-area .pki-wrap4').fadeOut('fast');
    $('.cert-location-area .pki-wrap3').fadeOut('fast');
  };

  /***********************************************
    공통 : 보안토큰&지문보안토큰 PIN 입력창 확인 클릭시
    ************************************************/
  NX_Issue_pubUi.moreSaveMediaHide7 = function () {
    var pinNum = document.getElementById('nx_cert_pin').value;

    if (InsertNullCheck(pinNum) === true) {
      KICA_Error.init();
      var errorcode = 'ERR_SCRIPT_DIALOG_INPUT_PINNUM';
      KICA_ERROR_RESOURCE.ErrorMessage('', '', errorcode);
      var ScriptErrorMessage = KICA_Error.getScriptError();
      alert(ScriptErrorMessage);

      // 인증서 선택창이 사라져...임시로 넣어둠..
      $('#nx-pki-ui-wrapper').show();
      $('#nx-cert-select').show();

      setTimeout(function () {
        try {
          $('#nx_cert_pin').focus();
        } catch (e) {}
      }, 500);
    } else {
      $('#certPwd').val(pinNum);
      setTimeout(function () {
        $('#certPwd').trigger('focus');
      }, 300);
      document.getElementById('nx_cert_pin').value = '';

      $('.cert-location-area .pki-wrap6').fadeOut('fast');
    }
  };

  /*새로운 보안 모듈 사용시*/
  NX_Issue_pubUi.custEnc = function () {
    EZKInput.Init(); //암호화 초기화
    EZKInput.InitInput('certPwd', NX_Issue_pubUi.pwdEnc); //certPwd 키가 입력될때 암호화 된 값을 받아서 처리하는 함수 지정
  };

  /***********************************************
    인증서 갱신 안내
    ************************************************/
  NX_Issue_pubUi.moreSaveMediaHide4 = function () {
    $('#nx-error-cert-update').fadeOut('fast');
    $('#NXcertList tr.on').focus();
  };

  /* 인증서 목록 전체삭제-초기화 */
  NX_Issue_pubUi.certListReset = function () {
    $('#select-cert-list-area').remove();
    $('#select-cert-list').append(KICA_CERTLISTEMPTY);
  };

  /* 저장매체 목록 선택시 컬러변경 */
  NX_Issue_pubUi.certSelectRow = function (index) {
    initTextAll();
    var index_ = index - 1;
    var obj = $('.nx-pki-ui-wrapper .js-selrow');
    obj.find('tr').each(function (n) {
      $(this).removeClass('on');
      if (n === index_) {
        $(this).addClass('on');
        $(this).focus();
      }
      $(this).on('click', function () {
        obj.find('tr').each(function (n) {
          $(this).removeClass('on');
        });
        $(this).addClass('on');
        return false;
      });
    });
  };

  /* 인증서 선택창 목록 선택시 컬러변경 */
  NX_Issue_pubUi.certSelectRow_CertList = function (index) {
    // 보안토큰인 경우 PIN 입력값 초기화 Skip
    var mediaType = SelectMediaInfo.getMediaType();
    if (mediaType !== 'HSM' && mediaType !== 'BIOHSM') {
      initTextAll();
    }

    var obj = $('.nx-pki-ui-wrapper .js-selrow-certlist');
    obj.find('tr').removeClass('on');
    obj.find('tr > td:first-child > span').html("선택되지 않음");
    obj.find('tr[id=' + index + ']').addClass('on');
    obj.find('tr[id=' + index + '] > td:first-child > span').html('선택됨 ');

    if (NX_SELECTED_ROW != index) {
      //비밀번호 횟수 초기화
      NX_PW_COUNT_GET = NX_PW_COUNT_SET;
      NX_SELECTED_ROW = index;
    }
  };

  /* 인증서 id값 리턴 */
  NX_Issue_pubUi.certIdReturn = function (_this) {
    $('.nx-pki-ui-wrapper #NX_SelectCertIndex').val(_this.id);
    certListInfo.setCertListIndex(_this.id);

    //인증서 유효기간 검사
    var certListObj = certListInfo.getCertListInfo();
    var fullCertTodate = certListObj[_this.id].todate;
    var certTodate = fullCertTodate.split('-');
    var d = new Date();
    var NowYear = ZerosDate(d.getFullYear(), 4);
    var NowMonth = ZerosDate(d.getMonth() + 1, 2);
    var NowDate = ZerosDate(d.getDate(), 2);
    var selectCertTodateString = certTodate[0] + certTodate[1] + certTodate[2];
    var dueDateString = NowYear + NowMonth + NowDate;

    // 만료기한 계산 : 보완이 좀 필요하다!!
    var restDate = getDifferentDays(dueDateString, selectCertTodateString);
    var restDateInt = parseInt(restDate);

    if (restDateInt < 30 && restDateInt >= 0) {
      // 기존 메시지 내용 삭제
      $('#nx-error-cert-update-msg-contents').remove();

      var updateMessage = '<div id="nx-error-cert-update-msg-contents">';
      updateMessage += '<div class="pki-body5">';
      updateMessage += '<p class="txt-l" style="line-height:1.5; font-size:13px;">';
      updateMessage += NX_ISSUE_PUB_TEXT_4 + '<br />';
      updateMessage +=
        '<strong>' +
        certTodate[0] +
        NX_ISSUE_PUB_TEXT_5 +
        certTodate[1] +
        NX_ISSUE_PUB_TEXT_6 +
        certTodate[2] +
        NX_ISSUE_PUB_TEXT_7 +
        '</strong>' +
        NX_ISSUE_PUB_TEXT_8 +
        ' </p></div>';
      updateMessage += '<div class="pki-bottom5">';
      updateMessage +=
        '<div class="gray-box2 mb10" style="line-height:1.5; font-size:13px;">' +
        NX_ISSUE_PUB_TEXT_9 +
        '(' +
        NowYear +
        '/' +
        NowMonth +
        '/' +
        NowDate +
        ')' +
        NX_ISSUE_PUB_TEXT_10 +
        ' <strong>' +
        restDateInt +
        '' +
        NX_ISSUE_PUB_TEXT_11 +
        '</strong> ' +
        NX_ISSUE_PUB_TEXT_12 +
        '</div>';
      updateMessage += '<button class="btn-ok" onclick="NX_Issue_pubUi.moreSaveMediaHide4();return false;">' + NX_ISSUE_PUB_TEXT_15 + '</button></div></div>';

      // 신규 메시지 append
      $('#nx-error-cert-update-msg-box').append(updateMessage);
      $('#nx-error-cert-update').show();
      setTimeout(function () {
        $('#nx-error-cert-update-msg-box button').focus();
      }, 100);

      if (NX_DIALOG_MOVE === true) {
        try {
          //추가 미디어 창 커서 이동을 위해 추가
          $('.pki-wrap5').draggable({ handle: '.pki-head5', cursor: 'move' });
        } catch (e) {
          console.log(e);
        }
      }
    }
  };

  /* 확장매체 클릭시 */
  NX_Issue_pubUi.moreSaveMediaShow = function () {
    // 추가매체 창 제거
    NX_Issue_pubUi.moreSaveMediaHide5();
    // 첫번째 인증서 선택된 index값 삭제
    certListInfo.setCertListIndex('');

    initTextAll();
    $('.cert-location-area .sub-layer').fadeIn('fast');
    setTimeout(function () {
      $('#MediaSet_1-sub button:enabled')[0].focus();
    }, 100);
  };

  NX_Issue_pubUi.moreSaveMediaHide = function () {
    $('.cert-location-area .sub-layer').fadeOut('fast');
  };

  NX_Issue_pubUi.moreSaveMediaHideFocusMediaList = function () {
    setTimeout(function () {
      $('#MediaSet_1 li:last-child button').focus();
    }, 300);
  };

  /* 추가매체 클릭시 */
  NX_Issue_pubUi.moreSaveMediaShow2 = function (media, extra) {
    // 확장매체 표시 제거
    NX_Issue_pubUi.moreSaveMediaHide();
    // 첫번째 인증서 선택된 index값 삭제
    certListInfo.setCertListIndex('');

    initTextAll();

    // 유심인 경우에는 추가 매체창이 출력되지 않도록
    // 나중에 서비스업체별 선택사항으로 변경시 if문 제거
    if (media === 'USIM' || media === 'usim') {
      $('.cert-location-area .pki-wrap3').hide();
    } else if (media === 'SECUREDISK' || media === 'securedisk') {
      $('.cert-location-area .pki-wrap3').hide();
    } else {
      $('.cert-location-area .pki-wrap3').fadeIn('fast');
    }

    SelectMediaInfo.setMediaType(media); //미디어 정보 Set
    SelectMediaInfo.setExtraValue(extra);
    //추가 정보(드라이브명 등)를 얻기 위한 함수 호출
    targetMediaFlag.init();
    targetMediaFlag.setFlag(false);
    //인증서 리스트 초기화
    NX_Issue_pubUi.certListReset();
    setTimeout(function () {
      NX_selectStorage(media, extra);
    }, 100);
  };

  NX_Issue_pubUi.moreSaveMediaHide2 = function () {
    var media = SelectMediaInfo.getMediaType();
    var extra = SelectMediaInfo.getExtraValue();

    initTextAll();

    if (extra === '' || extra === 'NULL') {
      alert(NX_ISSUE_PUB_TEXT_16);
    } else {
      $('.cert-location-area .pki-wrap3').fadeOut('fast');

      // Token인 경우 KISA 검증 여부 판단
      if (media === 'HSM' || media === 'BIOHSM') {
        var tmpObj = TokenMediaInfo.getTokenMediaObj();
        var tokenVrf = '';

        var i;
        for (i = 1; i <= tmpObj.size; i++) {
          if (extra === tmpObj[i].diskName) {
            tokenVrf = tmpObj[i].verify;

            if (tokenVrf === 'N') {
              alert(NX_ISSUE_PUB_TEXT_32);
            }
          }
        }
      }

      // BIOHSM인 경우
      if (media === 'BIOHSM') {
        var Tmp_BioTokenInfo = '';
        Tmp_BioTokenInfo = extra.split('|');
        extra = Tmp_BioTokenInfo[1];
      }

      // 인증서 리스트 호출
      Dialog.selectStorage(media, extra);
    }
  };

  /* 인증서 관리 화면 탭클릭 */
  NX_Issue_pubUi.certTabClick = function (_this) {
    var obj = $('.pki-tab');
    obj.find('a').removeClass('on');
    $(_this).addClass('on');
    $('#nx-cert-managemnet .cert-content').hide();
    $($(_this).attr('href')).show();
    return false;
  };

  /* 인증서 정보 화면 탭클릭 */
  NX_Issue_pubUi.certTabClick2 = function (_this) {
    var obj = $('.pki-tab-certInfo');
    obj.find('a').removeClass('on');
    $(_this).addClass('on');
    $('#nx-cert-detail-info .cert-content').hide();
    $($(_this).attr('href')).show();
    return false;
  };

  /* 인증서 선택창 Extra Value id값 리턴 */
  NX_Issue_pubUi.extraValueIdReturn = function (_this) {
    //NX_Issue_pubUi.certSelectRow();
    SelectMediaInfo.setExtraValue(_this.id);
  };

  /* 인증서 선택창 확인 버튼  */
  NX_Issue_pubUi.selectCertConfirm = function () {
    var media = SelectMediaInfo.getMediaType();
    var extra = SelectMediaInfo.getExtraValue();
    var Logic_flag = processLogic.getProcessLogic();

    //갱신창이 열려있는 경우 닫기
    $('#nx-error-cert-update').fadeOut('fast');

    // 인증서 선택 유무 확인
    var selectIndex = certListInfo.getCertListIndex();
    if (selectIndex === '') {
      alert(NX_WEBUI_SELECT_TEXT_5);
      return;
    }

    // 삭제 기능인 경우
    if (Logic_flag === 'KICA.MANAGEMENT.DeleteCertificate') {
      NX_Issue_pubUi.deleteCertificate();
    } else {
      var certIndex = certListInfo.getCertListIndex();
      var pwd = '';
      if (NOS_F === true) {
        // 잉카 키보드보안 동작하는 경우
        var nosData = npPfsCtrl.GetReplaceField('formRe', 'RePwName'); //NOS
        var nosTable = npPfsCtrl.GetResultField('formRe', 'RePwName'); //NOS
        pwd = nosTable + '|' + nosData; //  구분자 '|'
      } else if (RAON_F === true) {
        // 라온 키보드보안 동작하는 경우
        GetEncYT();
        return;
      } else if (AHNLAB_F === true) {
        // 안랩 키보드보안 동작하는 경우
        if (media === 'BIOHSM' || media === 'HSM') {
          pwd = 'ahnlab' + '|' + aASTx_windowid + '|' + aASTx_formid + '|' + $ASTX2.getE2EInputID(document.getElementById('nx_cert_pin'));
        } else {
          pwd = 'ahnlab' + '|' + aASTx_windowid + '|' + aASTx_formid + '|' + $ASTX2.getE2EInputID(document.getElementById('certPwd')); //aOriInput은 password 필드 name입니다.
        }
      } else if (CUSTENC === true) {
        //새로운 키보드 보안 동작하는 경우
        pwd = 'easykeytec' + '|' + document.getElementById('certEncPwd').value;
      } else {
        pwd = $('#certPwd').val();
        if (pwd === '' && (media !== 'BIOHSM' || media !== 'HSM')) {
          alert(NX_WEBUI_SELECT_TEXT_8);
          $('#certPwd').focus();
          return;
        } else if (pwd === '' && (media === 'BIOHSM' || media === 'HSM')) {
          alert(NX_WEBUI_SELECT_TEXT_10);
          $('#certPwd').focus();
          return;
        }
      }

      // 관리 기능인 경우
      if (Logic_flag === 'KICA.MANAGEMENT.CheckPassword' || Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
        CertManagement.init();
        CertManagement.setPwd(pwd);
      }

      var tmp = certListInfo.getCertID();
      var certID = '';

      if (tmp === '' || tmp === null || tmp === undefined) {
        certID = '';
      } else {
        certID = tmp;
      }

      Dialog.selectCertificate(certIndex, pwd, certID);
    }
  };

  /* 새로운 키보드 암호화 모듈 처리함수*/
  NX_Issue_pubUi.pwdEnc = function (objInput, sEnterText) {
    if (objInput.id == 'certPwd') {
      document.getElementById('certEncPwd').value = sEnterText;
    }
  };

  /* 인증서 선택창 취소 버튼  */
  NX_Issue_pubUi.selectCertCancel = function () {
    // 인증서 선택창에서 선택한 인증서 Index 초기화
    certListInfo.setCertListIndex('');
    //갱신창이 열려있는 경우 닫기
    $('#nx-error-cert-update').fadeOut('fast');
    initTextAll();
    //hideNXBlockLayer();
    $('#nx-cert-select').hide();
    $('#nx-pki-ui-wrapper').hide();
    window.callback("CANCEL");
  };

  /***********************************************
     인증서 보기 버튼
     ************************************************/
  NX_Issue_pubUi.viewCertInfo = function () {
    var selectCertIndex = certListInfo.getCertListIndex();
    if (selectCertIndex !== '') {
      NXviewCertDetailInfomation(selectCertIndex);
      $('#nx-cert-detail-info2').addClass('on');
      $('#certContent_01').show();

      setTimeout(function () {
        $('#nx-cert-detail-info button').focus();
      }, 100);
    } else {
      alert(NX_ISSUE_PUB_TEXT_34);
    }
  };
  /* 인증서 정보창 닫기 버튼  */
  NX_Issue_pubUi.viewCertInfoConfirm = function () {
    $('#nx-cert-detail-info').hide();
    $('#nx-cert-select').show();
    $('#NXcertList tr.on').focus();
  };

  /* 휴대폰 저장소 */
  NX_Issue_pubUi.selectPhoneSevice = function () {
    SelectMediaInfo.setExtraValue('UBIKEY');
    var media = SelectMediaInfo.getMediaType();
    var extra = SelectMediaInfo.getExtraValue();

    $('.cert-location-area .pki-wrap3').fadeOut('fast');

    // 인증서 리스트 호출
    Dialog.selectStorage(media, extra);
  };

  /**############################################################ 발급/관리 모듈 전용 ###########################################################**/

  /***********************************************
     발급 모듈 : 관리버튼
     ************************************************/
  NX_Issue_pubUi.managemnetPageOpen = function () {
    //인증서 선택창 hide
    $('#nx-cert-select').hide();
    //관리창 show
    $('#nx-cert-managemnet').show();
    setTimeout(function () {
      $('#nx-cert-managemnet #certContent_03 button')[0].focus();
    }, 500);
  };

  /***********************************************
    발급 모듈 : 관리창 확인 버튼
    ************************************************/
  NX_Issue_pubUi.managemnetPageOpenConfirm = function () {
    //관리창 show
    $('#nx-cert-managemnet').hide();
    //인증서 선택창 hide
    $('#nx-cert-select').show();
    $('#NXcertList tr.on').focus();
  };

  /***********************************************
     발급 모듈 : 인증서 삭제 버튼
    ************************************************/
  NX_Issue_pubUi.deleteCertificate = function () {
    var certPwd = $('#certPwd').val();
    var selectCertIndex = certListInfo.getCertListIndex();
    var certID = certListInfo.getCertID();
    if (selectCertIndex !== '' && certPwd !== '') {
      // 인증서 삭제 로직 설정
      processLogic.setProcessLogic('KICA.MANAGEMENT.DeleteCertificate');
      Dialog.selectCertificate(selectCertIndex, certPwd, certID);
    } else {
      initTextAll();
      alert(NX_ISSUE_PUB_TEXT_17);
    }
  };

  /* 인증서 삭제 예 클릭시 */
  NX_Issue_pubUi.deleteCertConfirm = function () {
    var certPwd = $('#certPwd').val();
    var selectCertIndex = certListInfo.getCertListIndex();
    var certID = certListInfo.getCertID();

    initTextAll();
    $('#nx-cert-delete').hide();
    CertManagement.deleteCertIssue(selectCertIndex, certPwd, certID);
  };

  /* 인증서 삭제 아니오 클릭시 */
  NX_Issue_pubUi.deleteCertCancel = function () {
    initTextAll();
    $('#nx-cert-delete').hide();
    $('#nx-cert-select').show();
    alert(NX_WEBUI_ALERT_MESSAGE_1);
  };

  ///***********************************************
  // 발급 모듈 : 인증서 폐지 버튼
  //************************************************/
  NX_Issue_pubUi.revokeCertificate = function () {
    var certPwd = $('#certPwd').val();
    var selectCertIndex = certListInfo.getCertListIndex();
    var certID = certListInfo.getCertID();
    if (selectCertIndex !== '' && certPwd !== '') {
      // 인증서 폐지 로직 설정
      processLogic.setProcessLogic('KICA.ISSUE.RevokeCert');
      Dialog.selectCertificate(selectCertIndex, certPwd, certID);
    } else {
      alert(NX_ISSUE_PUB_TEXT_17);
    }
  };

  ///* 인증서 폐지 예 클릭시 */
  NX_Issue_pubUi.revokeCertConfirm = function () {
    initTextAll();
    $('#nx-cert-revoke').hide();
    issueCertificate.revokeCert();
  };

  ///* 인증서 폐지 아니오 클릭시 */
  NX_Issue_pubUi.revokeCertCancel = function () {
    initTextAll();
    $('#nx-cert-revoke').hide();
    $('#nx-cert-select').show();
    $('#NXcertList tr.on').focus();
  };

  /***********************************************
     발급 모듈 :  인증서 발급&재발급&갱신&복사 타켓 미디어 선택시 이벤트 시작
    ************************************************/
  /* 타켓 미디어 Extra Value id값 리턴 */
  NX_Issue_pubUi.IssueExtraValueIdReturn = function (_this) {
    TargetMediaInfo.setExtraValue(_this.id);
  };

  /* 발급 대상 미디어창 추가매체 클릭시 */
  NX_Issue_pubUi.moreSaveMediaShow3 = function (media, extra) {
    if (media !== 'USIM') {
      $('.cert-location-area .pki-wrap4').fadeIn('fast');
    } else {
      NX_Issue_pubUi.moreSaveMediaHide();
    }
    TargetMediaInfo.setMediaType(media); //미디어 정보 Set

    if (InsertNullCheck(extra) === true || extra === 'NULL') {
      extra = '';
    }

    TargetMediaInfo.setExtraValue(extra);
    //추가 정보(드라이브명 등)를 얻기 위한 함수 호출
    targetMediaFlag.init();
    targetMediaFlag.setFlag(true);

    if (media !== 'USIM') {
      NX_selectStorage(media, extra);
    } else {
      // USIM site code set
      TargetMediaInfo.setExtraValue(USIM_SITECODE);

      var selectMedia_noti = NX_TARGET_DIALOG_TEXT_13 + ' : ' + NX_TARGET_DIALOG_TEXT_4;
      document.getElementById('targetMedia_noti').innerHTML = selectMedia_noti;
      NX_selectStorage(media, extra);
    }
  };

  NX_Issue_pubUi.moreSaveMediaHide3 = function () {
    var media = TargetMediaInfo.getMediaType();
    var extra = TargetMediaInfo.getExtraValue();

    var selectMedia_noti = '';
    if (media === 'USB') {
      selectMedia_noti = NX_ISSUE_PUB_TEXT_2 + ' : ' + NX_ISSUE_PUB_TEXT_19 + ', ' + extra;
    }
    if (media === 'HSM') {
      selectMedia_noti = NX_ISSUE_PUB_TEXT_2 + ' : ' + NX_ISSUE_PUB_TEXT_20 + ', ' + extra;
    }
    if (media === 'BIOHSM') {
      var Tmp_BioTokenInfo = '';
      Tmp_BioTokenInfo = extra.split('|');
      extra = Tmp_BioTokenInfo[1];
      TargetMediaInfo.setExtraValue(extra);

      selectMedia_noti = NX_ISSUE_PUB_TEXT_2 + ' : ' + NX_ISSUE_PUB_TEXT_21 + ', ' + extra;
    }
    if (media === 'USIM') {
      selectMedia_noti = NX_ISSUE_PUB_TEXT_2 + ' : ' + NX_ISSUE_PUB_TEXT_22 + ', ' + extra;
    }
    if (media === 'SECUREDISK') {
      selectMedia_noti = NX_ISSUE_PUB_TEXT_2 + ' : ' + NX_ISSUE_PUB_TEXT_22_1 + ', ' + extra;
    }
    if (media !== '') {
      document.getElementById('targetMedia_noti').innerHTML = selectMedia_noti;
    }

    //extraValue 선택 안했을 시 알림
    if (extra === '') {
      alert(NX_ISSUE_PUB_TEXT_16);
    } else {
      $('.cert-location-area .pki-wrap4').fadeOut('fast');
    }
  };

  /* 저장매체 선택 확인 버튼  */
  NX_Issue_pubUi.selectMediaConfirm = function () {
    //저장매체가 PIN번호를 요구하는 저장매체인지 일반 비밀번호를 요구하는 저장매체 인지 분기처리
    var mediaType = TargetMediaInfo.getMediaType();
    var extraValue = TargetMediaInfo.getExtraValue();

    if (mediaType !== '' && extraValue !== '') {
      if (mediaType === 'HSM' || mediaType === 'BIOHSM') {
        //PIN 번호를 입력하는 경우
        $('#nx-targetMedia-select').hide();
        $('#nx-secureToken-pin-insert').show();
      } else {
        //인증서 복사인 경우 암호입력을 별도로 받지 않는다.
        var Logic_flag = processLogic.getProcessLogic();
        if (Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
          _copyMedia.disabled = false;
          _copyMedia = null;

          $('#nx-targetMedia-select').hide();
          CertManagement.copyCert(mediaType, extraValue, '');
        } else {
          //인증서 암호 입력인 경우
          $('#nx-targetMedia-select').hide();
          $('#nx-pwd-insert').show();

          setTimeout(function () {
            $('#nx_issue_cert_pw').focus();
          }, 100);
        }
      }
    } else {
      alert(NX_ISSUE_PUB_TEXT_23);
    }
  };

  /* 저장매체 선택 취소 버튼  */
  NX_Issue_pubUi.selectMediaCancel = function () {
    $('#nx-targetMedia-select').hide();
    // WebUI 백그라운드 화면 버튼 클릭 차단 해제
    hideNXBlockLayer();

    var Logic_flag = processLogic.getProcessLogic();
    if (Logic_flag.indexOf('CopyCert') !== -1) {
      _copyMedia.disabled = false;
      _copyMedia = null;
      alert(NX_WEBUI_COPY_TEXT_9);
    }
  };

  /* 비밀번호 확인 버튼  */
  NX_Issue_pubUi.insertPWConfirm = function () {
    //인증서 비밀번호 확인
    var issueCertPW = $('#nx_issue_cert_pw').val();
    var issueCertPWConfirm = $('#nx_issue_cert_pw_confirm').val();

    // 초기화
    initTextAll();

    //비밀번호 검증 로직 필요
    var res_ValidPW = validPassword(issueCertPW, issueCertPWConfirm);
    if (res_ValidPW === 'result_OK') {
      //암호입력창 제거
      $('#nx-pwd-insert').hide();

      var Logic_flag = processLogic.getProcessLogic();

      // 발급 관련 진행인 경우
      if (Logic_flag.indexOf('ISSUE') !== -1) {
        issueCertificate.setPw(issueCertPW);
        issueCertificate.setConfirmPw(issueCertPWConfirm);

        // 진행중 창 표시
        $('.nx-issue-ing-alert-head-msg').remove();
        var headMessage = '';
        var alertMessage = '';
        headMessage += '<div class="nx-issue-ing-alert-head-msg">';
        if (Logic_flag.indexOf('KICA.ISSUE.IssueCert') !== -1) {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_24 + '</h1>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_25 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        } else if (Logic_flag.indexOf('KICA.ISSUE.ReissueCertificate') !== -1) {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_26 + '</h1>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_27 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        } else if (Logic_flag === 'KICA.ISSUE.RenewCertificate') {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_28 + '</h>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_29 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        }
        headMessage += '</div>';
        $('#nx-issue-ing-alert-head').append(headMessage);
        $('#issue-ing-alert-message').remove();
        $('#nx-issue-ing-alert-box').append(alertMessage);

        setTimeout(function () {
          $('#nx-issue-ing-alert').show();
        }, 100);
      } else {
        //인증서 발급 이외의 비밀번호 사용인 경우 : 비밀번호 변경
        CertManagement.init();
        CertManagement.setNEW_NXPWD(issueCertPW);
        CertManagement.setNEW_NXPWD_CONFIRM(issueCertPWConfirm);
      }

      //인증서 발급&복사 실행
      setTimeout(function () {
        if (Logic_flag.indexOf('KICA.ISSUE.IssueCert') !== -1) {
          //인증서 발급
          issueCertificate.issueCert();
        } else if (Logic_flag.indexOf('KICA.ISSUE.ReissueCertificate') !== -1) {
          //인증서 재발급
          issueCertificate.reIssueCert();
        } else if (Logic_flag.indexOf('KICA.ISSUE.RenewCertificate') !== -1) {
          //인증서 갱신
          issueCertificate.updateCert();
        } else if (Logic_flag === 'KICA.MANAGEMENT.ChangePassword') {
          CertManagement.changePassword();
        }
      }, 200);
    } else {
      alert(res_ValidPW);
      $('#nx-pki-ui-wrapper').show();
    }
  };

  /* 비밀번호 취소 버튼  */
  NX_Issue_pubUi.insertPWCancel = function () {
    // 초기화
    initTextAll();
    $('#nx-pwd-insert').hide();
    // WebUI 백그라운드 화면 버튼 클릭 차단 해제
    hideNXBlockLayer();

    var Logic_flag = processLogic.getProcessLogic();
    if (Logic_flag.indexOf('IssueCert') !== -1) {
      // 인증서 발급
      alert(NX_WEBUI_ALERT_MESSAGE_2);
    } else if (Logic_flag.indexOf('ReissueCertificate') !== -1) {
      // 인증서 재발급
      alert(NX_WEBUI_ALERT_MESSAGE_3);
    } else if (Logic_flag.indexOf('RenewCertificate') !== -1) {
      // 인증서 갱신
      alert(NX_WEBUI_ALERT_MESSAGE_4);
    } else if (Logic_flag.indexOf('ChangePassword') !== -1) {
      // 비밀번호 변경
      alert(NX_WEBUI_ALERT_MESSAGE_5);
    }
  };

  /* PIN 확인 버튼  */
  NX_Issue_pubUi.insertPINConfirm = function () {
    var Logic_flag = processLogic.getProcessLogic();

    //PIN 비밀번호 확인
    var issueCertPIN = $('#nx_issue_cert_pin').val();

    if (issueCertPIN !== '') {
      //PIN 입력창 제거
      $('#nx-secureToken-pin-insert').hide();

      // 초기화
      initTextAll();

      //인증서 복사인 경우
      if (Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
        CertManagement.init();
        CertManagement.setPwd(issueCertPIN);
      } else {
        issueCertificate.setPw(issueCertPIN);
        issueCertificate.setConfirmPw(issueCertPIN);

        // 진행중 창 표시
        $('.nx-issue-ing-alert-head-msg').remove();
        var headMessage = '';
        var alertMessage = '';
        headMessage += '<div class="nx-issue-ing-alert-head-msg">';
        if (Logic_flag.indexOf('KICA.ISSUE.IssueCert') !== -1) {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_24 + '</h1>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_25 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        } else if (Logic_flag.indexOf('KICA.ISSUE.ReissueCertificate') !== -1) {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_26 + '</h1>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_27 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        } else if (Logic_flag === 'KICA.ISSUE.RenewCertificate') {
          headMessage += '<h1>' + NX_ISSUE_PUB_TEXT_28 + '</h>';
          alertMessage =
            '<div id="issue-ing-alert-message" class="gray-box2"><p class="txt-c"><span class="inline-tit">' +
            NX_ISSUE_PUB_TEXT_29 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_35 +
            '<br><br>' +
            NX_ISSUE_PUB_TEXT_36 +
            '</span></p></div>';
        }
        headMessage += '</div>';
        $('#nx-issue-ing-alert-head').append(headMessage);
        $('#issue-ing-alert-message').remove();
        $('#nx-issue-ing-alert-box').append(alertMessage);

        setTimeout(function () {
          $('#nx-issue-ing-alert').show();
        }, 100);
      }

      //인증서 발급 실행
      setTimeout(function () {
        if (Logic_flag === 'KICA.ISSUE.IssueCert') {
          //인증서 발급
          issueCertificate.issueCert();
        } else if (Logic_flag === 'KICA.ISSUE.ReissueCertificate') {
          //인증서 재발급
          issueCertificate.reIssueCert();
        } else if (Logic_flag === 'KICA.ISSUE.RenewCertificate') {
          //인증서 갱신
          issueCertificate.updateCert();
        } else if (Logic_flag === 'KICA.MANAGEMENT.CopyCert') {
          //인증서 복사
          var mediaType = TargetMediaInfo.getMediaType();
          var extraValue = TargetMediaInfo.getExtraValue();
          var overWrite = '';
          CertManagement.copyCert(mediaType, extraValue, overWrite);
        }
      }, 200);
    } else {
      alert(NX_ISSUE_PUB_TEXT_30);
    }
  };

  /* PIN 취소 버튼  */
  NX_Issue_pubUi.insertPINCancel = function () {
    // 초기화
    initTextAll();
    // PIN 입력창 제거
    $('#nx-secureToken-pin-insert').hide();
    // WebUI 백그라운드 화면 버튼 클릭 차단 해제
    hideNXBlockLayer();
  };

  /* 인증서 발급 완료 확인 버튼 */
  NX_Issue_pubUi.issueCertConfirm = function () {
    $('#nx-issue-success-alert').hide();
    hideNXBlockLayer();

    var callFuncName = issueFuncName.getFuncName();
    SecuKitNX_Result(callFuncName);
  };

  /* 인증서 발급 실패 확인 버튼 */
  NX_Issue_pubUi.issueCertFailConfirm = function () {
    $('#nx-issue-fail-alert').hide();
    hideNXBlockLayer();

    var callFuncName = issueFuncName.getFuncName();
    SecuKitNX_Result(callFuncName);
  };

  /***********************************************
     인증서 관리 
     ************************************************/
  /* 인증서 내보내기 버튼 클릭 */
  NX_Issue_pubUi.exportPFX = function () {
    // 내보내기 파일명 초기화
    $('#exportP12Name').val = '';
    // 인증서 관리 창 제거
    $('#nx-cert-managemnet').hide();
    // 인증서 선택창 비밀번호 입력창 초기화
    initTextAll();
    // 인증서 내보내기 파일 지정 화면 표시
    $('#nx-cert-management-exportPFX-fileName').show();

    setTimeout(function () {
      $('#exportP12Name').focus();
    }, 500);
  };

  /* 인증서 내보내기 파일 이름 지정 확인 버튼 */
  NX_Issue_pubUi.exportPFXFileNameConfirm = function () {
    // 인증서 내보내기 파일 지정 화면 제거
    $('#nx-cert-management-exportPFX-fileName').hide();
    // 인증서 내보내기 키 분배용 인증서 확인 창 표시
    $('#nx-cert-management-exportPFX-withKm').show();
  };

  /* 인증서 내보내기 파일 이름 지정 취소 버튼 */
  NX_Issue_pubUi.exportPFXFileNameCancel = function () {
    // 입력한 p12 파일명 삭제
    $('#exportP12Name').val('');
    // 인증서 내보내기 파일 지정 화면 제거
    $('#nx-cert-management-exportPFX-fileName').hide();
    // 인증서 관리창 표시
    $('#nx-cert-managemnet').show();

    setTimeout(function () {
      $('.cert-info-box2 ul>li:first-child button').focus();
    }, 300);
  };

  /* 인증서 내보내기 개인키 포함 여부 확인 버튼 */
  NX_Issue_pubUi.exportPFXWithKmConfirm = function () {
    // 인증서 내보내기 키 분배용 인증서 확인 창 제거
    $('#nx-cert-management-exportPFX-withKm').hide();
    // 인증서 내보내기 로직 세팅
    var flag = 'KICA.MANAGEMENT.EXPORT_PFX';
    processLogic.setProcessLogic(flag);
    // 인증서 선택 알림창 표시
    alert(NX_ISSUE_PUB_TEXT_31);
    // 인증서 선택창 표시
    $('#nx-cert-select').show();
    $('#NXcertList tr.on').focus();
  };

  /* 인증서 내보내기 개인키 포함 여부 취소 버튼 */
  NX_Issue_pubUi.exportPFXWithKmCancel = function () {
    // 입력한 p12 파일명 삭제
    $('#exportP12Name').val('');
    // 인증서 내보내기 키 분배용 인증서 확인 창 제거
    $('#nx-cert-management-exportPFX-withKm').hide();
    // 인증서 관리창 표시
    $('#nx-cert-managemnet').show();
  };

  /* 인증서 가져오기 버튼 클릭 */
  NX_Issue_pubUi.importPFX = function () {
    // 가져오기 정보 초기화
    $('#importPFXFileName').val = '';
    $('#importPFXPwd').val = '';
    // 인증서 관리창 제거
    $('#nx-cert-managemnet').hide();
    // 인증서 가져오기 화면 표시
    $('#nx-cert-management-importPFX').show();

    setTimeout(function () {
      $('.file-type-wrap button').focus();
    }, 300);
  };

  /* 인증서 가져오기 확인 버튼 클릭 */
  NX_Issue_pubUi.importPFXConfirm = function () {
    // 인증서 가져오기 화면 제거
    $('#nx-cert-management-importPFX').hide();
    // 인증서 가져오기 로직 세팅
    var flag = 'KICA.MANAGEMENT.IMPORT_PFX';
    processLogic.setProcessLogic(flag);
    // 가져오기 진행
    NX_branchLogic_ISSUE();
  };

  /* 인증서 가져오기 취소 버튼 클릭 */
  NX_Issue_pubUi.importPFXCancel = function () {
    // 인증서 가져오기 화면 제거
    $('#nx-cert-management-importPFX').hide();
    alert(NX_CERT_MANAGEMENT_TEXT_32);
    // 인증서 관리창 표시
    $('#nx-cert-managemnet').show();

    setTimeout(function () {
      $('.cert-info-box2 ul>li:nth-child(2) button').focus();
    }, 300);
  };

  /* 인증서 비밀번호 확인 성공 */
  NX_Issue_pubUi.checkPasswordConfirm = function () {
    // 인증서 비밀번호 확인 성공창 제거
    $('#nx-cert-checkPassword').hide();
    hideNXBlockLayer();
  };

  /* 인증서 중복 복사 확인 버튼 */
  NX_Issue_pubUi.copyCertDuplicationConfirm = function () {
    // 선택한 값 가져오기
    var OverWriteFlag_Y = document.getElementById('certChangeYes');
    var OverWriteFlag_N = document.getElementById('certChangeNo');

    if (OverWriteFlag_Y.checked) {
      var mediaType = TargetMediaInfo.getMediaType();
      var extraValue = TargetMediaInfo.getExtraValue();
      var overWrite = 'Y';
      CertManagement.copyCert(mediaType, extraValue, overWrite);
    }

    if (OverWriteFlag_N.checked) {
      CertManagement.init();
      alert(NX_ISSUE_PUB_TEXT_33);
      $('#nx-cert-copy-duplication').hide();
      hideNXBlockLayer();
    } else {
      // 화면 제거
      $('#nx-cert-copy-duplication').hide();
    }
  };

  /* 인증서 중복 복사 취소 버튼 */
  NX_Issue_pubUi.copyCertDuplicationCancel = function () {
    // 인증서 복사를 진행하지 않고 화면 제거
    $('#nx-cert-copy-duplication').hide();
    // WebUI 백그라운드 화면 버튼 클릭 차단 해제
    hideNXBlockLayer();

    var Logic_flag = processLogic.getProcessLogic();
    if (Logic_flag.indexOf('CopyCert') !== -1) {
      alert(NX_WEBUI_COPY_TEXT_9);
    }
  };

  /* 인증서 신원확인 확인 버튼 */
  NX_Issue_pubUi.VerifyIdentifyConfirm = function () {
    var tmp = $('#chkSSN').val();
    CertManagement.setNXSSN(tmp); //입력된 ssn set

    // 초기화
    initTextAll();

    $('#nx-cert-VerifyIdentify').hide();
    CertManagement.verifyIdentity();
  };

  /* 인증서 신원확인 취소 버튼 */
  NX_Issue_pubUi.VerifyIdentifyCancel = function () {
    alert(NX_CERT_MANAGEMENT_TEXT_31);
    // 초기화
    initTextAll();
    $('#nx-cert-VerifyIdentify').hide();
    // WebUI 백그라운드 화면 버튼 클릭 차단 해제
    hideNXBlockLayer();
  };

  /* 문서로딩시 실행 */
  $(document).ready(function () {
    NX_Issue_pubUi.placeholder();
  });

      /*인증서 이동복사(PC2PHONE) 확인*/
	NX_Issue_pubUi.pc2phoneConfirm = function() {
		var authCode1 = document.getElementById("pc2phone_auth_1").value;
		var authCode2 = document.getElementById("pc2phone_auth_2").value;
		var authCode3 = document.getElementById("pc2phone_auth_3").value;

		if ((authCode1.length === 4) && (authCode2.length === 4) && (authCode3.length === 4)) {
			$('#nx-cert-pc2phone').hide();
			$('#nx-pki-ui-wrapper').hide();
			$('#nx-background-block-layer').hide();

			var tmpAuthCode = authCode1 + authCode2 + authCode3;
			// 인증번호 저장
			nxPC2Phone.setAuthCode(tmpAuthCode);
			KICA_RELAY.exportP12ToData();
		} else {
			return;
		}
	}

	/*인증서 이동복사(PC2PHONE) 취소*/
	NX_Issue_pubUi.pc2phoneCancel = function() {

		document.getElementById("pc2phone_auth_1").value = "";
		document.getElementById("pc2phone_auth_2").value = "";
		document.getElementById("pc2phone_auth_3").value = "";

		$('#nx-cert-pc2phone').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();
	}

	/*인증서 이동복사(PHONE2PC) 인증번호 확인*/
	NX_Issue_pubUi.phone2pc_Auth_Confirm = function() {
		$('#nx-cert-phone2pc-authcode').hide();
		$('#nx-cert-phone2pc-pwd').show();
	}

	/*인증서 이동복사(PHONE2PC) 인증번호 취소*/
	NX_Issue_pubUi.phone2pc_Auth_Cancel = function() {
		$('#nx-cert-phone2pc-authcode').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();
	}

	/*인증서 이동복사(PHONE2PC) 비밀번호입력 확인*/
	NX_Issue_pubUi.phone2pc_pwd_Confirm = function() {

		var pwdTmp = document.getElementById("phone2pc_pwd").value;
		if (pwdTmp !== "") {
			$('#nx-cert-phone2pc-pwd').hide();
			nxPhone2PC.init();
			nxPhone2PC.setPhone2PCPwd(pwdTmp);

			pwdTmp = "";
			document.getElementById("phone2pc_pwd").value = "";

			$('#nx-targetMedia-select').show();
		}
	}

	/*인증서 이동복사(PHONE2PC) 비밀번호입력 취소*/
	NX_Issue_pubUi.phone2pc_pwd_Cancel = function() {
		$('#nx-cert-phone2pc-pwd').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();
	}

	/*인증서 이동복사(PHONE2PC) 성공 알림창 확인버튼*/
	NX_Issue_pubUi.phone2pc_success_Confirm = function() {
		$('#nx-relay-success-alert').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();
	}

	NX_Issue_pubUi.phone2pc_fail_Confirm = function() {
		$('#nx-relay-fail-alert').hide();
		$('#nx-pki-ui-wrapper').hide();
		$('#nx-background-block-layer').hide();
	}

  /* 팝업창 리스트 엔터키 활성화 */
  NX_Issue_pubUi.EnterOnSublayerList = function (e) {
    var keycode = e.key || e.which || e.charCode || e.keyCode;
    if (keycode === 'Enter' || keycode === 13) {
      if (e.target.id === 'phoneUbikey') NX_Issue_pubUi.selectPhoneSevice();
      else NX_Issue_pubUi.moreSaveMediaHide2();
    }
  };

  /* 핀번호 입력 엔터키 활성화 */
  NX_Issue_pubUi.EnterOnPINNumber = function (e) {
    var keycode = e.key || e.which || e.charCode || e.keyCode;
    if (keycode === 'Enter' || keycode === 13) NX_Issue_pubUi.moreSaveMediaHide7();
  };
}

// 저장매체 선택시 Hold 효과를 위한 Function
//$(function () {
function NXStopMediaSelect() {
  $('#MediaSet_1>li>button').click(function (e) {
    e.stopPropagation();
    if (!$(this).hasClass('active')) {
      $('#MediaSet_1>li>button.active').removeClass('active');
      $(this).addClass('active');
    }
  });

  $('#MediaSet_1-sub>button').click(function (e) {
    e.stopPropagation();
    if (!$(this).hasClass('active')) {
      $('#MediaSet_1-sub>button.active').removeClass('active');
      $(this).addClass('active');
    }
  });
}

// 타겟 저장매체 선택시 Hold 효과를 위한 Function
// 호출은 targetDialog.js에서
function NXStopMediaSelect2() {
  $('#MediaSet_2>li>button').click(function (e) {
    e.stopPropagation();
    if (!$(this).hasClass('active')) {
      $('#MediaSet_2>li>button.active').removeClass('active');
      $(this).addClass('active');
    }
  });

  $('#MediaSet_2-sub>button').click(function (e) {
    e.stopPropagation();
    if (!$(this).hasClass('active')) {
      $('#MediaSet_2-sub>button.active').removeClass('active');
      $(this).addClass('active');
    }
  });
}

// 라온 키보드보안 실행 함수
function GetEncYT() {
  var pubkey =
    '-----BEGIN CERTIFICATE-----MIIDPTCCAiWgAwIBAgIJAOYjCX4wgWNVMA0GCSqGSIb3DQEBCwUAMGcxCzAJBgNVBAYTAktSMR0wGwYDVQQKExRSYW9uU2VjdXJlIENvLiwgTHRkLjEaMBgGA1UECxMRUXVhbGl0eSBBc3N1cmFuY2UxHTAbBgNVBAMTFFJhb25TZWN1cmUgQ28uLCBMdGQuMB4XDTE2MDUyNDAyMzEwMVoXDTM2MDUxOTAyMzEwMVowOTELMAkGA1UEBhMCS1IxDzANBgNVBAoTBkhhbndoYTEZMBcGA1UEAxMQd3d3LnBpbmVjbHViLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALpvn2sDKsEwJDwej9u3343M/rSyWz1+dfmfmmRFJPZVpYAXpxNxuUlDnonVgcK5km18DFOq27pcx3NhypJdIZ87L/9jkkgYuL9cTdC7VlS3iprZ5GKDTcCtqucmMKrK+b4TSyqGgxKTWsfFX2cuiNFI6YNiUDBG00LMxmgeUvMlwme8KhlI2qe3qQ1sQgljmThe0wYtIIVXouLHYGZ/q9v1W4ap7jkWYxrrvpxV6XTRLQdflPjvoB52mipjKZ9djh0gL05ZJUIU2Di89UiqX6cYDaPStoIdhhAF4ba6b7C3r9cQEmQoYwtgcuxbcYqgeH3np8NyZA7NYybDE/buky0CAwEAAaMaMBgwCQYDVR0TBAIwADALBgNVHQ8EBAMCBeAwDQYJKoZIhvcNAQELBQADggEBAFWCzVKe50JuX4cILaYNdV9gL88UbcUbIaUCbkcXV+o9Bv+oDUxJdJqHiFhhduPW2ztp/1cdI0Z4f5VZ7CUMalK4jPFQyQfOO2nJa575MIrdEZ/sySmNkYz5ugq+EeGWqKD9qLlAwHTCQI6fZ6jsXOu+RMNt4kBTp6PKjsi8DzzRRMrh6tSqwpzSECO4ck9V0vj2Owgz/D6vd5cREqg+xpw8TIis5Mk5HE5roWOMHHaAJC/mY8eMMl0MTlpzu55sy3y5kJVpaTJVdYanmI6xQCHRK2QTR1LKCcrirPGuA3Szs3yit1QDW1udW1xnHAa8GIaHh4WZusGZYCAZzW821n4=-----END CERTIFICATE-----';
  var frmName = 'formRe';
  var eleName = 'certPwd';
  TK_GetEncYT(frmName, eleName, pubkey, function (result) {
    var tmp = result;
    var pwd = 'raon' + '|' + tmp;
    tmp = '';

    // 초기화
    initTextAll();
    TK_Clear('formRe', 'certPwd'); // 라온시큐어 키보드보안 메모리 해제

    var certID = certListInfo.getCertID();
    var certIndex = certListInfo.getCertListIndex();

    Dialog.selectCertificate(certIndex, pwd, certID); //result : 치환된 비밀번호
  });
}
