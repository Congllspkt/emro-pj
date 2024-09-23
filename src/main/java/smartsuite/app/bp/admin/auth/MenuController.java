package smartsuite.app.bp.admin.auth;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.auth.service.MenuFuncService;
import smartsuite.app.bp.admin.auth.service.MenuService;

import smartsuite.app.common.shared.ResultMap;

/**
 * Menu 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @since 2016. 2. 4
 * @FileName MenuController.java
 * @package smartsuite.app.bp.admin.auth
 * @변경이력 : [2016. 2. 4] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/auth/menu/")
public class MenuController {

	/** The menu service. */
	@Inject
	MenuService menuService;

	/** The menu-func service. */
	@Inject
	MenuFuncService menuFuncService;

	/**
	 * list menu 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListMenu
	 */

	@RequestMapping (value = "findListMenu.do")
	public @ResponseBody List findListMenu(@RequestBody Map param) {
		return menuService.findListMenu(param);
	}

	/**
	 * list menu 삭제를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 11
	 * @Method Name : deleteListMenu
	 */
	@RequestMapping (value = "deleteListMenu.do")
	public @ResponseBody ResultMap deleteListMenu(@RequestBody Map saveParam) {
		return menuService.deleteListMenuRequest(saveParam);
	}

	/**
	 * list menu 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @return the binding result
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListMenu
	 */
	@RequestMapping (value = "saveListMenu.do")
	public @ResponseBody ResultMap saveListMenu(@RequestBody Map saveParam) {
		return menuService.saveListMenu(saveParam);
	}
	@RequestMapping (value = "moveMenuInfo.do")
	public @ResponseBody void moveMenuInfo(@RequestBody Map saveParam) {
		menuService.updateMenuInfo(saveParam);
	}

	/**
	 * 메뉴기능 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the menu func list
	 * @Date : 2016. 2. 2
	 * @Method Name : findMenuFuncList
	 */
	@RequestMapping (value = "findListMenuFunc.do")
	public @ResponseBody List findListMenuFunc(@RequestBody Map param) {
		return menuFuncService.findListMenuFunc(param);
	}

	/**
	 * list menu ptrn 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2018. 3. 27
	 * @Method Name : findListMenuPattern
	 */
	@RequestMapping(value="findListMenuPattern.do")
	public @ResponseBody List findListMenuPattern(@RequestBody Map param){
		return menuFuncService.findListMenuPattern(param);
	}

	/**
	 * list menu func ptrn 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 3. 27
	 * @Method Name : saveListMenuFuncPattern
	 */
	@RequestMapping(value="saveListMenuFuncPattern.do")
	public @ResponseBody ResultMap saveListMenuFuncPattern(@RequestBody Map param){
		return menuFuncService.saveListMenuFuncPattern(param);
	}

	/**
	 * 메뉴기능 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListMenuFunc
	 */
	@RequestMapping (value = "saveListMenuFunc.do")
	public @ResponseBody ResultMap saveListMenuFunc(@RequestBody Map param) {
		return menuFuncService.saveListMenuFunc(param);
	}

	/**
	 * 메뉴기능 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListMenuFunc
	 */
	@RequestMapping (value = "deleteListMenuFunc.do")
	public @ResponseBody ResultMap deleteListMenuFunc(@RequestBody Map param) {
		return menuFuncService.deleteListMenuFuncRequest(param);
	}
	/**
	 * list menu func ptrn 저장을 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2020. 7. 21
	 * @Method Name : saveListMenuUrl
	 */
	@RequestMapping(value="saveListMenuUrl.do")
	public @ResponseBody ResultMap saveListMenuUrl(@RequestBody Map param){
		return menuFuncService.saveListMenuUrl(param);
	}

	/**
	 * 메뉴URL 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListMenuUrl
	 */
	@RequestMapping("deleteListMenuUrl.do")
	public @ResponseBody ResultMap deleteListMenuUrl(@RequestBody Map param){
		return menuFuncService.deleteListMenuUrlRequest(param);
	}
}