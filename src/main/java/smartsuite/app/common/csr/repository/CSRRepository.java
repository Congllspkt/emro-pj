package smartsuite.app.common.csr.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CSRRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE =  "csr.";

	public List<Map<String, Object>> findListCSRInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListCSRInfo", param);
	}

	public List<Map<String, Object>> findListCSRReceipt(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListCSRReceipt", param);
	}

	public void deleteCSRInfo(String csrId) {
		sqlSession.delete(NAMESPACE + "deleteCSRInfo", csrId);
	}

	public void insertProcessHistoryRecordInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertProcessHistoryRecordInfo", param);
	}

	public void updateProcessHistoryRecordInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "updateProcessHistoryRecordInfo", param);
	}

	public void updateCSRPersonInCharge(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateCSRPersonInCharge", param);
	}

	public List<Map<String, Object>> findListCSRProcessingInfo(String csrId) {
		return sqlSession.selectList(NAMESPACE + "findListCSRProcessingInfo", csrId);
	}

	public void updateCSRStatus(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateCSRStatus", param);
	}

	public String getCSRProgressStatus(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE +"getCSRProgressStatus", param);
	}

	public void insertCSRInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertCSRInfo", param);
	}

	public void updateCSRInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateCSRInfo", param);
	}

	public Map<String, Object> selectCSRInfo(String csrId) {
		return sqlSession.selectOne(NAMESPACE +"selectCSRInfo", csrId);
	}
}
