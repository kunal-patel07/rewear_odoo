package com.example.cafe_backedn.config;

import com.example.cafe_backedn.dto.ProductDto;
import com.example.cafe_backedn.entity.ProductEntity;
import com.example.cafe_backedn.repo.CategoryRepo;
import com.example.cafe_backedn.repo.UserRepo;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    
    public ProductDto toDto(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setOriginalPrice(entity.getOriginalPrice());
        dto.setConditionType(entity.getConditionType());
        dto.setBrand(entity.getBrand());
        dto.setSize(entity.getSize());
        dto.setColor(entity.getColor());
        dto.setMaterial(entity.getMaterial());
        dto.setCategoryId(entity.getCategoryId());
        dto.setSellerId(entity.getSellerId());
        dto.setStatus(entity.getStatus());
        dto.setViews(entity.getViews());
        dto.setLikes(entity.getLikes());
        dto.setLocation(entity.getLocation());
        dto.setShippingCost(entity.getShippingCost());
        dto.setIsNegotiable(entity.getIsNegotiable());
        dto.setIsFeatured(entity.getIsFeatured());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        // Fetch and set category name
        if (entity.getCategoryId() != null) {
            categoryRepo.findById(entity.getCategoryId())
                .ifPresent(category -> dto.setCategoryName(category.getName()));
        }
        
        // Fetch and set seller name
        if (entity.getSellerId() != null) {
            userRepo.findById(entity.getSellerId())
                .ifPresent(user -> dto.setSellerName(user.getName()));
        }
        
        return dto;
    }

    public ProductEntity toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setOriginalPrice(dto.getOriginalPrice());
        entity.setConditionType(dto.getConditionType());
        entity.setBrand(dto.getBrand());
        entity.setSize(dto.getSize());
        entity.setColor(dto.getColor());
        entity.setMaterial(dto.getMaterial());
        entity.setCategoryId(dto.getCategoryId());
        entity.setSellerId(dto.getSellerId());
        entity.setStatus(dto.getStatus());
        entity.setViews(dto.getViews());
        entity.setLikes(dto.getLikes());
        entity.setLocation(dto.getLocation());
        entity.setShippingCost(dto.getShippingCost());
        entity.setIsNegotiable(dto.getIsNegotiable());
        entity.setIsFeatured(dto.getIsFeatured());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        
        return entity;
    }
}
