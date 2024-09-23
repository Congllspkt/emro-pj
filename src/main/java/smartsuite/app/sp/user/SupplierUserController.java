package smartsuite.app.sp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.auth.service.UserService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.sp.user.service.SupplierUserService;
import smartsuite.app.common.shared.Const;
import smartsuite.security.annotation.AuthCheck;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 협력사 사용자 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 11. 2
 * @FileName VendorUserController.java
 * @package smartsuite.app.bp.admin.auth
 * @변경이력 : [2016. 11. 2] JuEung Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/bp/**/")
public class SupplierUserController {

	/** The user service. */
	@Inject
	SupplierUserService supplierUserService;
	
	/** The user service. */
	@Inject
	private UserService userService;

	/**
	 * 협력사 사용자 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 11. 2
	 * @Method Name : findLisVendorUser
	 */
	@AuthCheck (authCode = Const.READ)
	@RequestMapping (value = "findListVendorUser.do")
	public @ResponseBody List findLisVendorUser(@RequestBody Map param) {
		return supplierUserService.findListVendorUser(param);
	}
	
	/**
	 * 협력사 사용자 정보 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 11. 2
	 * @Method Name : findVendorUserInfo
	 */
	@AuthCheck (authCode = Const.READ)
	@RequestMapping (value = "findVendorUserInfo.do")
	public @ResponseBody Map findVendorUserInfo(@RequestBody Map param) {
		return supplierUserService.findVendorUserInfo(param);
	}
	
	/**
	 * 계정 잠김해제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 11. 2
	 * @Method Name : saveInfoAccLockYn
	 */
	@AuthCheck (authCode = Const.SAVE)
	@RequestMapping (value = "saveInfoAccLockYn.do")
	public @ResponseBody ResultMap saveInfoAccLockYn(@RequestBody Map param) {
		return userService.saveUserAccLockYn(param);
	}
	
	/**
	 * 비밀번호 초기화 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 11. 3
	 * @Method Name : saveInfoPwReset
	 */
	@AuthCheck (authCode = Const.SAVE)
	@RequestMapping (value = "saveInfoPwReset.do")
	public @ResponseBody Map saveInfoPwReset(@RequestBody Map param) {
		return userService.initPassword(param);
	}
}
