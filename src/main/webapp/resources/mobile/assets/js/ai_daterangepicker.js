/**
 * 날짜범위 (하나만 사용할경우 beginDatePicker 사용함)
 * @author 최주섭
 * @since  2019
 * @version 1.0
 * @see
 * Copyright (C)  All right reserved.
*/
(function ($) {
			
    $.fn.aiDateRangePicker = function (options) {

        var aiDateRangePicker = this;
        var isDateRange = false; //false단일
        var beginDatePicker = null;
        var endDatePicker = null;
        
        var defaults = {
        	beginInputId : null, //시작날짜 입력 아이디
        	endInputId : null, //종료날짜 입력아이디
        	clearId : null, //지우기 아이디
        	
        	startMonthLimit : null,
			endMonthLimit : null,
    		initToday : 0 ,/*오늘부터 몇일전까지 초기설정할지*/
    		initAfterDay : 0 /*오늘부터 몇일후까지 초기설정할지*/
        };
        
        var settings = $.extend({}, defaults, options);

        var handler = [];

        var run = function(){
        	methods.init();
        	event.onClear().onBeginChange().onEndChange();          
            return methods;
        };

        var methods = {
    		
    		init : function(){    			
    			//기본날짜 설정
    			var nowDate = new Date();
    			
    			var datepickerOption = {
    					format : 'yyyy/mm/dd',
    		        	todayHighlight : true, //오늘날짜 표시
    		        	autoclose : true, // 선택 시 자동으로 달력 닫기
    		        	forceParse : true, //날짜 포맷 강제 변환
    		        	changeMonth : true,
    		        	changeYear : true
    			};
    			
    			if(!this.isEmpty(settings.startMonthLimit)){
    				datepickerOption.startDate = new Date(nowDate.getFullYear(),nowDate.getMonth() - settings.startMonthLimit , nowDate.getDate() , 0, 0, 0);
    			}
    			
    			if(!this.isEmpty(settings.endMonthLimit)){
    				datepickerOption.endDate = new Date(nowDate.getFullYear(),nowDate.getMonth() + settings.endMonthLimit , nowDate.getDate() , 0, 0, 0);
    			}			
    			
    			//시작 입력폼
    			if(!this.isEmpty(settings.beginInputId)){
    				beginDatePicker = aiDateRangePicker.find('#'+ settings.beginInputId).datepicker(datepickerOption);    				
    			}
    			//종료입력폼
    			if(!this.isEmpty(settings.endInputId)){
    				endDatePicker = aiDateRangePicker.find('#'+ settings.endInputId).datepicker(datepickerOption);
    			}
    			//범위 달력 여부 확인 및 설정
    			if(!this.isEmpty(beginDatePicker) && !this.isEmpty(endDatePicker) ){
    				isDateRange = true;    
    			}
    			if( settings.initToday > 0 ){
    				
    				var beginDate= new Date(nowDate.getFullYear(),nowDate.getMonth(),nowDate.getDate() - settings.initToday , 0, 0, 0);
        			var endDate  = nowDate;
        			
        			this.setBeginDate(beginDate);        			
        			if(isDateRange){
        				this.setEndDate(endDate);        				
        			}
    			}    			
    			else if( settings.initAfterDay > 0 ){
    				
    				var beginDate= nowDate;
        			var endDate  = new Date(nowDate.getFullYear(),nowDate.getMonth(),nowDate.getDate() + settings.initAfterDay , 0, 0, 0); 
        			
        			this.setBeginDate(beginDate);        			
        			if(isDateRange){
        				this.setEndDate(endDate);        				
        			}
    			}
    			
    			if(!methods.isEmpty(settings.clearId)){
        			aiDateRangePicker.find('#'+ settings.clearId).css("cursor","pointer"); 
    			}
    			
    		}, 
    		getBeginDate : function(){
    			return aiDateRangePicker.find('#'+ settings.beginInputId).val();
    		},
    		setBeginDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.beginInputId).datepicker('setDate', date);
    		},
    		setBeginMinDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.beginInputId).datepicker('setStartDate', this.addDay1(date));
    		},
    		setBeginMaxDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.beginInputId).datepicker('setEndDate', date);
    		},
    		getEndDate : function(){
    			return aiDateRangePicker.find('#'+ settings.endInputId).val();
    		},
    		setEndDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.endInputId).datepicker('setDate', date);
    		},
    		setEndMinDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.endInputId).datepicker('setStartDate', this.addDay1(date));
    		},
    		setEndMaxDate : function(date) {
    			aiDateRangePicker.find('#'+ settings.endInputId).datepicker('setEndDate', date);
    		},
    		clear : function(){
    			this.setBeginDate('');
    			this.setEndDate('');
    		},
    		// 빈문자열 검사 true : 빈값
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
    		formatDate : function(date) {
    			var year = date.getFullYear();//yyyy
    			var month = (1 + date.getMonth());//M
    			month = month >= 10 ? month : '0' + month;     // month 두자리로 저장
    			var day = date.getDate();//d
    			day = day >= 10 ? day : '0' + day;//day 두자리로 저장
    			return  year + '/' + month + '/' + day;
    		},
    		addDay1 : function(date) {
    			var cDate = date;
    			if(!this.isEmpty(cDate)){
    				var dateArr = date.split('/');//yyyy-mm-dd
        			cDate = this.formatDate(new Date(dateArr[0],Number(dateArr[1])-1,(Number(dateArr[2])+1)));
    			}
				return cDate;
    		}
        };
  
        var event = {     
        	onClear : function() {
        		if(!methods.isEmpty(settings.clearId)){
        			aiDateRangePicker.find('#'+ settings.clearId).on('click',function(e) {			
        				methods.clear();
            		});
    			}
        		return this;
        	},
	        onBeginChange : function() {
	        	aiDateRangePicker.find('#'+ settings.beginInputId).on('change',function(e,i){
	        		var beginDate = $(this).val();
	        		
	        		if(isDateRange){
	        		  //종료일자를 넘을수 없다
	        		  var endDate = methods.getEndDate();
	        		  if(!methods.isEmpty(endDate)){
	        			  var startDateArr = beginDate.split('/');
	        			  var endDateArr = endDate.split('/');
	        			  var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
	        		      var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);
	        		      if(startDateCompare.getTime() > endDateCompare.getTime()) {	        		             
	        		    	  methods.setEndDate('');
	        		      }  
	        			  
	        		  }	        			
	        		}
				});
				return this;
			},
			
			onEndChange : function() {
				aiDateRangePicker.find('#'+ settings.endInputId).on('change',function(e,i){
					var endDate = $(this).val();
					var beginDate = methods.getBeginDate();
					if(!methods.isEmpty(beginDate)){
						 var startDateArr = beginDate.split('/');
	        			  var endDateArr = endDate.split('/');
	        			  var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
	        		      var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);
	        		      if(startDateCompare.getTime() > endDateCompare.getTime()) {	        		             
	        		    	  methods.setBeginDate('');
	        		      }  

	        		}	 
				});
				return this;
			}
        };
        
        return run();
    }
})(jQuery);