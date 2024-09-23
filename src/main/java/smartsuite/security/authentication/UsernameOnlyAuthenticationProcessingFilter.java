package smartsuite.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class UsernameOnlyAuthenticationProcessingFilter extends
		AbstractAuthenticationProcessingFilter {

	private boolean postOnly;
	
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	protected UsernameOnlyAuthenticationProcessingFilter(
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
		String username = obtainUsername(request);
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

	protected String obtainUsername(HttpServletRequest request) {
		// 요청에서 로그인 username 을 추출하여 리턴
		return request.getParameter("username");
		//TEST를 위해 ADMIN 셋팅
		//return "ADMIN";
	}

}
