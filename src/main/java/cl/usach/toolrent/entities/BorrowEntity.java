package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "borrowed_tools",
            joinColumns = @JoinColumn(name = "borrow_id"),
            inverseJoinColumns = @JoinColumn(name = "tool_id")
    )
    private List<ToolEntity> borrowedTools = new ArrayList<>();
    //tabla intermedia para consultas de herramientas especificas mediante el prestamo

    private int cost;
    private Date borrowDate;
    private Date returnDate; //fecha pactada
    private Date clientReturnDate; //fecha real de entrega


    @Enumerated(EnumType.STRING)
    private BorrowState borrowState;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity responsible;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;


    public enum BorrowState {
        Active,
        Overdue,
        Returned
    }
}