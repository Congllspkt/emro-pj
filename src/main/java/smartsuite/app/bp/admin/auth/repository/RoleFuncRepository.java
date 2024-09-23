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
public class RoleFuncRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "role-function.";


	// ESAAURF 삭제 (매핑된 롤메뉴 전체 삭제)
	public void deleteRoleFuncAll(Map<String,Object> deleteMenu){
		sqlSession.delete(NAMESPACE + "deleteRoleFuncAll", deleteMenu);
	}

	/**
	 * 롤기능을 카운트한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the count RoleFunc
	 * @Date : 2016. 2. 2
	 * @Method Name : getCountRoleFunc
	 */
	public int getCountRoleFunc(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountRoleFunc", param);
	}


	/**
	 * 롤 기능 추가
	 * @param param
	 */
	public void insertRoleFunc(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertRoleFunc", param);
	}

	/**
	 * 롤 기능 삭제
	 * @param param
	 */
	public void deleteRoleFunc(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteRoleFunc", param);
	}

	/**
	 * 롤 메뉴 기능 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleMenuFunc(Map deleteRoleInfo) {
		sqlSession.delete(NAMESPACE + "deleteRoleMenuFunc", deleteRoleInfo);
	}

	/**
	 * 롤 메뉴기능 목록 조회를 요청한다.
	 * @param roleInfo
	 * @return
	 */
	public List findListRoleMenuWithMenuFunc(Map roleInfo) {
		return sqlSession.selectList(NAMESPACE + "findListRoleMenuWithMenuFunc", roleInfo);
	}

	public List<Map<String, Object>> findListMenuFuncRole(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListMenuFuncRole", param);
	}
}
