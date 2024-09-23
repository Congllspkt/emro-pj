package smartsuite.mybatis;

import org.apache.ibatis.session.Configuration;

public interface ConfigurationModifyObserver {
	
	void setConfiguration(Configuration configuration);

	void start();
	
	void stop();
	
	void addModifyListener(ConfigurationModifyListener listener);
	
	void removeModifyListener(ConfigurationModifyListener listener);
}
