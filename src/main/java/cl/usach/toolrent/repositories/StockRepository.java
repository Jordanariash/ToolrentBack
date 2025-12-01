package cl.usach.toolrent.repositories;
import cl.usach.toolrent.entities.StockEntity;
import cl.usach.toolrent.entities.ToolCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    StockEntity findStockByToolCategoryEntity(ToolCategoryEntity toolCategoryEntity);
    @Query(value =
            "SELECT stocks.* " +
                    "FROM stocks " +
                    "JOIN tool_categories ON stocks.category_id = tool_categories.id " +
                    "WHERE tool_categories.id = :categoryId",
            nativeQuery = true)
    StockEntity findStockByToolCategoryId(@Param("categoryId") Long categoryId);
}