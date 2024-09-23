/* validator 설정
                regex : /^\d*$/ // 소수점 허용하지 않음
    regex : /^\d*(\.?\d*)$/ // 소수점 허용
    regex : /^\d*(\.?[0-9]{0,2})$/ // 소수점 둘째자리까지 허용
 */
var validators = [{
    type : 'decimalprecision',
    validator: function(value, decimalprecision, formatArgs) {
        var regex = new RegExp("^[+-]?\\d*(\\.?[0-9]{0," + decimalprecision + "})$","i");
        if(regex.test(value))
            return true;
        return "이 필드는 소수점 "+ decimalprecision + " 자리까지 입력가능합니다.";
    }
},{
    type : 'nospace',
    validator: function(value, nospace, formatArgs) {
        nospace = typeof nospace !== 'undefined' ? nospace : true;
        var pattern = /[\s\t\r\n\v\f]/;
        if (nospace && pattern.test(value)) {
            return "이 필드는 공백 문자를 허용하지 않습니다.";
        }
        return true;
    }
},/*{
    type : 'integer',
    minValue : 0,
    regex : /^\d*$/
},{
    type : 'decimal',
    minValue : 0,
    regex : /^\d*(\.?\d*)$/
},{
    type : 'amt',
    maxLength : 25,
    decimalprecision:2,
    nospace: true,
    minValue : 0,
    regex : /^\d*(\.?\d*)$/
},{
    type : 'rate',
    maxValue : 100,
    decimalprecision:3,
    nospace: true,
    minValue : 0,
    regex : /^\d*(\.?\d*)$/
},{
    type : 'price',
    maxLength : 13,
    decimalprecision:2,
    minValue : 0,
    regex : /^\d*(\.?\d*)$/,
},{
    type : 'qty',
    maxLength : 8,
    decimalprecision:3,
    minValue : 0,
    regex : /^\d*(\.?\d*)$/,
},{
    type : 'score',
    maxLength : 3,
    minValue: 0,
    maxValue: 100,
    regex : /^\d*$/
},{
    type : 'matrix_score',
    maxLength : 6,
    decimalprecision:2,
    nospace: true,
    maxValue : 100,
    minValue : 0,
    regex : /^\d*(\.?\d*)$/
},*/
{
    type : 'lengthValid',
    validator: function(value, obj, formatArgs) {
        if(!value) return true;

        var minLength = obj.minLength || 8;
        var maxLength = obj.maxLength || 20;
        var regex = new RegExp("^.{"+ minLength + "," + maxLength + "}$");	//길이
        if(regex.test(value))
            return true;
        return I18N.translate("STD.E1019", null, formatArgs[0], minLength, maxLength);
    }
},{
    type : 'letterValid',
    validator: function(value, obj, formatArgs) {
        if(!value) return true;

        var regex = new RegExp("^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-])","i"); //영문, 특수문자, 숫자 포함 여부
        if(regex.test(value))
            return true;
        return I18N.translate("STD.E1018", null, formatArgs[0]);
    }
},
{
    type : 'repeatValid',
    validator: function(value, repeatLength, formatArgs) {
        if(!value) return true;

        var regex = new RegExp("(.)\\1{"+ (repeatLength-1) +",}"); //반복 길이
        if(!regex.test(value))
            return true;
        return I18N.translate("STD.E1020", null, formatArgs[0], repeatLength);
    }
},{
    type : 'password',
    validator: function(value,obj,formatArgs){
    	var rules = SCSessionManager.getPasswordRules();

    	var regExpFn = function(){
    		var alphabetRegExp = '(?=.*?[a-z])';
    		var digitRegExp = '(?=.*?[0-9])';
    		var specialRegExp = '(?=.*?[#?!@$%^&*-])';
    		var letterRegExp = '';
    		if(rules.alphabetCharacterRule > 0) {
    			letterRegExp += alphabetRegExp;
    		}
    		if(rules.digitCharacterRule > 0) {
    			letterRegExp += digitRegExp;
    		}
    		if(rules.specialCharacterRule > 0) {
    			letterRegExp += specialRegExp
    		}
    		return {
                lengthRe : new RegExp("^.{" + rules.minLengthRule + "," + rules.maxLengthRule + "}$"),	//길이 8~20자
    			letterRe : new RegExp("^" + letterRegExp, "i"), //영문, 특수문자, 숫자 포함 여부
    			repeatRe : new RegExp("(.)\\1{" + (rules.repeatCharacterRule-1) + ",}")	// 4번 이상연속된 문자
    		};
    	}
    	
    	var pw = value || '';
    	var regExp = regExpFn(); //validator.js, UT.passwordRegExp() 참고
		var	letterCheck = SCUtil.isEmpty(pw) ? false :  regExp.letterRe.test(pw); //영문,숫자,특수문자 포함 여부
		var lengthCheck = SCUtil.isEmpty(pw) ? false : regExp.lengthRe.test(pw); //길이 8~20 여부
		var repeatCheck = SCUtil.isEmpty(pw) ? false : !regExp.repeatRe.test(pw);  //동일 문자열 미포함 여부
		var characters = pw.split(''), sequence = 0;
		if(characters.length) {
			characters.reduce(function(v1, v2) {
				if(Math.abs(v1.charCodeAt() - v2.charCodeAt()) === 1) {
					sequence++; 					
				}
				return v2;
			});
		}
		var sequenceCheck = SCUtil.isEmpty(pw) ? false : ((sequence+1) < SCSessionManager.getPasswordRules().sequenceCharacterRule);
		
		var passwordStr = I18N.translate("비밀번호");
		
		//특수문자
		if(!letterCheck){
			return I18N.translate("STD.E1018",null,passwordStr);
		}
		
		//길이
		if(!lengthCheck){
			return UT.formatString("STD.E1019",passwordStr,rules.minLengthRule,rules.maxLengthRule);
		}
		
		//연속문자
		if(!repeatCheck){
			return UT.formatString("STD.E1020",passwordStr,1,rules.repeatCharacterRule);
		}
		
		//반복문자
		if(!sequenceCheck){
			return UT.formatString("STD.E1021",passwordStr,rules.sequenceCharacterRule);
		}
		
		return true;
    }
	/*
        repeatValid : 4,
        lengthValid: {minLength: 8, maxLength: 20},
        letterValid: {}*/
},{
	type : 'ipAddress',
	validator: function(value, decimalprecision, formatArgs) {
		var regex = new RegExp("^([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\.([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3}$")
        if(regex.test(value))
            return true;
        return "IP 주소 형식이 아닙니다.";
    }
},{
	type : 'phone',
	regex : /^\d{2,3}-?\d{3,4}-?\d{4}$/
},{
	type : 'mobile',
	regex : /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/
},{
	type : 'required_whitespace',
	validator: function(value, obj, formatArgs){
		value = value || '';
		if(!Polymer.$Util.isEmpty(value.trim())) {
			return true;
		}
		return '이 필드는 필수 항목입니다.';
	}
}
];
for(var j=0, validator; validator = validators[j]; j++){
	SCValidator.factory(validator);
}
