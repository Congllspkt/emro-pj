package smartsuite.app.common.repository;

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
public class SupplierSharedRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "supplier-shared.";

	public List findListVendorInfo(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListVendorInfo", param);
	}

	public List findListVendorMaster(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListVendorMaster", param);
	}

	public List findListVendorBasicInfo(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListVendorBasicInfo", param);
	}

	public List findListVendor(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListVendor", param);
	}

	public List findListSourcingGroupVendor(Map param) {
		return sqlSession.selectList(NAMESPACE + "findListSourcingGroupVendor", param);
	}

	public List<Map<String, Object>> findUserIdByBusinessRegistrationNumber(Map param) {
		return sqlSession.selectList(NAMESPACE + "findUserIdByBusinessRegistrationNumber", param);
	}


	public List<Map<String, Object>> findListOperationOrganizationLinkVendor(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListOperationOrganizationLinkVendor", param);
	}


	public Map<String, Object> findUserInfoByEmailAndUserId(Map param) {
		return sqlSession.selectOne(NAMESPACE + "findUserInfoByEmailAndUserId", param);
	}

	public void initPasswordByEmailAndUserId(Map<String, Object> userInfo) {
		sqlSession.update(NAMESPACE + "initPasswordByEmailAndUserId", userInfo);
	}

}
