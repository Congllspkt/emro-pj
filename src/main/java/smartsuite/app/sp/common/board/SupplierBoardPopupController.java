package smartsuite.app.sp.common.board;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.sp.common.board.service.SupplierBoardPopupService;
import smartsuite.mybatis.data.impl.PageResult;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/sp/board/**/")
public class SupplierBoardPopupController {
	
	@Inject
	SupplierBoardPopupService supplierBoardPopupService;
	
	/**
	 * 게시목록 조회 
	 * 로그인 이전에 팝업으로 게시판 관련 조회 하기 위한 controller입니다.
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 11. 30
	 * @Method Name : findBoardListPopup
	 */
	@RequestMapping(value = "findListBoardPopup.do")
	public @ResponseBody PageResult findListBoardPopup(@RequestBody Map param) {
		return new PageResult(supplierBoardPopupService.findListBoardPopup(param));
	}
	
	/**
	 * 게시글 조회 
	 * 로그인 이전에 팝업으로 게시판 관련 조회 하기 위한 controller입니다.
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 11. 30
	 * @Method Name : findInfoBoardPopup
	 */
	@RequestMapping(value = "findBoardPopupInfo.do")
	public @ResponseBody Map findBoardPopupInfo(@RequestBody Map param) {
		return supplierBoardPopupService.findBoardPopupInfo(param);
	}
	
	
	/**
	 * 게시글 댓글 조회 
	 * 로그인 이전에 팝업으로 게시판 관련 조회 하기 위한 controller입니다.
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 11. 30
	 * @Method Name : findListBoardComntPopup
	 */
	@RequestMapping(value = "findBoardCommentPopupList.do")
	public @ResponseBody List findBoardCommentPopupList(@RequestBody Map param) {
		return supplierBoardPopupService.findBoardCommentPopupList(param);
	}
}
