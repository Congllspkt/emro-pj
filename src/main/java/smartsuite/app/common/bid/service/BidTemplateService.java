package smartsuite.app.common.bid.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.bid.repository.BidTemplateRepository;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class BidTemplateService {


    public static final String COLUMN_VALUE_TYPE_STRING = "STRING";
    public static final String COLUMN_VALUE_TYPE_INT = "INT";
    public static final String COLUMN_VALUE_TYPE_DATE = "DATE";

    @Inject
    BidTemplateSheetService bidTemplateSheetService;

    @Inject
    BidTemplateColumnService bidTemplateColumnService;

    @Inject
    BidTemplateRepository bidTemplateRepository;

    public List findListBidTemplateMaster(Map<String, Object> param) {
        return bidTemplateRepository.findListBidTemplateMaster(param);
    }
    public List findListConfirmBidTemplate(Map<String, Object> param) {
        return bidTemplateRepository.findListConfirmBidTemplate(param);
    }
    public List findListBidTemplateConfirm(Map<String, Object> param) {
        return bidTemplateRepository.findListBidTemplateConfirm(param);
    }

    public void saveListBidTemplateMaster(Map<String, Object> param) {
        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList():(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList():(List<Map<String, Object>>) param.get("updateList");
        // UPDATE
        this.updateListBidTemplateInfo(updateList);
        // INSERT
        this.insertListBidTemplateInfo(insertList);
    }

    private void insertListBidTemplateInfo(List<Map<String, Object>> insertList) {
        for (Map row : insertList) {
            if (this.existSheetMasterInfo(row)) {
                throw new CommonException(ErrorCode.DUPLICATED);
            } else {
                row.put("templ_cd", bidTemplateSheetService.generateBidDocumentNumber());
                this.insertBidTemplateInfo(row);
            }
        }
    }

    private void updateListBidTemplateInfo(List<Map<String, Object>> updateList) {
        for (Map row : updateList) {
            if (this.existSheetMasterInfo(row)) {
                this.updateBidTemplateInfo(row);
            } else {
                row.put("templ_cd", bidTemplateSheetService.generateBidDocumentNumber());
                this.insertBidTemplateInfo(row);
            }
        }
    }

    private void insertBidTemplateInfo(Map row) {
        bidTemplateRepository.insertBidTemplateInfo(row);
    }

    private void updateBidTemplateInfo(Map row) {
        bidTemplateRepository.updateBidTemplateInfo(row);
    }

    private boolean existSheetMasterInfo(Map row) {
        return (bidTemplateRepository.selectBidTemplateSheetInfo(row) > 0);
    }


    @Transactional
    public void deleteListBidTemplateMaster(Map<String, Object> param) {

        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        for(Map row :  deleteItem) {
            List<Map<String,Object>> getTemplateSheetList = bidTemplateSheetService.findListBidTemplateSheet(row);

            for(Map<String,Object> sheetInfo: getTemplateSheetList){
                String templSheetId = sheetInfo.get("templ_sheet_id") == null ? UUID.randomUUID().toString() : sheetInfo.get("templ_sheet_id").toString();

                if(!StringUtils.isEmpty(templSheetId)){
                    if(this.existVendorTemplateAppCntForTemplateId(sheetInfo)) {
                        throw new CommonException("해당 Template는 업무에서 사용 중인 Template이기 때문에 삭제가 불가능합니다. \r\n BidTemplate Code : "+sheetInfo.get("templ_cd"));
                    }
                    bidTemplateSheetService.deleteTemplateSheetData(sheetInfo);
                }
            }
            this.deleteListBidTemplateMaster(row);
    }
}

    private boolean existVendorTemplateAppCntForTemplateId(Map<String, Object> sheetInfo) {
        return (bidTemplateRepository.selectVendorTemplateAppCntForTemplateId(sheetInfo) > 0);
    }


    public void saveBidTemplateSheetColumns(Map<String, Object> param) {
        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("updateList");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");

        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? UUID.randomUUID().toString() : sheetInfo.get("templ_sheet_id").toString();

        if (StringUtils.isNotEmpty(templSheetId)) {
            sheetInfo.put("templ_sheet_id",templSheetId);
            if (!this.existBidTemplateSheet(sheetInfo)) {
                //시트 추가
                bidTemplateSheetService.insertBidTemplateSheet(sheetInfo);
            }else{
                //시트 update
                bidTemplateSheetService.updateBidTemplateSheetInfo(sheetInfo);
            }
        }


        // UPDATE
        for (Map row : updateList) {
            if (StringUtils.isEmpty(templSheetId)) continue;

            if (this.existCheckFindColumnInfo(row)) {
                bidTemplateColumnService.updateBidTemplateSheetColumns(row);
            }
        }
        // INSERT
        for (Map row : insertList) {
            row.put("templ_sheet_id",templSheetId);
            if (StringUtils.isNotEmpty(templSheetId)) {
                String displyColNm = row.get("disp_nm") == null? ( row.get("col_nm") == null ? "" : row.get("col_nm").toString()) : row.get("disp_nm").toString();
                row.put("disp_nm",displyColNm);
                bidTemplateColumnService.insertBidTemplateSheetColumns(row);
            }
        }
    }

    private boolean existCheckFindColumnInfo(Map row) {
        return bidTemplateRepository.updateCheckFindColumnInfo(row) > 0;
    }

    public boolean existBidTemplateSheet(Map<String, Object> sheetInfo) {
        return bidTemplateRepository.findBidTemplateSheetCnt(sheetInfo) > 0;
    }

    public void deleteBidTemplateSheetColumns(Map<String, Object> param) {
        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();

        for(Map row :  deleteItem) {
            row.put("templ_sheet_id",templSheetId);
            if (StringUtils.isNotEmpty(templSheetId)) {
                bidTemplateColumnService.deleteBidTemplateSheetColumns(row);
            }
        }
    }

    public Map<String, Object> findTemplateSheetColumnAndDataList(Map<String, Object> param) {
        Map<String,Object> resultMap = Maps.newHashMap();

        List<Map<String, Object>> headerListMap = Lists.newArrayList();
        List<Map<String, Object>> headerGroupListMap = Lists.newArrayList();
        List<Map<String, Object>> headerDepthGroupListMap = Lists.newArrayList();
        List<Map<String, Object>> getSheetDataListMap = Lists.newArrayList();
        List<Map<String, Object>> sheetRowList = Lists.newArrayList();
        List<Map<String, Object>> dataList = Lists.newArrayList();

        Map<String,List<Map<String,Object>>> rowDataMap = new HashMap<String, List<Map<String, Object>>>();

        String templSheetId = param.get("templ_sheet_id") == null ? "" : param.get("templ_sheet_id").toString();

        if(StringUtils.isNotEmpty(templSheetId)) {
            headerListMap = bidTemplateColumnService.findBidTemplateSheetColumnHeaders(param);
            headerGroupListMap = bidTemplateColumnService.findBidTemplateSheetGroupColumnHeaders(param);
            headerDepthGroupListMap = bidTemplateColumnService.findBidTemplateSheetHeaderDepthGroupColumnHeaders(param);
        }

        if(StringUtils.isNotEmpty(templSheetId)) {
            getSheetDataListMap = bidTemplateSheetService.findTemplateSheetColumnAndDataListForSheet(param);
        }

        rowDataMap = this.getSheetDataList(getSheetDataListMap,rowDataMap);

        if(StringUtils.isNotEmpty(templSheetId)) sheetRowList = bidTemplateSheetService.findBidTemplateSheetRowIdList(param);

        for(Map<String, Object> rowInfo : sheetRowList) {
            String rowId = rowInfo.get("sheet_data_row_id") == null ? "" : rowInfo.get("sheet_data_row_id").toString();
            String confirmYn = rowInfo.get("confirm_yn") == null ? "" : rowInfo.get("confirm_yn").toString();
            resultMap.put("confirmYn",confirmYn);
            dataList.add(this.findHeaderData(headerListMap,rowId,rowDataMap));
        }

        resultMap.put("headerListMap",headerListMap);
        resultMap.put("headerGroupListMap",headerGroupListMap);
        resultMap.put("headerDepthGroupListMap",headerDepthGroupListMap);
        resultMap.put("dataListMap",dataList);

        return resultMap;
    }

    private Map<String, Object> findHeaderData(List<Map<String, Object>> headerListMap, String rowId, Map<String, List<Map<String, Object>>> rowDataMap) {
        int headerSize = headerListMap.size();

        Map<String,Object> dataSetMap = Maps.newHashMap();
        List<Map<String, Object>> getDataListMap = Lists.newArrayList();
        getDataListMap = rowDataMap.get(rowId) == null? Lists.newArrayList() : rowDataMap.get(rowId);

        for (int i = 0; i < headerSize; i++) {

            Map<String, Object> headerMap = headerListMap.get(i);
            dataSetMap.put("sheet_data_row_id", rowId);
            for (Map<String, Object> dataMap : getDataListMap) {

                String colId = headerMap.get("templ_sheet_col_id") == null ? "" : headerMap.get("templ_sheet_col_id").toString();
                String dataColId = dataMap.get("templ_sheet_col_id") == null ? "" : dataMap.get("templ_sheet_col_id").toString();

                // column의 id가 동일할 경우
                if (colId.equals(dataColId)) {

                    String colCd = headerMap.get("col_cd") == null ? "" : headerMap.get("col_cd").toString();
                    //String sheet_data_id = dataMap.get("sheet_data_id") == null ? "" : dataMap.get("sheet_data_id").toString();
                    String dataValue = dataMap.get("column_val") == null ? "" : dataMap.get("column_val").toString();
                    String valueType = dataMap.get("val_type") == null ? "" : dataMap.get("val_type").toString();

                    if (valueType.equals(COLUMN_VALUE_TYPE_INT)) {
                        int intValue = Integer.parseInt(dataValue);
                        dataSetMap.put(dataColId, intValue);
                    } else {
                        dataSetMap.put(dataColId, dataValue);
                    }
                }
            }
        }
        return dataSetMap;
    }

    private Map<String, List<Map<String, Object>>> getSheetDataList(List<Map<String, Object>> getSheetDataListMap, Map<String, List<Map<String, Object>>> rowDataMap) {
        for(Map<String,Object> getData : getSheetDataListMap){

            String rowId =  getData.get("sheet_data_row_id") == null ? "" : getData.get("sheet_data_row_id").toString();

            List<Map<String,Object>> getRowDataList = rowDataMap.get(rowId) == null? Lists.newArrayList() :  rowDataMap.get(rowId);

            getRowDataList.add(getData);

            rowDataMap.put(rowId,getRowDataList);
        }
        return rowDataMap;
    }

    public void saveBidTemplateSheetDataList(Map<String, Object> param) {



        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("updateList");
        List<Integer> insertIndex = param.get("insertIndex") == null? new ArrayList<Integer>() :(List<Integer>) param.get("insertIndex");
        List<Integer> updateIndex = param.get("updateIndex") == null? new ArrayList<Integer>() :(List<Integer>) param.get("updateIndex");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");


        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();

        if (StringUtils.isEmpty(templSheetId)) {
            throw new CommonException("시트의 정보를 찾을 수 없습니다. 다시 한 번 확인 해주세요.");
        }


        // UPDATE
        for (int i=0; i<updateList.size(); i++) {
            Map<String,Object> updateMap = updateList.get(i);
            Integer index = updateIndex.get(i);

            if (StringUtils.isEmpty(templSheetId)) continue;

            updateMap.put("templ_sheet_col_id",sheetInfo.get("templ_sheet_col_id"));

            Iterator<String> updateSheetDataId = updateMap.keySet().iterator();

            //update로 넘어오는
            while(updateSheetDataId.hasNext()){
                Map<String,Object> sheetDataInfo = Maps.newHashMap();

                String key = updateSheetDataId.next();
                String value = String.valueOf(updateMap.get(key));

                sheetDataInfo.put("column_val",value);
                sheetDataInfo.put("templ_sheet_col_id",key);
                sheetDataInfo.put("sheet_data_row_id",updateMap.get("sheet_data_row_id"));
                sheetDataInfo.put("templ_sheet_id",sheetInfo.get("templ_sheet_id"));

                if (!this.existColumnData(sheetDataInfo)) {
                    bidTemplateSheetService.insertSheetDataInfo(sheetDataInfo);
                }else {
                    bidTemplateColumnService.updateSheetDataInfo(sheetDataInfo);
                }
            }
        }
        // INSERT
        for (int a=0; a<insertList.size(); a ++) {
            Map<String,Object> insertMap = insertList.get(a);
            Integer index = insertIndex.get(a);

            if (StringUtils.isNotEmpty(templSheetId)) {
                String sheetDataRowId = UUID.randomUUID().toString();

                Map<String,Object> rowInfo = Maps.newHashMap();

                rowInfo.put("sheet_data_row_id",sheetDataRowId); //insert로 넘어온 값에, sheet_data_row_id를 넣어준다.
                rowInfo.put("templ_sheet_id",sheetInfo.get("templ_sheet_id"));
                rowInfo.put("row_num",index);

                //row relation table insert
                bidTemplateSheetService.insertSheetRowInfo(rowInfo);

                Iterator<String> insertSheetDataId = insertMap.keySet().iterator();

                //insert로 넘어오는
                while(insertSheetDataId.hasNext()){
                    Map<String,Object> sheetDataInfo = Maps.newHashMap();

                    String key = insertSheetDataId.next();
                    String value = String.valueOf(insertMap.get(key));

                    sheetDataInfo.put("column_val",value);
                    sheetDataInfo.put("sheet_data_row_id",sheetDataRowId); //insert로 넘어온 값에, sheet_data_row_id를 넣어준다.
                    sheetDataInfo.put("templ_sheet_col_id",key);
                    sheetDataInfo.put("templ_sheet_id",sheetInfo.get("templ_sheet_id"));
                    bidTemplateSheetService.insertSheetDataInfo(sheetDataInfo);
                }

            }
        }
    }

    private boolean existColumnData(Map<String, Object> sheetDataInfo) {
        return bidTemplateRepository.selectColumnDataCnt(sheetDataInfo) > 0;
    }


    public List<Map<String, Object>> findCalculateColumn(Map<String, Object> param) {

        List<Map<String, Object>> items = param.get("items") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("items");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();

        if (StringUtils.isEmpty(templSheetId)) {
            throw new CommonException("시트의 정보를 찾을 수 없습니다. 다시 한 번 확인 해주세요.");
        }

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

                Map<String,Object> rowInfo = bidTemplateSheetService.findBidTemplateSheetRowInfo(itemMap);
                String rowNum = rowInfo.get("row_num") == null? "0":rowInfo.get("row_num").toString();
                itemMap.put("row_index",rowNum);
            }
        }
        return items;
    }


    public Map findBidTemplateSheetColumnInfo(Map<String, Object> param) {

        Map<String,Object> data = Maps.newHashMap();

        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() :  (Map<String,Object>) param.get("sheetInfo");
        String templSheetColId = param.get("templ_sheet_col_id") == null? "" :  param.get("templ_sheet_col_id").toString();
        String templSheetId = sheetInfo.get("templ_sheet_id") == null? "" : sheetInfo.get("templ_sheet_id").toString();

        if(StringUtils.isNotEmpty(templSheetColId) && StringUtils.isNotEmpty(templSheetId)){
            data.put("templ_sheet_col_id",templSheetColId);
            data.put("templ_sheet_id",templSheetId);
            return  bidTemplateColumnService.findBidTemplateSheetColumnInfo(data);
        }else{
            return new HashMap<String,Object>();
        }
    }


    public void deleteBidTemplateSheetDataList(Map<String, Object> param) {

        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");

        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? "" : sheetInfo.get("templ_sheet_id").toString();

        for(Map row :  deleteItem) {
            if (StringUtils.isEmpty(templSheetId)) continue;
            row.put("templ_sheet_id",templSheetId);
            bidTemplateSheetService.deleteBidTemplateSheetDataList(row);
            bidTemplateSheetService.deleteBidTemplateSheetRowId(row);
        }
    }


    public void saveSheetCalcColumns(Map<String, Object> param) {



        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("updateList");
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");


        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? UUID.randomUUID().toString() : sheetInfo.get("templ_sheet_id").toString();

        if (StringUtils.isNotEmpty(templSheetId)) {
            sheetInfo.put("templ_sheet_id",templSheetId);
            if (this.existSheetMasterInfo(sheetInfo)) {
                //시트 추가
                bidTemplateSheetService.insertBidTemplateSheet(sheetInfo);
            }else{
                //시트 update
                bidTemplateSheetService.updateBidTemplateSheetInfo(sheetInfo);
            }
        }


        // UPDATE
        for (Map row : updateList) {
            if (StringUtils.isEmpty(templSheetId)) continue;

            if (this.existCheckFindColumnInfo(row)) {
                bidTemplateColumnService.updateBidTemplateSheetColumns(row);
            }
        }
        // INSERT
        for (Map row : insertList) {
            row.put("templ_sheet_id",templSheetId);
            if (StringUtils.isNotEmpty(templSheetId)) {
                bidTemplateColumnService.insertBidTemplateSheetColumns(row);
            }
        }
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


    @Transactional
    public void confirmBidTemplateAll(Map<String, Object> param) {
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        List<Map<String, Object>> vendorList = param.get("vendorList") == null? Lists.newArrayList() :(List<Map<String, Object>>) param.get("vendorList");

        //TemplateInfo 확정 여부 update
        this.confirmBidTemplateInfo(sheetInfo);

        List<Map<String,Object>> getTemplateSheetList = bidTemplateSheetService.findListBidTemplateSheet(sheetInfo);

        // UPDATE
        for (Map row : getTemplateSheetList) {
            this.updateConfirmBidTemplate(row);
        }
    }

    private void updateConfirmBidTemplate(Map row) {
        bidTemplateRepository.updateConfirmBidTemplate(row);
    }

    private void confirmBidTemplateInfo(Map<String, Object> sheetInfo) {
        bidTemplateRepository.confirmBidTemplateInfo(sheetInfo);
    }


    @Transactional
    public void confirmBidTemplateAllCancel(Map<String, Object> param) {

        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");

        //TemplateInfo 확정 여부 update
        this.confirmBidTemplateCancel(sheetInfo);

        List<Map<String,Object>> getTemplateSheetList = bidTemplateSheetService.findListBidTemplateSheet(sheetInfo);

        // UPDATE
        for (Map row : getTemplateSheetList) {
            this.updateConfirmBidTemplateCancel(row);
        }
    }

    private void updateConfirmBidTemplateCancel(Map row) {
        bidTemplateRepository.updateConfirmBidTemplateCancel(row);
    }

    private void confirmBidTemplateCancel(Map<String, Object> sheetInfo) {
        bidTemplateRepository.confirmBidTemplateCancel(sheetInfo);
    }



    public List findListGroupColumnMaster(Map param) {
       return bidTemplateRepository.findListGroupColumnMaster(param);
    }

    public List findListColumnListNotGroup(Map param) {
        return bidTemplateRepository.findListColumnListNotGroup(param);
    }

    public List findListColumnMaster(Map param) {
        return bidTemplateRepository.findListColumnMaster(param);
    }

}
