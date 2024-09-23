package smartsuite.app.bp.admin.organizationManager.operationUnit;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.organizationManager.operationUnit.service.OperationUnitManagerService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 운영단위 관련 처리를 하는 컨트롤러 Class입니다.

 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping (value = "**/org/operunit/")
public class OperationUnitManagerController {

	/** The oper unit service. */
	@Inject
	OperationUnitManagerService operationUnitManagerService;

	/**
	 * 운영단위 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the oper unit list
	 * @Date : 2016. 2. 2
	 * @Method Name : findOperUnitList
	 */
	@RequestMapping (value = "findListOperationUnit.do")
	public @ResponseBody List findListOperationUnit(@RequestBody Map param) {
		return operationUnitManagerService.findListOperationUnit(param);
	}

	/**
	 * 운영단위 목록 조회를 요청한다. (코드성 조회)
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the oper unit list
	 * @Date : 2016. 2. 2
	 * @Method Name : getAllListOperUnit
	 */
	@RequestMapping (value = "getAllListOperUnit.do")
	public @ResponseBody List getAllListOperUnit(@RequestBody Map param) {
		return operationUnitManagerService.findListOperationUnitForCode(param);
	}

	/**
	 * 운영단위 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListOperationUnit
	 */
	@RequestMapping (value = "saveListOperationUnit.do")
	public @ResponseBody ResultMap saveListOperationUnit(@RequestBody Map param) {
		return operationUnitManagerService.saveListOperationUnit(param);
	}

	/**
	 * 운영단위 목록 삭제를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListOperationUnit
	 */
	@RequestMapping (value = "deleteListOperationUnit.do")
	public @ResponseBody ResultMap deleteListOperationUnitRequest(@RequestBody Map param) {
		return operationUnitManagerService.deleteListOperationUnitRequest(param);
	}

}