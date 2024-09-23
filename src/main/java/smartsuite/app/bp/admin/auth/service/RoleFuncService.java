package smartsuite.app.bp.admin.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.auth.repository.RoleFuncRepository;
import smartsuite.app.common.message.MessageUtil;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;

/**
 * 롤기능 관련 처리하는 서비스 Class입니다.
 *
 * @author JongKyu Kim
 * @see
 * @FileName RoleFuncService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] JongKyu Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class RoleFuncService {

	@Inject
	RoleFuncRepository roleFuncRepository;

	@Inject
	MessageUtil messageUtil;
	/**
	 * 롤기능을 등록한다.
	 *
	 * - 롤기능코드는 Unique : getCountRoleFunc 로 검사하고 호출할 것.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : insertRoleFunc
	 */
	public void insertRoleFunc(Map<String, Object> param) {
		roleFuncRepository.insertRoleFunc(param);
	}

	/**
	 * 롤기능을 삭제한다.
	 *
	 * - 물리적 삭제
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @Date : 2016. 2. 2
	 * @Method Name : deleteRoleFunc
	 */
	public void deleteRoleFunc(Map<String, Object> param) {
		roleFuncRepository.deleteRoleFunc(param);
	}

	/**
	 * 롤기능을 카운트한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the count RoleFunc
	 * @Date : 2016. 2. 2
	 * @Method Name : getCountRoleFunc
	 */
	public boolean existRoleFunc(Map<String, Object> param) {
		int getCountRoleFunc = roleFuncRepository.getCountRoleFunc(param);
		return (getCountRoleFunc > 0);
	}

	/**
	 * 롤기능 목록을 등록/삭제한다.
	 *
	 * @author : JongKyu Kim
	 * @param param {"insertRoleFuncs", "deleteRoleFuncs"}
	 * @return the map<string, object>
	 * @Date : 2016. 2. 2
	 * @Method Name : saveListRoleFunc
	 */
	public ResultMap saveListRoleFunc(Map<String, Object> param) {
		List<Map<String, Object>> insertRoleFuncList = (List<Map<String, Object>>)param.getOrDefault("insertRoleFuncs", Lists.newArrayList());
		List<Map<String, Object>> deleteRoleFuncList = (List<Map<String, Object>>)param.getOrDefault("deleteRoleFuncs", Lists.newArrayList());

		//롤 기능 추가
		int dupleCnt = this.insertListRoleFunc(insertRoleFuncList);

		// 롤기능 삭제
		this.deleteRoleFuncList(deleteRoleFuncList);

		if(dupleCnt > 0) {
			MessageUtil.MessageBean messageBean= new MessageUtil.MessageBean();
			messageBean.setCodeName("STD.N2010");
			messageBean.setDefaultMessage("중복 데이터 {0}건 제외 후 추가하였습니다.");
			messageBean.setReplaceMessage(dupleCnt);
			throw new CommonException(messageUtil.getCodeMessage(messageBean));
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 롤 기능 리스트 삭제
	 * @param deleteRoleFuncList
	 */
	public void deleteRoleFuncList(List<Map<String, Object>> deleteRoleFuncList) {
		for (Map<String, Object> deleteRoleInfo : deleteRoleFuncList) {
			this.deleteRoleFunc(deleteRoleInfo);
		}
	}

	/**
	 * 롤 기능 리스트 추가
	 * @param insertRoleFuncList
	 * @return
	 */
	public int insertListRoleFunc(List<Map<String, Object>> insertRoleFuncList) {
		int dupleCnt = 0;
		for (Map<String, Object> insertRoleFuncInfo : insertRoleFuncList) {
			if (this.existRoleFunc(insertRoleFuncInfo)) { // 롤기능 중복 체크
				dupleCnt++;
			}else{
				this.insertRoleFunc(insertRoleFuncInfo);
			}
		}
		return dupleCnt;
	}

	/**
	 * ESAAURF 삭제 (매핑된 롤메뉴 전체 삭제)
	 * @param deleteMenu
	 */
	public void deleteRoleFuncAll(Map deleteMenu) {
		roleFuncRepository.deleteRoleFuncAll(deleteMenu);
	}

	/**
	 * 롤 메뉴 기능 삭제
	 * @param deleteRoleInfo
	 */
	public void deleteRoleMenuFunc(Map deleteRoleInfo) {
		roleFuncRepository.deleteRoleMenuFunc(deleteRoleInfo);
	}

	/**
	 * 롤 메뉴기능 목록 조회를 요청한다.
	 * @param roleInfo
	 * @return
	 */
	public List findListRoleMenuWithMenuFunc(Map roleInfo) {
		return roleFuncRepository.findListRoleMenuWithMenuFunc(roleInfo);
	}

	public List<Map<String, Object>> findListMenuFuncRole(Map param) {
		return roleFuncRepository.findListMenuFuncRole(param);
	}
}
