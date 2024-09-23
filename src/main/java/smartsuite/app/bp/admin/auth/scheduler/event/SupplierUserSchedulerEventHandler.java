package smartsuite.app.bp.admin.auth.scheduler.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.scheduler.service.SupplierUserSchedulerService;
import smartsuite.app.event.CustomSpringEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class SupplierUserSchedulerEventHandler {

	@Inject
	SupplierUserSchedulerService supplierUserSchedulerService;

	@EventListener(condition = "#event.eventId == 'findListDormantAccountForAllUser'")
	public void findListDormantAccountForAllUser(CustomSpringEvent event) {
		List dormantAccountUserList = supplierUserSchedulerService.findListDormantAccountForAllUser((Map<String, Object>) event.getData());
		event.setResult(dormantAccountUserList);
	}

	@EventListener(condition = "#event.eventId == 'findListDormantAccountForSupplier'")
	public void findListDormantAccountForSupplier(CustomSpringEvent event) {
		List dormantAccountUserList = supplierUserSchedulerService.findListDormantAccountForSupplier((Map<String, Object>) event.getData());
		event.setResult(dormantAccountUserList);
	}


}
