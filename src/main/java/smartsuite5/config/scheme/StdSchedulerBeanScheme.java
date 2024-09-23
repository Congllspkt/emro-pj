package smartsuite5.config.scheme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import smartsuite5.spring.beans.scheme.scheduler.DefaultSchedulerBeanScheme;

import javax.sql.DataSource;

public class StdSchedulerBeanScheme extends DefaultSchedulerBeanScheme {

	@Autowired
    @Qualifier("dataSource") // Spring 패키지의 Annotation 사용
    DataSource dataSource;

	@Override
    public SchedulerFactoryBean getSchedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactory = super.getSchedulerFactoryBean();
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setQuartzProperties(null);
        return schedulerFactory;
    }
}
