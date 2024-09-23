package smartsuite.app.bp.approval.arbitrary.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 전결결재선 관련 처리하는 서비스 Class입니다.
 *
 * @Since 2019.02.20
 */
@Service
@Transactional
public class ArbitraryDataSourceService {

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "arbitrary-datasrc.";
	
	/**
	 * 전결결재선 목록을 조회한다.
	 *
	 */
	public List<Map<String, Object>> selectListDataSource(Map<String, Object> param) {
		String sqlId = (String)param.get("sql_id");
		return sqlSession.selectList(NAMESPACE + sqlId, param);
	}
	
}
