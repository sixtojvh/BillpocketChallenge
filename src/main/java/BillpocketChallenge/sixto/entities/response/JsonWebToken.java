package BillpocketChallenge.sixto.entities.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;

public class JsonWebToken implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toJson () throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String stringify() throws Exception {
        ObjectWriter mapper = new ObjectMapper().writer();
        return mapper.writeValueAsString(this);
    }
}
