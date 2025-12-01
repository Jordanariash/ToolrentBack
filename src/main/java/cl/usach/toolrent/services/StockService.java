
package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.*;
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
    private ToolCategoryService toolCategoryService;

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
        StockEntity stock = stockRepository.findStockByToolCategoryEntity(tool.getCategory());
        if (stock == null) {
            throw new RuntimeException("Stock not found for tool category: " + tool.getCategory());
        }

        return stock;
    }

    public List<StockEntity> getAllStocks(){
        return stockRepository.findAll();
    }

    public StockEntity getStockByCategory(Long categoryId){
        return stockRepository.findStockByToolCategoryId(categoryId);
    }

    public void addNewTool(int quantity, String name, ToolEntity.ToolType tooltype, ToolCategoryEntity toolCategoryEntity, Integer replacementValue, Integer dailyTariff, Long userId){
        ToolEntity newTool = new ToolEntity();
        newTool.setName(name);
        newTool.setType(tooltype);
        newTool.setCategory(toolCategoryEntity);
        newTool.setReplacementValue(replacementValue);
        newTool.setDailyTariff(dailyTariff);
        newTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
        newTool.setState(ToolEntity.ToolState.Available);

        StockEntity selectedStock = stockRepository.findStockByToolCategoryEntity(toolCategoryEntity);

        if (selectedStock == null) {
            // Crear nuevo stock para esta categoría
            selectedStock = new StockEntity();
            selectedStock.setToolCategoryEntity(toolCategoryEntity);
            selectedStock.setToolList(new ArrayList<>());

            //guardar plantilla
            selectedStock.setBaseToolName(name);
            selectedStock.setBaseToolType(tooltype);
            selectedStock.setBaseReplacementValue(replacementValue);
            selectedStock.setBaseDailyTariff(dailyTariff);

            // GUARDAR Y VERIFICAR
            StockEntity savedStock = stockRepository.save(selectedStock);
            if (savedStock == null) {
                throw new RuntimeException("No se pudo crear el stock para la categoría: " + toolCategoryEntity.getName());
            }
            selectedStock = savedStock;
        }else{
            selectedStock.setBaseToolName(name);
            selectedStock.setBaseToolType(tooltype);
            selectedStock.setBaseReplacementValue(replacementValue);
            selectedStock.setBaseDailyTariff(dailyTariff);
        }

        if (selectedStock.getToolList() == null) {
            selectedStock.setToolList(new ArrayList<>());
        }


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

        StockEntity finalStock = stockRepository.save(selectedStock);
        if (finalStock == null) {
            throw new RuntimeException("❌ ERROR: No se pudo guardar el stock con las nuevas herramientas");
        }

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Add);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(quantity);
        moveService.saveMove(move);
    }

    public void addToolsByCategory(int quantity, Long categoryId, Long userId) {
        ToolCategoryEntity category = toolCategoryService.getToolCategoryById(categoryId);
        StockEntity stock = stockRepository.findStockByToolCategoryEntity(category);

        if (stock == null) {
            throw new RuntimeException("No existe stock para esta categoría");
        }

        // ⬇️ USAR LA PLANTILLA DEL STOCK
        if (stock.getBaseToolName() == null) {
            throw new RuntimeException("Esta categoría no tiene una plantilla definida.");
        }

        for (int i = 0; i < quantity; i++) {
            ToolEntity newTool = new ToolEntity();
            newTool.setName(stock.getBaseToolName());
            newTool.setType(stock.getBaseToolType());
            newTool.setCategory(category);
            newTool.setReplacementValue(stock.getBaseReplacementValue());
            newTool.setDailyTariff(stock.getBaseDailyTariff());
            newTool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
            newTool.setState(ToolEntity.ToolState.Available);

            stock.getToolList().add(newTool);
        }

        stockRepository.save(stock);

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

        StockEntity stock = stockRepository.findStockByToolCategoryEntity(tool.getCategory());
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + tool.getCategory());
        }

        tool.setState(ToolEntity.ToolState.Deleted);

        stockRepository.save(stock);

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Remove);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(1);
        moveService.saveMove(move);
    }

    public void removeOutOfServiceTools(Long userId, ToolCategoryEntity toolCategoryEntity){

        if (!userService.verifyAdmin(userId)) {
            throw new RuntimeException("User is not admin");
        }

        StockEntity stock = stockRepository.findStockByToolCategoryEntity(toolCategoryEntity);
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + toolCategoryEntity);
        }

        List<ToolEntity> tools = stock.getToolList();

        int removedCount = 0;
        for (ToolEntity tool: tools) {
            if(tool.getState() == ToolEntity.ToolState.OutOfService){
                tool.setState(ToolEntity.ToolState.Deleted);
                removedCount++;
            }
        }



        if (removedCount > 0) {
            stockRepository.save(stock);
            MoveEntity move = moveService.createMove();
            move.setDate(Date.valueOf(LocalDate.now()));
            move.setType(MoveEntity.MovementType.Remove);
            move.setResponsible(userService.getUserById(userId));
            move.setQuantityAffected(removedCount);
            moveService.saveMove(move);
        }

    }

    public void removeAvailableTools(Long userId, ToolCategoryEntity toolCategoryEntity, Integer quantity){
        if (!userService.verifyAdmin(userId)) {
            throw new RuntimeException("User is not admin");
        }

        StockEntity stock = stockRepository.findStockByToolCategoryEntity(toolCategoryEntity);
        if (stock == null) {
            throw new RuntimeException("No stock found for category: " + toolCategoryEntity);
        }

        List<ToolEntity> availableTools = stock.getToolList().stream()
                .filter(t -> t.getState() == ToolEntity.ToolState.Available)
                .toList();

        if (availableTools.size() < quantity) {
            throw new RuntimeException("Not enough available tools in stock");
        }

        int removedCount = 0;


        for (int i = 0; i < quantity; i++) {
            availableTools.get(i).setState(ToolEntity.ToolState.Deleted);
            removedCount++;
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
