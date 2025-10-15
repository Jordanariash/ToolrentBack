package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.BorrowEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.services.BorrowService;
import cl.usach.toolrent.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/borrow")
@CrossOrigin("*")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // Clase para recibir la información de un nuevo préstamo
    public static class BorrowRequest {
        public Long clientId;
        public List<Long> toolIds;
        public Long userId;
        public Date returnDate;
    }

    // Clase para recibir la información de la devolución
    public static class ReturnRequest {
        public Long borrowId;
        public Date returnDate;
        public Long userId;
        public List<ToolEntity.DamageLevel> damages;
    }


    @PostMapping("/borrowTools")
    public ResponseEntity<BorrowEntity> createBorrow(@RequestBody BorrowRequest request) {
        BorrowEntity newBorrow = borrowService.createBorrow(
                request.clientId,
                request.toolIds,
                request.userId,
                request.returnDate
        );
        return ResponseEntity.ok(newBorrow);
    }

    @PutMapping("/returnTools")
    public ResponseEntity<BorrowEntity> returnBorrow(@RequestBody ReturnRequest request) {
        borrowService.returnBorrow(
                request.borrowId,
                request.returnDate,
                request.userId,
                new ArrayList<>(request.damages)
        );
        return ResponseEntity.ok(borrowService.getBorrowById(request.borrowId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BorrowEntity>> getActiveBorrows() {
        return ResponseEntity.ok(borrowService.getActiveBorrows());
    }
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowEntity>> getOverdueBorrows(){
        return ResponseEntity.ok(borrowService.getOverdueBorrows());
    }
    @GetMapping("/byClient")
    public ResponseEntity<List<BorrowEntity>> getBorrowsByClientId(@RequestParam Long clientId){
        return ResponseEntity.ok(borrowService.getBorrowsByClientId(clientId));
    }

}