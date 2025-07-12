package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImageEntity, Long> {
    List<ProductImageEntity> findByProductIdOrderBySortOrderAsc(Long productId);
    List<ProductImageEntity> findByProductId(Long productId);
    ProductImageEntity findByProductIdAndIsPrimaryTrue(Long productId);
    void deleteByProductId(Long productId);
} 