package smartsuite.app.bp.admin.mailManager.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.mailManager.repository.MailManagerRepository;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.mail.data.MailResult;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MailManagerService {
	
	@Inject
	MailService mailService;
	
	@Inject
	MailManagerRepository mailManagerRepository;
	
	/* 메일 목록 조회 */
	public List<Map<String, Object>> findListMail(Map<String, Object> param) {
		Locale locale = LocaleContextHolder.getLocale();
		String localeString = "ko_KR";
		if(null != locale){
			localeString = locale.toString();
		}
		String LangCode = param.get("lang_ccd") == null? localeString : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);
		return mailManagerRepository.findListMail(param);
	}
	
	/* 메일 목록 상세 조회 */
	public Map findListMailTemplate(Map param) {
		String LangCode = param.get("lang_ccd") == null? "" : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);
		if(this.existMailTemplateMultiLang(param)){
			return mailManagerRepository.findListMailMultiLangTemplate(param);
		}else{
			return mailManagerRepository.findListMailTemplate(param);
		}
	}

	public Map findMailMultiLangTemplateInfo(Map param) {
		return mailManagerRepository.findMailMultiLangTemplateInfo(param);
	}

	/* 메일 목록 삭제 요청*/
	public ResultMap deleteListMailRequest(Map<String, Object> param) {
		List <Map<String, Object>> deleteMailList = (List<Map<String, Object>>)param.get("deleteMail");

		//메일 목록 삭제
		this.deleteListMail(deleteMailList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 메일 목록 삭제
	 * @param deleteMailList
	 */
	public void deleteListMail(List<Map<String, Object>> deleteMailList) {
		for(Map<String, Object> deleteMail : deleteMailList){
			this.deleteMail(deleteMail);
		}
	}

	/* 메일 삭제 */
	public void deleteMail(Map<String, Object> param){
		//유효성체크
		String invalidMailTemplateId = this.findMailSetForTemplateIdByMailSetupId(param);

		if(StringUtils.isNotEmpty(invalidMailTemplateId)){
			param.put("ctmpl_uuid", invalidMailTemplateId);
			if(this.existFindMailHistory(param)){
				this.updateMailSetupStatusDeleteByMailSetupId(param);
				this.updateMailTemplateStatusDeleteByTemplateId(param);
			}else{
				this.deleteMailSetupByMailSetId(param);
				this.deleteMailMultlangTemplateByTemplateId(param);
				this.deleteMailTemplateByTemplateId(param);
			}
		}
	}
	
	private void deleteMailMultlangTemplateByTemplateId(Map<String, Object> param) {
		mailManagerRepository.deleteMailMultlangTemplateByTemplateId(param);
	}
	
	/**
	 * 메일 템플릿 정보 삭제 ( 조회 조건 template id ) / 물리삭제
	 * @param param
	 */
	public void deleteMailTemplateByTemplateId(Map<String, Object> param) {
		mailManagerRepository.deleteMailTemplateByTemplateId(param);
	}

	/**
	 * 메일 설정 정보 삭제 ( 조회 조건 mail set id ) / 물리 삭제
	 * @param param
	 */
	public void deleteMailSetupByMailSetId(Map<String, Object> param) {
		mailManagerRepository.deleteMailSetupByMailSetId(param);
	}

	/**
	 * 메일 템플릿 삭제 상태 업데이트 ( 조회 조건 template ID )
	 * @param param
	 */
	public void updateMailTemplateStatusDeleteByTemplateId(Map<String, Object> param) {
		mailManagerRepository.updateMailTemplateStatusDeleteByTemplateId(param);
	}

	/**
	 * 메일 삭제 상태 업데이트 ( 조회 조건 mail set id )
	 * @param param
	 */
	public void updateMailSetupStatusDeleteByMailSetupId(Map<String, Object> param) {
		mailManagerRepository.updateMailSetupStatusDeleteByMailSetupId(param);
	}

	/**
	 * 메일 이력 내에 존재여부 확인
	 * @param param
	 * @return
	 */
	public boolean existFindMailHistory(Map<String, Object> param) {
		int historyCount = mailManagerRepository.findCountMailHistory(param);
		return (historyCount > 0);
	}

	/**
	 * 메일 설정 내에 존재하는 TemplateId 찾기 ( 조회 조건 mail set id )
	 * @param param
	 * @return
	 */
	public String findMailSetForTemplateIdByMailSetupId(Map<String, Object> param) {
		return mailManagerRepository.findMailSetForTemplateIdByMailSetupId(param);
	}

	/* 저장 */
	public ResultMap saveListMail(Map<String, Object> mailInfo) {
		boolean isNew = (Boolean)mailInfo.getOrDefault("isNew" , false);

		if(MapUtils.isEmpty(mailInfo)){
			throw new CommonException("저장하려는 메일 정보가 존재하지 않습니다.");
		}

		String LangCode = mailInfo.get("lang_ccd") == null? "ko_KR" : (String) mailInfo.get("lang_ccd");
		mailInfo.put("lang_ccd",LangCode);
		
		// 신규 저장
		if(isNew){
			// 메일 템플릿 존재여부
			if(this.existMailTemplate(mailInfo)) throw new CommonException(ErrorCode.DUPLICATED);
			mailInfo.put("ctmpl_uuid", UUID.randomUUID().toString());

			// 메일 템플릿 저장
			this.insertMailTemplate(mailInfo);
			this.insertMailTemplateMultiLang(mailInfo);

			// 메일 설정 정보 저장
			this.insertMailSetup(mailInfo);
		}
		else{
			// 메일 설정 정보 수정
			this.updateMailSetup(mailInfo);

			// 메일 템플릿 정보 수정
			this.updateMailTemplate(mailInfo);

			// 메일 템플릿 다국어 정보가 존재할 경우 수정 , 존재하지 않으면 추가
			if(this.existMailTemplateMultiLang(mailInfo)){
				this.updateMailTemplateMultiLang(mailInfo);
			}else{
				this.insertMailTemplateMultiLang(mailInfo);
			}
		}
		return ResultMap.SUCCESS();
	}

	private void updateMailTemplateMultiLang(Map<String, Object> mailInfo) {
		mailManagerRepository.updateMailTemplateMultiLang(mailInfo);
	}

	private void insertMailTemplateMultiLang(Map<String, Object> mailInfo) {
		mailManagerRepository.insertMailTemplateMultiLang(mailInfo);
	}

	/**
	 * 메일 템플릿 수정
	 * @param mailInfo
	 */
	private void updateMailTemplate(Map<String, Object> mailInfo) {
		mailManagerRepository.updateMailTemplate(mailInfo);
	}

	/**
	 * 메일 설정정보 수정
	 * @param mailInfo
	 */
	private void updateMailSetup(Map<String, Object> mailInfo) {
		mailManagerRepository.updateMailSetup(mailInfo);
	}

	/**
	 * 메일 설정정보 추가
	 * @param mailInfo
	 */
	public void insertMailSetup(Map<String, Object> mailInfo) {
		mailManagerRepository.insertMailSetup(mailInfo);
	}

	/**
	 * 메일 템플릿 추가
	 * @param mailInfo
	 */
	private void insertMailTemplate(Map<String, Object> mailInfo) {
		mailManagerRepository.insertMailTemplate(mailInfo);
	}

	/**
	 * 메일 템플릿 존재 여부 체크
	 * @param param
	 * @return
	 */
	public boolean existMailTemplate(Map<String, Object> param) {
		int countMailTemplate = mailManagerRepository.findCountMailTemplate(param);
		return (countMailTemplate > 0);
	}
	/**
	 * 메일 다국어 템플릿 존재 여부 체크
	 * @param param
	 * @return
	 */
	public boolean existMailTemplateMultiLang(Map<String, Object> param) {
		int countMailTemplate = mailManagerRepository.findCountMailTemplateMultiLang(param);
		return (countMailTemplate > 0);
	}

	/**
	 * 이메일 양식 구분 템플릿 조회
	 * @param param
	 * @return
	 */
	public List findListEmailTemplate(Map<String, Object> param) {
		return mailManagerRepository.findListEmailTemplate(param);
	}

	/**
	 * 메일 전송 이력 조회
	 * @param param
	 * @return
	 */
	public  List<Map<String, Object>> findListMailSendHistory(Map<String, Object> param) {
		return mailManagerRepository.findListMailSendHistory(param);
	}

	/**
	 * SMTP 메일 전송 테스트 & 메일 발송
	 * @param param
	 * @return
	 */
	public ResultMap smtpSyncTestSendMail(Map param) {
		String mailKey = param.getOrDefault("mailKey","") == null ? "":(String) param.getOrDefault("mailKey","");
		if(StringUtils.isNotEmpty(mailKey)){
			mailService.test(mailKey, param);
		}else {
			throw new CommonException("전송하려는 메일 Key가 존재하지 않습니다.");
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 메일 다중 전송이력 조회 ( cc/bcc/to 등 일괄 전송에 대한 )
	 * @param param
	 * @return
	 */
	public  List<Map<String, Object>> findMultiMailSendHistory(Map<String, Object> param) {
		return mailManagerRepository.findMultiMailSendHistory(param);
	}
	
	public Map findMultiMailSendHistoryDetail(Map param) {
		return mailManagerRepository.findMultiMailSendHistoryDetail(param);
	}
	
	public ResultMap sendByMailHistrec(Map param) {
		List<Map<String, Object>> selected = (List<Map<String, Object>>) param.get("selected");

		int checkSuccessCount = 0;
		for(Map<String, Object> item : selected) {
			MailResult mailResult = mailService.sendByMailHistrec((String) item.get("eml_snd_uuid"));
			if(!mailResult.isSuccess()) checkSuccessCount++;

		}

		ResultMap resultMap = ResultMap.getInstance();
		if(checkSuccessCount > 0){
			resultMap.setResultMessage("선택한 메일 발송 "+selected.size() + " 건 중 실패한 건 수가 "+ checkSuccessCount + " 건 입니다.");
			resultMap.setResultStatus(ResultMap.STATUS.FAIL);
			return resultMap;
		}else{
			return ResultMap.SUCCESS();
		}
	}
	
	/**
	 * 발신 유형 배치 인 경우
	 * @param param
	 */
	public void batchMail(HashMap<String, Object> param) {
		List<Map<String, Object>> mailList = mailManagerRepository.findListUndeliveredMail(param);
		for(Map<String, Object> mailInfo : mailList) {
			mailService.sendByMailHistrec((String) mailInfo.get("eml_snd_uuid"));
		}
	}
	
	public List searchReceiptSubjectEmail(Map param) {
		return mailManagerRepository.searchReceiptSubjectEmail(param);
	}
	
	public void updateExceptEmail(Map<String, Object> param) {
		List<Map<String, Object>> updateItems = (List<Map<String, Object>>) param.get("updateItems");
		mailManagerRepository.updateExceptEmail(updateItems);
	}
}
