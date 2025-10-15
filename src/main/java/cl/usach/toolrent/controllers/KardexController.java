package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.MoveEntity;
import cl.usach.toolrent.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/kardex")
@CrossOrigin("*")
public class KardexController {
    @Autowired
    private KardexService kardexService;


    @GetMapping("/report")
    public ResponseEntity<KardexService.Report> generateReport(){
        KardexService.Report report = kardexService.generateReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/moves")
    public ResponseEntity<List<MoveEntity>> findMovesByToolIdAndDateRange(@RequestParam Long toolId, @RequestParam Date date1, @RequestParam Date date2){
        kardexService.findMovesByToolIdAndDateRange(toolId, date1, date2);
        return ResponseEntity.ok(kardexService.findMovesByToolIdAndDateRange(toolId, date1, date2));
    }
}