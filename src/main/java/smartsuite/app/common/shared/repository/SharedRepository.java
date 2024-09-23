package smartsuite.app.common.shared.repository;

import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SharedRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "shared.";


	public List findCommonCode(String code) {
		return sqlSession.selectList(NAMESPACE + "findCommonCode", code);
	}

	public List findListCommonCodeAttributeCode(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListCommonCodeAttributeCode", param);
	}

	public List<Map<String, Object>> findListCommonCodeAttributeValue(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListCommonCodeAttributeValue", param);
	}

	public List<Map<String, Object>> findListCommonCodeConstraintCode(Map<String,Object> param){
		return sqlSession.selectList(NAMESPACE+"findListCommonCodeConstraintCode",param);
	}

	public List findListModifiedCode(Date lastUpdated) {
		return sqlSession.selectList(NAMESPACE + "findListModifiedCode", lastUpdated);
	}

	public List findListCompanyCodeForCombobox() {
		return sqlSession.selectList(NAMESPACE + "findListCompanyCodeForCombobox");
	}

	public List findListFormatterForCombobox() {
		return sqlSession.selectList(NAMESPACE + "findListFormatterForCombobox");
	}

	public Map<String, Object> findDocumentInfo(String docNoCd) {
		return sqlSession.selectOne(NAMESPACE + "findDocumentInfo", docNoCd);
	}

	public void insertLoginLogInfo(Map param) {
		sqlSession.insert(NAMESPACE + "insertLoginLogInfo", param);
	}


	public List findListOperationOrganizationByUser(String param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationByUser", param);
	}

	public List findListOperationOrganizationByOperationOrganizationLink(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationByOperationOrganizationLink", param);
	}

	public List<Map<String, Object>> findListUserFunction(String param) {
		return sqlSession.selectList(NAMESPACE + "findListUserFunction", param);
	}

	public List<Map<String, Object>> findListUserMenu(Map searchParam) {
		return sqlSession.selectList(NAMESPACE + "findListUserMenu", searchParam);
	}

	public List findListDefaultMenu(Map<String, Object> searchParam) {
		return sqlSession.selectList(NAMESPACE + "findListDefaultMenu", searchParam);
	}

	public List findListMajorCategory(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListMajorCategory", param);
	}

	public List findListCategoryByParentCategoryCode(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListCategoryByParentCategoryCode", param);
	}

	public List findListAmountUnitCode(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListAmountUnitCode", param);
	}

	public List findListOperationOrganizationBySupplier(String param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationBySupplier", param);
	}

	public Map<String, Object> findHelpManualType(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findHelpManualType", param);
	}

	public Map findHelpManualInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findHelpManualInfo", param);
	}


	public List<Map<String, Object>> findListMenuFunctionAndUrlByUserRoleList() {
		return sqlSession.selectList(NAMESPACE + "findListMenuFunctionAndUrlByUserRoleList");
	}

	public void updateUserInfoByLoginSession(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateUserInfoByLoginSession", param);
	}

	public Map findDisabledAccountInfo() {
		return sqlSession.selectOne(NAMESPACE + "findDisabledAccountInfo");
	}

	public String findCodeName(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCodeName", param);
	}

	public List<Map<String, Object>> findListOperationOrganizationForCombobox(String param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationForCombobox", param);
	}

	public FloaterStream findListZipCodeByDatabase(Map param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListZipCodeByDatabase", param);
	}

	public List findListOperationOrganizationByTenant(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationByTenant", param);
	}

	public void saveUserHomeType(Map param) {
		sqlSession.update(NAMESPACE + "saveUserHomeType", param);
	}


	public Map<String, Object> findInfoAutoManual(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findInfoAutoManual",param);
	}

	public List findListLatelyExchange(String param) {
		return sqlSession.selectList(NAMESPACE + "findListLatelyExchange",param);
	}

	public void updateDocumentNumber(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateDocumentNumber", param);
	}

	public Map<String, Object> findDocumentInfoByTenant(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findDocumentInfoByTenant", param);
	}

	public String findOperationOrganizationName(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findOperationOrganizationName", param);
	}
	
	/**
	 * 공통코드 제약 조건 값 조회
	 * @param codeInfo
	 * @return
	 */
	public String getCommonCodeConstraintConditionValue(Map<String, Object> codeInfo) {
		return sqlSession.selectOne(NAMESPACE + "getCommonCodeConstraintConditionValue", codeInfo);
	}
	
	public Map<String, Object> findTimezoneByOorgCd(String oorgCd) {
		return sqlSession.selectOne(NAMESPACE + "findTimezoneByOorgCd", oorgCd);
	}

	public List findListCompanyCodeForVendor(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListCompanyCodeForVendor");
	}
	
	public void insertTempQueryId(Map param) {
		sqlSession.insert(NAMESPACE + "insertTempQueryId", param);
	}
	
	public void deleteTempQueryId() {
		sqlSession.delete(NAMESPACE + "deleteTempQueryId", Maps.newHashMap());
	}

	public List findListModuleAttach(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListModuleAttach",param);
	}

	public List findListGroupModuleAttach(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListGroupModuleAttach",param);
	}

	public void updateModuleAttachCode(Map updateModuleAttachInfo) {
		sqlSession.update(NAMESPACE + "updateModuleAttachCode", updateModuleAttachInfo);
	}

	public void insertModuleAttachCode(Map insertModuleAttache) {
		sqlSession.insert(NAMESPACE + "insertModuleAttachCode", insertModuleAttache);
	}

	public int getModuleAttachCode(Map moduleAttacheInfo) {
		return sqlSession.selectOne(NAMESPACE + "getModuleAttachCode",moduleAttacheInfo);
	}

	public void deleteModuleAttachCode(Map deleteModuleAttachInfo) {
		sqlSession.delete(NAMESPACE + "deleteModuleAttachCode",deleteModuleAttachInfo);
	}

	public Map findModuleAttach(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findModuleAttach",param);
	}
}
