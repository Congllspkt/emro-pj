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
public class MenuFuncRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "menu-function.";

	// MENU_ACT 삭제 (메뉴기능 전체 삭제)
	public void deleteMenuFuncAll(Map<String,Object> deleteMenu){
		sqlSession.delete(NAMESPACE + "deleteMenuFuncAll", deleteMenu);
	}

	/**
	 * 메뉴기능을 삭제한다.
	 * - 물리적 삭제
	 * - 롤 기능에서 사용중인 메뉴기능은 삭제 불가 : roleFuncService.getCountRoleFunc 로 검사하고 호출할 것.
	 * @param deleteMenu
	 */
	public void deleteMenuFunc(Map<String,Object> deleteMenu){
		sqlSession.delete(NAMESPACE + "deleteMenuFunc", deleteMenu);
	}

	/**
	 * 메뉴기능을 등록한다.
	 * - 메뉴기능코드는 Unique : getCountMenuFunc 로 검사하고 호출할 것.
	 * @param funcInfo
	 */
	public void insertMenuFunc(Map<String,Object> funcInfo){
		sqlSession.insert(NAMESPACE + "insertMenuFunc", funcInfo);
	}

	/**
	 * 메뉴기능을 수정한다.
	 * @param funcInfo
	 */
	public void updateMenuFunc(Map<String,Object> funcInfo){
		sqlSession.update(NAMESPACE + "updateMenuFunc", funcInfo);
	}

	/**
	 * 메뉴기능 목록을 조회한다.
	 * @param funcInfo
	 */
	public List<Map<String, Object>> findListMenuFunc(Map<String,Object> funcInfo){
		return sqlSession.selectList(NAMESPACE + "findListMenuFunc", funcInfo);
	}

	/**
	 * 메뉴의 패턴 리스트를 조회한다
	 * @param funcInfo
	 */
	public List<Map<String, Object>> findListMenuPattern(Map<String,Object> funcInfo){
		return sqlSession.selectList(NAMESPACE + "findListMenuPattern", funcInfo);
	}


	public int getCountMenuFunc(Map<String,Object> funcInfo){
		return sqlSession.selectOne(NAMESPACE + "getCountMenuFunc", funcInfo);
	}
}
