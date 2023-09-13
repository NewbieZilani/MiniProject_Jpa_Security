package com.spring.main.services;

import com.spring.main.dtoClasses.BorrowingHistoryDto;
import com.spring.main.entity.BookEntity;
import com.spring.main.entity.BorrowingEntity;
import com.spring.main.entity.ReservationEntity;
import com.spring.main.entity.UserEntity;
import com.spring.main.exception.CustomException;
import com.spring.main.repository.BookRepository;
import com.spring.main.repository.BorrowingRepository;
import com.spring.main.repository.ReserveRepository;
import com.spring.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BorrowService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingRepository borrowingRepository;
    private final ReserveRepository reserveRepository;

    @Autowired
    public BorrowService(
            BookRepository bookRepository,
            UserRepository userRepository,
            BorrowingRepository borrowingRepository,ReserveRepository reserveRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowingRepository = borrowingRepository;
        this.reserveRepository=reserveRepository;
    }


    public BorrowingEntity borrowBook(Long bookId, Long userId) {
        try {
            BookEntity book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CustomException("Book not found"));

            if (!book.getAvailability()) {
                throw new CustomException("Book is not available for borrowing");
            }

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found"));

            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, -15);
            Date borrowedDate = calendar.getTime();

            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, 10);
            Date returnDate = calendar.getTime();

            book.setAvailability(false);
            bookRepository.save(book);

            BorrowingEntity borrowingEntity = new BorrowingEntity();
            borrowingEntity.setUser(user);
            borrowingEntity.setBook(book);
            borrowingEntity.setBorrowDate(borrowedDate);
            borrowingEntity.setReturnDate(returnDate);
            borrowingRepository.save(borrowingEntity);

            return borrowingEntity;
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage());
        }
    }


    public BorrowingEntity returnBook(Long bookId, Long userId) {
        try {
            Optional<BorrowingEntity> optionalBorrowingEntity = borrowingRepository.findByBookId(bookId);

            if (optionalBorrowingEntity.isEmpty()) {
                throw new CustomException("Borrowing record not found for the specified book");
            }

            BorrowingEntity borrowingEntity = optionalBorrowingEntity.get();

            if (userId == borrowingEntity.getUser().getUserId()) {
                BookEntity book = borrowingEntity.getBook();
                book.setAvailability(true);
                bookRepository.save(book);

                Date currentDate = new Date();
                Date returnDate = new Date(currentDate.getTime() - 5 * 24 * 60 * 60 * 1000);
                borrowingEntity.setReturnDate(returnDate);

                for(ReservationEntity reservationEntity: reserveRepository.findAll()){
                    if(reservationEntity.getBook().getId() == book.getId()){
                        reservationEntity.setNotificationStatus("Available");
                        reserveRepository.save(reservationEntity);
                        break;
                    }
                }

                borrowingRepository.save(borrowingEntity);

                return borrowingEntity;
            } else {
                throw new CustomException("Invalid user for returning the book");
            }

        } catch (CustomException ex) {
            throw ex;
        }
    }


    public List<BorrowingHistoryDto> getUserBorrowingHistory(Long userId) {
        List<BorrowingEntity> borrowingRecords = borrowingRepository.findByUserUserId(userId);
        return borrowingRecords.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BorrowingHistoryDto mapToDto(BorrowingEntity entity) {
        BorrowingHistoryDto dto = new BorrowingHistoryDto();
        dto.setBookName(entity.getBook().getTitle());
        dto.setBorrowedDate(entity.getBorrowDate());
        dto.setReturnDate(entity.getReturnDate());
        return dto;
    }

}
