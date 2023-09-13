package com.spring.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String description;
    private Boolean availability=true;
    private double rating=0;


    @OneToMany(mappedBy = "book", orphanRemoval = false)
    @JsonIgnore
    private List<BorrowingEntity> borrowedBooks;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @JsonIgnore
    private List<ReviewEntity> reviews;


}
