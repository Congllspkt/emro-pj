package smartsuite.app.bp.edu.cmsCommon.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.event.CustomSpringEvent;

import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class CmsCommonEventPublisher {

	@Autowired
	ApplicationEventPublisher publisher;

	/**
	 * Item-doctor 연동 유사도 조회
	 *
	 * @param
	 * @return list
	 */
	public List<Map<String, Object>> findListItemSimilarity(Map<String, Object> param) {
		CustomSpringEvent event =  CustomSpringEvent.toCompleteEvent("findSimilarPredictItemList", param);
		publisher.publishEvent(event);
		return (List<Map<String, Object>>) event.getResult();
	}


	/**
	 * bpa 여부, 멀티협력사 여부 조회, 품목코드로
	 *
	 * @param
	 * @return list
	 */
	public List<Map<String, Object>> findListBpaYnAndMutlVdYnByItemCdFromCms(Map<String, Object> param) {
		CustomSpringEvent event =  CustomSpringEvent.toCompleteEvent("findListBpaYnAndMutlVdYnByItemCd", param);
		publisher.publishEvent(event);
		return (List<Map<String, Object>>) event.getResult();
	}
}
