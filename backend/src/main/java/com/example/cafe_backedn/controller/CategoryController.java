package com.example.cafe_backedn.controller;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(allCategory == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCategory);
    }
    @GetMapping("/count")
    public Object getCountOfCategory(){
        Map<String, Object> response = new HashMap<>();
        response.put("count", categoryService.getCountCategory());
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> storeCategory(@Valid @RequestBody CategoryDto data){
        CategoryDto categoryDto = categoryService.createCategory(data);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOneCategory(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto data, @PathVariable Long id) {
        try {
            CategoryDto updated = categoryService.updateCategory(data, id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }



    @GetMapping("/categories")
    public ResponseEntity<PaginationResponse<CategoryDto>> getCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        PaginationResponse<CategoryDto> response = categoryService.getPaginatedCategory(pageable, keyword);
        return ResponseEntity.ok(response);
    }



}
