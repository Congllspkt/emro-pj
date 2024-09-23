package smartsuite.app.sp.login.bidding.event;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import smartsuite.app.event.CustomSpringEvent;

import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class SupplierRootEventPublisher {

	@Autowired
	ApplicationEventPublisher publisher;

	/**
	 * pki 모듈 확인
	 *
	 * @param : void
	 * @return : String
	 */
	public String findSignModule() {
		Map eventParam = Maps.newHashMap();
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findSignModule", eventParam);
		publisher.publishEvent(event);
		return (String)event.getResult();
	}

	/**
	 * pki 모듈 확인
	 *
	 * @param : void
	 * @return : String
	 */
	public String findCertificatePopupPage(boolean isInstallComplete) {
		Map eventParam = Maps.newHashMap();
		eventParam.put("isInstallComplete", isInstallComplete);
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findCertificatePopupPage", eventParam);
		publisher.publishEvent(event);
		return (String)event.getResult();
	}

}
