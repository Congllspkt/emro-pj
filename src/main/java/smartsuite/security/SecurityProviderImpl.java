package smartsuite.security;

import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import smartsuite.security.account.service.AccountService;
import smartsuite.security.userdetails.User;
import smartsuite.security.userdetails.UserDetailsProxy;

public class SecurityProviderImpl implements SecurityProvider {
	@Inject
	AccountService accountService;
	
	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			return (User) principal;
		}
		return null;
	}
	
	@Override
	public void refreshUserInfo(){
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetailsProxy proxy = (UserDetailsProxy)principal;
			
			Map userInfo = accountService.findUserSessionInfo(proxy.getUsername());
			proxy.setUserInfo(userInfo);
			
			authentication = new UsernamePasswordAuthenticationToken(proxy, authentication.getCredentials(), authentication.getAuthorities());
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
