package smartsuite.app.common.stateful.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.app.common.stateful.repository.StatefulRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatefulService {
	
	static final Logger LOG = LoggerFactory.getLogger(StatefulService.class);
	
	static final private boolean SERVER_STARTUP = false; 


	@Inject
	StatefulRepository statefulRepository;

	/**
	 * 현재 cache bust 값을 가져온다.
	 * @return
	 */
	public String findCacheBustValue(){
		return statefulRepository.findCacheBustValue();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateCacheBust(){
		String cacheBust = findCacheBustValue();
		if(cacheBust == null){
			statefulRepository.insertCacheBust();
		}else{
			statefulRepository.updateCacheBust();
		}
	}

	@PostConstruct
	public void init() {
		if(SERVER_STARTUP){
			updateCacheBust();
		}
	}
	
	public Map<String, Map<String,Object>> findUserPersonalizationClientDataProcess() {
		List<Map<String,Object>> personalizationClientData = this.findUserPersonalizationClientData("");
		Map<String,Map<String,Object>> clientData = Maps.newHashMap();
		for(Map<String,Object> data : personalizationClientData) {
			clientData.put((String)data.get("usr_pzn_dat_uuid"), data);
		}
		return clientData;
	}

	private List<Map<String, Object>> findUserPersonalizationClientData(String userPersonalizationDataUuid) {
		return statefulRepository.findUserPersonalizationClientData(userPersonalizationDataUuid);
	}

	public ResultMap saveUserPersonalizationClientData(List<Map<String,Object>> states) {
		List<Map<String,Object>> createClientData = Lists.newArrayList();
		List<Map<String,Object>> updateClientData = Lists.newArrayList();
		List<Map<String,Object>> deleteClientData = Lists.newArrayList();
		
		for(Map<String,Object> state : states) {
			String id = (String)state.get("usr_pzn_dat_uuid");
			if(state.containsKey("erased")) {
				deleteClientData.add(state);
			}
			else {
				List<Map<String,Object>> savedState = this.findUserPersonalizationClientData(id);
				//modified, created 상태중 저장된 상태값이 없으면 무조건 생성
				if(savedState.size() == 0) {
					createClientData.add(state);
				}
				else {
					updateClientData.add(state);
				}
			}
		}
		for(Map<String,Object> state : createClientData) {
			this.insertUserPersonalizationClientData(state);
		}
		for(Map<String,Object> state : updateClientData) {
			this.updateUserPersonalizationClientData(state);
		}
		for(Map<String,Object> state : deleteClientData) {
			this.deleteUserPersonalizationClientData(state);
		}
		return ResultMap.SUCCESS();
	}

	private void deleteUserPersonalizationClientData(Map<String, Object> state) {
		statefulRepository.deleteUserPersonalizationClientData(state);
	}

	private void updateUserPersonalizationClientData(Map<String, Object> state) {
		statefulRepository.updateUserPersonalizationClientData(state);
	}

	private void insertUserPersonalizationClientData(Map<String, Object> state) {
		statefulRepository.insertUserPersonalizationClientData(state);
	}

	public ResultMap allDeleteUserPersonalizationClientData() {
		statefulRepository.allDeleteUserPersonalizationClientData();
		return ResultMap.SUCCESS();
	}
}
