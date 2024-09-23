package smartsuite.app.bp.admin.mailManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import smartsuite.app.bp.admin.mailManager.service.MailManagerService;

import smartsuite.app.common.shared.ResultMap;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value="**/bp/**/")
public class MailManagerController {

	@Inject
	MailManagerService mailManagerService;

	/* 메일 목록 조회 */
	@RequestMapping(value="findListMail.do")
	public @ResponseBody
    List findListMail(@RequestBody Map param){
		return mailManagerService.findListMail(param);
	}
	
	
	/* 메일 목록 상세 조회(단건조회) */
	@RequestMapping(value="findListMailTemplate.do")
	public @ResponseBody
    Map findListMailTemplate(@RequestBody Map param){
		return mailManagerService.findListMailTemplate(param);
	}
	/* 메일 목록 상세 조회(단건조회) */
	@RequestMapping(value="findMailMultiLangTemplateInfo.do")
	public @ResponseBody
    Map findListMailTemplateMultiLangInfo(@RequestBody Map param){
		return mailManagerService.findMailMultiLangTemplateInfo(param);
	}

	/* 삭제 */
	@RequestMapping(value="deleteListMail.do")
	public @ResponseBody
	ResultMap deleteListMail(@RequestBody Map param){
		return mailManagerService.deleteListMailRequest(param);
	}
	
	/* 저장 */
	@RequestMapping(value="saveListMail.do")
	public @ResponseBody
    ResultMap saveListMail(@RequestBody Map param){
		return mailManagerService.saveListMail(param);
	}
	
	/* combobox 목록조회 */
	@RequestMapping(value="findListEmailTemplate.do")
	public @ResponseBody
    List findListEmailTemplate(@RequestBody Map param){
		return mailManagerService.findListEmailTemplate(param);
	}
	
	/* 메일전송이력 조회 */
	@RequestMapping(value="findListMailSendHistory.do")
	public @ResponseBody
    List findListMailSendHistory(@RequestBody Map param){
		return mailManagerService.findListMailSendHistory(param);
	}
	
	/* 다중메일 전송 이력 조회 */
	@RequestMapping(value="findMultiMailSendHistory.do")
	public @ResponseBody
    List findMultiMailSendHistory(@RequestBody Map param){
		return mailManagerService.findMultiMailSendHistory(param);
	}
	
	@RequestMapping(value="findMultiMailSendHistoryDetail.do")
	public @ResponseBody
	Map findMultiMailSendHistoryDetail(@RequestBody Map param){
		return mailManagerService.findMultiMailSendHistoryDetail(param);
	}
	
	@RequestMapping(value="sendByMailHistrec.do")
	public @ResponseBody ResultMap sendByMailHistrec(@RequestBody Map param) {
		return mailManagerService.sendByMailHistrec(param);
	}

	/* SMTP 연동 테스트  */
	@RequestMapping(value="smtpSyncTestSendMail.do")
	public @ResponseBody
    ResultMap smtpSyncTestSendMail(@RequestBody Map param){
		return mailManagerService.smtpSyncTestSendMail(param);
	}
	


}
