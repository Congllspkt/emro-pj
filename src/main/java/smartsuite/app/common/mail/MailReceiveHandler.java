package smartsuite.app.common.mail;

import java.util.Map;

public interface MailReceiveHandler {
	
	public void handleMail(Map<String, Object> mailInfo);
	
}
