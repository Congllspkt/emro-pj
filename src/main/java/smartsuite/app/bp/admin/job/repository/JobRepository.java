package smartsuite.app.bp.admin.job.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class JobRepository {

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "job.";

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;


	/**
	 * 직무 담당자 삭제
	 * @param param
	 */
	public void deletePurchaseGroupCategoryJobUserByUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE + "deletePurchaseGroupCategoryJobUserByUserId", param);
	}

	/**
	 * 구매 그룹 코드 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListPurchaseGroupCategory", param);
	}

	/**
	 * 구매 그룹 추가
	 * @param insertPurchaseGroupCategory
	 */
	public void insertPurchaseGroupCategory(Map<String, Object> insertPurchaseGroupCategory) {
		sqlSession.insert(NAMESPACE + "insertPurchaseGroupCategory", insertPurchaseGroupCategory);
	}

	/**
	 * 직무 수정
	 * @param updateJobInfo
	 */
	public void updateJob(Map<String, Object> updateJobInfo) {
		sqlSession.update(NAMESPACE + "updateJobList", updateJobInfo);
	}

	/**
	 * 구매 그룹 코드 삭제
	 * @param deleteJobInfo
	 */
	public void deletePurchaseGroupCode(Map deleteJobInfo) {
		sqlSession.delete(NAMESPACE + "deletePurchaseGroupCode", deleteJobInfo);
	}

	/**
	 * 품목 분류 구매 그룹 맵핑 리스트 조회 ( 세 분류 리스트 조회 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryAndItemMapping(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListPurchaseGroupCategoryAndItemMapping", param);
	}

	/**
	 * 직무 담당자 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryJobUser(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListPurchaseGroupCategoryJobUser", param);
	}

	/**
	 * 직무 담당자 추가
	 * @param purchaseGroupCategoryJobUser
	 */
	public void insertPurchaseGroupCategoryJobUser(Map<String, Object> purchaseGroupCategoryJobUser) {
		sqlSession.insert(NAMESPACE + "insertPurchaseGroupCategoryJobUser", purchaseGroupCategoryJobUser);
	}

	/**
	 * 직무 담당자 수정
	 * @param updatePurchaseGroupCategoryJobUser
	 */
	public void updatePurchaseGroupCategoryJobUser(Map<String, Object> updatePurchaseGroupCategoryJobUser) {
		sqlSession.update(NAMESPACE + "updatePurchaseGroupCategoryJobUser", updatePurchaseGroupCategoryJobUser);
	}

	/**
	 * 직무 담당자 삭제
	 * @param purchaseGroupCategoryJobUser
	 */
	public void deletePurchaseGroupCategoryJobUser(Map purchaseGroupCategoryJobUser) {
		sqlSession.delete(NAMESPACE + "deletePurchaseGroupCategoryJobUser", purchaseGroupCategoryJobUser);
	}

	/**
	 * 현재 사용중인 구매 그룹 코드 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUsedPurchaseGroupCategory(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListUsedPurchaseGroupCategory", param);
	}

	/**
	 * 직무별 품목 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListPurchaseGroupCategoryItem(Map<String, Object> param) {
		return sqlSession.selectList(NAMESPACE + "findListPurchaseGroupCategoryItem", param);
	}

	/**
	 * 구매그룹 코드 및 직무 연결 코드 삭제 ( 조건 purc group code )
	 * @param purchaseGroupCategoryAndItemMapping
	 */
	public void deletePurchaseGroupCategoryAndItemMappingByPurchaseGroupCode(Map<String, Object> purchaseGroupCategoryAndItemMapping) {
		sqlSession.delete(NAMESPACE + "deletePurchaseGroupCategoryAndItemMappingByPurchaseGroupCode", purchaseGroupCategoryAndItemMapping);
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 추가
	 * @param insertMapping
	 */
	public void insertPurchaseGroupCategoryAndItemMapping(Map<String, Object> insertMapping) {
		sqlSession.insert(NAMESPACE + "insertPurchaseGroupCategoryAndItemMapping", insertMapping);
	}

	/**
	 * 구매 그룹 코드 및 품목분류 맵핑 수정
	 * @param updateMapping
	 */
	public void updatePurchaseGroupCategoryAndItemMapping(Map<String, Object> updateMapping) {
		sqlSession.update(NAMESPACE + "updatePurchaseGroupCategoryAndItemMapping", updateMapping);
	}
}
