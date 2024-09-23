package smartsuite.app.common.error.service;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.error.reposiroty.ErrorRepository;
import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 공통으로 사용하는 서비스 관련 Class입니다.
 *
 * @author hjhwang
 * @see
 * @FileName SharedService.java
 * @package smartsuite.app.shared
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] hjhwang 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class ErrorService {

	static final Logger LOG = LoggerFactory.getLogger(ErrorService.class);

	@Inject
	ErrorRepository errorRepository;

	/**
	 * 클라이언트 에러가 발생했을 때 에러 정보를 저장 및 알림 PUSH
	 */
	public Map saveBrowserErrorInfo(HttpServletRequest request, Map<String, Object> param) {
		InetAddress local;
		try {
			local = InetAddress.getLocalHost();

			param.put("err_occr_typ", "B");
			param.put("menu_cd", request.getHeader("menucode"));

			String serverIp = local == null? "" : (local.getHostAddress() == null? "" : local.getHostAddress() );
			param.put("svr_ip", serverIp);

			this.insertErrorInfo(param);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 브라우저 에러가 발생했을 때 에러 정보를 저장 및 알림 PUSH
	 */
	public void saveServerErrorInfo(HttpServletRequest request, Exception exception, String errId) {
		InetAddress local;
		Map<String, Object> param = Maps.newHashMap();
		try {
			local = InetAddress.getLocalHost();
			StackTraceElement[] stackList = exception.getStackTrace();
			StackTraceElement getStack = null;
			for(StackTraceElement stack : stackList){
				if(stack.getClassName().indexOf("smartsuite") > -1){
					getStack = stack;
					break;
				}
			}

			param.put("class_nm", getStack.getClassName());
			param.put("meth_nm", getStack.getMethodName());
			param.put("menu_cd", request.getHeader("menucode"));
			param.put("err_occr_typ", "S");
			String serverIp = local == null? "" : (local.getHostAddress() == null? "" : local.getHostAddress() );
			param.put("svr_ip", serverIp);
			param.put("err_uuid", errId);
			param.put("sess_id", getSessionId(request));
			
			param.put("err_occr_dttm", new Date());
			param.put("sys_err_msg", exception.getMessage());
			
			String message = exception.getMessage();
			if(message == null) {
				message = "";
			}
			
			//convert stack trace object to string
			String stackTrace = createStackTrace(exception); 
			param.put("stk_trce_cont", message.concat(stackTrace));

			this.insertErrorInfo(param);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
    protected String getSessionId( HttpServletRequest request) {
    	if(request != null) {
			return request.getRequestedSessionId();
		}else {
			return "?????";
		}
	}
    
	public String createStackTrace(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);		
		exception.printStackTrace(pw);
		pw.flush();
		sw.flush();
		
		return sw.toString();
	}

	/**
	 * 에러 리스트 조회 (서버, 클라이언트 공용)
	 */
	public List findListError(Map param) {
		return errorRepository.findListError(param);
	}

	/**
	 * 에러 상세 조회 (서버, 클라이언트 공용)
	 */
	public Map findErrorInfo(Map param) {
		return errorRepository.findErrorInfo(param);
	}
	
	/**
	 * 에러 상세 조회 (서버, 클라이언트 공용) 
	 */
	public ResultMap deleteListError(Map param) {
		List<Map<String, Object>> deleteErrors = (List<Map<String, Object>>)param.get("deleteErrors");
		for(Map<String, Object> deleteError : deleteErrors) {
			errorRepository.deleteError(deleteError);
		}
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 서버 에러 강제 발생 
	 */
	public void occurError(Map param) {
		errorRepository.occurError(param);
	}
	
	/**
	 * Error 업데이트
	 */
	public ResultMap updateError(Map param) {
		errorRepository.updateError(param);
		return ResultMap.SUCCESS();
	}

	public void insertErrorInfo(Map param){
		errorRepository.insertErrorInfo(param);
	}
}
