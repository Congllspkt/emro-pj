package smartsuite.app.common.portal.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PortletRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "portlet.";


	public List<Map<String, Object>> findListPortlet(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListPortlet", param);
	}

	public Map<String, Object> findInfoPortlet(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE +"findInfoPortlet", param);
	}

	public List<Map<String, Object>> findListPortletRoles(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListPortletRoles", param);
	}

	public void insertPortlet(Map<String, Object> portletInfo) {
		sqlSession.insert(NAMESPACE +"insertPortlet", portletInfo);
	}

	public void updatePortlet(Map<String, Object> portletInfo) {
		sqlSession.update(NAMESPACE +"updatePortlet", portletInfo);
	}

	public void insertPortletRole(Map<String,Object> portletRoleInfo) {
		sqlSession.insert(NAMESPACE +"insertPortletRole", portletRoleInfo);
	}

	public void deletePortletRoleByClassName(Map portletRoleInfo) {
		sqlSession.delete(NAMESPACE +"deletePortletRoleByClassName", portletRoleInfo);
	}

	public void deletePortlet(Map row) {
		sqlSession.delete(NAMESPACE +"deletePortlet", row);
	}

	public Map findDefaultLayout(Map param) {
		return sqlSession.selectOne(NAMESPACE +"findDefaultLayout", param);
	}

	public void insertDefaultLayout(Map param) {
		sqlSession.insert(NAMESPACE +"insertDefaultLayout", param);
	}

	public void updateDefaultLayout(Map param) {
		sqlSession.update(NAMESPACE +"updateDefaultLayout", param);
	}
}
