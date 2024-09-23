package smartsuite.app.common.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smartsuite.security.core.crypto.AsymmetricKeyGenerator;
import smartsuite.security.core.crypto.CipherUtil;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * javax.crypto.Cipher 를 이용한 RSA 알고리즘 암,복호화 유틸 클래스 입니다.
 * 견적입찰용 RSA Cipher Util
 */
public class BidRSACipherUtil implements CipherUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidRSACipherUtil.class);

    AsymmetricKeyGenerator bidKeyGenerator = new BidRSACipherUtil.DefaultRSAKeyGenerator();

    
    public BidRSACipherUtil(AsymmetricKeyGenerator bidKeyGenerator) {
        this.bidKeyGenerator = bidKeyGenerator;
    }

    public BidRSACipherUtil(){
    }
    
    public AsymmetricKeyGenerator getBidKeyGenerator() {
		return bidKeyGenerator;
	}

	public void setBidKeyGenerator(AsymmetricKeyGenerator bidKeyGenerator) {
		this.bidKeyGenerator = bidKeyGenerator;
	}
    
    @Override
    public String encrypt(String plainText) {
        String encryptText = "";
        try{
            encryptText = Base64.encodeBase64String(Hex.encodeHexString(encryptToByteArray(plainText)).getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return encryptText;
    }

    
    public byte[] encryptToByteArray(String plainText) {
        Cipher cipher = null;
        byte[] plainTextByte = null;

        try{
            Key publicKey = bidKeyGenerator.genPublicKey();
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            plainTextByte = cipher.doFinal(plainText.getBytes("UTF-8"));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return plainTextByte;
    }

    
    @Override
    public String decrypt(String encryptedText) {
        String decryptText = "";
        try{
            decryptText = new String(decryptToByteArray(encryptedText), "UTF-8");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return decryptText;
    }

    
    public byte[] decryptToByteArray(String encryptedText) {
        Cipher cipher = null;
        byte[] decryptTextByte = null;
        byte[] hexBytes = null;
        Key privateKey = null;

        try{
            privateKey = bidKeyGenerator.genPrivateKey();
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            hexBytes = Base64.decodeBase64(encryptedText.getBytes("UTF-8"));
            decryptTextByte = cipher.doFinal(Hex.decodeHex(new String(hexBytes, "UTF-8").toCharArray()));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return decryptTextByte;
    }

    public class DefaultRSAKeyGenerator implements AsymmetricKeyGenerator {

        private final KeyPair keyPair;

        private static final int KEY_SIZE = 1024;

        
        public DefaultRSAKeyGenerator() {
            KeyPairGenerator keyPairGenerator;

            KeyPairGenerator key = null;
            try{
                key = KeyPairGenerator.getInstance("RSA");
            }catch (Exception e){
                LOGGER.error(e.getMessage());
            }
            keyPairGenerator = key;
            keyPairGenerator.initialize(KEY_SIZE);
            keyPair = keyPairGenerator.genKeyPair();
        }

        @Override
        public Key genKey() {
            return genPublicKey();
        }

        @Override
        public PublicKey genPublicKey() {
            return keyPair.getPublic();
        }

        @Override
        public PrivateKey genPrivateKey() {
            return keyPair.getPrivate();
        }
    }

}
