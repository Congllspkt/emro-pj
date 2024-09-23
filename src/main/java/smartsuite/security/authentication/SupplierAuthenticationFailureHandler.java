package smartsuite.security.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SupplierAuthenticationFailureHandler extends
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
