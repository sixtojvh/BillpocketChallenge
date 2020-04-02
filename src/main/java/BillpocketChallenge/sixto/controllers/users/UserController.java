package BillpocketChallenge.sixto.controllers.users;

import BillpocketChallenge.sixto.entities.Request.UserRequest;
import BillpocketChallenge.sixto.entities.response.JsonWebToken;
import BillpocketChallenge.sixto.entities.response.Status;
import BillpocketChallenge.sixto.entities.users.UserEntity;
import BillpocketChallenge.sixto.persistence.service.users.UserServices;
import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@RestController
public class UserController {

    @Value("${APP.NAME}")
    private String APP_NAME;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserServices userServices;

    /**
     * Crear un usuario
     * */
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity createUser (@RequestBody String request,
                                              @RequestHeader HttpHeaders headers) throws Exception {

        Status status = new Status();

        UserRequest newUser = convertJsonToClass(request, UserRequest.class);
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


        UserEntity user = convertJsonToClass(request, UserEntity.class);
        JsonWebToken token = null;
        Status status = new Status();

        if(!StringUtils.isEmpty(user)) {

            if(this.validateDataJwt(user)){
                final boolean existUser = userServices.existUser(user.getUsername(), user.getPassword());
                if (!existUser) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                final Instant now = Instant.now();

                final String jwt = Jwts.builder()
                        .setSubject(user.getUsername())
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
                        .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(secret))
                        .compact();

                token = new JsonWebToken();
                token.setToken(jwt);
                return new ResponseEntity(token.stringify(), HttpStatus.OK);
            }
        }

        return new ResponseEntity(status.stringify(), HttpStatus.BAD_REQUEST);
    }

    /**
     * obtenga los datos del usuario autenticado mediante el JWT provisto
     * */
    @RequestMapping(value = "/me", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity authenticateUser (@RequestBody String request,
                                            @RequestHeader HttpHeaders headers) throws Exception {

        final String bearer = headers.getFirst("authorization");

        UserEntity newUser = convertJsonToClass(request, UserEntity.class);

        if (newUser == null || !bearer.startsWith("Bearer "))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        final String token = bearer.substring(7);
        final Claims claims;
        try {
             claims = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.encode(secret))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (final JwtException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!StringUtils.isEmpty(claims)){
            Calendar calendar = Calendar.getInstance();
            Date date = claims.getExpiration();

            if(new Timestamp(calendar.getTimeInMillis()).after(new Timestamp(date.getTime())))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            UserEntity user = userServices.getUser(claims.getSubject());
            if(user != null)
                return new ResponseEntity(user.stringify(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    public boolean validateDataJwt(UserEntity user){
        if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()))
            return false;
        else
            return true;
    }

}
