package smartsuite.app.common.workingday.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings({"rawtypes", "unchecked"})
public class WorkingdayRepository {

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "workingday.";

	public int findCntWorkingday(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCntWorkingday", param);
	}

	public List<Map<String, Object>> findListWorkingday(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListWorkingday", param);
	}

	public void insertInfoWorkingday(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertInfoWorkingday", param);
	}

	public void updateInfoWorkingday(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateInfoWorkingday", param);
	}

	public void deleteListWorkingday(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteListWorkingday", param);
	}

	public List<String> findListHolidayFromNow(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListHolidayFromNow", param);
	}
}
