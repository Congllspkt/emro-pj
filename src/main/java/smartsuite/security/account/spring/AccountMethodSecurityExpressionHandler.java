package smartsuite.security.account.spring;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import smartsuite.security.account.info.AccountSecurityExpressionRoot;
import smartsuite.security.account.service.AccountService;

public class AccountMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler   {
	
	@Inject
	AccountService accountService;
	
	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	@Override
    public MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication auth, MethodInvocation mi) {
		final AccountSecurityExpressionRoot root = new AccountSecurityExpressionRoot(auth);
		root.setAccountService(accountService);
		root.setThis(mi.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(trustResolver);
		root.setRoleHierarchy(getRoleHierarchy());
		return root;
    }
}
