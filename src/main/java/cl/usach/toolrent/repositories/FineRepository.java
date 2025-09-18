package cl.usach.toolrent.repositories;

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
    List<FineEntity> findByDelayPayments(Integer delayPayments);
    List<FineEntity> findByAmount(Integer amount);
    List<FineEntity> findByFineType(FineEntity.FineType fineType);
    public FineEntity findByClient(ClientEntity client);
}
