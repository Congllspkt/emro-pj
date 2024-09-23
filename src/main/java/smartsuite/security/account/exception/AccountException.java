package smartsuite.security.account.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountException extends AuthenticationException {

	private static final long serialVersionUID = -5556251270674387701L;

	public AccountException(String msg) {
		super(msg);
	}

}
