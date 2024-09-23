package smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 운영조직/운영조직의 사용자/운영조직의 연결정보 관련 처리를 하는 컨트롤러 Class입니다.

 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/org/operorg/")
public class OperationOrganizationController {

	/** The oper org service. */
	@Inject
	OperationOrganizationService operationOrganizationService;

	/**
	 * 운영조직 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListOperationOrganization
	 */
	@RequestMapping (value = "findListOperationOrganization.do")
	public @ResponseBody List findListOperationOrganization(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganization(param);
	}
	
	/**
	 * 운영조직 연결을 위한 운영조직(source or target) 목록 조회를 요청한다.
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "findListOperationOrganizationForLinking.do")
	public @ResponseBody List findListOperationOrganizationForLinking(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganization(param);
	}

	/**
	 * 운영조직의 사용자 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListOperationOrganizationUser
	 */
	@RequestMapping (value = "findListOperationOrganizationUser.do")
	public @ResponseBody List findListOperationOrganizationUser(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganizationUser(param);
	}

	/**
	 * 운영조직의 연결정보 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListOperationOrganizationLink
	 */
	@RequestMapping (value = "findListOperationOrganizationLink.do")
	public @ResponseBody List findListOperationOrganizationLink(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganizationLink(param);
	}

	/**
	 * 운영조직 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListOperationOrganization
	 */
	@RequestMapping (value = "saveListOperationOrganization.do")
	public @ResponseBody ResultMap saveListOperationOrganization(@RequestBody Map param) {
		return operationOrganizationService.saveListOperationOrganization(param);
	}

	/**
	 * 운영조직의 사용자 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListOperationOrganizationUser
	 */
	@RequestMapping (value = "saveListOperationOrganizationUser.do")
	public @ResponseBody ResultMap saveListOperationOrganizationUser(@RequestBody Map param) {
		return operationOrganizationService.saveListOperationOrganizationUser(param);
	}

	/**
	 * 운영조직의 연결정보 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListOperationOrganizationLink
	 */
	@RequestMapping (value = "saveListOperationOrganizationLink.do")
	public @ResponseBody ResultMap saveListOperationOrganizationLink(@RequestBody Map param) {
		return operationOrganizationService.saveListOperationOrganizationLink(param);
	}

	/**
	 * 운영조직 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : deleteListOperationOrganization
	 */
	@RequestMapping (value = "deleteListOperationOrganization.do")
	public @ResponseBody ResultMap deleteListOperationOrganization(@RequestBody Map param) {
		return operationOrganizationService.deleteListOperationOrganizationRequest(param);
	}

	/**
	 * 운영조직의 사용자 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : deleteListOperationOrganizationUser
	 */
	@RequestMapping (value = "deleteListOperationOrganizationUser.do")
	public @ResponseBody ResultMap deleteListOperationOrganizationUser(@RequestBody Map param) {
		return operationOrganizationService.deleteListOperationOrganizationUserRequest(param);
	}


	/**
	 * 운영조직에 연결된 부서 목록을 조회한다.
	 */
	@RequestMapping (value = "findListOperationOrganizationDept.do")
	public @ResponseBody List findListOperationOrganizationDept(@RequestBody Map param) {
		return operationOrganizationService.findListOperationOrganizationDept(param);
	}

	/**
	 * 운영조직에 연결된 부서 목록을 저장한다.
	 * @param param
	 * @return
	 */
	@RequestMapping (value = "saveListOperationOrganizationDept.do")
	public @ResponseBody ResultMap saveListOperationOrganizationDept(@RequestBody Map param) {
		return operationOrganizationService.saveListOperationOrganizationDept(param);
	}


	@RequestMapping (value = "deleteListOperationOrganizationDept.do")
	public @ResponseBody ResultMap deleteListOperationOrganizationDept(@RequestBody Map param) {
		return operationOrganizationService.deleteListOperationOrganizationDeptRequest(param);
	}

}
