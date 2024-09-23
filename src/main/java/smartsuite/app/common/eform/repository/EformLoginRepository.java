package smartsuite.app.common.eform.repository;

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
public class EformLoginRepository {

	static final Logger LOG = LoggerFactory.getLogger(EformLoginRepository.class);

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/**
	 * eform 로그인 정보 및 계약정보 조회
	 *
	 * @author : LDS
	 * @param param the param
	 * @return the map
	 * @Date : 2021. 11 26
	 * @Method Name : findEformUserInfo
	 */
	public Map<String,Object> findEformUserInfo(Map<String, Object> param) {
		return sqlSession.selectOne("e-form-login.findEformUserInfo", param);
	}



}
