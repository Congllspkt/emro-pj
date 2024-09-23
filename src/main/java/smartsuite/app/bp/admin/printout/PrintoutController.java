package smartsuite.app.bp.admin.printout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.printout.service.PrintoutService;

import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Printout 관련 처리를 하는 컨트롤러 Class입니다.
 *
 */
@Controller
@RequestMapping(value="**/bp/**/**")
public class PrintoutController {
	
	@Inject
	PrintoutService printoutService;
	
	/**
	 * 문서 출력 관리 조회(DOC_OUTP_MGMT)
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListDocumentOutputManagement.do")
	public @ResponseBody List<Map<String, Object>> findListDocumentOutputManagement(@RequestBody Map<String, Object> param) {
		return printoutService.findListDocumentOutputManagement(param);
	}
	
	/**
	 * 문서 출력 관리 정보 저장
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListDocumentOutputManagement.do")
	public @ResponseBody ResultMap saveListDocumentOutputManagement(@RequestBody Map<String, Object> param) {
		return printoutService.saveListDocumentOutputManagement(param);
	}
	
	/**
	 * 문서 출력 관리 정보 삭제
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListDocumentOutputManagement.do")
	public @ResponseBody ResultMap deleteListDocumentOutputManagement(@RequestBody Map<String, Object> param) {
		return printoutService.deleteListDocumentOutputManagement(param);
	}
	
	/**
	 * 문서 출력 데이터 설정 정보 조회(DOC_OUTP_DAT_SETUP)
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListDocumentOutputSetup.do")
	public @ResponseBody List<Map<String, Object>> findListDocumentOutputSetup(@RequestBody Map<String, Object> param) {
		return printoutService.findListDocumentOutputSetup(param);
	}
	
	/**
	 * 문서 출력 데이터 설정 정보 저장
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListDocumentOutputSetup.do")
	public @ResponseBody ResultMap saveListDocumentOutputSetup(@RequestBody Map<String, Object> param) {
		return printoutService.saveListDocumentOutputSetup(param);
	}
	
	/**
	 * 문서 출력 데이터 설정 정보 삭제
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListDocumentOutputDataSetup.do")
	public @ResponseBody ResultMap deleteListDocumentOutputDataSetup(@RequestBody Map<String, Object> param) {
		return printoutService.deleteListDocumentOutputDataSetup(param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터 조회(DOC_OUTP_DAT_PARM)
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListDocumentOutputDataParam.do")
	public @ResponseBody List<Map<String, Object>> findListDocumentOutputDataParam(@RequestBody Map<String, Object> param) {
		return printoutService.findListDocumentOutputDataParam(param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터 저장
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListDocumentOutputDataParam.do")
	public @ResponseBody ResultMap saveListDocumentOutputDataParam(@RequestBody Map<String, Object> param) {
		return printoutService.saveListDocumentOutputDataParam(param);
	}
	
	/**
	 * 문서 출력 데이터 파라미터삭제
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListDocumentOutputDataParam.do")
	public @ResponseBody ResultMap deleteListDocumentOutputDataParam(@RequestBody Map<String, Object> param) {
		return printoutService.deleteListDocumentOutputDataParam(param);
	}
}
