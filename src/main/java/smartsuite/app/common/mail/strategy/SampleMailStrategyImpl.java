package smartsuite.app.common.mail.strategy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import smartsuite.app.common.mail.data.TemplateMailData;
import smartsuite.app.common.mail.data.TemplateMailData.Receiver;
import smartsuite.app.common.mail.data.TemplateMailData.Sender;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import java.util.List;
import java.util.Map;

@Service
public class SampleMailStrategyImpl implements MailStrategy {
	
	/**
	 * 관리자 > 템플릿 > 메일 - 이메일 템플릿 아이디 반환
	 * @return
	 */
	@Override
	public String getEmailTemplateId() {
		return "SAMPLE_EMAIL_TEMPLATE_ID";
	}
	
	/**
	 * <b>Return Required:</b><br>
	 * TemplateMailData.receivers - 수신자 정보<br>
	 *
	 * <b>Return Option:</b><br>
	 * TemplateMailData.sender - 수신자 정보
	 * TemplateMailData.parameter - 메일 본문 템플릿에 매핑할 파라미터
	 * TemplateMailData.athgUuid - 첨부파일 그룹 ID
	 *
	 * @param appId - 업무 Key
	 * @param data - 이메일 발송을 위해 업무단에서 넘겨받은 map data
	 * @return TemplateMailData
	 */
	@Override
	public TemplateMailData getTemplateMailData(String appId, Map<String, Object> data) throws Exception {
		
		// 수신자 설정
		// RecipientType.TO / CC / BCC 가능
		List<Receiver> receivers = Lists.newArrayList();
		Receiver receiver = Receiver.getInstance(RecipientType.TO, "receiver@emro.co.kr", "테스트 사용자");
		receivers.add(receiver);
		
		// 송신자 설정
		Sender sender = Sender.getInstance("sender@emro.co.kr", "테스트 송신자");
		
		// 제목 설정
		// 없을 경우 메일 템플릿 명 사용
		// 메일 템플릿 명 사용하는 경우 freemarker 변수 치환 가능
		String title = null;
		
		// 첨부파일 설정
		String athgUuid = null;
		
		// 본문 contents 에서 사용할 freemarker 변수 치환 파라미터
		Map<String, Object> parameter = Maps.newHashMap();
		
		TemplateMailData templateMailData = TemplateMailData.getInstance(
				sender,
				receivers,
				title,
				athgUuid,
				parameter);
		
		return templateMailData;
	}
}
