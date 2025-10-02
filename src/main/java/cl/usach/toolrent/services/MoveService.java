package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.MoveEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.repositories.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class MoveService {
    @Autowired
    private MoveRepository moveRepository;

    public MoveEntity createMove(){
        return new MoveEntity();
    }
    public MoveEntity saveMove(MoveEntity move){
        return moveRepository.save(move);
    }
}
