package smartsuite.app.sp.login.bidding.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import freemarker.template.TemplateException;
import smartsuite.app.sp.login.bidding.event.SupplierBiddingEventPublisher;
import smartsuite.mybatis.data.impl.PageResult;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
@Transactional
public class SupplierBiddingService {

	@Inject
	SupplierBiddingEventPublisher supplierBiddingEventPublisher;

	/**
	 * 입찰공고 totalRow 가져오기
	 * @param param
	 * @return
	 */
	public int findTotalBiddingCount(Map<String, Object> param){
		return supplierBiddingEventPublisher.getBiddingNoticeTotalCount(param);
	}
	/**
	 * 협력사포탈 입찰공고 목록 조회
	 *
	 * @author :
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2020. 05. 28
	 * @Method Name : findPagingBiddingNoticeList
	 */
	public PageResult findPagingBiddingNoticeList(Map<String, Object> pageInfo) {
		return supplierBiddingEventPublisher.findPagingBiddingNoticeList(pageInfo);
	}
	/**
	 * 입찰 공고 Template 데이터 binding
	 * @param param(rfx_uuid)
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String getTemplateContent(Map<String, Object> param) throws TemplateException, IOException {
		return supplierBiddingEventPublisher.getBiddingNoticeTemplateContent(param);

	}
}
