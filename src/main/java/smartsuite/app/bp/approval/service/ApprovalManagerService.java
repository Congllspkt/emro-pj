package smartsuite.app.bp.approval.service;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.bp.approval.repository.ApprovalRepository;
import smartsuite.data.FloaterStream;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalManagerService {

	@Inject
	ApprovalRepository approvalRepository;

	/**
	 * 결재 목록을 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list approval
	 * @Date : 2016. 2. 2
	 * @Method Name : findListApproval
	 */
	public FloaterStream findListApprovalMasterProcessForSubmitAndApproval(Map<String, Object> param, String selectType) {
		if(("submit").equals(selectType)){
			param.put("list_type", "submit"); // 상신(submit) 목록
		}else if(("receipt").equals(selectType)){
			param.put("list_type", "receipt"); // 결재(receipt) 목록
		}
		return this.findListApprovalMasterForSubmitAndApproval(param);
	}

	/**
	 * 결재 마스터 리스트 조회 ( 결재 수신 / 송신 리스트 조회 용 )
	 * @param param
	 * @return
	 */
	public FloaterStream findListApprovalMasterForSubmitAndApproval(Map<String, Object> param) {
		// 결재 목록의 경우 담당자의 대리 결재 ID 미리 조회하여 결재 목록을 검색한다.
		if("receipt".equals(param.get("list_type"))) {
			List<String> apvrIds = approvalRepository.findDptyApproverIds(Maps.newHashMap());
			param.put("apvr_ids", apvrIds);
		}
		return approvalRepository.findListApprovalMasterForSubmitAndApproval(param);
	}

	/**
	 * 결개 승인 오류내용 조회.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2021. 3. 23
	 * @Method Name : findApprovedErrorContext
	 */
	public Map<String, Object> findApprovedErrorContext(Map<String, Object> param) {
		return approvalRepository.findApprovedErrorContext(param);
	}
	
	/**
	 * 결재 수신/참조 조회를 요청한다.
	 *
	 * @param param
	 * @return
	 */
	public FloaterStream findListApprovalReceipt(Map param) {
		return approvalRepository.findListApprovalReceipt(param);
	}
}
