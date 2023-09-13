package com.spring.main.services;

import com.spring.main.entity.BookEntity;
import com.spring.main.entity.BorrowingEntity;
import com.spring.main.entity.ReservationEntity;
import com.spring.main.entity.UserEntity;
import com.spring.main.exception.CustomException;
import com.spring.main.repository.BookRepository;
import com.spring.main.repository.BorrowingRepository;
import com.spring.main.repository.ReserveRepository;
import com.spring.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReserveRepository reserveRepository;
    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    public ReservationEntity getbookReserve(Long bookId, Long userId){
        try {
            BorrowingEntity book=borrowingRepository.findByBookId(bookId)
                    .orElseThrow(()-> new CustomException("Book not found to reserve"));
            if (book.getBook().getAvailability()){
                throw new CustomException("You do not need to reserve because this book is available to borrow");
            }
            else {
                ReservationEntity reservationEntity = new ReservationEntity();
                BookEntity bookEntity=bookRepository.findById(bookId).get();
                UserEntity user= userRepository.findByUserId(userId).get();

                reservationEntity.setBook(bookEntity);
                reservationEntity.setUser(user);
                reservationEntity.setReservationDate(LocalDate.now());
                reservationEntity.setNotificationStatus("Reserved");

                reserveRepository.save(reservationEntity);
                return reservationEntity;

            }
        }catch (CustomException ex){
            throw new CustomException(ex.getMessage());
        }
    }
}
