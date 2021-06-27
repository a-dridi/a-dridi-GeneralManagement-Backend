/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseBudget;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseBudgetRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of ExpenseBudgetService DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseBudgetService {

    @Autowired
    private ExpenseBudgetRepository expenseBudgetRepository;
    @Autowired
    private ExpenseService expenseService;

    /**
     * Save new expense budget.
     *
     * @param newExpenseCategory
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed
     */
    @Transactional()
    public ExpenseBudget save(ExpenseBudget newExpenseBudget) {
        if (newExpenseBudget == null) {
            return null;
        }

        return this.expenseBudgetRepository.save(newExpenseBudget);
    }

    /**
     * Updates Expense Budget Expense value and difference (incl. s) of the
     * passed "editExpenseBudget" with the values of updatedExpense. Calls other
     * service methods.
     *
     * @param expense
     * @param editExpenseBudget
     * @return null if not possible
     */
    @Transactional()
    public ExpenseBudget updateExpensesOfAExpenseBudgetCategory(Expense expense) {
        try {
            ExpenseBudget updatedExpenseBudget = this.getExpenseBudgetByExpenseCategory(expense.getExpenseCategory(), expense.getUserId());
            if (expense == null || updatedExpenseBudget == null) {
                return null;
            }
            int expenseCategoryMonthSum = this.expenseService.getCurrentMonthExpensesOfExpenseCategory(expense.getExpenseCategory().getExpenseCategoryId(), expense.getUserId());
            updatedExpenseBudget.setCentActualExpenses(expenseCategoryMonthSum);
            updatedExpenseBudget.setCentDifference(updatedExpenseBudget.getCentBudgetValue() - updatedExpenseBudget.getCentActualExpenses());

            int difference = updatedExpenseBudget.getCentDifference();
            if (difference > 0) {
                updatedExpenseBudget.setS("+");
            } else if (difference < 0) {
                updatedExpenseBudget.setS("-");
            } else {
                updatedExpenseBudget.setS(" ");
            }

            return this.save(updatedExpenseBudget);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get certain expense budget with the passed id. Throws
     * DataValueNotFoundException if expense budget is not available.
     *
     * @param id
     * @return
     */
    public ExpenseBudget getExpenseBudgetById(Long id) {
        return this.expenseBudgetRepository.findByExpensesbudgetId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Budget Does Not Exist"));
    }

    /**
     * Get Expense Budget by Expense category id.
     *
     * @param expenseCategory
     * @param userId
     * @return
     */
    public ExpenseBudget getExpenseBudgetByExpenseCategory(ExpenseCategory expenseCategory, int userId) {
        return this.expenseBudgetRepository.findByExpenseCategoryAndUserId(expenseCategory, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Budget Does Not Exist"));
    }

    /**
     * Get a List of all saved expense budget. Throws DataValueNotFoundException
     * if expense budget is not available.
     *
     * @return
     */
    public List<ExpenseBudget> getAllExpenseBudget(int userId) {
        List<ExpenseBudget> savedExpenseBudget = expenseBudgetRepository.getAllExpenseBudgetList(userId).orElse(new ArrayList<>());
        //ExpenseBudget list with the actual expenses of the current month
        List<ExpenseBudget> generatedExpenseBudget = new ArrayList<>();
        
        if (CollectionUtils.isEmpty(savedExpenseBudget)) {
            throw new DataValueNotFoundException("Expense Budget List could not be loaded!");
        }
        for (ExpenseBudget expenseBudget : savedExpenseBudget) {
            System.out.println("getAllExpenseBudget!!! 11");
            int monthExpensesOfCategory = this.expenseService.getCurrentMonthExpensesOfExpenseCategory(expenseBudget.getExpenseCategory().getExpenseCategoryId(), userId);
            ExpenseBudget updatedExpenseBudget = expenseBudget;
            updatedExpenseBudget.setCentActualExpenses(monthExpensesOfCategory);
            updatedExpenseBudget.setCentDifference(expenseBudget.getCentBudgetValue() - expenseBudget.getCentActualExpenses());
            int difference = updatedExpenseBudget.getCentDifference();
            if (difference > 0) {
                updatedExpenseBudget.setS("+");
            } else if (difference < 0) {
                updatedExpenseBudget.setS("-");
            } else {
                updatedExpenseBudget.setS(" ");
            }
            generatedExpenseBudget.add(updatedExpenseBudget);
        }
        return generatedExpenseBudget;
    }

    /**
     * Delete an existing expense budget
     *
     * @param expenseBudgetId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long expenseBudgetId) {
        if (expenseBudgetId == null || expenseBudgetId == 0) {
            return false;
        }
        ExpenseBudget expenseBudget = this.getExpenseBudgetById(expenseBudgetId);
        try {
            expenseBudget = this.getExpenseBudgetById(expenseBudgetId);
        } catch (DataValueNotFoundException e) {
            return false;
        }

        try {
            this.expenseBudgetRepository.delete(expenseBudget);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete an existing expense budget by expense category id
     *
     * @param expenseBudgetId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByExpenseCategory(ExpenseCategory expenseCategory, int userId) {
        if (expenseCategory == null) {
            return false;
        }
        ExpenseBudget expenseBudget = null;
        try {
            expenseBudget = this.getExpenseBudgetByExpenseCategory(expenseCategory, userId);
        } catch (DataValueNotFoundException e) {
            return false;
        }

        try {
            this.expenseBudgetRepository.delete(expenseBudget);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
