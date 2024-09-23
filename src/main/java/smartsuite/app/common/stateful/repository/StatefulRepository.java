package smartsuite.app.common.stateful.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatefulRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "infra-stateful.";

	public String findCacheBustValue() {
		return sqlSession.selectOne(NAMESPACE+"findCacheBustValue");
	}

	public void insertCacheBust() {
		sqlSession.insert(NAMESPACE+"insertCacheBust", Long.toString(new Date().getTime()));
	}

	public void updateCacheBust() {
		sqlSession.update(NAMESPACE+"updateCacheBust", Long.toString(new Date().getTime()));
	}

	public List<Map<String, Object>> findUserPersonalizationClientData(String userPersonalizationDataUuid) {
		return sqlSession.selectList(NAMESPACE + "findUserPersonalizationClientData",userPersonalizationDataUuid);
	}

	public void insertUserPersonalizationClientData(Map<String, Object> state) {
		sqlSession.insert(NAMESPACE + "insertUserPersonalizationClientData", state);
	}

	public void updateUserPersonalizationClientData(Map<String, Object> state) {
		sqlSession.update(NAMESPACE + "updateUserPersonalizationClientData", state);
	}

	public void deleteUserPersonalizationClientData(Map<String, Object> state) {
		sqlSession.delete(NAMESPACE + "deleteUserPersonalizationClientData", state);
	}

	public void allDeleteUserPersonalizationClientData() {
		sqlSession.delete(NAMESPACE + "allDeleteUserPersonalizationClientData");
	}
}
