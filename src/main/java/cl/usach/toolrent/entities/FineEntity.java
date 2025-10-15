package cl.usach.toolrent.entities;

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
    private ClientEntity client;

    @OneToOne
    @JoinColumn(name = "borrow_id", nullable = false, unique = true)
    private BorrowEntity borrow;

    public enum FineType {
        Delay,            // Retraso
        Damage,           // Daño
        DelayAndDamage    // Retraso y daño
    }
}
