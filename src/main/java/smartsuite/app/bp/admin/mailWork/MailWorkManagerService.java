package smartsuite.app.bp.admin.mailWork;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.ExcelCreateUtil;
import smartsuite.app.common.excel.bean.CellInfoBean;
import smartsuite.app.common.excel.bean.ExcelInfoBean;
import smartsuite.app.common.excel.bean.RowInfoBean;
import smartsuite.app.common.excel.bean.SheetInfoBean;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MailWorkManagerService {
	static final Logger LOG = LoggerFactory.getLogger(MailWorkManagerService.class);

	@Inject
	MailWorkRepository mailWorkRepository;

	@Inject
	MailWorkService mailWorkService;

	@Inject
	ExcelCreateUtil excelCreateUtil;


	@Value("#{file['file.upload.temp.path']}")
	String fileTempPath;

	/**
	 * 현재 할당된 이메일 업무 코드를 가지고, 엑셀 INFO 리스트를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListWorkExcelList(Map<String, Object> param) {
		return mailWorkRepository.findListWorkExcelList(param);
	}


	/* 업무메일 리스트 목록조회 Combobox용 */
	public List<Map<String, Object>> getMailWorkList(Map<String, Object> param) {
		return mailWorkRepository.getMailWorkList(param);
	}


	public List<Map<String, Object>> findListExcelInfoList(Map<String, Object> param) {
		return mailWorkRepository.findListExcelInfoList(param);
	}


	public List<Map<String, Object>> findListSheetInfoListCombo(Map<String, Object> param) {
		return mailWorkRepository.findListSheetInfoListCombo(param);
	}


	public List<Map<String, Object>> findExcelRowListBySheetId(Map<String, Object> param) {
		return mailWorkRepository.findExcelRowListBySheetId(param);
	}


	public ResultMap deleteExcelInfo(Map<String, Object> param){
		List<Map<String, Object>> excelList = (List<Map<String, Object>>)param.getOrDefault("excelInfo", Lists.newArrayList());

		for(Map<String,Object> excelInfo : excelList){
			Map<String,Object> deleteExcelInfo = Maps.newHashMap();

			//1. 파일이 달라졌는지 확인한다.
			ExcelInfoBean getExcelInfoBean = mailWorkService.findWorkExcelInfo(excelInfo);
			deleteExcelInfo.put("eml_task_uuid",getExcelInfoBean.getEml_task_uuid());
			/**
			 * EML_TASK_EXCEL_ROW, EML_TASK_EXCEL_SHT, EML_TASK_EXCEL_ATH 는 email_work_id로 지울수있음.
			 * EML_TASK_EXCEL_CEL는 row_id로 지울수있음
			 * 이메일 업무 삭제 프로세스
			 */
			mailWorkService.deleteEmailWorkProc(getExcelInfoBean,deleteExcelInfo);
		}
		return ResultMap.SUCCESS(param);
	}

	/**
	 * @param param
	 * @return
	 */
	public ResultMap updateUseYnSaveExcel(Map<String, Object> param){
		List<String> emailWorkIdList = Lists.newArrayList();

		List<Map<String, Object>> excelList = (List<Map<String, Object>>)param.getOrDefault("excelInfo",Lists.newArrayList());
		List<Map<String, Object>> updateExcelList = (List<Map<String, Object>>)param.getOrDefault("updateExcel",Lists.newArrayList());

		for(Map<String,Object> excelInfo : excelList){
			String emailWorkId = excelInfo.get("eml_task_uuid") == null? "" : excelInfo.get("eml_task_uuid").toString();
			emailWorkIdList.add(emailWorkId);

			excelInfo.put("use_yn",excelInfo.getOrDefault("use_yn","N"));
			mailWorkRepository.updateUseYnSaveExcel(excelInfo);
		}

		for(Map<String,Object> update : updateExcelList){
			String emailWorkId = update.get("eml_task_uuid") == null? "" : update.get("eml_task_uuid").toString();

			if(!emailWorkIdList.contains(emailWorkId)){
				update.put("use_yn", "N");
				mailWorkRepository.updateUseYnSaveExcel(update);
			};
		}
		return ResultMap.SUCCESS(param);
	}

	public ResultMap saveExcelInfo(Map<String, Object> param){
		boolean isNew = param.getOrDefault("isNew",false) == null? false : (Boolean) param.getOrDefault("isNew",false);

		// 신규 저장
		if(isNew){
			//새로이 정보 저장
			mailWorkService.excelFileInsertProc(param);
		}else {
			//1. 파일이 달라졌는지 확인한다.
			ExcelInfoBean getExcelInfoBean = mailWorkService.findWorkExcelInfo(param);

			String getAttNo = getExcelInfoBean.getAthg_uuid();

			if (StringUtils.isNotEmpty(getAttNo)) {
				String attNo = param.getOrDefault("athg_uuid","") == null? "" : (String) param.getOrDefault("athg_uuid","");

				if ((getAttNo).equals(attNo)) {//att_no가 같다면, 동일하다고 가정
					//2. 파일이 달라졌으면 파일을 읽고, 아니라면 excelinfo만 업데이트한다.
					mailWorkRepository.updateExcelInfo(param);
				} else { //새로이 파일을 넣었을 경우
					/**
					 * EML_TASK_EXCEL_ROW, EML_TASK_EXCEL_SHT, EML_TASK_EXCEL_ATH 는 email_work_id로 지울수있음.
					 * EML_TASK_EXCEL_CEL는 row_id로 지울수있음
					 */
					Map<String,Object> deleteExcelInfo = Maps.newHashMap();

					deleteExcelInfo.put("eml_task_uuid",getExcelInfoBean.getEml_task_uuid());

					// 이메일 업무 삭제 프로세스
					mailWorkService.deleteEmailWorkProc(getExcelInfoBean,deleteExcelInfo);

					//새로이 정보 저장
					mailWorkService.excelFileInsertProc(param);
				}
			}
		}
		return ResultMap.SUCCESS(param);
	}


	public ResultMap updateExcelInfoConfirmYn(Map<String, Object> param){
		param.put("cnfd_yn","Y");
		mailWorkRepository.updateExcelInfoConfirmYn(param);
		return ResultMap.SUCCESS(param);
	}

	/* ExcelInfo Field grid용 combobox조회 */
	public List<Map<String, Object>> findSheetFieldValue(Map<String, Object> excelInfo) {
		List<Map<String,Object>> cellFieldList = Lists.newArrayList();

		ExcelInfoBean excelInfoBean = mailWorkService.findWorkExcelInfo(excelInfo);

		if(null != excelInfoBean){
			List<SheetInfoBean> sheetInfoBeanList = mailWorkService.findWorkExcelSheet(excelInfoBean);
			for(SheetInfoBean sheetInfoBean : sheetInfoBeanList){
				//해당 시트에 ROW 정보를 조회한다.
				List<RowInfoBean> rowList = mailWorkService.findListWorkExcelSheetRow(sheetInfoBean);
				for(RowInfoBean rowInfo : rowList){
					List<CellInfoBean> cellList = mailWorkService.findListWorkExcelSheetCellList(rowInfo);
					for(CellInfoBean cell : cellList){
						String cellValue = Strings.isNullOrEmpty(cell.getStr_cel_val()) ? "" : cell.getStr_cel_val();
						//cell에 ${}로 시작되는 value가 존재하는지
						if(cellValue.indexOf("$[") > -1 || cellValue.indexOf("${") > -1){
							Map<String,Object> cellFieldMap = Maps.newHashMap();
							String labelName = sheetInfoBean.getXls_work_sht_nm() + "[" + rowInfo.getExcel_row_no() +"]" + "[" + cellValue + "]";
							String dataValue = sheetInfoBean.getExcel_sht_uuid()+"&&"+rowInfo.getExcel_row_no() +"&&" +cellValue;
							cellFieldMap.put("data",dataValue);
							cellFieldMap.put("label",labelName);
							cellFieldList.add(cellFieldMap);
						}
					}
				}
			}
		}
		return cellFieldList;
	}


	public ResponseEntity<byte[]> excelTemplateSetupAfterDownLoad(Map<String,Object> param){
		List<Map<String, Object>> templateDataList = (List<Map<String, Object>>)param.get("templateDataList");
		Map<String, Object> excelInfoData = (Map<String, Object>)param.get("excelInfo");

		String emailNm = "";//excelInfoBean.getEmail_work_nm() == null ? "이메일업무 템플릿.xlsx" : excelInfoBean.getEmail_work_nm() + ".xlsx";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/vnd.ms-excel");
		responseHeaders.set("Content-Disposition", "attachment; filename=\"" + emailNm + "\";");
		responseHeaders.set("file-name", emailNm);

		byte[] byt = this.excelTemplateCreate(templateDataList, excelInfoData);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(byt, responseHeaders, HttpStatus.CREATED);
		return responseEntity;
	}


	public void mailTemplateMailSendTest(Map<String,Object> param) {
		List<Map<String, Object>> templateDataList = (List<Map<String, Object>>) param.get("templateDataList");
		Map<String, Object> excelInfoData = (Map<String, Object>) param.get("excelInfo");

		//선택된 EML_TASK_UUID 를 가지고, Template를 구성한다.
		//1. EML_TASK_EXCEL_ATH & EML_TASK_EXCEL_SHT 에 정보를 가져와, 해당 하는 athg_uuid 및 info 정보를 가져온다.
		// ( key = eml_task_uuid )
		ExcelInfoBean excelInfoBean = mailWorkService.findWorkExcelInfo(excelInfoData);

		if (null != excelInfoBean) {
			List<SheetInfoBean> sheetInfoBeanList = mailWorkService.findWorkExcelSheet(excelInfoBean);
			excelInfoBean = mailWorkService.sheetRowDataSetup(sheetInfoBeanList, excelInfoBean);
			String emailNm = excelInfoBean.getEml_task_nm() == null ? "이메일업무 템플릿.xlsx" : excelInfoBean.getEml_task_nm() + ".xlsx";

			try {
				//DATA 테스트를 위한 TESTSAMPLE
				Map<String, Map<String, Map<String, Map<String, Object>>>> dataMapForSheet = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();
				dataMapForSheet = mailWorkService.dataSetTemplateProcess(templateDataList);

				Map<String, Object> excelResult = excelCreateUtil.createExcelTemplateToDataSetup(excelInfoBean.getSheetList(), dataMapForSheet, null, emailNm);
				mailWorkService.mailWorkSendTestProcess(excelInfoData, excelResult, excelInfoBean);

			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
	}

	public ResponseEntity<byte[]> downLoadMailExcel(Map<String, Object> excelInfoData){
		OutputStream out = null;
		InputStream io = null;
		Workbook destinationWorkBook = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		ResponseEntity<byte[]> responseEntity = null;
		File file = null;
		try {
			//선택된 EML_TASK_UUID 를 가지고, Template를 구성한다.
			//1. EML_TASK_EXCEL_ATH & EML_TASK_EXCEL_SHT 에 정보를 가져와, 해당 하는 athg_uuid 및 info 정보를 가져온다.
			ExcelInfoBean excelInfoBean = mailWorkService.findWorkExcelInfo(excelInfoData);

			if (null != excelInfoBean) {
				List<SheetInfoBean> sheetInfoBeanList = mailWorkService.findWorkExcelSheet(excelInfoBean);
				excelInfoBean = mailWorkService.sheetRowDataSetup(sheetInfoBeanList,excelInfoBean);

				String emailNm = excelInfoBean.getEml_task_nm() == null ? "이메일업무 템플릿.xlsx" : excelInfoBean.getEml_task_nm() + ".xlsx";
				emailNm = URLEncoder.encode(emailNm, "UTF-8");
				file = this.generateTempFile();
				destinationWorkBook = new XSSFWorkbook();
				out = new FileOutputStream(file);

				responseHeaders.set("Content-Type", "application/vnd.ms-excel");
				responseHeaders.set("Content-Disposition", "attachment; filename=\"" + emailNm + "\";");
				responseHeaders.set("file-name", emailNm);


				excelCreateUtil.copyExcel(excelInfoBean.getSheetList(), (XSSFWorkbook) destinationWorkBook);
				destinationWorkBook.write(out);

				byte[] byt = IOUtils.toByteArray(new FileInputStream(file));
				responseEntity = new ResponseEntity<byte[]>(byt, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			try {
				if (destinationWorkBook != null) destinationWorkBook.close();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			try {
				if (file != null) file.delete();
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
			try {
				if (io != null) io.close();
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}

		return responseEntity;
	}

	/**
	 * 관리자 설정화면에서 등록된 정보를 활용하여 excel파일을 generate.
	 * @param templateDataList
	 * @param excelInfoData
	 * @return
	 */
	public byte[] excelTemplateCreate(List<Map<String, Object>> templateDataList,Map<String, Object> excelInfoData){
		byte[] byt = null;

		//선택된 EML_TASK_UUID 를 가지고, Template를 구성한다.

		//1. EML_TASK_EXCEL_ATH & EML_TASK_EXCEL_SHT 에 정보를 가져와, 해당 하는 athg_uuid 및 info 정보를 가져온다.
		// ( key = eml_task_uuid )
		ExcelInfoBean excelInfoBean = mailWorkService.findWorkExcelInfo(excelInfoData);

		if (null != excelInfoBean) {
			List<SheetInfoBean> sheetInfoBeanList = mailWorkService.findWorkExcelSheet(excelInfoBean);
			excelInfoBean = mailWorkService.sheetRowDataSetup(sheetInfoBeanList,excelInfoBean);
			//DATA 테스트를 위한 TESTSAMPLE
			Map<String, Object> dataMapForSheet = Maps.newHashMap();
			for(Map<String,Object> data :  templateDataList){

				String getFieldNm = data.get("set_field_nm") == null? "" : data.get("set_field_nm").toString();
				String value      = data.get("value") == null? "" : data.get("value").toString();
				if(StringUtils.isNotEmpty(getFieldNm)){

					String sheetId = "";
					String rowNo = "";
					String cellReplaceValue = "";
					String[] values = {};

					if(getFieldNm.indexOf("&&") > -1){
						values =  getFieldNm.split("&&");

						sheetId = values[0];
						rowNo = values[1];
						cellReplaceValue = values[2];
						String excelStringValue = cellReplaceValue == null? "" : cellReplaceValue;
						excelStringValue = excelStringValue.replace("${","");
						excelStringValue = excelStringValue.replace("}","");

						cellReplaceValue = excelStringValue;
					}

					Map<String, Object> sheetDataList = (Map<String, Object>)((dataMapForSheet.get(sheetId) == null) ? Maps.newHashMap() : dataMapForSheet.get(sheetId));
					Map<String, Object> dataList = (Map<String, Object>) (sheetDataList.get(rowNo) == null ? Maps.newHashMap() : sheetDataList.get(rowNo));

					// Cell과 매칭할 data 객체들
					if(dataList.size() > 0){
						Map<String,Object> getCellDataMap = (Map<String, Object>)dataList.get(rowNo);
						getCellDataMap.put(cellReplaceValue, value);
						dataList.put(rowNo,getCellDataMap);

					}else{
						Map<String,Object> setCellDataMap = Maps.newHashMap();
						setCellDataMap.put(cellReplaceValue, value);
						// sheet id 에 맞는 row_no에 setCellDataMap이 들어가야함.
						dataList.put(rowNo,setCellDataMap);
					}

					//row
					sheetDataList.put(rowNo,dataList);

					//sheet
					dataMapForSheet.put(sheetId,sheetDataList);

				}
			}

			Workbook destinationWorkBook = new XSSFWorkbook();
			excelCreateUtil.createExcelByDataSetup(excelInfoBean.getSheetList(),(XSSFWorkbook) destinationWorkBook, dataMapForSheet, null);

			//temp file
			File file = generateTempFile();
			InputStream   in = null;
			OutputStream out = null;
			try {
				out = new FileOutputStream(file);
				destinationWorkBook.write(out);
				in = FileUtils.openInputStream(file);
				byt = IOUtils.toByteArray(in);
			} catch (IOException e) {
				LOG.error(e.getMessage());
			} finally {
				try {
					if (destinationWorkBook != null) destinationWorkBook.close();
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
				try {
					if (out != null) out.close();
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
				try {
					if (in  != null) in.close();
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}


		}
		return byt;
	}



	//임시파일 생성.
	public File generateTempFile() {
		File file;
		file = new File(fileTempPath + File.separator + "email_work" + File.separator + UUID.randomUUID()+".xlsx");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		return file;
	}


}
