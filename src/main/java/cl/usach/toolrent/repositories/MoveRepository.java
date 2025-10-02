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
    List<MoveEntity> findByMovementType(MoveEntity.MovementType type);
    List<MoveEntity> findByResponsible(UserEntity responsible);
}
