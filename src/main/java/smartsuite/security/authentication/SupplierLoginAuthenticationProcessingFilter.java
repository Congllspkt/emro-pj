package smartsuite.security.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SupplierLoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private String usernameParameter;

	private String passwordParameter;

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}

	private boolean postOnly = true;

	@Value ("#{loggingProperties['logging.lightLogging']}")
	boolean lightLogging;

	public void setPostOnly(boolean po){
		this.postOnly = po;
	}

	protected SupplierLoginAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		// login log 기록
		if (logger.isInfoEnabled()) {
			writeBeforeLogging(request);
		}

		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {

		super.successfulAuthentication(request, response, chain, authResult);

		// login log 기록
		if (logger.isInfoEnabled()) {
			writeAfterLogging(request);
		}
	};

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	private void writeBeforeLogging(HttpServletRequest request) {
		StringBuffer log = new StringBuffer();

		if (lightLogging) {
			log.append("begin request url ").append(request.getServletPath());
		} else {
			log.append("\r\n*************************************************************************\r\n current-memory                 = ")
			   .append(usingMemory()).append('/').append(totalMemory()).append("\r\n requested-session-id           = ").append(request.getRequestedSessionId())
			   .append("\r\n session-id                     = ").append(request.getSession().getId()).append("\r\n requested-session-id-valid     = ")
			   .append(request.isRequestedSessionIdValid()).append("\r\n request-url	                = ").append(request.getServletPath())
			   .append("\r\n*************************************************************************\r\n");
		}
		logger.info(log.toString());
	}

	private void writeAfterLogging(HttpServletRequest request) {
		StringBuffer log = new StringBuffer();

		if (lightLogging) {
			log.append("finish request ").append(request.getServletPath());
		} else {
			log.append("\r\n*************************************************************************\r\n finish reqeust\r\n requested-session-id           = ")
			   .append(request.getRequestedSessionId()).append("\r\n request-url	                = ")
			   .append(request.getServletPath()).append("\r\n*************************************************************************\r\n");
		}
		logger.info(log.toString());
	}

	/**
	 * 현재 사용 메모리
	 * 
	 * @return
	 */
	private static Long usingMemory() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	/**
	 * 전체 메모리
	 * 
	 * @return
	 */
	private static Long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

}
