package com.example.cafe_backedn.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.CloudinaryConfig;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.dto.UpdateCategoryDto;
import com.example.cafe_backedn.services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    private Cloudinary cloudinary;

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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto> storeCategory(
            @RequestParam("name") String name,
            @RequestParam("slug") String slug,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "parentId", required = false) String parentIdStr,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "0") Integer sortOrder,
            @RequestParam(value = "isActive", required = false, defaultValue = "true") Boolean isActive,
            HttpServletRequest request
    ) {
        try {
            // Debug all request parameters
            System.out.println("=== CREATE CATEGORY DEBUG ===");
            System.out.println("Content-Type: " + request.getContentType());
            System.out.println("Method: " + request.getMethod());
            System.out.println("All parameter names: " + java.util.Collections.list(request.getParameterNames()));
            
            // Debug each parameter
            System.out.println("name: " + name);
            System.out.println("slug: " + slug);
            System.out.println("description: " + description);
            System.out.println("parentIdStr: " + parentIdStr);
            System.out.println("sortOrder: " + sortOrder);
            System.out.println("isActive: " + isActive);
            
            // Convert parentId string to Long, handling "null" case
            Long parentId = null;
            if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("null")) {
                try {
                    parentId = Long.parseLong(parentIdStr);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(null);
                }
            }

            String imageUrl = null;
            
            // Debug log
            System.out.println("imageFile="+imageFile);
            System.out.println("Image file received: " + (imageFile != null ? imageFile.getOriginalFilename() : "NULL"));
            System.out.println("Image file size: " + (imageFile != null ? imageFile.getSize() : "NULL"));
            System.out.println("Image file isEmpty: " + (imageFile != null ? imageFile.isEmpty() : "NULL"));
            
            if (imageFile != null && !imageFile.isEmpty()) {
                // Upload image to Cloudinary
                File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                imageFile.transferTo(uploadedFile);

                Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                imageUrl = result.get("secure_url").toString();
                
                // Debug log
                System.out.println("Cloudinary upload result: " + imageUrl);
                
                // Clean up temp file
                uploadedFile.delete();
            } else {
                System.out.println("No image file received or file is empty");
            }

            // Create DTO manually
            CategoryDto data = new CategoryDto();
            data.setName(name);
            data.setSlug(slug);
            data.setDescription(description);
            data.setImage(imageUrl);
            data.setParentId(parentId);
            data.setSortOrder(sortOrder);
            data.setIsActive(isActive);

            // Debug log
            System.out.println("CategoryDto created with image: " + data.getImage());

            // Save category
            CategoryDto saved = categoryService.createCategory(data);
            
            // Debug log
            System.out.println("CategoryDto saved with image: " + saved.getImage());
            System.out.println("=== END CREATE CATEGORY DEBUG ===");
            
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create-json")
    public ResponseEntity<CategoryDto> storeCategoryJson(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto saved = categoryService.createCategory(categoryDto);
        if(saved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(saved);
    }

    @PostMapping(value = "/create-alt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto> storeCategoryAlternative(
            @RequestPart("name") String name,
            @RequestPart("slug") String slug,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "parentId", required = false) String parentIdStr,
            @RequestPart(value = "sortOrder", required = false) String sortOrderStr,
            @RequestPart(value = "isActive", required = false) String isActiveStr,
            HttpServletRequest request
    ) {
        try {
            Long parentId = null;
            if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("null")) {
                try {
                    parentId = Long.parseLong(parentIdStr);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(null);
                }
            }
            
            Integer sortOrder = 0;
            if (sortOrderStr != null && !sortOrderStr.trim().isEmpty()) {
                try {
                    sortOrder = Integer.parseInt(sortOrderStr);
                } catch (NumberFormatException e) {
                    sortOrder = 0;
                }
            }
            
            Boolean isActive = true;
            if (isActiveStr != null && !isActiveStr.trim().isEmpty()) {
                isActive = Boolean.parseBoolean(isActiveStr);
            }

            String imageUrl = null;
            
            if (imageFile != null && !imageFile.isEmpty()) {
                // Upload image to Cloudinary
                File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                imageFile.transferTo(uploadedFile);

                Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                imageUrl = result.get("secure_url").toString();
                
                // Debug log
                System.out.println("Cloudinary upload result: " + imageUrl);
                
                // Clean up temp file
                uploadedFile.delete();
            } else {
                System.out.println("No image file received or file is empty");
            }

            // Create DTO manually
            CategoryDto data = new CategoryDto();
            data.setName(name);
            data.setSlug(slug);
            data.setDescription(description);
            data.setImage(imageUrl);
            data.setParentId(parentId);
            data.setSortOrder(sortOrder);
            data.setIsActive(isActive);

            // Debug log
            System.out.println("CategoryDto created with image: " + data.getImage());

            // Save category
            CategoryDto saved = categoryService.createCategory(data);
            
            // Debug log
            System.out.println("CategoryDto saved with image: " + saved.getImage());
            System.out.println("=== END CREATE CATEGORY ALT DEBUG ===");
            
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Simple test endpoint to verify file upload is working
    @PostMapping(value = "/test-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> testUpload(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("received", imageFile != null);
        response.put("filename", imageFile != null ? imageFile.getOriginalFilename() : null);
        response.put("size", imageFile != null ? imageFile.getSize() : null);
        response.put("isEmpty", imageFile != null ? imageFile.isEmpty() : null);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOneCategory(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateCategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("slug") String slug,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "parentId", required = false) String parentIdStr,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            HttpServletRequest request
    ) {
        try {
            // Convert parentId string to Long, handling "null" case
            Long parentId = null;
            if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("null")) {
                try {
                    parentId = Long.parseLong(parentIdStr);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(null);
                }
            }

            UpdateCategoryDto data = new UpdateCategoryDto();
            data.setName(name);
            data.setSlug(slug);
            data.setDescription(description);
            data.setParentId(parentId);
            data.setSortOrder(sortOrder);
            data.setIsActive(isActive);

            UpdateCategoryDto updated = categoryService.updateCategory(data, id, imageFile);
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update-json/{id}")
    public ResponseEntity<UpdateCategoryDto> updateCategoryJson(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCategoryDto updateCategoryDto
    ) {
        updateCategoryDto.setId(id);
        UpdateCategoryDto updated = categoryService.updateCategoryJson(updateCategoryDto);
        if(updated == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(updated);
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

    @GetMapping("/parent-categories")
    public ResponseEntity<List<CategoryDto>> getParentCategories() {
        List<CategoryDto> parentCategories = categoryService.getParentCategories();
        return ResponseEntity.ok(parentCategories);
    }

    @GetMapping("/children/{parentId}")
    public ResponseEntity<List<CategoryDto>> getChildCategories(@PathVariable Long parentId) {
        List<CategoryDto> childCategories = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(childCategories);
    }
}
