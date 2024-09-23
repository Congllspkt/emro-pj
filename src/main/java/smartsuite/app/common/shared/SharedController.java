package smartsuite.app.common.shared;

import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.bp.admin.auth.service.UserService;
import smartsuite.app.bp.admin.job.service.JobService;
import smartsuite.app.bp.admin.mailManager.service.MailManagerService;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.bp.admin.organizationManager.service.OrganizationManagerService;
import smartsuite.app.bp.admin.terms.service.TermsService;
import smartsuite.app.bp.approval.document.service.ApprovalDocumentService;
import smartsuite.app.bp.approval.line.service.ApprovalLineService;
import smartsuite.app.bp.approval.service.ApprovalService;
import smartsuite.app.common.board.service.BoardService;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.app.common.template.service.TemplateGeneratorService;
import smartsuite.data.FloaterStream;
import smartsuite.excel.core.exporter.CustomDataExportUtil;
import smartsuite.excel.core.importer.manager.XLSXLargeImporter;
import smartsuite.excel.spring.largeexporter.LargeExportUtil;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;
import smartsuite.module.ModuleManager;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.authentication.Auth;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 공통으로 사용하는 서비스 관련 컨트롤러 Class입니다.
 *
 * @author hjhwang
 * @see
 * @since 2016. 2. 2
 * @FileName SharedController.java
 * @package smartsuite.app.shared
 * @변경이력 : [2016. 2. 2] hjhwang 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class SharedController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SharedController.class);

	/** The shared service. */
	@Inject
	SharedService sharedService;

	/** The user service. */
	@Inject
	UserService userService;
	
	/* The job service */
	@Inject
	JobService jobService;

	/** The approval service. */
	@Inject
	ApprovalService approvalService;

	/** The approval service. */
	@Inject
	ApprovalLineService approvalLineService;

	/** The org service. */
	@Inject
	OrganizationManagerService organizationManagerService;

	/** The oper org service. */
	@Inject
	OperationOrganizationService operationOrganizationService;

	/** The file upload path. */
	@Value ("#{file['file.upload.path']}")
	String fileUploadPath;

	@Inject
	LargeExportUtil largeExportManager;
	
	@Inject
	CustomDataExportUtil customDataExportManager;
	
	/** FreeMarker 템플릿 생성 Service */
	@Inject
	TemplateGeneratorService templateGeneratorService;
	
	@Inject
	AccountService accountService;
	
	@Inject
	BoardService boardService;
	
	@Inject
	StdFileService stdFileService;
	
	@Inject
	TermsService termsService;

	@Inject
	ApprovalDocumentService approvalDocumentService;
	
	@Inject
	MailManagerService mailManagerService;

	private static final String CHARSET = "UTF-8";
	private static final String SERVICE_KEY = "saGCGL7cAgKe6hSW6g4rCbxoEGJlJfPDrByWVogQ5YqjU5eW2Ki7jR5vUfnlYrvm5WHl2UGDev6VsKe7Mx%2Fx6w%3D%3D";
	private static final String SERVICE_URL = "http://openapi.epost.go.kr/postal/retrieveNewAdressAreaCdSearchAllService/retrieveNewAdressAreaCdSearchAllService/getNewAddressListAreaCdSearchAll";

	/**
	 * 여러개의 CCD를 통한 공통코드 ITEM LIST가져오기.
	 * @param params
	 * @return
	 */
	@RequestMapping (value = "**/findListCommonCode.do")
	public @ResponseBody Map<String,List> findListCommonCode(@RequestBody List<String> params) {
		Map<String,List> codes = Maps.newHashMap();
		for(String param : params) {
			codes.put(param, this.findCommonCode(param));
		}
		return codes;
	}

	/**
	 * 공통코드 CCD를 통한 ITEM LIST가져오기.
	 * @param code
	 * @return
	 */
	@RequestMapping (value = "**/findCommonCode.do")
	//@Cacheable(value="cmmn-code", key="#code + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public @ResponseBody List findCommonCode(@RequestParam String code) {
		return sharedService.findCommonCode(code);
	}

	/**
	 * 공통코드와 특정 속성 list 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 19
	 * @Method Name : findListCommonCodeAttributeCode
	 */
	@RequestMapping (value = "**/findListCommonCodeAttributeCode.do")
	public @ResponseBody List findListCommonCodeAttributeCode(@RequestBody Map param) {
		return sharedService.findListCommonCodeAttributeCode(param);
	}

	@RequestMapping (value = "**/findListCommonCodeConstraintCode.do")
	public @ResponseBody List findListCommonCodeConstraintCode(@RequestBody Map param) {
		return sharedService.findListCommonCodeConstraintCode(param);
	}

	/**
	 * common codes 조회를 요청한다.
	 *
	 * @author : hjhwang
	 * @param lastUpdated the last updated
	 * @return the common codes
	 * @Date : 2016. 2. 2
	 * @Method Name : findListCommonCode
	 */
	@RequestMapping (value = "findListModifiedCode.do")
	public @ResponseBody List findListModifiedCode(@RequestParam (value = "lastUpdated") @DateTimeFormat (pattern = "yyyy-MM-ddHH:mm:ss") Date lastUpdated) {
		return sharedService.findListModifiedCode(lastUpdated);
	}

	/**
	 * 전체 company list 조회를 요청한다. Combobox 용
	 *
	 * @author : hjhwang
	 * @return the common code list
	 * @Date : 2016. 2. 2
	 * @Method Name : findCommonCode
	 */
	@RequestMapping (value = "**/findListCompanyCodeForCombobox.do")
	public @ResponseBody List findListCompanyCodeForCombobox() {
		return sharedService.findListCompanyCodeForCombobox();
	}
	/**
	 * 전체 formatter list 조회를 요청한다. Combobox 용
	 *
	 */
	@RequestMapping (value = "**/findListFormatterForCombobox.do")
	public @ResponseBody List findListFormatterForCombobox() {
		return sharedService.findListFormatterForCombobox();
	}

	@RequestMapping (value = "**/findListCompanyCodeForBulletinBoard.do")
	public @ResponseBody List findListCompanyCodeForBulletinBoard(@RequestBody Map param) {
		return sharedService.findListCompanyCodeForBulletinBoard(param);
	}

	/**
	 * user list 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 3
	 * @Method Name : findUserList
	 */
	@RequestMapping (value = "**/findListUser.do")
	public @ResponseBody List findListUser(@RequestBody Map param) {
		return userService.findListUser(param);
	}
	
	/**
	 * user_edu list 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 3
	 * @Method Name : findUserList
	 */
	@RequestMapping (value = "**/findListUserEdu.do")
	public @ResponseBody List findListUserEdu(@RequestBody Map param) {
		return userService.findListUserEdu(param);
	}

	/**
	 * session user 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @return the session user
	 * @Date : 2016. 4. 14
	 * @Method Name : getSessionUser
	 */
	@RequestMapping (value = "**/getSessionUser.do")
	public @ResponseBody Map<String,Object> getSessionUser() {
		return accountService.getSessionUser();
	}

	@RequestMapping(value = "**/getModules.do")
	public @ResponseBody Map<String,Object> getModules() {
		Map<String,Object> result = Maps.newHashMap();
		result.put("modules", ModuleManager.getModules());
		result.put("ccModules", ModuleManager.getCcModules());
		result.put("modulePropertiesMap", ModuleManager.getModulePropertiesMap());
		return result;
	}
	

	/**
	 * session 사용자 역할 조회.
	 *
	 * @author : Yeon-u Kim
	 * @return the session user
	 * @Date : 2016. 4. 14
	 * @Method Name : getSessionUser
	 */
	@RequestMapping (value = "**/findListUserRole.do")
	public @ResponseBody List<Map<String,Object>> findListUserRole() {
		return Auth.getCurrentUserRoles();
	}

	/**
	 * list dept 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 4. 14
	 * @Method Name : findListDepartmentByOrganization
	 */
	@RequestMapping (value = "**/findListDepartmentByOrganization.do")
	public @ResponseBody List findListDepartmentByOrganization(@RequestBody Map param) {
		return organizationManagerService.findListDepartmentByOrganization(param);
	}
	
	/**
	 * 사용중인 직무 목록 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the job list
	 * @Method Name : findListUsedPurchaseGroupCategory
	 */
	@RequestMapping (value = "**/findListUsedPurchaseGroupCategory.do")
	public @ResponseBody List findListUsedPurchaseGroupCategory(@RequestBody Map param) {
		return jobService.findListUsedPurchaseGroupCategory(param);
	}

	/**
	 * 로그인 사용자 운영단위별 운영조직 조회.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return 운영조직 목록
	 * @Date : 2016. 2. 3
	 * @Method Name : findListOperationOrganizationByUser
	 */
	@RequestMapping (value = "**/findListOperationOrganizationByUser.do")
	public @ResponseBody List findListOperationOrganizationByUser(@RequestBody String param) {
		return sharedService.findListOperationOrganizationByUser(param);
	}

	/**
	 * 운영단위 조직 연결정보 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 11
	 * @Method Name : findListOperationOrganizationByOperationOrganizationLink
	 */
	@RequestMapping (value = "**/findListOperationOrganizationByOperationOrganizationLink.do")
	public @ResponseBody List findListOperationOrganizationByOperationOrganizationLink(@RequestBody Map param) {
		return sharedService.findListOperationOrganizationByOperationOrganizationLink(param);
	}
	/**
	 * 업무 유형에 따른 온보딩 유형 코드 사용 여부를 반환 한다.
	 * @param obd_job_type_code : 온보딩 관련 업무 유형 코드
	 */
	@RequestMapping (value = "**/findListOnboardingTypeCodeByJob.do")
	public @ResponseBody String[] findListOnboardingTypeCodeByJob(@RequestBody String onBoardingJobTypeCode) {
		return sharedService.findListOnboardingTypeCodeByJob(onBoardingJobTypeCode);
	}

	/**
	 * 운영조직 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListOperationOrganization
	 */
	@RequestMapping (value = "**/findListOperationOrganization.do")
	public @ResponseBody List findListOperationOrganization(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganization(param);
	}

	/**
	 * 결재 상세정보 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the approval
	 * @Date : 2016. 2. 2
	 * @Method Name : findApprovalInfo
	 */
	@RequestMapping (value = "**/findApprovalInfo.do")
	public @ResponseBody Map findApprovalInfo(@RequestBody Map param) {
		String aprvId = (String)param.get("apvl_uuid");
		String appId = (String)param.get("task_uuid");
		if (StringUtils.isEmpty(aprvId)) {
			if (StringUtils.isEmpty(appId)) {
				//결재 템플릿 조회
				return approvalDocumentService.findApprovalDocumentTemplate(param);
			}
			param.put("apvl_uuid", approvalService.findTaskApprovalInfoUsingForApprovalUuid(param));
			if (StringUtils.isEmpty((String)param.get("apvl_uuid"))) {
				//결재 템플릿 조회
				return approvalDocumentService.findApprovalDocumentTemplate(param);
			}
		}
		return approvalService.findApprovalInfo(param);
	}

	/**
	 * 결재 요약정보를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the approval
	 * @Date : 2016. 2. 2
	 * @Method Name : findApprovalSummary
	 */
	@RequestMapping (value = "**/findApprovalSummary.do")
	public @ResponseBody Map findApprovalSummary(@RequestBody Map param) {
		String appId = (String)param.get("task_uuid");
		if (StringUtils.isEmpty(appId)) {
			return null;
		}
		String aprvId = approvalService.findTaskApprovalInfoUsingForApprovalUuid(param);
		if (StringUtils.isEmpty(aprvId)) {
			return null;
		}
		param.put("apvl_uuid", aprvId);
		return approvalService.findApprovalMaster(param);
	}

	/**
	 * 결재 임시저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveDraftApproval
	 */
	@RequestMapping (value = "**/saveDraftApproval.do")
	public @ResponseBody ResultMap saveDraftApproval(@RequestBody Map param) {
		return approvalService.saveApproval(param);
	}

	/**
	 * 결재 상신을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveSubmitApproval
	 */
	@RequestMapping (value = "**/saveSubmitApproval.do")
	public @ResponseBody ResultMap saveSubmitApproval(@RequestBody Map param) {
		return approvalService.saveApproval(param);
	}

	/**
	 * 결재 상신취소를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveCancelApproval
	 */
	@RequestMapping (value = "**/saveCancelApproval.do")
	public @ResponseBody ResultMap saveCancelApproval(@RequestBody Map param) {
		return approvalService.cancelApproval(param);
	}

	/**
	 * 결재 처리(승인, 반려)를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveResultApproval
	 */
	@RequestMapping (value = "**/saveResultApproval.do")
	public @ResponseBody Map saveResultApproval(@RequestBody Map param) {
		return approvalService.saveResultApproval(param);
	}
	
	/**
	 * 결재의견을 저장한다.(수신자 전용)
	 */
	@RequestMapping (value = "**/saveOpinionApproval.do")
	public @ResponseBody ResultMap saveOpinionApproval(@RequestBody Map param) {
		return approvalLineService.saveOpinionApproval(param);
	}	

	/**
	 * 결재 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteApproval
	 */
	@RequestMapping (value = "**/deleteApproval.do")
	public @ResponseBody ResultMap deleteApproval(@RequestBody Map param) {
		return approvalService.deleteApproval(param);
	}
	
	/**
	 * 결재 재상신을 요청한다.
	 *
	 * @author : kh_lee
	 * @param param
	 * @return resultMap
	 * @Date : 2017. 4. 12
	 * @Method Name : reSubmitApproval
	 */
	@RequestMapping (value = "**/reSubmitApproval.do")
	public @ResponseBody ResultMap reSubmitApproval(@RequestBody Map param) {
		return approvalService.reSubmitApproval(param);
	}
	
	/**
	 * 결재선 저장을 요청한다.
	 * 결재선 변경 기능 추가 [SMARTNINE-2289]
	 *
	 * @author : wskim
	 * @param param
	 * @return the Map
	 * @Date : 2018. 7. 16
	 * @Method Name : saveApprovalLine
	 */
	@RequestMapping (value = "**/saveApprovalLine.do")
	public @ResponseBody Map saveApprovalLine(@RequestBody Map param){
		return approvalService.saveApprovalLine(param);
	}
	
	/**
	 * 결재선을 조회한다.
	 * 결재선 변경 기능 추가 [SMARTNINE-2289]
	 *
	 * @author : wskim
	 * @param param
	 * @return the List
	 * @Date : 2018. 7. 16
	 * @Method Name : findListApprovalLineProcess
	 */
	@RequestMapping (value = "**/findListApprovalLineProcess.do")
	public @ResponseBody List findListApprovalLineProcess(@RequestBody Map param){
		return approvalService.findListApprovalLineProcess(param);
	}
	
	@RequestMapping(value = "**/convertDisplayApprover.do")
	public @ResponseBody Map convertDisplayApprover(@RequestBody Map param) {
		return approvalService.convertDisplayApprover(param);
	}
	
	@RequestMapping(value = "**/findListHierachiDept.do")
	public @ResponseBody List findListHierachiDept(@RequestBody Map param) {
		return organizationManagerService.findListHierachiDept(param);
	}
	
	@RequestMapping(value = "**/findListUserByDept.do")
	public @ResponseBody List findListUserByDept(@RequestBody Map param) {
		return userService.findListUserByDept(param);
	}
	
	@RequestMapping(value = "**/printApproval.do")
	public ModelAndView printApproval(HttpServletRequest request, HttpServletResponse response) {
		String apvlUuid = request.getParameter("apvl_uuid");
		
		Map param = Maps.newHashMap();
		param.put("apvl_uuid", apvlUuid);
		
		String body = approvalService.printApproval(param);
		Map resultInfo = Maps.newHashMap();
		resultInfo.put("tmpCon", body);
		
		ModelAndView model = new ModelAndView();
		model.setViewName("approvalPrint");
		model.addObject("resultInfo", resultInfo);
		return model;
	}
	

	/**
	 * 로그인 사용자 메뉴 목록 조회.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListAttach
	 */
	@RequestMapping (value = "**/findListUserMenu.do")
	public @ResponseBody List findListLoginUserMenu(@RequestBody Map param) {
		
		return sharedService.findListUserMenu(param);
	}
	
	@RequestMapping (value = "**/findListMenu.do")
	public @ResponseBody List findListMenu(@RequestBody Map param) {
		
		return sharedService.findListMenu(param);
	}

	/**
	 * 품목분류의 품목분류1 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 2
	 * @Method Name : findListMajorCategory
	 */
	@RequestMapping (value = "**/findListMajorCategory.do")
	public @ResponseBody List findListMajorCategory(@RequestBody Map param) {
		return sharedService.findListMajorCategory(param);
	}

	/**
	 * 상위분류에 대한 하위분류 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 2
	 * @Method Name : findListCategoryByParentCategoryCode
	 */
	@RequestMapping (value = "**/findListCategoryByParentCategoryCode.do")
	public @ResponseBody List findListCategoryByParentCategoryCode(@RequestBody Map param) {
		return sharedService.findListCategoryByParentCategoryCode(param);
	}

	/**
	 * 수량 단위 공통코드 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 4
	 * @Method Name : findListAmountUnitCode
	 */
	@RequestMapping (value = "**/findListAmountUnitCode.do")
	public @ResponseBody List findListAmountUnitCode(@RequestBody Map param) {
		return sharedService.findListAmountUnitCode(param);
	}

	/**
	 * SP 운영단위 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 27
	 * @Method Name : findListOperationOrganizationBySupplier
	 */
	@RequestMapping (value = "**/findListOperationOrganizationBySupplier.do")
	public @ResponseBody List findListOperationOrganizationBySupplier(@RequestBody String param) {
		return sharedService.findListOperationOrganizationBySupplier(param);
	}
	
	/**
	 * help 버튼 클릭시 메뉴얼 타입을 조회 한다.
	 *
	 * @author : JuEung Kim
	 * @param menuCd the menu cd
	 * @Date : 2016. 7. 1
	 * @Method Name : findHelpManualType
	 */
	@RequestMapping (value = "**/findHelpManualType.do")
	public @ResponseBody Map findHelpManualType (@RequestBody Map param) {
		return sharedService.findHelpManualTypeProcess(param);
	}

	/**
	 * 메뉴얼 정보를 조회하여 메뉴얼을 보여줄 화면을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param menuCd the menu cd
	 * @return the model and view
	 * @throws IOException
	 * @Date : 2016. 7. 1
	 * @Method Name : popupHelpManual
	 */
	@RequestMapping (value = "**/popupHelpManual.do")
	public ModelAndView popupHelpManual(HttpServletRequest request, HttpServletResponse response, @RequestParam (value = "menuCd", required = false) String menuCd, @RequestParam (value = "langCd", required = false) String langCd) throws IOException {
		ModelAndView mav = new ModelAndView();

		Map param = Maps.newHashMap();
		Map returnMap = Maps.newHashMap();
		
		param.put("menu_cd", menuCd);
		param.put("lang_ccd", langCd);

		Map manualInfo = sharedService.findHelpManualInfo(param);

		if (manualInfo != null) {
			// 매뉴얼 유형
			if ("PDF".equals(manualInfo.get("mnl_typ_ccd"))) {
				try {
					FileItem fileItem = stdFileService.findFileItemWithContents((String) manualInfo.get("athf_uuid"));
					byte[] pdfByteArray = fileItem.toByteArray();
					returnMap.put("pdfByteArray", pdfByteArray);
				} catch (Exception e) {
					IOException ioe = new IOException(e.getMessage());
					throw ioe;
				} 
				
				mav.setViewName("manual/manualPdfView");
			} else {
				returnMap.put("mnl_cont", manualInfo.get("mnl_cont"));
				mav.setViewName("manual/manualHtmlView");
			}
		} else {
			mav.setViewName("manual/manualHtmlView");
		}
		mav.addObject("manualInfo", returnMap);
		return mav;
	}

	/**
	 * 엑셀파일을 업로드해 데이터를 JSON 형식으로 파싱, response outputstream에 써준다.
	 *
	 * @author : SungWuk Ahn
	 * @param request the request
	 * @param response the response
	 * @param name the name
	 * @param parentIdProperty the parent id property
	 * @param idProperty the id property
	 * @param file the file
	 * @return void
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @Date : 2016. 7. 15
	 * @Method Name : importExcel
	 */
	@RequestMapping (value = { "/**/importexcel.do" })
	public void importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam (value = "name", required = false) String name,
			@RequestParam (value = "parentIdProperty", required = false) String parentIdProperty,
			@RequestParam (value = "excludeEmptyRow", required = false, defaultValue = "true") boolean excludeEmptyRow,
			@RequestParam (value = "idProperty", required = false) String idProperty, @RequestParam (value = "file", required = false) MultipartFile file)
			throws IOException {
		XLSXLargeImporter im = new XLSXLargeImporter(excludeEmptyRow);
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		InputStream inputStream = null;
		try{
			if( null != file){
				inputStream = file.getInputStream();
				im.doImport(inputStream, response.getOutputStream());
			}
		}catch (Exception e){
			LOGGER.error(e.getMessage());
		}finally {
			if(null != inputStream) inputStream.close();
		}

	}

	/**
	 * 대용량 엑셀파일을 다운로드 한다.
	 *
	 * @author : SungWuk Ahn
	 * @param request the request
	 * @param response the response
	 * @param fileName the fileName
	 * @return void
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @Date : 2016. 7. 29
	 * @Method Name : largeexportExcel
	 */
	@RequestMapping (value = { "/**/largeexport.do" })
	public void largeexportExcel(@RequestParam ("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		largeExportManager.doExport(request, response, URLEncoder.encode(fileName, "UTF-8").replace('+', ' '));
	}
	
	// 엑셀 지정형식 다운로드
	@RequestMapping (value = { "/**/customexport.do" })
	public void customexportExcel(@RequestParam ("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		customDataExportManager.doExport(request, response, URLEncoder.encode(fileName, "UTF-8").replace('+', ' '));
	}

	/**
	 * zip code by DB 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the floater stream
	 * @Date : 2017. 8. 18
	 * @Method Name : findListZipCodeByDatabase
	 */
	@RequestMapping(value="**/findListZipCodeByDatabase.do")
	public @ResponseBody FloaterStream findListZipCodeByDatabase(@RequestBody Map param) {
		// 대용량 처리
		return sharedService.findListZipCodeByDatabase(param);
	}
	/**
	 * 우편번호 검색을 요청한다.
	 *
	 * @author : 
	 * @param request the request
	 * @param response the response
	 * @param param the param
	 * @return void
	 * @Date : 2016. 7. 19
	 * @Method Name : findZipcode
	 */
	@RequestMapping (value = "**/findZipcode.do")
	public void findZipcode(HttpServletRequest request, HttpServletResponse response, @RequestBody Map param) throws Exception {

		String srchWord = (String)param.get("srch_word");
		Integer srchPage = (Integer)param.get("srch_page");
		if (srchPage == null) {
			srchPage = 1;
		}

		StringBuffer sendUrl = new StringBuffer();
		sendUrl.append(SERVICE_URL)
				.append("?ServiceKey=").append(SERVICE_KEY)
				.append("&srchwrd=").append(URLEncoder.encode(srchWord, CHARSET))
				.append("&countPerPage=50&currentPage=")
				.append(srchPage);

		URL url = new URL(sendUrl.toString());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		
		InputStream is = null;
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();
		PrintWriter out = null;
		
		try{
			is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is , CHARSET));
			
			response.setCharacterEncoding(CHARSET);
			response.setContentType("application/json; charset=" + CHARSET);
			
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				line = reader.readLine();
			}
			
			out = response.getWriter();
			out.print(XML.toJSONObject(buffer.toString().toLowerCase(LocaleContextHolder.getLocale())));
			out.flush();
			
		}finally{
			if(out != null){
				out.close();
			}
			if(reader != null){
				reader.close();
			}
			if(is != null){
				is.close();
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		
		
		
	}
	
	/**
	 * 팝업에서 보여줄 템플릿을 가져온다. 
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the Map
	 * @Date : 2016. 8. 30
	 * @Method Name : getTmpByTmpId.do
	 */
	@RequestMapping(value="**/getTmpByTmpId.do")
	public ModelAndView getTmpByTmpId(String tmpId){
		
		Map param = Maps.newHashMap();
		ModelAndView mav = new ModelAndView();
		param.put("basc_ctmpl_cd", tmpId);
		
		Map tmpInfo = templateGeneratorService.findTemplateBaseInfoByTemplateBaseId(param);
		
		if (tmpInfo == null) {
			mav.setViewName("portal/htmlView");
		} else {
			mav.addObject("tmpInfo", tmpInfo);

			// 매뉴얼 유형(HTML : HTML, TXT : Text, EML : Mail, APVL : Approval)
			if ("HTML".equals(tmpInfo.get("tmpl_typ_ccd"))) {
				mav.setViewName("portal/htmlView");
			} else if("TXT".equals(tmpInfo.get("tmpl_typ_ccd"))) {
				mav.setViewName("portal/textView");
			}
		}
		
		return mav;
	}
	
	/**
	 * Captcha 이미지 크기 설정 및 Captcha 이미지 반환 
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the Map
	 * @Date : 2017. 2. 10
	 * @Method Name : captchaValidation.do
	 */
	@RequestMapping(value="**/captchaValidation.do")
	public @ResponseBody Map captchaValidation(@RequestBody Map param){
		int imgWidth = (Integer) param.get("imgWidth");
		int imgHeight =(Integer) param.get("imgHeight");
		
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("captchaImage", sharedService.captchaValidation(imgWidth, imgHeight));
		return resultMap;
	}
	
	/**
	 * 사용자가 입력값과 세션의 Captcha값 비교  
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return Boolean
	 * @Date : 2017. 2. 10
	 * @Method Name : checkUserInputValueCompareSessionCaptcha.do
	 */
	@RequestMapping(value="**/checkUserInputValueCompareSessionCaptcha.do")
	public @ResponseBody Boolean checkUserInputValueCompareSessionCaptcha(@RequestBody Map param){
		return sharedService.checkUserInputValueCompareSessionCaptcha((String) param.get("captcha"));
	}
	
	/**
	 * 사용자 자신의 정보 저장, UserController.saveUser와 다름(다른 사용자도 저장 가능)   
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return Boolean
	 * @Date : 2017. 2. 17
	 * @Method Name : saveUser.do
	 */
	@RequestMapping (value = "**/saveUser.do")
	public @ResponseBody ResultMap saveUser(@RequestBody Map param) {
		return sharedService.saveUser(param);
	}
	
	/**
	 * 사용자 자신의 패스워드 갱신, UserController.updatePassword와 다름(다른 사용자도 저장 가능)   
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return Boolean
	 * @Date : 2017. 2. 17
	 * @Method Name : updatePassword.do
	 */
	@RequestMapping (value = "**/updatePassword.do")
	public @ResponseBody ResultMap updatePassword(@RequestBody Map param) {
		return userService.updatePasswordProcess(param);
	}

	/**
	 * Find info auto manual.
	 *
	 * @param param the param
	 * @return the map
	 */
	@RequestMapping(value="**/findInfoAutoManual.do")
	public @ResponseBody Map<String,Object> findInfoAutoManual(@RequestBody Map param){
		return sharedService.findInfoAutoManualProcess(param);
	}
	
	/**
	 * 사용자 자신의 정보 검색 , UserController.findUserByUserId와 다름(다른 사용자도 검색 가능)   
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return Boolean
	 * @Date : 2017. 2. 17
	 * @Method Name : findUserByUserId.do
	 */
	@RequestMapping (value = "**/findCurrntUserInfo.do")
	public @ResponseBody Map findCurrntUserInfo() {
		return sharedService.findCurrntUserInfo();
	}

	
	/**
	 * 미사용 계정 기준 정보 조회
	 *
	 * @author : Joon Huh
	 * @return the map
	 * @Date : 2017. 5. 2
	 * @Method Name : findDisabledAccountInfo
	 */
	@RequestMapping (value = "**/findDisabledAccountInfo.do")
	public @ResponseBody Map findDisabledAccountInfo() {
		return sharedService.findDisabledAccountInfo();
	}
	
	/**
	 * Preview Tempalte Generator
	 * 
	 * @param param : {base_template, data}
	 * @return list
	 */
	@RequestMapping(value = "**/generatePreviewTemplate.do")
	public @ResponseBody Map generatePreviewTemplate(@RequestBody Map param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		try {
			resultMap.put("html", templateGeneratorService.freemarkerTemplateGenerate("preview", (String) param.get("template"), param.get("templateData")));
		} catch (TemplateException e) {
			throw new CommonException(ErrorCode.FAIL, e);
		} catch (IOException e) {
			throw new CommonException(ErrorCode.FAIL, e);
		}
		
		return resultMap;
	}
	
	/**
     * 운영단위별 운영조직 조회.
     */
    @RequestMapping (value = "**/findListOperationOrganizationForCombobox.do")
    public @ResponseBody List findListOperationOrganizationForCombobox(@RequestBody String param) {
        return sharedService.findListOperationOrganizationForCombobox(param);
    }
    
    /**
	 * 공지 팝업 목록 조회
	 *
	 * @author : 
	 * @param param the param
	 * @return the result vo
	 * @Date : 2017. 8. 24
	 * @Method Name : findListMainNotice
	 */
	@RequestMapping(value="**/findListNoticeInfo.do", method = RequestMethod.POST)
	public @ResponseBody List findListNoticeInfo(@RequestBody Map<String, Object> param){
		return boardService.findListNoticeInfo(param);
	}

	
	@RequestMapping(value = "**/updateExceptEmail.do")
	public @ResponseBody void updateExceptEmail(@RequestBody Map param) {
		mailManagerService.updateExceptEmail(param);
	}


	/**
	 * Find list oper org by tenant.
	 *
	 * @return the list
	 */
	@RequestMapping (value = "**/findListOperationOrganizationByTenant.do")
	public @ResponseBody List findListOperationOrganizationByTenant(@RequestBody Map param) {
		return sharedService.findListOperationOrganizationByTenant(param);
	}
	
	/**
	 * 약관 동의 여부 체크 
	 *
	 * @param param the param
	 * @return the result map
	 * @Date : 2017. 8. 24
	 * @Method Name : existChargeTermsAgreeInfoByTermsConditionsAgreeId
	 */
	@RequestMapping(value="/**/existChargeTermsAgreeInfoByTermsConditionsAgreeId.do", method = RequestMethod.POST)
	public @ResponseBody Map existChargeTermsAgreeInfoByTermsConditionsAgreeId(@RequestBody Map<String, Object> param){
		return termsService.existChargeTermsAgreeInfoByTermsConditionsAgreeId(param);
	}
	
	/**
	 * 유효한 약관 정보 조회 
	 *
	 * @param param the param
	 * @return the result map
	 * @Date : 2017. 8. 24
	 * @Method Name : findListNewRegistrationApplyChargeTermsTemplateInfo 
	 */
	@RequestMapping(value="/**/findListNewRegistrationApplyChargeTermsTemplateInfo.do", method = RequestMethod.POST)
	public @ResponseBody List findListNewRegistrationApplyChargeTermsTemplateInfo(@RequestBody Map<String, Object> param){
		return termsService.findListNewRegistrationApplyChargeTermsTemplateInfo(param);
	}
	
	/**
	 * 약관 정보 저장
	 *
	 * @param param the param
	 * @return the result map
	 * @Date : 2017. 8. 24
	 * @Method Name : saveChargeTermsAgree
	 */
	@RequestMapping(value="/**/saveChargeTermsAgree.do")
	public @ResponseBody ResultMap saveChargeTermsAgree(HttpServletRequest request, @RequestBody Map param){
		return termsService.saveChargeTermsAgree(param, request.getRemoteAddr());
	}

	@RequestMapping(value="**/saveUserHomeType.do")
    public @ResponseBody ResultMap saveUserHomeType(@RequestBody Map param) {
    	return sharedService.saveUserHomeType(param);
    }
	
	/**
	 * 유효한 약관 정보 조회 
	 *
	 * @param param the param
	 * @return the result map
	 * @Date : 2017. 8. 24
	 * @Method Name : findValidTermsMasterInfoByTermsConditions 
	 */
	@RequestMapping(value="**/findValidTermsMasterInfoByTermsConditions.do")
	public @ResponseBody Map findValidTermsMasterInfoByTermsConditions(@RequestBody Map<String, Object> param){
		return termsService.findValidTermsMasterInfoByTermsConditions(param);
	}
	
	/**
	 * mdi에서 locale을 변경하고 user에 변경된 locale의 정보를 set한다.
	 *
	 * @Date : 2021. 2. 9
	 * @Method Name : afterChangeLocale 
	 */
	@RequestMapping (value = "**/afterChangeLocale.do")
	public @ResponseBody void afterChangeLocale() {
		sharedService.afterChangeLocale();
	}

	@RequestMapping(value = "**/searchReceiptSubjectEmail.do")
	public @ResponseBody List searchReceiptSubjectEmail(@RequestBody Map param) {
		return mailManagerService.searchReceiptSubjectEmail(param);
	}
	
	/* 원화 환산 환율 목록 조회 */
	@RequestMapping(value = "**/findListLatelyExchange.do")
    public @ResponseBody List findListLatelyExchange(@RequestBody String param) {
        return sharedService.findListLatelyExchange(param);
    }

	/*
	 * 템플릿 미리보기
	 * */
	@RequestMapping(value = "**/findTemplatePreview.do")
	public @ResponseBody Map findTemplatePreview(@RequestBody Map param) {
		return templateGeneratorService.findTemplatePreview(param);
	}



	/**
	 * 모듈 별 첨부파일 리스트를 조회한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "**/findListModuleAttach.do")
	public @ResponseBody List findListModuleAttach(@RequestBody Map<String, Object> param){
		return sharedService.findListModuleAttach(param);
	}

	@RequestMapping(value = "**/findListGroupModuleAttach.do")
	public @ResponseBody List findListGroupModuleAttach(@RequestBody Map<String, Object> param){
		return sharedService.findListGroupModuleAttach(param);
	}
	@RequestMapping(value = "**/saveListModuleAttachCode.do")
	public @ResponseBody void saveListModuleAttachCode(@RequestBody Map<String, Object> param){
		sharedService.saveListModuleAttachCode(param);
	}

	@RequestMapping(value = "**/deleteListModuleAttachCode.do")
	public @ResponseBody void deleteListModuleAttachCode(@RequestBody Map<String, Object> param){
		sharedService.deleteListModuleAttachCode(param);
	}

	@RequestMapping(value = "**/findModuleAttach.do")
	public @ResponseBody Map findModuleAttach(@RequestBody Map<String, Object> param){
		return sharedService.findModuleAttach(param);
	}


}
