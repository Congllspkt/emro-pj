package smartsuite.app.common.excel;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smartsuite.app.common.excel.bean.CellInfoBean;
import smartsuite.app.common.excel.bean.ExcelCellDataBean;
import smartsuite.app.common.excel.bean.RowInfoBean;
import smartsuite.app.common.excel.bean.SheetInfoBean;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.*;

@Service
@SuppressWarnings("unused")
public class ExcelReaderUtil {

    static final Logger LOG = LoggerFactory.getLogger(ExcelReaderUtil.class);


	@Inject
	StdFileService stdFileService;

    @Inject
    private SqlSession sqlSession;

    public static List<RowInfoBean> readExcel(XSSFSheet source , String emailWorkId,String xlsWorkSht) {

        List<RowInfoBean> rowInfoBeanList = new ArrayList<RowInfoBean>();
        for (int i = source.getFirstRowNum(); i <= source.getLastRowNum(); i++) {
            XSSFRow srcRow = source.getRow(i);
            RowInfoBean sheetRow = new RowInfoBean();
            String rowKey = UUID.randomUUID().toString();
            sheetRow.setExcel_row_no(i);
            if (srcRow != null) {
                sheetRow.setExcel_row_uuid(rowKey);
                sheetRow.setEml_task_uuid(emailWorkId);
                sheetRow.setExcel_sht_uuid(xlsWorkSht);
                sheetRow.setCellList(ExcelCellDataBean.readRow(source,  srcRow ,emailWorkId , rowKey));
                //sheetRow.setColumnIndex(i);
                rowInfoBeanList.add(sheetRow);
            }
        }
        return rowInfoBeanList;
    }



    public Map<String,Object> excelReadAndMappingSheetBean(FileItem fileitem , Map<String,Object> param){

        //sheetName -> key
        //rowDataMap -> value
        Map<String,Map<String,Map<String,Object>>> sheetDataMap = new HashMap<String, Map<String, Map<String, Object>>>();
        Map<String,Object> dataMap = Maps.newHashMap();
        Map<String,Object> excelInfoMap = Maps.newHashMap();
        String emailWorkTargId = "";
        String emailSndLogId = "";
        XSSFWorkbook sourceWorkBook = null;

        try{
            //메일에서 받아온 파일을 읽어들인다.
            if(fileitem.getReference() != null){
                excelInfoMap = this.findMailExcelCheck(fileitem);
                emailWorkTargId = excelInfoMap.get("emailWorkTargId") == null? "" : (String) excelInfoMap.get("emailWorkTargId");
                emailSndLogId = excelInfoMap.get("emailSndLogId") == null? "" : (String) excelInfoMap.get("emailSndLogId");
                sourceWorkBook = excelInfoMap.get("sourceWorkBook") == null? null : (XSSFWorkbook) excelInfoMap.get("sourceWorkBook");
            }else{
                try{
                    throw new FileNotFoundException("메일 EXCEL File이 서버에 존재하지 않습니다.");
                }catch (Exception e){
                    LOG.error(e.getMessage());
                }
            }


            String getParamEmailSndLogId = param.get("eml_snd_histrec_uuid") == null? "" : param.get("eml_snd_histrec_uuid").toString();
            if(!emailSndLogId.equals(getParamEmailSndLogId)){
                try{
                    throw new FileNotFoundException("메일 EXCEL 내에 EMAIL_SND_LOG_ID와 메일의 EMAIL_SND_LOG_ID가 다릅니다. ");
                }catch (Exception e){
                    LOG.error(e.getMessage());
                }
            }

            Map<String,Object> sendMailInfo = this.mailReceiveExcelFileGetInfo(emailWorkTargId,emailSndLogId);

            //미리 만들어둔 temp file grp_cd
            String attNo = sendMailInfo.get("tmpl_athg_uuid") == null? "" : sendMailInfo.get("tmpl_athg_uuid").toString();
            FileItem excelFileItem = this.fileExistCheck(attNo);


            // 메일로 받은 Excel에 대해서 SheetList 형태로 변경한다.
            // SheetInfoBean List new
            List<SheetInfoBean> getExcelSheetList = new ArrayList<SheetInfoBean>();

            String sendEmailWorkId = sendMailInfo.get("eml_task_uuid") == null? "" :sendMailInfo.get("eml_task_uuid").toString();
            String sendEmailWorkCd = sendMailInfo.get("eml_task_typ_ccd") == null? "" :sendMailInfo.get("eml_task_typ_ccd").toString();

            getExcelSheetList = this.findSheetCountForInitData(sourceWorkBook,sendEmailWorkId,sendEmailWorkCd);

            // SheetInfoBean List new
            List<SheetInfoBean> templateSheetList = new ArrayList<SheetInfoBean>();

            // SEND 처리간 발송된 Template 구현 내역과 , Cell 위치값을 기준으로 Mail로 받은 파일의 key를 매칭시킨다..
            if(null != excelFileItem.getInputStreamContents()){

                //sendmailinfo에 temp file을 담는다.
                sendMailInfo.put("tmpExcelFileItem",excelFileItem);

                templateSheetList = compareSendTemplateDataAndMailKey(getExcelSheetList,excelFileItem,sendEmailWorkId);
                //getExcelSheetList & templateSheetList 비교 시작
                dataMap = this.compareGetExcelSheetListAndTemplateSheetList(templateSheetList,getExcelSheetList,dataMap);

            }

        dataMap.put("eml_snd_histrec_uuid",emailSndLogId); //발송메일 UUID
        dataMap.put("eml_task_typ_ccd",sendEmailWorkCd); //메일 업무코드
        dataMap.put("sendEmailInfo",sendMailInfo);  //발송메일 Info 정보
        dataMap.put("receivedEmailInfo",param);   //수신메일 정보


        }catch (RuntimeException rune){
            LOG.error(rune.getMessage());
        }catch (Exception e){
            LOG.error(e.getMessage());
        }

        //return sheetDataMap;
        return dataMap;

    }

    private Map<String, Object> compareGetExcelSheetListAndTemplateSheetList(List<SheetInfoBean> templateSheetList, List<SheetInfoBean> getExcelSheetList, Map<String, Object> dataMap) {

        Map<String,Map<String,Object>> subMap = new HashMap<String, Map<String, Object>>(); //subMap으로 ${dataBean.key} 로 구성될 경우 찾아오기위한 map

        //getExcelSheetList & templateSheetList 비교 시작
        for(SheetInfoBean tempSheetInfo : templateSheetList){

            //sheetInfo를 기준으로 동일 sheet인지 구분.
            SheetInfoBean getSheetInfo = new SheetInfoBean();

            String tempSheetName = tempSheetInfo.getXls_work_sht_nm() == null? "" : tempSheetInfo.getXls_work_sht_nm();

            ExcelCellDataBean.sheetInfoCopy(getSheetInfo,getExcelSheetList,tempSheetName);

            //tempSheetInfo와 getSheetInfo를 기준으로 비교하여, value와 key를 매칭 시켜야함.
            List<RowInfoBean> templateRowList = tempSheetInfo.getRowList();
            List<RowInfoBean> getRowList = getSheetInfo.getRowList();

            //-- Map<String,Map<String,Object>> rowDataMap --
            //rowNo ->key
            //dataMap -> value
            Map<String,Map<String,Object>> rowDataMap = new HashMap<String, Map<String, Object>>();

            int listRowNoCheck = 0;

            Map<String,List<Map<String,Object>>> dataListMap = new HashMap<String, List<Map<String, Object>>>(); // $[ ] list data를 map형태로 객체화 시키기 위한 map

            //row별로 찾는다.
            for(RowInfoBean templateRowInfo : templateRowList){

                int tempRowNo = templateRowInfo.getExcel_row_no();
                RowInfoBean getRowInfo = new RowInfoBean();

                ExcelCellDataBean.rowInfoCopy(getRowInfo,getRowList,tempRowNo);

                if(listRowNoCheck == 0) listRowNoCheck = tempRowNo; // map형태로 취득하던 내역을 list 일 경우, row no 을 체크해서 map -> listMap으로 변환한다.

                List<CellInfoBean> templateCellList = templateRowInfo.getCellList();
                List<CellInfoBean> getCellList = getRowInfo.getCellList();

                //-- Map<String,Object> dataMap --
                //templateCell string value -> key
                //getcell string value -> value
                // Map<String,Object> dataMap = Maps.newHashMap();
                for(CellInfoBean templateCellInfo : templateCellList){

                    int cellIndex = templateCellInfo.getCel_idx_no();

                    CellInfoBean getCellInfo = new CellInfoBean();

                    ExcelCellDataBean.cellInfoCopy(getCellInfo,getCellList,cellIndex);

                    String templateStringValue = templateCellInfo.getStr_cel_val();
                    String getStringValue = ExcelCellDataBean.cellTypeCheck(getCellInfo);


                    //CellDataMap에 담기.
                    if(!Strings.isNullOrEmpty(templateStringValue)) {

                        if (templateStringValue.startsWith(ExcelCellDataBean.STARTFORMULATOKEN) && templateStringValue.endsWith(ExcelCellDataBean.ENDFORMULATOKEN)) { //list value인지 확인.
                            templateStringValue = templateStringValue.replace("$[","");
                            templateStringValue = templateStringValue.replace("]","");


                            if(templateStringValue.indexOf(".") > -1){ //  data.value  형식의 구조라면, 아래와 같이.

                                String dataGetKey = "";
                                String subDataGetKey = "";
                                String[] splitValue = templateStringValue.split("\\.");
                                dataGetKey = splitValue[0];
                                subDataGetKey = splitValue[1];

                                Map<String,Object> dumyMap = Maps.newHashMap();
                                if(subMap.size() > 0){ // subMap에 현재 데이터가 들어갓을 경우, (list인 경우 row no 체크해주어야 함 - 2019.08.16)
                                    dumyMap = (subMap.get(dataGetKey) == null || listRowNoCheck != tempRowNo)? new HashMap<String, Object>() : subMap.get(dataGetKey);
                                    dumyMap.put(subDataGetKey,getStringValue);
                                }else{
                                    dumyMap.put(subDataGetKey,getStringValue);
                                }

                                subMap.put(dataGetKey,dumyMap); //sub map


                                List<Map<String,Object>> dumyListMap = Lists.newArrayList();
                                if(listRowNoCheck != tempRowNo){
                                    if(dataListMap.size() > 0){
                                        dumyListMap = dataListMap.get(dataGetKey) == null ? Lists.newArrayList() : dataListMap.get(dataGetKey);
                                        dumyListMap.add(dumyMap);
                                        dataListMap.put(dataGetKey,dumyListMap);
                                    }else{
                                        dumyListMap.add(dumyMap);
                                        dataListMap.put(dataGetKey,dumyListMap);
                                    }

                                    listRowNoCheck = tempRowNo;
                                }

                            }else {
                                dataMap.put(templateStringValue,getStringValue);
                            }
                        }else{

                            if (templateStringValue.startsWith(ExcelCellDataBean.STARTEXPRESSIONTOKEN) && templateStringValue.endsWith(ExcelCellDataBean.ENDEXPRESSIONTOKEN)) {
                                templateStringValue = templateStringValue.replace("${","");
                                templateStringValue = templateStringValue.replace("}","");
                                templateStringValue = templateStringValue.replace("$[","");
                                templateStringValue = templateStringValue.replace("]","");

                                if(templateStringValue.indexOf(".") > -1){ //  data.value  형식의 구조라면, 아래와 같이.

                                    String dataGetKey = "";
                                    String subDataGetKey = "";
                                    String[] splitValue = templateStringValue.split("\\.");
                                    dataGetKey = splitValue[0];
                                    subDataGetKey = splitValue[1];

                                    Map<String,Object> dumyMap = Maps.newHashMap();
                                    if(subMap.size() > 0){ // subMap에 현재 데이터가 들어갓을 경우,
                                        dumyMap = subMap.get(dataGetKey) == null ? new HashMap<String, Object>() : subMap.get(dataGetKey);
                                        dumyMap.put(subDataGetKey,getStringValue);
                                    }else{
                                        dumyMap.put(subDataGetKey,getStringValue);
                                    }

                                    subMap.put(dataGetKey,dumyMap); //sub map

                                    dataMap.put(dataGetKey,dumyMap);
                                }
                            }else {
                                templateStringValue = templateStringValue.replace("${","");
                                templateStringValue = templateStringValue.replace("}","");
                                templateStringValue = templateStringValue.replace("$[","");
                                templateStringValue = templateStringValue.replace("]","");
                                dataMap.put(templateStringValue,getStringValue);
                            }
                        }

                        if(dataListMap.size() > 0) {
                            dataMap.put("list",dataListMap);
                        }
                    }
                }
            }
        }
        return dataMap;
    }


    private List<SheetInfoBean> compareSendTemplateDataAndMailKey(List<SheetInfoBean> getExcelSheetList, FileItem excelFileItem, String sendEmailWorkId) throws Exception{
        List<SheetInfoBean> templateSheetList = new ArrayList<SheetInfoBean>();

        // 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.
        Workbook sendMailTemplateWorkBook = new XSSFWorkbook(OPCPackage.open(excelFileItem.getInputStreamContents()));

        //해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
        int sheetCnt = sendMailTemplateWorkBook.getNumberOfSheets();

        // SheetCnt 개수만큼 시트별 데이터를 bean에 담는다.
        for(int i = 0; i < sheetCnt; i++) {

            SheetInfoBean sheetInfo = new SheetInfoBean();

            // Excel에 대한 데이터를 가져온다.
            XSSFSheet source = ((XSSFWorkbook) sendMailTemplateWorkBook).getSheetAt(i);

            for(SheetInfoBean getSheetInfo : getExcelSheetList){

                String sourceSheetName = source.getSheetName() == null? "" : source.getSheetName();
                String getSheetInfoSheetName =  getSheetInfo.getXls_work_sht_nm() == null? "" :  getSheetInfo.getXls_work_sht_nm();

                //SheetName은 독립적이기에 비교하여, 처리 가능. SheetInfo를 여기서 가져옴.
                if(sourceSheetName.equals(getSheetInfoSheetName)) {


                    String emailWorkId = getSheetInfo.getEmail_work_id();
                    String xlsWorkSht = getSheetInfo.getExcel_sht_uuid();

                    sheetInfo.setEmail_work_id(sendEmailWorkId);
                    sheetInfo.setExcel_sht_uuid(xlsWorkSht);
                    sheetInfo.setXls_work_sht_nm(source.getSheetName());

                    //Excel에 ROW / CELL 정보를 취합한다.
                    List<RowInfoBean> sheetRow = readExcel(source,sendEmailWorkId,xlsWorkSht);

                    //EXCEL에서 읽어온 정보를 기준으로 ROW LIST를 SET 한다.
                    sheetInfo.setRowList(sheetRow);

                    break;
                }
            }


            //Excel에 ROW / CELL 정보를 취합한다.
            List<RowInfoBean> sheetRow = readExcel(source,sheetInfo.getEmail_work_id(),sheetInfo.getExcel_sht_uuid());

            //EXCEL에서 읽어온 정보를 기준으로 ROW LIST를 SET 한다.
            sheetInfo.setRowList(sheetRow);

            //ArrayList add
            templateSheetList.add(sheetInfo);
        }

        return templateSheetList;
    }

    private List<SheetInfoBean> findSheetCountForInitData(XSSFWorkbook sourceWorkBook, String sendEmailWorkId, String sendEmailWorkCd) {
        //해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
        int getExcelSheetCnt = sourceWorkBook.getNumberOfSheets();
        List<SheetInfoBean> getExcelSheetList = new ArrayList<SheetInfoBean>();

        // SheetCnt 개수만큼 시트별 데이터를 bean에 담는다.
        for(int i = 0; i < getExcelSheetCnt; i++) {

            SheetInfoBean sheetInfo = new SheetInfoBean();
            // 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.

            //Sheet에 대한 UUID
            String xlsWorkSht = UUID.randomUUID().toString();

            // Excel에 대한 데이터를 가져온다.
            XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(i);

            sheetInfo.setEmail_work_id(sendEmailWorkId);
            sheetInfo.setExcel_sht_uuid(xlsWorkSht);
            sheetInfo.setXls_work_sht_nm(source.getSheetName());



            //Excel에 ROW / CELL 정보를 취합한다.
            List<RowInfoBean> sheetRow = readExcel(source,sendEmailWorkId,xlsWorkSht);

            //EXCEL에서 읽어온 정보를 기준으로 ROW LIST를 SET 한다.
            sheetInfo.setRowList(sheetRow);

            //ArrayList add
            getExcelSheetList.add(sheetInfo);
        }

        return getExcelSheetList;
    }

    private FileItem fileExistCheck(String attNo) throws Exception{
	    FileList fileList = stdFileService.findFileListWithContents(attNo);
        if(fileList == null || fileList.getSize() == 0){
            throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
        }

        FileItem excelFileItem = null;
        for(FileItem fileItem : fileList.getItems()){
            if("xlsx".equals(fileItem.getExtension())){
                excelFileItem = fileItem;
                break;
            }else if(fileItem.getName().indexOf("xlsx") > -1){
                excelFileItem = fileItem;
                break;
            }

        }

        /*if(excelFileItem == null){
            try{
                throw new FileNotFoundException("엑셀파일이 없습니다");
            }catch (Exception e){
                LOG.error(e.getMessage());
            }
        }else{
            try {
                excelFileItem = fileService.findDownloadItem(excelFileItem.getId());
            } catch (Exception e1) {
                try{
                    throw new FileNotFoundException("파일을 가져오는 중 오류발생!");
                }catch (Exception e){
                    LOG.error(e.getMessage());
                }

            }
        }*/
        return excelFileItem;
    }

    private Map<String, Object> mailReceiveExcelFileGetInfo(String emailWorkTargId, String emailSndLogId) {
        Map<String,Object> resultGetExcelFindKey = Maps.newHashMap();
        resultGetExcelFindKey.put("eml_task_subj_uuid",emailWorkTargId);
        resultGetExcelFindKey.put("eml_snd_histrec_uuid",emailSndLogId);

        //가져온 키 값으로, 조회한다. (temp_file_template 확인용 )
        return sqlSession.selectOne("mail-work.mailReceiveExcelFileGetInfo",resultGetExcelFindKey);
    }

    private Map<String,Object> findMailExcelCheck(FileItem fileitem) throws Exception {
        String emailWorkTargId = "";
        String emailSndLogId = "";
        Workbook sourceWorkBook = null;

        Map<String,Object> excelInfoMap = Maps.newHashMap();

        // 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.
        sourceWorkBook = new XSSFWorkbook(OPCPackage.open(fileitem.getInputStreamContents()));

        SheetInfoBean sheetInfo = new SheetInfoBean();

        // Excel에 대한 데이터를 가져온다.
        XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(0); //첫번째 Sheet / row를 기준으로 생각한다.
        XSSFRow srcRow = source.getRow(0);

        XSSFCell emailWorkTargIdNewCell = srcRow.getCell(1); // n
        XSSFCell emailSndLogIdNewCell = srcRow.getCell(2);



        // EML_TASK_SUBJ_UUID
        if( null != emailWorkTargIdNewCell){
            emailWorkTargId = emailWorkTargIdNewCell.getStringCellValue() == null? "" : emailWorkTargIdNewCell.getStringCellValue();
        }

        // EML_SND_HISTREC_UUID
        if( null != emailSndLogIdNewCell){
            emailSndLogId = emailSndLogIdNewCell.getStringCellValue() == null? "" : emailSndLogIdNewCell.getStringCellValue();
        }

        //주요키값을 가져온다. 해당 키값을 가지고, EASMWSH에서 TEMP_FILE을 조회해야한다.. ( mail send 이전 template file , data와 매칭 시켜야함. )
        if(StringUtils.isEmpty(emailWorkTargId)){
            try{
                throw new FileNotFoundException("메일 EXCEL 내에 EML_TASK_SUBJ_UUID 가 존재하지 않습니다.");
            }catch (Exception e){
                LOG.error(e.getMessage());
            }
        }

        if(StringUtils.isEmpty(emailSndLogId)){
            try{
                throw new FileNotFoundException("메일 EXCEL 내에 EML_SND_HISTREC_UUID 가 존재하지 않습니다.");
            }catch (Exception e){
                LOG.error(e.getMessage());
            }
        }

        excelInfoMap.put("emailSndLogId",emailSndLogId);
        excelInfoMap.put("emailWorkTargId",emailWorkTargId);
        excelInfoMap.put("sourceWorkBook",sourceWorkBook);

        return excelInfoMap;
    }
}
