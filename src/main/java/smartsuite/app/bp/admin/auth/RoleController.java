package smartsuite.app.bp.admin.auth;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.auth.service.RoleFuncService;
import smartsuite.app.bp.admin.auth.service.RoleService;

import smartsuite.app.common.shared.ResultMap;

/**
 * Role 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @since 2016. 2. 3
 * @FileName RoleController.java
 * @package smartsuite.app.bp.admin.auth
 * @변경이력 : [2016. 2. 3] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/bp/**/")
public class RoleController {

	/** The role service. */
	@Inject
	RoleService roleService;

	/** The role func service. */
	@Inject
	RoleFuncService roleFuncService;

	/**
	 * list role 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListRole
	 */
	@RequestMapping (value = "findListRole.do")
	public @ResponseBody List findListRole(@RequestBody Map param) {
		return roleService.findListRole(param);
	}

	/**
	 * role 삭제를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 11
	 * @Method Name : deleteRole
	 */
	@RequestMapping (value = "deleteListRole.do")
	public @ResponseBody ResultMap deleteListRole(@RequestBody Map saveParam) {
		return roleService.deleteListRoleRequest(saveParam);
	}

	/**
	 * list role 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListRole
	 */
	@RequestMapping (value = "saveListRole.do")
	public @ResponseBody ResultMap saveListRole(@RequestBody Map param) {
		roleService.validate(param);
		return ResultMap.SUCCESS();
	}

	/**
	 * list role menu 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param roleMenuDatas the role menu datas
	 * @param roleId the role id
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleMenu
	 */
	@RequestMapping (value = "saveListRoleMenu.do")
	public @ResponseBody ResultMap saveListRoleMenu(@RequestBody Map saveParam) {
		roleService.saveListRoleMenu(saveParam);
		return ResultMap.SUCCESS();
	}

	/**
	 * list role user 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListUserByRole
	 */
	@RequestMapping (value = "findListUserByRole.do")
	public @ResponseBody List findListUserByRole(@RequestBody Map param) {
		return roleService.findListUserByRole(param);
	}

	/**
	 * list role user 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleUser
	 */
	@RequestMapping (value = "saveListRoleUser.do")
	public @ResponseBody ResultMap saveListRoleUser(@RequestBody Map saveParam) {
		roleService.saveListRoleUser(saveParam);
		return ResultMap.SUCCESS();
	}

	/**
	 * list role dept 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListRoleDept
	 */
	@RequestMapping (value = "findListRoleDept.do")
	public @ResponseBody List findListRoleDept(@RequestBody Map param) {
		return roleService.findListRoleDept(param);
	}

	/**
	 * list role dept 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param params the params
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleDept
	 */
	@RequestMapping (value = "saveListRoleDept.do")
	public @ResponseBody ResultMap saveListRoleDept(@RequestBody Map saveParam) {
		roleService.saveListRoleDept(saveParam);
		return ResultMap.SUCCESS();
	}

	@RequestMapping (value = "findListRoleMenu.do")
	public @ResponseBody List findListRoleMenu(@RequestBody Map param) {
		return roleService.findListRoleMenu(param);
	}

	/**
	 * 롤 메뉴기능 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 10
	 * @Method Name : findListRoleMenuWithMenuFunc
	 */
	@RequestMapping (value = "findListRoleMenuWithMenuFunc.do")
	public @ResponseBody List findListRoleMenuWithMenuFunc(@RequestBody Map param) {
		return roleFuncService.findListRoleMenuWithMenuFunc(param);
	}

	/**
	 * 롤 메뉴기능 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 10
	 * @Method Name : saveListRoleMenuFunc
	 */
	@RequestMapping (value = "saveListRoleMenuFunc.do")
	public @ResponseBody ResultMap saveListRoleMenuFunc(@RequestBody Map param) {
		return roleFuncService.saveListRoleFunc(param);
	}

	/**
	 * 메뉴에 부여된 role list 조회를 요청한다.
	 *
	 * @param param the param
	 * @return the list
	 * @Method Name : findListMenuRole
	 */
	@RequestMapping (value = "findListMenuRole.do")
	public @ResponseBody List findListMenuRole(@RequestBody Map param) {
		return roleService.findListMenuRole(param);
	}


	@RequestMapping("findListMenuFuncRole.do")
	public @ResponseBody List findListMenuFuncRole(@RequestBody Map param){
		return roleFuncService.findListMenuFuncRole(param);
	}
}
