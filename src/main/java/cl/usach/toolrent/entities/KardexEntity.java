package cl.usach.toolrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "kardex")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ArrayList<MoveEntity> moves;
    private ArrayList<BorrowEntity> borrows;
    private ArrayList<ClientEntity> clients;
    private ArrayList<StockEntity>  stocks;
}
