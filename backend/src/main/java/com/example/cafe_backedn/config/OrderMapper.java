package com.example.cafe_backedn.config;

import com.example.cafe_backedn.dto.OrderDto;
import com.example.cafe_backedn.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(entity.getId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setEmail(entity.getEmail());
        dto.setNumber(entity.getNumber());
        dto.setPayment_option(entity.getPayment_option());
        dto.setProduct_id(entity.getProduct_id());
        dto.setQuantity(entity.getQuantity());
        dto.setTotal(entity.getTotal());
        return dto;
    }

    public OrderEntity toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setEmail(dto.getEmail());
        entity.setNumber(dto.getNumber());
        entity.setPayment_option(dto.getPayment_option());
        entity.setProduct_id(dto.getProduct_id());
        entity.setQuantity(dto.getQuantity());
        entity.setTotal(dto.getTotal());
        return entity;
    }
}
