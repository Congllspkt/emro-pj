package smartsuite.app.bp.admin.todoManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.todoManager.service.ToDoManagerService;

import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * 할일 관리 Controller
 */
@Controller
@RequestMapping(value="**/todo/**/")
public class ToDoManagerController {

	@Inject
	ToDoManagerService todoManagerService;
	
	/**
	 * 할일 그룹 목록 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListTodoGroup.do")
	public @ResponseBody List<Map<String, Object>> findListTodoGroup(@RequestBody Map<String, Object> param) {
		return todoManagerService.findListTodoGroup(param);
	}
	
	/**
	 * 할일 그룹 목록 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListTodoGroup.do")
	public @ResponseBody Map<String, Object> saveListTodoGroup(@RequestBody Map<String, Object> param) {
		return todoManagerService.saveListTodoGroup(param);
	}
	
	/**
	 * 할일 그룹 목록 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListTodoGroup.do")
	public @ResponseBody ResultMap deleteListTodoGroup(@RequestBody Map<String, Object> param) {
		return todoManagerService.deleteListTodoGroupRequest(param);
	}
	
	/**
	 * 할일 항목 목록 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListTodoFactor.do")
	public @ResponseBody List<Map<String, Object>> findListTodoFactor(@RequestBody Map<String, Object> param) {
		return todoManagerService.findListTodoFactor(param);
	}
	
	/**
	 * 할일 항목 목록 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListTodoFactor.do")
	public @ResponseBody ResultMap saveListTodoFactor(@RequestBody Map<String, Object> param) {
		return todoManagerService.saveListTodoFactor(param);
	}
	
	/**
	 * 할일 항목 목록 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListTodoFactor.do")
	public @ResponseBody ResultMap deleteListTodoFactor(@RequestBody Map<String, Object> param) {
		return todoManagerService.deleteListTodoFactorRequest(param);
	}
	
	/**
	 * 할일 항목 별 사용자 목록 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListTodoUser.do")
	public @ResponseBody List<Map<String, Object>> findListTodoUser(@RequestBody Map<String, Object> param) {
		return todoManagerService.findListTodoUser(param);
	}
	
	/**
	 * 할일 항목 별 사용자 목록 저장
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "insertListTodoUser.do")
	public @ResponseBody ResultMap insertListTodoUser(@RequestBody Map<String, Object> param) {
		return todoManagerService.insertListTodoUser(param);
	}
	
	/**
	 * 할일 항목 별 사용자 목록 삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "deleteListTodoUser.do")
	public @ResponseBody ResultMap deleteListTodoUser(@RequestBody Map<String, Object> param) {
		return todoManagerService.deleteListTodoUserRequest(param);
	}
	
	/**
	 * 할일 사용자 별 항목 목록 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "findListTodoFactorByUser.do")
	public @ResponseBody List<Map<String, Object>> findListTodoFactorByUser(@RequestBody Map<String, Object> param) {
		return todoManagerService.findListTodoFactorByCurrentUser(param);
	}
	
	/**
	 * 할일 사용자 별 항목 저장/삭제
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "saveListTodoFactorByUser.do")
	public @ResponseBody ResultMap saveListTodoFactorByUser(@RequestBody Map<String, Object> param) {
		return todoManagerService.saveListTodoFactorByUser(param);
	}
}
