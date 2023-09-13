package com.spring.main.dtoClasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingHistoryDto {
    private String bookName;
    private Date borrowedDate;
    private Date returnDate;
}
