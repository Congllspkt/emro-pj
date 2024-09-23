package smartsuite.app.bp.approval.mail;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.bp.approval.repository.ApprovalMailRepository;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 합의 시점에 합의자에게 메일 발송
 */
@Service
public class ApprovalAgreeMailStrategy implements MailStrategy {
	
	@Inject
	ApprovalMailRepository approvalMailRepository;
	
	@Override
	public String getEmailTemplateId() {
		return "APPROVAL_AGREE_MAIL";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		
		Map param = Maps.newHashMap();
		param.put("apvl_uuid", appId);
		param.put("next_apvlln_sort", data.get("next_apvlln_sort"));
		param.put("apvr_typ_ccd", "AG");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		
		Map approvalInfo = approvalMailRepository.findApprovalInfo(param);
		
		approvalInfo.put("apvl_rptg_dttm", simpleDateFormat.format(approvalInfo.get("apvl_rptg_dttm")));
		templateMailData.setParameter(approvalInfo);
		
		List<TemplateMailData.Receiver> receivers = approvalMailRepository.findListApprovalNextApprover(param);
		templateMailData.setReceivers(receivers);
		
		return templateMailData;
	}
}
