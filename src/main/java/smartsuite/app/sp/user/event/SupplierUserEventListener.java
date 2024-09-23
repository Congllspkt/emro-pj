package smartsuite.app.sp.user.event;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.event.CustomSpringEvent;
import smartsuite.app.sp.user.repository.SupplierUserRepository;

import javax.inject.Inject;
import java.util.Map;

@Service
@Transactional
public class SupplierUserEventListener {
	@Inject
	SupplierUserRepository supplierUserRepository;

	@EventListener(condition = "#event.eventId == 'findVendorHashValue'")
	public void findVendorHashValue(CustomSpringEvent event) {
		Map param = Maps.newHashMap();

		param = (Map)event.getData();


		event.setResult(supplierUserRepository.findVendorHashValue(param));
	}

	@EventListener(condition = "#event.eventId == 'removeVendorHashValue'")
	public void removeVendorHashValue(CustomSpringEvent event) {
		Map param = Maps.newHashMap();

		param = (Map)event.getData();

		supplierUserRepository.removeVendorHashValue(param);
	}

}
