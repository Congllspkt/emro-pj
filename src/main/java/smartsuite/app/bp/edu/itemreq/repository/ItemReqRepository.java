package smartsuite.app.bp.edu.itemreq.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartsuite.data.FloaterStream;
import smartsuite.mybatis.QueryFloaterStream;

import javax.inject.Inject;
import java.util.List;
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

	public void deleteItemOorgRegReq(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteItemOorgRegReq", param);
	}

	public void deleteItemRegReq(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteItemRegReq", param);
	}

	public Map<String,Object> findInfoItemReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findInfoItemReq", param);
	}

	public List<Map<String, Object>> findListItemAsgnAttrByItemIattrRegReq(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListItemAsgnAttrByItemIattrRegReq", param);
	}

	public int checkExistedItemRegReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "checkExistedItemRegReq", param);
	}

	public int checkExistedApvdAndReqg(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "checkExistedApvdAndReqg", param);
	}

	public Map<String, Object> findInfoItemRegReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findInfoItemRegReq", param);
	}

	public int findCntProgressingItemRegReq(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "findCntProgressingItemRegReq", param);
	}
	
	public Map<String, Object> checkItemMasterDuplicate(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE + "checkItemMasterDuplicate", param);
	}
	
	public void insertItem(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItem", param);
	}

	public void insertItemOorg(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemOorg", param);
	}
 }
