package cl.usach.toolrent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int delayDays;
    private float amount;

    @Enumerated(EnumType.STRING)
    private FineType type;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private ClientEntity client;

    @Enumerated(EnumType.STRING)
    private FineStatus status = FineStatus.Unpaid;

    @OneToOne
    @JoinColumn(name = "borrow_id", nullable = false, unique = true)
    private BorrowEntity borrow;

    public enum FineType {
        Delay,            // Retraso
        Damage,           // Daño
        DelayAndDamage    // Retraso y daño
    }

    public enum FineStatus {
        Unpaid,    // No pagada
        Paid       // Pagada
    }
}
