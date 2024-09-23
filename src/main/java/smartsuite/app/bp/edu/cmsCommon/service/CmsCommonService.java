package smartsuite.app.bp.edu.cmsCommon.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.edu.cmsCommon.event.CmsCommonEventPublisher;
import smartsuite.app.bp.edu.cmsCommon.repository.CmsCommonRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.module.ModuleManager;
import smartsuite.security.authentication.Auth;


import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 품목 속성 common 관련 service
 */
@Service
@Transactional
@SuppressWarnings({"rawtypes", "unchecked"})
public class CmsCommonService {

	@Inject
	CmsCommonRepository cmsCommonRepository;

	@Inject
	SharedService sharedService;

	@Inject
	CmsCommonEventPublisher cmsCommonEventPublisher;

	private String itemDoctorModule = "item-doctor";

	private String proModule = "pro";

	/**
	 * 속성 그룹 배정 속성, 사용 분류를 집계한다.
	 *
	 * @param
	 * @return void
	 */
	public void updateCountListItemAttributeGroup(List<Map<String, Object>> param){
		if(param.size() > 0) {
			this.updateCountInfoItemAttributeGroup(param.get(0));
		}
	}

	public void updateCountInfoItemAttributeGroup(Map<String, Object> param) {
		cmsCommonRepository.updateCountInfoItemAttributeGroup(param);
	}

	/**
	 * 배정된 속성을 조회(기본)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String,Object>> findListItemAsgnAttr(Map<String, Object> param) {
		List<Map<String, Object>> resultList = this.findListItemAsgnAttrByItemcat(param);
		return this.addInfoUomList(resultList);
	}

	/**
	 * 배정된 속성을 조회(itemcat_cd로)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListItemAsgnAttrByItemcat(Map<String, Object> param) {
		return cmsCommonRepository.findListItemAsgnAttrByItemcat(param);
	}

	/**
	 * 배정된 속성을 조회(event)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String,Object>> findListItemAsgnAttrFromItemMaster(Map<String, Object> param) {
		List<Map<String, Object>> resultList = this.findListItemAsgnAttrByItemIattr(param);
		return this.addInfoUomList(resultList);
	}

	/**
	 * 배정된 속성을 조회(item_iattr로)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListItemAsgnAttrByItemIattr(Map<String, Object> param) {
		return cmsCommonRepository.findListItemAsgnAttrByItemIattr(param);
	}


	/**
	 * 속성 목록 및 속성값, 단위코드값 함께 조회(itemcat_cd, item_reg_req_no)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListReqAsgnAttr(Map<String, Object> param) {
		return cmsCommonRepository.findListReqAsgnAttr(param);
	}

	/**
	 * 속성 목록 조회(itemcat_cd, item_cd)
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String,Object>> findListRegAttr(Map<String, Object> param) {
		return cmsCommonRepository.findListRegAttr(param);
	}

	/**
	 * uom list 추가
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> addInfoUomList(List<Map<String, Object>> resultList) {

		for(Map item : resultList){
			// 속성 유형이 UOM 이면 단위 콤보 리스트 조회
			if("Y".equals(item.get("iattr_uom_use_yn")) ){
				String unitGrpCd = (String) item.get("iattr_uom_cls_ccd");
				// 단위그룹코드 매핑(SMARTNINE-5887 단위코드 상세코드 변경 건)
				Map<String, Object> codeParam = Maps.newHashMap();
				codeParam.put("ccd", "MT010");
				codeParam.put("cstr_cnd_val", unitGrpCd);

				item.put(unitGrpCd, sharedService.findListCommonCodeAttributeValue(codeParam));
			}
		}

		return resultList;
	}

	/**
	 * 품목 등록 요청 복사에서 사용할 정보를 만든다
	 *
	 * @param
	 * @return the map
	 */
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
	 * Object null check
	 *
	 * @param
	 * @return the boolean
	 */
	public Boolean checkNullObject(Object obj) {
		return null == obj || "".equals(obj);
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

	/**
	 * 품목 등록 요청 and 품목 운영조직 정보 등록 요청 insert
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemRegReqWithOorg(Map<String, Object> reqInfo, List<Map<String, Object>> oorgList) {
		this.insertItemRegReq(reqInfo);
		this.insertItemOorgRegReq(reqInfo, oorgList);
	}

	/**
	 * 품목 등록 요청 정보 insert
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemRegReq(Map<String, Object> param) {
		cmsCommonRepository.insertItemRegReq(param);
	}

	/**
	 * 품목 운영조직 정보 등록 요청 insert
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemOorgRegReq(Map<String, Object> reqInfo, List<Map<String, Object>> oorgList) {
		Object itemRegReqNo = reqInfo.get("item_reg_req_no");
		Object itemCd = reqInfo.get("item_cd");

		for(Map<String, Object> oorgInfo : oorgList) {
			oorgInfo.put("item_reg_req_no", itemRegReqNo);
			oorgInfo.put("item_cd", itemCd);
			cmsCommonRepository.insertItemOorgRegReq(oorgInfo);
		}
	}

	/**
	 * 품목 등록 요청 and 품목 운영조직 정보 등록 요청 update
	 *
	 * @param
	 * @return the void
	 */
	public void updateItemRegReqWithOorg(Map<String, Object> reqInfo, List<Map<String, Object>> oorgList){
		this.updateItemRegReq(reqInfo);
		this.updateItemOorgRegReq(reqInfo, oorgList);
	}

	/**
	 * 품목 등록 요청 정보 update
	 *
	 * @param
	 * @return the void
	 */
	public void updateItemRegReq(Map<String, Object> param) {
		cmsCommonRepository.updateItemRegReq(param);
	}

	/**
	 * 품목 운영조직 정보 등록 요청 update
	 *
	 * @param
	 * @return the void
	 */
	public void updateItemOorgRegReq(Map<String, Object> reqInfo, List<Map<String, Object>> oorgList) {
		this.deleteAllItemOorgRegReq(reqInfo);
		this.insertItemOorgRegReq(reqInfo, oorgList);
	}

	/**
	 * 품목 운영조직 등록 요청 삭제 (전체)
	 *
	 * @param
	 * @return the void
	 */
	public void deleteAllItemOorgRegReq(Map<String, Object> param) {
		cmsCommonRepository.deleteAllItemOorgRegReq(param);
	}

	/**
	 * 품목 품목속성 등록요청을 지우고 다시 insert 한다.
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemIattrRegReqAfterDelete(Map<String, Object> paramMap, List<Map<String, Object>> paramList, Object str) {
		//기존 속성을 제거하고 다시 넣는다.
		this.deleteItemIattrRegReq(paramMap);

		for (Map<String, Object> row : paramList) {
			row.put("item_reg_req_no", str);
			//속성 값 추가
			this.insertItemIattrRegReq(row);
		}
	}

	/**
	 * 품목 품목속성 등록요청 정보를 지운다
	 *
	 * @param
	 * @return the void
	 */
	public void deleteItemIattrRegReq(Map<String, Object> param) {
		cmsCommonRepository.deleteItemIattrRegReq(param);
	}

	/**
	 * 품목 품목속성 등록요청 정보를 insert 한다.
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemIattrRegReq(Map<String, Object> param) {
		cmsCommonRepository.insertItemIattrRegReq(param);
	}

	/**
	 * 품목 품목속성 정보를 지우고 다시 insert 한다.
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemIattrAfterDelete(Map<String, Object> param, List<Map<String, Object>> list) {
		//폼목의 속성을 삭제한다.
		this.deleteItemIattr(param);

		for(Map<String, Object> row: list){
			// 속성 값에 품목 번호를 추가한다.
			row.put("item_cd", param.get("item_cd"));
			//속성 값 추가
			this.insertItemIattr(row);
		}
	}

	/**
	 * 품목 품목속성 정보를 지운다.
	 *
	 * @param
	 * @return the void
	 */
	public void deleteItemIattr(Map<String, Object> param) {
		cmsCommonRepository.deleteItemIattr(param);
	}

	/**
	 * 품목 품목속성 정보를 insert 한다.
	 *
	 * @param
	 * @return the void
	 */
	public void insertItemIattr(Map<String, Object> param) {
		cmsCommonRepository.insertItemIattr(param);
	}

	/**
	 * Item-doctor 연동 유사도 조회
	 *
	 * @param
	 * @return the resultmap
	 */
	public ResultMap findListItemSimilarity(Map param) {
		ModuleManager moduleManager = new ModuleManager();
		List<Map<String, Object>> resultList = Lists.newArrayList();
		if(moduleManager.exist(itemDoctorModule)) {
			resultList = cmsCommonEventPublisher.findListItemSimilarity(param);
		} else {
			return ResultMap.FAIL();
		}

		return ResultMap.SUCCESS(resultList);
	}

	/**
	 * 품목 운영조직 조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> getUserOorgList() {
		return sharedService.findListOperationOrganizationByUser("IO");
	}

	/**
	 * 품목 운영조직 초기화 세팅
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListInitItemRegReqOorg(Map<String, Object> param) {
		List<Map<String, Object>> oorgList = this.getUserOorgList();
		for(Map<String, Object> oorgInfo : oorgList) {
			oorgInfo.put("use_yn", "N");
			oorgInfo.put("tl_yn", "N");
			oorgInfo.put("qta_yn", "N");
		}
		return oorgList;
	}

	/**
	 * 품목 등록요청 운영조직 db조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListItemOorgRegReq(Map<String, Object> param) {
		return cmsCommonRepository.findListItemOorgRegReq(param);
	}

	/**
	 * 품목 등록 요청 운영조직 조합
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListItemRegReqOorg(Map<String, Object> param) {
		List<Map<String, Object>> oorgList = this.findListInitItemRegReqOorg(param);
		List<Map<String, Object>> itemOorgList = this.findListItemOorgRegReq(param);

		Object itemCd = param.get("item_cd");
		ModuleManager moduleManager = new ModuleManager();
		if(moduleManager.exist(proModule) && !Objects.isNull(itemCd)) {
			List<Map<String, Object>> bpaMultVdList = this.findListBpaMultVdByItemCdFromCms(param);
			itemOorgList = this.mergeBpaMultVdCmsList(itemOorgList, bpaMultVdList);
		}

		return this.mergeTlQtaCmsList(oorgList, itemOorgList);
	}

	/**
	 * bpa, 멀티협력사 여부 조회
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> findListBpaMultVdByItemCdFromCms(Map<String, Object> param) {
		return cmsCommonEventPublisher.findListBpaYnAndMutlVdYnByItemCdFromCms(param);
	}

	/**
	 * bpa, 멀티협력사 여부 조합
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> mergeBpaMultVdCmsList(List<Map<String, Object>> aList, List<Map<String, Object>> bList) {
		for(Map<String, Object> aInfo : aList) {
			for(Map<String, Object> bInfo : bList) {
				if(aInfo.get("oorg_cd").equals(bInfo.get("oorg_cd"))) {
					aInfo.put("bpa_yn", bInfo.get("bpa_yn"));
					aInfo.put("mult_vd_yn", bInfo.get("mult_vd_yn"));
				}
			}
		}

		return aList;
	}

	/**
	 * 품목 등록요청 운영조직 조합
	 *
	 * @param
	 * @return the list
	 */
	public List<Map<String, Object>> mergeTlQtaCmsList(List<Map<String, Object>> aList, List<Map<String, Object>> bList) {
		for(Map<String, Object> aInfo : aList) {
			for(Map<String, Object> bInfo : bList) {
				if(aInfo.get("oorg_cd").equals(bInfo.get("oorg_cd"))) {
					aInfo.put("use_yn", "Y");
					aInfo.put("tl_yn", bInfo.get("tl_yn"));
					aInfo.put("qta_yn", bInfo.get("qta_yn"));
					aInfo.put("bpa_yn", bInfo.get("bpa_yn"));
					aInfo.put("mult_vd_yn", bInfo.get("mult_vd_yn"));
				}
			}
		}

		return aList;
	}

}
