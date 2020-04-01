package BillpocketChallenge.sixto.controllers.users;

import BillpocketChallenge.sixto.entities.response.Status;
import BillpocketChallenge.sixto.entities.users.UserEntity;
import BillpocketChallenge.sixto.persistence.service.users.UserServices;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Value("${APP.NAME}")
    private String APP_NAME;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServices userServices;

    /**
     * Crear un usuario
     * */
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createUser (@RequestBody String request,
                                              @RequestHeader HttpHeaders headers) throws Exception {

        Status status = new Status();

        UserEntity newUser = convertJsonToClass(request, UserEntity.class);
        if (!StringUtils.isEmpty(newUser)) {

            boolean response = userServices.createUser(newUser);
            status.setStatus(response ? 1 : 0);
            return new ResponseEntity(status.stringify(), HttpStatus.OK);
        }
        return new ResponseEntity("Error no controlado en el proceso", HttpStatus.BAD_REQUEST);

    }

    /**
     * obtener los detalles de un usuario utilizando su username
     * */
    @RequestMapping(value = "/users/{username}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getUser (@PathVariable String username,
                                   @RequestHeader HttpHeaders headers) throws Exception {

        Status status = new Status();
        if(!StringUtils.isEmpty(username)){
            UserEntity user = userServices.getUser(username);
            if(user != null)
                return new ResponseEntity(user.stringify(), HttpStatus.OK);
        }
        return new ResponseEntity(status.stringify(), HttpStatus.BAD_REQUEST);
    }

    /**
     * utilizando el username y password de un usuario, para generar un JWT
     * */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity authenticateJWT (@RequestBody String request,
                                           @RequestHeader HttpHeaders headers) throws Exception {


        UserEntity newUser = convertJsonToClass(request, UserEntity.class);
        if(!StringUtils.isEmpty(newUser)){

           // UserEntity user = userServices.getUser()
        }
        return new ResponseEntity("Error no controlado en el proceso", HttpStatus.BAD_REQUEST);
    }

    /**
     * obtenga los datos del usuario autenticado mediante el JWT provisto
     * */
    @RequestMapping(value = "/me", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity authenticateUser (@RequestBody String request,
                                            @RequestHeader HttpHeaders headers) throws Exception {

        UserEntity newUser = convertJsonToClass(request, UserEntity.class);
        if(!StringUtils.isEmpty(newUser)){

        }
        return null;
    }

    /**
     * Retorna una serializacion con respecto a la clase <T> pasada por parametro
     *
     * @param objJson
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T convertJsonToClass(String objJson, Class<T> clazz) {
        return new Gson().fromJson(objJson, clazz);
    }

}
