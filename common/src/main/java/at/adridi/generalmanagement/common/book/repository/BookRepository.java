/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.repository;

import at.adridi.generalmanagement.common.book.model.Book;
import at.adridi.generalmanagement.common.book.model.BookCategory;
import at.adridi.generalmanagement.common.book.model.BookGraph;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author A.Dridi
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookId(Long bookId);

    Optional<ArrayList<Book>> findByBookCategoryAndUserId(BookCategory bookCategory, int userId);

    @Query(value = "SELECT * FROM Book WHERE user_id=?1 AND deleted=false ORDER BY book_id DESC", nativeQuery = true)
    Optional<ArrayList<Book>> getAllBookList(int userId);

    @Query(value = "SELECT * FROM Book WHERE LOWER(title) ILIKE %?1% AND user_id=?2 AND deleted=false", nativeQuery = true)
    Optional<ArrayList<Book>> searchItemByTitle(String title, int userId);

    @Query(value = "SELECT new at.adridi.generalmanagement.common.book.model.BookGraph(b.bookLanguage, COUNT(b.bookId)) FROM Book b WHERE b.userId=?1 GROUP BY b.bookLanguage ORDER BY b.bookLanguage ASC")
    ArrayList<BookGraph> getBooksListGroupedByLanguage(int userId);

    @Query(value = "SELECT new at.adridi.generalmanagement.common.book.model.BookGraph(bc.categoryTitle, COUNT(b.bookId)) FROM Book AS b LEFT JOIN b.bookCategory AS bc WHERE b.userId=?1 GROUP BY bc.categoryTitle ORDER BY bc.categoryTitle ASC")
    ArrayList<BookGraph> getBooksListGroupedByCategory(int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Book SET deleted=false WHERE book_id=?1", nativeQuery = true)
    void restoreDeletedBook(Long bookId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Book SET title = ?1, book_category_bookcategory_id = ?2, location = ?3, book_availability_bookavailability_id=?4, book_language = ?5, year_date = ?6, isbn = ?7, information=?8 WHERE book_id=?9 and user_id=?10", nativeQuery = true)
    void updateBookTableData(String description, Long bookCategoryId, String location, Long bookAvailabilityId, String bookLanguage, String yearDate, String isbn, String information, Long bookId, int userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Book SET book_category_book_category_id = ?1 WHERE book_category_bookcategory_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateCategoryOfAllBookItems(long newBookCategoryId, long oldBookCategoryId, int userId);

}
