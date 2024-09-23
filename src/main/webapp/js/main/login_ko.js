/**
 * 협렧업체 페잉지의 Login 관련
 * 2016.08.09, Choi JongHyeok
 *
 * 의존성 : (emro)util.js
 */

var loginDir = "login/sp/";

/*
 * 2016.08.09, Choi JongHyeok
 * ID 찾기 팝업창
 */
function popupFindId() {
	UT.popupJsp(loginDir + "findId.do", null, 490, 355, "", {
		titleText: "아이디 찾기"
	});
}

/*
 * 2016.08.09, Choi JongHyeok
* PW 찾기 팝업창
 */
function popupFindPw() {
	UT.popupJsp(loginDir + "findPassword.do", null, 490, 355, "", {
		titleText: "비밀번호 초기화"
	});
}

/*
 * 2016.08.09, Choi JongHyeok
 * PW 창 리다이렉트
 */
function redirectFindPw() {
	location.href= loginDir + "findPassword.do";
}

/*
 * 2016.08.09, Choi JongHyeok
 * PW 창 리다이렉트
 */
function redirectFindPwEn() {
	location.href= loginDir + "findPasswordEn.do";
}

function popupFindIdEn() {
	UT.popupJsp(loginDir + "findIdEn.do", null, 490, 400, "", {
		titleText: "Find ID"
	});
}

/*
 * 2016.08.09, Choi JongHyeok
* PW 찾기 팝업창
 */
function popupFindPwEn() {
	UT.popupJsp(loginDir + "findPasswordEn.do", null, 490, 355, "", {
		titleText: "Reset PW"
	});
}

/*
 * 2016.08.09, Choi JongHyeok
 * PW 창 리다이렉트
 */
function redirectFindPw() {
	location.href= loginDir + "findPasswordEn.do";
}

/*
 * 2016.08.09, Choi JongHyeok
 * PW 창 닫기
 */
function popupClose(popup) {
	if(popup && popup.fire) {
		popup.fire("close");
	}
}



