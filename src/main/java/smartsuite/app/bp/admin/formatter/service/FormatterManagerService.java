package smartsuite.app.bp.admin.formatter.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections4.MapUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import smartsuite.app.bp.admin.formatter.repository.FormatterManagerRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * Formatter 관련 처리하는 서비스 Class입니다.
 *
 * @see 
 * @FileName FormatterService.java
 * @package smartsuite.app.bp.admin.formatter
 * @Since 2019. 2. 19
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class FormatterManagerService {

	@Inject
	FormatterManagerRepository formatterManagerRepository;

	
	/**
	 * list grp code by no formatter 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 19
	 * @Method Name : findListGroupCodeByNoFormatter
	 */
	public List<Map<String, Object>> findListGroupCodeByNoFormatter(Map<String, Object> param) {
		return formatterManagerRepository.findListGroupCodeByNoFormatter(param);
	}

	/**
	 * 자리 수 관련 그룹 코드를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPrecisionGroupCode(Map<String, Object> param) {
		return formatterManagerRepository.findListPrecisionGroupCode(param);
	}

	/**
	 * list prec dtl cd 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 19
	 * @Method Name : findListPrecisionDetailCode
	 */
	public List<Map<String, Object>> findListPrecisionDetailCode(Map<String, Object> param) {
		return formatterManagerRepository.findListPrecisionDetailCode(param);
	}

	/**
	 * prec grp cds 입력한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 2. 19
	 * @Method Name : insertPrecisionGroupCodes
	 */
	public ResultMap insertListPrecisionGroupCode(Map<String, Object> param) {
		List<Map<String, Object>> insertedPrecisionGroupCodeList = (List<Map<String, Object>>) param.getOrDefault("insertPrecisionCommonCodeList",Lists.newArrayList());
		List<String> existPrecisionGroupCodeList = Lists.newArrayList();
		
		if(insertedPrecisionGroupCodeList.size() > 0){
			existPrecisionGroupCodeList = findListExistPrecisionGroupCode(param);
		}

		for(Map<String, Object> inserted: insertedPrecisionGroupCodeList){
			String groupCode = inserted.getOrDefault("grp_cd","") == null ? "": (String) inserted.getOrDefault("grp_cd","");
			if(existPrecisionGroupCodeList.size() > 0){
				if(!existPrecisionGroupCodeList.contains(groupCode)){
					//insert
					insertPrecisionGroupCode(inserted);
				}
			}else {
				insertPrecisionGroupCode(inserted);
			}
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * prec grp cd 입력한다.
	 *
	 * @param param the param
	 * @Date : 2019. 2. 19
	 * @Method Name : insertPrecisionGroupCode
	 */
	@CachePut(value="cmmn_prec_format", key="#param['grp_cd'] + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public void insertPrecisionGroupCode(Map<String, Object> param){
		formatterManagerRepository.insertPrecisionGroupCode(param);
	}
	
	/**
	 * list exsit prec grp cd 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 19
	 * @Method Name : findListExistPrecisionGroupCode
	 */
	public List<String> findListExistPrecisionGroupCode(Map<String, Object> param) {
		return formatterManagerRepository.findListExistPrecisionGroupCode(param);
	}

	/**
	 * 소수점 그룹 코드 리스트 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListPrecisionGroupCodeRequest(Map<String, Object> param) {
		List<Map<String, Object>> deletePrecisionGroupCodeList = (List<Map<String, Object>>) param.getOrDefault("deletedPrecGrpCds",Lists.newArrayList());

		//소수점 그룹 코드 리스트 삭제
		this.deleteListPrecisionGroupCode(deletePrecisionGroupCodeList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 소수점 그룹 코드 리스트 삭제
	 * @param deletePrecisionGroupCodeList
	 */
	public void deleteListPrecisionGroupCode(List<Map<String, Object>> deletePrecisionGroupCodeList) {
		for(Map<String, Object> deletePrecisionGroupCode: deletePrecisionGroupCodeList){
			deletePrecisionDetailCodeByGroupCode(deletePrecisionGroupCode);
			deletePrecisionGroupCode(deletePrecisionGroupCode);
		}
	}

	/**
	 * prec grp cd 삭제한다.
	 *
	 * @param deletePrecisionGroupCode the deleted
	 * @Date : 2019. 2. 19
	 * @Method Name : deletePrecisionGroupCode
	 */
	@CacheEvict(value="cmmn_prec_format", key="#deletePrecisionGroupCode['decpt_use_ccd'] + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public void deletePrecisionGroupCode(Map<String, Object> deletePrecisionGroupCode) {
		formatterManagerRepository.deletePrecisionGroupCode(deletePrecisionGroupCode);
	}

	/**
	 * prec dtl cd by grp cd 삭제한다.
	 *
	 * @param deletePrecisionDetailCodeInfo the deleted
	 * @Date : 2019. 2. 19
	 * @Method Name : deletePrecisionDetailCodeByGroupCode
	 */
	public void deletePrecisionDetailCodeByGroupCode(Map<String, Object> deletePrecisionDetailCodeInfo) {
		formatterManagerRepository.deletePrecisionDetailCodeByGroupCode(deletePrecisionDetailCodeInfo);
	}

	/**
	 * list prec dtl cd 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 2. 20
	 * @Method Name : saveListPrecisionDetailCode
	 */
	public ResultMap saveListPrecisionDetailCode(Map<String, Object> param) {
		List<Map<String, Object>> updatePrecisionDetailCodeList = (List<Map<String, Object>>) param.get("updatePrecisionDetailCodeList");
		
		for(Map<String, Object> updatePrecisionInfo: updatePrecisionDetailCodeList){
			if(!updatePrecisionInfo.containsKey("decpt_use_dtlcd")  || "".equals((String)updatePrecisionInfo.get("decpt_use_dtlcd"))){
				insertPrecisionDetailCode(updatePrecisionInfo);
			}else{
				updatePrecisionDetailCode(updatePrecisionInfo);
			}
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * prec dtl cd 입력한다.
	 *
	 * @param insertPrecisionInfo the inserted
	 * @Date : 2019. 2. 20
	 * @Method Name : insertPrecisionDetailCode
	 */
	public void insertPrecisionDetailCode(Map<String, Object> insertPrecisionInfo) {
		formatterManagerRepository.insertPrecisionDetailCode(insertPrecisionInfo);
	}
	
	/**
	 * prec dtl cd 수정한다.
	 *
	 * @param updatePrecisionInfo the updated
	 * @Date : 2019. 2. 20
	 * @Method Name : updatePrecisionDetailCode
	 */
	public void updatePrecisionDetailCode(Map<String, Object> updatePrecisionInfo){
		formatterManagerRepository.updatePrecisionDetailCode(updatePrecisionInfo);
	}

	/**
	 * list display format 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 20
	 * @Method Name : findListDisplayFormat
	 */
	public List<Map<String, Object>> findListDisplayFormat(Map<String, Object> param) {
		return formatterManagerRepository.findListDisplayFormat(param);
	}
	
	/**
	 * list display format detail 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 21
	 * @Method Name : findListDisplayFormatDetail
	 */
	public List<Map<String, Object>> findListDisplayFormatDetail(Map<String, Object> param) {
		return formatterManagerRepository.findListDisplayFormatDetail(param);
	}

	/**
	 * list display format 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 2. 21
	 * @Method Name : saveListDisplayFormat
	 */
	public ResultMap saveListDisplayFormat(Map<String, Object> param) {
		List<Map<String, Object>> insertDisplayFormatList = (List<Map<String, Object>>) param.get("insertDisplayFormatList");
		List<Map<String, Object>> updateDisplayFormatList = (List<Map<String, Object>>) param.get("updateDisplayFormatList");

		if(insertDisplayFormatList.size() > 0 && existFormatName(param)){
			throw new CommonException(ErrorCode.DUPLICATED);
		}
		this.insertListDisplayFormat(insertDisplayFormatList);

		this.updateListDisplayFormat(updateDisplayFormatList);

		return ResultMap.SUCCESS();
	}

	/**
	 * Display format List 정보 수정
	 * @param updateDisplayFormatList
	 */
	public void updateListDisplayFormat(List<Map<String, Object>> updateDisplayFormatList) {
		for(Map<String, Object> updateDisplayFormat: updateDisplayFormatList){
			updateDisplayFormat(updateDisplayFormat);
		}
	}

	/**
	 * Display format list 정보 추가
	 * @param insertDisplayFormatList
	 */
	public void insertListDisplayFormat(List<Map<String, Object>> insertDisplayFormatList) {
		for(Map<String, Object> insertDisplayFormat: insertDisplayFormatList){
			insertDisplayFormat(insertDisplayFormat);
		}
	}

	/**
	 * Display 용 소수점 포맷 수정
	 * @param param
	 * @return
	 */
	public ResultMap updateDisplayPrecisionFormat(Map<String, Object> param) {
		formatterManagerRepository.updateDisplayPrecisionFormat(param);
		return ResultMap.SUCCESS();
	}


	/**
	 * 포맷 이름의 존재여부를 확인한다.
	 * @param param
	 * @return
	 */
	public boolean existFormatName(Map<String, Object> param) {
		int getFormatName = formatterManagerRepository.getCountDisplayFormatName(param);
		return (getFormatName > 0);
	}

	/**
	 * display format 입력한다.
	 *
	 * @param param the param
	 * @Date : 2019. 2. 21
	 * @Method Name : insertDisplayFormat
	 */
	public void insertDisplayFormat(Map<String, Object> param){
		formatterManagerRepository.insertDisplayFormat(param);
	}

	/**
	 * display format 수정한다.
	 *
	 * @param param the param
	 * @Date : 2019. 2. 21
	 * @Method Name : updateDisplayFormat
	 */
	public void updateDisplayFormat(Map<String, Object> param){
		formatterManagerRepository.updateDisplayFormat(param);
	}
	
	/**
	 * list display format 삭제한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 2. 21
	 * @Method Name : deleteListDisplayFormat
	 */
	/**
	 * 표기 포맷 리스트 삭제요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDisplayFormatRequest(Map<String, Object> param) {
		List<Map<String, Object>> deleteDisplayFormatList = (List<Map<String, Object>>) param.getOrDefault("deleteDisplayFormats",Lists.newArrayList());

		//표기 포맷 리스트 삭제
		this.deleteListDisplayFormat(deleteDisplayFormatList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 표기 포맷 리스트 삭제
	 * @param deleteDisplayFormatList
	 */
	public void deleteListDisplayFormat(List<Map<String, Object>> deleteDisplayFormatList) {
		for(Map<String, Object> deleted: deleteDisplayFormatList){
			deleteDisplayFormatDetailByFormatName(deleted);
			deleteDisplayFormatGroup(deleted);
		}
	}

	/**
	 * display format detail by grp 삭제한다.
	 *
	 * @param deleted the deleted
	 * @Date : 2019. 3. 5
	 * @Method Name : deleteDisplayFormatDetailByFormatName
	 */
	public void deleteDisplayFormatDetailByFormatName(Map<String, Object> deleted) {
		formatterManagerRepository.deleteDisplayFormatDetailByFormatName(deleted);
	}

	/**
	 * display format 삭제한다.
	 *
	 * @param deleted the deleted
	 * @Date : 2019. 2. 21
	 * @Method Name : deleteDisplayFormat
	 */
	public void deleteDisplayFormatGroup(Map<String, Object> deleted) {
		formatterManagerRepository.deleteDisplayFormatGroup(deleted);
	}

	/**
	 * list display format detail 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 3. 5
	 * @Method Name : saveListDisplayFormatDetail
	 */
	public ResultMap saveListDisplayFormatDetail(Map<String, Object> param) {
		List<Map<String, Object>> insertDisplayFormatDetailList = (List<Map<String, Object>>) param.get("insertDisplayFormatDetailList");
		List<Map<String, Object>> updateDisplayFormatDetailList = (List<Map<String, Object>>) param.get("updateDisplayFormatDetailList");
		
		
		this.insertListFormatDetail(insertDisplayFormatDetailList);
	
		this.updateListDisplayFormatDetail(updateDisplayFormatDetailList);
	
		return ResultMap.SUCCESS();
	}

	/**
	 * 표기 용 포맷 상세 리스트 수정
	 * @param updateDisplayFormatDetailList
	 */
	public void updateListDisplayFormatDetail(List<Map<String, Object>> updateDisplayFormatDetailList) {
		for(Map<String, Object> updated: updateDisplayFormatDetailList){
			updateDisplayFormatDetail(updated);
		}
	}

	/**
	 * 표기 용 포맷 상세 리스트 추가
	 * @param insertDisplayFormatDetailList
	 */
	public void insertListFormatDetail(List<Map<String, Object>> insertDisplayFormatDetailList) {
		for(Map<String, Object> inserted: insertDisplayFormatDetailList){
			insertDisplayFormatDetail(inserted);
		}
	}

	/**
	 * disp format detail 수정한다.
	 *
	 * @param updated the updated
	 * @Date : 2019. 3. 5
	 * @Method Name : updateDisplayFormatDetail
	 */
	public void updateDisplayFormatDetail(Map<String, Object> updated) {
		formatterManagerRepository.updateDisplayFormatDetail(updated);
	}

	/**
	 * disp format detail 입력한다.
	 *
	 * @param inserted the inserted
	 * @Date : 2019. 3. 5
	 * @Method Name : insertDisplayFormatDetail
	 */
	public void insertDisplayFormatDetail(Map<String, Object> inserted) {
		formatterManagerRepository.insertDisplayFormatDetail(inserted);
	}

	/**
	 * 표기 포맷 상세 리스트삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListDisplayFormatDetailRequest(Map<String, Object> param){
		List<Map<String, Object>> deleteDisplayFormatDetailList = (List<Map<String, Object>>) param.getOrDefault("deleteDisplayFormatDetailList",Lists.newArrayList());

		this.deleteListDisplayFormatDetail(deleteDisplayFormatDetailList);
		return ResultMap.SUCCESS();
	}

	/**
	 * 표기 포맷 상세 리스트 삭제
	 * @param deleteDisplayFormatDetailList
	 */
	public void deleteListDisplayFormatDetail(List<Map<String, Object>> deleteDisplayFormatDetailList) {
		for(Map<String, Object> deleted: deleteDisplayFormatDetailList){
			deleteDisplayFormatDetail(deleted);
		}
	}

	/**
	 * disp format detail 삭제한다.
	 *
	 * @param deleted the deleted
	 * @Date : 2019. 2. 21
	 * @Method Name : deleteDisplayFormatDetail
	 */
	public void deleteDisplayFormatDetail(Map<String, Object> deleted) {
		formatterManagerRepository.deleteDisplayFormatDetail(deleted);
	}

	/**
	 * list prec format by grp cd 조회한다.
	 *
	 * @param precGrpCd the prec grp cd
	 * @return the list< map< string, object>>
	 * @Date : 2019. 2. 22
	 * @Method Name : findListPrecisionFormatByGroupCode
	 */
	@Cacheable(value="cmmn_prec_format", key="#precGrpCd + '_' + T(org.springframework.context.i18n.LocaleContextHolder).getLocale().toString()")
	public List<Map<String, Object>> findListPrecisionFormatByGroupCode(String precGrpCd) {
		return formatterManagerRepository.findListPrecisionFormatByGroupCode(precGrpCd);
	}

	/**
	 * list prec format cur 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 3. 5
	 * @Method Name : findListPrecisionFormatCur
	 */
	public List<Map<String, Object>> findListPrecisionFormatCur(Map<String, Object> param) {
		return formatterManagerRepository.findListPrecisionFormatCur(param);
	}

	/**
	 * list all prec format 조회한다.
	 *
	 * @return the list< map< string, object>>
	 * @Date : 2019. 3. 5
	 * @Method Name : findListPrecisionFormat
	 */
	public List<Map<String, Object>> findListPrecisionFormat() {
		return formatterManagerRepository.findListPrecisionFormat();
	}


	/**
	 * 사용자 사용 포맷 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUserFormat(Map<String, Object> param) {
		return formatterManagerRepository.findListUserFormat(param);
	}
	
	/**
	 * 사용자 사용 포맷 조회 ( 조건 사용자 표현식 구분 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUserFormatByUserExpressionClass(Map<String, Object> param){
		return formatterManagerRepository.findListUserFormatByUserExpressionClass(param);
	}

	/**
	 * list user format dt 삭제한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 3. 5
	 * @Method Name : deleteListUserFormatInfo
	 */
	public ResultMap deleteListUserFormatInfo(Map<String, Object> param) {
		List<Map<String, Object>> deleteUserFormatInfoList = (List<Map<String, Object>>) param.get("deleteUserFormatInfoList");
		
		for(Map<String,Object> deleteUserFormatInfo: deleteUserFormatInfoList){
			deleteUserFormatLinkByExpressionId(deleteUserFormatInfo);
			deleteUserFormatInfoByUserExpressionId(deleteUserFormatInfo);
		}
		return ResultMap.SUCCESS();
	}

	public void deleteUserFormatInfoByUserExpressionId(Map<String, Object> deleteUserFormatInfo) {
		formatterManagerRepository.deleteUserFormatInfoByUserExpressionId(deleteUserFormatInfo);
	}

	/**
	 * user format by exp id 삭제한다.
	 *
	 * @param deleteUserFormatInfo the deleted
	 * @Date : 2019. 4. 8
	 * @Method Name : deleteUserFormatLinkByExpressionId
	 */
	public void deleteUserFormatLinkByExpressionId(Map<String, Object> deleteUserFormatInfo) {
		formatterManagerRepository.deleteUserFormatLinkByExpressionId(deleteUserFormatInfo);
	}

	/**
	 * user format dt 삭제한다.
	 *
	 * @param deleteUserFormatInfo the deleted
	 * @Date : 2019. 3. 5
	 * @Method Name : deleteUserFormatInfo
	 */
	public void deleteUserFormatLinkByUserExpressionClass(Map<String, Object> deleteUserFormatInfo) {
		formatterManagerRepository.deleteUserFormatLinkByUserExpressionClass(deleteUserFormatInfo);
	}

	/**
	 * list user format dt 저장한다.
	 *
	 * @param param the param
	 * @return the map< string, object>
	 * @Date : 2019. 3. 5
	 * @Method Name : saveListUserFormatInfo
	 */
	public ResultMap saveListUserFormatInfo(Map<String, Object> param) {
		List<Map<String, Object>> insertUserFormatInfoList = (List<Map<String, Object>>) param.get("insertUserFormatInfoList");
		List<Map<String, Object>> updateUserFormatInfoList = (List<Map<String, Object>>) param.get("updateUserFormatInfoList");
		
		this.insertListUserFormatInfo(insertUserFormatInfoList);
		
		this.updateListUserFormatInfo(updateUserFormatInfoList);
		
		return ResultMap.SUCCESS();
	}

	public void updateListUserFormatInfo(List<Map<String, Object>> updateUserFormatInfoList) {
		for(Map<String, Object> updated: updateUserFormatInfoList){
			updateUserFormatInfo(updated);
		}
	}

	public void insertListUserFormatInfo(List<Map<String, Object>> insertUserFormatInfoList) {
		for(Map<String, Object> inserted: insertUserFormatInfoList){
			insertUserFormatInfo(inserted);
		}
	}

	/**
	 * 사용자 로케일 포맷 수정
	 * @param updated
	 */
	public void updateUserFormatInfo(Map<String, Object> updated) {
		formatterManagerRepository.updateUserFormatInfo(updated);
	}

	/**
	 * 사용자 포맷 연결 정보 수정
	 * @param updated
	 */
	public void updateUserFormatLink(Map<String, Object> updated) {
		formatterManagerRepository.updateUserFormatLink(updated);
	}

	/**
	 * user format dt 입력한다.
	 *
	 * @param inserted the inserted
	 * @Date : 2019. 3. 5
	 * @Method Name : insertUserFormatInfo
	 */
	public void insertUserFormatInfo(Map<String, Object> inserted) {
		formatterManagerRepository.insertUserFormatInfo(inserted);
	}

	/**
	 * list use display format 조회한다.
	 *
	 * @return the list< map< string, object>>
	 * @Date : 2019. 4. 8
	 * @Method Name : findListUseDisplayFormat
	 */
	public List<Map<String, Object>> findListUseDisplayFormat() {
		return formatterManagerRepository.findListUseDisplayFormat();
	}

	/**
	 * list current user display format 조회한다.
	 *
	 * @return the list< map< string, object>>
	 * @Date : 2019. 4. 8
	 * @Method Name : findListCurrentUserDisplayFormat
	 */
	public List<Map<String, Object>> findListCurrentUserDisplayFormat() {
		return formatterManagerRepository.findListCurrentUserDisplayFormat();
	}

	/**
	 * current user 포맷 조회
	 * @return
	 */
	public List<Map<String, Object>> findCurrentUserFormatInfo() {
		return formatterManagerRepository.findCurrentUserFormatInfo();
	}

	/**
	 * Marge current user format info.
	 *
	 * @param data the data
	 */
	public void margeCurrentUserFormatLink(Map<String, Object> data) {
		Map<String,Object> currentUserFormatInfo = this.findCurrentUserFormatLinkByUserExpressionClass(data);

		//존재 할 경우, update
		if(MapUtils.isNotEmpty(currentUserFormatInfo)){
			this.updateCurrentUserFormatLinkByUserExpressionClass(data);

		}else{//존재하지 않을 경우, insert
			this.insertCurrentUserFormatLinkByUserExpressionClass(data);
		}
	}

	public void insertCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		formatterManagerRepository.insertCurrentUserFormatLinkByUserExpressionClass(data);
	}

	public void updateCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		formatterManagerRepository.updateCurrentUserFormatLinkByUserExpressionClass(data);
	}

	/**
	 * 로그인한 사용자의 format 조회 ( 조회 조건 user Expression Class )
	 * @param data
	 * @return
	 */
	public Map<String, Object> findCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		return formatterManagerRepository.findCurrentUserFormatLinkByUserExpressionClass(data);
	}

	/**
	 * user format info 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2019. 4. 8
	 * @Method Name : findUserFormatInfo
	 */
	public List<Map<String, Object>> findUserFormatInfo(Map<String, Object> param) {
		return formatterManagerRepository.findUserFormatInfo(param);
	}

	/**
	 * Marge user format info.
	 *
	 * @param data the data
	 */
	public void margeUserFormatLink(Map<String, Object> data) {
		//sqlSession.update(namespace+"margeUserFormatLink",data);
		Map<String,Object> userFormatInfo = this.findUserFormatLinkByUserExpressionClass(data);

		//존재 할 경우, update
		if(MapUtils.isNotEmpty(userFormatInfo)){
			this.updateUserFormatLink(data);
		}else{//존재하지 않을 경우, insert
			this.insertUserFormatLink(data);
		}
	}

	public void insertUserFormatLink(Map<String, Object> data) {
		formatterManagerRepository.insertUserFormatLink(data);
	}

	public Map<String, Object> findUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		return formatterManagerRepository.findUserFormatLinkByUserExpressionClass(data);
	}

	/**
	 * current user format info 삭제한다.
	 *
	 * @param data the data
	 * @Date : 2019. 4. 8
	 * @Method Name : deleteCurrentUserFormatLink
	 */
	public void deleteCurrentUserFormatLink(Map<String, Object> data) {
		formatterManagerRepository.deleteCurrentUserFormatLink(data);
	}
	

	public Map<String, Object> findDisplayFormat(String formatName) {
		return formatterManagerRepository.findDisplayFormat(formatName);
	}
}
