package smartsuite.app.bp.admin.organizationManager.operationUnit.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.bp.admin.organizationManager.operationUnit.repository.OperationUnitManagerRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * 운영단위 관련 처리하는 서비스 Class입니다.

 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class OperationUnitManagerService {

	@Inject
	private OperationOrganizationService operationOrganizationService;

	@Inject
	OperationUnitManagerRepository operationUnitManagerRepository;

	/**
	 * 운영단위를 등록한다.
	 * @param param
	 */
	public void insertOperationUnit(Map<String, Object> param) {
		operationUnitManagerRepository.insertOperationUnit(param);
	}

	/**
	 * 운영단위 삭제
	 * @param param
	 */
	public void deleteOperationUnit(Map<String, Object> param) {
		operationUnitManagerRepository.deleteOperationUnit(param);
	}

	/**
	 * 운영단위 수정
	 * @param param
	 */
	public void updateOperationUnit(Map<String, Object> param) {
		operationUnitManagerRepository.updateOperationUnit(param);
	}

	/**
	 * 운영단위 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationUnit(Map<String, Object> param) {
		return operationUnitManagerRepository.findListOperationUnit(param);
	}

	/**
	 * 코드 표기를 위한 운영단위 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationUnitForCode(Map<String, Object> param) {
		return operationUnitManagerRepository.findListOperationUnitForCode(param);
	}


	/**
	 * 운영단위 존재여부 체크 ( 조회조건 운영단위코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationUnitByOperationUnitCode(Map<String, Object> param) {
		return (operationUnitManagerRepository.getCountOperationUnitByOperationUnitCode(param) > 0);
	}

	/**
	 * 운영단위 목록을 등록 또는 수정
	 * @param param
	 * @return
	 */
	public ResultMap saveListOperationUnit(Map<String, Object> param) {
		List<Map<String, Object>> insertOperationUnitList = (List<Map<String, Object>>)param.getOrDefault("insertOperUnits", Lists.newArrayList());
		List<Map<String, Object>> updateOperationUnitList = (List<Map<String, Object>>)param.getOrDefault("updateOperUnits", Lists.newArrayList());

		//운영단위 리스트 추가
		this.insertListOperationUnit(insertOperationUnitList);

		//운영단위 리스트 수정
		this.updateListOperationUnit(updateOperationUnitList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 운영단위 리스트 수정
	 * @param updateOperationUnitList
	 */
	public void updateListOperationUnit(List<Map<String, Object>> updateOperationUnitList) {
		for (Map<String, Object> row : updateOperationUnitList) {
			this.updateOperationUnit(row);
		}
	}

	/**
	 * 운영단위 리스트 추가
	 * @param insertOperationUnitList
	 */
	public void insertListOperationUnit(List<Map<String, Object>> insertOperationUnitList) {
		for (Map<String, Object> row : insertOperationUnitList) {
			if (this.existOperationUnitByOperationUnitCode(row)) { // 운영단위코드 중복 체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}else{
				this.insertOperationUnit(row);
			}
		}
	}

	/**
	 * 운영 단위 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListOperationUnitRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteOperationUnitList = (List<Map<String, Object>>)param.getOrDefault("deleteOperUnits",Lists.newArrayList());

		// 운영단위 리스트 삭제
		this.deleteListOperationUnit(deleteOperationUnitList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 운영 단위 목록 삭제
	 * @param deleteOperationUnitList
	 */
	public void deleteListOperationUnit(List<Map<String, Object>> deleteOperationUnitList) {
		for (Map<String, Object> row : deleteOperationUnitList) {
			if (operationOrganizationService.existOperationOrganizationByOperationUnitCode(row)) { // 운영조직에서 사용중인 운영단위인지 검사
				throw new CommonException(ErrorCode.USED);
			}else{
				this.deleteOperationUnit(row);
			}
		}
	}

}
