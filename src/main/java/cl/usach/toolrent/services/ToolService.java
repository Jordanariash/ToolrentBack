package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.ToolEntity;
import cl.usach.toolrent.entities.UserEntity;
import cl.usach.toolrent.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;

    public ArrayList<ToolEntity>  getAllTools(){
        return (ArrayList<ToolEntity>) toolRepository.findAll();
    }

    public ToolEntity getToolById(Long id){
        return toolRepository.findById(id).orElse(null);
    }

    public ToolEntity addTool(ToolEntity tool){
        return toolRepository.save(tool);
    }

    public ToolEntity updateTool(ToolEntity tool){
        return toolRepository.save(tool);
    }

    public void deleteToolById(Long id, UserEntity user){
        if (user.getRole() != UserEntity.Role.Admin) {
            throw new RuntimeException("Only admins can delete tools");
        }

        ToolEntity existing = getToolById(id);
        existing.setState(ToolEntity.ToolState.OutOfService);
        toolRepository.save(existing);
    }

    public ArrayList<ToolEntity> getAvailableTools(){
        return (ArrayList<ToolEntity>) toolRepository.findByState(ToolEntity.ToolState.Available);
    }

    public void changeState(ToolEntity tool, ToolEntity.ToolState state){
        ToolEntity selectedTool = getToolById(tool.getId());
        Objects.requireNonNull(toolRepository.findById(selectedTool.getId()).orElse(null)).setState(state);
    }

    public void changeReplacementValue(ToolEntity tool, Integer replacementValue){
        ToolEntity selectedTool = getToolById(tool.getId());
        Objects.requireNonNull(toolRepository.findById(selectedTool.getId()).orElse(null)).setReplacementValue(replacementValue);
    }

    public void changeDailyTariff(ToolEntity tool, Integer dailyTariff, UserEntity user){
        ToolEntity selectedTool = getToolById(tool.getId());
        if (user.getRole() != UserEntity.Role.Admin) {
            throw new RuntimeException("Only admins can change daily tariff");
        }
        Objects.requireNonNull(toolRepository.findById(selectedTool.getId()).orElse(null)).setDailyTariff(dailyTariff);
    }

    public void changeDamageLevel(ToolEntity tool, ToolEntity.DamageLevel damageLevel){
        ToolEntity selectedTool = getToolById(tool.getId());
        Objects.requireNonNull(toolRepository.findById(selectedTool.getId()).orElse(null)).setDamageLevel(damageLevel);
    }
}
