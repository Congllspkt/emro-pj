package smartsuite.app.bp.admin.code.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class CommonCodeRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "common-code.";

	/**
	 *  그룹코드(CCD) 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListGroupCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListGroupCode", param);
	}

	/**
	 * 그룹 코드(CCD) 수정
	 * @param groupCodeInfo
	 */
	public void updateGroupCodeInfo(Map groupCodeInfo) {
		sqlSession.update(NAMESPACE + "updateGroupCodeInfo", groupCodeInfo);
	}

	/**
	 * 그룹 코드(CCD) 추가
	 * @param groupCodeInfo
	 */
	public void insertGroupCode(Map groupCodeInfo) {
		sqlSession.insert(NAMESPACE + "insertGroupCodeInfo", groupCodeInfo);
	}

	/**
	 * 그룹 코드 삭제
	 * @param row
	 */
	public void deleteGroupCodeInfo(Map row) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeInfo", row);
	}

	/**
	 * 상세코드속성값(CCD_CSTR_CND_VAL)을 삭제한다.
	 * @param param
	 */
	public void deleteGroupCodeAttributeValue(Map param) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeValue", param);
	}

	/**
	 * CCD_CSTR_CND 그룹코드 속성 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListGroupCodeAttribute(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListGroupCodeAttribute", param);
	}

	/**
	 * 그룹코드 속성 수정
	 * @param row
	 */
	public void updateGroupCodeAttributeInfo(Map row) {
		sqlSession.update(NAMESPACE + "updateGroupCodeAttributeInfo", row);
	}

	/**
	 * 그룹코드 속성 추가
	 * @param insertGroupCodeAttrInfo
	 */
	public void insertGroupCodeAttributeInfo(Map insertGroupCodeAttrInfo) {
		sqlSession.insert(NAMESPACE + "insertGroupCodeAttributeInfo", insertGroupCodeAttrInfo);
	}

	/**
	 * 그룹 코드 속성(CCD_CSTR_CND) DELETE GROUP CD & AttributeCode
	 * @param deleteGroupCodeAttribute
	 */
	public void deleteGroupCodeAttributeInfoByAttrCode(Map deleteGroupCodeAttribute) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeInfoByAttrCode", deleteGroupCodeAttribute);
	}

	/**
	 * 상세 코드 속성 값(CCD_CSTR_CND_VAL) DELETE by ATTR_CD
	 * @param param
	 */
	public void deleteGroupCodeAttributeValueByAttributeCode(Map param) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeValueByAttributeCode", param);
	}

	/**
	 * 그룹 코드 속성(CCD_CSTR_CND) DELETE
	 * @param groupCodeInfo
	 */
	public void deleteGroupCodeAttributeInfo(Map groupCodeInfo) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeInfo", groupCodeInfo);
	}

	/**
	 * 상세코드 목록(DTLCD & ESACDDL) 조회
	 * @param param
	 */
	public List<Map<String, Object>> findListGroupDetailCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListGroupDetailCode", param);
	}

	/**
	 * 그룹코드 속성(CCD_CSTR_CND&CCD_CSTR_CND_VAL) 목록 조회
	 * @param groupDetailCodeInfo
	 */
	public List<Map<String, Object>> findListGroupCodeAttributeAndAttrValue(Map groupDetailCodeInfo) {
		return sqlSession.selectList(NAMESPACE + "findListGroupCodeAttributeAndAttrValue", groupDetailCodeInfo);
	}

	/**
	 * 상세 코드 테이블(DTLCD) 수정
	 * @param updateDetailCode
	 */
	public void updateGroupCodeDetailInfo(Map updateDetailCode) {
		sqlSession.update(NAMESPACE + "updateGroupCodeDetailInfo", updateDetailCode);
	}

	/**
	 * 상세 코드 명(ESACDDL) 수정
	 * @param updateDetailCode
	 */
	public void updateGroupCodeDetailName(Map updateDetailCode) {
		sqlSession.update(NAMESPACE + "updateGroupCodeDetailName", updateDetailCode);
	}

	/**
	 * 상세 코드 속성 값(ESADATA) 조회
	 * @param updateDetailCode
	 */
	public Map findDetailCodeAttributeValue(Map updateDetailCode) {
		return sqlSession.selectOne(NAMESPACE + "findDetailCodeAttributeValue", updateDetailCode);
	}

	/**
	 * 상세 코드 속성 값(CCD_CSTR_CND_VAL) 수정
	 * @param updateDetailCode
	 */
	public void updateGroupCodeAttributeValue(Map updateDetailCode) {
		sqlSession.update(NAMESPACE + "updateGroupCodeAttributeValue", updateDetailCode);
	}

	/**
	 * 상세코드속성값(CCD_CSTR_CND_VAL)을 입력한다.
	 * @param groupCodeAttribute
	 */
	public void insertGroupCodeAttributeValue(Map groupCodeAttribute) {
		sqlSession.insert(NAMESPACE + "insertGroupCodeAttributeValue", groupCodeAttribute);
	}

	/**
	 * 상세코드 조회 (DTLCD)
	 * @param insertDetailCode
	 * @return
	 */
	public Map<String, Object> findGroupCodeDetailInfo(Map insertDetailCode) {
		return sqlSession.selectOne(NAMESPACE + "findGroupCodeDetailInfo",insertDetailCode);
	}

	/**
	 * 상세 코드 추가 (DTLCD)
	 * @param insertDetailCode
	 */
	public void insertGroupCodeDetailInfo(Map insertDetailCode) {
		sqlSession.insert(NAMESPACE + "insertGroupCodeDetailInfo",insertDetailCode);
	}

	/**
	 * 상세 코드 update / insert merge
	 * @param insertDetailCode
	 */
	public void mergeGroupCodeDetailInfo(Map insertDetailCode) {
		sqlSession.insert(NAMESPACE + "mergeGroupCodeDetailInfo", insertDetailCode);
	}

	/**
	 * 상세 코드 명(ESACDDL) 추가
	 * @param insertDetailCode
	 */
	public void insertGroupCodeDetailName(Map insertDetailCode) {
		sqlSession.insert(NAMESPACE + "insertGroupCodeDetailName", insertDetailCode);
	}

	/**
	 * 상세 코드 명 기본 이름 추가 ESACDDL
	 * @param insertDetailCode
	 */
	public void insertDefaultGroupCodeDetailName(Map insertDetailCode) {
		sqlSession.insert(NAMESPACE + "insertDefaultGroupCodeDetailName", insertDetailCode);
	}

	/**
	 * 상세 코드 속성 값 삭제 CCD_CSTR_CND_VAL
	 * @param detailCode
	 */
	public void deleteGroupCodeAttributeValueByDetailCode(Map detailCode) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeValueByDetailCode", detailCode);
	}

	/**
	 * 상세코드 삭제 ESACDDL
	 * @param detailGroupCode
	 */
	public void deleteGroupCodeDetailNameByDetailCode(Map detailGroupCode) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeDetailNameByDetailCode", detailGroupCode);
	}

	/**
	 * 상세 코드 DTLCD
	 * @param detailCode
	 */
	public void deleteGroupCodeDetailInfoByDetailCode(Map detailCode) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeDetailInfoByDetailCode", detailCode);
	}

	/**
	 * 상세 코드 명 삭제
	 * @param param
	 */
	public void deleteGroupCodeDetailName(Map param) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeDetailName", param);
	}


	/**
	 * 상세 코드 속성 명 삭제 ( 조건 attr code / detail code )
	 * @param param
	 */
	public void deleteGroupCodeAttributeValueByAttributeCodeAndDetailCode(Map param) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeAttributeValueByAttributeCodeAndDetailCode", param);
	}

	/**
	 * 그룹 코드 정보 삭제 DTLCD
	 * @param param
	 */
	public void deleteGroupCodeDetailInfo(Map param) {
		sqlSession.delete(NAMESPACE + "deleteGroupCodeDetailInfo", param);
	}
}
