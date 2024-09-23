package smartsuite.app.common.util;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ListUtils {
	
	/**
	 * 첫번째 인자 list 기준으로 두번째 인자 list 합친다.<br>
	 * 이때 두 list 의 합치는 기준이 keys 이다.<br>
	 * 키가 같은 경우 합쳐질 두번째 인자 list 의 특정 컬럼은 네번째 인자에 정의한다.<br>
	 * 네번째 인자가 null인 경우 전부 합친다.<br>
	 * ex) ListUtils.innerJoin(baseList, mergeList, Lists.newArrayList("sys_id", "item_id"), Lists.newArrayList("item_nm"));<br>
	 * <br>
	 * 기준이 첫번째 인자이므로 두번째 인자 길이가 더 길다 하더라도 첫번째 인자보다 커지지 않는다.<br>
	 * 결론적으로, 첫번째 list에 두번째 list 값을 합친다.<br>
	 * 키값에 해당하는 값이 없다면 반환 시 제외된다.<br><br>
	 * 
	 * <b>Required:</b><br>
	 * list<br>
	 * mergeList<br>
	 * keys<br><br>
	 * 
	 * <b>Option:</b><br>
	 * columns<br>
	 * 
	 * @param list - 기준이 될 list
	 * @param mergeList - 기준에 합칠 list
	 * @param keys - 최소 하나의 key
	 * @param columns - mergeList 에서 합쳐질 columns
	 * @return List - 합쳐진 결과 list
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List innerJoin(List<Map> list, List<Map> mergeList, List<String> keys, List<String> columns) {
		if(list == null) {
			return null;
		}
		if(mergeList == null) {
			return list;
		}
		
		List result = Lists.newArrayList();
		List baseList = Lists.newArrayList(list);
		Map mergeMap = ListUtils.makeMergeMap(mergeList, keys);
		for(Map baseInfo : (List<Map>) baseList) {
			String baseKeySum = ListUtils.returnKeyValue(baseInfo, keys);
			Map mergeInfo = (Map) mergeMap.get(baseKeySum);
			if(mergeInfo == null) {
				continue;
			}
			if(columns == null) {
				baseInfo.putAll(mergeInfo);
			} else {
				for(String column : columns) {
					baseInfo.put(column, mergeInfo.get(column));
				}
			}
			result.add(baseInfo);
		}
		return result;
	}

	/**
	 * 첫번째 인자 list 기준으로 두번째 인자 list 합친다.<br>
	 * 이때 두 list 의 합치는 기준이 keys 이다.<br>
	 * 키가 같은 경우 합쳐질 두번째 인자 list 의 특정 컬럼은 네번째 인자에 정의한다.<br>
	 * 네번째 인자가 null인 경우 전부 합친다.<br>
	 * ex) ListUtils.leftOuterJoin(baseList, mergeList, Lists.newArrayList("sys_id", "item_id"), Lists.newArrayList("item_nm"));<br>
	 * <br>
	 * 기준이 첫번째 인자이므로 두번째 인자 길이가 더 길다 하더라도 첫번째 인자보다 커지지 않는다.<br>
	 * 결론적으로, 첫번째 list에 두번째 list 값을 합친다.<br><br>
	 * 
	 * <b>Required:</b><br>
	 * list<br>
	 * mergeList<br>
	 * keys<br><br>
	 * 
	 * <b>Option:</b><br>
	 * columns<br>
	 * 
	 * @param list - 기준이 될 list
	 * @param mergeList - 기준에 합칠 list
	 * @param keys - 최소 하나의 key
	 * @param columns - mergeList 에서 합쳐질 columns
	 * @return List - 합쳐진 결과 list
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List leftOuterJoin(List<Map> list, List<Map> mergeList, List<String> keys, List<String> columns) {
		if(list == null) {
			return null;
		}
		if(mergeList == null) {
			return list;
		}
		
		List baseList = Lists.newArrayList(list);
		Map mergeMap = ListUtils.makeMergeMap(mergeList, keys);
		for(Map baseInfo : (List<Map>) baseList) {
			String baseKeySum = ListUtils.returnKeyValue(baseInfo, keys);
			Map mergeInfo = (Map) mergeMap.get(baseKeySum);
			if(mergeInfo == null) {
				continue;
			}
			if(columns == null) {
				baseInfo.putAll(mergeInfo);
			} else {
				for(String column : columns) {
					baseInfo.put(column, mergeInfo.get(column));
				}
			}
		}
		return baseList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map makeMergeMap(List<Map> mergeList, List<String> keys) {
		Map mergeMap = Maps.newHashMap();
		for(Map mergeInfo : mergeList) {
			mergeMap.put(ListUtils.returnKeyValue(mergeInfo, keys), mergeInfo);
		}
		return mergeMap;
	}
	
	@SuppressWarnings("rawtypes")
	private static String returnKeyValue(Map data, List<String> keys) {
		String keySum = "";
		for(String key : keys) {
			if(data.get(key) instanceof Double) {
				keySum += Double.toString((Double) data.get(key));
			} else if(data.get(key) instanceof Integer) {
				keySum += Integer.toString((Integer) data.get(key));
			} else {
				keySum += (String) data.get(key);
			}
		}
		return keySum;
	}
	
	/**
	 * List Map 내부에서 key에 해당하는 value값을 List Object로 반환한다. <br>
	 * 가령, 리스트 내부의 item_cd 값만 List String으로 뽑아낼 경우<br>
	 * ex) ListUtils.getArrayValuesByList(list, "item_cd")<br>
	 * 
	 * @param list - List Map 형태의 list
	 * @param key - list 에서 뽑아낼 key value
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public static List getArrayValuesByList(List<Map> list, String key) {
		if(list == null) {
			return null;
		}
		if(list.isEmpty()) {
			return null;
		}
		if(key == null) {
			return null;
		}
		
		List<Object> result = Lists.newArrayList();
		for(Map item : list) {
			if(item.get(key) != null && !result.contains(item.get(key))) {
				result.add(item.get(key));
			}
		}
		return result;
	}
}
