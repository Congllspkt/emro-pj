package smartsuite.app.bp.admin.mailWork;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.common.mail.mailWorkExcel.MailWorkSendService;

import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/bp/**/")
public class MailWorkController {

	static final Logger LOG = LoggerFactory.getLogger(MailWorkController.class);

	@Inject
    MailWorkService mailWorkService;

	@Inject
	MailWorkSendService mailWorkSendService;

	@Inject
	MailWorkManagerService mailWorkManagerService;

	
    @Value ("#{file['file.upload.temp.path']}")
    String fileTempPath;

	/* 저장 */
	@RequestMapping(value="saveExcelInfo.do")
	public @ResponseBody
    ResultMap saveExcelInfo(@RequestBody Map param){
		return mailWorkManagerService.saveExcelInfo(param);
	}


	@RequestMapping(value="updateExcelInfoConfirmYn.do")
	public @ResponseBody
    ResultMap updateExcelInfoConfirmYn(@RequestBody Map param){
		return mailWorkManagerService.updateExcelInfoConfirmYn(param);
	}

	/**
	 * Excel Info 정보를 통하여 해당 엑셀 정보를 삭제
	 */
	@RequestMapping(value="deleteExcelInfo.do")
	public @ResponseBody ResultMap deleteExcelInfo(@RequestBody Map param){
		return mailWorkManagerService.deleteExcelInfo(param);
	}


	/**
	 * 엑셀 사용 여부 수정
	 * @param param
	 * @return
	 */
	@RequestMapping(value="updateUseYnSaveExcel.do")
	public @ResponseBody  ResultMap updateUseYnSaveExcel(@RequestBody Map param){
		return mailWorkManagerService.updateUseYnSaveExcel(param);
	}

	/**
	 * 업무코드에 따라 해당하는 업무에 할당된 이메일 업무 리스트를 가져온다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findListWorkExcelList.do")
	public @ResponseBody
    List findListWorkExcelList(@RequestBody Map param){
		return mailWorkManagerService.findListWorkExcelList(param);
	}

	/**
	 * 업무 코드에 따라 현재 해당 업무에 할당되어 있는 이메일 업무 ( excelinfo )를 가져온다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findListExcelInfoList.do")
	public @ResponseBody
    List findListExcelInfoList(@RequestBody Map param){
		return mailWorkManagerService.findListExcelInfoList(param);
	}

	/**
	 * 업무 info 정보에서 선택된 Sheet 리스트를 가져온다, ( 콤보박스용 )
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findListSheetInfoListCombo.do")
	public @ResponseBody
    List findListSheetInfoListCombo(@RequestBody Map param){
		return mailWorkManagerService.findListSheetInfoListCombo(param);
	}


	/**
	 * 엑셀의 Sheet ID별 ROW LIST를 조회 한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findExcelRowListBySheetId.do")
	public @ResponseBody
    List findExcelRowListBySheetId(@RequestBody Map param){
		return mailWorkManagerService.findExcelRowListBySheetId(param);
	}

	/**
	 * 엑셀의 Row ID별 Celk LIST를 조회 한다.
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findExcelCellListByRowId.do")
	public @ResponseBody
    Map findExcelCellListByRowId(@RequestBody Map param){

		Map<String, Object> excelInfoData = Maps.newHashMap();
		List<Map<String,Object>> cellList = mailWorkService.findExcelCellListByRowId(param);

		excelInfoData.put("cellList", cellList);

		return excelInfoData;
	}


	/* 업무메일 리스트 목록조회 Combobox용 */
	@RequestMapping(value="getMailWorkList.do")
	public @ResponseBody
	List getMailWorkList(@RequestBody Map param){
		return mailWorkManagerService.getMailWorkList(param);
	}


	/* ExcelInfo Field grid용 combobox조회 */
	@RequestMapping(value="findSheetFieldValue.do")
	public @ResponseBody
	List findSheetFieldValue(@RequestBody Map param){
		return mailWorkManagerService.findSheetFieldValue(param);
	}

	/**
	 * 엑셀의 Sheet Config를 저장/수정 한다.
	 */
	@RequestMapping(value = "saveListExcelSheetHeader.do")
	public @ResponseBody ResultMap saveListExcelSheetHeader(@RequestBody Map param){
		return mailWorkService.saveListExcelSheetHeader(param);
	}

	/**
	 * task_uuid / app_dtl_id를 가지고 발송 내역을 조회
	 * @param param
	 * @return
	 */
	@RequestMapping(value="findListEmailWorkSendHistory.do")
	public @ResponseBody
	List findListEmailWorkSendHistory(@RequestBody Map param){
		return mailWorkService.findListEmailWorkSendHistory(param);
	}

	/**
	 * Excel Template 를 다운로드 한다. ( 이메일 업무 관리에 업로드한 양식으로 다운로드 )
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/**/downLoadMailWorkExcel.do")
	public @ResponseBody ResponseEntity<byte[]> downLoadMailWorkExcel(@RequestBody Map param) {
		Map<String, Object> excelInfoData = Maps.newHashMap();
		excelInfoData.put("eml_task_uuid", param.get("eml_task_uuid"));
		return mailWorkManagerService.downLoadMailExcel(excelInfoData);
	}

	/**
	 * 업무 히스토리 화면에서 메일을 수신 처리할 때 사용하는 내역
	 */
	@RequestMapping(value="/**/mailWorkGetMailInboxProc.do")
	public @ResponseBody void mailWorkGetMailInboxProc(@RequestBody Map param){
		try{
			mailWorkSendService.receiveMail(new HashMap<String, Object>());
		}catch (Exception e){
			LOG.error(e.getMessage());
		}finally {
			try{
				mailWorkSendService.processReplyMail(new HashMap<String, Object>());
			}catch (Exception e){
				LOG.error(e.getMessage());
			}
		}
	}

	@RequestMapping(value="/**/excelTemplateSetupAfterDownLoad.do")
	public ResponseEntity<byte[]> excelTemplateSetupAfterDownLoad(HttpServletRequest request,@RequestBody Map param, HttpServletResponse response) {
		return mailWorkManagerService.excelTemplateSetupAfterDownLoad(param);
	}


	@RequestMapping(value="/**/excelTemplateMailSendTest.do")
	public void excelTemplateMailSendTest(HttpServletRequest request,@RequestBody Map param, HttpServletResponse response) {
		mailWorkManagerService.mailTemplateMailSendTest(param);
	}

}
