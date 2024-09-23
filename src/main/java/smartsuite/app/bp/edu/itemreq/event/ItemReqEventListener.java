package smartsuite.app.bp.edu.itemreq.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.edu.itemreq.service.ItemReqService;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.event.CustomSpringEvent;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
@Transactional
public class ItemReqEventListener {
	
	@Inject
	ItemReqService itemReqService;

	/**
	 * 품목현황에서 넘어온 진행중인 품목등록요청 cnt 구하기
	 * 
	 * @param 
	 * @return int
	 */
	@EventListener(condition = "#event.eventId == 'findCntProgressingItemRegReq'")
	public void findCntProgressingItemRegReq(CustomSpringEvent event) {
		int checkCnt = itemReqService.findCntProgressingItemRegReq((Map<String, Object>) event.getData());
		event.setResult(checkCnt);
	}

	/**
	 * 품목현황에서 넘어온 진행중인 품목등록요청 이 있는지 확인
	 *
	 * @param
	 * @return boolean
	 */
	@EventListener(condition = "#event.eventId == 'checkExistedItemRegReq'")
	public void checkExistedItemRegReq(CustomSpringEvent event) {
		Boolean checkReq = itemReqService.checkExistedItemRegReq((Map<String, Object>) event.getData());
		event.setResult(checkReq);
	}

	/**
	 * 품목현황에서 넘어온 변경 요청 품목 정보 
	 *
	 * @param
	 * @return map
	 */
	@EventListener(condition = "#event.eventId == 'findInfoModifyItemReq'")
	public void findInfoModifyItemReq(CustomSpringEvent event) {
		Map<String, Object> itemInfo = itemReqService.findInfoModifyItemReq((Map<String, Object>) event.getData());
		event.setResult(itemInfo);
	}

	/**
	 * 품목현황에서 넘어온 변경 요청 품목 요청
	 *
	 * @param
	 * @return map
	 */
	@EventListener(condition = "#event.eventId == 'saveInfoChangeItemReq'")
	public void saveInfoChangeItemReq(CustomSpringEvent event) {
		ResultMap resultMap = itemReqService.saveInfoChangeItemReq((Map<String, Object>) event.getData());
		event.setResult(resultMap);
	}

}
