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
public class SupplierToDoManagerRepository {

	@Inject
	SqlSession sqlSession;

	private static final String NAMESPACE = "supplier-todo-manager.";

	/**
	 * 할일 항목 별 협력사 조회
	 *
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUserSupplier(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTodoUserSupplier", param);
	}
}
