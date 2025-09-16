package cl.usach.toolrent.repositories;

import cl.usach.toolrent.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {
    public BorrowEntity findBorrowById(Long id);
    List<BorrowEntity> findByTool(ToolEntity tool);
    List<BorrowEntity> findByCost(Integer cost);
    List<BorrowEntity> findByDate(Date borrowDate);
    List<BorrowEntity> findByResponsible(UserEntity responsible);
    List<BorrowEntity> findByClient(ClientEntity client);
}
