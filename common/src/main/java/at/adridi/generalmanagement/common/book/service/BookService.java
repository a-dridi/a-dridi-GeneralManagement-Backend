/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.service;

import at.adridi.generalmanagement.common.book.model.Book;
import at.adridi.generalmanagement.common.book.model.BookCategory;
import at.adridi.generalmanagement.common.book.repository.BookCategoryRepository;
import at.adridi.generalmanagement.common.book.repository.BookRepository;
import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of book DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private BookCategoryService bookCategoryService;

    /**
     * Save new Book.
     *
     * @param newBook
     * @return saved book object. Null if not successful.
     */
    @Transactional
    public Book save(Book newBook) {
        if (newBook == null) {
            return null;
        }
        return this.bookRepository.save(newBook);
    }

    /**
     * Get certain Book with the passed id. Throws DataValueNotFoundException if
     * Book is not available.
     *
     * @param id
     * @return
     */
    public Book getBookById(Long id) {
        return this.bookRepository.findByBookId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Book Does Not Exist"));
    }

    /**
     * Get certain Book with the passed BookCategory object. Throws
     * DataValueNotFoundException if Book is not available.
     *
     * @param bookCategory
     * @param userId
     * @return
     */
    public List<Book> getBooksByCategoryAndUserId(BookCategory bookCategory, int userId) {
        return this.bookRepository.findByBookCategoryAndUserId(bookCategory, userId).orElseThrow(() -> new DataValueNotFoundException("Book Does Not Exist"));
    }

    /**
     * Get a List of all saved book items of a user
     *
     * @param userId
     * @return
     */
    public List<Book> getAllBook(int userId) {
        return this.bookRepository.getAllBookList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Book items or does not exist"));
    }

    /**
     * Get a List of the found book items of a user for your searched title.
     *
     * @param userId
     * @return
     */
    public List<Book> searchBookItemByDescription(String title, int userId) {
        return this.bookRepository.searchItemByTitle(title, userId).orElseThrow(() -> new DataValueNotFoundException("Book items with the title '" + title + "' not found "));
    }

    /**
     * Update only table data (without attachment info) of an Book item.
     *
     * @param title
     * @param bookCategoryId
     * @param location
     * @param bookAvailabilityId
     * @param language
     * @param isbn
     * @param information
     * @param bookId
     * @param userId
     * @return
     */
    @Transactional
    public int updateBookTableData(String title, Long bookCategoryId, String location, Long bookAvailabilityId, String bookLanguage, String yearDate, String isbn, String information, Long bookId, int userId) {
        try {
            this.bookRepository.updateBookTableData(title, bookCategoryId, location, bookAvailabilityId, bookLanguage, yearDate, isbn, information, bookId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Update book categories of all book items with oldBookCategoryId and
     * userId to the new book category id. And delete old book category object.
     *
     * @param oldBookCategoryId
     * @param newBookCategoryId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateBookItemsCategoryId(long oldBookCategoryId, long newBookCategoryId, int userId) {
        if ((oldBookCategoryId > 0 && newBookCategoryId > 0 && userId > 0)) {
            BookCategory oldBookCategory = this.bookCategoryService.getBookCategoryById(oldBookCategoryId);
            BookCategory newExpenseCategory = this.bookCategoryService.getBookCategoryById(newBookCategoryId);

            if ((oldBookCategory != null && !oldBookCategory.getCategoryTitle().equals("")) && (newExpenseCategory != null && !newExpenseCategory.getCategoryTitle().equals(""))) {
                try {
                    this.bookRepository.updateCategoryOfAllBookItems(newBookCategoryId, oldBookCategoryId, userId);
                    this.bookCategoryRepository.delete(oldBookCategory);
                    return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Delete an existing book
     *
     * @param bookId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long bookId) {
        if (bookId == null || bookId == 0) {
            return false;
        }
        Book book = null;
        try {
            book = this.getBookById(bookId);
        } catch (DataValueNotFoundException e) {
        }

        if (book != null) {
            book.setDeleted(true);
            try {
                if (this.bookRepository.save(book) != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param bookId
     */
    @Transactional
    public void restoreDeletedBook(Long bookId) {
        this.bookRepository.restoreDeletedBook(bookId);
    }

}
