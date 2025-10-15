
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
    BorrowEntity findBorrowById(Long id);
    List<BorrowEntity> findByCost(Integer cost);

    List<BorrowEntity> findByBorrowDate(Date borrowDate); //especifico
    List<BorrowEntity> findByBorrowDateBetween(Date startDate, Date endDate); //entre 2 fechas
    List<BorrowEntity> findByReturnDateBefore(Date date); //antes de cierta fecha
    List<BorrowEntity> findByReturnDateAfter(Date date); //despues de cierta fecha
    List<BorrowEntity> findByBorrowedTools(List<ToolEntity> borrowedTools);
    List<BorrowEntity> findByResponsible(UserEntity responsible);
    List<BorrowEntity> findByClient(ClientEntity client);
    List<BorrowEntity> findByBorrowState(BorrowEntity.BorrowState borrowState);

    // Préstamos no devueltos (activos o atrasados)
    @Query(value =
            "SELECT * FROM borrows " +
                    "WHERE borrow_state IN ('Active', 'Overdue')",
            nativeQuery = true)
    List<BorrowEntity> findActiveBorrows();

    // Préstamos atrasados
    @Query(value =
            "SELECT * FROM borrows " +
                    "WHERE return_date < CURRENT_DATE " +
                    "AND borrow_state = 'Overdue'",
            nativeQuery = true)
    List<BorrowEntity> findOverdueBorrows();

    // Préstamos de un cliente con multa asociada
    @Query(value =
            "SELECT DISTINCT b.* " +
                    "FROM borrows b " +
                    "JOIN fines f ON b.client_id = f.client_id " +
                    "WHERE b.client_id = :clientId",
            nativeQuery = true)
    List<BorrowEntity> findBorrowsWithFinesByClient(@Param("clientId") Long clientId);
}
