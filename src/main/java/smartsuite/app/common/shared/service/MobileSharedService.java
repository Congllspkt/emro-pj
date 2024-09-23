package smartsuite.app.common.shared.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공통으로 사용하는 서비스 관련 Class입니다.
 *
 * @author hjhwang
 * @see
 * @FileName SharedService.java
 * @package smartsuite.app.shared
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] hjhwang 최초작성
 */
@SuppressWarnings({ "rawtypes" })
@Service
@Transactional
public class MobileSharedService {

	static final Logger LOG = LoggerFactory.getLogger(MobileSharedService.class);

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/**
	 * 공통 코드 조회.
	 *
	 * @param code the code
	 * @return the list
	 */
	public List findCommonCode(Map<String,Object> param) {
		return sqlSession.selectList("mobile-shared.findCommonCode", param);
	}
	
	/**
	 * 로그인 사용자 운영조직 목록 조회.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return 운영조직 목록
	 * @Date : 2016. 5. 2
	 * @Method Name : findListOperationOrganizationByUser
	 */
	public List findListOperationOrganizationByUser(Map<String,Object> param) {
		return sqlSession.selectList("mobile-shared.findListOperationOrganizationByUser", param);
	}

}
