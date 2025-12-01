package cl.usach.toolrent.repositories;

import cl.usach.toolrent.entities.ToolCategoryEntity;
import cl.usach.toolrent.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
    List<ToolEntity> findByName(String name);
    List<ToolEntity> findByType(ToolEntity.ToolType toolType);
    List<ToolEntity> findByCategory(ToolCategoryEntity toolCategoryEntity);
    List<ToolEntity> findByreplacementValue(Integer replacementValue);
    List<ToolEntity> findByState(ToolEntity.ToolState state);
    List<ToolEntity> findByDailyTariff(Integer dailyTariff);
    List<ToolEntity> findByDamageLevel(ToolEntity.DamageLevel damageLevel);
    List<ToolEntity> findByCategoryAndState(ToolCategoryEntity toolCategoryEntity, ToolEntity.ToolState state);
}
