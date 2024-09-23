package smartsuite.app.bp.approval.line.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class ApprovalLineCcRepository {
	
	@Inject
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "approval-line-cc.";
	
	public void deleteApprovalLineCcByApprovalUuid(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteApprovalLineCcByApprovalUuid", param);
	}
	
	public void insertApprovalLineCc(Map<String, Object> row) {
		sqlSession.insert(NAMESPACE + "insertApprovalLineCc", row);
	}
	
	public List<Map<String, Object>> findListApprovalLineCc(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalLineCc", param);
	}
	
	public List<Map<String, Object>> findListApprovalLineCcForReSubmit(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalLineCcForReSubmit", param);
	}
	
	public void updateApprovalLineCc(Map<String, Object> row) {
		sqlSession.update(NAMESPACE + "updateApprovalLineCc", row);
	}
	
	public void removeApprovalLineCc(Map<String, Object> row) {
		sqlSession.delete(NAMESPACE + "removeApprovalLineCc", row);
	}
}
