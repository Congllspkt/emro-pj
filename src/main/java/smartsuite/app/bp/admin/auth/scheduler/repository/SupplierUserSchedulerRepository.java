package smartsuite.app.bp.admin.auth.scheduler.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SupplierUserSchedulerRepository {

	/** The namespace. */
	private static final String NAMESPACE = "supplier-account.";

	/** The sql session. */
	@Inject
	SqlSession sqlSession;

	/**
	 * 사용자의 현재 로그인 만료 기간을 조회한다..
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListDormantAccountForAllUser(Map<String,Object> param){
		return 	sqlSession.selectList(NAMESPACE + "findListDormantAccountForAllUser",param);
	}

	public List<Map<String,Object>> findListDormantAccountForSupplier(Map<String,Object> param){
		return 	sqlSession.selectList(NAMESPACE + "findListDormantAccountForSupplier",param);
	}
}
