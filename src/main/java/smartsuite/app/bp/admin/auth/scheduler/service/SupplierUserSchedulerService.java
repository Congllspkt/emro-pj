package smartsuite.app.bp.admin.auth.scheduler.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.scheduler.repository.SupplierUserSchedulerRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * UserScheduler 관련 처리하는 서비스 Class입니다.
 *
 * @see
 * @FileName UserSchedulerService.java
 * @package smartsuite.app.bp.admin.auth.scheduler
 * @Since 2020. 6. 26
 */
@Service
@Transactional
public class SupplierUserSchedulerService {

	@Inject
	SupplierUserSchedulerRepository supplierUserSchedulerRepository;

	public List<Map<String,Object>> findListDormantAccountForAllUser(Map<String, Object> userDormantInfo){
		return supplierUserSchedulerRepository.findListDormantAccountForAllUser(userDormantInfo);
	}
	public List<Map<String,Object>> findListDormantAccountForSupplier(Map<String, Object> userDormantInfo){
		return supplierUserSchedulerRepository.findListDormantAccountForSupplier(userDormantInfo);
	}

}
