package smartsuite.app.sp.login.bidding;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

import freemarker.template.TemplateException;
import smartsuite.app.common.template.service.TemplateGeneratorService;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.app.sp.login.bidding.service.SupplierBiddingService;
import smartsuite.mybatis.data.impl.PageResult;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/sp/login/bidding/**")
public class SupplierBiddingController {

	@Inject
	SupplierBiddingService supplierBiddingService;

	@Inject
	SharedService sharedService;

	@Inject
	TemplateGeneratorService templateGeneratorService;

	/**
	 *  로그인 이전에 팝업으로 입찰공고 모듈을 호출합니다.
	 */
	@RequestMapping(value = "biddingNoticeLink.do", method = RequestMethod.POST)
	public ModelAndView biddingNoticeLink(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> searchParam = Maps.newHashMap();
		String rfxUuid = request.getParameter("rfx_uuid");
		String page = request.getParameter("page");
		String locale = request.getParameter("locale");
		try {
			if(!StringUtils.isEmpty(rfxUuid)){
				searchParam.put("rfx_uuid", rfxUuid);

				mv.addObject("content", supplierBiddingService.getTemplateContent(searchParam));
				mv.addObject("page", page);
				mv.setViewName("login/sp/biddingNoticePopup");
			}else{
				int totalBiddingCount = supplierBiddingService.findTotalBiddingCount(searchParam);
				if(totalBiddingCount > 0){
					searchParam.put("totalRow", totalBiddingCount);
					searchParam.put("page", page);

					Map<String, Object> pageInfo = sharedService.getPageInfo(searchParam);
					Map<String, Object> pageParam = Maps.newHashMap();
					pageParam.put("manualQuery", false);
					pageParam.put("end", pageInfo.get("endRow"));
					pageParam.put("page", pageInfo.get("page"));
					pageParam.put("start", pageInfo.get("startRow"));
					pageParam.put("size", pageInfo.get("pageRows"));
					pageParam.put("_page", pageParam);
					PageResult biddingList = supplierBiddingService.findPagingBiddingNoticeList(pageParam);
					mv.addObject("_page", pageInfo);
					mv.addObject("biddingList", biddingList.get("content"));
				}
				if(StringUtils.isNotEmpty(locale) && "en_US".equals(locale)){
					mv.setViewName("login/sp/en/biddingNoticeListPopup_en");
				}else {
					mv.setViewName("login/sp/biddingNoticeListPopup");
				}
			}
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mv;
	}
}
