package smartsuite.app.bp.admin.terms.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class TermsRepository {
	/** The sql session. */
	@Inject
	SqlSession sqlSession;

	/** The namespace. */
	private static final String NAMESPACE = "terms.";


	/**
	 * 약관 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTerms(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListTerms",param);
	}

	/**
	 * 약관 목록 조회 Text mode template 콤보박스 조회용
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTermsTextModeTemplateComboboxItem(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListTermsTextModeTemplateComboboxItem", param);
	}

	/**
	 * 약관 마스터 정보 추가
	 * @param termsDetailInfo
	 */
	public void insertTermsMasterInfo(Map<String, Object> termsDetailInfo) {
		sqlSession.insert(NAMESPACE + "insertTermsMasterInfo", termsDetailInfo);
	}

	/**
	 * 약관 마스터 정보 수정
	 * @param termsDetailInfo
	 */
	public void updateTermsMasterInfo(Map<String, Object> termsDetailInfo) {
		sqlSession.update(NAMESPACE + "updateTermsMasterInfo", termsDetailInfo);
	}

	/**
	 * 약관 정보 마스터 정보 존재여부 확인 ( 조회조건 약관 ID )
	 * @param termsInfo
	 * @return
	 */
	public String existsTermsMasterByTermsConditionId(Map<String, Object> termsInfo) {
		return sqlSession.selectOne(NAMESPACE+"existsTermsMasterByTermsConditionId", termsInfo);
	}

	/**
	 * 담당자 동의 약관 삭제  ( 조회조건 약관 아이디 )
	 * @param termsInfo
	 */
	public void deleteChargeTermsAgreeInfoByTermsConditionsId(Map<String, Object> termsInfo) {
		sqlSession.update(NAMESPACE + "deleteChargeTermsAgreeInfoByTermsConditionsId", termsInfo);
	}

	/**
	 *  약관 마스터 정보 삭제 ( 조회조건 템플릿 id / 약관 id )
	 * @param termsInfo
	 */
	public void deleteTermsMasterByTemplateIdAndTermsConditionsId(Map<String, Object> termsInfo) {
		sqlSession.update(NAMESPACE + "deleteTermsMasterByTemplateIdAndTermsConditionsId", termsInfo);
	}

	/**
	 * 약관 템플릿 삭제 ( 조회조건 템플릿 id )
	 * @param termsInfo
	 */
	public void deleteTermsTemplateByTemplateId(Map<String, Object> termsInfo) {
		sqlSession.update(NAMESPACE + "deleteTermsTemplateByTemplateId", termsInfo);
	}

	/**
	 * 약관 상세 정보 조회 ( 마스터&상세 조회 조회조건 약관 id )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTermsDetailInfoByTermsConditionsId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findTermsDetailInfoByTermsConditionsId", param);
	}

	/**
	 * 약관 마지막 Revision 차수 상세 조회 ( 조회조건 약관 구분 코드 )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findLastRevisionTermsMasterAndContentByTermsConditionClassCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findLastRevisionTermsMasterAndContentByTermsConditionClassCode", param);
	}

	/**
	 * 이전 차수 약관 마스터 정보 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> findPreviousTermsMasterInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findPreviousTermsMasterInfo", param);
	}

	/**
	 * 다음 차수 약관 마스터 정보 조회
	 * @param param
	 * @return
	 */
	public Map<String, Object> findPostTermsMasterInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findPostTermsMasterInfo", param);
	}

	/**
	 * 담당자 약관 동의여부 체크 ( map "y" "n" )
	 * @param param
	 * @return
	 */
	public Map existChargeTermsAgreeInfoByTermsConditionsAgreeId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "existChargeTermsAgreeInfoByTermsConditionsAgreeId", param);
	}

	/**
	 * 담당자 신규 등록된 유효한 약관 템플릿 정보 리스트 조회
	 * @param param
	 * @return
	 */
	public List findListNewRegistrationApplyChargeTermsTemplateInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListNewRegistrationApplyChargeTermsTemplateInfo",param);
	}

	/**
	 * 담당자 약관 정보 추가
	 * @param termsInfo
	 */
	public void insertChargeTermsAgreeInfo(Map<String, Object> termsInfo) {
		sqlSession.insert(NAMESPACE+"insertChargeTermsAgreeInfo", termsInfo);
	}

	/**
	 * 이용약관 마스터 조회 ( 조회조건 현재 날짜까지 유효한 상태 )
	 * @param param
	 * @return
	 */
	public Map findValidTermsMasterInfoByTermsConditions(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findValidTermsMasterInfoByTermsConditions", param);
	}

	/**
	 * 담당자 동의 약관 삭제 ( 조회조건 템플릿 base id )
	 * @param param
	 */
	public void deleteChargeTermsAgreeInfoByTemplateBaseId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteChargeTermsAgreeInfoByTemplateBaseId", param);
	}

	/**
	 * 이용 약관 마스터 삭제
	 * @param param
	 */
	public void deleteTermsMasterByTemplateBaseId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTermsMasterByTemplateBaseId", param);
	}

	/**
	 * 약관 템플릿 내용 추가
	 * @param termsDetailInfo
	 */
	public void insertTermsTemplateContent(Map<String, Object> termsDetailInfo) {
		sqlSession.insert(NAMESPACE + "insertTermsTemplateContent", termsDetailInfo);
	}

	/**
	 * 약관 템플릿 내용 수정
	 * @param termsDetailInfo
	 */
	public void updateTermsTemplateContent(Map<String, Object> termsDetailInfo) {
		sqlSession.update(NAMESPACE + "updateTermsTemplateContent",termsDetailInfo);
	}

	public void insertTermsTemplateContentMultiLang(Map<String, Object> termsDetailInfo) {
		sqlSession.insert(NAMESPACE + "insertTermsTemplateContentMultiLang", termsDetailInfo);
	}

	public void updateTermsTemplateContentMultiLang(Map<String, Object> termsDetailInfo) {
		sqlSession.update(NAMESPACE + "updateTermsTemplateContentMultiLang",termsDetailInfo);
	}

	public int getCountTermsTemplateBaseMultiLangInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountTermsTemplateBaseMultiLangInfo", param);
	}

	public Map findTermsMultiLangDetailInfoByTermsConditionsId(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findTermsMultiLangDetailInfoByTermsConditionsId", param);
	}

	public Map<String, Object> findLastRevisionTermsMasterAndContentMultiLangByTermsConditionClassCode(Map<String, Object> curTerms) {
		return sqlSession.selectOne(NAMESPACE + "findLastRevisionTermsMasterAndContentMultiLangByTermsConditionClassCode", curTerms);
	}
}
