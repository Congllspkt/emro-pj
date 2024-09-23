package smartsuite.mybatis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ReflectionUtils;

public class ConfigurationModifyObserverTimerTask extends TimerTask implements ConfigurationModifyObserver{
	
	static final Logger LOG = LoggerFactory.getLogger(ConfigurationModifyObserverTimerTask.class);
	
	private final Map<Resource, Long> lastModifiedTimes = new HashMap<Resource, Long>();
	
	private Configuration configuration;
	
	private static final int INTERVAL = 1000;
	
	private final Timer timer = new Timer(true);
	
	private final Set<ConfigurationModifyListener> listeners = new HashSet<ConfigurationModifyListener>();
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void start() {
		timer.schedule(this, INTERVAL, INTERVAL);
	}
	
	@Override
	public void stop() {
		timer.cancel();
	}
	
	@Override
	public void addModifyListener(ConfigurationModifyListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeModifyListener(ConfigurationModifyListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void run() {
		try {
			if(configuration != null && isModified()){
				for(ConfigurationModifyListener listener : listeners){
					listener.onConfigutionModify();
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean isModified() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException{
		Field loadedResourcesField = Configuration.class
				.getDeclaredField("loadedResources");
		ReflectionUtils.makeAccessible(loadedResourcesField);
		boolean modified = false;
		Set<String> loadedResources = (Set<String>) loadedResourcesField
				.get(configuration);
		List<String> modifiedResourceDescriptions = new ArrayList<String>();
		for (String loadedResource : loadedResources) {
			Resource resource = null;
			if (loadedResource.startsWith("file [") && loadedResource.endsWith("]")) {
				resource = new FileSystemResource(loadedResource.substring(6,loadedResource.length() -1));
			}else if (loadedResource.startsWith("URL [") && loadedResource.endsWith("]")) {
				resource = new UrlResource(loadedResource.substring(5,loadedResource.length() -1));
			}else if (loadedResource.startsWith("class path resource [") && loadedResource.endsWith("]")) {
				resource = new ClassPathResource(loadedResource.substring(21,loadedResource.length() -1));
			}else if (loadedResource.endsWith(".xml")) {
				resource = new ClassPathResource(loadedResource);
			} 
			if(resource != null && isModifiedResource(resource)){
				modifiedResourceDescriptions.add(resource.getDescription());
				modified = true;
			}
		}
		if(!modifiedResourceDescriptions.isEmpty() && LOG.isInfoEnabled()){
			for(String modifiedResourceDescription : modifiedResourceDescriptions){
				LOG.info("modified resource : " + modifiedResourceDescription);
			}
		}
		return modified;
	}
	
	private boolean isModifiedResource(Resource resource) throws IOException {
		long modified = resource.lastModified();
		if (lastModifiedTimes.containsKey(resource)) {
			long lastModified = lastModifiedTimes.get(resource).longValue();

			if (lastModified != modified) {
				lastModifiedTimes.put(resource, modified);
				return true;
			}
		} else {
			lastModifiedTimes.put(resource, modified);
		}
		return false;
	}

}
