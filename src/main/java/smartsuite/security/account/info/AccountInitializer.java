package smartsuite.security.account.info;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;

import smartsuite.security.account.service.AccountService;

public class AccountInitializer implements InitializingBean {
	
	@Inject
	AccountService accountService;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		//행위 전체를 accountService 에 위임함
		accountService.create();
		accountService.registerAccountDisableSchedule();
	}

}
