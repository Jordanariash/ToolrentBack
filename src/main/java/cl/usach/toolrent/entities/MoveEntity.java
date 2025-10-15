
package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private MovementType type;
    private int quantityAffected;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity responsible;


    @ManyToMany
    @JoinTable(
            name = "move_tools",
            joinColumns = @JoinColumn(name = "move_id"),
            inverseJoinColumns = @JoinColumn(name = "tool_id")
    )
    private List<ToolEntity> tools = new ArrayList<>();


    public enum MovementType {
        Add,        // añadir
        Remove,     // quitar
        Borrow,     // prestar
        Return      // devolver

    }
}