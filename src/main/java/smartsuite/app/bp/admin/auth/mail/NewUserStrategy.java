package smartsuite.app.bp.admin.auth.mail;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.mail.Message;
import java.util.Map;

/**
 * 신규 사용자 아이디 비번 발급 메일 설정
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class NewUserStrategy implements MailStrategy {
	@Override
	public String getEmailTemplateId() {
		return "NEW_USER";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		
		// 수신자 설정
		TemplateMailData.Receiver receiver = TemplateMailData.Receiver.getInstance(
				Message.RecipientType.TO,
				(String) data.get("eml"),
				(String) data.get("usr_nm"),
				(String) data.getOrDefault("usr_id","")
		);
		
		Map<String, Object> parameter = Maps.newHashMap();
		parameter.put("pw", data.get("tempPw"));
		parameter.put("usr_id", data.get("usr_id"));
		parameter.put("usr_nm", data.get("usr_nm"));
		
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		templateMailData.addReceiver(receiver);
		templateMailData.setParameter(parameter);
		
		return templateMailData;
	}
}
