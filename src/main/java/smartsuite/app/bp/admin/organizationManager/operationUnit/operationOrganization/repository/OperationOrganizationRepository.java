package smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class OperationOrganizationRepository {
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "operation-organization.";


	public int getCountOperationOrganizationByOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationByOrganizationCode", param);
	}

	/**
	 * 운영조직 등록
	 * @param param
	 */
	public void insertOperationOrganization(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertOperationOrganization", param);
	}

	/**
	 * 운영조직 사용자 등록
	 * @param param
	 */
	public void insertOperationOrganizationUser(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertOperationOrganizationUser", param);
	}

	/**
	 * 운영조직 연결정보를 등록
	 * @param param
	 */
	public void insertOperationOrganizationLink(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertOperationOrganizationLink", param);
	}

	/**
	 * 운영조직 삭제
	 * @param param
	 */
	public void deleteOperationOrganization(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationOrganization", param);
	}

	/**
	 * 운영조직 사용자 삭제
	 * @param param
	 */
	public void deleteOperationOrganizationUser(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationOrganizationUser", param);
	}

	/**
	 * 운영조직 삭제 ( 조회 조건 user id )
	 * @param param
	 */
	public void deleteOperationOrganizationByUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationOrganizationByUserId", param);
	}

	/**
	 * 운영조직 연결정보 삭제
	 * @param param
	 */
	public void deleteOperationOrganizationLink(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationOrganizationLink", param);
	}

	/**
	 * 운영조직 수정
	 * @param param
	 */
	public void updateOperationOrganization(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateOperationOrganization", param);
	}

	/**
	 * 운영조직 상위계층 리프노드(자식없는 노드) 여부 수정
	 * @param param
	 */
	public void updateOperationOrganizationParentLeafNodeYn(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateOperationOrganizationParentLeafNodeYn", param);
	}

	/**
	 * 운영조직 사용자 수정
	 * @param param
	 */
	public void updateOperationOrganizationUserInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateOperationOrganizationUserInfo", param);
	}

	/**
	 * 운영조직 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganization(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganization", param);
	}

	/**
	 * 운영조직 사용자 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganizationUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationUser", param);
	}

	/**
	 * 운영조직 연결 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationOrganizationLink(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationLink", param);
	}

	/**
	 * 운영조직 존재여부 카운트 ( 조회조건 운영조직 코드 )
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationByOperationOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationByOperationOrganizationCode", param);
	}

	/**
	 * 운영조직 존재여부 카운트 ( 조회조건 운영 단위 코드 )
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationByOperationUnitCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationByOperationUnitCode", param);
	}

	/**
	 * 운영조직 사용자 존재여부 카운트 ( 조회 조건 운영조직코드 )
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationUserByOperationOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationUserByOperationOrganizationCode", param);
	}

	/**
	 * 운영조직 사용자 존재여부 카운트 ( 조회조건 사용자 아이디 )
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationUserByUserId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationUserByUserId", param);
	}

	/**
	 * 운영조직 연결 존재여부 카운트 ( 조회조건 운영조직 코드 )
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationLinkByOperationOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationLinkByOperationOrganizationCode", param);
	}

	/**
	 * 운영조직의 연결정보 존재여부 카운트 (조회조건 소스운영조직코드 & 타겟운영조직코드)
	 * @param param
	 * @return
	 */
	public int getCountOperationOrganizationLinkBySourceOperationOrganizationCodeAndTargetOperationOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationLinkBySourceOperationOrganizationCodeAndTargetOperationOrganizationCode", param);
	}

	public List findListOperationOrganizationDept(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListOperationOrganizationDept",param);
	}

	public int getCountOperationOrganizationUserByDeptCode(Map<String, Object> deptInfo) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationOrganizationUserByDeptCode", deptInfo);
	}

	public void insertOperationOrganizationDept(Map<String, Object> deptInfo) {
		sqlSession.insert(NAMESPACE+ "insertOperationOrganizationDept",deptInfo);
	}

	public void deleteOperationOrganizationDept(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationOrganizationDept", param);
	}

	public String findCompanyCodeByLogicOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCompanyCodeByLogicOrganizationCode",param);
	}

	public List<String> findListIncludeCompanyLoginOrganizationCodeByLogicOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListIncludeCompanyLoginOrganizationCodeByLogicOrganizationCode",param);
	}

	public List<String> findListLoginOrganizationCodeByIncludeCompanyLoginOrganizationCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListLoginOrganizationCodeByIncludeCompanyLoginOrganizationCode",param);
	}

    public List<Map<String, Object>> findOperationOrgVendorList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findOperationOrgVendorList",param);
    }

    public String findCompanyCodeByOorgCd(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findCompanyCodeByOorgCd",param);
    }
}
