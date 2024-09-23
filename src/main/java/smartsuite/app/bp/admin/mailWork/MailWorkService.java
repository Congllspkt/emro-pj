package smartsuite.app.bp.admin.mailWork;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.ExcelCreateUtil;
import smartsuite.app.common.excel.ExcelReaderUtil;
import smartsuite.app.common.excel.bean.CellInfoBean;
import smartsuite.app.common.excel.bean.ExcelInfoBean;
import smartsuite.app.common.excel.bean.RowInfoBean;
import smartsuite.app.common.excel.bean.SheetInfoBean;
import smartsuite.app.common.mail.mailWorkExcel.MailWorkSendService;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MailWorkService {

	static final Logger LOG = LoggerFactory.getLogger(MailWorkService.class);

	@Inject
	StdFileService stdFileService;

	@Inject
	MailWorkSendService mailWorkSendService;

	@Inject
	MailWorkRepository mailWorkRepository;
	
	@Inject
	ExcelCreateUtil excelCreateUtil;


	public void deleteEmailWorkProc(ExcelInfoBean getExcelInfoBean,Map<String,Object> deleteExcelInfo){

		List<SheetInfoBean> sheetInfoBeanList = findWorkExcelSheet(getExcelInfoBean);

		for(SheetInfoBean sheetInfoBean : sheetInfoBeanList){
			//해당 시트에 ROW 정보를 조회한다.
			List<RowInfoBean> rowList = findListWorkExcelSheetRow(sheetInfoBean);

			for(RowInfoBean rowInfoBean : rowList){
				deleteExcelInfo.put("excel_row_uuid",rowInfoBean.getEml_task_uuid());
				this.deleteExcelCellInfo(deleteExcelInfo);
			}
		}
		this.deleteExcelRowInfo(deleteExcelInfo);
		this.deleteExcelSheetInfo(deleteExcelInfo);


		List<String> findEmailWorkSendProcInfoList = mailWorkRepository.findListEmailWorkTaskSubjectUUID(deleteExcelInfo);

		if(findEmailWorkSendProcInfoList.size() > 0) {
			Map<String,Object> emailWorkSendProcInfo = Maps.newHashMap();
			emailWorkSendProcInfo.put("emailTaskSubjectList",findEmailWorkSendProcInfoList);

			//EML_TASK_RCPT_HISTREC
			mailWorkSendService.deleteEmailWorkReceivedMailList(emailWorkSendProcInfo);

			//EML_TASK_SND_HISTREC
			this.deleteEmailWorkSendMailList(emailWorkSendProcInfo);

			//EML_TASK_PRGS_STS
			this.deleteEmailWorkMailInfo(emailWorkSendProcInfo);
		}


		this.deleteExcelInfo(deleteExcelInfo);
	}

	public FileItem findFileItemByEmailWorkId(String attNo){
		FileItem excelFileItem = null;
		try{
			FileList fileList = stdFileService.findFileListWithoutContents(attNo);
			if(fileList == null || fileList.getSize() == 0){
				throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
			}

			for(FileItem fileItem : fileList.getItems()){
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
			}else{
				try {
					excelFileItem = stdFileService.findFileItemWithContents(excelFileItem.getId());
				} catch (Exception e) {
					throw new FileNotFoundException("파일을 가져오는 중 오류발생!" + e.getMessage());
				}
			}
		}catch (Exception e){
			throw new CommonException(e.getMessage());
		}finally {
			if(excelFileItem.getInputStreamContents() == null){
				try{
					throw new FileNotFoundException("파일이 존재하지 않습니다.");
				}catch (Exception e){
					throw new CommonException(e.getMessage());
				}
			}
		}

		return excelFileItem;
	}


	public ExcelInfoBean excelFileInsertProc(Map<String,Object> param){

		String attNo = param.getOrDefault("athg_uuid","") == null ? "" : (String) param.getOrDefault("athg_uuid","");
		String emailWorkId = UUID.randomUUID().toString();


		// ExcelInfo에 기본적인 정보를 setup해준다.
		param.put("eml_task_uuid",emailWorkId);
		ExcelInfoBean excelInfoBean = new ExcelInfoBean(param);

		try{
			FileItem excelFileItem = this.findFileItemByEmailWorkId(attNo);

			//Excel Info 기본정보 저장
			this.insertMailWorkExcelInfo(excelInfoBean);

			// 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.
			Workbook sourceWorkBook = new XSSFWorkbook(OPCPackage.open(excelFileItem.getInputStreamContents()));

			//해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
			int sheetCnt = sourceWorkBook.getNumberOfSheets();

			// SheetInfoBean List new
			List<SheetInfoBean> sheetList = Lists.newArrayList();

			// SheetCnt 개수만큼 시트별 데이터를 bean에 담는다.
			for(int i = 0; i < sheetCnt; i++) {
				SheetInfoBean sheetInfo = new SheetInfoBean();

				//Sheet에 대한 UUID
				String xlsWorkSht = UUID.randomUUID().toString();

				// Excel에 대한 데이터를 가져온다.
				XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(i);

				sheetInfo.setEmail_work_id(emailWorkId);
				sheetInfo.setExcel_sht_uuid(xlsWorkSht);
				sheetInfo.setXls_work_sht_nm(source.getSheetName());

				//Excel에 ROW / CELL 정보를 취합한다.
				List<RowInfoBean> sheetRow = ExcelReaderUtil.readExcel(source,emailWorkId,xlsWorkSht);

				//EXCEL에서 읽어온 정보를 기준으로 ROW LIST를 SET 한다.
				sheetInfo.setRowList(sheetRow);

				//ArrayList add
				sheetList.add(sheetInfo);

				//Sheet INFO Insert
				this.insertExcelSheetInfo(sheetInfo);

				for (RowInfoBean row : sheetRow) {
					//Sheet index (i) Row INFO Insert
					this.insertExcelRowInfo(row);

					for (CellInfoBean cellInfo : row.getCellList()) {
						//Sheet Index (i) Cell INFO Insert
						this.insertExcelCellInfo(cellInfo);
					}
				}
			}
			//Excel info에 sheet 정보를 넣는다.
			excelInfoBean.setSheetList(sheetList);

		}catch (RuntimeException rune){
			LOG.error(rune.getMessage());
		}catch (Exception e){
			LOG.error(e.getMessage());
		}
		return excelInfoBean;
	}

	
	public ResultMap saveListExcelSheetHeader(Map<String, Object> param) {
		List<Map<String, Object>> insertList =  (List<Map<String, Object>>) param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String, Object>> updateList =  (List<Map<String, Object>>) param.getOrDefault("updateList",Lists.newArrayList());

		String tmpId = param.getOrDefault("excel_row_uuid","") == null ? "" : (String) param.getOrDefault("excel_row_uuid","");
		
		for(Map<String,Object> insert: insertList){
			insert.put("excel_row_uuid",tmpId);

			RowInfoBean rowInfoBean = new RowInfoBean();
			try{
				BeanUtils.populate(rowInfoBean,insert);
				this.insertExcelRowInfo(rowInfoBean);
			}catch (CommonException ce){
				LOG.error(ce.getMessage());
			}catch (Exception e){
				LOG.error(e.getMessage());
			}
		}
		
		for(Map<String,Object> update: updateList){
			update.put("excel_row_uuid",tmpId);
			mailWorkRepository.updateExcelInfo(update);
		}
		
		return ResultMap.SUCCESS();
	}


	public void excelFieldSetupCreate(Map<String,Object> excelInfoData , Map<String,Object> appDataList){

		List<Map<String,Object>> listMap = Lists.newArrayList();
		Map<String,Object> listParam = Maps.newHashMap();

		try {

			// 업무화면에서 넘길때는 WORK_CD (즉, 업무 유형만 던질수 있기에 이를 가지고 구분 처리해야함 , EML_TASK_EXCEL_ATH 기준 사용여부 : Y / 확정여부 : 확정인 상태 )
			ExcelInfoBean excelInfoBean = mailWorkRepository.findWorkExcelInfoByWorkCd(excelInfoData);

			if (null != excelInfoBean) {
				List<SheetInfoBean> sheetInfoBeanList = findWorkExcelSheet(excelInfoBean);
				excelInfoBean = sheetRowDataSetup(sheetInfoBeanList,excelInfoBean);
			}

			if (null != excelInfoBean) {
				String emailFileNm = excelInfoData.getOrDefault("email_file_nm","이메일업무 템플릿").toString(); //실제 발송될 EXCEL FILE NAME
				String emailNm = emailFileNm + ".xlsx";
				
				Map<String, Object> excelResult = excelCreateUtil.createEmailWorkProc(excelInfoBean.getSheetList(), appDataList, null, emailNm);
				
				listParam.put("eml_tit", excelInfoData.get("eml_tit"));
				listParam.put("rcpnt_eml", excelInfoData.get("rcpnt_eml"));
				listParam.put("to_nm", excelInfoData.get("to_nm"));
				listParam.put("tmpl_athg_uuid", excelResult.get("tmpl_athg_uuid"));
				listParam.put("athg_uuid", excelResult.get("athg_uuid"));            // file 생성 api method 구축 후 전달받은 객체
				listParam.put("task_uuid", excelInfoData.get("task_uuid"));            // 업무 키
				listParam.put("eml_task_dtl_uuid", excelInfoData.get("eml_task_dtl_uuid"));        // 업무 상세 키 (ex. vd_cd)
				listParam.put("eml_task_subj_uuid", excelResult.get("eml_task_subj_uuid"));    // eml_task_subj_uuid
				listParam.put("eml_snd_histrec_uuid", excelResult.get("eml_snd_histrec_uuid"));    // eml_snd_histrec_uuid
				
				listMap.add(listParam);
				
				try {
					mailWorkSendService.addMailWork(excelInfoBean, listMap);
				} catch(Exception e) {
					LOG.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}



	public Map<String, Map<String, Map<String, Map<String, Object>>>> dataSetTemplateProcess(List<Map<String, Object>> templateDataList) {
		Map<String, Map<String, Object>> subMap = new HashMap<String, Map<String, Object>>(); //subMap으로 ${dataBean.key} 로 구성될 경우 찾아오기위한 map
		Map<String, Map<String, Map<String, Map<String, Object>>>> dataMapForSheet = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();

		for (Map<String, Object> data : templateDataList) {

			String getFieldNm = data.get("set_field_nm") == null ? "" : data.get("set_field_nm").toString();
			String value = data.get("value") == null ? "" : data.get("value").toString();
			String cellReplaceValue = "";

			if (!StringUtils.isEmpty(getFieldNm)) {
				String sheetId = "";
				String rowNo = "";
				String[] values = {};

				if (getFieldNm.indexOf("&&") > -1) {
					values = getFieldNm.split("&&");
					sheetId = values[0];
					rowNo = values[1];
					cellReplaceValue = values[2] == null ? "" : values[2];
					cellReplaceValue = cellReplaceValue.replace("${", "");
					cellReplaceValue = cellReplaceValue.replace("}", "");
					cellReplaceValue = cellReplaceValue.replace("$[", "");
					cellReplaceValue = cellReplaceValue.replace("]", "");
				}

				Map<String, Map<String, Map<String, Object>>> sheetDataList = dataMapForSheet.get(sheetId) == null ? sheetDataList = new HashMap<String, Map<String, Map<String, Object>>>() : dataMapForSheet.get(sheetId);
				Map<String, Map<String, Object>> dataList = sheetDataList.get(rowNo) == null ? dataList = new HashMap<String, Map<String, Object>>() : sheetDataList.get(rowNo);

				// Cell과 매칭할 data 객체들
				if (dataList.size() > 0) {
					dataList = cellMatchingDataInfo(dataList, rowNo, cellReplaceValue, subMap, value);
				} else {
					if (cellReplaceValue.indexOf(".") > -1) { //  data.value  형식의 구조라면, 아래와 같이.
						dataList = cellMatchingDataInfoByDataValueType(dataList, rowNo, cellReplaceValue, subMap, value);
					} else {
						Map<String, Object> setCellDataMap = Maps.newHashMap();
						setCellDataMap.put(cellReplaceValue, value);
						// sheet id 에 맞는 row_no에 setCellDataMap이 들어가야함.
						dataList.put(rowNo, setCellDataMap);
					}
				}
				//row
				sheetDataList.put(rowNo, dataList);

				//sheet
				dataMapForSheet.put(sheetId, sheetDataList);
			}
		}
		return dataMapForSheet;
	}

	public void mailWorkSendTestProcess(Map<String, Object> excelInfoData, Map<String, Object> excelResult, ExcelInfoBean excelInfoBean) {
		List<Map<String,Object>> listMap = Lists.newArrayList();
		Map<String,Object> listParam = Maps.newHashMap();
		listParam.put("rcpnt_eml"	, excelInfoData.get("email"));
		listParam.put("to_nm"	, "");
		listParam.put("tmpl_athg_uuid"	, excelResult.get("tmpl_athg_uuid"));
		listParam.put("athg_uuid",excelResult.get("athg_uuid"));  // file 생성 api method 구축 후 전달받은 객체
		listParam.put("task_uuid", UUID.randomUUID().toString()); // 업무 키
		listParam.put("eml_task_subj_uuid",excelResult.get("eml_task_subj_uuid")); // eml_task_subj_uuid
		listParam.put("eml_snd_histrec_uuid",excelResult.get("eml_snd_histrec_uuid")); // eml_task_subj_uuid

		listMap.add(listParam);
		mailWorkSendService.addMailWork(excelInfoBean,listMap);
	}

	private Map<String, Map<String, Object>> cellMatchingDataInfoByDataValueType(Map<String, Map<String, Object>> dataList, String rowNo, String cellReplaceValue, Map<String, Map<String, Object>> subMap, String value) {
		String dataGetKey = "";
		String subDataGetKey = "";
		String[] splitValue = cellReplaceValue.split("\\.");
		dataGetKey = splitValue[0];
		subDataGetKey = splitValue[1];

		Map<String,Object> dumyMap = Maps.newHashMap();
		if(subMap.size() > 0){ // subMap에 현재 데이터가 들어갓을 경우,
			dumyMap = subMap.get(dataGetKey) == null ? new HashMap<String, Object>() : subMap.get(dataGetKey);
			dumyMap.put(subDataGetKey,value);
		}else{
			dumyMap.put(subDataGetKey,value);
		}

		subMap.put(dataGetKey,dumyMap); //sub map

		Map<String,Object> setCellDataMap = Maps.newHashMap(); //main map
		setCellDataMap.put(dataGetKey, dumyMap); //main map
		// sheet id 에 맞는 row_no에 setCellDataMap이 들어가야함.
		dataList.put(rowNo,setCellDataMap);
		return dataList;
	}

	private Map<String, Map<String, Object>> cellMatchingDataInfo(Map<String, Map<String, Object>> dataList, String rowNo, String cellReplaceValue, Map<String, Map<String, Object>> subMap, String value) {
		Map<String,Object> getCellDataMap = dataList.get(rowNo);
		if(cellReplaceValue.indexOf(".") > -1){ //  data.value  형식의 구조라면, 아래와 같이.
			String dataGetKey = "";
			String subDataGetKey = "";
			String[] splitValue = cellReplaceValue.split("\\.");
			dataGetKey = splitValue[0];
			subDataGetKey = splitValue[1];

			Map<String,Object> dumyMap = Maps.newHashMap();
			if(subMap.size() > 0){ // subMap에 현재 데이터가 들어갓을 경우,
				dumyMap = subMap.get(dataGetKey) == null ? new HashMap<String, Object>() : subMap.get(dataGetKey);
				dumyMap.put(subDataGetKey,value);
			}else{
				dumyMap.put(subDataGetKey,value);
			}

			subMap.put(dataGetKey,dumyMap); //sub map

			getCellDataMap.put(dataGetKey, dumyMap); //main map
			dataList.put(rowNo,getCellDataMap);

		}else {
			getCellDataMap.put(cellReplaceValue, value);
			dataList.put(rowNo,getCellDataMap);
		}

		return dataList;
	}

	public ExcelInfoBean sheetRowDataSetup(List<SheetInfoBean> sheetInfoBeanList, ExcelInfoBean excelInfoBean) {
		for (SheetInfoBean sheetInfoBean : sheetInfoBeanList) {
			//해당 시트에 ROW 정보를 조회한다.
			List<RowInfoBean> rowList = this.findListWorkExcelSheetRow(sheetInfoBean);
			for (RowInfoBean rowInfo : rowList) {
				List<CellInfoBean> excelDTData = this.findListWorkExcelSheetCellList(rowInfo);
				//각 ROW별로 CELL LIST 정보 담기.l
				rowInfo.setCellList(excelDTData);
			}
			sheetInfoBean.setRowList(rowList);
		}
		excelInfoBean.setSheetList(sheetInfoBeanList);
		return excelInfoBean;
	}



	private void deleteEmailWorkMailInfo(Map<String, Object> emailWorkSendProcInfo) {
		mailWorkRepository.deleteEmailWorkMailInfo(emailWorkSendProcInfo);
	}

	private void deleteEmailWorkSendMailList(Map<String, Object> emailWorkSendProcInfo) {
		mailWorkRepository.deleteEmailWorkSendMailList(emailWorkSendProcInfo);
	}

	private void deleteExcelSheetInfo(Map<String, Object> deleteExcelInfo) {
		mailWorkRepository.deleteExcelSheetInfo(deleteExcelInfo);
	}

	private void deleteExcelRowInfo(Map<String, Object> deleteExcelInfo) {
		mailWorkRepository.deleteExcelRowInfo(deleteExcelInfo);
	}

	private void deleteExcelInfo(Map<String, Object> deleteExcelInfo) {
		mailWorkRepository.deleteExcelInfo(deleteExcelInfo);
	}

	private void deleteExcelCellInfo(Map<String, Object> deleteExcelInfo) {
		mailWorkRepository.deleteExcelCellInfo(deleteExcelInfo);
	}


	private void insertExcelCellInfo(CellInfoBean cellInfo) {
		mailWorkRepository.insertExcelCellInfo(cellInfo);
	}

	private void insertExcelRowInfo(RowInfoBean row) {
		mailWorkRepository.insertExcelRowInfo(row);
	}

	public void insertExcelSheetInfo(SheetInfoBean sheetInfo) {
		mailWorkRepository.insertExcelSheetInfo(sheetInfo);
	}

	public void insertMailWorkExcelInfo(ExcelInfoBean excelInfoBean) {
		mailWorkRepository.insertMailWorkExcelInfo(excelInfoBean);
	}

	public List<Map<String, Object>> findExcelCellListByRowId(Map<String, Object> param) {
		return mailWorkRepository.findExcelCellListByRowId(param);
	}

	public List<CellInfoBean> findListWorkExcelSheetCellList(RowInfoBean param) {
		return mailWorkRepository.findListWorkExcelSheetCellList(param);
	}

	public List<SheetInfoBean> findWorkExcelSheet(ExcelInfoBean excelInfoBean) {
		return mailWorkRepository.findWorkExcelSheet(excelInfoBean);
	}

	public List<RowInfoBean> findListWorkExcelSheetRow(SheetInfoBean sheetInfoBean) {
		return mailWorkRepository.findListWorkExcelSheetRow(sheetInfoBean);
	}

	public ExcelInfoBean findWorkExcelInfo(Map<String, Object> param) {
		return mailWorkRepository.findWorkExcelInfo(param);
	}

	public List<Map<String, Object>> findListEmailWorkSendHistory(Map<String, Object> param) {
		param.put("eml_task_dtl_uuid",param.getOrDefault("eml_task_dtl_uuid","")); //같이 활용하기 위하여, c:if를 위하여, dummy value 넣어줌.
		return mailWorkRepository.findListEmailWorkSendHistory(param);
	}

}
