package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "tools")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ToolType type;

    @ManyToOne
    @JoinColumn(name = "categoryId") // FK en la tabla tools
    private ToolCategory category;

    private Integer replacementValue;

    @Enumerated(EnumType.STRING)
    private ToolState state;

    private Integer dailyTariff;

    @Enumerated(EnumType.STRING)
    private DamageLevel damageLevel;



    public enum ToolState {
        Available,      // Disponible
        Borrowed,       // Prestada
        InRepair,       // En reparación
        OutOfService    // Dada de baja
    }

    public enum ToolType {
        Assembly,   // Destornillador y llaves
        Clamping,   // Alicates
        Percussion, // Martillos
        Cutting,    // Sierras
        Joining,    // Soldadores
        Measuring   // Cintas métricas
    }

    public enum DamageLevel {
        NoDamage,
        SmallDamage,
        MediumDamage,
        HighDamage,
        Unusable
    }
}