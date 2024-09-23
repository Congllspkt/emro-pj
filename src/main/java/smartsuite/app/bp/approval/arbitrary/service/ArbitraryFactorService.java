package smartsuite.app.bp.approval.arbitrary.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import smartsuite.app.bp.approval.arbitrary.repository.ArbitraryFactorRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

/**
 * 전결설정항목 관련 처리하는 서비스 Class입니다.
 *
 * @Since 2019.02.20
 */
@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ArbitraryFactorService {

	@Inject
	ArbitraryFactorRepository arbitraryFactorRepository;
	
	/**
	 * 전결설정항목를 등록한다.
	 *
	 */
	public void insertFactor(Map<String, Object> param) {
		arbitraryFactorRepository.insertFactor(param);
	}
	
	/**
	 * 전결설정항목를 수정한다.
	 *
	 */
	public void updateFactor(Map<String, Object> param) {
		arbitraryFactorRepository.updateFactor(param);
	}
	
	/**
	 * 전결설정항목를 삭제한다.
	 *
	 */
	public void deleteFactor(Map<String, Object> param) {
		arbitraryFactorRepository.deleteFactor(param);
	}
	
	/**
	 * 전결설정항목 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListFactor(Map<String, Object> param) {
		return arbitraryFactorRepository.selectListFactor(param);
	}
	
	/**
	 * 전결설정항목 상세 정보를 조회한다.
	 *
	 */
	public Map<String, Object> selectFactor(Map<String, Object> param) {
		return arbitraryFactorRepository.selectFactor(param);
	}
	
	/**
	 * 전결설정항목 상세 정보를 저장(신규등록/수정)한다.
	 *
	 */
	public Map<String, Object> saveFactor(Map<String, Object> param) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> factorData = (Map<String, Object>)param.get("factorData");
		final String FACTOR_ID_KEY = "dapvl_cnd_inpvar_uuid";

		String factId = (String)factorData.get(FACTOR_ID_KEY); // 전결항목 아이디
		if (StringUtils.isEmpty(factId)) { // 신규
			factId = UUID.randomUUID().toString(); // UUID
			factorData.put(FACTOR_ID_KEY, factId);

			this.insertFactor(factorData);
		} else {
			this.updateFactor(factorData);
		}

		resultMap.put("factorData", factorData);
		return resultMap;
	}
	
	/**
	 * 전결설정항목 목록을 삭제한다.
	 *
	 */
	public Map<String, Object> deleteListFactor(Map param) {
		List<Map<String, Object>> deleteFactors = (List<Map<String, Object>>)param.get("deleteFactors");
		List<Map<String, Object>> deleteList = Lists.newArrayList();
		
		for (Map<String, Object> data : deleteFactors) {
			deleteList.add(data);
		}
		
		for (Map<String, Object> data : deleteList) {
			this.deleteFactor(data);
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		return resultMap;
	}
	
}
