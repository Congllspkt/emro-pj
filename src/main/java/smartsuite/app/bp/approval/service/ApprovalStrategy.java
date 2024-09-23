package smartsuite.app.bp.approval.service;

import java.util.Map;

public interface ApprovalStrategy {

	/**
	 * 결재 승인
	 *
	 * @param approvalType
	 * @param appId
	 */
	void doApprove(String approvalType, String appId);

	/**
	 * 결재 반려
	 *
	 * @param approvalType
	 * @param appId
	 */
	void doReject(String approvalType, String appId);

	/**
	 * 결재 취소
	 *
	 * @param approvalType
	 * @param appId
	 */
	void doCancel(String approvalType, String appId);

	/**
	 * 결재 상신
	 *
	 * @param approvalType
	 * @param appId
	 */
	void doSubmit(String approvalType, String appId);

	/**
	 * 결재 임시 저장
	 *
	 * @param approvalType
	 * @param appId
	 */
	void doTemporary(String approvalType, String appId);
	
	/**
	 * 결재서식에서 사용하는 param을 만들기위한 함수
	 *
	 * @author : LMS
	 * @param approvalType, appId
	 * @return Map<String, Object>
	 * @Date : 2017. 05. 23
	 * @Method Name : makeParam
	 */
	Map<String, Object> makeParam(String approvalType, String appId);

}
