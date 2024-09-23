package smartsuite.security.event;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import smartsuite.app.event.CustomSpringEvent;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;


@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class CertificateEventPublisher {
	
	@Autowired
	ApplicationEventPublisher publisher;
	
	/**
	 * 서명값 검증
	 * 
	 * @param : signValue
	 * @return void
	 */
	public void verifySignValue(String signValue) {

		Map eventParam = Maps.newHashMap();
		eventParam.put("sign_value", signValue);
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("verifySignValue", eventParam);
		publisher.publishEvent(event);

	}

	/**
	 * 서명값 검증, 신원확인
	 *
	 * @param : signValue
	 * @return void
	 */
	public void verifySignValueAndIdentification(String signValue, String rvalue, String ssn) {
		Map eventParam = Maps.newHashMap();
		eventParam.put("signValue", signValue);
		eventParam.put("rvalue", rvalue);
		eventParam.put("ssn", ssn);
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("verifySignValueAndIdentification", eventParam);
		publisher.publishEvent(event);
	}




	/**
	 * 해쉬값 생성
	 *
	 * @param : String
	 * @return : String
	 */
	public String getHashValueFromStr(String signValue) {
		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("getHashValueFromStr", signValue);
		publisher.publishEvent(event);
		return (String)event.getResult();
	}

	/**
	 * 랜덤값 생성
	 *
	 * @param : signValue
	 * @return void
	 */
	public String getRandomString(int size, String format) {
		Map eventParam = Maps.newHashMap();
		eventParam.put("size", size);
		eventParam.put("format", format);

		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("getRandomString", eventParam);
		publisher.publishEvent(event);
		return (String)event.getResult();
	}

	/**
	 * 협력사 사용자 hash값 조회
	 *
	 * @param : signValue
	 * @return void
	 */
	public String findVendorHashValue(String usrId) {
		Map eventParam = Maps.newHashMap();
		eventParam.put("usr_id", usrId);

		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("findVendorHashValue", eventParam);
		publisher.publishEvent(event);
		return (String)event.getResult();
	}

	/**
	 * 협력사 사용자 hash값 초기화
	 *
	 * @param : signValue
	 * @return void
	 */
	public void removeVendorHashValue(String usrId) {
		Map eventParam = Maps.newHashMap();
		eventParam.put("usr_id", usrId);

		CustomSpringEvent event = CustomSpringEvent.toCompleteEvent("removeVendorHashValue", eventParam);
		publisher.publishEvent(event);
	}
}