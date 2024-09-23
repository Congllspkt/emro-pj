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
public class RoleRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "role.";


	// ESAAUMS 삭제 (수집된 호출패턴 전체 삭제)
	public void deleteCallPatternAll(Map<String,Object> deleteMenu){
		sqlSession.delete(NAMESPACE + "deleteCallPatternAll", deleteMenu);
	}

	//ESAAUMS 추가
	public void insertCallPattern(Map<String,Object> funcUrlInfo){
		sqlSession.insert(NAMESPACE + "insertCallPattern", funcUrlInfo);
	}


	/**
	 * 콜 패턴 조회
	 * @param menuInfo
	 * @return
	 */
	public List<Map<String,Object>> findListCallPattern(Map<String, Object> menuInfo){
		return sqlSession.selectList(NAMESPACE + "findListCallPattern", menuInfo);
	}


	/**
	 * 롤 조회
	 * @param param
	 * @return
	 */
	public List findListRole(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListRole", param);
	}

	/**
	 * 롤 추가
	 * @param insertRoleInfo
	 */
	public void insertRole(Map insertRoleInfo) {
		sqlSession.insert(NAMESPACE + "insertRole", insertRoleInfo);
	}

	/**
	 * 롤 수정
	 * @param insertRoleInfo
	 */
	public void updateRole(Map insertRoleInfo) {
		sqlSession.update(NAMESPACE + "updateRole", insertRoleInfo);
	}

	/**
	 * 롤 코드 조회 find 조건 code
	 * @param code
	 * @return
	 */
	public Map findRoleByCode(String code) {
		return sqlSession.selectOne(NAMESPACE + "findRoleByCode", code);
	}

	/**
	 * 롤 메뉴 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleMenu(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRoleMenu", deleteRoleInfo);
	}

	/**
	 * 롤 유저 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleUser(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRoleUser", deleteRoleInfo);
	}

	/**
	 * 롤 부서 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleDept(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRoleDept", deleteRoleInfo);
	}

	/**
	 * 롤 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRole(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRole", deleteRoleInfo);
	}

	/**
	 * 롤 메뉴 추가
	 * @param insertRoleMenu
	 */
	public void insertRoleMenu(Map insertRoleMenu) {
		sqlSession.insert(NAMESPACE + "insertRoleMenu", insertRoleMenu);
	}

	/**
	 * 롤 메뉴 리스트 조회
	 * @param roleInfo
	 * @return
	 */
	public List findListRoleMenu(Map roleInfo) {
		return sqlSession.selectList(NAMESPACE + "findListRoleMenu", roleInfo);
	}


	/**
	 * 롤 유저 조회
	 * @param param
	 * @return
	 */
	public List findListUserByRole(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListUserByRole", param);
	}

	/**
	 * 롤 유저 추가
	 * @param insertRoleUser
	 */
	public void insertRoleUser(Map insertRoleUser) {
		sqlSession.insert(NAMESPACE + "insertRoleUser", insertRoleUser);
	}

	/**
	 * 롤 부서 리스트 조회
	 * @param param
	 * @return
	 */
	public List findListRoleDept(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListRoleDept", param);
	}

	/**
	 * 롤 부서 추가
	 * @param insertRoleDept
	 */
	public void insertRoleDept(Map insertRoleDept) {
		sqlSession.insert(NAMESPACE + "insertRoleDept", insertRoleDept);
	}

	/**
	 * URL 롤 조회
	 * @param pattern
	 * @return
	 */
	public List<Map<String, Object>> selectRoleWithUrl(Map pattern) {
		return sqlSession.selectList(NAMESPACE + "selectRoleWithUrl", pattern);
	}

	/**
	 * 메뉴 롤 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMenuRole(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListMenuRole", param);
	}

	public void deleteCallPattern(Map<String, Object> deleteMenuUrlInfo) {
		sqlSession.delete(NAMESPACE + "deleteCallPattern", deleteMenuUrlInfo);
	}

	public void deleteRolePortlet(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRolePortlet", deleteRoleInfo);
	}
}
