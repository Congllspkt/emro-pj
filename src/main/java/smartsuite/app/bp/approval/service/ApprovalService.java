package smartsuite.app.bp.approval.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.validator.ValidatorUtil;
import smartsuite.app.bp.approval.document.service.ApprovalDocumentService;
import smartsuite.app.bp.approval.line.service.ApprovalLineCcService;
import smartsuite.app.bp.approval.line.service.ApprovalLineService;
import smartsuite.app.bp.approval.link.service.ApprovalLinkService;
import smartsuite.app.bp.approval.repository.ApprovalRepository;
import smartsuite.app.bp.approval.scheduler.ApprovalExcuteService;
import smartsuite.app.bp.approval.scheduler.ApprovalSchedulerService;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;
import smartsuite.upload.StdFileService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 결재마스터 관련 처리하는 서비스 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @FileName ApprovalService.java
 * @package smartsuite.app.bp.admin.approval
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ApprovalService {
	
	static final Logger LOG = LoggerFactory.getLogger(ApprovalService.class);

	/** The shared service. */
	@Inject
	public SharedService sharedService;

	/** The factory. */
	@Inject
	public ApprovalFactory factory;
	
	/** The approval validator. */
	@Inject
	public ApprovalValidator approvalValidator;

	/** The attach service. */
	@Inject
	StdFileService stdFileService;

	/** The approval scheduler service. */
	@Inject
	ApprovalSchedulerService approvalSchedulerService;
	
	/** The common mail service. */
	@Inject
	MailService mailService;
	
	@Inject
	ApprovalRepository approvalRepository;

	@Inject
	ApprovalLineService approvalLineService;
	
	@Inject
	ApprovalLineCcService approvalLineCcService;
	
	@Inject
	ApprovalDocumentService approvalDocumentService;
	
	@Inject
	ApprovalLinkService approvalLinkService;
	
	@Inject
	MessageSource messageSource;


	/**
	 * 결재마스터를 등록한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertApprovalMaster
	 */
	public void insertApprovalMaster(Map<String, Object> param) {
		approvalRepository.insertApprovalMaster(param);
	}


	/**
	 * 결재마스터를 수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : updateApprovalMaster
	 */
	public void updateApprovalMaster(Map<String, Object> param) {
		approvalRepository.updateApprovalMaster(param);
	}

	/**
	 * 결재마스터 상태정보를 수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param approvalId - 결재 아이디
	 * @param approvalTypeCode - 결재 유형 코드
	 * @param statusCode - 결재 상태 코드
	 * @param applicationId - 모듈 applicationId
	 * @Date : 2016. 2. 2
	 * @Method Name : updateApprovalMasterStatus
	 */
	public void updateApprovalMasterStatusProcess(String approvalId, String approvalTypeCode, String statusCode, String applicationId) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("apvl_uuid", approvalId);

		if (ApprovalConst.APPROVAL_STS_APPROVED.equals(statusCode) || ApprovalConst.APPROVAL_STS_RETURN.equals(statusCode)) {	// 결재승인(C) or 결재반려(B)
			param.put("apvl_sts_ccd", statusCode);
			// 결재 마스터 상태 업데이트
			this.updateApprovalMasterStatus(param);
		} else if (ApprovalConst.APPROVAL_STS_CANCEL.equals(statusCode)) { // 상신취소(D)이면
			param.put("apvl_sts_ccd", ApprovalConst.APPROVAL_STS_TEMP_SAVE);	// 임시저장(T) 상태로 돌린다.
			// 결재 마스터 상태 업데이트
			this.updateApprovalMasterStatus(param);
		}
		// 업무 후 처리 Bean을 가져온 뒤 관련 Bean 내에 있는 후 처리 프로세스 진행 ( 상태 코드에 따라 결재상태 프로세스 진행 )
		factory.afterApprovalProcessing(approvalTypeCode, applicationId, statusCode);
	}

	/**
	 * 결재 마스터 상태 업데이트 
	 * @param param
	 */
	public void updateApprovalMasterStatus(Map<String, Object> param) {
		approvalRepository.updateApprovalMasterStatus(param);
	}
	

	/**
	 * 결재마스터를 삭제한다.
	 */
	public void deleteApprovalMaster(Map<String, Object> param) {
		approvalRepository.deleteApprovalMaster(param);
	}
	

	/**
	 * 결재마스터 상세정보를 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return map
	 * @Date : 2016. 2. 2
	 * @Method Name : findApprovalMaster
	 */
	public Map<String, Object> findApprovalMaster(Map<String, Object> param) {
		return approvalRepository.findApprovalMaster(param);
	}

	/**
	 * 결재 상세정보를 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return map {"approvalMaster", "approvalDoc", "approvalLines"}
	 * @Date : 2016. 2. 2
	 * @Method Name : findApprovalInfo
	 */
	public Map<String, Object> findApprovalInfo(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		String approvalUuid = (String)param.getOrDefault("apvl_uuid",""); // 결재 아이디
		
		if (StringUtils.isNotEmpty(approvalUuid)) {
			Map<String, Object> master = this.findApprovalMaster(param);
			master.put("max_aprv_rev", this.getMaxRevisionApprovalMaster(master));
			resultMap.put("approvalMaster", master);

			Map<String, Object> approvalDocument = approvalDocumentService.findApprovalDocument(param);
			
			String approvalStatusCode = (String) master.getOrDefault("apvl_sts_ccd","");
			//결재상태가 임시저장(T)일 경우에만 결재본문 재조회하도록
			if(MapUtils.isNotEmpty(approvalDocument) && ApprovalConst.APPROVAL_STS_TEMP_SAVE.equals(approvalStatusCode)){
				
				Map<String, Object> documentTemplate = approvalDocumentService.findApprovalDocumentTemplate(master);
				if(MapUtils.isNotEmpty(documentTemplate)) {
					String approvalDocumentContent = (String) documentTemplate.getOrDefault("appDocCont","");
					approvalDocument.put("apvl_body_cont", approvalDocumentContent);
				}
			}
			resultMap.put("approvalDoc", approvalDocument);
			resultMap.put("approvalLines", approvalLineService.findListApprovalLineProcess(param));
			resultMap.put("approvalCcLines", approvalLineCcService.findListApprovalLineCc(param));
		}
		
		return resultMap;
	}


	/**
	 * 결재마스터의 최대 차수.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return int
	 * @Date : 2016. 2. 2
	 * @Method Name : getMaxRevisionApprovalMaster
	 */
	public int getMaxRevisionApprovalMaster(Map<String, Object> param) {
		Integer rev = approvalRepository.getMaxRevisionApprovalMaster(param);
		return rev == null ? 1 : rev.intValue();
	}

	/**
	 * 업무 결재에 사용중인 결재 아이디 조회
	 * @param param
	 * @return
	 */
	public String findTaskApprovalInfoUsingForApprovalUuid(Map<String, Object> param) {
		return approvalLinkService.findTaskApprovalInfoUsingForApprovalUuid(param);
	}

	/**
	 * 결재 정보를 등록/수정한다. - 임시저장/상신
	 *
	 * @author : JongKyu Kim
	 * @param param {"approvalMaster", "approvalDoc", "insertApprovalLines", "updateApprovalLines", "deleteApprovalLines"}
	 * @return the map<string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : saveApproval
	 */
	public ResultMap saveApproval(Map<String, Object> param){
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> approvalMaster = (Map<String, Object>)param.get("approvalMaster");
		Map<String, Object> documentInfo = (Map<String, Object>)param.get("approvalDoc");
		List<Map<String, Object>> approvalLines = (List<Map<String, Object>>) param.getOrDefault("approvalLines", Lists.newArrayList());
		List<Map<String, Object>> approvalCcLines = (List<Map<String, Object>>) param.getOrDefault("approvalCcLines", Lists.newArrayList());

		if (MapUtils.isEmpty(approvalMaster)) {
			throw new CommonException(ErrorCode.NOT_EXIST);
		}
		
		// validation 수행
		resultMap = approvalValidator.validate(approvalMaster);

		// 결재마스터
		String approvalId = (String)approvalMaster.get("apvl_uuid"); // 결재 아이디
		if (StringUtils.isEmpty(approvalId)) { // 결재 신규 추가
			approvalId = UUID.randomUUID().toString();
			this.insertApproval(approvalMaster,approvalId);
		} else {  // 결재 마스터 수정
			this.updateApprovalMaster(approvalMaster);
		}

		// 결재본문
		if (MapUtils.isNotEmpty(documentInfo)) {
			documentInfo.put("apvl_uuid", approvalId);
			approvalDocumentService.saveApprovalDocumentProcess(approvalMaster,documentInfo);
		}
		
		// 결재선 초기화
		approvalLineService.deleteApprovalLineByApprovalUuid(approvalMaster);
		approvalLineCcService.deleteApprovalLineCcByApprovalUuid(approvalMaster);
		
		boolean draftApproval = false;
		if(approvalLines.size() == 1) {
			// 기안자만 존재하는 기안자 전결
			for(int i = 0; i < approvalLines.size(); i++) {
				Map<String, Object> approvalLine = approvalLines.get(i);
				approvalLine.put("last_apvr_yn", "Y");
				draftApproval = true;
			}
		}
		
		if(approvalLines.size() > 0) {
			approvalLineService.insertListApprovalLine(approvalLines,approvalId);
		}
		
		if(approvalCcLines.size() > 0) {
			approvalLineCcService.insertListApprovalLineCc(approvalCcLines, approvalId);
		}
		
		// 결재선 결재 결과에 따른 결재 마스터 상태 수정
		this.updateApprovalMasterStatusProcess(approvalId, (String) approvalMaster.get("apvl_typ_ccd"), (String) approvalMaster.get("apvl_sts_ccd"), (String) approvalMaster.get("task_uuid")); // 결재마스터 상태 수정
		
		// 상신인 경우
		if("PRGSG".equals(approvalMaster.get("apvl_sts_ccd"))) {
			// 다음 결재자 세팅
			this.nextStepApproval(approvalId);
			
			// 참조자에게 메일 발송
			mailService.sendAsync("APPROVAL_REF_MAIL", approvalId);
			
			// 기안자 전결
			if(draftApproval) {
				this.lastApprovalApproved(approvalId, (String) approvalMaster.get("apvl_typ_ccd"), "APVD", (String) approvalMaster.get("task_uuid"));
			}
		}
		
		Map<String,Object> setParamMap = Maps.newHashMap();
		setParamMap.put("apvl_uuid",approvalId);
		resultMap.setResultData(setParamMap);
		return resultMap;
	}

	/**
	 * 결재 신규 추가
	 *
	 * @param approvalMaster
	 * @param approvalId
	 */
	public void insertApproval(Map<String, Object> approvalMaster, String approvalId) {
		approvalMaster.put("apvl_uuid", approvalId);

		// 결재마스터
		String documentNumber = (String)approvalMaster.get("apvl_docno");
		if (StringUtils.isEmpty(documentNumber)) {
			approvalMaster.put("apvl_docno", sharedService.generateDocumentNumber("AP")); // 결재 문서번호
			approvalMaster.put("apvl_revno", 1); // 차수
		}
		this.insertApprovalMaster(approvalMaster);

		// 업무 결재 연동
		Map<String, Object> link = Maps.newHashMap();
		link.put("task_uuid"    , approvalMaster.get("task_uuid"));		// app id
		link.put("apvl_typ_ccd", approvalMaster.get("apvl_typ_ccd"));	// 결재 유형 코드

		approvalLinkService.updateApprovalLinkNotUsed(link); // 기존에 등록되어 있던 연동 정보는 사용안함

		link.put("apvl_uuid", approvalId);
		approvalLinkService.insertTaskAndApprovalLink(link); // 새로 등록되는 연동정보를 사용함
	}
	
	/**
	 * 결재 다음 Step 진행
	 *
	 * @param apvlId
	 */
	public void nextStepApproval(String apvlId) {
		// Next Step 진행 가능여부 판단
		Map param = Maps.newHashMap();
		param.put("apvl_uuid", apvlId);
		// 승인자 중 마지막 순번 조회
		int lastApvdApvllnSort = approvalLineService.findLastApvdApvllnSort(param);
		
		// 현재 순번의 모든 결재 완료여부 판단
		param.put("last_apvd_apvlln_sort", lastApvdApvllnSort);
		boolean valid = approvalLineService.isCompleteCurrStepApprovalLine(param);
		if(!valid) {
			// Next Step 진행 불가
			// 현재 순번의 모든 결재가 완료나지 않음
			return;
		}
		
		// Next Step 진행
		param.put("next_apvlln_sort", lastApvdApvllnSort + 1);
		List<Map<String, Object>> findListNextApvllnApprover = approvalLineService.findListNextApvllnApprover(param);
		for(Map<String, Object> nextApvllnApprover : findListNextApvllnApprover) {
			nextApvllnApprover.put("curr_apvr_yn", "Y");
			approvalLineService.updateCurrApvrYn(nextApvllnApprover);
			
			// 다음 결재 / 합의 진행자에게 메일 발송
			mailService.sendAsync("APPROVAL_REQ_MAIL", apvlId, param);
			mailService.sendAsync("APPROVAL_AGREE_MAIL", apvlId, param);
		}
	}

	/**
	 * Adds the minutes to date.
	 *
	 * @param minutesInMills the minutes in mills
	 * @param beforeTime the before time
	 * @return the date
	 */
	public static Date addMinutesToDate(long minutesInMills, Date beforeTime){
	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + minutesInMills);
	    return afterAddingMins;
	}

	/**
	 * 일괄결재.
	 *
	 * @param param the param
	 * @return the map
	 */
	public ResultMap batchApproveProcess(Map<String, Object> param){
		List<Map<String, Object>> invalids = Lists.newArrayList();
		List<Map<String, Object>> notExists = Lists.newArrayList();
		List<String> aprvIds = (List<String>)((param.get("aprv_ids") == null) ? Lists.newArrayList() : param.get("aprv_ids"));
		List<Map<String, Object>> checkedItems = (List<Map<String, Object>>)((param.get("checked_items") == null) ? Lists.newArrayList() : param.get("checked_items"));
		
		
		Date startScheduleTime = new Date();
		if(checkedItems.size() > 99) {
			startScheduleTime = addMinutesToDate(20000, startScheduleTime);
		}
		
		for(Map<String, Object> approvalInfo : checkedItems) {
			Map<String, Object> searchParam = Maps.newHashMap();
			String approvalId = approvalInfo.get("apvl_uuid") == null? "" : approvalInfo.get("apvl_uuid").toString();
			String deputyApprovalYn = approvalInfo.get("dpty_aprv_yn") == null? "N" : approvalInfo.get("dpty_aprv_yn").toString();
			
			searchParam.put("apvl_uuid", approvalId);
			searchParam.put("dpty_aprv_yn", deputyApprovalYn);
			
			Map<String, Object> approvalLine = approvalLineService.findMyApprovalLine(searchParam);
			if(MapUtils.isNotEmpty(approvalLine)) {
				String approvalStatusCode = (String) approvalLine.getOrDefault("apvl_sts_ccd","") == null? "" : approvalLine.get("apvl_sts_ccd").toString();
				String currentApprovalYn = (String) approvalLine.getOrDefault("curr_apvr_yn","") == null? "" : approvalLine.get("curr_apvr_yn").toString();
				
				// 결재진행중이고 현재결재순서인 경우
				if(ApprovalConst.APPROVAL_STS_PROGRESSING.equals(approvalStatusCode) && "Y".equals(currentApprovalYn)) {
					String approvalErrorCode = approvalLine.get("apvlln_apvl_res_ccd") == null? "" : approvalLine.get("apvlln_apvl_res_ccd").toString(); // 결재 승인에러코드 가져오기
					if(!(StringUtils.isNotEmpty(approvalErrorCode) && approvalErrorCode.equals(ApprovalConst.APPROVAL_RESULT_PROCESS_PROGRESSING))){ //결재진행 시 하기 로직을 태우지 않음.
						Map<String, Object> aprvInfo = Maps.newHashMap();
						aprvInfo.put("apvl_uuid"   , approvalId);
						aprvInfo.put("apvl_res_ccd", param.get("apvl_res_ccd"));
						aprvInfo.put("apvl_opn"  , param.get("apvl_opn"));
						aprvInfo.put("apvlln_uuid" , approvalLine.get("apvlln_uuid"));
						aprvInfo.put("usr_id"    , approvalLine.get("usr_id"));
						if("Y".equals(deputyApprovalYn)) {
							aprvInfo.put("dpty_apvl_uuid", approvalLine.get("dpty_apvl_uuid"));
							aprvInfo.put("dpty_aprv_yn", deputyApprovalYn);
						}
						startScheduleTime = addMinutesToDate(3000, startScheduleTime);
						aprvInfo.put("startScheduleTime", startScheduleTime);
						approvalSchedulerService.saveApprovalLineResult(aprvInfo);


						Map<String, Object> updateParam = Maps.newHashMap();
						updateParam.put("apvl_uuid"   , approvalId);
						updateParam.put("apvl_res_ccd", ApprovalConst.APPROVAL_RESULT_PROCESS_PROGRESSING);	// 결재 승인처리 오류상태: 결재승인 처리중
						this.updateApprovalErrorCodeAndMessage(updateParam);						// 결재 승인처리 오류상태 업데이트
					}
				} else {
					invalids.add(approvalLine);
				}
			} else {
				notExists.add(param);
			}
		}
		return ValidatorUtil.setupDataListForValidationDataList(aprvIds, invalids, notExists);
	}
	
	/**
	 * 결재 결과 처리 관련 error log.
	 *
	 * @param param {apvl_uuid, apvl_res_ccd, apvl_err_cont}
	 * <pre>
	 * apvl_uuid: 결재 마스터 id
	 * apvl_res_ccd: (
	 * 	  	ApprovalConst.APRV_PROG="P": 결재 결과 처리중
	 * 	  	ApprovalConst.APRV_NON_ERR="N": 결재결과 처리완료
	 * 	  	ApprovalConst.APRV_ERR="E": 결재결과 처리오류
	 * 	   )
	 * apvl_err_cont: exception 내용
	 * </pre>
	 * @Date : 2021. 3. 23
	 * @Method Name : updateApprovalErrorCodeAndMessage
	 */
	public void updateApprovalErrorCodeAndMessage(Map<String, Object> param) {
		approvalRepository.updateApprovalErrorCodeAndMessage(param);
	}
	
	/**
	 * 결재선 결과 저장 (ApprovalExcuteService에서 호출되는 서비스)
	 * <pre>
	 * Checked Exception을 포함한 모든 Exception 발생 시 rollback 처리한다.
	 * </pre>
	 * @param param {apvl_uuid, apvlln_uuid, usr_id, apvlln_apvl_res_ccd, apvl_opn}
	 * @see ApprovalExcuteService
	 */
	@Transactional(rollbackFor={Exception.class})
	public void saveApprovalLineResult(Map<String, Object> param){
		Map<String, Object> approvalMaster = this.findApprovalMaster(param); // 결재마스터 정보
		String apvlUuid = (String) approvalMaster.get("apvl_uuid");
		String apvlTypCcd = (String) approvalMaster.get("apvl_typ_ccd");
		String taskId = (String) approvalMaster.get("task_uuid");
		String apvlResCcd = (String) param.get("apvl_res_ccd");
		
		approvalLineService.updateApprovalResultInfoByApprovalLine(param); // 결재선 결과 수정
		//this.updateApproval(param.get("apvl_uuid").toString()); // 결재 상태 정보를 수정
		this.nextStepApproval(apvlUuid);
		
		// 결재선 결재 결과에 따른 결재 마스터 상태 수정
		this.updateApprovalMasterStatusProcess(apvlUuid, apvlTypCcd, apvlResCcd, taskId); // 결재마스터 상태 수정
	}
	
	/**
	 * 결재 승인/반려/열람.
	 *
	 * @author : JongKyu Kim
	 * @param param {"approvalInfo"} - apvl_uuid(결재 아이디), aprvnl_id(결재선 아이디), usr_id(사용자 아이디), apvl_res_ccd(결재 결과코드), apvl_opn(결재 의견)
	 * @return the map<string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : approval
	 */
	public Map<String, Object> saveResultApproval(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();

		Map<String, Object> info = (Map<String, Object>)param.get("approvalInfo");
		// 결재선 변경건이 존재하는 경우 전체 결재라인
		// 결재선 변경이 없으면 null
		List<Map<String, Object>> aprvLineList = (List<Map<String, Object>>) param.get("aprvLineList");
		// 참조 라인의 경우 추가만 가능하므로 신규 참조라인만 들어올 수 있음
		List<Map<String, Object>> insertAprvLineCcList = (List<Map<String, Object>>) param.get("insertAprvLineCcList");
		// 수정은 수신 -> 참조인 경우만 가능
		List<Map<String, Object>> updateAprvLineCcList = (List<Map<String, Object>>) param.get("updateAprvLineCcList");
		// 삭제의 경우 수신 라인만 가능함
		List<Map<String, Object>> removeAprvLineCcList = (List<Map<String, Object>>) param.get("removeAprvLineCcList");
		
		String aprvId = info.get("apvl_uuid") == null ? "" : (String)info.get("apvl_uuid"); // 결재 아이디
		String deputyApprovalYn = info.get("dpty_aprv_yn") == null ? "" : (String)info.get("dpty_aprv_yn");//대리결재여부
		
		if(!StringUtils.isEmpty(deputyApprovalYn) && "Y".equals(deputyApprovalYn)) {
			Map<String, Object> deputyApprovalMap = approvalRepository.selectDeputyApprovalUuid(info);
			info.put("dpty_apvl_uuid", (String)deputyApprovalMap.get("dpty_apvl_uuid"));
		}
		
		Map<String, Object> approvalMaster = this.findApprovalMaster(info); // 결재마스터 정보
		String apvlTypCcd = (String) approvalMaster.get("apvl_typ_ccd");
		String taskId = (String) approvalMaster.get("task_uuid");
		String apvllnApvlResCcd = (String) info.get("apvlln_apvl_res_ccd");
		
		// 결재 / 합의 라인 변경 건이 존재하므로 현재 결재자 이후 순번에 대해서만 삭제 후 다시 넣음
		if(aprvLineList != null && !aprvLineList.isEmpty()) {
			int currApvllnSort = Integer.parseInt(info.get("apvlln_sort").toString());
			Map deleteParam = Maps.newHashMap();
			deleteParam.put("apvl_uuid", approvalMaster.get("apvl_uuid"));
			// 현재 결재자 순번
			deleteParam.put("apvlln_sort", currApvllnSort);
			// 현재 결재자 순번 이후 순번 전부 삭제
			approvalLineService.deleteApprovalLineAfterApvllnSort(deleteParam);
			
			// 현재 결재자 순번보다 이후 순번만 저장
			List<Map<String, Object>> approvalLines = Lists.newArrayList();
			for(int i = 0; i < aprvLineList.size(); i++) {
				Map aprvLineInfo = aprvLineList.get(i);
				int apvllnSort = Integer.parseInt(aprvLineInfo.get("apvlln_sort").toString());
				if(currApvllnSort < apvllnSort) {
					approvalLines.add(aprvLineInfo);
				}
			}
			approvalLineService.insertListApprovalLine(approvalLines, (String) approvalMaster.get("apvl_uuid"));
		}
		
		// 수신/참조 라인 추가
		approvalLineCcService.insertListApprovalLineCc(insertAprvLineCcList, (String) approvalMaster.get("apvl_uuid"));
		// 수신 라인 수정
		approvalLineCcService.updateLineApprovalLineCc(updateAprvLineCcList);
		// 수신 라인 삭제
		approvalLineCcService.removeListApprovalLineCc(removeAprvLineCcList);
		
		approvalLineService.updateApprovalResultInfoByApprovalLine(info); // 결재선 결과 수정
		
		if("APVD".equals(info.get("apvlln_apvl_res_ccd"))) {
			if("Y".equals(info.get("last_apvr_yn"))) {
				this.lastApprovalApproved(aprvId, apvlTypCcd, apvllnApvlResCcd, taskId);
			} else {
				// 결재자 / 합의자 승인 시
				this.nextStepApproval(aprvId);
				//this.updateApproval(aprvId); // 결재 상태 정보를 수정
				
				// 현재 결재자여부 N 업데이트
				info.put("curr_apvr_yn", "N");
				approvalLineService.updateCurrApvrYn(info);
			}
			
			// 수신/참조 추가 수정에 따라 참조자가 신규로 생긴 경우 참조자에게 메일 발송
			// 수신 : 결재 완료 시 메일 발송
			// 참조 : 결재 상신 시 메일 발송
			Map<String, Object> data = Maps.newHashMap();
			List<String> newRefUsrIds = Lists.newArrayList();
			for(int i = 0; i < insertAprvLineCcList.size(); i++) {
				Map<String, Object> aprvLineCcInfo = insertAprvLineCcList.get(i);
				if("REF".equals(aprvLineCcInfo.get("rdg_typ_ccd"))) {
					newRefUsrIds.add((String) aprvLineCcInfo.get("usr_id"));
				}
			}
			for(int i = 0; i < updateAprvLineCcList.size(); i++) {
				Map<String, Object> aprvLineCcInfo = updateAprvLineCcList.get(i);
				if("REF".equals(aprvLineCcInfo.get("rdg_typ_ccd"))) {
					newRefUsrIds.add((String) aprvLineCcInfo.get("usr_id"));
				}
			}
			if(!newRefUsrIds.isEmpty() && newRefUsrIds.size() > 0) {
				data.put("newRefUsrIds", newRefUsrIds);
				mailService.sendAsync("APPROVAL_REF_MAIL", aprvId, data);
			}
		} else {
			// 결재자 / 합의자 반려 시
			// 결재선 결재 결과에 따른 결재 마스터 상태 수정
			this.updateApprovalMasterStatusProcess(aprvId, apvlTypCcd, apvllnApvlResCcd, taskId); // 결재마스터 상태 수정
			
			// 반려 메일 기안자에게 발송
			mailService.sendAsync("APPROVAL_RETURN_MAIL", aprvId);
			// 해당 결재 순번의 결재/합의 진행하지 않은 담당자에게 반려 알림 메일 발송
			mailService.sendAsync("APPROVAL_PARLL_RETURN_MAIL", aprvId, info);
		}
		
		return resultMap;
	}
	
	public void lastApprovalApproved(String aprvId, String apvlTypCcd, String apvlStsCcd, String taskId) {
		// 최종 결재자 승인 시
		// 결재선 결재 결과에 따른 결재 마스터 상태 수정
		this.updateApprovalMasterStatusProcess(aprvId, apvlTypCcd, apvlStsCcd, taskId); // 결재마스터 상태 수정
		
		// 결재 메일 기안자에게 발송
		mailService.sendAsync("APPROVAL_COMPLETE_MAIL", aprvId);
		// 결재 메일 수신자에게 발송
		mailService.sendAsync("APPROVAL_ALRAM_MAIL", aprvId);
	}

	/**
	 * 상신취소한다.
	 *
	 * @author : JongKyu Kim
	 * @param param {"cancelApproval"} - apvl_uuid, apvl_typ_ccd, task_uuid
	 * @return the map< string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : cancelApproval
	 */
	public ResultMap cancelApproval(Map<String, Object> param) {
		Map<String, Object> cancelApproval = (Map<String, Object>)param.get("cancelApproval");

		if (MapUtils.isNotEmpty(cancelApproval)) {
			String approvalId = (String)cancelApproval.get("apvl_uuid"); // 결재 아이디
			String approvalTypeCode = (String)cancelApproval.get("apvl_typ_ccd"); // 결재 유형 코드
			String taskId = (String)cancelApproval.get("task_uuid"); // app id
			this.updateApprovalMasterStatusProcess(approvalId, approvalTypeCode, ApprovalConst.APPROVAL_STS_CANCEL, taskId); // 결재마스터 상태 수정(상신취소)
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 결재마스터/결재본문/결재선/업무결재연동 목록을 삭제한다.(상신취소)
	 *
	 * @author : JongKyu Kim
	 * @param param {"deleteApproval"} - apvl_uuid, apvl_typ_ccd, task_uuid
	 * @return the map< string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteApproval
	 */
	public ResultMap deleteApproval(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> delete = (Map<String, Object>)param.get("deleteApproval");
		
		if (delete == null || delete.isEmpty()) {
			throw new CommonException(ErrorCode.NOT_EXIST);
		}
		
		// validation 수행
		resultMap = approvalValidator.validate(delete);
		this.deleteApprovalMaster(delete); // 결재마스터 삭제
		approvalDocumentService.deleteApprovalDocument(delete); // 결재본문 삭제
		approvalLineService.deleteApprovalLineByApprovalUuid(delete); // 결재선 삭제
		approvalLinkService.deleteApprovalLink(delete); // 업무결재연동 삭제
		return resultMap;
	}
	
	/**
	 * 결재 재상신.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Method Name : reSubmitApproval
	 */
	public ResultMap reSubmitApproval(Map<String, Object> param) {
		Map<String, Object> approvalMaster = approvalRepository.findApprovalMasterForReSubmit(param);

		
		if(MapUtils.isEmpty(approvalMaster)) {
			throw new CommonException(ErrorCode.NOT_EXIST);
		} else {
			// 이전차수의 첨부파일 존재 시 복사
			String prevApprovalAttachId = (String)approvalMaster.get("prev_athg_uuid");
			if(!StringUtils.isEmpty(prevApprovalAttachId)) {
				String copyAttachNumber = stdFileService.copyFile(prevApprovalAttachId);
				approvalMaster.put("athg_uuid", copyAttachNumber);
			}

			//재상신용 결재본문 상세정보 조회
			Map<String, Object> approvalDocument = approvalDocumentService.findApprovalDocumentForReSubmit(param);

			//재상신용 결재선 목록 조회
			List<Map<String, Object>> approvalLineList = approvalLineService.findListApprovalLineForReSubmit(param);
			List<Map<String, Object>> approvalLineCcList = approvalLineCcService.findListApprovalLineCcForReSubmit(param);
			
			Map<String, Object> saveParam = Maps.newHashMap();
			saveParam.put("approvalMaster"     , approvalMaster);
			saveParam.put("approvalDoc"        , approvalDocument);
			saveParam.put("approvalLines", approvalLineList);
			saveParam.put("approvalCcLines", approvalLineCcList);
			
			return saveApproval(saveParam);
		}
	}
	
	/**
	 * 결재선을 저장한다.
	 *
	 * @author : wskim
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 7. 16
	 * @Method Name : saveApprovalLine
	 */
	public Map<String, Object> saveApprovalLine(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> insertLines = param.getOrDefault("insertApprovalLines",Lists.newArrayList()) == null? Lists.newArrayList() : (List<Map<String, Object>>)param.get("insertApprovalLines");
		List<Map<String, Object>> updateLines = param.getOrDefault("updateApprovalLines",Lists.newArrayList()) == null? Lists.newArrayList() : (List<Map<String, Object>>)param.get("updateApprovalLines");
		List<Map<String, Object>> deleteLines = param.getOrDefault("deleteApprovalLines",Lists.newArrayList()) == null? Lists.newArrayList() : (List<Map<String, Object>>)param.get("deleteApprovalLines");
		
		
		if (insertLines.size() > 0) {
			approvalLineService.insertListApprovalLine(insertLines);
		}
		if (updateLines.size() > 0) {
			approvalLineService.updateListApprovalLine(updateLines);
		}
		if (deleteLines.size() > 0) {
			approvalLineService.deleteListApprovalLine(deleteLines);
		}
		
		return resultMap;
	}
	
	/**
	 * 결재선을 조회한다.
	 *
	 * @author : wskim
	 * @param param the param
	 * @return the list
	 * @Date : 2018. 7. 16
	 * @Method Name : saveApprovalLine
	 */
	public List<Map<String, Object>> findListApprovalLineProcess(Map<String, Object> param){
		return approvalLineService.findListApprovalLineProcess(param);
	}
	
	/**
	 * 결재선, 수신, 참조 데이터를 결재창 표현 방식으로 변경
	 *
	 * @param param
	 * @return
	 */
	public Map convertDisplayApprover(Map param) {
		List<Map<String, Object>> aprvLineList = (List<Map<String, Object>>) param.get("aprvLineList");
		List<Map<String, Object>> aprvLineCcList = (List<Map<String, Object>>) param.get("aprvLineCcList");
		
		Map<String, Object> result = Maps.newHashMap();
		Map<String, Object> drafter = this.convertDisplayDrafter(aprvLineList);
		result.put("drafter", drafter);
		result.put("approver", this.convertDisplayApprover(aprvLineList));
		result.put("lastApprover", this.convertDisplayLastApprover(aprvLineList));
		result.put("agreementList", this.convertDisplayAgreement(drafter, aprvLineList));
		result.put("receiptList", this.convertDisplayReceipt(aprvLineCcList));
		result.put("referenceList", this.convertDisplayReference(aprvLineCcList));
		return result;
	}
	protected Map convertDisplayDrafter(List<Map<String, Object>> aprvLineList) {
		Map<String, Object> drafter = Maps.newHashMap();
		for(int i = 0; i < aprvLineList.size(); i++) {
			Map<String, Object> aprvLineInfo = aprvLineList.get(i);
			if("DFT".equals(aprvLineInfo.get("apvr_typ_ccd"))) {
				drafter = aprvLineInfo;
				break;
			}
		}
		if(!drafter.isEmpty()) {
			if("APVD".equals(drafter.get("apvlln_apvl_res_ccd"))) {
				drafter.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("기안", null, "기안", LocaleContextHolder.getLocale()));
			}
		}
		return drafter;
	}
	protected Map convertDisplayApprover(List<Map<String, Object>> aprvLineList) {
		Map<String, Object> approverMap = Maps.newHashMap();
		List<Map<String, Object>> approverList = Lists.newArrayList();
		for(int i = 0; i < aprvLineList.size(); i++) {
			Map<String, Object> aprvLineInfo = aprvLineList.get(i);
			if("APVL".equals(aprvLineInfo.get("apvr_typ_ccd")) && "N".equals(aprvLineInfo.get("last_apvr_yn"))) {
				approverList.add(aprvLineInfo);
			}
		}
		for(int i = 0; i < approverList.size(); i++) {
			Map<String, Object> approver = approverList.get(i);
			approver.put("apvl_sort", i + 1); // 순수 결재자 순서 표시
			
			if("APVD".equals(approver.get("apvlln_apvl_res_ccd"))) {
				approver.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("결재", null, "결재", LocaleContextHolder.getLocale()));
			} else if("RET".equals(approver.get("apvlln_apvl_res_ccd"))) {
				approver.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("반려", null, "반려", LocaleContextHolder.getLocale()));
			}
			if(approver.get("dpty_usr_nm") != null) {
				approver.put("usr_nm", approver.get("usr_nm") + " > " + approver.get("dpty_usr_nm"));
			}
			approverMap.put("approver" + (i + 1), approver);
		}
		return approverMap;
	}
	protected List<Map<String, Object>> convertDisplayAgreement(Map<String, Object> drafter, List<Map<String, Object>> aprvLineList) {
		List<Map<String, Object>> agreementList = Lists.newArrayList();
		for(int i = 0; i < aprvLineList.size(); i++) {
			Map<String, Object> aprvLineInfo = aprvLineList.get(i);
			if("AG".equals(aprvLineInfo.get("apvr_typ_ccd")) || "PARLL_AG".equals(aprvLineInfo.get("apvr_typ_ccd"))) {
				agreementList.add(aprvLineInfo);
			}
		}
		List<Map<String, Object>> approverList = Lists.newArrayList();
		for(int i = 0; i < aprvLineList.size(); i++) {
			Map<String, Object> aprvLineInfo = aprvLineList.get(i);
			if("APVL".equals(aprvLineInfo.get("apvr_typ_ccd")) && "N".equals(aprvLineInfo.get("last_apvr_yn"))) {
				approverList.add(aprvLineInfo);
			}
		}
		
		for(int i = 0; i < agreementList.size(); i++) {
			Map<String, Object> agreementInfo = agreementList.get(i);
			int agreementApvllnSort;
			if(agreementInfo.get("apvlln_sort") instanceof BigDecimal) {
				agreementApvllnSort = ((BigDecimal) agreementInfo.get("apvlln_sort")).intValue();
			} else {
				agreementApvllnSort = (Integer) agreementInfo.get("apvlln_sort");
			}
			
			List<Map<String, Object>> fastSortApproverList = Lists.newArrayList();
			for(int j = 0; j < aprvLineList.size(); j++) {
				Map<String, Object> aprvLineInfo = aprvLineList.get(j);
				int apvllnSort;
				if(aprvLineInfo.get("apvlln_sort") instanceof BigDecimal) {
					apvllnSort = ((BigDecimal) aprvLineInfo.get("apvlln_sort")).intValue();
				} else {
					apvllnSort = (Integer) aprvLineInfo.get("apvlln_sort");
				}
				
				if("APVL".equals(aprvLineInfo.get("apvr_typ_ccd")) && "N".equals(aprvLineInfo.get("last_apvr_yn")) && apvllnSort < agreementApvllnSort) {
					fastSortApproverList.add(aprvLineInfo);
				}
			}
			
			if(fastSortApproverList.size() == 0) {
				// 합의자 입장에서 결재라인에 상위 결재자가 없다면 상위는 기안자
				// 최종승인자 제외 합의자 별 상위 결재자 정보
				agreementInfo.put("apvl_sort", drafter.get("apvl_sort"));
				agreementInfo.put("up_usr_nm", drafter.get("usr_nm"));
				agreementInfo.put("description", messageSource.getMessage("기안 후 합의", null, "기안 후 합의", LocaleContextHolder.getLocale()));
			} else {
				// 합의자의 상위 결재자 정보
				Map<String, Object> upApprover = fastSortApproverList.get(fastSortApproverList.size() - 1);
				Map<String, Object> approver = null;
				for(int j = 0; j < approverList.size(); j++) {
					Map<String, Object> approverInfo = approverList.get(j);
					String usrId = approverInfo.get("usr_id") == null ? "" : (String)approverInfo.get("usr_id");
					String upAppUsrId = upApprover.get("usr_id") == null ? "" : (String)upApprover.get("usr_id");
					if(usrId.equals(upAppUsrId)) {
						approver = approverInfo;
					}
				}
				if(approver != null) {
					// 최종승인자 제외 합의자 별 상위 결재자 정보
					agreementInfo.put("apvl_sort", approver.get("apvl_sort"));
					agreementInfo.put("up_usr_nm", approver.get("usr_nm"));
					agreementInfo.put("description", messageSource.getMessage("결재 후 합의", null, "결재 후 합의", LocaleContextHolder.getLocale()));
				}
			}
			
			if("APVD".equals(agreementInfo.get("apvlln_apvl_res_ccd"))) {
				agreementInfo.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("합의", null, "합의", LocaleContextHolder.getLocale()));
			} else if("RET".equals(agreementInfo.get("apvlln_apvl_res_ccd"))) {
				agreementInfo.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("반려", null, "반려", LocaleContextHolder.getLocale()));
			}
		}
		return agreementList;
	}
	protected Map convertDisplayLastApprover(List<Map<String, Object>> aprvLineList) {
		Map<String, Object> lastApprover = Maps.newHashMap();
		for(int i = 0; i < aprvLineList.size(); i++) {
			Map<String, Object> aprvLineInfo = aprvLineList.get(i);
			if("APVL".equals(aprvLineInfo.get("apvr_typ_ccd")) && "Y".equals(aprvLineInfo.get("last_apvr_yn"))) {
				lastApprover = aprvLineInfo;
				break;
			}
		}
		if(!lastApprover.isEmpty()) {
			if("APVD".equals(lastApprover.get("apvlln_apvl_res_ccd"))) {
				lastApprover.put("apvlln_apvl_res_ccd_nm", messageSource.getMessage("결재", null, "결재", LocaleContextHolder.getLocale()));
			}
			if(lastApprover.get("dpty_usr_nm") != null) {
				lastApprover.put("usr_nm", lastApprover.get("usr_nm") + " > " + lastApprover.get("dpty_usr_nm"));
			}
		}
		return lastApprover;
	}
	protected List<Map<String, Object>> convertDisplayReceipt(List<Map<String, Object>> aprvLineCcList) {
		List<Map<String, Object>> receiptList = Lists.newArrayList();
		for(int i = 0; i < aprvLineCcList.size(); i++) {
			Map<String, Object> aprvLineCcInfo = aprvLineCcList.get(i);
			if("RCPT".equals(aprvLineCcInfo.get("rdg_typ_ccd"))) {
				receiptList.add(aprvLineCcInfo);
			}
		}
		for(int i = 0; i < receiptList.size(); i++) {
			Map<String, Object> receiptInfo = receiptList.get(i);
			
			if(i != (receiptList.size() - 1)) {
				receiptInfo.put("separator", ", ");
			}
		}
		return receiptList;
	}
	protected List<Map<String, Object>> convertDisplayReference(List<Map<String, Object>> aprvLineCcList) {
		List<Map<String, Object>> referenceList = Lists.newArrayList();
		for(int i = 0; i < aprvLineCcList.size(); i++) {
			Map<String, Object> aprvLineCcInfo = aprvLineCcList.get(i);
			if("REF".equals(aprvLineCcInfo.get("rdg_typ_ccd"))) {
				referenceList.add(aprvLineCcInfo);
			}
		}
		for(int i = 0; i < referenceList.size(); i++) {
			Map<String, Object> referenceInfo = referenceList.get(i);
			
			if(i != (referenceList.size() - 1)) {
				referenceInfo.put("separator", ", ");
			}
		}
		return referenceList;
	}
	
	public String printApproval(Map param) {
		Map approvalInfo = this.findApprovalInfo(param);
		
		Map approvalMaster = (Map) approvalInfo.get("approvalMaster");
		approvalMaster.put("apvl_typ_ccd_nm", sharedService.findCodeName(approvalMaster.get("apvl_typ_ccd"), "G003"));
		approvalMaster.put("apvl_sts_ccd_nm", sharedService.findCodeName(approvalMaster.get("apvl_sts_ccd"), "G002"));
		
		Map lineParam = Maps.newHashMap();
		lineParam.put("aprvLineList", approvalInfo.get("approvalLines"));
		lineParam.put("aprvLineCcList", approvalInfo.get("approvalCcLines"));
		
		Map displayApprover = this.convertDisplayApprover(lineParam);
		
		Map approvalParam = Maps.newHashMap();
		approvalParam.put("header", approvalInfo.get("approvalMaster"));
		approvalParam.put("document", approvalInfo.get("approvalDoc"));
		approvalParam.put("drafter", displayApprover.get("drafter"));
		approvalParam.put("approver", displayApprover.get("approver"));
		approvalParam.put("lastApprover", displayApprover.get("lastApprover"));
		approvalParam.put("agreementList", displayApprover.get("agreementList"));
		approvalParam.put("receiptList", displayApprover.get("receiptList"));
		approvalParam.put("referenceList", displayApprover.get("referenceList"));
		
		String content = approvalDocumentService.findPrintApprovalDocumentTemplate(approvalParam);
		return content;
	}
}