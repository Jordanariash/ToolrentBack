package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.StockEntity;
import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockController {
    @Autowired
    private StockService stockService;

    // Para addNewTool
    public static class AddNewToolRequest {
        public int quantity;
        public String name;
        public ToolEntity.ToolType toolType;
        public ToolCategory toolCategory;
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
        public ToolCategory toolCategory;
        public Long userId;
    }

    // Para removeAvailableTools
    public static class RemoveAvailableToolsRequest {
        public ToolCategory toolCategory;
        public Integer quantity;
        public Long userId;
    }

    @PutMapping("/add-new")
    public ResponseEntity<Void> addNewTool(@RequestBody AddNewToolRequest request){
        stockService.addNewTool(
                request.quantity,
                request.name,
                request.toolType,
                request.toolCategory,
                request.replacementValue,
                request.dailyTariff,
                request.userId
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add-existing")
    public ResponseEntity<Void> addExistingTool(@RequestBody AddExistingToolRequest request){
        stockService.addExistingTool(request.quantity, request.toolId, request.userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove")
    public ResponseEntity<Void> removeTool(@RequestBody RemoveToolRequest request){
        stockService.removeTool(request.toolId, request.userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove-out-of-service")
    public ResponseEntity<Void> removeOutOfServiceTools(@RequestBody RemoveOutOfServiceToolsRequest request){
        stockService.removeOutOfServiceTools(request.userId, request.toolCategory);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/remove-available")
    public ResponseEntity<Void> removeAvailableTools(@RequestBody RemoveAvailableToolsRequest request){
        stockService.removeAvailableTools(request.userId, request.toolCategory, request.quantity);
        return ResponseEntity.ok().build();
    }
}