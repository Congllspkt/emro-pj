package smartsuite.security.userdetails;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import smartsuite.security.account.service.AccountService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Transactional
public class SupplierUserDetailsService implements UserDetailsService {

	@Inject
	AccountService accountService;
	
	String accountEnabledName = "use_yn";
	
	String accountNonLockedName = "acct_lckd_yn";
	
	String accountCredentialsNonExpiredName = "pwd_chg_dttm";
	
	String accountCredentialsNonInitializedName = "pwd_chg_reqd_yn";
	
	boolean withoutPassword = false;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		ServletRequestAttributes servletAttr= (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String strLocale =   servletAttr.getRequest().getParameter("locale");
		
        if("" != strLocale && null != strLocale) {
        	String[] splitedLocale = strLocale.split("_");
        	Locale usrLocale = new Locale(splitedLocale[0], splitedLocale[1]);
        	LocaleContextHolder.setLocale(usrLocale);
        }
		
        
		User user = accountService.findUserNameAndPasswordForSupplier(username);

		if (user == null) {
			throw new UsernameNotFoundException(username + " id does not exist.");
		}
		//사용자 정보 조회
		Map<String,Object> userInfo = accountService.findSupplierUserSessionInfo(username);

		user.setUserInfo(userInfo);
		//사용자 롤 조회
		Collection<GrantedAuthority> authorities = accountService.findListRoleCodeByLoginUserSession(userInfo);

		user.setAuthorities(authorities);
		
		//사용여부
		user.setEnabled("Y".equals(userInfo.get(accountEnabledName)));
		//잠김여부
		user.setAccountNonLocked("N".equals(userInfo.get(accountNonLockedName)));

		if(withoutPassword) {
			//비밀번호 없이 로그인 하는 경우 만료여부 체크 안함
			user.setCredentialsNonExpired(true);
		} else if(null == userInfo.get(accountCredentialsNonExpiredName)){
			//pw_mod_dt가 null 일 경우 처리 방안
			user.setCredentialsNonExpired(false);
		} else {
			//비밀번호 만료여부
			user.setCredentialsNonExpired(accountService.isCredentialsNonExpired((Date)userInfo.get(accountCredentialsNonExpiredName)));
		}

		//비밀번호 초기화 여부
		user.setCredentialsNonInitialized("N".equals(userInfo.get(accountCredentialsNonInitializedName)));
		
		return new UserDetailsProxy(user);
	}
	
	public void setWithoutPassword(boolean value) {
		this.withoutPassword = value;
	}
	
}