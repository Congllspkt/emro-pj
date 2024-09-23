package smartsuite.app.common.compare;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.compare.service.CodeCompareService;

import smartsuite.app.common.shared.ResultMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class CodeCompareController {
	
	@Inject
	CodeCompareService codeCompareService;
	
	/**
	 * 테이블비교 정보 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListCompareTable.do")
	public @ResponseBody List findListCompareTable(@RequestBody Map param) {
		return codeCompareService.findListCompareTable(param);
	}
	
	/**
	 * 테이블비교 정보 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListCompareTable.do")
	public @ResponseBody ResultMap saveListCompareTable(@RequestBody Map<String, Object> param) {
		return codeCompareService.saveListCompareTable(param);
	}
	
	/**
	 * 테이블비교 정보 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteCompareTable.do")
	public @ResponseBody ResultMap deleteCompareTable(@RequestBody Map<String, Object> param) {
		return codeCompareService.deleteCompareTable(param);
	}
	
	/**
	 * 그룹코드 정보 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListGrpCd.do")
	public @ResponseBody List findListGrpCd(@RequestBody Map param) {
		return codeCompareService.findListGrpCd(param);
	}
	
	/**
	 * 그룹코드 정보 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListGrpCd.do")
	public @ResponseBody ResultMap saveListGrpCd(@RequestBody Map<String, Object> param) {
		return codeCompareService.saveListGrpCd(param);
	}
	
	/**
	 * 그룹코드 정보 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListGrpCd.do")
	public @ResponseBody ResultMap deleteListGrpCd(@RequestBody Map<String, Object> param) {
		return codeCompareService.deleteListGrpCd(param);
	}
}
