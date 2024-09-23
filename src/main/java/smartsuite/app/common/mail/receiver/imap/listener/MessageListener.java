package smartsuite.app.common.mail.receiver.imap.listener;

import smartsuite.app.common.mail.data.MailInfo;


public interface MessageListener {
	
	public void handleMessage(MailInfo mail);

}
