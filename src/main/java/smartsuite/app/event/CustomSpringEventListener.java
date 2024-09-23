package smartsuite.app.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class CustomSpringEventListener {

    static final Logger LOGGER = LoggerFactory.getLogger(CustomSpringEventListener.class);
/*
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	//@Order(Ordered.HIGHEST_PRECEDENCE)
	public void orderHighest(CustomSpringEvent e) {
		LOGGER.info("[Start Event Call]"+e.getData()+e.getEventId());
        LOGGER.info("[Start Called service]"+ TransactionSynchronizationManager.getCurrentTransactionName());
	}*/

	/*@EventListener
	@Order(Ordered.LOWEST_PRECEDENCE)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void orderLowest(CustomSpringEvent e) {
		LOGGER.info("[EventListener End Event Call]"+e.getData()+e.getEventId());
        LOGGER.info("[EventListener End Called service]"+ TransactionSynchronizationManager.getCurrentTransactionName());
	}*/

	@EventListener
	@Order(Ordered.HIGHEST_PRECEDENCE)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void orderHighest(CustomSpringEvent e) {
		LOGGER.info("[EventListener Event Call] "+e.getEventId());
		LOGGER.info("[EventListener Event Call Data] "+e.getData());
		String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
		if(transactionName == null){
			 LOGGER.info("[EventListener Called service] 404 NO EventListener!!!!");
		}else{
			LOGGER.info("[EventListener Called service] "+ transactionName);
		}
	}
}
