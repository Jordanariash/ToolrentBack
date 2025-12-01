package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
@CrossOrigin("*")

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

    @PutMapping("/{fineId}/pay")
    public ResponseEntity<FineEntity> markFineAsPaid(@PathVariable Long fineId) {
        FineEntity paidFine = fineService.markAsPaid(fineId);
        return ResponseEntity.ok(paidFine);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<FineEntity>> getUnpaidFines() {
        List<FineEntity> unpaidFines = fineService.getUnpaidFines();
        return ResponseEntity.ok(unpaidFines);
    }

    @GetMapping("/client/{clientId}/unpaid")
    public ResponseEntity<List<FineEntity>> getUnpaidFinesByClient(@PathVariable Long clientId) {
        List<FineEntity> unpaidFines = fineService.getUnpaidFinesByClient(clientId);
        return ResponseEntity.ok(unpaidFines);
    }
}
