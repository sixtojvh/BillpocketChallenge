package BillpocketChallenge.sixto.entities.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.Serializable;

public class Status implements Serializable {

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String stringify() throws Exception {
        ObjectWriter mapper = new ObjectMapper().writer();
        return mapper.writeValueAsString(this);
    }
}
