package com.example.cafe_backedn.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.CategoryMapper;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.dto.UpdateCategoryDto;
import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.repo.ProductRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    @Autowired
    private Cloudinary cloudinary;

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    // Helper method to manually map CategoryEntity to CategoryDto to avoid ModelMapper conflicts
    private CategoryDto mapEntityToDto(CategoryEntity entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        dto.setParentId(entity.getParentId());
        dto.setSortOrder(entity.getSortOrder());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        // Set parent name if parent exists
        if (entity.getParent() != null) {
            dto.setParentName(entity.getParent().getName());
        }
        
        return dto;
    }

    // Helper method to manually map CategoryEntity to UpdateCategoryDto
    private UpdateCategoryDto mapEntityToUpdateDto(CategoryEntity entity) {
        UpdateCategoryDto dto = new UpdateCategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        dto.setParentId(entity.getParentId());
        dto.setSortOrder(entity.getSortOrder());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        // Set parent name if parent exists
        if (entity.getParent() != null) {
            dto.setParentName(entity.getParent().getName());
        }
        
        return dto;
    }

    public List<CategoryDto> getAllCategory() {
        List<CategoryEntity> categories = categoryRepo.findAllByOrderByIdDesc();
        if(categories.isEmpty()) return null;
        return categories.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public CategoryDto createCategory(@Valid CategoryDto data) {
        // Manual mapping to avoid ModelMapper conflicts with parent field
        CategoryEntity category = new CategoryEntity();
        category.setName(data.getName());
        category.setSlug(data.getSlug());
        category.setDescription(data.getDescription());
        category.setImage(data.getImage());
        category.setParentId(data.getParentId());
        
        // Set default values for fields that might be null from DTO
        category.setSortOrder(data.getSortOrder() != null ? data.getSortOrder() : 0);
        category.setIsActive(data.getIsActive() != null ? data.getIsActive() : true);
        
        // Debug log
        System.out.println("Creating category with image: " + data.getImage());
        
        CategoryEntity create = categoryRepo.save(category);
        
        // Debug log
        System.out.println("Saved category with image: " + create.getImage());
        
        return mapEntityToDto(create);
    }

    @Transactional
    public UpdateCategoryDto updateCategory(@Valid UpdateCategoryDto data, Long id, MultipartFile imageFile) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        // Update fields
        category.setName(data.getName());
        category.setSlug(data.getSlug());
        category.setDescription(data.getDescription());
        category.setParentId(data.getParentId());
        if(data.getSortOrder() != null) {
            category.setSortOrder(data.getSortOrder());
        }
        if(data.getIsActive() != null) {
            category.setIsActive(data.getIsActive());
        }

        // If image is provided, update it
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Upload new image to Cloudinary
                File uploadedFile = File.createTempFile("temp", imageFile.getOriginalFilename());
                imageFile.transferTo(uploadedFile);

                Map<?, ?> result = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                String imageUrl = result.get("secure_url").toString();

                // Debug log
                System.out.println("Uploaded image URL: " + imageUrl);

                // Update the image URL in entity
                category.setImage(imageUrl);
                
                // Clean up temp file
                uploadedFile.delete();
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        // Save updated entity
        CategoryEntity updatedCategory = categoryRepo.save(category);
        
        // Debug log
        System.out.println("Updated category with image: " + updatedCategory.getImage());
        
        return mapEntityToUpdateDto(updatedCategory);
    }

    @Transactional
    public UpdateCategoryDto updateCategoryJson(@Valid UpdateCategoryDto updateCategoryDto) {
        Optional<CategoryEntity> existingCategoryOpt = categoryRepo.findById(updateCategoryDto.getId());
        if(existingCategoryOpt.isEmpty()){
            throw new RuntimeException("Category not found with id: " + updateCategoryDto.getId());
        }
        
        CategoryEntity existingCategory = existingCategoryOpt.get();
        
        // Update fields
        existingCategory.setName(updateCategoryDto.getName());
        existingCategory.setSlug(updateCategoryDto.getSlug());
        existingCategory.setDescription(updateCategoryDto.getDescription());
        existingCategory.setParentId(updateCategoryDto.getParentId());
        if(updateCategoryDto.getSortOrder() != null) {
            existingCategory.setSortOrder(updateCategoryDto.getSortOrder());
        }
        if(updateCategoryDto.getIsActive() != null) {
            existingCategory.setIsActive(updateCategoryDto.getIsActive());
        }
        // Note: image is not updated in JSON endpoint, use multipart endpoint for image updates
        
        CategoryEntity savedCategory = categoryRepo.save(existingCategory);
        return mapEntityToUpdateDto(savedCategory);
    }

    public CategoryDto getCategoryById(Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        return mapEntityToDto(category);
    }

    public boolean isCategoryExist(Long id) {
        return categoryRepo.existsById(id);
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        // First delete all products associated with this category
        List<ProductEntity> products = productRepo.findByCategoryId(id);
        productRepo.deleteAll(products);
        
        // Then delete the category
        categoryRepo.deleteById(id);
        return true;
    }

    public PaginationResponse<CategoryDto> getPaginatedCategory(Pageable pageable, String keyword) {
        Page<CategoryEntity> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = categoryRepo.findAll(pageable);
        } else {
            page = categoryRepo.findByNameContainingIgnoreCase(keyword, pageable);
        }

        Page<CategoryDto> dtoPage = page.map(this::mapEntityToDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public List<CategoryDto> getParentCategories() {
        List<CategoryEntity> parentCategories = categoryRepo.findByParentIdIsNull();
        return parentCategories.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getChildCategories(Long parentId) {
        List<CategoryEntity> childCategories = categoryRepo.findByParentId(parentId);
        return childCategories.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public String getCountCategory() {
        String count = String.valueOf(categoryRepo.count());
        return count;
    }
}
