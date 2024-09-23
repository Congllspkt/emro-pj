package smartsuite.app.bp.admin.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.code.repository.ExchangeRateRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * 환율 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @FileName ExchangeRateService.java
 * @package smartsuite.app.bp.admin.code
 * @Since 2017. 06. 05
 * @변경이력 : [2017. 06. 05] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class ExchangeRateService {

	@Inject
	ExchangeRateRepository exchangeRateRepository;

	/**
	 * 환율관리 데이터를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListExchangeRate(Map<String, Object> param) {
		return exchangeRateRepository.findListExchangeRate(param);
	}
	
	/**
	 * 환율관리 데이터를 저장한다.
	 * @param param
	 * @return
	 */
	public ResultMap saveListExchangeRate(Map<String, Object> param){
		List<Map<String, Object>> updateExchangeRateList = (List<Map<String, Object>>)param.getOrDefault("updateList", Lists.newArrayList());
		List<Map<String, Object>> insertExchangeRateList = (List<Map<String, Object>>)param.getOrDefault("insertList", Lists.newArrayList());

		//  환율관리 데이터 리스트 수정
		this.updateListExchangeRate(updateExchangeRateList);
		
		//  환율관리 데이터 리스트 추가
		this.insertListExchangeRate(insertExchangeRateList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 환율관리 데이터 리스트 추가
	 * @param insertExchangeRateList
	 */
	public void insertListExchangeRate(List<Map<String, Object>> insertExchangeRateList) {
		for(Map exchangeRate : insertExchangeRateList){
			if (this.existDateCur(exchangeRate)) { // 중복체크
				throw new CommonException(ErrorCode.DUPLICATED);
			}
			this.insertExchangeRate(exchangeRate);
		}
	}

	/**
	 * 환율관리 데이터 추가
	 * @param exchangeRate
	 */
	public void insertExchangeRate(Map exchangeRate) {
		exchangeRateRepository.insertExchangeRate(exchangeRate);
	}

	/**
	 * 환율관리 데이터 리스트 수정
	 * @param updateExchangeRateList
	 */
	public void updateListExchangeRate(List<Map<String, Object>> updateExchangeRateList) {
		for(Map exchangeRate : updateExchangeRateList){
			this.updateExchangeRate(exchangeRate);
		}
	}

	/**
	 * 환율관리 데이터 수정
	 * @param exchangeRate
	 */
	public void updateExchangeRate(Map exchangeRate) {
		exchangeRateRepository.updateExchangeRate(exchangeRate);
	}

	/**
	 * 환율 정보 존재 여부
	 * @param row
	 * @return
	 */
	public boolean existDateCur(Map<String, Object> row) {
		int count = exchangeRateRepository.getCountDateCur(row);
		return (count > 0);
	}

	/**
	 * 환율관리 데이터 리스트를 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListExchangeRateRequest(Map<String, Object> param){
		List<Map<String, Object>> deleteExchangeRateList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());

		//환율 관리 데이터 리스트 삭제
		this.deleteListExchangeRate(deleteExchangeRateList);

		return ResultMap.SUCCESS();
	}

	public void deleteListExchangeRate(List<Map<String, Object>> deleteExchangeRateList) {
		for(Map exchangeRate : deleteExchangeRateList){
			this.deleteExchangeRate(exchangeRate);
		}
	}

	/**
	 * 환율관리 데이터 삭제
	 * @param exchangeRate
	 */
	public void deleteExchangeRate(Map exchangeRate) {
		exchangeRateRepository.deleteExchangeRate(exchangeRate);
	}
}
