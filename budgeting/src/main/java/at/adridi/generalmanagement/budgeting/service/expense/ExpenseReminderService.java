/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseReminder;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseReminderRepository;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseReminderService {

    @Autowired
    private ExpenseReminderRepository expenseReminderRepository;

    /**
     * Get certain expense reminder for the passed expense id. Throws
     * DataValueNotFoundException if expense reminder is not available.
     *
     * @param id
     * @return
     */
    public ExpenseReminder getExpenseReminderByExpenseId(Long expenseId) {
        return this.expenseReminderRepository.getExpenseReminderByExpenseId(expenseId).orElseThrow(() -> new DataValueNotFoundException("Expense Reminder Does Not Exist"));
    }

    /**
     * Save new expense reminder.
     *
     * @param newExpenseReminder
     * @return saved expense reminder object. Null if not successful.
     */
    @Transactional
    public ExpenseReminder save(ExpenseReminder newExpenseReminder) {
        if (newExpenseReminder == null) {
            return null;
        }
        newExpenseReminder.setPayedDate(null);
        return this.expenseReminderRepository.save(newExpenseReminder);
    }

    /**
     * Delete an existing expense reminder
     *
     * @param expenseReminderId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long expenseReminderId) {
        if (expenseReminderId == null || expenseReminderId == 0) {
            return false;
        }
        try {
            this.expenseReminderRepository.deleteExpenseReminder(expenseReminderId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an existing expense reminder by expense id.
     *
     * @param expenseId
     * @return true if successful
     */
    @Transactional
    public boolean deleteByExpenseId(Long expenseId) {
        if (expenseId == null || expenseId == 0) {
            return false;
        }
        try {
            this.expenseReminderRepository.deleteExpenseReminderByExpenseId(expenseId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update expense reminder with the paid date and the date for the next due
     * date.
     *
     * @param expenseReminder
     * @return
     */
    @Transactional
    public boolean payExpenseReminder(ExpenseReminder expenseReminder) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
            LocalDate newDueDate = LocalDate.now();

            //Set next due date according to if the expense is monthly, quarterly, semi yearly or yearly, etc.
            switch ((expenseReminder.getExpense().getExpenseTimerange().getTimerangeId()).intValue()) {
                case 2:
                    newDueDate = newDueDate.plusDays(1);
                    break;
                case 3:
                    newDueDate = newDueDate.plusWeeks(1);
                    break;
                case 4:
                    newDueDate = newDueDate.plusWeeks(2);
                    break;
                case 5:
                    newDueDate = newDueDate.plusMonths(1);
                    break;
                case 6:
                    newDueDate = newDueDate.plusMonths(2);
                    break;
                case 7:
                    newDueDate = newDueDate.plusMonths(3);
                    break;
                case 8:
                    newDueDate = newDueDate.plusMonths(6);
                    break;
                case 9:
                    newDueDate = newDueDate.plusYears(1);
                    break;
                case 10:
                    newDueDate = newDueDate.plusYears(2);
                    break;
                case 11:
                    newDueDate = newDueDate.plusYears(5);
                    break;
            }

            if ((expenseReminder.getExpense().getExpenseTimerange().getTimerangeId()).intValue() != 1) {
                this.expenseReminderRepository.updateExpenseReminderTableData(dateFormat.format(newDueDate), dateFormat.format(new Date()), expenseReminder.getExpensereminderId());
            } else {
                //One time expense reminder - for one time expense, whish was paid
                this.expenseReminderRepository.deleteExpenseReminder(expenseReminder.getExpensereminderId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
