package smartsuite.app.bp.admin.formatter.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class FormatterManagerRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	private static final String NAMESPACE = "formatter.";


	public List<Map<String, Object>> findListGroupCodeByNoFormatter(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListGroupCodeByNoFormatter",param);
	}

	/**
	 * 자리 수 관련 그룹 코드를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPrecisionGroupCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListPrecisionGroupCode",param);
	}

	/**
	 * 자리수 관련 상세 코드를 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPrecisionDetailCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListPrecisionDetailCode",param);
	}

	/**
	 * 자리수 관련 존재 여부 조회 ( 조회 시 String List로 받게 처리 함)
	 * @param param
	 * @return
	 */
	public List<String> findListExistPrecisionGroupCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListExistPrecisionGroupCode",param);
	}

	public void deletePrecisionGroupCode(Map<String, Object> deletePrecisionGroupCode) {
		sqlSession.delete(NAMESPACE +"deletePrecisionGroupCode",deletePrecisionGroupCode);
	}

	public void insertPrecisionGroupCode(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertPrecisionGroupCode",param);
	}

	public void deletePrecisionDetailCodeByGroupCode(Map<String, Object> deletePrecisionDetailCodeInfo) {
		sqlSession.delete(NAMESPACE +"deletePrecisionDetailCodeByGroupCode",deletePrecisionDetailCodeInfo);
	}

	public void insertPrecisionDetailCode(Map<String, Object> insertPrecisionInfo) {
		sqlSession.insert(NAMESPACE +"insertPrecisionDetailCode",insertPrecisionInfo);
	}

	public void updatePrecisionDetailCode(Map<String, Object> updatePrecisionInfo) {
		sqlSession.update(NAMESPACE +"updatePrecisionDetailCode",updatePrecisionInfo);
	}

	public List<Map<String, Object>> findListDisplayFormat(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListDisplayFormat",param);
	}

	public List<Map<String, Object>> findListDisplayFormatDetail(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListDisplayFormatDetail",param);
	}

	public int getCountDisplayFormatName(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE +"getCountDisplayFormatName",param);
	}
	
	public void updateDisplayPrecisionFormat(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateDisplayPrecisionFormat",param);
	}

	public void insertDisplayFormat(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertDisplayFormat",param);
	}

	public void updateDisplayFormat(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateDisplayFormat",param);
	}

	public void deleteDisplayFormatDetailByFormatName(Map<String, Object> deleted) {
		sqlSession.delete(NAMESPACE +"deleteDisplayFormatDetailByFormatName",deleted);
	}

	public void deleteDisplayFormatGroup(Map<String, Object> deleted) {
		sqlSession.delete(NAMESPACE +"deleteDisplayFormatGroup",deleted);
	}

	public void updateDisplayFormatDetail(Map<String, Object> updated) {
		sqlSession.update(NAMESPACE +"updateDisplayFormatDetail",updated);
	}

	public void insertDisplayFormatDetail(Map<String, Object> inserted) {
		sqlSession.insert(NAMESPACE +"insertDisplayFormatDetail",inserted);
	}

	public void deleteDisplayFormatDetail(Map<String, Object> deleted) {
		sqlSession.delete(NAMESPACE +"deleteDisplayFormatDetail",deleted);
	}

	public List<Map<String, Object>> findListPrecisionFormatByGroupCode(String precGrpCd) {
		return sqlSession.selectList(NAMESPACE +"findListPrecisionFormatByGroupCode",precGrpCd);
	}

	public List<Map<String, Object>> findListPrecisionFormatCur(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListPrecisionFormatCur",param);
	}

	public List<Map<String, Object>> findListPrecisionFormat() {
		return sqlSession.selectList(NAMESPACE +"findListPrecisionFormat");
	}

	public List<Map<String, Object>> findListUserFormat(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListUserFormat",param);
	}

	public List<Map<String, Object>> findListUserFormatByUserExpressionClass(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findListUserFormatByUserExpressionClass",param);
	}

	public void deleteUserFormatLinkByUserExpressionClass(Map<String, Object> deleteUserFormatInfo) {
		sqlSession.delete(NAMESPACE +"deleteUserFormatLinkByUserExpressionClass",deleteUserFormatInfo);
	}

	public void deleteUserFormatLinkByExpressionId(Map<String, Object> deleteUserFormatInfo) {
		sqlSession.delete(NAMESPACE +"deleteUserFormatLinkByExpressionId",deleteUserFormatInfo);
	}

	public void updateUserFormatInfo(Map<String, Object> updated) {
		sqlSession.update(NAMESPACE +"updateUserFormatInfo",updated);
	}

	public void updateUserFormatLink(Map<String, Object> updated) {
		sqlSession.update(NAMESPACE +"updateUserFormatLink",updated);
	}

	public void insertUserFormatInfo(Map<String, Object> inserted) {
		sqlSession.insert(NAMESPACE +"insertUserFormatInfo",inserted);
	}

	public void insertUserFormatLink(Map<String, Object> data) {
		sqlSession.insert(NAMESPACE +"insertUserFormatLink",data);
	}

	public List<Map<String, Object>> findListUseDisplayFormat() {
		return sqlSession.selectList(NAMESPACE +"findListUseDisplayFormat");
	}

	public List<Map<String, Object>> findListCurrentUserDisplayFormat() {
		return sqlSession.selectList(NAMESPACE +"findListCurrentUserDisplayFormat");
	}

	public List<Map<String, Object>> findCurrentUserFormatInfo() {
		return sqlSession.selectList(NAMESPACE +"findCurrentUserFormatInfo");
	}

	public Map<String, Object> findCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		return sqlSession.selectOne(NAMESPACE +"findCurrentUserFormatLinkByUserExpressionClass",data);
	}

	public void updateCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		sqlSession.update(NAMESPACE +"updateCurrentUserFormatLinkByUserExpressionClass",data);
	}

	public void insertCurrentUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		sqlSession.insert(NAMESPACE +"insertCurrentUserFormatLinkByUserExpressionClass",data);
	}

	public List<Map<String, Object>> findUserFormatInfo(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE +"findUserFormatInfo",param);
	}

	public Map<String, Object> findUserFormatLinkByUserExpressionClass(Map<String, Object> data) {
		return sqlSession.selectOne(NAMESPACE +"findUserFormatLinkByUserExpressionClass",data);
	}

	public void deleteCurrentUserFormatLink(Map<String, Object> data) {
		sqlSession.delete(NAMESPACE +"deleteCurrentUserFormatLink",data);
	}

	public Map<String,Object> findDisplayFormat(String formatName) {
		return sqlSession.selectOne(NAMESPACE +"findDisplayFormat", formatName);
	}

	public void deleteUserFormatInfoByUserExpressionId(Map<String, Object> deleteUserFormatInfo) {
		sqlSession.delete(NAMESPACE +"deleteUserFormatInfoByUserExpressionId",deleteUserFormatInfo);
	}

}



