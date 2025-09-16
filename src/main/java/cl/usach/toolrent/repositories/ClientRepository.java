package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findClientById(Long id);
    public List<ClientEntity> findByState(ClientEntity.ClientState state);
    public List<ClientEntity> findByTelephoneNumber(String telephoneNumber);
    public List<ClientEntity> findByEmail(String email);
    public List<ClientEntity> findByCanBorrow(Boolean canBorrow);



}
