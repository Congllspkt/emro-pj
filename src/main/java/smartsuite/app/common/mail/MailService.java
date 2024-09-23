package smartsuite.app.common.mail;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.mailManager.repository.MailManagerRepository;
import smartsuite.app.common.mail.data.MailResult;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.sender.MailSender;
import smartsuite.app.common.mail.strategy.MailStrategy;
import smartsuite.app.common.template.service.TemplateGeneratorService;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class MailService implements InitializingBean {
	
	static final Logger LOG = LoggerFactory.getLogger(MailService.class);
	
	@Inject
	List<MailStrategy> mailStrategies;
	
	@Value("#{mail['mail.sender.address']}")
	private String fromAddress;
	
	@Value("#{mail['mail.sender.name']}")
	private String fromName;
	
	@Inject
	TemplateGeneratorService templateGeneratorService;
	
	@Inject
	MailManagerRepository mailManagerRepository;
	
	/** SMTP  */
	@Inject
	MailSender mailSender;
	
	@Async
	public CompletableFuture<MailResult> sendAsync(String emailTemplateId, String appId) {
		return this.sendAsync(emailTemplateId, appId, null);
	}
	
	@Async
	public CompletableFuture<MailResult> sendAsync(String emailTemplateId, String appId, Map<String, Object> data) {
		MailResult mailResult = null;
		try {
			// Async 수행 시 동일 트랜잭션 데이터 생성 시점과 맞지 않는 경우 발생하여 1초 딜레이
			Thread.sleep(1000);
			mailResult = this.send(emailTemplateId, appId, data);
		} catch(InterruptedException e) {
			throw new RuntimeException(e);
		}
		return new AsyncResult<MailResult>(mailResult).completable();
	}
	
	public void send(String emailTemplateId, String appId) {
		this.send(emailTemplateId, appId, null);
	}
	
	public MailResult send(String emailTemplateId, String appId, Map<String, Object> data) {
		if(Strings.isNullOrEmpty(emailTemplateId)) {
			return MailResult.getInstance(false, "emailTemplateId empty");
		}
		if(Strings.isNullOrEmpty(appId) && data == null) {
			return MailResult.getInstance(false, "appId and data empty");
		}
		
		MailStrategy mailStrategy = this.findModule(emailTemplateId);
		if(mailStrategy == null) {
			LOG.error("not exists template!");
			return MailResult.getInstance(false, "not exists template!");
		}
		TemplateMailData templateMailData = null;
		try {
			templateMailData = mailStrategy.getTemplateMailData(appId, data);
		} catch(Exception e) {
			LOG.error("template Mail data load failed!");
			return MailResult.getInstance(false, "template Mail data load failed!");
		}
		if(templateMailData == null) {
			LOG.error("not exists template mail information");
			return MailResult.getInstance(false, "not exists template mail information");
		}
		if(templateMailData.getReceivers() == null) {
			LOG.error("receivers empty");
			return MailResult.getInstance(false, "receivers empty");
		}
		if(templateMailData.getReceivers().size() == 0) {
			LOG.error("receivers empty");
			return MailResult.getInstance(false, "receivers empty");
		}
		
		return this.process(emailTemplateId, templateMailData, false);
	}
	
	private MailResult process(String emailTemplateId, TemplateMailData templateMailData, boolean isTest) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("eml_tmpl_cd", emailTemplateId);
		
		Map<String, Object> templateInfo = mailManagerRepository.findListMailTemplate(param);
		
		// 템플릿 기초 아이디가 없을 경우 이메일로 셋팅
		if(Strings.isNullOrEmpty((String) templateInfo.get("tmpl_typ_ccd"))) {
			templateInfo.put("tmpl_typ_ccd", "EML"); // 이메일
		}
		
		//useYn이 N일 경우 데이터 생성 X
		String useYn = (String) templateInfo.get("use_yn");
		if(useYn != null && "N".equals(useYn)) {
			return MailResult.getInstance(false, "not use");
		}
		
		// 별도의 제목을 설정하지 않은 경우 메일 템플릿의 제목 사용
		if(templateMailData.getTitle() == null) {
			String title = null;
			try {
				title = this.createTitle(templateInfo, templateMailData.getParameter());
			} catch(Exception e) {
				LOG.error(e.getMessage(), e);
				return MailResult.getInstance(false, "create title failed");
			}
			templateMailData.setTitle(title);
		}
		
		Map<String, Object> parameter = templateMailData.getParameter();
		String contents = null;
		try {
			contents = this.createMailContents(emailTemplateId, templateInfo, templateMailData.getTitle(), parameter);
		} catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return MailResult.getInstance(false, "create contents failed");
		}
		
		if(contents == null) {
			return MailResult.getInstance(false, "not exists template");
		}
		
		TemplateMailData.Sender sender = templateMailData.getSender();
		if(sender == null) {
			sender = TemplateMailData.Sender.getInstance();
			sender.setAddress(this.fromAddress);
			sender.setName(this.fromName);
			templateMailData.setSender(sender);
		}
		
		String emlSndUuid = null;
		if(!isTest) {
			// 테스트 전송이 아닌 경우 이력 관리
			this.exceptSendMailSubject(emailTemplateId, templateMailData);
			emlSndUuid = this.insertMailHistory(emailTemplateId, contents, templateMailData);
		}
		// 테스트 전송이거나 실시간 전송인 경우
		if(isTest || "RT".equals(templateInfo.get("eml_snd_typ_ccd"))) {
			return this.mailSendProcess(emlSndUuid, contents, templateMailData, isTest);
		}
		return MailResult.getInstance(true, null, emlSndUuid);
	}
	
	/**
	 * 메일 제목 생성
	 * @param templateInfo
	 * @param data
	 * @return
	 */
	private String createTitle(Map<String, Object> templateInfo, Map<String, Object> data) throws TemplateException, IOException {
		String titTemplate = (String) templateInfo.getOrDefault("ctmpl_nm","");
		return templateGeneratorService.freemarkerTemplateGenerate(UUID.randomUUID().toString(), titTemplate, data);
	}
	
	/**
	 * 메일 템플릿과 데이터 파라미터를 통해 메일 contents 생성
	 *
	 * @param emailTemplateId
	 * @param templateInfo
	 * @param data
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private String createMailContents(String emailTemplateId, Map<String, Object> templateInfo, String title, Map<String, Object> data) throws TemplateException, IOException {
		String result = null;
		
		// 메일 관리 템플릿 변환
		String mailContent = templateGeneratorService.mailTemplateGenerate(emailTemplateId, data);
		
		// 공용 템플릿 메일 폼 조회
		Map<String, Object> generatorTemplate = templateGeneratorService.findTemplateInfoByTemplateClassAndTemplateBaseId(templateInfo);
		String content;
		if(generatorTemplate.get("basc_ctmpl_cont") == null) {
			content = (String) generatorTemplate.get("display_basc_ctmpl_cont");
		} else {
			content = (String) generatorTemplate.get("basc_ctmpl_cont");
		}
		Map<String, Object> param = Maps.newHashMap();
		param.put("contents", mailContent);
		param.put("title", title);
		
		// 공용 템플릿에 업무 별 메일 포함하여 변환
		result = templateGeneratorService.freemarkerTemplateGenerate(emailTemplateId, content, param);
		return result;
	}
	
	/**
	 * 다중 이력 관리 테이블 저장
	 * @param emailTemplateId
	 * @param contents
	 * @param templateMailData
	 */
	private String insertMailHistory(String emailTemplateId, String contents, TemplateMailData templateMailData) {
		List<TemplateMailData.Receiver> toReceivers = templateMailData.getReceiversByRecipientType(RecipientType.TO);
		List<TemplateMailData.Receiver> ccReceivers = templateMailData.getReceiversByRecipientType(RecipientType.CC);
		List<TemplateMailData.Receiver> bccReceivers = templateMailData.getReceiversByRecipientType(RecipientType.BCC);
		
		String[] toReceiverAddress = this.addressToStringArray(toReceivers);
		String[] toReceiverName = this.nameToStringArray(toReceivers);
		String[] toReceiverId = this.idToStringArray(toReceivers);
		String[] toReceiverXceptYn = this.xceptYnToStringArray(toReceivers);
		String[] ccReceiverAddress = this.addressToStringArray(ccReceivers);
		String[] ccReceiverName = this.nameToStringArray(ccReceivers);
		String[] ccReceiverId = this.idToStringArray(ccReceivers);
		String[] ccReceiverXceptYn = this.xceptYnToStringArray(ccReceivers);
		String[] bccReceiverAddress = this.addressToStringArray(bccReceivers);
		String[] bccReceiverName = this.nameToStringArray(bccReceivers);
		String[] bccReceiverId = this.idToStringArray(bccReceivers);
		String[] bccReceiverXceptYn = this.xceptYnToStringArray(bccReceivers);
		
		Map<String, Object> history = Maps.newHashMap();
		String emlSndUuid = UUID.randomUUID().toString();
		history.put("eml_snd_uuid", emlSndUuid);
		history.put("eml_tmpl_cd", emailTemplateId);
		history.put("eml_tit", templateMailData.getTitle());
		history.put("eml_cont", contents);
		history.put("sndr_eml", templateMailData.getSender().getAddress());
		history.put("sndr_nm", templateMailData.getSender().getName());
		history.put("rcpnt_eml_list", String.join(",", toReceiverAddress));
		history.put("rcpnt_nm_list", String.join(",", toReceiverName));
		history.put("rcpnt_id_list", String.join(",", toReceiverId));
		history.put("rcpnt_xcept_yn_list", String.join(",", toReceiverXceptYn));
		history.put("cc_eml_list", String.join(",", ccReceiverAddress));
		history.put("cc_nm_list", String.join(",", ccReceiverName));
		history.put("cc_id_list", String.join(",", ccReceiverId));
		history.put("cc_xcept_yn_list", String.join(",", ccReceiverXceptYn));
		history.put("bcc_eml_list", String.join(",", bccReceiverAddress));
		history.put("bcc_nm_list", String.join(",", bccReceiverName));
		history.put("bcc_id_list", String.join(",", bccReceiverId));
		history.put("bcc_xcept_yn_list", String.join(",", bccReceiverXceptYn));
		history.put("athg_uuid", templateMailData.getAttachmentGroupId());
		mailManagerRepository.insertMultiMailInfo(history);
		
		return emlSndUuid;
	}
	
	private String[] addressToStringArray(List<TemplateMailData.Receiver> list) {
		String[] result = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i).getAddress();
		}
		return result;
	}
	
	private String[] nameToStringArray(List<TemplateMailData.Receiver> list) {
		String[] result = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i).getName();
		}
		return result;
	}
	
	private String[] idToStringArray(List<TemplateMailData.Receiver> list) {
		String[] result = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i).getId();
		}
		return result;
	}
	private String[] xceptYnToStringArray(List<TemplateMailData.Receiver> list) {
		String[] result = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i).getXceptYn();
		}
		return result;
	}
	
	/**
	 * 테스트 메일 발송
	 *
	 * @param emailTemplateId
	 * @param param
	 */
	public void test(String emailTemplateId, Map<String, Object> param) {
		if(Strings.isNullOrEmpty(emailTemplateId)) {
			return;
		}
		
		// 수신자 생성
		TemplateMailData.Receiver receiver = TemplateMailData.Receiver.getInstance(RecipientType.TO, (String) param.get("rcpnt_eml"), (String) param.get("to_nm"));
		
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		templateMailData.addReceiver(receiver);
		templateMailData.setParameter((Map<String, Object>) param.get("data"));
		
		this.process(emailTemplateId, templateMailData, true);
	}
	
	/**
	 * 메일 이력관리에서 재전송 시 이력 테이블 조회 후 메일 발송 프로세스 수행
	 * @param emlSndUuid
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public MailResult sendByMailHistrec(String emlSndUuid) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("eml_snd_uuid", emlSndUuid);
		param.put("snd_yn", "Y");

		Map<String, Object> emlHistrec = mailManagerRepository.findMultiMailSendHistoryDetail(param);
		String rcpntEmlList = emlHistrec.get("rcpnt_eml_list") == null? "" :  (String) emlHistrec.get("rcpnt_eml_list");
		String rcpntNmList = emlHistrec.get("rcpnt_nm_list") == null? "" :  (String) emlHistrec.get("rcpnt_nm_list");
		String rcpntIdList = emlHistrec.get("rcpnt_id_list") == null? "" :  (String) emlHistrec.get("rcpnt_id_list");
		String ccEmlList =   emlHistrec.get("cc_eml_list") == null? "" :    (String) emlHistrec.get("cc_eml_list");
		String ccNmList =    emlHistrec.get("cc_nm_list") == null? "" :    (String) emlHistrec.get("cc_nm_list");
		String ccIdList =    emlHistrec.get("cc_id_list") == null? "" :    (String) emlHistrec.get("cc_id_list");
		String bccEmlList =  emlHistrec.get("bcc_eml_list") == null? "" :  (String) emlHistrec.get("bcc_eml_list");
		String bccNmList =   emlHistrec.get("bcc_nm_list") == null? "" :    (String) emlHistrec.get("bcc_nm_list");
		String bccIdList =   emlHistrec.get("bcc_id_list") == null? "" :    (String) emlHistrec.get("bcc_id_list");
		String senderEml =   emlHistrec.get("sndr_eml") == null? "" :    (String) emlHistrec.get("sndr_eml");
		String senderName =   emlHistrec.get("sndr_nm") == null? "" :    (String) emlHistrec.get("sndr_nm");
		String emailTitle =   emlHistrec.get("eml_tit") == null? "" :    (String) emlHistrec.get("eml_tit");
		String attachUuid =   emlHistrec.get("athg_uuid") == null? "" :    (String) emlHistrec.get("athg_uuid");
		String emailTemplateCode =   emlHistrec.get("eml_tmpl_cd") == null? "" :    (String) emlHistrec.get("eml_tmpl_cd");
		String emailContent =   emlHistrec.get("eml_cont") == null? "" :    (String) emlHistrec.get("eml_cont");

		List<TemplateMailData.Receiver> receivers = Lists.newArrayList();
		this.addReceiverByEmailList(receivers, RecipientType.TO, rcpntEmlList, rcpntNmList, rcpntIdList);
		this.addReceiverByEmailList(receivers, RecipientType.CC, ccEmlList, ccNmList, ccIdList);
		this.addReceiverByEmailList(receivers, RecipientType.BCC, bccEmlList, bccNmList, bccIdList);
		
		TemplateMailData templateMailData = TemplateMailData.getInstance(
				TemplateMailData.Sender.getInstance(senderEml, senderName),
				receivers,
				emailTitle,
				attachUuid
		);
		this.exceptSendMailSubject(emailTemplateCode, templateMailData);
		String reSendEmlSndUuid = this.insertMailHistory(emailTemplateCode, emailContent, templateMailData);

		// 재전송 시 새로운 메일을 따기 때문에, 이전 메일은 UPDATE
		mailManagerRepository.updateEmailMultiSendingComplete(param);
		// 재전송도 좋으나, 새로이 전송하는게 맞아보임.
		return this.mailSendProcess(reSendEmlSndUuid,emailContent, templateMailData, false);
	}
	
	private void addReceiverByEmailList(List<TemplateMailData.Receiver> receivers, RecipientType recipientType, String emailList, String nameList, String idList) {
		if(!Strings.isNullOrEmpty(emailList)) {
			String[] emailArr = emailList.split(",");
			String[] nameArr = nameList.split(",");
			String[] idArr = idList.split(",");
			for(int i = 0; i < emailArr.length; i++) {
				receivers.add(TemplateMailData.Receiver.getInstance(recipientType, emailArr[i], nameArr[i], idArr[i]));
			}
		}
	}
	
	private MailResult mailSendProcess(String emlSndUuid, String contents, TemplateMailData templateMailData, boolean isTest) {
		MailResult mailResult = MailResult.getInstance();
		mailResult.setEmlSndUuid(emlSndUuid);
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("eml_snd_uuid", emlSndUuid);
		
		try {
			mailSender.send(contents, templateMailData);
			param.put("snd_yn", "Y");
			mailResult.setSuccess(true);
		} catch(Exception e) {
			param.put("snd_yn", "N");
			param.put("snd_err_msg",e.getMessage());
			mailResult.setSuccess(false);
			mailResult.setErrorMessage(e.getMessage());
		}finally {
			if(!isTest) {
				mailManagerRepository.updateEmailMultiSendingComplete(param);
			}
		}

		return mailResult;
	}
	
	protected MailStrategy findModule(String emailTemplateId) {
		MailStrategy result = null;
		for(MailStrategy mailStrategy : this.mailStrategies) {
			if(emailTemplateId.equals(mailStrategy.getEmailTemplateId())) {
				result = mailStrategy;
			}
		}
		return result;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		for(MailStrategy mailStrategy : this.mailStrategies) {
			if(Strings.isNullOrEmpty(mailStrategy.getEmailTemplateId())) {
				LOG.error("MailTemplate loading failed");
				throw new RuntimeException("MailTemplate loading failed");
			}
		}
	}
	
	private void exceptSendMailSubject(String emailTemplateCommonCode, TemplateMailData templateMailData) {
		String tempId = this.insertSendMailInfoListTempTable(emailTemplateCommonCode, templateMailData);
		templateMailData.setReceivers(this.searchFilterReceiver(tempId));
		this.deleteSendMailInfoListTempTable(tempId);
	}
	
	private String insertSendMailInfoListTempTable(String emailTemplateCommonCode,TemplateMailData templateMailData) {
		List<TemplateMailData.Receiver> receivers = templateMailData.getReceivers();
		return mailManagerRepository.insertSendMailInfoListTempTable(emailTemplateCommonCode, receivers);
	}
	
	private List<TemplateMailData.Receiver> searchFilterReceiver(String tempId) {
		return mailManagerRepository.searchFilterReceiver(tempId);
	}
	
	private void deleteSendMailInfoListTempTable(String tempId) {
		mailManagerRepository.deleteSendMailInfoListTempTable(tempId);
	}
}
