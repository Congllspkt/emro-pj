package smartsuite.app.common.csr.service;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.csr.repository.CSRRepository;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CSR 관련 처리하는 서비스 Class입니다.
 *
 * @author WanSeob Kim
 * @Since 2020. 2. 25
 * @FileName CSRService.java
 * @package smartsuite.app.common.csr
 */
@Service
@Transactional
public class CSRService {

	@Inject
	private SharedService sharedService;

	@Inject
	public CSRRepository csrRepository;

	/**
	 * CSRProcess Enum.
	 */
	private enum CSRProcessStatusBean {

		// STEP 1. CSR 요청
		TEMPSAVE(Arrays.asList("CRNG"), "CRNG"),        // CSR 임시저장  [ 신규 , 임시저장(10) -> 임시저장(10) ]
		REQUEST(Arrays.asList("CRNG"), "RCPT_WTG"),         // CSR 요청       [ 신규 , 임시저장(10) -> 접수대기(20) ]
		DELETE(Arrays.asList("CRNG"), null),          // CSR 삭제       [ 임시저장(10) -> 삭제 ]
		RECALL(Arrays.asList("RCPT_WTG"), "CRNG"),          // CSR 회수       [ 접수대기(20) -> 임시저장(10) ]
		// STEP 2. 처리 담당자 접수 및 처리
		RECEIPT(Arrays.asList("RCPT_WTG", "RPRCSG_REQ"), "RCPT"),   // CSR 접수        [ 접수대기(20), 재처리요청(55) -> 접수(30) ]
		START(Arrays.asList("RCPT"), "PRCSG_WTG"),           // CSR 처리시작   [ 접수(30) -> 처리중(40) ]
		STOP(Arrays.asList("PRCSG_WTG"), "RCPT"),            // CSR 처리중지   [ 처리중(40) -> 접수(30) ]
		RESOLVE(Arrays.asList("RCPT", "PRCSG_WTG"), "PRCSG_CMPLD"),   // CSR 처리완료   [ 접수(30), 처리중(40) -> 처리완료(50) ]
		REOPEN(Arrays.asList("PRCSG_CMPLD"), "RCPT"),          // CSR 처리회수   [ 처리완료(50) -> 접수(30) ]
		// STEP 3. 요청자 확인
		CLOSE(Arrays.asList("PRCSG_CMPLD", "RPRCSG_REQ"), "CONFM_CMPLD"),     // CSR 확인완료   [ 처리완료(50), 재처리요청(55) -> 확인완료(60) ]
		REPROCESS(Arrays.asList("PRCSG_CMPLD", "CONFM_CMPLD"), "RPRCSG_REQ"); // CSR 재처리요청 [ 처리완료(50), 확인완료(60) -> 재처리요청(55) ]

		private final List<String> validationStatus; // 해당 프로세스를 진행하기 위한 진행상태

		private final String updateStatus; // 해당 프로세스완료 후 update 할 진행상태

		/**
		 * {@code CSRProcess} 생성자
		 *
		 * @param validationStatus - validation 대상이 되는 상태 코드
		 * @param updateStatus - 업데이트 될 상태코드
		 */
		CSRProcessStatusBean(List<String> validationStatus, String updateStatus) {
			this.validationStatus = validationStatus;
			this.updateStatus = updateStatus;
		}

		public List<String> getValidationStatus() {
			return validationStatus;
		}

		public String getUpdateStatus() {
			return updateStatus;
		}
	}

	/**
	 * [CSR 등록 및 현황] 목록 조회.
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	public List<Map<String, Object>> findListCSRInfo(Map<String, Object> param) {
		return csrRepository.findListCSRInfo(param);
	}

	/**
	 * [CSR 접수 및 조치결과] 목록 조회.
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	public List<Map<String, Object>> findListCSRReceipt(Map<String, Object> param) {
		return csrRepository.findListCSRReceipt(param);
	}

	/**
	 * CSR 정보 조회 ( 요청내용 + 처리내용 ).
	 *
	 * @param param 조회 조건
	 * @return 조회 결과
	 */
	public Map<String, Object> findCSRInfo(Map<String, Object> param) {
		Map<String, Object> csrData = Maps.newHashMap();

		String csrId = param.getOrDefault("csr_uuid","") == null? "" : (String) param.getOrDefault("csr_uuid","");
		// CSR 요청정보 조회
		Map<String, Object> csrInfo = selectCSRInfo(csrId);
		csrData.put("csrInfo", csrInfo);
		// CSR 처리정보 조회
		List<Map<String, Object>> processingList = findListCSRProcessingInfo(csrId);
		csrData.put("processingList", processingList);
		return csrData;
	}

	/**
	 * [CSR 등록 및 현황] CSR 임시저장.
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	public String templateSaveCSR(Map<String, Object> param) {
		String csrId = param.getOrDefault("csr_uuid","") == null? "" : (String) param.getOrDefault("csr_uuid","");
		// [step1] CSR 요청 신규 생성 or 수정
		if (StringUtils.isEmpty(csrId)) {
			// CSR 정보 신규 생성
			csrId = insertCSRInfo(param);
		} else {
			// 진행상태 유효성 검사
			validateCSRProgressStatus(param, CSRProcessStatusBean.TEMPSAVE);
			// CSR 정보 수정
			updateCSRInfo(param);
		}
		// [step2] CSR 요청 진행상태 및 처리일시 update ( "CRNG" : 임시저장)
		updateCSRStatus(csrId, CSRProcessStatusBean.TEMPSAVE);
		return csrId;
	}

	/**
	 * [CSR 등록 및 현황] CSR 요청 ( 임시저장 -> 접수대기 ).
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	public String requestCSR(Map<String, Object> param) {
		String csrId = (String)param.get("csr_uuid");

		// [step1] CSR 요청 신규 생성 or 수정
		if (StringUtils.isEmpty(csrId)) {
			csrId = insertCSRInfo(param);
		} else {
			// 진행상태 유효성 검사
			validateCSRProgressStatus(param, CSRProcessStatusBean.REQUEST);
			// CSR 정보 수정
			updateCSRInfo(param);
		}
		// [step2] CSR 요청 진행상태 및 처리일시 update ( "RCPT_WTG" : 접수대기 )
		updateCSRStatus(csrId, CSRProcessStatusBean.REQUEST);
		return csrId;
	}

	/**
	 * [CSR 등록 및 현황] CSR 삭제 ( 임시저장 -> 삭제 ).
	 *
	 * @param param CSR 정보
	 */
	public void deleteCSRInfoRequest(Map<String, Object> param) {
		String csrId = param.getOrDefault("csr_uuid","") == null? "" : (String) param.getOrDefault("csr_uuid","");

		// [step1] 진행상태 유효성 검사
		validateCSRProgressStatus(param, CSRProcessStatusBean.DELETE);

		// [step2] CSR 정보 삭제
		deleteCSRInfo(csrId);
	}

	/**
	 * [CSR 등록 및 현황] CSR 회수 ( 요청 -> 임시저장 ).
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	public String reCollectCSR(Map<String, Object> param) {
		String csrId = param.getOrDefault("csr_uuid","") == null? "" : (String) param.getOrDefault("csr_uuid","");
		// [step1] 진행상태 유효성 검사
		validateCSRProgressStatus(param, CSRProcessStatusBean.RECALL);

		// [step2] CSR 요청 진행상태 및 처리일시 update ( "CRNG" : 임시저장)
		updateCSRStatus(csrId, CSRProcessStatusBean.RECALL);
		return csrId;
	}

	/**
	 * CSR 처리 정보 제출.
	 * 
	 * [처리담당자] 접수, 처리시작, 처리중지, 처리완료, 처리회수, 재처리시작, 재처리완료
	 * [요청자] 확인완료, 재처리요청
	 *
	 * @param param CSR 정보 및 처리 내역 정보
	 * @return CSR 아이디
	 */
	public String processHistoryRequest(Map<String, Object> param) {
		String processType = param.getOrDefault("process_type","") == null? "" : (String) param.getOrDefault("process_type","");

		// 요청자의 제출이 아닐 경우, 처리 담당자 업데이트
		String[] requestType = { CSRProcessStatusBean.CLOSE.name(), CSRProcessStatusBean.REPROCESS.name() };
		if (!Arrays.asList(requestType).contains(processType)) {
			updateCSRPersonInCharge(param);
		}

		return submitCSR(param, CSRProcessStatusBean.valueOf(processType));
	}

	/**
	 * CSR 처리 내역 수정.
	 *
	 * @param param CSR 처리내역 정보
	 */
	public void processHistoryContentModify(Map<String, Object> param) {
		updateProcessHistoryRecordInfo(param);
	}

	/**
	 * CSR 처리 정보 제출.
	 *
	 * @param param CSR 정보
	 * @param csrProcessStatusBean CSRProcess enum
	 */
	private String submitCSR(Map<String, Object> param, CSRProcessStatusBean csrProcessStatusBean) {
		String csrId = (String)param.get("csr_uuid");
		// [step1] 진행상태 유효성 검사
		validateCSRProgressStatus(param, csrProcessStatusBean);
		// [step2] CSR 요청 진행상태 및 처리일시 update
		updateCSRStatus(csrId, csrProcessStatusBean);
		// [step3] CSR 처리 이력 insert
		insertProcessHistoryRecordInfo(param, csrProcessStatusBean);
		return csrId;
	}

	/**
	 * CSR 진행상태 및 처리일시 업데이트.
	 *
	 * @param csrId CSR 아이디
	 * @param csrProcessStatusBean CSRProcess enum
	 */
	private void updateCSRStatus(String csrId, CSRProcessStatusBean csrProcessStatusBean) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("csr_uuid", csrId);
		param.put("csr_sts_ccd", csrProcessStatusBean.updateStatus);
		
		csrRepository.updateCSRStatus(param);
	}

	/**
	 * CSR 진행상태 유효성검사. 
	 * 1. 클라이언트에서의 parameter 진행상태가 DB에서의 진행상태와 일치 하는 가 (진행상태 위변조 체크) 
	 * 2. 매개변수 진행상태가 DB에서의 진행상태와 일치 하는 가 (비지니스 유효성 체크)
	 *
	 * @param param CSR 정보
	 * @param csrProcessStatusBean CSRProcess enum
	 */
	private void validateCSRProgressStatus(Map<String, Object> param, CSRProcessStatusBean csrProcessStatusBean) {
		String clientSts = (String)param.get("csr_sts_ccd");
		String serverSts = csrRepository.getCSRProgressStatus(param);

		if (!clientSts.equals(serverSts)) {
			throw new CommonException(ErrorCode.INVALID_STATUS);
		}

		if (!csrProcessStatusBean.validationStatus.contains(serverSts)) {
			throw new CommonException(ErrorCode.INVALID_STATUS);
		}
	}

	/**
	 * CSR 요청내용 insert.
	 *
	 * @param param CSR 정보
	 * @return CSR 아이디
	 */
	private String insertCSRInfo(Map<String, Object> param) {
		String csrId = UUID.randomUUID().toString();
		param.put("csr_uuid", csrId);
		param.put("csr_no", sharedService.generateDocumentNumber("CSR"));

 		csrRepository.insertCSRInfo(param);
		return csrId;
	}

	/**
	 * CSR 요청내용 update.
	 *
	 * @param param CSR 정보
	 */
	private void updateCSRInfo(Map<String, Object> param) {
		csrRepository.updateCSRInfo(param);
	}

	/**
	 * CSR 요청내용 select.
	 *
	 * @param csrId CSR 아이디
	 * @return CSR 정보
	 */
	private Map<String, Object> selectCSRInfo(String csrId) {
		return csrRepository.selectCSRInfo(csrId);
	}

	/**
	 * CSR 요청내용 delete.
	 *
	 * @param csrId CSR 아이디
	 */
	public void deleteCSRInfo(String csrId) {
		csrRepository.deleteCSRInfo(csrId);
	}

	/**
	 * CSR 처리이력 insert.
	 *
	 * @param param 처리이력 정보
	 * @param csrProcessStatusBean CSRProcess enum
	 */
	private void insertProcessHistoryRecordInfo(Map<String, Object> param, CSRProcessStatusBean csrProcessStatusBean) {
		String csrHistoryRecordLogId = UUID.randomUUID().toString();
		param.put("csr_prcsg_histrec_uuid", csrHistoryRecordLogId);
		// 이전 진행상태
		param.put("csr_pre_sts_ccd", param.getOrDefault("csr_sts_ccd",""));
		// 신규 진행상태
		param.put("csr_sts_ccd", csrProcessStatusBean.getUpdateStatus());
		
		csrRepository.insertProcessHistoryRecordInfo(param);
	}

	/**
	 * CSR 처리이력 update.
	 *
	 * @param param 처리이력 정보
	 */
	private void updateProcessHistoryRecordInfo(Map<String, Object> param) {
		csrRepository.updateProcessHistoryRecordInfo(param);
	}

	/**
	 * CSR 담당자 update.
	 *
	 * @param param CSR 처리 정보
	 */
	private void updateCSRPersonInCharge(Map<String, Object> param) {
		csrRepository.updateCSRPersonInCharge(param);
	}

	/**
	 * CSR 처리내역 목록 조회.
	 *
	 * @param csrId CSR 아이디
	 * @return 조회 결과
	 */
	private List<Map<String, Object>> findListCSRProcessingInfo(String csrId) {
		return csrRepository.findListCSRProcessingInfo(csrId);
	}

}
