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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class BidTemplateColumnService {

    @Inject
    BidTemplateRepository bidTemplateRepository;


    public void deleteListColumnMaster(Map<String, Object> param) {
        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        for(Map row :  deleteItem) {
            bidTemplateRepository.deleteListBidColumnMaster(row);
        }
    }

    public void saveListColumnMaster(Map<String, Object> param) {
        List<Map<String, Object>> insertList = (List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = (List<Map<String, Object>>) param.get("updateList");

        this.updateListColumnMasterInfo(updateList);
        this.insetListColumnMasterInfo(insertList);
    }

    private void insetListColumnMasterInfo(List<Map<String, Object>> insertList) {
        // INSERT
        for (Map row : insertList) {
            if (this.existColumnMasterInfo(row)) {
                throw new CommonException(ErrorCode.DUPLICATED);
            } else {
                this.insertColumnMasterInfo(row);
            }
        }
    }

    private void updateListColumnMasterInfo(List<Map<String, Object>> updateList) {
        // UPDATE
        for (Map row : updateList) {
            if (this.existColumnMasterInfo(row)) {
                this.updateColumnMasterInfo(row);
            } else {
                this.insertColumnMasterInfo(row);
            }
        }
    }

    private void insertColumnMasterInfo(Map row) {
        bidTemplateRepository.insertColumnMasterInfo(row);
    }

    private void updateColumnMasterInfo(Map row) {
        bidTemplateRepository.updateColumnMasterInfo(row);
    }

    public boolean existColumnMasterInfo(Map row) {
        return (bidTemplateRepository.selectColumnMasterInfo(row) > 0);
    }


    public List findSheetColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findSheetColumnHeaders(param);
    }

    public List findSheetMasterGroupColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findSheetMasterGroupColumnHeaders(param);
    }

    public List findSheetMasterHeaderDepthGroupColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findSheetMasterHeaderDepthGroupColumnHeaders(param);
    }


    public Map<String,Object> findSheetColumnHeadersDialog(Map<String,Object> param){
        Map<String,Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> headerListMap = Lists.newArrayList();
        List<Map<String, Object>> headerGroupListMap = Lists.newArrayList();
        List<Map<String, Object>> headerDepthGroupListMap = Lists.newArrayList();

        String sheetId = param.get("sheet_id") == null ? "" : param.get("sheet_id").toString();

        if(StringUtils.isNotEmpty(sheetId)) {
            headerListMap = this.findSheetColumnHeaders(param);
            headerGroupListMap = this.findSheetMasterGroupColumnHeaders(param);
            headerDepthGroupListMap = this.findSheetMasterHeaderDepthGroupColumnHeaders(param);
            resultMap.put("headerListMap",headerListMap);
            resultMap.put("headerGroupListMap",headerGroupListMap);
            resultMap.put("headerDepthGroupListMap",headerDepthGroupListMap);
        }
        return resultMap;
    }


    public void saveListSheetColumns(Map<String, Object> param) {

        List<Map<String, Object>> insertList = (List<Map<String, Object>>) param.get("insertList");
        List<Map<String, Object>> updateList = (List<Map<String, Object>>) param.get("updateList");

        // UPDATE
        this.updateListSheetColumnInfo(updateList);
        // INSERT
        this.insertListSheetColumnInfo(insertList);
    }

    private void insertListSheetColumnInfo(List<Map<String, Object>> insertList) {
        for (Map row : insertList) {
            String sheetId = row.get("sheet_id") ==null? "" : row.get("sheet_id").toString();
            if(StringUtils.isEmpty(sheetId)) continue;

            if (this.existColumnMasterInfo(row)) {
                throw new CommonException(ErrorCode.DUPLICATED);
            } else {
                this.insertSheetColumnInfo(row);
            }
        }
    }

    private void updateListSheetColumnInfo(List<Map<String, Object>> updateList) {
        for (Map row : updateList) {
            String sheetId = row.get("sheet_id") ==null? "" : row.get("sheet_id").toString();
            if(StringUtils.isEmpty(sheetId)) continue;

            if (this.existColumnMasterInfo(row)) {
                this.updateSheetColumnInfo(row);
            } else {
                this.insertSheetColumnInfo(row);
            }
        }
    }

    private void insertSheetColumnInfo(Map row) {
        bidTemplateRepository.insertSheetColumnInfo(row);
    }

    private void updateSheetColumnInfo(Map row) {
        bidTemplateRepository.updateSheetColumnInfo(row);
    }


    public void deleteListSheetColumnInfo(Map<String, Object> param) {

        List<Map<String,Object>> deleteItem = (List<Map<String,Object>>)param.get("deleteItem");
        for(Map row :  deleteItem) {
            this.deleteSheetColumnInfo(row);
        }
    }

    private void deleteSheetColumnInfo(Map row) {
        bidTemplateRepository.deleteSheetColumnInfo(row);
    }

    public void updateBidTemplateSheetColumns(Map row) {
        bidTemplateRepository.updateBidTemplateSheetColumns(row);
    }

    public void insertBidTemplateSheetColumns(Map row) {
        bidTemplateRepository.insertBidTemplateSheetColumns(row);
    }

    public void deleteBidTemplateSheetColumns(Map row) {
        bidTemplateRepository.deleteBidTemplateSheetColumns(row);
    }


    public List findBidTemplateSheetColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetColumnHeaders(param);
    }

    public List findColTypeCalcHeadersColumns(Map<String, Object> param) {
        return bidTemplateRepository.findColTypeCalcHeadersColumns(param);
    }

    public List findBidTemplateHeadersColumnsForCalc(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateHeadersColumnsForCalc(param);
    }

    public List findBidTemplateSheetGroupColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetGroupColumnHeaders(param);
    }

    public List findBidTemplateSheetHeaderDepthGroupColumnHeaders(Map<String, Object> param) {
        return bidTemplateRepository.findBidTemplateSheetHeaderDepthGroupColumnHeaders(param);
    }

    public void updateSheetDataInfo(Map<String, Object> sheetDataInfo) {
        bidTemplateRepository.updateSheetDataInfo(sheetDataInfo);
    }

    public Map findBidTemplateSheetColumnInfo(Map<String, Object> data) {
        return bidTemplateRepository.findBidTemplateSheetColumnInfo(data);
    }


}
