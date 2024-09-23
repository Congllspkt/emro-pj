package smartsuite.app.bp.admin.code;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.code.service.DocNumberService;

import smartsuite.app.common.shared.ResultMap;

/**
 * DocNumber 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 2. 3
 * @FileName DocNumberController.java
 * @package smartsuite.app.bp.admin.code
 * @변경이력 : [2016. 2. 3] JuEung Kim 최초작성
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class DocNumberController {
	
	/** The doc number service. */
	@Inject
	DocNumberService docNumberService;
	
	/**
	 * 문서번호 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 3
	 * @Method Name : findListDocNumber
	 */
	@RequestMapping(value = "findListDocNumber.do")
	public @ResponseBody List findListCodeType(@RequestBody Map param) {
		return docNumberService.findListDocNumber(param);
	}
	
	/**
	 * 문서번호 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 3
	 * @Method Name : saveListDocNumber
	 */
	@RequestMapping(value = "saveListDocNumber.do")
	public @ResponseBody ResultMap saveListDocNumber(@RequestBody Map param) {
		return docNumberService.saveListDocNumber(param);
	}
	
	/**
	 * 문서번호 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 3
	 * @Method Name : deleteListDocNumber
	 */
	@RequestMapping(value = "deleteListDocNumber.do")
	public @ResponseBody ResultMap deleteListDocNumber(@RequestBody Map param) {
		return docNumberService.deleteListDocNumberRequest(param);
	}
	
}