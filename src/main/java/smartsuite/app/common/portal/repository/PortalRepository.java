package smartsuite.app.common.portal.repository;

import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PortalRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "portal.";

	public Map findUserLayout(Map param){
		return sqlSession.selectOne(NAMESPACE +"findUserLayout", param);
	}


	public Integer insertUserLayout(Map param){
		return sqlSession.insert(NAMESPACE +"insertUserLayout", param);
	}

	public Integer updateUserLayout(Map param){
		return sqlSession.insert(NAMESPACE +"updateUserLayout", param);
	}

	public Integer deleteUserLayout(Map param){
		return sqlSession.insert(NAMESPACE +"deleteUserLayout", param);
	}


	public List<Map> findComposableWidgetList(Map param){
		return sqlSession.selectList(NAMESPACE +"findComposableWidgetList", param);
	}

	public Map findPortalCommonConfig(Map param) {
		return sqlSession.selectOne(NAMESPACE +"findPortalCommonConfig", param);
	}

	public Integer savePortalCommonConfig(Map param) {
		return sqlSession.insert(NAMESPACE +"savePortalCommonConfig", param);
	}

	public Integer updatePortalCommonConfig(Map param) {
		return sqlSession.update(NAMESPACE +"updatePortalCommonConfig", param);
	}

}
