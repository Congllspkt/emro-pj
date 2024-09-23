package smartsuite.app.common.message;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 *  Message Context 설정 시 불필요한 arg 생성 및 message 중복 코드가 많아 생성
 */
@Service
public class MessageUtil {

	@Autowired
	MessageSource messageSource;

	public String getCodeMessage(String code, Object replaceMessage, String defaultMessage){
		Object[] args = new Object[]{replaceMessage};
		return this.replaceMessage(code,args,defaultMessage,LocaleContextHolder.getLocale());
	}

	public String getCodeMessage(String code, List<Object> replaceMessageList, String defaultMessage){
		Object[] args = new Object[]{replaceMessageList.size()};
		for(int a=0;a<replaceMessageList.size(); a++){
			args[a] = replaceMessageList.get(a);
		}
		return this.replaceMessage(code,args,defaultMessage,LocaleContextHolder.getLocale());
	}

	public String getCodeMessage(String code, Object replaceMessage, String defaultMessage, Locale locale){
		Object[] args = new Object[]{replaceMessage};
		return this.replaceMessage(code,args,defaultMessage,locale);
	}

	public String getCodeMessage(String code, List<Object> replaceMessageList, String defaultMessage, Locale locale){
		Object[] args = new Object[replaceMessageList.size()];
		for(int a=0;a<replaceMessageList.size(); a++){
			args[a] = replaceMessageList.get(a);
		}
		return this.replaceMessage(code,args,defaultMessage,locale);
	}

	public String getCodeMessage(MessageBean messageBean){
		if(messageBean.getReplaceMessageList().size() > 0){
			return this.getCodeMessage(messageBean.getCodeName(),messageBean.getReplaceMessageList(),messageBean.getDefaultMessage(),messageBean.getLocale());
		}else{
			return this.getCodeMessage(messageBean.getCodeName(),messageBean.getReplaceMessage(),messageBean.getDefaultMessage(),messageBean.getLocale());
		}
	}

	private String replaceMessage(String code , Object[] args , String defaultMessage , Locale locale){
		String getMessage;
		if(defaultMessage != null) {
			getMessage = messageSource.getMessage(code, args, defaultMessage, locale);
		} else {
			getMessage = messageSource.getMessage(code, args, locale);
		}
		if(getMessage.length()>200){
			getMessage = getMessage.substring(0, 200);
		}
		return getMessage;
	}


	public static class MessageBean{
		public String codeName;
		public Object replaceMessage;
		public List<Object> replaceMessageList = Lists.newArrayList();
		public String defaultMessage;
		public Locale locale = LocaleContextHolder.getLocale();

		public String getCodeName() {
			return codeName;
		}

		public void setCodeName(String codeName) {
			this.codeName = codeName;
		}

		public Object getReplaceMessage() {
			return replaceMessage;
		}

		public void setReplaceMessage(Object replaceMessage) {
			this.replaceMessage = replaceMessage;
		}

		public List<Object> getReplaceMessageList() {
			return replaceMessageList;
		}

		public void setReplaceMessageList(List<Object> replaceMessageList) {
			this.replaceMessageList = replaceMessageList;
		}

		public String getDefaultMessage() {
			return defaultMessage;
		}

		public void setDefaultMessage(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}

		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}
	}

}
