package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.entities.UserEntity;
import cl.usach.toolrent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ArrayList<UserEntity> getAllUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserEntity getUserByRut(String rut){
        return userRepository.findByRut(rut);
    }

}
