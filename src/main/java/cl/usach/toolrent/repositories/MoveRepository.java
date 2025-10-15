package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.MoveEntity;
import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<MoveEntity, Long> {
    public MoveEntity findMoveById(Long id);
    List<MoveEntity> findByDate(Date date);
    List<MoveEntity> findByDateBetween(Date date1, Date date2);
    List<MoveEntity> findByType(MoveEntity.MovementType type);
    List<MoveEntity> findByResponsible(UserEntity responsible);

    // Obtener todos los movimientos de un usuario
    @Query(value =
            "SELECT * FROM moves " +
                    "WHERE user_id = :userId",
            nativeQuery = true)
    List<MoveEntity> findMovesByUserId(@Param("userId") Long userId);

    // Obtener todos los movimientos asociados a una herramienta específica
    @Query(value =
            "SELECT m.* FROM moves m " +
                    "JOIN move_tools mt ON m.id = mt.move_id " +
                    "WHERE mt.tool_id = :toolId",
            nativeQuery = true)
    List<MoveEntity> findMovesByToolId(@Param("toolId") Long toolId);

    // Contar movimientos de un tipo específico (Add, Remove, Borrow, Return)
    @Query(value =
            "SELECT COUNT(m.id) " +
                    "FROM moves m " +
                    "WHERE m.type = :type",
            nativeQuery = true)
    Long countMovesByType(@Param("type") MoveEntity.MovementType type);

    // Movimientos realizados entre dos fechas
    @Query(value =
            "SELECT * FROM moves " +
                    "WHERE date BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<MoveEntity> findMovesBetweenDates(@Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    // Sumar la cantidad total afectada en movimientos de un tipo
    @Query(value =
            "SELECT SUM(quantity_affected) " +
                    "FROM moves " +
                    "WHERE type = :type",
            nativeQuery = true)
    Integer sumQuantityByType(@Param("type") String type);

    // Obtener todos los movimientos de una herramienta específica dentro de un rango de fechas
    @Query(value =
            "SELECT m.* FROM moves m " +
                    "JOIN move_tools mt ON m.id = mt.move_id " +
                    "WHERE mt.tool_id = :toolId " +
                    "AND m.date BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<MoveEntity> findMovesByToolIdAndDateRange(@Param("toolId") Long toolId,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);

}