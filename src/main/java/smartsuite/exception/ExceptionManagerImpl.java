package smartsuite.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import smartsuite.security.account.exception.AccountException;
import smartsuite.app.common.error.service.ErrorService;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.service.SharedService;

public class ExceptionManagerImpl implements ExceptionManager {
	
	@Inject
	private ErrorService errorService;
	
	SharedService sharedService;
	
	// SMARTNINE-2622 replaceMessage 코드화
	//String replaceMessage = "에러가 발생하였습니다.";
	String replaceMessage = "STD.E9999"; //오류가 발생하였습니다.<br>관리자에게 문의하세요.
	
	boolean debug = true;
	
	boolean recordable;
	
	public String getReplaceMessage() {
		return replaceMessage;
	}

	public void setReplaceMessage(String replaceMessage) {
		this.replaceMessage = replaceMessage;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isRecordable() {
		return recordable;
	}

	public void setRecordable(boolean recordable) {
		this.recordable = recordable;
	}

	@Override
	public Map<String, Object> handle(HttpServletRequest request, Exception exception) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("debug", debug);
		
		// SMARTNINE-2622 debug=true인 경우 stackTrace를 담고, false인 경우 message 처리
		if(debug) { 
			map.put("stackTrace", createStackTrace(exception));
		}
		 
		// Custom Exception의 메시지 처리
		if(AccountException.class.isInstance(exception)) { //관리자 2차인증으로 인한 메세지 처리
			 map.put("message", exception.getMessage());
	    } else if (CommonException.class.isInstance(exception)) { // CommonException 처리 -> resultStatus 전달
	        map.put("message", exception.getMessage());
	        String resultStatus = Const.FAIL;
	        String args[] = null;
	        
	        CommonException commonException = (CommonException)exception;
	        if(commonException.getArgument() != null) {
	        	args = commonException.getArgument();
	        }
	        if(commonException.getResultStatus() != null) {
	            resultStatus = commonException.getResultStatus();
	        }
	        map.put("arguments", args);
	        map.put(Const.RESULT_STATUS, resultStatus);
	    } else { // custom Exception이 아닌 경우 공통 메시지 처리
	        map.put("message", replaceMessage);
	    }
		
		if(recordable) {
			//클라이언트에 해당 LOG_ID를 전달 (브라우저에서 생성되는 추가 정보 저장할 수 있도록)
			String errId = UUID.randomUUID().toString(); 
			map.put("err_uuid", errId);
			//Exception 저장
			this.saveException(request, exception, errId);
		}
		
		return map;
	}
	
	@Override
	public void saveException(HttpServletRequest request, Exception exception, String errId) {
		errorService.saveServerErrorInfo(request, exception, errId);
	}
	
	public String createStackTrace(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);		
		exception.printStackTrace(pw);
		pw.flush();
		sw.flush();
		
		return sw.toString();
	}

}
