package smartsuite.app.common.mail.receiver;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import smartsuite.app.common.mail.data.FileInfo;
import smartsuite.app.common.mail.data.MailInfo;
import smartsuite.exception.CommonException;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.*;


public abstract class AbstractMailReceiver implements MailReceiver {
	
	static final Logger LOG = LoggerFactory.getLogger(MailReceiver.class);
	
	@Value("#{mail['mail.receiver.tempfilestoragepath']}")
	protected String tempfilestoragepath;
	
	@Value("#{mail['mail.receiver.host']}")
	protected String host;
	
	@Value("#{mail['mail.receiver.port']}")
	protected String port;
	
	@Value("#{mail['mail.receiver.username']}")
	protected String user;
	
	@Value("#{mail['mail.receiver.password']}")
	protected String password;
	
	protected Properties properties = new Properties();
	
	@Value("#{mail['mail.receiver.ssl'] ?: false}")
	protected boolean ssl = false;
	
	protected abstract String getStoreType();
	
	protected String getTextFromMessage(Message message) throws IOException, MessagingException {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        mimeMultipart.getCount();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	protected String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws IOException, MessagingException {
	    String result = "";
//		try{

	    	int count = mimeMultipart.getCount();
		    if (count == 0)
		        throw new MessagingException("Multipart with no body parts not supported.");
		    boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
		    if (multipartAlt)
		        return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));

		    for (int i = 0; i < count; i++) {
		        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
		        result += getTextFromBodyPart(bodyPart);
		    }

/*		}catch(Exception e){
		}*/
	    return result;
	}

	protected String getTextFromBodyPart(
	        BodyPart bodyPart) throws IOException, MessagingException {

	    String result = "";
	    if (bodyPart.isMimeType("text/plain")) {
	        result = (String) bodyPart.getContent();
	    } else if (bodyPart.isMimeType("text/html")) {
	        result = (String) bodyPart.getContent();
	    } else if (bodyPart.getContent() instanceof MimeMultipart){
	        result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	    }
	    return result;
	}
	
	protected List<FileInfo> saveFiles(String rootPath, Message message) throws IOException, MessagingException {
		List<FileInfo> attachments = new ArrayList<FileInfo>();
		
		// noAttachments
		if(!(message.getContent() instanceof Multipart)){
			return attachments;
		}
		
		Multipart multipart = (Multipart) message.getContent();
		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) && (bodyPart.getFileName() == null || "".equals(bodyPart.getFileName()))) {
				continue;
			}
			InputStream is = bodyPart.getInputStream();
			String fileName = MimeUtility.decodeText(bodyPart.getFileName());
			FileOutputStream fos = null;
			try{
				File f = new File(tempfilestoragepath+ UUID.randomUUID() + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(fileName));
				if(!f.getParentFile().exists()) // 폴더 생성.
					f.getParentFile().mkdirs();
				fos = new FileOutputStream(f);
				byte[] buf = new byte[4096];
				int bytesRead;
				do {
					bytesRead = is.read(buf);
					if (bytesRead > 0) {
						fos.write(buf, 0, bytesRead);
					}
				}while (bytesRead != -1);
				fos.close();
				FileInfo fileInfo = new FileInfo();
				fileInfo.setFile(f);
				fileInfo.setFileName(fileName);
				attachments.add(fileInfo);
			}catch (RuntimeException rune){
				try{
					throw new FileNotFoundException("File Storage Path is required");
				}catch (Exception e){
					LOG.error(e.getMessage());
				}
			}catch (Exception e){
				try{
					throw new FileNotFoundException("File Storage Path is required");
				}catch (Exception e1){
					LOG.error(e1.getMessage());
				}
			}finally{
				if( null!= fos )fos.close();
				if( null!= is) is.close();
			}


		}
		return attachments;
	}
	
	
	protected MailInfo convertMessageToMailInfo(Message message) throws MessagingException, IOException{
		MailInfo mailInfo = new MailInfo();
		try{
			mailInfo.setSubject(message.getSubject());
			Address[] froms = message.getFrom();
			String email = null;
			String fromName = null;
			if(froms != null){
				InternetAddress addr = ((InternetAddress) froms[0]);
				email = addr.getAddress();
				fromName = addr.getPersonal();
			}
			mailInfo.setFrom(email);
			mailInfo.setFromName(fromName);
			String html = getTextFromMessage(message);
			mailInfo.setText(html);
			mailInfo.setSentDate(message.getSentDate());
			mailInfo.setAttachments(saveFiles(tempfilestoragepath, message));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			mailInfo.setParseError(sStackTrace);
			LOG.error("convertingMessageError ",e);
		}
		
		return mailInfo;
	}
	
	protected Folder getEmailFolder(String folderName, int mode) throws MessagingException {
		Session emailSession = Session.getInstance(this.properties);

		
		Store emailStore = emailSession.getStore(getStoreType());
		emailStore.connect(this.host, this.user, this.password);

		Folder emailFolder = emailStore.getFolder(folderName);
		emailFolder.open(mode);
		
		return emailFolder;
	}
	
	@Override
	public List<MailInfo> receiveEmail(Date afterDate) throws Exception {
		List<MailInfo> result = new ArrayList<MailInfo>();
		Folder emailFolder = getEmailFolder("INBOX", Folder.READ_ONLY);
		
		Message[] messages = emailFolder.getMessages();
		int index = messages.length - 1;
		while(index >= 0){
			LOG.info(" 메일 inbox 처리 index :: " + index);
			Message currentMessage = messages[index];
			Date sentDate = null;
			try{//TODO :: EOF on socket
				if(currentMessage != null)
					sentDate = currentMessage.getSentDate();
			}catch(FolderClosedException e){
				throw new CommonException(e.getMessage());
			}
			if(sentDate == null){
				// sentDate가 null인 mail은 무시한다
				LOG.error("Receiving mail and sentDate is null and ignored / mailTitle : "+currentMessage.getSubject());
				index--;
				continue;
			}
			if(sentDate.after(afterDate)){
				result.add(0, convertMessageToMailInfo(currentMessage));
				index--;
			}else{
				break;
			}
		}
		if(emailFolder.isOpen()){
			emailFolder.close(false);
			emailFolder.getStore().close();
		}
		
		return result;
	}
	
	@Override
	public List<MailInfo> receiveAllEmail() throws Exception {
		
		List<MailInfo> result = new ArrayList<MailInfo>();
		Folder emailFolder = getEmailFolder("INBOX", Folder.READ_ONLY);
		Message[] messages = emailFolder.getMessages();
		
		for(int i = 0; i < messages.length; i++){
			result.add(convertMessageToMailInfo(messages[i]));
		}
		
		emailFolder.close(false);
		emailFolder.getStore().close();
		
		return result;
	}

	@Override
	public void initialize(String host, String user, String password, boolean ssl,
			Properties properties) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.properties = properties;
		this.ssl = ssl;
	}

	public String getTempfilestoragepath() {
		return tempfilestoragepath;
	}

	public void setTempfilestoragepath(String tempfilestoragepath) {
		this.tempfilestoragepath = tempfilestoragepath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
}
