/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseGraph;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Implementation of expense DAO
 *
 * @author A.Dridi
 */
@Service
@Transactional
@NoArgsConstructor
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    /**
     * Save new expense.
     *
     * @param newExpense
     * @return saved expense object. Null if not successful.
     */
    public Expense save(Expense newExpense) {
        if (newExpense == null) {
            return null;
        }
        return this.expenseRepository.save(newExpense);

    }

    /**
     * Get certain expense with the passed id. Throws DataValueNotFoundException
     * if expense is not available.
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Expense getExpenseById(Long id) {
        return this.expenseRepository.findByExpenseId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Does Not Exist"));
    }

    /**
     * Get certain expense with the passed title. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Expense> getExpenseByTitle(String title, int userId) {
        return this.expenseRepository.findByTitleAndUserId(title, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Does Not Exist"));
    }

    /**
     * Get certain expense with the passed ExpenseCategory object. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param expenseCategory
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ArrayList<Expense> getExpensesByCategoryAndUserId(ExpenseCategory expenseCategory, int userId) {
        return this.expenseRepository.findByExpenseCategoryAndUserId(expenseCategory, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Does Not Exist"));
    }

    /**
     * Get certain expense with the passed ExpenseTimerange object. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param expenseTimerange
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ArrayList<Expense> getExpensesByExpenseTimerangeAndUserId(ExpenseTimerange expenseTimerange, int userId) {
        return this.expenseRepository.findByExpenseTimerangeAndUserId(expenseTimerange, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Does Not Exist"));
    }

    /**
     * Get a List of all saved expenses of a user
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Expense> getAllExpense(int userId) {
        return this.expenseRepository.getAllExpenseList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have expenses or does not exist"));
    }

    /**
     * Get all expenses with one time expenses that belong to a certain year.
     * Throws DataValueNotFoundException if not available.
     *
     * @param month
     * @param year
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Expense> getExpensesByMonthYear(int month, int year, int userId) {
        return this.expenseRepository.getExpensesOfCertainMonthYear(month, year, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses could not be load for the passed arguments!"));
    }

    /**
     * Get all expenses with one time expenses that belong to a certain year.
     * Throws DataValueNotFoundException if not available.
     *
     * @param year
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Expense> getExpensesByYear(int year, int userId) {
        return this.expenseRepository.getExpensesOfCertainYear(year, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses could not be load for the passed arguments!"));
    }

    /**
     * Delete an existing expense
     *
     * @param expenseId
     * @return true if successful
     */
    public boolean deleteById(Long expenseId) {
        if (expenseId == null || expenseId == 0) {
            return false;
        }

        Expense expense = this.getExpenseById(expenseId);
        if (expense != null) {
            expense.setDeleted(true);
            try {
                if (this.expenseRepository.save(expense) != null) {
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
     * Get single and custom expense of a certain month calculated.
     * DataValueNotFoundException if it is not available.
     *
     * @param expenseTimerange
     * @param month Month number 1-12
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Integer getSingleAndCustomExpensesByCertainMonth(int month, int userId) {
        Integer expensesOfCertainMonth = this.getMonthlyRecurringExpensesSum(userId);
        expensesOfCertainMonth += this.expenseRepository.getSumByCertainTimerangeAndCertainMonth(1, month, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses of month" + month + " could not be loaded"));
        expensesOfCertainMonth += this.expenseRepository.getSumByCertainTimerangeAndCertainMonth(12, month, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses of month" + month + " could not be loaded"));
        return expensesOfCertainMonth;
    }

    /**
     * Get Expenses of certain year (onetime expenses with payment date on the
     * certain year). DataValueNotFoundException if it is not available.
     *
     * @param year example 2000, 2010
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ArrayList<Expense> getExpensesOfCertainYear(int year, int userId) {
        return this.expenseRepository.getExpensesOfCertainYear(year, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses of year" + year + " could not be loaded"));
    }

    /**
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param expenseId
     */
    public void restoreDeletedExpense(int expenseId) {
        this.expenseRepository.restoreDeletedExpense(expenseId);
    }

    /**
     * Get certain expense with the passed ExpenseCategory id. Throws
     * DataValueNotFoundException if expense is not available.
     *
     * @param expenseCategoryId
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ArrayList<Expense> getExpenseByExpenseCategoryId(int expenseCategoryId, int userId) {
        return this.expenseRepository.getExpensesByExpenseCategoryId(expenseCategoryId, userId).orElseThrow(() -> new DataValueNotFoundException("Expenses with the category id " + userId + " could not be loaded"));
    }

    /**
     * Update expense categories of all expenses with oldExpenseCategoryId and
     * userId to the new expense category id. And delete old expense category
     * object.
     *
     * @param oldExpenseCategoryId
     * @param newExpenseCategoryId
     * @param userId
     * @return ß if successful. -1 if unsucessful.
     */
    public int updateExpensesExpenseCategoryId(int oldExpenseCategoryId, int newExpenseCategoryId, int userId) {

        if ((oldExpenseCategoryId > 0 && newExpenseCategoryId > 0 && userId > 0)) {
            ExpenseCategory oldExpenseCategory = this.expenseCategoryService.getExpenseCategoryById((long) oldExpenseCategoryId);
            ExpenseCategory newExpenseCategory = this.expenseCategoryService.getExpenseCategoryById((long) newExpenseCategoryId);

            if ((oldExpenseCategory != null && !oldExpenseCategory.getCategoryTitle().equals("")) && (newExpenseCategory != null && !newExpenseCategory.getCategoryTitle().equals(""))) {
                try {
                    this.expenseRepository.updateExpensesCategory(oldExpenseCategoryId, newExpenseCategoryId, userId);
                    this.expenseCategoryService.deleteById((long) newExpenseCategoryId);
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
     * Update only table data (without attachment info) of an Expense item
     *
     * @param title
     * @param expenseCategoryId
     * @param centValue
     * @param timerangeId
     * @param paymentDate
     * @param information
     * @param expenseId
     * @param userId
     * @return
     */
    public int updateExpenseTableData(String title, Long expenseCategoryId, int centValue, Long timerangeId, Date paymentDate, String information, Long expenseId, int userId) {
        try {
            this.expenseRepository.updateExpenseTableData(title, expenseCategoryId, centValue, timerangeId, paymentDate.toString(), information, expenseId, userId);

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get monthly sum of recurring expenses for every expense category of a
     * user. Recurring expenses are divided down to a month.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExpenseGraph> getMonthlySumExpenses(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            List<ExpenseGraph> monthlyExpenses = new ArrayList();
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                int monthlySum = 0;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 30;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 4;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 6;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 12;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 24;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 60;
                monthlyExpenses.add(new ExpenseGraph(expenseCategory.getCategoryTitle(), monthlySum));
            }
            return monthlyExpenses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get yearly sum of recurring expenses for every expense category of a
     * user. Recurring expenses are divided down to a year.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExpenseGraph> getYearlySumExpenses(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            List<ExpenseGraph> yearlyExpenses = new ArrayList();
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                int yearlySum = 0;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 365;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 52;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 26;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 12;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 6;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 5;
                yearlyExpenses.add(new ExpenseGraph(expenseCategory.getCategoryTitle(), yearlySum));
            }
            return yearlyExpenses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get sum of all expenses (incl. recurring expenses) happened in this year
     * of a user. Recurring expenses are divided down to a year. Expenses sum
     * for every expense category.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExpenseGraph> getSumExpensesOfCurrentYear(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            List<ExpenseGraph> currentYearExpenses = new ArrayList();
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                int currentYearSum = 0;
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                int currentYear = Integer.parseInt(yearFormat.format(Calendar.getInstance().getTime()));
                currentYearSum += this.expenseRepository.getSumSingleExpensesByYear(currentYear, userId);
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 365;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 52;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 26;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 12;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 6;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                currentYearSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 5;
                currentYearExpenses.add(new ExpenseGraph(expenseCategory.getCategoryTitle(), currentYearSum));
            }
            return currentYearExpenses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get monthly expenses and other recurring expenses calculated down to a
     * monthly rate. 0 if it is not available.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public int getMonthlyRecurringExpensesSum(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            int monthlySum = 0;
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 30;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 4;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 6;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 12;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 24;
                monthlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 60;
            }
            return monthlySum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get yearly expenses and other recurring expenses calculated down to a
     * yearly rate. 0 if it is not available.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public int getYearlyRecurringExpensesSum(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            int yearlySum = 0;
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 365;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 52;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 26;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 12;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 6;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                yearlySum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 5;
            }
            return yearlySum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get sum of current expenses this month. 0 if it is not available.
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public int getCurrentMonthExpenses(int userId) {
        try {
            List<ExpenseCategory> allExpenseCategories = this.expenseCategoryService.getAllExpenseCategory();
            int currentMonthSum = 0;
            for (ExpenseCategory expenseCategory : allExpenseCategories) {
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                int currentMonth = Integer.parseInt(monthFormat.format(Calendar.getInstance().getTime()));
                currentMonthSum += this.expenseRepository.getSumSingleExpensesByMonth(currentMonth, userId);
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(2L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 30;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(3L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 4;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(4L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) * 2;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(5L, userId, expenseCategory.getExpenseCategoryId())).orElse(0);
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(6L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 2;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(7L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 4;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(8L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 6;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(9L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 12;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(10L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 24;
                currentMonthSum += (this.expenseRepository.getSumExpensesByTimerangeIdExpenseCategoryId(11L, userId, expenseCategory.getExpenseCategoryId())).orElse(0) / 60;
            }
            return currentMonthSum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
