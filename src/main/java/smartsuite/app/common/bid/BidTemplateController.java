package smartsuite.app.common.bid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.bid.service.BidTemplateColumnService;
import smartsuite.app.common.bid.service.BidTemplateService;
import smartsuite.app.common.bid.service.BidTemplateSheetService;
import smartsuite.app.common.bid.service.ProBidTemplateService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class BidTemplateController {


    @Inject
    BidTemplateService bidTemplateService;

    @Inject
    ProBidTemplateService proBidTemplateService;

    @Inject
    BidTemplateColumnService bidTemplateColumnService;

    @Inject
    BidTemplateSheetService bidTemplateSheetService;

    @Inject
    RfqTemplateController rfqTemplateController;

    @RequestMapping(value = "findListColumnMaster.do")
    public @ResponseBody
    List findListColumnMaster(@RequestBody Map param) {
        return bidTemplateService.findListColumnMaster(param);
    }
    @RequestMapping(value = "findListColumnListNotGroup.do")
    public @ResponseBody
    List findListColumnListNotGroup(@RequestBody Map param) {
        return bidTemplateService.findListColumnListNotGroup(param);
    }

    @RequestMapping(value = "findListGroupColumnMaster.do")
    public @ResponseBody
    List findListGroupColumnMaster(@RequestBody Map param) {
        return bidTemplateService.findListGroupColumnMaster(param);
    }

    @RequestMapping(value = "deleteListColumnMaster.do")
    public @ResponseBody
    void deleteListColumnMaster(@RequestBody Map param) {
        bidTemplateColumnService.deleteListColumnMaster(param);
    }

    @RequestMapping(value = "saveListColumnMaster.do")
    public @ResponseBody
    void saveListColumnMaster(@RequestBody Map param) {
        bidTemplateColumnService.saveListColumnMaster(param);
    }

    //SHEET MASTER

    @RequestMapping(value = "findListSheetMaster.do")
    public @ResponseBody
    List<Map<String,Object>> findListSheetMaster(@RequestBody Map param) {

        String sheetId = param.get("sheet_id") ==null? "" : param.get("sheet_id").toString();
        String sheetTyp = param.get("sheet_typ") ==null? "" : param.get("sheet_typ").toString();

        if(StringUtils.isEmpty(sheetId)){
            param.put("sheet_id","");
        }

        if(StringUtils.isEmpty(sheetTyp)){
            param.put("sheet_typ","");
        }

        return bidTemplateSheetService.findListSheetMaster(param);
    }

    @RequestMapping(value = "deleteListSheetMaster.do")
    public @ResponseBody
    void deleteListSheetMaster(@RequestBody Map param) {
        bidTemplateSheetService.deleteListSheetMaster(param);
    }


    @RequestMapping(value = "saveListSheetMaster.do")
    public @ResponseBody
    void saveListSheetMaster(@RequestBody Map param) {
        bidTemplateSheetService.saveListSheetMaster(param);
    }

    @RequestMapping(value = "deleteListSheetColumnInfo.do")
    public @ResponseBody
    void deleteListSheetColumnInfo(@RequestBody Map param) {
        bidTemplateColumnService.deleteListSheetColumnInfo(param);
    }


    //COLUMN HEADER
    @RequestMapping(value = "findSheetColumnHeaders.do")
    public @ResponseBody
    List findSheetColumnHeaders(@RequestBody Map param) {
        return bidTemplateColumnService.findSheetColumnHeaders(param);
    }


    @RequestMapping(value = "findSheetColumnHeadersDialog.do")
    public @ResponseBody
    Map findSheetColumnHeadersDialog(@RequestBody Map param) {
        return bidTemplateColumnService.findSheetColumnHeadersDialog(param);
    }

    @RequestMapping(value = "saveListSheetColumns.do")
    public @ResponseBody
    void saveListSheetColumns(@RequestBody Map param) {
        bidTemplateColumnService.saveListSheetColumns(param);
    }



    //Bid TEMPLATE
    @RequestMapping(value = "findListBidTemplateMaster.do")
    public @ResponseBody
    List findListBidTemplateMaster(@RequestBody Map param) {

        String templCd = param.get("templ_cd") ==null? "" : param.get("templ_cd").toString();
        String templNm = param.get("templ_nm") ==null? "" : param.get("templ_nm").toString();

        if(StringUtils.isEmpty(templCd)){
            param.put("templ_cd","");
        }

        if(StringUtils.isEmpty(templNm)){
            param.put("templ_nm","");
        }

        return bidTemplateService.findListBidTemplateMaster(param);
    }

    @RequestMapping(value = "saveListBidTemplateMaster.do")
    public @ResponseBody
    void saveListBidTemplateMaster(@RequestBody Map param) {
        bidTemplateService.saveListBidTemplateMaster(param);
    }

    @RequestMapping(value = "deleteListBidTemplateMaster.do")
    public @ResponseBody
    void deleteListBidTemplateMaster(@RequestBody Map param) {
        bidTemplateService.deleteListBidTemplateMaster(param);
    }



    //bid TEMPLATE SHEET
    @RequestMapping(value = "findListBidTemplateSheet.do")
    public @ResponseBody
    List findListBidTemplateSheet(@RequestBody Map param) {
        return bidTemplateSheetService.findListBidTemplateSheet(param);
    }


    @RequestMapping(value = "saveBidTemplateSheetColumns.do")
    public @ResponseBody
    void saveBidTemplateSheetColumns(@RequestBody Map param) {
        bidTemplateService.saveBidTemplateSheetColumns(param);
    }

    @RequestMapping(value = "deleteBidTemplateSheetColumns.do")
    public @ResponseBody
    void deleteBidTemplateSheetColumns(@RequestBody Map param) {
        bidTemplateService.deleteBidTemplateSheetColumns(param);
    }



    @RequestMapping(value = "findBidTemplateSheetColumnHeaders.do")
    public @ResponseBody
    List findBidTemplateSheetColumnHeaders(@RequestBody Map param) {
        String templSheetId = param.get("templ_sheet_id") == null? "" :  param.get("templ_sheet_id").toString();

        if(StringUtils.isNotEmpty(templSheetId)){
            return bidTemplateColumnService.findBidTemplateSheetColumnHeaders(param);
        }else{
            return new ArrayList();
        }
    }

    @RequestMapping(value = "findTemplateSheetColumnAndDataList.do")
    public @ResponseBody
    Map findTemplateSheetColumnAndDataList(@RequestBody Map param) {
        return bidTemplateService.findTemplateSheetColumnAndDataList(param);
    }


    @RequestMapping(value = "saveBidTemplateSheetDataList.do")
    public @ResponseBody
    void saveBidTemplateSheetDataList(@RequestBody Map param) {
        bidTemplateService.saveBidTemplateSheetDataList(param);
    }

    @RequestMapping(value = "deleteBidTemplateSheetDataList.do")
    public @ResponseBody
    void deleteBidTemplateSheetDataList(@RequestBody Map param) {
        bidTemplateService.deleteBidTemplateSheetDataList(param);
    }

    @RequestMapping(value = "deleteTemplateSheetDataAll.do")
    public @ResponseBody
    void deleteTemplateSheetDataAll(@RequestBody Map param) {
        bidTemplateSheetService.deleteTemplateSheetDataAll(param);
    }



    // 컬럼 계산식 추가/수정
    @RequestMapping(value = "saveCalcColumns.do")
    public @ResponseBody
    void saveCalcColumns(@RequestBody Map param) {
        bidTemplateService.saveSheetCalcColumns(param);
    }


    // 컬럼 계산식을 위해서, INT인 컬럼리스트를 추출한다.
    @RequestMapping(value = "findBidTemplateHeadersColumnsForCalc.do")
    public @ResponseBody
    List findBidTemplateHeadersColumnsForCalc(@RequestBody Map param) {

        String templSheetId = param.get("templ_sheet_id") == null? "" :  param.get("templ_sheet_id").toString();

        if(StringUtils.isNotEmpty(templSheetId)){
            return bidTemplateColumnService.findBidTemplateHeadersColumnsForCalc(param);
        }else{
            return new ArrayList();
        }
    }


    @RequestMapping(value = "findCalculateColumn.do")
    public @ResponseBody
    List findCalculateColumn(@RequestBody Map param) {
        return bidTemplateService.findCalculateColumn(param);
    }


    @RequestMapping(value = "findBidTemplateSheetColumnInfo.do")
    public @ResponseBody
    Map findBidTemplateSheetColumnInfo(@RequestBody Map param) {
        return bidTemplateService.findBidTemplateSheetColumnInfo(param);
    }


    //BidTemplate 확정 처리
    @RequestMapping(value = "confirmBidTemplateAll.do")
    public @ResponseBody
    void confirmBidTemplateAll(@RequestBody Map param) {
        bidTemplateService.confirmBidTemplateAll(param);
    }


    @RequestMapping(value = "confirmBidTemplateAllCancel.do")
    public @ResponseBody
    void confirmBidTemplateAllCancel(@RequestBody Map param) {
        bidTemplateService.confirmBidTemplateAllCancel(param);
    }



    @RequestMapping(value = "findListBidTemplateConfirm.do")
    public @ResponseBody
    List findListBidTemplateConfirm(@RequestBody Map param) {

        String templCd = param.get("templ_cd") ==null? "" : param.get("templ_cd").toString();
        String templNm = param.get("templ_nm") ==null? "" : param.get("templ_nm").toString();

        if(StringUtils.isEmpty(templCd)){
            param.put("templ_cd","");
        }

        if(StringUtils.isEmpty(templNm)){
            param.put("templ_nm","");
        }

        return bidTemplateService.findListBidTemplateConfirm(param);
    }


    @RequestMapping(value = "findTemplateSheetColumnAndDataListForPro.do")
    public @ResponseBody
    Map findTemplateSheetColumnAndDataListForPro(@RequestBody Map param) {
        return proBidTemplateService.findTemplateSheetColumnAndDataListForProProcess(param);
    }


    @RequestMapping(value = "saveBidTemplateSheetDataListForPro.do")
    public @ResponseBody
    void saveBidTemplateSheetDataListForPro(@RequestBody Map param) {
        proBidTemplateService.saveBidTemplateSheetDataListForPro(param);
    }


    @RequestMapping(value = "findListBidTemplateSheetForApp.do")
    public @ResponseBody
    List findListBidTemplateSheetForApp(@RequestBody Map param) {
        return proBidTemplateService.findListBidTemplateSheetForApp(param);
    }


    @RequestMapping(value = "deleteBidTemplateSyncAndDataByApp.do")
    public @ResponseBody
    void deleteBidTemplateSyncAndDataByApp(@RequestBody Map param) {
        proBidTemplateService.deleteBidTemplateSyncAndDataByApp(param);
    }


    @RequestMapping(value = "findListConfirmBidTemplate.do")
    public @ResponseBody
    List findListConfirmBidTemplate(@RequestBody Map param) {
        String templCd = param.get("templ_cd") ==null? "" : param.get("templ_cd").toString();
        String templNm = param.get("templ_nm") ==null? "" : param.get("templ_nm").toString();

        if(StringUtils.isEmpty(templCd))  param.put("templ_cd","");
        if(StringUtils.isEmpty(templNm))  param.put("templ_nm","");

        return bidTemplateService.findListConfirmBidTemplate(param);
    }

}
