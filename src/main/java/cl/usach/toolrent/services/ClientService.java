package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.repositories.ClientRepository;
import cl.usach.toolrent.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ArrayList<ClientEntity> getAllClients() {
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    public ClientEntity getClientById(Long id) {
        return clientRepository.findClientById(id);
    }

    public ArrayList<ClientEntity> getClientsByOverdue(){
        return (ArrayList<ClientEntity>) clientRepository.findClientsWithOverdueBorrows();
    }

    public boolean checkClientExpiredBorrows(Long id){
        return clientRepository.findClientById(id).getExpiredBorrows().isEmpty();
    }//si no tiene prestamos atrasados

    public boolean checkClientUnpaidFines(Long id){
        return clientRepository.findClientById(id).getUnpaidFines().isEmpty();
    } //una multa impaga, abarca a las de daño, Y TAMBIEN las de retraso

    public void restrictClient(Long id){
        ClientEntity clientEntity = clientRepository.findClientById(id);
        if(!checkClientExpiredBorrows(id) || !checkClientUnpaidFines(id)){
            clientRepository.findClientById(id).setState(ClientEntity.ClientState.Restricted);
        }
        clientRepository.save(clientEntity);
    }//Cambia a estado restringido siguiendo los 2 criterios de prestamos y multas

    public void acquitClient(Long id){
        ClientEntity clientEntity = clientRepository.findClientById(id);
        clientRepository.findClientById(id).setState(ClientEntity.ClientState.Allowed);
    }//Libera al cliente sin checks
}