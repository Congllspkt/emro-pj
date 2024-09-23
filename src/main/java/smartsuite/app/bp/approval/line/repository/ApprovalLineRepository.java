package smartsuite.app.bp.approval.line.repository;

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
public class ApprovalLineRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "approval-line.";
	
	public void insertApprovalLine(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertApprovalLine", param);
	}


	public void updateApprovalLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalLine", param);
	}

	public void updateApprovalLineCurrentY(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalLineCurrentY", param);
	}

	public void updateApprovalLineCurrentN(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalLineCurrentN", param);
	}

	public void updateApprovalCurrentLineByParallelApprovalLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalCurrentLineByParallelApprovalLine", param);
	}

	public void updateApprovalNotCurrentLineByParallelApprovalLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalNotCurrentLineByParallelApprovalLine", param);
	}

	public void updateApprovalResultInfoByApprovalLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateApprovalResultInfoByApprovalLine", param);
	}

	public void updateApprovalOpinionByApprovalLine(Map<String, Object> approvalInfo) {
		sqlSession.update(NAMESPACE+"updateApprovalOpinionByApprovalLine", approvalInfo);
	}

	public void deleteApprovalLineByApprovalUuid(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteApprovalLineByApprovalUuid", param);
	}

	public void deleteApprovalLine(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"deleteApprovalLine", param);
	}

	public List<Map<String, Object>> findListApprovalLine(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListApprovalLine", param);
	}

	public Map<String, Object> findInfoDeputyApprovalUserInfo(Map<String, Object> lineInfo) {
		return sqlSession.selectOne(NAMESPACE+"findInfoDeputyApprovalUserInfo", lineInfo);
	}

	public int getCountParallelApprovalLineProgressStatus(Map<String, Object> lineInfo) {
		return sqlSession.selectOne(NAMESPACE+"getCountParallelApprovalLineProgressStatus", lineInfo);
	}

	public Map<String, Object> findApprovalLine(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findApprovalLine",param);
	}

	public List<Map<String, Object>> findListApprovalLineByApprovalLineSortOrd(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListApprovalLineByApprovalLineSortOrd", param);
	}

	public Map<String, Object> findMyApprovalLine(Map<String, Object> searchParam) {
		return sqlSession.selectOne(NAMESPACE+"findMyApprovalLine", searchParam);
	}

	public List<Map<String, Object>> findListApprovalLineForReSubmit(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListApprovalLineForReSubmit", param);
	}
	
	public int findLastApvdApvllnSort(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findLastApvdApvllnSort", param);
	}
	
	public boolean isCompleteCurrStepApprovalLine(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "isCompleteCurrStepApprovalLine", param);
	}
	
	public List<Map<String, Object>> findListNextApvllnApprover(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListNextApvllnApprover", param);
	}
	
	public void updateCurrApvrYn(Map<String, Object> nextApvllnApprover) {
		sqlSession.update(NAMESPACE + "updateCurrApvrYn", nextApvllnApprover);
	}
	
	public void deleteApprovalLineAfterApvllnSort(Map deleteParam) {
		sqlSession.delete(NAMESPACE + "deleteApprovalLineAfterApvllnSort", deleteParam);
	}
}
