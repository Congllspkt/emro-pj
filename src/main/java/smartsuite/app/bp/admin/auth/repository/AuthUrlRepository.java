package smartsuite.app.bp.admin.auth.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class AuthUrlRepository {

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "auth-url.";

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;


	// ESAAURS 삭제 (호출패턴 역할 전체 삭제)
	public void deleteFuncUrlAll(Map<String,Object> deleteMenu){
		sqlSession.delete(NAMESPACE + "deleteFuncUrlAll", deleteMenu);
	}

	// ESAAURS 추가 (호출패턴 역할 추가)
	public void insertFuncUrl(Map<String,Object> funcUrlInfo){
		sqlSession.insert(NAMESPACE + "insertFuncUrl", funcUrlInfo);
	}

	/**
	 * count func url의 값을 반환한다.
	 *
	 * @param param the param
	 * @return count func url
	 */
	public int getCountFuncUrl(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "getCountFuncUrl", param);
	}


	/**
	 *  menu func ptrn 수정한다.
	 * @param param
	 */
	public void updateMenuFuncPattern(Map<String,Object> param){
		sqlSession.update(NAMESPACE + "updateMenuFuncPattern",param);
	}
	public void deleteFuncUrl(Map<String, Object> deleteMenuUrlInfo) {
		sqlSession.delete(NAMESPACE + "deleteFuncUrl",deleteMenuUrlInfo);
	}
}
