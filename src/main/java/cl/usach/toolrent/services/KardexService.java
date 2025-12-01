package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.BorrowEntity;
import cl.usach.toolrent.entities.ClientEntity;
import cl.usach.toolrent.entities.MoveEntity;
import cl.usach.toolrent.entities.ToolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KardexService {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private MoveService moveService;

    @Autowired
    private ClientService clientService;

    public class Report{
        private List<BorrowEntity> activeBorrows;
        private List<BorrowEntity> overdueBorrows;
        private List<Map.Entry<ToolEntity, Integer>> rankedTools;

        // Constructor corregido
        public Report(List<BorrowEntity> activeBorrows, List<BorrowEntity> overdueBorrows, List<Map.Entry<ToolEntity, Integer>> rankedTools) {
            this.activeBorrows = activeBorrows;
            this.overdueBorrows = overdueBorrows;
            this.rankedTools = rankedTools;
        }

        // Getters necesarios
        public List<BorrowEntity> getActiveBorrows() { return activeBorrows; }
        public List<BorrowEntity> getOverdueBorrows() { return overdueBorrows; }
        public List<Map.Entry<ToolEntity, Integer>> getRankedTools() { return rankedTools; }
    }

    public Report generateReport(){
        List<Map.Entry<ToolEntity, Integer>> ranking = rankingTools();
        return new Report(borrowService.getActiveBorrows(), borrowService.getOverdueBorrows(), ranking);
    }

    public List<Map.Entry<ToolEntity, Integer>> rankingTools() {
        Map<ToolEntity, Integer> counter = borrowService.mostBorrowedTools();
        List<Map.Entry<ToolEntity, Integer>> ranking = new ArrayList<>(counter.entrySet());
        ranking.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return ranking;
    }


    //Es redundante, pero a nivel de logica moveS->kardexS>kardexC
    public List<MoveEntity> findMovesByToolIdAndDateRange(Long toolId, Date date1, Date date2){
        return moveService.findMovesByToolIdAndDateRange(toolId, date1, date2);
    }
}
