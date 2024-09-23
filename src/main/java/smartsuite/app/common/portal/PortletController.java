package smartsuite.app.common.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.portal.service.PortletService;
import smartsuite.app.common.shared.ResultMap;

/**
 * Portlet 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 10. 12
 * @FileName PortletController.java
 * @package smartsuite.app.bp.common.portlet
 * @변경이력 : [2016. 10. 12] JuEung Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/portlet/**/")
@PreAuthorize("isRoleAdmin() and isAllowIp()")
public class PortletController {

	/** The portlet service. */
	@Inject
	PortletService portletService;

	/**
	 * 포틀릿 현황 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 10. 12
	 * @Method Name : findListPortlet
	 */
	@RequestMapping (value = "findListPortlet.do")
	public @ResponseBody List findListPortlet(@RequestBody Map param) {
		return portletService.findListPortlet(param);
	}
	
	/**
	 * 포틀릿 정보 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 10. 13
	 * @Method Name : findInfoPortlet
	 */
	@RequestMapping (value = "findInfoPortlet.do")
	public @ResponseBody Map findInfoPortlet(@RequestBody Map param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		resultMap.put("portletInfo", portletService.findInfoPortlet(param) );
		resultMap.put("portletRoleList", portletService.findListPortletRoles(param) );
		
		return resultMap;
	}
	
	/**
	 * 포틀릿 정보 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 10. 13
	 * @Method Name : saveInfoPortlet
	 */
	@RequestMapping(value = "saveInfoPortlet.do")
	public @ResponseBody ResultMap saveInfoPortlet(@RequestBody Map param) {
		return portletService.saveInfoPortlet(param);
	}
	
	/**
	 * 포틀릿 현황 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 10. 13
	 * @Method Name : deleteListPortlet
	 */
	@RequestMapping(value = "deleteListPortlet.do")
	public @ResponseBody ResultMap deleteListPortlet(@RequestBody Map param) {
		return portletService.deleteListPortlet(param);
	}
	
	/**
	 * 포틀릿 사용자 롤 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 10. 13
	 * @Method Name : findListPortletRole
	 */
	@RequestMapping (value = "findListPortletRole.do")
	public @ResponseBody List findListPortletRole(@RequestBody Map param) {
		return portletService.findListPortletRoles(param);
	}
    
    @RequestMapping(value = "findDefaultLayout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Map findDefaultLayout(@RequestBody Map param){
        return portletService.findDefaultLayout(param);
    }
    
    @RequestMapping(value = "saveDefaultLayout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody ResultMap saveDefaultLayout(@RequestBody Map param){
        return portletService.saveDefaultLayout(param);
    }
		
}