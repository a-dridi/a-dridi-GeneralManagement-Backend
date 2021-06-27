/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.controller;

import at.adridi.generalmanagement.common.book.model.BookGraph;
import at.adridi.generalmanagement.common.book.service.BookGraphService;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Book - Graph
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class BookGraphController {

    @Autowired
    private BookGraphService bookGraphService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKGRAPH + "/amountBy/category/{userId}")
    public ResponseEntity<List<BookGraph>> getBookCategoryAmountList(@PathVariable int userId) {
        List<BookGraph> bookList = new ArrayList<>();
        try {
            bookList = this.bookGraphService.getAllBookCategoryAmountList(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!CollectionUtils.isEmpty(bookList)) {
            return status(HttpStatus.OK).body(bookList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<BookGraph>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_BOOKGRAPH + "/amountBy/language/{userId}")
    public ResponseEntity<List<BookGraph>> getBookLanguageAmountList(@PathVariable int userId) {
        List<BookGraph> bookList = new ArrayList<>();
        try {
            bookList = this.bookGraphService.getAllBookLanguageAmountList(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!CollectionUtils.isEmpty(bookList)) {
            return status(HttpStatus.OK).body(bookList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<BookGraph>());
        }
    }
}
