package smartsuite.app.common.mail.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailInfo {
	
	private String from;
	
	private String fromName;

	private String to;
	
	private List<FileInfo> attachments = new ArrayList<FileInfo>();
	
	private String text;
	
	private String subject;
	
	private Date sentDate;
	
	private String parseError;

	public List<FileInfo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<FileInfo> attachments) {
		this.attachments = attachments;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getParseError() {
		return parseError;
	}

	public void setParseError(String parseError) {
		this.parseError = parseError;
	}
}