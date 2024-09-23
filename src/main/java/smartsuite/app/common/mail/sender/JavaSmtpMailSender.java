package smartsuite.app.common.mail.sender;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.exception.CommonException;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;

import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class JavaSmtpMailSender implements MailSender {

	static final Logger LOG = LoggerFactory.getLogger(JavaSmtpMailSender.class);
	
	
	@Value ("#{mail['mail.smtp.encoding']}")
	String encoding;

	@Value ("#{mail['mail.smtp.port']}")
	String port;

	@Value ("#{mail['mail.smtp.host']}")
	String host;

	@Value ("#{mail['mail.smtp.username']}")
	String username;
	
	@Value ("#{mail['mail.smtp.password']}")
	String password;
	
	@Value ("#{mail['mail.smtp.socketFactory.class']}")
	String smtpSocketFactoryClass;
	
	@Inject
	StdFileService stdFileService;

	boolean debug; //Session Debug 
	
	protected boolean requireAuthenticate() {
		return username != null && password != null;
	}

	protected Authenticator createAuthenticator() {
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
	}
	
	protected Properties createProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);

		if(StringUtils.isNotEmpty(smtpSocketFactoryClass)){
			properties.put("mail.smtp.socketFactory.class", smtpSocketFactoryClass);
			properties.put("mail.smtp.auth", "true");
		}else {
			properties.put("mail.smtp.auth", requireAuthenticate());
		}
		
		return properties;

	}

	private Address createAddress(String address, String name) throws UnsupportedEncodingException {
		InternetAddress ia = new InternetAddress(address, name);
		
		String personal = ia.getPersonal();
		if(personal != null){
			ia.setPersonal(personal, encoding);
		}
		return ia;
	}

	@Override
	public void send(String contents, TemplateMailData templateMailData) throws Exception {
		Session session = null;
		if (requireAuthenticate()) {
			try {
				session = Session.getDefaultInstance(createProperties(), createAuthenticator());
			} catch (SecurityException exception) {
				LOG.debug("메일서버(SMTP)가 연동되어 있지 않습니다.");
				throw new CommonException(exception.getMessage());
			}
			 
		} else {
			session = Session.getDefaultInstance(createProperties());
		}
		session.setDebug(debug);
		MimeMessage message = new MimeMessage(session);
		Multipart multipart = new MimeMultipart();

		Address from = createAddress(templateMailData.getSender().getAddress(), templateMailData.getSender().getName());
		message.setFrom(from);
		message.setSubject(templateMailData.getTitle(), encoding);
		
		List<TemplateMailData.Receiver> toReceivers = templateMailData.getReceiversByRecipientTypeToSend(RecipientType.TO);
		List<TemplateMailData.Receiver> ccReceivers = templateMailData.getReceiversByRecipientTypeToSend(RecipientType.CC);
		List<TemplateMailData.Receiver> bccReceivers = templateMailData.getReceiversByRecipientTypeToSend(RecipientType.BCC);
		
		Address[] toAddressList = new Address[toReceivers.size()];
		for(int i = 0; i < toReceivers.size(); i++) {
			TemplateMailData.Receiver toReceiver = toReceivers.get(i);
			toAddressList[i] = createAddress(toReceiver.getAddress(), toReceiver.getName());
		}
		if(toAddressList.length > 0) {
			message.addRecipients(RecipientType.TO, toAddressList);
		}
		
		Address[] ccAddressList = new Address[ccReceivers.size()];
		for(int i = 0; i < ccReceivers.size(); i++) {
			TemplateMailData.Receiver ccReceiver = ccReceivers.get(i);
			ccAddressList[i] = createAddress(ccReceiver.getAddress(), ccReceiver.getName());
		}
		if(ccAddressList.length > 0) {
			message.addRecipients(RecipientType.CC, ccAddressList);
		}
		
		Address[] bccAddressList = new Address[bccReceivers.size()];
		for(int i = 0; i < bccReceivers.size(); i++) {
			TemplateMailData.Receiver bccReceiver = bccReceivers.get(i);
			bccAddressList[i] = createAddress(bccReceiver.getAddress(), bccReceiver.getName());
		}
		if(bccAddressList.length > 0) {
			message.addRecipients(RecipientType.BCC, bccAddressList);
		}

		MimeBodyPart body = new MimeBodyPart();
		body.setHeader("Content-Transfer-Encoding", "base64");
		body.setContent(contents, "text/html; charset=\""
				+ encoding + "\"");
		multipart.addBodyPart(body);
		
		if(!Strings.isNullOrEmpty(templateMailData.getAttachmentGroupId())) {
			/*
			 * 첨부파일 존재시
			 * */
			try {
				FileList fileList = stdFileService.findFileListWithContents(templateMailData.getAttachmentGroupId());
				
				for (FileItem fileItem : fileList.getItems()) {
					ByteArrayDataSource dataSource;
					dataSource = new ByteArrayDataSource(fileItem.toInputStream(), "application/octet-stream");
					String attachmentName = MimeUtility.encodeText(
							fileItem.getName(), encoding, "B");
					MimeBodyPart attachmentBody = new MimeBodyPart();
					attachmentBody.setHeader("Content-Transfer-Encoding", "base64");
					attachmentBody.setDataHandler(new DataHandler(dataSource));
					attachmentBody.setFileName(attachmentName);
					
					multipart.addBodyPart(attachmentBody);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage());
				throw new CommonException(e.getMessage());
			}
		}
		
		message.setContent(multipart);
		message.setSentDate(new Date());

		try{
			Transport.send(message);
		}catch (Exception e){
			throw new CommonException(e.getMessage());
		}
	}
}
