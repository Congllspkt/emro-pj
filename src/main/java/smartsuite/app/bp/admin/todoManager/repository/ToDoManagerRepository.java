package smartsuite.app.bp.admin.todoManager.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ToDoManagerRepository {

	@Inject
	SqlSession sqlSession;

	private static final String NAMESPACE = "todo-manager.";

	/**
	 * 할일 그룹 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoGroup(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTodoGroup", param);
	}

	/**
	 * 할일 그룹 추가
	 * @param todoInfo
	 */
	public void insertTodoGroup(Map<String, Object> todoInfo) {
		sqlSession.insert(NAMESPACE+"insertTodoGroup", todoInfo);
	}

	/**
	 * to-do 그룹 수정
	 * @param todoInfo
	 */
	public void updateTodoGroup(Map<String, Object> todoInfo) {
		sqlSession.update(NAMESPACE+"updateTodoGroup", todoInfo);
	}

	/**
	 * 할일 항목 별 사용자 삭제
	 * @param todoInfo
	 */
	public void deleteTodoUser(Map<String, Object> todoInfo) {
		sqlSession.delete(NAMESPACE+"deleteTodoUser", todoInfo);
	}

	/**
	 * 할일 항목 삭제
	 * @param todoInfo
	 */
	public void deleteTodoFactor(Map<String, Object> todoInfo) {
		sqlSession.delete(NAMESPACE+"deleteTodoFactor", todoInfo);
	}

	/**
	 * 할일 그룹 삭제
	 * @param todoInfo
	 */
	public void deleteTodoGroup(Map<String, Object> todoInfo) {
		sqlSession.delete(NAMESPACE+"deleteTodoGroup", todoInfo);
	}

	/**
	 * 할일 항목 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoFactor(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTodoFactor", param);
	}

	/**
	 * 할일 항목 추가
	 * @param todoInfo
	 */
	public void insertTodoFactor(Map<String, Object> todoInfo) {
		sqlSession.insert(NAMESPACE+"insertTodoFactor", todoInfo);
	}

	/**
	 * 할일 항목 수정
	 * @param todoInfo
	 */
	public void updateTodoFactor(Map<String, Object> todoInfo) {
		sqlSession.update(NAMESPACE+"updateTodoFactor", todoInfo);
	}

	/**
	 * 할일 항목 별 사용자 목록 조회
	 *
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTodoUser", param);
	}

	/**
	 * 할일 항목별 사용자 추가
	 * @param todoInfo
	 */
	public void insertTodoUser(Map<String, Object> todoInfo) {
		sqlSession.insert(NAMESPACE+"insertTodoUser", todoInfo);
	}

	/**
	 * 할일 사용자 별 항목 목록 조회 ( 조회 조건 현재 접속 사용자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoFactorByCurrentUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTodoFactorByCurrentUser", param);
	}

	/**
	 * 사용자 to-do 리스트 조회 ( 조회조건 현재 접속 사용자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUserByCurrentUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListTodoUserByCurrentUser",param);
	}

	/**
	 * 할일 항목 별 사용자에 현재 사용자 신규 추가
	 * @param param
	 */
	public void insertTodoUserByCurrentUser(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "insertTodoUserByCurrentUser", param);
	}

	/**
	 * 할일 항목 별 사용자에 현재 사용자 삭제
	 * @param param
	 */
	public void deleteTodoUserByCurrentUser(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTodoUserByCurrentUser", param);
	}
}
