
package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.repositories.ClientRepository;
import cl.usach.toolrent.repositories.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FineService {
    @Autowired
    private FineRepository fineRepository;
    @Autowired
    private ClientService clientService;

    public ArrayList<FineEntity> getAllFine(){
        return  (ArrayList<FineEntity>) fineRepository.findAll();
    }

    public FineEntity getFineById(Long id){
        return fineRepository.findById(id).orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + id));
    }

    public int calculateDays(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public FineEntity markAsPaid(Long fineId) {
        FineEntity fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con ID: " + fineId));

        fine.setStatus(FineEntity.FineStatus.Paid);
        clientService.updateClientState(clientService.getClientByFineId(fineId).getId());
        return fineRepository.save(fine);
    }

    public List<FineEntity> getUnpaidFines() {
        return fineRepository.findByStatus(FineEntity.FineStatus.Unpaid);
    }

    public List<FineEntity> getUnpaidFinesByClient(Long clientId) {
        return fineRepository.findByClientIdAndStatus(clientId, FineEntity.FineStatus.Unpaid);
    }
}