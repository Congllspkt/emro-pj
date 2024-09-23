package smartsuite.app.common.shared;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class ResultMap {

	public static class STATUS {
		public final static String SUCCESS = "S";
		public final static String FAIL = "E";
		public final static String DUPLICATED = "D";
		public final static String USED = "U";
		public final static String NOT_EXIST = "N";
		public final static String SKIP = "SKIP";
		public final static String INVALID_STATUS_ERR = "INVALID_STATUS_ERR";
	}

	private String resultStatus;

	public void setResultStatus(Object resultStatus) {
		this.resultStatus = (String) resultStatus;
	}

	public String getResultStatus() {
		return this.resultStatus;
	}

	public boolean isSuccess() {
		return this.resultStatus.equals(ResultMap.STATUS.SUCCESS) ||
				this.resultStatus.equals(ResultMap.STATUS.SKIP);
	}

	public boolean isFail() {
		return this.resultStatus.equals(ResultMap.STATUS.FAIL);
	}

	public boolean isDuplicate() {
		return this.resultStatus.equals(ResultMap.STATUS.DUPLICATED);
	}

	public boolean getSuccess() {
		return this.isSuccess();
	}

	private String resultMessage;

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getResultMessage() {
		return this.resultMessage;
	}

	private Map<String, Object> resultData;

	public void setResultData(Map<String, Object> resultData) {
		this.resultData = resultData;
	}

	public Map<String, Object> getResultData() {
		return this.resultData;
	}

	private List<Map<String, Object>> resultList;

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public List<Map<String, Object>> getResultList() {
		return this.resultList;
	}


	public static ResultMap getInstance() {
		return ResultMap.builder().build();
	}

	public static ResultMap SUCCESS() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.SUCCESS)
				.build();
	}

	public static ResultMap SUCCESS(Map<String, Object> resultData) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.SUCCESS)
				.resultData(resultData)
				.build();
	}

	public static ResultMap SUCCESS(List<Map<String, Object>> resultList) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.SUCCESS)
				.resultList(resultList)
				.build();
	}

	public static ResultMap FAIL() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.FAIL)
				.build();
	}

	public static ResultMap FAIL(Map<String, Object> resultData) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.FAIL)
				.resultData(resultData)
				.build();
	}
	public static ResultMap FAIL(String resultMessage) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.FAIL)
				.resultMessage(resultMessage)
				.build();
	}
	public static ResultMap FAIL(List<Map<String, Object>> resultList) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.FAIL)
				.resultList(resultList)
				.build();
	}

	public static ResultMap USED() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.USED)
				.build();
	}

	public static ResultMap USED(String resultMessage) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.USED)
				.resultMessage(resultMessage)
				.build();
	}

	public static ResultMap USED(Map<String, Object> resultData) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.USED)
				.resultData(resultData)
				.build();
	}

	public static ResultMap USED(List<Map<String, Object>> resultList) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.USED)
				.resultList(resultList)
				.build();
	}

	public static ResultMap NOT_EXISTS() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.NOT_EXIST)
				.build();
	}

	public static ResultMap NOT_EXISTS(String resultMessage) {
		return ResultMap.builder()
				.resultStatus(STATUS.NOT_EXIST)
				.resultMessage(resultMessage)
				.build();
	}

	public static ResultMap SKIP() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.SKIP)
				.build();
	}

	public static ResultMap INVALID() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.INVALID_STATUS_ERR)
				.build();
	}

	public static ResultMap DUPLICATED() {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.DUPLICATED)
				.build();
	}

	public static ResultMap DUPLICATED(String resultMessage) {
		return ResultMap.builder()
				.resultStatus(ResultMap.STATUS.DUPLICATED)
				.resultMessage(resultMessage)
				.build();
	}

	private ResultMap(String resultStatus, String resultMessage, Map<String, Object> resultData, List<Map<String, Object>> resultList) {
		this.resultStatus = StringUtils.isEmpty(resultStatus) ? STATUS.SUCCESS : resultStatus;
		this.resultMessage = resultMessage;
		this.resultData = resultData;
		this.resultList = resultList;
	}

	public static ResultMapBuilder builder() {
		return new ResultMapBuilder();
	}

	public static class ResultMapBuilder {
		private String resultStatus;
		private String resultMessage;
		private Map<String, Object> resultData;
		private List<Map<String, Object>> resultList;

		public ResultMapBuilder resultStatus(String resultStatus) {
			this.resultStatus = resultStatus;
			return this;
		}

		public ResultMapBuilder resultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
			return this;
		}

		public ResultMapBuilder resultData(Map<String, Object> resultData) {
			this.resultData = resultData;
			return this;
		}

		public ResultMapBuilder resultList(List<Map<String, Object>> resultList) {
			this.resultList = resultList;
			return this;
		}

		public ResultMap build() {
			return new ResultMap(resultStatus, resultMessage, resultData, resultList);
		}
	}

}
