package BillpocketChallenge.sixto.persistence.repositories.OTPService;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;


@Service
public class OTPServiceImp implements OTPService {

    /**
     *
     * @param keyString
     *            : the shared secret
     * @param time
     *            : a value that reflects a time
     *
     * @param crypto
     *            : the crypto function to use
     *
     * @return: digits
     */
    @Override
    public String hotp(String keyString, long time, String crypto) throws Exception {

            String inputString = keyString;
            Charset charset = StandardCharsets.UTF_16;

            byte[] key = inputString.getBytes(charset);
            byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
            byte[] hash = hmacSha(crypto, key, msg);

            int offset = hash[hash.length - 1] & 0xf;

            int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

            return String.valueOf(binary);
    }

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     *
     * @param crypto
     *            : the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes
     *            : the bytes to use for the HMAC key
     * @param text
     *            : the message or text to be authenticated
     */
    private static  byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }



}
