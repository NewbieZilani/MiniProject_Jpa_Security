package com.spring.main.repository;

import com.spring.main.entity.BookEntity;
import com.spring.main.entity.ReviewEntity;
import com.spring.main.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByBookId(Long bookId);

}
