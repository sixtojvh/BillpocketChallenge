package BillpocketChallenge.sixto.entities.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "User", schema = "users")
@NamedQuery(name = "userEntity.findAll", query = "SELECT a from userEntity a")
@Where(clause = "soft_delete = 0")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class userEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    private String Nombre;
    private String Completo;
    private String Username;
    private String Password;
    private Timestamp FechaDeNacimiento;
    private String Sexo;
    private boolean softDelete;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCompleto() {
        return Completo;
    }

    public void setCompleto(String completo) {
        Completo = completo;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Timestamp getFechaDeNacimiento() {
        return FechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Timestamp fechaDeNacimiento) {
        FechaDeNacimiento = fechaDeNacimiento;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }

    public List<userEntity> toList (String list) throws IOException {
        return new ObjectMapper().readValue(list, new TypeReference<List<userEntity>>(){});
    }

    public String toJson () throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String stringify() throws Exception {
        ObjectWriter mapper = new ObjectMapper().writer();
        return mapper.writeValueAsString(this);
    }

}
