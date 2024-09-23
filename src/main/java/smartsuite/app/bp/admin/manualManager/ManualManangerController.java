package smartsuite.app.bp.admin.manualManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.manualManager.service.ManualManagerService;

import smartsuite.app.common.shared.ResultMap;

/**
 * Manual 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 6. 28
 * @FileName ManualController.java
 * @package smartsuite.app.bp.admin.manual
 * @변경이력 : [2016. 6. 28] JuEung Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/bp/**")
public class ManualManangerController {

	/** The manual service. */
	@Inject
	ManualManagerService manualManagerService;

	/**
	 * 메뉴목록을 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 6. 28
	 * @Method Name : findListSelectionLanguageManualMenu
	 */
	@RequestMapping (value = "findListSelectionLanguageManualMenu.do")
	public @ResponseBody List findListSelectionLanguageManualMenu(@RequestBody Map param) {
		return manualManagerService.findListSelectionLanguageManualMenu(param);
	}
	
	/**
	 * 다국어 메뉴얼 목록 조회
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findListAllLanguageManual.do")
	public @ResponseBody List findListAllLanguageManual(@RequestBody Map param) {
		return manualManagerService.findListAllLanguageManual(param);
	}
	
	/**
	 * 해당 메뉴의 메뉴얼을 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 6. 28
	 * @Method Name : findLastRevisionManualInfo
	 */
	@RequestMapping (value = "findLastRevisionManualInfo.do")
	public @ResponseBody Map findLastRevisionManualInfo(@RequestBody Map param) {
		Map result = Maps.newHashMap();
		
		// 메뉴얼 관리 데이터 조회
		result.put("manualInfo", manualManagerService.findLastRevisionManualInfo(param) );
		
		// 메뉴얼 차수 콤보 데이터 조회
		result.put("mnlRevCombo", manualManagerService.findListRevisionManualComboboxItem(param) );
		
		return result;
	}
	
	/**
	 * 메뉴얼 데이터를 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 6. 29
	 * @Method Name : saveManualInfo
	 */
	@RequestMapping (value = "saveManualInfo.do")
	public @ResponseBody ResultMap saveManualInfo(@RequestBody Map param) {
		return manualManagerService.saveManualInfo(param);
	}


	/**
	 * 이전 Revision manual 을 복사한다.
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "copyPreviousRevisionManualInfo.do")
	public @ResponseBody ResultMap copyPreviousRevisionManualInfo(@RequestBody Map param) {
		return manualManagerService.copyPreviousRevisionManualInfo(param);
	}
	
}
