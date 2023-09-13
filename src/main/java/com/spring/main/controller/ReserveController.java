package com.spring.main.controller;

import com.spring.main.entity.BorrowingEntity;
import com.spring.main.entity.ReservationEntity;
import com.spring.main.services.BorrowService;
import com.spring.main.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class ReserveController {
    private final ReservationService reservationservice;
    private final BorrowService borrowService;

    @PostMapping("bookId/userId/reserve")
    public ResponseEntity<ReservationEntity> getbookReserve(@PathVariable Long bookId, @PathVariable Long userId){
         ReservationEntity reservationEntity = reservationservice.getbookReserve(bookId, userId);

         return new ResponseEntity<>(reservationEntity, HttpStatus.CREATED);
    }

}
