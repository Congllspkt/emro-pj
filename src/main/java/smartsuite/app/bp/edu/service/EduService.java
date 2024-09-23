package smartsuite.app.bp.edu.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.repository.UserRepository;
import smartsuite.app.bp.admin.job.service.JobService;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
import smartsuite.app.bp.edu.repository.EduRepository;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.shared.Const;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.exception.CommonException;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.authentication.Auth;
import smartsuite.security.authentication.PasswordGenerator;
import smartsuite.security.core.authentication.encryption.PasswordEncryptor;
import smartsuite.security.core.crypto.CipherUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * User 관련 처리하는 서비스 Class입니다.
 *
 * @author Yeon-u Kim
 * @see 
 * @FileName UserService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 2. 3
 * @변경이력 : [2016. 2. 3] Yeon-u Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" , "rawtypes"})
public class EduService {
	
	/** global.properties. */
	@Value("#{globalProperties['bp.main.url']}")
	private String buyerMainUrl;

	@Inject
	MailService mailService;
	
	@Inject
	CipherUtil cipherUtil;
	
	@Inject
	PasswordGenerator passwordGenerator;
	
	@Inject
	OperationOrganizationService operationOrganizationService;
	
	@Inject
	JobService jobService;
	
	@Inject
	PasswordEncryptor passwordEncryptor;

	@Inject
	EduRepository eduRepository;
	
	/**
	 * user list의 값을 반환한다.
	 *
	 * @author : Yeon-u Kim
	 * @param searchParam the search param
	 * @return user list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListUser
	 */
	public List<Map<String,Object>> findListRegReq(Map searchParam) {
		return eduRepository.findListRegReq(searchParam);
	}
	
	public List<Map<String,Object>> findListItemCat(Map searchParam) {
		return eduRepository.findListItemCat(searchParam);
	}
	
	
}
