package smartsuite.app.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class EventFactory implements ApplicationContextAware {

	ApplicationContext actx;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		actx = applicationContext;
	}
	
	public EventInterface getApprovalStrategy(String eventType) {
		EventInterface strategy = null;
		if ("SRM".equals(eventType)) { // 구매요청
			strategy = (EventInterface)actx.getBean("srm");
		}
		return strategy;
	}

}
