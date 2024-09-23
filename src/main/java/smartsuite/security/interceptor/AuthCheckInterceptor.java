package smartsuite.security.interceptor;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import smartsuite.app.bp.admin.auth.service.RoleService;
import smartsuite.security.annotation.AuthCheck;
import smartsuite.security.authentication.Auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class AuthCheckInterceptor implements HandlerInterceptor {

	@Value("#{globalProperties['activate.url.gathering']}")
	private String activateUrlGathering;

	@Value("#{globalProperties['activate.url.gathering.authcode.mapping']}")
	private String activateUrlAuthCodeMappingGathering; //NOPMD

	@Value("#{globalProperties['validate.url.with.auth']}")
	private String validateUrlWithAuthCode;

	@Value("#{globalProperties['validate.url']}")
	private String validateUrl;


	@Value("#{globalProperties['validate.excludedList'].split(',')}")
	List<String> excludedList;


	@Inject
	RoleService roleService;

    //주로 preHandle()에 로직을 작성할 것이다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod handlerMethod = null;

		if (handler instanceof HandlerMethod) {
        	handlerMethod = (HandlerMethod) handler;
        	// Handle the request with the handlerMethod
		} else {
			// Handler is not a HandlerMethod instance
			return true;
		}

		String menuCode = request.getHeader("menucode");

		/*
		 *  해당메뉴가 아닌 공용으로 사용하는 controller에서 메뉴코드 기준으로 해당 역할 체크하여 허용함.
		 */
		// 현재 호출된 핸들러 메서드에 해당 어노테이션이 존재하는지 체크
		AuthCheck authCheckAnnotation = handlerMethod.getMethodAnnotation(AuthCheck.class);
		// 현재 호출된 핸들러 클래스에 해당 어노테이션이 존재하는지 체크
		authCheckAnnotation = (authCheckAnnotation == null) ? handlerMethod.getBeanType().getAnnotation(AuthCheck.class) : authCheckAnnotation;
		if(authCheckAnnotation != null && (authCheckAnnotation.fixedMenuCode() != null &&  !"".equals(authCheckAnnotation.fixedMenuCode()))){
			// 호출된 핸들러의 어노테이션에서 메뉴코드 존재하는지 체크(공용으로 사용하는 컨트롤러)
			menuCode = authCheckAnnotation.fixedMenuCode();
		}

		boolean isPass = false;
		boolean isGathering = false;
//		String authCode = "";
		RequestMapping requestMappingAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
		String [] urls = requestMappingAnnotation.value();
		String url = urls[0];
		if(!!!StringUtils.isEmpty(url)){
			url = url.substring(url.lastIndexOf("/")+1);
		}

		/*
		 * request가 ajax 요청이 아닐 경우 이후 로직 수행하지 않고 바이패스
		 */
		RequestMatcher requestMatcher = new ELRequestMatcher("hasHeader('X-Requested-With', 'XMLHttpRequest')");
		RequestMatcher requestUrlPatenMatcher = new AntPathRequestMatcher("/**/upload/**");
//		if(!requestMatcher.matches(request) || (menuCode == null || "".equals(menuCode) || "undefined".equals(menuCode))) {
		if(!requestMatcher.matches(request)) {
			if(requestUrlPatenMatcher.matches(request)){
				//SFNSUPP-3009
				if(menuCode == null && ("download.do".equals(url) || "downloadZip.do".equals(url))){
					menuCode = request.getParameter("menucode");
				}
			}else{
				return true;/* 바이패스 */
			}
		}

		/**
		 * smartsuite.app 패키지, exclude에 선언된 controller인 경우 제외
		 */
		if(isExcludedAccessLog(handlerMethod))
			return true;

		// 수집 로직
		if("true".equals(activateUrlGathering) && !!!(menuCode == null || "".equals(menuCode) || "undefined".equals(menuCode))) {
			Map<String,String> pattern = Maps.newHashMap();
			pattern.put("menu_cd", menuCode);
			pattern.put("menu_act_url", url);
			if(!roleService.hasCallPattern(pattern)) {
				roleService.saveCallPattern(pattern);
				//isGathering = true; //gathering시 역할체크 안해서 다시 재접속때 역할체크하므로 모호해서 일단 빼고 테스트
			}
		}


		/**
		 * url과 메뉴만 체크하여 유효한 호출인지 파악 함.
		 */
		if("true".equals(validateUrl) && !!!isGathering) {
			/*
			 * authCheck 어노테이션이 존재하지 않는 경우
			 *  - menucode에 매핑되어 있는 url일 경우에만 통과
			 */

			Map<String,String> pattern = Maps.newHashMap();
			pattern.put("menu_act_url", url);
			if(!(menuCode == null || "".equals(menuCode) || "undefined".equals(menuCode))){
				pattern.put("menu_cd", menuCode);
			}

			List<Map<String,Object>> urlMenuFuncs = roleService.hasRoleWithUrl(pattern);
			//메뉴코드가 존재하면서 url이 없는 경우는 관리화면에서 적용 안된것으로 판단하여 false를 반환
			if(!(menuCode == null || "".equals(menuCode) || "undefined".equals(menuCode)) && (urlMenuFuncs == null || urlMenuFuncs.size() == 0))
				return false;

			for(Map<String,Object> urlMenu : urlMenuFuncs){
				if(urlMenu.get("menu_cd")!=null && urlMenu.get("menu_act_url")!=null
						&& Auth.hasRoleWithUrl(String.valueOf(urlMenu.get("menu_cd")), String.valueOf(urlMenu.get("menu_act_url")))){
					isPass = true;
					break;
				}
			}

			if(isPass && "true".equals(validateUrlWithAuthCode)) {
				isPass = false;
				for(Map<String,Object> urlMenu : urlMenuFuncs){
					if(Auth.hasRoleWithUrlAndAuthCode(String.valueOf(urlMenu.get("menu_cd")), String.valueOf(urlMenu.get("menu_act_url")), String.valueOf(urlMenu.get("act_cd")))){
						isPass = true;
						break;
					}
				}
			}

			if(!isPass) {
				response.setStatus(401);
				return false;
			}



			/*
			 * 어노테이션 사용시 괄호 안에 인수로 입력한 값을 활용하는 예시 (예: @AuthCheck(authCode = Const.READ))

			if(authCheckAnnotation == null){
				response.setStatus(401);
				return false;
			}

			authCode = authCheckAnnotation.authCode();
			if(authCode != null && !"".equals(authCode)){

				// 수집 로직
				if("true".equals(activateUrlAuthCodeMappingGathering)) {
					Map<String,String> pattern = Maps.newHashMap();
					pattern.put("menu_cd", menuCode);
					pattern.put("menu_act_url", url);
					pattern.put("act_cd", authCode);
					if(!roleService.hasUrlAuthcodeMapping(pattern)) {
						roleService.saveUrlAuthcodeMapping(pattern);
						isGathering = true;
					}
				}
//				System.out.println("auth cheker :::::::::: auth="+authCode);
				isPass = Auth.hasRole(menuCode, authCode);

			}
			 */


			/*
			 * authCheck 어노테이션이 존재하는 경우
			 *  - menucode에 url과 authCode이 매핑된 경우에만 통과

			if("true".equals(validateUrlWithAuthCode)) {
				isPass = Auth.hasRoleWithUrlAndAuthCode(menuCode, url, authCode);
			}
			 */
		}



        return true; //preHandle()에서 false를 리턴하면 DispatcherServlet은 Interceptor가 알아서 response를 처리했다고 간주한다.
	}


	/**
	 * access logging 제외 여부
	 * @param handlerMethod
	 * @return
	 */
	private boolean isExcludedAccessLog(HandlerMethod handlerMethod){
		String candidate = handlerMethod.getMethod().toString();
		String className = handlerMethod.getBean().getClass().getName();
		String packageName = handlerMethod.getBean().getClass().getPackage().getName();
		//config.do 예외처리, upload 쪽 예외처리
		if(!(packageName.startsWith("smartsuite.app.") || (packageName.startsWith("smartsuite.upload.web") && !(className.indexOf("UploadConfigurationController") > -1))))
				return true;

		for (String excluded : excludedList) {
			if(candidate.contains(excluded)) {
				return true;
			}
		}

		return false;
	}
}
