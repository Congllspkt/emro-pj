package smartsuite.mybatis;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Intercepts({
	@Signature(method="handleResultSets", type=ResultSetHandler.class, args={Statement.class})
})
public class ResultSetLoggingInterceptor implements Interceptor {
	
	static final Logger LOGGER = LoggerFactory.getLogger(ResultSetLoggingInterceptor.class);
	
	// 조회 건수에 따른 tagging
	int countLevelNormal = 1000;
	
	int countLevelWarning = 5000;
	
	int countLevelCritical = 10000;
	
	
	String logTemplate = "ResultSetCount:{} [RSC:{}]";
	
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result =  invocation.proceed();
		
		if(result instanceof ArrayList){
			int rowCount = ((ArrayList)result).size();
						
			LOGGER.info(logTemplate, new Object[] { 
					rowCount, 
					getCountWarningLevel(rowCount) });
		}
        
        return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
		this.countLevelNormal = Integer.parseInt(properties.getProperty("countLevelNormal"));
		this.countLevelWarning = Integer.parseInt(properties.getProperty("countLevelWarning"));
		this.countLevelCritical = Integer.parseInt(properties.getProperty("countLevelCritical"));
		
	}
	
	/**
	 * 조회 건수에 따른 tagging 설정
	 * @param rowCount
	 * @return
	 */
	private String getCountWarningLevel(int rowCount) {		
		
		if(rowCount < countLevelNormal) {
			return "NORMAL";
		} else if (rowCount < countLevelWarning) {
			return "WARNING";
		} else if (rowCount < countLevelCritical) {
			return "CRITICAL";
		} else {
			return "OVERFLOW";
		}
	}

}
