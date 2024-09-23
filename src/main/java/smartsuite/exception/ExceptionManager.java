package smartsuite.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ExceptionManager {
	
	Map<String, Object> handle(HttpServletRequest request, Exception exception);
	
	void saveException(HttpServletRequest request, Exception exception, String uuid);
}
