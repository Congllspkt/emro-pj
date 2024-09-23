package smartsuite.app.bp.edu.cmsCommon.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class CmsCommonRepository {

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "cms-common.";

	public void updateCountInfoItemAttributeGroup(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateCountInfoItemAttributeGroup", param);
	}

	public List<Map<String, Object>> findListItemAsgnAttrByItemcat(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListItemAsgnAttrByItemcat", param);
	}

	public List<Map<String, Object>> findListItemAsgnAttrByItemIattr(Map<String, Object> param) {
		return  sqlSession.selectList(NAMESPACE + "findListItemAsgnAttrByItemIattr", param);
	}

	public List<Map<String,Object>> findListAssignedAttribute(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListAssignedAttribute", param);
	}

	public List<Map<String,Object>> findListReqAsgnAttr(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListReqAsgnAttr", param);
	}

	public List<Map<String,Object>> findListRegAttr(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListRegAttr", param);
	}

	public void insertItemRegReq(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemRegReq", param);
	}

	public void insertItemOorgRegReq(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemOorgRegReq", param);
	}

	public void updateItemRegReq(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateItemRegReq", param);
	}

	public void updateItemOorgRegReq(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "updateItemOorgRegReq", param);
	}

	public void deleteItemIattrRegReq(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteItemIattrRegReq", param);
	}

	public void deleteAllItemOorgRegReq(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deleteAllItemOorgRegReq", param);
	}

	public void insertItemIattrRegReq(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemIattrRegReq", param);
	}

	public void insertItemIattr(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE + "insertItemIattr", param);
	}

	public void deleteItemIattr(Map<String, Object> param) {
		sqlSession.update(NAMESPACE + "deleteItemIattr", param);
	}

	public List<Map<String, Object>> findListItemOorgRegReq(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListItemOorgRegReq", param);
	}
}
