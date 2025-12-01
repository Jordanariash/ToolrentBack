package cl.usach.toolrent.repositories;

import cl.usach.toolrent.entities.ToolCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolCategoryRepository extends JpaRepository<ToolCategoryEntity, Long>{
    ToolCategoryEntity findToolCategoryEntityByName(String name);

}
