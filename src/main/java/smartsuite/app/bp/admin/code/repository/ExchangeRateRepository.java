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
public class ExchangeRateRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "exchange-rate.";


	/**
	 * 환율 데이터 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListExchangeRate(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListExchangeRate", param);
	}

	/**
	 * 환율정보 존재 여부
	 * @param row
	 * @return
	 */
	public int getCountDateCur(Map<String, Object> row) {
		return sqlSession.selectOne(NAMESPACE+"getCountDateCur",row);
	}

	/**
	 * 환율관리 데이터 수정
	 * @param exchangeRate
	 */
	public void updateExchangeRate(Map exchangeRate) {
		sqlSession.update(NAMESPACE+"updateExchangeRate", exchangeRate);
	}

	/**
	 * 환율관리 데이터 추가
	 * @param exchangeRate
	 */
	public void insertExchangeRate(Map exchangeRate) {
		sqlSession.insert(NAMESPACE+"insertExchangeRate", exchangeRate);
	}

	/**
	 * 환율관리 데이터 삭제
	 * @param exchangeRate
	 */
	public void deleteExchangeRate(Map exchangeRate) {
		sqlSession.delete(NAMESPACE+"deleteExchangeRate", exchangeRate);
	}



}
