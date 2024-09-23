package smartsuite.app.bp.admin.template.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.template.repository.TemplateRepository;
import smartsuite.app.bp.admin.terms.service.TermsService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;


@Service
@Transactional
@SuppressWarnings ({ "unchecked", "rawtypes"})
public class TemplateService {

	@Inject
	TemplateRepository templateRepository;

	@Inject
	TermsService termsService;

	/**
	 * 템플릿 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTemplate(Map<String, Object> param) {
		return templateRepository.findListTemplate(param);
	}

	/**
	 * 템플리 정보 조회 ( 조회조건 템플릿 기본 아이디 & 템플릿 구분 )
	 * @param templateData
	 * @return
	 */
	public Map findTemplateInfoByTemplateClassAndTemplateBaseId(Map<String, Object> templateData) {
		return templateRepository.findTemplateInfoByTemplateClassAndTemplateBaseId(templateData);
	}

	/**
	 * 템플릿 목록 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListTemplateRequest(Map<String, Object> param) {
		List <Map<String, Object>> deleteTemplateList = (List<Map<String, Object>>)param.get("deleteTemplates");
		//템플릿 목록 삭제
		this.deleteListTemplate(deleteTemplateList);
		return ResultMap.SUCCESS();
	}

	/**
	 * 템플릿 목록 삭제
	 * @param deleteTemplateList
	 */
	public void deleteListTemplate(List<Map<String, Object>> deleteTemplateList) {
		for(Map<String, Object> row : deleteTemplateList){
			this.deleteTemplateInfo(row);
		}
	}

	/**
	 * 템플릿 삭제
	 * @param param
	 */
	public void deleteTemplateInfo(Map<String, Object> param) {

		// 템플릿 삭제 시 메일 양식 설정 삭제
		this.deleteMailTemplateInfo(param);
		
		// 담당자 동의 약관 삭제
		termsService.deleteChargeTermsAgreeInfoByTemplateBaseId(param);

		// 이용 약관 마스터 삭제
		termsService.deleteTermsMasterByTemplateBaseId(param);

		//템플릿 삭제 시 템플릿 본문내용 삭제 ( 조회조건 template base id )
		this.deleteListTemplateContentByTemplateBaseId(param);

		//템플릿 다국어 정보 삭제
		this.deleteListTemplateBaseMultiLangInfo(param);
		
		//템플릿 기초 정보 삭제
		this.deleteTemplateBaseInfo(param);

	}

	public void deleteListTemplateBaseMultiLangInfo(Map<String, Object> param) {
		templateRepository.deleteListTemplateBaseMultiLangInfo(param);
	}

	/**
	 * 템플릿 기초 정보 삭제
	 * @param param
	 */
	public void deleteTemplateBaseInfo(Map<String, Object> param) {
		templateRepository.deleteTemplateBaseInfo(param);
	}


	/**
	 * 템플릿 삭제 시 템플릿 본문내용 삭제 ( 조회조건 template base id )
	 * @param param
	 */
	public void deleteListTemplateContentByTemplateBaseId(Map<String, Object> param) {
		templateRepository.deleteListTemplateContentByTemplateBaseId(param);
	}


	/**
	 *템플릿 저장*/
	@Transactional
	public ResultMap saveTemplateBaseInfo(Map<String, Object> param) {
		boolean isNew = param.getOrDefault("isNew",false) == null? false : (boolean) param.getOrDefault("isNew",false);

		String LangCode = param.get("lang_ccd") == null? "ko_KR" : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);

		// 신규
		if (isNew) {
			if (this.existTemplateBaseInfo(param)) {
				throw new CommonException(ErrorCode.DUPLICATED);
			}else{
				// 템플릿 기초 정보 추가
				this.insertTemplateBaseInfo(param);
				this.insertTemplateBaseInfoMultiLang(param);
			}
		} else {

			if(this.existTemplateBaseMultiLangInfo(param)){
				this.updateTemplateBaseInfoMultiLang(param);
			}else{
				this.insertTemplateBaseInfoMultiLang(param);
			}
			// 템플릿 기초 정보 수정
			this.updateTemplateBaseInfo(param);
		}
		return ResultMap.SUCCESS();
	}

	private void updateTemplateBaseInfoMultiLang(Map<String, Object> param) {
		templateRepository.updateTemplateBaseInfoMultiLang(param);
	}

	private void insertTemplateBaseInfoMultiLang(Map<String, Object> param) {
		templateRepository.insertTemplateBaseInfoMultiLang(param);
	}

	/**
	 * 템플릿 기초 정보 수정
	 * @param param
	 */
	public void updateTemplateBaseInfo(Map<String, Object> param) {
		templateRepository.updateTemplateBaseInfo(param);
	}

	/**
	 * 템플릿 기초 정보 추가
	 * @param param
	 */
	public void insertTemplateBaseInfo(Map<String, Object> param) {
		templateRepository.insertTemplateBaseInfo(param);
	}

	/**
	 * 템플릿 기초 존재여부 확인
	 * @param param
	 * @return
	 */
	public boolean existTemplateBaseInfo(Map<String, Object> param) {
		return (templateRepository.getCountTemplateBaseInfo(param) > 0);
	}
	public boolean existTemplateBaseMultiLangInfo(Map<String, Object> param) {
		return (templateRepository.getCountTemplateBaseMultiLangInfo(param) > 0);
	}

	/**
	 * 메일 설정 키를 사용하는 템플릿 본문 정보 검색
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTemplateContentByMailSetKey(String mailKey) {
		return templateRepository.findTemplateContentByMailSetKey(mailKey);
	}

	/**
	 * 템플릿 기초정보 조회 ( 조회조건 template base id )
	 * @param param
	 * @return
	 */
	public Map findTemplateBaseInfoByTemplateBaseId(Map param) {
		return templateRepository.findTemplateBaseInfoByTemplateBaseId(param);
	}


	/**
	 * 결제 구분 코드를 사용하는 템플릿 본문 정보 검색
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTemplateContentByApprovalTypeCode(Map<String, Object> param) {
		String LangCode = param.get("lang_ccd") == null? "ko_KR" : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);
		return templateRepository.findTemplateContentByApprovalTypeCode(param);
	}


	/**
	 * list approval tmp 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2017. 2. 20
	 * @Method Name : findListApprovalTemplate
	 */
	/* 결재 템플릿 목록 조회 */
	public List<Map<String, Object>> findListApprovalTemplate(Map<String, Object> param) {
		return templateRepository.findListApprovalTemplate(param);
	}

	/**
	 * 결재 템플릿 상세 조회 ( 조회 조건 결재구분코드)
	 * @param param
	 * @return
	 */
	public Map<String, Object> findApprovalTemplateByApprovalTypeCode(Map<String, Object> param) {
		if(param.get("lang_ccd") == null) {
			param.put("lang_ccd", LocaleContextHolder.getLocale());
		}
		return templateRepository.findApprovalTemplateByApprovalTypeCode(param);
	}

	/**
	 * approval tmp 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2017. 2. 20
	 * @Method Name : saveApprovalTemplate
	 */
	/* 결재 템플릿 저장 */
	public ResultMap saveApprovalTemplate(Map<String, Object> param) {
		String LangCode = param.get("lang_ccd") == null? "ko_KR" : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);

		if(this.existApproval(param)) { //수정
			this.updateApprovalSetForTemplate(param);
			this.updateApprovalTemplateContent(param);

			if(this.existApprovalMultiLangTemplate(param)){
				this.updateApprovalTemplateContentMultiLang(param);
			}else{
				this.insertApprovalTemplateContentMultiLang(param);
			}
		} else { //신규
			param.put("ctmpl_uuid", UUID.randomUUID().toString());
			this.insertApprovalTemplateContent(param);
			this.insertApprovalSetForTemplate(param);
			this.insertApprovalTemplateContentMultiLang(param);
		}
		return ResultMap.SUCCESS();
	}

	private void insertApprovalTemplateContentMultiLang(Map<String, Object> param) {
		templateRepository.insertApprovalTemplateContentMultiLang(param);
	}

	private void updateApprovalTemplateContentMultiLang(Map<String, Object> param) {
		templateRepository.updateApprovalTemplateContentMultiLang(param);
	}

	/**
	 * 템플릿 결재 연결 설정 등록
	 * @param param
	 */
	public void insertApprovalSetForTemplate(Map<String, Object> param) {
		templateRepository.insertApprovalSetForTemplate(param);
	}

	/**
	 * 결재 템플릿 내용을 저장한다.
	 * @param param
	 */
	public void insertApprovalTemplateContent(Map<String, Object> param) {
		templateRepository.insertApprovalTemplateContent(param);
	}

	/**
	 * 결재 템플릿 내용 수정
	 * @param param
	 */
	public void updateApprovalTemplateContent(Map<String, Object> param) {
		templateRepository.updateApprovalTemplateContent(param);
	}

	/**
	 * 템플릿 결재 연결 설정을 수정
	 * @param param
	 */
	public void updateApprovalSetForTemplate(Map<String, Object> param) {
		templateRepository.updateApprovalSetForTemplate(param);
	}

	/**
	 * 콤보 박스 조회용 결재 템플릿 ( 조회조건 결재 템플릿 구분 )
	 * @param param
	 * @return
	 */
	public List findListApprovalTemplateComboboxItemByApprovalTemplateClass(Map<String, Object> param) {
		String LangCode = param.get("lang_ccd") == null? "ko_KR" : (String) param.get("lang_ccd");
		param.put("lang_ccd",LangCode);
		return templateRepository.findListApprovalTemplateComboboxItemByApprovalTemplateClass(param);
	}

	/**
	 * 결재 템플릿의 존재여부를 확인한다.
	 * @param param
	 * @return
	 */
	public boolean existApproval(Map<String, Object> param) {
		return (templateRepository.getCountApprovalSetForTemplateByApprovalTypeCode(param) > 0);
	}
	public boolean existApprovalMultiLangTemplate(Map<String, Object> param) {
		return (templateRepository.getCountApprovalSetForMultiLangTemplateByApprovalTypeCode(param) > 0);
	}

	/**
	 *  템플릿 삭제 시 메일 양식 설정 삭제
	 * @param param
	 */
	public void deleteMailTemplateInfo(Map<String, Object> param) {
		templateRepository.deleteMailTemplateInfo(param);
	}

	public Map findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId(Map templateData) {
		return templateRepository.findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId(templateData);
	}
}
