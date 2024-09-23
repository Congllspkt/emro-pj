package smartsuite.app.common.mail.strategy;

import smartsuite.app.common.mail.data.TemplateMailData;

import java.util.List;
import java.util.Map;

public interface MailStrategy {
	
	/**
	 * 관리자 > 템플릿 > 메일
	 *
	 * @return 이메일 템플릿 아이디
	 */
	String getEmailTemplateId();
	
	TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception;
}
