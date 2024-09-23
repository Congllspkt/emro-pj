package smartsuite.app.sp.login.bidding;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.common.board.service.BoardService;
import smartsuite.app.common.eform.repository.EformLoginRepository;
import smartsuite.app.common.portal.service.PortalService;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.app.common.stateful.service.StatefulService;
import smartsuite.app.sp.login.bidding.event.SupplierRootEventPublisher;
import smartsuite.app.sp.login.bidding.service.SupplierBiddingService;
import smartsuite.app.sp.user.service.SupplierUserService;
import smartsuite.exception.CommonException;
import smartsuite.mybatis.data.impl.PageResult;
import smartsuite.security.account.info.PasswordProvider;
import smartsuite.security.authentication.Auth;
import smartsuite.security.core.authentication.encryption.salt.SaltSource;
import smartsuite.security.core.crypto.AESCipherUtil;
import smartsuite.security.core.crypto.AESIvParameterGenerator;
import smartsuite.security.core.crypto.AESSecretKeyGenerator;
import smartsuite.security.core.crypto.CipherUtil;
import smartsuite.security.web.authentication.encryption.salt.PasswordSalt;
import smartsuite.security.web.crypto.AESCipherKey;
import smartsuite.spring.tenancy.context.TenancyContextHolder;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class SupplierRootController {

	@Inject
	SaltSource saltSource;

	@Inject
	SupplierUserService supplierUserService;
	@Inject
	CipherUtil cipherUtil;

	@Inject
	BoardService boardService;

	@Inject
	SharedService sharedService;

	@Inject
	PasswordEncoder passwordEncoder;

	@Inject
	private PasswordProvider passwordProvider;

	@Inject
	EformLoginRepository eformLoginRepository;

	@Inject
	AESSecretKeyGenerator keyGenerator;

	@Inject
	AESIvParameterGenerator parameterGenerator;

	@Inject
	SupplierRootEventPublisher supplierRootEventPublisher;

	@Value("#{globalProperties['sec.param.encrypt']}")
	private String securityParamEncrypt;

	private static final Logger LOG = LoggerFactory.getLogger(SupplierRootController.class);

	@Value("#{globalProperties['server.cls']}")
	private String serverCls;


	@Value("#{globalProperties['error.user.message']}")
	private Boolean useErrorUserMessage;

	@Inject
	PortalService portalService;

	@Inject
	StatefulService statefulService;

	@Inject
	SupplierBiddingService supplierBiddingService;

	@RequestMapping(value = "spLogin.do")
	public ModelAndView spLoginPagePost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/spLogin.do");
		}
		
		String sysId = request.getParameter("sysId");
		try {
			sysId = sharedService.getTenantId(sysId);
		} catch(RuntimeException e) {
			sysId = TenancyContextHolder.getDefaultTenant().getId();
		}
		
		sharedService.setTenant(sysId, request, response);
		
		Map<String, Object> param = new HashMap<String, Object>();
		// 외부에 노출 할 게시판 bbd_uuid 와 보여질 개수를 명시합니다.
		param.put("bbd_uuid", "10002");
		param.put("startRow", 1);
		param.put("endRow", 3);
		Map<String, Object> pageParam = Maps.newHashMap();
		pageParam.put("manualQuery", false);
		pageParam.put("end", 3);
		pageParam.put("page", 1);
		pageParam.put("start", 1);
		pageParam.put("size", 3);
		param.put("_page", pageParam);
		PageResult boardList = boardService.findListPortalNoticeForGridPaging(param);
		PageResult biddingList = supplierBiddingService.findPagingBiddingNoticeList(param);
		
		ModelAndView model = new ModelAndView();
		model.addObject("boardList",boardList.get("content"));
		model.addObject("biddingList",biddingList.get("content"));
		model.addObject("boardId",cipherUtil.encrypt((String) param.get("bbd_uuid")));
		model.addObject("sysId", sysId);
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		model.setViewName("login/sp/spLogin");

		return model;
	}


	@RequestMapping(value = "spLoginKr.do")
	public ModelAndView spLoginKrPagePost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/spLoginKr.do");
		}

		String sysId = request.getParameter("sysId");
		try {
			sysId = sharedService.getTenantId(sysId);
		} catch(RuntimeException e) {
			sysId = TenancyContextHolder.getDefaultTenant().getId();
		}

		sharedService.setTenant(sysId, request, response);

		Map<String, Object> param = new HashMap<String, Object>();
		// 외부에 노출 할 게시판 bbd_uuid 와 보여질 개수를 명시합니다.
		param.put("bbd_uuid", "10002");
		param.put("startRow", 1);
		param.put("endRow", 3);
		Map<String, Object> pageParam = Maps.newHashMap();
		pageParam.put("manualQuery", false);
		pageParam.put("end", 3);
		pageParam.put("page", 1);
		pageParam.put("start", 1);
		pageParam.put("size", 3);
		param.put("_page", pageParam);
		PageResult boardList = boardService.findListPortalNoticeForGridPaging(param);
		PageResult biddingList = supplierBiddingService.findPagingBiddingNoticeList(param);

		ModelAndView model = new ModelAndView();
		model.addObject("boardList",boardList.get("content"));
		model.addObject("biddingList",biddingList.get("content"));
		model.addObject("boardId",cipherUtil.encrypt((String) param.get("bbd_uuid")));
		model.addObject("sysId", sysId);
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		model.setViewName("login/sp/kr/spLogin_kr");

		return model;
	}

	@RequestMapping(value = "spLoginEn.do")
	public ModelAndView spLoginPagePostEn(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/spLoginEn.do");
		}

		String sysId = request.getParameter("sysId");
		try {
			sysId = sharedService.getTenantId(sysId);
		} catch(RuntimeException e) {
			sysId = TenancyContextHolder.getDefaultTenant().getId();
		}

		sharedService.setTenant(sysId, request, response);

		Map<String, Object> param = new HashMap<String, Object>();
		// 외부에 노출 할 게시판 bbd_uuid 와 보여질 개수를 명시합니다.
		param.put("bbd_uuid", "10002");
		param.put("startRow", 1);
		param.put("endRow", 3);
		Map<String, Object> pageParam = Maps.newHashMap();
		pageParam.put("manualQuery", false);
		pageParam.put("end", 3);
		pageParam.put("page", 1);
		pageParam.put("start", 1);
		pageParam.put("size", 3);
		param.put("_page", pageParam);
		PageResult boardList = boardService.findListPortalNoticeForGridPaging(param);
		PageResult biddingList = supplierBiddingService.findPagingBiddingNoticeList(param);

		ModelAndView model = new ModelAndView();
		model.addObject("boardList",boardList.get("content"));
		model.addObject("biddingList",biddingList.get("content"));
		model.addObject("boardId",cipherUtil.encrypt((String) param.get("bbd_uuid")));
		model.addObject("sysId", sysId);
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		model.setViewName("login/sp/en/spLogin_en");

		return model;
	}

	/**
	 * 협력사 등록 페이지
	 *
	 * @return the model and view
	 */
	@RequestMapping(value = "newVendor.do")
	public ModelAndView newVendor(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.setViewName("/login/sp/newVendor");
		String locale = request.getParameter("locale");
		if(locale != null){
			model.addObject("_locale",request.getParameter("locale"));
		}
		return model;
	}


	@RequestMapping(value = "afterSpLogin.do", method = RequestMethod.GET)
	public ModelAndView afterSpLogin() throws JsonGenerationException, JsonMappingException, IOException {
		if(!isAuthenticated()) {
			return new ModelAndView("redirect:spLogin.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("mdi");
		model.addObject("useErrorUserMessage", useErrorUserMessage);
		model.addObject("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(), keyGenerator.getIterationCount(), parameterGenerator.getIv()));
		model.addObject("_cacheBust", statefulService.findCacheBustValue());
		model.addObject("_secParam", securityParamEncrypt);
		model.addObject("session_usr_id", Auth.getCurrentUserInfo() != null ? (String) Auth.getCurrentUserInfo().get("usr_id") : "");

		// 포탈 타입정보를 가져옴
		String portalType = "vueGrid";
		Map configMap = portalService.findPortalCommonConfig(null);
		if(configMap != null && configMap.get("value") != null) {
			String configJsonStr = (String) configMap.get("value");
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> configJson = null;
			int qIdx = configJsonStr.lastIndexOf("{");

			// ~ 1.0.3의 경우 (단순히 vueGrid, slider 만 값이 있는 경우)
			if(qIdx == -1) {
				portalType = configJsonStr;
			}
			// 1.0.3 ~ 1.0.5의 경우 ({layout_type: 'vueGrid', use_userlayout: 'Y'}) 처리
			else if(qIdx == 0) {
				configJson = mapper.readValue(configJsonStr, new TypeReference<Map<String, String>>(){});
				portalType = (String) configJson.get("layout_type");
			}

			// 1.0.5 ~ 의 경우 ({ 'B': {layout_type: 'vueGrid', use_userlayout: 'Y'}, ... }) 처리
			else if(qIdx > 0) {
				Map<String, Map<String, String>> configJsonTmp = mapper.readValue(configJsonStr, new TypeReference<Map<String, Map<String, String>>>(){});
				String currentUsrCls = (String) Auth.getCurrentUserInfo().get("usr_typ_ccd");
				configJson = configJsonTmp.get(currentUsrCls);
				portalType = (String) configJson.get("layout_type");
			}
		}
		model.addObject("_portalType", portalType);

		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		return model;
	}


	@RequestMapping(value = "spLoginFailure.do", method = RequestMethod.GET)
	public ModelAndView spLoginFailure() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/spLoginFailure");
		return model;
	}

	@RequestMapping("spLogoutSuccess.do")
	public ModelAndView spLogoutSuccessPage(HttpServletResponse response) {
		return new ModelAndView("login/sp/spLogoutSuccess");
	}

	@RequestMapping("spLogoutSuccessEn.do")
	public ModelAndView spLogoutSuccessEnPage(HttpServletResponse response) {
		return new ModelAndView("login/sp/en/spLogoutSuccess_en");
	}

	/**
	 * password 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the model and view
	 * @throws Exception the exception
	 * @Date : 2017. 12. 12
	 * @Method Name : findPassword
	 */
	@RequestMapping (value = "/**/login/sp/findPassword.do", method = RequestMethod.GET)
	public ModelAndView findPassword(@RequestParam Map param) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/findPassword");
		return model;
	}

	/**
	 * id 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the model and view
	 * @throws Exception the exception
	 * @Date : 2017. 12. 12
	 * @Method Name : findId
	 */
	@RequestMapping (value = "/**/login/sp/findId.do", method = RequestMethod.GET)
	public ModelAndView findId(@RequestParam Map param) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/findId");
		return model;
	}

	/**
	 * id 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the model and view
	 * @throws Exception the exception
	 * @Date : 2017. 12. 12
	 * @Method Name : findId
	 */
	@RequestMapping (value = "/**/login/sp/findIdEn.do", method = RequestMethod.GET)
	public ModelAndView findIdEn(@RequestParam Map param) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/en/findId_en");
		return model;
	}


	/**
	 * password 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the model and view
	 * @throws Exception the exception
	 * @Date : 2017. 12. 12
	 * @Method Name : findPassword
	 */
	@RequestMapping (value = "/**/login/sp/findPasswordEn.do", method = RequestMethod.GET)
	public ModelAndView findPasswordEn(@RequestParam Map param) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/en/findPassword_en");
		return model;
	}

	/**
	 * 협력사
	 *
	 * @author : daesung lee
	 * @param : the param
	 * @return ModelAndView
	 * @Date : 2019. 5. 17
	 * @Method Name : openCertSelectionPage
	 */
	@RequestMapping(value="openCertSelectionPage.do", method = RequestMethod.POST)
	public ModelAndView openCertSelectionPage(@RequestParam Map<String, Object> paramMap) {

		String installStatus    = (String) paramMap.get("installStatus"); // 한국전자인증 호출 순서
		ModelAndView mv = null;
		Map<String,Object> result = new HashMap<String,Object>();
		String certificatePopupPage = "";
		try{

			String pkiModule = supplierRootEventPublisher.findSignModule();

			if("KICA".equals(pkiModule)) {
				result = supplierUserService.checkBizRegNo(paramMap);

				if(Const.SUCCESS.equals(result.get(Const.RESULT_STATUS))) { //성공

					result = supplierUserService.getCertLoginSignValue(paramMap);

					certificatePopupPage = supplierRootEventPublisher.findCertificatePopupPage(true);

					mv = new ModelAndView(certificatePopupPage, result);
					mv.addObject(result);
				}else {
					mv = new ModelAndView("failCheckBizRegNo");
					result.put(Const.RESULT_STATUS, Const.FAIL);
					result.put(Const.RESULT_MSG, "사업자번호가 존재하지 않습니다.");
					mv.addObject("result", result);
					return mv;
				}

			}else if("CrossCert".equals(pkiModule)) {
				// 한국전자인증 (관리 프로그램 유/무 확인 후 인증서 선택창페이지로 이동)
				if("BEFORE".equals(installStatus)) {

					//사업자번호 확인
					result = supplierUserService.checkBizRegNo(paramMap);
					if(Const.SUCCESS.equals(result.get(Const.RESULT_STATUS))) { //성공
						// 인증서 관리 프로그램 설치 유/무 검사 페이지
						certificatePopupPage = supplierRootEventPublisher.findCertificatePopupPage(false);
						mv = new ModelAndView(certificatePopupPage);
						return mv;
					}else{
						mv = new ModelAndView("failCheckBizRegNo");
						result.put(Const.RESULT_STATUS, Const.FAIL);
						result.put(Const.RESULT_MSG, "사업자번호가 존재하지 않습니다.");
						mv.addObject("result", result);
						return mv;
					}
				}else if("COMPLETE".equals(installStatus)) {

					result = supplierUserService.getCertLoginSignValue(paramMap);
					result.put("serverCls", serverCls);
					certificatePopupPage = supplierRootEventPublisher.findCertificatePopupPage(true);
					mv = new ModelAndView(certificatePopupPage, result);
					return mv;
				}
			}
		}catch(CommonException e){
			LOG.error("#################EDOC ERROR#################");
			LOG.error("Exception msg : {}"   , e.getMessage());
			LOG.error("#################EDOC ERROR#################");
			mv = new ModelAndView("certLoginResult");
			result.put(Const.RESULT_STATUS, Const.FAIL);
			mv.addObject("result", result);
		}catch(Exception e){
			LOG.error("#################ERROR#################");
			LOG.error("Exception msg : {}"    ,e.getMessage());
			LOG.error("Exception cause : {}" , e.getCause());
			LOG.error("#################ERROR#################");
			mv = new ModelAndView("certLoginResult");
			result.put(Const.RESULT_STATUS, Const.FAIL);
			mv.addObject("result", result);
		}
		return mv;
	}

	/**
	 * 세션 정보 제거
	 *
	 * @return void
	 */
	private void clearSessionInfo(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		// 인증 삭제
		SecurityContextHolder.clearContext();
		// 세션삭제
		if(session != null) {
			session.invalidate();
		}
		// 쿠키 삭제
		for(Cookie cookie : request.getCookies()) {
			cookie.setMaxAge(0);
			cookie.setValue(null);
//            cookie.setPath("/");
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		}
	}

	/**
	 * 세션 확인(로그인 여부)
	 *
	 * @return boolean
	 */
	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken);
	}

	/**
	 * 인증서 로그인
	 *
	 * @author : daesung lee
	 * @return ModelAndView
	 * @throws Exception
	 * @Date : 2019. 5. 28
	 * @Method Name : completeCertLogin
	 */
	@RequestMapping(value="completeCertLogin.do", method=RequestMethod.POST)
	public ModelAndView completeCertLogin(HttpServletRequest request, @RequestParam Map<String,Object> paramMap) {
		ModelAndView mv = new ModelAndView("certLoginResult");
		Map<String,Object> result = new HashMap<String,Object>();

		try{
			//인증서에 사업자번호와 시스템 등록된 사업자번호를 비교 및 서명+인증서 검증로직
			String hashValue = supplierUserService.completeCertLogin(paramMap);
			result.put("hash_value", hashValue);
			result.put(Const.RESULT_STATUS, Const.SUCCESS);
		}catch(CommonException e){
			LOG.error("#################EDOC ERROR#################");
			LOG.error("Exception msg : {}"   , e.getMessage());
			LOG.error("#################EDOC ERROR#################");
			result.put(Const.RESULT_MSG, e.getMessage());
			result.put(Const.RESULT_STATUS, Const.FAIL);
		}catch(Exception e){
			LOG.error("#################ERROR#################");
			LOG.error("Exception msg : {}"   , e.getMessage());
			LOG.error("Exception trace : {}" , e.toString());
			LOG.error("#################ERROR#################");
			result.put(Const.RESULT_MSG, e.getMessage());
			result.put(Const.RESULT_STATUS, Const.FAIL);
		}catch(UnsatisfiedLinkError e){
			LOG.error("#################UnsatisfiedLinkError#################");
			LOG.error("Check CrossCert Configuration");
			LOG.error("error msg : {}"   , e.getMessage());
			LOG.error("error string: {}" , e.toString());
			LOG.error("#################UnsatisfiedLinkError#################");
			result.put(Const.RESULT_MSG, e.getMessage());
			result.put(Const.RESULT_STATUS, Const.FAIL);
		}
		mv.addObject("result", result);
		return mv;
	}

	/**
	 * 이용약관을 조회를 요청한다.
	 *
	 * @param : param the param
	 * @return the model and view
	 * @throws Exception the exception
	 * @Date : 2020. 04. 21
	 * @Method Name : findTerms
	 */
	@RequestMapping (value = "findTerms.do", method = RequestMethod.GET)
	public ModelAndView findTerms(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/sp/terms");
		if(null != request.getParameter("type")){
			model.addObject("type", request.getParameter("type"));
		}
		return model;
	}

	/**
	 *  로그인 이전에 팝업으로 게시판 모듈을 호출합니다.
	 */
	@RequestMapping(value = "noticeLink.do", method = RequestMethod.POST)
	public ModelAndView noticeLink(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> searchParam = Maps.newHashMap();
		String boardId = request.getParameter("bbd_uuid");
		String page = request.getParameter("page");
		String postNo = request.getParameter("pst_no");
		String sysId = request.getParameter("sys_id");
		String locale = request.getParameter("locale");

		try {
			searchParam.put("bbd_uuid", cipherUtil.decrypt(boardId));
			searchParam.put("sys_id", sysId);
			if(!StringUtils.isEmpty(postNo)){
				searchParam.put("pst_no", postNo);

				mv.addObject("content", boardService.getTemplateContent(searchParam));
				mv.addObject("boardId", boardId);
				mv.addObject("page", page);
				mv.setViewName("login/sp/noticePopup");
			}else{
				//page기본정보
				int totRow = boardService.getTotalBoardCount(searchParam);
				if(totRow > 0){
					searchParam.put("totalRow", totRow);
					searchParam.put("page", page);

					Map<String, Object> pageInfo = sharedService.getPageInfo(searchParam);
					Map<String, Object> pageParam = Maps.newHashMap();
					pageParam.put("manualQuery", false);
					pageParam.put("end", pageInfo.get("endRow"));
					pageParam.put("page", pageInfo.get("page"));
					pageParam.put("start", pageInfo.get("startRow"));
					pageParam.put("size", pageInfo.get("pageRows"));
					pageParam.put("_page", pageParam);
					pageParam.put("bbd_uuid", searchParam.get("bbd_uuid"));
					PageResult boardList = boardService.findListPortalNoticeForGridPaging(pageParam);
					mv.addObject("_page", pageInfo);
					mv.addObject("boardList", boardList.get("content"));
				}

				mv.addObject("boardId", boardId);
				if(locale != null && "en_US".equals(locale)){
					mv.setViewName("login/sp/en/noticeListPopup_en");
				}else{
					mv.setViewName("login/sp/noticeListPopup");
				}
			}
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mv;
	}

	/* 간편서명 메일 로그인화면 */
	public ModelAndView eformSignLogin(final HttpServletRequest request, final HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		ModelAndView model = new ModelAndView();
		String encryptedText = "";
		String keyValue = "";
		String[] params = null;
		String[] keyVal = new String[2];
		Map<String, Object> requestMap = Maps.newHashMap();

		try {
			encryptedText = cipherUtil.decrypt(request.getParameterMap().toString());
			params = encryptedText.split("&");
			for (int i = 0 ; i <  params.length ; i ++) {
				keyValue = params[i];
				keyVal = keyValue.split("=");
				requestMap.put(keyVal[0], keyVal[1]);
			}

			// 파라미터 없으면 오류페이지 이동
			if(Strings.isNullOrEmpty((String) requestMap.get("contractor_no"))
					|| Strings.isNullOrEmpty((String) requestMap.get("usr_id"))
					|| Strings.isNullOrEmpty((String) requestMap.get("usrCls"))
					|| Strings.isNullOrEmpty((String) requestMap.get("menuId"))) {
				return new ModelAndView("redirect:" + "/error.do");
			}

		} catch(Exception e) {
			return new ModelAndView("redirect:" + "/error.do"); // 파라미터 오류.
		}

		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		model.addObject("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(),
				keyGenerator.getIterationCount(), parameterGenerator.getIv()));
		model.addObject("usrCls", requestMap.get("usrCls"));
		model.addObject("menuId", requestMap.get("menuId"));
		model.addObject("appId", requestMap.get("contractor_no"));
		model.addObject("param", request.getParameterMap().toString());
		model.addObject("mobileYn", "N");
		model.setViewName("eformSignLogin");
		return model;
	}

	/* 간편서명 메일 로그인화면 */
	@RequestMapping(value = "eformSignLogin.do", method = RequestMethod.GET)
	public ModelAndView eformSignLoginIncludeMobile(final HttpServletRequest request, final HttpServletResponse response, Device device)
			throws JsonGenerationException, JsonMappingException, IOException {
		ModelAndView model = eformSignLogin(request, response);//PC버전

		// 모바일 분기처리
		if(device != null && device.isMobile()) {
			model.setViewName("eformMobileLogin");
		}

		return model;
	}

	/* 간편서명 메일 PW 입력 */
	@RequestMapping(value = "eformSignPwLogin.do", method = RequestMethod.GET)
	public ModelAndView eformSignPwLogin(final HttpServletRequest request, final HttpServletResponse response, Device device)
			throws JsonGenerationException, JsonMappingException, IOException {
		final ModelAndView model = new ModelAndView();
		String encryptedText = "";

		try {
			//encryptedText = request.getParameterMap().toString();
			//encryptedText = request.getParameter("p_link");
			encryptedText = request.getParameter("p_link");
			//request.setAttribute("param", encryptedText);
			if(encryptedText == null) {
				return new ModelAndView("redirect:" + "/error.do"); // 파라미터 오류.
			}
			LOG.info("encryptedText : " + encryptedText);
			model.addObject("eformSignParam", encryptedText);
		} catch(Exception e) {
			return new ModelAndView("redirect:" + "/error.do"); // 파라미터 오류.
		}

		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		model.addObject("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(),
				keyGenerator.getIterationCount(), parameterGenerator.getIv()));

		// 모바일 분기처리
		if(device != null && device.isMobile()) {
			model.setViewName("eformMobileLoginPw");
		}else {
			model.setViewName("eformSignPwLogin");
		}
		return model;
	}

	/* 간편서명 메일 PW 검증 */
	@RequestMapping(value = "eformSignPwLoginProcess.do", method = RequestMethod.POST)
	public ModelAndView eformSignPwAfterLogin(final HttpServletRequest request, final HttpServletResponse response) {


		//String en = cipherUtil.encrypt("test1");
		//String de = cipherUtil.decrypt(en);
		LOG.info("param :" + request.getParameter("param"));
		//String param = request.getParameter("param");
		//param = param.substring("p_link=".length() +1, param.length());

		final ModelAndView model = new ModelAndView();
		String decryptedText = "";
		String keyValue = "";
		String[] params = null;
		String[] keyVal = new String[2];
		Map<String, Object> requestMap = Maps.newHashMap();
		try {
			// 파라미터 없으면 오류페이지 이동
			if(Strings.isNullOrEmpty(request.getParameter("param"))) {
				return new ModelAndView("redirect:" + "/eformSignPwLogin.do");
			}
			AESCipherUtil aesCipherUtil = new AESCipherUtil();
			// param 복호화
			decryptedText = aesCipherUtil.decrypt(request.getParameter("param"));

			params = decryptedText.split("&");
			for (int i = 0 ; i <  params.length ; i ++) {
				keyValue = params[i];
				keyVal = keyValue.split("=");
				requestMap.put(keyVal[0], keyVal[1]);
			}

			// 파라미터 없으면 오류페이지 이동
			String reutnParam = request.getParameter("param").toString();
			reutnParam = reutnParam.substring(1).substring(0, reutnParam.length()-2);
			reutnParam = reutnParam.replace("+", "%2B");
			if(Strings.isNullOrEmpty((String) requestMap.get("contractor_no"))) {
				return new ModelAndView("redirect:" + "/eformSignPwLogin.do?"+reutnParam);
			}

			// 서명대상자ID로 계약정보 찾아오기
			Map<String, Object> eformUserInfo = eformLoginRepository.findEformUserInfo(requestMap);
			// 암호화 값 비교
			String telPw = passwordProvider.passwordEncryptor((String) eformUserInfo.get("tel"));
			if(passwordEncoder.matches(request.getParameter("password"), telPw)) {
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("eformMailAuthCheck", true);
				model.addObject("_secParam", securityParamEncrypt);
				model.addObject("contractor_no", requestMap.get("contractor_no"));
				model.addObject("mobileYn", request.getParameter("mobileYn"));

				model.addObject("originParam", request.getParameter("param"));
				model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
				model.addObject("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(),
						keyGenerator.getIterationCount(), parameterGenerator.getIv()));
				model.setViewName("eformSignPwAfterLogin");
			} else {
				String param = request.getParameter("param");
				param = URLEncoder.encode(param);
				return new ModelAndView("redirect:" + "/eformSignPwLogin.do?p_link="+param);
				//return new ModelAndView("redirect:" + "/eformSignPwLogin.do?"+reutnParam);
			}
		} catch(Exception e) {
			return new ModelAndView("redirect:" + "/error.do"); // 파라미터 오류.
		}
		return model;
	}

}
