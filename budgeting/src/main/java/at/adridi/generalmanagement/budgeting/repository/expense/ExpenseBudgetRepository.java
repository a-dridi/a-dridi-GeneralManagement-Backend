/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.ExpenseBudget;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 *
 * @author A.Dridi
 */
@Repository
public interface ExpenseBudgetRepository extends JpaRepository<ExpenseBudget, Long> {

    Optional<ExpenseBudget> findByExpensesbudgetId(Long expensesbudgetId);

    Optional<ExpenseBudget> findByExpenseCategory(ExpenseCategory expenseCategory);

    @Query(value = "SELECT * FROM Expense_Budget eb INNER JOIN Expense_Category ec ON eb.expense_category_expense_category_id=ec.expense_category_id WHERE user_id=?1 ORDER BY ec.category_title ASC", nativeQuery = true)
    Optional<ArrayList<ExpenseBudget>> getAllExpenseBudgetList(int userId);
}
