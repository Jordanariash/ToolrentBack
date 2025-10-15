
package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.*;
import cl.usach.toolrent.repositories.BorrowRepository;
import cl.usach.toolrent.repositories.MoveRepository;
import cl.usach.toolrent.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ToolService toolService;

    @Autowired
    private UserService userService;

    @Autowired
    private MoveService moveService;
    @Autowired
    private MoveRepository moveRepository;


    public StockEntity getStockByToolId(Long id){
        ToolEntity tool = toolService.getToolById(id);
        if (tool == null) {
            throw new RuntimeException("Tool not found with id: " + id);
        }

        // Busca el stock al que pertenece la categoría de la herramienta
        StockEntity stock = stockRepository.findStockByToolCategory(tool.getCategory());
        if (stock == null) {
            throw new RuntimeException("Stock not found for tool category: " + tool.getCategory());
        }

        return stock;
    }

    public void addNewTool(int quantity, String name, ToolEntity.ToolType tooltype, ToolCategory toolCategory, Integer replacementValue, Integer dailyTariff, Long userId){
        ToolEntity newTool = new ToolEntity();
        newTool.setName(name);
        newTool.setType(tooltype);
        newTool.setCategory(toolCategory);
        newTool.setReplacementValue(replacementValue);
        newTool.setDailyTariff(dailyTariff);
        newTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
        newTool.setState(ToolEntity.ToolState.Available);
        StockEntity selectedStock = stockRepository.findStockByToolCategory(toolCategory);
        for(int i = 0; i < quantity; i++){
            ToolEntity addedTool = new ToolEntity();
            addedTool.setName(newTool.getName());
            addedTool.setType(newTool.getType());
            addedTool.setCategory(newTool.getCategory());
            addedTool.setReplacementValue(newTool.getReplacementValue());
            addedTool.setState(ToolEntity.ToolState.Available);
            addedTool.setDailyTariff(newTool.getDailyTariff());
            addedTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
            selectedStock.getToolList().add(addedTool);
        }

        stockRepository.save(selectedStock);

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Add);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(quantity);
        moveService.saveMove(move);
    }

    public void addExistingTool(int quantity, Long toolId, Long userId){
        ToolEntity tool = toolService.getToolById(toolId);
        StockEntity selectedStock = stockRepository.findStockByToolCategory(tool.getCategory());
        for(int i = 0; i < quantity; i++){
            ToolEntity addedTool = new ToolEntity();
            addedTool.setName(tool.getName());
            addedTool.setType(tool.getType());
            addedTool.setCategory(tool.getCategory());
            addedTool.setReplacementValue(tool.getReplacementValue());
            addedTool.setState(ToolEntity.ToolState.Available);
            addedTool.setDailyTariff(tool.getDailyTariff());
            addedTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);

            selectedStock.getToolList().add(addedTool);
        }
        stockRepository.save(selectedStock);

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Add);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(quantity);
        moveService.saveMove(move);
    }

    public void removeTool(Long toolId, Long userId){
        if (!userService.verifyAdmin(userId)) {
            throw new RuntimeException("User is not admin");
        }

        ToolEntity tool = toolService.getToolById(toolId);
        if (tool == null) {
            throw new RuntimeException("Tool not found");
        }

        StockEntity stock = stockRepository.findStockByToolCategory(tool.getCategory());
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + tool.getCategory());
        }

        boolean removed = stock.getToolList().removeIf(t -> Objects.equals(t.getId(), toolId));

        if (!removed) {
            throw new RuntimeException("Tool not found in stock");
        }

        stockRepository.save(stock);

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Remove);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(1);
        moveService.saveMove(move);
    }

    public void removeOutOfServiceTools(Long userId, ToolCategory toolCategory){
        if (!userService.verifyAdmin(userId)) {
            throw new RuntimeException("User is not admin");
        }

        StockEntity stock = stockRepository.findStockByToolCategory(toolCategory);
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + toolCategory);
        }

        List<ToolEntity> tools = stock.getToolList();
        int initialSize = tools.size();

        tools.removeIf(t -> t.getState() == ToolEntity.ToolState.OutOfService);
        int removedCount = initialSize - tools.size();

        if (removedCount > 0) {
            stockRepository.save(stock);
        }

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Remove);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(removedCount);
        moveService.saveMove(move);
    }

    public void removeAvailableTools(Long userId, ToolCategory toolCategory, Integer quantity){
        if (!userService.verifyAdmin(userId)) {
            throw new RuntimeException("User is not admin");
        }

        StockEntity stock = stockRepository.findStockByToolCategory(toolCategory);
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + toolCategory);
        }

        List<ToolEntity> availableTools = stock.getToolList().stream()
                .filter(t -> t.getState() == ToolEntity.ToolState.Available)
                .toList();

        if (availableTools.size() < quantity) {
            throw new RuntimeException("Not enough available tools in stock");
        }

        // Crear lista mutable para eliminar
        List<ToolEntity> tools = stock.getToolList();
        int removedCount = 0;

        Iterator<ToolEntity> iterator = tools.iterator();
        while (iterator.hasNext() && removedCount < quantity) {
            ToolEntity t = iterator.next();
            if (t.getState() == ToolEntity.ToolState.Available) {
                iterator.remove();
                removedCount++;
            }
        }

        stockRepository.save(stock);

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Remove);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(removedCount);
        moveService.saveMove(move);
    }
}
