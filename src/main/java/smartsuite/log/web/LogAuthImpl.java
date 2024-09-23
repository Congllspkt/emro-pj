package smartsuite.log.web;

import org.springframework.stereotype.Service;

import smartsuite.security.authentication.Auth;

@Service
public class LogAuthImpl implements ILogAuth{

	@Override
	public String getTenancy() {
		return Auth.getCurrentTenantId();
	}

	@Override
	public String getCompany() {
		if(Auth.getCurrentUserInfo() == null) {
			return null;
		} else {
			return (String)Auth.getCurrentUserInfo().get("co_cd");
		}
	}

	@Override
	public String getUsercls() {
		if(Auth.getCurrentUserInfo() == null) {
			return null;
		} else { 
			return (String)Auth.getCurrentUserInfo().get("usr_typ_ccd");
		}
	}

	@Override
	public String getUsername() {
		String username = Auth.getCurrentUserName();		
		if(username == null) {
			return "unknown";
		} else {
			return username;
		}	
	}

}
