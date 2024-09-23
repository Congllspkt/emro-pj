package smartsuite.app.common.portalwidget.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.exchange.scheduler.service.ExchangeSchedulerService;
import smartsuite.app.common.portalwidget.repository.PortalWidgetRepository;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PortalWidget 관련 처리하는 서비스 Class입니다.
 *
 * @see 
 * @FileName PortalWidgetService.java
 * @package smartsuite.app.common.portalwidget
 * @Since 2017. 12. 4
 */
@Service
@Transactional
public class PortalWidgetService {

	@Inject
	ExchangeSchedulerService exchangeSchedulerService;

	@Inject
	PortalWidgetRepository portalWidgetRepository;

	/**
	 * list board 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2017. 12. 4
	 * @Method Name : findListBoard
	 */
	public List<Map<String,Object>> findNoticeBoardList(Map<String, Object> param) {
		return portalWidgetRepository.findNoticeBoardList(param);
	}

	/**
	 * todo 정보를 조회한다.
	 *
	 * @param param the param
	 * @return the map<string, object>
	 * @Date : 2017. 12. 4
	 * @Method Name : findListTodo
	 */
	public Map<String, Object> findTodoInfo(Map<String, Object> param) {
		// 할일 항목 조회
		List<Map<String, Object>> todoList = this.findListMyTodo(param);
		
		Integer totalCnt = 0;
		for(Map<String, Object> todo : todoList) {
			// 할일 항목 관리에서 설정한 쿼리 아이디, 파라미터 정보를 사용하여 할일 항목별 count를 조회해온다.
			String parameters = todo.get("menu_url_parm") == null ? "" : todo.get("menu_url_parm").toString();
			parameters = parameters.substring( parameters.indexOf('?')+1 );
			String[] parameterArray = parameters.split("&");
			
			Map<String, Object> queryParameterMap = Maps.newHashMap();
			for(int i=0; i<parameterArray.length; i++) {
				String qryParam = parameterArray[i];
				String[] keyVal = qryParam.split("=");
				
				if(keyVal.length == 2) {
					queryParameterMap.put(keyVal[0], keyVal[1]);
				}
			}
			String sqlId = (String) todo.getOrDefault("sql_id","");
			Integer cnt = this.findTodoSqlIdMapper(queryParameterMap,sqlId);

			todo.put("cnt", cnt);
			totalCnt += cnt;
		}
		
		Map<String, Object> todoGroup = this.findTodoGroup(param);

		if(todoGroup != null) {
			todoGroup.put("tot_cnt", totalCnt);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("todoGroup", todoGroup);
		result.put("todoList", todoList);
		
		return result;
	}

	private Map<String, Object> findTodoGroup(Map<String, Object> param) {
		return portalWidgetRepository.findTodoGroup(param);
	}

	private Integer findTodoSqlIdMapper(Map<String, Object> qryParams ,String sqlId) {
		return portalWidgetRepository.findTodoSqlIdMapper(qryParams,sqlId);
	}

	private List<Map<String, Object>> findListMyTodo(Map<String, Object> param) {
		return portalWidgetRepository.findListMyTodo(param);
	}

	/**
	 * list exchange 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @throws Exception 
	 * @Date : 2017. 12. 20
	 * @Method Name : findListExchange
	 */
	public List<Map<String, Object>> findListExchangeView(Map<String, Object> param) throws Exception {
		List<Map<String,Object>> resultList = this.findListExchange(param);

		if(resultList.size() < 1){
			exchangeSchedulerService.updateListExchange(Maps.newHashMap());
			resultList = this.findListExchange(param);
		}
		return resultList;
	}

	private List<Map<String, Object>> findListExchange(Map<String, Object> param) {
		return portalWidgetRepository.findListExchange(param);
	}

	/**
	 * list po tot amt tree mon 조회한다.
	 */
	public List<Map<String, Object>> findListPoTotalAmountTree(Map<String, Object> paramMap) {
		
		Calendar cal= Calendar.getInstance();
		cal.add(Calendar.MONTH,-2);

		paramMap.put("tree_date", cal.getTime());
		Integer treeMonth = cal.get(Calendar.MONTH) + 1;
		
		List<Map<String,Object>> totalAmountList = this.findListPoTotalAmountTreeByDate(paramMap);
		
		//3개의 빈 리스트
		List<Map<String,Object>> resultList = Lists.newArrayList();
		for(int i = 0; i < 3 ; i++){
			Map<String,Object> monthStringMap = Maps.newHashMap();
			Integer month = treeMonth+i;
			if(month > 12){
				month = month - 12;
			}
			String monthStr = changeMonthString(month);
			monthStringMap.put("month",monthStr);
			for(Map<String,Object> totalAmount: totalAmountList){
				if(monthStr.equals(totalAmount.get("month"))){
					monthStringMap = totalAmount;
				}
			}
			resultList.add(monthStringMap);
		}
		
		Map<String,Object> totalAmountTree = this.findListPoTotalAmountTreeByYear(paramMap);
		resultList.add(totalAmountTree);
		return resultList;
	}

	private Map<String, Object> findListPoTotalAmountTreeByYear(Map<String, Object> paramMap) {
		return portalWidgetRepository.findListPoTotalAmountTreeByYear(paramMap);
	}

	private List<Map<String, Object>> findListPoTotalAmountTreeByDate(Map<String, Object> paramMap) {
		return portalWidgetRepository.findListPoTotalAmountTreeByDate(paramMap);
	}

	/**
	 * Change mon str.
	 *
	 * @param mon the mon
	 * @return the string
	 */
	private String changeMonthString(Integer mon){
		String monStr = "";
		if(mon < 10){
			monStr = "0" + mon;
		}else{
			monStr = Integer.toString(mon);
		}
		return monStr;
	}

	/**
	 * list exchange 수정한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map< string, object>
	 * 
	 * on
	 * @throws Exception 
	 * @Date : 2017. 12. 29
	 * @Method Name : updateListExchange
	 */
	public void updateListExchange(Map<String, Object> param) throws Exception{
		exchangeSchedulerService.updateListExchange(new HashMap<String,Object>());
	}
}
