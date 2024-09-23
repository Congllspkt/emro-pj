package smartsuite.app.sp.user.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class SupplierUserRepository {

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "supplier-user.";

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;


	public List<Map<String, Object>> findListVendorUser(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListVendorUser", param);
	}

	public Map findVendorUserInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE+"findVendorUserInfo", param);
	}

	/**
	 * 사업자 번호 가져오기
	 * @param param
	 * @return
	 */
	public String checkBizRegNo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"checkBizRegNo", param);
	}

	public Map<String, Object> getCertLoginSignValue(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"getCertLoginSignValue", param);
	}

	/**
	 * 공인인증서 로그인 전자서명 이후 업체 유저 정보에 업데이트
	 * @param param
	 */
	public void updateVendorSignValue(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateVendorSignValue",param);
	}

	/**
	 * 협력사 사용자 hash값 추출
	 * @param param
	 */
	public String findVendorHashValue(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE+"findVendorHashValue",param);
	}
	
	/**
	 * 협력사 사용자 hash값 초기화
	 * @param param
	 */
	public void removeVendorHashValue(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"removeVendorHashValue",param);
	}
}
