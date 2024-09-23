package smartsuite.app.common.bid.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.bid.repository.BidTemplateRepository;
import smartsuite.exception.CommonException;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class RfqTemplateService {

    @Inject
    BidTemplateColumnService bidTemplateColumnService;

    @Inject
    BidTemplateSheetService bidTemplateSheetService;

    @Inject
    ProBidTemplateService proBidTemplateService;

    @Inject
    BidTemplateRepository bidTemplateRepository;


    public Map<String, Object> findRFQTemplateSheetList(Map<String, Object> param) {
        List<Map<String, Object>> headerListMap = Lists.newArrayList();
        List<Map<String, Object>> headerGroupListMap = Lists.newArrayList();
        List<Map<String, Object>> getDataListMap = Lists.newArrayList();
        List<Map<String, Object>> getSheetDataListMap = Lists.newArrayList();
        List<Map<String, Object>> sheetRowList = Lists.newArrayList();
        List<Map<String, Object>> sheetInfoList = Lists.newArrayList();
        List<Map<String, Object>> headerDepthGroupListMap = Lists.newArrayList();

        Map<String,List<Map<String,Object>>> rowDataMap = new HashMap<String, List<Map<String, Object>>>();

        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        Map<String,Object> rfxQtaData = param.get("rfxQtaData") == null? Maps.newHashMap() : (Map<String,Object>)param.get("rfxQtaData");
        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
        String appId = sheetInfo.get("task_uuid") == null ? "" : sheetInfo.get("task_uuid").toString();
        String rfxId = rfxQtaData.get("rfx_uuid") == null ? "" : rfxQtaData.get("rfx_uuid").toString();

        Map<String,Object> setVendorSearchMap =  Maps.newHashMap();

        if(StringUtils.isNotEmpty(templSheetId)) {
            headerListMap = bidTemplateColumnService.findBidTemplateSheetColumnHeaders(sheetInfo);
            headerGroupListMap = bidTemplateColumnService.findBidTemplateSheetGroupColumnHeaders(sheetInfo);
            headerDepthGroupListMap = bidTemplateColumnService.findSheetMasterHeaderDepthGroupColumnHeaders(param);
        }

        setVendorSearchMap.put("templ_sheet_id",templSheetId);
        String templId = sheetInfo.get("templ_id") == null ? "" : sheetInfo.get("templ_id").toString();

        if(StringUtils.isEmpty(templId)){
            Map<String,Object> getSheetInfo = bidTemplateSheetService.findBidTemplateSheetInfo(setVendorSearchMap);
            templId = getSheetInfo.get("templ_id") == null ? "" : getSheetInfo.get("templ_id").toString();
        }

        setVendorSearchMap.put("templ_id",templId);
        setVendorSearchMap.put("task_uuid",appId);
        setVendorSearchMap.put("rfx_uuid",rfxId);


        sheetInfoList = bidTemplateSheetService.findListBidTemplateSheetForApp(setVendorSearchMap);

        if (sheetInfoList.size() > 0) {
            getSheetDataListMap = proBidTemplateService.findTemplateSheetColumnAndDataListForPro(setVendorSearchMap);
        }else{
            sheetInfoList = bidTemplateSheetService.findListBidTemplateSheet(setVendorSearchMap);
            getSheetDataListMap = bidTemplateSheetService.findTemplateSheetColumnAndDataListForSheet(setVendorSearchMap);
        }

        rowDataMap = bidTemplateSheetService.setRowDataGetRowDataInfo(getSheetDataListMap,rowDataMap);

        if(StringUtils.isNotEmpty(templSheetId)){
            sheetRowList = bidTemplateSheetService.findBidTemplateSheetRowIdListForAppId(setVendorSearchMap);
        }

        List<Map<String, Object>> dataList = Lists.newArrayList();
        for(Map<String, Object> rowInfo : sheetRowList) {
            String rowId = rowInfo.get("sheet_data_row_id") == null ? "" : rowInfo.get("sheet_data_row_id").toString();
            getDataListMap = rowDataMap.get(rowId) == null? Lists.newArrayList() : rowDataMap.get(rowId);
            Map<String, Object> dataSetMap = this.setDataListColumnForHeaderSort(headerListMap,rowId,getDataListMap);
            dataList.add(dataSetMap);
        }

        Map<String,Object> resultMap = Maps.newHashMap();
        resultMap.put("headerListMap",headerListMap);
        resultMap.put("headerGroupListMap",headerGroupListMap);
        resultMap.put("headerDepthGroupListMap",headerDepthGroupListMap);
        resultMap.put("dataListMap",dataList);
        resultMap.put("getTemplateAppInfoList",sheetInfoList);

        return resultMap;
    }

    private Map<String, Object> setDataListColumnForHeaderSort(List<Map<String, Object>> headerListMap, String rowId, List<Map<String, Object>> getDataListMap) {
        Map<String, Object> dataColumnInfo = Maps.newHashMap();

        for (Map<String,Object> headerMap : headerListMap) {
            dataColumnInfo.put("sheet_data_row_id", rowId);
            for (Map<String, Object> dataMap : getDataListMap) {
                String colId = headerMap.get("templ_sheet_col_id") == null ? "" : headerMap.get("templ_sheet_col_id").toString();
                String dataColId = dataMap.get("templ_sheet_col_id") == null ? "" : dataMap.get("templ_sheet_col_id").toString();

                // column의 id가 동일할 경우
                if (colId.equals(dataColId)) {
                    String dataValue = dataMap.get("column_val") == null ? "" : dataMap.get("column_val").toString();
                    String valueType = dataMap.get("val_type") == null ? "" : dataMap.get("val_type").toString();

                    if (valueType.equals(BidTemplateService.COLUMN_VALUE_TYPE_INT)) {
                        int intValue = Integer.parseInt(dataValue);
                        dataColumnInfo.put(dataColId, intValue);
                    } else {
                        dataColumnInfo.put(dataColId, dataValue);
                    }
                }
            }
        }
        return dataColumnInfo;
    }

    @Transactional
    public Map<String,Object> saveRFQTemplateSave(Map<String, Object> param) {
        Map<String, Object> setVendorSearchMap = Maps.newHashMap();

        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("updateList");
        List<Integer> insertIndex = param.get("insertIndex") == null? new ArrayList<Integer>() :(List<Integer>) param.get("insertIndex");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        Map<String,Object> rfxQtaItemInfo = param.get("rfxQtaItemInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("rfxQtaItemInfo");

        if(rfxQtaItemInfo == null) return sheetInfo;

        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
        String rfxItemId = rfxQtaItemInfo.get("rfx_item_id") == null ? "" : rfxQtaItemInfo.get("rfx_item_id").toString();
        String templId = sheetInfo.get("templ_id") == null ? "" : sheetInfo.get("templ_id").toString();

        if (StringUtils.isEmpty(templSheetId)) {
            throw new CommonException("시트의 정보를 찾을 수 없습니다. 다시 한 번 확인 해주세요.");
        }

        if (StringUtils.isEmpty(templId)) {
            Map<String,Object> getSheetInfo = bidTemplateSheetService.findBidTemplateSheetInfo(sheetInfo);
            templId = getSheetInfo.get("templ_id") == null ? "" : getSheetInfo.get("templ_id").toString();
        }

        //APP_ID가 있을 경우 해당되는 데이터 값을 가져온다.
        if(StringUtils.isNotEmpty(rfxItemId)){
            String vdCd = param.get("vd_cd") == null ? "" : param.get("vd_cd").toString();
            setVendorSearchMap.put("templ_id",templId);
            setVendorSearchMap.put("task_uuid",rfxItemId);
            setVendorSearchMap.put("vd_cd",vdCd);

            if (!bidTemplateSheetService.existVendorTemplateAppCnt(setVendorSearchMap)) {
                this.insertBidTemplateRFQData(setVendorSearchMap);
            }

            // UPDATE
            String templateSheetColumnId = sheetInfo.get("templ_sheet_col_id") == null ? "" : (String) sheetInfo.get("templ_sheet_col_id");
            bidTemplateSheetService.updateBidTemplateSheetData(updateList,templSheetId,templateSheetColumnId,rfxItemId);

            // INSERT
            bidTemplateSheetService.insertBidTemplateSheetData(insertList,insertIndex,templSheetId,templateSheetColumnId,rfxItemId);
            sheetInfo.put("templ_id",templId);
            sheetInfo.put("task_uuid",rfxItemId);
            sheetInfo.put("vendor_templ_id",setVendorSearchMap.get("vendor_templ_id"));
        }
        return sheetInfo;
    }

    private void insertBidTemplateRFQData(Map<String, Object> setVendorSearchMap) {
        String vendorTemplId = UUID.randomUUID().toString();
        setVendorSearchMap.put("vendor_templ_id",vendorTemplId);
        bidTemplateSheetService.insertBidTemplateAndAppIdRelation(setVendorSearchMap);
        this.updateRFQItemVendorTemplId(setVendorSearchMap);
    }

    private void updateRFQItemVendorTemplId(Map<String, Object> setVendorSearchMap) {
        bidTemplateRepository.updateRFQItemVendorTemplId(setVendorSearchMap);
    }


    public void deleteRFQTemplate(Map<String, Object> param) {
        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        Map<String,Object> rfxQtaItemInfo = param.get("rfxQtaItemInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("rfxQtaItemInfo");

        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
        String appId = rfxQtaItemInfo.get("rfx_item_id") == null ? "" : rfxQtaItemInfo.get("rfx_item_id").toString();

        for(Map row :  deleteItem) {
            if (StringUtils.isEmpty(templSheetId)) continue;

            row.put("templ_sheet_id",templSheetId);
            row.put("task_uuid",appId);
            bidTemplateSheetService.deleteBidTemplateSheetDataListForAppId(row);
            bidTemplateSheetService.deleteBidTemplateSheetRowIdForAppId(row);
        }
    }

    public void deleteRFQTemplateSheetAll(Map<String, Object> param) {

        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        Map<String,Object> rfxQtaItemInfo = param.get("rfxQtaItemInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("rfxQtaItemInfo");

        Map<String,Object> getAppTemplInfo = Maps.newHashMap();
        Map<String,Object> setSelectMap = Maps.newHashMap();


        String vendorTemplId = sheetInfo.get("vendor_templ_id") == null ? "" : sheetInfo.get("vendor_templ_id").toString();
        String templId = sheetInfo.get("templ_id") == null ? "" : sheetInfo.get("templ_id").toString();
        String appId = rfxQtaItemInfo.get("rfx_item_id") == null ? "" : rfxQtaItemInfo.get("rfx_item_id").toString();

        setSelectMap.put("rfx_item_id",appId);
        setSelectMap.put("task_uuid",appId);
        setSelectMap.put("vendor_templ_id",vendorTemplId);
        setSelectMap.put("templ_id",templId);


        //templ_id가 없을 경우 Relation 테이블부터 찾아야한다.
        if(StringUtils.isNotEmpty(appId)){
            vendorTemplId = this.findRFQItemVendorTemplId(setSelectMap);
        }

        setSelectMap.put("vendor_templ_id",vendorTemplId);

        //vendor templ id 가 존재할 경우 Template를 찾을수 있다.
        if(StringUtils.isNotEmpty(vendorTemplId)){
            getAppTemplInfo = bidTemplateSheetService.selectVendorTemplateAppByVendorTemplId(setSelectMap);
            setSelectMap.put("templ_id",getAppTemplInfo.get("templ_id"));
        }

        List<Map<String,Object>> getTemplateSheetList = bidTemplateSheetService.findListBidTemplateSheet(setSelectMap);

        for(Map<String,Object> getSheetInfo: getTemplateSheetList){
            String templSheetId = getSheetInfo.get("templ_sheet_id") == null ? "" : getSheetInfo.get("templ_sheet_id").toString();
            getSheetInfo.put("task_uuid",appId);
            if(StringUtils.isNotEmpty(templSheetId)){
                //ESBTRTD DELETE
                bidTemplateSheetService.deleteBidTemplateSheetDataForAppId(getSheetInfo);

                //ESBTRTR DELETE
                bidTemplateSheetService.deleteBidTemplateRowIdForAppId(getSheetInfo);
            }
        }

        //delete ESBTRAR
        bidTemplateSheetService.deleteBidTemplateSyncAppInfo(setSelectMap);
        bidTemplateSheetService.updateVendorTemplIdNull(setSelectMap);
    }

    private String findRFQItemVendorTemplId(Map<String, Object> setSelectMap) {
        return bidTemplateRepository.findRFQItemVendorTemplId(setSelectMap);
    }


    public List findListRFQTemplateList(Map<String, Object> param) {
        Map<String,Object> prData = param.get("prData") == null? new HashMap<String,Object>() : (Map<String,Object>)param.get("prData");

        String templId = param.get("templ_id") == null ? "" : param.get("templ_id").toString();
        String rfxItemId = prData.get("rfx_item_id") == null ? param.get("rfx_item_id") == null ? "" : param.get("rfx_item_id").toString() : prData.get("rfx_item_id").toString();
        String vendorTemplId =  param.get("vendor_templ_id") == null ? "" : param.get("vendor_templ_id").toString();

        Map<String,Object> getAppTemplInfo = Maps.newHashMap();
        getAppTemplInfo.put("templ_id",templId);

        if(StringUtils.isEmpty(templId)){
            Map<String,Object> getPrInfoMap = Maps.newHashMap();

            //vendor templ id 가 존재할 경우 Template를 찾을수 있다.
            if(StringUtils.isNotEmpty(vendorTemplId)){
                getPrInfoMap.put("task_uuid",rfxItemId);
                getPrInfoMap.put("vendor_templ_id",vendorTemplId);
                getAppTemplInfo = bidTemplateSheetService.selectVendorTemplateAppByVendorTemplId(getPrInfoMap);
            }
        }

        if(StringUtils.isNotEmpty(vendorTemplId)){
            return bidTemplateSheetService.findListBidTemplateSheetForApp(getAppTemplInfo);
        }else{
            return bidTemplateSheetService.findListBidTemplateSheet(getAppTemplInfo);
        }
    }


    public void deleteRFQTemplateSheetData(Map<String, Object> param) {
        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        Map<String,Object> rfxQtaItemInfo = param.get("rfxQtaItemInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("rfxQtaItemInfo");
        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
        String appId = rfxQtaItemInfo.get("rfx_item_id") == null ? "" : rfxQtaItemInfo.get("rfx_item_id").toString();

        for(Map row :  deleteItem) {
            if (StringUtils.isEmpty(templSheetId)) continue;
            row.put("templ_sheet_id",templSheetId);
            row.put("task_uuid",appId);
            bidTemplateSheetService.deleteBidTemplateSheetDataListForAppId(row);
            bidTemplateSheetService.deleteBidTemplateSheetRowIdForAppId(row);
        }
    }




    public Map<String, Object> findCalculateColumnAndSum(Map<String, Object> param) {
        Map<String,Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> items = param.get("items") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("items");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");


        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();
        String rfxItemId = sheetInfo.get("task_uuid") == null ? "" : sheetInfo.get("task_uuid").toString();

        if (StringUtils.isEmpty(templSheetId)) {
            throw new CommonException("시트의 정보를 찾을 수 없습니다. 다시 한 번 확인 해주세요.");
        }
        BigDecimal sumValue = new BigDecimal(0);

        List<Map<String,Object>> calcHeadersColumns = bidTemplateColumnService.findColTypeCalcHeadersColumns(sheetInfo);

        for(Map<String,Object> calcInfo : calcHeadersColumns){
            String calcVal = calcInfo.get("calculate_val") == null? "" : calcInfo.get("calculate_val").toString();
            String templateSheetHeaderColId = calcInfo.get("templ_sheet_col_id") == null? "" : calcInfo.get("templ_sheet_col_id").toString();

            for (int a=0; a<items.size(); a ++) {
                Map<String,Object> itemMap = items.get(a);

                String caclString = calcVal;
                if (StringUtils.isNotEmpty(templSheetId)) {
                    Iterator<String> insertSheetDataId = itemMap.keySet().iterator();
                    //insert로 넘어오는
                    while(insertSheetDataId.hasNext()){
                        Map<String,Object> sheetDataInfo = Maps.newHashMap();

                        String key = insertSheetDataId.next();
                        String value = String.valueOf(itemMap.get(key));

                        //key가 value에 있을 경우만 처리
                        if(calcVal.indexOf(key) > -1){
                            caclString = caclString.replace("[" + key +"]" , value);
                        }

                    }
                }

                //계산 처리
                final BigDecimal resultCalcValue = this.executeCalculus(caclString);
                itemMap.put(templateSheetHeaderColId,resultCalcValue); //계산 결과 삽입.
                itemMap.put("templ_sheet_id",templSheetId);

                //최종 sum 계산
                if(null != resultCalcValue && resultCalcValue.intValue() > 0) sumValue = sumValue.add(resultCalcValue);

                Map<String,Object> rowInfo = bidTemplateSheetService.findBidTemplateSheetRowInfo(itemMap);
                String rowNum = rowInfo.get("row_num") == null? "0":rowInfo.get("row_num").toString();
                itemMap.put("row_index",rowNum);

            }

        }


        resultMap.put("rfqItems",items);
        resultMap.put("calculateSumValue",sumValue);
        resultMap.put("rfxItemId",rfxItemId);

        return resultMap;
    }


    /**
     * 계산식을 수행하고 결과를 반환한다.
     *
     * @param str the str
     * @return the big decimal
     */
    private BigDecimal executeCalculus(final String str){
        // 계산식 수행 계산 결과 반환
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("JavaScript");
        BigDecimal retVal = null;

        if(StringUtils.isNotEmpty(str)){
            // 계산식 구분자 제거
            final String calcStr = str.replaceAll("\\^", "");

            try{
                if(calcStr.isEmpty()){
                    retVal = null;
                }else{
                    retVal = new BigDecimal(String.valueOf(engine.eval(calcStr)));
                }
            }catch(ScriptException scrptEx){
                // 계산식 수행 오류 발생시 결과값 무실적(null) 처리
                retVal = null;
            }
        }

        return retVal;
    }

    public List findRFQTemplateConfirmYn(Map<String, Object> param) {
        return bidTemplateRepository.findRFQTemplateConfirmYn(param);
    }

}
