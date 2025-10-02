package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.*;
import cl.usach.toolrent.repositories.BorrowRepository;
import cl.usach.toolrent.repositories.MoveRepository;
import cl.usach.toolrent.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    private ToolService toolService;
    private UserService userService;
    private MoveService moveService;

    public void addTool(int quantity, ToolEntity tool, Long userId){
        for(int i = 0; i < quantity; i++){
            ToolEntity addedTool = new ToolEntity();
            addedTool.setName(tool.getName());
            addedTool.setType(tool.getType());
            addedTool.setCategory(tool.getCategory());
            addedTool.setReplacementValue(tool.getReplacementValue());
            addedTool.setState(ToolEntity.ToolState.Available);
            addedTool.setDailyTariff(tool.getDailyTariff());
            addedTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
            toolService.addTool(addedTool);
        }
        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Add);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(quantity);
    }

    public void removeTool(Long toolId, Long userId){
        toolService.deleteToolById(toolId, userService.getUserById(userId));
        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Remove);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(1);

    }

    public Map<ToolEntity, Integer> mostBorrowedTools(){
        Map<ToolEntity, Integer> counter = new HashMap<>();
        for(BorrowEntity borrow : borrowRepository.findAll()){
            for(ToolEntity tool : borrow.getBorrowedTools()){
                ToolCategory Category = tool.getCategory();
                counter.put(tool, counter.getOrDefault(tool, 0) + 1);
            }
        }

        return counter;
    }

    public List<Map.Entry<ToolEntity, Integer>> rankingTools(){
        Map<ToolEntity, Integer> counter = new HashMap<>();
        List<Map.Entry<ToolEntity, Integer>> ranking = new ArrayList<>(counter.entrySet());
        ranking.sort((a, b)-> b.getValue().compareTo(a.getValue()));
        return ranking;
    }
}
