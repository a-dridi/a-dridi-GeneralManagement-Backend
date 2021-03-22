/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseBudget;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseBudgetRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ExpenseBudgetService DAO
 *
 * @author A.Dridi
 */
@Service
@Transactional
@NoArgsConstructor
public class ExpenseBudgetService {

    @Autowired
    private ExpenseBudgetRepository expenseBudgetRepository;

    /**
     * Save new expense budget.
     *
     * @param newExpenseCategory
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed
     */
    public ExpenseBudget save(ExpenseBudget newExpenseBudget) {
        if (newExpenseBudget == null) {
            return null;
        }

        return this.expenseBudgetRepository.save(newExpenseBudget);
    }

    /**
     * Get certain expense budget with the passed id. Throws
     * DataValueNotFoundException if expense budget is not available.
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public ExpenseBudget getExpenseBudgetById(Long id) {
        return this.expenseBudgetRepository.findByExpensesbudgetId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Budget Does Not Exist"));
    }

    /**
     * Get a List of all saved expense budget
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExpenseBudget> getAllExpenseBudget(int userId) {
        return this.expenseBudgetRepository.getAllExpenseBudgetList(userId).orElseThrow(() -> new DataValueNotFoundException("Expense Budget List could not be loaded!"));
    }

    /**
     * Delete an existing expense budget
     *
     * @param expenseBudgetId
     * @return true if successful
     */
    public boolean deleteById(Long expenseBudgetId) {
        if (expenseBudgetId == null || expenseBudgetId == 0) {
            return false;
        }

        ExpenseBudget expenseBudget = this.getExpenseBudgetById(expenseBudgetId);

        try {
            this.expenseBudgetRepository.delete(expenseBudget);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

}
