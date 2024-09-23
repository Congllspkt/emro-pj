package smartsuite.app.bp.admin.organizationManager.operationUnit.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class OperationUnitManagerRepository {
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "operation-unit.";

	/**
	 * 운영단위를 등록한다.
	 * @param param
	 */
	public void insertOperationUnit(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertOperationUnit", param);
	}

	/**
	 * 운영단위 삭제
	 * @param param
	 */
	public void deleteOperationUnit(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteOperationUnit", param);
	}

	/**
	 * 운영단위 수정
	 * @param param
	 */
	public void updateOperationUnit(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateOperationUnit", param);
	}

	/**
	 * 운영단위 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationUnit(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationUnit", param);
	}

	/**
	 * 코드 표기를 위한 운영단위 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListOperationUnitForCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationUnitForCode", param);
	}

	/**
	 * 운영단위를 개수 조회 ( 조회 조건 운영단위 코드)
	 * @param param
	 * @return
	 */
	public int getCountOperationUnitByOperationUnitCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountOperationUnitByOperationUnitCode", param);
	}
}
