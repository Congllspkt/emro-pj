package smartsuite.app.common.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.portal.service.PortalService;
import smartsuite.app.common.portal.service.PortletService;

/**
 * Created by woomun.jung on 2016-03-28.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/portal/**")
public class PortalController {

	/** The portlet service. */
	@Inject
	PortletService portletService;

    @Autowired
    private PortalService portalService;

    @RequestMapping(value = "findUserLayout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Map findUserLayout(@RequestBody Map param){
    	Map userLayout = portalService.findUserLayout(param);
    	
    	if(userLayout != null){
    		return userLayout;
    	}

        return portletService.findDefaultLayout(param);
    }
    
    @RequestMapping(value = "saveUserLayout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Integer saveUserLayout(@RequestBody Map param){
        return  portalService.saveUserLayout(param);
    }
    
    @RequestMapping(value = "deleteUserLayout.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Integer deleteUserLayout(@RequestBody Map param){
        return  portalService.deleteUserLayout(param);
    }
    
    @RequestMapping(value = "findComposableWidgetList.do", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody Map findComposableWidgetList(@RequestBody Map param) {
        return portalService.findComposableWidgetList(param);
    }
    
    @RequestMapping(value="findPortalCommonConfig.do")
    public @ResponseBody Map findPortalCommonConfig(@RequestBody Map param) {
    	return portalService.findPortalCommonConfig(param);
    }
    
    @RequestMapping(value="savePortalCommonConfig.do")
    public @ResponseBody Integer savePortalCommonConfig(@RequestBody Map param) {
    	return portalService.savePortalCommonConfig(param);
    }
    
    @RequestMapping(value="updatePortalCommonConfig.do")
    public @ResponseBody Integer updatePortalCommonConfig(@RequestBody Map param) {
    	return portalService.updatePortalCommonConfig(param);
    }
}
