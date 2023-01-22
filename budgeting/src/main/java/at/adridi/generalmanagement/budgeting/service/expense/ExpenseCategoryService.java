/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseCategoryRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of expensecategory DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    /**
     * Save new expense category.
     *
     * @param newExpenseCategory
     * @return null if failed. If successful: the saved object. 
     */
    @Transactional()
    public ExpenseCategory save(ExpenseCategory newExpenseCategory) {
        if (newExpenseCategory == null) {
            return null;
        }
        return this.expenseCategoryRepository.save(newExpenseCategory);
    }

    /**
     * Get certain expense category with the passed id. Throws
     * DataValueNotFoundException if expense category is not available.
     *
     * @param id
     * @return
     */
    public ExpenseCategory getExpenseCategoryById(Long id) {
        return this.expenseCategoryRepository.findByExpenseCategoryId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Category Does Not Exist"));
    }

    /**
     * Get certain expense category with the passed title. Throws
     * DataValueNotFoundException if expense category is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    public ExpenseCategory getExpenseCategoryByTitle(String title, int userId) {
        return this.expenseCategoryRepository.findByCategoryTitleAndUserId(title, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Category Does Not Exist"));
    }

    /**
     * Get a List of all saved expense categories of a user. 
     *
     * @return
     */
    public List<ExpenseCategory> getAllExpenseCategory(int userId) {
        return this.expenseCategoryRepository.getAllExpenseCategoryList(userId).orElseThrow(() -> new DataValueNotFoundException("Expense Category List could not be loaded!"));
    }

    /**
     * Delete an existing expense category
     *
     * @param expenseCategoryId
     * @return true if successful
     */
    @Transactional()
    public boolean deleteById(Long expenseCategoryId) {
        if (expenseCategoryId == null || expenseCategoryId == 0) {
            return false;
        }

        ExpenseCategory expenseCategory = this.getExpenseCategoryById(expenseCategoryId);

        try {
            this.expenseCategoryRepository.delete(expenseCategory);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Delete an existing expense category
     *
     * @param categoryTitle
     * @return true if successful
     */
    @Transactional()
    public boolean deleteByTitle(String categoryTitle, int userId) {
        if (categoryTitle == null || categoryTitle.trim().isEmpty()) {
            return false;
        }

        ExpenseCategory expenseCategory = this.getExpenseCategoryByTitle(categoryTitle, userId);

        try {
            this.expenseCategoryRepository.delete(expenseCategory);
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
