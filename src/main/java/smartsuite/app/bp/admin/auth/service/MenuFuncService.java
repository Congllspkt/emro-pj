package smartsuite.app.bp.admin.auth.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import smartsuite.app.bp.admin.auth.repository.MenuFuncRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * 메뉴기능 관련 처리하는 서비스 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @FileName MenuFuncService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class MenuFuncService {

	@Inject
	RoleFuncService roleFuncService;

	@Inject
	MenuFuncRepository menuFuncRepository;

	@Inject
	AuthUrlService authUrlService;

	@Inject
	RoleService roleService;

	/**
	 * 메뉴기능을 등록한다.
	 * - 메뉴기능코드는 Unique : getCountMenuFunc 로 검사하고 호출할 것.
	 * @param param
	 */
	public void insertMenuFunc(Map<String, Object> param) {
		menuFuncRepository.insertMenuFunc(param);
	}

	/**
	 * 메뉴기능을 삭제한다.
	 * - 물리적 삭제
	 * - 롤 기능에서 사용중인 메뉴기능은 삭제 불가 : roleFuncService.getCountRoleFunc 로 검사하고 호출할 것.
	 * @param param
	 */
	public void deleteMenuFunc(Map<String, Object> param) {
		menuFuncRepository.deleteMenuFunc(param);
	}

	/**
	 * 메뉴기능을 수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : updateMenuFunc
	 */
	public void updateMenuFunc(Map<String, Object> param) {
		menuFuncRepository.updateMenuFunc(param);
	}

	/**
	 * 메뉴기능 목록을 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMenuFunc(Map<String, Object> param) {
		return menuFuncRepository.findListMenuFunc(param);
	}

	/**
	 * 메뉴의 패턴 리스트를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMenuPattern(Map<String, Object> param) {
		return menuFuncRepository.findListMenuPattern(param);
	}

	/**
	 * list menu func ptrn 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2018. 3. 27
	 * @Method Name : saveListMenuFuncPattern
	 */
	public ResultMap saveListMenuFuncPattern(Map<String, Object> param) {
		List<Map<String,Object>> saveListFunc = (List<Map<String, Object>>) param.getOrDefault("saveList", Lists.newArrayList());

		for (Map<String, Object> menuFunc : saveListFunc) {
			if (authUrlService.existFunctionUrl(menuFunc)) { // 롤기능 중복 체크
				authUrlService.updateMenuFuncPattern(menuFunc);
			}else{
				authUrlService.insertFuncUrl(menuFunc);
			}
		}
		return ResultMap.SUCCESS();
	}




	/**
	 * 메뉴 Function 이 존재하는지의 여우 확인
	 * @param param
	 * @return
	 */
	public boolean existMenuFunc(Map<String, Object> param) {
		int getCountMenuFunc = menuFuncRepository.getCountMenuFunc(param);
		return (getCountMenuFunc > 0);
	}

	/**
	 * 메뉴기능 목록을 등록/수정한다.
	 *
	 * @author : JongKyu Kim
	 * @param param {"insertMenuFuncList", "updateMenuFuncList"}
	 * @return the map<string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListMenuFunc
	 */
	public ResultMap saveListMenuFunc(Map<String, Object> param) {
		List<Map<String, Object>> insertMenuFuncList = (List<Map<String, Object>>)param.getOrDefault("insertMenuFuncList",Lists.newArrayList());
		this.insertMenuFuncList(insertMenuFuncList);
		
		List<Map<String, Object>> updateMenuFuncList = (List<Map<String, Object>>)param.getOrDefault("updateMenuFuncList",Lists.newArrayList());
		this.updateListMenuFunc(updateMenuFuncList);
		return ResultMap.SUCCESS();
	}


	/**
	 * menu function을 수정한다.
	 * @param updateMenuFuncList
	 */
	public void updateListMenuFunc(List<Map<String, Object>> updateMenuFuncList) {
		for (Map<String, Object> functionInfo : updateMenuFuncList) {
			if (this.existMenuFunc(functionInfo)) { // 메뉴기능 중복 체크
				this.updateMenuFunc(functionInfo);
			}
		}
	}

	/**
	 * Menu function 추가
	 * @param insertMenuFuncList
	 */
	public void insertMenuFuncList(List<Map<String, Object>> insertMenuFuncList) {
		for (Map<String, Object> functionInfo : insertMenuFuncList) {
			if (this.existMenuFunc(functionInfo)) { // 메뉴기능 중복 체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}
			this.insertMenuFunc(functionInfo);
		}
	}

	/**
	 * 메뉴기능 목록을 삭제 요청
	 */
	public ResultMap deleteListMenuFuncRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteMenuFuncList = (List<Map<String, Object>>)param.getOrDefault("deleteMenuFuncList",Lists.newArrayList());

		// 메뉴기능 목록 삭제
		this.deleteListMenuFunc(deleteMenuFuncList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 메뉴기능 목록을 삭제한다.
	 * @param deleteMenuFuncList
	 */
	public void deleteListMenuFunc(List<Map<String, Object>> deleteMenuFuncList) {
		for (Map<String, Object> row : deleteMenuFuncList) {
			if (roleFuncService.existRoleFunc(row)) { // 롤기능에서 사용중인 메뉴기능인지 검사
				throw new CommonException(ErrorCode.USED);
			}else {
				this.deleteMenuFunc(row);
			}
		}
	}

	/**
	 * list menu func ptrn 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2020. 7. 21
	 * @Method Name : saveListMenuUrl
	 */
	public ResultMap saveListMenuUrl(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> menuInfo = (Map<String,Object>) param.get("menuInfo");
		List<Map<String,Object>> insertMenuList = (List<Map<String, Object>>) param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String,Object>> updateMenuList = (List<Map<String, Object>>) param.getOrDefault("updateList",Lists.newArrayList());

		//메뉴 추가 및 중복 Count
		int dupleCnt = this.insertListMenu(insertMenuList);

		if(dupleCnt > 0 ){
			resultMap.setResultStatus(ResultMap.STATUS.DUPLICATED);
			Map<String,Object> resultData = Maps.newHashMap();
			resultData.put("dupleCnt", dupleCnt);
			resultMap.setResultData(resultData);

			// 중복데이터가 1개 이상이고, insert size = duple count 가 동일하면 표기
			if(insertMenuList.size() == dupleCnt){
				resultMap.setResultStatus(ResultMap.STATUS.USED);
			}
		}



		//메뉴 수정
		this.updateListMenu(updateMenuList);

		return resultMap;
	}

	/**
	 * 메뉴 리스트 수정
	 * @param updateMenuList
	 */
	public void updateListMenu(List<Map<String, Object>> updateMenuList) {
		for (Map<String, Object> updateMenuInfo : updateMenuList) {
			if(!authUrlService.existFunctionUrl(updateMenuInfo)){
				authUrlService.insertFuncUrl(updateMenuInfo);
			}else {
				this.updateMenu(updateMenuInfo);
			}
		}
	}

	/**
	 * 메뉴 수정
	 * @param updateMenuInfo
	 */
	public void updateMenu(Map<String, Object> updateMenuInfo) {
		if (authUrlService.existFunctionUrl(updateMenuInfo)) { // 롤기능 중복 체크
			authUrlService.insertFuncUrl(updateMenuInfo);
		}else{
			authUrlService.updateMenuFuncPattern(updateMenuInfo);
		}
	}

	/**
	 * 메뉴 리스트 추가
	 * @param insertMenuList
	 * @return
	 */
	public int insertListMenu(List<Map<String, Object>> insertMenuList) {
		int dupleCnt = 0;
		for (Map<String, Object> insertMenuInfo : insertMenuList) {
			if(!this.existMenuCallPatten(insertMenuInfo)){
				this.insertMenu(insertMenuInfo);
			}else{
				dupleCnt++;
			}

			String funcCode = insertMenuInfo.getOrDefault("func_cd","") == null?  "" : (String) insertMenuInfo.getOrDefault("func_cd","");
			if (StringUtils.isNotEmpty(funcCode) && authUrlService.existFunctionUrl(insertMenuInfo)) { // 롤기능 중복 체크
				authUrlService.insertFuncUrl(insertMenuInfo);
			}
		}
		return dupleCnt;
	}

	/**
	 * 메뉴 추가
	 * @param insertMenuInfo
	 */
	public void insertMenu(Map<String, Object> insertMenuInfo) {
		roleService.insertCallPattern(insertMenuInfo);
		String funcCd = insertMenuInfo.getOrDefault("act_cd","") == null?  "" : (String) insertMenuInfo.getOrDefault("act_cd","");
		if (StringUtils.isNotEmpty(funcCd) && !authUrlService.existFunctionUrl(insertMenuInfo)) { // 롤기능 중복 체크
			authUrlService.insertFuncUrl(insertMenuInfo);
		}
	}


	/**
	 * menu call patten이 존재여부 확인
	 * @param row
	 * @return
	 */
	public boolean existMenuCallPatten(Map<String, Object> row) {
		List<Map<String,Object>> findListCallPattern = roleService.findListCallPattern(row);
		return findListCallPattern.size() > 0;
	}




	/**
	 * 메뉴URL 목록 삭제를 요청한다.
	 *
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteListMenuUrl
	 */
	public ResultMap deleteListMenuUrlRequest(Map param) {
		List<Map<String, Object>> deleteMenuUrlList = (List<Map<String,Object>>) param.getOrDefault("deleteList",Lists.newArrayList());

		// 메뉴 URL 목록 삭제
		this.deleteListMenuUrl(deleteMenuUrlList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 메뉴 URL 목록 삭제
	 * @param deleteMenuUrlList
	 */
	public void deleteListMenuUrl(List<Map<String, Object>> deleteMenuUrlList) {
		for (Map<String,Object> deleteMenuUrlInfo : deleteMenuUrlList) {
			this.deleteMenuUrl(deleteMenuUrlInfo);
		}
	}

	/**
	 * 메뉴 URL 삭제
	 * @param deleteMenuUrlInfo
	 */
	public void deleteMenuUrl(Map<String, Object> deleteMenuUrlInfo) {
		authUrlService.deleteFuncUrl(deleteMenuUrlInfo);
		if(this.existMenuCallPatten(deleteMenuUrlInfo)){
			roleService.deleteCallPattern(deleteMenuUrlInfo);
		}
	}


	/**
	 * MENU_ACT 삭제 (메뉴기능 전체 삭제)
	 * @param deleteMenu
	 */
	public void deleteMenuFuncAll(Map deleteMenu) {
		menuFuncRepository.deleteMenuFuncAll((deleteMenu));
	}
}
