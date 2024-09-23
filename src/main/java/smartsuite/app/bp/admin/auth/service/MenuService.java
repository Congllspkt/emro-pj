package smartsuite.app.bp.admin.auth.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import smartsuite.app.bp.admin.auth.repository.*;
import smartsuite.app.bp.admin.manualManager.service.ManualManagerService;
import smartsuite.app.common.board.BoardController;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.mybatis.SqlSessionTransactionUtil;

/**
 * Menu 관련 처리하는 서비스 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @FileName MenuService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 4
 * @변경이력 : [2016. 2. 4] Yeon-u Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MenuService {

	private static final String[] DEFAULT_FUNCTION_CODE_LIST = {"R","S"};

	@Inject
	MenuRepository menuRepository;

	@Inject
	MenuFuncService menuFuncService;

	@Inject
	AuthUrlService authUrlService;

	@Inject
	RoleService roleService;

	@Inject
	RoleFuncService roleFuncService;

	/** The sql session. */
	@Inject
	SqlSessionTransactionUtil sqlSessionTransactionUtil;

	@Inject
	ManualManagerService manualManagerService;

	/**
	 * list menu 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListMenu
	 */
	public List findListMenu(Map param) {
		return menuRepository.findListMenu(param);
	}


	/**
	 * 메뉴 리스트 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListMenuRequest(Map<String,Object> param) {
		List<Map<String,Object>> deleteMenus = (List<Map<String,Object>>)param.getOrDefault("deleteMenus",Lists.newArrayList());

		//메뉴 리스트 삭제
		this.deleteListMenu(deleteMenus);

		return ResultMap.SUCCESS();
	}

	/**
	 * 메뉴 리스트 삭제
	 * @param deleteMenus
	 */
	public void deleteListMenu(List<Map<String, Object>> deleteMenus) {
		for (Map deleteMenu : deleteMenus) {
			this.deleteMenu(deleteMenu);
		}
	}


	public void deleteMenu(Map deleteMenu) {

		if(manualManagerService.existManualInfo(deleteMenu)) manualManagerService.deleteManualInfo(deleteMenu);

		// ESAAURS 삭제 (호출패턴 역할 전체 삭제)
		authUrlService.deleteFuncUrlAll(deleteMenu);

		// ESAAUMS 삭제 (수집된 호출패턴 전체 삭제)
		roleService.deleteCallPatternAll(deleteMenu);

		// MENU_ACT 삭제 (메뉴기능 전체 삭제)
		menuFuncService.deleteMenuFuncAll(deleteMenu);

		// ESAAURF 삭제 (매핑된 롤메뉴 전체 삭제)
		roleFuncService.deleteRoleFuncAll(deleteMenu);

		// ESAAURM 삭제 (매핑된 롤메뉴 삭제)
		this.deleteRoleMenuByMenu(deleteMenu);

		// MENU_MULTLANG 삭제 (메뉴명 삭제)
		this.deleteMenuName(deleteMenu);

		// ESAAUMM 삭제 (메뉴 삭제)
		this.deleteMenuInfo(deleteMenu);

		// MNL 삭제 (메뉴얼 삭제)
		this.deleteManual(deleteMenu);

		// Sql session session flush
		sqlSessionTransactionUtil.sqlSessionFlush();
	}

	/**
	 * ESAAURM 삭제 (매핑된 롤메뉴 삭제)
	 * @param deleteMenu
	 */
	public void deleteRoleMenuByMenu(Map deleteMenu) {
		menuRepository.deleteRoleMenuByMenu(deleteMenu);
	}

	/**
	 * ESAAUMM 삭제 (메뉴 삭제)
	 * @param deleteMenu
	 */
	public void deleteMenuInfo(Map deleteMenu) {
		menuRepository.deleteMenu(deleteMenu);
	}

	/**
	 * MNL 삭제 (메뉴 삭제)
	 * @param deleteMenu
	 */
	public void deleteManual(Map deleteMenu) {
		menuRepository.deleteManual(deleteMenu);
	}

	/**
	 * menu by code 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param code the code
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : findMenuByCode
	 */
	public Map findMenuByCode(String code) {
		return menuRepository.findMenuByCode(code);
	}

	/**
	 * Validate menu.
	 *
	 * @param saveParam the save param
	 * @return the binding result
	 */
	public boolean validateMenu(Map saveParam) {
		boolean validationType = false;
		List<Map> insertList = (List<Map>)saveParam.getOrDefault("insertMenus",Lists.newArrayList());

		for (Map createMenu : insertList) {
			String menuCd = createMenu.getOrDefault("menu_cd","") == null?  "" : (String) createMenu.getOrDefault("menu_cd","");
			Map<String,Object> getMenuMap = this.findMenuByCode(menuCd) == null? Maps.newHashMap() : this.findMenuByCode(menuCd);
			String getMenuCd = getMenuMap.getOrDefault("menu_cd","") == null?  "" : (String) getMenuMap.getOrDefault("menu_cd","");

			if (StringUtils.isNotEmpty(getMenuCd)) {
				validationType = true;
				break;
			}
		}
		return validationType;
	}

	/**
	 * list menu 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListMenu
	 */
	public ResultMap saveListMenu(Map<String, Object> saveParam) {
		if (this.validateMenu(saveParam)) {
			throw new CommonException("STD.ADM1039"); //중복된 메뉴코드가 존재합니다.
		}

		List<Map<String, Object>> insertMenus = (List<Map<String, Object>>)saveParam.getOrDefault("insertMenus", Lists.newArrayList());
		//메뉴 리스트 추가
		this.insertMenuList(insertMenus,saveParam);

		//메뉴 리스트 업데이트
		List<Map<String, Object>> updateMenus = (List<Map<String, Object>>)saveParam.getOrDefault("updateMenus", Lists.newArrayList());
		this.updateMenuList(updateMenus);

		//메뉴 이름 삭제 및 삭제 Status update
		List<Map<String, Object>> deleteMenus = (List<Map<String, Object>>)saveParam.getOrDefault("deleteMenus", Lists.newArrayList());
		this.deleteMenuNameAndStatus(deleteMenus);

		return ResultMap.SUCCESS();
	}

	/**
	 * 메뉴 삭제 시 이름 및 status 업데이트(D)
	 *
	 * @param deleteMenus
	 */
	public void deleteMenuNameAndStatus(List<Map<String, Object>> deleteMenus) {
		//delete
		for (Map deleteMenu : deleteMenus) {
			// esaauml삭제 (메뉴명 삭제)
			this.deleteMenuName(deleteMenu);

			// esaaumm sts D로 업데이트 (메뉴상태업데이트)
			this.updateMenuDeleteStatus(deleteMenu);
		}
	}

	/**
	 * 메뉴 삭제 status update
	 * @param deleteMenu
	 */
	private void updateMenuDeleteStatus(Map deleteMenu) {
		// esaaumm sts D로 업데이트 (메뉴상태업데이트)
		menuRepository.updateMenuDeleteStatus(deleteMenu);
	}

	/**
	 * 메뉴명 삭제
	 * @param deleteMenu
	 */
	public void deleteMenuName(Map deleteMenu) {
		// esaauml삭제 (메뉴명 삭제)
		menuRepository.deleteMenuName(deleteMenu);
	}

	/**
	 * 메뉴 리스트 수정
	 * @param updateMenus
	 */
	public void updateMenuList(List<Map<String, Object>> updateMenus) {
		// update
		for (Map updateMenu : updateMenus) {
			this.updateMenuProcess(updateMenu);
		}
	}

	/**
	 * 메뉴 수정 프로세스
	 * @param updateMenu
	 */
	public ResultMap updateMenuProcess(Map updateMenu) {
		this.updateMenuInfo(updateMenu);

		String mlMenuCd = updateMenu.getOrDefault("ml_menu_cd","") == null?  "" : (String) updateMenu.getOrDefault("ml_menu_cd","");
		if (updateMenu.containsKey("ml_menu_cd") && StringUtils.isNotEmpty(mlMenuCd)){
			this.updateMenuName(updateMenu);
		}else{
			this.insertMenuName(updateMenu);
		}

		String langCdTwo = updateMenu.getOrDefault("lang_ccd2","") == null?  "" : (String) updateMenu.getOrDefault("lang_ccd2","");
		String langCd = updateMenu.getOrDefault("lang_ccd","") == null?  "" : (String) updateMenu.getOrDefault("lang_ccd","");
		if (StringUtils.isNotEmpty(langCdTwo)) {

			if(!langCd.equals(langCdTwo)){
				String menuNmTwo = updateMenu.getOrDefault("lang_ccd_menu_nm","") == null?  "" : (String) updateMenu.getOrDefault("lang_ccd_menu_nm","");
				updateMenu.put("lang_ccd", langCdTwo);
				updateMenu.put("menu_nm", menuNmTwo);
			}

			String menuNameLangTwo = updateMenu.getOrDefault("ml_menu_cd2","") == null?  "" : (String) updateMenu.getOrDefault("ml_menu_cd2","");
			if (updateMenu.containsKey("ml_menu_cd2") && StringUtils.isNotEmpty(menuNameLangTwo)){
				this.updateMenuNameLang(updateMenu);
			}else{
				this.updateMenuNameLang(updateMenu);
				this.insertMenuName(updateMenu);
			}
		}
		return ResultMap.SUCCESS();
	}

	/**
	 *  MENU_MULTLANG 메뉴 이름 언어 수정 (다국어)
	 * @param updateMenu
	 */
	private void updateMenuNameLang(Map updateMenu) {
		menuRepository.updateMenuNameLang(updateMenu);
	}

	/**
	 * 메뉴 이름 추가
	 * @param updateMenu
	 */
	public void insertMenuName(Map updateMenu) {
		menuRepository.insertMenuName(updateMenu);
	}

	/**
	 * 메뉴 이름 수정
	 * @param updateMenu
	 */
	public void updateMenuName(Map updateMenu) {
		menuRepository.updateMenuName(updateMenu);
	}

	/**
	 * 메뉴 정보 수정
	 * @param updateMenu
	 */
	public void updateMenuInfo(Map updateMenu) {
		menuRepository.updateMenuInfo(updateMenu);
	}

	/**
	 * 메뉴 리스트 추가
	 * @param insertMenus
	 * @param saveParam
	 */
	public void insertMenuList(List<Map<String, Object>> insertMenus, Map<String,Object> saveParam){

		boolean autoMenuFunctionAndUrlYn = saveParam.containsKey("autoMenuFuncAndUrlYn") && "Y".equals(saveParam.getOrDefault("autoMenuFuncAndUrlYn",""));

		// insert
		for (Map insertMenu : insertMenus) {
			this.insertMenu(insertMenu , autoMenuFunctionAndUrlYn);
		}
	}

	/**
	 * 메뉴 추가
	 * @param insertMenu
	 * @param autoMenuFunctionAndUrlYn
	 */
	private void insertMenu(Map insertMenu ,boolean autoMenuFunctionAndUrlYn) {
		this.insertMenuInfo(insertMenu);
		this.insertMenuName(insertMenu);

		String langCdTwo = insertMenu.getOrDefault("lang_ccd2","") == null?  "" : (String) insertMenu.getOrDefault("lang_ccd2","");
		if (StringUtils.isNotEmpty(langCdTwo) && !langCdTwo.equals("ko_KR")) {
			String menuNmTwo = insertMenu.getOrDefault("lang_ccd_menu_nm","") == null?  "" : (String) insertMenu.getOrDefault("lang_ccd_menu_nm","");
			insertMenu.put("lang_ccd", langCdTwo);
			insertMenu.put("menu_nm", menuNmTwo);
			//다국어용 insert menu
			this.insertMenuName(insertMenu);
		}
	}

	/**
	 * ESAAUMM 추가 ( 메뉴 추가 )
	 * @param insertMenu
	 */
	public void insertMenuInfo(Map insertMenu) {
		menuRepository.insertMenuInfo(insertMenu);
	}


	public void saveListBoardMenuFuncUrl(Map<String, Object> insertMenu) {
		String menuCd = insertMenu.getOrDefault("menu_cd","") == null?  "" : (String) insertMenu.getOrDefault("menu_cd","");

		for(String defaultFuncCd : DEFAULT_FUNCTION_CODE_LIST){
			Map<String, Object> funcInfo = Maps.newHashMap();
			funcInfo.put("act_cd", defaultFuncCd);
			funcInfo.put("menu_cd", menuCd);
			funcInfo.put("act_nm", defaultFuncCd);
			//this.insertMenuInfo(funcInfo);
			this.insertMenuFunc(funcInfo);

			// Class 내에 method 찾은 뒤 자동메뉴 역할 등록
			this.classMethodFindAndMenuUrlAutoInsert(BoardController.class,menuCd);
		}
	  }

	/**
	 * 메뉴기능을 등록한다.
	 * - 메뉴기능코드는 Unique : getCountMenuFunc 로 검사하고 호출할 것.
	 * @param funcInfo
	 */
	public void insertMenuFunc(Map<String, Object> funcInfo) {
		menuFuncService.insertMenuFunc(funcInfo);
	}

	/**
	 * 매개변수로 받은 Class (controller) 내에 urlMapping (.do) 호출자를 가진 method를 검색하여, 자동으로 메뉴역할을 등록한다.
	 * 해당 method를 사용하기 위해서는 urlmapping이 method name + .do 로 처리 가능하도록 작성되어 있어야한다.
	 * @param object
	 * @param menuCd
	 */
	public void classMethodFindAndMenuUrlAutoInsert(Class object,String menuCd){
		  Class classObject = object;
		  Method[] declaredMethods = classObject.getDeclaredMethods();

		  for (int i = 0; i < declaredMethods.length; i++) {
			  Map<String, Object> funcUrlInfo = Maps.newHashMap();

			  String url = declaredMethods[i].getName().toString();
			  url = url +".do";
			  if(url.indexOf("find") > -1){
				  funcUrlInfo.put("act_cd", "R");
			  }else{
				  funcUrlInfo.put("act_cd", "S");
			  }

			  funcUrlInfo.put("menu_act_url",url);
			  funcUrlInfo.put("menu_cd", menuCd);
			  roleService.saveCallPattern(funcUrlInfo);
			  authUrlService.insertFuncUrl(funcUrlInfo);
		  }
	  }

}
