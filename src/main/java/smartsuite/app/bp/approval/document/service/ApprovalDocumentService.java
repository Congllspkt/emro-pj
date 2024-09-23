package smartsuite.app.bp.approval.document.service;

import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.approval.service.ApprovalFactory;
import smartsuite.app.bp.approval.service.ApprovalStrategy;
import smartsuite.app.bp.approval.document.repository.ApprovalDocumentRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.template.service.TemplateGeneratorService;
import smartsuite.security.authentication.Auth;

import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
public class ApprovalDocumentService {

	static final Logger LOG = LoggerFactory.getLogger(ApprovalDocumentService.class);

	@Inject
	ApprovalDocumentRepository approvalDocumentRepository;

	/** The template generator service. */
	@Inject
	private TemplateGeneratorService templateGeneratorService;

	@Inject
	ApprovalFactory approvalFactory;


	/**
	 * 결재본문을 등록한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertApprovalDocument
	 */
	public void insertApprovalDocument(Map<String, Object> param) {
		approvalDocumentRepository.insertApprovalDocument(param);
	}


	/**
	 * 결재본문을 수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : updateApprovalDocument
	 */
	public void updateApprovalDocument(Map<String, Object> param) {
		approvalDocumentRepository.updateApprovalDocument(param);
	}

	/**
	 * 결재본문을 삭제한다. - 물리적 삭제
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteApprovalDocument
	 */
	public void deleteApprovalDocument(Map<String, Object> param) {
		approvalDocumentRepository.deleteApprovalDocument(param);
	}

	/**
	 * 결재본문 상세정보를 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return map
	 * @Date : 2016. 2. 2
	 * @Method Name : findApprovalDocument
	 */
	public Map<String, Object> findApprovalDocument(Map<String, Object> param) {
		return approvalDocumentRepository.findApprovalDocument(param);
	}


	/**
	 * 결재 템플릿을 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2016. 9. 19
	 * @Method Name : findApprovalDocumentTemplate
	 */
	public Map<String, Object> findApprovalDocumentTemplate(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		//결재 유형 코드
		String aprvTypCd = (String)param.get("apvl_typ_ccd");

		if(!StringUtils.isEmpty(aprvTypCd)){
			param.put("apvl_typ_ccd", aprvTypCd);
			param.put("use_yn", "Y");

			Map<String, Object> templateData = templateGeneratorService.findApprovalTemplateByApprovalTypeCode(param);	// 결재서식(template)조회
			Map<String, Object> tmpParam = (Map<String, Object>) getDocumentTaskApprovalFormParameter(param);				// 서식에 맵팽될 param정보조회
			Map<String, Object> approvalHeader = (Map<String, Object>) getDocumentApprovalHeader(param);		// 결재 header정보 ( 임시저장 이후에는 param에 결재정보가 넘어온다 )

			if(tmpParam != null) {
				tmpParam.put("approvalInfo", approvalHeader);//결재 header정보 설정
			}

			//결재 템플릿이 존재하면 결재 문서 리턴
			if(templateData != null){
				Map<String,Object> checkData = templateGeneratorService.parameterCheck(templateData, tmpParam);
				Map<String, Object> approvalDocMap = Maps.newHashMap();
				try {
					// 템플릿 내용의 파라미터값 치환
					String contents = templateGeneratorService.freemarkerTemplateGenerate(
							(String)templateData.get("apvl_typ_ccd"),
							(String)templateData.get("ctmpl_cont"),
							checkData.get("data"));

					// 템플릿 기초 아이디 값으로 템플릿 내용을 조회한다.
					if(templateData.get("tmpl_typ_ccd") == null || "".equals(templateData.get("tmpl_typ_ccd")) ){
						templateData.put("tmpl_typ_ccd", "APVL"); // 결재
					}

					// 결재관리에서 템플릿을 사용하지 않는 경우
					if(templateData.get("basc_ctmpl_cd") == null || "".equals(templateData.get("basc_ctmpl_cd")) ){
						approvalDocMap.put("apvl_body_cont", contents);

						// 결재관리에서 템플릿을 사용하는 경우
					}else{
						// 템플릿 관리의 템플릿 조회
						Map template = templateGeneratorService.findTemplateInfoByTemplateClassAndTemplateBaseId(templateData);
						String content;
						if(template.get("basc_ctmpl_cont") == null) {
							content = (String) template.get("display_basc_ctmpl_cont");
						} else {
							content = (String) template.get("basc_ctmpl_cont");
						}
						template.put("contents", contents);

						// 템플릿 관리의 템플릿과 결재 관리의 템플릿 내용을 합침.
						approvalDocMap.put("apvl_body_cont"
								,templateGeneratorService.freemarkerTemplateGenerate(
										aprvTypCd
										,content
										,template
								)
						);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage());
					checkData.put(Const.RESULT_STATUS, Const.FAIL);
					resultMap.put("templateParamCheckResult", checkData);
				}
				resultMap.put("approvalDoc", approvalDocMap);
				resultMap.put("appDocCont", approvalDocMap.get("apvl_body_cont"));
				resultMap.put("templateParamCheckResult", checkData);
			}
		}
		return resultMap;
	}


	/**
	 * ApprovalFactory를 사용하여, approvalStrategy를 찾고, 해당 strategy에서 결재서식param을 가져온다.
	 *
	 * @author : LMS
	 * @param param the param
	 * @return Map<String, Object> resultMap
	 * @Date : 2017. 05. 23
	 * @Method Name : getFormParam
	 */
	private Map<String, Object> getDocumentTaskApprovalFormParameter(Map<String, Object> param){
		String aprvTypcd = (String)param.getOrDefault("apvl_typ_ccd","");
		String appId = (String)param.getOrDefault("task_uuid","");

		ApprovalStrategy strategy = approvalFactory.afterApprovalProcessing(aprvTypcd);

		Map<String, Object> resultMap  = Maps.newHashMap();
		if(strategy != null) {
			resultMap = strategy.makeParam(aprvTypcd, appId);
		}
		return resultMap;
	}


	/**
	 * 결재서식의 header 정보를 설정.
	 * 기안일자, 기안자이름, 기안자 운영단위 정도.
	 *
	 * @author : LMS
	 * @param param the param
	 * @return Map<String, Object>
	 * @Date : 2017. 05. 23
	 * @Method Name : getApprovalMaster
	 */
	private Map<String, Object> getDocumentApprovalHeader(Map<String, Object> param){
		Map<String, Object> approvalMaster = Maps.newHashMap();

		Map<String, Object> userInfo = Auth.getCurrentUserInfo();//세션정보
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		approvalMaster.put("apvl_rptg_dttm", simpleDateFormat.format(new Date()));	//기안일자
		approvalMaster.put("regr_id", userInfo.get("usr_id"));							//기안자 id
		approvalMaster.put("reg_nm", userInfo.get("usr_nm"));							//기안자 이름
		approvalMaster.put("dept_cd", userInfo.get("dept_cd"));							//기안자 부서 cd
		approvalMaster.put("dept_nm", userInfo.get("dept_nm"));							//기안자 부서 이름

		String approvalUuid = (String) param.getOrDefault("apvl_uuid","");
		if(StringUtils.isNotEmpty(approvalUuid)) {
			//결재id가 있을 경우 품의번호 설정.
			//품의번호(결재 수정시에 넘어오는 param에는 기안일자가없음...)
			approvalMaster.put("apvl_docno", param.get("apvl_docno"));
		}

		return approvalMaster;
	}

	/**
	 * 결재 본문이 존재하는지 체크한다.
	 */
	public boolean existApprovalDocumentContent(Map<String, Object> param) {
		return (approvalDocumentRepository.getCountApprovalDocument(param) > 0 );
	}

	/**
	 * 결재 본문 추가 및 수정 프로세스
	 * @param approvalMaster
	 * @param documentInfo
	 */
	public void saveApprovalDocumentProcess(Map<String, Object> approvalMaster, Map<String, Object> documentInfo) {
		Map<String, Object> template = this.findApprovalDocumentTemplate(approvalMaster);

		if(template != null) {
			documentInfo.put("apvl_body_cont", template.get("appDocCont"));	//저장시에도 결재서식 재생성.
		}

		if (this.existApprovalDocumentContent(documentInfo)) { // 등록된 결재 본문이 존재하면
			this.updateApprovalDocument(documentInfo); // 본문 수정
		} else {
			this.insertApprovalDocument(documentInfo); // 본문 신규 등록
		}
	}

	public Map<String, Object> findApprovalDocumentForReSubmit(Map<String, Object> param) {
		return approvalDocumentRepository.findApprovalDocumentForReSubmit(param);
	}
	
	public String findPrintApprovalDocumentTemplate(Map<String, Object> param){
		Map templateParam = Maps.newHashMap();
		templateParam.put("tmpl_typ_ccd", "HTML");
		templateParam.put("basc_ctmpl_cd", "APRV_PRINT");
		templateParam.put("lang_ccd", LocaleContextHolder.getLocale());
		
		Map<String, Object> template = templateGeneratorService.findTemplateInfoByTemplateClassAndTemplateBaseId(templateParam);
		String content;
		if(template.get("basc_ctmpl_cont") == null) {
			content = (String) template.get("display_basc_ctmpl_cont");
		} else {
			content = (String) template.get("basc_ctmpl_cont");
		}
		
		String generateContent = null;
		try {
			generateContent = templateGeneratorService.freemarkerTemplateGenerate(
					UUID.randomUUID().toString()
					, content
					, param
			);
		} catch(TemplateException e) {
			throw new RuntimeException(e);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return generateContent;
	}
}