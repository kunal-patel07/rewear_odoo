package com.example.cafe_backedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private long id;

    @NotBlank(message = "name can not blank")
    @NotNull(message = "name can not null")
    private String name;

    @NotBlank(message = "email can not blank")
    @NotNull(message = "email can not null")
    private String email;

    @NotNull(message = "password can not null")
    @NotBlank(message = "password can not blank")
    private String password;

    @NotNull(message = "username can not null")
    @NotBlank(message = "username can not blank")
    private String username;
}
