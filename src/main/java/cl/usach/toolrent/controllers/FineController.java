package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fine")
@CrossOrigin("*")
//SOLO PARA CONSULTAS, LAS MULTAS SE CREAN DESDE BORROW
public class FineController {
    @Autowired
    private FineService fineService;

    // Obtener todas las multas
    @GetMapping("/all")
    public ResponseEntity<List<FineEntity>> getAllFine() {
        List<FineEntity> fines = fineService.getAllFine();
        return ResponseEntity.ok(fines);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FineEntity> getFineById(@PathVariable Long id){
        return ResponseEntity.ok(fineService.getFineById(id));
    }
}
