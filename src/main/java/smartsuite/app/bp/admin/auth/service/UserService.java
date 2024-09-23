package smartsuite.app.bp.admin.auth.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.bp.admin.auth.repository.UserRepository;
import smartsuite.app.bp.admin.job.service.JobService;
import smartsuite.app.bp.admin.organizationManager.operationUnit.operationOrganization.service.OperationOrganizationService;
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
public class UserService {
	
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
	UserRepository userRepository;

	@Inject
	AccountService accountService;
	
	/**
	 * user list의 값을 반환한다.
	 *
	 * @author : Yeon-u Kim
	 * @param searchParam the search param
	 * @return user list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListUser
	 */
	public List<Map<String,Object>> findListUser(Map searchParam) {
		return userRepository.findListUser(searchParam);
	}
	
	/**
	 * user_edu list의 값을 반환한다.
	 *
	 * @author : Yeon-u Kim
	 * @param searchParam the search param
	 * @return user_edu list
	 * @Date : 2016. 2. 4
	 * @Method Name : findListUser
	 */
	public List<Map<String,Object>> findListUserEdu(Map searchParam) {
		return userRepository.findListUserEdu(searchParam);
	}

	/**
	 * user info의 값을 반환한다.
	 *
	 * @author : Yeon-u Kim
	 * @param searchParam the search param
	 * @return user info
	 * @Date : 2016. 2. 4
	 * @Method Name : findUserInfo
	 */
	public Map<String,Object> findUserByUserId(Map searchParam) {
		Map<String,Object> findUserInfo = userRepository.findUserByUserId(searchParam);;
		findUserInfo.put("limitLoginInvalidPw",accountService.getAccountSettings().getLimitLoginInvalidPasswordCount());
		return findUserInfo;
	}

	/**
	 * 사용자 리스트 삭제 요청
	 * @param param
	 * @return
	 */
	public ResultMap deleteListUserRequest(Map<String,Object> param) {
		List<Map<String, Object>> deleteUserList = (List<Map<String, Object>>)param.getOrDefault("deleteUsers", Lists.newArrayList());

		// 사용자 리스트 삭제
		this.deleteListUser(deleteUserList);

		return ResultMap.SUCCESS();
	}

	/**
	 * 사용자 리스트 삭제
	 * @param deleteUserList
	 */
	public void deleteListUser(List<Map<String, Object>> deleteUserList) {
		// 사용자 역할 삭제
		this.deleteListUserRoleByUserId(deleteUserList);

		// 사용자 운영조직 삭제
		operationOrganizationService.deleteListOperationOrganizationByUserId(deleteUserList);

		// 사용자 직무담당자 리스트 삭제
		jobService.deletePurchaseGroupCategoryJobUserByUserId(deleteUserList);

		// 사용자 정보 리스트 삭제
		this.deleteUserInfoList(deleteUserList);
	}

	/**
	 * 사용자 정보 리스트 삭제
	 * @param deleteUserList
	 */
	public void deleteUserInfoList(List<Map<String, Object>> deleteUserList) {
		for (Map<String, Object> deleteUserInfo : deleteUserList) {
			this.deleteUser(deleteUserInfo);
		}
	}

	/**
	 * 사용자 역할 삭제 ( 조회조건 user id )
	 * @param deleteUserList
	 */
	public void deleteListUserRoleByUserId(List<Map<String, Object>> deleteUserList) {
		for (Map<String, Object> deleteUserInfo : deleteUserList) {
			this.deleteUserRoleByUserId(deleteUserInfo);
		}
	}

	/**
	 * 사용자를 삭제 한다.
	 *
	 * @author : Yeon-u Kim
	 * @param userIds the user ids
	 * @Date : 2016. 2. 3
	 * @Method Name : deleteUserList
	 */
	public void deleteUser(Map<String, Object> user) {
		userRepository.deleteUser(user);
	}	

	/**
	 * user 저장한다.
	 *
	 * @author : Yeon-u Kim
	 * @param param the param
	 * @return the int
	 * @Date : 2016. 2. 3
	 * @Method Name : saveUser
	 */
	public ResultMap saveUser(Map<String,Object> param) {
		Boolean isNew = (Boolean)param.getOrDefault("isNew",false);
		Boolean isExistUser = this.existUser(param);
		// 신규 등록인데 이미 사용자 아이디가 있는 경우
		if(isNew && isExistUser) {
			// 중복 오류
			throw new CommonException("STD.ADM1040"); // 중복되는 사용자 아이디가 존재합니다.
		}
		
		if(isExistUser) { // 수정 (update)
			this.updateUser(param);


		}else{ // 신규 (insert)
			this.insertUser(param);

			// 메일 발송
			mailService.sendAsync("NEW_USER", null, param);
		}
		
		Map<String, Object> userInfo = this.findUserByUserId(param);
		ResultMap resultMap = ResultMap.getInstance();
		resultMap.setResultData(userInfo);
		return resultMap;
	}

	/**
	 * 사용자 추가
	 * @param param
	 */
	public void insertUser(Map<String, Object> param) {
		// 신규생성 시 비밀번호 발급
		// global.properties : use-default-password 가 true 일경우 default-password에 설정된 비밀번호로 발급
		userRepository.insertUser(this.templatePassWordGenerate(param));
	}

	/**
	 * 사용자 수정
	 * @param param
	 */
	public void updateUser(Map<String, Object> param) {
		userRepository.updateUser(param);
	}

	/**
	 * 패스워드 변경 프로세스
	 * @param param
	 * @return
	 */
	public ResultMap updatePasswordProcess(Map<String,Object> param) {
		//패스워드 validation
		String password = this.updatePasswordValidation(param);
		param.put("password", password);

		// 패스워드 업데이트
		this.updatePassword(param);

		return ResultMap.SUCCESS();
	}

	/**
	 * 패스워드 수정
	 * @param param
	 */
	public void updatePassword(Map<String, Object> param) {
		userRepository.updatePassword(param);
	}

	/**
	 * 패스워드 Validation
	 * @param param
	 * @return
	 */
	public String updatePasswordValidation(Map<String, Object> param) {
		String myPassword = Auth.getCurrentUser().getPassword();
		String currentPassword = cipherUtil.decrypt((String) param.getOrDefault("currentPassword",""));
		String password = cipherUtil.decrypt((String) param.getOrDefault("password",""));
		if(myPassword.equals(currentPassword)) {
			if(myPassword.equals(password)) {
				throw new CommonException("STD.ADM1032"); // 새 비밀번호가 기존 비밀번호화 동일합니다.
			}
		} else {
			throw new CommonException("STD.ADM1031"); // 비밀번호가 일치하지 않습니다.
		}
		return password;
	}


	public boolean existUser(Map<String,Object> param) {
		int getUserCount = userRepository.getUserCount(param);
		return (getUserCount > 0);
	}

	/**
	 * 사용자 별 역할 리스트 조회
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findListRoleByUser(Map<String, Object> param) {
		return userRepository.findListRoleByUser(param);
	}

	/**
	 * 사용자 별 역할 저장
	 * @param param
	 */
	public void saveRoleByUser(Map<String, Object> param) {

		// 기존에 granted 가 Y 인 건은 삭제 처리, N 인 건은 신규 추가
		String granted = param.getOrDefault("granted","")  == null?  "" : (String) param.getOrDefault("granted","");

		if("Y".equals(granted)) {
			// 사용자 별 역할 삭제 ( roleCode & UserId )
			this.deleteUserRoleByRoleCodeAndUserId(param);
		} else {
			// 사용자 별 역할 추가
			this.insertUserRoleByUser(param);
		}
	}

	/**
	 * 사용자 별 역할 추가
	 * @param param
	 */
	public void insertUserRoleByUser(Map<String, Object> param) {
		userRepository.insertUserRoleByUser(param);
	}

	/**
	 * 사용자 롤 삭제 ( 조건 role code & user id )
	 * @param param
	 */
	public void deleteUserRoleByRoleCodeAndUserId(Map<String, Object> param) {
		userRepository.deleteUserRoleByRoleCodeAndUserId(param);
	}

	/**
	 * 사용자 역할 삭제 ( 조건 UserId )
	 * @param param
	 */
	public void deleteUserRoleByUserId(Map<String, Object> param){
		userRepository.deleteUserRoleByUserId(param);
	}

	/**
	 * 사용자별 운영조직 현황을 조회한다.
	 * @param param
	 * @return
	 */
	public List findListUserOperationOrganization(Map param) {
		return userRepository.findListUserOperationOrganization(param);
	}
	
	/**
	 * 유저 패스워드를 초기화 후 메일을 전송한다. 
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @Date : 2016. 8. 16
	 * @Method Name : saveListUserOperationOrganization
	 */
	public Map initPassword(Map param){
		Map<String, Object> result = Maps.newHashMap(); 
		Map<String, Object> user = userRepository.findUserInfoByUserIdAndEmail(param);
		if(MapUtils.isEmpty(user)) {
			result.put(Const.RESULT_STATUS, Const.FAIL); 
			result.put(Const.RESULT_DATA  , Const.NOT_EXIST);
			return result;
		}
		// 임시패스워드 메일 발송
		mailSend(this.templatePassWordGenerate(user));
		
		//사용자 비밀번호 초기화
		this.initPwByUserId(user);
		
		result.put(Const.RESULT_STATUS, Const.SUCCESS);
		return result;
	}

	/**
	 * 사용자 비밀번호 초기화 (조건 userId)
	 * @param user
	 */
	private void initPwByUserId(Map<String, Object> user) {
		userRepository.initPwByUserId(user);
	}


	/**
	 * Template 용 password Generate
	 * @param user
	 * @return
	 */
	private Map<String, Object> templatePassWordGenerate(Map<String, Object> user) {
		String tempPassword = passwordGenerator.generate();
		String encryptedPassword = passwordEncryptor.encryptPw(tempPassword);
		user.put("password", encryptedPassword);
		user.put("tempPw", tempPassword);

		return user;
	}

	/**
	 * 메일을 전송한다. 
	 *
	 * @author : JongHyeok Choi
	 * @param param the param
	 * @Date : 2016. 8. 16
	 * @Method Name : mailSend
	 */
	private void mailSend(Map user) {
		user.put("url", buyerMainUrl);
		mailService.sendAsync("INIT_PW_ML", null, user);
	}


	/**
	 * 계정 잠김해제를 저장한다.
	 * @Method Name : saveInfoAccLockYn
	 */
	public ResultMap saveUserAccLockYn(Map param) {
		// 계정 잠김 상태 해제
		userRepository.saveUserAccLockYn(param);

		ResultMap resultMap = ResultMap.getInstance();
		resultMap.setResultStatus(ResultMap.STATUS.SUCCESS);
		return resultMap;
	}


	/**
	 * 패스워드 초기화 시 result page url setting
	 * @param param
	 * @return
	 */
	public String passwordResultPageProcess(Map param) {
		String resultPage = "portal/bp/result/";
		String userId = param.getOrDefault("username","") == null?  "" : (String) param.getOrDefault("username","");
		param.put("usr_id", userId);

		Map resultPasswordData = this.initPassword(param);
		String getResultStatus = resultPasswordData.getOrDefault(Const.RESULT_STATUS,"") == null?  "" : (String) resultPasswordData.getOrDefault(Const.RESULT_STATUS,"");
		String getResultData = resultPasswordData.getOrDefault(Const.RESULT_DATA,"") == null?  "" : (String) resultPasswordData.getOrDefault(Const.RESULT_DATA,"");

		if (Const.FAIL.equals(getResultStatus)) {
			if (Const.UNAUTHORIZED.equals(getResultData)) {
				resultPage += "contactRes"; // 이메일 수신 거부 사용자
			} else if (Const.NOT_EXIST.equals(getResultData)) {
				resultPage += "noUser"; // 회원 정보 불일치 페이지
			}
		} else {
			resultPage += "successMailSend"; // 성공 페이지
		}
		return resultPage;
	}
	
	public List findListUserByDept(Map param) {
		return userRepository.findListUserByDept(param);
	}
}
