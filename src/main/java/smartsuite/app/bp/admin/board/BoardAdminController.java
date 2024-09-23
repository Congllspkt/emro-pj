package smartsuite.app.bp.admin.board;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.bp.admin.board.service.BoardAdminService;

import smartsuite.app.common.shared.ResultMap;

/**
 * BoardAdmin 관련 처리를 하는 컨트롤러 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @since 2016. 3. 16
 * @FileName onClickCompanyBulletinBoardUseYntroller.java
 * @package smartsuite.app.bp.admin.board
 * @변경이력 : [2016. 3. 16] JuEung Kim 최초작성
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class BoardAdminController {
	
	/** The board admin service. */
	@Inject
	BoardAdminService boardAdminService;
	
	/**
	 * 게시판 관리 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 16
	 * @Method Name : findListBoardAdmin
	 */
	@RequestMapping(value = "findBoardAdminList.do")
	public @ResponseBody List findBoardAdminList(@RequestBody Map param) {
		return boardAdminService.findBoardAdminList(param);
	}
	
	
	/**
	 * 게시판 관리 단건 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 17
	 * @Method Name : findInfoBoardAdmin
	 */
	@RequestMapping(value = "findBoardAdminInfo.do")
	public @ResponseBody Map findBoardAdminInfo(@RequestBody Map param) {
		return boardAdminService.findBoardAdminInfo(param);
	}
	
	
	/**
	 * Admin 계정 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveInfoBoardAdmin
	 */
	@RequestMapping(value = "saveBoardAdminInfo.do")
	public @ResponseBody ResultMap saveBoardAdminInfo(@RequestBody Map param) {
		return boardAdminService.saveBoardAdminInfo(param);
	}
	
	
	/**
	 * Admin 계정 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : deleteInfoBoardAdmin
	 */
	@RequestMapping(value = "deleteBoardAdminInfo.do")
	public @ResponseBody ResultMap deleteBoardAdminInfo(@RequestBody Map param) {
		return boardAdminService.deleteBoardAdminInfo(param);
	}
	
	/**
	 * Admin 계정 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 17
	 * @Method Name : findListAdminUser
	 */
	@RequestMapping(value = "findAdminUserList.do")
	public @ResponseBody List findAdminUserList(@RequestBody Map param) {
		return boardAdminService.findAdminUserList(param);
	}
	
	
	/**
	 * Admin 계정 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveListAdminUser
	 */
	@RequestMapping(value = "saveAdminUserList.do")
	public @ResponseBody ResultMap saveAdminUserList(@RequestBody Map param) {
		return boardAdminService.saveAdminUserList(param);
	}
	
	
	/**
	 * Admin 계정 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : deleteListAdminUser
	 */
	@RequestMapping(value = "deleteAdminUserList.do")
	public @ResponseBody ResultMap deleteAdminUserList(@RequestBody Map param) {
		return boardAdminService.deleteAdminUserList(param);
	}
	
	
	/**
	 * 게시판 역할 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 17
	 * @Method Name : findListBoardAuth
	 */
	@RequestMapping(value = "findBoardAuthList.do")
	public @ResponseBody List findBoardAuthList(@RequestBody Map param) {
		return boardAdminService.findBoardAuthList(param);
	}
	
	
	/**
	 * 게시판 역할 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveListBoardAuth
	 */
	@RequestMapping(value = "saveBoardAuthList.do")
	public @ResponseBody ResultMap saveBoardAuthList(@RequestBody Map param) {
		return boardAdminService.saveBoardAuthList(param);
	}

	/**
	 * 게시판 사용하는 회사(법인) 목록 조회
	 *
	 * @author : Sangmoon Kang
	 * @param param the param
	 * @return the list
	 * @Date : 2023. 6. 23
	 * @Method Name : findBoardCompanyList
	 * */
	@RequestMapping(value = "findCompanyListForBoard.do")
	public @ResponseBody List findCompanyListForBoard(@RequestBody Map param) {
		return boardAdminService.findCompanyListForBoard(param);
	}

	/**
	 * 게시판 사용 법인 저장을 요청한다.
	 *
	 * @author : Sangmoon Kang
	 * @param param the param
	 * @return the map
	 * @Date : 2023. 6. 26
	 * @Method Name : saveBoardCompanyList
	 */
	@RequestMapping(value = "saveBoardCompanyList.do")
	public @ResponseBody ResultMap saveBoardCompanyList(@RequestBody Map param) {
		return boardAdminService.saveBoardCompanyList(param);
	}

}