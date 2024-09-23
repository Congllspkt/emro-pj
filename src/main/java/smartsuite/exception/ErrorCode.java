package smartsuite.exception;

public enum ErrorCode {
	FAIL("STD.E9999", "오류가 발생하였습니다.<br/>관리자에게 문의하세요.", "FAIL"),
	DUPLICATED("STD.E9100", "중복된 데이터가 존재합니다.", "DUPLICATED"),
	NOT_EXIST("STD.E9300", "삭제되었거나 존재하지 않는 데이터입니다.", "NOT_EXIST"),
	USED("STD.E9200","사용중인 데이터가 존재합니다.", "USED"),
	UNAUTHORIZED("STD.COM1000", "해당 역할이 존재하지 않습니다.", "UNAUTHORIZED"),
	INVALID_STATUS("STD.E9400", "유효한 상태가 아니거나 상태가 변경되어 요청을 수행할 수 없습니다.", "INVALID_STATUS");
	
	private final String messageCode;
	private final String defaultMessage;
	private final String errorCode;
	
	ErrorCode(String messageCode, String defaultMessage, String errorCode) {
		this.messageCode = messageCode;
		this.defaultMessage = defaultMessage;
		this.errorCode = errorCode;
	}
	
	public String getMessageCode() {
		return messageCode;
	}
	
	public String getDefaultMessage() {
		return defaultMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

}