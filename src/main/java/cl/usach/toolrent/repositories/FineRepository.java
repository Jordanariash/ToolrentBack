package cl.usach.toolrent.repositories;

import cl.usach.toolrent.entities.BorrowEntity;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.entities.ToolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {
    public FineEntity findFineById(Long id);
    List<FineEntity> findByDelayDays(Integer delayDays);
    List<FineEntity> findByAmount(Float amount);
    List<FineEntity> findByType(FineEntity.FineType fineType);
    List<FineEntity> findByClient(ClientEntity client);
    List<FineEntity> findByBorrow(BorrowEntity borrow);

    @Query(value =
            "SELECT SUM(fines.amount) " +
                    "FROM fines " +
                    "WHERE fines.client_id = :clientId",
            nativeQuery = true)
    Float getTotalFinesByClient(@Param("clientId") Long clientId);

    @Query(value =
            "SELECT COUNT(fines.id) " +
                    "FROM fines " +
                    "WHERE fines.type = :type",
            nativeQuery = true)
    Long countFinesByType(@Param("type") FineEntity.FineType type);
}