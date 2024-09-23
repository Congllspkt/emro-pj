package smartsuite.app.common.mail.receiver.imap;

import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.receiver.AbstractMailReceiver;
import smartsuite.app.common.mail.receiver.imap.listener.MessageListener;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

@Primary
@Service
public class ImapMailReceiver extends AbstractMailReceiver{

	static final Logger LOG = LoggerFactory.getLogger(ImapMailReceiver.class);
	
	@Override
	protected String getStoreType() {
		if(this.ssl){
			return "imaps";
		}else{
			return "imap";
		}
		
	}
	
	protected Folder getEmailFolder(String folderName, int mode) throws MessagingException {
		if(this.port != null){
			this.properties.setProperty("mail.imap.port", this.port);
		}
		return super.getEmailFolder(folderName, mode);
	}
	
	public void monitoringNewMessages(final MessageListener msgListner) throws Exception {
	    Session session = Session.getInstance(properties); // not
	                                                       // getDefaultInstance
	    Folder inbox = null;
	    try{
	    	inbox = this.getEmailFolder("INBOX",  Folder.READ_ONLY);


		    inbox.addMessageCountListener(new MessageCountAdapter() {

		        @Override
		        public void messagesAdded(MessageCountEvent event) {
		            Message[] messages = event.getMessages();

		            for (Message message : messages) {
		                try {
		                	msgListner.handleMessage(convertMessageToMailInfo(message));
		                } catch (Exception e) {
		                	LOG.error(e.getMessage());
		                }
		            }
		        }
		    });

		    IdleThread idleThread = new IdleThread(inbox, user, password);
		    idleThread.setDaemon(false);
		    idleThread.start();

		    idleThread.join();      
	    }catch(Exception e){
	    	try{
				throw new MessagingException(e.getMessage());
			}catch (Exception e1){
	    		LOG.error(e1.getMessage());
			}
	    }finally{
	    	
	    	if(inbox != null){
	    		try{
		    		inbox.close(true);
		    	}catch(Exception e){
	    			LOG.error(e.getMessage());
				}finally{
		    		inbox.getStore().close();
		    	}
	    	}
	    	
	    }
	     
	}
	
	private class IdleThread extends Thread {
        private final Folder folder;
        private volatile boolean running = true;
        
        private final String username;
        
        private final String password;

        public IdleThread(Folder folder, String username, String password) {
            super();
            this.folder = folder;
            this.username = username;
            this.password = password;
        }

        public synchronized void kill() { //NOPMD

            if (!running)
                return;
            this.running = false;
        }

        @Override
        public void run() {
            while (running) {

                try {
                    ensureOpen(folder);
                    LOG.info("enter idle");
                    ((IMAPFolder) folder).idle();
                } catch (Exception e) {
                    // something went wrong
                    // wait and try again
                	LOG.info(e.getMessage());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    	LOG.error(e1.getMessage());
                    }
                }

            }
        }
        public void ensureOpen(final Folder folder) throws MessagingException {

            if (folder != null) {
                Store store = folder.getStore();
                if (store != null && !store.isConnected()) {
                    store.connect(username, password);
                }
            } else {
                throw new MessagingException("Unable to open a null folder");
            }

            if (folder.exists() && !folder.isOpen() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
				LOG.info("open folder " + folder.getFullName());
                folder.open(Folder.READ_ONLY);
                if (!folder.isOpen())
                    throw new MessagingException("Unable to open folder " + folder.getFullName());
            }

        }
    }
}