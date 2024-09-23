package smartsuite.app.common.shared.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FavoriteRepository {

	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The NAMESPACE. */
	private static final String NAMESPACE = "favorite.";


	public List<Map<String, Object>> findListMyFavoriteMenu() {
		return sqlSession.selectList(NAMESPACE +"findListMyFavoriteMenu");
	}

	public Map<String, Object> findMyFavoriteMenuInfo(Map<String, Object> param) {
		return sqlSession.selectOne(NAMESPACE +"findMyFavoriteMenuInfo", param);
	}

	public void insertMyFavoriteMenuInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertMyFavoriteMenuInfo", param);
	}

	public void deleteMyFavoriteFolderAndUpdateFolderSort(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"deleteMyFavoriteFolderAndUpdateFolderSort", param);
	}

	public void deleteMyFavoriteFolderAndUpdateFolderSortByEqualsFolderAndNotFolder(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"deleteMyFavoriteFolderAndUpdateFolderSortByEqualsFolderAndNotFolder", param);
	}

	public void deleteMyFavoriteMenuByFolderUuid(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE +"deleteMyFavoriteMenuByFolderUuid", param);
	}

	public void deleteListMyFavoriteFolderByUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE +"deleteListMyFavoriteFolderByUserId", param);
	}

	public void deleteMyFavoriteMenuByUserId(Map<String, Object> param) {
		sqlSession.delete(NAMESPACE +"deleteMyFavoriteMenuByUserId", param);
	}

	public void insertMyFavoriteFolder(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE +"insertMyFavoriteFolder", param);
	}

	public void updateMyFavoriteFolderName(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateMyFavoriteFolderName", param);
	}

	public void updateMyFavoriteFolderSortOrdByMovingWithInSameParent(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateMyFavoriteFolderSortOrdByMovingWithInSameParent", param);
	}

	public void updateMyFavoriteMenuSortOrdByMovingWithInSameParent(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateMyFavoriteMenuSortOrdByMovingWithInSameParent", param);
	}

	public void updateMyFavoriteFolderSortOrdByMovingToOtherParent(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateMyFavoriteFolderSortOrdByMovingToOtherParent", param);
	}

	public void updateMyFavoriteMenuSortOrdByMovingToOtherParent(Map<String, Object> param) {
		sqlSession.update(NAMESPACE +"updateMyFavoriteMenuSortOrdByMovingToOtherParent", param);
	}
}
