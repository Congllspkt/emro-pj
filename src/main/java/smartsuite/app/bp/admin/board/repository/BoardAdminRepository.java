package smartsuite.app.bp.admin.board.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class BoardAdminRepository {
	/** The sql session. */
	@Inject
	SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "board-comment.";
	
	/**
	 * before : findListBoardAdmin
	 * @param param
	 * @return
	 */
	public List findBoardAdminList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findBoardAdminList", param);
	}
	
	/**
	 * before : findInfoBoardAdmin
	 * @param param
	 * @return
	 */
	public Map findBoardAdminInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardAdminInfo", param);
	}
	
	/**
	 * before : findInfoBoardAdminYn
	 * @param param
	 * @return
	 */
	public Map findBoardAdminYnInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardAdminYnInfo", param);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public String findBoardId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardId", param);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void insertBoardAdmin(Map<String, Object> info) {
		sqlSession.insert(NAMESPACE+"insertBoardAdmin", info);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void updateBoardAdmin(Map<String, Object> info) {
		sqlSession.update(NAMESPACE+"updateBoardAdmin", info);
	}
	
	/**
	 * before : findListBoardByBoardId
	 * @param info
	 * @return
	 */
	public List findBoardByBoardIdList(Map<String, Object> info) {
		return sqlSession.selectList(NAMESPACE+"findBoardByBoardIdList", info);
	}
	
	/**
	 * before : findListAdminUser
	 * @param param
	 * @return
	 */
	public List findAdminUserList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findAdminUserList", param);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void deleteBoardAuthById(Map<String, Object> info) {
		sqlSession.delete(NAMESPACE+"deleteBoardAuthById", info);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void deleteAdminUserById(Map<String, Object> info) {
		sqlSession.delete(NAMESPACE+"deleteAdminUserById", info);
	}
	
	/**
	 * 
	 * @param info
	 */
	public void deleteBoardAdmin(Map<String, Object> info) {
		sqlSession.delete(NAMESPACE+"deleteBoardAdmin", info);
	}
	
	/**
	 * 
	 * @param row
	 */
	public void insertAdminUser(Map<String, Object> row) {
		sqlSession.insert(NAMESPACE+"insertAdminUser", row);
	}
	
	/**
	 * 
	 * @param row
	 */
	public void deleteAdminUser(Map<String, Object> row) {
		sqlSession.delete(NAMESPACE+"deleteAdminUser", row);
	}
	
	/**
	 * before : findListBoardAuth
	 * @param param
	 * @return
	 */
	public List findBoardAuthList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findBoardAuthList", param);
	}
	
	/**
	 * 
	 * @param row
	 */
	public void deleteBoardAuth(Map<String, Object> row) {
		sqlSession.delete(NAMESPACE+"deleteBoardAuth", row);
	}
	
	/**
	 * 
	 * @param row
	 */
	public void insertBoardAuth(Map<String, Object> row) {
		sqlSession.insert(NAMESPACE+"insertBoardAuth", row);
	}

    public List<Map<String, Object>> findCompanyListForBoard(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findCompanyListForBoard", param);
    }

	public List<Map<String, Object>> findBoardCompanyList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findBoardCompanyList", param);
	}

	public void insertBoardCompany(Map company) {
		sqlSession.insert(NAMESPACE+"insertBoardCompany", company);
	}

	public void deleteBoardCompany(Map company) {
		sqlSession.delete(NAMESPACE+"deleteBoardCompany", company);
	}

	public void updateBoardCompany(Map info) { sqlSession.update(NAMESPACE+"updateBoardCompany", info); }
}
