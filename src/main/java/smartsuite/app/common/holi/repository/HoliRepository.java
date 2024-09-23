package smartsuite.app.common.holi.repository;

import com.google.common.collect.Maps;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.exception.CommonException;
import smartsuite.exception.ErrorCode;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Holi 관련 처리하는 서비스 Class입니다.
 *
 * @see 
 * @FileName HoliService.java
 * @package smartsuite.app.common.holi
 * @Since 2020. 12. 9
 */
@SuppressWarnings ({ "rawtypes", "unchecked" })
@Service
@Transactional
public class HoliRepository {
	
	/** The sql session. */
	@Inject
	public SqlSession sqlSession;
	
	/** The namespace. */
	public static final String NAMESPACE = "holi.";
	
	/**
	 * list hd 조회한다.
	 *
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2020. 12. 9
	 * @Method Name : findListHd
	 */
	public List<Map<String, Object>> findListHd(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findListHd",param);
	}
	

	/**
	 * count hd by date의 값을 반환한다.
	 *
	 * @param row the row
	 * @return count hd by date
	 */
	public int getCountHdByDate(Map<String, Object> row) {
		return sqlSession.selectOne(NAMESPACE+"getCountHdByDate",row);
	}

	/**
	 * hd 수정한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : updateHd
	 */
	public void updateHd(Map<String, Object> row) {
		sqlSession.update(NAMESPACE+"updateHd",row);
	}

	/**
	 * hd 입력한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : insertHd
	 */
	public void insertHd(Map<String, Object> row) {
		sqlSession.insert(NAMESPACE+"insertHd",row);
	}
	
	/**
	 * hd 삭제한다.
	 *
	 * @param row the row
	 * @Date : 2020. 12. 9
	 * @Method Name : deleteHd
	 */
	public void deleteHd(Map<String, Object> row) {
		sqlSession.delete(NAMESPACE+"deleteHd",row);
	}
}

