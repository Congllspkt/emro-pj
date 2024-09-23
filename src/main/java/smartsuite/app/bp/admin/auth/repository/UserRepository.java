package smartsuite.app.bp.admin.auth.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class UserRepository {

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "user.";

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;


	/**
	 * 사용자 리스트 조회
	 * @param searchParam
	 * @return
	 */
	public List<Map<String, Object>> findListUser(Map searchParam) {
		return sqlSession.selectList(NAMESPACE + "findListUser", searchParam);
	}
	
	/**
	 * 교육용 사용자 리스트 조회
	 * @param searchParam
	 * @return
	 */
	public List<Map<String, Object>> findListUserEdu(Map searchParam) {
		return sqlSession.selectList(NAMESPACE + "findListUserEdu", searchParam);
	}

	/**
	 * 사용자 조회 ( 조건 user id )
	 * @param searchParam
	 * @return
	 */
	public Map<String, Object> findUserByUserId(Map searchParam) {
		return sqlSession.selectOne(NAMESPACE + "findUserByUserId", searchParam);
	}

	/**
	 * 사용자 역할 삭제
	 * @param param
	 */
	public void deleteUserRoleByUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteUserRoleByUserId", param);
	}

	/**
	 * 사용자 삭제
	 * @param user
	 */
	public void deleteUser(Map<String, Object> user) {
		sqlSession.delete(NAMESPACE + "deleteUser",user);
	}


	/**
	 * 사용자 조회 후 count로 받기
	 * @param param
	 * @return
	 */
	public int getUserCount(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getUserCount",param);
	}

	/**
	 * 사용자 수정
	 * @param param
	 */
	public void updateUser(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateUser",param);
	}

	/**
	 * 사용자 추가
	 * @param param
	 */
	public void insertUser(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertUser",param);
	}

	/**
	 * 패스워드 수정
	 * @param param
	 */
	public void updatePassword(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updatePw", param);
	}

	/**
	 * 사용자 롤 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListRoleByUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListRoleByUser", param);
	}

	/**
	 * 사용자 롤 삭제 ( 조건 role code & user id )
	 * @param param
	 */
	public void deleteUserRoleByRoleCodeAndUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteUserRoleByRoleCodeAndUserId",param);
	}

	/**
	 * 사용자 별 역할 추가
	 * @param param
	 */
	public void insertUserRoleByUser(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertUserRoleByUser",param);
	}

	/**
	 * 사용자 운영조직리스트 조회
	 * @param param
	 * @return
	 */
	public List findListUserOperationOrganization(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListUserOperationOrganization", param);
	}

	/**
	 * 사용자 정보 조회 ( 조회조건 email/ userid )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findUserInfoByUserIdAndEmail(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findUserInfoByUserIdAndEmail", param);
	}

	/**
	 * 패스워드 초기화  ( 조회조건 user id )
	 * @param user
	 */
	public void initPwByUserId(Map<String, Object> user) {
		sqlSession.update(NAMESPACE + "initPwByUserId", user);
	}

	/**
	 * 계정 잠김 상태 해제
	 * @param param
	 */
	public void saveUserAccLockYn(Map param) {
		sqlSession.update(NAMESPACE + "saveUserAccLockYn", param);
	}
	
	public List findListUserByDept(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListUserByDept", param);
	}
}
