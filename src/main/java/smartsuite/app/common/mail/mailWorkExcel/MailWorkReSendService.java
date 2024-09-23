package smartsuite.app.common.mail.mailWorkExcel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.ExcelCopyUtil;
import smartsuite.app.common.excel.bean.SheetInfoBean;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.mail.data.MailResult;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkReceivedRepository;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkRepository;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.FileItem;
import smartsuite.upload.entity.FileList;
import smartsuite.upload.entity.SimpleMultipartFileItem;
import smartsuite.upload.util.AthfServiceUtil;

import javax.inject.Inject;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * MailWorkReSendService.java - MailService.java를 재발송하기 위하여 호출하여야하는데, 동일한 MailService.java를 호출 할 경우 Spring 순환 오류가 발생하여 별도의 Class를 생성
 * 이메일 업무 후 처리 후 Excel error Mail Send를 위한 Class
 */
@Service
@SuppressWarnings({"unchecked"})
@Transactional
public class MailWorkReSendService {
	
	static final Logger LOG = LoggerFactory.getLogger(MailWorkReSendService.class);
	
	@Value("#{file['file.upload.path']}")
	String fileUploadPath;
	
	@Inject
	StdFileService stdFileService;
	
	@Inject
	MailWorkReceivedRepository mailWorkReceivedRepository;
	
	@Inject
	MailWorkRepository mailWorkRepository;
	
	@Inject
	MailService mailService;
	
	//Validation 처리 후 업무단에서 재전송 메일을 발송,
	public void reSendEmailWork(Map<String, Object> dataResultMap) {
		//발송메일 정보 Map
		Map<String, Object> sendEmailInfo = dataResultMap.get("sendEmailInfo") == null ? new HashMap<String, Object>() : (Map<String, Object>) dataResultMap.get("sendEmailInfo");
		
		//수신메일 정보
		Map<String, Object> receivedEmailInfo = dataResultMap.get("receivedEmailInfo") == null ? new HashMap<String, Object>() : (Map<String, Object>) dataResultMap.get("receivedEmailInfo");
		
		
		//Validation 처리 후 업무단에서 넘어오는 ERROR 취합 내역 ( Cell memo로 해당 내역을 표현해야한다. )
		String reSndCause = dataResultMap.get("resnd_rsn") == null ? "" : dataResultMap.get("resnd_rsn").toString();
		String mailTit = receivedEmailInfo.get("eml_tit") == null ? "" : receivedEmailInfo.get("eml_tit").toString();
		
		
		//1. receivedEmailInfo에서 수신내역에 대한 정보를 받아와, excel file을 가져온다.
		FileItem excelFileItem = this.receivedMailGetExcelFile(receivedEmailInfo, sendEmailInfo);
		
		FileItem excelTempFileItem = sendEmailInfo.get("tmpExcelFileItem") == null ? null : (FileItem) sendEmailInfo.get("tmpExcelFileItem");
		
		
		//2. excelfile을 가져오면, 해당 파일내에 email_snd_log_id를 새로 넣어준다.
		OutputStream tempOut = null;
		InputStream tempIo = null;
		Workbook sourceTempWorkBook = null;
		File tempFile = null;
		String tempGrpCd = UUID.randomUUID().toString();
		
		
		String reSndLogId = UUID.randomUUID().toString();
		
		Workbook sourceWorkBook = null;
		File file = null;
		OutputStream out = null;
		InputStream io = null;
		
		try {
			
			if(null != excelTempFileItem && excelTempFileItem.getInputStreamContents() != null) {

				// 발송 시 사용하였던 temp_file을 가져온다..
				sourceTempWorkBook = new XSSFWorkbook(OPCPackage.open(excelTempFileItem.getInputStreamContents()));
				
				String fileNm = UUID.randomUUID() + ".xlsx";
				
				tempFile = new File(fileUploadPath + UUID.randomUUID() + ".xlsx");
				
				tempOut = new FileOutputStream(tempFile);
				
				
				XSSFWorkbook destinationWorkBook = new XSSFWorkbook();
				
				//해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
				int sheetCnt = sourceTempWorkBook.getNumberOfSheets();
				
				for(int i = 0; i < sheetCnt; i++) {
					// Excel에 대한 데이터를 가져온다.
					XSSFSheet source = ((XSSFWorkbook) sourceTempWorkBook).getSheetAt(i);
					XSSFSheet destination = destinationWorkBook.createSheet();
					
					
					ExcelCopyUtil.copySheet(source, destination);
				}
				
				
				SheetInfoBean sheetInfo = new SheetInfoBean();
				
				// Excel에 대한 데이터를 가져온다.
				XSSFSheet source = ((XSSFWorkbook) destinationWorkBook).getSheetAt(0); //첫번째 Sheet / row를 기준으로 생각한다.
				XSSFRow srcRow = source.getRow(0);
				
				XSSFCell emailWorkTargIdNewCell = srcRow.getCell(1); // EML_TASK_SUBJ_UUID
				XSSFCell emailSndLogIdNewCell = srcRow.getCell(2); //EML_SND_HISTREC_UUID
				emailSndLogIdNewCell.setCellValue(reSndLogId); // 재발송 UUID SND_LOG_ID
				emailSndLogIdNewCell.setCellType(1);
				
				
				destinationWorkBook.write(tempOut);
				
				SimpleMultipartFileItem fileItem = AthfServiceUtil.newMultipartFileItem(
						UUID.randomUUID().toString(),
						tempGrpCd,
						excelFileItem.getName(),
						tempFile
				);
				stdFileService.createWithMultipart(fileItem);
			}
			
			
			//메일에서 받아온 파일을 읽어들인다.
			if(excelFileItem.getReference() != null) {
				
				// 화면단에서 정의한 attachment의 file grp_cd를 가지고 취득하여 excel을 가져온다.
				sourceWorkBook = new XSSFWorkbook(OPCPackage.open(excelFileItem.getInputStreamContents()));
				
				String fileNm = UUID.randomUUID() + ".xlsx";
				
				file = new File(fileUploadPath + UUID.randomUUID() + ".xlsx");
				
				out = new FileOutputStream(file);
				
				
				XSSFWorkbook destinationWorkBook = new XSSFWorkbook();
				
				//해당 엑셀 파일 내에 존재하는 Sheet의 개수를 가져온다.
				int sheetCnt = sourceWorkBook.getNumberOfSheets();
				
				for(int i = 0; i < sheetCnt; i++) {
					// Excel에 대한 데이터를 가져온다.
					XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(i);
					XSSFSheet destination = destinationWorkBook.createSheet();
					
					
					ExcelCopyUtil.copySheet(source, destination);
				}
				
				
				// Excel에 대한 데이터를 가져온다.
				XSSFSheet source = ((XSSFWorkbook) destinationWorkBook).getSheetAt(0); //첫번째 Sheet / row를 기준으로 생각한다.
				XSSFRow srcRow = source.getRow(0);
				
				XSSFCell emailWorkTargIdNewCell = srcRow.getCell(1); // EML_TASK_SUBJ_UUID
				XSSFCell emailSndLogIdNewCell = srcRow.getCell(2); //EML_SND_HISTREC_UUID
				emailSndLogIdNewCell.setCellValue(reSndLogId); // 재발송 UUID SND_LOG_ID
				emailSndLogIdNewCell.setCellType(1);
				
				//3. re_snd_cause를 memo생성하여 넣어준다.
				this.createExcelMemo(emailWorkTargIdNewCell, reSndCause);
				
				destinationWorkBook.write(out);
				
				String grpCd = UUID.randomUUID().toString();
				
				SimpleMultipartFileItem fileItem = AthfServiceUtil.newMultipartFileItem(
						UUID.randomUUID().toString(),
						grpCd,
						excelFileItem.getName(),
						file
				);
				stdFileService.createWithMultipart(fileItem);
				
				// 4. 메일 발송
				String emailTaskSubjectUuid = emailWorkTargIdNewCell.getStringCellValue();
				String emailRcptHistoryUuid = receivedEmailInfo.get("eml_rcpt_histrec_uuid") == null ? "" : (String) receivedEmailInfo.get("eml_rcpt_histrec_uuid");
				this.mailWorkFormSend(emailTaskSubjectUuid, sendEmailInfo, mailTit, reSndCause, emailRcptHistoryUuid, grpCd, reSndLogId, tempGrpCd);
				
			} else {
				try {
					throw new FileNotFoundException("메일 EXCEL File이 서버에 존재하지 않습니다.");
				} catch(Exception e) {
					LOG.error(e.getMessage());
				}
			}
		} catch(Exception e) {
			LOG.error(e.getMessage());
		} finally {
			try {
				if(out != null) out.close();
				if(io != null) io.close();
				if(tempIo != null) tempIo.close();
				if(tempOut != null) tempOut.close();
			} catch(Exception e) {
				LOG.error(e.getMessage());
			}
		}
	}
	
	private void createExcelMemo(XSSFCell emailWorkTargIdNewCell, String reSndCause) {
		Drawing drawing = emailWorkTargIdNewCell.getSheet().createDrawingPatriarch();
		//메모크기(0,0,0,0,col,row,col,row)
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 10, 15, 15);
		Comment comment = drawing.createCellComment(anchor);
		comment.setVisible(true);
		
		reSndCause = reSndCause.replace(":", "\n");
		RichTextString textString = new XSSFRichTextString(reSndCause);//메모내용
		comment.setString(textString);
	}
	
	private void mailWorkFormSend(String emailTaskSubjectUuid, Map<String, Object> sendEmailInfo, String mailTit, String reSndCause, String emailRcptHistoryUuid, String grpCd, String reSndLogId, String tempGrpCd) {
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("grp_cd", grpCd);
		resultMap.put("eml_task_subj_uuid", emailTaskSubjectUuid);
		resultMap.put("eml_snd_histrec_uuid", reSndLogId);
		resultMap.put("tmpl_athg_uuid", tempGrpCd); //발송 시 새로 생성해줌.
		
		
		resultMap.put("rcpnt_eml", sendEmailInfo.get("rcpnt_eml"));
		resultMap.put("to_nm", sendEmailInfo.get("to_nm"));
		resultMap.put("task_uuid", sendEmailInfo.get("task_uuid"));
		resultMap.put("eml_task_dtl_uuid", sendEmailInfo.get("eml_task_dtl_uuid"));
		resultMap.put("eml_tit", mailTit);
		resultMap.put("resnd_rsn", reSndCause);
		
		List<Map<String, Object>> mailList = Lists.newArrayList();
		mailList.add(resultMap);
		
		Map<String, Object> mailInfo = Maps.newHashMap();
		mailInfo.put("eml_tmpl_cd", sendEmailInfo.get("eml_tmpl_cd"));
		mailInfo.put("eml_task_uuid", sendEmailInfo.get("eml_task_uuid"));
		mailInfo.put("resnd_rsn", reSndCause);
		
		mailInfo.put("eml_rcpt_histrec_uuid", emailRcptHistoryUuid);
		
		//4. re mail발송을 한다.
		try {
			this.addMailWork(mailInfo, mailList);
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
	}
	
	private FileItem receivedMailGetExcelFile(Map<String, Object> receivedEmailInfo, Map<String, Object> sendEmailInfo) {
		String emailWorkTargId = receivedEmailInfo.get("eml_task_subj_uuid").toString();
		if(StringUtils.isEmpty(emailWorkTargId)) {
			try {
				throw new FileNotFoundException("EML_TASK_SUBJ_UUID 가 존재하지 않습니다.");
			} catch(Exception e) {
				LOG.error(e.getMessage());
			}
		}
		
		if(receivedEmailInfo.get("athg_uuid") == null) {
			try {
				throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
			} catch(FileNotFoundException e) {
				LOG.error(e.getMessage());
			}
		}
		
		FileList fileList = null;
		try {
			fileList = stdFileService.findFileListWithoutContents(receivedEmailInfo.get("athg_uuid").toString());
			if(fileList == null || fileList.getSize() == 0) {
				throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
			}
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
		
		FileItem excelFileItem = null;
		for(FileItem fileItem : fileList.getItems()) {
			if("xlsx".equals(fileItem.getExtension())) {
				excelFileItem = fileItem;
				break;
			} else if(fileItem.getName().indexOf("xlsx") > -1) {
				excelFileItem = fileItem;
				break;
			}
			
		}
		
		if(excelFileItem == null) {
			try {
				throw new FileNotFoundException("엑셀파일이 없습니다");
			} catch(FileNotFoundException e) {
				LOG.error(e.getMessage());
			}
		} else {
			try {
				excelFileItem = stdFileService.findFileItemWithContents(excelFileItem.getId());
			} catch(Exception e1) {
				try {
					throw new FileNotFoundException("파일을 가져오는 중 오류발생!");
				} catch(FileNotFoundException e) {
					LOG.error(e.getMessage());
				}
			}
		}
		
		
		//temp file 을 가져와야함 ( sendmail 에서 처리된 엑셀 템플릿 파일 )
		
		/*FileGroup groupTempFile = fileService.findGroup(sendEmailInfo.get("tmpl_athg_uuid").toString());
		if(group == null || groupTempFile.getSize() == 0) {
			try {
				throw new FileNotFoundException("첨부된 파일이 없는 메일 입니다!");
			} catch(Exception e) {
				LOG.error(e.getMessage());
			}
		}*/
		
		return excelFileItem;
	}
	
	
	/**
	 * (비동기 방식)
	 * 보낼 메일을 ESAMAIL에 저장 실시간 전송일 경우 바로 전송
	 * 이메일 업무 발송용 메소드 이며, MAIL INFO MAP에 담긴 EXCEL CREATE DATA를 통하여 EXCEL CREATE 및 회신관련 내역을 처리한다.
	 *
	 * @param 메일 Key, 메일 내용
	 * @return void
	 * @Method Name : addMailWork
	 */
	public Future<String> addMailWork(Map<String, Object> mailInfo, List<Map<String, Object>> mailList) {
		String mailKey = mailInfo.get("eml_tmpl_cd").toString();
		
		try {
			
			//Store Mail
			for(Map<String, Object> row : mailList) {
				
				String appId = (String) row.get("task_uuid");
				String mailTit = (String) row.get("eml_tit");
				String reSndCause = row.get("resnd_rsn") == null ? "" : row.get("resnd_rsn").toString();
				
				if(!StringUtils.isEmpty(appId)) {
					//app_id를 기준으로 EML_TASK_PRGS_STS 에서 Close_yn을 가져온다. 만약 Y일 경우 메일 발송을 제외한다.
					String mailWorkCloseYn = mailWorkRepository.findCloseYnExcelMailAP(row);
					
					if(!StringUtils.isEmpty(mailWorkCloseYn) && ("N").equals(mailWorkCloseYn)) {
						row.put("eml_tit", mailTit);
						row.put("task_uuid", appId);
						/*row.put("to_nm", row.get("to_nm"));    //수신자 이름
						row.put("rcpnt_eml", row.get("rcpnt_eml"));    //수신자 주소
						row.put("sndr_nm", row.get("sndr_nm") == null ? fromName : row.get("sndr_nm"));    //송신자 이름
						row.put("sndr_eml", row.get("sndr_eml") == null ? fromAddress : row.get("sndr_eml"));//송신자 주소*/
						
						row.put("resnd_rsn", reSndCause);
						//tmp.put("contents", mailCont);
						// EXCEL CREATE & 인증테이블 INSERT & 메일HISTORY,인증테이블 릴레이션테이블 생성  & result grp_cd
						row.put("athg_uuid", row.get("grp_cd"));
						row.put("tmpl_athg_uuid", row.get("tmpl_athg_uuid"));
						
						row.put("eml_task_subj_uuid", row.get("eml_task_subj_uuid") == null ? UUID.randomUUID().toString() : row.get("eml_task_subj_uuid"));
						row.put("eml_snd_histrec_uuid", row.get("eml_snd_histrec_uuid") == null ? UUID.randomUUID().toString() : row.get("eml_snd_histrec_uuid"));
						row.put("eml_task_uuid", mailInfo.get("eml_task_uuid").toString());
						row.put("eml_task_sts_ccd", row.get("eml_task_sts_ccd") == null ? "N" : row.get("eml_task_sts_ccd"));
						row.put("eml_task_dtl_uuid", row.get("eml_task_dtl_uuid")); // public key (ex. vd_cd)
						row.put("eml_re_yn", row.get("eml_re_yn") == null ? "N" : row.get("eml_re_yn")); // public key (ex. vd_cd)
						row.put("resnd_rsn", mailInfo.get("resnd_rsn"));
						row.put("eml_rcpt_histrec_uuid", mailInfo.get("eml_rcpt_histrec_uuid"));
						
						//email excel send insert
						mailWorkRepository.insertExcelWorkSendMail(row);
						
						Future<MailResult> future = mailService.sendAsync(mailKey, null, row);
						MailResult mailResult = future.get();
						
						if(mailResult.isSuccess()) {
							row.put("eml_task_sts_ccd", "RESND_RECVG");
							row.put("snd_sts_ccd", "RESND_PASS");
							row.put("eml_re_yn", "N");
						} else {
							row.put("rcpt_res_msg", mailResult.getErrorMessage());
							row.put("eml_task_sts_ccd", "RESND_RCPT_ERR");
							row.put("snd_sts_ccd", "RESND_FAIL");
							row.put("eml_re_yn", "N");
						}
						
						row.put("eml_snd_uuid", mailResult.getEmlSndUuid());
						// 이메일 업무 이력 테이블에 이메일 이력 연결
						// 전송만 실패한 경우에는 실패해도 이메일 이력은 연결됨
						mailWorkRepository.updateExcelWorkSendMail(row);
						// 이메일 업무 상태 update
						mailWorkRepository.updateEmailWorkSendComplete(row);
						//전송시간 및 상태  UPDATE 처리
						mailWorkRepository.updateEmailWorkSendSuccess(row);
						
						mailWorkReceivedRepository.updateEmailWorkReceivedStatus(row);
					}
				}
			}
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return new AsyncResult<String>("success");
	}
}