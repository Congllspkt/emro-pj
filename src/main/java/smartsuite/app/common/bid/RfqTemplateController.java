package smartsuite.app.common.bid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.bid.service.RfqTemplateService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller
@RequestMapping(value="**/bp/**/")
public class RfqTemplateController {

    @Inject
    RfqTemplateService rfqTemplateService;


    @RequestMapping(value = "findRFQTemplateSheetList.do")
    public @ResponseBody
    Map findRFQTemplateSheetList(@RequestBody Map param) {
        return rfqTemplateService.findRFQTemplateSheetList(param);
    }

    @RequestMapping(value = "saveRFQTemplateSave.do")
    public @ResponseBody
    Map saveRFQTemplateSave(@RequestBody Map param) {
        return rfqTemplateService.saveRFQTemplateSave(param);
    }


    @RequestMapping(value = "deleteRFQTemplateSheetAll.do")
    public @ResponseBody
    void deleteRFQTemplateSheetAll(@RequestBody Map param) {
        rfqTemplateService.deleteRFQTemplateSheetAll(param);
    }

    @RequestMapping(value = "findListRFQTemplateList.do")
    public @ResponseBody
    List findListRFQTemplateList(@RequestBody Map param) {
        return rfqTemplateService.findListRFQTemplateList(param);
    }

    @RequestMapping(value = "deleteRFQTemplateSheetData.do")
    public @ResponseBody
    void deleteBidTemplateSheetDataListForPro(@RequestBody Map param) {
        rfqTemplateService.deleteRFQTemplateSheetData(param);
    }

    @RequestMapping(value = "findCalculateColumnAndSum.do")
    public @ResponseBody
    Map findCalculateColumnAndSum(@RequestBody Map param) {
        return rfqTemplateService.findCalculateColumnAndSum(param);
    }


    @RequestMapping(value = "findRFQTemplateConfirmYn.do")
    public @ResponseBody
    List findRFQTemplateConfirmYn(@RequestBody Map param) {
        return rfqTemplateService.findRFQTemplateConfirmYn(param);
    }



}
