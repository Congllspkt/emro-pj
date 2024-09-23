package smartsuite.app.bp.admin.todoManager.event;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.event.CustomSpringEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class ToDoManagerEventPublisher {

	@Inject
	ApplicationEventPublisher publisher;

	public List<Map<String, Object>> findListTodoUserSupplier(Map<String, Object> param) {
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findListTodoUserSupplier", param);
		publisher.publishEvent(event);

		if(event.getResult() != null) {
			return (List) event.getResult();
		} else {
			return Lists.newArrayList();
		}
	}
}
