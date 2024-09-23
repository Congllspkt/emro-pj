package smartsuite.security.authentication;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UsernameOnlyAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	@Inject
	public AuthenticationPostService authenticationPostService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException, BadCredentialsException {

//		authenticationPostService.authenticationFailure(exception.getAuthentication().getName());
		
		// 인증 실패시 추가적으로 처리할 로직 구현
		

		// 완료 후 /ssoFailure.do 로 리다이렉트 됨 
		
		super.onAuthenticationFailure(request, response, exception);
	}

}
