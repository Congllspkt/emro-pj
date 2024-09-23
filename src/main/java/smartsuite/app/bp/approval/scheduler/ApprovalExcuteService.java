package smartsuite.app.bp.approval.scheduler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import smartsuite.app.bp.approval.service.ApprovalConst;
import smartsuite.app.bp.approval.service.ApprovalService;

@Service
public class ApprovalExcuteService {

	@Inject
	ApprovalService approvalService;
	
	/**
	 * 결재선 결과 저장 스케쥴러 수행 service
	 * <pre>
	 * transaction 분리를 위해 이 service에는 transaction을 주지 않음
	 * 
	 * (1) approvalService.saveApprovalLineResult
	 *     : 실제 로직 수행 service (Exception 발생 시 rollback)
	 *     
	 * (2) approvalService.updateApprovalErrorCodeAndMessage
	 *     : 결재선 결과 처리 오류상태 업데이트
	 *      (1)의 service에서 Exception이 발생하여도 별도의 트랜잭션으로 수행하여
	 *      에러로그를 기록한다.
	 * </pre>
	 * @param param {apvl_uuid, apvlln_uuid, usr_id, apvlln_apvl_res_ccd, apvl_opn}
	 * @see ApprovalSchedulerService
	 * @see ApprovalService
	 * @see ApprovalConst
	 */
	public void saveApprovalLineResult(HashMap<String, Object> param) {
		Map<String, Object> updateParam = Maps.newHashMap();
		updateParam.put("apvl_uuid", param.get("apvl_uuid"));
		
		try {
			approvalService.saveApprovalLineResult(param);
			
			updateParam.put("apvl_res_ccd", ApprovalConst.APPROVAL_RESULT_PROCESS_COMPLETED);	// 결재 결과 처리 완료(오류없음)
		} catch(Exception e) {
			updateParam.put("apvl_res_ccd", ApprovalConst.APPROVAL_RESULT_PROCESS_ERROR);		// 결재 결과 처리 오류
			updateParam.put("apvl_err_cont", e.getMessage());
		} finally {
			approvalService.updateApprovalErrorCodeAndMessage(updateParam);		// 결재 결과 처리 오류상태 업데이트
		}
	}
}