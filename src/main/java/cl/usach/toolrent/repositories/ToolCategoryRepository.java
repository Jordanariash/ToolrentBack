package cl.usach.toolrent.repositories;

import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolCategoryRepository extends JpaRepository<ToolCategory, Long>{
    ToolCategory findByName(String name);
}
