package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;

@Entity
@Table(name = "borrows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "tool_id", nullable = false)//No revise como afecta esto
    private ArrayList<ToolEntity> borrowedTools;

    private int cost;
    private Date borrowDate;
    private Date returnDate; //fecha pactada
    private Date clientReturnDate; //fecha real de entrega
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