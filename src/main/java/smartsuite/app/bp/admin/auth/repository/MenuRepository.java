package smartsuite.app.bp.admin.auth.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Menu 관련 처리하는 서비스 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @FileName MenuService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 4
 * @변경이력 : [2016. 2. 4] Yeon-u Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MenuRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "menu.";

	/**
	 * list menu 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListMenu
	 */
	public List findListMenu(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListMenu", param);
	}

	/**
	 * ESAAURM 삭제 (매핑된 롤메뉴 삭제)
	 * @param deleteMenu
	 */
	public void deleteRoleMenuByMenu(Map deleteMenu) {
		sqlSession.delete(NAMESPACE + "deleteRoleMenuByMenu", deleteMenu);
	}

	/**
	 * MENU_MULTLANG 삭제 (메뉴명 삭제)
	 * @param deleteMenu
	 */
	public void deleteMenuName(Map deleteMenu) {
		sqlSession.delete(NAMESPACE + "deleteMenuName", deleteMenu);
	}

	/**
	 * ESAAUMM 삭제 (메뉴 삭제)
	 * @param deleteMenu
	 */
	public void deleteMenu(Map deleteMenu) {
		sqlSession.delete(NAMESPACE + "deleteMenu", deleteMenu);
	}

	/**
	 * ESAAUMM 추가 ( 메뉴 추가 )
	 * @param insertMenu
	 */
	public void insertMenuInfo(Map insertMenu) {
		sqlSession.insert(NAMESPACE + "insertMenuInfo", insertMenu);
	}

	/**
	 * MENU_MULTLANG 추가 ( 메뉴 이름 추가 )
	 * @param insertMenu
	 */
	public void insertMenuName(Map insertMenu) {
		sqlSession.insert(NAMESPACE + "insertMenuName", insertMenu);
	}


	/**
	 * menu by code 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param code the code
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : findMenuByCode
	 */
	public Map findMenuByCode(String code) {
		return sqlSession.selectOne(NAMESPACE +"findMenuByCode", code);
	}

	/**
	 * ESAAUMM 메뉴 수정
	 * @param updateMenu
	 */
	public void updateMenuInfo(Map updateMenu) {
		sqlSession.update(NAMESPACE + "updateMenuInfo", updateMenu);
	}

	/**
	 * MENU_MULTLANG 메뉴 이름 수정
	 * @param updateMenu
	 */
	public void updateMenuName(Map updateMenu) {
		sqlSession.update(NAMESPACE + "updateMenuName", updateMenu);
	}

	/**
	 * MENU_MULTLANG 메뉴 이름 언어 수정 (다국어)
	 * @param updateMenu
	 */
	public void updateMenuNameLang(Map updateMenu) {
		sqlSession.update(NAMESPACE +"updateMenuNameLang", updateMenu);
	}


	public void updateMenuDeleteStatus(Map deleteMenu) {
		sqlSession.update(NAMESPACE + "updateMenuDeleteStatus", deleteMenu);
	}

    public void deleteManual(Map deleteMenu) {
		sqlSession.delete(NAMESPACE + "deleteManual", deleteMenu);
    }
}
