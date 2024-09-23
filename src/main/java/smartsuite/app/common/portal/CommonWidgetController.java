package smartsuite.app.common.portal;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by woomun.jung on 2016-03-28.
 */

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/portal/**")
public class CommonWidgetController {
	
	static final Logger logger = LoggerFactory.getLogger(CommonWidgetController.class); //NOPMD

    @RequestMapping(value = "findWidgetProperties.do", method = {RequestMethod.POST, RequestMethod.GET})
    @Deprecated
    public @ResponseBody String findWidgetProperties(@RequestBody Map param) throws Exception{
    	logger.warn("findWidgetProperties is Deprecated!!");
    	return null;
    }

    @RequestMapping(value = "saveWidgetProperties.do", method = {RequestMethod.POST, RequestMethod.GET})
    @Deprecated
    public @ResponseBody Integer saveWidgetProperties(@RequestBody Map param) throws Exception{
    	logger.warn("saveWidgetProperties is Deprecated!!");
    	return 0;
    }
    
    @RequestMapping(value = "deleteWidgetProperties.do", method = {RequestMethod.POST, RequestMethod.GET})
    @Deprecated
    public @ResponseBody Integer deleteWidgetProperties(@RequestBody Map param) throws Exception{
    	logger.warn("deleteWidgetProperties is Deprecated!!");
        return 0;
    }
}
