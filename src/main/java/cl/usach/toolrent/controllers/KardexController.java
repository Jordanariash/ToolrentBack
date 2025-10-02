package cl.usach.toolrent.controllers;
import cl.usach.toolrent.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kardex")
@CrossOrigin("*")
public class KardexController {
    @Autowired
    private KardexService kardexService;
    @PostMapping("")
    public ResponseEntity<KardexService.Report> generateReport(){
        KardexService.Report report = kardexService.generateReport();
        return ResponseEntity.ok(report);
    }
}