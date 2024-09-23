package smartsuite.security.authentication;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import smartsuite.security.account.exception.AccountException;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.userdetails.User;
import smartsuite.security.userdetails.UserDetailsProxy;

@Service
@Transactional
public class AuthenticationPostService {
	
	static final Logger LOG = LoggerFactory.getLogger(AuthenticationPostService.class);

	// 솔루션 관리자 롤
	private final static GrantedAuthority ADMIN_ROLE_CODE = new SimpleGrantedAuthority("A100");


	// 솔루션 슈퍼 관리자 롤
	private final static GrantedAuthority SUPER_ADMIN_ROLE_CODE = new SimpleGrantedAuthority("SA100");

	@Inject
	SqlSession sqlSession;
	
	@Inject
	AccountService accountService;
	
	@Inject
	MessageSource messageSource;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	String passwordInvalidCountName = "pwd_inperr_cnt";
	
	String accountLockName = "acct_lckd_yn";
	
	String accountIdName = "usr_id";
	
	@SuppressWarnings("deprecation")
	public AuthenticationException authenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		//존재하지 않는 사용자
		if(exception instanceof UsernameNotFoundException) {
			LOG.debug("User not found");
			return new BadCredentialsException(messageSource.getMessage("STD.SEC1000", null, "아이디 또는 비밀번호를 다시 확인하시기 바랍니다. 등록되지 않은 아이디이거나, 비밀번호를 잘못 입력하셨습니다.", LocaleContextHolder.getLocale()));
		}
		//패스워드 오류
		else if(exception instanceof BadCredentialsException) {
			int limitLoginAttempsCountByInvalidPassword = accountService.getAccountSettings().getLimitLoginInvalidPasswordCount();
			if(limitLoginAttempsCountByInvalidPassword > 0) {
				String username = request.getParameter("username");
				Map<String,Object> authInfo = accountService.findUserAuthenticationInfo(username);
				
				int passwordMissCount = 0;
				if(authInfo.get(passwordInvalidCountName) != null) {
					passwordMissCount = Integer.parseInt(authInfo.get(passwordInvalidCountName).toString());
				}
				Map<String,Object> param = Maps.newHashMap();
				param.put(accountIdName, username);
				param.put(passwordInvalidCountName, ++passwordMissCount);
				//5회가 되지 않으면 카운트 업데이트
				if(passwordMissCount < limitLoginAttempsCountByInvalidPassword) {
					param.put(accountLockName, "N");
				}
				//계정잠금
				else {
					param.put(accountLockName, "Y");
				}
				accountService.updateUserAuthenticationFailCountAndAccountLock(param);
			}
			LOG.debug("User account password is wrong");
			return new BadCredentialsException(messageSource.getMessage("STD.SEC1000", null, "아이디 또는 비밀번호를 다시 확인하시기 바랍니다. 등록되지 않은 아이디이거나, 비밀번호를 잘못 입력하셨습니다.", LocaleContextHolder.getLocale()));
		}
		return exception;
	}
	
	public void authenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

		Map<String,String> param = new HashMap<String, String>();
		String clientIp = accountService.getClientIpAddress(request);
		param.put("last_login_ip", clientIp);
		
		// 로그인한 클라이언트 IP, 로그인 일시
		accountService.updateUserLoginClientIpAndDate(param);
		

	}	
	
	public void authenticationUpdate() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 현재 로그인 성공한 user 객체 조회
		Object principal = authentication.getPrincipal();
		UserDetailsProxy proxy = null;
		if (principal instanceof UserDetails) {
			proxy = (UserDetailsProxy)principal;
		}
		
		if(proxy != null) {
			//new Info 
			Map userInfo = accountService.findUserSessionInfo((String)proxy.getUserInfo().get("usr_id"));

			proxy.setUserInfo(userInfo);

			//new Session Update
			Authentication newAuth = new UsernamePasswordAuthenticationToken(proxy, authentication.getCredentials(), authentication.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
	}


	public Map<String, Object> checkRoleAdminAuthenticate(HttpServletRequest request, Map<String, String> param) {
		User user = Auth.getCurrentUser();
		if (!passwordEncoder.matches(param.get("pw"), user.getPassword())) {
			throw new AccountException("관리자 비밀번호와 일치하지 않습니다.");
		}
		if (!user.getAuthorities().contains(SUPER_ADMIN_ROLE_CODE) && !user.getAuthorities().contains(ADMIN_ROLE_CODE)) {
			throw new AccountException("접속한 계정이 관리자 역할을 가지고 있지 않습니다.");
		}
		String ip = accountService.getClientIpAddress(request);
		List<String> allowIps = accountService.getAllowIps();
		if (!allowIps.isEmpty() && !allowIps.contains(ip)) {
			throw new AccountException("관리자 페이지 접속허가를 받지 않은 IP 주소 입니다.");
		}

		Collection<GrantedAuthority> authorities = user.getAuthorities();
		if (authorities == null) {
			authorities = Lists.newArrayList();
			user.setAuthorities(authorities);
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		this.authenticationUpdate();

		Map<String, Object> result = Maps.newHashMap();
		result.put("authenticated", true);
		return result;
	}


}