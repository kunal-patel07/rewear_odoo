package com.example.cafe_backedn.config;

import com.example.cafe_backedn.dto.UserPaginateDto;
import com.example.cafe_backedn.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserPaginateDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserPaginateDto dto = new UserPaginateDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setUsername(entity.getUsername());
        return dto;
    }

    public UserEntity toEntity(UserPaginateDto dto) {
        if (dto == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setUsername(dto.getUsername());
        return entity;
    }
}
