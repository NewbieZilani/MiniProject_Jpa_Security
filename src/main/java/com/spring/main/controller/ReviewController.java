package com.spring.main.controller;

import com.spring.main.dtoClasses.ReviewDTO;
import com.spring.main.entity.ReviewEntity;
import com.spring.main.exception.CustomException;
import com.spring.main.response.CustomResponse;
import com.spring.main.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, ModelMapper modelMapper){
        this.reviewService=reviewService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/review/{bookId}/{userId}/create")
    public ResponseEntity<ReviewEntity> createReview(
            @PathVariable Long bookId,
            @PathVariable Long userId,
            @RequestBody ReviewEntity reviewEntity
    ) {
        ReviewEntity createdReview = reviewService.createReview(bookId, userId, reviewEntity);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }


    @GetMapping("/{bookId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getBookReviews(@PathVariable Long bookId) {
        List<ReviewDTO> reviews = reviewService.getBookReviews(bookId);

        if (!reviews.isEmpty()) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            throw new CustomException("This book got no reviews");
        }
    }


    @DeleteMapping("/{reviewId}/{userId}/delete")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        boolean deleted = reviewService.deleteUserReview(reviewId, userId);
        if (deleted) {
            return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
        } else {
            throw new CustomException("The review id not found");
        }
    }


    @PutMapping("/review/{reviewId}/{userId}/update")
    public ResponseEntity<ReviewEntity> updateReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId,
            @RequestBody ReviewDTO reviewDTO
    ) {
        ReviewEntity updatedReview = reviewService.updateReview(reviewId, userId, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }


}
