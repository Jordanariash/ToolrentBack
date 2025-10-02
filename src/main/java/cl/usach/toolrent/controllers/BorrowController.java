package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.BorrowEntity;
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
    BorrowService borrowService;

    /*
    * Faltan los @ de los parametros
    * @PathVariable ó
     *@BodyRequest
    * */

    @PostMapping("/")
    public ResponseEntity<BorrowEntity> createBorrow(@RequestParam Long clientId,@RequestParam ArrayList<Long> toolIds,@RequestParam Long userId,@RequestParam Date returnDate) {
        BorrowEntity newBorrow = borrowService.createBorrow(clientId, toolIds,userId, returnDate);
        return ResponseEntity.ok(newBorrow);
    }

    @PutMapping("/")
    public ResponseEntity<BorrowEntity> returnBorrow(@RequestParam Long borrowId,@RequestParam Date returnDate,@RequestParam  Long userId) {
        borrowService.returnBorrow(borrowId,returnDate,userId);
        return ResponseEntity.ok(borrowService.getBorrowById(borrowId));
    }

    @GetMapping("/")
    public ResponseEntity<List<BorrowEntity>> getActiveBorrows(){
        List<BorrowEntity> activeBorrows = borrowService.getActiveBorrows();
        return ResponseEntity.ok(activeBorrows);
    }
    @GetMapping("/")
    public ResponseEntity<List<BorrowEntity>> getOverdueBorrows(){
        List<BorrowEntity> overdueBorrows = borrowService.getOverdueBorrows();
        return ResponseEntity.ok(overdueBorrows);
    }
    @GetMapping("/")
    public ResponseEntity<List<BorrowEntity>> getBorrowsByClientId(@RequestParam Long clientId){
        List<BorrowEntity> borrows = borrowService.getBorrowsByClientId(clientId);
        return ResponseEntity.ok(borrows);
    }

}
