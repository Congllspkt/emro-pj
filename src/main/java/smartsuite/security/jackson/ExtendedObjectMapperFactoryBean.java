package smartsuite.security.jackson;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.module.SimpleModule;

import smartsuite.spring.jackson.ObjectMapperFactoryBean;

public class ExtendedObjectMapperFactoryBean extends ObjectMapperFactoryBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		SimpleModule module = new SimpleModule();
		module.addSerializer(BigDecimal.class, new BigDecimalSerializer(null));
		super.getObject().registerModule(module);
	}

}
