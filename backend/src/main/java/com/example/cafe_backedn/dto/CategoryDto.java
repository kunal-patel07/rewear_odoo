package com.example.cafe_backedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotNull(message = "name can not be null")
    @NotBlank(message = "name can not be blank")
    private String name;

    @NotNull(message = "slug can not be null")
    @NotBlank(message = "slug can not be blank")
    private String slug;

    private String description;

    private String image;

    private Long parentId;

    private String parentName; // For display purposes

    private List<CategoryDto> children;

    private Integer sortOrder;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
