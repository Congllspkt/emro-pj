package smartsuite.app.bp.admin.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.repository.AuthUrlRepository;

import javax.inject.Inject;
import java.util.Map;


@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class AuthUrlService {

	@Inject
	AuthUrlRepository authUrlRepository;

	/**
	 *  menu func ptrn 수정한다.
	 * @param menuFunc
	 */
	public void updateMenuFuncPattern(Map<String, Object> menuFunc) {
		authUrlRepository.updateMenuFuncPattern(menuFunc);
	}

	/**
	 * ESAAURS 추가 (호출패턴 역할 추가)
	 * @param menuFunc
	 */
	public void insertFuncUrl(Map<String, Object> menuFunc) {
		authUrlRepository.insertFuncUrl(menuFunc);
	}

	/**
	 * MenuFunction이 존재하는지 확인한다.
	 * @param param
	 * @return
	 */
	public boolean existFunctionUrl(Map<String, Object> param) {
		int getCountFunctionUrl = authUrlRepository.getCountFuncUrl(param);
		return (getCountFunctionUrl > 0);
	}

	/**
	 * ESAAURS 삭제 (호출패턴 역할 전체 삭제)
	 * @param deleteMenuUrlInfo
	 */
	public void deleteFuncUrlAll(Map<String, Object> deleteMenuUrlInfo) {
		authUrlRepository.deleteFuncUrlAll(deleteMenuUrlInfo);
	}

	public void deleteFuncUrl(Map<String, Object> deleteMenuUrlInfo) {
		authUrlRepository.deleteFuncUrl(deleteMenuUrlInfo);
	}
}
