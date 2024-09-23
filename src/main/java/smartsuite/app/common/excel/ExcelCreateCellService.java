package smartsuite.app.common.excel;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ExcelCreateCellService {

	static final Logger LOG = LoggerFactory.getLogger(ExcelCreateCellService.class);


	static List<FormulaInfo> formulaInfoList = new ArrayList<FormulaInfo>();


	@Inject
	StdFileService stdFileService;

	@Inject
	ExcelReaderUtil excelReaderUtil;

	@Value("#{file['file.upload.path']}")
	String fileUploadPath;


	/**
	 * Cell의 String Value내에 객체를 표기하는 형태가 존재하는지 찾고, 분류값을 지정한다. ( 해당 Method는 Row 내에 1개의 구분값(리스트,단일객체)가 있다는 전재하이다.)
	 * -- List 객체의 경우  $[ ]
	 * -- 단일 객체의 경우  ${ }
	 * @param srcRow
	 * @return
	 */
	public static int checkCellListAndValue(  RowInfoBean srcRow) {
		boolean checkReplaceValue = false;
		boolean checkReplaceList = false;

		// 현재까지 기준으론 1개의 row에 단일 또는 리스트로만 정의하도록 한다.
		int checkReplaceType = 0;

		for(CellInfoBean cellInfoBean : srcRow.getCellList()){
			String getValue = cellInfoBean.getStr_cel_val() == null? "" : cellInfoBean.getStr_cel_val();
			if(StringUtils.isNotEmpty(getValue)){
				if (getValue.startsWith(ExcelCellDataBean.STARTEXPRESSIONTOKEN) && getValue.endsWith(ExcelCellDataBean.ENDEXPRESSIONTOKEN)) {
					checkReplaceValue = true;
					break;
				}else if(getValue.startsWith(ExcelCellDataBean.STARTFORMULATOKEN) && getValue.endsWith(ExcelCellDataBean.ENDFORMULATOKEN)){
					checkReplaceList = true;
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

		return checkReplaceType;
	}


	/**
	 *  Merge Cell을 판별하기 위한 Method
	 */
	public static class CellRangeAddressWrapper implements Comparable<CellRangeAddressWrapper> {

		public CellRangeAddress range;

		public CellRangeAddressWrapper(CellRangeAddress theRange) {
			this.range = theRange;
		}

		public int compareTo(CellRangeAddressWrapper o) {
			if (range.getFirstColumn() < o.range.getFirstColumn()
					&& range.getFirstRow() < o.range.getFirstRow()) {
				return -1;
			} else if (range.getFirstColumn() == o.range.getFirstColumn()
					&& range.getFirstRow() == o.range.getFirstRow()) {
				return 0;
			} else {
				return 1;
			}
		}
	}


	/**
	 * Cell type을 측정하기 위하여 별도로 구성하였으나, 기본적인 형태의 Template 라면 별도로 구현은 하지 않아도 될것으로 판단.
	 * @param workbook
	 */
	public static void refreshFormula(XSSFWorkbook workbook) {
		for (FormulaInfo formulaInfo : formulaInfoList) {
			workbook.getSheet(formulaInfo.getSheetName()).getRow(formulaInfo.getRowIndex())
					.getCell(formulaInfo.getCellIndex()).setCellFormula(formulaInfo.getFormula());
		}
		formulaInfoList.removeAll(formulaInfoList);
	}

	/**
	 * Merge Cell 가져오기 Method
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 * @return
	 */
	public static CellRangeAddress getMergedRegion(XSSFSheet sheet, int rowNum, short cellNum) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress merged = sheet.getMergedRegion(i);
			if (merged.isInRange(rowNum, cellNum)) {
				return merged;
			}
		}
		return null;
	}


	public static Map<String, Object> createFirstCell(XSSFSheet destSheet) {
		Map<String,Object> resultMap = Maps.newHashMap();
		String emailWorkTargId = UUID.randomUUID().toString();
		XSSFRow destRow = destSheet.createRow(0);
		CellStyle rowStyle = destRow.getRowStyle();
		DataFormat newDataFormat = destSheet.getWorkbook().createDataFormat();

		XSSFCell emailWorkTargIdNewCell = destRow.createCell(1); // new cell
		CellInfoBean emailWorkTargIdCell  = new CellInfoBean();
		emailWorkTargIdCell.setStr_cel_val(emailWorkTargId); //eml_task_subj_uuid
		emailWorkTargIdCell.setExcel_cel_typ(1);
		emailWorkTargIdCell.setHide_yn(true);
		emailWorkTargIdCell.setCel_brd_bttm_cd((short)0);
		emailWorkTargIdCell.setCel_brd_lft_cd((short)0);
		emailWorkTargIdCell.setCel_brd_rgt_cd((short)0);
		emailWorkTargIdCell.setCel_brd_top_cd((short)0);
		emailWorkTargIdCell.setDat_fmt(newDataFormat.getFormat(";;;"));
		copyCell(emailWorkTargIdCell, emailWorkTargIdNewCell);



		String emailSndLogId = UUID.randomUUID().toString();
		XSSFCell emailSndLogIdNewCell = destRow.createCell(2); // new cell
		CellInfoBean emailSndLogIdCell  = new CellInfoBean();
		emailSndLogIdCell.setStr_cel_val(emailSndLogId); //eml_snd_histrec_uuid
		emailSndLogIdCell.setExcel_cel_typ(1);
		copyCell(emailSndLogIdCell, emailSndLogIdNewCell);

		resultMap.put("eml_task_subj_uuid",emailWorkTargId);
		resultMap.put("eml_snd_histrec_uuid",emailSndLogId);

		return resultMap;
	}




	/**
	 *  넘어온 Data Map 에 맞게 객체들을 생성 + 치환하는 형태로 구현
	 *   -> excel cell 내에 list 형이 있다면, data map list count 만큼 행을 생성하여, 치환
	 *   -> excel cell 내에 단일 객체가 있다면, data map에서 해당 객체들을 find 하여, 치환
	 *
	 * @param rowList
	 * @param destination
	 * @param dataListRow
	 * @param headersList
	 */
	public static void createExcelForMapList(List<RowInfoBean> rowList, XSSFSheet destination , Map<String, Object> dataListRow , List<Map<String,Object>> headersList ) {
		int maxColumnNum = 0;
		int headerNextRow = 0;

		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			int rowNo = sheetRow.getExcel_row_no();
			int rowCreateNum = 0;

			if(rowNo == 0) continue;

			int replaceType = checkCellListAndValue(sheetRow);

			if(replaceType == ExcelCellDataBean.REPLACE_TYPE_LIST){

				if (rowCreateNum == 0)  rowCreateNum = rowNo;

				int dumyCreateNum = sheetRow.getExcel_row_no();
				String getRowNo = Integer.toString(sheetRow.getExcel_row_no());
				Map<String,Map<String,Object>> dataList = (Map<String, Map<String, Object>>) dataListRow.get(getRowNo);

				int lastDataList = dataList.size();

				for(int b = 0; b < lastDataList; b++){
					XSSFRow destRow = destination.createRow(dumyCreateNum);
					copyRow(sheetRow, destRow);
					dumyCreateNum++;
				}

				for(int a = 0; a < lastDataList; a++){
					Map<String,Object> data = dataList.get(Integer.toString(rowCreateNum));
					//data list 를 기준으로 copy 처리한다.
					dataSetRowForMapList( destination.getRow(rowCreateNum) , data);
					rowCreateNum++; // create할때마다 1을 증가시켜서 create하는 row가 계속 증식 가능하도록 처리한다.
				}

			}else if(replaceType == ExcelCellDataBean.REPLACE_TYPE_VALUE){

				if (rowCreateNum == 0)  rowCreateNum = rowNo;

				String getRowNo = Integer.toString(sheetRow.getExcel_row_no());
				Map<String,Map<String,Object>> dataList = (Map<String, Map<String, Object>>) dataListRow.get(getRowNo);

				Map<String,Object> data = dataList.get(Integer.toString(rowCreateNum));

				XSSFRow destRow = destination.createRow(rowCreateNum);
				copyRow(sheetRow, destRow);

				//data list 를 기준으로 copy 처리한다.
				dataSetRowForMapValue( destination.getRow(rowCreateNum) , data);

			}else{
				if (rowCreateNum == 0)  rowCreateNum = rowNo;
				XSSFRow destRow = destination.createRow(rowCreateNum);
				copyRow(sheetRow, destRow);
			}

		}
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			destination.autoSizeColumn(i);
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}


	/**
	 * String value 내에 ${ }가 존재한다면, 이를 replace 처리하고, "." 이 객체안에 존재할 경우, 이를 key 값으로 보아 main key / sub key로 구분하여, 넘어온 data map에서 찾아 치환 하도록 한다.
	 * 단일 객체 치환하는 Method
	 * @param destRow
	 * @param data
	 */
	public static void dataSetRowForMapList(  XSSFRow destRow , Map<String,Object> data) {
		//Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();

		for (int j = 0; j <= destRow.getLastCellNum(); j++) {
			XSSFCell getCell = destRow.getCell(j); // ancienne cell
			if(getCell == null) continue;

			String excelStringValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
			excelStringValue = excelStringValue.replace(ExcelCellDataBean.STARTFORMULATOKEN,"");
			excelStringValue = excelStringValue.replace(ExcelCellDataBean.ENDFORMULATOKEN,"");

			String getValue = "";

			if(!StringUtils.isEmpty(excelStringValue)){
				String dataGetKey = "";

				if(excelStringValue.indexOf(".") > -1){ // $[ data.value ] 형식의 구조라면, 아래와 같이.

					String[] splitValue = excelStringValue.split("\\.");
					dataGetKey = splitValue[0];
					excelStringValue = splitValue[1];
					Map<String,Object> getValueMap = (Map<String,Object>)data.get(dataGetKey) == null? null : (Map<String,Object>)data.get(dataGetKey);
					if(null != getValueMap){
						getValue = getValueMap.get(excelStringValue) == null? "" : getValueMap.get(excelStringValue).toString();
					}
				}else{ // $[ value ] 형식의 구조라면, 아래와 같이.
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}
			}

			if(StringUtils.isEmpty(getValue)){
				getValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
			}

			getCell.setCellValue(getValue);
		}
	}


	/**
	 * String value 내에  $[ ]가 존재한다면, 이를 replace 처리하고, "." 이 객체안에 존재할 경우, 이를 key 값으로 보아 main key / sub key로 구분하여, 넘어온 data map에서 찾아 치환 하도록 한다.
	 * LIST 형에 대한 치환 처리 Method
	 * @param destRow
	 * @param data
	 */
	public static void dataSetRowForMapValue(  XSSFRow destRow , Map<String,Object> data) {
		//Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();

		for (int j = 0; j <= destRow.getLastCellNum(); j++) {
			XSSFCell getCell = destRow.getCell(j); // ancienne cell
			if(getCell == null) continue;

			String excelStringValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
			excelStringValue = excelStringValue.replace(ExcelCellDataBean.STARTEXPRESSIONTOKEN,"");
			excelStringValue = excelStringValue.replace(ExcelCellDataBean.ENDEXPRESSIONTOKEN,"");

			String getValue = "";

			if(!StringUtils.isEmpty(excelStringValue)){
				String dataGetKey = "";

				if(excelStringValue.indexOf(".") > -1){ // ${ data.value } 형식의 구조라면, 아래와 같이.

					String[] splitValue = excelStringValue.split("\\.");
					dataGetKey = splitValue[0];
					excelStringValue = splitValue[1];
					Map<String,Object> getValueMap = (Map<String,Object>)data.get(dataGetKey) == null? null : (Map<String,Object>)data.get(dataGetKey);
					if(null != getValueMap){
						getValue = getValueMap.get(excelStringValue) == null? "" : getValueMap.get(excelStringValue).toString();
					}
				}else{ // ${ value } 형식의 구조라면, 아래와 같이.
					getValue = data.get(excelStringValue) == null? "" : data.get(excelStringValue).toString();
				}
			}

			if(StringUtils.isEmpty(getValue)){
				getValue = getCell.getStringCellValue() == null? "" : getCell.getStringCellValue();
			}

			getCell.setCellValue(getValue);
		}
	}

	/**
	 * template 로 만들어둔 excel 파일의 변수명에 맞춰 넘어온 data map과 맞춰 치환 시키는 method
	 * @param sheetList
	 * @param destWorkbook
	 * @param dataListSheet
	 * @param headersList
	 * @param templateCreationMap
	 */
	public static  void createExcelDataSetup(List<SheetInfoBean> sheetList, XSSFWorkbook destWorkbook,Map<String,Map<String,Map<String,Map<String,Object>>>> dataListSheet ,List<Map<String,Object>> headersList , Map<String,Object> templateCreationMap){

		Map<String,Object> resultMap = Maps.newHashMap();
		OutputStream out = null;
		InputStream io = null;

		for (int i = 0; i < sheetList.size(); i++) {
			SheetInfoBean sheet = sheetList.get(i);

			XSSFSheet destSheet = destWorkbook.createSheet(sheet.getXls_work_sht_nm());

			if(i==0){ //기본 정보 입력

				String emailWorkTargId = templateCreationMap.get("eml_task_subj_uuid") == null? UUID.randomUUID().toString() : templateCreationMap.get("eml_task_subj_uuid") .toString();
				createTemplateFirstCell(destSheet,emailWorkTargId);
			}

			//sheet별로 데이터를 다르게 저장해놨음.
			Map<String,Map<String,Map<String,Object>>> dataListRow = dataListSheet.get(sheet.getExcel_sht_uuid());
			createExcelForMapListDataCreate(sheet.getRowList(), destSheet,dataListRow,headersList);
		}

	}







	/**
	 * 메일 회신으로 넘어온 excel에 대해 첫번째 row / 첫번째 cell & 두번째 cell의 값을 찾아오는 메소드 ( 해당 값들을 이용하여, copy 처리 & 비교 처리 )
	 * @param param
	 * @param getSheetList
	 * @return
	 */
	public List<SheetInfoBean> excelFileInsertProc(Map<String,Object> param,List<SheetInfoBean> getSheetList){

		// SheetInfoBean List new
		List<SheetInfoBean> sheetList = new ArrayList<SheetInfoBean>();

		try{

			//미리 만들어둔 temp file grp_cd
			String attNo = param.get("tmpl_athg_uuid") == null? "" : param.get("tmpl_athg_uuid").toString();


			FileList fileList = stdFileService.findFileListWithoutContents(attNo);
			if(fileList == null || fileList.getSize() == 0){
				throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
			}

			FileItem excelFileItem = null;
			for(FileItem fileItem :fileList.getItems()){
				if("xlsx".equals(fileItem.getExtension())){
					excelFileItem = fileItem;
					break;
				}else if(fileItem.getName().indexOf("xlsx") > -1){
					excelFileItem = fileItem;
					break;
				}

			}

			if(excelFileItem == null){
				throw new FileNotFoundException("엑셀파일이 없습니다");
			}

			try {
				excelFileItem = stdFileService.findFileItemWithContents(excelFileItem.getId());
			} catch (Exception e1) {
				throw new FileNotFoundException("파일을 가져오는 중 오류발생!");
			}

			if(excelFileItem.getInputStreamContents() != null){
				// 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.
				Workbook sourceWorkBook = new XSSFWorkbook(OPCPackage.open(excelFileItem.getInputStreamContents()));

				//해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
				int sheetCnt = sourceWorkBook.getNumberOfSheets();

				// SheetCnt 개수만큼 시트별 데이터를 bean에 담는다.
				for(int i = 0; i < sheetCnt; i++) {

					SheetInfoBean sheetInfo = new SheetInfoBean();

					// Excel에 대한 데이터를 가져온다.
					XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(i);

					for(SheetInfoBean getSheetInfo : getSheetList){

						String sourceSheetName = source.getSheetName() == null? "" : source.getSheetName();
						String getSheetInfoSheetName =  getSheetInfo.getXls_work_sht_nm() == null? "" :  getSheetInfo.getXls_work_sht_nm();

						//SheetName은 독립적이기에 비교하여, 처리 가능. SheetInfo를 여기서 가져옴.
						if(sourceSheetName.equals(getSheetInfoSheetName)) sheetInfo = getSheetInfo; break;
					}

					String emailWorkId = sheetInfo.getEmail_work_id();
					String xlsWorkSht = sheetInfo.getExcel_sht_uuid();

					//Excel에 ROW / CELL 정보를 취합한다.
					List<RowInfoBean> sheetRow = excelReaderUtil.readExcel(source,emailWorkId,xlsWorkSht);

					//EXCEL에서 읽어온 정보를 기준으로 ROW LIST를 SET 한다.
					sheetInfo.setRowList(sheetRow);

					//ArrayList add
					sheetList.add(sheetInfo);
				}
			}
		}catch (RuntimeException rune){
			LOG.error(rune.getMessage());
		}catch (Exception e){
			LOG.error(e.getMessage());
		}

		return sheetList;

	}


	/**
	 * LIST의 개수를 체크하면서, LIST 밑에 있는 단일 객체도 치환이 정상적으로 가능하도록 하여, 치환되도록 하는 Method
	 * @param rowList
	 * @param destination
	 * @param dataMapForRow
	 * @param headersList
	 */
	public static void createExcelForMapListDataCreate(List<RowInfoBean> rowList, XSSFSheet destination , Map<String,Map<String,Map<String,Object>>> dataMapForRow , List<Map<String,Object>> headersList ) {
		int maxColumnNum = 0;
		int headerNextRow = 0;
		int dumyRowCnt = 0;

		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			int rowCreateNum = sheetRow.getExcel_row_no();

			int replaceType = checkCellListAndValue(sheetRow);

			if(replaceType == ExcelCellDataBean.REPLACE_TYPE_LIST){

				String getRowNo = Integer.toString(sheetRow.getExcel_row_no());
				Map<String,Map<String,Object>> dataList = dataMapForRow.get(getRowNo);

				int lastDataList = dataList.size();

				for(int a = 0; a < lastDataList; a++){
					Map<String,Object> data = dataList.get(Integer.toString(rowCreateNum));
					//data list 를 기준으로 copy 처리한다.
					XSSFRow destRow = destination.createRow(rowCreateNum);
					copyRow(sheetRow, destRow);
					dataSetRowForMapList( destRow , data);
					dumyRowCnt++;
				}

			}else if(replaceType == ExcelCellDataBean.REPLACE_TYPE_VALUE){

				int checkDataCnt = 0;

				if(dumyRowCnt > 0){
					checkDataCnt = rowCreateNum - dumyRowCnt;
				}else{
					checkDataCnt = rowCreateNum;
				}

				Map<String,Map<String,Object>> dataList = dataMapForRow.get(Integer.toString(checkDataCnt));

				Map<String,Object> data = dataList.get(Integer.toString(checkDataCnt));

				XSSFRow destRow = destination.createRow(rowCreateNum);
				copyRow(sheetRow, destRow);

				//data list 를 기준으로 copy 처리한다.
				dataSetRowForMapValue( destination.getRow(rowCreateNum) , data);

			}else{
				XSSFRow destRow = destination.createRow(rowCreateNum);
				copyRow(sheetRow, destRow);
			}


            /*if(headersList.size() > 0 && i == 0){
                XSSFRow destRow = destination.createRow(sheetRow.getRowNum());
                setHeadersRow(sheetRow, destRow,headersList);
                headerNextRow = sheetRow.getRowNum() + 1;*/

		}
		for (int i = 0; i < rowList.size(); i++) {
			RowInfoBean sheetRow = rowList.get(i);
			destination.autoSizeColumn(i);
			//destination.setColumnWidth(i, sheetRow.getColumn_width());
		}
	}




	public static Map<String, Object> createTemplateFirstCell(XSSFSheet destSheet, String emailWorkTargId) {
		Map<String,Object> resultMap = Maps.newHashMap();
		XSSFRow destRow = destSheet.createRow(0);
		CellStyle rowStyle = destRow.getRowStyle();
		DataFormat newDataFormat = destSheet.getWorkbook().createDataFormat();

		XSSFCell emailWorkTargIdNewCell = destRow.createCell(1); // new cell
		CellInfoBean emailWorkTargIdCell  = new CellInfoBean();
		emailWorkTargIdCell.setStr_cel_val(emailWorkTargId); //eml_task_subj_uuid
		emailWorkTargIdCell.setExcel_cel_typ(1);
		emailWorkTargIdCell.setHide_yn(true);
		emailWorkTargIdCell.setCel_brd_bttm_cd((short)0);
		emailWorkTargIdCell.setCel_brd_lft_cd((short)0);
		emailWorkTargIdCell.setCel_brd_rgt_cd((short)0);
		emailWorkTargIdCell.setCel_brd_top_cd((short)0);
		emailWorkTargIdCell.setDat_fmt(newDataFormat.getFormat(";;;"));
		emailWorkTargIdCell.setCol_width(3);
		copyCell(emailWorkTargIdCell, emailWorkTargIdNewCell);

		String emailSndLogId = UUID.randomUUID().toString();
		XSSFCell emailSndLogIdNewCell = destRow.createCell(2); // new cell
		CellInfoBean emailSndLogIdCell  = new CellInfoBean();
		emailSndLogIdCell.setStr_cel_val(emailSndLogId); //eml_task_subj_uuid
		emailSndLogIdCell.setExcel_cel_typ(1);
		emailSndLogIdCell.setHide_yn(true);
		emailSndLogIdCell.setCel_brd_bttm_cd((short)0);
		emailSndLogIdCell.setCel_brd_lft_cd((short)0);
		emailSndLogIdCell.setCel_brd_rgt_cd((short)0);
		emailSndLogIdCell.setCel_brd_top_cd((short)0);
		emailSndLogIdCell.setDat_fmt(newDataFormat.getFormat(";;;"));
		emailSndLogIdCell.setCol_width(3);
		copyCell(emailSndLogIdCell, emailSndLogIdNewCell);

		resultMap.put("eml_task_subj_uuid",emailWorkTargId);
		resultMap.put("eml_snd_histrec_uuid",emailSndLogId);

		return resultMap;
	}

	/**
	 * Cell의 Style 중 Color가 테마/기본/사용자 지정 등 컬러들이 여러가지가 있어, 이를 애초에 rgb 값으로 측정하기 위하여 별도로 Check 를 만듬.
	 * @param rgb
	 * @return
	 */
	public static XSSFColor getColorForRGB(int rgb) {
		java.awt.Color color =new java.awt.Color(rgb);
		return new XSSFColor(color);
	}

	/**
	 * Cell Value / Info / Style 정보를 Copy 하기 위하여 각 객체를 가져오는 형태로 구현
	 * @param oldCell
	 * @param newCell
	 */
	public static void copyCell(CellInfoBean oldCell, XSSFCell newCell) {

		XSSFFont newFont = (XSSFFont) newCell.getSheet().getWorkbook().createFont();

		newFont.setBold(oldCell.isLtr_thk_yn());
		//newFont.setColor(oldFont.getColor());
		newFont.setFontHeight(oldCell.getLtr_hgt());
		newFont.setFontName(oldCell.getLtr_font_nm());
		newFont.setItalic(oldCell.isLtr_ital_yn());
		newFont.setStrikeout(oldCell.isLtr_strkth_yn());
		newFont.setTypeOffset(oldCell.getOffset_use_yn());
		newFont.setUnderline(oldCell.getUndln_scop());
		newFont.setCharSet(oldCell.getStr_set());
		//newFont.setThemeColor(oldFont.getThemeColor());
		newFont.setColor(getColorForRGB(oldCell.getLtr_clr()));

		XSSFCellStyle newCellStyle = (XSSFCellStyle) newCell.getSheet().getWorkbook().createCellStyle();
		newCellStyle.setFont(newFont);
		newCellStyle.setDataFormat(oldCell.getDat_fmt());
		newCellStyle.setAlignment(HorizontalAlignment.forInt(oldCell.getCel_custmz_yn()));
		newCellStyle.setHidden(oldCell.isHide_yn());
		newCellStyle.setLocked(oldCell.isLckd_yn());
		newCellStyle.setWrapText(oldCell.isLnbrk_yn());
		newCellStyle.setBorderBottom(BorderStyle.valueOf(oldCell.getCel_brd_bttm_cd()));
		newCellStyle.setBorderLeft(BorderStyle.valueOf(oldCell.getCel_brd_lft_cd()));
		newCellStyle.setBorderRight(BorderStyle.valueOf(oldCell.getCel_brd_rgt_cd()));
		newCellStyle.setBorderTop(BorderStyle.valueOf(oldCell.getCel_brd_top_cd()));
		newCellStyle.setBottomBorderColor(getColorForRGB(oldCell.getCel_brd_bttm_clr()));
		newCellStyle.setFillBackgroundColor(getColorForRGB(oldCell.getBgd_fill_clr()));
		newCellStyle.setFillForegroundColor(getColorForRGB(oldCell.getFgd_fill_clr()));
		newCellStyle.setFillPattern(FillPatternType.forInt(oldCell.getFill_patt_typ_cd()));
		newCellStyle.setIndention(oldCell.getCel_marg());
		newCellStyle.setLeftBorderColor(getColorForRGB(oldCell.getCel_brd_lft_clr()));
		newCellStyle.setRightBorderColor(getColorForRGB(oldCell.getCel_brd_rgt_clr()));
		newCellStyle.setRotation(oldCell.getTov_yn());
		newCellStyle.setTopBorderColor(getColorForRGB(oldCell.getCel_brd_top_clr()));
		newCellStyle.setVerticalAlignment(VerticalAlignment.forInt(oldCell.getCel_vert_custmz_typ_cd()));
		newCell.setCellValue(oldCell.getExcel_cel_typ());
		newCell.setCellStyle(newCellStyle);
		newCell.getSheet().setColumnWidth(oldCell.getCel_idx_no(),oldCell.getCol_width());



		switch (oldCell.getExcel_cel_typ()) {
			case 1:
				newCell.setCellType(1);
				newCell.setCellValue(oldCell.getStr_cel_val());
				break;
			case 0:
				newCell.setCellType(0);
				newCell.setCellValue(oldCell.getDbl_cel_val());
				break;
			case 3:
				newCell.setCellType(3);
				break;
			case 4:
				newCell.setCellType(4);
				newCell.setCellValue(oldCell.isBool_cel_val());
				break;
			case 5:
				newCell.setCellType(5);
				newCell.setCellErrorValue(oldCell.getErr_cel_val());
				break;
			case 2:
				newCell.setCellType(2);
				newCell.setCellFormula(oldCell.getFmla_cel_val());
				break;
			default:
				break;
		}
	}

	/**
	 * Row 내에 있는 Cell들을 취득하여, 복사 가능하도록 처리하는 메소드.
	 * @param srcRow
	 * @param destRow
	 */
	public static void copyRow(RowInfoBean srcRow, XSSFRow destRow) {
		for(CellInfoBean cellInfoBean :  srcRow.getCellList()){
			XSSFCell newCell = destRow.getCell(cellInfoBean.getCel_idx_no()); // new cell
			if (newCell == null) newCell = destRow.createCell(cellInfoBean.getCel_idx_no());
			copyCell(cellInfoBean, newCell);
		}
	}


}
