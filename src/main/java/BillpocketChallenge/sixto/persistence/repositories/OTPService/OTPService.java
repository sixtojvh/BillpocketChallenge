package BillpocketChallenge.sixto.persistence.repositories.OTPService;

public interface OTPService {

    String hotp(String keyString, long time, String crypto) throws Exception;

}
