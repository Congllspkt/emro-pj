package smartsuite.app.bp.admin.auth.scheduler.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.event.CustomSpringEvent;

import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class UserSchedulerEventPublisher {

	@Autowired
	ApplicationEventPublisher publisher;


	public List<Map<String, Object>> findListDormantAccountForAllUser(Map<String, Object> userDormantInfo) {
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findListDormantAccountForAllUser", userDormantInfo);
		publisher.publishEvent(event);

		if(event.getResult() != null) {
			return (List) event.getResult();
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> findListDormantAccountForSupplier(Map<String, Object> userDormantInfo) {
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findListDormantAccountForSupplier", userDormantInfo);
		publisher.publishEvent(event);

		if(event.getResult() != null) {
			return (List) event.getResult();
		} else {
			return null;
		}
	}
}
