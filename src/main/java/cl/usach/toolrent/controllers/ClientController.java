package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping("")
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> allClients = clientService.getAllClients();
        return ResponseEntity.ok(allClients);
    }

    @PostMapping("")
    public ResponseEntity<List<ClientEntity>> getClientById(@RequestParam Long id) {
        List<ClientEntity> clientsById = clientService.getAllClients();
        return ResponseEntity.ok(clientsById);
    }

    @PutMapping("")
    public ResponseEntity<ClientEntity> restrictClient(Long idClient){
        clientService.restrictClient(idClient);
        return ResponseEntity.ok(clientService.getClientById(idClient));
    }

    @PutMapping("")
    public ResponseEntity<ClientEntity> acquitClient(Long idClient){
        clientService.acquitClient(idClient);
        return ResponseEntity.ok(clientService.getClientById(idClient));
    }

}
