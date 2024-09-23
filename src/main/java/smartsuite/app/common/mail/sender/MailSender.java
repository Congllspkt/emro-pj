package smartsuite.app.common.mail.sender;

import smartsuite.app.common.mail.data.TemplateMailData;

public interface MailSender {
	
	void send(String contents, TemplateMailData templateMailData) throws Exception;
}
