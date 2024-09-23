package smartsuite.app.bp.admin.organizationManager.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class OrganizationManagerRepository {
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "organization.";


	/**
	 * 조직 정보 추가
	 * @param param
	 */
	public void insertLogicOrganizationInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertLogicOrganizationInfo", param);
	}

	/**
	 * 조직 내 부서 추가
	 * @param param
	 */
	public void insertDepartmentByOrganization(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertDepartmentByOrganization", param);
	}

	/**
	 * 조직 유형을 삭제한다.
	 * @param param
	 */
	public void deleteLogicOrganizationType(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteLogicOrganizationType", param);
	}

	public void deleteOperatingOrganizationByDepartmentCode(Map<String, Object> param){
		sqlSession.delete(NAMESPACE + "deleteOperatingOrganizationByDepartmentCode", param);
	}

	/**
	 * 조직 정보 삭제
	 * @param param
	 */
	public void deleteLogicOrganizationInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteLogicOrganizationInfo", param);
	}

	/**
	 * 부서 삭제
	 * @param param
	 */
	public void deleteDepartment(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "deleteDepartment", param);
	}

	/**
	 * 조직 정보 수정
	 * @param param
	 */
	public void updateLogicOrganizationInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateLogicOrganizationInfo", param);
	}

	/**
	 * 조직내 부서 수정
	 * @param param
	 */
	public void updateDepartmentByOrganization(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateDepartmentByOrganization", param);
	}

	/**
	 * 조직 상태 정보 수정
	 * @param param
	 */
	public void updateDepartmentStatusToDelete(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateDepartmentStatusToDelete", param);
	}

	/**
	 * 조직 내 부서 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDepartmentByOrganization(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListDepartmentByOrganization", param);
	}

	/**
	 * 상위 부서 소속 부서이름 리스트 조회
	 * @param parentsDepartmentCode
	 * @return
	 */
	public List<String> findListParentsDepartmentCodeAffiliationDepartmentName(String parentsDepartmentCode) {
		return sqlSession.selectList(NAMESPACE + "findListParentsDepartmentCodeAffiliationDepartmentName", parentsDepartmentCode);
	}


	/**
	 * 조직 개수 조회 ( 조회 조건 조직코드)
	 * @param param
	 * @return
	 */
	public int getCountLogicOrganizationByOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountLogicOrganizationByOrganizationCode", param);
	}

	/**
	 * 조직 개수 조회  ( 조회조건 조직유형코드)
	 * @param param
	 * @return
	 */
	public int getCountOrganizationByOrganizationTypeCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOrganizationByOrganizationTypeCode", param);
	}

	/**
	 * 부서 개수 조회 ( 조회 조건 무서 코드 )
	 * @param param
	 * @return
	 */
	public int getCountDepartmentByDepartmentCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountDepartmentByDepartmentCode", param);
	}

	/**
	 * 사업자 번호 개수 조회
	 * @param param
	 * @return
	 */
	public int getCountBusinessRegistrationNumber(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountBusinessRegistrationNumber", param);
	}

	/**
	 * 사업자 번호 개수 조회 (조회 조건 조직코드)
	 * @param param
	 * @return
	 */
	public int getCountBusinessRegistrationNumberByLogicOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountBusinessRegistrationNumberByLogicOrganizationCode", param);
	}

	/**
	 * 조직 부서 맵핑 테이블 등록
	 * @param departmentInfo
	 */
	public void insertDepartmentOrganizationMapping(Map<String, Object> departmentInfo) {
		sqlSession.insert(NAMESPACE+"insertDepartmentOrganizationMapping",departmentInfo);
	}

	/**
	 * 조직 & 부서 맵핑 테이블 삭제 (조회 조건 조직 코드 )
	 * @param organizationInfo
	 */
	public void deleteLogicOrganizationAndDeptInfoByOrganizationCode(Map<String, Object> organizationInfo) {
		sqlSession.delete(NAMESPACE+"deleteLogicOrganizationAndDeptInfoByOrganizationCode",organizationInfo);
	}

	/**
	 * 조직 & 부서 맵핑 테이블 삭제 (조회 조건 부서 코드 )
	 * @param organizationInfo
	 */
	public void deleteLogicalOrganizationDepartmentMappingByDepartmentCode(Map<String, Object> organizationInfo) {
		sqlSession.delete(NAMESPACE+"deleteLogicalOrganizationDepartmentMappingByDepartmentCode",organizationInfo);
	}

	public List findListLogicOrganizationInfo(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListLogicOrganizationInfo",param);
	}

	public Map<String, Object> findLogicOrganizationInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findLogicOrganizationInfo", param);
	}

	public Map<String, Object> getDepartmentByCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getDepartmentByCode", param);
	}
	
	public List findListHierachiDept(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListHierachiDept", param);
	}
	
	public Map findLogicOrganizationByCompanyCode(String companyCode) {
		return sqlSession.selectOne(NAMESPACE + "findLogicOrganizationByCompanyCode", companyCode);
	}
}
