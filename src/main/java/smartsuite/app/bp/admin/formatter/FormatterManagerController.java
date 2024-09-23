package smartsuite.app.bp.admin.formatter;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.formatter.service.FormatterManagerService;

import smartsuite.app.common.shared.ResultMap;

/**
 * FormatterMgt 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @see 
 * @since 2019. 2. 19
 * @FileName FormatterMgtController.java
 * @package smartsuite.app.bp.admin.formatter
 * @변경이력 : [2019. 2. 19] 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/bp/**/")
public class FormatterManagerController {

	/** The formatter service. */
	@Inject
	FormatterManagerService formatterManagerService;
	
	/**
	 * list grp code by no formatter 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 2. 19
	 * @Method Name : findListGroupCodeByNoFormatter
	 */
	@RequestMapping(value = "findListGroupCodeByNoFormatter.do")
	public @ResponseBody List findListGroupCodeByNoFormatter(@RequestBody Map param) {
		return formatterManagerService.findListGroupCodeByNoFormatter(param);
	}
	
	/**
	 * list prec grp cd 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 2. 19
	 * @Method Name : findListPrecisionGroupCode
	 */
	@RequestMapping(value = "findListPrecisionGroupCode.do")
	public @ResponseBody List findListPrecisionGroupCode(@RequestBody Map param) {
		return formatterManagerService.findListPrecisionGroupCode(param);
	}
	
	/**
	 * list prec dtl cd 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 2. 19
	 * @Method Name : findListPrecisionDetailCode
	 */
	@RequestMapping(value = "findListPrecisionDetailCode.do")
	public @ResponseBody List findListPrecisionDetailCode(@RequestBody Map param) {
		return formatterManagerService.findListPrecisionDetailCode(param);
	}
	
	/**
	 * list prec grp cd 입력을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 19
	 * @Method Name : insertListPrecisionGroupCode
	 */
	@RequestMapping(value="insertListPrecisionGroupCode.do")
	public @ResponseBody ResultMap insertListPrecisionGroupCode(@RequestBody Map param){
		return formatterManagerService.insertListPrecisionGroupCode(param);
	}
	
	/**
	 * list prec grp cd 삭제를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 19
	 * @Method Name : deleteListPrecisionGroupCode
	 */
	@RequestMapping(value="deleteListPrecisionGroupCode.do")
	public @ResponseBody ResultMap deleteListPrecisionGroupCode(@RequestBody Map param){
		return formatterManagerService.deleteListPrecisionGroupCodeRequest(param);
	}
	
	/**
	 * list prec dtl cd 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 19
	 * @Method Name : saveListPrecisionDetailCode
	 */
	@RequestMapping(value="saveListPrecisionDetailCode.do")
	public @ResponseBody ResultMap saveListPrecisionDetailCode(@RequestBody Map param){
		return formatterManagerService.saveListPrecisionDetailCode(param);
	}
	
	/**
	 * list display format 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 2. 20
	 * @Method Name : findListDisplayFormat
	 */
	@RequestMapping(value="findListDisplayFormat.do")
	public @ResponseBody List findListDisplayFormat(@RequestBody Map param){
		return formatterManagerService.findListDisplayFormat(param);
	}
	
	/**
	 * list display format detail 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 2. 20
	 * @Method Name : findListDisplayFormatDetail
	 */
	@RequestMapping(value="findListDisplayFormatDetail.do")
	public @ResponseBody List findListDisplayFormatDetail(@RequestBody Map param){
		return formatterManagerService.findListDisplayFormatDetail(param);
	}
	
	/**
	 * list display format 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 21
	 * @Method Name : saveListDisplayFormat
	 */
	@RequestMapping(value="saveListDisplayFormat.do")
	public @ResponseBody ResultMap saveListDisplayFormat(@RequestBody Map param){
		return formatterManagerService.saveListDisplayFormat(param);
	}
	
	/**
	 * display format prec format 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 4. 15
	 * @Method Name : updateDisplayPrecisionFormat
	 */
	@RequestMapping(value="updateDisplayPrecisionFormat.do")
	public @ResponseBody ResultMap updateDisplayPrecisionFormat(@RequestBody Map param){
		return formatterManagerService.updateDisplayPrecisionFormat(param);
	}
	
	/**
	 * list display format 삭제를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 21
	 * @Method Name : deleteListDisplayFormat
	 */
	@RequestMapping(value="deleteListDisplayFormat.do")
	public @ResponseBody ResultMap deleteListDisplayFormat(@RequestBody Map param){
		return formatterManagerService.deleteListDisplayFormatRequest(param);
	}
	
	/**
	 * list display format detail 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 21
	 * @Method Name : saveListDisplayFormatDetail
	 */
	@RequestMapping(value="saveListDisplayFormatDetail.do")
	public @ResponseBody ResultMap saveListDisplayFormatDetail(@RequestBody Map param){
		return formatterManagerService.saveListDisplayFormatDetail(param);
	}
	
	/**
	 * list display format detail 삭제를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 2. 21
	 * @Method Name : deleteListDisplayFormatDetail
	 */
	@RequestMapping(value="deleteListDisplayFormatDetail.do")
	public @ResponseBody ResultMap deleteListDisplayFormatDetail(@RequestBody Map param){
		return formatterManagerService.deleteListDisplayFormatDetailRequest(param);
	}
	
	
	/**
	 * list user format 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 3. 5
	 * @Method Name : findListUserFormat
	 */
	@RequestMapping(value="findListUserFormat.do")
	public @ResponseBody List<Map<String, Object>> findListUserFormat(@RequestBody Map param){
		return formatterManagerService.findListUserFormat(param);
	}
	
	/**
	 * list user format dt 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 3. 5
	 * @Method Name : findListUserFormatByUserExpressionClass
	 */
	@RequestMapping(value="findListUserFormatByUserExpressionClass.do")
	public @ResponseBody List<Map<String, Object>> findListUserFormatByUserExpressionClass(@RequestBody Map param){
		return formatterManagerService.findListUserFormatByUserExpressionClass(param);
	}
	
	/**
	 * list user format dt 삭제를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 3. 5
	 * @Method Name : deleteListUserFormatInfo
	 */
	@RequestMapping(value="deleteListUserFormatInfo.do")
	public @ResponseBody ResultMap deleteListUserFormatInfo(@RequestBody Map param){
		return formatterManagerService.deleteListUserFormatInfo(param);
	}
	
	/**
	 * list user format dt 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2019. 3. 5
	 * @Method Name : saveListUserFormatInfo
	 */
	@RequestMapping(value="saveListUserFormatInfo.do")
	public @ResponseBody ResultMap saveListUserFormatInfo(@RequestBody Map param){
		return formatterManagerService.saveListUserFormatInfo(param);
	}
}
