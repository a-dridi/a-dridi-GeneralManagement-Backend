/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.controller;

import at.adridi.generalmanagement.common.book.model.BookCategory;
import at.adridi.generalmanagement.common.book.service.BookCategoryService;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
 * API: BookCategory - Categories for Book
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/all")
    public ResponseEntity<List<BookCategory>> getAllBookCategory() {
        List<BookCategory> bookCategoryList = new ArrayList<>();
        try {
            bookCategoryList = this.bookCategoryService.getAllBookCategory();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookCategoryList)) {
            return status(HttpStatus.OK).body(bookCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<BookCategory>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/get/byId/{id}")
    public ResponseEntity<BookCategory> getBookCategoryById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.bookCategoryService.getBookCategoryById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new BookCategory());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/get/byTitle/{title}")
    public ResponseEntity<BookCategory> getAllBookCategoryByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.bookCategoryService.getBookCategoryByCategoryTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new BookCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/add")
    public ResponseEntity<BookCategory> addBookCategory(@RequestBody String newBookCategoryString) {
        if (newBookCategoryString != null || newBookCategoryString.trim().equals("")) {
            BookCategory newBookCategory = new BookCategory();
            newBookCategory.setCategoryTitle(newBookCategoryString);
            BookCategory createdBookCategory = this.bookCategoryService.save(newBookCategory);
            if (createdBookCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdBookCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookCategory());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookCategory());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/update")
    public ResponseEntity<BookCategory> updateBookCategory(@RequestBody String updatedBookCategoryString) {
        ObjectMapper objectMapper = new ObjectMapper();
        BookCategory updatedBookCategory;
        try {
            updatedBookCategory = objectMapper.readValue(updatedBookCategoryString, BookCategory.class);
            BookCategory newUpdatedBookCategory = this.bookCategoryService.save(updatedBookCategory);
            if (newUpdatedBookCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedBookCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(BookCategoryController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookCategory());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/delete/byId/{expenseCategoryId}")
    public ResponseEntity<ResponseMessage> deleteBookCategoryById(@PathVariable Long bookCategoryId) {
        if (this.bookCategoryService.deleteById(bookCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your book category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKCATEGORY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteBookCategoryByTitle(@PathVariable String title) {
        if (this.bookCategoryService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your book category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book category does not exists!"));
        }
    }

}
