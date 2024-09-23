package smartsuite.app.common.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.common.portal.repository.PortletRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * Portlet 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @FileName PortletService.java
 * @package smartsuite.app.bp.common.portlet
 * @Since 2016. 10. 13
 * @변경이력 : [2016. 10. 13] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class PortletService {

	@Inject
	PortletRepository portletRepository;

	/**
	 * 포틀릿 현황 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 10. 13
	 * @Method Name : findListPortlet
	 */
	public List<Map<String, Object>> findListPortlet(Map<String, Object> param) {
		return portletRepository.findListPortlet(param);
	}
	
	/**
	 * 포틀릿 정보를 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2016. 10. 13
	 * @Method Name : findInfoPortlet
	 */
	public Map<String, Object> findInfoPortlet(Map<String, Object> param) {
		return portletRepository.findInfoPortlet(param);
	}
	
	/**
	 * 포틀릿 사용자 롤을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 10. 13
	 * @Method Name : findListPortletRoles
	 */
	public List<Map<String, Object>> findListPortletRoles(Map<String, Object> param) {
		return portletRepository.findListPortletRoles(param);
	}
	
	/**
	 * 포틀릿 정보 및 사용자 롤을 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 10. 13
	 * @Method Name : saveInfoPortlet
	 */
	public ResultMap saveInfoPortlet(Map<String, Object> param){
		
		// 포틀릿 정보
		Map<String, Object> portletInfo = (Map<String, Object>)param.getOrDefault("portletInfo",Maps.newHashMap());

		boolean isNew = (boolean) portletInfo.getOrDefault("is_new",false);
		
		if(isNew){
			// 중복체크
			Map existPortlet = this.findInfoPortlet(portletInfo);
			
			if(MapUtils.isNotEmpty(existPortlet)){
				throw new CommonException(ErrorCode.DUPLICATED);
			}

			this.insertPortlet(portletInfo);
		}else{
			this.updatePortlet(portletInfo);
		}
		
		// 포틀릿 사용자 롤 리스트
		List<Map<String, Object>> portletRoleList = (List<Map<String, Object>>)param.getOrDefault("portletRoleList", Lists.newArrayList()); 
		
		for(Map portletRoleInfo: portletRoleList){
			String useYn = (String)portletRoleInfo.getOrDefault("use_yn","");

			if("Y".equals(useYn)){
				portletRoleInfo.put("portl_usr_typ_ccd", portletInfo.get("portl_usr_typ_ccd") );
				portletRoleInfo.put("portlt_id", portletInfo.get("portlt_id") );
				
				portletRepository.insertPortletRole(portletRoleInfo);
			}else if(!isNew) {
				portletRoleInfo.put("portl_usr_typ_ccd", portletInfo.get("portl_usr_typ_ccd") );
				portletRoleInfo.put("portlt_id", portletInfo.get("portlt_id"));
				this.deletePortletRoleByClassName(portletRoleInfo);
			}
		}
		return ResultMap.SUCCESS();
	}

	private void deletePortletRoleByClassName(Map portletRoleInfo) {
		portletRepository.deletePortletRoleByClassName(portletRoleInfo);
	}

	public void updatePortlet(Map<String, Object> portletInfo) {
		portletRepository.updatePortlet(portletInfo);
	}

	public void insertPortlet(Map<String, Object> portletInfo) {
		portletRepository.insertPortlet(portletInfo);
	}

	/**
	 * 포틀릿 현황을 삭제한다.(포틀릿 사용자 롤도 삭제한다.)
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 10. 13
	 * @Method Name : deleteListPortlet
	 */
	public ResultMap deleteListPortlet(Map<String, Object> param){
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());

		for(Map row : deleteList){
			this.deletePortletRoleByClassName(row);
			this.deletePortlet(row);
		}
		
		return ResultMap.SUCCESS();
	}

	public void deletePortlet(Map row) {
		portletRepository.deletePortlet(row);
	}

	/**
	 * 포틀릿 기본 레이아웃을 조회한다.
	 *
	 * @author : dmwon
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 07. 05
	 * @Method Name : findDefaultLayout
	 */
	public Map findDefaultLayout(Map param) {
		String portalUserTypeCcd = (String) param.getOrDefault("portl_usr_typ_ccd","");
		if(StringUtils.isEmpty(portalUserTypeCcd)) param.put("portl_usr_typ_ccd", "ALL");
		return portletRepository.findDefaultLayout(param);
	}
	
	/**
	 * 포틀릿 기본 레이아웃을 저장한다.
	 *
	 * @author : dmwon
	 * @param param the param
	 * @return the map
	 * @Date : 2017. 07. 05
	 * @Method Name : saveDefaultLayout
	 */
	public ResultMap saveDefaultLayout(Map param){
		Map userLayout = findDefaultLayout(param);
        if (MapUtils.isEmpty(userLayout)){
			this.insertDefaultLayout(param);
        }else{
			this.updateDefaultLayout(param);
        }
        return ResultMap.SUCCESS();
	}

	private void updateDefaultLayout(Map param) {
		portletRepository.updateDefaultLayout(param);
	}

	private void insertDefaultLayout(Map param) {
		portletRepository.insertDefaultLayout(param);
	}

}
