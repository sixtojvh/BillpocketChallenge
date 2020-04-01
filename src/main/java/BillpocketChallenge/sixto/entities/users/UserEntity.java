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
@NamedQuery(name = "UserEntity.findAll", query = "SELECT a from UserEntity a")
@Where(clause = "soft_delete = 0")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    private String nombreCompleto;

    private String username;

    private String password;

    private Timestamp fechaDeNacimiento;

    private String sexo;

    private boolean softDelete;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Timestamp fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }

    public List<UserEntity> toList (String list) throws IOException {
        return new ObjectMapper().readValue(list, new TypeReference<List<UserEntity>>(){});
    }

    public String toJson () throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String stringify() throws Exception {
        ObjectWriter mapper = new ObjectMapper().writer();
        return mapper.writeValueAsString(this);
    }

}
