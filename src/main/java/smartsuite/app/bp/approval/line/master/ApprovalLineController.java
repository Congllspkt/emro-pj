package smartsuite.app.bp.approval.line.master;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.approval.line.master.service.ApprovalLineMasterService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.data.FloaterStream;

/**
 * ApprovalLine 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author Yeon-u Kim
 * @see 
 * @since 2017. 2. 1
 * @FileName ApprovalLineController.java
 * @package smartsuite.app.bp.approval.mng
 * @변경이력 : [2017. 2. 1] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes",
	"PMD.AtLeastOneConstructor"})
@Controller
@RequestMapping (value = "**/approval/mng/")
public class ApprovalLineController {
	
	/** The approval line service. */
	@Inject
	private transient ApprovalLineMasterService approvalLineMasterService;
	
    /**
     * aprv line list 조회를 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the list
     * @Date : 2017. 2. 1
     * @Method Name : findApprovalLineMasterList
     */
    @RequestMapping(value = "findApprovalLineMasterList.do")
    public @ResponseBody FloaterStream findAprvLineList(@RequestBody final Map<String,Object> param) {
    	// 대용량 처리
        return approvalLineMasterService.findApprovalLineMasterList(param);
    }
	
    /**
     * aprv line master detail list 조회를 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the list
     * @Date : 2017. 2. 2
     * @Method Name : findApprovalLineMasterDetailList
     */
    @RequestMapping(value = "findApprovalLineMasterDetailList.do")
    public @ResponseBody List findApprovalLineMasterDetailList(@RequestBody final Map<String,Object> param) {
        return approvalLineMasterService.findApprovalLineMasterDetailList(param);
    }
	
    /**
     * aprv line master 저장을 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the map
     * @Date : 2017. 2. 2
     * @Method Name : saveApprovalLineMaster
     */
    @RequestMapping(value = "saveApprovalLineMaster.do")
    public @ResponseBody Map saveApprovalLineMaster(@RequestBody final Map<String,Object> param) {
        return approvalLineMasterService.saveApprovalLineMaster(param);
    }
    
    /**
     * aprv line master detail 저장을 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the map
     * @Date : 2017. 2. 2
     * @Method Name : saveAprvLineMasterDetail
     */
    @RequestMapping(value = "saveAprvLineMasterDetail.do")
    public @ResponseBody Map saveAprvLineMasterDetail(@RequestBody final Map<String,Object> param) {
        return approvalLineMasterService.saveApprovalLineDetail(param);
    }
    
    /**
     * aprv line master 삭제를 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the map
     * @Date : 2017. 2. 1
     * @Method Name : deleteApprovalLineMaster
     */
    @RequestMapping(value = "deleteApprovalLineMaster.do")
    public @ResponseBody ResultMap deleteApprovalLineMaster(@RequestBody final Map<String,Object> param) {
        return approvalLineMasterService.deleteApprovalLineMaster(param);
    }
    
    /**
     * aprv line master detail 삭제를 요청한다.
     *
     * @author : Yeon-u Kim
     * @param param the param
     * @return the map
     * @Date : 2017. 2. 2
     * @Method Name : deleteListApprovalLineMasterDetail
     */
    @RequestMapping(value = "deleteListApprovalLineMasterDetail.do")
    public @ResponseBody Map deleteListApprovalLineMasterDetail(@RequestBody final Map<String,Object> param) {
        return approvalLineMasterService.deleteListApprovalLineMasterDetail(param);
    }
	
	@RequestMapping(value = "findListApprovalLineDetailForApproval.do")
	public @ResponseBody Map findListApprovalLineDetailForApproval(@RequestBody Map<String, Object> param) {
		return approvalLineMasterService.findListApprovalLineDetailForApproval(param);
	}
	
	@RequestMapping(value = "saveApprovalLineByApproval.do")
	public @ResponseBody ResultMap saveApprovalLineByApproval(@RequestBody Map<String, Object> param) {
		return approvalLineMasterService.saveApprovalLineByApproval(param);
	}
}
