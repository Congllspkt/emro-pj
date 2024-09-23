package smartsuite.app.common.bid.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.bid.repository.BidTemplateRepository;
import smartsuite.app.common.shared.service.SharedService;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BidTemplateSheetService {

    @Inject
    BidTemplateRepository bidTemplateRepository;
    
    @Inject
    SharedService sharedService;


    public List findListSheetMaster(Map<String, Object> param) {
        return bidTemplateRepository.findListSheetMaster(param);
    }

    public void deleteListSheetMaster(Map<String, Object> param) {
        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        for(Map row :  deleteItem) {
            this.deleteSheetMaster(row);
        }
    }

    public void deleteSheetMaster(Map row) {
        this.deleteSheetMasterInfo(row);
        this.deleteSheetMasterDataInfo(row);
    }

    public void deleteSheetMasterDataInfo(Map row) {
        bidTemplateRepository.deleteSheetMasterDataInfo(row);
    }

    public void deleteSheetMasterInfo(Map row) {
        bidTemplateRepository.deleteSheetMasterInfo(row);
    }


    //BID Template에서 tab closable 처리 시 전체 delete
    @Transactional
    public void deleteTemplateSheetDataAll( Map<String,Object> param){
        Map<String,Object> sheetInfo = param.get("sheetInfo") == null? Maps.newHashMap() : (Map<String,Object>)param.get("sheetInfo");
        String templSheetId = sheetInfo.get("templ_sheet_id") == null ? UUID.randomUUID().toString() : sheetInfo.get("templ_sheet_id").toString();

        if(StringUtils.isNotEmpty(templSheetId)){
            this.deleteTemplateSheetData(sheetInfo);
        }
    }

    public void deleteTemplateSheetData(Map<String, Object> sheetInfo) {

        //ESBTRTD DELETE
        this.deleteBidTemplateSheetDataForTmpSheetId(sheetInfo);

        //ESBTRTR DELETE
        this.deleteBidTemplateRowIdForTmpSheetId(sheetInfo);

        //ESBTRTC DELETE
        this.deleteBidTemplateHeaderColumnsForTmpSheetId(sheetInfo);

        //ESBTRTS DELETE
        this.deleteBidTemplateSheetForTmpSheetId(sheetInfo);
    }

    public void deleteBidTemplateSheetForTmpSheetId(Map<String, Object> sheetInfo) {
        bidTemplateRepository.deleteBidTemplateSheetForTmpSheetId(sheetInfo);
    }

    public void deleteBidTemplateHeaderColumnsForTmpSheetId(Map<String, Object> sheetInfo) {
        bidTemplateRepository.deleteBidTemplateHeaderColumnsForTmpSheetId(sheetInfo);
    }

    public void deleteBidTemplateRowIdForTmpSheetId(Map<String, Object> sheetInfo) {
        bidTemplateRepository.deleteBidTemplateRowIdForTmpSheetId(sheetInfo);
    }

    public void deleteBidTemplateSheetDataForTmpSheetId(Map<String, Object> sheetInfo) {
        bidTemplateRepository.deleteBidTemplateSheetDataForTmpSheetId(sheetInfo);
    }


    public void saveListSheetMaster(Map<String, Object> param) {
        List<Map<String, Object>> insertList = param.get("insertList") == null? Lists.newArrayList():(List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = param.get("updateList") == null? Lists.newArrayList():(List<Map<String, Object>>) param.get("updateList");

        // UPDATE
        this.updateListSheetMasterInfo(updateList);
        // INSERT
        this.insertListSheetMasterInfo(insertList);
    }

    public void insertListSheetMasterInfo(List<Map<String, Object>> insertList) {
        for (Map row : insertList) {
            if (this.existSheetMasterInfo(row)) {
                throw new CommonException(ErrorCode.DUPLICATED);
            } else {
                row.put("sheet_cd", this.generateBidDocumentNumber());
                this.insertSheetMasterInfo(row);
            }
        }
    }

    public void updateListSheetMasterInfo(List<Map<String, Object>> updateList) {
        for (Map row : updateList) {
            if (this.existSheetMasterInfo(row)) {
                this.updateSheetMasterInfo(row);
            } else {
                row.put("sheet_cd", this.generateBidDocumentNumber());
                this.insertSheetMasterInfo(row);
            }
        }
    }

    public void insertSheetMasterInfo(Map row) {
        bidTemplateRepository.insertSheetMasterInfo(row);
    }

    public void updateSheetMasterInfo(Map row) {
        bidTemplateRepository.updateSheetMasterInfo(row);
    }

    public boolean existSheetMasterInfo(Map row) {
        return bidTemplateRepository.selectSheetMasterInfo(row) > 0;
    }
    
    public String generateBidDocumentNumber(){
        return sharedService.generateDocumentNumber("BID");
    }


    public List findListBidTemplateSheet(Map<String, Object> param) {
        return bidTemplateRepository.findListBidTemplateSheet(param);
    }

    public void updateBidTemplateSheetInfo(Map<String, Object> sheetInfo) {
        bidTemplateRepository.updateBidTemplateSheetInfo(sheetInfo);
    }

    @Transactional
    public void insertBidTemplateSheet(Map<String, Object> param){
        //Sheet 추가
        bidTemplateRepository.insertBidTemplateSheetInfo(param);
    }

    public List<Map<String, Object>> findTemplateSheetColumnAndDataListForSheet(Map<String, Object> param) {
        return bidTemplateRepository.findTemplateSheetColumnAndDataListForSheet(param);
    }

    public List<Map<String, Object>> findBidTemplateSheetRowIdList(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetRowIdList(param);
    }

    public Map findBidTemplateSheetRowInfo(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetRowInfo(param);
    }

    public void insertSheetDataInfo(Map<String, Object> sheetDataInfo) {
        bidTemplateRepository.insertSheetDataInfo(sheetDataInfo);
    }

    public void insertSheetRowInfo(Map<String, Object> rowInfo) {
        bidTemplateRepository.insertSheetRowInfo(rowInfo);
    }

    public void deleteBidTemplateSheetDataList(Map row) {
        bidTemplateRepository.deleteBidTemplateSheetDataList(row);
    }

    public void deleteBidTemplateSheetRowId(Map row) {
        bidTemplateRepository.deleteBidTemplateSheetRowId(row);
    }

    public Map<String, Object> findBidTemplateSheetInfo(Map<String, Object> setVendorSearchMap) {
        return bidTemplateRepository.findBidTemplateSheetInfo(setVendorSearchMap);
    }

    public List<Map<String, Object>> findListBidTemplateSheetForApp(Map<String, Object> setVendorSearchMap) {
        return bidTemplateRepository.findListBidTemplateSheetForApp(setVendorSearchMap);
    }

    public List<Map<String, Object>> findBidTemplateSheetRowIdListForAppId(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetRowIdListForAppId(param);
    }

    public Map<String, Object> selectVendorTemplateAppByVendorTemplId(Map<String, Object> getPrInfoMap) {
        return bidTemplateRepository.selectVendorTemplateAppByVendorTemplId(getPrInfoMap);
    }

    public boolean existVendorTemplateAppCnt(Map<String, Object> setVendorSearchMap) {
        return bidTemplateRepository.selectVendorTemplateAppCnt(setVendorSearchMap) > 0;
    }

    public void insertBidTemplateAndAppIdRelation(Map<String, Object> setVendorSearchMap) {
        bidTemplateRepository.insertBidTemplateAndAppIdRelation(setVendorSearchMap);
    }

    public void updatePrHeaderVendorTemplId(Map<String, Object> setVendorSearchMap) {
        bidTemplateRepository.updatePrHeaderVendorTemplId(setVendorSearchMap);
    }

    public boolean existColumnDataBuyerCnt(Map<String, Object> sheetDataInfo) {
        return bidTemplateRepository.selectColumnDataBuyerCnt(sheetDataInfo) > 0;
    }

    public void updateSheetDataInfoForAppId(Map<String, Object> sheetDataInfo) {
        bidTemplateRepository.updateSheetDataInfoForAppId(sheetDataInfo);
    }

    public void deleteBidTemplateSheetDataListForAppId(Map row) {
        bidTemplateRepository.deleteBidTemplateSheetDataListForAppId(row);
    }

    public void deleteBidTemplateSheetRowIdForAppId(Map row) {
        bidTemplateRepository.deleteBidTemplateSheetRowIdForAppId(row);
    }

    public void deleteBidTemplateSheetDataForAppId(Map<String, Object> getSheetInfo) {
        bidTemplateRepository.deleteBidTemplateSheetDataForAppId(getSheetInfo);
    }

    public void deleteBidTemplateRowIdForAppId(Map<String, Object> getSheetInfo) {
        bidTemplateRepository.deleteBidTemplateRowIdForAppId(getSheetInfo);
    }

    public void deleteBidTemplateSyncAppInfo(Map<String, Object> setSelectMap) {
        bidTemplateRepository.deleteBidTemplateSyncAppInfo(setSelectMap);
    }

    public void updateVendorTemplIdNull(Map<String, Object> setSelectMap) {
        bidTemplateRepository.updateVendorTemplIdNull(setSelectMap);
    }

    public void updateBidTemplateSheetData(List<Map<String, Object>> updateList, String templSheetId, String templateSheetColumnId, String appUuid) {
        for (Map<String,Object> updateMap : updateList) {
            if (StringUtils.isEmpty(templSheetId)) continue;

            updateMap.put("templ_sheet_col_id",templateSheetColumnId);

            Iterator<String> updateSheetDataId = updateMap.keySet().iterator();

            //update로 넘어오는
            while(updateSheetDataId.hasNext()){
                Map<String,Object> sheetDataInfo = Maps.newHashMap();

                String key = updateSheetDataId.next();
                String value = String.valueOf(updateMap.get(key));

                sheetDataInfo.put("column_val",value);
                sheetDataInfo.put("templ_sheet_col_id",key);
                sheetDataInfo.put("sheet_data_row_id",updateMap.get("sheet_data_row_id"));
                sheetDataInfo.put("templ_sheet_id",templateSheetColumnId);
                sheetDataInfo.put("task_uuid", appUuid);

                if (!this.existColumnDataBuyerCnt(sheetDataInfo)) {
                    this.insertSheetDataInfo(sheetDataInfo);
                }else {
                    this.updateSheetDataInfoForAppId(sheetDataInfo);
                }
            }
        }
    }


    public void insertBidTemplateSheetData(List<Map<String, Object>> insertList, List<Integer> insertIndex, String templSheetId, String templateSheetColumnId, String appUuid) {
        for (int a=0; a<insertList.size(); a ++) {
            Map<String,Object> insertMap = insertList.get(a);
            Integer index = insertIndex.get(a);

            if (StringUtils.isNotEmpty(templSheetId)) {
                String sheetDataRowId = UUID.randomUUID().toString();
                Map<String,Object> rowInfo = Maps.newHashMap();
                rowInfo.put("sheet_data_row_id",sheetDataRowId); //insert로 넘어온 값에, sheet_data_row_id를 넣어준다.
                rowInfo.put("templ_sheet_id",templateSheetColumnId);
                rowInfo.put("row_num",index);
                rowInfo.put("task_uuid",appUuid);

                //row relation table insert
                this.insertSheetRowInfo(rowInfo);

                Iterator<String> insertSheetDataId = insertMap.keySet().iterator();

                //insert로 넘어오는
                while(insertSheetDataId.hasNext()){
                    Map<String,Object> sheetDataInfo = Maps.newHashMap();

                    String key = insertSheetDataId.next();
                    String value = String.valueOf(insertMap.get(key));

                    sheetDataInfo.put("column_val",value);
                    sheetDataInfo.put("sheet_data_row_id",sheetDataRowId); //insert로 넘어온 값에, sheet_data_row_id를 넣어준다.
                    sheetDataInfo.put("templ_sheet_col_id",key);
                    sheetDataInfo.put("templ_sheet_id",templateSheetColumnId);
                    sheetDataInfo.put("task_uuid",appUuid);
                    this.insertSheetDataInfo(sheetDataInfo);
                }

            }
        }
    }

    public Map<String, List<Map<String, Object>>> setRowDataGetRowDataInfo(List<Map<String, Object>> getSheetDataListMap, Map<String, List<Map<String, Object>>> rowDataMap) {
        for(Map<String,Object> getData : getSheetDataListMap){

            String rowId =  getData.get("sheet_data_row_id") == null ? "" : getData.get("sheet_data_row_id").toString();
            List<Map<String,Object>> getRowDataList = rowDataMap.get(rowId) == null? Lists.newArrayList() :  rowDataMap.get(rowId);
            getRowDataList.add(getData);

            rowDataMap.put(rowId,getRowDataList);
        }
        return rowDataMap;
    }
}

