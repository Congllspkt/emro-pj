package smartsuite.app.bp.admin.template.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class TemplateRepository {
	@Inject
	private SqlSession sqlSession;


	/* NAMESPACE*/
	private static final String NAMESPACE = "template.";

	/**
	 * 템플릿 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTemplate(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListTemplate", param);
	}

	/**
	 * 템플리 정보 조회 ( 조회조건 템플릿 기본 아이디 & 템플릿 구분 )
	 * @param param
	 * @return
	 */
	public Map findTemplateInfoByTemplateClassAndTemplateBaseId(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findTemplateInfoByTemplateClassAndTemplateBaseId", param);
	}

	/**
	 * 템플릿 삭제 시 템플릿 본문내용 삭제 ( 조회조건 template base id )
	 * @param param
	 */
	public void deleteListTemplateContentByTemplateBaseId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteListTemplateContentByTemplateBaseId", param);
	}

	/**
	 * 템플릿 기초 정보 삭제
	 * @param param
	 */
	public void deleteTemplateBaseInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteTemplateBaseInfo", param);
	}

	/**
	 * 템플릿 기초 개수 조회
	 * @param param
	 * @return
	 */
	public int getCountTemplateBaseInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+ "getCountTemplateBaseInfo", param);
	}

	/**
	 * 템플릿 기초 정보 추가
	 * @param param
	 */
	public void insertTemplateBaseInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertTemplateBaseInfo", param);
	}

	/**
	 * 템플릿 기초 정보 수정
	 * @param param
	 */
	public void updateTemplateBaseInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateTemplateBaseInfo", param);
	}

	/**
	 * 메일 설정 키를 사용하는 템플릿 본문 정보 검색
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTemplateContentByMailSetKey(String mailKey) {
		return sqlSession.selectOne(NAMESPACE + "findTemplateContentByMailSetKey", mailKey);
	}

	/**
	 * 결제 구분 코드를 사용하는 템플릿 본문 정보 검색
	 * @param param
	 * @return
	 */
	public Map<String, Object> findTemplateContentByApprovalTypeCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findTemplateContentByApprovalTypeCode", param);
	}

	/**
	 * 템플릿 기초정보 조회 ( 조회조건 template base id )
	 * @param param
	 * @return
	 */
	public Map findTemplateBaseInfoByTemplateBaseId(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findTemplateBaseInfoByTemplateBaseId", param);
	}


	/**
	 * 결재 템플릿 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListApprovalTemplate(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListApprovalTemplate", param);
	}

	
	/**
	 * 결재 템플릿 상세 조회 ( 조회 조건 결재구분코드 )
	 * @param param
	 * @return
	 */
	public Map<String, Object> findApprovalTemplateByApprovalTypeCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findApprovalTemplateByApprovalTypeCode", param);
	}
	

	/**
	 * 콤보 박스 조회용 결재 템플릿 ( 조회조건 결재 템플릿 구분 )
	 * @param param
	 * @return
	 */
	public List findListApprovalTemplateComboboxItemByApprovalTemplateClass(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListApprovalTemplateComboboxItemByApprovalTemplateClass", param);
	}

	/**
	 * 템플릿 결재 연결 설정 개수 조회 ( 조회조건 결재 구분 코드 )
	 * @param param
	 * @return
	 */
	public int getCountApprovalSetForTemplateByApprovalTypeCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountApprovalSetForTemplateByApprovalTypeCode", param);
	}

	/**
	 * 템플릿 결재 연결 설정을 수정
	 * @param param
	 */
	public void updateApprovalSetForTemplate(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalSetForTemplate", param);
	}

	/**
	 * 결재 템플릿 내용 수정
	 * @param param
	 */
	public void updateApprovalTemplateContent(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalTemplateContent", param);
	}

	/**
	 * 결재 템플릿 내용을 저장한다.
	 * @param param
	 */
	public void insertApprovalTemplateContent(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertApprovalTemplateContent", param);
	}

	/**
	 * 템플릿 결재 연결 설정 등록
	 * @param param
	 */
	public void insertApprovalSetForTemplate(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertApprovalSetForTemplate", param);
	}

	/**
	 * 메일용 템플릿 양식 삭제
	 * @param param
	 */
	public void deleteMailTemplateInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteMailTemplateInfo", param);
	}

	public void insertTemplateBaseInfoMultiLang(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertTemplateBaseInfoMultiLang", param);
	}

	public void updateTemplateBaseInfoMultiLang(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateTemplateBaseInfoMultiLang", param);
	}

	public void insertApprovalTemplateContentMultiLang(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertApprovalTemplateContentMultiLang", param);
	}

	public void updateApprovalTemplateContentMultiLang(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateApprovalTemplateContentMultiLang", param);
	}

	public Map findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId(Map templateData) {
		return sqlSession.selectOne(NAMESPACE + "findTemplateInfoByTemplateClassMultiLangAndTemplateBaseId", templateData);
	}

	public int getCountTemplateBaseMultiLangInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+ "getCountTemplateBaseMultiLangInfo", param);
	}

	public int getCountApprovalSetForMultiLangTemplateByApprovalTypeCode(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountApprovalSetForMultiLangTemplateByApprovalTypeCode", param);
	}

	public void deleteListTemplateBaseMultiLangInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteListTemplateBaseMultiLangInfo",param);
	}
}
