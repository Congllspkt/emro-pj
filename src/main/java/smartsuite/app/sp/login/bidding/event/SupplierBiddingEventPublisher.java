package smartsuite.app.sp.login.bidding.event;

import com.google.common.collect.Lists;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.event.CustomSpringEvent;
import smartsuite.mybatis.data.impl.PageResult;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupplierBiddingEventPublisher {
	@Inject
	ApplicationEventPublisher applicationEventPublisher;

	public PageResult findPagingBiddingNoticeList(Map<String, Object> pageInfo) {
		CustomSpringEvent customSpringEvent = CustomSpringEvent.toCompleteEvent("findPagingBidNoticeList", pageInfo);
		applicationEventPublisher.publishEvent(customSpringEvent);

		if(customSpringEvent.getResult() != null) {
			return (PageResult) customSpringEvent.getResult();
		} else {
			return null;
		}
	}

	public String getBiddingNoticeTemplateContent(Map<String,Object> param){
		CustomSpringEvent customSpringEvent = CustomSpringEvent.toCompleteEvent("getBidNoticeTemplateContent", param);
		applicationEventPublisher.publishEvent(customSpringEvent);
		return  (String) customSpringEvent.getResult();
	}

	public int getBiddingNoticeTotalCount(Map<String,Object> param){
		CustomSpringEvent customSpringEvent = CustomSpringEvent.toCompleteEvent("getBidNoticeTotalCount", param);
		applicationEventPublisher.publishEvent(customSpringEvent);
		return  (int) customSpringEvent.getResult();
	}

}
