package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long> {
    // Search methods
    Page<ProductEntity> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    List<ProductEntity> findAllByOrderByIdDesc(); // Order by 'id' descending
    
    // Category and seller filtering
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findBySellerId(Long sellerId);
    
    // Featured products
    List<ProductEntity> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(ProductEntity.Status status);
    
    // Status filtering
    List<ProductEntity> findByStatus(ProductEntity.Status status);
    
    // Condition type filtering
    List<ProductEntity> findByConditionType(ProductEntity.ConditionType conditionType);
    
    // Price range filtering
    List<ProductEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Complex search query
    @Query("SELECT p FROM ProductEntity p WHERE " +
           "(:query IS NULL OR LOWER(p.searchIndex) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:conditionType IS NULL OR p.conditionType = :conditionType) AND " +
           "p.status = 'active' " +
           "ORDER BY p.createdAt DESC")
    List<ProductEntity> searchProducts(@Param("query") String query,
                                     @Param("categoryId") Long categoryId,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("conditionType") String conditionType);
}
