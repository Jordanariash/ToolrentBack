package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contact;
    private ClientState state;
    private String telephoneNumber;
    private String email;
    private Boolean canBorrow;
    private ArrayList<ToolEntity> borrowedTools;
    private ArrayList<BorrowEntity> expiredBorrows;
    private ArrayList<FineEntity> unpaidFines;

    public enum ClientState {
        Allowed,      // Disponible
        Restricted,       // Restringido
    }
}
