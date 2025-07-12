package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    Page<CategoryEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<CategoryEntity> findAllByOrderByIdDesc(); // Order by 'id' descending
    
    // Methods for parent/child category relationships
    List<CategoryEntity> findByParentIdIsNull(); // Find parent categories (no parent)
    List<CategoryEntity> findByParentId(Long parentId); // Find child categories by parent ID
}
