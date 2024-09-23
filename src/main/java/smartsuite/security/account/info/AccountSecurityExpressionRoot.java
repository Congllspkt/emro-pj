package smartsuite.security.account.info;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.authentication.Auth;
import smartsuite.security.userdetails.User;

public class AccountSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations  {
	
	private List<String> allowIps;
	
	private AccountService accountService;

	private Object filterObject;
	
	private Object returnObject;
	
	private Object target;
	
	final static GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

	public AccountSecurityExpressionRoot(Authentication authentication) {
		super(authentication);
	}
	
	@Override
	public Object getThis() {
		return target;
	}
	
	public void setThis(Object target) {
		this.target = target;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
		this.allowIps = accountService.getAllowIps();
	}
	
	public boolean isRoleAdmin() {
		User user = Auth.getCurrentUser();
		if(accountService.getAccountSettings().isAdminAdditionalAuthentication()) {
			return user.getAuthorities().contains(ROLE_ADMIN);
		}
		return true;
	}
	
	public boolean isAllowIp() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = accountService.getClientIpAddress(request);
		if(allowIps.isEmpty()){
			return true;
		}else{
			return allowIps.contains(ip);
		}
	}
	
	public Object getFilterObject() {
		return filterObject;
	}

	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

}
