package smartsuite.app.common.excel;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartsuite.app.common.excel.bean.CellInfoBean;
import smartsuite.app.common.excel.bean.ExcelCellDataBean;
import smartsuite.app.common.excel.bean.RowInfoBean;
import smartsuite.app.common.excel.bean.SheetInfoBean;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.SimpleMultipartFileItem;
import smartsuite.upload.util.AthfServiceUtil;

import javax.inject.Inject;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ExcelCreateService {

	static final Logger LOG = LoggerFactory.getLogger(ExcelCreateService.class);

	@Inject
	ExcelCreateCellService excelCreateCellService;

	@Value("#{file['file.upload.path']}")
	String fileUploadPath;

	@Inject
	StdFileService stdFileService;


	/**
	 * Sheet를 복사하여, 복사한 시트에 행 (ROW) 체크하여, Row 별로 Copy가 될수있도록 처리한다.
	 * @param rowList
	 * @param destination
	 */
	public static void copySheet(List<RowInfoBean> rowList, XSSFSheet destination) {
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			XSSFRow destRow = destination.createRow(sheetRow.getExcel_row_no());
			ExcelCreateCellService.copyRow( sheetRow, destRow);
			destination.autoSizeColumn(i);
			// sheet별이 아닌 excel sheet 전체를 돌리게 끔하여서 가져와야할껏으로 보임.
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}

	/**
	 * 이메일 업무 관리 화면에서 Template 만을 복사하려할때 사용하는 Method 첫번째 ( 1번째 row에 1번째/2번째 Cell에 선택하지 않으면 보이지 않는 Key값을 숨겨둔다. )
	 * eml_task_subj_uuid : mail set id / excel info 를 연결하는 Key
	 * eml_snd_histrec_uuid : 이메일 업무 발송 시 정보를 취득할수있는 Key ( mail send key )
	 * 해당 메소드에서는 Sheet를 생성하고, 기본적인 1차원적인 값들을 저장한다.
	 *
	 * @param sheetList
	 * @param destWorkbook
	 * @param dataMapForSheet
	 * @param headersList
	 * @return
	 */
	public static Map<String,Object> createExcelByListDataSetup(List<SheetInfoBean> sheetList, XSSFWorkbook destWorkbook, Map<String, Object> dataMapForSheet , List<Map<String,Object>> headersList) {
		Map<String,Object> resultMap = Maps.newHashMap();

		for (int i = 0; i < sheetList.size(); i++) {
			SheetInfoBean sheet = sheetList.get(i);

			XSSFSheet destSheet = destWorkbook.createSheet(sheet.getXls_work_sht_nm());

			if(i==0){ //기본 정보 입력
				resultMap = ExcelCreateCellService.createFirstCell(destSheet);
			}

			//sheet별로 데이터를 다르게 저장해놨음.
			Map<String, Object> dataListRow = (Map<String, Object>) dataMapForSheet.get(sheet.getExcel_sht_uuid());
			ExcelCreateCellService.createExcelForMapList(sheet.getRowList(), destSheet,dataListRow,headersList);
		}
		return resultMap;
	}

	/**
	 * 업무에서 이메일 견적을 생성할 시 추후 회신 시에 넘어오는 excel 파일이 정상적인지 판단하기 위함 및 cell index 위치 및 row의 치환을 위한 여러가지 방편으로 data map에 있는 list 및 각 객체를 위하여 발송전 template를 생성
	 * 해당 파일은 tmpl_athg_uuid 로 저장되며, 회신 온 메일과 비교 분석하여, result map으로 나타나게 합니다.
	 *
	 * @param sheetList
	 * @param dataListSheet
	 * @param headersList
	 * @return
	 */
	public Map<String,Object> createEmailWorkExcelTemplate(List<SheetInfoBean> sheetList, Map<String,Object> dataMap , Map<String, Map<String, Map<String, Map<String, Object>>>> dataListSheet, List<Map<String,Object>> headersList){

		Map<String,Object> resultMap = Maps.newHashMap();
		OutputStream out = null;
		XSSFWorkbook tempWorkBook = null;
		File file = null;
		try{

			String fileNm = UUID.randomUUID() + ".xlsx";

			file = new File(fileUploadPath + FilenameUtils.getExtension(fileNm));

			tempWorkBook = new XSSFWorkbook();

			out = new FileOutputStream(file);

			for (int i = 0; i < sheetList.size(); i++) {

				SheetInfoBean sheet = sheetList.get(i);


				XSSFSheet destSheet = tempWorkBook.createSheet(sheet.getXls_work_sht_nm());

				if(i==0){ //기본 정보 입력
					String emailWorkTargId = UUID.randomUUID().toString();
					resultMap = excelCreateCellService.createTemplateFirstCell(destSheet,emailWorkTargId);
				}

				if(MapUtils.isNotEmpty(dataListSheet)){
					//sheet별로 데이터를 다르게 저장해놨음.
					Map<String,Map<String,Map<String,Object>>> dataListRow = dataListSheet.get(sheet.getExcel_sht_uuid());
					createExcelForMapListTemplateCreate(sheet.getRowList(), destSheet,dataListRow,headersList);
				}else{
					createEmailWorkExcelForMapTemplateCreate(sheet.getRowList(), destSheet,dataMap,headersList);
				}
			}


			tempWorkBook.write(out);

			String grpCd = UUID.randomUUID().toString();
			
			SimpleMultipartFileItem fileItem = AthfServiceUtil.newMultipartFileItem(
					UUID.randomUUID().toString(),
					grpCd,
					fileNm,
					file
			);
			stdFileService.createWithMultipart(fileItem);

			resultMap.put("tmpl_athg_uuid",grpCd);  // template용 file
		}catch (Exception e){
			LOG.error(e.getMessage());
		}finally{
			try {
				if(tempWorkBook != null) tempWorkBook.close();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			try {
				if(out != null) out.close();
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}

		return resultMap;
	}


	// DATA를 받아와 해당 데이터만큼의 VALUE의 SIZE를 계산하여, 미리 TEMPLATE를 생성
	public static void createEmailWorkExcelForMapTemplateCreate(List<RowInfoBean> rowList, XSSFSheet destination , Map<String,Object> dataMap , List<Map<String,Object>> headersList ) {
		int maxColumnNum = 0;
		int headerNextRow = 0;

		int lastDataRow = 0;

		Map<String,Object> listDataMap = Maps.newHashMap();

		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			int rowNo = sheetRow.getExcel_row_no();
			int rowCreateNum = 0;

			if(rowNo == 0) continue;

			Map<String,Object> checkValueMap = checkCellListAndValueRetrunMapType(sheetRow);
			int replaceType = checkValueMap.get("checkReplaceType") == null? 0 : Integer.parseInt(checkValueMap.get("checkReplaceType").toString()); //value의 list or object type
			String getValue = checkValueMap.get("getValue") == null? "" : checkValueMap.get("getValue").toString();  // data의 map을 체크하는 value



			if(replaceType == ExcelCellDataBean.REPLACE_TYPE_LIST){
				List<Map<String,Object>> dataList = (List<Map<String, Object>>) dataMap.get(getValue);

				if (rowCreateNum == 0)  rowCreateNum = rowNo;

				int lastDataList = dataList.size();

				for(int b = 0; b < lastDataList; b++){
					XSSFRow destRow = destination.createRow(rowCreateNum);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
					listDataMap.put(Integer.toString(rowCreateNum) , dataList.get(b));
					rowCreateNum++;
				}
				lastDataRow = lastDataList;

			}else if(replaceType == ExcelCellDataBean.REPLACE_TYPE_VALUE){
				Map<String,Object> dataList = (Map<String,Object>)dataMap.get(getValue);

				if (rowCreateNum == 0)  rowCreateNum = rowNo;
				if(lastDataRow != 0) {
					XSSFRow destRow = destination.createRow(rowCreateNum+lastDataRow);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
				}else{
					XSSFRow destRow = destination.createRow(rowCreateNum);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
				}
			}else{
				if (rowCreateNum == 0)  rowCreateNum = rowNo;
				XSSFRow destRow = destination.createRow(rowCreateNum+lastDataRow);
				ExcelCreateCellService.copyRow(sheetRow, destRow);
			}
		}

		dataMap.put("listDataMap",listDataMap);
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			destination.autoSizeColumn(i);
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}


	// DATA를 받아와 해당 데이터만큼의 VALUE의 SIZE를 계산하여, 미리 TEMPLATE를 생성
	public static void createExcelForMapListTemplateCreate(List<RowInfoBean> rowList, XSSFSheet destination , Map<String,Map<String,Map<String,Object>>> dataMapForRow , List<Map<String,Object>> headersList ) {
		int maxColumnNum = 0;
		int headerNextRow = 0;

		int lastDataRow = 0;
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			int rowNo = sheetRow.getExcel_row_no();
			int rowCreateNum = 0;

			if(rowNo == 0) continue;

			String getRowNo = Integer.toString(sheetRow.getExcel_row_no());
			Map<String,Map<String,Object>> dataList = dataMapForRow.get(getRowNo);

			int replaceType = ExcelCreateCellService.checkCellListAndValue(sheetRow);

			if(replaceType == ExcelCellDataBean.REPLACE_TYPE_LIST){

				if (rowCreateNum == 0)  rowCreateNum = rowNo;

				int lastDataList = dataList.size();

				for(int b = 0; b < lastDataList; b++){
					XSSFRow destRow = destination.createRow(rowCreateNum);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
					rowCreateNum++;
					lastDataRow = rowCreateNum;
				}

			}else if(replaceType == ExcelCellDataBean.REPLACE_TYPE_VALUE){

				if (rowCreateNum == 0)  rowCreateNum = rowNo;
				int lastDataList = dataList.size();
				if(lastDataRow != 0) {
					XSSFRow destRow = destination.createRow(rowCreateNum+lastDataList);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
				}else{
					XSSFRow destRow = destination.createRow(rowCreateNum);
					ExcelCreateCellService.copyRow(sheetRow, destRow);
				}
			}else{
				if (rowCreateNum == 0)  rowCreateNum = rowNo;
				XSSFRow destRow = destination.createRow(rowCreateNum);
				ExcelCreateCellService.copyRow(sheetRow, destRow);
			}
		}
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			destination.autoSizeColumn(i);
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}



	/**
	 * Cell의 String Value내에 객체를 표기하는 형태가 존재하는지 찾고, 분류값을 지정한다. ( 해당 Method는 Row 내에 1개의 구분값(리스트,단일객체)가 있다는 전재하이다.)
	 * -- List 객체의 경우  $[ ]
	 * -- 단일 객체의 경우  ${ }
	 * @param srcRow
	 * @return
	 */
	public static Map<String,Object> checkCellListAndValueRetrunMapType(  RowInfoBean srcRow) {
		boolean checkReplaceValue = false;
		boolean checkReplaceList = false;

		// 현재까지 기준으론 1개의 row에 단일 또는 리스트로만 정의하도록 한다.
		int checkReplaceType = 0;
		String getValue = "";
		Map<String,Object> resultMap = Maps.newHashMap();


		List<CellInfoBean> rowList = srcRow.getCellList();

		for (int i = 0; i < rowList.size(); i++) {
			CellInfoBean cellInfoBean = rowList.get(i);
			getValue = cellInfoBean.getStr_cel_val() == null? "" : cellInfoBean.getStr_cel_val();

			if(!StringUtils.isEmpty(getValue)){
				if (getValue.startsWith(ExcelCellDataBean.STARTEXPRESSIONTOKEN) && getValue.endsWith(ExcelCellDataBean.ENDEXPRESSIONTOKEN)) {
					checkReplaceValue = true;
					getValue = getValue.replace(ExcelCellDataBean.STARTEXPRESSIONTOKEN,"");
					getValue = getValue.replace(ExcelCellDataBean.ENDEXPRESSIONTOKEN,"");

					if(!StringUtils.isEmpty(getValue)){
						String dataGetKey = "";
						if(getValue.indexOf(".") > -1){ // ${ data.value } 형식의 구조라면, 아래와 같이.
							String[] splitValue = getValue.split("\\.");
							getValue = splitValue[0];
						}
					}
					break;
				}else if(getValue.startsWith(ExcelCellDataBean.STARTFORMULATOKEN) && getValue.endsWith(ExcelCellDataBean.ENDFORMULATOKEN)){
					checkReplaceList = true;
					getValue = getValue.replace(ExcelCellDataBean.STARTFORMULATOKEN,"");
					getValue = getValue.replace(ExcelCellDataBean.ENDFORMULATOKEN,"");

					if(!StringUtils.isEmpty(getValue)){
						String dataGetKey = "";
						if(getValue.indexOf(".") > -1){ // ${ data.value } 형식의 구조라면, 아래와 같이.
							String[] splitValue = getValue.split("\\.");
							getValue = splitValue[0];
						}
					}
					break;
				}
			}
		}


		if(checkReplaceList){  // 리스트
			checkReplaceType = ExcelCellDataBean.REPLACE_TYPE_LIST;
		}else if(checkReplaceValue){  // 단일
			checkReplaceType = ExcelCellDataBean.REPLACE_TYPE_VALUE;
		}else{
			checkReplaceType = ExcelCellDataBean.REPLACE_TYPE_NONE;
		}

		resultMap.put("checkReplaceType",checkReplaceType);
		resultMap.put("getValue",getValue);
		return resultMap;
	}



	public Map<String, Object> createEmailWorkExcelTemplateProcess(List<SheetInfoBean> sheetList, Map<String, Object> dataMap, Map<String, Map<String, Map<String, Map<String, Object>>>> dataListSheet, List<Map<String, Object>> headersList, String fileNm, String athgUuid) {
		int maxColumnNum = 0;
		Map<String,Object> resultMap = Maps.newHashMap();

		OutputStream out = null;
		Workbook destinationWorkBook = null;
		XSSFSheet destination = null;
		File file = null;

		try{
			//temp file
			file = new File(fileUploadPath  + UUID.randomUUID() + ".xlsx");

			destinationWorkBook = new XSSFWorkbook();

			out = new FileOutputStream(file);


			//우선 template를 만든다.
			resultMap = createEmailWorkExcelTemplate(sheetList,dataMap,dataListSheet,headersList);

			/**
			 * resultMap
			 * - eml_task_subj_uuid
			 * - eml_snd_histrec_uuid
			 * - tmpl_athg_uuid  ( template grp_cd )
			 *
			 */

			//template -> file read & sheet bean 객체로 변환
			List<SheetInfoBean> tmpSheetList = excelCreateCellService.excelFileInsertProc(resultMap,sheetList);

			//data만 밀어넣는다.
			createEmailWorkExcelDataSetup(tmpSheetList,(XSSFWorkbook)destinationWorkBook,dataMap,headersList,resultMap);

			destinationWorkBook.write(out);

			SimpleMultipartFileItem fileItem = AthfServiceUtil.newMultipartFileItem(
					UUID.randomUUID().toString(),
					athgUuid,
					fileNm,
					file
			);
			stdFileService.createWithMultipart(fileItem);
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			try {
				if (out != null) out.close();
				if (destinationWorkBook != null) destinationWorkBook.close();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}

		return resultMap;
	}



	/**
	 * template 로 만들어둔 excel 파일의 변수명에 맞춰 넘어온 data map과 맞춰 치환 시키는 method
	 * @param sheetList
	 * @param destWorkbook
	 * @param dataListSheet
	 * @param headersList
	 * @param templateCreationMap
	 */
	public static  void createEmailWorkExcelDataSetup(List<SheetInfoBean> sheetList, XSSFWorkbook destWorkbook,Map<String,Object> dataMap ,List<Map<String,Object>> headersList , Map<String,Object> templateCreationMap){

		Map<String,Object> resultMap = Maps.newHashMap();
		OutputStream out = null;
		InputStream io = null;

		for (int i = 0; i < sheetList.size(); i++) {
			SheetInfoBean sheet = sheetList.get(i);

			XSSFSheet destSheet = destWorkbook.createSheet(sheet.getXls_work_sht_nm());

			if(i==0){ //기본 정보 입력
				String emailWorkTargId = templateCreationMap.get("eml_task_subj_uuid") == null? UUID.randomUUID().toString() : templateCreationMap.get("eml_task_subj_uuid") .toString();
				ExcelCreateCellService.createTemplateFirstCell(destSheet,emailWorkTargId);
			}
			createEmailWorkExcelForDataMapMapping(sheet.getRowList(), destSheet,dataMap,headersList);
		}

	}

	/**
	 * LIST의 개수를 체크하면서, LIST 밑에 있는 단일 객체도 치환이 정상적으로 가능하도록 하여, 치환되도록 하는 Method
	 * @param rowList
	 * @param destination
	 * @param dataMapForRow
	 * @param headersList
	 */
	public static void createEmailWorkExcelForDataMapMapping(List<RowInfoBean> rowList, XSSFSheet destination , Map<String,Object> dataMap , List<Map<String,Object>> headersList ) {
		int maxColumnNum = 0;
		int headerNextRow = 0;
		int dumyRowCnt = 0;

		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			int rowCreateNum = sheetRow.getExcel_row_no();

			Map<String,Object> checkValueMap = checkCellListAndValueRetrunMapType(sheetRow);
			int replaceType = checkValueMap.get("checkReplaceType") == null? 0 : Integer.parseInt(checkValueMap.get("checkReplaceType").toString()); //value의 list or object type
			String getValue = checkValueMap.get("getValue") == null? "" : checkValueMap.get("getValue").toString();  // data의 map을 체크하는 value


			if(replaceType == ExcelCellDataBean.REPLACE_TYPE_LIST){
				String rowNo = Integer.toString(sheetRow.getExcel_row_no());

				Map<String,Object> listDataMap = (Map<String,Object>)dataMap.get("listDataMap");

				//data list 를 기준으로 copy 처리한다.
				XSSFRow destRow = destination.createRow(rowCreateNum);
				ExcelCreateCellService.copyRow(sheetRow, destRow);
				dataSetRowForDataMapValue( destRow ,   (Map<String,Object>)listDataMap.get(rowNo));
			}else if(replaceType == ExcelCellDataBean.REPLACE_TYPE_VALUE){

				int checkDataCnt = 0;
				Map<String,Object> dataList = (Map<String,Object>)dataMap.get(getValue);
				XSSFRow destRow = destination.createRow(rowCreateNum);
				ExcelCreateCellService.copyRow(sheetRow, destRow);

				//data list 를 기준으로 copy 처리한다.
				dataSetRowForDataMapValue( destination.getRow(rowCreateNum) , dataList);

			}else{
				XSSFRow destRow = destination.createRow(rowCreateNum);
				ExcelCreateCellService.copyRow(sheetRow, destRow);
			}
		}
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			destination.autoSizeColumn(i);
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}


	public static void dataSetRowForDataMapValue(  XSSFRow destRow , Map<String,Object> data) {
		//Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();

		for (int j = 0; j <= destRow.getLastCellNum(); j++) {
			XSSFCell getCell = destRow.getCell(j); // ancienne cell
			if(getCell == null) continue;

			String excelStringValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
			String getValue = "";

			if (excelStringValue.startsWith(ExcelCellDataBean.STARTEXPRESSIONTOKEN) && excelStringValue.endsWith(ExcelCellDataBean.ENDEXPRESSIONTOKEN)) {
				String dataGetKey = "";
				excelStringValue = excelStringValue.replace(ExcelCellDataBean.STARTEXPRESSIONTOKEN,"");
				excelStringValue = excelStringValue.replace(ExcelCellDataBean.ENDEXPRESSIONTOKEN,"");

				if(excelStringValue.indexOf(".") > -1){ // ${ data.value } 형식의 구조라면, 아래와 같이.

					String[] splitValue = excelStringValue.split("\\.");
					dataGetKey = splitValue[0];
					excelStringValue = splitValue[1];
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}else{ // ${ value } 형식의 구조라면, 아래와 같이.
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}
			} else if (excelStringValue.startsWith(ExcelCellDataBean.STARTFORMULATOKEN) && excelStringValue.endsWith(ExcelCellDataBean.ENDFORMULATOKEN)) {
				String dataGetKey = "";
				excelStringValue = excelStringValue.replace(ExcelCellDataBean.STARTFORMULATOKEN,"");
				excelStringValue = excelStringValue.replace(ExcelCellDataBean.ENDFORMULATOKEN,"");

				if(excelStringValue.indexOf(".") > -1){ // ${ data.value } 형식의 구조라면, 아래와 같이.
					String[] splitValue = excelStringValue.split("\\.");
					dataGetKey = splitValue[0];
					excelStringValue = splitValue[1];
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}else{ // ${ value } 형식의 구조라면, 아래와 같이.
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}
			}else{
				if(StringUtils.isEmpty(getValue)){
					getValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
				}
			}
			getCell.setCellValue(getValue);
		}
	}



}
