package smartsuite.app.common.board.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class BoardRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "board.";
	
	/**
	 * before : findListBoard 
	 * @param param
	 * @return
	 */
	public List findListBoardInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListBoardInfo", param);
	}
	
	public String findPostNo(Map<String, Object> info) {
		return sqlSession.selectOne(NAMESPACE+"findPostNo", info);
	}
	
	public void updateBoardForPostGrpSn(Map<String, Object> info) {
		sqlSession.update(NAMESPACE+"updateBoardForPostGrpSn", info);
	}
	
	public void insertBoard(Map<String, Object> info) {
		sqlSession.update(NAMESPACE+"insertBoard", info);
	}
	
	public void updateBoard(Map<String, Object> info) {
		sqlSession.update(NAMESPACE+"updateBoard", info);
	}
	
	public void updateDeleteBoard(Map info){
		sqlSession.update(NAMESPACE+"updateDeleteBoard", info);
	}
	
	public int findBoardReadRoleInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardReadRoleInfo",param);
	}
	
	public int findBoardSaveRoleInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardSaveRoleInfo",param);
	}
	
	public int hasBoardRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasBoardRole", boardInfo);
	}
	
	public int hasBoardInsertRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasBoardInsertRole", boardInfo);
	}
	
	public int hasBoardUpdateRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasBoardUpdateRole", boardInfo);
	}
	
	public int hasCommentUpdateRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasCommentUpdateRole", boardInfo);
	}
	
	public int hasBoardDeleteRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasBoardDeleteRole", boardInfo);
	}
	
	public int hasCommentDeleteRole(Map<String, Object> boardInfo) {
		return sqlSession.selectOne(NAMESPACE+"hasCommentDeleteRole", boardInfo);
	}
	
	public void updateBoardOfViewCount(Map param) {
		sqlSession.update(NAMESPACE+"updateBoardOfViewCount", param);
	}
	
	/**
	 * before : findInfoBoard 
	 * @param param
	 * @return
	 */
	public Map findBoardInfoByPostNo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findBoardInfoByPostNo", param);
	}
	
	public List findBoardCommentList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findBoardCommentList", param);
	}
	
	public void insertBoardComment(Map info) {
		sqlSession.insert(NAMESPACE+"insertBoardComment", info);
	}
	
	public void updateBoardOfCommentCount(Map info) {
		sqlSession.insert(NAMESPACE+"updateBoardOfCommentCount", info);
	}
	
	public void updateBoardComment(Map info) {
		sqlSession.insert(NAMESPACE+"updateBoardComment", info);
	}
	
	public void deleteBoardComment(Map info) {
		sqlSession.insert(NAMESPACE+"deleteBoardComment", info);
	}
	
	public List<Map<String, Object>> findListNoticeInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListNoticeInfo", param);
	}
	
	public List<Map<String, Object>> findListPortalNoticeInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListPortalNoticeInfo", param);
	}
	
	public int hasReadRole(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"hasReadRole", param);
	}
	
	public int hasSaveRole(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"hasSaveRole", param);
	}
	
	public List findListPortalNoticeForGridPaging(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListPortalNoticeForGridPaging", param);
	}
	
	public int  getTotalBoardCount(Map<String, Object> param){
		return sqlSession.selectOne(NAMESPACE + "getTotalBoardCount",param);
	}
}
