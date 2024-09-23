package smartsuite.app.bp.approval.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.approval.line.repository.ApprovalLineCcRepository;
import smartsuite.app.bp.approval.line.repository.ApprovalLineRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class ApprovalLineCcService {
	
	@Inject
	ApprovalLineCcRepository approvalLineCcRepository;
	
	public void deleteApprovalLineCcByApprovalUuid(Map<String, Object> param) {
		approvalLineCcRepository.deleteApprovalLineCcByApprovalUuid(param);
	}
	
	public void insertListApprovalLineCc(List<Map<String, Object>> aprvLineCcList, String approvalId) {
		for (Map<String, Object> row : aprvLineCcList) {
			row.put("apvl_uuid", approvalId); // 결재 아이디
			row.put("rdg_uuid", UUID.randomUUID().toString()); // 열람 아이디
			this.insertApprovalLineCc(row);
		}
	}
	
	private void insertApprovalLineCc(Map<String, Object> row) {
		approvalLineCcRepository.insertApprovalLineCc(row);
	}
	
	public void updateLineApprovalLineCc(List<Map<String, Object>> updateAprvLineCcList) {
		for(Map<String, Object> row : updateAprvLineCcList) {
			this.updateLineApprovalCc(row);
		}
	}
	
	private void updateLineApprovalCc(Map<String, Object> row) {
		approvalLineCcRepository.updateApprovalLineCc(row);
	}
	
	public void removeListApprovalLineCc(List<Map<String, Object>> aprvLineCcList) {
		for(Map<String, Object> row : aprvLineCcList) {
			this.removeApprovalLineCc(row);
		}
	}
	
	private void removeApprovalLineCc(Map<String, Object> row) {
		approvalLineCcRepository.removeApprovalLineCc(row);
	}
	
	public List<Map<String, Object>> findListApprovalLineCc(Map<String, Object> param) {
		return approvalLineCcRepository.findListApprovalLineCc(param);
	}
	
	public List<Map<String, Object>> findListApprovalLineCcForReSubmit(Map<String, Object> param) {
		return approvalLineCcRepository.findListApprovalLineCcForReSubmit(param);
	}
}
