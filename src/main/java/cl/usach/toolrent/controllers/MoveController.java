package cl.usach.toolrent.controllers;
import cl.usach.toolrent.services.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/borrow")
@CrossOrigin("*")
public class MoveController {
    @Autowired
    private MoveService moveService; //aqui tampoco iria nada, porque el move se crea desde borrow
}
