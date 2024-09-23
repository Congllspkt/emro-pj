package smartsuite.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class SsoAuthenticationProcessingFilter extends
		AbstractAuthenticationProcessingFilter {

	protected SsoAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	
	protected Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	protected boolean isAuthenticated() {
		Authentication authentication = getAuthentication();
		return !(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		
		// Session Check
        if(isAuthenticated()){
        	return getAuthentication();
        }
        
		String username = obtainUsername();
		if (username == null) {
			throw new UsernameNotFoundException(username);
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, "");
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	protected String obtainUsername() {
		// 프로젝트 SSO 처리 로직 구현 필수
		return null;
	}

}
