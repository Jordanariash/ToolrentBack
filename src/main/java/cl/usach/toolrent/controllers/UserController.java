package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.UserEntity;
import cl.usach.toolrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    // Clase para el request de creación de usuario
    public static class CreateUserRequest {
        public String rut;
        public String name;
        public String email;
        public String password;
        public UserEntity.Role role;
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserRequest request) {
        UserEntity newUser = userService.createUser(
                request.rut,
                request.name,
                request.email,
                request.password,
                request.role
        );
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

