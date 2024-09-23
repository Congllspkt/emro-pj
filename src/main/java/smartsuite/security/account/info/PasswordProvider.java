package smartsuite.security.account.info;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import smartsuite.security.authentication.PasswordGenerator;
import smartsuite.security.core.authentication.encryption.PasswordEncryptor;
import smartsuite.security.core.crypto.AESIvParameterGenerator;
import smartsuite.security.core.crypto.AESSecretKeyGenerator;
import smartsuite.security.core.crypto.CipherUtil;

/**
 * PasswordProvider Class 입니다.
 */
@Service
public class PasswordProvider {
	
	/** The use default password. */
	@Value ("#{globalProperties['use-default-password']}")
	private Boolean useDefaultPassword;
	
	/** The default password. */
	@Value ("#{globalProperties['default-password']}")
	private String defaultPassword;

	/** The password generator. */
	@Inject
	PasswordGenerator passwordGenerator;
	
	/** The password encryptor. */
	@Inject
	PasswordEncryptor passwordEncryptor;

	
	/**
	 * Password generator.
	 *
	 * @return the string
	 */
	public String passwordGenerator(){
		return useDefaultPassword ? defaultPassword : passwordGenerator.generate();
	}
	
	/**
	 * Password encryptor.
	 *
	 * @param tempPassword the temp password
	 * @return the string
	 */
	public String passwordEncryptor(String tempPassword){
		return passwordEncryptor.encryptPw(tempPassword);
	}
}
