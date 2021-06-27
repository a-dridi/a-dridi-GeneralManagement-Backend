/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.service;

import at.adridi.generalmanagement.common.book.model.BookGraph;
import at.adridi.generalmanagement.common.book.repository.BookRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create data list for the book graph.
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class BookGraphService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Get a list containing book categories and their respective amounts.
     *
     * @return
     */
    public List<BookGraph> getAllBookCategoryAmountList(int userId) {
        return this.bookRepository.getBooksListGroupedByCategory(userId);
        
    }

    /**
     * Get a list containing book languages and their respective amounts.
     *
     * @return
     */
    public List<BookGraph> getAllBookLanguageAmountList(int userId) {
        return this.bookRepository.getBooksListGroupedByLanguage(userId);
    }
}
