package smartsuite.app.sp.common.board.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@Service
@Transactional
@SuppressWarnings ({"rawtypes", "unchecked" })
public class SupplierBoardPopupRepository {
	
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;
	
	/** The NAMESPACE. */
	private static final String NAMESPACE = "board.";
	
	public List findBoardCommentList(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE+"findBoardCommentList", param);
	}
}
