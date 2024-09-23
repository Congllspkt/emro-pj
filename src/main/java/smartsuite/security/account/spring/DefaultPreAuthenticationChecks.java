package smartsuite.security.account.spring;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import smartsuite.app.common.message.MessageUtil;

public class DefaultPreAuthenticationChecks implements UserDetailsChecker {

	static final Logger LOG = LoggerFactory.getLogger(DefaultPreAuthenticationChecks.class);
	@Inject
	MessageUtil messageUtil;

	@Override
	public void check(UserDetails user) {
		if (!user.isAccountNonLocked()) {
            LOG.debug("User account is locked");
			MessageUtil.MessageBean messageBean = new MessageUtil.MessageBean();
			messageBean.setCodeName("STD.SEC1001");
			messageBean.setDefaultMessage("계정이 잠겨 있습니다. 관리자에게 문의하시기 바랍니다.");

            throw new LockedException(messageUtil.getCodeMessage(messageBean));
		}
        if (!user.isEnabled()) {
        	LOG.debug("User account is disabled");
			MessageUtil.MessageBean messageBean = new MessageUtil.MessageBean();
			messageBean.setCodeName("STD.SEC1002");
			messageBean.setDefaultMessage("계정이 사용정지 되었습니다. 관리자에게 문의하시기 바랍니다.");

            throw new DisabledException(messageUtil.getCodeMessage(messageBean));
        }
        if (!user.isAccountNonExpired()) {
        	LOG.debug("User account is expired");
			MessageUtil.MessageBean messageBean = new MessageUtil.MessageBean();
			messageBean.setCodeName("STD.SEC1003");
			messageBean.setDefaultMessage("사용자 계정이 만료되었습니다. 관리자에게 문의하시기 바랍니다.");

            throw new AccountExpiredException(messageUtil.getCodeMessage(messageBean));
        }
	}

}
