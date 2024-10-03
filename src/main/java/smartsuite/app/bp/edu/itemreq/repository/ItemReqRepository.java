package smartsuite.app.bp.edu.itemreq.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.Map;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ItemReqRepository {

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "item-req.";

	public FloaterStream findListItemReq(Map<String, Object> param) {
		return new QueryFloaterStream(sqlSession, NAMESPACE + "findListItemReq", param);
	}

	public void deleteItemRegReq(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteItemRegReq", param);
	}

	public Map<String,Object> findInfoItemReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findInfoItemReq", param);
	}

	public int checkExistedItemRegReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "checkExistedItemRegReq", param);
	}

	public Map<String, Object> findInfoItemRegReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findInfoItemRegReq", param);
	}

	public void insertItemRegReq(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemRegReq", param);
	}
	
	public void updateItemRegReq(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateItemRegReq", param);
	}
	
 }
