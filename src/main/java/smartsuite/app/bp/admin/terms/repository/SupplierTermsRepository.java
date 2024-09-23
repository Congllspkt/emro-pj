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
public class SupplierTermsRepository {
	/** The sql session. */
	@Inject
	SqlSession sqlSession;

	/** The namespace. */
	private static final String NAMESPACE = "supplier-terms.";

	/**
	 * 사용자 약관동의 이력 목록 조회 ( 내부사용자&외부담당자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUserTermsAgreeHistory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListUserTermsAgreeHistory",param);
	}
}
