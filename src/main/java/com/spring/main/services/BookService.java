package com.spring.main.services;

import com.spring.main.entity.BookEntity;
import com.spring.main.exception.CustomException;
import com.spring.main.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookEntity addBook(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBook(Long bookId) {
        System.out.println("delete book   kkkkkkkkkk");
        Optional<BookEntity> bookOptional = bookRepository.findById(bookId);
        BookEntity book=bookOptional.get();
        System.out.println(book.getId());
        if (bookOptional.isPresent() && book.getAvailability()) {
            bookRepository.deleteById(bookId);
            return true;
        } else {
            throw new CustomException("The book is borrowed. You can't delete");
        }
    }


    public BookEntity updateBook(Long bookId, BookEntity updatedBook) {
        Optional<BookEntity> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            BookEntity existingBook = bookOptional.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());
            existingBook.setAvailability(updatedBook.getAvailability());

            return bookRepository.save(existingBook);
        } else {
            return null;
        }
    }

}
