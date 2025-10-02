package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.BorrowEntity;
import cl.usach.toolrent.entities.KardexEntity;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class KardexService {
    @Autowired
    private KardexRepository kardexRepository;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private StockService stockService;

    public class Report{
        private ArrayList<BorrowEntity> activeBorrows;
        private ArrayList<BorrowEntity> overdueBorrows;
        private Map<ToolEntity, Integer> rankedTools;

        public Report(ArrayList<BorrowEntity> activeBorrows, ArrayList<BorrowEntity> overdueBorrows, Map<ToolEntity, Integer> toolEntityIntegerMap) {
        }
    }//Clase envoltorio, necesaria?
    public Report generateReport(){
        return new Report(borrowService.getActiveBorrows(), borrowService.getOverdueBorrows(), stockService.mostBorrowedTools());
    }

}