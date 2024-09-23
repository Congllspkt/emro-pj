package smartsuite.app.common.holi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/holi/**/")
public class HoliController {

	@Inject
	private HoliService holiService;
	
	@RequestMapping(value = "findListHd.do")
	public @ResponseBody List<Map<String, Object>> findListHd(@RequestBody Map<String, Object> param){
		return holiService.findListHd(param);
	}
	
	@RequestMapping(value = "saveListHd.do")
	public @ResponseBody Map<String, Object> saveListHd(@RequestBody Map<String, Object> param){
		return holiService.saveListHd(param);
	}
	
	@RequestMapping(value = "deleteListHd.do")
	public @ResponseBody Map<String, Object> deleteListHd(@RequestBody Map<String, Object> param){
		return holiService.deleteListHd(param);
	}
	
}
