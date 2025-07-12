package com.example.cafe_backedn.config;

import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.repo.CategoryRepo;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepo categoryRepo;
    
    public ProductDto toDto(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(String.valueOf(entity.getPrice()));
        dto.setCategory(String.valueOf(entity.getCategory()));
        
        // Fetch and set category name
        categoryRepo.findById(Long.valueOf(entity.getCategory()))
            .ifPresent(category -> dto.setCategoryName(category.getName()));
            
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public ProductEntity toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(Integer.parseInt(dto.getPrice()));
        entity.setCategory(Integer.parseInt(dto.getCategory()));
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
