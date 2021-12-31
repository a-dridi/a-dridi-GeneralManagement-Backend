/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.ExpenseReminder;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author A.Dridi
 */
public interface ExpenseReminderRepository extends JpaRepository<ExpenseReminder, Long> {

    @Query(value = "SELECT * FROM Expense_Reminder WHERE expense_expense_id=?1", nativeQuery = true)
    Optional<ExpenseReminder> getExpenseReminderByExpenseId(Long expenseId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Expense_Reminder SET due_date=cast(?1 AS timestamp), payed_date=cast(?2 AS timestamp) WHERE expensereminder_id=?3", nativeQuery = true)
    void updateExpenseReminderTableData(String dueDate, String payedDate, Long expensereminderId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Expense_Reminder WHERE expensereminder_id=?1", nativeQuery = true)
    int deleteExpenseReminder(Long expensereminderId);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Expense_Reminder WHERE expense_expense_id=?1", nativeQuery = true)
    int deleteExpenseReminderByExpenseId(Long expenseId);

}
