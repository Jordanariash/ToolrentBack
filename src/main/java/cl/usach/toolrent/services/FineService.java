package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.FineEntity;
import cl.usach.toolrent.repositories.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class FineService {
    @Autowired
    private FineRepository fineRepository;

    public ArrayList<FineEntity> getAllFine(){
        return  (ArrayList<FineEntity>) fineRepository.findAll();
    }

    public FineEntity getFineById(Long id){
        return fineRepository.findFineById(id);
    }

    public int calculateDays(Date startDate, Date endDate) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void createFine(FineEntity fine){
        fineRepository.save(fine);
    }

}
