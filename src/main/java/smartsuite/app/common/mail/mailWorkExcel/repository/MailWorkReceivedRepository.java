package smartsuite.app.common.mail.mailWorkExcel.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MailWorkReceivedRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE =  "mail-work-received.";

	public Map<String, Object> findLastReceivedMail(HashMap<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findLastReceivedMail", param);
	}

	public List<Map<String, Object>> findNotProcessedMailWork(HashMap<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findNotProcessedMailWork", param);
	}

	public void updateEmailWorkReceivedStatus(Map<String, Object> mailParam) {
		sqlSession.update(NAMESPACE + "updateEmailWorkReceivedStatus", mailParam);
	}

	public List<Map<String, Object>> findExcelReceiveMailById(Map<String, Object> mailConnect) {
		return sqlSession.selectList( NAMESPACE + "findExcelReceiveMailById", mailConnect);
	}

	public void updateRepliedMail(Map<String, Object> mailConnect) {
		sqlSession.update(NAMESPACE + "updateRepliedMail", mailConnect);
	}

	public void insertReceivedMail(Map newMailMap) {
		sqlSession.insert( NAMESPACE +"insertReceivedMail", newMailMap);
	}

	public void deleteEmailWorkReceivedMailList(Map<String, Object> emailWorkProcMap) {
		sqlSession.delete(NAMESPACE + "deleteEmailWorkReceivedMailList",emailWorkProcMap);
	}

	public List<Map<String, Object>> findLastReceivedMailCheck(Map newMailMap) {
		return sqlSession.selectList(NAMESPACE + "findLastReceivedMailCheck",newMailMap);
	}
}
