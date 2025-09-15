package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ArrayList<ToolEntity> toolList;
}
