package com.example.cafe_backedn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false, columnDefinition = "ENUM('new', 'like_new', 'excellent', 'good', 'fair')")
    private ConditionType conditionType;

    @Column(length = 100)
    private String brand;

    @Column(length = 20)
    private String size;

    @Column(length = 50)
    private String color;

    @Column(length = 100)
    private String material;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    private UserEntity seller;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('active', 'sold', 'inactive', 'under_review', 'rejected') DEFAULT 'active'")
    private Status status = Status.active;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer views = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer likes = 0;

    @Column(length = 100)
    private String location;

    @Column(name = "shipping_cost", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal shippingCost = BigDecimal.valueOf(0.00);

    @Column(name = "is_negotiable", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isNegotiable = true;

    @Column(name = "is_featured", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImageEntity> images;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Add the search index field
    @Column(name = "search_index")
    private String searchIndex;

    public enum ConditionType {
        new_condition("new"), like_new, excellent, good, fair;
        
        private String value;
        
        ConditionType() {
            this.value = name();
        }
        
        ConditionType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }

    public enum Status {
        active, sold, inactive, under_review, rejected
    }

    @PrePersist
    @PreUpdate
    public void updateSearchIndex() {
        this.searchIndex = String.join(" ",
                safe(title),
                safe(description),
                safe(brand),
                safe(color),
                safe(material),
                safe(String.valueOf(price)),
                safe(String.valueOf(categoryId))
        ).toLowerCase();
    }

    private String safe(String input) {
        return input != null ? input : "";
    }
}
