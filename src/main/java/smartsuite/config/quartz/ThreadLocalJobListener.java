package smartsuite.config.quartz;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import smartsuite.spring.tenancy.context.TenancyContext;
import smartsuite.spring.tenancy.context.TenancyContextHolder;
import smartsuite.spring.tenancy.core.DefaultTenant;
import smartsuite.spring.tenancy.core.Tenant;

public class ThreadLocalJobListener implements JobListener {

	String LISTENER_NAME = "tenantJobListener"; //NOPMD
	
	@Override
	public String getName() {
		return LISTENER_NAME;
	}
	
	public void setName(String name) {
		LISTENER_NAME = name;
	}

	// 스케쥴러 실행 전(이미 다른 쓰레드로 호출된 상태)
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		
		String tenantId = (String)context.getMergedJobDataMap().get("tenId");
		Tenant tenant = new DefaultTenant().createInstance(tenantId);
		
		if(tenant != null && !StringUtils.isBlank(tenantId)){
			TenancyContext tenancyContext = TenancyContextHolder.createEmptyContext();
			tenancyContext.setTenant(tenant);
			TenancyContextHolder.setContext(tenancyContext); 
		}

		Authentication authentication = (Authentication) context.getMergedJobDataMap().get("authentication");
		if(authentication != null){
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			securityContext.setAuthentication(authentication);
			SecurityContextHolder.setContext(securityContext);
		}
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		//EMPTY METHOD
	}

	// 스케쥴러 실행 후
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		TenancyContextHolder.clearContext();
		SecurityContextHolder.clearContext();
	}

}
