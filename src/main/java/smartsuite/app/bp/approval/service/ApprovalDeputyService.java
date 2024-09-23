package smartsuite.app.bp.approval.service;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import smartsuite.app.bp.approval.repository.ApprovalRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.message.MessageUtil;
import smartsuite.data.FloaterStream;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ApprovalDeputyService {

	@Inject
	ApprovalRepository approvalRepository;

	@Inject
	MessageUtil messageUtil;

	/**
	 * 대리결재 목록조회
	 *
	 * @param param
	 * @return the list
	 * @Method Name : findListDeputyApproval
	 */
	public FloaterStream findListDeputyApproval(Map<String, Object> param){
		return approvalRepository.findListDeputyApproval(param);
	}

	/**
	 * 대리결재 목록저장
	 *
	 * @param param
	 * @return the map
	 * @Method Name : saveDeputyApproval
	 */
	public ResultMap saveDeputyApproval(Map<String, Object> param){
		ResultMap resultMap = ResultMap.getInstance();
		List<Map<String, Object>> deputyApprovalNewItemList = (List<Map<String, Object>>)param.get("newItems");
		List<Map<String, Object>> deputyApprovalUpdateItemList = (List<Map<String, Object>>)param.get("updateItems");

		for(Map<String, Object> deputyApprovalInfo : deputyApprovalNewItemList) {
			if(this.getDeputyApprovalInfo(deputyApprovalInfo)) {
				String resultMsg = messageUtil.getCodeMessage("STD.APR2011", deputyApprovalInfo.get("apvr_id") + "(" + deputyApprovalInfo.get("aprv_usr_nm") + ")", null, LocaleContextHolder.getLocale()); //{0}의 부재일이 이미 등록된 부재일과 중복됩니다. 결재자의 부재기간을 확인하세요.
				resultMap.setResultMessage(resultMsg);
				return ResultMap.DUPLICATED(resultMap.getResultMessage());
			} else {
				deputyApprovalInfo.put("dpty_apvl_uuid", UUID.randomUUID().toString());
				this.insertDeputyApprovalInfo(deputyApprovalInfo); // 대리결재 정보 저장
			}
		}

		for(Map<String, Object> deputyApprovalInfo : deputyApprovalUpdateItemList) {
			if(this.getDeputyApprovalInfo(deputyApprovalInfo)) {
				String resultMsg = messageUtil.getCodeMessage("STD.APR2011", deputyApprovalInfo.get("apvr_id") + "(" + deputyApprovalInfo.get("aprv_usr_nm") + ")", null, LocaleContextHolder.getLocale()); //{0}의 부재일이 이미 등록된 부재일과 중복됩니다. 결재자의 부재기간을 확인하세요.
				resultMap.setResultMessage(resultMsg);
				return ResultMap.DUPLICATED(resultMap.getResultMessage());
			} else {
				this.updateDeputyApprovalInfo(deputyApprovalInfo); // 대리결재 정보 저장
			}
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 대리 결재 정보 추가
	 * @param deputyApprovalInfo
	 */
	public void insertDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		approvalRepository.insertDeputyApprovalInfo(deputyApprovalInfo);
	}

	/**
	 * 대리 결재 정보 저장
	 * @param deputyApprovalInfo
	 */
	public void updateDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		approvalRepository.updateDeputyApprovalInfo(deputyApprovalInfo);
	}

	/**
	 * 대리 결재 정보 개수 조회(결재자의 날짜 기준)
	 * @param deputyApprovalInfo
	 * @return
	 */
	public boolean getDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		return (approvalRepository.getCountDeputyApprovalInfoEither(deputyApprovalInfo)
				+ approvalRepository.getCountDeputyApprovalInfoBoth(deputyApprovalInfo) > 0);
	}

	/**
	 *대리결재 목록삭제
	 *
	 * @param param
	 * @return the map
	 * @Method Name : saveDeputyApproval
	 */
	public ResultMap deleteDeputyApprovalInfoProcess(Map<String, Object> param){
		List<Map<String, Object>> deleteDeputyApprovalList = (List<Map<String, Object>>)param.get("removeItems");
		this.deleteListDeputyApprovalInfo(deleteDeputyApprovalList);
		return ResultMap.SUCCESS();
	}

	/**
	 * 대리 결재 정보 리스트 삭제
	 * @param deleteDeputyApprovalList
	 */
	public void deleteListDeputyApprovalInfo(List<Map<String, Object>> deleteDeputyApprovalList) {
		for(Map<String, Object> deputyApprovalInfo : deleteDeputyApprovalList) {
			this.deleteDeputyApprovalInfo(deputyApprovalInfo);
		}
	}

	/**
	 * 대리결재 정보 삭제
	 * @param deputyApprovalInfo
	 */
	public void deleteDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		approvalRepository.deleteDeputyApprovalInfo(deputyApprovalInfo);
	}


	/**
	 * 대리결재건수 목록조회
	 *
	 * @param param
	 * @return the list
	 * @Method Name : findListDeputyApprovalCount
	 */
	public FloaterStream findListDeputyApprovalCount(Map<String, Object> param){
		return approvalRepository.findListDeputyApprovalCount(param);
	}
}


