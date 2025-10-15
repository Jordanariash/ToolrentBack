package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.ToolCategory;
import cl.usach.toolrent.repositories.ToolCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class ToolCategoyService {
    @Autowired
    private ToolCategoryRepository  toolCategoryRepository;

    public ToolCategory createToolCategory(ToolCategory toolCategory){
        return new ToolCategory();
    }

    public ToolCategory saveToolCategory(ToolCategory toolCategory){
        return toolCategoryRepository.save(toolCategory);
    }

    public void deleteToolCategory(ToolCategory toolCategory){
        toolCategoryRepository.delete(toolCategory);
    }
}
