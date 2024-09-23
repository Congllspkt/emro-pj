package smartsuite.app.bp.admin.auth.scheduler.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserSchedulerRepository {

	/** The namespace. */
	private static final String NAMESPACE = "account.";

	/** The sql session. */
	@Inject
	SqlSession sqlSession;



	/**
	 * 사용자 비활성화 처리
	 * @param disableTime
	 */
	public void accountDisable(Date disableTime){
		sqlSession.update(NAMESPACE+"accountDisable",disableTime);
	}

	public List<Map<String, Object>> findListDormantAccountForBuyer(Map<String, Object> param) {
		return 	sqlSession.selectList(NAMESPACE + "findListDormantAccountForBuyer",param);
	}
}
