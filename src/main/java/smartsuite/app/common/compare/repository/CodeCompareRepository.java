package smartsuite.app.common.compare.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes"})
public class CodeCompareRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	private static final String NAMESPACE = "codeCompare.";
	
	/**
	 * 테이블비교 정보 조회
	 * @param param
	 * @return
	 */
	public List findListCompareTable(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListCompareTable", param);
	}
	
	/**
	 * 테이블비교 정보 추가
	 * @param param
	 */
	public void insertCompareTable(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"insertCompareTable", param);
	}
	
	/**
	 * 테이블비교 정보 수정
	 * @param param
	 */
	public void updateCompareTable(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateCompareTable", param);
	}
	
	/**
	 * 테이블비교 정보 삭제
	 * @param param
	 */
	public void deleteCompareTable(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"deleteCompareTable", param);
	}
	
	/**
	 * 그룹코드 정보 조회
	 * @param param
	 * @return
	 */
	public List findListGrpCd(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListGrpCd", param);
	}
	
	/**
	 * 그룹코드 정보 추가
	 * @param param
	 */
	public void insertGrpCd(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"insertGrpCd", param);
	}
	
	/**
	 * 그룹코드 정보 수정
	 * @param param
	 */
	public void updateGrpCd(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateGrpCd", param);
	}
	
	/**
	 * 그룹코드 정보 삭제
	 * @param param
	 */
	public void deleteListGrpCdInfo(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE+"deleteListGrpCdInfo", param);
	}
}
