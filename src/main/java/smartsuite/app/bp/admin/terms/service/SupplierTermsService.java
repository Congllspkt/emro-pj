package smartsuite.app.bp.admin.terms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.terms.repository.SupplierTermsRepository;

import javax.inject.Inject;
import java.util.*;

/**
 * 약관 관리 관련 처리하는 서비스 Class입니다.
 *  매뉴얼 유형(HTML : HTML, TX : Text, EML : Mail, APVL : Approval)
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" })
public class SupplierTermsService {

	@Inject
	SupplierTermsRepository supplierTermsRepository;

	/**
	 * 사용자 약관동의 이력 목록 조회 ( 내부사용자&외부담당자 )
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListUserTermsAgreeHistory(Map<String, Object> param) {
		return supplierTermsRepository.findListUserTermsAgreeHistory(param);
	}

}
