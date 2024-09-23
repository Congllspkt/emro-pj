package smartsuite.security.account.service;

import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.security.account.info.AccountSettings;
import smartsuite.security.account.repository.AccountRepository;
import smartsuite.exception.CommonException;
import smartsuite.scheduler.core.ScheduleService;
import smartsuite.security.account.repository.UserSessionRepository;
import smartsuite.security.authentication.Auth;
import smartsuite.security.userdetails.User;

@Service
@Transactional
public class AccountService  {

	@Inject
	ScheduleService scheduleService;

	@Inject
	AccountRepository accountRepository;

	@Inject
	UserSessionRepository userSessionRepository;

	public AccountSettings load() {
		return accountRepository.load();
	}

	public void update(AccountSettings accountSettings) {
		accountRepository.update(accountSettings);
	}

	public AccountSettings create() {
		AccountSettings accountSettings = this.load();
		if(accountSettings == null) {
			AccountSettings defaultAccountSettings = new AccountSettings();
			accountRepository.create(defaultAccountSettings);
			return defaultAccountSettings;
		} else {
			return accountSettings;
		}
	}



	public List<Map<String,String>> ipAddressLoad() {
		return accountRepository.ipAddressLoad();
	}

	public void ipAddressSave(List<Map<String, String>> newItems) {
		for(Map<String,String> item : newItems) {
			accountRepository.ipAddressSave(item);
		}
	}

	public void ipAddressDelete(List<Map<String, String>> deleteItems) {
		for(Map<String,String> item : deleteItems) {
			accountRepository.ipAddressDelete(item);
		}
	}
	
	public boolean isCredentialsNonExpired(Date lastModifyDate) {
		AccountSettings accountSettings = this.load();
		int passwordExpiredPeriod = accountSettings.getPasswordExpiredPeriod();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -passwordExpiredPeriod);
		return lastModifyDate.after(calendar.getTime());
	}

	public String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP"); // WebLogic
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (this.ipNullCheck(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private boolean ipNullCheck(String ip){
		return (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"));
	}


	//허용 ip 조회를 전역 변수에 선언하지 못하므로 필요 시 실시간 조회로 변경
	public List<String> getAllowIps() {
		List<String> allowIps = Lists.transform(this.ipAddressLoad(), new Function<Map<String,String>, String>() {
			@Override
			public String apply(Map<String, String> input) {
				return input.get("mgr_ip");
			}
		});
		return allowIps;
	}

	//타 로직에서 accountSettings 필요한 시점에 실시간 load 수행으로 변경
	public AccountSettings getAccountSettings() {
		return this.load();
	}


	//AccountInitializer 에서 수행하던 부분을 registAccountDisableSchedule 에 포함시켜 로직 위임
	public void registerAccountDisableSchedule() {
		AccountSettings accountSettings = this.load();
		if(accountSettings.isDisableOnSpecifiedDate() && accountSettings.getAccountDisableForSpecifiedDate().after(new Date())) {
			scheduleService.removeSchedule("accountDisable", "account");
			try {
				scheduleService.registSchedule(
						Class.forName(this.getClass().getName()),
						"accountDisable",
						null,
						accountSettings.getAccountDisableForSpecifiedDate(),
						"account",
						"accountDisable",
						"accountDisable");
			} catch (ClassNotFoundException e) {
				throw new CommonException(e.getMessage());
			}
		}
	}

	//accountSettings 조회 후 로직 수행
	public void accountDisable() {
		AccountSettings accountSettings = this.load();
		accountRepository.accountDisable(accountSettings.getAccountDisableByLastLoginDate());
	}


	public Map findUserSessionInfo(String userId) {
		return userSessionRepository.findUserSessionInfo(userId);
	}

	public User findUserNameAndPassword(String username) {
		return userSessionRepository.findUserNameAndPassword(username);
	}

	public Map<String, Object> findUserAuthenticationInfo(String username) {
		return userSessionRepository.findUserAuthenticationInfo(username);
	}

	public void updateUserAuthenticationFailCountAndAccountLock(Map<String, Object> param) {
		userSessionRepository.updateUserAuthenticationFailCountAndAccountLock(param);
	}

	public void updateUserLoginClientIpAndDate(Map<String, String> param) {
		userSessionRepository.updateUserLoginClientIpAndDate(param);
	}

	public Collection<GrantedAuthority> findListRoleCodeByLoginUserSession(Map<String, Object> userInfo) {
		return userSessionRepository.findListRoleCodeByLoginUserSession(userInfo);
	}

	public Map<String, Object> getSessionUser() {
		User user = Auth.getCurrentUser();
		Map<String,Object> sessionUser = Maps.newHashMap();
		if(user != null) {
			sessionUser.put("credentialsNonExpired", user.isCredentialsNonExpired());
			sessionUser.put("credentialsNonInitialized", user.isCredentialsNonInitialized());
			sessionUser.put("userInfo", user.getUserInfo());
			sessionUser.put("accountSettings", this.getAccountSettings());
			sessionUser.put("authorities", user.getAuthorities());
		}
		return sessionUser;
	}

	public Map<String, Object> findSupplierUserSessionInfo(String username) {
		return userSessionRepository.findSupplierUserSessionInfo(username);
	}

	public User findUserNameAndPasswordForSupplier(String username) {
		return userSessionRepository.findUserNameAndPasswordForSupplier(username);
	}
}
