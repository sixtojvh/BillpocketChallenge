package BillpocketChallenge.sixto.persistence.service.users;

import BillpocketChallenge.sixto.entities.users.UserEntity;
import BillpocketChallenge.sixto.persistence.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean createUser(UserEntity newUser){
        Calendar current = Calendar.getInstance();
        try {
            UserEntity userResponse = null;

            if (!userRepository.existsByUsername(newUser.getUsername())){
                newUser.setFechaDeNacimiento(new Timestamp(current.getTimeInMillis()));
                newUser.setSoftDelete(false);
                userResponse = userRepository.save(newUser);
            }

            if (userResponse != null)
                return true;

            return false;

        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public UserEntity getUser(String userName) throws Exception {
        return userRepository.findByUsername(userName);
    }
}
