package smartsuite.security.authentication;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class DefaultAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	@Inject
	public AuthenticationPostService authenticationPostService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException, BadCredentialsException {

		AuthenticationException authenticationException = authenticationPostService.authenticationFailure(request, response, exception);
		super.onAuthenticationFailure(request, response, authenticationException);
	}

}
