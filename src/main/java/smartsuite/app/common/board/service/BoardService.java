package smartsuite.app.common.board.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.template.TemplateException;
import smartsuite.app.common.template.service.TemplateGeneratorService;
import smartsuite.app.bp.admin.board.service.BoardAdminService;
import smartsuite.app.bp.admin.board.repository.BoardAdminRepository;
import smartsuite.app.common.board.repository.BoardRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;
import smartsuite.mybatis.data.impl.PageResult;
import smartsuite.security.authentication.Auth;
import smartsuite.security.core.crypto.CipherUtil;

@Service
@Transactional
@SuppressWarnings ({"rawtypes", "unchecked" })
public class BoardService {

	@Inject
	CipherUtil cipherUtil;
	
	@Inject
	BoardAdminService boardAdminService;
	
	@Inject
	BoardRepository boardRepository;
	
	@Inject
	SharedService sharedService;

	@Inject
	TemplateGeneratorService templateGeneratorService;

	public Map findBoardInfo(String menuCode, Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();

		param.put("menu_cd", menuCode);

		hasReadRole(param);
		hasBoardRole(param);

		// 게시판 유형 조회
		Map boardAdminInfo = boardAdminService.findBoardAdminInfo(param);

		// 게시판 어드민
		Map boardAdminYn = boardAdminService.findBoardAdminYnInfo(param);

		// 게시판 롤 ( ACT Code를 기준으로 조회/작성에 대한 역할을 부여한다 )
		Map boardInfoActRole = findBoardRoleInfo(param);

		resultMap.put("boardAdminInfo", boardAdminInfo);
		resultMap.put("boardAdminYn"	, boardAdminYn);
		resultMap.put("boardInfoActRole", boardInfoActRole);

		return resultMap;
	}
	
	public Map<String, Object> findListBoardForGridPaging(String menuCode, Map param) {
		// 역할 체크 파라미터
		Map<String, Object> checkParam = Maps.newHashMap();
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> result = Maps.newHashMap();
		checkParam.put("bbd_uuid", param.getOrDefault("bbd_uuid", ""));
		checkParam.put("menu_cd", menuCode);

		hasReadRole(checkParam);
		hasBoardRole(checkParam);

		// 페이징 조회
		PageResult resultList = this.findListBoardInfo(param);
		resultMap.put(Const.RESULT_DATA, resultList);
		result.put("content", resultMap);	// sc-grid-paging 조회 시 on-response에서 content를 받음
		result.put("pageable", resultList.get("pageable"));
		return result;
	}
	
	/**
	 * 게시판을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 3. 18
	 * @Method Name : findListBoard
	 */
	public PageResult findListBoardInfo(Map<String, Object> param) {
		List<Map<String,Object>> boardList = boardRepository.findListBoardInfo(param);

		return new PageResult(boardList);
	}

	/**
	 * 게시판을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : deleteListBoard
	 */
	public Map deleteListBoard(String menuCode, Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		
		param.put("menu_cd", menuCode);
		List<Map<String,Object>> deleteFailListRole = hasDeleteListRole(param);

		if(deleteFailListRole.size() > 0){
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
		
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.get("deleteList");

		// DELETE
		for(Map row : deleteList){
			this.updateDeleteBoard(row);
		}

		return resultMap;
	}

	/**
	 * 게시물 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : saveInfoBoard
	 */
	public Map<String, Object> saveBoardInfo(String menuCode, Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();

		// 세션 정보
		Map<String, Object> userInfo = Auth.getCurrentUserInfo();
		// 게시물
		Map<String, Object> info = (Map<String, Object>)param.get("boardInfo");
		info.put("menu_cd", menuCode);
		hasSaveRole(info);
		
		boolean isNew = info.get("is_new") == null ? false : (boolean)info.getOrDefault("is_new", false);
		
		if (isNew) {
			this.hasBoardInsertRole(info);
			String postNo = findPostNo(info);

			info.put("crtr_eml", userInfo.getOrDefault("email", ""));	// 이메일
			info.put("pst_no", postNo);

			this.updateBoardForPostGrpSn(info);

			this.insertBoard(info);

		}else{
			//게시판 작성자 또는 게시판 관리자만 가능
			this.hasBoardUpdateRole(info);
			info.put("mod_nm", userInfo.getOrDefault("usr_nm", ""));	// 수정자명
			info.put("modr_eml", userInfo.getOrDefault("email", ""));	// 이메일
			this.updateBoard(info);
		}

		return resultMap;
	}
	
	public String findPostNo(Map<String, Object> info) {
		return boardRepository.findPostNo(info);
	}
	
	public void updateBoardForPostGrpSn(Map<String, Object> info) {
		boardRepository.updateBoardForPostGrpSn(info);
	}
	
	public void insertBoard(Map<String, Object> info) {
		boardRepository.insertBoard(info);
	}
	
	public void updateBoard(Map<String, Object> info) {
		boardRepository.updateBoard(info);
	}
		
	/**
	 * 게시물을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 21
	 * @Method Name : deleteInfoBoard
	 */
	public Map deleteBoardInfo(String menuCode, Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> info = (Map<String, Object>)param.get("boardInfo");
		info.put("menu_cd", menuCode);
		this.hasSaveRole(info);
		//게시판 작성자 또는 게시판 관리자만 삭제 가능
		this.hasBoardDeleteRole(info);
		this.updateDeleteBoard(info);
		return resultMap;
	}


	/**
	 * 게시물 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param info the info
	 * @Date : 2016. 3. 21
	 * @Method Name : updateDeleteBoard
	 */
	public void updateDeleteBoard(Map<String, Object> info){
		boardRepository.updateDeleteBoard(info);
	}

	/**
	 * 게시판 롤을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 24
	 * @Method Name : findInfoBoardRole
	 */
	public Map<String, Object> findBoardRoleInfo(Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		String wrtYn = "N";
		String viewYn = "N";

		int viewCnt = this.findBoardReadRoleInfo(param);
		int wrtCnt = this.findBoardSaveRoleInfo(param);

		if(viewCnt > 0){
			viewYn = "Y";
		}
		if(wrtCnt > 0){
			wrtYn = "Y";
		}
		resultMap.put("wrtg_perm_yn", wrtYn);
		resultMap.put("show_yn", viewYn);
		return resultMap;
		//return sqlSession.selectOne("boardAdmin.findInfoBoardRole", param);
	}
	
	public int findBoardReadRoleInfo(Map<String, Object> param) {
		return boardRepository.findBoardReadRoleInfo(param);
	}
	
	public int findBoardSaveRoleInfo(Map<String, Object> param) {
		return boardRepository.findBoardSaveRoleInfo(param);
	}

	/**
	 * 게시판 조회 역할
	 *
	 * @author : WanSeob Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 12. 04
	 * @Method Name : hasBoardRole
	 */
	public Boolean hasBoardRole(Map boardInfo){
		// 1. 해당 사용자가 해당 게시판의 관리자 역할을 가졌을 경우
		// 2. 해당 게시판이 역할 체크를 안하는 게시판일 경우
		// 3. 해당 사용자가 해당 게시판에 조회 역할을 가졌을 경우
		int cnt = boardRepository.hasBoardRole(boardInfo);
		return cnt > 0;
	}

	/**
	 * 게시물 작성 역할
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 5. 03
	 * @Method Name : hasBoardInsertRole
	 */
	public void hasBoardInsertRole(Map boardInfo){
		// 1. 해당 사용자가 해당 게시판의 관리자 역할을 가졌을 경우
		// 2. 해당 게시판이 역할 체크를 안하는 게시판일 경우
		// 3. 해당 사용자가 해당 게시판에 쓰기 역할을 가졌을 경우
		int cnt = boardRepository.hasBoardInsertRole(boardInfo);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 게시물 수정 역할
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 3. 08
	 * @Method Name : hasBoardUpdateRole
	 */
	public void hasBoardUpdateRole(Map boardInfo){
		int cnt = boardRepository.hasBoardUpdateRole(boardInfo);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 게시물 코멘트 수정 역할
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 3. 08
	 * @Method Name : hasBoardUpdateRole
	 */
	public void hasCommentUpdateRole(Map boardInfo){
		int cnt = boardRepository.hasCommentUpdateRole(boardInfo);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 게시물 삭제 역할
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 3. 08
	 * @Method Name : hasBoardDeleteRole
	 */
	public void hasBoardDeleteRole(Map boardInfo){
		int cnt = boardRepository.hasBoardDeleteRole(boardInfo);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 게시물 코멘트 삭제 역할
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @return the map
	 * @Date : 2018. 3. 08
	 * @Method Name : hasComntDeleteRole
	 */
	public void hasCommentDeleteRole(Map boardInfo){
		int cnt = boardRepository.hasCommentDeleteRole(boardInfo);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 게시물을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 18
	 * @Method Name : findInfoBoard
	 */
	public Map findBoardPostInfo(String menuCode, Map<String, Object> param) {
		param.put("menu_cd", menuCode);

		hasReadRole(param);
		updateBoardOfViewCount(param);

		return findBoardInfoByPostNo(param);
	}
	
	public Map findBoardInfoByPostNo(Map<String, Object> param) {
		return boardRepository.findBoardInfoByPostNo(param);
	}
	
	public void updateBoardOfViewCount(Map<String, Object> param) {
		boardRepository.updateBoardOfViewCount(param);
	}

	/**
	 * 게시물 댓글을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 25
	 * @Method Name : findListBoardComnt
	 */
	public List findBoardCommentList(String menuCode, Map<String, Object> param) {
		param.put("menu_cd", menuCode);

		hasReadRole(param);
		return boardRepository.findBoardCommentList(param);
	}
	
	/**
	 * 게시물 댓글을 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 25
	 * @Method Name : saveInfoBoardComnt
	 */
	public Map saveBoardCommentInfo(String menuCode, Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> info = (Map<String, Object>)param.get("boardComntInfo");
		info.put("menu_cd", menuCode);

		hasSaveRole(info);
		// 댓글
		boolean isNew = info.get("is_new") == null ? false : (boolean)info.getOrDefault("is_new", false);

		if (isNew) {
			insertBoardComment(info);
			// 댓글수 update
			updateBoardOfCommentCount(info);
		} else {
			//작성자 또는 게시판 관리자만 가능
			this.hasCommentUpdateRole(info);
			updateBoardComment(info);
		}
		return resultMap;
	}
	
	public void insertBoardComment(Map<String, Object> info) {
		boardRepository.insertBoardComment(info);
	}
	
	public void updateBoardOfCommentCount(Map<String, Object>  info) {
		boardRepository.updateBoardOfCommentCount(info);
	}
	
	public void updateBoardComment(Map<String, Object> info) {
		boardRepository.updateBoardComment(info);
	}

	/**
	 * 게시물 댓글을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 25
	 * @Method Name : deleteInfoBoardComnt
	 */
	public Map deleteBoardCommentInfo(String menuCode, Map<String, Object> param){
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> info = (Map<String, Object>)param.get("boardComntInfo");
		info.put("menu_cd", menuCode);

		hasReadRole(info);
		this.hasCommentDeleteRole(info);
		deleteBoardComment(info);
		// 댓글수 update
		updateBoardOfCommentCount(info);
		return resultMap;
	}
	
	public void deleteBoardComment(Map<String, Object> info) {
		boardRepository.deleteBoardComment(info);
	}

	/**
	 * 공지 팝업 목록 조회
	 *
	 * @author :
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2017. 8. 24
	 * @Method Name : findListMainNotice
	 */
	public List<Map<String, Object>> findListNoticeInfo(Map<String, Object> param) {
		return boardRepository.findListNoticeInfo(param);
	}

	/**
	 * 협력사포탈 공지사항 목록 조회
	 *
	 * @author :
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2017. 11. 27
	 * @Method Name : findListPortalNoticeInfo
	 */
	public List<Map<String, Object>> findListPortalNoticeInfo(Map<String, Object> param) {
		return boardRepository.findListPortalNoticeInfo(param);
	}


	/**
	 * 게시목록 조회
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 11. 30
	 * @Method Name : findBoardListPopup
	 */
	public List<Map<String,Object>> findListBoardPopup(Map<String, Object> param) {
		// bbd_uuid 복호화
		String boardUuid = param.getOrDefault("bbd_uuid", "") == null? "" : (String) param.getOrDefault("bbd_uuid", "");
		String decBoarId = cipherUtil.decrypt(boardUuid);
		param.put("bbd_uuid", decBoarId);

		// 게시목록 조회
		List<Map<String,Object>> boardList = boardRepository.findListBoardInfo(param);

		// 조회 결과 암호화
		for(Map<String, Object> boardInfo : boardList) {
			String boardInfoUuid = boardInfo.getOrDefault("bbd_uuid", "") == null? "" : (String) boardInfo.getOrDefault("bbd_uuid", "");
			String postNumber = boardInfo.getOrDefault("pst_no", "") == null? "" : (String) boardInfo.getOrDefault("pst_no", "");
			boardInfo.put("bbd_uuid", cipherUtil.encrypt(boardInfoUuid));
			boardInfo.put("pst_no", cipherUtil.encrypt(postNumber));
		}

		return boardList;
	}

	/**
	 * 게시글 조회
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 11. 30
	 * @Method Name : findInfoBoardPopup
	 */
	public Map<String, Object> findBoardPopupInfo(Map<String, Object> param) {
		param.put("pst_no", param.get("pst_no"));

		// 조회 수 update
		updateBoardOfViewCount(param);

		// 게시글 조회
		Map<String,Object> boardInfo = findBoardInfoByPostNo(param);

		// 조회 결과 암호화
		String boardInfoUuid = boardInfo.getOrDefault("bbd_uuid", "") == null? "" : (String) boardInfo.getOrDefault("bbd_uuid", "");
		String postNumber = boardInfo.getOrDefault("pst_no", "") == null? "" : (String) boardInfo.getOrDefault("pst_no", "");
		boardInfo.put("bbd_uuid", cipherUtil.encrypt(boardInfoUuid));
		boardInfo.put("pst_no", cipherUtil.encrypt(postNumber));

		return boardInfo;
	}

	public void hasReadRole(Map<String, Object> param){
		int cnt = boardRepository.hasReadRole(param);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	public List<Map<String,Object>> hasDeleteListRole(Map<String, Object> param){
		List<Map<String,Object>> boardList = param.get("deleteList") == null? Lists.newArrayList() : (ArrayList<Map<String,Object>>)param.get("deleteList");

		int deleteSucCnt = 0;
		int deleteFailCnt = 0;

		List<Map<String,Object>> failBoardList = Lists.newArrayList();
		for(Map<String,Object> boardInfo : boardList){
			int cnt = 0;
			cnt = boardRepository.hasReadRole(boardInfo);

			if(cnt > 0){
				deleteSucCnt = deleteSucCnt + cnt;
			}else{
				deleteFailCnt = deleteFailCnt + 1;
				failBoardList.add(boardInfo);
			}
		}

		return failBoardList;
	}

	public void hasSaveRole(Map<String, Object> param){
		int cnt = boardRepository.hasSaveRole(param);
		if(cnt == 0) {
			throw new CommonException(ErrorCode.UNAUTHORIZED);
		}
	}

	/**
	 * 협력사포탈 공지사항 목록 페이징 조회
	 *
	 * @author :
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2020. 05. 28
	 * @Method Name : findListPortalNoticeForGridPaging
	 */
	public PageResult findListPortalNoticeForGridPaging(Map<String, Object> param) {
		List<Map<String, Object>> boardList = boardRepository.findListPortalNoticeForGridPaging(param);
		
		//페이지에 따른 리스트 조회
		for(Map<String, Object> board : boardList){
			String boardUuid = board.getOrDefault("bbd_uuid", "") == null? "" : (String) board.getOrDefault("bbd_uuid", "");
			board.put("bbd_uuid", cipherUtil.encrypt(boardUuid));
		}
		return new PageResult(boardList);
	}
	
	/**
	 * 게시판 totalRow 가져오기
	 * @param param
	 * @return
	 */
	public int getTotalBoardCount(Map<String, Object> param){
		return boardRepository.getTotalBoardCount(param);
	}
	
	/**
	 * 게시판 Template 데이터 binding
	 * @param param(pst_no)
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String getTemplateContent(Map<String, Object> param) throws TemplateException, IOException {
		Map<String, Object> boardInfo = findBoardPopupInfo(param);
		if (boardInfo.containsKey("athg_uuid") && null != boardInfo.getOrDefault("athg_uuid", "")) {
			Map<String, Object> fileSearchParam = Maps.newHashMap();
			fileSearchParam.put("grp_cd", boardInfo.getOrDefault("athg_uuid", ""));
			boardInfo.put("attData", sharedService.findAttList(fileSearchParam));
		}

		return templateGeneratorService.tmpGenerate("NOTICE",boardInfo);
	}
}
