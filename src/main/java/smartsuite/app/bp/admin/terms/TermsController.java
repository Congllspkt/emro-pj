package smartsuite.app.bp.admin.terms;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.terms.service.TermsService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 약관 관리 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @see 
 * @since 2019. 05. 08
 * @FileName TermsController.java
 * @package smartsuite.app.bp.admin.terms
 * @변경이력 : [2019. 05. 08] 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/bp/**")
public class TermsController {

	/** The formatter service. */
	@Inject
	TermsService termsService;

	
	/**
	 * 약관 목록 조회
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 05. 08
	 * @Method Name : findListTerms
	 */
	@RequestMapping(value = "findListTerms.do")
	public @ResponseBody List findListTerms(@RequestBody Map param) {
		return termsService.findListTerms(param);
	}
	
	/* 
	 * 템플릿 구분 Combobox 목록 조회 
	 * 
	 * @param param the param
	 * @return the list
	 * @Date : 2019. 05. 08
	 * @Method Name : findListTermsTextModeTemplateComboboxItem
	 */
	@RequestMapping(value="findListTermsTextModeTemplateComboboxItem.do")
	public @ResponseBody List findListTermsTextModeTemplateComboboxItem(@RequestBody Map param){
		return termsService.findListTermsTextModeTemplateComboboxItem(param);
	}
	
	/* 
	 * 약관 상세 정보 저장 
	 * 
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : saveTerms
	 */
	@RequestMapping(value="saveTerms.do")
	public @ResponseBody ResultMap saveTerms(@RequestBody Map param) throws Exception {
		return termsService.saveTermsInfo(param);
	}
	
	/* 
	 * 약관 정보 삭제 
	 * 
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : deleteTerms
	 */
	@RequestMapping(value="deleteTermsInfo.do")
	public @ResponseBody ResultMap deleteTermsInfo(@RequestBody Map param){
		return termsService.deleteTermsInfo(param);
	}
	
	/**
	 * 약관 상세 조회
	 *
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : findTermsDetailInfoByTermsConditionsId
	 */
	@RequestMapping(value = "findTermsDetailInfoByTermsConditionsId.do")
	public @ResponseBody Map findTermsDetailInfoByTermsConditionsId(@RequestBody Map param) {
		return termsService.findTermsDetailInfoByTermsConditionsId(param);
	}
	
	@RequestMapping(value = "findTermsMultiLangDetailInfoByTermsConditionsId.do")
	public @ResponseBody Map findTermsMultiLangDetailInfoByTermsConditionsId(@RequestBody Map param) {
		return termsService.findTermsMultiLangDetailInfoByTermsConditionsId(param);
	}

	/**
	 * 약관 구분 기준 Max 차수 상세 조회
	 *
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : findMaxTcByTcClsCd
	 */
	@RequestMapping(value = "findLastRevisionTermsMasterAndContentByTermsConditionClassCode.do")
	public @ResponseBody Map findLastRevisionTermsMasterAndContentByTermsConditionClassCode(@RequestBody Map param) {
		return termsService.findLastRevisionTermsMasterAndContentByTermsConditionClassCode(param);
	}

}
