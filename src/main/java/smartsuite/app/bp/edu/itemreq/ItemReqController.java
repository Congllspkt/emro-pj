package smartsuite.app.bp.edu.itemreq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.edu.itemreq.service.ItemReqService;
import smartsuite.app.bp.edu.cmsCommon.service.CmsCommonService;

import smartsuite.app.common.shared.ResultMap;
import smartsuite.data.FloaterStream;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

/**
 * 품목 등록 요청 관련 controller
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/itemreq/**/")
public class ItemReqController {

	@Inject
	ItemReqService itemReqService;

	@Inject
	CmsCommonService cmsCommonService;


	/**
	 * 품목 등록 요청 조회
	 *
	 * @param
	 * @return the FloaterStream
	 */
	@RequestMapping(value = "findListItemReq.do")
	public @ResponseBody FloaterStream findListItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.findListItemReq(param);
	}

	/**
	 * 품목 등록 요청 삭제
	 *
	 * @param
	 * @return the resultmap
	 */
	@RequestMapping(value = "deleteListItemReq.do")
	public @ResponseBody ResultMap deleteListItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.deleteListItemReq(param);
	}

	/**
	 * 품목 등록 요청 상세
	 *
	 * @param
	 * @return the map
	 */
	@RequestMapping(value = "findInfoAllItemReq.do")
	public @ResponseBody Map<String, Object> findInfoAllItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.findInfoAllItemReq(param);
	}

	/**
	 * 품목 등록 요청 복사 데이터
	 *
	 * @param
	 * @return the map
	 */
	@RequestMapping(value = "findInfoCopyItemReq.do")
	public @ResponseBody Map<String, Object> findInfoCopyItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.findInfoCopyItemReq(param);
	}

	/**
	 * 품목 등록 요청 저장
	 *
	 * @param
	 * @return the resultmap
	 */
	@RequestMapping(value = "saveInfoItemReq.do")
	public @ResponseBody ResultMap saveInfoItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.saveInfoItemReq(param);
	}

	/**
	 * 품목 등록 변경 요청 조회
	 *
	 * @param
	 * @return the resultmap
	 */
	@RequestMapping(value = "findInfoModifyItemReq.do")
	public @ResponseBody Map<String, Object> findInfoModifyItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.findInfoModifyItemReq(param);
	}

	/**
	 * 품목 등록 요청 변경 저장
	 *
	 * @param
	 * @return the resultmap
	 */
	@RequestMapping(value = "saveInfoModifyItemReq.do")
	public @ResponseBody ResultMap saveInfoModifyItemReq(@RequestBody Map<String, Object> param) {
		return itemReqService.saveInfoItemReq(param);
	}

	/**
	 * Item-doctor 연동 유사도 조회
	 *
	 * @param
	 * @return the resultmap
	 */
	@RequestMapping(value = "findListItemSimilarity.do")
	public @ResponseBody ResultMap findListItemSimilarity(@RequestBody Map<String, Object> param) {
		return cmsCommonService.findListItemSimilarity(param);
	}
}