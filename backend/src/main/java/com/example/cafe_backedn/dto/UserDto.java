package com.example.cafe_backedn.dto;

import com.example.cafe_backedn.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(message = "name can not blank")
    @NotNull(message = "name can not null")
    private String name;

    @NotBlank(message = "username can not blank")
    @NotNull(message = "username can not null")
    private String username;

    @NotBlank(message = "email can not blank")
    @NotNull(message = "email can not null")
    private String email;

    @NotNull(message = "password can not null")
    @NotBlank(message = "password can not blank")
    private String password;

    private String avatar;
    private String bio;
    private String location;
    private String phone;
    private LocalDate dateOfBirth;
    private UserEntity.Gender gender;
    private Boolean verified;
    private UserEntity.Status status;
    private BigDecimal rating;
    private Integer totalSales;
    private Integer totalPurchases;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
