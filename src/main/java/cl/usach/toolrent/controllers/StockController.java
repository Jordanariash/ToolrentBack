package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.StockEntity;
import cl.usach.toolrent.entities.ToolCategoryEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.services.StockService;
import cl.usach.toolrent.services.ToolCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private ToolCategoryService toolCategoryService;

    // Para addNewTool
    public static class AddNewToolRequest {
        public int quantity;
        public String name;
        public ToolEntity.ToolType toolType;
        public Long categoryId;  // SOLO EL ID, no el objeto completo
        public Integer replacementValue;
        public Integer dailyTariff;
        public Long userId;
    }

    // Para addExistingTool
    public static class AddExistingToolRequest {
        public int quantity;
        public Long toolId; // enviamos solo el id de la herramienta existente
        public Long userId;
    }

    // Para removeTool
    public static class RemoveToolRequest {
        public Long toolId;
        public Long userId;
    }

    // Para removeOutOfServiceTools
    public static class RemoveOutOfServiceToolsRequest {
        public ToolCategoryEntity toolCategoryEntity;
        public Long userId;
    }

    // Para removeAvailableTools
    public static class RemoveAvailableToolsRequest {
        public ToolCategoryEntity toolCategoryEntity;
        public Integer quantity;
        public Long userId;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockEntity>> getAllStocks() {
        List<StockEntity> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<StockEntity> getStockByCategory(@PathVariable Long categoryId) {
        StockEntity stock = stockService.getStockByCategory(categoryId);
        return ResponseEntity.ok(stock);
    }

    @PutMapping("/add-new")
    public ResponseEntity<Void> addNewTool(@RequestBody AddNewToolRequest request){
        System.out.println("🔥 STOCK CONTROLLER - Recibiendo nueva herramienta:");
        System.out.println("🔥 Cantidad: " + request.quantity);
        System.out.println("🔥 Nombre: " + request.name);
        System.out.println("🔥 Tipo: " + request.toolType);
        System.out.println("🔥 Categoría ID: " + request.categoryId); //️ Cambiar aquí
        System.out.println("🔥 Valor reemplazo: " + request.replacementValue);
        System.out.println("🔥 Tarifa diaria: " + request.dailyTariff);
        System.out.println("🔥 User ID: " + request.userId);

        // Obtener la categoría completa desde la base de datos
        ToolCategoryEntity category = toolCategoryService.getToolCategoryById(request.categoryId);
        if (category == null) {
            throw new RuntimeException("Categoría no encontrada con ID: " + request.categoryId);
        }

        stockService.addNewTool(
                request.quantity,
                request.name,
                request.toolType,
                category,  // ⬅️ Pasar la entidad completa obtenida de la BD
                request.replacementValue,
                request.dailyTariff,
                request.userId
        );
        return ResponseEntity.ok().build();
    }


    public static class AddToolsByCategoryRequest {
        public int quantity;
        public Long categoryId;
        public Long userId;
    }

    @PutMapping("/add-by-category")
    public ResponseEntity<Void> addToolsByCategory(@RequestBody AddToolsByCategoryRequest request) {
        stockService.addToolsByCategory(request.quantity, request.categoryId, request.userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove")
    public ResponseEntity<Void> removeTool(@RequestBody RemoveToolRequest request){
        stockService.removeTool(request.toolId, request.userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove-out-of-service")
    public ResponseEntity<Void> removeOutOfServiceTools(@RequestBody RemoveOutOfServiceToolsRequest request){
        stockService.removeOutOfServiceTools(request.userId, request.toolCategoryEntity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove-available")
    public ResponseEntity<Void> removeAvailableTools(@RequestBody RemoveAvailableToolsRequest request){
        stockService.removeAvailableTools(request.userId, request.toolCategoryEntity, request.quantity);
        return ResponseEntity.ok().build();
    }
}