package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.BorrowEntity;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientEntity getClientById(Long id) {
        return clientRepository.findClientById(id);
    }



    public List<ClientEntity> getClientsWithOverdueBorrows(){
        return clientRepository.findClientsWithOverdueBorrows();
    }

    public List<ClientEntity> getClientsWithActiveBorrows(){
        return clientRepository.findClientsWithActiveBorrows();
    }


    public boolean hasExpiredBorrows(Long id){
        ClientEntity client = getClientById(id);
        if(client == null || client.getBorrows() == null || client.getBorrows().isEmpty()){
            return false;
        }
        return client.getBorrows().stream().anyMatch(borrow -> borrow.getBorrowState() == BorrowEntity.BorrowState.Overdue);
    }

    public boolean hasUnpaidFines(Long id){
        ClientEntity client = getClientById(id);
        return client != null && client.getUnpaidFines() != null && !client.getUnpaidFines().isEmpty();
    }

    public void restrictClient(Long id){
        ClientEntity client = clientRepository.findClientById(id);
        if(hasExpiredBorrows(id) || hasUnpaidFines(id)){
            clientRepository.findClientById(id).setState(ClientEntity.ClientState.Restricted);
        }
        clientRepository.save(client);
    }//Cambia a estado restringido siguiendo los 2 criterios de prestamos y multas

    public void acquitClient(Long id){
        ClientEntity client = clientRepository.findClientById(id);
        if(!hasExpiredBorrows(id) && !hasUnpaidFines(id)){
            clientRepository.findClientById(id).setState(ClientEntity.ClientState.Allowed);
        }
        clientRepository.save(client);
    }//Libera al cliente

    public void updateClientState(Long id){
        if(hasExpiredBorrows(id) || hasUnpaidFines(id)){
            restrictClient(id);
        }else{
            acquitClient(id);
        }
    }
}
