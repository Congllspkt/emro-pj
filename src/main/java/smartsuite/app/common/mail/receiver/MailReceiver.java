package smartsuite.app.common.mail.receiver;

import smartsuite.app.common.mail.data.MailInfo;

import java.util.Date;
import java.util.List;
import java.util.Properties;


public interface MailReceiver {
	
	public void initialize(String host, String user, String password, boolean ssl, Properties properties);
	
	public List<MailInfo> receiveAllEmail() throws Exception;
	
	public List<MailInfo> receiveEmail(Date date) throws Exception;
	
	public String getTempfilestoragepath();

	public void setTempfilestoragepath(String tempfilestoragepath);
	
	public String getHost();

	public void setHost(String host);

	public String getPort();

	public void setPort(String port);

	public String getUser();

	public void setUser(String user);

	public String getPassword();

	public void setPassword(String password);

	public Properties getProperties();

	public void setProperties(Properties properties);

	public boolean isSsl();

	public void setSsl(boolean ssl);
	
}
