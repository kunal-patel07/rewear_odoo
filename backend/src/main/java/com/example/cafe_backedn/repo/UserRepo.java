package com.example.cafe_backedn.repo;

import com.example.cafe_backedn.entity.OrderEntity;
import com.example.cafe_backedn.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAllByOrderByIdDesc(); // Order by 'id' descending


    Page<UserEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
