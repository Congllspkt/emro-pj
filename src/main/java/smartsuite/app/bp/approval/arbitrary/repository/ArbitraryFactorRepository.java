package smartsuite.app.bp.approval.arbitrary.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ArbitraryFactorRepository {

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "arbitrary-factor.";
	
	/**
	 * 전결설정항목를 등록한다.
	 *
	 */
	public void insertFactor(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertFactor", param);
	}
	
	/**
	 * 전결설정항목를 수정한다.
	 *
	 */
	public void updateFactor(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateFactor", param);
	}
	
	/**
	 * 전결설정항목를 삭제한다.
	 *
	 */
	public void deleteFactor(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteFactor", param);
	}
	
	/**
	 * 전결설정항목 목록을 조회한다.
	 *
	 */
	public FloaterStream selectListFactor(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "selectListFactor", param);
	}
	
	/**
	 * 전결설정항목 상세 정보를 조회한다.
	 *
	 */
	public Map<String, Object> selectFactor(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "selectFactor", param);
	}
}
