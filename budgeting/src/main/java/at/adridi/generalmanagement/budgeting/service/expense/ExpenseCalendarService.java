/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCalendar;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseRepository;
import at.adridi.generalmanagement.budgeting.util.ExpenseTimerangeShortcuts;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Create Expenses containing data (expense title, character shortcut for
 * expense timerange and payment date adjusted to current year) to display in
 * calendar view in the calendar tab of expenses.
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseCalendarService {

    @Autowired
    private ExpenseRepository expenseRepository;
    //Counter counts all created expense calendar item - used as in id for a expense calendar item
    private int expenseCalendarCounter = 1;

    /**
     * Create recurring Expenses (between daily and yearly) containing data
     * (expense title, character shortcut for expense timerange and payment date
     * adjusted to current year) to display in calendar view in the calendar tab
     * of expenses. DataValueNotFoundException if not available.
     *
     * @param userId
     * @return
     */
    public List<ExpenseCalendar> getExpensesCalendarData(int userId) {
        List<Expense> allExpenses = this.expenseRepository.getAllExpenseList(userId).orElseThrow(() -> new DataValueNotFoundException("Expense Category Does Not Exist"));
        List<ExpenseCalendar> expenseCalendarList = new ArrayList<>();
        for (Expense expense : allExpenses) {
            String expenseCalendarTitle = new StringBuilder(this.createExpenseTimerangeShortcut(expense.getExpenseTimerange().getTimerangeId())).append(" ").append(expense.getTitle()).toString();
            try {
                expenseCalendarList.addAll(this.adjustExpenseCalendarColorAmountAndPaymentDate(expenseCalendarTitle, expense.getPaymentDate(), expense.getExpenseTimerange().getTimerangeId()));
            } catch (ParseException ex) {
                Logger.getLogger(ExpenseCalendarService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return expenseCalendarList;
    }

    /**
     * HELPER FUNCTION. Returns a character shortcut for a expense timerange
     * (recurring expenses between timeranges daily and yearly). Used to display
     * timerange of an expense in a short form in a calendar view.
     *
     * @param timerangeId
     * @return Empty string if timerange id is wrong.
     */
    public String createExpenseTimerangeShortcut(long timerangeId) {
        switch ((int) timerangeId) {
            case 2:
                return ExpenseTimerangeShortcuts.DAILY;
            case 3:
                return ExpenseTimerangeShortcuts.WEEKLY;
            case 4:
                return ExpenseTimerangeShortcuts.BIWEEKLY;
            case 5:
                return ExpenseTimerangeShortcuts.MONTHLY;
            case 6:
                return ExpenseTimerangeShortcuts.TWO_MONTHS;
            case 7:
                return ExpenseTimerangeShortcuts.THREE_MONTHS;
            case 8:
                return ExpenseTimerangeShortcuts.SIX_MONTHS;
            case 9:
                return ExpenseTimerangeShortcuts.YEARLY;
            default:
                return "";
        }
    }

    /**
     * HELPER FUNCTION. Create list of expense calendar list containg certain
     * amount of expense calendar item, color of text & background and payment
     * date adjusted to timerange id.
     *
     * @param expenseCalendarTitle
     * @param paymentDate
     * @param timerangeId
     * @return empty arraylist if not available or an error happened during
     * creation.
     */
    public List<ExpenseCalendar> adjustExpenseCalendarColorAmountAndPaymentDate(String expenseCalendarTitle, Date paymentDate, long timerangeId) throws ParseException {
        List<ExpenseCalendar> adjustedExpenseCalendarList = new ArrayList<>();
        Date currentDate = new Date();
        int currentMonth = 0;
        int currentYear = 0;
        int dayOfPaymentDate = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yyyy");
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("YYYY-MM-dd");

        try {
            currentMonth = Integer.parseInt((new SimpleDateFormat("MM")).format(currentDate));
            currentYear = Integer.parseInt((new SimpleDateFormat("YYYY")).format(currentDate));
            dayOfPaymentDate = Integer.parseInt((new SimpleDateFormat("dd")).format(paymentDate));
        } catch (Exception e) {
            return new ArrayList<ExpenseCalendar>();
        }

        switch ((int) timerangeId) {
            case 2:
                //Recreate a list of expense calendar for all days of current month
                int daysInCurrentMonth = (YearMonth.of(currentYear, currentMonth)).lengthOfMonth();
                 {
                    for (int i = 1; i <= daysInCurrentMonth; i++) {
                        try {
                            LocalDate adjustedPaymentDate = LocalDate.of(currentYear, currentMonth, i);
                            adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, adjustedPaymentDate.toString(), ExpenseTimerangeShortcuts.DAILY_BG_COLOR, ExpenseTimerangeShortcuts.DAILY_TXT_COLOR));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return new ArrayList<ExpenseCalendar>();
                        }
                    }
                    return adjustedExpenseCalendarList;
                }
            case 3: {
                LocalDate adjustedPaymentDate = LocalDate.of(currentYear, currentMonth, dayOfPaymentDate);
                for (int i = 1; i <= 4; i++) {
                    adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, adjustedPaymentDate.toString(), ExpenseTimerangeShortcuts.WEEKLY_BG_COLOR, ExpenseTimerangeShortcuts.WEEKLY_TXT_COLOR));
                    try {
                        adjustedPaymentDate = adjustedPaymentDate.plusDays(7);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return new ArrayList<ExpenseCalendar>();
                    }
                }
                return adjustedExpenseCalendarList;
            }
            case 4: {
                LocalDate adjustedPaymentDate = LocalDate.of(currentYear, currentMonth, dayOfPaymentDate);
                for (int i = 1; i <= 2; i++) {
                    adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, adjustedPaymentDate.toString(), ExpenseTimerangeShortcuts.BIWEEKLY_BG_COLOR, ExpenseTimerangeShortcuts.BIWEEKLY_TXT_COLOR));
                    try {
                        adjustedPaymentDate = adjustedPaymentDate.plusDays(14);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return new ArrayList<ExpenseCalendar>();
                    }
                }
                return adjustedExpenseCalendarList;
            }
            case 5: {
                LocalDate adjustedPaymentDate = LocalDate.of(currentYear, currentMonth, dayOfPaymentDate);

                adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, adjustedPaymentDate.toString(), ExpenseTimerangeShortcuts.MONTHLY_BG_COLOR, ExpenseTimerangeShortcuts.MONTHLY_TXT_COLOR));
                return adjustedExpenseCalendarList;
            }
            case 6:
                adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, calendarDateFormat.format(paymentDate), ExpenseTimerangeShortcuts.TWO_MONTHS_BG_COLOR, ExpenseTimerangeShortcuts.TWO_MONTHS_TXT_COLOR));
                return adjustedExpenseCalendarList;
            case 7:
                adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, calendarDateFormat.format(paymentDate), ExpenseTimerangeShortcuts.THREE_MONTHS_BG_COLOR, ExpenseTimerangeShortcuts.THREE_MONTHS_TXT_COLOR));
                return adjustedExpenseCalendarList;
            case 8:
                adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, calendarDateFormat.format(paymentDate), ExpenseTimerangeShortcuts.SIX_MONTHS_BG_COLOR, ExpenseTimerangeShortcuts.SIX_MONTHS_TXT_COLOR));
                return adjustedExpenseCalendarList;
            case 9:
                adjustedExpenseCalendarList.add(new ExpenseCalendar(this.expenseCalendarCounter++, expenseCalendarTitle, calendarDateFormat.format(paymentDate), ExpenseTimerangeShortcuts.YEARLY_BG_COLOR, ExpenseTimerangeShortcuts.YEARLY_TXT_COLOR));
                return adjustedExpenseCalendarList;
            default:
                return new ArrayList<ExpenseCalendar>();
        }
    }
}
