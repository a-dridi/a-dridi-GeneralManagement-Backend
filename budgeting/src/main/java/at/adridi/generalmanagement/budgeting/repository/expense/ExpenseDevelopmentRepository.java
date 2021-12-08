/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.ExpenseDevelopment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface ExpenseDevelopmentRepository extends JpaRepository<ExpenseDevelopment, Long> {

    Optional<ExpenseDevelopment> findByExpenseDevelopmentId(Long expenseDevelopmentId);

    Optional<List<ExpenseDevelopment>> findByMonthAndYearAndUserId(int month, int year, int userId);

    Optional<List<ExpenseDevelopment>> findByDateDisplayAndUserId(String dateDisplay, int userId);

    @Query(value = "SELECT * FROM Expense_Development ORDER BY expense_development_id DESC", nativeQuery = true)
    Optional<ArrayList<ExpenseDevelopment>> getAllExpenseDevelopmentList(Integer userId);

    @Query(value = "SELECT * FROM Expense_Development ORDER BY expense_development_id DESC LIMIT 24", nativeQuery = true)
    Optional<ArrayList<ExpenseDevelopment>> getLast24ExpenseDevelopmentList(Integer userId);

    @Query(value = "SELECT * FROM Expense_Development WHERE user_id = ?1 ORDER BY expense_development_id DESC LIMIT 1", nativeQuery = true)
    Optional<ExpenseDevelopment> getLastExpenseDevelopmentRow(int userId);

    @Query(value = "SELECT * FROM Expense_Development WHERE month >=?1 AND month <=?2 AND year >=?3 AND year <=?4 AND user_id = ?5 ORDER BY expense_development_id ASC", nativeQuery = true)
    Optional<List<ExpenseDevelopment>> getExpenseDevelopmentItemsForMonthAndYearRange(int monthStart, int monthEnd, int yearStart, int yearEnd, int userId);

    @Query(value = "SELECT SUM(cent_sum) FROM Expense_Development WHERE year = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Integer> getCentSumOfExpenseDevelopmentOfCertainYear(Integer year, Integer userId);

    @Query(value = "SELECT SUM(cent_sum) FROM Expense_Development WHERE year >= ?1 AND year <= ?2 user_id = ?3", nativeQuery = true)
    Optional<Integer> getCentSumOfExpenseDevelopmentOfYearRange(Integer startYear, Integer endYear, Integer userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Expense_Development SET month=?1, year=?2, date_display=?3, cent_sum=?4 WHERE expense_development_id=?5 and user_id=?6", nativeQuery = true)
    void updateExpenseDevelopmentTableData(Integer month, Integer year, String dateDisplay, Integer centSum, Long expenseDevelopmentId, int userId);

}
