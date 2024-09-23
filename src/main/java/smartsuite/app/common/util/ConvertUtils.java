package smartsuite.app.common.util;

public class ConvertUtils {
	
	/**
	 * 그리드 수량 / 단가 / 금액 등 데이터가 작업방식에 따라 java data type 다른 문제를 해결하기 위함
	 *
	 * @param data
	 * @return String
	 */
	public static String convertString(Object data) {
		if(data == null) {
			return null;
		}
		if(data instanceof String) {
			return (String) data;
		} else if(data instanceof Integer) {
			return Integer.toString((Integer) data);
		} else if(data instanceof Double) {
			return Double.toString((Double) data);
		} else {
			return data.toString();
		}
	}
	
	/**
	 * null 과 0 은 다르므로 데이터 존재하지 않을 경우 0 으로 인식하기 위함
	 *
	 * @param data
	 * @return String
	 */
	public static String convertStringNullZero(Object data) {
		if(data == null) {
			return "0";
		}
		return ConvertUtils.convertString(data);
	}
}
