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
    private ToolType type;

    @ManyToOne
    @JoinColumn(name = "categoryId") // FK en la tabla tools
    private ToolCategory category;

    private Integer replacementValue;
    private ToolState state;



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
}



/*

* package edu.mtisw.payrollbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String rut;
    private String name;
    private int salary;
    private int children;
    private String category;
}

* */