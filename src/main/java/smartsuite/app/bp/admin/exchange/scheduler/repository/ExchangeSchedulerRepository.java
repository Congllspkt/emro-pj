package smartsuite.app.bp.admin.exchange.scheduler.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ExchangeSchedulerRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;


	/** The Constant NAMESPACE. */
	private static final String NAMESPACE = "exchange.";


	/**
	 * 환율API등록
	 * @param apiDataMap
	 */
	public void insertExchangeKrApi(Map<String, Object> apiDataMap) {
		sqlSession.insert(NAMESPACE + "insertExchangeKrApi",apiDataMap);
	}
}



