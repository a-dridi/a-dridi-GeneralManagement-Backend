/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.controller;

import at.adridi.generalmanagement.common.book.model.BookAvailability;
import at.adridi.generalmanagement.common.book.service.BookAvailabilityService;
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
 * API: BookAvailability - Categories for Book
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class BookAvailabilityController {

    @Autowired
    private BookAvailabilityService bookAvailabilityService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/all")
    public ResponseEntity<List<BookAvailability>> getAllBookAvailability() {
        List<BookAvailability> bookAvailabilityList = new ArrayList<>();
        try {
            bookAvailabilityList = this.bookAvailabilityService.getAllBookAvailability();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(bookAvailabilityList)) {
            return status(HttpStatus.OK).body(bookAvailabilityList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<BookAvailability>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/get/byId/{id}")
    public ResponseEntity<BookAvailability> getBookAvailabilityById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.bookAvailabilityService.getBookAvailabilityById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/get/byTitle/{title}")
    public ResponseEntity<BookAvailability> getAllBookAvailabilityByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.bookAvailabilityService.getBookAvailabilityByAvailabilityTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/add")
    public ResponseEntity<BookAvailability> addBookAvailability(@RequestBody String newBookAvailabilityString) {
        if (newBookAvailabilityString != null || newBookAvailabilityString.trim().equals("")) {
            BookAvailability newBookAvailability = new BookAvailability();
            newBookAvailability.setAvailabilityTitle(newBookAvailabilityString);
            BookAvailability createdBookAvailability = this.bookAvailabilityService.save(newBookAvailability);
            if (createdBookAvailability != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdBookAvailability);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/update")
    public ResponseEntity<BookAvailability> updateBookAvailability(@RequestBody String updatedBookAvailabilityString) {
        ObjectMapper objectMapper = new ObjectMapper();
        BookAvailability updatedBookAvailability;
        try {
            updatedBookAvailability = objectMapper.readValue(updatedBookAvailabilityString, BookAvailability.class);
            BookAvailability newUpdatedBookAvailability = this.bookAvailabilityService.save(updatedBookAvailability);
            if (newUpdatedBookAvailability != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedBookAvailability);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
            }
        } catch (IOException ex) {
            Logger.getLogger(BookAvailabilityController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookAvailability());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/delete/byId/{expenseAvailabilityId}")
    public ResponseEntity<ResponseMessage> deleteBookAvailabilityById(@PathVariable Long bookAvailabilityId) {
        if (this.bookAvailabilityService.deleteById(bookAvailabilityId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your book availability was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book Availability cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKAVAILABILITY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteBookAvailabilityByTitle(@PathVariable String title) {
        if (this.bookAvailabilityService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your book availability was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Book availability does not exists!"));
        }
    }

}
