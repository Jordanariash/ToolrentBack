package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //UserEntity findByUserId(Long userId); //necesario?
    UserEntity findByRut(String rut);
    List<UserEntity> findByName(String name);
    UserEntity findByEmail(String email);
    List <UserEntity> findByRole(UserEntity.Role role);

}
