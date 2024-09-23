package smartsuite.app.bp.approval.link.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.approval.link.repository.ApprovalLinkRepository;

import javax.inject.Inject;
import java.util.Map;


@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
public class ApprovalLinkService {

	@Inject
	ApprovalLinkRepository approvalLinkRepository;

	/**
	 * 업무 결재 연동 사용안함으로 수정
	 * @param param
	 */
	public void updateApprovalLinkNotUsed(Map<String, Object> param) {
		approvalLinkRepository.updateApprovalLinkNotUsed(param);
	}


	/**
	 * 업무 결재 연동을 삭제한다. - 물리적 삭제
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteApprovalLink
	 */
	public void deleteApprovalLink(Map<String, Object> param) {
		approvalLinkRepository.deleteApprovalLink(param);
	}

	/**
	 * 사용중인 결재 정보 조회
	 * @param param
	 * @return
	 */
	public String findTaskApprovalInfoUsingForApprovalUuid(Map<String, Object> param) {
		return approvalLinkRepository.findTaskApprovalInfoUsingForApprovalUuid(param);
	}

	/**
	 * 업무 결재 연동을 등록한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertTaskAndApprovalLink
	 */
	public void insertTaskAndApprovalLink(Map<String, Object> param) {
		approvalLinkRepository.insertTaskAndApprovalLink(param);
	}
}