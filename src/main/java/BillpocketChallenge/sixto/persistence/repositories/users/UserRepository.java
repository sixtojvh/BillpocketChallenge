package BillpocketChallenge.sixto.persistence.repositories.users;

import BillpocketChallenge.sixto.entities.users.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername (@Param("username") String username);
    UserEntity findByUsername (@Param("username") String username);

}
