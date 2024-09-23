package smartsuite.app.bp.approval.link.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ApprovalLinkRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "approval-link.";

	public void updateApprovalLinkNotUsed(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalLinkNotUsed", param);
	}

	public void deleteApprovalLink(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalLink", param);
	}

	public String findTaskApprovalInfoUsingForApprovalUuid(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findTaskApprovalInfoUsingForApprovalUuid", param);
	}

	public void insertTaskAndApprovalLink(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertTaskAndApprovalLink", param);
	}

}
