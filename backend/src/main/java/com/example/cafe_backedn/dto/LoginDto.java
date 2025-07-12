package com.example.cafe_backedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @NotNull(message = "email is not null")
    @NotBlank(message = "email is not blank")
    public String email;
    @NotNull(message = "password is not null")
    @NotBlank(message = "password is not blank")
    public String password;

    public String token;
}
