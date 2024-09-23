package smartsuite.app.common.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.common.portal.repository.PortalRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
@Transactional
public class PortalService {

	@Inject
	PortalRepository portalRepository;
	


	public Map findUserLayout(Map param){
		return portalRepository.findUserLayout(param);
	}
	
	public Integer saveUserLayout(Map param){
		Map userLayout = this.findUserLayout(param);
        Integer success = null;

        if (MapUtils.isEmpty(userLayout)){
            success = insertUserLayout(param);
        }else{
            success = updateUserLayout(param);
        }
        return success;
	}
	
	public Integer insertUserLayout(Map param){
		return portalRepository.insertUserLayout(param);
	}

	public Integer updateUserLayout(Map param){
		return portalRepository.updateUserLayout(param);
	}
	
	public Integer deleteUserLayout(Map param){
		return portalRepository.deleteUserLayout(param);
	}

	public Map findComposableWidgetList(Map param){
		List<Map> widgetList = portalRepository.findComposableWidgetList(param);

		Map baseAttr = new HashMap();
		baseAttr.put("baseAttr", "[]");

		if (widgetList == null){
			return baseAttr;
		}

		StringBuffer widgetListSrc = new StringBuffer("[");

		for (int i=0; i<widgetList.size(); i++){
			Map widget = widgetList.get(i);
			String baseAttrStr = (String)widget.get("portlt_scr_attr");
			widgetListSrc.append(baseAttrStr);
			if (i != widgetList.size()-1)
				widgetListSrc.append(',');
		}
		widgetListSrc.append(']');
		baseAttr.put("baseAttr", widgetListSrc);
		baseAttr.put("baseList", widgetList);

		return baseAttr;
	}
	
	public Map findPortalCommonConfig(Map param) {
		return portalRepository.findPortalCommonConfig(param);
	}
	
	public Integer savePortalCommonConfig(Map param) {
		return portalRepository.savePortalCommonConfig(param);	
	}
	
	public Integer updatePortalCommonConfig(Map param) {
		return portalRepository.updatePortalCommonConfig(param);
	}

}
