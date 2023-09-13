package com.spring.main.controller;

import com.spring.main.entity.BorrowingEntity;
import com.spring.main.services.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}/{userId}/borrow")
    public ResponseEntity<BorrowingEntity> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long userId) {
        BorrowingEntity borrowingEntity = borrowService.borrowBook(bookId, userId);
        return new ResponseEntity<>(borrowingEntity, HttpStatus.CREATED);
    }


    @PostMapping("/{bookId}/{userId}/return")
    public ResponseEntity<BorrowingEntity> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long userId) {
        BorrowingEntity borrowingEntity = borrowService.returnBook(bookId, userId);
        return new ResponseEntity<>(borrowingEntity, HttpStatus.OK);
    }

}
