package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.StockEntity;
import cl.usach.toolrent.entities.ToolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    StockEntity findStockById(Long id);
    StockEntity findStockByToolCategory(ToolCategory toolCategory);
}