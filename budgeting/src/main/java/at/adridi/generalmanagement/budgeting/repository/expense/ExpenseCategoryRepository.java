/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
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
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByExpenseCategoryId(Long expenseCategoryId);

    Optional<ExpenseCategory> findByCategoryTitleAndUserId(String categoryTitle, int userId);

    @Query(value = "SELECT * FROM Expense_Category WHERE user_id = ?1 ORDER BY category_title ASC", nativeQuery = true)
    Optional<ArrayList<ExpenseCategory>> getAllExpenseCategoryList(int userId);

}
