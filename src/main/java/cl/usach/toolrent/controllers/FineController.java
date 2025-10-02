package cl.usach.toolrent.controllers;
import cl.usach.toolrent.services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
@CrossOrigin("*")
public class FineController {
    @Autowired
    private FineService fineService;

    //Por como lo estructure, creeeeo que no iria nada aqui, porque se crea desde borrow
}
