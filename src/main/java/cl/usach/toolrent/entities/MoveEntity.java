package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "moves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private MovementType type;
    private int quantityAffected;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity responsible;

    public enum MovementType {
        Add,        // añadir
        Remove,     // quitar
        Borrow,     // prestar
        Return      // devolver

    }
}

/*
-responsible*/
