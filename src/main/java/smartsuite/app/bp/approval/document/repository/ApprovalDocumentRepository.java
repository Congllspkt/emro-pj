package smartsuite.app.bp.approval.document.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ApprovalDocumentRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "approval-doc.";

	public void insertApprovalDocument(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertApprovalDocument", param);
	}

	public void updateApprovalDocument(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalDocument", param);
	}

	public void deleteApprovalDocument(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalDocument", param);
	}

	public Map<String, Object> findApprovalDocument(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findApprovalDocument", param);
	}

	public int getCountApprovalDocument(Map<String, Object> param) {
		return 	sqlSession.selectOne(NAMESPACE+"getCountApprovalDocument", param);
	}

	public Map<String, Object> findApprovalDocumentForReSubmit(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findApprovalDocumentForReSubmit", param);
	}
}
