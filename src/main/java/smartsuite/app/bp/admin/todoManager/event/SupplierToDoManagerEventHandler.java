package smartsuite.app.bp.admin.todoManager.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.todoManager.service.SupplierToDoManagerService;
import smartsuite.app.event.CustomSpringEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class SupplierToDoManagerEventHandler {

	@Inject
	SupplierToDoManagerService supplierToDoManagerService;

	@EventListener(condition = "#event.eventId == 'findListTodoUserSupplier'")
	public void findListTodoUserSupplier(CustomSpringEvent event) {
		List dormantAccountUserList = supplierToDoManagerService.findListTodoUserSupplier((Map<String, Object>) event.getData());
		event.setResult(dormantAccountUserList);
	}


}
