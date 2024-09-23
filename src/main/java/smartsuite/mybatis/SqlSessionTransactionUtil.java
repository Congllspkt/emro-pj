package smartsuite.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class SqlSessionTransactionUtil {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/**
	 * Service 내에서 flush 처리를 위해 사용
	 */
	public void sqlSessionFlush(){
		sqlSession.flushStatements();
	}
}
