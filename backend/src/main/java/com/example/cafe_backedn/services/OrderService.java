package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.OrderMapper;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.OrderDto;
import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.OrderEntity;
import com.example.cafe_backedn.repo.OrderRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;

    public String generateBillId() {
        int randomNum = (int) (Math.random() * 9000000) + 1000000; // ensures 7-digit number
        return "BILL" + randomNum;
    }
    public OrderDto storeOrder(@Valid OrderDto body) {
        OrderEntity order = modelMapper.map(body, OrderEntity.class);
        String BillId = generateBillId();
        order.setBill_id(BillId);
        OrderEntity orderSave = orderRepo.save(order);
        return modelMapper.map(orderSave,OrderDto.class);
    }

    public List<OrderDto> allOrder() {
        List<OrderEntity> allOrder = orderRepo.findAllByOrderByIdDesc();
        return allOrder.stream().map(OrderEntity -> modelMapper.map(OrderEntity,OrderDto.class)).collect(Collectors.toList());
    }

    public OrderDto getOneOrder(Long id) {
        OrderEntity order = orderRepo.findById(id).get();
        return modelMapper.map(order,OrderDto.class);
    }

    public boolean updateOrder(Long id) {
        orderRepo.deleteById(id);
        return true;
    }


    public PaginationResponse<OrderDto> getPaginatedOrder(Pageable pageable, String keyword) { Page<OrderEntity> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = orderRepo.findAll(pageable);
        } else {
            page = orderRepo.findByCustomerNameContainingIgnoreCase(keyword, pageable);
        }

        Page<OrderDto> dtoPage = page.map(orderMapper::toDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public Object getCountCategory() {
        String count = String.valueOf(orderRepo.count());
        return count;
    }
}
