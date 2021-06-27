/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.book.repository;

import at.adridi.generalmanagement.common.book.model.BookCategory;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    Optional<BookCategory> findByBookcategoryId(Long bookCategoryId);

    Optional<BookCategory> findByCategoryTitle(String categoryTitle);

    @Query(value = "SELECT * FROM Book_Category ORDER BY category_title ASC", nativeQuery = true)
    Optional<ArrayList<BookCategory>> getAllBookCategoryList();

}
