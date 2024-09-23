package smartsuite.app.bp.admin.job;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.job.service.JobService;

import smartsuite.app.common.shared.ResultMap;

/**
 * 직무관리 하는 컨트롤러 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @since 2016. 2. 2
 * @FileName OperUnitController.java
 * @package smartsuite.app.bp.admin.org
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/bp/**/")
public class JobController {

	/** 직무관리 service. */
	@Inject
	JobService jobService;

	/**
	 * 직무 목록 조회를 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the oper unit list
	 * @Date : 2016. 2. 2
	 * @Method Name : findOperUnitList
	 */
	@RequestMapping (value = "findListPurchaseGroupCategory.do")
	public @ResponseBody List findListPurchaseGroupCategory(@RequestBody Map param) {
		return jobService.findListPurchaseGroupCategory(param);
	}

	/**
	 * 직무 목록 저장을 요청한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListJob
	 */
	@RequestMapping (value = "saveListPurchaseGroupCategory.do")
	public @ResponseBody ResultMap saveListJob(@RequestBody Map param) {
		return jobService.saveListPurchaseGroupCategory(param);
	}	
	
	/**
	 * 직무 목록 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 18
	 * @Method Name : deleteListJob
	 */
	@RequestMapping (value = "deleteListJobAndPurchaseGroupCategory.do")
	public @ResponseBody ResultMap deleteListJob(@RequestBody Map param) {
		return jobService.deleteListJobAndPurchaseGroupCategory(param);
	}
	
	
	/**
	 * 직무담당자 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 19
	 * @Method Name : findListPurchaseGroupCategoryJobUser
	 */
	@RequestMapping(value = "findListPurchaseGroupCategoryJobUser.do")
	public @ResponseBody List findListPurchaseGroupCategoryJobUser(@RequestBody Map param){
		return jobService.findListPurchaseGroupCategoryJobUser(param);
	}
	
	/**
	 * 직무담당자 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 19
	 * @Method Name : savePurchaseGroupCategoryJobUser
	 */
	@RequestMapping (value = "savePurchaseGroupCategoryJobUser.do")
	public @ResponseBody ResultMap savePurchaseGroupCategoryJobUser(@RequestBody Map param) {
		return jobService.savePurchaseGroupCategoryJobUser(param);
	}
	
	
	/**
	 * 직무담당자 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 19
	 * @Method Name : deleteListPurchaseGroupCategoryJobUser
	 */
	@RequestMapping (value = "deleteListPurchaseGroupCategoryJobUser.do")
	public @ResponseBody ResultMap deleteListPurchaseGroupCategoryJobUser(@RequestBody Map param) {
		return jobService.deleteListPurchaseGroupCategoryJobUser(param);
	}
	
	/**
	 * 사용중인 직무 목록 조회를 요청한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the job list
	 * @Date : 2016. 2. 2
	 * @Method Name : findListUsedPurchaseGroupCategory
	 */
	@RequestMapping (value = {"**/jobitem/findListUsedPurchaseGroupCategory.do", "**/jobuser/findListUsedPurchaseGroupCategory.do"})
	public @ResponseBody List findListUsedPurchaseGroupCategory(@RequestBody Map param) {
		return jobService.findListUsedPurchaseGroupCategory(param);
	}
	
	/**
	 * 직무별 품목목록 조회를 요청한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 19
	 * @Method Name : findListPurchaseGroupCategoryItem
	 */
	@RequestMapping(value = "**/jobitem/findListPurchaseGroupCategoryItem.do")
	public @ResponseBody List findListPurchaseGroupCategoryItem(@RequestBody Map param){
		return jobService.findListPurchaseGroupCategoryItem(param);
	}
	
	/**
	 * 직무와 연결된 품목목록 삭제를 요청한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 19
	 * @Method Name : deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode
	 */
	@RequestMapping (value = "**/jobitem/deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode.do")
	public @ResponseBody ResultMap deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(@RequestBody Map param) {
		return jobService.deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(param);
	}
	
	/**
	 * 직무와 연결할 품목목록 저장을 요청한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 19
	 * @Method Name : savePurchaseGroupCategoryAndItemMapping
	 */
	@RequestMapping (value = "**/jobitem/savePurchaseGroupCategoryAndItemMapping.do")
	public @ResponseBody Map savePurchaseGroupCategoryAndItemMapping(@RequestBody Map param) {
		return jobService.savePurchaseGroupCategoryAndItemMapping(param);
	}
}