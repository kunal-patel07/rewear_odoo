package com.example.cafe_backedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long Id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String price;

    @NotNull
    @NotBlank
    private String category;

    private String categoryName;

    @NotNull
    @NotBlank
    private String description;

    private Date createdAt;

    private Date updatedAt;
}
