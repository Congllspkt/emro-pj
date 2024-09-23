package smartsuite.app.common.compare.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import smartsuite.app.common.compare.repository.CodeCompareRepository;
import smartsuite.app.common.shared.ResultMap;


@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class CodeCompareService {
	
	@Inject
	CodeCompareRepository codeCompareRepository;
	
	/**
	 * 테이블비교 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListCompareTable(Map<String, Object> param) {
		return codeCompareRepository.findListCompareTable(param);
	}
	
	/**
	 * 테이블비교 정보 저장
	 * @param param
	 * @return
	 */
	public ResultMap saveListCompareTable(Map<String, Object> param) {
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		// 추가
		if(insertList != null && !insertList.isEmpty()) {
			for(Map<String, Object> row : insertList) {
				if(row.get("module_uuid") == null || row.get("module_uuid") == ""){
					String moduleUuid = UUID.randomUUID().toString();
					row.put("module_uuid", moduleUuid);
				}
				this.insertCompareTable(row);
			}
		}
		// 수정
		if(updateList != null && !updateList.isEmpty()) {
			for(Map<String, Object> row : updateList) {
				if(row.get("module_uuid") == null || row.get("module_uuid") == ""){
					String moduleUuid = UUID.randomUUID().toString();
					row.put("module_uuid", moduleUuid);
				}
				this.updateCompareTable(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 테이블비교 정보 수정
	 * @param param
	 */
	public void updateCompareTable(Map<String, Object> param) {
		codeCompareRepository.updateCompareTable(param);
	}
	
	/**
	 * 테이블비교 정보 추가
	 * @param param
	 */
	public void insertCompareTable(Map<String, Object> param) {
		codeCompareRepository.insertCompareTable(param);
	}
	
	/**
	 * 테이블비교 정보 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteCompareTable(Map<String, Object> param){
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		// 삭제
		if(deleteList != null && !deleteList.isEmpty()) {
			for(Map<String, Object> row : deleteList) {
				this.deleteCompareTableInfo(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 테이블비교 정보 삭제
	 * @param param
	 */
	public void deleteCompareTableInfo(Map<String, Object> param) {
		codeCompareRepository.deleteCompareTable(param);
	}
	
	/**
	 * 그룹코드 정보 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListGrpCd(Map<String, Object> param) {
		return codeCompareRepository.findListGrpCd(param);
	}
	
	/**
	 * 그룹코드 정보 저장
	 * @param param
	 * @return
	 */
	public ResultMap saveListGrpCd(Map<String, Object> param) {
		List<Map<String, Object>> insertList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String, Object>> updateList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		// 추가
		if(insertList != null && !insertList.isEmpty()) {
			for(Map<String, Object> row : insertList) {
				if(row.get("module_uuid") == null || row.get("module_uuid") == ""){
					String moduleUuid = UUID.randomUUID().toString();
					row.put("module_uuid", moduleUuid);
				}
				this.insertGrpCd(row);
			}
		}
		// 수정
		if(updateList != null && !updateList.isEmpty()) {
			for(Map<String, Object> row : updateList) {
				if(row.get("module_uuid") == null || row.get("module_uuid") == ""){
					String moduleUuid = UUID.randomUUID().toString();
					row.put("module_uuid", moduleUuid);
				}
				this.updateGrpCd(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 그룹코드 정보 수정
	 * @param param
	 */
	public void updateGrpCd(Map<String, Object> param) {
		codeCompareRepository.updateGrpCd(param);
	}
	
	/**
	 * 그룹코드 정보 추가
	 * @param param
	 */
	public void insertGrpCd(Map<String, Object> param) {
		codeCompareRepository.insertGrpCd(param);
	}
	
	/**
	 * 그룹코드 정보 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteListGrpCd(Map<String, Object> param){
		List<Map<String, Object>> deleteList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		if(deleteList != null && !deleteList.isEmpty()) {
			for(Map<String, Object> row : deleteList) {
				this.deleteListGrpCdInfo(row);
			}
		}
		
		return ResultMap.SUCCESS();
	}
	
	/**
	 * 그룹코드 정보 삭제
	 * @param param
	 */
	public void deleteListGrpCdInfo(Map<String, Object> param) {
		codeCompareRepository.deleteListGrpCdInfo(param);
	}
}
