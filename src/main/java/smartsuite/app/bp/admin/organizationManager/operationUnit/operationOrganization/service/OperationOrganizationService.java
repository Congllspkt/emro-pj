package smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.repository.OperationOrganizationRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 운영조직/운영조직의 사용자/운영조직의 연결정보 관련 처리하는 서비스 Class입니다.
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class OperationOrganizationService {

	@Inject
	OperationOrganizationRepository operationOrganizationRepository;

	/**
	 * 운영조직 등록
	 * @param param
	 */
	public void insertOperationOrganization(Map<String, Object> param) {
		operationOrganizationRepository.insertOperationOrganization(param);
	}

	/**
	 * 운영조직의 사용자를 등록한다.
	 * @param param
	 */
	public void insertOperationOrganizationUser(Map<String, Object> param) {
		operationOrganizationRepository.insertOperationOrganizationUser(param);
	}

	/**
	 * 운영조직의 연결정보를 등록한다.
	 * @param param
	 */
	public void insertOperationOrganizationLink(Map<String, Object> param) {
		operationOrganizationRepository.insertOperationOrganizationLink(param);
	}

	/**
	 * 운영조직 삭제
	 * @param param
	 */
	public void deleteOperationOrganization(Map<String, Object> param) {
		operationOrganizationRepository.deleteOperationOrganization(param);
	}

	/**
	 * 운영조직 내 사용자 삭제
	 * @param param
	 */
	public void deleteOperationOrganizationUser(Map<String, Object> param) {
		operationOrganizationRepository.deleteOperationOrganizationUser(param);
	}

	/**
	 * 운영조직 삭제 (조회조건 사용자 아이디 )
	 * @param param
	 */
	public void deleteOperationOrganizationByUserId(Map<String, Object> param) {
		operationOrganizationRepository.deleteOperationOrganizationByUserId(param);
	}

	/**
	 * 운영조직 연결정보 삭제
	 * @param param
	 */
	public void deleteOperationOrganizationLink(Map<String, Object> param) {
		operationOrganizationRepository.deleteOperationOrganizationLink(param);
	}

	/**
	 * 운영조직 수정
	 * @param param
	 */
	public void updateOperationOrganization(Map<String, Object> param) {
		operationOrganizationRepository.updateOperationOrganization(param);
	}

	/**
	 * 운영조직 상위계층 리프노드(자식없는 노드) 여부 수정
	 * @param param
	 */
	public void updateOperationOrganizationParentLeafNodeYn(Map<String, Object> param) {
		operationOrganizationRepository.updateOperationOrganizationParentLeafNodeYn(param);
	}

	/**
	 * 운영조직의 사용자를 수정
	 * @param param
	 */
	public void updateOperationOrganizationUserInfo(Map<String, Object> param) {
		operationOrganizationRepository.updateOperationOrganizationUserInfo(param);
	}

	/**
	 * 운영조직 목록을 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganization(Map<String, Object> param) {
		return operationOrganizationRepository.findListOperationOrganization(param);
	}

	/**
	 * 운영조직 사용자 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganizationUser(Map<String, Object> param) {
		return operationOrganizationRepository.findListOperationOrganizationUser(param);
	}
	
	/**
	 * 운영조직의 연결정보 목록을 조회한다. (회사 코드로 소속된 logic_org_cd를 취득 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganizationLink(Map<String, Object> param) {

		String logicOrgCode = param.getOrDefault("logic_org_cd","") == null? "" : (String)param.getOrDefault("logic_org_cd","");
		if(StringUtils.isNotEmpty(logicOrgCode)){
			List<String> companyIncludeLogicOrganizationCodeList = this.findListLoginOrganizationCodeByIncludeCompanyLoginOrganizationCode(param);
			param.put("logic_org_cds",companyIncludeLogicOrganizationCodeList);
		}
		return operationOrganizationRepository.findListOperationOrganizationLink(param);
	}

	private List<String> findListLoginOrganizationCodeByIncludeCompanyLoginOrganizationCode(Map<String, Object> param) {
		return operationOrganizationRepository.findListLoginOrganizationCodeByIncludeCompanyLoginOrganizationCode(param);
	}

	/**
	 * logic_org_cd 을 통해 계층 구조 간 상위 logic_org_cd list를 찾아낸다.
	 * @param param
	 * @return
	 */
	public List<String> findListIncludeCompanyLoginOrganizationCodeByLogicOrganizationCode(Map<String, Object> param) {
		return operationOrganizationRepository.findListIncludeCompanyLoginOrganizationCodeByLogicOrganizationCode(param);
	}


	/**
	 * 운영조직 존재여부를 확인한다. (조회 조건 운영조직 코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationByOperationOrganizationCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationByOperationOrganizationCode(param) > 0);
	}


	/**
	 * 운영조직 존재여부 확인 ( 조회조건 조직코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationByOrganizationCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationByOrganizationCode(param) > 0);
	}

	/**
	 * 운영조직을 카운트한다.(by operUnitCd - 운영단위코드)
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the count OperationOrganization (by operUnitCd)
	 * @Date : 2016. 2. 2
	 * @Method Name : getCountOperationOrganizationByOperationUnitCode
	 */
	/**
	 * 운영조직 존재여부 확인 ( 조회조건 운영단위코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationByOperationUnitCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationByOperationUnitCode(param) > 0);
	}

	/**
	 * 운영조직 사용자 존재여부 ( 조회조건 운영조직코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationUserByOperationOrganizationCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationUserByOperationOrganizationCode(param) > 0 );
	}

	/**
	 * 운영조직사용자 존재여부 체크 ( 조회조건 사용자 아이디 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationUserByUserId(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationUserByUserId(param) > 0 );
	}

	/**
	 * 운영조직 연결정보 존재여부 체크 ( 조회조건 운영조직코드 )
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationLinkByOperationOrganizationCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationLinkByOperationOrganizationCode(param) > 0 );
	}

	/**
	 * 운영조직의 연결정보 존재여부 체크 (조회조건 소스운영조직코드 & 타겟운영조직코드)
	 * @param param
	 * @return
	 */
	public boolean existOperationOrganizationLinkBySourceOperationOrganizationCodeAndTargetOperationOrganizationCode(Map<String, Object> param) {
		return (operationOrganizationRepository.getCountOperationOrganizationLinkBySourceOperationOrganizationCodeAndTargetOperationOrganizationCode(param) > 0);
	}

	/**
	 * 운영조직 목록 등록 및 수정
	 * @param param
	 * @return
	 */
	public ResultMap saveListOperationOrganization(Map<String, Object> param) {
		List<Map<String, Object>> insertOperationOrganizationList = (List<Map<String, Object>>)param.getOrDefault("insertOperationOrganizations", Lists.newArrayList());
		List<Map<String, Object>> updateOperationOrganizationList = (List<Map<String, Object>>)param.getOrDefault("updateOperationOrganizations", Lists.newArrayList());

		// 운영조직 목록 추가
		this.insertListOperationOrganization(insertOperationOrganizationList);

		// 운영조직 목록 수정
		this.updateListOperationOrganization(updateOperationOrganizationList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 운영조직 목록 수정
	 * @param updateOperationOrganizationList
	 */
	public void updateListOperationOrganization(List<Map<String, Object>> updateOperationOrganizationList) {
		for (Map<String, Object> row : updateOperationOrganizationList) {
			this.updateOperationOrganization(row);
		}
	}

	/**
	 * 운영조직 목록 추가
	 * @param insertOperationOrganizationList
	 */
	public void insertListOperationOrganization(List<Map<String, Object>> insertOperationOrganizationList) {
		for (Map<String, Object> row : insertOperationOrganizationList) {
			if (this.existOperationOrganizationByOperationOrganizationCode(row)) { // 운영조직코드 중복체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}else{
				this.insertOperationOrganization(row);
				this.updateOperationOrganizationParentLeafNodeYn(row);
			}
		}
	}

	/**
	 * 운영조직의 사용자 목록을 등록/수정
	 * @param param
	 * @return
	 */
	public ResultMap saveListOperationOrganizationUser(Map<String, Object> param) {
		List<Map<String, Object>> insertOperationOrganizationUserList = (List<Map<String, Object>>)param.getOrDefault("insertUsers",Lists.newArrayList());
		List<Map<String, Object>> updateOperationOrganizationUserList = (List<Map<String, Object>>)param.getOrDefault("updateUsers",Lists.newArrayList());

		// 운영조직 사용자 목록 추가
		this.insertListOperationOrganizationUser(insertOperationOrganizationUserList);

		// 운영조직 사용자 목록 수정
		this.updateListOperationOrganizationUserInfo(updateOperationOrganizationUserList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 운영조직 목록 수정
	 * @param updateOperationOrganizationUserList
	 */
	public void updateListOperationOrganizationUserInfo(List<Map<String, Object>> updateOperationOrganizationUserList) {
		for (Map<String, Object> row : updateOperationOrganizationUserList) {
			this.updateOperationOrganizationUserInfo(row);
		}
	}

	/**
	 * 운영조직 사용자 목록 추가
	 * @param insertOperationOrganizationUserList
	 */
	public void insertListOperationOrganizationUser(List<Map<String, Object>> insertOperationOrganizationUserList) {
		for (Map<String, Object> row : insertOperationOrganizationUserList) {
			if (this.existOperationOrganizationUserByUserId(row)) { // 사용자아이디 중복체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}else{
				this.insertOperationOrganizationUser(row);
			}
		}
	}

	/**
	 * 운영조직의 연결정보 목록을 등록/삭제
	 * @param param
	 * @return
	 */
	public ResultMap saveListOperationOrganizationLink(Map<String, Object> param) {
		List<Map<String, Object>> insertOperationOrganizationLinkList = (List<Map<String, Object>>)param.getOrDefault("insertLinks",Lists.newArrayList());
		List<Map<String, Object>> deleteOperationOrganizationLinkList = (List<Map<String, Object>>)param.getOrDefault("deleteLinks",Lists.newArrayList());
		
		// 운영조직 링크 목록 추가
		this.insertListOperationOrganizationLink(insertOperationOrganizationLinkList);
		
		//  운영조직 링크 목록 수정
		this.deleteListOperationOrganizationLink(deleteOperationOrganizationLinkList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 운영조직 링크 목록 수정
	 * @param deleteOperationOrganizationLinkList
	 */
	public void deleteListOperationOrganizationLink(List<Map<String, Object>> deleteOperationOrganizationLinkList) {
		for (Map<String, Object> row : deleteOperationOrganizationLinkList) {
			this.deleteOperationOrganizationLink(row);
		}
	}

	/**
	 * 운영조직 링크 목록 추가
	 * @param insertOperationOrganizationLinkList
	 */
	private void insertListOperationOrganizationLink(List<Map<String, Object>> insertOperationOrganizationLinkList) {
		for (Map<String, Object> row : insertOperationOrganizationLinkList) {
			if (this.existOperationOrganizationLinkBySourceOperationOrganizationCodeAndTargetOperationOrganizationCode(row)) { // 소스운영조직코드 & 타겟운영조직코드 중복체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}else {
				this.insertOperationOrganizationLink(row);
			}
		}
	}

	/**
	 * 운영조직의 목록을 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListOperationOrganizationRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteOperationOrganizationList = (List<Map<String, Object>>)param.getOrDefault("deleteOperationOrganizations",Lists.newArrayList());

		// 운영조직 목록 삭제
		this.deleteListOperationOrganization(deleteOperationOrganizationList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 운영조직 목록 삭제
	 * @param deleteOperationOrganizationList
	 */
	public void deleteListOperationOrganization(List<Map<String, Object>> deleteOperationOrganizationList) {
		for (Map<String, Object> row : deleteOperationOrganizationList) {
			if (this.existOperationOrganizationUserByOperationOrganizationCode(row) || this.existOperationOrganizationLinkByOperationOrganizationCode(row)) { // 추가된 사용자가 있는지, 연결된 운영조직이 있는지 검사
				throw new CommonException(ErrorCode.USED);
			}else {
				this.deleteOperationOrganization(row);
				this.updateOperationOrganizationParentLeafNodeYn(row);
			}
		}
	}
	
	/**
	 * 운영조직 사용자 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListOperationOrganizationUserRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteOperationOrganizationUserList = (List<Map<String, Object>>)param.getOrDefault("deleteUsers",Lists.newArrayList());

		// 운영조직 사용자 목록 삭제
		this.deleteListOperationOrganizationUser(deleteOperationOrganizationUserList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 운영조직 사용자 목록 삭제
	 * @param deleteOperationOrganizationUserList
	 */
	private void deleteListOperationOrganizationUser(List<Map<String, Object>> deleteOperationOrganizationUserList) {
		for (Map<String, Object> row : deleteOperationOrganizationUserList) {
			this.deleteOperationOrganizationUser(row);
		}
	}

	/**
	 * 사용자 운영조직 리스트 삭제
	 * @param deleteUserList
	 */
	public void deleteListOperationOrganizationByUserId(List<Map<String, Object>> deleteUserList) {
		for (Map<String, Object> deleteUserInfo : deleteUserList) {
			this.deleteOperationOrganizationByUserId(deleteUserInfo);
		}
	}

	public List findListOperationOrganizationDept(Map param) {
		return operationOrganizationRepository.findListOperationOrganizationDept(param);
	}

	public ResultMap saveListOperationOrganizationDept(Map param) {
		List<Map<String, Object>> insertOperationOrganizationDetpList = (List<Map<String, Object>>)param.getOrDefault("insertDeptList",Lists.newArrayList());

		// 운영조직 사용자 목록 추가
		this.insertListOperationOrganizationDept(insertOperationOrganizationDetpList);


		return ResultMap.SUCCESS();
	}

	private void insertListOperationOrganizationDept(List<Map<String, Object>> insertOperationOrganizationDetpList) {
		for (Map<String, Object> deptInfo : insertOperationOrganizationDetpList) {
			if (this.existOperationOrganizationUserByDeptCode(deptInfo)) { // 운영조직 부서 맵핑 중복체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}else{
				this.insertOperationOrganizationDept(deptInfo);
			}
		}
	}

	private void insertOperationOrganizationDept(Map<String, Object> deptInfo) {
		operationOrganizationRepository.insertOperationOrganizationDept(deptInfo);
	}

	/**
	 * 운영조직 부서 존재여부 체크 ( 조회조건 부서 코드 )
	 * @param deptInfo
	 * @return
	 */
	private boolean existOperationOrganizationUserByDeptCode(Map<String, Object> deptInfo) {
		return (operationOrganizationRepository.getCountOperationOrganizationUserByDeptCode(deptInfo) > 0 );
	}

	public ResultMap deleteListOperationOrganizationDeptRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteOperationOrganizationDeptList = (List<Map<String, Object>>)param.getOrDefault("deleteDeptList",Lists.newArrayList());

		// 운영조직 부서 목록 삭제
		this.deleteListOperationOrganizationDept(deleteOperationOrganizationDeptList);

		return ResultMap.SUCCESS();
	}

	private void deleteListOperationOrganizationDept(List<Map<String, Object>> deleteOperationOrganizationDeptList) {
		for (Map<String, Object> row : deleteOperationOrganizationDeptList) {
			this.deleteOperationOrganizationDept(row);
		}
	}

	public void deleteOperationOrganizationDept(Map<String, Object> param) {
		operationOrganizationRepository.deleteOperationOrganizationDept(param);
	}

	public String findCompanyCodeByLogicOrganizationCode(Map<String,Object> param){
		return operationOrganizationRepository.findCompanyCodeByLogicOrganizationCode(param);
	}

	public List<Map<String, Object>> findOperationOrgVendorList(Map<String,Object> param){
		return operationOrganizationRepository.findOperationOrgVendorList(param);
	}

	public String findCompanyCodeByOorgCd(Map<String,Object> param){
		return operationOrganizationRepository.findCompanyCodeByOorgCd(param);
	}
}
