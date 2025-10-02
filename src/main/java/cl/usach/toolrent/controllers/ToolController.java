package cl.usach.toolrent.controllers;
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
@RequestMapping("/tools")
@CrossOrigin("*")
public class ToolController {
    @Autowired
    ToolService toolService;

    @GetMapping("")
    public ResponseEntity<List<ToolEntity>> getAvailableTools() {
        List<ToolEntity> tools = toolService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }

    @PutMapping("")
    public ResponseEntity<ToolEntity> changeState(ToolEntity tool, ToolEntity.ToolState toolState) {
        toolService.changeState(tool, toolState);
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<ToolEntity> changeReplacementValue(ToolEntity tool, Integer replacementValue, UserEntity user) {
        toolService.changeReplacementValue(tool, replacementValue, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<ToolEntity> changeDailyTariff(ToolEntity tool,Integer dailyTariff, UserEntity user) {
        toolService.changeDailyTariff(tool, dailyTariff, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<ToolEntity> changeDamageLevel(ToolEntity tool, ToolEntity.DamageLevel damageLevel){
        toolService.changeDamageLevel(tool, damageLevel);
        return ResponseEntity.ok().build();
    }
}