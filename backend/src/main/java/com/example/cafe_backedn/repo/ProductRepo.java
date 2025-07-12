package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.CategoryEntity;
import com.example.cafe_backedn.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long> {
    Page<ProductEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<ProductEntity> findByCategory(Long categoryId);
    List<ProductEntity> findAllByOrderByIdDesc(); // Order by 'id' descending

}
