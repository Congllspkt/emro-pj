package smartsuite.app.common.bid.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.bid.repository.BidTemplateRepository;
import smartsuite.app.event.CustomSpringEvent;
import smartsuite.exception.CommonException;

import javax.inject.Inject;
import java.util.*;

@Service
public class ProBidTemplateService {

	@Inject
	BidTemplateColumnService bidTemplateColumnService;

	@Inject
	BidTemplateSheetService bidTemplateSheetService;

	@Inject
	BidTemplateRepository bidTemplateRepository;

	@Inject
	ApplicationEventPublisher applicationEventPublisher;


	public Map<String, Object> findTemplateSheetColumnAndDataListForProProcess(Map<String, Object> param) {
		Map<String,Object> resultMap = Maps.newHashMap();

		List<Map<String, Object>> headerListMap = Lists.newArrayList();
		List<Map<String, Object>> headerGroupListMap = Lists.newArrayList();
		List<Map<String, Object>> getDataListMap = Lists.newArrayList();
		List<Map<String, Object>> headerDepthGroupListMap = Lists.newArrayList();
		List<Map<String, Object>> getSheetDataListMap = Lists.newArrayList();
		List<Map<String, Object>> sheetRowList = Lists.newArrayList();
		List<Map<String, Object>> sheetInfoList = Lists.newArrayList();

		Map<String,List<Map<String,Object>>> rowDataMap = new HashMap<String, List<Map<String, Object>>>();


		Map<String,Object> prData = param.get("prData") == null? new HashMap<String,Object>() : (Map<String,Object>)param.get("prData");


		String templSheetId = param.get("templ_sheet_id") == null ? "" : param.get("templ_sheet_id").toString();
		String prId = prData.get("pr_uuid") == null ? param.get("task_uuid") == null ? "" : param.get("task_uuid").toString() : prData.get("pr_uuid").toString();

		if(StringUtils.isNotEmpty(templSheetId)) {
			headerListMap = bidTemplateColumnService.findBidTemplateSheetColumnHeaders(param);
			headerGroupListMap = bidTemplateColumnService.findBidTemplateSheetGroupColumnHeaders(param);
			headerDepthGroupListMap = bidTemplateColumnService.findBidTemplateSheetHeaderDepthGroupColumnHeaders(param);
		}

		if(StringUtils.isNotEmpty(templSheetId)) {
			//APP_ID가 있을 경우 해당되는 데이터 값을 가져온다.
			if(StringUtils.isNotEmpty(prId)){
				Map<String,Object> setVendorSearchMap =  Maps.newHashMap();
				setVendorSearchMap.put("templ_sheet_id",templSheetId);
				String templId = param.get("templ_id") == null ? "" : param.get("templ_id").toString();

				if(StringUtils.isEmpty(templId)){
					Map<String,Object> getSheetInfo = bidTemplateSheetService.findBidTemplateSheetInfo(setVendorSearchMap);
					templId = getSheetInfo.get("templ_id") == null ? "" : getSheetInfo.get("templ_id").toString();
				}

				setVendorSearchMap.put("templ_id",templId);
				setVendorSearchMap.put("task_uuid",prId);

				sheetInfoList = bidTemplateSheetService.findListBidTemplateSheetForApp(setVendorSearchMap);
				if (sheetInfoList.size() > 0) {
					param.put("task_uuid",prId);
					getSheetDataListMap = this.findTemplateSheetColumnAndDataListForPro(param);
				}
			}else{
				sheetInfoList = bidTemplateSheetService.findListBidTemplateSheet(param);
				getSheetDataListMap = bidTemplateSheetService.findTemplateSheetColumnAndDataListForSheet(param);
			}
		}

		rowDataMap = bidTemplateSheetService.setRowDataGetRowDataInfo(getSheetDataListMap,rowDataMap);

		List<Map<String, Object>> dataList = Lists.newArrayList();
		if(StringUtils.isNotEmpty(templSheetId)){
			sheetRowList = bidTemplateSheetService.findBidTemplateSheetRowIdListForAppId(param);
			dataList = this.setSheetDataRow(sheetRowList,getDataListMap,rowDataMap,headerListMap);
		}

		resultMap.put("headerListMap",headerListMap);
		resultMap.put("headerGroupListMap",headerGroupListMap);
		resultMap.put("headerDepthGroupListMap",headerDepthGroupListMap);
		resultMap.put("dataListMap",dataList);
		resultMap.put("getTemplateAppInfoList",sheetInfoList);

		return resultMap;
	}

	private List<Map<String, Object>> setSheetDataRow(List<Map<String, Object>> sheetRowList, List<Map<String, Object>> getDataListMap, Map<String, List<Map<String, Object>>> rowDataMap, List<Map<String, Object>> headerListMap) {

		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(Map<String, Object> rowInfo : sheetRowList) {
			String rowId = rowInfo.get("sheet_data_row_id") == null ? "" : rowInfo.get("sheet_data_row_id").toString();

			getDataListMap = rowDataMap.get(rowId) == null? Lists.newArrayList() : rowDataMap.get(rowId);

			Map<String, Object> dataSetMap = Maps.newHashMap();
			for (Map<String, Object> headerMap : headerListMap) {
				dataSetMap.put("sheet_data_row_id", rowId);
				for (Map<String, Object> dataMap : getDataListMap) {
					String colId = headerMap.get("templ_sheet_col_id") == null ? "" : headerMap.get("templ_sheet_col_id").toString();
					String dataColId = dataMap.get("templ_sheet_col_id") == null ? "" : dataMap.get("templ_sheet_col_id").toString();

					// column의 id가 동일할 경우
					if (colId.equals(dataColId)) {
						String dataValue = dataMap.get("column_val") == null ? "" : dataMap.get("column_val").toString();
						String valueType = dataMap.get("val_type") == null ? "" : dataMap.get("val_type").toString();

						if (valueType.equals(BidTemplateService.COLUMN_VALUE_TYPE_INT)) {
							int intValue = Integer.parseInt(dataValue);
							dataSetMap.put(dataColId, intValue);
						} else {
							dataSetMap.put(dataColId, dataValue);
						}
					}
				}
			}
			dataList.add(dataSetMap);
		}
		return dataList;
	}

	public List<Map<String, Object>> findTemplateSheetColumnAndDataListForPro(Map<String, Object> param) {
		return bidTemplateRepository.findTemplateSheetColumnAndDataListForPro(param);
	}

	public List findListBidTemplateSheetForApp(Map<String, Object> param) {
		Map<String,Object> prData = param.get("prData") == null? new HashMap<String,Object>() : (Map<String,Object>)param.get("prData");
		String templId = param.get("templ_id") == null ? "" : param.get("templ_id").toString();
		String prId = prData.get("pr_uuid") == null ? param.get("pr_uuid") == null ? "" : param.get("pr_uuid").toString() : prData.get("pr_uuid").toString();
		String vendorTemplId = "";

		Map<String,Object> getAppTemplInfo = Maps.newHashMap();
		getAppTemplInfo.put("templ_id",templId);

		if(StringUtils.isEmpty(templId)){
			Map<String,Object> getPrInfoMap = Maps.newHashMap();
			Map<String,Object> setSelectMap = Maps.newHashMap();
			setSelectMap.put("pr_uuid",prId);

			//templ_id가 없을 경우 Relation 테이블부터 찾아야한다.
			if(StringUtils.isNotEmpty(prId)) getPrInfoMap = this.findPrEventPublisher(setSelectMap);


			vendorTemplId = getPrInfoMap.get("vendor_templ_id") == null ? "" : getPrInfoMap.get("vendor_templ_id").toString();

			//vendor templ id 가 존재할 경우 Template를 찾을수 있다.
			if(StringUtils.isNotEmpty(vendorTemplId)){
				getPrInfoMap.put("task_uuid",prId);
				getAppTemplInfo = bidTemplateSheetService.selectVendorTemplateAppByVendorTemplId(getPrInfoMap);
			}
		}

		if(StringUtils.isNotEmpty(vendorTemplId)){
			return bidTemplateSheetService.findListBidTemplateSheetForApp(getAppTemplInfo);
		}else{
			return bidTemplateSheetService.findListBidTemplateSheet(getAppTemplInfo);
		}
	}


	@Transactional
	public void saveBidTemplateSheetDataListForPro(Map<String, Object> param) {

		Map<String,Object> prData = param.get("prData") == null? Maps.newHashMap() : (Map<String,Object>)param.get("prData");
		if(prData == null) return;

		Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
		String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
		if (StringUtils.isEmpty(templSheetId)) throw new CommonException("시트의 정보를 찾을 수 없습니다. 다시 한 번 확인 해주세요.");

		List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("insertList");
		List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("updateList");
		String prUuid = prData.get("pr_uuid") == null? "" : (String) prData.get("pr_uuid");
		String templId = sheetInfo.get("templ_id") == null ? "" : sheetInfo.get("templ_id").toString();

		if (StringUtils.isEmpty(templId)) {
			Map<String,Object> getSheetInfo = bidTemplateSheetService.findBidTemplateSheetInfo(sheetInfo);
			templId = getSheetInfo.get("templ_id") == null ? "" : getSheetInfo.get("templ_id").toString();
		}

		//APP_ID가 있을 경우 해당되는 데이터 값을 가져온다.
		if(StringUtils.isNotEmpty(prUuid)){
			Map<String,Object> setVendorSearchMap =  Maps.newHashMap();
			String vdCd = param.get("vd_cd") == null ? "" : param.get("vd_cd").toString();
			setVendorSearchMap.put("templ_id",templId);
			setVendorSearchMap.put("task_uuid",prUuid);
			setVendorSearchMap.put("vd_cd",vdCd);

			if (!bidTemplateSheetService.existVendorTemplateAppCnt(setVendorSearchMap)) {
				this.insertBidTemplatePrData(setVendorSearchMap);
			}

			String templateSheetColumnId = sheetInfo.get("templ_sheet_col_id") == null ? "" : (String) sheetInfo.get("templ_sheet_col_id");


			// UPDATE
			bidTemplateSheetService.updateBidTemplateSheetData(updateList,templSheetId,templateSheetColumnId,prUuid);

			// INSERT
			List<Integer> insertIndex = param.get("insertIndex") == null? new ArrayList<Integer>() :(List<Integer>) param.get("insertIndex");
			bidTemplateSheetService.insertBidTemplateSheetData(insertList,insertIndex,templSheetId,templateSheetColumnId,prUuid);
		}
	}




	private void insertBidTemplatePrData(Map<String, Object> setVendorSearchMap) {
		String vendorTemplId = UUID.randomUUID().toString();
		setVendorSearchMap.put("vendor_templ_id",vendorTemplId);
		bidTemplateSheetService.insertBidTemplateAndAppIdRelation(setVendorSearchMap);
		bidTemplateSheetService.updatePrHeaderVendorTemplId(setVendorSearchMap);
	}


	public void deleteBidTemplateSheetDataListForPro(Map<String, Object> param) {
		List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
		Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
		Map<String,Object> prData = param.get("prData") == null? Maps.newHashMap() : (Map<String,Object>)param.get("prData");

		String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
		String appId = prData.get("pr_uuid") == null ? "" : prData.get("pr_uuid").toString();

		for(Map row :  deleteItem) {
			if (StringUtils.isEmpty(templSheetId)) continue;

			row.put("templ_sheet_id",templSheetId);
			row.put("task_uuid",appId);
			bidTemplateSheetService.deleteBidTemplateSheetDataListForAppId(row);
			bidTemplateSheetService.deleteBidTemplateSheetRowIdForAppId(row);
		}
	}


	public void deleteBidTemplateSyncAndDataByApp(Map<String, Object> param) {
		Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
		Map<String,Object> prData = param.get("prData") == null? Maps.newHashMap() : (Map<String,Object>)param.get("prData");

		Map<String,Object> getAppTemplInfo = Maps.newHashMap();
		Map<String,Object> getPrInfoMap = Maps.newHashMap();
		Map<String,Object> setSelectMap = Maps.newHashMap();

		String vendorTemplId = prData.get("vendor_templ_id") == null ? "" : prData.get("vendor_templ_id").toString();
		String templId = param.get("templ_id") == null ? "" : param.get("templ_id").toString();
		String prId = prData.get("pr_uuid") == null ? "" : prData.get("pr_uuid").toString();

		setSelectMap.put("pr_uuid",prId);
		setSelectMap.put("task_uuid",prId);
		setSelectMap.put("vendor_templ_id",vendorTemplId);
		setSelectMap.put("templ_id",templId);


		//templ_id가 없을 경우 Relation 테이블부터 찾아야한다.
		if(StringUtils.isNotEmpty(prId)){
			getPrInfoMap = this.findPrEventPublisher(setSelectMap);
			vendorTemplId = getPrInfoMap.get("vendor_templ_id") == null ? "" : getPrInfoMap.get("vendor_templ_id").toString();
		}

		setSelectMap.put("vendor_templ_id",vendorTemplId);

		//vendor templ id 가 존재할 경우 Template를 찾을수 있다.
		if(StringUtils.isNotEmpty(vendorTemplId)){
			getPrInfoMap.put("task_uuid",prId);
			getAppTemplInfo = bidTemplateSheetService.selectVendorTemplateAppByVendorTemplId(getPrInfoMap);
			setSelectMap.put("templ_id",getAppTemplInfo.get("templ_id"));
		}

		List<Map<String,Object>> getTemplateSheetList = bidTemplateSheetService.findListBidTemplateSheet(setSelectMap);

		for(Map<String,Object> getSheetInfo: getTemplateSheetList){
			String templSheetId = getSheetInfo.get("templ_sheet_id") == null ? "" : getSheetInfo.get("templ_sheet_id").toString();
			getSheetInfo.put("task_uuid",prId);
			if(StringUtils.isNotEmpty(templSheetId)){
				//ESBTRTD DELETE
				bidTemplateSheetService.deleteBidTemplateSheetDataForAppId(getSheetInfo);
				//ESBTRTR DELETE
				bidTemplateSheetService.deleteBidTemplateRowIdForAppId(getSheetInfo);
			}
		}
		//delete ESBTRAR
		bidTemplateSheetService.deleteBidTemplateSyncAppInfo(setSelectMap);
	}

	public Map<String, Object> findPrEventPublisher(Map<String, Object> setSelectMap) {
		CustomSpringEvent completeEvent = CustomSpringEvent.toCompleteEvent("findPr",setSelectMap);
		applicationEventPublisher.publishEvent(completeEvent);
		Map<String,Object> result = (Map<String,Object>) completeEvent.getResult();
		return result;
	}

}
