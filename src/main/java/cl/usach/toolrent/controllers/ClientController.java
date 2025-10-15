package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin("*")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        List<ClientEntity> allClients = clientService.getAllClients();
        return ResponseEntity.ok(allClients);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id) {
        ClientEntity client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<ClientEntity> updateClientState(@PathVariable Long id){
        clientService.updateClientState(id);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

}