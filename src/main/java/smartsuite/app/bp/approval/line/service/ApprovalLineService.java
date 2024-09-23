package smartsuite.app.bp.approval.line.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.approval.line.repository.ApprovalLineRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.security.authentication.Auth;

import javax.inject.Inject;
import java.util.*;

@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
public class ApprovalLineService {

	@Inject
	ApprovalLineRepository approvalLineRepository;

	/**
	 * 결재선을 등록한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertApprovalLine
	 */
	public void insertApprovalLine(Map<String, Object> param) {
		approvalLineRepository.insertApprovalLine(param);
	}


	/**
	 * 결재선을 수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : updateApprovalLine
	 */
	public void updateApprovalLine(Map<String, Object> param) {
		approvalLineRepository.updateApprovalLine(param);
	}

	/**
	 * 결재선 현재 결재선를 수정한다.
	 *
	 * @Method Name : updateApprovalLineCurrent
	 */
	public void updateApprovalLineCurrent(Map<String, Object> param) {
		//현재 결재선로 변경
		this.updateApprovalLineCurrentY(param);
		
		//현재 결재선 제외 모두 결재선가 아니도록 변경
		this.updateApprovalLineCurrentN(param);
	}

	/**
	 * 현재 결재선 제외 모두 결재선가 아니도록 변경
	 * @param param
	 */
	private void updateApprovalLineCurrentN(Map<String, Object> param) {
		approvalLineRepository.updateApprovalLineCurrentN(param);
	}

	/**
	 * 현재 결재선으로 변경
	 * @param param
	 */
	private void updateApprovalLineCurrentY(Map<String, Object> param) {
		approvalLineRepository.updateApprovalLineCurrentY(param);
	}

	/**
	 * 병렬 결재선 처리 프로세스
	 * @param param
	 */
	public void updateApprovalCurrentLineByParallelApprovalLineProcess(Map<String, Object> param) {
		// 현재 결재선 제외 모두 결재선이 아니도록 변경
		this.updateApprovalLineCurrentN(param);
		// 병렬 결재선 현재 결재선으로 수정
		this.updateApprovalCurrentLineByParallelApprovalLine(param);
	}

	/**
	 * 병렬 결재선 현재 결재선로 수정
	 * @param param
	 */
	private void updateApprovalCurrentLineByParallelApprovalLine(Map<String, Object> param) {
		approvalLineRepository.updateApprovalCurrentLineByParallelApprovalLine(param);
	}

	/**
	 * 병렬 결재선 현재 결재선이 아니도록 변경
	 * @param param
	 */
	public void updateApprovalNotCurrentLineByParallelApprovalLine(Map<String, Object> param) {
		approvalLineRepository.updateApprovalNotCurrentLineByParallelApprovalLine(param);
	}

	/**
	 * 결재 결과 정보를 결재선에 업데이트 한다.
	 * @param param
	 */
	public void updateApprovalResultInfoByApprovalLine(Map<String, Object> param) {
		approvalLineRepository.updateApprovalResultInfoByApprovalLine(param);
	}


	/**
	 * 결재 의견을 저장하는 프로세스
	 * @param param
	 * @return
	 */
	public ResultMap saveOpinionApproval(Map<String, Object> param) {
		Map<String, Object> approvalInfo = (Map<String, Object>) param.getOrDefault("approvalInfo",Maps.newHashMap());
		this.updateApprovalOpinionByApprovalLine(approvalInfo);
		return ResultMap.SUCCESS();

	}

	/**
	 * 결재선에 결재의견을 저장 (수신자전용)
	 * @param approvalInfo
	 */
	private void updateApprovalOpinionByApprovalLine(Map<String, Object> approvalInfo) {
		approvalLineRepository.updateApprovalOpinionByApprovalLine(approvalInfo);
	}

	/**
	 * 결재에 대한 결재선을 모두 삭제한다.
	 */
	public void deleteApprovalLineByApprovalUuid(Map<String, Object> param) {
		approvalLineRepository.deleteApprovalLineByApprovalUuid(param);
	}
	

	/**
	 * 결재선을 삭제한다.
	 */
	public void deleteApprovalLine(Map<String, Object> param) {
		approvalLineRepository.deleteApprovalLine(param);
	}

	/**
	 * 결재선 목록을 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list approval
	 * @Date : 2016. 2. 2
	 * @Method Name : findListApprovalLine
	 */
	public List<Map<String, Object>> findListApprovalLineProcess(Map<String, Object> param) {
		List<Map<String, Object>> resultList = Lists.newArrayList();
		List<Map<String, Object>> lineList = this.findListApprovalLine(param);

		String deputyApprovalYn = param.get("dpty_aprv_yn") == null? "" : param.get("dpty_aprv_yn").toString();
		String approvalUserId = param.get("apvr_id") == null? "" : param.get("apvr_id").toString();

		//대리결재를 한 이후에는 대리결재자 정보도 함께 넣어준다
		for(Map<String, Object> lineInfo : lineList) {
			String deputyApprovalId = lineInfo.get("dpty_apvl_uuid") == null? "" : lineInfo.get("dpty_apvl_uuid").toString();
			if(StringUtils.isNotEmpty(deputyApprovalId)) {
				Map<String, Object> deputyApprovalInfo = this.findInfoDeputyApprovalUserInfo(lineInfo);

				if(MapUtils.isNotEmpty(deputyApprovalInfo)) {
					String positionName = deputyApprovalInfo.get("posi_nm") == null ? "" : deputyApprovalInfo.get("posi_nm").toString();
					String deptName = deputyApprovalInfo.get("dept_nm") == null ? "" :"(" + deputyApprovalInfo.get("dept_nm").toString() + ")" ;
					String approvalSubjectName = " " + positionName + deptName;
					lineInfo.put("aprv_nm", lineInfo.get("aprv_nm") + " > " + deputyApprovalInfo.get("usr_nm") + approvalSubjectName);
				}
			}
		}

		if(!StringUtils.isEmpty(deputyApprovalYn) && "Y".equals(deputyApprovalYn)) {
			for(Map<String, Object> lineInfo : lineList) {
				String userId = lineInfo.get("usr_id") == null? "" : lineInfo.get("usr_id").toString();
				if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(approvalUserId) && userId.equals(approvalUserId) && !(approvalUserId.equals(Auth.getCurrentUserName()))) {
					Map<String, Object> userInfo = Auth.getCurrentUser().getUserInfo();
					lineInfo.put("dpty_aprv_yn", deputyApprovalYn);
					lineInfo.put("apvr_id", approvalUserId);
					lineInfo.put("dpty_apvr_id", userInfo.get("usr_id"));
					lineInfo.put("dpty_usr_nm", userInfo.get("disp_usr_nm"));
				}
				resultList.add(lineInfo);
			}
		} else {
			resultList.addAll(lineList);
		}

		return resultList;
	}

	private Map<String, Object> findInfoDeputyApprovalUserInfo(Map<String, Object> lineInfo) {
		return approvalLineRepository.findInfoDeputyApprovalUserInfo(lineInfo);
	}

	private List<Map<String, Object>> findListApprovalLine(Map<String, Object> param) {
		
		List<Map<String,Object>> approvalList = approvalLineRepository.findListApprovalLine(param);
		for(Map<String,Object> lineInfo : approvalList){
			String approvalJobTitleName = lineInfo.get("apvr_jobtit_nm") == null ? "" : lineInfo.get("apvr_jobtit_nm").toString();
			String deptName = lineInfo.get("apvr_dept_nm") == null ? "" :"(" + lineInfo.get("apvr_dept_nm").toString() + ")" ;
			String approvalSubjectName = " " + approvalJobTitleName + deptName;
			lineInfo.put("aprv_nm", lineInfo.get("usr_nm") + approvalSubjectName);
		}

		return approvalList;
	}


	/**
	 * 결재 선 리스트 추가
	 * @param insertLines
	 * @param approvalId
	 */
	public void insertListApprovalLine(List<Map<String, Object>> insertLines,String approvalId) {
		for (Map<String, Object> row : insertLines) {
			row.put("apvl_uuid", approvalId); // 결재 아이디
			row.put("apvlln_uuid", UUID.randomUUID().toString()); // 결재선 아이디
			this.insertApprovalLine(row);
		}
	}

	/**
	 * 결재선 리스트 수정
	 * @param updateLines
	 */
	public void updateListApprovalLine(List<Map<String, Object>> updateLines) {
		for (Map<String, Object> row : updateLines) {
			this.updateApprovalLine(row);
		}
	}

	public void deleteListApprovalLine(List<Map<String, Object>> deleteLines) {
		for (Map<String, Object> row : deleteLines) {
			this.deleteApprovalLine(row);
		}
	}

	/**
	 * 병렬 결재선 결재자 진행 상태
	 * @param lineInfo
	 * @return
	 */
	public int getCountParallelApprovalLineProgressStatus(Map<String, Object> lineInfo) {
		return approvalLineRepository.getCountParallelApprovalLineProgressStatus(lineInfo);
	}

	/**
	 * approval line 조회한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2021. 3. 23
	 * @Method Name : findApprovalLine
	 */
	public Map<String, Object> findApprovalLine(Map<String, Object> param) {
		return approvalLineRepository.findApprovalLine(param);
	}

	/**
	 * 결재선 리스트 조회 ( 결재선 정렬기준 )
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 10. 18
	 * @Method Name : findListApprLineBySortOrd
	 */
	public List<Map<String, Object>> findListApprovalLineByApprovalLineSortOrd(Map<String, Object> param) {
		return approvalLineRepository.findListApprovalLineByApprovalLineSortOrd(param);
	}

	public Map<String, Object> findMyApprovalLine(Map<String, Object> searchParam) {
		return approvalLineRepository.findMyApprovalLine(searchParam);
	}

	public List<Map<String, Object>> findListApprovalLineForReSubmit(Map<String, Object> param) {
		return approvalLineRepository.findListApprovalLineForReSubmit(param);
	}

	public void insertListApprovalLine(List<Map<String, Object>> insertLines) {
		for (Map<String, Object> row : insertLines) {
			row.put("apvlln_uuid", UUID.randomUUID().toString()); // 결재선 아이디
			this.insertApprovalLine(row);
		}
	}
	
	public int findLastApvdApvllnSort(Map param) {
		return approvalLineRepository.findLastApvdApvllnSort(param);
	}
	
	public boolean isCompleteCurrStepApprovalLine(Map<String, Object> param) {
		return approvalLineRepository.isCompleteCurrStepApprovalLine(param);
	}
	
	public List<Map<String, Object>> findListNextApvllnApprover(Map param) {
		return approvalLineRepository.findListNextApvllnApprover(param);
	}
	
	public void updateCurrApvrYn(Map<String, Object> nextApvllnApprover) {
		approvalLineRepository.updateCurrApvrYn(nextApvllnApprover);
	}
	
	public void deleteApprovalLineAfterApvllnSort(Map deleteParam) {
		approvalLineRepository.deleteApprovalLineAfterApvllnSort(deleteParam);
	}
}