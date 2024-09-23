package smartsuite.app.common.portalwidget;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.portalwidget.service.PortalWidgetService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.mybatis.data.impl.PageResult;

/**
 * PortalWidget 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @see 
 * @since 2017. 12. 4
 * @FileName PortalWidgetController.java
 * @package smartsuite.app.common.portalwidget
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/portal/widget/**/")
public class PortalWidgetController {
	
	/** The portal widget service. */
	@Inject
	PortalWidgetService portalWidgetService;
	
	/**
	 * notice board list 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the page result
	 * @Date : 2017. 12. 4
	 * @Method Name : findNoticeBoardList
	 */
	@RequestMapping(value = "findNoticeBoardList.do")
	public @ResponseBody PageResult findNoticeBoardList(@RequestBody Map param) {
		List result = portalWidgetService.findNoticeBoardList(param);
		return new PageResult(result);
	}
	
	/**
	 * to do 정보 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the map<string, object>
	 * @Date : 2017. 12. 4
	 * @Method Name : findTodoInfo
	 */
	@RequestMapping(value="findTodoInfo.do")
	public @ResponseBody Map<String, Object> findTodoInfo(@RequestBody Map<String, Object> param) {
		return portalWidgetService.findTodoInfo(param);
	}
	
	/**
	 * 환율리스트API 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param request the request
	 * @param response the response
	 * @param param the param
	 * @throws Exception 
	 * @Date : 2017. 12. 19
	 * @Method Name : findListExchange
	 */
	@RequestMapping (value = "findListExchange.do")
	public @ResponseBody List<Map<String, Object>> findListExchange(@RequestBody Map<String, Object> param) throws Exception {
		return portalWidgetService.findListExchangeView(param);
	}
	
	/**
	 * list exchange 수정을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @throws Exception 
	 * @Date : 2017. 12. 29
	 * @Method Name : updateListExchange
	 */
	@RequestMapping (value = "updateListExchange.do")
	public @ResponseBody ResultMap updateListExchange(@RequestBody Map<String, Object> param) throws Exception {
		portalWidgetService.updateListExchange(param);
		return ResultMap.SUCCESS();
	}
	
	/**
	 * list po tot amt tree mon 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2017. 12. 20
	 * @Method Name : findListPoTotalAmountTreeByDate
	 */
	@RequestMapping(value="findListPoTotalAmountTreeByDate.do")
	public @ResponseBody List<Map<String, Object>> findListPoTotalAmountTreeByDate(@RequestBody Map<String, Object> param) {
		return portalWidgetService.findListPoTotalAmountTree(param);
	}
}
