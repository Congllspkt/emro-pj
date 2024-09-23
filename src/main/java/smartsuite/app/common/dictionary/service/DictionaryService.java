package smartsuite.app.common.dictionary.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import smartsuite.app.common.dictionary.repository.DictionaryRepository;
import smartsuite.app.common.shared.ResultMap;


@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class DictionaryService {
	
	@Inject
	DictionaryRepository dictionaryRepository;
	
	/**
	 * 용어집 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDictionary(Map<String, Object> param) {
		return dictionaryRepository.findListDictionary(param);
	}
	
	/**
	 * 용어집 정보 저장
	 * @param param
	 * @return
	 */
	public ResultMap saveListDictionary(Map<String, Object> param) {
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		// 추가
		if(insertList != null && !insertList.isEmpty()) {
			for(Map<String, Object> row : insertList) {
				this.insertDictionary(row);
			}
		}
		// 수정
		if(updateList != null && !updateList.isEmpty()) {
			for(Map<String, Object> row : updateList) {
				this.updateDictionary(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 용어집 정보 수정
	 * @param param
	 */
	public void updateDictionary(Map<String, Object> param) {
		dictionaryRepository.updateDictionary(param);
	}
	
	/**
	 * 용어집 정보 추가
	 * @param param
	 */
	public void insertDictionary(Map<String, Object> param) {
		dictionaryRepository.insertDictionary(param);
	}
	
	/**
	 * 용어집 정보 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteDictionary(Map<String, Object> param){
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		// 삭제
		if(deleteList != null && !deleteList.isEmpty()) {
			for(Map<String, Object> row : deleteList) {
				this.deleteDictionaryInfo(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 용어집 정보 삭제
	 * @param param
	 */
	public void deleteDictionaryInfo(Map<String, Object> param) {
		dictionaryRepository.deleteDictionary(param);
	}
}
