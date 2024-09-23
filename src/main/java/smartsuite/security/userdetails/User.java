package smartsuite.security.userdetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	static final long serialVersionUID = -9147876361743024716L;

	String username;

	String password;

	//smartsuite9.x 에서 계정의 expired 는 사용하지 않음
	boolean accountNonExpired = true;

	boolean accountNonLocked;

	boolean credentialsNonExpired;
	
	boolean credentialsNonInitialized = true;

	boolean enabled;

	Collection<GrantedAuthority> authorities;
	
	Map<String,Object> userInfo;
	
	List<Map<String,Object>> userRoles;
	
	List<Map<String,Object>> userOperationOrganizationCodes;
	
	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isCredentialsNonInitialized() {
		return credentialsNonInitialized;
	}

	public void setCredentialsNonInitialized(boolean credentialsNonInitialized) {
		this.credentialsNonInitialized = credentialsNonInitialized;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	public List<Map<String, Object>> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<Map<String, Object>> userRoles) {
		this.userRoles = userRoles;
	}
	
	public List<Map<String, Object>> getUserOperationOrganizationCodes() {
		return userOperationOrganizationCodes;
	}
	
	public void setUserOperationOrganizationCodes(List<Map<String, Object>> userOperationOrganizationCodes) {
		this.userOperationOrganizationCodes = userOperationOrganizationCodes;
	}
	
}
