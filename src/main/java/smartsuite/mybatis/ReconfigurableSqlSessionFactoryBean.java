package smartsuite.mybatis;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReconfigurableSqlSessionFactoryBean extends SqlSessionFactoryBean
		implements ConfigurationModifyListener {
	
	static final Logger LOG = LoggerFactory.getLogger(ReconfigurableSqlSessionFactoryBean.class);

	private final ReentrantLock lock = new ReentrantLock();


	private final ProxySqlSessionFactory proxySqlSessionFactory = new ProxySqlSessionFactory();

	private final ConfigurationModifyObserver configurationModifyObserver = new ConfigurationModifyObserverTimerTask();

	
	public ReconfigurableSqlSessionFactoryBean() {
		super();
		configurationModifyObserver.addModifyListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public SqlSessionFactory getObject() throws Exception {
		proxySqlSessionFactory.setDelegate(super.getObject());
		return proxySqlSessionFactory;
	}

	public void destroy() throws Exception {
		configurationModifyObserver.stop();
	}

	public void onConfigutionModify() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Refreshing SqlSessionFactory.");
		}
		lock.lock();
		try {
			SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
			proxySqlSessionFactory.setDelegate(sqlSessionFactory);
			configurationModifyObserver.setConfiguration(sqlSessionFactory.getConfiguration());
		}catch(Exception e){
			if (LOG.isErrorEnabled()) {
				LOG.error("Error Occured while Refreshing SqlSessionFactory.", e);
			}
		} finally {
			lock.unlock();
		}
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		SqlSessionFactory sqlSessionFactory = super.getObject();
		proxySqlSessionFactory.setDelegate(sqlSessionFactory);
		configurationModifyObserver.setConfiguration(sqlSessionFactory.getConfiguration());
		configurationModifyObserver.start();
	}

}

interface ExcludeSqlSessionFactoryBean {

	SqlSessionFactory getObject() throws Exception;

	void afterPropertiesSet() throws Exception;

}