package smartsuite.app.bp.approval;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.approval.service.ApprovalDeputyService;
import smartsuite.app.bp.approval.service.ApprovalManagerService;
import smartsuite.app.bp.approval.service.ApprovalService;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.data.FloaterStream;

/**
 * 결재 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @since 2016. 2. 2
 * @FileName ApprovalController.java
 * @package smartsuite.app.bp.approval
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/approval/")
public class ApprovalController {

	/** The approval service. */
	@Inject
	ApprovalService approvalService;

	@Inject
	ApprovalManagerService approvalManagerService;

	@Inject
	ApprovalDeputyService approvalDeputyService;

	/**
	 * 일괄결재
	 */
	@RequestMapping (value = "batchApproveProcess.do")
	public @ResponseBody ResultMap batchApproveProcess(@RequestBody Map param){
		return approvalService.batchApproveProcess(param);
	}
	
	/**
	 * 대리결재 목록조회
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findListDeputyApproval.do")
	public @ResponseBody FloaterStream findListDeputyApproval(@RequestBody Map param) {
		return approvalDeputyService.findListDeputyApproval(param);
	}
	
	/**
	 * 대리결재 목록 저장
	 */
	@RequestMapping (value = "saveDeputyApproval.do")
	public @ResponseBody ResultMap saveDeputyApproval(@RequestBody Map param){
		return approvalDeputyService.saveDeputyApproval(param);
	}
	
	/**
	 * 대리결재 목록삭제
	 */
	@RequestMapping (value = "deleteDeputyApprovalInfo.do")
	public @ResponseBody ResultMap deleteDeputyApprovalInfo(@RequestBody Map param){
		return approvalDeputyService.deleteDeputyApprovalInfoProcess(param);
	}
	/**
	 * 대리결재건수 목록조회
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findListDeputyApprovalCount.do")
	public @ResponseBody FloaterStream findListDeputyApprovalCount(@RequestBody Map param) {
		return approvalDeputyService.findListDeputyApprovalCount(param);
	}

	/**
	 * 결재 상신 목록 조회를 요청한다.
	 *
	 */
	@RequestMapping (value = "findListApprovalSubmit.do")
	public @ResponseBody FloaterStream findListApprovalSubmit(@RequestBody Map param) {
		// 대용량 처리
		return approvalManagerService.findListApprovalMasterProcessForSubmitAndApproval(param, "submit");
	}

	/**
	 * 결재 목록 조회를 요청한다.
	 *
	 */
	@RequestMapping (value = "findListApproval.do")
	public @ResponseBody FloaterStream findListApproval(@RequestBody Map param) {
		// 대용량 처리
		return approvalManagerService.findListApprovalMasterProcessForSubmitAndApproval(param, "receipt");
	}
	
	/**
	 * 결재 수신/참조 조회를 요청한다.
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findListApprovalReceipt.do")
	public @ResponseBody FloaterStream findListApprovalReceipt(@RequestBody Map param) {
		// 대용량 처리
		return approvalManagerService.findListApprovalReceipt(param);
	}

	/**
	 * 결재 승인 오류내용 조회
	 *
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findApprovedErrorContext.do")
	public @ResponseBody Map findApprovedErrorContext(@RequestBody Map param) {
		return approvalManagerService.findApprovedErrorContext(param);
	}
}