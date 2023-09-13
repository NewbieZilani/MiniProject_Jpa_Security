package com.spring.main.controller;

import com.spring.main.entity.BookEntity;
import com.spring.main.response.CustomResponse;
import com.spring.main.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    public final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookEntity> addBook(@RequestBody BookEntity bookEntity){
        BookEntity newBook= bookService.addBook(bookEntity);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        List<BookEntity> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @DeleteMapping("/{bookId}/delete")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        System.out.println("ppppppppppppppppppppppppppppppppp");
        boolean deleted = bookService.deleteBook(bookId);
        if (deleted) {
            return new ResponseEntity<>("Successfull", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{bookId}/update")
    public ResponseEntity<BookEntity> updateBook(
            @PathVariable Long bookId,
            @RequestBody BookEntity updatedBook) {
        BookEntity updated = bookService.updateBook(bookId, updatedBook);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
