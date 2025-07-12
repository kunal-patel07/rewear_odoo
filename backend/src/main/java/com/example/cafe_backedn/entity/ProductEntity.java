package com.example.cafe_backedn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    private Integer price;

    private Integer category;

    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    // Add the search index field
    @Column(name = "search_index")
    private String searchIndex;

    @PrePersist
    @PreUpdate
    public void updateSearchIndex() {
        this.searchIndex = String.join(" ",
                safe(name),
                safe(description),
                safe(String.valueOf(price)),
                safe(String.valueOf(category))
                // Add other fields you want to search
        ).toLowerCase();
    }

    private String safe(String input) {
        return input != null ? input : "";
    }
}
