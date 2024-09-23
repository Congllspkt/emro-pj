package smartsuite.app.bp.admin.organizationManager.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.bp.admin.organizationManager.repository.OrganizationManagerRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * 조직유형/조직/부서 관련 처리하는 서비스 Class입니다.
 *
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class OrganizationManagerService {

	@Inject
	OperationOrganizationService operationOrganizationService;

	@Inject
	OrganizationManagerRepository organizationManagerRepository;

	/**
	 * 조직 정보를 추가
	 * - 조직유형를 선택하고 조직을 추가하나, 조직코드는 전체 유형 duplicate
	 */
	public void insertLogicOrganizationInfo(Map<String, Object> param) {
		organizationManagerRepository.insertLogicOrganizationInfo(param);
	}

	/**
	 * 조직 내 부서 추가
	 */
	public void insertDepartmentByOrganization(Map<String, Object> param) {
		organizationManagerRepository.insertDepartmentByOrganization(param);
	}

	/**
	 * 조직을 삭제한다.
	 */
	public void deleteLogicOrganizationInfo(Map<String, Object> param) {
		organizationManagerRepository.deleteLogicOrganizationInfo(param);
	}

	/**
	 * 조직 내 부서 삭제
	 */
	public void deleteDepartment(Map<String, Object> param) {
		organizationManagerRepository.deleteDepartment(param);
	}


	/**
	 * 조직을 수정한다.
	 */
	public void updateLogicOrganizationInfo(Map<String, Object> param) {
		organizationManagerRepository.updateLogicOrganizationInfo(param);
	}

	/**
	 * 조직 내 부서를 수정한다.
	 */
	public void updateDepartmentByOrganization(Map<String, Object> param) {
		organizationManagerRepository.updateDepartmentByOrganization(param);
	}

	/**
	 * 조직 내 부서 목록 조회
	 *
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDepartmentByOrganization(Map<String, Object> param) {

		String companyLogicOrganizationCode = operationOrganizationService.findCompanyCodeByLogicOrganizationCode(param);
		param.put("logic_org_cd",companyLogicOrganizationCode);
		List<Map<String, Object>> departmentList = organizationManagerRepository.findListDepartmentByOrganization(param);

		String departmentCode = param.getOrDefault("dept_cd", "") == null?  "" : (String) param.getOrDefault("dept_cd", "");
		String departmentName = param.getOrDefault("dept_nm", "")== null?  "" : (String) param.getOrDefault("dept_nm", "");
		if (StringUtils.isNotEmpty(departmentCode) || StringUtils.isNotEmpty(departmentName)) {
			// 조회된 dept_cd를 사용하여, 최상위 조직까지 조직도를 가져온다
			return this.findOrganizationDepartmentHierarchy(departmentList);
		}

		return departmentList;
	}

	/**
	 * dept_cd를 사용하여, 최상위 조직까지 조직도를 가져온다 ex> A부문 > A그룹 > A팀
	 *
	 * @param param the param
	 * @return the list dept
	 * @author : Sangmoon
	 * @Date : 2020. 6. 25
	 * @Method Name : getOrgChart
	 */
	public List<Map<String, Object>> findOrganizationDepartmentHierarchy(List<Map<String, Object>> departmentList) {
		Map<String, Object> departmentListMap = Maps.newHashMap();
		String parentsRootDepartmentCode = "ROOT";

		for (Map departmentInfo : departmentList) {
			String departmentName = departmentInfo.getOrDefault("disp_dept_nm", "") == null? "" : (String)departmentInfo.getOrDefault("disp_dept_nm", "");
			String parentsDepartmentCode = departmentInfo.getOrDefault("par_dept_cd", "") == null? "" : (String)departmentInfo.getOrDefault("par_dept_cd", "");

			// 상위 부서가 없을 경우
			if (parentsRootDepartmentCode.equals(parentsDepartmentCode)) {
				departmentInfo.put("org_chart", departmentName);
			} else { // 상위 부서가 있을 경우
				List<String> affiliationDepartmentName = null;

				if (!departmentListMap.containsKey(parentsDepartmentCode)) {
					//상위 부서에 포함된 부서 이름 리스트 검색
					affiliationDepartmentName = this.findListParentsDepartmentCodeAffiliationDepartmentName(parentsDepartmentCode);
					departmentListMap.put(parentsDepartmentCode, affiliationDepartmentName);
				} else {
					affiliationDepartmentName = (List<String>) departmentListMap.getOrDefault(parentsDepartmentCode, Lists.newArrayList());
				}
				String chartDepartmentString = "";

				for (int j = affiliationDepartmentName.size() - 1; j >= 0; j--) {
					if (j == affiliationDepartmentName.size() - 1) {
						chartDepartmentString = affiliationDepartmentName.get(j);
					} else {
						chartDepartmentString = chartDepartmentString + " > " + affiliationDepartmentName.get(j);
					}
				}
				if (StringUtils.isEmpty(chartDepartmentString)) {
					chartDepartmentString = departmentName;
				} else {
					chartDepartmentString = chartDepartmentString + " > " + departmentName;
				}
				departmentInfo.put("org_chart", chartDepartmentString);
			}
		}
		return departmentList;
	}

	/**
	 * 상위 부서 소속 부서이름 리스트 조회
	 * @param parentsDepartmentCode
	 * @return
	 */
	public List<String> findListParentsDepartmentCodeAffiliationDepartmentName(String parentsDepartmentCode) {
		return organizationManagerRepository.findListParentsDepartmentCodeAffiliationDepartmentName(parentsDepartmentCode);
	}

	/**
	 * 조직 존재 여부 조회 ( 조회 조건 조직코드 )
	 * @param param
	 * @return
	 */
	public boolean existLogicOrganizationByOrganizationCode(Map<String, Object> param) {
		int countOrganization = organizationManagerRepository.getCountLogicOrganizationByOrganizationCode(param);
		return (countOrganization > 0);
	}

	/**
	 * 조직 존재 여부 조회 ( 조회 조건 조직유형코드 )
	 * @param param
	 * @return
	 */
	public boolean existOrganizationByOrganizationTypeCode(Map<String, Object> param) {
		int countOrganization = organizationManagerRepository.getCountOrganizationByOrganizationTypeCode(param);
		return (countOrganization > 0);
	}

	/**
	 *  부서 삭제 여부
	 * @param param
	 * @return
	 */
	public Map<String, Object> findDepartmentInfoByCode(Map<String, Object> param) {
		Map<String, Object> departmentInfo = Maps.newHashMap();
		departmentInfo = organizationManagerRepository.getDepartmentByCode(param);

		return organizationManagerRepository.getDepartmentByCode(param);
	}

	/**
	 * 조직 상세 정보 등록 및 수정
	 * @param param
	 * @return
	 */
	public Map<String, Object> saveLogicOrganizationInfo(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> organizationInfo = (Map<String, Object>)param.getOrDefault("orgInfo",Maps.newHashMap());

		if (MapUtils.isEmpty(organizationInfo)) {
			throw new CommonException("조직 정보가 존재하지 않습니다. 다시 정보를 확인해주세요. ");
		}
		boolean isNew = organizationInfo.get("is_new") == null? false : (Boolean) organizationInfo.get("is_new");
		String countryCode = organizationInfo.get("ctry_ccd") == null? "" : (String) organizationInfo.get("ctry_ccd");

		if (isNew) {
			if(this.existLogicOrganizationByOrganizationCode(organizationInfo) || ("KR".equals(countryCode) && this.existBusinessRegistrationNumber(organizationInfo))){
				throw new CommonException(ErrorCode.DUPLICATED);
			}
			// 조직 정보를 추가
			this.insertLogicOrganizationInfo(organizationInfo);

		} else {
			if(("KR".equals(countryCode) && this.existBusinessRegistrationNumberByOrganizationCode(organizationInfo))) {
				throw new CommonException(ErrorCode.DUPLICATED);
			}

			//조직정보 수정
			this.updateLogicOrganizationInfo(organizationInfo);
		}
		return resultMap;
	}


	/**
	 * 부서 목록을 등록/수정한다.
	 * @param param
	 * @return
	 */
	public Map<String, Object> saveListDepartmentByOrganization(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, Object>> insertDepartmentList = (List<Map<String, Object>>)param.getOrDefault("insertDepts",Lists.newArrayList());
		List<Map<String, Object>> updateDepartmentList = (List<Map<String, Object>>)param.getOrDefault("updateDepts",Lists.newArrayList());

		if(insertDepartmentList.size() > 0){
			//부서 리스트 추가
			this.insertListDepartmentByOrganization(insertDepartmentList);
		}
		if (updateDepartmentList.size() > 0) {
			//부서 리스트 수정
			this.updateListDepartmentByOrganization(updateDepartmentList);
		}
		return resultMap;
	}


	/**
	 * 조직별 부서 리스트 수정
	 * @param updateDepartmentList
	 */
	public void updateListDepartmentByOrganization(List<Map<String, Object>> updateDepartmentList) {
		for (Map<String, Object> departmentInfo : updateDepartmentList) {
			this.updateDepartmentByOrganization(departmentInfo);
		}
	}

	/**
	 * 조직별 부서 리스트 추가
	 * @param insertDepartmentList
	 */
	public void insertListDepartmentByOrganization(List<Map<String, Object>> insertDepartmentList) {
		Map<String, Object> tempDepartmentInfo = Maps.newHashMap();
		for (Map<String, Object> departmentInfo : insertDepartmentList) {
			tempDepartmentInfo = this.findDepartmentInfoByCode(departmentInfo);
			// 부서가 존재하지 않을 경우
			if(tempDepartmentInfo == null || tempDepartmentInfo.isEmpty()){
				this.insertDepartmentByOrganization(departmentInfo);  // 부서 Insert
				this.insertDepartmentOrganizationMapping(departmentInfo); // 조직 부서 맵핑 테이블 Insert
				continue;
			}

			// 부서가 존재하지만 삭제처리된 경우
			if("D".equals(tempDepartmentInfo.get("sts"))){
				this.updateDepartmentByOrganization(departmentInfo); // 부서 Update
				this.insertDepartmentOrganizationMapping(departmentInfo); // 조직 부서 맵핑 테이블 Insert
				continue;
			}
			// 부서가 존재할 경우 중복 Exception 처리
			throw new CommonException(ErrorCode.DUPLICATED);
		}
	}

	/**
	 * 부서 등록 시 조직 & 부서 맵핑테이블 등록
	 * @param departmentInfo
	 */
	public void insertDepartmentOrganizationMapping(Map<String, Object> departmentInfo) {
		organizationManagerRepository.insertDepartmentOrganizationMapping(departmentInfo);
	}


	/**
	 * 조직 목록을 삭제한다.
	 * @param param
	 * @return
	 */
	public ResultMap deleteListLogicOrganizationInfo(Map<String, Object> param) {
		List<Map<String, Object>> deleteOrganizationList = (List<Map<String, Object>>)param.getOrDefault("deleteLogicOrganizationList",Lists.newArrayList());

		if (deleteOrganizationList.size() > 0) {
			for (Map<String, Object> organizationInfo : deleteOrganizationList) {
				if (operationOrganizationService.existOperationOrganizationByOrganizationCode(organizationInfo)) { // 조직에서 사용중인 조직유형인지 검사
					throw new CommonException(ErrorCode.USED);
				}else{
					this.deleteLogicOrganizationInfo(organizationInfo); //조직 삭제
					this.deleteLogicOrganizationAndDeptInfoByOrganizationCode(organizationInfo); //조직 부서 연계 삭제
				}
			}
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 조직 & 부서 맵핑 테이블 삭제 (조회 조건 조직코드 )
	 * @param organizationInfo
	 */
	private void deleteLogicOrganizationAndDeptInfoByOrganizationCode(Map<String, Object> organizationInfo) {
		organizationManagerRepository.deleteLogicOrganizationAndDeptInfoByOrganizationCode(organizationInfo);
	}

	/**
	 * 조직 & 부서 맵핑 테이블 삭제 (조회 조건 부서코드 )
	 * @param organizationInfo
	 */
	private void deleteLogicalOrganizationDepartmentMappingByDepartmentCode(Map<String, Object> organizationInfo) {
		organizationManagerRepository.deleteLogicalOrganizationDepartmentMappingByDepartmentCode(organizationInfo);
	}

	private void deleteOperatingOrganizationByDepartmentCode(Map<String, Object> organizationInfo) {
		organizationManagerRepository.deleteOperatingOrganizationByDepartmentCode(organizationInfo);
	}


	/**
	 * 부서 목록을 삭제한다.
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDepartmentByOrganization(Map<String, Object> param) {
		List<Map<String, Object>> deleteDepartmentsList = (List<Map<String, Object>>)param.getOrDefault("deleteDepts",Lists.newArrayList());

		for (Map<String, Object> row : deleteDepartmentsList) {
			this.deleteLogicalOrganizationDepartmentMappingByDepartmentCode(row);
			this.deleteOperatingOrganizationByDepartmentCode(row);
			this.deleteDepartment(row);
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 사업자번호 존재여부 체크
	 * @param param
	 * @return
	 */
	public boolean existBusinessRegistrationNumber(Map<String, Object> param) {
		if(Strings.isNullOrEmpty(param.get("bizregno").toString())) {
			return false;
		}
		int countBusinessRegistrationNumber = organizationManagerRepository.getCountBusinessRegistrationNumber(param);
		return (countBusinessRegistrationNumber > 0);
	}

	/**
	 * 사업자번호 존재여부 체크 (조회 조건 조직코드)
	 * @param param
	 * @return
	 */
	public boolean existBusinessRegistrationNumberByOrganizationCode(Map<String, Object> param) {
		if(Strings.isNullOrEmpty(param.get("bizregno").toString())) {
			return false;
		}
		int countBusinessRegistrationNumber = organizationManagerRepository.getCountBusinessRegistrationNumberByLogicOrganizationCode(param);
		return (countBusinessRegistrationNumber > 0);
	}


	public List findListLogicOrganizationInfo(Map param) {
		return organizationManagerRepository.findListLogicOrganizationInfo(param);
	}

	public Map<String, Object> findLogicOrganizationInfo(Map<String, Object> param) {
		return organizationManagerRepository.findLogicOrganizationInfo(param);
	}

	public String findRootLogicOrganizationInfo(String logic_org_cd){
		String parnodeLogicOrgCd = "";
		Map logicOrgInfo = Maps.newHashMap();
		Map param = Maps.newHashMap();
		param.put("logic_org_cd", logic_org_cd);
		int cnt = 0;
		while(parnodeLogicOrgCd != "ROOT"){

			logicOrgInfo = organizationManagerRepository.findLogicOrganizationInfo(param);

			parnodeLogicOrgCd = (String)logicOrgInfo.get("parnode_logic_org_cd");

			if("ROOT".equals(parnodeLogicOrgCd)){
				break;
			}else{
				param.put("logic_org_cd", parnodeLogicOrgCd);
			}
			cnt++;

			if(cnt > 200){
				throw new CommonException("ROOT가 존재하지 않습니다. 다시 정보를 확인해주세요.");
			}
		}
		return (String)logicOrgInfo.get("logic_org_cd");
	}
	
	public List findListHierachiDept(Map param) {
		return organizationManagerRepository.findListHierachiDept(param);
	}
	
	public Map findLogicOrganizationByCompanyCode(String companyCode) {
		return organizationManagerRepository.findLogicOrganizationByCompanyCode(companyCode);
	}
}
