package smartsuite.app.bp.admin.manualManager.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@SuppressWarnings ({ "rawtypes", "unchecked" })
public class ManualManagerRepository {
	/** The sql session. */
	@Inject
	private SqlSession sqlSession;

	/** The namespace. */
	/*NAMESPACE*/
	private static final String NAMESPACE = "manual-manager.";


	/**
	 * 각 언어 별 메뉴얼 조회
	 * @param param
	 * @return
	 */
	public List findListAllLanguageManual(Map param) {
		return sqlSession.selectList(NAMESPACE +"findListAllLanguageManual", param);
	}

	/**
	 * 마지막 revision의 메뉴얼 정보를 조회한다.
	 * @param param
	 * @return
	 */
	public Map findLastRevisionManualInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE+"findLastRevisionManualInfo", param);
	}

	/**
	 * 메뉴얼 차수를 표기하기 위한 combobox 조회용 
	 * @param param
	 * @return
	 */
	public List findListRevisionManualComboboxItem(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListRevisionManualComboboxItem", param);
	}

	/**
	 * 메뉴얼 정보를 마지막 revision으로 추가한다.
	 * @param param
	 */
	public void insertNextRevisionManualInfo(Map<String, Object> param) {
		sqlSession.insert(NAMESPACE+"insertNextRevisionManualInfo", param);
	}

	/**
	 * 선택한 revision 의 메뉴얼 정보를 수정한다.
	 * @param param
	 */
	public void updateSelectionRevisionManualInfo(Map<String, Object> param) {
		sqlSession.update(NAMESPACE+"updateSelectionRevisionManualInfo", param);
	}

	/**
	 * 선택한 언어의 메뉴얼 메뉴 리스트 조회
	 * @param param
	 * @return
	 */
	public List findListSelectionLanguageManualMenu(Map param) {
		return sqlSession.selectList(NAMESPACE+"findListSelectionLanguageManualMenu", param);
	}

	public int getCountManualInfo(Map param) {
		return sqlSession.selectOne(NAMESPACE+"getCountManualInfo", param);
	}

	public void deleteManualInfo(Map param) {
		sqlSession.delete(NAMESPACE+"deleteManualInfo", param);
	}
}
