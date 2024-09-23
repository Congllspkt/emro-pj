package smartsuite.app.common.mail.data;

public class MailResult {
	
	public MailResult() {}
	
	public MailResult(boolean success) {
		this.success = success;
	}
	
	public MailResult(boolean success, String message) {
		this.success = success;
		this.errorMessage = message;
	}
	
	public MailResult(boolean success, String message, String emlSndUuid) {
		this.success = success;
		this.errorMessage = message;
		this.emlSndUuid = emlSndUuid;
	}
	
	public static MailResult getInstance() {
		return new MailResult();
	}
	
	public static MailResult getInstance(boolean success) {
		return new MailResult(success);
	}
	
	public static MailResult getInstance(boolean success, String message) {
		return new MailResult(success, message);
	}
	
	public static MailResult getInstance(boolean success, String message, String emlSndUuid) {
		return new MailResult(success, message, emlSndUuid);
	}
	
	boolean success;
	
	String errorMessage;
	
	String emlSndUuid;
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setEmlSndUuid(String emlSndUuid) {
		this.emlSndUuid = emlSndUuid;
	}
	
	public String getEmlSndUuid() {
		return this.emlSndUuid;
	}
}
