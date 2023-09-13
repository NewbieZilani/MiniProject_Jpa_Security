package com.spring.main.controller;


import com.spring.main.constants.AppConstants;
import com.spring.main.dtoClasses.BorrowingHistoryDto;
import com.spring.main.dtoClasses.UserDto;
import com.spring.main.entity.BookEntity;
import com.spring.main.entity.UserEntity;
import com.spring.main.services.BorrowService;
import com.spring.main.services.UserService;
import com.spring.main.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final BorrowService borrowService;


    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            String accessToken = JWTUtils.generateToken(createdUser.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("user", createdUser);
            response.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long userId) {
        UserEntity user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{userId}/books")
    public ResponseEntity<List<BookEntity>> getAllBorrowedBooksByUserId(
            @PathVariable Long userId) {
        List<BookEntity> books = userService.getAllBorrowedBooksByUserId(userId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/{userId}/borrowed-books")
    public ResponseEntity<List<BookEntity>> getCurrentlyBorrowedBooksByUserId(
            @PathVariable Long userId) {
        List<BookEntity> books = userService.getCurrentlyBorrowedBooksByUserId(userId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("/{userId}/history")
    public ResponseEntity<List<BorrowingHistoryDto>> getUserBorrowingHistory(@PathVariable Long userId) {
        List<BorrowingHistoryDto> borrowingHistory = borrowService.getUserBorrowingHistory(userId);
        return new ResponseEntity<>(borrowingHistory, HttpStatus.OK);
    }

}
