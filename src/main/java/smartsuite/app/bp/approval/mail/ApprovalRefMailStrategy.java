package smartsuite.app.bp.approval.mail;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.bp.approval.repository.ApprovalMailRepository;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.strategy.MailStrategy;

import javax.inject.Inject;
import javax.mail.Message;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 결재 상신 시점에 참조자에게 메일 발송
 */
@Service
public class ApprovalRefMailStrategy implements MailStrategy {
	
	@Inject
	ApprovalMailRepository approvalMailRepository;
	
	@Override
	public String getEmailTemplateId() {
		return "APPROVAL_REF_MAIL";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		
		Map param = Maps.newHashMap();
		param.put("apvl_uuid", appId);
		param.put("rdg_typ_ccd", "REF");
		
		// 신규 참조자 추가가 존재하는 경우 해당 인원에만 메일 발송을 보내기 위함
		if(data != null && data.get("newRefUsrIds") != null) {
			param.put("newRefUsrIds", data.get("newRefUsrIds"));
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		
		Map approvalInfo = approvalMailRepository.findApprovalInfo(param);
		
		approvalInfo.put("apvl_rptg_dttm", simpleDateFormat.format(approvalInfo.get("apvl_rptg_dttm")));
		templateMailData.setParameter(approvalInfo);
		
		List<TemplateMailData.Receiver> receivers = approvalMailRepository.findListApprovalRdgLine(param);
		templateMailData.setReceivers(receivers);
		
		return templateMailData;
	}
}
