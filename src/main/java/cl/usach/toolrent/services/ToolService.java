package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.entities.UserEntity;
import cl.usach.toolrent.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;
    private UserService userService;


    //Consultas
    public List<ToolEntity> getAllTools(){
        return toolRepository.findAll();
    }

    public ToolEntity getToolById(Long id){
        return toolRepository.findById(id).orElseThrow(() -> new RuntimeException("Tool not found with ID: " + id));
    }

    public List<ToolEntity> getAvailableTools(){
        return toolRepository.findByState(ToolEntity.ToolState.Available);
    }

    public List<ToolEntity> getAvailableToolsByCategory(ToolCategory toolCategory){
        return  toolRepository.findByCategoryAndState(toolCategory, ToolEntity.ToolState.Available);
    }



    //Para cambiar el estado cuando se presta y devuelve
    public void changeState(Long toolId, ToolEntity.ToolState newState){
        ToolEntity tool = getToolById(toolId);
        tool.setState(newState);
        toolRepository.save(tool);
    }

    public void changeDamageLevel(Long toolId, ToolEntity.DamageLevel damageLevel){
        ToolEntity tool = getToolById(toolId);
        tool.setDamageLevel(damageLevel);
        toolRepository.save(tool);
    }




    //Metodo intermediario para cambiar
    public void markAsBorrowed(Long toolId) {
        changeState(toolId, ToolEntity.ToolState.Borrowed);
    }

    public void markAsAvailable(Long toolId) {
        changeState(toolId, ToolEntity.ToolState.Available);
    }

    public void markAsInRepair(Long toolId) {changeState(toolId, ToolEntity.ToolState.InRepair);}

    public void markAsOutOfService(Long toolId) {changeState(toolId, ToolEntity.ToolState.OutOfService);}







    public void changeReplacementValue(Long toolId, Integer replacementValue, Long userId){
        if (userService.verifyAdmin(userId)){
            ToolEntity selectedTool = getToolById(toolId);
            String toolName = selectedTool.getName();
            List<ToolEntity> tools = toolRepository.findByName(toolName);
            for(ToolEntity tool : tools){
                tool.setReplacementValue(replacementValue);
            }
        }else {
            throw new RuntimeException("User is not admin");
        }


    }

    public void changeDailyTariff(Long toolId, Integer dailyTariff, Long userId) {
        if (userService.verifyAdmin(userId)) {
            ToolEntity selectedTool = getToolById(toolId);
            String toolName = selectedTool.getName();
            List<ToolEntity> tools = toolRepository.findByName(toolName);
            for (ToolEntity tool : tools) {
                tool.setDailyTariff(dailyTariff);
            }
        } else {
            throw new RuntimeException("User is not admin");
        }
    }






}
