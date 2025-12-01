package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.ToolCategoryEntity;
import cl.usach.toolrent.repositories.ToolCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolCategoryService {
    @Autowired
    private ToolCategoryRepository  toolCategoryRepository;

    public List<ToolCategoryEntity> getAllToolCategories() {
        return toolCategoryRepository.findAll();
    }

    public ToolCategoryEntity getToolCategoryById(Long id) {
        return toolCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    public ToolCategoryEntity createToolCategory(ToolCategoryEntity category) {
        ToolCategoryEntity saved = toolCategoryRepository.save(category);
        return saved;
    }

    public ToolCategoryEntity saveToolCategory(ToolCategoryEntity toolCategoryEntity){
        return toolCategoryRepository.save(toolCategoryEntity);
    }

    public void deleteToolCategory(ToolCategoryEntity toolCategoryEntity){
        toolCategoryRepository.delete(toolCategoryEntity);
    }
}
