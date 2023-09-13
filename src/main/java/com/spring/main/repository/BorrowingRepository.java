package com.spring.main.repository;

import com.spring.main.entity.BorrowingEntity;
import com.spring.main.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRepository extends JpaRepository<BorrowingEntity, Long> {
    Optional<BorrowingEntity> findByBookId(Long bookId);
    public List<BorrowingEntity> findByUser(UserEntity user);

    List<BorrowingEntity> findByUserUserId(Long userId);
}
