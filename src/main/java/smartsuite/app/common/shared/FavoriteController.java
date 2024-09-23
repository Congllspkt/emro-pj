package smartsuite.app.common.shared;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smartsuite.app.common.shared.service.FavoriteService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings ({ "rawtypes", "unchecked" })
@Controller
public class FavoriteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);

	@Inject
	FavoriteService favoriteService;
	
	/**
	 * 내 즐겨찾기 리스트 조회를 요청한다.
	 *
	 * @author : Yeon-u Kim
	 * @return the list< map< string, object>>
	 * @Date : 2017. 3. 30
	 * @Method Name : findListMyFavoriteMenu
	 */
	@RequestMapping(value="**/findListMyFavoriteMenu.do")
	public @ResponseBody List<Map<String,Object>> findListMyFavoriteMenu(){
		return favoriteService.findListMyFavoriteMenu();
	}

	/**
	 * 내 즐겨찾기 메뉴를 추가/해제 전환 요청한다.
	 *
	 * @param param
	 * @return
	 * @Method Name : toggleMyFavorite
	 */
	@RequestMapping(value="**/toggleAppendOrDeleteMyFavoriteMenu.do")
	public @ResponseBody List<Map<String,Object>> toggleAppendOrDeleteMyFavoriteMenu(@RequestBody Map param){
		return favoriteService.toggleAppendOrDeleteMyFavoriteMenu(param);
	}
	
	/**
	 * 내 즐겨찾기 폴더 삭제를 요청한다.
	 *
	 * @param param
	 * @return
	 * @Method Name : deleteListMyFavoriteFolderByUserId
	 */
	@RequestMapping(value="**/deleteListMyFavoriteFolderByUserId.do")
	public @ResponseBody List<Map<String,Object>> deleteListMyFavoriteFolderByUserId(@RequestBody Map param){
		return favoriteService.deleteMyFavorite(param);
	}
	
	/**
	 * 내 즐겨찾기 폴더를 저장 요청한다.
	 * 
	 * @param param
	 * @return
	 * @Method Name : saveMyFavoriteFolder
	 */
	@RequestMapping(value="**/saveMyFavoriteFolder.do")
	public @ResponseBody List<Map<String,Object>> saveMyFavoriteFolder(@RequestBody Map param) {
		return favoriteService.saveMyFavoriteFolder(param);
	}

	/**
	 * 내 즐겨찾기 순서 수정을 요청한다.
	 *
	 * @param param
	 * @return
	 * @Method Name : updateMyFavoriteSortOrd
	 */
	@RequestMapping(value="**/updateMyFavoriteSortOrd.do")
	public @ResponseBody List<Map<String,Object>> updateMyFavoriteSortOrd(@RequestBody Map param){
		return favoriteService.updateMyFavoriteSortOrd(param);
	}


}
