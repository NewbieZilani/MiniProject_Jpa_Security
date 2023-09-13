package com.spring.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.print.Book;

@Data
@Entity
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // Many-to-One relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Many-to-One relationship with Book
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    private String reviewText;
    private double rating;

    // Getters and setters
}
