/**
 * 공통 form 유효성검사
 * jquery-validation/1.19.2
 * @author JSCHOI
 * @since  2021
 * @version 1.0
 * @see
 * Copyright (C)  All right reserved.
*/
var coreForm = {
      isEmpty : function(text) {
    	if (text == ""
        	|| text == null
        	|| text == undefined
        	|| (text != null && typeof text == "object" && !Object
    	.keys(text).length)) {
    	    return true;
    	}
    	return false;
     },
    //유효성 검사 맵핑
    validate : function (formId) {
        
	 //유효성 검사 맵핑
        var validator  = $("#" + formId ).validate({
            errorElement: 'div',
            errorPlacement: function(error, element) {
            	error.addClass('invalid-feedback');
              	if( element.is(":radio")  ) {
		    error.insertBefore(element.parent());
                }else {
                    error.insertAfter(element);
                }
            },
            highlight: function (element, errorClass, validClass) {
    	       $(element).addClass('is-invalid');
    	    },
    	    unhighlight: function (element, errorClass, validClass) {
    	       $(element).removeClass('is-invalid');
    	    }
        });


        //앞뒤공백제거
        $.validator.addMethod('requiredNotBlank', function(value, element) {
            if(value.length > 0 ){
                if(value[0] == ' ')
                 $( element ).val($.trim(value));	
            }					
            return $.validator.methods.required.call(this, $.trim(value), element);
        }, $.validator.messages.required);

        //동일할경우 유효성처리
        $.validator.addMethod( "notEqualTo", function( value, element, param ) {
            return this.optional( element ) || !$.validator.methods.equalTo.call( this, value, element, param );
        }, "Please enter a different value, values must not be the same." );

	//02
	//031,032,033,
	//041,042,043,044,
	//051,052,053,054,055,
	//061,062,063,064
	$.validator.addMethod( "fax", function( value, element, param ) {
    		return this.optional( element ) || /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\d{3,4})-(\d{4})$/.test( value );
	}, "Please enter a valid fax." );

	//not http url 유효성 처리
	$.validator.addMethod( "notHttpUrl", function( value, element, param ) {
		return this.optional( element ) || /^(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test( value );
	}, "Please enter a valid url." );

	$.validator.addMethod( "hp", function( value, element, param ) {
	    return this.optional( element ) || /^(\d{3})-(\d{3,4})-(\d{4})$/.test( value );
	}, "Please enter a valid hp." );

	//11-222-33333
	$.validator.addMethod( "bno", function( value, element, param ) {
	    return this.optional( element ) || /(\d{3})-(\d{2})-(\d{5})/.test( value );
	}, "Please enter a valid bno." );

	$.validator.addMethod( "mailaddr", function( value, element, param ) {
	    return this.optional( element ) || /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/.test( value );
	}, "Please enter a valid mailaddr." );

	//validate 초기화 확장함수
	$.extend( validator , {
	    clearForm : function(){
		this.resetForm();//remove error class on name elements and clear history
		$('#' + formId ).find('.is-invalid').each(function( i ) {
		    $(this).removeClass('is-invalid');
		});
             }
	});
	// Serialize form fields as URI argument/HTTP POST data
	var form = document.getElementById(formId);
	for ( var i = 0; i < form.elements.length; i++ ) {
	   	var obj = null;
	    	var e = form.elements[i];
	        if(!e.name) continue;
	        switch (e.type) {
	            case 'text':
	            	obj = $("#" + formId ).find("input[name='" + e.name + "']");
	            	break;
	            case 'textarea':
	            	obj = $("#" + formId ).find("textarea[name='" + e.name + "']");
	            	break;
	            case 'password':
	            	obj = $("#" + formId ).find("input[name='" + e.name + "']");
	            	break;
	            case 'hidden':
	            	obj = $("#" + formId ).find("input[name='" + e.name + "']");
	            	break;
	            case 'radio':
	            	obj = $("#" + formId ).find("input:radio[name='" + e.name + "']");
	            	break;
	            case 'checkbox':
	            	obj = $("#" + formId ).find("input:checkbox[name='" + e.name + "']");
	            	break;
	            case 'select-one':
	            	obj = $("#" + formId ).find("select[name='" + e.name + "']");
	            	break;
	            default :
	            	obj = null;
	            	break;
	       }
	       if(!this.isEmpty(obj)) {
	    	  var required 	= obj.hasClass("cv-required");
	       	  var number 	= obj.hasClass("cv-number");
	       	  var email 	= obj.hasClass("cv-email");
	       	  var url 	= obj.hasClass("cv-url");
	       	  var notHttpUrl = obj.hasClass("cv-notHttpUrl");
	       	  var digits 	= obj.hasClass("cv-digits");
	       	  var equalTo 	= obj.hasClass("cv-equalTo");
	       	  var maxlength = obj.hasClass("cv-maxlength");
	       	  var minlength = obj.hasClass("cv-minlength");
	       	  var rangelength = obj.hasClass("cv-rangelength"); //[2, 6]
	       	  var range 	= obj.hasClass("cv-range");//[2, 6]
	       	  var max 	= obj.hasClass("cv-max");
	       	  var min 	= obj.hasClass("cv-min");
	       	  var notEqualTo = obj.hasClass("cv-notEqualTo");
	       	  var dateISO 	= obj.hasClass("cv-dateISO");
	       	  var hp 	= obj.hasClass("cv-hp");
	       	  var fax 	= obj.hasClass("cv-fax");
	       	  var bno 	= obj.hasClass("cv-bno");
	       	  var mailaddr  = obj.hasClass("cv-mailaddr");
	       	  var requiredNotBlank = obj.hasClass("cv-requiredNotBlank");
	       	  obj.rules("add",{
	       		  required: required,
	       		  number : number,
	       		  email : email,
	       		  url : url,
	       		  digits : digits,
	       		  notHttpUrl : notHttpUrl,
	       		  equalTo : equalTo===true?"#" + obj.attr("cv-equalTo-data"):false,
	       		  maxlength : maxlength===true?obj.attr("cv-maxlength-data"):false,
	    		  minlength : minlength===true?obj.attr("cv-minlength-data"):false,
	    		  rangelength : rangelength===true?obj.attr("cv-rangelength-data"):false,
	    		  range : range===true?obj.attr("cv-range-data"):false,
	    		  max : max===true?obj.attr("cv-max-data"):false,
		    	  min : min===true?obj.attr("cv-min-data"):false,
		    	  notEqualTo : notEqualTo===true?"#" + obj.attr("cv-notEqualTo-data"):false,
		    	  dateISO: dateISO,
		    	  hp : hp,
		    	  fax : fax,
		    	  bno : bno,
		    	  mailaddr : mailaddr,
		    	  requiredNotBlank : requiredNotBlank,
		    	  messages: {
		        	dateISO: obj.attr("cv-dateISO-msg"),
		    	    	required: obj.attr("cv-required-msg"),
				number: obj.attr("cv-number-msg"),
				email: obj.attr("cv-email-msg"),
			   	url: obj.attr("cv-url-msg"),
			   	notHttpUrl: obj.attr("cv-notHttpUrl-msg"),
			   	digits: obj.attr("cv-digits-msg"),
			   	equalTo: obj.attr("cv-equalTo-msg"),
			   	maxlength: obj.attr("cv-maxlength-msg"),
			   	minlength: obj.attr("cv-minlength-msg"),
			   	rangelength: obj.attr("cv-rangelength-msg"),
			   	range: obj.attr("cv-range-msg"),
			 	max: obj.attr("cv-max-msg"),
			 	min: obj.attr("cv-min-msg"),
			 	notEqualTo: obj.attr("cv-notEqualTo-msg"),
			 	hp : obj.attr("cv-hp-msg"),
			 	fax: obj.attr("cv-fax-msg"),
			 	bno: obj.attr("cv-bno-msg"),
			 	mailaddr : obj.attr("cv-mailaddr-msg"),
			 	requiredNotBlank : obj.attr("cv-requiredNotBlank-msg")
		       	}
	       	  });
	       }
	}
	return validator;
     },
     getSerializeForm : function (formId) {
		// Serialize form fields as URI argument/HTTP POST data
		var form = document.getElementById(formId);		
		var textField = {};
		var textareaField = {};
		var passwordField = {};
		var hiddenField = {};
		var radioField = {};
		var checkboxField = {};
		var selectField = {};
		
	    var fieldPair = { text : {} , password : {}, hidden : {} , textarea : {}  , radio : {} , checkbox : {} , select : {} };
	    
	    for ( var i = 0; i < form.elements.length; i++ ) {
	        var e = form.elements[i];	        
	        //if(!e.name || !e.value) continue; // Shortcut, may not be suitable for values = 0 (considered as false)
	        if(!e.name) continue;
	        switch (e.type) {
	            case 'text':
	            	textField[e.name] = e.value;	            		
	            	break;
	            case 'textarea':
	            	textareaField[e.name] = e.value;	            	
	            	break;
	            case 'password':	            	
	            	passwordField[e.name] = e.value;	            	
	            	break;
	            case 'hidden':	                
	            	hiddenField[e.name] = e.value;
	            	break;	                
	            case 'radio':
	            	radioField[e.name] = e.value;	            	
	            	break;
	            case 'checkbox': 
	                //e.checked?fieldPair[e.name]=e.value: fieldPair[e.name]= false;
	            	checkboxField[e.name] = e.value;
	            	break;	
	            case 'select-one': 
	            	selectField[e.name] = e.value;
	            	break;
	            /*  To be implemented if needed:
	            case 'select-one':
	                ... document.forms[x].elements[y].options[0].selected ...
	                break;
	            case 'select-multiple':
	                for (z = 0; z < document.forms[x].elements[y].options.length; z++) {
	                    ... document.forms[x].elements[y].options[z].selected ...
	                } */
	                //break;
	        }	        	        
	    }
	    fieldPair.text = textField;
	    fieldPair.textarea = textareaField;
	    fieldPair.password = passwordField;
	    fieldPair.hidden = hiddenField;
	    fieldPair.radio = radioField;
	    fieldPair.checkbox = checkboxField;
	    fieldPair.select = selectField;
	    return fieldPair;
	},	
	//data object 폼에 설정
	setFormByObject : function (formId,data) {
		
		coreForm.initSerializeForm(formId);
		
		var fieldPair = coreForm.getSerializeForm(formId);
		
		//넘겨진 데이터 설정
		for( var key in data ) {
		  //console.log("text : " + key + '=>' + data[key] );
		
		  if(fieldPair.text.hasOwnProperty(key)){
		      fieldPair.text[key] = data[key];
		      continue;
		  }
		  if(fieldPair.textarea.hasOwnProperty(key)){
		      fieldPair.textarea[key] = data[key];
		      continue;
		  }
		  if(fieldPair.password.hasOwnProperty(key)){
		      fieldPair.password[key] = data[key];
		      continue;
		  }
		  if(fieldPair.hidden.hasOwnProperty(key)){
		      fieldPair.hidden[key] = data[key];
		      continue;
		  }
		  if(fieldPair.radio.hasOwnProperty(key)){
		      fieldPair.radio[key] = data[key];
		      continue;
		  }
		  if(fieldPair.checkbox.hasOwnProperty(key)){
		      fieldPair.checkbox[key] = data[key];
		      continue;
		  }
		  
		  if(fieldPair.select.hasOwnProperty(key)){
		      fieldPair.select[key] = data[key];
		      continue;
		  }
		}		
		coreForm.setSerializeForm(formId,fieldPair);		
	},
	setSerializeForm : function (formId,fieldPair) {
		// Serialize form fields as URI argument/HTTP POST data
		$.each(fieldPair.text, function( key, value ) {	
		    // console.debug("text : " + key  + " => " + value );
		    $("#" + formId ).find("input[name='" + key + "']").val(value);
		});
		
		$.each(fieldPair.password, function( key, value ) {		  
		    //console.debug("password : " + key  + " => " + value );
		    $("#" + formId ).find("input[name='" + key + "']").val(value);
		});
		
		$.each(fieldPair.hidden, function( key, value ) {
		    // console.debug("hidden : " + key  + " => " + value );
		    $("#" + formId ).find("input[name='" + key + "']").val(value);
		});
		
		$.each(fieldPair.textarea, function( key, value ) {
		    //console.debug("textarea : " + key  + " => " + value );
		    $("#" + formId ).find("textarea[name='" + key + "']").val(value);
		});
		
		$.each(fieldPair.radio, function( key, value ) {
		    //console.debug("radio : " + key  + " => " + value );			 
		    $("#" + formId ).find("input:radio[name='" + key + "']:input[value='" + value + "']").prop("checked", true);  
		});
		
		$.each(fieldPair.checkbox, function( key, value ) {
		    // console.debug("checkbox : " + key  + " => " + value );			 
		    $("#" + formId ).find('input:checkbox[name="' + key + '"]').each(function() {
			//console.debug(this.value +":"+ value);
			if(this.value == value ){ //값 비교
			    this.checked = true; //checked 처리			    	
			}else {
			    this.checked = false;
			}
		    });	
			
		});
		
		$.each(fieldPair.select, function( key, value ) {
			 $("#" + formId ).find("select[name='" + key + "']").val(value);
		});
	    
	},	
	//form 컨틀롤 초기화
	initSerializeForm : function (formId,cbFunc) {
		
		var fieldPair = this.getSerializeForm(formId);
		// Serialize form fields as URI argument/HTTP POST data	
		for( var key in fieldPair.text ) {
		  //console.log("text : " + key + '=>' + fieldPair.text[key] );
		  $("#" + formId ).find("input[name='" + key + "']").val("");
		}
		
		for( var key in fieldPair.password ) {
		  //console.log("password : " + key + '=>' + fieldPair.password[key] );
		  $("#" + formId ).find("input[name='" + key + "']").val("");
		}
		
		for( var key in fieldPair.hidden ) {
		  $("#" + formId ).find("input[name='" + key + "']").val("");
		}
			
		for( var key in fieldPair.textarea ) {
		 // console.log("textarea : " + key + '=>' + fieldPair.textarea[key] );
		  $("#" + formId ).find("textarea[name='" + key + "']").val("");
		}
	
	
		for( var key in fieldPair.radio ) {
		  //console.log( "radio : " +key + '=>' + fieldPair.radio[key] );
		  $("#" + formId ).find("input:radio[name='" + key + "']").prop("checked", false);
		}
	
		for( var key in fieldPair.checkbox ) {
		  //console.log("checkbox : " + key + '=>' + fieldPair.checkbox[key] );
		  //$("#" + formId ).find("input:checkbox[name='" + key + "']").prop("checked", false);
		  $("#" + formId ).find("input:checkbox[name='" + key + "']").removeAttr("checked");  
		}
		
		for( var key in fieldPair.select ) {
		  $("#" + formId ).find("select[name='" + key + "']").val("");
		}
		
		if(typeof cbFunc === "function"){
			cbFunc();
		}	    
	},
	getPostData : function (formId) {
		var postData = {};
		var fieldPair = this.getSerializeForm(formId);
		// Serialize form fields as URI argument/HTTP POST data	
		for( var key in fieldPair.text ) {			  
		  postData[key] =  $("#" + formId ).find("input[name='" + key + "']").val();
		}
		
		for( var key in fieldPair.password ) {			  
		  postData[key] = $("#" + formId ).find("input[name='" + key + "']").val();
		}
		
		for( var key in fieldPair.hidden ) {
		 postData[key] = $("#" + formId ).find("input[name='" + key + "']").val();
		}
		
		for( var key in fieldPair.textarea ) {
		  postData[key] = $("#" + formId ).find("textarea[name='" + key + "']").val();
		}
	
	
		for( var key in fieldPair.radio ) {
		  postData[key] = $("#" + formId ).find("input:radio[name='" + key + "']:checked").val();
		}
	
		for( var key in fieldPair.checkbox ) {
		  postData[key] = [];
		  $("#" + formId ).find("input:checkbox[name='" + key + "']:checked").each(function() {
			  postData[key].push($(this).val());
		  });
			  //console.log(postData[key]);
		}
		
		for( var key in fieldPair.select ) {
		  postData[key] = $("#" + formId ).find("select[name='" + key + "']").val();
		}	
		return postData;	    
	},
};