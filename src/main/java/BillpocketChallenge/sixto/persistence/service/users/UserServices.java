package BillpocketChallenge.sixto.persistence.service.users;

import BillpocketChallenge.sixto.entities.response.JsonWebToken;
import BillpocketChallenge.sixto.entities.users.UserEntity;

public interface UserServices {

    boolean createUser (UserEntity newUser) throws Exception;
    UserEntity getUser (String  userName) throws Exception;
    boolean  existUser (String  userName, String password) throws Exception;

}
