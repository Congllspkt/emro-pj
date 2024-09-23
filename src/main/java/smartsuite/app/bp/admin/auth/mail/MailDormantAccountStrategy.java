package smartsuite.app.bp.admin.auth.mail;

import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.mail.Message;
import java.util.Map;

/**
 * 휴먼계정 알림 메일 설정
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class MailDormantAccountStrategy implements MailStrategy {
	
	@Override
	public String getEmailTemplateId() {
		return "MAIL_DORMANT_ACCOUNT";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		
		// 수신자 설정
		TemplateMailData.Receiver receiver = TemplateMailData.Receiver.getInstance(
				Message.RecipientType.TO,
				(String) data.get("email"),
				(String) data.get("usr_nm"),
				(String) data.getOrDefault("usr_id","")
				);
		
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		templateMailData.addReceiver(receiver);
		templateMailData.setParameter(data);
		
		return templateMailData;
	}
}
