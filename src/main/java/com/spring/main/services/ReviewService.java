package com.spring.main.services;

import com.spring.main.dtoClasses.ReviewDTO;
import com.spring.main.entity.BookEntity;
import com.spring.main.entity.ReviewEntity;
import com.spring.main.exception.CustomException;
import org.modelmapper.ModelMapper;
import com.spring.main.entity.UserEntity;
import com.spring.main.repository.BookRepository;
import com.spring.main.repository.ReviewRepository;
import com.spring.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.modelMapper=modelMapper;
    }

    public ReviewEntity createReview(Long bookId, Long userId, ReviewEntity reviewEntity) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        Optional<BookEntity> bookOptional = bookRepository.findById(bookId);

        if (userOptional.isPresent() && bookOptional.isPresent()) {
            UserEntity user = userOptional.get();
            BookEntity book = bookOptional.get();

            reviewEntity.setUser(user);
            reviewEntity.setBook(book);

            reviewEntity = reviewRepository.save(reviewEntity);

            double updatedRating = calculateUpdatedRating(book);
            book.setRating(updatedRating);
            bookRepository.save(book);

            return reviewEntity;
        } else {
            throw new CustomException("Book is not found with user");
        }
    }

    private double calculateUpdatedRating(BookEntity book) {
        List<ReviewEntity> bookReviews = book.getReviews();

        if (bookReviews.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0;

        for (ReviewEntity review : bookReviews) {
            totalRating += review.getRating();
        }

        return totalRating / bookReviews.size();
    }

    public List<ReviewDTO> getBookReviews( Long bookId) {
         List<ReviewEntity> reviews= reviewRepository.findByBookId(bookId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (ReviewEntity review : reviews) {
            ReviewDTO reviewDTO=modelMapper.map(review,ReviewDTO.class);
            reviewDTOS.add(reviewDTO);
        }
        //reviews.stream().map(reviewEntity -> modelMapper.map(reviewEntity,ReviewDTO.class)).toList();
        return reviewDTOS;
    }



    public boolean deleteUserReview(Long reviewId, Long userId) {
        Optional<ReviewEntity> reviewOptional = reviewRepository.findById(reviewId);

        if (reviewOptional.isPresent()) {
            ReviewEntity review = reviewOptional.get();

            if (review.getUser().getUserId() == userId) {
                reviewRepository.delete(review);
                return true;
            } else {
                throw new CustomException("User id not found");
            }
        } else {
            throw new CustomException("Review id not found");
        }
    }


    public ReviewEntity updateReview(Long reviewId, Long userId, ReviewDTO reviewDTO) {
        Optional<ReviewEntity> reviewOptional = reviewRepository.findById(reviewId);

        if (reviewOptional.isPresent()) {
            ReviewEntity review = reviewOptional.get();

            if (review.getUser().getUserId() == userId) {
                review.setReviewText(reviewDTO.getReviewText());
                review.setRating(reviewDTO.getRating());

                review = reviewRepository.save(review);

                return review;
            } else {
                throw new CustomException("User id not found");
            }
        } else {
            throw new CustomException("Review id not found");
        }
    }


}
