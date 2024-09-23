package smartsuite.log.core;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.listener.SLF4JLogLevel;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smartsuite.log.web.ILogAuth;

import javax.inject.Inject;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SLF4JQueryLoggingListener implements QueryExecutionListener {

	@Inject
	private ILogAuth auth;
	private static Logger logger = LoggerFactory
			.getLogger(SLF4JQueryLoggingListener.class);

	SLF4JLogLevel logLevel = SLF4JLogLevel.INFO;

	boolean useReplaceParameter = true;

	boolean useReplaceWhitespace = false;

	boolean includeParameter = true;

	boolean useExcludeSqlLogging = true;

	String anonymousTenancyId = "?????";

	String anonymousCompanyId = "?????";

	String anonymousUserName = "?????";

	// sql 수행시간에 따른 tagging
	long timeLevelNormal = 1000;

	long timeLevelWarning = 5000;

	long timeLevelCritical = 10000;

	private final String defaultTenancy = "EMRO";

	private final String excluedSqlLoggingRemark = "EXCLUDE_SQL_LOGGING";

	String logTemplate = "Tenancy:{}/{} Datasource:{} Time:{} [QET:{}] Num:{} Batch:{}/{} Query:{}";


	/**
	 * ? 를 찾는 pattern
	 */
	private static Pattern questionPattern = Pattern.compile("\\?{1}");

	@Override
	public void beforeQuery(ExecutionInfo executioninfo, List<QueryInfo> list) {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterQuery(ExecutionInfo execInfo, List queryInfoList) {
		String dataSourceName = execInfo.getDataSourceName();
		int count = 0;
		StringBuffer sb = new StringBuffer();
		Iterator it = queryInfoList.iterator();
		String batch = execInfo.isBatch() ? "True" : "False";
		int batchSize = execInfo.getBatchSize();
		if (it.hasNext()) {
			QueryInfo queryInfo = (QueryInfo) it.next();
			List args = queryInfo.getQueryArgsList();
			String query = queryInfo.getQuery();
			
			// http://alm.emro.co.kr/browse/NGTFT-370
			/** JPA 쿼리 포매팅 */
			/*if(!query.contains(System.lineSeparator())) {
				query = new BasicFormatterImpl().format(query);
			}*/
			/** JPA 쿼리 포매팅 */
			
			String padding = null;
			Iterator itArgs = queryInfo.getQueryArgsList().iterator();

			if(isExcluedSqlLogging(query)) {
				sb.append(" exclude sql logging.");
			} else {
				if (useReplaceWhitespace) {
					query = query.replaceAll("\\s+", " ");
					padding = "\t\t";
				} else {
					padding = "\n";
				}
				sb.append("{");
				sb.append(padding);
				count++;
				if (useReplaceParameter && includeParameter) {
					Matcher matcher = questionPattern.matcher(query);
					if (itArgs.hasNext()) {

						Map<String, Object> paramMap = (Map) itArgs.next();
						SortedMap<String, Object> sortedParamMap = new TreeMap<String, Object>(new StringAsIntegerComparator());
						sortedParamMap.putAll(paramMap);
						Iterator itSortedParam = sortedParamMap.entrySet().iterator();

						int i = 0, length = paramMap.size();

						while (matcher.find()) {

							if (itSortedParam.hasNext()) {
								Map.Entry<String, Object> paramEntry = (Entry<String, Object>) itSortedParam.next();
								String replaced = i < length ? toString( paramEntry.getValue() , true) : "?";
								matcher.appendReplacement(sb, replaced);
								i++;
							}

						}
					}

					matcher.appendTail(sb);
					sb.append(padding);
					sb.append("}");
				} else {
					sb.append(query);
					sb.append(padding);
					sb.append("}");
					if (includeParameter) {
						sb.append("\n");
						while (itArgs.hasNext()) {
							appendQueryArgs(sb, (Map) itArgs.next());
						}
					}
				}
			}
			if (includeParameter && !isExcluedSqlLogging(query)) {
				while (itArgs.hasNext()) {
					sb.append("\n ");
					sb.append(++count);
					sb.append(" : ");
					appendQueryArgs(sb, (Map) itArgs.next());
				}
			}
		}


		writeLog(new Object[] {
				getTenancy(),
				getCompany(),
				dataSourceName,
				execInfo.getElapsedTime(),
				getTimeWarningLevel(execInfo.getElapsedTime()),
				queryInfoList.size(),
				batch,
				batchSize,
				sb.toString() });
	}


	@SuppressWarnings("rawtypes")
	private void appendQueryArgs(StringBuffer sb, Map<String, Object> args) {

		SortedMap<String, Object> sortedParamMap = new TreeMap<String, Object>(new StringAsIntegerComparator());
		sortedParamMap.putAll(args);

		sb.append("[");
		for (Map.Entry<String, Object> paramEntry : sortedParamMap.entrySet()) {
			sb.append(toString(paramEntry.getValue(), false));
			sb.append(",");
		}
		chompIfEndWith(sb, ',');
		sb.append("]");
	}


	private String toString(Object o, boolean withQuote) {
		if (o == null) {
			return "null";
		} else {
			return withQuote ? "'" + Matcher.quoteReplacement(o.toString()) + "'" : Matcher.quoteReplacement(o.toString());
		}
	}

	protected void writeLog(Object argArray[]) {
		switch (logLevel) {
			case DEBUG:
				logger.debug(logTemplate, argArray);
				break;
			case ERROR:
				logger.error(logTemplate, argArray);
				break;
			case INFO:
				logger.info(logTemplate, argArray);
				break;
			case TRACE:
				logger.trace(logTemplate, argArray);
				break;
			case WARN:
				logger.warn(logTemplate, argArray);
				break;
			default:
		}
	}


	protected String getTenancy() {
		if(auth != null){
			return auth.getTenancy();
		}
		return defaultTenancy;
	}

	protected String getCompany() {
		if(auth != null) {
			return auth.getCompany();
		} else {
			return null;
		}
	}

	protected void chompIfEndWith(StringBuffer sb, char c) {
		final int lastCharIndex = sb.length() - 1;
		if (sb.charAt(lastCharIndex) == c) {
			sb.deleteCharAt(lastCharIndex);
		}
	}

	/**
	 * SQL Logging 제외
	 * @param query
	 * @return
	 */
	private boolean isExcluedSqlLogging(String query) {
		if(useExcludeSqlLogging && query.contains(excluedSqlLoggingRemark)){
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Comparator considering string as integer.
	 *
	 * When it has null, put it as first element(smaller).
	 * If string cannot be parsed to integer, it compared as string.
	 */
	public static class StringAsIntegerComparator implements Comparator<String> {
		@Override
		public int compare(String left, String right) {
			// make null first
			if (left == null && right == null) {
				return 0;
			}
			if (left == null) {
				return -1; // right is greater
			}
			if (right == null) {
				return 1; // left is greater;
			}

			try {
				return Integer.valueOf(left).compareTo(Integer.valueOf(right));
			} catch (NumberFormatException e) {
				return left.compareTo(right);  // use String comparison
			}
		}
	}


	/**
	 * sql 경과시간에 따른 tagging 설정
	 * @param etime
	 * @return
	 */
	private String getTimeWarningLevel(long etime) {

		if(etime < timeLevelNormal) {
			return "NORMAL";
		} else if (etime < timeLevelWarning) {
			return "WARNING";
		} else if (etime < timeLevelCritical) {
			return "CRITICAL";
		} else {
			return "OVERFLOW";
		}
	}

	public void setLogTemplate(String logTemplate) {
		this.logTemplate = logTemplate;
	}

	public ILogAuth getAuth() {
		return auth;
	}

	public void setAuth(ILogAuth auth) {
		this.auth = auth;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SLF4JQueryLoggingListener.logger = logger;
	}

	public SLF4JLogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(SLF4JLogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public boolean isUseReplaceParameter() {
		return useReplaceParameter;
	}

	public void setUseReplaceParameter(boolean useReplaceParameter) {
		this.useReplaceParameter = useReplaceParameter;
	}

	public boolean isUseReplaceWhitespace() {
		return useReplaceWhitespace;
	}

	public void setUseReplaceWhitespace(boolean useReplaceWhitespace) {
		this.useReplaceWhitespace = useReplaceWhitespace;
	}

	public boolean isIncludeParameter() {
		return includeParameter;
	}

	public void setIncludeParameter(boolean includeParameter) {
		this.includeParameter = includeParameter;
	}

	public boolean isUseExcludeSqlLogging() {
		return useExcludeSqlLogging;
	}

	public void setUseExcludeSqlLogging(boolean useExcludeSqlLogging) {
		this.useExcludeSqlLogging = useExcludeSqlLogging;
	}

	public String getAnonymousTenancyId() {
		return anonymousTenancyId;
	}

	public void setAnonymousTenancyId(String anonymousTenancyId) {
		this.anonymousTenancyId = anonymousTenancyId;
	}

	public String getAnonymousCompanyId() {
		return anonymousCompanyId;
	}

	public void setAnonymousCompanyId(String anonymousCompanyId) {
		this.anonymousCompanyId = anonymousCompanyId;
	}

	public String getAnonymousUserName() {
		return anonymousUserName;
	}

	public void setAnonymousUserName(String anonymousUserName) {
		this.anonymousUserName = anonymousUserName;
	}

	public long getTimeLevelNormal() {
		return timeLevelNormal;
	}

	public void setTimeLevelNormal(long timeLevelNormal) {
		this.timeLevelNormal = timeLevelNormal;
	}

	public long getTimeLevelWarning() {
		return timeLevelWarning;
	}

	public void setTimeLevelWarning(long timeLevelWarning) {
		this.timeLevelWarning = timeLevelWarning;
	}

	public long getTimeLevelCritical() {
		return timeLevelCritical;
	}

	public void setTimeLevelCritical(long timeLevelCritical) {
		this.timeLevelCritical = timeLevelCritical;
	}

	public static Pattern getQuestionPattern() {
		return questionPattern;
	}

	public static void setQuestionPattern(Pattern questionPattern) {
		SLF4JQueryLoggingListener.questionPattern = questionPattern;
	}

	public String getDefaultTenancy() {
		return defaultTenancy;
	}

	public String getExcluedSqlLoggingRemark() {
		return excluedSqlLoggingRemark;
	}

	public String getLogTemplate() {
		return logTemplate;
	}
}
