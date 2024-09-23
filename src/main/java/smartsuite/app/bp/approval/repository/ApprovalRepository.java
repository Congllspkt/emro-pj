package smartsuite.app.bp.approval.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ApprovalRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "approval-master.";

	public FloaterStream findListApprovalMasterForSubmitAndApproval(Map<String, Object> param) {
		// 대용량 처리
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListApprovalMasterForSubmitAndApproval", param);
	}

	public void insertApprovalMaster(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertApprovalMaster", param);
	}

	public void updateApprovalMaster(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalMaster", param);
	}

	public void updateApprovalMasterStatus(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalMasterStatus", param);
	}

	public void deleteApprovalMaster(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "deleteApprovalMaster", param);
	}

	public Map<String, Object> findApprovalMaster(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalMaster", param);
	}

	public Map<String, Object> findApprovedErrorContext(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovedErrorContext", param);
	}

	public void updateApprovalErrorCodeAndMessage(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalErrorCodeAndMessage", param);
	}

	public Map<String, Object> selectDeputyApprovalUuid(Map<String, Object> info) {
		return sqlSession.selectOne(NAMESPACE + "selectDeputyApprovalUuid", info);
	}

	public Map<String, Object> findApprovalMasterForReSubmit(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalMasterForReSubmit", param);
	}

	public FloaterStream findListDeputyApproval(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListDeputyApproval", param);
	}

	public Integer getCountDeputyApprovalInfoEither(Map<String, Object> deputyApprovalInfo) {
		return 	sqlSession.selectOne(NAMESPACE + "getCountDeputyApprovalInfoEither", deputyApprovalInfo);
	}

	public Integer getCountDeputyApprovalInfoBoth(Map<String, Object> deputyApprovalInfo) {
		return 	sqlSession.selectOne(NAMESPACE + "getCountDeputyApprovalInfoBoth", deputyApprovalInfo);
	}

	public void insertDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		sqlSession.insert(NAMESPACE + "insertDeputyApprovalInfo", deputyApprovalInfo);
	}
	public void updateDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		sqlSession.update(NAMESPACE + "updateDeputyApprovalInfo", deputyApprovalInfo);
	}

	public void deleteDeputyApprovalInfo(Map<String, Object> deputyApprovalInfo) {
		sqlSession.update(NAMESPACE + "deleteDeputyApprovalInfo", deputyApprovalInfo);
	}

	public FloaterStream findListDeputyApprovalCount(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListDeputyApprovalCount", param);
	}

	public Integer getMaxRevisionApprovalMaster(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getMaxRevisionApprovalMaster", param);
	}
	
	public FloaterStream findListApprovalReceipt(Map param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListApprovalReceipt", param);
	}
	
	public List<String> findDptyApproverIds(Map param) {
		return sqlSession.selectList(NAMESPACE + "findDptyApproverIds", param);
	}
}
