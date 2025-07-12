package com.example.cafe_backedn.config;

import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public CategoryEntity toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
