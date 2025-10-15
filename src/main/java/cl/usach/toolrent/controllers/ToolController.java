package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.entities.UserEntity;
import cl.usach.toolrent.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tool")
@CrossOrigin("*")
public class ToolController {
    @Autowired
    ToolService toolService;

    @GetMapping("/availableTools")
    public ResponseEntity<List<ToolEntity>> getAvailableTools() {
        List<ToolEntity> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }
    @GetMapping("/availableAndCategoryTools")
    public ResponseEntity<List<ToolEntity>> getAvailableToolsByCategory(ToolCategory toolCategory){
        List<ToolEntity> tools = toolService.getAvailableToolsByCategory(toolCategory);
        return ResponseEntity.ok(tools);
    }



    @PutMapping("/borrow")
    public ResponseEntity<ToolEntity> markAsBorrowed(Long toolId) {
        toolService.markAsBorrowed(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/available")
    public ResponseEntity<ToolEntity> markAsAvailable(Long toolId) {
        toolService.markAsAvailable(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/repair")
    public ResponseEntity<ToolEntity> markAsInRepair(Long toolId) {
        toolService.markAsInRepair(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/out")
    public ResponseEntity<ToolEntity> markAsOutOfService(Long toolId) {
        toolService.markAsOutOfService(toolId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/changeDamageLevel")
    public ResponseEntity<ToolEntity> changeDamageLevel(Long toolId, ToolEntity.DamageLevel damageLevel){
        toolService.changeDamageLevel(toolId, damageLevel);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/changeReplacementValue")
    public ResponseEntity<ToolEntity> changeReplacementValue(Long toolId, Integer replacementValue, Long userId) {
        toolService.changeReplacementValue(toolId, replacementValue, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changeDailyTariff")
    public ResponseEntity<ToolEntity> changeDailyTariff(Long toolId,Integer dailyTariff, Long userId) {
        toolService.changeDailyTariff(toolId, dailyTariff, userId);
        return ResponseEntity.ok().build();
    }

}
