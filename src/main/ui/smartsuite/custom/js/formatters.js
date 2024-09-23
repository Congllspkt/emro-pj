//숫자관련 기본 포매터
var numberFormat = function(value, isRound) {
	//올림옵션 default round true
	var isRound = (typeof isRound === "boolean") ? isRound : true;
	if (isNaN(value) || SCUtil.isEmpty(value)) {
		return '';
	}
	if (isRound) {
		return numeral(value).format(this.defaultFormat);
	} else {
		return numeral(value).format(this.defaultFormat, Math['floor']);
	}
};
//포매터 설정
var customFormatters = [/*
{
    name : 'number',
    format : function(value) {
        return numberFormat.call(this, value);
    },
    defaultFormat : '0'
}
,{
    name : 'amt',
    format : function(value) {
    	//var bigNumber = new BigNumber(value || 0); //Uncaught Error: new BigNumber() number type has more than 15 significant digits
    	var bigNumber = new BigNumber((value) ? value.toString() : 0);
    	value = bigNumber.toFixed(2);
    	return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    defaultFormat : '0,00.00'
}*//*, {
    name : 'rate',
    format : function(value) {
    	return numberFormat.call(this, value);
    },
    defaultFormat : '0.00'
}, {
    name : 'qty',
    format : function(value) {
    	return numberFormat.call(this, value);
    },
    defaultFormat : '0,0.000'
},{
    name : 'score',
    format : function(value){
    	return numberFormat.call(this, value);
    },
    defalutFormat : '0'
},{
	name : 'scoreDecimal',
    format : function(value){
        return numberFormat.call(this, value);
    },
    defalutFormat : '0.0000'
},{
    name: 'decimal',
    format: function(value){
    	return numberFormat.call(this, value);
    },
    defaultFormat : '0,0.00'
},
{
    name: 'percent',
    format: function(value){
    	var val = numberFormat.call(this, value);
    	val += val ? ' %' : ''
    	return val;
    },
    defaultFormat: '0'
},{
    name: 'percentDecimal',
    format: function(value){
    	var val = numberFormat.call(this, value);
    	val += val ? ' %' : ''
    	return val;
    },
    defaultFormat: '0,0.00'
},
 *//*
{
    name : 'integer',
    format : function(value) {
    	return numberFormat.call(this, value);
    },
    defaultFormat : '0,0'
},{
	name : 'date',
	format : function(value){
		return typeof value !== 'undefined' ? moment(value).format(this.defaultFormat) : "";
	},
	defaultFormat : 'YYYY/MM/DD'
},{
	name : 'datetime',
	format : function(value){
		return typeof value !== 'undefined' ? moment(value).format(this.defaultFormat) : "";
	},
	defaultFormat : 'YYYY/MM/DD HH:mm:ss'
},{
    name : 'usd_convrate',
    format : function(value) {
    	return numberFormat.call(this, value);
    },
    defaultFormat : '0.0000'
},
{
    name : 'KRW',
    format : function(value){
        return "₩"+numeral(value).format(this.defaultFormat);
    },
    defaultFormat : '0,0.00'
},{
    name : 'USD',
    format : function(value){
        return "$"+numeral(value).format(this.defaultFormat);
    },
    defaultFormat : '0,0.00'
},{
    name : 'AMD',
    format : function(value){
        return "€"+numeral(value).format(this.defaultFormat);
    },
    defaultFormat : '0,0.00'
},{
    name : 'CNY',
    format : function(value){
        return "¥"+numeral(value).format(this.defaultFormat);
    },
    defaultFormat : '0,0.00'
},{
    name : 'JPY',
    format : function(value){
        return "¥"+numeral(value).format(this.defaultFormat);
    },
    defaultFormat : '0,0.00'
},*/{
	name : 'hour',
	format : function(value) {
		if (UT.isNumber(value) && value < 10) {
			return '0' + value;
		} else {
			return value;
		}
	}
} ];
//formatters.js에 등록된 정적 formatter 처리
for (var i = 0, item; item = customFormatters[i]; i++) {
	SCFormatter.factory(item);
}
var CCPrecManager = {};
CCFormatterManager = new (function() {

	var AJAXSETTINGS = SCPreloader.ajaxSettings(), CONVERT = SCPreloader.convert;

	function CCFormatterManagerImpl() {
		this._callbacks = [];
		this._precFormats = [];
		this._setting();
	}

	CCFormatterManagerImpl.prototype._setting = function() {
		return $
				.ajax('findListFormatter.do', AJAXSETTINGS)
				.then(function(response) {
					return CONVERT(response);
				})
				.done(
						function(formatterData) {

							var precFormats = formatterData.precFormats;
							var currentUserDisplayFormats = formatterData.currentUserDisplayFormats;
							var displayFormats = formatterData.displayFormats;

							this.precFormats = precFormats;
							this.displayFormats = displayFormats;

							var obj = currentUserDisplayFormats.reduce(
									function(acc, item, i) {
										var key = item["loc_fmt_typ_ccd"];
										acc[key] = item;
										return acc;
									}, {});

							this.currentUserDisplayFormat = obj;
							this._displayFormat();
							this.setPrecFormat(precFormats);
							this.setPrecValidate(precFormats);
							this._complete();
						}.bind(this)).fail(function() {
					console.error("formatter list error");
				});
	};

	CCFormatterManagerImpl.prototype.setPrecFormat = function(precFormatGrps) {
		var dispPrecFormats = this._precFormats;
		CCPrecManager.regex = function(formatterName, dtlCd) {
			var precision = 0;
			var selectedFormat = [];
			var selectedCurDtls = [];
			for (var i = 0, len = dispPrecFormats.length; i < len; i++) {
				var row = dispPrecFormats[i];
				if (row["fmter_cd"] === formatterName) {
					selectedFormat = row;
					break;
				}
			}

			//몇자리 올림인지
			var lmt = selectedFormat["decpt_len_lmt"] || 0;
			precision = parseInt(lmt, 10);
			
			//prec_grp_cd에 따른 소수점처리
			if (UT.isNotEmpty(selectedFormat["decpt_use_ccd"])) {
				var precFormatDtls = [];
				for (var j = 0, len2 = precFormatGrps.length; j < len2; j++) {
					if (precFormatGrps[j]["decpt_use_ccd"] === selectedFormat["decpt_use_ccd"]) {
						precFormatDtls = precFormatGrps[j].precFormatDtls;
						break;
					}
				}

				for (var z = 0, len3 = precFormatDtls.length; z < len3; z++) {
					var row2 = precFormatDtls[z];
					if (UT.isNotEmpty(row2['decpt_len'])
							&& row2["decpt_use_dtlcd"] === dtlCd) {
						precision = parseInt(row2['decpt_len'], 10);
						break;
					}
				}
			}

			if (precision === 0) {
				return "/^[-]?[0-9]*$/";
			} else {
				return "/^[-]?[0123456789]+([.]?[0-9]{0," + precision + "})$|^[-]$/";
			}
		};

		CCPrecManager.format = function(formatterName, value, dtlCd) {
			//몇자리 올림인지
			var precision = 0;
			if(UT.isNotEmpty(value)){
				var selectedFormat = [];
				var selectedCurDtls = [];
				//(ROUND: 반올림, CEIL: 올림, FLOOR: 내림)
				var hndlTyp = "ROUND";
				for (var i = 0, len = dispPrecFormats.length; i < len; i++) {
					var row = dispPrecFormats[i];
					if (row["fmter_cd"] === formatterName) {
						selectedFormat = row;
						break;
					}
				}

				//처리방법유형이 정해져있는경우
				if (UT.isNotEmpty(selectedFormat["decpt_trunc_typ_ccd"])) {
					//(ROUND: 반올림, CEIL: 올림, FLOOR: 내림)
					hndlTyp = CCFormatterManager.getHndlTypByC999(selectedFormat["decpt_trunc_typ_ccd"]);
				}
				
				//몇자리 올림인지
				var lmt = selectedFormat["decpt_len_lmt"];
				precision = parseInt(lmt, 10);
				
				//직접 DIRECT 소수점처리
				if (UT.isEmpty(selectedFormat["decpt_use_ccd"])) {
					//몇자리 올림인지
					var lmt = selectedFormat["decpt_len_lmt"];
					precision = parseInt(lmt, 10);
				} else if(UT.isNotEmpty(dtlCd)){
					var precFormatDtls = [];
					for (var j = 0, len2 = precFormatGrps.length; j < len2; j++) {
						if (precFormatGrps[j]["decpt_use_ccd"] === selectedFormat["decpt_use_ccd"]) {
							precFormatDtls = precFormatGrps[j].precFormatDtls;
							break;
						}
					}

					for (var z = 0, len3 = precFormatDtls.length; z < len3; z++) {
						var row2 = precFormatDtls[z];
						if (UT.isNotEmpty(row2['decpt_len'])
								&& row2["decpt_use_dtlcd"] === dtlCd) {
							precision = parseInt(row2['decpt_len'], 10);
							hndlTyp = CCFormatterManager.getHndlTypByC999(row2["decpt_trunc_typ_ccd"])
							break;
						}
					}
				}

				return UT.toFixedRound(value, hndlTyp, precision + 1);
			}else{
				return value;
			}
		};
	};
	
	CCFormatterManagerImpl.prototype.setPrecValidate = function() {
		var dispPrecFormats = this._precFormats;
		//validator function 함수 처리 방안
		CCPrecManager.validate = function(formatterName, value, dtlCd) {
			
			if(UT.isNotEmpty(dtlCd)){
				var validator = SCValidator.getValidator(formatterName+"_"+dtlCd);
				if(UT.isNotEmpty(validator)){
					return SCValidator.validate(formatterName+"_"+dtlCd,value);
				}
			}
			return SCValidator.validate(formatterName,value);
		};
		
		CCPrecManager.validateInfo = function(formatterName, dtlCd) {
			
			if(UT.isNotEmpty(dtlCd)){
				var validator = SCValidator.getValidator(formatterName+"_"+dtlCd);
				if(UT.isNotEmpty(validator)){
					return SCValidator.getValidator(formatterName+"_"+dtlCd);
				}
			}
			return SCValidator.getValidator(formatterName);
		};
	};

	//사용자 date displayFormat 설정
	CCFormatterManagerImpl.prototype.userDisplayFormatDate = function(row) {
		var date = this.currentUserDisplayFormat["DATE"];
		var formatExp = date["loc_fmt_expr"];
		formatExp = formatExp.replace(/#/gi, "");
		formatExp = formatExp.toUpperCase();

		if (UT.isNotEmpty(formatExp)) {
			var format = formatExp;
			if(row["fmter_cd"] === "date"){
				Polymer.SCDateFieldBehaviorImpl.properties.format.value = format;
				Polymer.SCPeriodDateFieldBehaviorImpl.properties.format.value = format;
				
				var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
				var monthFormatExp = "";
				if(regExp.test(formatExp)){
					
					var t = formatExp.match(regExp);
					var splitStr = t[0];
					
					var regexp1 = new RegExp("DD"+"\\"+splitStr,"gi");
					var regexp2 = new RegExp("\\"+splitStr+"DD","gi");
					
					monthFormatExp = formatExp.replace(regexp1, "");
					monthFormatExp = monthFormatExp.replace(regexp2, "");
				}else{
					monthFormatExp = formatExp.replace(/DD/gi, "");
				}
				var monthFormat = UT.copy(monthFormatExp);
				//sc-month-field, sc-period-month-field format지정 추가
				Polymer.SCMonthFieldBehaviorImpl.properties.format.value = monthFormat;
				Polymer.SCPeriodMonthFieldBehaviorImpl.properties.format.value = monthFormat;
				if(typeof SCSessionManager != "undefined" && typeof SCSessionManager.currentUser != "undefined"){
					SCSessionManager.currentUser.tzFormat = format;
				}
			}

			var defaultFormat = formatExp;

			//time
			if (UT.isNotEmpty(row["fmt_expr_2"])) {
				var formatExp2 = row["fmt_expr_2"];
				formatExp2 = formatExp2.replace(/#/gi, "");
				defaultFormat = defaultFormat + " " + formatExp2;
			}

			item = {
				name : row["fmter_cd"],
				format : function(value) {
					return UT.isNotEmpty(value) ? moment(value).format(this.defaultFormat) : "";
				},
				defaultFormat : defaultFormat
			};
		}
		return item;
	};

	//소수점관리에 따른 displayFormat설정
	/*CCFormatterManagerImpl.prototype.makePrcGrpCdFormat = function(dispRow,thousandExp) {
		var precFormats = this.precFormats || [];
		for (var j = 0, len2 = precFormats.length; j < len2; j++) {
			var precFormatDtls = precFormats[j].precFormatDtls || [];
			for (var i = 0, len = precFormatDtls.length; i < len; i++) {
				var row = precFormatDtls[i];

				//util
				//precision 소수점만 처리하는 formatter
				var precionFormatter = {
					name : "prec_" + dispRow["fmter_cd"] + "_"
							+ row['decpt_use_dtlcd'],
					format : function(value) {
						var lmt = 0;
						var hndlTermsTyp = "ROUND";
						// 단가 통화는 절산.
						if (UT.isNotEmpty(this.formatInfo['decpt_len'])
								&& UT.isNotEmpty(parseInt(
										this.formatInfo['decpt_len'], 10))) {
							lmt = parseInt(this.formatInfo['decpt_len'], 10);
						}
						//처리방법유형이 정해져있는경우
						if (UT.isNotEmpty(this.formatInfo["decpt_trunc_typ_ccd"])) {
							//(ROUND: 반올림, CEIL: 올림, FLOOR: 내림)
							hndlTermsTyp = this.formatInfo["decpt_trunc_typ_ccd"];
						}
						// front, after text add
						var formatValue = UT.toFixedRound(value || 0,
								hndlTermsTyp, lmt + 1);
						var bigNumber = new BigNumber(
								(formatValue) ? formatValue.toString() : 0);
						//x = 3.456  change '3.46'
						//value = bigNumber.toFixed(2);
						//formatValue = formatValue.replace(/./g,precExp);
						return formatValue;
					},
					formatInfo : row,
					thousandExp : thousandExp
				};
				SCFormatter.factory(precionFormatter);
			}

		}
	};*/
	//validator를 format관리에서 지정한방식으로 구현 type number 만
	CCFormatterManagerImpl.prototype.makePrecValidator = function(dispRow,
			thousandExp) {
		//전역적으로 소수점 제어를 validator에 적용할것인지 고민..

		//몇자리 올림인지
		var lmt = dispRow["decpt_len_lmt"] || 0;
		var validator = {
			type : dispRow["fmter_cd"],
			decimalprecision : lmt,
			regex : /^\d*(\.?\d*)$/
		};
		SCValidator.factory(validator);
		
		if (UT.isNotEmpty(dispRow["decpt_use_ccd"])) {
			var precFormats = this.precFormats || [];
			for (var j = 0, len2 = precFormats.length; j < len2; j++) {
				var precFormatDtls = precFormats[j].precFormatDtls || [];
				for (var i = 0, len = precFormatDtls.length; i < len; i++) {
					var row = precFormatDtls[i];
					var formatInfo = row;
					var lmt = 0;
					var hndlTermsTyp = "ROUND";
					// 단가 통화는 절산.
					if (UT.isNotEmpty(formatInfo['decpt_len'])
							&& UT.isNotEmpty(parseInt(formatInfo['decpt_len'], 10))) {
						lmt = parseInt(formatInfo['decpt_len'], 10);
					}

					var validator = {
						type : dispRow["fmter_cd"] + "_" + row['decpt_use_dtlcd'],
						decimalprecision : lmt,
						regex : /^\d*(\.?\d*)$/
					};
					SCValidator.factory(validator);
				}

			} //: ~ end var j
		} //: ~ end (UT.isEmpty(dispRow["decpt_use_ccd"]))
	};

	CCFormatterManagerImpl.prototype._displayFormat = function() {
		var displayFormats = this.displayFormats || [];
		var precFormats = this.precFormats || [];
		var item = {};


		for (var i = 0, len = displayFormats.length; i < len; i++) {
			var row = displayFormats[i];
			if (row["dat_typ_ccd"] === "DT") {
				//표현식에 따른 format 처리
				if (row["fmt_typ_ccd"] === "USR_LOC") {
					item = this.userDisplayFormatDate(row);
				} else {
					//DIRECT
					if (UT.isNotEmpty(row["fmt_expr_1"])) {
						//표현식
						var formatExp = row["fmt_expr_1"];
						formatExp = formatExp.replace(/#/gi, "");
						formatExp = formatExp.toUpperCase();
						if (UT.isNotEmpty(formatExp)) {
							
							var format = formatExp;
							
							if(row["fmter_cd"] === "date"){
								Polymer.SCDateFieldBehaviorImpl.properties.format.value = format;
								Polymer.SCPeriodDateFieldBehaviorImpl.properties.format.value = format;
								if(typeof SCSessionManager != "undefined" && typeof SCSessionManager.currentUser != "undefined"){
									SCSessionManager.currentUser.tzFormat = format;
								}
							}

							var defaultFormat = formatExp;

							//time
							if (UT.isNotEmpty(row["fmt_expr_2"])) {
								var formatExp2 = row["fmt_expr_2"];
								formatExp2 = formatExp2.replace(/#/gi, "");
								defaultFormat = defaultFormat + " "
										+ formatExp2;
							}
							item = {
								name : row["fmter_cd"],
								format : function(value) {
									return UT.isNotEmpty(value) ? moment(
											value).format(this.defaultFormat)
											: "";
								},
								defaultFormat : defaultFormat
							};
						}
					}
				}
			} else if (row["dat_typ_ccd"] === "NUMC") {

				//천단위 표현식
				var thousandExp = "";
				//소수점 표현식
				var precExp = "";
				//표현식에 따른 format 처리
				if (row["fmt_typ_ccd"] === "USR_LOC") {
					if (UT.isNotEmpty(this.currentUserDisplayFormat["THOUSAND"])) {
						thousandExp = this.currentUserDisplayFormat["THOUSAND"]["loc_fmt_expr"];
					}
					if (UT.isNotEmpty(this.currentUserDisplayFormat["PREC"])) {
						precExp = this.currentUserDisplayFormat["PREC"]["loc_fmt_expr"];
					}
				} else {

					//직접처리 천단위 표현식 처리
					if (UT.isNotEmpty(row["fmt_expr_1"])) {
						//천단위 표현식
						thousandExp = row["fmt_expr_1"];
					} // ~ UT.isNotEmpty(row["fmt_expr_1"])
					
					//직접처리 소수점 표현식 처리
					if (UT.isNotEmpty(row["decpt_str"])) {
						//소수점 표현식
						precExp = row["decpt_str"];
					}
				}
				
				//직접 DIRECT 소수점처리
				//if (UT.isEmpty(row["decpt_use_ccd"])) {
					item = {
						name: row["fmter_cd"],
    				    format: function(value){
    				    	//소수점 표현식
    	    				var precExp = this.precExp || ".";
    	    				//시작허용문자 (- 또는 소수점 표현식 하나만 입력한 경우 format하지 않는다)
    	    				var startAllowed = ["-", precExp];
    	    				//몇자리 올림인지
    	    				var lmt = this.formatInfo["decpt_len_lmt"];
    	    				//(ROUND: 반올림, CEIL: 올림, FLOOR: 내림)
    	    				var hndlTyp = CCFormatterManager.getHndlTypByC999(this.formatInfo["decpt_trunc_typ_ccd"]) || "ROUND";
    	    				// front, after text add
    	    				var frnot = this.formatInfo["fmt_prfx"] || "";
    	    				var after = this.formatInfo["fmt_suf"] || "";
    	    				if(UT.isNotEmpty(value) && startAllowed.indexOf(value) === -1){
    	    					//var formatValue = UT.toFixedRound(value, hndlTyp,lmt + 1);
    	    					var formatValue = value;
        				    	var bigNumber = new BigNumber((formatValue) ? formatValue.toString() : 0);
        				    	//x = 3.456  change '3.46'
        				    	formatValue = bigNumber.toFixed(lmt);
        				    	formatValue = formatValue.replace(/\./g,precExp);
        				    	
        				    	var parts = formatValue.split(precExp);
        				    	//var string = "" + formatValue;  // 문자로 바꾸기. 
        				   	 	//var regExp = /^([-+]?\d+)(\d{3})(\.\d+)?/;  // 필요한 정규식. 
        				    	formatValue = parts[0].toString().replace(/\B(?=(\d{3})+(?!\d))/g, this.thousandExp) + (parts[1]? precExp+ parts[1] : "");
        				   	 	//while ( regExp.test( string ) ) string = string.replace( regExp, "$1" + this.thousandExp + "$2" + "$3" );  // 쉼표 삽입. 
        				   	 	//formatValue = string;
        				    	//formatValue = formatValue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, this.thousandExp);
        				    	formatValue = frnot + formatValue + after;
        				    	return formatValue;
    	    				}else{
    	    					return value;
    	    				}
    				    },
    				    formatInfo: row,
    				    thousandExp: thousandExp,
    				    precExp: precExp
	    			};
				//} else {
					// 소수점관리에 따라 생성,
					//this.makePrcGrpCdFormat(row, thousandExp);
				//} // ~ UT.isEmpty(row["decpt_use_ccd"])
				this._precFormats.push(row);
				// 소수점 validator 함수 생성.
				this.makePrecValidator(row, thousandExp);
			}

			if(UT.isNotEmpty(item)){
    			SCFormatter.factory(item);
			}
		}
	};

	CCFormatterManagerImpl.prototype.getHndlTypByC999 = function(code){
			//hdnlTyp = C999
			//ROUND	반올림 = RO	반올림 , FLOOR	절사 = TRUNC	절사
		 if(code =="RO"){
			 return "ROUND";
		 }else if(code == "TRUNC"){
			 return "FLOOR";
		 }else{
			 return code;
		 }
	};
	CCFormatterManagerImpl.prototype._complete = function(callback) {
		var callbacks = this._callbacks, fn;

		// this._readied = true;
		// callback run
		while ((fn = callbacks.shift(0))) {
			fn()
		}
	};
	return CCFormatterManagerImpl;
}());