package smartsuite.app.common.mail.strategy;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.data.TemplateMailData;

import javax.mail.Message;
import java.util.Map;

@Service
public class EmailExcelStrategy implements MailStrategy {
	
	@Override
	public String getEmailTemplateId() {
		return "EMAIL_EXCEL";
	}
	
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		TemplateMailData templateMailData = TemplateMailData.getInstance();
		// 메일 제목 설정
		templateMailData.setTitle((String) data.get("eml_tit"));
		
		// 메일 수신자 설정
		TemplateMailData.Receiver receiver = TemplateMailData.Receiver.getInstance(
				Message.RecipientType.TO,
				(String) data.get("rcpnt_eml"),
				(String) data.get("to_nm"),
				(String) data.getOrDefault("to_id","")
		);
		templateMailData.addReceiver(receiver);
		
		// 메일 송신자 설정
		if(!Strings.isNullOrEmpty((String) data.get("sndr_nm"))) {
			TemplateMailData.Sender sender = TemplateMailData.Sender.getInstance(
					(String) data.get("sndr_eml"),
					(String) data.get("sndr_nm")
			);
			templateMailData.setSender(sender);
		}
		// 첨부파일 설정
		templateMailData.setAttachmentGroupId((String) data.get("athg_uuid"));
		templateMailData.setParameter(data);
		
		return templateMailData;
	}
}
