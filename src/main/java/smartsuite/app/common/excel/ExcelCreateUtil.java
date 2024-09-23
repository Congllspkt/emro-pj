package smartsuite.app.common.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.bean.SheetInfoBean;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Excel을 Copy하고, 해당 하는 Data / Style들에 대해서 기본값들을 정의하기 위하여 Create Util
 */
@SuppressWarnings("unused")
@Service
public class ExcelCreateUtil {

    @Inject
    ExcelCreateService excelCreateService;
    /**
     * Excel을 Copy 시작하는 메소드 최초에 Sheet의 틀을 복사한다.
     * @param sheetList
     * @param destWorkbook
     */
    public static void copyExcel(List<SheetInfoBean> sheetList, XSSFWorkbook destWorkbook) {
        for(SheetInfoBean sheetInfoBean : sheetList){
            XSSFSheet destSheet = destWorkbook.createSheet(sheetInfoBean.getXls_work_sht_nm());
            ExcelCreateService.copySheet(sheetInfoBean.getRowList(), destSheet);
        }
    }

    //이메일 업무관리에서 메일 발송시 사용하는 Method
    public Map<String,Object> createExcelTemplateToDataSetup(List<SheetInfoBean> sheetList, Map<String,Map<String,Map<String,Map<String,Object>>>> dataListSheet ,List<Map<String,Object>> headersList,String fileNm) {
        String grpCd = UUID.randomUUID().toString();
        Map<String,Object> resultMap = excelCreateService.createEmailWorkExcelTemplateProcess(sheetList,null,dataListSheet,headersList,fileNm,grpCd);
        resultMap.put("athg_uuid",grpCd);
        return resultMap;
    }


    //Template 만들때 사용하는 method
    public static Map<String,Object> createExcelByDataSetup(List<SheetInfoBean> sheetList, XSSFWorkbook destWorkbook,Map<String, Object> dataMapForSheet ,List<Map<String,Object>> headersList) {
        return ExcelCreateService.createExcelByListDataSetup(sheetList,destWorkbook,dataMapForSheet,headersList);
    }


    // 업무에서 map을 던지면 template 생성 및 data 매칭하는 중간 Method
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<String,Object> createEmailWorkProc(List<SheetInfoBean> sheetList, Map<String,Object> dataMap ,List<Map<String,Object>> headersList,String fileNm) {
        String athgUuid = UUID.randomUUID().toString();
        Map<String,Object> resultMap = excelCreateService.createEmailWorkExcelTemplateProcess(sheetList,dataMap,null,headersList,fileNm,athgUuid);
        resultMap.put("athg_uuid",athgUuid);
        return resultMap;
    }
}
