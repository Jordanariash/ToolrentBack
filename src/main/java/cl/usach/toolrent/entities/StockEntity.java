package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private ToolCategoryEntity toolCategoryEntity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stockId") // crea la FK en la tabla tools
    private List<ToolEntity> toolList = new ArrayList<>();



    private String baseToolName;

    @Enumerated(EnumType.STRING)
    private ToolEntity.ToolType baseToolType;

    private Integer baseReplacementValue;
    private Integer baseDailyTariff;

}