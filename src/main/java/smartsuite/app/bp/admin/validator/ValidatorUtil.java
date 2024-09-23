package smartsuite.app.bp.admin.validator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({"rawtypes", "unchecked"})
public final class ValidatorUtil {

	private ValidatorUtil() {}

	 /**
	 * validate 수행 후 리턴된 결과로 resultMap을 생성한다.
	 *
	 * @param param : validate 수행 시 파라미터
	 * @param checkResult : validate 수행 결과
	 * @return
	 */
	public static ResultMap getResultMapByCheckResult(Map<String, Object> param, Map<String, Object> checkResult) {
		ResultMap resultMap = ResultMap.getInstance();

		if(MapUtils.isEmpty(checkResult)){
			resultMap.setResultData(param);
			resultMap.setResultStatus(ResultMap.STATUS.NOT_EXIST);
		} else if("Y".equals(checkResult.getOrDefault(ValidatorConst.VALID_YN , "") == null ? "" : checkResult.getOrDefault(ValidatorConst.VALID_YN , ""))) {
			resultMap.setResultStatus(ResultMap.STATUS.SUCCESS);
		}else {
			resultMap.setResultStatus(ResultMap.STATUS.INVALID_STATUS_ERR);
			resultMap.setResultData(checkResult);
		}

		return resultMap;
	}
	
	/**
	 * validate 수행 후 리턴된 결과로 예외 처리
	 * 
	 * @param checkParam : validate 수행 시 파라미터
	 * @param checkResult : validate 수행 결과
	 */
	public static void handleResultMapByCheckResult(Map<String, Object> checkResult) {
		String validationParam = checkResult.getOrDefault(ValidatorConst.VALID_YN , "") == null? "" : (String) checkResult.getOrDefault(ValidatorConst.VALID_YN , "");
		if(MapUtils.isEmpty(checkResult)) {
			throw new CommonException(ErrorCode.NOT_EXIST);
		} else if(!("Y").equals(validationParam)) {
			throw new CommonException(ErrorCode.INVALID_STATUS);
		}
	}

	/**
	 * 업무 method 내에서 validation 처리 후 validation type에 따라 result data map 에 담아 표현 할 때 사용
	 * 
	 * @param parameterList : validator 수행 시 파라미터 리스트
	 * @param invalidDataList : validator 수행 후 유효하지 않는 데이터 리스트
	 * @param notExistDataList : validator 수행 후 존재하지 않는 데이터 리스트
	 * @return
	 */
	public static ResultMap setupDataListForValidationDataList(List<?> parameterList, List<Map<String, Object>> invalidDataList, List<Map<String, Object>> notExistDataList) {
		ResultMap resultMap = ResultMap.getInstance();

		int notExecuteDataSize = invalidDataList.size() + notExistDataList.size();
		if(notExecuteDataSize == 0) {
			resultMap.setResultStatus(ResultMap.STATUS.SUCCESS);
		} else {
			Map<String, Object> resultDataMap = Maps.newHashMap();
			resultDataMap.put(ValidatorConst.INVALID_DATAS  , invalidDataList);
			resultDataMap.put(ValidatorConst.NOT_EXIST_DATAS, notExistDataList);
			resultDataMap.put(ValidatorConst.VALID_CNT      , parameterList.size() - notExecuteDataSize);

			resultMap.setResultData(resultDataMap);
			resultMap.setResultStatus(ResultMap.STATUS.INVALID_STATUS_ERR);
		}
		return resultMap;
	}
	
	/**
	 * 유효여부에 따라 그룹핑한 validate 결과 리스트로 resultMap 생성
	 * 
	 * @param parameterIdList : validate 수행 시 파라미터 ids
	 * @param useValidSearchDataList : valid_yn 으로 그룹핑한 validate 결과 리스트 {valid_yn, itemList}
	 * @param columId : 데이터 id column
	 * @param returnErrorCode : 유효하지 않은 데이터 포함 시 리턴할 오류코드
	 * @return resultMap {result_status, result_data{invalid_datas, not_exist_ids}
	 */
	public static ResultMap getResultMapByCheckValidYnList(List<String> parameterIdList, List<Map<String, Object>> useValidSearchDataList, String columId, String returnErrorCode) {
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> resultDataMap = Maps.newHashMap();
		
		List<String> validIdList = Lists.newArrayList();
		List<String> notExistIdList = Lists.newArrayList();
		List<Map<String, Object>> invalidDataList = Lists.newArrayList();
		int validIdSize = 0;
		
		if(useValidSearchDataList.isEmpty()) {
			resultMap.setResultStatus(returnErrorCode);  						// 오류
			notExistIdList.addAll(parameterIdList);								// 삭제되거나 존재하지 않는 id 리스트
		} else {
			for(Map<String, Object> searchDataMap : useValidSearchDataList) {
				List<Map<String, Object>> checkedList = (List<Map<String, Object>>)searchDataMap.getOrDefault("itemList",Lists.newArrayList());
				String validYn = searchDataMap.getOrDefault(ValidatorConst.VALID_YN,"") == null? "": (String) searchDataMap.getOrDefault(ValidatorConst.VALID_YN,"");

				if("Y".equals(validYn)) {
					validIdList.addAll(findDataMapListByKeyForResultMap(checkedList, columId));
				} else {
					invalidDataList.addAll(checkedList);
				}
			}

			validIdSize = validIdList.size();
			int invalidDataSize = invalidDataList.size();
			int parameterIdSize = parameterIdList.size();

			if(validIdSize == parameterIdSize) {
				resultMap.setResultStatus(ResultMap.STATUS.SUCCESS);			// 유효
			} else {
				resultMap.setResultStatus(returnErrorCode);			// 오류
				
				if(validIdSize + invalidDataSize < parameterIdSize) {
					notExistIdList = new ArrayList<String>(parameterIdList);
					notExistIdList.removeAll(validIdList);
					notExistIdList.removeAll(findDataMapListByKeyForResultMap(invalidDataList, columId));
				}
			}
		}
		resultDataMap.put(ValidatorConst.VALID_IDS, validIdList);					// 유효 id 리스트
		resultDataMap.put(ValidatorConst.VALID_CNT, validIdSize);			// 유효 count
		
		if(!Const.SUCCESS.equals(resultMap.getResultStatus())) {
			resultDataMap.put(ValidatorConst.NOT_EXIST_IDS, notExistIdList);			// 삭제되거나 존재하지 않는 id 리스트
			resultDataMap.put(ValidatorConst.INVALID_DATAS, invalidDataList);			// 유효하지 않은 데이터 리스트
		}
		
		resultMap.setResultData(resultDataMap);
		return resultMap;
	}
	
	/**
	 * column id 값으로 list 내에 value를 찾아서 해당 column id (key)의 value (object)를 list 형으로 result 해준다.
	 *  -> object를 string으로 변환해주어도 될 것으로 보이나, 우선 object를 그대로 가져감.
	 * @param dataList
	 * @param columnId
	 * @return
	 */
	private static List findDataMapListByKeyForResultMap(List<Map<String, Object>> dataList, String columnId) {
		List resultMap = Lists.newArrayList();
		for(Map<String, Object> dataInfo : dataList) {
			Object getValue = dataInfo.get(columnId);
			if(getValue != null) {
				resultMap.add(getValue);
			}
		}
		return resultMap;
	}
}
