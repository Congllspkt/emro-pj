package smartsuite.app.bp.admin.board.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.board.repository.BoardAdminRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * BoardAdmin 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @FileName BoardAdminService.java
 * @package smartsuite.app.bp.admin.board
 * @Since 2016. 3. 16
 * @변경이력 : [2016. 3. 16] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class BoardAdminService {
	
	@Inject
	BoardAdminRepository boardAdminRepository;

	@Inject
	SharedService sharedService;
	
	/**
	 * 게시판 관리 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 3. 16
	 * @Method Name : findListBoardAdmin
	 */
	public List<Map<String,Object>> findBoardAdminList(Map<String, Object> param) {
		return boardAdminRepository.findBoardAdminList(param);
	}
	
	
	/**
	 * 게시판 관리 단건을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : findInfoBoardAdmin
	 */
	public Map findBoardAdminInfo(Map<String, Object> param) {
		return boardAdminRepository.findBoardAdminInfo(param);
	}
	
	
	/**
	 * 게시판 어드민 여부를 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 24
	 * @Method Name : findInfoBoardAdminYn
	 */
	public Map findBoardAdminYnInfo(Map<String, Object> param){
		return boardAdminRepository.findBoardAdminYnInfo(param);
	}
	
	/**
	 * 게시판 관리 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveInfoBoardAdmin
	 */
	public ResultMap saveBoardAdminInfo(Map<String, Object> param){
		Map<String, Object> info = (Map<String, Object>)param.get("boardAdminInfo");
		boolean isNew = info.get("is_new") == null ? false : (boolean)info.getOrDefault("is_new", false);
		
		if (isNew) {
			info.put("bbd_uuid", this.findBoardId(param));
			this.insertBoardAdmin(info);
		}else{
			this.updateBoardAdmin(info);
			this.updateBoardCompany(info);
		}
		
		return ResultMap.SUCCESS();
	}
	
	public String findBoardId(Map<String, Object> param) {
		return boardAdminRepository.findBoardId(param);
	}
	
	public void insertBoardAdmin(Map<String, Object> info) {
		boardAdminRepository.insertBoardAdmin(info);
	}
	
	public void updateBoardAdmin(Map<String, Object> info) {
		boardAdminRepository.updateBoardAdmin(info);
	}
	
	/**
	 * 게시판 관리를 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : deleteInfoBoardAdmin
	 */
	public ResultMap deleteBoardAdminInfo(Map<String, Object> param){
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> info = (Map<String, Object>)param.get("boardAdminInfo");
		
		// 게시판의 등록된 게시물 조회(BBD)
		List boardList = this.findBoardByBoardIdList(info);
		
		if(boardList.size() > 0){
			// 게시판 롤 삭제(BBD_ROLE)
			deleteBoardAuthById(info);
			
			// 게시판 Admin 삭제(BBD_MGR)
			deleteAdminUserById(info);
			
			// 게시판 유형 삭제(BBD_SETUP)
			deleteBoardAdmin(info);

			resultMap.setResultStatus(ResultMap.SUCCESS());
		}else{
			resultMap.setResultStatus(ResultMap.FAIL());
			throw new CommonException(ErrorCode.USED);
		}
		
		return resultMap;
	}
	
	public List findBoardByBoardIdList(Map<String, Object> info) {
		return boardAdminRepository.findBoardByBoardIdList(info);
	}
	
	public void deleteBoardAuthById(Map<String, Object> info) {
		boardAdminRepository.deleteBoardAuthById(info);
	}
	
	public void deleteAdminUserById(Map<String, Object> info) {
		boardAdminRepository.deleteAdminUserById(info);
	}
	
	public void deleteBoardAdmin(Map<String, Object> info) {
		boardAdminRepository.deleteBoardAdmin(info);
	}
	
	/**
	 * Admin 계정을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 3. 17
	 * @Method Name : findListAdminUser
	 */
	public List<Map<String,Object>> findAdminUserList(Map<String, Object> param) {
		return boardAdminRepository.findAdminUserList(param);
	}
	
	/**
	 * Admin 계정을 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveListAdminUser
	 */
	public ResultMap saveAdminUserList(Map<String, Object> param){
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.get("insertList");
		
		// INSERT
		for(Map row : insertList){
			insertAdminUser(row);
		}
		
		return ResultMap.SUCCESS();
	}
	
	public void insertAdminUser(Map<String, Object> row) {
		 boardAdminRepository.insertAdminUser(row);
	}
	
	
	/**
	 * Admin 계정을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : deleteListAdminUser
	 */
	public ResultMap deleteAdminUserList(Map<String, Object> param){
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.get("deleteList");
		
		// DELETE
		for(Map row : deleteList){
			this.deleteAdminUser(row);
		}
		return ResultMap.SUCCESS();
	}
	
	public void deleteAdminUser(Map<String, Object> row) {
		boardAdminRepository.deleteAdminUser(row);
	}
	
	/**
	 * 게시판 역할 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 3. 17
	 * @Method Name : findListBoardAuth
	 */
	public List<Map<String,Object>> findBoardAuthList(Map<String, Object> param) {
		return boardAdminRepository.findBoardAuthList(param);
	}
	
	
	/**
	 * 게시판 역할을 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 3. 17
	 * @Method Name : saveListBoardAuth
	 */
	public ResultMap saveBoardAuthList(Map<String, Object> param){
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.get("insertList");
		
		// INSERT
		for(Map row : insertList){
			deleteBoardAuth(row);
			insertBoardAuth(row);
		}
		return ResultMap.SUCCESS();
	}
	
	public void deleteBoardAuth(Map<String, Object> row) {
		boardAdminRepository.deleteBoardAuth(row);
	}
	
	public void insertBoardAuth(Map<String, Object> row) {
		boardAdminRepository.insertBoardAuth(row);
	}

	public List<Map<String,Object>> findCompanyListForBoard(Map<String, Object> param) {
		return boardAdminRepository.findCompanyListForBoard(param);
	}

	public ResultMap saveBoardCompanyList(Map param) {
		List<Map<String, Object>> companyList = (List<Map<String, Object>>)param.get("updateList");
		for(Map company : companyList){
			if(isExistBoardCompany(company)){
				deleteBoardCompany(company);
			} else{
				insertBoardCompany(company);
			}
		}
		return ResultMap.SUCCESS();
	}

	public void insertBoardCompany(Map company) {
		boardAdminRepository.insertBoardCompany(company);
	}

	public void deleteBoardCompany(Map company){
		boardAdminRepository.deleteBoardCompany(company);
	}

	public List<Map<String,Object>> findBoardCompanyList(Map company){
		return boardAdminRepository.findBoardCompanyList(company);
	}

	public boolean isExistBoardCompany(Map company){
		List<Map<String,Object>> boardCompanyList = findBoardCompanyList(company);
		if(boardCompanyList == null || boardCompanyList.isEmpty()){
			return false;
		}
		return true;
	}

	public void updateBoardCompany(Map info){
		if("Y".equals((String)info.get("co_bbd_use_yn"))){
			info.put("sts", "U");
		} else{
			info.put("sts", "D");
		}
		boardAdminRepository.updateBoardCompany(info);
	}
}
