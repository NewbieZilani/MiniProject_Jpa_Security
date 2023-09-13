package com.spring.main.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String role;

    // One-to-Many relationship with Borrowed_Books
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<BorrowingEntity> borrowedBooks;

    // One-to-Many relationship with Reserved_Books (Optional)
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ReservationEntity> reservedBooks;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private UserHistoryEntity userHistory;
}
