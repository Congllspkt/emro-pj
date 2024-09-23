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
public class MultiLangMgtRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "multiLang.";


	/**
	 * 다국어관리 목록을 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListMultiLang(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListMultiLang", param);
	}

	/**
	 * 다국어관리 수정
	 * @param multiLang
	 */
	public void updateMultiLang(Map multiLang) {
		sqlSession.update(NAMESPACE + "updateMultiLang", multiLang);
	}

	/**
	 * 다국어 관리 추가
	 * @param multiLang
	 */
	public void insertMultiLang(Map multiLang) {
		sqlSession.insert(NAMESPACE + "insertMultiLang", multiLang);
	}

	/**
	 * 다국어 관리 삭제
	 * @param multiLang
	 */
	public void deleteMultiLang(Map multiLang) {
		sqlSession.delete(NAMESPACE + "deleteMultiLang", multiLang);
	}


	/**
	 *  다국어관리 message 목록을 조회한다
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findMessageList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findMessageList", param);
	}

	/**
	 * 다국어 관리 message 존재여부 조회
	 * @param message
	 * @return
	 */
	public int getMessageCount(Map<String, Object> message) {
		return sqlSession.selectOne(NAMESPACE + "getMessageCount", message);
	}
}



