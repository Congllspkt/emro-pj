package smartsuite.exception;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class SpringWebExceptionHandler {
	private static final Log LOG = LogFactory.getLog(SpringWebExceptionHandler.class);
	private final RequestMatcher requestMatcher = new ELRequestMatcher("hasHeader('X-Requested-With', 'XMLHttpRequest')");
	ExceptionManager exceptionManager;
	
	@Inject
    MessageSource messageSource;
	
	public void setExceptionManager(ExceptionManager exceptionManager){
		this.exceptionManager = exceptionManager;
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(
			HttpServletRequest request, HttpServletResponse response, Exception exception) {
		
		LOG.error(exception.getMessage(), exception);		
		if(requestMatcher.matches(request)) {
			return new ResponseEntity<Map<String,Object>>(exceptionManager.handle(request, exception), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ModelAndView model = new ModelAndView();
        model.setViewName("error/" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ModelAndView>(model, HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	
	@ExceptionHandler(ServletException.class)
	public ModelAndView handleServletException(
			HttpServletRequest request, HttpServletResponse response, Exception exception) {
		
		LOG.error(exception.getMessage(), exception);		
		ModelAndView model = new ModelAndView();
        model.setViewName("error/" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        return model;
	}
	
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<?> handleCommonException(
			HttpServletRequest request, HttpServletResponse response, CommonException exception) {
		
		StringBuffer log = new StringBuffer();
		String exceptionLogMsg = "";
		// 로그 전용 메세지가 있을 경우, 해당 메세지를 로깅
		if(exception.getLogMessage() != null) {
			exceptionLogMsg = exception.getLogMessage();
		} else { 
			exceptionLogMsg = messageSource.getMessage(exception.getMessage(), exception.getArgument(), LocaleContextHolder.getLocale());
		}
		StackTraceElement stack = exception.getStackTrace()[0];
		log.append("\r\n************************* [Common Exception] ****************************");
		log.append("\r\n exception-message              = ").append(exceptionLogMsg);
		log.append("\r\n controller-class               = ").append(stack.getClassName());
		log.append("\r\n method-name                    = ").append(stack.getMethodName());
		log.append("\r\n line-number                    = ").append(stack.getFileName()).append(":").append(stack.getLineNumber());
		if(exception.getCause() != null) {
			log.append("\r\n cause                          = ").append(exception.getCause());
		}
		if(exception.getPrameter() != null) {
			log.append("\r\n parameter");
			log.append("\r\n -----------------------------------------------------------------------");
			log.append("\r\n ").append(ObjectUtils.getDisplayString(exception.getPrameter()));
		}
		log.append("\r\n*************************************************************************\r\n");
		LOG.error(log);
		if(requestMatcher.matches(request)) {
			return new ResponseEntity<Map<String,Object>>(exceptionManager.handle(request, exception), HttpStatus.SERVICE_UNAVAILABLE);
		}
		ModelAndView model = new ModelAndView();
        model.setViewName("error/" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
		return new ResponseEntity<ModelAndView>(model, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
