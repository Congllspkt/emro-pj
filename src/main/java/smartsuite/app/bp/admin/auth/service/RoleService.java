package smartsuite.app.bp.admin.auth.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import smartsuite.app.bp.admin.auth.repository.RoleRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * Role 관련 처리하는 서비스 Class입니다.
 *
 * @author Yeon-u Kim
 * @see
 * @FileName RoleService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 3
 * @변경이력 : [2016. 2. 3] Yeon-u Kim 최초작성
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class RoleService {

	@Inject
	RoleRepository roleRepository;

	@Inject
	RoleFuncService roleFuncService;


	/**
	 * list role 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListRole
	 */
	public List findListRole(Map param) {
		return roleRepository.findListRole(param);
	}

	/**
	 * list role 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 4
	 * @Method Name : saveListRole
	 */
	public void saveListRole(Map saveParam) {
		List<Map> insertRoleList = saveParam.get("insertRoles") == null? Lists.newArrayList() : (List<Map>) saveParam.get("insertRoles");
		List<Map> updateRoleList = saveParam.get("updateRoles") == null? Lists.newArrayList() : (List<Map>) saveParam.get("updateRoles");
		List<Map> deleteRoleList = saveParam.get("deleteRoles") == null? Lists.newArrayList() : (List<Map>) saveParam.get("deleteRoles");

		// 롤 추가
		this.insertRoleList(insertRoleList);

		// 롤 수정
		this.updateRoleList(updateRoleList);

		// 롤 삭제
		this.deleteListRole(deleteRoleList);
	}

	/**
	 * 롤 리스트 삭제
	 * @param deleteRoleList
	 */
	public void deleteListRole(List<Map> deleteRoleList) {
		for (Map deleteRoleInfo : deleteRoleList) {
			this.deleteRoleInfo(deleteRoleInfo);
		}
	}

	/**
	 * 롤 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleInfo(Map deleteRoleInfo) {
		// ESAAURM 삭제 (롤 메뉴, 사용자, 롤부서)

		// 롤 메뉴 삭제
		this.deleteRoleMenuInfo(deleteRoleInfo);

		// 롤 메뉴 기능 삭제
		roleFuncService.deleteRoleMenuFunc(deleteRoleInfo);

		// 롤 유저 삭제
		this.deleteRoleUser(deleteRoleInfo);

		// 롤 부서 삭제
		this.deleteRoleDept(deleteRoleInfo);

		// 포틀릿 롤 삭제
		this.deleteRolePortlet(deleteRoleInfo);

		// 롤 삭제
		this.deleteRole(deleteRoleInfo);
	}

	/**
	 * 포틀릿 연계 롤 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRolePortlet(Map deleteRoleInfo) {
		roleRepository.deleteRolePortlet(deleteRoleInfo);
	}


	/**
	 * 롤 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRole(Map deleteRoleInfo) {
		roleRepository.deleteRole(deleteRoleInfo);
	}

	/**
	 * 롤 부서 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleDept(Map deleteRoleInfo) {
		roleRepository.deleteRoleDept(deleteRoleInfo);
	}

	/**
	 * 롤 유저 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleUser(Map deleteRoleInfo) {
		roleRepository.deleteRoleUser(deleteRoleInfo);
	}

	/**
	 * 롤 리스트 수정
	 * @param updateRoleList
	 */
	public void updateRoleList(List<Map> updateRoleList) {
		for (Map updateRoleInfo : updateRoleList) {
			roleRepository.updateRole(updateRoleInfo);
		}
	}

	/**
	 * 롤 리스트 추가
	 * @param insertRoleList
	 */
	public void insertRoleList(List<Map> insertRoleList) {
		for (Map insertRoleInfo : insertRoleList) {
			this.insertRoleInfo(insertRoleInfo);
		}
	}

	/**
	 * 롤 추가 or 수정
	 * @param insertRoleInfo
	 */
	public void insertRoleInfo(Map insertRoleInfo) {
		String roleCode = insertRoleInfo.getOrDefault("role_cd","")  == null?  "" : (String) insertRoleInfo.getOrDefault("role_cd","");
		if (MapUtils.isEmpty(this.findRoleByCode(roleCode))){
			this.insertRole(insertRoleInfo);
		}else{
			this.updateRole(insertRoleInfo);
		}
	}

	/**
	 * 롤 수정
	 * @param insertRoleInfo
	 */
	public void updateRole(Map insertRoleInfo) {
		roleRepository.updateRole(insertRoleInfo);
	}

	/**
	 * 롤 추가
	 * @param insertRoleInfo
	 */
	public void insertRole(Map insertRoleInfo) {
		roleRepository.insertRole(insertRoleInfo);
	}

	/**
	 * list role menu 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @param roleId the role id
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleMenu
	 */
	public void saveListRoleMenu(Map saveParam) {
		List<Map> insertRoleMenuList = (List<Map>)saveParam.getOrDefault("insertListMenuRole",Lists.newArrayList());
		List<Map> deleteRoleMenuList = (List<Map>)saveParam.getOrDefault("deleteListMenuRole",Lists.newArrayList());

		// 롤 메뉴 리스트 추가
		this.insertRoleMenuList(insertRoleMenuList);

		this.deleteRoleMenuList(deleteRoleMenuList);
	}

	/**
	 * 롤 메뉴 리스트 삭제
	 * @param deleteRoleMenuList
	 */
	public void deleteRoleMenuList(List<Map> deleteRoleMenuList) {
		for (Map deleteRoleMenuInfo : deleteRoleMenuList) {
			this.deleteRoleMenuInfo(deleteRoleMenuInfo);
		}
	}

	/**
	 * 롤 메뉴 삭제
	 * @param deleteRoleMenuInfo
	 */
	public void deleteRoleMenuInfo(Map deleteRoleMenuInfo) {
		roleRepository.deleteRoleMenu(deleteRoleMenuInfo);
	}

	/**
	 * 롤 메뉴 리스트 추가
	 * @param insertRoleMenuList
	 */
	public void insertRoleMenuList(List<Map> insertRoleMenuList) {
		for (Map insertRoleMenu : insertRoleMenuList) {
			this.insertRoleMenuInfo(insertRoleMenu);
		}
	}

	/**
	 * 롤 메뉴 추가
	 * @param insertRoleMenu
	 */
	public void insertRoleMenuInfo(Map insertRoleMenu) {
		roleRepository.insertRoleMenu(insertRoleMenu);
	}

	/**
	 * Validate.
	 *
	 * @param saveParam the save param
	 * @return the binding result
	 */
	public void validate(Map saveParam) {
		BindingResult result = new BeanPropertyBindingResult(saveParam, "");
		List<Map> insertRoleList = (List<Map>)saveParam.getOrDefault("insertRoles",Lists.newArrayList());

		for (Map insertRoleInfo : insertRoleList) {
			String roleCode = insertRoleInfo.getOrDefault("role_cd","") == null?  "" : (String) insertRoleInfo.getOrDefault("role_cd","");
			if (MapUtils.isNotEmpty(this.findRoleByCode(roleCode))){
				result.reject("Duplicate roleCode");
				break;
			}
		}

		if (result.hasErrors()) {
			throw new CommonException(ErrorCode.DUPLICATED);
		} else {
			this.saveListRole(saveParam);
		}
	}

	/**
	 * role by code 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param code the code
	 * @return the map
	 * @Date : 2016. 2. 4
	 * @Method Name : findRoleByCode
	 */
	public Map findRoleByCode(String code) {
		return roleRepository.findRoleByCode(code);
	}

	/**
	 * list role menu 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param role the role
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListRoleMenu
	 */
	public List findListRoleMenu(Map roleInfo) {
		return roleRepository.findListRoleMenu(roleInfo);
	}


	/**
	 * 롤 리스트 삭제 요청
	 * @param saveParam
	 * @return
	 */
	public ResultMap deleteListRoleRequest(Map saveParam) {
		List<Map> deleteRoleList = (List<Map>)saveParam.getOrDefault("deleteRoles",Lists.newArrayList());
		// 롤 리스트 삭제
		this.deleteListRole(deleteRoleList);
		return ResultMap.SUCCESS();
	}




	/**
	 * list role user 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListUserByRole
	 */
	public List findListUserByRole(Map param) {
		return roleRepository.findListUserByRole(param);
	}

	/**
	 * list role user 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleUser
	 */
	public void saveListRoleUser(Map saveParam) {
		List<Map> insertRoleUserList = (List<Map>)saveParam.getOrDefault("insertRoleUsers",Lists.newArrayList());
		List<Map> deleteRoleUserList = (List<Map>)saveParam.getOrDefault("deleteRoleUsers",Lists.newArrayList());

		// 롤 유저 리스트 추가
		this.insertRoleUserList(insertRoleUserList);

		// 롤 유저 리스트 삭제
		this.deleteRoleUserList(deleteRoleUserList);
	}

	/**
	 * 롤 유저 리스트 삭제
	 * @param deleteRoleUserList
	 */
	public void deleteRoleUserList(List<Map> deleteRoleUserList) {
		for (Map deleteRoleUser : deleteRoleUserList) {
			this.deleteRoleUser(deleteRoleUser);
		}
	}

	/**
	 * 롤 유저 리스트 추가
	 * @param insertRoleUserList
	 */
	public void insertRoleUserList(List<Map> insertRoleUserList) {
		for (Map insertRoleUser : insertRoleUserList) {
			this.insertRoleUser(insertRoleUser);
		}
	}

	/**
	 * 롤 유저 추가
	 * @param insertRoleUser
	 */
	public void insertRoleUser(Map insertRoleUser) {
		roleRepository.insertRoleUser(insertRoleUser);
	}

	/**
	 * list role dept 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 2. 11
	 * @Method Name : findListRoleDept
	 */
	public List findListRoleDept(Map param) {
		return roleRepository.findListRoleDept(param);
	}

	/**
	 * list role dept 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param saveParam the save param
	 * @Date : 2016. 2. 11
	 * @Method Name : saveListRoleDept
	 */
	public void saveListRoleDept(Map saveParam) {
		List<Map> insertRoleDeptList = (List<Map>)saveParam.getOrDefault("insertRoleDepts",Lists.newArrayList());
		List<Map> deleteRoleDeptList = (List<Map>)saveParam.getOrDefault("deleteRoleDepts",Lists.newArrayList());

		// 롤 부서 리스트 추가
		this.insertRoleDeptList(insertRoleDeptList);

		// 롤 부서 리스트 삭제
		this.deleteRoleDeptList(deleteRoleDeptList);
	}

	/**
	 * 롤 부서 리스트 삭제
	 * @param deleteRoleDeptList
	 */
	public void deleteRoleDeptList(List<Map> deleteRoleDeptList) {
		for (Map deleteRoleDept : deleteRoleDeptList) {
			this.deleteRoleDept(deleteRoleDept);
		}
	}

	/**
	 * 롤 부서 리스트 추가
	 * @param insertRoleDeptList
	 */
	public void insertRoleDeptList(List<Map> insertRoleDeptList) {
		for (Map insertRoleDept : insertRoleDeptList) {
			this.insertRoleDept(insertRoleDept);
		}
	}

	/**
	 * 롤 부서 추가
	 * @param insertRoleDept
	 */
	public void insertRoleDept(Map insertRoleDept) {
		roleRepository.insertRoleDept(insertRoleDept);
	}

	/**
	 * call pattern 저장한다.
	 *
	 * @author : seonghun kim
	 * @param pattern the pattern
	 * @Date : 2017. 2. 14
	 * @Method Name : saveCallPattern
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveCallPattern(Map pattern) {
		roleRepository.insertCallPattern(pattern);
	}

	/**
	 * call pattern 이 존재하는지 확인한다.
	 *
	 * @author : seonghun kim
	 * @param pattern the pattern
	 * @Date : 2017. 2. 15
	 * @Method Name : hasCallPattern
	 */
	public boolean hasCallPattern(Map pattern) {
		List<Map<String,Object>> findListCallPattern =  roleRepository.findListCallPattern(pattern);
		return (null != findListCallPattern && findListCallPattern.size() > 0 );
	}

	/**
	 * url의 존재 여부와 해당 메뉴코드로의 사용자 접근 역할이 존재하는지 확인한다.
	 *
	 * @author : hjhwang
	 * @param pattern the pattern
	 * @Date : 2019. 3. 15
	 * @Method Name : hasCallPattern
	 */
	public List<Map<String,Object>> hasRoleWithUrl(Map pattern) {
		return roleRepository.selectRoleWithUrl(pattern);
	}

	/**
	 * 메뉴에 부여된 role list 조회를 요청한다.
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListMenuRole(Map param) {
		return roleRepository.findListMenuRole(param);
	}


	public void deleteCallPatternAll(Map deleteMenuUrlInfo){
		roleRepository.deleteCallPatternAll(deleteMenuUrlInfo);
	}

	/**
	 * Call pattern list를 조회한다.
	 * @param menuInfo
	 * @return
	 */
	public List<Map<String,Object>> findListCallPattern(Map<String,Object> menuInfo){
		return roleRepository.findListCallPattern(menuInfo);
	}

	/**
	 * ESAAUMS 추가
	 * @param insertMenuInfo
	 */
	public void insertCallPattern(Map<String, Object> insertMenuInfo) {
		roleRepository.insertCallPattern(insertMenuInfo);
	}

	public void deleteCallPattern(Map<String, Object> deleteMenuUrlInfo) {
		roleRepository.deleteCallPattern(deleteMenuUrlInfo);
	}

	/**
	 * url과 authcode 매핑 정보가 존재하는지 확인한다
	 *
	 * @author : hjhwang
	 * @param pattern the pattern
	 * @Date : 2017. 2. 15
	 * @Method Name : hasCallPattern

	public boolean hasUrlAuthcodeMapping(Map pattern) {
		return sqlSession.selectOne("role.selectUrlAuthcodeMapping", pattern) != null;
	}
	 */
	/**
	 * url과 authcode 매핑 정보를 저장한다.
	 *
	 * @author : hjhwang
	 * @param pattern the pattern
	 * @Date : 2017. 2. 14
	 * @Method Name : saveUrlAuthcodeMapping

	public void saveUrlAuthcodeMapping(Map pattern) {
		sqlSession.insert("role.insertUrlAuthcodeMapping", pattern);
	}
	 */
}
