/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 *
 * @author A.Dridi
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseId(Long expenseId);

    Optional<ArrayList<Expense>> findByUserId(int userId);

    Optional<ArrayList<Expense>> findByTitleAndUserId(String title, int userId);

    Optional<ArrayList<Expense>> findByExpenseCategoryAndUserId(ExpenseCategory expenseCategory, int userId);

    Optional<ArrayList<Expense>> findByExpenseTimerangeAndUserId(ExpenseTimerange expenseTimerange, int userId);

    @Query(value = "SELECT * FROM Expense WHERE user_id=?1 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> getAllExpenseList(int userId);

    @Query(value = "SELECT * FROM Expense WHERE ((expense_timerange_timerange_id = 1 AND (EXTRACT(year FROM payment_date)) = ?1) OR (expense_timerange_timerange_id != 1)) AND ((expense_timerange_timerange_id = 12 AND (EXTRACT(year FROM payment_date)) = ?1) OR (expense_timerange_timerange_id != 12)) AND user_id=?2 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> getExpensesOfCertainYear(int year, int userId);

    @Query(value = "SELECT * FROM Expense WHERE ((expense_timerange_timerange_id = 1 AND (EXTRACT(month FROM payment_date)) = ?1 AND (EXTRACT(year FROM payment_date))=?2) OR (expense_timerange_timerange_id != 1)) AND ((expense_timerange_timerange_id = 12 AND (EXTRACT(year FROM payment_date)) = ?1) OR (expense_timerange_timerange_id != 12)) AND user_id=?3 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> getExpensesOfCertainMonthYear(int month, int year, int userId);

    @Query(value = "SELECT * FROM Expense WHERE expense_category_expense_category_id = ?1 AND user_id=?2 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> getExpensesByExpenseCategoryId(long expenseCategoryId, int userId);

    @Query(value = "SELECT * FROM Expense WHERE expense_timerange_expense_timerange_id = ?1 AND user_id=?2 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> getExpenseByTimerangeAndUserId(long expenseTimerangeId, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = 5 AND user_id = ?1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getMonthlyExpensesSum(int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = 9 AND user_id = ?1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getYearlyExpensesSum(int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = ?1 AND user_id = ?2 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerange(long expenseTimerangeId, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = ?1 AND (EXTRACT(month from payment_date) = ?2) AND user_id = ?3 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCertainMonth(int expenseTimerangeId, int month, int userId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = ?1 AND (EXTRACT(month from payment_date) = ?2) AND (EXTRACT(year from payment_date) = ?3) AND user_id = ?4 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCertainMonthYear(int expenseTimerangeId, int month, int year, int userId);

    @Query(value = "SELECT * FROM Expense WHERE (EXTRACT(month FROM payment_date) = ?1) AND EXTRACT(year FROM payment_date) = ?2 AND user_id=?3 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> findByMonthAndYear(int month, int year, int userId);

    @Query(value = "SELECT * FROM Expense WHERE (EXTRACT(year FROM payment_date) = ?1) AND user_id=?2 AND deleted=false ORDER BY expense_id DESC", nativeQuery = true)
    Optional<ArrayList<Expense>> findByYear(int year, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Expense SET deleted=false WHERE expense_id=?1", nativeQuery = true)
    void restoreDeletedExpense(int expenseId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Expense SET expense_category_expense_category_id = ?1 WHERE expense_category_expense_category_id = ?2 AND user_id = ?3", nativeQuery = true)
    void updateCategoryOfAllExpenses(long newExpenseCategoryId, long oldExpenseCategoryId, int userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Expense SET title=?1, expense_category_expense_category_id=?2, cent_value=?3, expense_timerange_timerange_id=?4, payment_date=cast(?5 AS timestamp), information=?6 WHERE expense_id=?7 and user_id=?8", nativeQuery = true)
    void updateExpenseTableData(String title, Long expenseCategoryId, int centValue, Long timerangeId, String paymentDate, String information, Long expenseId, int userId);

    /*
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Expense SET title=?1 WHERE expense_id=?2 and user_id=?3", nativeQuery = true)
    void updateExpenseTableData2(String title, Long expenseId, int userId);
     */
    @Query(value = "SELECT ec.categoryTitle, sum(e.centValue) FROM Expense e INNER JOIN ExpenseCategory ec ON e.expenseCategory=ec INNER JOIN ExpenseTimerange et ON e.expenseTimerange=et WHERE et.timerangeId=?1 AND e.userId=?2 AND deleted=false GROUP BY e.expenseCategory ORDER BY ec.expenseCategoryId ASC")
    Optional<ArrayList<Object[]>> getSumExpensesByTimerangeId(Long timerangeId, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Expense WHERE expense_timerange_timerange_id=?1 AND user_id=?2 AND expense_category_expense_category_id=?3 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumExpensesByTimerangeIdExpenseCategoryId(Long timerangeId, int userId, Long expenseCategoryId);

    @Query(value = "SELECT SUM(cent_value) FROM Expense WHERE expense_timerange_timerange_id = ?1 AND expense_category_expense_category_id = ?2 AND (EXTRACT(month from payment_date) = ?3) AND user_id = ?4 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumByCertainTimerangeAndCategoryAndCertainMonth(long expenseTimerangeId, long expenseCategoriyId, int month, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Expense WHERE (EXTRACT(year from payment_date)=?1) AND expense_timerange_timerange_id=1 AND expense_category_expense_category_id = ?2 AND user_id=?3 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumSingleExpensesByYearCategoryId(int year, long categoryId, int userId);

    @Query(value = "SELECT sum(cent_value) FROM Expense WHERE (EXTRACT(month from payment_date)=?1) AND user_id=?2 AND expense_timerange_timerange_id=1 AND deleted=false", nativeQuery = true)
    Optional<Integer> getSumSingleExpensesByMonth(int month, int userId);

}
