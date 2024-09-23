var ExternalModuleAPI = undefined;

(function() {
	/**
	 * @namespace ExternalModuleAPI
	 */
	ExternalModuleAPI = {
	
		/**
		 * <내부 함수> 요청 보냄
		 * @Ignore
		 * @method _sendRequest
		 * @param {String} url - 요청을 보낼 url
		 * @param {Object} params - 요청의 body에 포함될 객체
		 * @param {Object} thisObj - 응답함수를 실행시킬 this 객체
		 * @param {Function} responseFunc - 응답시 실행할 함수
		 */
		_sendRequest : function(url, params, thisObj, responseFunc) {
			var ajaxEl = document.createElement('sc-ajax');
			ajaxEl.setAttribute('url', url);
			ajaxEl.body = params;
			
			if(responseFunc) {
				ajaxEl.addEventListener('response', function(event) {
					responseFunc.call(thisObj, event.detail.response);
				});
			}
			ajaxEl.generateRequest();
		},
			
		/**
		 * 일정 그룹 생성
		 * @method getAvailableGroup
		 * @memberof ExternalModuleAPI#
		 * @param {Object} thisObj - 응답 함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. 인자로 data를 가진다
		 * @returns {Array} 사용 가능한 그룹 목록
		 */
		getAvailableGroupList : function(thisObj, responseFunc) {
			var me = this;
			me._sendRequest('bp/common/calendar/getGroupList.do', undefined, thisObj, responseFunc);
		},
		
		/**
		 * 그룹의 상세 정보를 가져온다.
		 * @method getGroupDetail
		 * @memberof ExternalModuleAPI#
		 * @param {String} grp_id - 그룹 아이디
		 * @param {Object} thisObj - 응답 함수를 실행할 객체
		 * @param {Function} responseFunc - 응답 함수
		 * @return {Object} 입력한 그룹 아이디에 대한 상세 정보
		 */
		getGroupDetail : function(grp_id, thisObj, responseFunc) {
			var me = this;
			
			if(!grp_id) throw '정보를 가져올 그룹의 그룹 아이디가 없습니다.';
			
			// 요청 전송
			me._sendRequest('bp/common/calendar/getGroupDetail.do', {
				grp_id : grp_id
			}, thisObj, responseFunc);
		},
		
		/**
		 * 그룹 생성
		 * @method addGroup
		 * @memberof ExternalModuleAPI#
		 * @param {String} grp_nm - 생성할 그룹의 이름 (중복 허용)
		 * @param {String} grp_desc - 그룹의 설명
		 * @param {String} grp_color - 그룹의 컬러 코드
		 * @param {String} grp_font_color - 그룹의 폰트 컬러 코드
		 * @param {String} grp_border_color - 그룹의 보더 컬러 코드
		 * @param {Array} target_users - 공유 유저 목록
		 * @param {Array} target_depts - 공유 부서 목록
		 * @param {Array} target_vendors - 공유 협력사 목록
		 * @param {Array} target_jobs - 공유 직무 목록
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수
		 * @return {Object} 생성된 그룹의 아이디
		 */
		addGroup : function(grp_nm, grp_desc, grp_color, grp_font_color, grp_border_color,
				target_users, target_depts, target_vendors, target_jobs,
				thisObj, responseFunc) {
			var me = this;
			
			// 유효성 체크
			if(!grp_nm) throw '추가하려는 그룹의 그룹 명이 없습니다.';
			
			// 기본값 설정
			if(!grp_color || !grp_font_color || !grp_border_color) {
				grp_color = '#a02031';
				grp_font_color = '#fff';
				grp_border_color = '#901d2c';
			}
			
			// 정보 전송
			me._sendRequest('bp/common/calendar/saveGroup.do', {
				grp_nm : grp_nm,
				grp_desc : grp_desc,
				grp_color :  grp_color,
				grp_font_color : grp_font_color,
				target_users : target_users,
				target_jobs : target_jobs,
				target_depts : target_depts,
				target_vendors : target_vendors
			}, thisObj, responseFunc);
		},
		
		/**
		 * 그룹 업데이트
		 * @method updateGroup
		 * @memberof ExternalModuleAPI#
		 * @param {String} grp_id - 업데이트 할 그룹의 아이디
		 * @param {String} grp_nm - 그룹의 설명
		 * @param {String} grp_color - 그룹의 컬러 코드
		 * @param {String} grp_font_color - 그룹의 폰트 컬러
		 * @param {String} grp_border_color - 그룹의 보더 컬러
		 * @param {String} target_users - 공유 유저 목록
		 * @param {String} target_depts - 공유 부서 목록
		 * @param {String} target_vendors - 공유 협력사 목록
		 * @param {String} target_jobs - 공유 직무 목록 
		 * @param {Object} thisObj - 응답 합수의 this 객체
		 * @param {Function} responseFunc - 응답 함수. 인자로 data를 가짐
		 */
		updateGroup : function(
				grp_id, grp_nm, grp_desc, grp_color, grp_font_color, grp_border_color,
				target_users, target_depts, target_vendors, target_jobs,
				thisObj, responseFunc) {
			var me = this;
			
			// 유효성 체크
			if(!grp_id) throw '수정하려는 그룹의 그룹 아이디가 없습니다';
			
			// 전송 데이터 생성
			var sendData = { grp_id : grp_id };
			if(grp_nm) sendData.grp_nm = grp_nm;
			if(grp_desc) sendData.grp_desc = grp_desc;
			if(grp_color && grp_font_color && grp_border_color) {
				sendData.grp_color = grp_color;
				sendData.grp_font_color = grp_font_color;
				sendData.grp_border_color = grp_border_color;
			} else {
				console.log('배경색, 폰트색, 경계선 색 중 하나만을 변경할 수 없습니다.');
			}
			
			// 공유 정보 설정
			if(target_users) sendData.target_users = target_users;
			if(target_depts) sendData.target_depts = target_depts;
			if(target_vendors) sendData.target_vendors = target_vendors;
			if(target_jobs) sendData.target_jobs = target_jobs;
			
			// 정보 전송
			me._sendRequest('bp/common/calendar/updateScheduleGroup.do', sendData, thisObj, responseFunc);
		},
		
		/**
		 * 그룹 삭제
		 * @method delGroup
		 * @memberof ExternalModuleAPI#
		 * @param {String} grp_id - 삭제할 그룹 아이디
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. data 인자를 가진다.
		 */
		delGroup : function(grp_id, thisObj, responseFunc) {
			var me = this;
			
			if(!grp_id) throw '삭제하려는 그룹의 그룹 아이디가 없습니다.';
			
			me._sendRequest('bp/common/calendar/deleteScheduleGroup.do', {
				grp_id : grp_id
			}, thisObj, responseFunc);
		},
		
		/**
		 * 일정 상세 정보 요청
		 * @method getScheduleDetail
		 * @memberof ExternalModuleAPI#
		 * @param {String} sched_id - 정보를 얻어올 일정 아이디
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. data 인자를 가진다.
		 */
		getScheduleDetail : function(sched_id, thisObj, responseFunc) {
			
			if(!sched_id) throw '정보를 얻으려는 일정의 일정 아이디가 없습니다.';
			
			me._sendRequest('bp/common/calendar/getScheduleDetail.do', {
				sched_id : sched_id
			}, thisObj, responseFunc);
		},
		
		/**
		 * 일정 추가
		 * @method addSchedule
		 * @memberof ExternalModuleAPI#
		 * @param {String} sched_nm - 일정 명 (중복 허용)
		 * @param {String} sched_desc - 일정 설명
		 * @param {String} sched_loc - 일정 장소
		 * @param {String} grp_id - 일정이 속하는 그룹 아이디
		 * @param {Date} start_date - 일정 시작 시간
		 * @param {Date} end_date - 일정 종료 시간
		 * @param {Date} notice_date - 알람 시간
		 * @param {Date} notice - 알람 설정 문자
		 * @param {String} repeat - 반복 문자
		 * @param {Date} repeat_start - 반복 시작일
		 * @param {Date} repeat_end - 반복 종료일
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. data 인자를 가진다.
		 * @returns {Object} 생성된 일정의 아이디
		 */
		addSchedule : function(sched_nm, sched_desc, sched_loc, grp_id, 
				start_date, end_date, 
				notice_date, notice, 
				repeat, repeat_start, repeat_end,
				thisObj, responseFunc) {
			var me = this;
			
			// 유효성 체크
			if(!sched_nm) throw '일정 이름이 없습니다.';
			if(!grp_id) throw '연관된 그룹 아이디가 없습니다';
			if(!start_date) throw '일정 시작일이 없습니다';
			if(!end_date) throw '일정 종료일이 없습니다';
			if(notice && !notice_date) throw '알람 날짜가 존재하지 않습니다';
			if(repeat && (!repeat_start || !repeat_end)) throw '반복 시작 또는 종료일이 존재하지 않습니다.';
			
			// 데이터 전송
			me._sendRequest('bp/common/calendar/saveSchedule.do', {
				sched_nm : sched_nm, 
				sched_desc : sched_desc, 
				sched_loc : sched_loc, 
				grp_id : grp_id, 
				start_date : start_date, 
				end_date : end_date, 
				notice_date : notice_date, 
				notice : notice, 
				repeat : repeat, 
				repeat_start : repeat_start, 
				repeat_end : repeat_end,
				isNew : true
			}, thisObj, responseFunc);
		},
		
		/**
		 * 일정을 삭제한다.
		 * @method delSchedule
		 * @memberof ExternalModuleAPI#
		 * @param {String} sched_id - 삭제할 일정의 아이디
		 * @param {String} grp_id - 일정이 속한 그룹의 아이디
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. 인자로 data를 가짐
		 */
		delSchedule : function(sched_id, grp_id, thisObj, responseFunc) {
			var me = this;
			
			if(!grp_id) throw '삭제하려는 일정의 그룹 아이디가 없습니다.';
			if(!sched_id) throw '삭제하려는 일정의 일정 아이디가 없습니다.';
			
			me._sendRequest('bp/common/calendar/deleteSchedule.do', {
				sched_id : sched_id,
				grp_id : grp_id
			}, thisObj, responseFunc);
		},
		
		/**
		 * 일정 정보를 업데이트 한다.
		 * @method updateSchedule
		 * @memberof ExternalModuleAPI#
		 * @param {String} sched_id - 일정 아이디
		 * @param {String} sched_nm - 일정 명
		 * @param {String} sched_desc - 일정 설명
		 * @param {String} sched_loc - 일정 장소
		 * @param {String} grp_id - 일정이 속한 그룹
		 * @param {Date} start_date - 일정 시작 시간
		 * @param {Date} end_date - 일정 종료 시간
		 * @param {Date} notice_date - 알람 시간
		 * @param {Date} notice - 알람 설정 문자
		 * @param {String} repeat - 반복 문자
		 * @param {Date} repeat_start - 반복 시작일
		 * @param {Date} repeat_end - 반복 종료일
		 * @param {Object} thisObj - 응답함수를 실행할 this 객체
		 * @param {Function} responseFunc - 응답 함수. data 인자를 가진다.
		 * @returns {Object} 생성된 일정의 아이디 
		 */
		updateSchedule : function(
				sched_id, sched_nm, sched_desc, sched_loc, 
				grp_id, start_date, end_date, 
				notice_date, notice, repeat, repeat_start, repeat_end,
				thisObj, responseFunc) {
			var me = this;
			
			// 유효성 체크
			if(!sched_id) throw '일정 아이디가 존재하지 않습니다.';
			if(!grp_id) throw '일정이 속한 그룹을 지정하지 않았습니다';
			
			// 전송
			me._sendRequest('bp/common/calendar/updateSchedule.do', { 
				sched_id : sched_id, 
				sched_nm : sched_nm,
				sched_desc : sched_desc,
				sched_loc : sched_loc,
				notice : notice,
				notice_date : notice_date,
				repeat : repeat,
				repeat_start : repeat_start,
				repeat_end : repeat_end,
				grp_id : grp_id, 
				start_date : start_date, 
				end_date : end_date, 
				isNew : false
			}, thisObj, responseFunc);
		}
	};
})();