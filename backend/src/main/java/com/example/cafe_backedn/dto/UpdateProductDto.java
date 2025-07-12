package com.example.cafe_backedn.dto;

import com.example.cafe_backedn.entity.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {
    private Long id;

    @NotNull(message = "title can not be null")
    @NotBlank(message = "title can not be blank")
    private String title;

    private String description;

    @NotNull(message = "price can not be null")
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotNull(message = "condition type can not be null")
    private ProductEntity.ConditionType conditionType;

    private String brand;
    private String size;
    private String color;
    private String material;

    @NotNull(message = "category id can not be null")
    private Long categoryId;

    private String categoryName; // For display purposes

    private ProductEntity.Status status;
    private Integer views;
    private Integer likes;
    private String location;
    private BigDecimal shippingCost;
    private Boolean isNegotiable;
    private Boolean isFeatured;

    private List<ProductImageDto> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 