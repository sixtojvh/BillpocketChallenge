package BillpocketChallenge.sixto.persistence.service.users;

import BillpocketChallenge.sixto.entities.users.UserEntity;

public interface UserServices {

    boolean createUser (UserEntity newUser) throws Exception;
    UserEntity getUser (String  userName) throws Exception;

}
