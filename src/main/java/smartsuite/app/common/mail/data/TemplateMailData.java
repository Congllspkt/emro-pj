package smartsuite.app.common.mail.data;

import com.google.common.collect.Lists;

import javax.mail.Message.RecipientType;
import java.util.List;
import java.util.Map;

/**
 * 템플릿 메일 발송을 위한 기본 구조
 * 송신자 Nullable
 * 수신자 Not Null (1..n)
 * 파라미터 Nullable : 메일 템플릿의 변환이 필요 없을 경우
 * 제목 Nullable : null인 경우 메일 템플릿의 제목 사용
 * 첨부파일 ID Nullable
 */
public class TemplateMailData {
	
	public TemplateMailData() {}
	
	public TemplateMailData(Sender sender, List<Receiver> receivers, String title, String athgUuid) {
		this.sender = sender;
		this.receivers = receivers;
		this.title = title;
		this.athgUuid = athgUuid;
	}
	
	public TemplateMailData(Sender sender, List<Receiver> receivers, String title, String athgUuid, Map<String, Object> parameter) {
		this.sender = sender;
		this.receivers = receivers;
		this.title = title;
		this.athgUuid = athgUuid;
		this.parameter = parameter;
	}
	
	public static TemplateMailData getInstance() {
		return new TemplateMailData();
	}
	
	public static TemplateMailData getInstance(Sender sender, List<Receiver> receivers, String title, String athgUuid) {
		return new TemplateMailData(sender, receivers, title, athgUuid);
	}
	
	public static TemplateMailData getInstance(Sender sender, List<Receiver> receivers, String title, String athgUuid, Map<String, Object> parameter) {
		return new TemplateMailData(sender, receivers, title, athgUuid, parameter);
	}
	
	Sender sender;
	
	List<Receiver> receivers;
	
	Map<String, Object> parameter;
	
	String title;
	
	String athgUuid;
	
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	
	public Sender getSender() {
		return this.sender;
	}
	
	public void setReceivers(List<Receiver> receivers) {
		this.receivers = receivers;
	}
	
	public void addReceiver(Receiver receiver) {
		if(receivers == null) {
			receivers = Lists.newArrayList();
		}
		receivers.add(receiver);
	}
	
	public List<Receiver> getReceivers() {
		return this.receivers;
	}
	
	public List<Receiver> getReceiversByRecipientType(RecipientType recipientType) {
		List<Receiver> receivers = this.getReceivers();
		List<Receiver> result = Lists.newArrayList();
		
		if(receivers == null) {
			return result;
		}
		for(Receiver receiver : receivers) {
			if(recipientType.equals(receiver.getRecipientType())) {
				result.add(receiver);
			}
		}
		return result;
	}
	
	public List<Receiver> getReceiversByRecipientTypeToSend(RecipientType recipientType) {
		List<Receiver> receivers = this.getReceivers();
		List<Receiver> result = Lists.newArrayList();
		
		if(receivers == null) {
			return result;
		}
		for(Receiver receiver : receivers) {
			if(recipientType.equals(receiver.getRecipientType()) && "N".equals(receiver.getXceptYn())) {
				result.add(receiver);
			}
		}
		return result;
	}
	
	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}
	
	public Map<String, Object> getParameter() {
		return this.parameter;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setAttachmentGroupId(String athgUuid) {
		this.athgUuid = athgUuid;
	}
	
	public String getAttachmentGroupId() {
		return this.athgUuid;
	}
	
	/**
	 * 템플릿 메일 수신자 기본 구조
	 */
	public static class Receiver {
		
		public Receiver() {}
		
		public Receiver(RecipientType recipientType, String address, String name, String xceptYn, String id) {
			this.recipientType = recipientType;
			this.address = address;
			this.name = name;
			this.xceptYn = xceptYn;
			this.id = id;
		}
		
		public static Receiver getInstance() {
			return new Receiver();
		}
		
		public static Receiver getInstance(RecipientType recipientType, String address, String name) {
			return getInstance(recipientType, address, name, "");
		}
		
		public static Receiver getInstance(RecipientType recipientType, String address, String name, String id) {
			return getInstance(recipientType, address, name, "N", id);
		}
		public static Receiver getInstance(RecipientType recipientType, String address, String name,String xceptYn, String id) {
			return new Receiver(recipientType, address, name, xceptYn, id);
		}
		
		RecipientType recipientType = RecipientType.TO;
		
		String address;
		
		String name;
		
		String xceptYn = "N";
		
		String id;
		
		
		public void setRecipientType(String recipientType) {
			if("To".equals(recipientType)) {
				this.recipientType = RecipientType.TO;
			} else if("Cc".equals(recipientType)) {
				this.recipientType = RecipientType.CC;
			} else if("Bcc".equals(recipientType)) {
				this.recipientType = RecipientType.BCC;
			} else {
				this.recipientType = RecipientType.TO;
			}
		}
		
		public RecipientType getRecipientType() {
			return this.recipientType;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
		
		public String getAddress() {
			return this.address;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getXceptYn() {
			return xceptYn;
		}
		
		public void setXceptYn(String xceptYn) {
			this.xceptYn = xceptYn;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return "Receiver Info is {recipientType: " + this.getRecipientType()
			       +" , user mail: " + this.getAddress()
			       +" , user name: " + this.getName()
			       +" , user id: " + this.getId()
				   +"}";
					
		}
	}
	
	/**
	 * 템플릿 메일 송신자 기본 구조
	 */
	public static class Sender {
		
		public Sender() {}
		
		public Sender(String address, String name) {
			this.address = address;
			this.name = name;
		}
		
		public static Sender getInstance() {
			return new Sender();
		}
		
		public static Sender getInstance(String address, String name) {
			return new Sender(address, name);
		}
		
		String address;
		
		String name;
		
		public void setAddress(String address) {
			this.address = address;
		}
		
		public String getAddress() {
			return this.address;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
}
