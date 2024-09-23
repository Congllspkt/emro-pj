package smartsuite.app.bp.approval.line.master.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ApprovalLineMasterRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "approval-line-manager.";


	public FloaterStream findApprovalLineMasterList(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE+"findApprovalLineMasterList",param);
	}

	public List findApprovalLineMasterDetailList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findApprovalLineMasterDetailList",param);
	}

	public void insertApprovalLineMaster(Map<String, Object> inserted) {
		sqlSession.insert(NAMESPACE+"insertApprovalLineMaster",inserted);
	}

	public void updateApprovalLineMaster(List<Map<String, Object>> updateList) {
		sqlSession.update(NAMESPACE+"updateApprovalLineMaster",updateList);
	}

	public void insertApprovalLineDetail(Map<String, Object> inserted) {
		sqlSession.insert(NAMESPACE+"insertApprovalLineDetail",inserted);
	}

	public void updateApprovalLineDetail(Map<String, Object> updated) {
		sqlSession.update(NAMESPACE+"updateApprovalLineDetail",updated);
	}
	
	public void deleteApprovalLineMasterInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalLineMasterInfo",param);
	}

	public void deleteApprovalLineMasterDetail(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalLineMasterDetail",param);
	}

	public void deleteApprovalLineMasterList(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalLineMasterList",param);
	}
	
	public List findListApprovalLineDetailForApproval(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalLineDetailForApproval", param);
	}
	
	public Object findListReferenceListDetailForApproval(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListReferenceListDetailForApproval", param);
	}
}
