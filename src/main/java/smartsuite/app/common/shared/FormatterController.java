package smartsuite.app.common.shared;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


// TODO: Auto-generated Javadoc
/**
 * Formatter 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @see 
 * @since 2019. 3. 7
 * @FileName FormatterController.java
 * @package smartsuite.app.common.shared
 * @변경이력 : [2019. 3. 7] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class FormatterController {

	/** The formatter provider. */
	@Inject
	FormatterProvider formatterProvider;
	
	/**
	 * list formatter 조회를 요청한다.
	 *
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : findListFormatter
	 */
	@RequestMapping(value="/**/findListFormatter.do")
	public @ResponseBody Map findListFormatter() {
		return formatterProvider.findListFormatter();
	}
	
	/**
	 * current user all display format 조회를 요청한다.
	 *
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : findCurrentUserAllDisplayFormat
	 */
	@RequestMapping(value="/**/findCurrentUserAllDisplayFormat.do")
	public @ResponseBody Map findCurrentUserAllDisplayFormat() {
		return formatterProvider.findCurrentUserAllDisplayFormat();
	}
	
	/**
	 * list prec format cur 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 3. 7
	 * @Method Name : findListPrecisionFormatCur
	 */
	@RequestMapping(value="/**/findListPrecisionFormatCur.do")
	public @ResponseBody List findListPrecisionFormatCur(@RequestBody Map param){
		return formatterProvider.findListPrecisionFormatCur(param);
	}
	
	/**
	 * list all prec format 조회를 요청한다.
	 *
	 * @return the list
	 * @Date : 2019. 3. 7
	 * @Method Name : findListPrecisionFormat
	 */
	@RequestMapping(value="/**/findListPrecisionFormat.do")
	public @ResponseBody List findListPrecisionFormat(){
		return formatterProvider.findListPrecisionFormat();
	}
	
	/**
	 * all user exp format 조회를 요청한다.
	 *
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : findAllUserExpFormat
	 */
	@RequestMapping(value="/**/findAllUserExpFormat.do")
	public @ResponseBody Map findAllUserExpFormat(){
		return formatterProvider.findAllUserExpFormat();
	}
	
	/**
	 * currnt user format info 조회를 요청한다.
	 *
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : findCurrentUserFormatInfo
	 */
	@RequestMapping(value="/**/findCurrentUserFormatInfo.do")
	public @ResponseBody Map findCurrentUserFormatInfo(){
		return formatterProvider.findCurrentUserFormatInfo();
	}
	
	/**
	 * current user formatter 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : saveCurrentUserFormatter
	 */
	@RequestMapping(value="/**/saveCurrentUserFormatter.do")
	public @ResponseBody Map saveCurrentUserFormatter(@RequestBody Map param){
		return formatterProvider.saveCurrentUserFormatter(param);
	}
	
	/**
	 * user format info 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : findUserFormatInfo
	 */
	@RequestMapping (value = "/**/findUserFormatInfo.do")
	public @ResponseBody Map findUserFormatInfo(@RequestBody Map param) {
		return formatterProvider.findUserFormatInfo(param);
	}
	
	/**
	 * user formatter 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 3. 7
	 * @Method Name : saveUserFormatter
	 */
	@RequestMapping(value="/**/saveUserFormatter.do")
	public @ResponseBody Map saveUserFormatter(@RequestBody Map param){
		return formatterProvider.saveUserFormatter(param);
	}
}
