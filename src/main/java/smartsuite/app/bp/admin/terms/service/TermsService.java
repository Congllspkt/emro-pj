package smartsuite.app.bp.admin.terms.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.app.bp.admin.terms.repository.TermsRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

/**
 * 약관 관리 관련 처리하는 서비스 Class입니다.
 *  매뉴얼 유형(HTML : HTML, TX : Text, EML : Mail, APVL : Approval)
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class TermsService {

	@Inject
	TermsRepository termsRepository;

	/**
	 * 약관 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTerms(Map<String, Object> param) {
		return termsRepository.findListTerms(param);
	}

	/**
	 * 약관 목록 조회 Text mode template 콤보박스 조회용
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTermsTextModeTemplateComboboxItem(Map<String, Object> param) {
		return termsRepository.findListTermsTextModeTemplateComboboxItem(param);
	}

	/**
	 * 약관 상세 정보 저장
	 *
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : saveTerms
	 */
	public ResultMap saveTermsInfo(Map<String, Object> param) throws Exception{
		ResultMap resultMap = ResultMap.getInstance();
		Map<String, Object> termsDetailInfo = (param == null? Maps.newHashMap() : param);

		// 날짜 Validation Check
		validationTermsApplyStartDate(termsDetailInfo);

		String termsConditionsId = termsDetailInfo.getOrDefault("termcnd_uuid","") == null? "" : (String) termsDetailInfo.getOrDefault("termcnd_uuid","");

		if (StringUtils.isEmpty(termsConditionsId)) {
			// INSERT(메일 정보, 템플릿 본문) : 신규 or 차수증가
			termsDetailInfo.put("ctmpl_uuid", UUID.randomUUID().toString());
			termsDetailInfo.put("termcnd_uuid", UUID.randomUUID().toString());

			this.insertTermsTemplateContent(termsDetailInfo);
			this.insertTermsMasterInfo(termsDetailInfo);
			this.insertTermsTemplateContentMultiLang(termsDetailInfo);
		}else{
			// UPDATE(메일 정보, 템플릿 본문) : 정보 갱신
			this.updateTermsMasterInfo(termsDetailInfo);
			this.updateTermsTemplateContent(termsDetailInfo);
			if(this.existTermsTemplateBaseMultiLangInfo(param)){
				this.updateTermsTemplateContentMultiLang(termsDetailInfo);
			}else{
				this.insertTermsTemplateContentMultiLang(termsDetailInfo);
			}
		}

		resultMap.setResultData(termsDetailInfo);

		// 이전 차수 종료일 변경
		// 종료일 : 현재 차수의 시작일 - 1
		Map<String, Object> previousTerms = findPreviousTermsMasterInfo(termsDetailInfo);
		if(MapUtils.isNotEmpty(previousTerms)){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			Calendar cal = Calendar.getInstance();

			String applyStartDate = termsDetailInfo.getOrDefault("efct_st_dt","") == null? "" : (String) termsDetailInfo.getOrDefault("efct_st_dt","");
			if(StringUtils.isNotEmpty(applyStartDate)){
				Date date = dateFormat.parse(applyStartDate);
				cal.setTime(date);
				cal.add(Calendar.DATE, -1);
				previousTerms.put("efct_exp_dt", dateFormat.format(cal.getTime()));
				this.updateTermsMasterInfo(previousTerms);
			}else {
				throw new CommonException("STD.ADM1051");// 유효하지 않은 적용기간입니다.
			}
		}

		return resultMap;
	}

	public boolean existTermsTemplateBaseMultiLangInfo(Map<String, Object> param) {
		return (termsRepository.getCountTermsTemplateBaseMultiLangInfo(param) > 0);
	}

	private void updateTermsTemplateContentMultiLang(Map<String, Object> termsDetailInfo) {
		termsRepository.updateTermsTemplateContentMultiLang(termsDetailInfo);
	}

	private void insertTermsTemplateContentMultiLang(Map<String, Object> termsDetailInfo) {
		termsRepository.insertTermsTemplateContentMultiLang(termsDetailInfo);
	}

	/**
	 * 약관 템플릿 내용 추가
	 * @param termsDetailInfo
	 */
	public void insertTermsTemplateContent(Map<String, Object> termsDetailInfo) {
		termsRepository.insertTermsTemplateContent(termsDetailInfo);
	}

	/**
	 * 약관 마스터 정보 수정
	 * @param termsDetailInfo
	 */
	public void updateTermsMasterInfo(Map<String, Object> termsDetailInfo) {
		termsRepository.updateTermsMasterInfo(termsDetailInfo);
	}

	/**
	 * 약관 템플릿 내용 수정
	 * @param termsDetailInfo
	 */
	public void updateTermsTemplateContent(Map<String, Object> termsDetailInfo) {
		termsRepository.updateTermsTemplateContent(termsDetailInfo);
	}


	/**
	 * 약관 마스터 정보 추가
	 * @param termsDetailInfo
	 */
	public void insertTermsMasterInfo(Map<String, Object> termsDetailInfo) {
		termsRepository.insertTermsMasterInfo(termsDetailInfo);
	}

	/**
	 * 약관 시작일자 유효성 체크
	 *
	 * @param param the param
	 * @return Map
	 * @Date : 2019. 05. 08
	 * @Method Name : checkValidDate
	 */
	private void validationTermsApplyStartDate(Map<String, Object> curTerms){
		Map<String, Object> previousTerms = Maps.newHashMap();
		Map<String, Object> postTerms = Maps.newHashMap();

		String termsConditionsId = curTerms.getOrDefault("termcnd_uuid","") == null? "" : (String) curTerms.getOrDefault("termcnd_uuid","");
		Integer currentStartDate  = Integer.parseInt((String) curTerms.getOrDefault("efct_st_dt",""));
		Integer currentEndDate    = Integer.parseInt((String) curTerms.getOrDefault("efct_exp_dt",""));

		if (StringUtils.isEmpty(termsConditionsId)) {
			previousTerms = findLastRevisionTermsMasterAndContentMultiLangByTermsConditionClassCode(curTerms);
		}else{
			previousTerms = findPreviousTermsMasterInfo(curTerms);
			postTerms = findPostTermsMasterInfo(curTerms);

			// 상위 차수 존재할 경우
			if(MapUtils.isNotEmpty(postTerms)){
				Integer postStartDate = Integer.parseInt((String)postTerms.getOrDefault("efct_st_dt",""));
				if(currentStartDate.compareTo(postStartDate) > 0 || currentEndDate.compareTo(postStartDate) >= 0 ){
					throw new CommonException("STD.ADM1049"); // 유효하지 않은 적용기간입니다.<br/>상위차수의 시작일을 참고하세요.
				}
			}
		}
		
		if(MapUtils.isNotEmpty(previousTerms)){
			Integer prevStartDate = Integer.parseInt((String) previousTerms.getOrDefault("efct_st_dt",""));
			// 이전 차수/현재 차수 비교
			if(prevStartDate.compareTo(currentStartDate) >= 0  || currentStartDate.compareTo(currentEndDate) > 0){
				throw new CommonException("STD.ADM1050"); // 유효하지 않은 적용기간입니다.<br/>이전차수의 적용기간을 참고하세요.
			}
		}
	}

	private Map<String, Object> findLastRevisionTermsMasterAndContentMultiLangByTermsConditionClassCode(Map<String, Object> curTerms) {
		return termsRepository.findLastRevisionTermsMasterAndContentMultiLangByTermsConditionClassCode(curTerms);
	}


	/**
	 * 약관 삭제
	 * @param param
	 * @return
	 */
	public ResultMap deleteTermsInfo(Map param) {
		List<Map<String, Object>> deleteTermsList = (List<Map<String, Object>>) param.getOrDefault("deleteTermsList", Lists.newArrayList());

		for (Map<String, Object> termsInfo : deleteTermsList) {
			// 동시성 체크
			String existsTemrsMaster = this.existsTermsMasterByTermsConditionId(termsInfo);
			if("Y".equals(existsTemrsMaster)){
				// 담당자 동의 약관 삭제
				this.deleteChargeTermsAgreeInfoByTermsConditionsId(termsInfo);
				// 약관 마스터 정보 삭제
				this.deleteTermsMasterByTemplateIdAndTermsConditionsId(termsInfo);
				// 메일 템플릿 삭제
				this.deleteTermsTemplateByTemplateId(termsInfo);
			}else{
				throw new CommonException(ErrorCode.NOT_EXIST);
			}
		}

		return ResultMap.SUCCESS();
	}

	/**
	 * 약관 템플릿 삭제 ( 조회조건 템플릿 id )
	 * @param termsInfo
	 */
	public void deleteTermsTemplateByTemplateId(Map<String, Object> termsInfo) {
		termsRepository.deleteTermsTemplateByTemplateId(termsInfo);
	}

	/**
	 * 약관 마스터 정보 삭제 ( 조회조건 템플릿 id / 약관 id )
	 * @param termsInfo
	 */
	public void deleteTermsMasterByTemplateIdAndTermsConditionsId(Map<String, Object> termsInfo) {
		termsRepository.deleteTermsMasterByTemplateIdAndTermsConditionsId(termsInfo);
	}

	/**
	 * 담당자 동의 약관 삭제  ( 조회조건 약관 아이디 )
	 * @param termsInfo
	 */
	public void deleteChargeTermsAgreeInfoByTermsConditionsId(Map<String, Object> termsInfo) {
		termsRepository.deleteChargeTermsAgreeInfoByTermsConditionsId(termsInfo);
	}


	/**
	 * 약관 정보 존재여부 체크 ( "Y"/"N" )
	 * @param termsInfo
	 * @return
	 */
	public String existsTermsMasterByTermsConditionId(Map<String, Object> termsInfo) {
		return termsRepository.existsTermsMasterByTermsConditionId(termsInfo);
	}

	/**
	 * 약관 상세 정보 조회 ( 마스터&상세 조회 조회조건 약관 id )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTermsDetailInfoByTermsConditionsId(Map<String, Object> param) {
		return termsRepository.findTermsDetailInfoByTermsConditionsId(param);
	}

	/**
	 * 약관 마지막 Revision 차수 상세 조회 ( 조회조건 약관 구분 코드 )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findLastRevisionTermsMasterAndContentByTermsConditionClassCode(Map<String, Object> param) {
		return termsRepository.findLastRevisionTermsMasterAndContentByTermsConditionClassCode(param);
	}

	/**
	 * 이전 차수 약관 마스터 정보 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> findPreviousTermsMasterInfo(Map<String, Object> param) {
		return termsRepository.findPreviousTermsMasterInfo(param);
	}


	/**
	 * 다음 차수 약관 마스터 정보 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> findPostTermsMasterInfo(Map<String, Object> param) {
		return termsRepository.findPostTermsMasterInfo(param);
	}

	/**
	 * 담당자 약관 동의여부 체크 ( map "y" "n" )
	 * @param param
	 * @return
	 */
	public Map existChargeTermsAgreeInfoByTermsConditionsAgreeId(Map<String, Object> param) {
		return termsRepository.existChargeTermsAgreeInfoByTermsConditionsAgreeId(param);
	}

	/**
	 * 담당자 신규 등록된 유효한 약관 템플릿 정보 리스트 조회
	 * @param param
	 * @return
	 */
	public List findListNewRegistrationApplyChargeTermsTemplateInfo(Map<String, Object> param) {
		return termsRepository.findListNewRegistrationApplyChargeTermsTemplateInfo(param);
	}


	/**
	 * 협력사 담당자 약관 추가 및 수정
	 *
	 * @param param
	 * @param remoteAddr
	 * @return
	 */
	public ResultMap saveChargeTermsAgree(Map param, String loginIp) {
		List<Map<String, Object>> termsList = (List<Map<String, Object>>) param.getOrDefault("termsList",Lists.newArrayList());

		Map<String, Object> compareTerms = existChargeTermsAgreeInfoByTermsConditionsAgreeId(param);
		if("Y".equals((String) compareTerms.getOrDefault("terms_popup_yn",""))){
			this.insertListChargeTermsAgreeInfo(termsList,loginIp);
		} else {
			throw new CommonException("STD.N4000"); // 해당건은 이미 처리가 완료되었습니다.
		}
		return ResultMap.SUCCESS();
	}

	/**
	 * 담당자 약관 정보 리스트 추가
	 * @param termsList
	 * @param loginIp
	 */
	public void insertListChargeTermsAgreeInfo(List<Map<String, Object>> termsList, String loginIp) {
		for(Map<String, Object> termsInfo : termsList){
			String chargeTermsAgreeId = UUID.randomUUID().toString();
			termsInfo.put("login_ip", loginIp);
			termsInfo.put("termcnd_cnst_uuid", chargeTermsAgreeId);
			this.insertChargeTermsAgreeInfo(termsInfo);
		}
	}

	/**
	 * 담당자 약관 정보 추가
	 * @param termsInfo
	 */
	public void insertChargeTermsAgreeInfo(Map<String, Object> termsInfo) {
		termsRepository.insertChargeTermsAgreeInfo(termsInfo);
	}

	/**
	 * 이용약관 마스터 조회 ( 조회조건 현재 날짜까지 유효한 상태 )
	 * @param param
	 * @return
	 */
	public Map findValidTermsMasterInfoByTermsConditions(Map<String, Object> param) {
		return termsRepository.findValidTermsMasterInfoByTermsConditions(param);
	}

	/**
	 * 담당자 동의 약관 삭제 ( 조회조건 템플릿 base id )
	 * @param param
	 */
	public void deleteChargeTermsAgreeInfoByTemplateBaseId(Map<String, Object> param) {
		termsRepository.deleteChargeTermsAgreeInfoByTemplateBaseId(param);
	}

	/**
	 * 이용 약관 마스터 삭제
	 * @param param
	 */
	public void deleteTermsMasterByTemplateBaseId(Map<String, Object> param) {
		termsRepository.deleteTermsMasterByTemplateBaseId(param);
	}

	public Map findTermsMultiLangDetailInfoByTermsConditionsId(Map param) {
		return termsRepository.findTermsMultiLangDetailInfoByTermsConditionsId(param);
	}
}
