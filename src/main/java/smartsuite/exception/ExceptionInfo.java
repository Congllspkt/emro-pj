package smartsuite.exception;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * SmartSuite9.1 에서 공통으로 사용되는 예외 처리 클래스 (CommonException)에 다양한 정보를 전달하기 위해
 * 빌더 패턴을 사용한 클래스 입니다.
 */
public class ExceptionInfo {
	private final String message;
	private final String logMassage;
	private final Map<String, Object> prameter;
	private final Throwable cause;
	private final String[] args;
	private final String resultStatus;
	
	public static class Builder {
		private String message = "STD.E9999"; //오류가 발생하였습니다.<br>관리자에게 문의하세요.
		private String logMassage = null;
		private String[] args = null;
		private Map<String, Object> prameter = Maps.newHashMap();
		private Throwable cause = null;
		private String resultStatus = null;
		
		/**
	     * logging 및 client 메세지 처리에 사용될 메세지를 구성하며 다국어 처리 대상입니다.
	     */
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
	     * logging 에만 사용될 메세지를 구성합니다.
	     */
		public Builder logMassage(String logMassage) {
			this.logMassage = logMassage;
			return this;
		}
		
		/**
	     * message의 다국어 처리 시 사용될 매개변수를 구성합니다.
	     */
		public Builder argument(String [] args) {
			this.args = new String[args.length];  
			for (int i = 0; i < args.length; ++i)  
			this.args[i] = args[i];
			
			return this;
		}
		
		/**
	     * ErrorCode 상수집합을 구성합니다.
	     * @see ErrorCode
	     */
		public Builder errorCode(ErrorCode errorCode) {
			this.message = errorCode.getMessageCode();
			this.resultStatus = errorCode.getErrorCode();
			return this;
		}
		
		/**
	     * client 에 전달할 result_status 문자열을 구성합니다.
	     */
		public Builder resultStatus(String resultStatus) {
			this.resultStatus = resultStatus;
			return this;
		}
		
		/**
	     * logging 할 parameter 를  Map형태로 구성합니다. 
	     */
		public Builder parameter(Map<String,Object> prameter) {
			this.prameter = prameter;
			return this;
		}
		
		/**
	     * logging 할 parameter 를  key, value 형식으로 추가 구성합니다. 
	     */
		public Builder addParam(String key, Object value) {
			prameter.put(key, value);
			return this;
		}
		
		/**
	     * 예외처리 발생 원인을 구성합니다.
	     */
		public Builder cause(Throwable cause) {
			this.cause = cause;
			return this;
		}
		
		/**
	     * ExceptionInfo instance 를 반환합니다.
	     */
		public ExceptionInfo build() {
			return new ExceptionInfo(this);
		}
		
	}
	
	private ExceptionInfo (Builder builder) {
		message = builder.message;
		logMassage = builder.logMassage;
		args = builder.args;
		prameter = builder.prameter;
		cause = builder.cause;
		resultStatus = builder.resultStatus;
	}
	
	/**
	 * @return String logging 및 client 메세지 처리에 사용될 메세지를 반환합니다.
	 * errorCode 상수집합으로 exceptionInfo 를 구성하였다면, errorCode 에 정의된 default message 가 우선합니다.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return String[] message의 다국어 처리 시 사용될 매개변수를 반환합니다.
	 */
	public String[] getArgument() {
		String[] ret = null;
		if (this.args != null) {
			ret = args.clone();
		}
		return ret;
	}
	
	/**
	 * @return String logging 에만 사용될 메세지를 반환합니다.
	 */
	public String getLogMessage() {
		return logMassage;
	}

	/**
     * @return Map logging 할 parameter를 반환합니다.  
     */
	public Map<String, Object> getPrameter() {
		return prameter;
	}
	
	/**
     * @return String client 에 전달할 result_status 문자열을 반환합니다.  
     */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
     * @return Throwable 예외 원인을 반환합니다.  
     */
	public Throwable getThrowable() {
		return cause;
	}
}
