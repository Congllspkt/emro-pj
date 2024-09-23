package smartsuite.exception;

import java.util.Map;

/**
 * SmartSuite9.1 에서 공통으로 사용하기 위한 예외 처리 클래스
 * Transactional 선언 시 rollback 처리를 위해 RuntimeException 을 상속
 */
public class CommonException extends RuntimeException{
	
	private static final long serialVersionUID = 5494598356064122965L;
	String defaultMessage = null;
	Map<String, Object> parameter = null;
	String[] argument = null;
	String logMessage = null;
	String resultStatus = null;
	
	/**
     * 지정된 message 를 매개변수로 하는 {@code CommonException} 생성자
     *
     * @param message - logging 및 client 메세지 처리에 사용되며 다국어처리 대상
     */
	public CommonException(String message) {
		super(message);
	}
	/**
     * 지정된 message와 예외 원인(cause)을 매개변수로 하는 {@code CommonException} 생성자
     *
     * @param message - logging 및 client 메세지 처리에 사용되며 다국어처리 대상
     * @param cause 현재 예외에 대한 원인
     */
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
     * 지정된 message 와  client 에 전달할 resultStatus 문자열을 매개변수로 하는 {@code CommonException} 생성자
     *
     * @param message - logging 및 client 메세지 처리에 사용되며 다국어처리 대상
     * @param resultStatus - client 에 전달할 resultStatus 문자열
     */
	public CommonException(String message, String resultStatus) {
		super(message);
		this.resultStatus = resultStatus;
	}
	/**
     * 지정된 message와 예외 원인(cause)을 매개변수로 하는 {@code CommonException} 생성자
     *
     * @param message - logging 및 client 메세지 처리에 사용되며 다국어처리 대상
     * @param resultStatus - client 에 전달할 resultStatus 문자열
     * @param cause 현재 예외에 대한 원인
     */
	public CommonException(String message, String resultStatus, Throwable cause) {
		super(message, cause);
		this.resultStatus = resultStatus;
	}
	
	/**
     * ErrorCode를 매개변수로 하는 {@code CommonException} 생성자
     *
     * @param errorCode - ErrorCode로 관리 되는 상수집합
     * @see ErrorCode
     */
	public CommonException(ErrorCode errorCode) {
		super(errorCode.getMessageCode());
		this.defaultMessage = errorCode.getDefaultMessage();
		this.resultStatus = errorCode.getErrorCode();
	}
	
	/**
     * ErrorCode와 예외 원인(cause)을  매개변수로 하는 {@code CommonException} 생성자
     *
     * @param errorCode - ErrorCode로 관리 되는 상수집합
     * @param cause - 예외처리 발생 원인
     * @see ErrorCode
     */
	public CommonException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessageCode(), cause);
		this.defaultMessage = errorCode.getDefaultMessage();
		this.resultStatus = errorCode.getErrorCode();
	}
	
	/**
     * 빌더패턴을 사용한 exceptionInfo instance 를  매개변수로 하는 {@code CommonException} 생성자
     *
     * @param exceptionInfo - 빌더패턴을 사용한 exceptionInfo instance
     * @see ExceptionInfo
     */
	public CommonException(ExceptionInfo exceptionInfo) {
		super(exceptionInfo.getMessage(), exceptionInfo.getThrowable());
		this.parameter = exceptionInfo.getPrameter();
		this.argument = exceptionInfo.getArgument();
		this.logMessage = exceptionInfo.getLogMessage();
		this.resultStatus = exceptionInfo.getResultStatus();
		
	}
	
	/**
	 * @return String 다국어 key 가 없을 시 message 처리
	 */
	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	/**
	 * @return String logging 에만 사용될 메세지를 반환합니다.
	 */
	public String getLogMessage() {
		return this.logMessage;
	}
	
	/**
     * @return Map logging 할 parameter를 반환합니다.  
     */
	public Map<String, Object> getPrameter() {
		return this.parameter;
	}
	
	/**
     * @return String client 에 전달할 resultStatus 문자열을 반환합니다.  
     */
	public String getResultStatus() {
		return this.resultStatus;
	}
	
	/**
	 * @return String[] message의 다국어 처리 시 사용될 매개변수를 반환합니다.
	 */
	public String[] getArgument() {
		String[] ret = null;
		if (this.argument != null) {
			ret = argument.clone();
		}
		return ret;
	}
	
	@Override
	public void printStackTrace() {
		return;
	}

}
