package smartsuite.app.bp.admin.todoManager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.todoManager.repository.SupplierToDoManagerRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Service
@Transactional
public class SupplierToDoManagerService {

	@Inject
	SupplierToDoManagerRepository supplierToDoManagerRepository;

	/**
	 * 할일 항목 별 사용자 목록 조회
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findListTodoUserSupplier(Map<String, Object> param) {
		return supplierToDoManagerRepository.findListTodoUserSupplier(param);
	}
}
