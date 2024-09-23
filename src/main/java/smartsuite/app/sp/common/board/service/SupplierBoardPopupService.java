package smartsuite.app.sp.common.board.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.common.board.repository.BoardRepository;
import smartsuite.app.common.board.service.BoardService;
import smartsuite.app.sp.common.board.repository.SupplierBoardPopupRepository;
import smartsuite.security.core.crypto.CipherUtil;

@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class SupplierBoardPopupService {

	@Inject
	BoardService boardService;
	
	@Inject
	BoardRepository boardRepository;
	
	@Inject
	SupplierBoardPopupRepository supplierBoardPopupRepository;
	
	@Inject
	CipherUtil cipherUtil;
	
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
		
		// pst_no 복호화
		String getPostNumber = param.getOrDefault("pst_no", "") == null? "" : (String) param.getOrDefault("pst_no", "");
		param.put("pst_no", cipherUtil.decrypt(getPostNumber));
		
		// 조회 수 update
		boardService.updateBoardOfViewCount(param);
		
		// 게시글 조회
		Map<String,Object> boardInfo = boardService.findBoardInfoByPostNo(param);
		
		// 조회 결과 암호화
		String boardInfoUuid = boardInfo.getOrDefault("bbd_uuid", "") == null? "" : (String) boardInfo.getOrDefault("bbd_uuid", "");
		String postNumber = boardInfo.getOrDefault("pst_no", "") == null? "" : (String) boardInfo.getOrDefault("pst_no", "");
		boardInfo.put("bbd_uuid", cipherUtil.encrypt(boardInfoUuid));
		boardInfo.put("pst_no", cipherUtil.encrypt(postNumber));
		
		return boardInfo;
	}
	
	/**
	 * 게시글 댓글 조회 
	 * parameter 암/복호화 로직이 추가 되었습니다.
	 *
	 * @param param the param
	 * @return the list
	 * @Date : 2017. 11. 30
	 * @Method Name : findListBoardComntPopup
	 */
	public List<Map<String, Object>> findBoardCommentPopupList(Map<String, Object> param) {
		// pst_no 복호화
		String getPostNumber = param.getOrDefault("pst_no", "") == null? "" : (String) param.getOrDefault("pst_no", "");
		param.put("pst_no", cipherUtil.decrypt(getPostNumber));

		
		// 게시글 댓글 조회
		List<Map<String,Object>> boardComntList = this.findBoardCommentList(param);
		
		// 조회 결과 암호화
		for(Map<String, Object> boardComntInfo : boardComntList) {
			String boardInfoUuid = boardComntInfo.getOrDefault("bbd_uuid", "") == null? "" : (String) boardComntInfo.getOrDefault("bbd_uuid", "");
			String postNumber = boardComntInfo.getOrDefault("pst_no", "") == null? "" : (String) boardComntInfo.getOrDefault("pst_no", "");
			boardComntInfo.put("bbd_uuid", cipherUtil.encrypt(boardInfoUuid));
			boardComntInfo.put("pst_no", cipherUtil.encrypt(postNumber));
		}
		return boardComntList;
	}
	
	public List<Map<String, Object>> findBoardCommentList(Map<String, Object> param) {
		return supplierBoardPopupRepository.findBoardCommentList(param);
	}
}
