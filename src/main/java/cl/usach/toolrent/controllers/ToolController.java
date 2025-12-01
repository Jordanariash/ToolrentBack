package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.ToolCategoryEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
@CrossOrigin("*")
public class ToolController {
    @Autowired
    ToolService toolService;

    @GetMapping("/tools")
    public ResponseEntity<List<ToolEntity>> getAllTools() {
        List<ToolEntity> tools = toolService.getAllTools();
        return ResponseEntity.ok(tools);
    }
    @GetMapping("/availableTools")
    public ResponseEntity<List<ToolEntity>> getAvailableTools() {
        List<ToolEntity> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }
    @GetMapping("/availableAndCategoryTools")
    public ResponseEntity<List<ToolEntity>> getAvailableToolsByCategory(ToolCategoryEntity toolCategoryEntity){
        List<ToolEntity> tools = toolService.getAvailableToolsByCategory(toolCategoryEntity);
        return ResponseEntity.ok(tools);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ToolEntity> getToolById(@PathVariable Long id) {
        ToolEntity tool = toolService.getToolById(id);
        return ResponseEntity.ok(tool);
    }


    @PutMapping("/borrow")
    public ResponseEntity<ToolEntity> markAsBorrowed(@RequestParam Long toolId) {
        toolService.markAsBorrowed(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/mark-available")
    public ResponseEntity<ToolEntity> markAsAvailable(@RequestParam Long toolId) {
        toolService.markAsAvailable(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/repair")
    public ResponseEntity<ToolEntity> markAsInRepair(@RequestParam Long toolId) {
        toolService.markAsInRepair(toolId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/mark-out-of-service")
    public ResponseEntity<ToolEntity> markAsOutOfService(@RequestParam Long toolId) {
        toolService.markAsOutOfService(toolId);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/damage-level")
    public ResponseEntity<ToolEntity> changeDamageLevel(@RequestParam Long toolId,@RequestParam  ToolEntity.DamageLevel damageLevel){
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
