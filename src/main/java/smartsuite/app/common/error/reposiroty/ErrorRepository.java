package smartsuite.app.common.error.reposiroty;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ErrorRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE =  "error.";


	public void insertErrorInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertErrorInfo", param);
	}

	public List findListError(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListError", param);
	}

	public Map findErrorInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findListError", param);
	}

	public void deleteError(Map<String, Object> deleteError) {
		sqlSession.delete(NAMESPACE + "deleteError", deleteError);
	}

	public void occurError(Map param) {
		sqlSession.selectOne(NAMESPACE + "occurError", param); //에러 발생 쿼리 호출
	}

	public void updateError(Map param) {
		sqlSession.update(NAMESPACE + "updateError", param);
	}
}
