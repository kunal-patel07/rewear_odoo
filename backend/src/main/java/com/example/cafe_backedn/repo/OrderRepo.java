package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity,Long> {
    Page<OrderEntity> findByCustomerNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<OrderEntity> findAllByOrderByIdDesc(); // Order by 'id' descending

}
