package smartsuite.app.common.mail.mailWorkExcel;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.excel.bean.ExcelInfoBean;
import smartsuite.app.common.mail.MailReceiveHandler;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.mail.data.FileInfo;
import smartsuite.app.common.mail.data.MailInfo;
import smartsuite.app.common.mail.data.MailResult;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkReceivedRepository;
import smartsuite.app.common.mail.mailWorkExcel.repository.MailWorkRepository;
import smartsuite.app.common.mail.receiver.MailReceiver;
import smartsuite.upload.StdFileService;
import smartsuite.upload.entity.SimpleMultipartFileItem;
import smartsuite.upload.util.AthfServiceUtil;

import javax.inject.Inject;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class MailWorkSendService {

	static final Logger LOG = LoggerFactory.getLogger(MailWorkSendService.class);

	@Autowired(required=false)
	private MailReceiveHandler mailReceiveHandler;

	@Inject
	private MailReceiver mailReceiver;

	/** Mail Sender Default Email Address */
	@Value("#{mail['mail.sender.address']}")
	private String fromAddress;

	/** Mail Sender Default Name */
	@Value("#{mail['mail.sender.name']}")
	private String fromName;

	@Inject
	StdFileService stdFileService;

	@Inject
	MailWorkReceivedRepository mailWorkReceivedRepository;

	@Inject
	MailWorkRepository mailWorkRepository;

	@Inject
	MailService mailService;

	/**
	 *
	 * (비동기 방식)
	 * 보낼 메일을 ESAMAIL에 저장 실시간 전송일 경우 바로 전송
	 * 이메일 업무 발송용 메소드 이며, MAIL INFO MAP에 담긴 EXCEL CREATE DATA를 통하여 EXCEL CREATE 및 회신관련 내역을 처리한다.
	 *
	 * @param 메일 Key, 메일 내용
	 * @return void
	 * @Method Name : addMailWork
	 */
	public Future<String> addMailWork(ExcelInfoBean excelInfoBean, List<Map<String, Object>> mailList) {
		String mailKey = excelInfoBean.getEml_tmpl_cd();

		try {
			//Store Mail
			for(Map<String, Object> row : mailList){

				String appId = (String) row.get("task_uuid");
				String mailTit = (String) row.get("eml_tit");

				if(!StringUtils.isEmpty(appId)) {
					//app_id를 기준으로 EML_TASK_PRGS_STS 에서 Close_yn을 가져온다. 만약 Y일 경우 메일 발송을 제외한다.
					String mailWorkCloseYn = mailWorkRepository.findCloseYnExcelMailAP(row);

					if (StringUtils.isEmpty(mailWorkCloseYn) || ("N").equals(mailWorkCloseYn)) {
						row.put("eml_tit", mailTit);
						row.put("task_uuid", appId);
						/*row.put("to_nm"			, row.get("to_nm")); 	//수신자 이름
						row.put("rcpnt_eml"		, row.get("rcpnt_eml")); 	//수신자 주소
						row.put("sndr_nm"		, row.get("sndr_nm")   == null ? fromName    : row.get("sndr_nm"));	//송신자 이름
						row.put("sndr_eml"		, row.get("sndr_eml") == null ? fromAddress : row.get("sndr_eml"));//송신자 주소*/

						// EXCEL CREATE & 인증테이블 INSERT & 메일HISTORY,인증테이블 릴레이션테이블 생성  & result grp_cd
						row.put("athg_uuid",row.get("athg_uuid"));
						row.put("tmpl_athg_uuid",row.get("tmpl_athg_uuid"));

						row.put("eml_task_subj_uuid",row.get("eml_task_subj_uuid") == null? UUID.randomUUID().toString() : row.get("eml_task_subj_uuid"));
						row.put("eml_snd_histrec_uuid",row.get("eml_snd_histrec_uuid") == null? UUID.randomUUID().toString() : row.get("eml_snd_histrec_uuid"));
						row.put("eml_task_uuid",excelInfoBean.getEml_task_uuid());
						row.put("eml_task_sts_ccd",row.get("eml_task_sts_ccd") == null? "N" : row.get("eml_task_sts_ccd"));
						row.put("eml_task_dtl_uuid",row.get("eml_task_dtl_uuid")); // public key (ex. vd_cd)
						row.put("eml_re_yn",row.get("eml_re_yn") == null? "N" : row.get("eml_re_yn")); // public key (ex. vd_cd)

						//email excel template info & application process insert
						mailWorkRepository.insertExcelWorkApplicationProcess(row);

						//email excel send insert
						mailWorkRepository.insertExcelWorkSendMail(row);
						
						Future<MailResult> future = mailService.sendAsync(mailKey, null, row);
						MailResult mailResult = future.get();
						
						if(mailResult.isSuccess()) {
							row.put("eml_task_sts_ccd", "EML_RECVG");
							row.put("snd_sts_ccd", "SND_PASS");
							row.put("eml_re_yn", "N");
						} else {
							row.put("rcpt_res_msg", mailResult.getErrorMessage());
							row.put("eml_task_sts_ccd", "SND_ERR");
							row.put("snd_sts_ccd", "SND_FAIL");
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
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return new AsyncResult<String>("success");
	}

	public void processMail(HashMap<String,Object> param){
		processReplyMail(param);
	}

	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void processReplyMail(HashMap<String,Object> param){
		List<Map<String, Object>> mailConnectList = mailWorkReceivedRepository.findNotProcessedMailWork(param);

		for(Map<String, Object> mailConnect : mailConnectList){
			List<Map<String,Object>> mailInfoList = mailWorkReceivedRepository.findExcelReceiveMailById(mailConnect);

			for(Map<String,Object> mailInfo : mailInfoList){
				if(mailReceiveHandler != null){
					try{
						mailReceiveHandler.handleMail(mailInfo);
						mailConnect.put("rcpt_sts_ccd", "EML_RCPT_PASS");
					}catch(Exception e){
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						String sStackTrace = sw.toString();
						LOG.info(sStackTrace);
						mailConnect.put("rcpt_sts_ccd", "EML_RCPT_FAIL");
						mailConnect.put("rcpt_res_msg", sStackTrace);

						String emailWorkTargId = mailInfo.get("eml_task_subj_uuid") == null? "" :mailInfo.get("eml_task_subj_uuid").toString();

						if(!StringUtils.isEmpty(emailWorkTargId)){
							mailConnect.put("eml_task_subj_uuid", emailWorkTargId);
							mailConnect.put("eml_re_yn", "N");
							mailConnect.put("eml_task_sts_ccd", "RE_ERR");

							//회신 시 EMAIL_WORK_TARG_ID가 존재한다면, 회신 오류로 표기 한다. ( 재발송을 위하여 )
							mailWorkRepository.updateEmailWorkSendComplete(mailConnect);
						}

					}
				}
				mailWorkReceivedRepository.updateRepliedMail(mailConnect);
			}

		}
	}
	
	public static void main(String[] args) throws ParseException {
		String myString = "20230701";
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		Date mydate = dtFormat.parse(myString);
		
		System.out.println(mydate);
	}


	/**
	 * 배치 메일 수신
	 * @throws Exception
	 */
	public void receiveMail(HashMap<String,Object> param){
		Map<String,Object> lastReceivedMail = mailWorkReceivedRepository.findLastReceivedMail(param);

		Date lastSentDate = null;
		//lastReceivedMail이 null이면 개발날짜 기준으로 defaultValue를 정해 가져온다
		if(lastReceivedMail == null || lastReceivedMail.get("snd_dttm") == null){
			lastSentDate = new Date(1551254400000L);
		}else{
			lastSentDate = (Date)lastReceivedMail.get("snd_dttm");
		}

		List<MailInfo> newMails = new ArrayList<MailInfo>();
		try {
			newMails =  mailReceiver.receiveEmail(lastSentDate);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		for(MailInfo newMail : newMails){
			Map newMailMap = new HashMap();
			try{
				newMailMap = convertMailInfoToParam(newMail);
				Map<String,Object> getFileInfo = emailWorkGenerateFileGroupCode(newMail);
				String emailWorkTargId = "";
				String emailSndLogId = "";
				String emailReceivedLogId = UUID.randomUUID().toString();
				if(null != getFileInfo){
					emailWorkTargId = getFileInfo.get("eml_task_subj_uuid") == null? "" :getFileInfo.get("eml_task_subj_uuid").toString();
					emailSndLogId = getFileInfo.get("eml_snd_histrec_uuid") == null? "" :getFileInfo.get("eml_snd_histrec_uuid").toString();
					newMailMap.put("eml_task_subj_uuid", emailWorkTargId);
					newMailMap.put("eml_snd_histrec_uuid", emailSndLogId);
					newMailMap.put("athg_uuid", getFileInfo.get("athg_uuid"));


					if(!StringUtils.isEmpty(emailWorkTargId)){
						newMailMap.put("eml_task_sts_ccd", "RE_CMPLD"); //파일이 있고, email_work_targ_id가 있을 경우 회신 메일로 본다.
						newMailMap.put("rcpt_eml_typ_ccd", "RE"); //파일이 있고, email_work_targ_id가 있을 경우 회신 메일로 본다.
						newMailMap.put("rcpt_sts_ccd", "SYS_PRCSG_PRGSG");
						newMailMap.put("eml_re_yn", "Y");

						mailWorkRepository.updateEmailWorkSendComplete(newMailMap);
					}else{
						newMailMap.put("rcpt_eml_typ_ccd", "CM"); //파일이 있고, email_work_targ_id가 없을경우 연관되지 않은 메일로 판단한다.
						newMailMap.put("rcpt_sts_ccd", "EML_RCPT_PASS");
					}

				}else{
					newMailMap.put("rcpt_eml_typ_ccd", "CM"); //FILE이 없을경우 연관되지 않은 메일로 판단한다.
					newMailMap.put("rcpt_sts_ccd", "SYS_PRCSG_PRGSG");
				}

				newMailMap.put("eml_rcpt_histrec_uuid", emailReceivedLogId);
				newMailMap.put("rcpnt_eml",fromAddress);
				newMailMap.put("to_nm",fromName);
			}catch(Exception e){
				StringWriter sw = new StringWriter();
//				PrintWriter pw = new PrintWriter(sw);
				String sStackTrace = sw.toString();
				String resMsg = newMailMap.get("res_msg") == null ? "" : newMailMap.get("res_msg").toString();
				String emailReceivedLogId = UUID.randomUUID().toString();
				newMailMap.put("eml_rcpt_histrec_uuid", emailReceivedLogId);
				newMailMap.put("rcpt_res_msg", resMsg + sStackTrace);
				newMailMap.put("rcpt_sts_ccd", "EML_RCPT_FAIL");
				newMailMap.put("rcpt_eml_typ_ccd", "NP"); //에러가 난 경우, 분류되지 않은것으로 판단한다.
				newMailMap.put("rcpnt_eml",fromAddress);
				newMailMap.put("to_nm",fromName);
				LOG.info(sStackTrace);
			}finally {
				if(StringUtils.isNotEmpty(String.valueOf(newMailMap.get("eml_task_subj_uuid")))){

					List<Map<String,Object>> checkReceivedMail = mailWorkReceivedRepository.findLastReceivedMailCheck(newMailMap);

					if(checkReceivedMail.size() > 0){
						mailWorkReceivedRepository.insertReceivedMail(newMailMap);
					}

				}
			}
		}
	}

	private Map<String,Object> emailWorkGenerateFileGroupCode(MailInfo mail){
		List<FileInfo> attaches = mail.getAttachments();
		Map<String,Object> resultMap = Maps.newHashMap();
		if(attaches.size() == 0){
			return null;
		}
		String groupId = null;
		String emailWorkTargId = "";
		String emailSndLogId = "";
		FileInputStream mockFileInputStream = null;

		try{
			groupId = UUID.randomUUID().toString();
			for(FileInfo fileinfo : attaches){
				String fileId = UUID.randomUUID().toString();
				File file = fileinfo.getFile();
				String fileName = fileinfo.getFileName();
				
				SimpleMultipartFileItem fileItem = AthfServiceUtil.newMultipartFileItem(
						fileId,
						groupId,
						fileName,
						file
				);
				stdFileService.createWithMultipart(fileItem);

				if(StringUtils.isEmpty(emailWorkTargId)){ //존재할 경우 다시 읽어올 필요성 X
					if(("xlsx".equals(fileItem.getExtension())) || (fileItem.getName().indexOf("xlsx") > -1)){ //excel 첨부파일이 존재할 경우,
						Workbook sourceWorkBook = new XSSFWorkbook(OPCPackage.open(fileItem.getFile()));
						XSSFSheet source = ((XSSFWorkbook) sourceWorkBook).getSheetAt(0);
						if(null != source){
							XSSFRow row = source.getRow(0);
							if(null != row){
								XSSFCell cell = row.getCell(1);
								XSSFCell sdnLogcell = row.getCell(2);
								if(null != cell){
									emailWorkTargId = cell.getStringCellValue() == null? "" : cell.getStringCellValue();
								}
								if(null != sdnLogcell){
									emailSndLogId = sdnLogcell.getStringCellValue() == null? "" : sdnLogcell.getStringCellValue();
								}
							}
						}
					}
				}
			}

			resultMap.put("athg_uuid",groupId);
			resultMap.put("eml_task_subj_uuid",emailWorkTargId);
			resultMap.put("eml_snd_histrec_uuid",emailSndLogId);
		}catch(Exception e){
			try{
				LOG.error(e.getMessage());
				throw new FileNotFoundException("첨부파일 코드 생성 중 오류 발생");
			}catch (Exception e1){
				LOG.error(e1.getMessage());
			}
		}finally {
			if(null != mockFileInputStream) try {
				mockFileInputStream.close();
			}catch (Exception e){
				LOG.error(e.getMessage());
			}
		}
		try{
			for(FileInfo fileinfo : attaches){
				File file = fileinfo.getFile();
				if(file.delete()){
					LOG.debug("이메일 첨부파일 삭제 성공");
				}else{
					LOG.debug("이메일 첨부파일 삭제 실패");
				}
			}
		}catch(Exception e){
			LOG.error(e.getMessage());
		}

		return resultMap;
	}

	private Map<String,Object> convertMailInfoToParam(MailInfo mail){
		Map<String,Object> mailMap = new HashMap<String,Object>();
		mailMap.put("eml_tit", mail.getSubject());
		mailMap.put("eml_cont", mail.getText());
		mailMap.put("sndr_eml", mail.getFrom());
		mailMap.put("rcpt_dttm", mail.getSentDate());
		mailMap.put("sndr_nm", mail.getFromName());
		mailMap.put("rcpt_res_msg",mail.getParseError());
		return mailMap;
	}

	public void deleteEmailWorkReceivedMailList(Map<String, Object> emailWorkSendProcInfo) {
		mailWorkReceivedRepository.deleteEmailWorkReceivedMailList(emailWorkSendProcInfo);
	}
}
