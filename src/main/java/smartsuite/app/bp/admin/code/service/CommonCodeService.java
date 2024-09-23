package smartsuite.app.bp.admin.code.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.code.repository.CommonCodeRepository;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.messagesource.web.MessageService;


@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class CommonCodeService {

	/** The message service. */
	@Inject
	MessageService messageService;

	@Inject
	CacheManager cacheManager;

	@Inject
	CommonCodeRepository commonCodeRepository;

	/**
	 * 공통 코드의 경우 명칭이 혼재되어있어, 해당 부분을 명시함
	 *  	// CCD_CSTR_CND_VAL  - 상세 코드 속성 값
	 * 		GroupCodeAttributeValue
	 *
	 * 		// ESACDDL - 상세 코드 명
	 * 		GroupCodeDetailName
	 *
	 * 		// DTLCD - 상세 코드 정보
	 * 		GroupCodeDetailInfo
	 *
	 * 		// CCD_CSTR_CND - 그룹 코드 속성 정보
	 * 		GroupCodeAttributeInfo
	 *
	 * 		// CCD  - 그룹 코드 정보
	 * 		GroupCodeInfo
	 */

	/**
	 *  그룹코드(CCD) 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListGroupCode(Map<String, Object> param) {
		return commonCodeRepository.findListGroupCode(param);
	}

	/**
	 * 그룹코드(CCD) 저장한다.
	 * @param param
	 * @return
	 */
	public ResultMap saveListGroupCode(Map<String, Object> param){
		List<Map<String, Object>> updateGroupCodeList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertGroupCodeList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());

		// 코드 캐쉬
		this.commonCodeCacheEvict(insertGroupCodeList);

		// 그룹 코드 수정
		this.updateGroupCodeList(updateGroupCodeList);

		// 그룹 코드 추가
		this.insertGroupCodeList(insertGroupCodeList);

		return ResultMap.SUCCESS();
		
	}

	/**
	 * 그룹 코드 리스트 추가
	 * @param insertGroupCodeList
	 */
	public void insertGroupCodeList(List<Map<String, Object>> insertGroupCodeList) {
		// INSERT
		for(Map groupCodeInfo : insertGroupCodeList){
			this.insertGroupCode(groupCodeInfo);
		}
	}

	/**
	 * 그룹코드(CCD) 추가
	 * @param groupCodeInfo
	 */
	public void insertGroupCode(Map groupCodeInfo) {
		commonCodeRepository.insertGroupCode(groupCodeInfo);
	}

	/**
	 * 그룹코드 리스트 수정
	 * @param updateGroupCodeList
	 */
	private void updateGroupCodeList(List<Map<String, Object>> updateGroupCodeList) {
		// UPDATE
		for(Map groupCodeInfo : updateGroupCodeList){
			this.updateGroupCode(groupCodeInfo);
		}
	}

	/**
	 * 그룹 코드(CCD) 수정
	 * @param groupCodeInfo
	 */
	public void updateGroupCode(Map groupCodeInfo) {
		commonCodeRepository.updateGroupCodeInfo(groupCodeInfo);
	}

	/**
	 * 그룹코드(CCD) 삭제한다.
	 * @param param
	 * @return
	 */
	public ResultMap deleteListGroupCodeProcess(Map<String, Object> param) {
		List<Map<String, Object>> deleteGroupCodeList = (List<Map<String, Object>>)param.getOrDefault("deleteList",Lists.newArrayList());

		this.commonCodeCacheEvict(deleteGroupCodeList);
		
		// 삭제 시 그룹코드(CCD)와 연관된 CCD_CSTR_CND, DTLCD, ESACDDL, CCD_CSTR_CND_VAL 테이블 데이터도 함께 삭제
		this.deleteListGroupCode(deleteGroupCodeList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 그룹 코드 리스트 삭제
	 * @param deleteGroupCodeList
	 */
	public void deleteListGroupCode(List<Map<String, Object>> deleteGroupCodeList) {
		for(Map groupCodeInfo : deleteGroupCodeList){
			this.deleteGroupCode(groupCodeInfo);
		}
	}

	/**
	 * 그룹 코드 삭제
	 * 삭제 시 그룹코드(CCD)와 연관된 CCD_CSTR_CND, DTLCD, ESACDDL, CCD_CSTR_CND_VAL 테이블 데이터도 함께 삭제
	 * @param groupCodeInfo
	 */
	public void deleteGroupCode(Map groupCodeInfo) {
		// 상세 코드 속성 값(CCD_CSTR_CND_VAL) 삭제
		this.deleteGroupCodeAttributeValue(groupCodeInfo);

		// 상세 코드 명(ESACDDL) 삭제
		this.deleteGroupCodeDetailName(groupCodeInfo);

		// 상세 코드(DTLCD) 삭제
		this.deleteGroupCodeDetailInfo(groupCodeInfo);

		// 그룹 코드 속성(CCD_CSTR_CND) 삭제
		this.deleteGroupCodeAttributeInfo(groupCodeInfo);

		// 그룹 코드(CCD) 삭제
		this.deleteGroupCodeInfo(groupCodeInfo);
	}

	/**
	 * 그룹 코드 삭제
	 * @param row
	 */
	public void deleteGroupCodeInfo(Map row) {
		commonCodeRepository.deleteGroupCodeInfo(row);
	}

	/**
	 * 그룹코드 속성(CCD_CSTR_CND) 목록 조회한다.
	 *
	 * @param param the param
	 */
	public List findListGroupCodeAttribute(Map<String, Object> param){
		return commonCodeRepository.findListGroupCodeAttribute(param);
	}

	/**
	 * 그룹코드 속성(CCD_CSTR_CND) 저장한다.
	 * @param param
	 * @return
	 */
	public ResultMap saveListGroupCodeAttribute(Map<String, Object> param){
		List<Map<String, Object>> updateGroupCoudAttrList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertGroupCodeAttrList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		
		// UPDATE
		this.updateListGroupCodeAttribute(updateGroupCoudAttrList);

		// INSERT
		this.insertListGroupCodeAttribute(insertGroupCodeAttrList);

		
		return ResultMap.SUCCESS();
	}

	/**
	 * 그룹코드 속성 리스트 추가
	 * @param insertGroupCodeAttrList
	 */
	private void insertListGroupCodeAttribute(List<Map<String, Object>> insertGroupCodeAttrList) {
		for(Map insertGroupCodeAttrInfo : insertGroupCodeAttrList){
			this.insertGroupCodeAttributeInfo(insertGroupCodeAttrInfo);
		}
	}

	/**
	 * 그룹코드 속성 추가
	 * @param insertGroupCodeAttrInfo
	 */
	private void insertGroupCodeAttributeInfo(Map insertGroupCodeAttrInfo) {
		commonCodeRepository.insertGroupCodeAttributeInfo(insertGroupCodeAttrInfo);
	}

	/**
	 * 그룹코드 속성 리스트 수정
	 * @param updateGroupAttrCodeList
	 */
	public void updateListGroupCodeAttribute(List<Map<String, Object>> updateGroupAttrCodeList) {
		for(Map groupAttributeInfo : updateGroupAttrCodeList){
			this.updateGroupCodeAttribute(groupAttributeInfo);
		}
	}

	/**
	 * 그룹코드 속성 수정
	 * @param groupAttributeInfo
	 */
	public void updateGroupCodeAttribute(Map groupAttributeInfo) {
		commonCodeRepository.updateGroupCodeAttributeInfo(groupAttributeInfo);
	}


	/**
	 * 그룹코드 속성(CCD_CSTR_CND) 삭제요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListGroupCodeAttributeRequest(Map<String, Object> param){
		List<Map<String, Object>> deleteGroupCodeAttributeList = (List<Map<String, Object>>)param.get("deleteList");

		// 그룹 코드 속성 리스트 삭제
		this.deleteListGroupCodeAttribute(deleteGroupCodeAttributeList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 그룹 코드 속성 리스트 삭제
	 * @param deleteGroupCodeAttributeList
	 */
	public void deleteListGroupCodeAttribute(List<Map<String, Object>> deleteGroupCodeAttributeList) {
		for(Map deleteGroupCodeAttribute : deleteGroupCodeAttributeList){
			// 상세 코드 속성 값(CCD_CSTR_CND_VAL) 삭제
			this.deleteGroupCodeAttributeValueByAttributeCode(deleteGroupCodeAttribute);

			// 그룹 코드 속성(CCD_CSTR_CND) 삭제
			this.deleteGroupCodeAttributeInfoByAttrCode(deleteGroupCodeAttribute);
		}
	}


	/**
	 * 그룹코드 속성(CCD_CSTR_CND)을 삭제한다.
	 * @param deleteGroupCodeAttribute
	 */
	public void deleteGroupCodeAttributeInfoByAttrCode(Map deleteGroupCodeAttribute){
		commonCodeRepository.deleteGroupCodeAttributeInfoByAttrCode(deleteGroupCodeAttribute);
	}

	/**
	 * 그룹코드 속성(CCD_CSTR_CND)을 삭제한다.
	 * @param groupCodeInfo
	 */
	public void deleteGroupCodeAttributeInfo(Map groupCodeInfo){
		commonCodeRepository.deleteGroupCodeAttributeInfo(groupCodeInfo);
	}


	/**
	 * 상세코드(DTLCD) 목록 조회한다.
	 * @param param
	 * @return
	 */
	public List findListDetailCode(Map<String, Object> param){
		// 상세코드 목록(DTLCD) 조회
		List<Map<String,Object>> groupDetailCodeList = this.findListGroupDetailCode(param);
		
		// 상세코드 목록(DTLCD)에 매핑되어 있는 상세 코드 속성 값(CCD_CSTR_CND_VAL)을 세팅해 준다.
		for(Map groupDetailCodeInfo : groupDetailCodeList){
			// 그룹코드 속성(CCD_CSTR_CND,CCD_CSTR_CND_VAL) 조회
			List<Map<String,Object>> groupDetailCodeValueList = this.findListGroupCodeAttributeAndAttrValue(groupDetailCodeInfo);

			for(int uValue=0; uValue<groupDetailCodeValueList.size();uValue++){
				Map<String,Object> groupDetailCodeValue = groupDetailCodeValueList.get(uValue);
				groupDetailCodeInfo.put("cstr_cnd_cd"+uValue			, groupDetailCodeValue.getOrDefault("cstr_cnd_cd",""));			// 속성 코드
				groupDetailCodeInfo.put("cstr_cnd_nm"+uValue		, groupDetailCodeValue.getOrDefault("cstr_cnd_nm",""));		// 속성코드명
				groupDetailCodeInfo.put("cstr_cnd_val"+uValue	, groupDetailCodeValue.getOrDefault("cstr_cnd_val","")); 	// 상세 코드 속성 값
			}
		}
		return groupDetailCodeList;
	}

	/**
	 * 그룹코드 속성(CCD_CSTR_CND&CCD_CSTR_CND_VAL) 목록
	 * @param groupDetailCodeInfo
	 * @return
	 */
	public List<Map<String, Object>> findListGroupCodeAttributeAndAttrValue(Map groupDetailCodeInfo) {
		return commonCodeRepository.findListGroupCodeAttributeAndAttrValue(groupDetailCodeInfo);
	}

	/**
	 * 상세코드 목록(DTLCD) 조회
	 * @param param
	 */
	public List<Map<String, Object>> findListGroupDetailCode(Map<String, Object> param) {
		return commonCodeRepository.findListGroupDetailCode(param);
	}

	/**
	 * 상세코드(DTLCD)를 저장한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 2. 2
	 * @Method Name : saveGroupCodeAttributeList
	 */
	public ResultMap saveListDetailCode(Map<String, Object> param){
		List<Map<String, Object>> updateDetailCodeList = (List<Map<String, Object>>)param.getOrDefault("updateList",Lists.newArrayList());
		List<Map<String, Object>> insertDetailCodeList = (List<Map<String, Object>>)param.getOrDefault("insertList",Lists.newArrayList());
		
		// 신규 추가, 수정된 상세코드 정보를 DTLCD, ESACDDL 테이블에 저장
		// 속성컬럼에 값 입력 시 CCD_CSTR_CND_VAL 테이블에 상세코드 속성값 저장

		// UPDATE
		this.updateListDetailCode(updateDetailCodeList);

		// INSERT
		this.insertListDetailCode(insertDetailCodeList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 *  상세코드 리스트 추가
	 * @param insertDetailCodeList
	 */
	private void insertListDetailCode(List<Map<String, Object>> insertDetailCodeList) {
		for(Map insertDetailCode : insertDetailCodeList){
			this.insertDetailCode(insertDetailCode);
		}
	}

	/**
	 * 상세코드 추가
	 * @param insertDetailCode
	 */
	public void insertDetailCode(Map insertDetailCode) {
		// 코드가 현재 존재하는지 확인한다.
		Map<String,Object> getCommonCodeInfo = this.findGroupCodeDetailInfo(insertDetailCode);

		//존재 할 경우, update
		if(MapUtils.isNotEmpty(getCommonCodeInfo)){
			this.updateGroupCodeDetailInfo(insertDetailCode);
		}else{//존재하지 않을 경우, insert
			this.insertGroupCodeDetailInfo(insertDetailCode);
		}

		// 상세코드(DTLCD) 저장 - 다른 언어에서 추가하는 경우도 있으므로 merge
		//this.mergeGroupCodeDetailInfo(insertDetailCode);

		// 상세코드명(ESACDDL) 저장
		this.insertGroupCodeDetailName(insertDetailCode);
		
		// 다국어 별 기본 라벨 생성
		this.insertDefaultLabelByAvailableLocale(insertDetailCode);

		// 그룹코드속성(CCD_CSTR_CND) 조회
		List<Map<String,Object>> groupCodeAttributeList = this.findListGroupCodeAttribute(insertDetailCode);

		// 그룹코드 속성 개수 만큼 체크
		for(int i=0; i<groupCodeAttributeList.size(); i++){
			String detailCodeAttributeValue = insertDetailCode.getOrDefault("cstr_cnd_val" + i ,"") == null?  "" : (String) insertDetailCode.getOrDefault("cstr_cnd_val" + i ,"");
			Map<String ,Object> groupCodeAttribute = groupCodeAttributeList.get(i);
			
			// 속성컬럼에 값 입력시 INSERT
			if(StringUtils.isNotEmpty(detailCodeAttributeValue)){
				insertDetailCode.put("cstr_cnd_cd", groupCodeAttribute.getOrDefault("cstr_cnd_cd" , ""));
				insertDetailCode.put("cstr_cnd_val", detailCodeAttributeValue);
				this.insertGroupCodeAttributeValue(insertDetailCode);
			}
		}
	}

	/**
	 * 상세 코드 명(ESACDDL) 추가
	 * @param insertDetailCode
	 */
	public void insertGroupCodeDetailName(Map insertDetailCode) {
		commonCodeRepository.insertGroupCodeDetailName(insertDetailCode);
	}

	/**
	 * 상세 코드 update / insert merge
	 * @param insertDetailCode
	 */
	public void mergeGroupCodeDetailInfo(Map insertDetailCode) {
		commonCodeRepository.mergeGroupCodeDetailInfo(insertDetailCode);
	}

	/**
	 * 상세 코드 추가 (DTLCD)
	 * @param insertDetailCode
	 */
	public void insertGroupCodeDetailInfo(Map insertDetailCode) {
		commonCodeRepository.insertGroupCodeDetailInfo(insertDetailCode);
	}


	/**
	 * 상세코드 조회
	 * @param insertDetailCode
	 * @return
	 */
	public Map<String, Object> findGroupCodeDetailInfo(Map insertDetailCode) {
		return commonCodeRepository.findGroupCodeDetailInfo(insertDetailCode);
	}

	/**
	 * 상세코드 리스트 수정
	 * @param updateDetailCodeList
	 */
	public void updateListDetailCode(List<Map<String, Object>> updateDetailCodeList) {
		for(Map updateDetailCode : updateDetailCodeList){
			this.updateDetailCode(updateDetailCode);
		}
	}

	/**
	 * 상세 코드 수정
	 * @param updateDetailCode
	 */
	public void updateDetailCode(Map updateDetailCode) {
		// 상세코드(DTLCD) 저장
		this.updateGroupCodeDetailInfo(updateDetailCode);

		// 상세코드명(ESACDDL) 저장
		this.updateGroupCodeDetailName(updateDetailCode);

		// 그룹코드속성(CCD_CSTR_CND) 조회
		List<Map<String,Object>> groupCodeAttributeList = this.findListGroupCodeAttribute(updateDetailCode);

		// 그룹코드 속성 개수 만큼 체크
		for(int i=0; i<groupCodeAttributeList.size(); i++){
			// 상세코드속성값(CCD_CSTR_CND_VAL) 조회
			updateDetailCode.put("cstr_cnd_cd", updateDetailCode.getOrDefault("cstr_cnd_cd" + i ,""));

			Map<String,Object> detailCodeAttributeValueMap = this.findDetailCodeAttributeValue(updateDetailCode);

			/**
			 * 상세코드속성값(CCD_CSTR_CND_VAL) 저장
			 */
			String detailCodeAttributeValue = updateDetailCode.getOrDefault("cstr_cnd_val" + i , "") == null? "" : (String)updateDetailCode.getOrDefault("cstr_cnd_val" + i , "");
			if(MapUtils.isNotEmpty(detailCodeAttributeValueMap) && StringUtils.isNotEmpty(detailCodeAttributeValue)){
				// 상세코드속성값 테이블에 데이터가 존재하고, 속성컬럼에 값 입력시 UPDATE
				updateDetailCode.put("cstr_cnd_val", detailCodeAttributeValue); // 상세 코드 속성 값
				this.updateGroupCodeAttributeValue(updateDetailCode);

			}else if(MapUtils.isNotEmpty(detailCodeAttributeValueMap) && StringUtils.isEmpty(detailCodeAttributeValue)){
				// 상세코드속성값 테이블에 데이터가 존재하고, 속성컬럼에 값 입력이 null일경우 DELETE
				this.deleteGroupCodeAttributeValueByAttributeCodeAndDetailCode(updateDetailCode);

			}else if(MapUtils.isEmpty(detailCodeAttributeValueMap) && StringUtils.isNotEmpty(detailCodeAttributeValue)){
				// 상세코드속성값 테이블에 데이터가 존재하지 않고, 속성컬럼에 값 입력시 INSERT
				updateDetailCode.put("cstr_cnd_val", detailCodeAttributeValue);
				this.insertGroupCodeAttributeValue(updateDetailCode);
			}
		}
	}

	/**
	 * 상세 코드 속성 값(CCD_CSTR_CND_VAL) 수정
	 * @param updateDetailCode
	 */
	private void updateGroupCodeAttributeValue(Map updateDetailCode) {
		commonCodeRepository.updateGroupCodeAttributeValue(updateDetailCode);
	}

	/**
	 * 상세 코드 속성 값(ESADATA) 조회
	 * @param updateDetailCode
	 */
	private Map<String, Object> findDetailCodeAttributeValue(Map updateDetailCode) {
		return commonCodeRepository.findDetailCodeAttributeValue(updateDetailCode);
	}

	/**
	 * 상세 코드 명(ESACDDL) 수정
	 * @param updateDetailCode
	 */
	public void updateGroupCodeDetailName(Map updateDetailCode) {
		commonCodeRepository.updateGroupCodeDetailName(updateDetailCode);
	}

	/**
	 * 상세 코드 테이블(DTLCD) 수정
	 * @param updateDetailCode
	 */
	public void updateGroupCodeDetailInfo(Map updateDetailCode) {
		commonCodeRepository.updateGroupCodeDetailInfo(updateDetailCode);
	}


	/**
	 * 상세 코드(DTLCD)를 삭제요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDetailCodeRequest(Map param){
		List<Map> deleteDetailCodeList = (List<Map>)param.getOrDefault("deleteList",Lists.newArrayList());

		//상세 코드 리스트를 삭제한다.
		this.deleteListDetailCode(deleteDetailCodeList);
		
		return ResultMap.SUCCESS();
	}

	/**
	 * 상세 코드 리스트를 삭제한다.
	 * @param deleteDetailCodeList
	 */
	public void deleteListDetailCode(List<Map> deleteDetailCodeList) {
		// 상세코드 속성 값 삭제
		this.deleteListGroupCodeAttributeValueByDetailCode(deleteDetailCodeList);

		// 상세코드 명 삭제
		this.deleteListGroupCodeDetailNameByDetailCode(deleteDetailCodeList);

		// 상세코드 삭제
		this.deleteListGroupCodeDetailInfoByDetailCode(deleteDetailCodeList);
	}


	/**
	 * 상세 코드 속성값 리스트를 삭제
	 * @param deleteDetailCodeList
	 */
	public void deleteListGroupCodeAttributeValueByDetailCode(List<Map> deleteDetailCodeList){
		for(Map detailCode : deleteDetailCodeList){
			this.deleteGroupCodeAttributeValueByDetailCode(detailCode);
		}
	}

	/**
	 * 상세 코드 속성 값 삭제
	 * @param detailCode
	 */
	private void deleteGroupCodeAttributeValueByDetailCode(Map detailCode) {
		commonCodeRepository.deleteGroupCodeAttributeValueByDetailCode(detailCode);
	}

	/**
	 * 상세코드명(ESACDDL) 리스트를 삭제
	 * @param deleteDetailCodeList
	 */
	public void deleteListGroupCodeDetailNameByDetailCode(List<Map> deleteDetailCodeList){
		
		for(Map detailGroupCode : deleteDetailCodeList){
			this.deleteGroupCodeDetailNameByDetailCode(detailGroupCode);
		}
	}

	/**
	 * 상세코드 삭제 ESACDDL
	 * @param detailGroupCode
	 */
	public void deleteGroupCodeDetailNameByDetailCode(Map detailGroupCode) {
		commonCodeRepository.deleteGroupCodeDetailNameByDetailCode(detailGroupCode);
	}


	/**
	 * 상세 코드 리스트 삭제 DTLCD
	 * @param deleteDetailCodeList
	 */
	public void deleteListGroupCodeDetailInfoByDetailCode(List<Map> deleteDetailCodeList){

		for(Map detailCode : deleteDetailCodeList){
			this.deleteGroupCodeDetailInfoByDetailCode(detailCode);
		}
	}

	/**
	 * 상세 코드 삭제 DTLCD
	 * @param detailCode
	 */
	public void deleteGroupCodeDetailInfoByDetailCode(Map detailCode) {
		commonCodeRepository.deleteGroupCodeDetailInfoByDetailCode(detailCode);
	}

	/**
	 * 상세코드명(ESACDDL)을 삭제한다.
	 * @param param
	 */
	public void deleteGroupCodeDetailName(Map param){
		commonCodeRepository.deleteGroupCodeDetailName(param);
	}

	/**
	 * 상세코드속성값(CCD_CSTR_CND_VAL)을 삭제 (조건 attr code / detail code )
	 * @param param
	 */
	public void deleteGroupCodeAttributeValueByAttributeCodeAndDetailCode(Map param){
		commonCodeRepository.deleteGroupCodeAttributeValueByAttributeCodeAndDetailCode(param);
	}
	
	/**
	 * 상세코드속성값(CCD_CSTR_CND_VAL)을 삭제한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 23
	 * @Method Name : deleteGroupCodeAttributeValue
	 */
	public void deleteGroupCodeAttributeValue(Map param){
		commonCodeRepository.deleteGroupCodeAttributeValue(param);
	}
	
	/**
	 * 상세 코드 속성 값(CCD_CSTR_CND_VAL) DELETE by ATTR_CD
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @Date : 2016. 2. 23
	 * @Method Name : deleteGroupCodeAttributeValueByAttributeCode
	 */
	public void deleteGroupCodeAttributeValueByAttributeCode(Map param){
		commonCodeRepository.deleteGroupCodeAttributeValueByAttributeCode(param);
	}

	/**
	 * 그룹 코드 정보 삭제 DTLCD
	 * @param param
	 */
	public void deleteGroupCodeDetailInfo(Map param){
		commonCodeRepository.deleteGroupCodeDetailInfo(param);
	}


	/**
	 * 상세코드속성값(CCD_CSTR_CND_VAL)을 입력한다.
	 * @param groupCodeAttribute
	 */
	public void insertGroupCodeAttributeValue(Map groupCodeAttribute){
		commonCodeRepository.insertGroupCodeAttributeValue(groupCodeAttribute);
	}

	/**
	 * 다국어 목록 별 그룹코드 기본이름 추가
	 * @param insertDetailCode
	 */
	private void insertDefaultLabelByAvailableLocale(Map insertDetailCode){
		Locale defaultLocale = new Locale("ko","KR");
		// 서버에 관리하는 다국어 목록 가져와서 다국어 별로 insert
		Collection<Locale> availableLocales = messageService.getAvailableLocales();
		for(Locale availableLocale : availableLocales){
			insertDetailCode.put("locale", availableLocale);
			insertDetailCode.put("defaultLocale", defaultLocale);
			this.insertDefaultGroupCodeDetailName(insertDetailCode);
		}
	}

	/**
	 * 상세 코드 명 기본 이름 추가
	 * @param insertDetailCode
	 */
	public void insertDefaultGroupCodeDetailName(Map insertDetailCode) {
		commonCodeRepository.insertDefaultGroupCodeDetailName(insertDetailCode);
	}

	public void commonCodeCacheEvict(List<Map<String, Object>> codes) {
		Cache cache = cacheManager.getCache("cmmn-code");
		Collection<Locale> locales = messageService.getAvailableLocales();
		for(Locale locale : locales) {
			String localeStr = "_" + locale.toString();
			for(Map<String,Object> group : codes) {
				cache.evict(group.get("ccd") + localeStr);
			}
		}
	}
}
