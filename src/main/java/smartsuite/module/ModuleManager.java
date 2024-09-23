package smartsuite.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ModuleManager {

	static final Logger LOG = LoggerFactory.getLogger(ModuleManager.class);
	
	private static Resource[] localProperties;
	
	public static void setLocalProperties(Resource[] localProperties) {
		localProperties = localProperties;
		initialize(localProperties);
	}

	private static List<String> modules = Lists.newArrayList();
	
	private static List<String> ccModules = Lists.newArrayList();
	
	private static Map<String, Map> modulePropertiesMap = Maps.newHashMap();

	public static List<String> getModules() {
		return modules;
	}
	
	public static List<String> getCcModules() {
		return ccModules;
	}
	
	public static Map<String, Map> getModulePropertiesMap() {
		return modulePropertiesMap;
	}
	
	public static Map getModuleProperties(String module) {
		return modulePropertiesMap.get(module.toUpperCase());
	}
	
	public static String getModulePropertyValues(String module, String prop, String defaultValue) {
		Map props = modulePropertiesMap.get(module.toUpperCase());
		if(props == null) {
			return defaultValue;
		}
		return (String) props.get(prop);
	}

	public static boolean exist(String module) {
		return modules.contains(module.toUpperCase());
	}

	public static void initialize(Resource[] localProperties) {
		Properties props = null;
		Resource[] rs = localProperties;
		for(Resource r: rs) {
			try {
				props = PropertiesLoaderUtils.loadProperties(r);
				String module = props.getProperty("module");
				
				modules.add(module);
				LOG.info((String) props.get("module"));
				
				String customComponent = props.getProperty("custom.component", "false");
				if("true".equals(customComponent)) {
					ccModules.add(module);
				}
				
				Map<String, Object> properties = Maps.newHashMap();
				for(String key : props.stringPropertyNames()) {
					if("module".equals(key) || "custom.component".equals(key)) {
						continue;
					}
					if("true".equals(props.getProperty(key)) || "false".equals(props.getProperty(key))) {
						properties.put(key, Boolean.parseBoolean(props.getProperty(key)));
					} else {
						properties.put(key, props.getProperty(key));
					}
				}
				if(properties.size() > 0) {
					modulePropertiesMap.put(module, properties);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}
}
