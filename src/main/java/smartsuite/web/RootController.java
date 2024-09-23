package smartsuite.web;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.common.portal.service.PortalService;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.stateful.service.StatefulService;
import smartsuite.security.authentication.Auth;
import smartsuite.security.core.authentication.encryption.salt.SaltSource;
import smartsuite.security.core.crypto.AESIvParameterGenerator;
import smartsuite.security.core.crypto.AESSecretKeyGenerator;
import smartsuite.security.web.authentication.encryption.salt.PasswordSalt;
import smartsuite.security.web.crypto.AESCipherKey;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class RootController {

	@Inject
	SaltSource saltSource;

	@Inject
	AESSecretKeyGenerator keyGenerator;

	@Inject
	AESIvParameterGenerator parameterGenerator;

	@Inject
	StatefulService statefulService;

	@Inject
	PortalService portalService;

	@Inject
	HttpSession httpSession;

	@Value("#{globalProperties['error.user.message']}")
	private Boolean useErrorUserMessage;

	@Value("#{globalProperties['sec.param.encrypt']}")
	private String securityParamEncrypt;

	private static final Logger LOG = LoggerFactory.getLogger(RootController.class);

	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public ModelAndView indexPage() throws JsonGenerationException, JsonMappingException, IOException {
		if(isAuthenticated()) {
			return new ModelAndView("redirect:/afterLogin.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		return model;
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, JsonGenerationException, JsonMappingException, IOException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/login.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("login/bp/login");
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		return model;
	}

	@RequestMapping(value = "/loginEn.do", method = RequestMethod.GET)
	public ModelAndView loginEnPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, JsonGenerationException, JsonMappingException, IOException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/loginEn.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("login/bp/en/login_en");
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		return model;
	}

	@RequestMapping(value = "/loginKr.do", method = RequestMethod.GET)
	public ModelAndView loginKrPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, JsonGenerationException, JsonMappingException, IOException {
		// 세션이 존재할 경우
		if(isAuthenticated()) {
			// 세션 정보 초기화
			this.clearSessionInfo(request, response);
			return new ModelAndView("redirect:/loginKr.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("login/bp/kr/login_kr");
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		return model;
	}


	@RequestMapping(value = "afterLogin.do", method = RequestMethod.GET)
	public ModelAndView loginAfterPage() throws JsonGenerationException, JsonMappingException, IOException {
		if(!isAuthenticated()) {
			return new ModelAndView("redirect:login.do");
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

	@RequestMapping(value = "afterSinglePageLogin.do", method = RequestMethod.GET)
	public ModelAndView ssoLoginAfterPage() throws JsonGenerationException, JsonMappingException, IOException {
		if(!isAuthenticated()) {
			return new ModelAndView("redirect:/login.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("app");
		model.addObject("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(), keyGenerator.getIterationCount(), parameterGenerator.getIv()));
		model.addObject("_cacheBust", statefulService.findCacheBustValue());

		// 에러 발생 시 사용자가 에러 정보 입력 여부
		model.addObject("useErrorUserMessage", useErrorUserMessage);
		// 파라미터 암호화 처리 여부
		model.addObject("_secParam", securityParamEncrypt);
		model.addObject("session_usr_id", Auth.getCurrentUserInfo() != null ? (String) Auth.getCurrentUserInfo().get("usr_id") : "");

		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		return model;
	}

	@RequestMapping(value = "loginFailure.do", method = RequestMethod.GET)
	public ModelAndView loginFailure() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/loginFailure");
		return model;
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


	@RequestMapping("logoutSuccess.do")
	public ModelAndView logoutSuccessPage(HttpServletResponse response) {
		return new ModelAndView("login/bp/logoutSuccess");
	}

	@RequestMapping("logoutSuccessEn.do")
	public ModelAndView logoutSuccessEnPage(HttpServletResponse response) {
		return new ModelAndView("login/bp/en/logoutSuccess_en");
	}

	@RequestMapping("invalidSession.do")
	public ModelAndView invalidSession(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("invalidSession");
	}

	@RequestMapping("sessionExpired.do")
	public ModelAndView sessionExpired(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("invalidSession");
	}

	@RequestMapping(value = "docs.do", method = RequestMethod.GET)
	public ModelAndView docsPage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("docs");
		return model;
	}

	@RequestMapping(value = "default.do", method = RequestMethod.GET)
	public ModelAndView defaultPage() throws JsonGenerationException, JsonMappingException, IOException {
		if(isAuthenticated()) {
			return new ModelAndView("redirect:/afterLogin.do");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("noSessionMdi");
		//패스워드 암호화에 사용할 솔트값 전달
		model.addObject("_passwordSaltSource", new PasswordSalt(saltSource.getSalt()));
		//메뉴를 조회할 usrCls 를 지정
		httpSession.setAttribute("usrCls", "T");

		return model;
	}

	/**
	 * Session Update를 수행한다.
	 * Dummy
	 */
	@RequestMapping(value = "/**/sessionTimeUpdate.do")
	public @ResponseBody Map sessionTimeUpdate(@RequestBody Map param){
		Map resultMap = Maps.newHashMap();
		resultMap.put(Const.RESULT_STATUS, Const.SUCCESS);

		return resultMap;
	}
}
