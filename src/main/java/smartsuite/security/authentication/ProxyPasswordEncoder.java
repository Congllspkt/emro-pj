package smartsuite.security.authentication;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProxyPasswordEncoder implements PasswordEncoder {

		private final ConfigurablePasswordEncryptor encryptor;
		
		public ProxyPasswordEncoder(String algorithm){
			encryptor = new ConfigurablePasswordEncryptor();
			encryptor.setAlgorithm(algorithm);
			encryptor.setPlainDigest(true);
		}

		@Override
		public String encode(CharSequence rawPassword) {
			return encryptor.encryptPassword(rawPassword.toString());
		}

		@Override
		public boolean matches(CharSequence rawPassword, String encodedPassword) {
			return encryptor.checkPassword(rawPassword.toString(), encodedPassword);
		}
}