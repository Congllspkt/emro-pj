package smartsuite.app.bp.admin.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.code.service.CommonCodeService;

import smartsuite.app.common.shared.ResultMap;

/**
 * CommonCode 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 2. 2
 * @FileName CommonCodeController.java
 * @package smartsuite.app.common.code
 * @변경이력 : [2016. 2. 2] JuEung Kim 최초작성
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class CommonCodeController {
	
	/** The common code service. */
	@Inject
	CommonCodeService commonCodeService;
	
	/**
	 * 그룹코드 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the grp code list
	 * @Date : 2016. 2. 2
	 * @Method Name : findListGroupCode
	 */
	@RequestMapping(value = "findListGroupCode.do")
	public @ResponseBody List findListGroupCode(@RequestBody Map param) {
		return commonCodeService.findListGroupCode(param);
	}
	
	/**
	 * 그룹코드 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListGroupCode
	 */
	@RequestMapping(value = "saveListGroupCode.do")
//	@CacheEvict(value="cmmn-code", key="#param['ccd'] + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public @ResponseBody ResultMap saveListGroupCode(@RequestBody Map param) {
		return commonCodeService.saveListGroupCode(param);
	}
	
	/**
	 * 그룹코드 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListGrpCode
	 */
	@RequestMapping(value = "deleteListGrpCode.do")
	public @ResponseBody ResultMap deleteListGrpCode(@RequestBody Map param) {
		return commonCodeService.deleteListGroupCodeProcess(param);
	}

	
	/**
	 * 그룹코드 속성 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the grp attr code list
	 * @Date : 2016. 2. 2
	 * @Method Name : findListGroupCodeAttribute
	 */
	@RequestMapping(value = "findListGroupCodeAttribute.do")
	public @ResponseBody Map findListGroupCodeAttribute(@RequestBody Map param) {
		Map result = Maps.newHashMap();
		result.put("GroupCodeAttributeList", commonCodeService.findListGroupCodeAttribute(param) );
		result.put("detailCodeList",  commonCodeService.findListDetailCode(param) );
		return result;
	}
	
	/**
	 * 그룹코드 속성 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListGroupCodeAttribute
	 */
	@RequestMapping(value = "saveListGroupCodeAttribute.do")
	public @ResponseBody ResultMap saveListGroupCodeAttribute(@RequestBody Map param) {
		return commonCodeService.saveListGroupCodeAttribute(param);
	}
	
	/**
	 * 그룹코드 속성 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListGroupCodeAttribute
	 */
	@RequestMapping(value = "deleteListGroupCodeAttribute.do")
	public @ResponseBody ResultMap deleteListGroupCodeAttribute(@RequestBody Map param) {
		return commonCodeService.deleteListGroupCodeAttributeRequest(param);
	}
	
	/**
	 * 상세코드 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the dtl code list
	 * @Date : 2016. 2. 2
	 * @Method Name : findListDetailCode
	 */
	@RequestMapping(value = "findListDetailCode.do")
	public @ResponseBody List findListDetailCode(@RequestBody Map param) {
		return commonCodeService.findListDetailCode(param);
	}
	
	/**
	 * 상세코드 저장을 요청한다.
	 *
	 * @param param the param
	 */
	@RequestMapping(value = "saveListDetailCode.do")
	@CacheEvict(value="cmmn-code", key="#param['grp_cd'] + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public @ResponseBody ResultMap saveListDetailCode(@RequestBody Map param) {
		return commonCodeService.saveListDetailCode(param);
	}
	
	/**
	 * 상세코드 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 3
	 * @Method Name : deleteListDetailCode
	 */
	@RequestMapping(value = "deleteListDetailCode.do")
	@CacheEvict(value="cmmn-code", key="#param['grp_cd'] + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public @ResponseBody ResultMap deleteListDetailCode(@RequestBody Map param) {
		return commonCodeService.deleteListDetailCodeRequest(param);
	}
}