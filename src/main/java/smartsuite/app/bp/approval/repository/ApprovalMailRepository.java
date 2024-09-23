package smartsuite.app.bp.approval.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.mail.data.TemplateMailData;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class ApprovalMailRepository {
	
	@Inject
	SqlSession sqlSession;
	
	private static final String NAMESPACE = "approval-mail.";
	
	public Map findApprovalInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalInfo", param);
	}
	
	public List<TemplateMailData.Receiver> findListApprovalRdgLine(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalRdgLine", param);
	}
	
	public TemplateMailData.Receiver findApprovalDrafterInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalDrafterInfo", param);
	}
	
	public Map findApprovalReturnInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalReturnInfo", param);
	}
	
	public List<TemplateMailData.Receiver> findListApprovalNextApprover(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalNextApprover", param);
	}
	
	public List<TemplateMailData.Receiver> findListWtgApproverByApvllnSort(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListWtgApproverByApvllnSort", param);
	}
}
