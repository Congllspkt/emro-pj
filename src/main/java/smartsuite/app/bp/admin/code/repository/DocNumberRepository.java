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
public class DocNumberRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "document-number.";


	/**
	 * 문서번호 목록 조회한다.
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListDocNumber(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListDocNumber", param);
	}

	/**
	 * 문서 채번 번호 수정
	 * @param docNumber
	 */
	public void updateDocumentNumber(Map docNumber) {
		sqlSession.update(NAMESPACE + "updateDocumentNumber", docNumber);
	}

	/**
	 * 문서 채번
	 * @param docNumber
	 */
	public void insertDocNumber(Map docNumber) {
		sqlSession.insert(NAMESPACE + "insertDocNumber", docNumber);
	}


	public void deleteDocNumber(Map docNumber) {
		sqlSession.delete(NAMESPACE + "deleteDocNumber", docNumber);
	}
}
