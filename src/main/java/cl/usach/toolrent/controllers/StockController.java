package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.StockEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockController {
    @Autowired
    private StockService stockService;

    @PutMapping("")
    public ResponseEntity<StockEntity> addTool(int quantity, ToolEntity tool, Long userId){
        stockService.addTool(quantity,tool,userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<StockEntity> removetTool(Long toolId, Long userId){
        stockService.removeTool(toolId,userId);
        return ResponseEntity.ok().build();
    }
}
