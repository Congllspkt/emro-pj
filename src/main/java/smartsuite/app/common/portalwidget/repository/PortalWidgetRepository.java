package smartsuite.app.common.portalwidget.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PortalWidgetRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "portal-widget.";


	public List<Map<String, Object>> findListPoTotalAmountTreeByDate(Map<String, Object> paramMap) {
		return sqlSession.selectList(NAMESPACE + "findListPoTotalAmountTreeByDate",paramMap);
	}

	public Map<String, Object> findListPoTotalAmountTreeByYear(Map<String, Object> paramMap) {
		return sqlSession.selectOne(NAMESPACE + "findListPoTotalAmountTreeByYear",paramMap);
	}

	public List<Map<String, Object>> findNoticeBoardList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findNoticeBoardList", param);
	}

	public List<Map<String, Object>> findListMyTodo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListMyTodo", param);
	}

	public Integer findTodoSqlIdMapper(Map<String, Object> qryParams,String sqlId) {
		return sqlSession.selectOne(NAMESPACE + sqlId, qryParams);
	}

	public Map<String, Object> findTodoGroup(Map<String, Object> param) {
		return 	sqlSession.selectOne(NAMESPACE + "findTodoGroup", param);
	}

	public List<Map<String, Object>> findListExchange(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListExchange",param);
	}
}
