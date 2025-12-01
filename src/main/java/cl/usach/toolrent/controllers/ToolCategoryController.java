package cl.usach.toolrent.controllers;
import cl.usach.toolrent.entities.ToolCategoryEntity;
import cl.usach.toolrent.services.ToolCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/toolCategory")
@CrossOrigin("*")
public class ToolCategoryController {
    @Autowired
    private ToolCategoryService toolCategoryService;

    @GetMapping("")
    public ResponseEntity<List<ToolCategoryEntity>> getAllCategories() {
        List<ToolCategoryEntity> categories = toolCategoryService.getAllToolCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    public ResponseEntity<ToolCategoryEntity> createCategory(@RequestBody ToolCategoryEntity category) {
        ToolCategoryEntity created = toolCategoryService.createToolCategory(category);
        return ResponseEntity.ok(created);
    }


    @PutMapping("")
    public ResponseEntity<ToolCategoryEntity> updateCategory(@RequestBody ToolCategoryEntity updatedCategory) {
        ToolCategoryEntity category = toolCategoryService.saveToolCategory(updatedCategory);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteCategory(@RequestBody ToolCategoryEntity category) {
        toolCategoryService.deleteToolCategory(category);
        return ResponseEntity.noContent().build();
    }
}



