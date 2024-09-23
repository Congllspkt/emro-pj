package smartsuite.security;

import smartsuite.security.userdetails.User;

public interface SecurityProvider {

	User getCurrentUser();
	
	void refreshUserInfo();
}
