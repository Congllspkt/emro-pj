package smartsuite.app.bp.edu.itemreq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.edu.itemreq.repository.ItemReqRepository;
import smartsuite.app.common.message.MessageUtil;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.data.FloaterStream;
import smartsuite.security.authentication.Auth;

/**
 * 품목 등록 요청 관련 service
 */
@Service
@Transactional
@SuppressWarnings({"rawtypes","unchecked"})
public class ItemReqService {

	@Inject
	ItemReqRepository itemReqRepository;

	@Inject
	SharedService sharedService;
	
	@Inject
	MessageUtil messageUtil;

	/**
	 * 품목 등록 요청 조회
	 *
	 * @param
	 * @return the FloaterStream
	 */
	public FloaterStream findListItemReq(Map<String, Object> param) {
		return itemReqRepository.findListItemReq(param);
	}

	/**
	 * 품목 등록 요청 상세
	 *
	 * @param
	 * @return the map
	 */
	public Map<String, Object> findInfoAllItemReq(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
//		List<Map<String, Object>> asgnAttrList = this.findListItemAsgnAttrByItemIattrRegReq(param);

		resultMap.put("reqInfo", this.findInfoItemReq(param));
//		resultMap.put("oorgList", cmsCommonService.findListItemRegReqOorg(param));
//		resultMap.put("asgnAttrList", cmsCommonService.addInfoUomList(asgnAttrList));

		return resultMap;
	}

	/**
	 * 품목 등록 요청 상세 (테이블 정보)
	 *
	 * @param
	 * @return the map
	 */
	public Map<String, Object> findInfoItemReq(Map<String, Object> param) {
		return itemReqRepository.findInfoItemReq(param);
	}

	/**
	 * 품목 등록 요청 상세 (배정된 정보)
	 *
	 * @param
	 * @return the map
	 */
	public List<Map<String, Object>> findListItemAsgnAttrByItemIattrRegReq(Map<String, Object> param) {
		return itemReqRepository.findListItemAsgnAttrByItemIattrRegReq(param);
	}

	/**
	 * 품목 등록 요청 삭제
	 *
	 * @param
	 * @return the resultmap
	 */
	public ResultMap deleteListItemReq(Map<String, Object> param) {
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList", Lists.newArrayList());
		for(Map<String, Object> row : deleteList){
			this.deleteItemRegReq(row);
		}

		return ResultMap.SUCCESS();
	}


	/**
	 * 품목 등록 요청 삭제 (단건)
	 *
	 * @param
	 * @return the void
	 */
	public void deleteItemRegReq(Map<String, Object> param) {
		itemReqRepository.deleteItemRegReq(param);
	}

	/**
	 * 품목 등록 요청 복사에서 사용할 정보를 만든다
	 *
	 * @param
	 * @return the void
	 */
	public Map<String,Object> findInfoCopyItemReq(Map<String, Object> param) {
		Map<String, Object> itemInfo = itemReqRepository.findInfoItemReq(param);
		return this.setInitCopyItem(itemInfo);
	}

	/**
	 * 품목 등록 요청 저장
	 *
	 * @param
	 * @return the resultmap
	 */
	public ResultMap saveInfoItemReq(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();

		// 기본 정보
		Map<String, Object> reqInfo = (Map<String, Object>)param.getOrDefault("reqInfo", Maps.newHashMap());
		
		// 등록요청 번호
        Object itemRegReqNo = reqInfo.get("item_reg_req_no");

        // 표준코드가 기존에 사용되지않은 신규인경우
        boolean isNew = false;

		// 품목 코드가 존재하지 않으면 요청 유형을 신규 등록으로
        if(this.checkNullObject(reqInfo.get("item_cd"))) {
        	reqInfo.put("req_typ_ccd", "NEW");
        } else {
        	reqInfo.put("req_typ_ccd", "CHG");
        }

        //요청유형이 변경이면
        if(this.checkReqTypChg(reqInfo)) {
        	// 작성중이거나 승인요청중인 건이 있는지 체크
			resultMap = this.checkExistedItemRegReqWithResult(reqInfo);

			if(resultMap.isFail()){
				return ResultMap.FAIL(resultMap.getResultMessage());
        	}
        }

		// 상태 체크 (결재 요청중, 승인 인 경우 수정할 수 없다)
		resultMap = this.checkExistedApvdAndReqgWithResult(reqInfo);
		if(resultMap.isFail()) {
			return ResultMap.FAIL(resultMap.getResultMessage());
		}

		resultMap = this.setReqInfoWhenApvdReqgOrApvd(reqInfo, isNew);

		if(resultMap.isFail()) {
			return ResultMap.FAIL(resultMap.getResultMessage());
		} else {
			Map<String, Object> returnMap = resultMap.getResultData();
			reqInfo = (Map<String, Object>) returnMap.get("reqInfo");
			isNew = (Boolean) returnMap.get("is_new");
		}

		// reg_req_no로 채번만 되어 있는 상태인지 확인
		Map<String, Object> checkInfo = this.findInfoItemRegReq(reqInfo);
		//품목등록요청정보가 존재하지않으면
		if (checkInfo == null) {
			// 등록 요청 번호 채번
			if (this.checkNullObject(itemRegReqNo)) {
				itemRegReqNo = sharedService.generateDocumentNumber("RM");
				reqInfo.put("item_reg_req_no", itemRegReqNo);
			}

			// 품목마스터 등록요청 & 품목 운영 정보 등록 요청 저장
			//cmsCommonService.insertItemRegReqWithOorg(reqInfo, oorgList);
			this.insertItemRegReq(reqInfo);
		} else {
			//품목등록요청정보가 존재하면
			if (this.checkStsApvd(reqInfo)) {
				Object itemCd = reqInfo.get("item_cd");

				if(!isNew && this.checkNullObject(itemCd)) {
					// 작성중 상태에서 바로 등록시 itemCd 추가
					itemCd = sharedService.generateDocumentNumber("IC");

					reqInfo.put("item_cd", itemCd);
					reqInfo.put("req_item_cd", itemCd);
				}

				/*
				 * if (asgnAttrList.size() > 0) { // 속성 값에 품목 번호를 추가한다. for (Map attr :
				 * asgnAttrList) { attr.put("item_cd", itemCd); } }
				 */
			}

			//cmsCommonService.updateItemRegReqWithOorg(reqInfo, oorgList);
			this.updateItemRegReq(reqInfo);
		}
		//cmsCommonService.insertItemIattrRegReqAfterDelete(reqInfo, asgnAttrList, itemRegReqNo);

		Map returnMap = Maps.newHashMap();
		returnMap.put("item_reg_req_no", itemRegReqNo);
		return ResultMap.SUCCESS(returnMap);
	}

	/**
	 * 품목 등록 요청 저장
	 * 작성중이거나 승인요청중인 건이 있는지 체크
	 *
	 * @param
	 * @return the resultmap
	 */
	public ResultMap checkExistedItemRegReqWithResult(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();

		if(this.checkExistedItemRegReq(param)){
			resultMap.setResultMessage("STD.CMS1010");
			return ResultMap.FAIL(resultMap.getResultMessage());
		}

		return ResultMap.SUCCESS();
	}

	public ResultMap checkExistedApvdAndReqgWithResult(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();

		if(this.checkExistedApvdAndReqg(param)){
			resultMap.setResultMessage("STD.E9400");
			return ResultMap.FAIL(resultMap.getResultMessage());
		}

		return ResultMap.SUCCESS();
	}



	/**
	 * 품목 등록 요청 저장
	 * 작성중이거나 승인요청중인 건이 있는지 체크(count)
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkExistedItemRegReq(Map<String, Object> param){
		return itemReqRepository.checkExistedItemRegReq(param) > 0;
	}

	public Boolean checkExistedApvdAndReqg(Map<String, Object> param){
		return itemReqRepository.checkExistedApvdAndReqg(param) > 0;
	}

	/**
	 * 품목 등록 요청 저장
	 * 진행상태가 등록요청 or 승인 일 때 정보 setting
	 *
	 * @param
	 * @return the resultmap
	 */
	private ResultMap setReqInfoWhenApvdReqgOrApvd(Map reqInfo, Boolean isNew){
		ResultMap resultMap = ResultMap.getInstance();

		// 세션정보
		Map userInfo = Auth.getCurrentUserInfo();

		// 진행상태가 등록요청(APVL_REQG), 승인(APVD) 이면
		if (this.checkStsApvdReqg(reqInfo) || this.checkStsApvd(reqInfo)) {
			//표준품목마스터 정보 존재 여부 체크
			if (this.checkNullObject(reqInfo.get("mdl_no"))) {
				reqInfo.put("mdl_no", "");
			}

			if (this.checkNullObject(reqInfo.get("mfgr_nm"))) {
				reqInfo.put("mfgr_nm", "");
			}

			// 변경요청이 아닌 경우, 변경요청인 경우는 제외
			if (this.checkNullObject(reqInfo.get("item_chg_req_cont"))) {
				resultMap = this.checkItemMasterDuplicateWithResult(reqInfo);

				if (resultMap.isFail()) {
					return ResultMap.FAIL(resultMap.getResultMessage());
				} else {
					// Rule Engine 적용 : 구매담당자가 승인 시 프로세스 종료 여부
					// 등록요청(APVL_REQG)
					if (this.checkStsApvdReqg(reqInfo) && "true".equals(reqInfo.get("is_buyer"))
							&& "H".equals(((String) reqInfo.get("itemcat_lvl_1_cd")).substring(0, 1))) {
						isNew = true;
						reqInfo.put("item_reg_req_sts_ccd", "APVD"); // 승인
						reqInfo.put("apvr_id", userInfo.get("usr_id"));

						// 승인(APVD)
					} else if (this.checkStsApvd(reqInfo)) {
						isNew = true;
						reqInfo.put("apvr_id", userInfo.get("usr_id"));
					}
				}
			}
		}
		if(this.checkStsApvd(reqInfo)){
			Object itemCd = reqInfo.get("item_cd");

			if (isNew) {
				reqInfo = this.setNewItemCd(reqInfo, itemCd);
			}
		}

		Map<String, Object> returnMap = Maps.newHashMap();
		returnMap.put("reqInfo", reqInfo);
		returnMap.put("is_new", isNew);

		return ResultMap.SUCCESS(returnMap);
	}

	/**
	 * 품목 등록 요청 저장
	 * item code 채번 및 setting
	 *
	 * @param
	 * @return the resultmap
	 */
	private Map setNewItemCd(Map<String, Object> param, Object itemCd) {
		if(this.checkReqTypNew(param)){
			itemCd = sharedService.generateDocumentNumber("IC");
		}else{
			itemCd = param.get("req_item_cd");
		}

		param.put("item_cd", itemCd);
		param.put("req_item_cd", itemCd);

		return param;
	}

	/**
	 * 품목 등록 요청 정보 확인
	 *
	 * @param
	 * @return the map
	 */
	private Map<String, Object> findInfoItemRegReq(Map<String, Object> param){
		return itemReqRepository.findInfoItemRegReq(param);
	}

	/**
	 * 품목 등록 요청 변경 정보 조회
	 *
	 * @param
	 * @return the map
	 */
	public Map<String, Object> findInfoModifyItemReq(Map<String, Object> param){
		Map<String, Object> result = new HashMap<String, Object>();

		Map reqItem = this.findInfoItemReq(param);
		result.put("itemInfo", reqItem);

		if(reqItem != null && reqItem.get("itemcat_lvl_4_cd") != null){
			param.put("itemcat_cd"	, reqItem.get("itemcat_lvl_4_cd") );
			param.put("itemcat_lvl"	, "4" );
		}

		return result;
	}

	/**
	 * EVENT 진행중인 품목등록요청 데이터 확인
	 *
	 * @param
	 * @return int
	 */
	public int findCntProgressingItemRegReq(Map<String, Object> param) {
		return itemReqRepository.findCntProgressingItemRegReq(param);
	}

	/**
	 * EVENT 품목 변경 요청 저장
	 *
	 * @param
	 * @return resultmap
	 */
	public ResultMap saveInfoChangeItemReq(Map<String, Object> param) {
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> itemInfo = (Map<String, Object>) param.getOrDefault("itemInfo", Maps.newHashMap());
		Object itemRegReqNo = itemInfo.get("item_reg_req_no");

		itemInfo.put("req_typ_ccd", "CHG");

		resultMap = this.checkExistedItemRegReqWithResult(itemInfo);
		if(resultMap.isFail()) {
			return ResultMap.FAIL(resultMap.getResultMessage());
		}

		if(null == this.findInfoItemRegReq(itemInfo)) {
			// 등록 요청 번호 채번
			if(null == itemRegReqNo || "".equals(itemRegReqNo)) {
				itemRegReqNo = sharedService.generateDocumentNumber("RM");
				itemInfo.put("item_reg_req_no", itemRegReqNo);
			}
			this.insertItemRegReq(itemInfo);
		} else {
			this.updateItemRegReq(itemInfo);
		}

		resultMap.setResultData(itemInfo);
		return ResultMap.SUCCESS(resultMap.getResultData());
	}
	
	//nhatlt
	public ResultMap checkItemMasterDuplicateWithResult(Map param) {
		ResultMap resultMap = ResultMap.getInstance();

		Map item = this.checkItemMasterDuplicate(param);
		if((Integer)item.get("cnt") > 0) {
			String resultMsg = messageUtil.getCodeMessage("STD.N2101", item.get("item_cd"), null, LocaleContextHolder.getLocale());
			resultMap.setResultMessage(resultMsg);
			return resultMap.FAIL(resultMap.getResultMessage());
		}

		return ResultMap.SUCCESS(item);
	}
	
	public Map<String, Object> checkItemMasterDuplicate(Map param) {
		return itemReqRepository.checkItemMasterDuplicate(param);
	}
	
	public void insertItemRegReq(Map<String, Object> param) {
		itemReqRepository.insertItemRegReq(param);
	}
	
	public void updateItemRegReq(Map<String, Object> param) {
		itemReqRepository.updateItemRegReq(param);
	}
	
	public Boolean checkNullObject(Object obj) {
		return null == obj || "".equals(obj);
	}
	
	public Map<String, Object> setInitCopyItem(Map<String, Object> item) {
		// 복사 제외 값 초기화
		item.put("item_reg_req_no",			"");
		item.put("req_typ_ccd",				"");
		item.put("item_cd",					"");
		item.put("item_reg_req_sts_ccd",	"CRNG");
		item.put("athg_uuid",				"");
		item.put("img_athg_uuid",			"");
		item.put("apvr_id",					"");
		item.put("apvd_dttm",				null); //특정 DB Vendor에서 공백값으로 처리 시 이슈있음.
		item.put("item_reg_req_ret_rsn",	"");
		item.put("modr_id",					"");
		item.put("mod_dttm",				"");
		item.put("req_dttm",				"");
		item.put("reqr_id",					"");

		// 세션정보
		Map<String, Object> userInfo = Auth.getCurrentUserInfo();
		item.put("reqr_comp"	, userInfo.get("comp_nm")); 	// 회사명
		item.put("reqr_dept"	, userInfo.get("dept_nm")); 	// 부서명
		item.put("reqr_id"	, userInfo.get("usr_id")); 		// 사용자id
		item.put("reqr_nm"	, userInfo.get("usr_nm")); 		// 사용자명
		item.put("reqr_phone", userInfo.get("tel")); 	// 전화번호

		return item;
	}
	
	/**
	 * 상태가 등록요청(APVL_REQG) 인지 check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkStsApvdReqg(Map param) {
		return "APVL_REQG".equals(param.get("item_reg_req_sts_ccd"));
	}

	/**
	 * 상태가 승인(APVD) 인지 check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkStsApvd(Map param) {
		return "APVD".equals(param.get("item_reg_req_sts_ccd"));
	}

	/**
	 * 상태가 변경(CHG) 인지 check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkReqTypChg(Map param) {
		return "CHG".equals(param.get("req_typ_ccd"));
	}

	/**
	 * 상태가 신규(NEW) 인지 check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkReqTypNew(Map param) {
		return "NEW".equals(param.get("req_typ_ccd"));
	}
	
	
}
