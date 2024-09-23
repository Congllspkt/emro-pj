package smartsuite.app.bp.edu.cmsCommon.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.edu.cmsCommon.service.CmsCommonService;
import smartsuite.app.bp.edu.itemreq.service.ItemReqService;
import smartsuite.app.event.CustomSpringEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
@Transactional
public class CmsCommonEventListener {
	
	@Inject
	CmsCommonService cmsCommonService;

	/**
	 * 품목현황에서 넘어온 진행중인 품목등록요청 cnt 구하기
	 * 
	 * @param event
	 */
	@EventListener(condition = "#event.eventId == 'insertItemIattrAfterDelete'")
	public void insertItemIattrAfterDelete(CustomSpringEvent event) {
		Map<String, Object> param = (Map<String, Object>) event.getData();
		cmsCommonService.insertItemIattrAfterDelete((Map<String, Object>) param.get("itemInfo"), (List<Map<String, Object>>) param.get("asgnAttrList"));
	}

	/**
	 * 품목현황에서 넘어온 진행중인 품목등록요청 cnt 구하기
	 *
	 * @param event
	 */
	@EventListener(condition = "#event.eventId == 'findListItemAsgnAttrFromItemMaster'")
	public void findListItemAsgnAttrFromItemMaster(CustomSpringEvent event) {
		List<Map<String, Object>> resultList = cmsCommonService.findListItemAsgnAttrFromItemMaster((Map<String, Object>)event.getData());
		event.setResult(resultList);
	}

	/**
	 * 품목현황에서 넘어온 배정속성 목록 구하기(기본)
	 *
	 * @param event
	 */
	@EventListener(condition = "#event.eventId == 'findListItemAsgnAttr'")
	public void findListItemAsgnAttr(CustomSpringEvent event) {
		List<Map<String, Object>> resultList = cmsCommonService.findListItemAsgnAttr((Map<String, Object>) event.getData());
		event.setResult(resultList);
	}

	/**
	 * 품목현황에서 넘어온 배정속성 목록 구하기(itemcat_cd로)
	 *
	 * @param event
	 */
	@EventListener(condition = "#event.eventId == 'findListItemAsgnAttrByItemcat'")
	public void findListItemAsgnAttrByItemcat(CustomSpringEvent event) {
		List<Map<String, Object>> resultList = cmsCommonService.findListItemAsgnAttrByItemcat((Map<String, Object>) event.getData());
		event.setResult(resultList);
	}
}
