/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.controller;

import at.adridi.generalmanagement.common.book.model.Book;
import at.adridi.generalmanagement.common.book.model.BookCategory;
import at.adridi.generalmanagement.common.book.service.BookService;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Book - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/all/{userId}")
    public ResponseEntity<List<Book>> getAllBook(@PathVariable int userId) {
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = this.bookService.getAllBook(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookList)) {
            return status(HttpStatus.OK).body(bookList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Book>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/get/byId/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.bookService.getBookById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Book());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/get/byCategory/{userId}")
    public ResponseEntity<List<Book>> getAllBookByBookCategory(@RequestBody String bookCategoryJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        BookCategory bookCategory;
        try {
            bookCategory = objectMapper.readValue(bookCategoryJson, BookCategory.class);
            List<Book> bookList = new ArrayList<>();
            try {
                bookList = this.bookService.getBooksByCategoryAndUserId(bookCategory, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(bookList)) {
                return status(HttpStatus.OK).body(bookList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Book>());
            }
        } catch (IOException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Book>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/search/byDescription/{userId}")
    public ResponseEntity<List<Book>> searchBookByDescription(@RequestBody String descriptionPattern, @PathVariable int userId) {
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = this.bookService.searchBookItemByDescription(descriptionPattern, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookList)) {
            return status(HttpStatus.OK).body(bookList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Book>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/add")
    public ResponseEntity<Book> addBook(@RequestBody String newBookJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Book newBook;
        Book savedBook;
        try {
            newBook = objectMapper.readValue(newBookJson, Book.class);
            savedBook = this.bookService.save(newBook);
            return ResponseEntity.status(HttpStatus.OK).body(savedBook);
        } catch (IOException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Book());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/update")
    public ResponseEntity<Book> updateBook(@RequestBody String updatedBookJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Book updatedBook;
        Book savedBook;
        try {
            updatedBook = objectMapper.readValue(updatedBookJson, Book.class);
            savedBook = this.bookService.save(updatedBook);
            return ResponseEntity.status(HttpStatus.OK).body(savedBook);
        } catch (IOException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Book());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateBookTableData(@RequestBody String updatedBookJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Book updatedBook;
        try {
            updatedBook = objectMapper.readValue(updatedBookJson, Book.class);
            if (this.bookService.updateBookTableData(updatedBook.getTitle(), updatedBook.getBookCategory().getBookcategoryId(), updatedBook.getLocation(), updatedBook.getBookAvailability().getBookavailabilityId(), updatedBook.getBookLanguage(), updatedBook.getYearDate(), updatedBook.getIsbn(), updatedBook.getInformation(), updatedBook.getBookId(), updatedBook.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Book updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/updateBookCategories/{oldBookCategoryId}/{newBookCategoryId}/{userId}")
    public ResponseEntity<ResponseMessage> updateCategoryOfBookItems(@PathVariable long oldBookCategoryId, @PathVariable long newBookCategoryId, @PathVariable int userId) {
        if (this.bookService.updateBookItemsCategoryId(oldBookCategoryId, newBookCategoryId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Categories for Book items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Categories for Book items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/delete/{bookId}")
    public ResponseEntity<ResponseMessage> deleteBookById(@PathVariable Long bookId) {
        if (this.bookService.deleteById(bookId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Book item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOK + "/restore/{bookId}")
    public ResponseEntity<ResponseMessage> restoreDeletedBookItem(@PathVariable Long bookId) {
        try {
            this.bookService.restoreDeletedBook(bookId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(bookId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}
