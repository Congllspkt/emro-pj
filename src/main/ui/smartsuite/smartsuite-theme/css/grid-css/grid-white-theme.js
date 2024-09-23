//blueSky	
var gridWhiteTheme = {
	/*  모든 영역 스타일의 부모 스타일  */	
	"default": {
		/* 모든 영역의 font가 적용된다. header나 cell에 각각의 폰트를 적용할려면  header-head, body-cell 등 각각의 영역에서 지정한다.*/
		"fontSize" : "12",
		"fontName" : "Malgun Gothic"
	},
	/* 그리드 보더나 트리뷰 라인 등 그리드 자체를 그릴 때 사용. */
	"grid" : {
		"border" : null,
		"paddingRight" : null,
		"paddingTop" : null,
		"paddingBottom" : "10",
	},
	/*각각의 영역에 세부 스타일을 적용한다. 기본스타일 셋이 되지 않는다.(데이터셀들이 표시되는 body 영역의 바탕색 지정 가능)*/
	"body" : {
		
		"background" : "#ffffffff",
		
		/*그리드행들의 바탕색 및 아래쪽 경계선 스타일. */
		"row": {
			
		},
		
		/* 데이터셀들을 그릴 때 사용되는 기본 스타일셋. */
		"cell": {
			
		},
		"merged": {
			//"background" : "#ffffffff"
		}
	},
	/*GridHeader(컬럼)들을 그릴 때 사용되는 기본 스타일셋.*/
	"header" : {
		"color": "#000", /* 201808 LNR sk이노베이션 */
		"hoveredColor": "#000", /* 201808 LNR sk이노베이션 */
		"selectedColor":"#000", /* 201808 LNR sk이노베이션 */
		"borderRight" : "#ffbbbbbb,1",/* 201808 LNR sk이노베이션 */
		"borderBottom" : "#ffbbbbbb,1",/* 201808 LNR sk이노베이션 */
		"borderTop": "#ffbedef3,0",
		"background" : "#fffaf4e9", /* 201808 LNR sk이노베이션 */
		"selectedBackground" : "#ffffe9c1",  /* 201808 LNR sk이노베이션 */
		"hoveredBackground" : "#ffffe9c1",  /* 201808 LNR sk이노베이션 */
		"group" : {
			"color": "#000", /* 201808 LNR sk이노베이션 */
			"hoveredColor":"#000", /* 201808 LNR sk이노베이션 */
			"selectedColor":"#fff",
			"borderRight" : "#ffbbbbbb,1", /* 201808 LNR sk이노베이션 */
			"borderBottom" : "#ffbbbbbb,1", /* 201808 LNR sk이노베이션 */
			"borderTop": "#ffbedef3,0",
			"background" : "#fffaf4e9", /* 201808 LNR sk이노베이션 */
			"selectedBackground" : "#ffffe9c1",/* 201808 LNR sk이노베이션 */
            "hoveredBackground" : "#ffffe9c1",/* 201808 LNR sk이노베이션 */
            "selectedForeground" : "#ffffe9c1",/* 20180820 LNR sk이노베이션 */
		}
	},
	/* GridFooter 셀을 그릴 때 사용되는 스타일셋.*/
	"footer" : {
		"borderRight" : "#ffc6c6c6,1",
		"borderTop" : "#ffbedef3,1",
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
	/*RowIndicator(그리드 왼쪽의 숫자를 나타내는 영역) 셀들을 그릴 때 사용되는 기본 스타일셋.*/
	"rowIndicator" : {
		"borderTop" : "#ffd2d2d2,0",
		"borderRight" : "#ffd2d2d2,1",
		"borderLeft" : "#ffd2d2d2,0",
		"borderBottom" : "#ffd2d2d2,1",
		"background" : "#ffffffff",
		"selectedBackground" : "#fff58023",/* 201808 LNR sk이노베이션 */
		"hoveredBackground" : "#fff58023",/* 201808 LNR sk이노베이션 */
	    "shapeColor": "#ffffffff",
	    "shapeInactiveColor" : "#ffffffff",		
	    "shapeSelectedColor" : "#ffffffff",		
	    "shapeHoveredColor"  : "#ffffffff"	
		/** 배경색과 같이 바뀔수있는 속성(바뀐 배경색에 의해 안보일수 있다) 
		  "color" : "#ffffffff"
		  "shapeColor": "#ffffffff"
		  "shapeInactiveColor" : "#ffffffff"		
		  "shapeSelectedColor" : "#ffffffff"		
		  "shapeHoveredColor"  : "#ffffffff"
		**/
	},
	/* CheckBar 셀들을 그릴 때 사용되는 기본 스타일셋. */
	"checkBar" : {
		"borderRight" : "#ffd2d2d2,1",
		"borderBottom" : "#ffd2d2d2,1",
		"background" : "#ffffffff",
		"head" : {
			"background" : "#fffaf4e9", /* 201808 LNR sk이노베이션 */
		}
	
	    //"shapeColor" : "#ffffff",  //체크모양 색
		/**	sc-grid 2.2.7 부터 적용가능
	    "boxBackground" :"#1f91ff",  //박스 배경색 
	    "boxBorder" : "#1f91ff",  //박스 테두리색
	    "boxInactiveBackground" : "#c2c2c2",  //박스 비활성화시 배경색
	    "boxInactiveBorder" : "#c2c2c2"   //박스  비활성화시 테두리색 
		**/
	
	},
	/*트리의 속성을 변경할때 사용한다.*/
	"tree":{
		/** +,- 상자 색상변경	sc-grid 2.2.7 부터 적용가능
		expander:{
            shapeExpandedColor: '#3b73af',  //+
            shapeCollapsedColor: '#016600'  //-
        }
	    **/
	},
	
	
	"dynamicStyles" : {
		//column의 editable 속성이 true일 경우 아래 style이 설정된다.
		"editable" : {
			"styles" : {
				"background" : "#fdf1e7"/* 201808 LNR sk이노베이션 */
			}
		},
		//column의 required 속성이 true일 경우 아래 style이 설정된다.
		"required" : {
            "renderer" : {
                "type" : "icon" 
            },
            "styles" : {
            	"celltipLocation": "leftTop"
            	// "celltipColor":"#ff7800"   
            }
		},
		"icons" : {
			"basicIcons":{
                "name" : "__icons__",
                "root":"bower_components/sc-grid/resources/img/",
                "items" : [
                    "icon_attach.png",
                    "icon_link.png",
                    "icon_search.png",
                    "icon_error.png"
                ]
            },
			"stateIcons": {
            	"name": 'stateIcons',
            	"root":"bower_components/sc-grid/resources/smartdatagrid/assets/",
        		"items": [
        			"data_created.png",
        			"data_updated.png",
        			"data_deleted.png"
        		]
            },
		},
		//image column의 imageCls로 사용되는 image 목록
		"imageCls":{
            "link"   : "bower_components/sc-grid/resources/img/icon_link.png",
            "search" : "bower_components/sc-grid/resources/img/icon_search.png",
            "attach" : "bower_components/sc-grid/resources/img/icon_attach.png"
        },
        //column에서 styleName속성으로 아래 등록된 style을 사용할 수 있다.
        "columnStyles" : {
        	"link" :{
                "cursor": "pointer",
                "styles" : {
                    "fontBold": true,
                    "color": "#a95c60" /* 201808 LNR sk이노베이션 */
                } 
            }
        }
        /**
         *  checkbar 이미지 표현 sc-grid 2.2.8 부터 적용가능
        "selectionBarIcons": [
        	{defaultIcon: "gridicons/default-checkbox.png"},
        	{checkedIcon: "gridicons/check-checkbox.png"},
        	{defaultInactiveIcon: "gridicons/default-inactive-checkbox.png"},
        	{checkedInactiveIcon: "gridicons/check-inactive-checkbox.png"},
        	{radioDefaultIcon: "gridicons/default-radio.png"},
        	{radioCheckedIcon: "gridicons/checked-radio.png"},
        	{radioDefaultInactiveIcon: "gridicons/default-inactive-radio.png"},
        	{radioCheckedInactiveIcon: "gridicons/checked-inactive-radio.png"}
        ]
        */

	}
};