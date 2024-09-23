//blueSky1
/*-- SMARTsutie v10 : MDI
  -- 1st : 2023.04.10 emro DesignTeam pjsuny ---*/
var blueSkySkin = {
	/* 모든 영역 스타일의 부모 스타일 */
	"default" : {
		/*
		 * 모든 영역의 font가 적용된다. header나 cell에 각각의 폰트를 적용할려면 header-head, body-cell
		 * 등 각각의 영역에서 지정한다.
		 */
		"fontSize" : "13",
		"fontName" : "Pretendard"
	},
	/* 그리드 보더나 트리뷰 라인 등 그리드 자체를 그릴 때 사용. */
	"grid" : {
		"border" : null,
		"paddingRight" : null,
		"paddingTop" : null,
		"paddingBottom" : "10",
	},
	/* 각각의 영역에 세부 스타일을 적용한다. 기본스타일 셋이 되지 않는다.(데이터셀들이 표시되는 body 영역의 바탕색 지정 가능) */
	"body" : {

		"background" : "#ffffffff",

		/* 그리드행들의 바탕색 및 아래쪽 경계선 스타일. */
		"rowRange" : {
			"rows" : [ {
			// "range": "row % 2 == 1",
			// "styles": {
			// "background": "#08000000"
			// }
			} ]
		},

		/* 데이터셀들을 그릴 때 사용되는 기본 스타일셋. */
		"cell" : {
			"borderBottom" : "#ffE4E4E4,1",
			"borderRight" : "#ffE4E4E4,0",
		},
		"merged" : {
			"background" : "#fff8f8f8", /*그리드 리스트 merge*/
			"borderRight" : "#ffE4E4E4,1",
		},
		"selection" : { /* 그리드 데이터 드래그시 백그라운드 오퍼시티 배경색 */
			"background" : "#20ABABAB",
            "border": "#ffABABAB,1", /*테두리*/
		},
	},
	/* GridHeader(컬럼)들을 그릴 때 사용되는 기본 스타일셋. */
		"header" : {

		// 정렬 아이콘
		"sortHandle": {
		  "shapeBorder": "#FF000000", // border
		  "shapeColor": "#FF000000", // background

		  // hover
		  "shapeHoveredBorder": "#FF000000", // border
		  "shapeHoveredColor": "#FF000000", // background
		},
		// 필터 아이콘
		"filterHandle": {
		  // 1.필터(기본)
		  "shapeInactiveBorder": "#FF000000", // border

		  // 2.필터 체크한 경우
		  "shapeBorder": "#FF000000", // border
		  "shapeColor": "#FF000000", // background

		  // hover(필터 체크 여부와 상관없이 공통)
		  "shapeHoveredBorder": "#FF000000", // border
		  "shapeHoveredColor": "#FF000000", // background (필터 체크한 경우만 채워지는 효과 있음.)
		},
		// 정렬 숫자
		"sortOrder": {
		  "color": "#FF000000", // red
		},


		"fontSize" : "13",
		"color" : "#333333",
		"paddingTop" : "5",
		"hoveredColor" : "#FFFFFF",
		"selectedColor" : "#FFFFFF",
		"borderRight" : "#ffC9C9C9,1",
		"borderBottom" : "#ffC9C9C9,1", /* 헤더 보더 bottom색 */
		"borderTop" : "#ffE4E4E4,0", /* 헤더 보더 탑 : custom.css 536 라인 추가*/
		"background" : "#ffF2F2F2", /* 헤더 백그라운드 색 */
		"selectedBackground" : "#ffA0A0A0", /* 헤더 선택된 백그라운드 색 */
		"hoveredBackground" : "#ffA0A0A0", /* 헤더 호버 백그라운드 색 */
		"group" : {
			"fontSize" : "13",
			"color" : "#333333",
			"paddingTop" : "5",
			"hoveredColor" : "#FFFFFF",
			"selectedColor" : "#FFFFFF",
			"borderRight" : "#ffC9C9C9,1",
			"borderBottom" : "#ffC9C9C9,1", /* 헤더 보더 bottom색(구매요청진행상태) */
			"borderTop" : "#ffE4E4E4,0", /* 헤더 보더 탑 */
			"background" : "#ffF2F2F2", /* 헤더 백그라운드 색 */
			"selectedBackground" : "#ffA0A0A0",
			"hoveredBackground" : "#ffA0A0A0",
			// 정렬 아이콘
			"sortHandle": {
			  "shapeBorder": "#FF276EF1", // border
			  "shapeColor": "#FF276EF1", // background

			  // hover
			  "shapeHoveredBorder": "#FF001489", // border
			  "shapeHoveredColor": "#FF001489", // background
			},
			// 필터 아이콘
			"filterHandle": {
			  // 1.필터(기본)
			  "shapeInactiveBorder": "#FF276EF1", // border

			  // 2.필터 체크한 경우
			  "shapeBorder": "#FF276EF1", // border
			  "shapeColor": "#FF276EF1", // background

			  // hover(필터 체크 여부와 상관없이 공통)
			  "shapeHoveredBorder": "#FF276EF1", // border
			  "shapeHoveredColor": "#FF276EF1", // background (필터 체크한 경우만 채워지는 효과 있음.)
			},
			// 정렬 숫자
			"sortOrder": {
			  "color": "#FFff0000", // red
			},
		},
	},
	/* GridFooter 셀을 그릴 때 사용되는 스타일셋. */
	"footer" : {
		"borderRight" : "#ffbbbbbb,1",
		"borderTop" : "#ffbbbbbb,1",
		"background" : "#ffeeeeee",
		"textAlignment" : "near"
	},
	"rowGroup" : {
		/* 그룹행을 그릴 때 사용되는 기본 스타일셋. */
		"header" : {
			"borderRight" : "#ff097ac1,1",
			"borderBottom" : "#ff097ac1,1",
			"borderTop" : "#ffffffff,0",
			"background" : "#ff349cde",
		},
		/* 그룹 footer행(소계)을 그릴 때 사용되는 기본 스타일셋. */
		"footer" : {
			"borderRight" : "#ffc7c4b0,1",
			"borderBottom" : "#ffc7c4b0,1",
			"borderTop" : "#ffffffff,0",
			"background" : "#fffff9d4"
		}
	},
	/* RowIndicator(그리드 왼쪽의 숫자를 나타내는 영역) 셀들을 그릴 때 사용되는 기본 스타일셋. */
	"rowIndicator" : {
		"borderTop" : "#ffE0E0E0,0", /* 헤더 보더 bottom색 있을경우 > 0으로 */
		"borderRight" : "#ffE0E0E0,1",
		"borderLeft" : "#ffE0E0E0,0",
		"borderBottom" : "#ffE0E0E0,1",
		"background" : "#ffffffff",
		"selectedBackground" : "#ffC6C6C6", /* 화살표 배경 색 */
		"hoveredBackground" : "#ffC6C6C6", /* 화살표 hover 배경 색 */
		"color" : "#ff000000",
		"hoveredColor" : "#ffffffff", /* 화살표  hover 색 */
		"shapeColor" : "#ffffffff", /* 화살표  색 */
		"shapeInactiveColor" : "#ffffffff",
		"shapeSelectedColor" : "#ffffffff",
		"shapeHoveredColor" : "#ffffffff",
		"state":{
			"borderRight" :"#ffE0E0E0,1", /* 숫자와 체크박스 사이 cell 라인 색 */
			"borderBottom" : "#ffE0E0E0,1",/* 숫자와 체크박스 사이 cell 라인 색 */
			"background" : "#ffffffff",
		}

	},
	/* CheckBar 셀들을 그릴 때 사용되는 기본 스타일셋. */
	"checkBar" : {
		"borderRight" : "#ffE0E0E0,1",
		"borderBottom" : "#ffE0E0E0,1",
		"background" : "#ffffffff",
		"head" : {
			"color" : "#333333",
			"background" : "#ffF2F2F2" /* 체크박스 헤더 */
		}

	// "shapeColor" : "#ffffff", //체크모양 색
	/**
	 * sc-grid 2.2.7 부터 적용가능 "boxBackground" :"#1f91ff", //박스 배경색 "boxBorder" :
	 * "#1f91ff", //박스 테두리색 "boxInactiveBackground" : "#c2c2c2", //박스 비활성화시 배경색
	 * "boxInactiveBorder" : "#c2c2c2" //박스 비활성화시 테두리색
	 */

	},
	/* 트리의 속성을 변경할때 사용한다. */
	"tree" : {

		/** +,- 상자 색상변경 sc-grid 2.2.7 부터 적용가능 * */
		"expander" : {
			"borderBottom" : null,
		// shapeExpandedColor: '#3b73af', //+
		// shapeCollapsedColor: '#016600' //-
		}

	},

	"dynamicStyles" : {
		// column의 editable 속성이 true일 경우 아래 style이 설정된다.
		"editable" : {
			"styles" : {
				"background" : "#ECFCFC" /* 구매요청 상세 그리드 - 꼬깔 필수 배경색-2020.06.23
											 * SRM>기준정보관리>소싱그룹 관리 :그리드 활성화(정렬)
											 * 백그라운드 색
											 */
			}
		},
		// column의 required 속성이 true일 경우 아래 style이 설정된다.
		"required" : {
			"renderer" : {
				"type" : "icon"
			},
			"styles" : {
				"celltipLocation" : "leftTop"
			// "celltipColor":"#5497e6"
			}
		},
		"icons" : {
			"basicIcons" : {
				"name" : "__icons__",
				"root" : "bower_components/sc-grid/resources/img/",
				"items" : [ "icon_attach.png", "icon_link.png",
						"icon_search.png", "icon_error.png" ]
			},
			"stateIcons" : {
				"name" : 'stateIcons',
				"root" : "bower_components/sc-grid/resources/smartdatagrid/assets/",
				"items" : [ "data_created.png", "data_updated.png",
						"data_deleted.png" ]
			},
		},
		// image column의 imageCls로 사용되는 image 목록
		"imageCls" : {
			"link" : "bower_components/sc-grid/resources/img/icon_link.png",
			"search" : "bower_components/sc-grid/resources/img/icon_search.png",
			"attach" : "bower_components/sc-grid/resources/img/icon_attach.png"
		},
		// column에서 styleName속성으로 아래 등록된 style을 사용할 수 있다.
		"columnStyles" : {
			"link" : {
				"cursor" : "pointer",
				"styles" : {
					"fontBold" : true,
					"color" : "#276EF1" /* 컬럼 링크 텍스트 색 */,
				}
			}
		}
	/**
	 * checkbar 이미지 표현 sc-grid 2.2.8 부터 적용가능 "selectionBarIcons": [
	 * {defaultIcon: "gridicons/default-checkbox.png"}, {checkedIcon:
	 * "gridicons/check-checkbox.png"}, {defaultInactiveIcon:
	 * "gridicons/default-inactive-checkbox.png"}, {checkedInactiveIcon:
	 * "gridicons/check-inactive-checkbox.png"}, {radioDefaultIcon:
	 * "gridicons/default-radio.png"}, {radioCheckedIcon:
	 * "gridicons/checked-radio.png"}, {radioDefaultInactiveIcon:
	 * "gridicons/default-inactive-radio.png"}, {radioCheckedInactiveIcon:
	 * "gridicons/checked-inactive-radio.png"} ]
	 */

	}
};
