package smartsuite.config.quartz;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import smartsuite.scheduler.core.JobDataMapHandler;
import smartsuite.spring.tenancy.context.TenancyContext;
import smartsuite.spring.tenancy.context.TenancyContextHolder;
import smartsuite.spring.tenancy.core.Tenant;

import java.util.Locale;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TenancyJobDataMapHandler implements JobDataMapHandler {

	@Override
	public void handleJobData(Class<?> bean, String method, boolean isCronTrigger, Map jobData) {
		
		// Tenant 정보
		TenancyContext tenancyContext = TenancyContextHolder.getContext();
		if (tenancyContext != null) {
			Tenant tenant = tenancyContext.getTenant();
			if (tenant != null) {
				jobData.put("tenId", tenant.getId());
			}
		}
		
		// Authentication 정보
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext != null){
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				jobData.put("authentication", authentication);
			}
		}

		// 로케일 정보
		Locale locale = LocaleContextHolder.getLocale();
		if(locale != null){
			jobData.put("locale", locale);
		}
	}

}
