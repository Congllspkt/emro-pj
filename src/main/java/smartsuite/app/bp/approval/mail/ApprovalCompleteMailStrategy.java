package smartsuite.app.bp.approval.mail;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.bp.approval.repository.ApprovalMailRepository;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * 결재 완료 시점에 기안자에게 메일 발송
 */
@Service
public class ApprovalCompleteMailStrategy implements MailStrategy {
	
	@Inject
	ApprovalMailRepository approvalMailRepository;
	
	@Override
	public String getEmailTemplateId() {
		return "APPROVAL_COMPLETE_MAIL";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		
		Map param = Maps.newHashMap();
		param.put("apvl_uuid", appId);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		
		Map approvalInfo = approvalMailRepository.findApprovalInfo(param);
		approvalInfo.put("apvl_rptg_dttm", simpleDateFormat.format(approvalInfo.get("apvl_rptg_dttm")));
		approvalInfo.put("fnl_apvl_dttm", simpleDateFormat.format(approvalInfo.get("fnl_apvl_dttm")));
		templateMailData.setParameter(approvalInfo);
		templateMailData.addReceiver(approvalMailRepository.findApprovalDrafterInfo(param));
		
		return templateMailData;
	}
}
