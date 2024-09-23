package smartsuite.app.common.board;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import smartsuite.app.common.board.service.BoardService;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.mybatis.data.impl.PageResult;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class BoardController {

	@Inject
	BoardService boardService;

	/**
	 * 게시판 정보 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 18
	 * @Method Name : findListBoard
	 */
	@RequestMapping(value = "findBoardInfo.do")
	public @ResponseBody Map findBoardInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.findBoardInfo(menuCode, param);
	}

	/**
	 * 게시판 게시글 목록 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 18
	 * @Method Name : findListBoard
	 */
	//findBoardPostList
	@RequestMapping(value = "findListBoardForGridPaging.do")
	public @ResponseBody Map findListBoardForGridPaging(@RequestHeader(value="menucode") String menuCode, @RequestBody Map param) {
		return boardService.findListBoardForGridPaging(menuCode, param);
	}

	/**
	 * 게시판 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : deleteListBoard
	 */
	@RequestMapping(value = "deleteListBoard.do")
	public @ResponseBody Map deleteListBoard(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.deleteListBoard(menuCode, param);
	}


	/**
	 * 게시물 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : saveInfoBoard
	 */
	@RequestMapping(value = "saveBoardInfo.do")
	public @ResponseBody Map saveBoardInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.saveBoardInfo(menuCode, param);
	}


	/**
	 * 게시물 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 21
	 * @Method Name : deleteInfoBoard
	 */
	@RequestMapping(value = "deleteBoardInfo.do")
	public @ResponseBody Map deleteBoardInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.deleteBoardInfo(menuCode, param);
	}

	/**
	 * 게시물 단건 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : findInfoBoardAdmin
	 */
	@RequestMapping(value = "findBoardPostInfo.do")
	public @ResponseBody Map findBoardPostInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.findBoardPostInfo(menuCode, param);
	}

	/**
	 * 게시물 댓글 조회를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 3. 25
	 * @Method Name : findListBoardComnt
	 */
	@RequestMapping(value = "findBoardCommentList.do")
	public @ResponseBody List findBoardCommentList(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.findBoardCommentList(menuCode, param);
	}

	/**
	 * 게시물의 댓글 저장을 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 25
	 * @Method Name : saveInfoBoardComnt
	 */
	@RequestMapping(value = "saveBoardCommentInfo.do")
	public @ResponseBody Map saveBoardCommentInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.saveBoardCommentInfo(menuCode, param);
	}


	/**
	 * 게시물의 댓글 삭제를 요청한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 25
	 * @Method Name : deleteInfoBoardComnt
	 */
	@RequestMapping(value = "deleteBoardCommentInfo.do")
	public @ResponseBody Map deleteBoardCommentInfo(@RequestHeader(value="menucode") String menuCode,@RequestBody Map param) {
		return boardService.deleteBoardCommentInfo(menuCode, param);
	}
}
