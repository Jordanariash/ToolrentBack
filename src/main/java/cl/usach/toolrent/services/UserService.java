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
    private UserService userService;

    public UserEntity createUser(String rut, String name, String email, String password, UserEntity.Role role) {
        if (userRepository.findByRut(rut) != null) {
            throw new RuntimeException("El RUT ya está registrado: " + rut);
        }
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("El email ya está registrado: " + email);
        }

        UserEntity newUser = new UserEntity();
        newUser.setRut(rut);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);

        return userRepository.save(newUser);
    }

    public ArrayList<UserEntity> getAllUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserEntity getUserByRut(String rut){
        return userRepository.findByRut(rut);
    }

    public boolean verifyAdmin(Long userId) {
        UserEntity user = getUserById(userId);
        if (user.getRole() != UserEntity.Role.Admin) {
            return false;

        } else  {
            return true;
        }
    }
}
