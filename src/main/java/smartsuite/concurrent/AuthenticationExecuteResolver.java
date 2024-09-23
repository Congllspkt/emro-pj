package smartsuite.concurrent;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import smartsuite.spring.concurrent.core.ExecuteResolver;

public class AuthenticationExecuteResolver implements ExecuteResolver {

	private final String NAME = AuthenticationExecuteResolver.class.getName();
	
	@Override
	public void prepareExecute(Map<String, Object> context) {
		context.put(NAME, SecurityContextHolder.getContext().getAuthentication());
	}

	@Override
	public void beforeExecute(Map<String, Object> context) {
		Authentication authentication = (Authentication) context.get(NAME);
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Override
	public void afterExecute(Map<String, Object> context) {
		SecurityContextHolder.clearContext();
	}

}
