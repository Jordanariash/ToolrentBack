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

    @Query(value =
            "SELECT clients.* " +
            "FROM clients " +
            "JOIN borrows ON clients.id = borrows.client_id " +
            "WHERE borrows.id = :borrowId",
            nativeQuery = true)
    ClientEntity findClientByBorrowId(@Param("borrowId") Long borrowId);

    @Query(value =
            "SELECT clients.* " +
            "FROM clients " +
            "JOIN fines ON clients.id = fines.client_id " +
            "WHERE fines.id = :fineId",
            nativeQuery = true)
    ClientEntity findClientByFineId(@Param("fineId") Long fineId);

    @Query(value =
            "SELECT clients.* " +
            "FROM clients " +
            "JOIN borrows ON clients.id = borrows.client_id " +
            "WHERE borrows.borrowed_tool_id = :toolId " +
            "AND borrows.returned = false",
            nativeQuery = true)
    ClientEntity findClientByBorrowedTool(@Param("toolId") Long toolId);






}
