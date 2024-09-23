package smartsuite.app.bp.admin.job.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.job.repository.JobRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;

/**
 * 직무관리를 처리하는 서비스 Class입니다.
 *
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class JobService {

	@Inject
	JobRepository jobRepository;


	/**
	 * 구매 그룹 코드 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategory(Map<String, Object> param) {
		return jobRepository.findListPurchaseGroupCategory(param);
	}

	/**
	 * 구매 그룹 코드 추가 및 수정
	 * @param param
	 * @return
	 */
	public ResultMap saveListPurchaseGroupCategory(Map<String, Object> param) {
		List<Map<String, Object>> insertPurchaseGroupCategoryList = (List<Map<String, Object>>)param.getOrDefault("insertPurchaseGroupCategoryList", Lists.newArrayList());
		List<Map<String, Object>> updatePurchaseGroupCategoryList = (List<Map<String, Object>>)param.getOrDefault("updatePurchaseGroupCategoryList", Lists.newArrayList());

		//구매 그룹 코드 리스트 추가
		this.insertListPurchaseGroupCategory(insertPurchaseGroupCategoryList);

		//구매 그룹 코드 리스트 수정
		this.updateListPurchaseGroupCategory(updatePurchaseGroupCategoryList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 직무 리스트 수정
	 * @param updateJobList
	 */
	public void updateListPurchaseGroupCategory(List<Map<String, Object>> updateJobList) {
		for (Map<String, Object> updateJobInfo : updateJobList) {
			this.updateJob(updateJobInfo);
		}
	}

	/**
	 * 직무 수정
	 * @param updateJobInfo
	 */
	public void updateJob(Map<String, Object> updateJobInfo) {
		jobRepository.updateJob(updateJobInfo);
	}

	/**
	 * 직무 리스트 추가
	 * @param insertPurchaseGroupCategoryList
	 */
	public void insertListPurchaseGroupCategory(List<Map<String, Object>> insertPurchaseGroupCategoryList) {
		for (Map<String, Object> insertPurchaseGroupCategory : insertPurchaseGroupCategoryList) {
			this.insertPurchaseGroupCategory(insertPurchaseGroupCategory);
		}
	}

	/**
	 * 직무 추가
	 * @param insertPurchaseGroupCategory
	 */
	public void insertPurchaseGroupCategory(Map<String, Object> insertPurchaseGroupCategory) {
		jobRepository.insertPurchaseGroupCategory(insertPurchaseGroupCategory);
	}

	/**
	 * 직무목록을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 18
	 * @Method Name : deleteListJob
	 */
	public ResultMap deleteListJobAndPurchaseGroupCategory(Map<String, Object> param){
		List<Map<String, Object>> deleteJobList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		// DELETE
		for(Map deleteJobInfo : deleteJobList){
			this.deleteJobAndPurchaseGroupCategory(deleteJobInfo);
		}

		return ResultMap.SUCCESS();
	}


	/**
	 * 직무 담당자 및 구매 그룹 코드 삭제
	 * @param deleteJobInfo
	 */
	public void deleteJobAndPurchaseGroupCategory(Map deleteJobInfo) {

		// 삭제전 체크
		// 1. 사용자 존재여부 체크
		List<Map<String, Object>> jobUserList = this.findListPurchaseGroupCategoryJobUser(deleteJobInfo);

		if(jobUserList.size() > 0){
			throw new CommonException("직무에 등록된 사용자가 존재합니다.\n등록된 사용자를 먼저 삭제하여 주십시오.");
		}

		// 2. 품목분류4 데이터 존재여부 체크 ( 구매그룹 )
		List<Map<String, Object>> purchaseGroupCategoryList = this.findListPurchaseGroupCategoryAndItemMapping(deleteJobInfo);

		if(purchaseGroupCategoryList.size() > 0){
			throw new CommonException("직무에 등록된 세분류가 존재합니다.\\n등록된 세분류를 먼저 삭제하여 주십시오.");
		}

		//구매 그룹 코드 삭제
		this.deletePurchaseGroupCode(deleteJobInfo);
	}

	/**
	 * 구매 그룹 코드 삭제
	 * @param deleteJobInfo
	 */
	public void deletePurchaseGroupCode(Map deleteJobInfo) {
		jobRepository.deletePurchaseGroupCode(deleteJobInfo);
	}


	/**
	 * 직무담당자를 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 2. 19
	 * @Method Name : findListPurchaseGroupCategoryJobUser
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryJobUser(Map<String, Object> param) {
		return jobRepository.findListPurchaseGroupCategoryJobUser(param);
	}


	/**
	 * 품목 분류 구매 그룹 맵핑 리스트 조회 ( 세 분류 리스트 조회 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryAndItemMapping(Map<String, Object> param) {
		return jobRepository.findListPurchaseGroupCategoryAndItemMapping(param);
	}
	
	/**
	 * 직무담당자를 저장한다.
	 *
	 * @Method Name : savePurchaseGroupCategoryJobUser
	 */
	public ResultMap savePurchaseGroupCategoryJobUser(Map<String, Object> param) {
		List<Map<String, Object>> insertPurchaseGroupCategoryJobUserList = (List<Map<String, Object>>)param.getOrDefault("insertPurchaseGroupCategoryJobUserList",Lists.newArrayList());
		List<Map<String, Object>> updatePurchaseGroupCategoryJobUserList = (List<Map<String, Object>>)param.getOrDefault("updatePurchaseGroupCategoryJobUserList",Lists.newArrayList());

		// 직무 담당자 리스트 추가
		this.insertListPurchaseGroupCategoryJobUser(insertPurchaseGroupCategoryJobUserList);

		// 직무 담당자 리스트 수정
		this.updateListPurchaseGroupCategoryJobUser(updatePurchaseGroupCategoryJobUserList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 직무 담당자 리스트 수정
	 * @param updatePurchaseGroupCategoryJobUserList
	 */
	public void updateListPurchaseGroupCategoryJobUser(List<Map<String, Object>> updatePurchaseGroupCategoryJobUserList) {
		for (Map<String, Object> updatePurchaseGroupCategoryJobUser : updatePurchaseGroupCategoryJobUserList) {
			this.updatePurchaseGroupCategoryJobUser(updatePurchaseGroupCategoryJobUser);
		}
	}

	/**
	 * 직무 담당자 수정
	 * @param updatePurchaseGroupCategoryJobUser
	 */
	public void updatePurchaseGroupCategoryJobUser(Map<String, Object> updatePurchaseGroupCategoryJobUser) {
		jobRepository.updatePurchaseGroupCategoryJobUser(updatePurchaseGroupCategoryJobUser);
	}

	/**
	 * 직무 담당자 리스트 추가
	 * @param insertPurchaseGroupCategoryJobUserList
	 */
	public void insertListPurchaseGroupCategoryJobUser(List<Map<String, Object>> insertPurchaseGroupCategoryJobUserList) {
		for (Map<String, Object> purchaseGroupCategoryJobUser : insertPurchaseGroupCategoryJobUserList) {
			this.insertPurchaseGroupCategoryJobUser(purchaseGroupCategoryJobUser);
		}
	}

	/**
	 * 직무 담당자 추가
	 * @param purchaseGroupCategoryJobUser
	 */
	public void insertPurchaseGroupCategoryJobUser(Map<String, Object> purchaseGroupCategoryJobUser) {
		jobRepository.insertPurchaseGroupCategoryJobUser(purchaseGroupCategoryJobUser);
	}


	/**
	 * 직무담당자를 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2016. 2. 19
	 * @Method Name : deleteListPurchaseGroupCategoryJobUser
	 */
	public ResultMap deleteListPurchaseGroupCategoryJobUser(Map<String, Object> param){
		List<Map<String, Object>> deletePurchaseGroupCategoryJobUserList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());
		
		// DELETE
		for(Map purchaseGroupCategoryJobUser : deletePurchaseGroupCategoryJobUserList){
			this.deletePurchaseGroupCategoryJobUser(purchaseGroupCategoryJobUser);
		}
		
		return ResultMap.SUCCESS();
	}

	private void deletePurchaseGroupCategoryJobUser(Map purchaseGroupCategoryJobUser) {
		jobRepository.deletePurchaseGroupCategoryJobUser(purchaseGroupCategoryJobUser);
	}


	/**
	 * 사용자 직무담당자 삭제
	 * @param param
	 */
	private void deletePurchaseGroupCategoryJobUserByUserId(Map<String, Object> param) {
		jobRepository.deletePurchaseGroupCategoryJobUserByUserId(param);
	}


	/**
	 * 현재 사용 중인 구매 그룹 코드 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUsedPurchaseGroupCategory(Map<String, Object> param) {
		return jobRepository.findListUsedPurchaseGroupCategory(param);
	}

	/**
	 * 직무 별 품목 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryItem(Map<String, Object> param) {
		return jobRepository.findListPurchaseGroupCategoryItem(param);
	}
	
	/**
	 * 직무와 연결된 품목목록을 삭제한다.
	 * @Method Name : deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode
	 */
	public ResultMap deleteListPurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(Map<String, Object> param){
		List<Map<String, Object>> deletePurchaseGroupCategoryAndItemMappingList = (List<Map<String, Object>>)param.get("deleteList");
		
		// DELETE
		for(Map<String, Object> purchaseGroupCategoryAndItemMapping : deletePurchaseGroupCategoryAndItemMappingList){
			this.deletePurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(purchaseGroupCategoryAndItemMapping);
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 직무 연결된 구매그룹 코드 품목 삭제 ( 조회 조건 구매그룹 코드 )
	 * @param purchaseGroupCategoryAndItemMapping
	 */
	public void deletePurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(Map<String, Object> purchaseGroupCategoryAndItemMapping) {
		jobRepository.deletePurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(purchaseGroupCategoryAndItemMapping);
	}

	/**
	 * 직무와 연결할 품목목록을 저장한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2016. 2. 19
	 * @Method Name : savePurchaseGroupCategoryAndItemMapping
	 */
	public Map<String, Object> savePurchaseGroupCategoryAndItemMapping(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		List<Map<String, Object>> insertMappingList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		List<Map<String, Object>> updateMappingList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());

		// 구매 그룹 & 품목 분류 맵핑 리스트 추가
		this.insertListPurchaseGroupCategoryAndItemMapping(insertMappingList);

		// 구매 그룹 & 품목 분류 맵핑 리스트 수정
		this.updateListPurchaseGroupCategoryAndItemMapping(updateMappingList);
		
		
		return resultMap;
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 리스트 수정
	 * @param updateMappingList
	 */
	public void updateListPurchaseGroupCategoryAndItemMapping(List<Map<String, Object>> updateMappingList) {
		for (Map<String, Object> updateMapping : updateMappingList) {
			this.updatePurchaseGroupCategoryAndItemMapping(updateMapping);
		}
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 수정
	 * @param updateMapping
	 */
	public void updatePurchaseGroupCategoryAndItemMapping(Map<String, Object> updateMapping) {
		jobRepository.updatePurchaseGroupCategoryAndItemMapping(updateMapping);
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 리스트 추가 
	 * @param insertMappingList
	 */
	public void insertListPurchaseGroupCategoryAndItemMapping(List<Map<String, Object>> insertMappingList) {
		for (Map<String, Object> insertMapping : insertMappingList) {
			this.insertPurchaseGroupCategoryAndItemMapping(insertMapping);
		}
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 추가
	 * @param insertMapping
	 */
	public void insertPurchaseGroupCategoryAndItemMapping(Map<String, Object> insertMapping) {
		jobRepository.insertPurchaseGroupCategoryAndItemMapping(insertMapping);
	}


	/**
	 * 사용자 직무담당자 리스트 삭제 ( 조회 조건 user id )
	 * @param deleteUserList
	 */
	public void deletePurchaseGroupCategoryJobUserByUserId(List<Map<String, Object>> deleteUserList) {
		for (Map<String, Object> deleteUserInfo : deleteUserList) {
			this.deletePurchaseGroupCategoryJobUserByUserId(deleteUserInfo);
		}
	}
}
