package smartsuite.app.common.dictionary.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes"})
public class DictionaryRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	private static final String NAMESPACE = "dictionary.";
	
	/**
	 * 용어집 정보 조회
	 * @param param
	 * @return
	 */
	public List findListDictionary(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListDictionary", param);
	}
	
	/**
	 * 용어집 정보 추가
	 * @param param
	 */
	public void insertDictionary(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"insertDictionary", param);
	}
	
	/**
	 * 용어집 정보 수정
	 * @param param
	 */
	public void updateDictionary(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateDictionary", param);
	}
	
	/**
	 * 용어집 정보 삭제
	 * @param param
	 */
	public void deleteDictionary(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"deleteDictionary", param);
	}
}
