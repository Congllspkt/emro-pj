package smartsuite.app.bp.admin.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import smartsuite.app.bp.admin.auth.service.UserService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.security.account.service.AccountService;


import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * User 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @since 2016. 2. 3
 * @FileName UserController.java
 * @package smartsuite.app.bp.admin.auth
 * @변경이력 : [2016. 2. 3] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/bp/**/")
public class UserController {

	/** The user service. */
	@Inject
	UserService userService;

	@Inject
	AccountService accountService;

	/**
	 * user list 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 3
	 * @Method Name : findListUserInfo
	 */
	@RequestMapping (value = "findListUserInfo.do")
	public @ResponseBody List findListUserInfo(@RequestBody Map param) {
		return userService.findListUser(param);
	}

	/**
	 * user by user id 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 11
	 * @Method Name : findUserByUserId
	 */
	@RequestMapping (value = "findUserByUserId.do")
	public @ResponseBody Map findUserByUserId(@RequestBody Map param) {
		return userService.findUserByUserId(param);
	}

	/**
	 * user list 삭제를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @Date : 2016. 2. 3
	 * @Method Name : deleteUserList
	 */
	@RequestMapping (value = "deleteListUser.do")
	public @ResponseBody ResultMap deleteListUser(@RequestBody Map param) {
		return userService.deleteListUserRequest(param);
	}

	/**
	 * user 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the int
	 * @Date : 2016. 2. 3
	 * @Method Name : saveUserInfo
	 */
	@RequestMapping (value = "saveUserInfo.do")
	public @ResponseBody ResultMap saveUserInfo(@RequestBody Map param) {
		return userService.saveUser(param);
	}

	/**
	 * 사용자별 롤 적용 현황을 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 11
	 * @Method Name : findListRoleByUser
	 */
	@RequestMapping (value = "findListRoleByUser.do")
	public @ResponseBody List findListRoleByUser(@RequestBody Map param) {
		return userService.findListRoleByUser(param);
	}

	/**
	 * list user role 저장을 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @Date : 2016. 2. 11
	 * @Method Name : saveRoleByUser
	 */
	@RequestMapping (value = "saveRoleByUser.do")
	public @ResponseBody void saveRoleByUser(@RequestBody Map param) {
		userService.saveRoleByUser(param);
	}

	/**
	 * 사용자별 운영조직 현황을 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListUserOperationOrganization
	 */
	@RequestMapping (value = "findListUserOperationOrganization.do")
	public @ResponseBody List findListUserOperationOrganization(@RequestBody Map param) {
		return userService.findListUserOperationOrganization(param);
	}

	/**
	 * 내부사용자 비밀번호를 초기화 한다.
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @Date : 2016. 8. 16
	 * @Method Name : initPaswordByUserInfo
	 */
	@RequestMapping (value = "initPassword.do", method = RequestMethod.POST)
	public ModelAndView initPaswordByUserInfo(@RequestParam Map param) {
		ModelAndView model = new ModelAndView();
		model.setViewName(userService.passwordResultPageProcess(param));
		return model;
	}

	/**
	 * 내부사용자 비밀번호를 초기화 한다.
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @Date : 2016. 8. 16
	 * @Method Name : initPaswordByUserInfo
	 */
	@RequestMapping (value = "findPassword.do", method = RequestMethod.GET)
	public ModelAndView findPassword(@RequestParam Map param) {
		ModelAndView model = new ModelAndView();
		model.setViewName("portal/bp/login/findPassword");
		return model;
	}

	@RequestMapping (value = "saveUserAccLockYn.do")
	public @ResponseBody ResultMap saveUserAccLockYn(@RequestBody Map param) {
		return userService.saveUserAccLockYn(param);
	}

	@RequestMapping (value = "saveUserPwReset.do")
	public @ResponseBody Map saveUserPwReset(@RequestBody Map param) {
		return userService.initPassword(param);
	}

}
