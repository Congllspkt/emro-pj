package smartsuite.security.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


import smartsuite.app.common.shared.service.SharedService;
import smartsuite.security.event.CertificateEventPublisher;

public class CertificateAuthenticationProcessingFilter extends
		AbstractAuthenticationProcessingFilter {
	
	@Inject
	SharedService sharedService;

	@Inject
	CertificateEventPublisher certificateEventPublisher;
	
	private boolean postOnly;
	
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	protected CertificateAuthenticationProcessingFilter(
			String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: "
							+ request.getMethod());
		}
		String username = obtainUsername(request, response);
		if (username == null) {
			throw new UsernameNotFoundException(username);
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, "");

		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected void setDetails(HttpServletRequest request,
			UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource
				.buildDetails(request));
	}

	protected String obtainUsername(HttpServletRequest request, HttpServletResponse response) {
	
		// 요청에서 로그인 username 을 추출하여 리턴
		String loginType = request.getParameter("login_type");
		String certLoginTenant = request.getParameter("cert_login_tenant");
		
		if(StringUtils.isNotBlank(certLoginTenant)) {
			try {
				certLoginTenant = sharedService.getTenantId(certLoginTenant);
			} catch(RuntimeException e) {
				return "";
			}
			
			sharedService.setTenant(certLoginTenant, request, response);
		}
		
		if("certLogin".equals(loginType)) { //협력사 공인인증서 로그인
			String hashValue = request.getParameter("hash_value");
			String userName = request.getParameter("username");
			String vendorSignValue = "";

				
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("usr_id",userName);

			vendorSignValue = certificateEventPublisher.findVendorHashValue(userName);

			certificateEventPublisher.verifySignValue(vendorSignValue);

			String convertHashValue = certificateEventPublisher.getHashValueFromStr(vendorSignValue);

			if(hashValue.equals(convertHashValue)) {
				//로그인 성공이니 공인인증서 서명 후 저장해 놓은 radom_value 값 초기화
				certificateEventPublisher.removeVendorHashValue(userName);
				//sqlSession.update("supplier-user.removeVendorHashValue",param);
				return userName;
			}else {
				return "";
			}
		}
		
		// 요청에서 로그인 username 을 추출하여 리턴
		return "";
		//TEST를 위해 ADMIN 셋팅
		//return "ADMIN";
	}

}
