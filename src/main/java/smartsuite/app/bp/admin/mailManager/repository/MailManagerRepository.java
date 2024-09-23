package smartsuite.app.bp.admin.mailManager.repository;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.mail.data.TemplateMailData;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MailManagerRepository {

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "mail-manager.";

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	
	public Map<String, Object> findListMailTemplate(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findListMailTemplate", param);
	}
	
	public List<Map<String, Object>> findListSendMultiMail(HashMap<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListSendMultiMail", param);
	}
	
	public void updateEmailMultiSendingComplete(Map<String, Object> mailParam) {
		sqlSession.update(NAMESPACE + "updateEmailMultiSendingComplete", mailParam);
	}
	
	public void insertMultiMailInfo(Map<String, Object> history) {
		sqlSession.insert(NAMESPACE + "insertMultiMailInfo", history);
	}

	/**
	 * 메일 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMail(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListMail", param);
	}

	/**
	 * 메일 리스트 조회 ( 조건 mail set id )
	 * @param param
	 * @return
	 */
	public Map findListMailMultiLangTemplate(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findListMailMultiLangTemplate", param);
	}


	/**
	 * 메일 설정 내에 존재하는 TemplateId 찾기 ( 조회 조건 mail set id )
	 * @param param
	 * @return
	 */
	public String findMailSetForTemplateIdByMailSetupId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findMailSetForTemplateIdByMailSetupId", param);
	}

	/**
	 * 메일 이력 내에 존재하는 메일 개수 가져오기
	 * @param param
	 * @return
	 */
	public int findCountMailHistory(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findCountMailHistory",param);
	}

	/**
	 * 메일 삭제 상태 업데이트 ( 조회 조건 mail set id )
	 * @param param
	 */
	public void updateMailSetupStatusDeleteByMailSetupId(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateMailSetupStatusDeleteByMailSetupId",param);
	}

	/**
	 * 메일 템플릿 삭제 상태 업데이트 ( 조회 조건 template ID )
	 * @param param
	 */
	public void updateMailTemplateStatusDeleteByTemplateId(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateMailTemplateStatusDeleteByTemplateId", param);
	}

	/**
	 * 메일 설정 정보 삭제 ( 조회 조건 mail set id ) / 물리 삭제
	 * @param param
	 */
	public void deleteMailSetupByMailSetId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteMailSetupByMailSetId", param);
	}

	/**
	 * 메일 템플릿 정보 삭제 ( 조회 조건 template id ) / 물리삭제
	 * @param param
	 */
	public void deleteMailTemplateByTemplateId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteMailTemplateByTemplateId", param);
	}

	/**
	 * 메일 템플릿 개수 조회
	 * @param param
	 * @return
	 */
	public int findCountMailTemplate(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCountMailTemplate", param);
	}

	/**
	 * 메일 설정 정보 추가
	 * @param mailInfo
	 */
	public void insertMailSetup(Map<String, Object> mailInfo) {
		sqlSession.insert(NAMESPACE + "insertMailSetup", mailInfo);
	}

	/**
	 * 메일 템플릿 추가
	 * @param mailInfo
	 */
	public void insertMailTemplate(Map<String, Object> mailInfo) {
		sqlSession.insert(NAMESPACE + "insertMailTemplate", mailInfo);
	}

	/**
	 * 메일 템플릿 수정
	 * @param mailInfo
	 */
	public void updateMailTemplate(Map<String, Object> mailInfo) {
		sqlSession.update(NAMESPACE + "updateMailTemplate", mailInfo);
	}

	/**
	 * 메일 설정 정보 수정
	 * @param mailInfo
	 */
	public void updateMailSetup(Map<String, Object> mailInfo) {
		sqlSession.update(NAMESPACE + "updateMailSetup", mailInfo);
	}

	/**
	 * 이메일 양식 구분 템플릿 조회
	 * @param param
	 * @return
	 */
	public List findListEmailTemplate(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListEmailTemplate", param);
	}

	/**
	 * 메일 전송 이력 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMailSendHistory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListMailSendHistory", param);
	}

	public void insertMailTemplateMultiLang(Map<String, Object> mailInfo) {
		sqlSession.insert(NAMESPACE + "insertMailTemplateMultiLang", mailInfo);
	}

	public void updateMailTemplateMultiLang(Map<String, Object> mailInfo) {
		sqlSession.update(NAMESPACE + "updateMailTemplateMultiLang", mailInfo);
	}

	public int findCountMailTemplateMultiLang(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCountMailTemplateMultiLang", param);
	}

	public Map findMailMultiLangTemplateInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findMailMultiLangTemplateInfo", param);
	}
	
	/**
	 * 메일 다중 전송이력 조회 ( cc/bcc/to 등 일괄 전송에 대한 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findMultiMailSendHistory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findMultiMailSendHistory", param);
	}
	
	public Map findMultiMailSendHistoryDetail(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findMultiMailSendHistoryDetail", param);
	}
	
	public List<Map<String, Object>> findListUndeliveredMail(HashMap<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListUndeliveredMail", param);
	}
	
	public void deleteMailMultlangTemplateByTemplateId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteMailMultlangTemplateByTemplateId", param);
	}
	
	public List<Map<String, Object>> searchReceiptSubjectEmail(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "searchReceiptSubjectEmail", param);
	}
	
	public void updateExceptEmail(List<Map<String, Object>> updateItems) {
		for(Map updateItem : updateItems) {
			String xceptYn = (String) updateItem.get("xcept_yn");
			
			if("Y".equals(xceptYn)) {
				sqlSession.insert(NAMESPACE + "insertExceptEmail", updateItem);
			} else {
				sqlSession.delete(NAMESPACE + "deleteExceptEmail", updateItem);
			}
		}
	}
	
	public String insertSendMailInfoListTempTable(String emailTemplateCommonCode, List<TemplateMailData.Receiver> receivers) {
		Map<String, Object> insertMap = Maps.newHashMap();
		int idx = 0;
		String tempId = UUID.randomUUID().toString();
		
		insertMap.put("temp_id", tempId);
		insertMap.put("eml_tmpl_cd", emailTemplateCommonCode);
		
		for(TemplateMailData.Receiver receiver : receivers){
			if(StringUtils.isNotBlank(receiver.getAddress())){
				insertMap.put("temp_seq", idx++);
				insertMap.put("receiver", receiver);
				this.insertSendMailInfoTempTable(insertMap);
			}
		}
		
		return tempId;
	}
	
	public void insertSendMailInfoTempTable(Map<String, Object> insertMap) {
		sqlSession.insert(NAMESPACE + "insertSendMailInfoListTempTable", insertMap);
		
	}
	
	public void deleteSendMailInfoListTempTable(String tempId) {
		sqlSession.delete(NAMESPACE +"deleteSendMailInfoListTempTable", tempId);
	}
	
	public List<TemplateMailData.Receiver> searchFilterReceiver(String tempId) {
		return sqlSession.selectList(NAMESPACE + "searchFilterReceiver", tempId);
	}
}
