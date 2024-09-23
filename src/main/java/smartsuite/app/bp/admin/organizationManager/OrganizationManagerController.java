package smartsuite.app.bp.admin.organizationManager;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.organizationManager.service.OrganizationManagerService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 조직유형/조직/부서 관련 처리를 하는 컨트롤러 Class입니다.

 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/org/")
public class OrganizationManagerController {

	/** The org service. */
	@Inject
	OrganizationManagerService organizationManagerService;



	

	/**
	 * 부서 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the dept list
	 * @Date : 2016. 2. 2
	 * @Method Name : findListDepartmentByOrganization
	 */
	@RequestMapping (value = "dept/findListDepartmentByOrganization.do")
	public @ResponseBody List findListDepartmentByOrganization(@RequestBody Map param) {
		return organizationManagerService.findListDepartmentByOrganization(param);
	}

	/**
	 * 부서 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListDepartmentByOrganization
	 */
	@RequestMapping (value = "dept/saveListDepartmentByOrganization.do")
	public @ResponseBody Map saveListDepartmentByOrganization(@RequestBody Map param) {
		return organizationManagerService.saveListDepartmentByOrganization(param);
	}

	/**
	 * 부서 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListDepartmentByOrganization
	 */
	@RequestMapping (value = "dept/deleteListDepartmentByOrganization.do")
	public @ResponseBody ResultMap deleteListDepartmentByOrganization(@RequestBody Map param) {
		return organizationManagerService.deleteListDepartmentByOrganization(param);
	}

	@RequestMapping(value = "logicorg/findListLogicOrganizationInfo.do")
	public @ResponseBody List findListLogicOrganizationInfo(@RequestBody Map param) {
		return organizationManagerService.findListLogicOrganizationInfo(param);
	}

	/**
	 * 논리조직을 삭제한다.
	 */
	@RequestMapping (value = "logicorg/deleteListLogicOrganizationInfo.do")
	public @ResponseBody ResultMap deleteListLogicOrganizationInfo(@RequestBody Map param) {
		return organizationManagerService.deleteListLogicOrganizationInfo(param);
	}

	/**
	 * 논리조직을 저장한다.
	 */
	@RequestMapping (value = "logicorg/saveLogicOrganizationInfo.do")
	public @ResponseBody Map saveLogicOrganizationInfo(@RequestBody Map param) {
		return organizationManagerService.saveLogicOrganizationInfo(param);
	}

	/**
	 * 논리조직을 정보를 조회한다.
	 */
	@RequestMapping (value = "logicorg/findLogicOrganizationInfo.do")
	public @ResponseBody Map findLogicOrganizationInfo(@RequestBody Map param) {
		return organizationManagerService.findLogicOrganizationInfo(param);
	}

}
