package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.CategoryMapper;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.repo.ProductRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategory() {
        List<CategoryEntity> categories = categoryRepo.findAllByOrderByIdDesc();
        if(categories.isEmpty()) return null;
        return categories.stream().map(categoryEntity -> modelMapper.map(categoryEntity,CategoryDto.class)).collect(Collectors.toList());
    }

    public CategoryDto createCategory(@Valid CategoryDto data) {
        CategoryEntity category = modelMapper.map(data,CategoryEntity.class);
        CategoryEntity create = categoryRepo.save(category);
        return modelMapper.map(create,CategoryDto.class);
    }

    @Transactional
    public CategoryDto updateCategory(@Valid CategoryDto data, Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        // Update category name
        category.setName(data.getName());
        CategoryEntity updatedCategory = categoryRepo.save(category);

        // Map to DTO
        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(updatedCategory.getId());
        updatedDto.setName(updatedCategory.getName());
        updatedDto.setCreatedAt(updatedCategory.getCreatedAt());
        updatedDto.setUpdatedAt(updatedCategory.getUpdatedAt());

        return updatedDto;
    }

    public CategoryDto getCategoryById(Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
        return modelMapper.map(category,CategoryDto.class);
    }

    public boolean isCategoryExist(Long id) {
        return categoryRepo.existsById(id);
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        // First delete all products associated with this category
        List<ProductEntity> products = productRepo.findByCategory(id);
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

        Page<CategoryDto> dtoPage = page.map(categoryMapper::toDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public String getCountCategory() {
        String count = String.valueOf(categoryRepo.count());
        return count;
    }
}
