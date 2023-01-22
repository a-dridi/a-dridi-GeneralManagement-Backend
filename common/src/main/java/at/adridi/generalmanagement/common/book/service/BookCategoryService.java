/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.service;

import at.adridi.generalmanagement.common.book.model.BookCategory;
import at.adridi.generalmanagement.common.book.repository.BookCategoryRepository;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of BookCategory DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class BookCategoryService {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    /**
     * Save new book category.
     *
     * @param newBookCategory
     * @return null if failed. If successful: the saved object.
     */
    @Transactional()
    public BookCategory save(BookCategory newBookCategory) {
        if (newBookCategory == null) {
            return null;
        }
        return this.bookCategoryRepository.save(newBookCategory);
    }

    /**
     * Get certain book category with the passed id. Throws
     * DataValueNotFoundException if book category is not available.
     *
     * @param id
     * @return
     */
    public BookCategory getBookCategoryById(Long id) {
        return this.bookCategoryRepository.findByBookcategoryId(id).orElseThrow(() -> new DataValueNotFoundException("Book Category Does Not Exist"));
    }

    /**
     * Get certain book category with the passed category title. Throws
     * DataValueNotFoundException if book category is not available.
     *
     * @param categoryTitle
     * @return
     */
    public BookCategory getBookCategoryByCategoryTitle(String categoryTitle) {
        return this.bookCategoryRepository.findByCategoryTitle(categoryTitle).orElseThrow(() -> new DataValueNotFoundException("Book Category Does Not Exist"));
    }

    /**
     * Get a List of all saved book categories
     *
     * @return
     */
    public List<BookCategory> getAllBookCategory() {
        return this.bookCategoryRepository.getAllBookCategoryList().orElseThrow(() -> new DataValueNotFoundException("Book Category List could not be loaded!"));
    }

    /**
     * Delete an existing book category
     *
     * @param bookCategoryId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long bookCategoryId) {
        if (bookCategoryId == null || bookCategoryId == 0) {
            return false;
        }

        BookCategory bookCategory = this.getBookCategoryById(bookCategoryId);

        try {
            this.bookCategoryRepository.delete(bookCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing book category
     *
     * @param categoryTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String categoryTitle) {
        if (categoryTitle == null || categoryTitle.trim().isEmpty()) {
            return false;
        }
        BookCategory bookCategory = this.getBookCategoryByCategoryTitle(categoryTitle);

        try {
            this.bookCategoryRepository.delete(bookCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
