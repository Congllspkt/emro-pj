package smartsuite.app.bp.admin.auth.mail;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.mail.Message;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class InitPwMlStrategy implements MailStrategy {
	
	@Override
	public String getEmailTemplateId() {
		return "INIT_PW_ML";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {

		String eml = data.get("eml") == null? "" : (String) data.get("eml");
		String userName = data.get("usr_nm") == null? "" : (String) data.get("usr_nm");
		String userId = data.get("usr_id") == null? "" : (String) data.get("usr_id");
		String url = data.get("url") == null? "" : (String) data.get("url");
		String tempPw = data.get("tempPw") == null? "" : (String) data.get("tempPw");

		TemplateMailData.Receiver receiver = TemplateMailData.Receiver.getInstance(
				Message.RecipientType.TO,
				eml,
				userName,
				userId
		);
		
		Map<String, Object> parameter = Maps.newHashMap();
		
		//필수 데이터
		parameter.put("pw", tempPw);
		parameter.put("usr_nm", userName);
		parameter.put("url", url);
		parameter.put("usr_id", userId);

		TemplateMailData templateMailData = TemplateMailData.getInstance();
		templateMailData.addReceiver(receiver);
		templateMailData.setParameter(parameter);
		
		return templateMailData;
	}
}
