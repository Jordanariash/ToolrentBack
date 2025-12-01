package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.MoveEntity;
import cl.usach.toolrent.repositories.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class MoveService {
    @Autowired
    private MoveRepository moveRepository;

    public MoveEntity createMove(){
        return new MoveEntity();
    }

    public void saveMove(MoveEntity move){
        moveRepository.save(move);
    }

    public List<MoveEntity> getMovesByUserId(Long userId) {
        return moveRepository.findMovesByUserId(userId);
    }

    public List<MoveEntity> getMovesByToolId(Long toolId) {
        return moveRepository.findMovesByToolId(toolId);
    }

    public List<MoveEntity> findMovesBetweenDates(Date date1, Date date2) {
        return moveRepository.findMovesBetweenDates(date1, date2);
    }

    public List<MoveEntity> findMovesByToolIdAndDateRange(Long toolId, Date date1, Date date2){
        return moveRepository.findMovesByToolIdAndDateRange(toolId, date1, date2);
    }
}