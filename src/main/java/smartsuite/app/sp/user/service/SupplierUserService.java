package smartsuite.app.sp.user.service;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartsuite.app.common.shared.Const;
import smartsuite.security.event.CertificateEventPublisher;
import smartsuite.app.sp.user.repository.SupplierUserRepository;
import smartsuite.security.account.service.AccountService;
import smartsuite.security.core.crypto.AESIvParameterGenerator;
import smartsuite.security.core.crypto.AESSecretKeyGenerator;
import smartsuite.security.core.crypto.CipherUtil;
import smartsuite.security.web.crypto.AESCipherKey;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 협력사 사용자 관련 처리하는 서비스 Class입니다.
 *
 * @author JuEung Kim
 * @see 
 * @FileName VendorUserService.java
 * @package smartsuite.app.bp.admin.auth
 * @Since 2016. 11. 2
 * @변경이력 : [2016. 11. 2] JuEung Kim 최초작성
 */
@Service
@Transactional
@SuppressWarnings ({ "unchecked" , "rawtypes"})
public class SupplierUserService {
	
	
	@Inject
	AccountService accountService;
	
	@Inject
	AESSecretKeyGenerator keyGenerator;
	
	@Inject
	AESIvParameterGenerator parameterGenerator;
	
	@Inject
	CipherUtil cipherUtil;

	@Inject
	CertificateEventPublisher certificateEventPublisher;
	
	@Inject
	SupplierUserRepository supplierUserRepository;

	
	/**
	 * 협력사 사용자 목록을 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the list< map< string, object>>
	 * @Date : 2016. 11. 2
	 * @Method Name : findListVendorUser
	 */
	public List<Map<String,Object>> findListVendorUser(Map param) {		
		return supplierUserRepository.findListVendorUser(param);
	}
	
	/**
	 * 협력사 사용자 정보를 조회한다.
	 *
	 * @author : JuEung Kim
	 * @param param the param
	 * @return the map
	 * @Date : 2016. 11. 2
	 * @Method Name : findVendorUserInfo
	 */
	public Map findVendorUserInfo(Map param) {
		Map vendorUserInfo = supplierUserRepository.findVendorUserInfo(param);
		Date pwModDt = (Date) vendorUserInfo.getOrDefault("pw_mod_dt",null);

		if(MapUtils.isNotEmpty(vendorUserInfo) && null != pwModDt){
			if(!accountService.isCredentialsNonExpired(pwModDt)) {
				vendorUserInfo.put("pw_expired_yn", "Y");
			}
		}
		vendorUserInfo.put("limitLoginInvalidPw",accountService.getAccountSettings().getLimitLoginInvalidPasswordCount());
		return vendorUserInfo;
	}

	/**
	 * 사업자번호 확인
	 *
	 * @author : lee daesung
	 * @param param the param
	 * @return the map
	 * @Date : 2020. 3. 05
	 * @Method Name : checkBizRegNo
	 */
	public Map<String,Object> checkBizRegNo(Map<String,Object> param){
		Map<String,Object> result = new HashMap<String,Object>();
		//사업자 번호 가져오기,
		String bizRegNo = supplierUserRepository.checkBizRegNo(param);
		if(StringUtils.isNotEmpty(bizRegNo)) {
			result.put("bizregno", bizRegNo);
			result.put(Const.RESULT_STATUS, Const.SUCCESS);
		}else {
			result.put(Const.RESULT_STATUS, Const.FAIL);
		}
		return result;
	}
	
	/**
	 * 공인 인증서 로그인에 서명할 데이터 조회
	 *
	 * @author : lee daesung
	 * @param param the param
	 * @return the map
	 * @Date : 2020. 3. 05
	 * @Method Name : getCertLoginSignValue
	 */
	public Map<String,Object> getCertLoginSignValue(Map<String,Object> param){
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> signContentInfo = new HashMap<String,Object>();
		signContentInfo = supplierUserRepository.getCertLoginSignValue(param);
		String signSource = (String)signContentInfo.get("sign_source");
		
		//서명소스에 랜덤값 추가하기
		String random = certificateEventPublisher.getRandomString(20,"NLU");

		signSource += random;
		signContentInfo.put("usr_id", param.get("username"));
		signContentInfo.put("tenant", param.get("tenant"));
		signContentInfo.put("hash_value", certificateEventPublisher.getHashValueFromStr(signSource));
		signContentInfo.put("callbackUrl", "completeCertLogin.do");
		result.put("signContentInfo", signContentInfo);
		result.put("_aesCipherKey", new AESCipherKey(keyGenerator.getKey(), keyGenerator.getPassPhrase(), keyGenerator.getIterationCount(), parameterGenerator.getIv()));
		
		return result;
	}
	
	/**
	 * 공인 인증서 로그인 전자서명 이후 완료 로직
	 *
	 * @author : lee daesung
	 * @param param the param
	 * @return void
	 * @Date : 2020. 3. 05
	 * @Method Name : completeCertLogin
	 */
	public String completeCertLogin(Map<String,Object> param){
		
		verifyCertLogin(param);
		String signSource = param.getOrDefault("sign_source","") == null? "" : (String) param.getOrDefault("sign_source","");
		param.put("vd_cd", signSource);
		this.updateVendorSignValue(param);

		String signValue = param.getOrDefault("sign_value","") == null? "" :  (String) param.getOrDefault("sign_value","");
		return certificateEventPublisher.getHashValueFromStr(signValue);
	}

	/**
	 * 공인인증서 로그인 전자서명 이후 업체 유저 정보에 업데이트
	 * @param param
	 */
	private void updateVendorSignValue(Map<String, Object> param) {
		supplierUserRepository.updateVendorSignValue(param);
	}


	/**
	 * 서명값 검증, 인증서 검증
	 *
	 * @author : lee daesung
	 * @param param the param
	 * @return void
	 * @Date : 2020. 3. 05
	 * @Method Name : completeCesign_valuetLogin
	 */
	private void verifyCertLogin(Map<String,Object> param){
		String rvalue = param.getOrDefault("rvalue","") == null? "" : (String) param.getOrDefault("rvalue","");
		String ssn = param.getOrDefault("ssn","") == null? "" : (String) param.getOrDefault("ssn","");
		String signValue = param.getOrDefault( "sign_value","") == null? "" : (String) param.getOrDefault("sign_value","");
		rvalue = cipherUtil.decrypt(rvalue); //rvalue 복호화
		certificateEventPublisher.verifySignValueAndIdentification(signValue, rvalue, ssn);
	}
	
}
