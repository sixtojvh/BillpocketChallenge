package BillpocketChallenge.sixto.persistence.service.users;

import BillpocketChallenge.sixto.entities.Request.UserRequest;
import BillpocketChallenge.sixto.entities.response.JsonWebToken;
import BillpocketChallenge.sixto.entities.users.UserEntity;
import BillpocketChallenge.sixto.persistence.repositories.OTPService.OTPService;
import BillpocketChallenge.sixto.persistence.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    @Override
    public boolean createUser(UserRequest newUser){
        LocalDate date = LocalDate.parse(newUser.getFechaDeNacimiento());

        try {
            UserEntity userResponse = null;



            if (!userRepository.existsByUsername(newUser.getUsername())){
                UserEntity user = new UserEntity();
                user.setNombreCompleto(newUser.getNombreCompleto());
                user.setUsername(newUser.getUsername());
                user.setFechaDeNacimiento(Timestamp.valueOf(date.atStartOfDay()));
                user.setSexo(newUser.getSexo());
                user.setSoftDelete(false);


                /**
                * ENCRYPTED PASS WITH HmacSHA256
                 *
                 * El proceso de encryptado, utiliza como llave la fecha en la que se crea el usuario
                 * con esto podemos calcularla y compararla haciendo una consulta de la fecha y realizando
                 * el mismo prosedimiento
                * */
                String pass = otpService.hotp(
                        newUser.getPassword(),
                        user.getFechaDeNacimiento().getTime(),
                        "HmacSHA256");

                user.setPassword(pass);
                userResponse = userRepository.save(user);
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

    @Override
    public boolean existUser(String userName, String password) throws Exception {

        boolean response = false;
        UserEntity userSaved = userRepository.findByUsername(userName);

        if(userName != null) {
            String pass = otpService.hotp(
                    password,
                    userSaved.getFechaDeNacimiento().getTime(),
                    "HmacSHA256");

            if (pass.equals(userSaved.getPassword()))
                response = true;
        }
        return response;
    }
}
