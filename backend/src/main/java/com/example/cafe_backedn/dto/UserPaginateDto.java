package com.example.cafe_backedn.dto;

import lombok.Data;

@Data
public class UserPaginateDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private String username;

}
