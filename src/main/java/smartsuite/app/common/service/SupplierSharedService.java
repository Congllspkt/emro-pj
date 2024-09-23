package smartsuite.app.common.service;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.mail.MailService;
import smartsuite.app.common.repository.SupplierSharedRepository;
import smartsuite.app.common.shared.ResultMap;
import smartsuite.security.authentication.PasswordGenerator;
import smartsuite.security.core.authentication.encryption.PasswordEncryptor;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes" })
@Service
@Transactional
public class SupplierSharedService {

	/** global.properties.*/
	@Value("#{globalProperties['sp.main.url']}")
	private String supplierMainUrl;

	@Inject
	SupplierSharedRepository supplierSharedRepository;

	@Inject
	private PasswordEncryptor passwordEncryptor;

	@Inject
	private PasswordGenerator passwordGenerator;

	@Inject
	MailService mailService;

	static final Logger LOG = LoggerFactory.getLogger(SupplierSharedService.class);
	/**
	 * list vendor 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 10
	 * @Method Name : findListVendorInfo
	 */
	public List findListVendorInfo(Map param) {
		return supplierSharedRepository.findListVendorInfo(param);
	}

	/**
	 * list vendor master 조회한다.
	 *
	 * @author : mgPark
	 * @param param the param
	 * @return the list
	 * @Date : 2018. 7. 9
	 * @Method Name : findListVendorMaster
	 */
	public List findListVendorMaster(Map param) {
		return supplierSharedRepository.findListVendorMaster(param);
	}

	/**
	 * 협력사 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 7. 19
	 * @Method Name : findListVendorBasicInfo
	 */
	public List findListVendorBasicInfo(Map param) {
		return supplierSharedRepository.findListVendorBasicInfo(param);
	}

	/**
	 * list all vendor 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 7. 20
	 * @Method Name : findListVendor
	 */
	public List findListVendor(Map param) {
		return supplierSharedRepository.findListVendor(param);
	}

	/**
	 * list sg vendor 조회한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 7. 20
	 * @Method Name : findListSourcingGroupVendor
	 */
	public List findListSourcingGroupVendor(Map param) {
		return supplierSharedRepository.findListSourcingGroupVendor(param);
	}


	public ResultMap findUserIdByBusinessRegistrationNumberProcess(Map param) {
		param.put("bizregno", param.get("id"));

		// 사업자등록번호로 사용자검색
		List<Map<String, Object>> userList = this.findUserIdByBusinessRegistrationNumber(param);

		if (userList.size() == 0 || userList.isEmpty()) {
			return ResultMap.FAIL();
		} else {
			ResultMap resultMap = ResultMap.getInstance();
			Map<String,Object> resultData = Maps.newHashMap();
			resultData.put("userList",userList);
			resultMap.setResultData(resultData);
			resultMap.setResultStatus(ResultMap.STATUS.SUCCESS);
			return resultMap;
		}
	}

	private List<Map<String, Object>> findUserIdByBusinessRegistrationNumber(Map param) {
		return supplierSharedRepository.findUserIdByBusinessRegistrationNumber(param);
	}


	/**
	 * 운영조직에 연결된 vendor 목록을 조회한다.
	 *
	 * @author : JongKyu Kim
	 * @param param the param
	 * @return the list
	 * @Date : 2016. 5. 10
	 * @Method Name : findListOperationOrganizationLinkVendor
	 */
	public List<Map<String, Object>> findListOperationOrganizationLinkVendor(Map<String, Object> param) {
		return supplierSharedRepository.findListOperationOrganizationLinkVendor(param);
	}

	public ResultMap initPassword(Map param) {
		param.put("usr_id", param.get("username"));

		// email과 id로 사용자 검색
		Map<String, Object> userInfo = this.findUserInfoByEmailAndUserId(param);

		if (MapUtils.isEmpty(userInfo)) {
			return ResultMap.FAIL();
		}
		// 패스워드 초기화시에 이메일 수신 동의 여부는 검사하지 않음
		String tempPassword = passwordGenerator.generate();
		String encryptedPassword = passwordEncryptor.encryptPw(tempPassword);
		userInfo.put("password", encryptedPassword);
		userInfo.put("tempPw", tempPassword);
		mailSendBySupplier(userInfo);
		this.initPasswordByEmailAndUserId(userInfo);

		return ResultMap.SUCCESS();
	}


	public void initPasswordByEmailAndUserId(Map<String, Object> userInfo) {
		supplierSharedRepository.initPasswordByEmailAndUserId(userInfo);
	}

	public Map<String, Object> findUserInfoByEmailAndUserId(Map param) {
		return supplierSharedRepository.findUserInfoByEmailAndUserId(param);
	}

	/**
	 * 메일을 전송한다.
	 *
	 * @param param the param
	 * @Method Name : mailSend
	 */
	public void mailSendBySupplier(Map user) {
		user.put("url", supplierMainUrl);
		mailService.sendAsync("INIT_PW_ML", null, user);
	}


}
