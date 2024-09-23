package smartsuite.app.common.mail.receiver.pop3;

import smartsuite.app.common.mail.receiver.AbstractMailReceiver;

import javax.mail.Folder;
import javax.mail.MessagingException;

public class Pop3MailReceiver extends AbstractMailReceiver{
	
	@Override
	protected String getStoreType() {
		if(this.ssl){
			return "pop3s";
		}else{
			return "pop3";
		}
	}
	
	protected Folder getEmailFolder(String folderName, int mode) throws MessagingException {
		if(this.port != null){
			this.properties.setProperty("mail.pop3.port", this.port);
		}
		return super.getEmailFolder(folderName, mode);
	}
}