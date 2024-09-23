package smartsuite.app.common.shared.service;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.shared.repository.FavoriteRepository;

import javax.inject.Inject;
import java.util.*;

/**
 * 공통으로 사용하는 서비스 관련 Class입니다.
 *
 * @author hjhwang
 * @see
 * @FileName SharedService.java
 * @package smartsuite.app.shared
 * @Since 2016. 2. 2
 * @변경이력 : [2016. 2. 2] hjhwang 최초작성
 */
@SuppressWarnings({ "rawtypes" })
@Service
@Transactional
public class FavoriteService {

	static final Logger LOG = LoggerFactory.getLogger(FavoriteService.class);

	@Inject
	FavoriteRepository favoriteRepository;


	/**
	 * list my favorite 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @return the list< map< string, object>>
	 * @Date : 2017. 3. 30
	 * @Method Name : findListMyFavoriteMenu
	 */
	public List<Map<String, Object>> findListMyFavoriteMenu() {
		return favoriteRepository.findListMyFavoriteMenu();
	}

	/**
	 * 내 즐겨찾기 메뉴를 추가/해제 전환한다.
	 *
	 * @param param
	 * @return
	 * @Method Name : toggleAppendOrDeleteMyFavoriteMenu
	 */
	public List<Map<String, Object>> toggleAppendOrDeleteMyFavoriteMenu(Map<String, Object> param) {
		Map<String, Object> favoriteMenu = favoriteRepository.findMyFavoriteMenuInfo(param);
				
		if (MapUtils.isEmpty(favoriteMenu)) {
			favoriteRepository.insertMyFavoriteMenuInfo(param);
		} else {
			deleteMyFavorite(param);
		}
		return findListMyFavoriteMenu();
	}

	/**
	 * 내 즐겨찾기를 삭제한다.
	 *
	 * @param param
	 * @return
	 * @Method Name : deleteMyFavorite
	 */
	public List<Map<String, Object>> deleteMyFavorite(Map<String, Object> param) {
		String foldId = (String)param.getOrDefault("fav_flr_uuid","");

		// folder 이거나 folder에 속하지 않은 1레벨 menu인 경우
		if ("FOLDER".equals(param.get("fav_cls")) || StringUtils.isEmpty(foldId)) {
			// 삭제 대상의 뒤에 위치한 폴더 순서 조정
			favoriteRepository.deleteMyFavoriteFolderAndUpdateFolderSort(param);
		}
		// 삭제 대상의 뒤에 위치한 메뉴 순서 조정(삭제 대상이 1레벨인 경우: fold_id가 null인 메뉴들만 수정, 삭제 대상이 folder에
		// 속한 경우: fold_id가 동일한 메뉴들만 수정)
		favoriteRepository.deleteMyFavoriteFolderAndUpdateFolderSortByEqualsFolderAndNotFolder(param);

		if ("FOLDER".equals(param.get("fav_cls"))) {
			favoriteRepository.deleteMyFavoriteMenuByFolderUuid(param);
			favoriteRepository.deleteListMyFavoriteFolderByUserId(param);
		} else {
			favoriteRepository.deleteMyFavoriteMenuByUserId(param);
		}
		return findListMyFavoriteMenu();
	}

	/**
	 * 내 즐겨찾기 폴더를 저장한다.
	 *
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> saveMyFavoriteFolder(Map<String, Object> param) {
		String foldId = (String)param.getOrDefault("fav_flr_uuid","");
		if (StringUtils.isEmpty(foldId)) {
			param.put("fav_flr_uuid", UUID.randomUUID().toString());
			favoriteRepository.insertMyFavoriteFolder(param);
		} else {
			favoriteRepository.updateMyFavoriteFolderName(param);
		}
		return findListMyFavoriteMenu();
	}

	/**
	 * 내 즐겨찾기 순서를 수정한다.
	 *
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> updateMyFavoriteSortOrd(Map<String, Object> param) {
		String srcFoldId = param.get("src_fold_id") == null ? "" : (String) param.get("src_fold_id");
		String targetFoldId = param.get("targ_fold_id") == null ? "" : (String) param.get("targ_fold_id");
		String favoriteClass = param.get("fav_cls") == null ? "" : (String) param.get("fav_cls");

		// folder 이거나, 1레벨에서 1레벨로 이동하는 menu이거나, 동일 부모내에서 이동하는 menu인 경우
		if ("FOLDER".equals(favoriteClass)
				|| StringUtils.isEmpty(srcFoldId) && StringUtils.isEmpty(targetFoldId)
				|| srcFoldId.equals(targetFoldId)) {

			int srcSortOrd = (Integer) param.get("src_sort_ord");
			int targetSortOrd = (Integer) param.get("targ_sort_ord");
			param.put("move_type", srcSortOrd < targetSortOrd ? "DOWN" : "UP");

			if ("FOLDER".equals(param.get("fav_cls"))
					|| StringUtils.isEmpty(srcFoldId) && StringUtils.isEmpty(targetFoldId)) {
				this.updateMyFavoriteFolderSortOrdByMovingWithInSameParent(param);
			}
			this.updateMyFavoriteMenuSortOrdByMovingWithInSameParent(param);


			// 1레벨에서 2레벨, 2레벨에서 1레벨, 2레벨에서 다른 부모(folder)에 속하는 2레벨로 이동하는 menu인 경우
		} else {
			if (StringUtils.isEmpty(srcFoldId) && !StringUtils.isEmpty(targetFoldId)
					|| !StringUtils.isEmpty(srcFoldId) && StringUtils.isEmpty(targetFoldId)) {
				this.updateMyFavoriteFolderSortOrdByMovingToOtherParent(param);
			}
			this.updateMyFavoriteMenuSortOrdByMovingToOtherParent(param);
		}
		return findListMyFavoriteMenu();
	}

	public void updateMyFavoriteMenuSortOrdByMovingToOtherParent(Map<String, Object> param) {
		favoriteRepository.updateMyFavoriteMenuSortOrdByMovingToOtherParent(param);
	}

	public void updateMyFavoriteFolderSortOrdByMovingToOtherParent(Map<String, Object> param) {
		favoriteRepository.updateMyFavoriteFolderSortOrdByMovingToOtherParent(param);
	}

	public void updateMyFavoriteMenuSortOrdByMovingWithInSameParent(Map<String, Object> param) {
		favoriteRepository.updateMyFavoriteMenuSortOrdByMovingWithInSameParent(param);
	}

	public void updateMyFavoriteFolderSortOrdByMovingWithInSameParent(Map<String, Object> param) {
		favoriteRepository.updateMyFavoriteFolderSortOrdByMovingWithInSameParent(param);
	}


}
