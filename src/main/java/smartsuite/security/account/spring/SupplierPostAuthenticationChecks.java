package smartsuite.security.account.spring;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import smartsuite.app.common.message.MessageUtil;
import smartsuite.security.account.service.AccountService;

import javax.inject.Inject;
import java.util.List;

public class SupplierPostAuthenticationChecks implements UserDetailsChecker {
	
	static final Logger LOG = LoggerFactory.getLogger(SupplierPostAuthenticationChecks.class);
	
	@Inject
	AccountService accountService;
	
	@Inject
	MessageUtil messageUtil;
	
	@Override
	public void check(UserDetails user) {
		//패스워드 만료시 로그인 실패가 아닌 로그인 후 변경하도록 유도함
		if(!user.isCredentialsNonExpired() && accountService.getAccountSettings().isAccountPasswordExpiredThrowException()) {
			LOG.debug("User account credentials have expired");

			List<Object> messageList = Lists.newArrayList();
			messageList.add(user.getUsername());
			messageList.add(accountService.getAccountSettings().getPasswordExpiredPeriod());

			MessageUtil.MessageBean messageBean = new MessageUtil.MessageBean();
			messageBean.setCodeName("STD.SEC1004");
			messageBean.setReplaceMessageList(messageList);
			messageBean.setDefaultMessage("{0}님은 {1}개월동안 비밀번호를 변경하지 않았습니다. 비밀번호를 변경해 주시길 바랍니다.");

			throw new CredentialsExpiredException(messageUtil.getCodeMessage(messageBean));
		}
	}

}
