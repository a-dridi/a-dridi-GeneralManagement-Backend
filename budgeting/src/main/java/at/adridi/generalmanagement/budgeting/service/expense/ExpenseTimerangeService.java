/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseTimerangeRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author A.Dridi
 */
@Service
@Transactional
@NoArgsConstructor
public class ExpenseTimerangeService {

    @Autowired
    private ExpenseTimerangeRepository expenseTimerangeRepository;

    /**
     * Save new expense timerange.
     *
     * @param newExpenseTimerange
     * @return 0 if successful. 1: Passed object is null. 2: Saving failed
     */
    public Integer save(ExpenseTimerange newExpenseTimerange) {
        if (newExpenseTimerange == null) {
            return 1;
        }

        if (this.expenseTimerangeRepository.save(newExpenseTimerange) != null) {
            return 0;
        } else {
            return 2;
        }
    }

    /**
     * Get certain expense timerange with the passed id. Throws
     * DataValueNotFoundException if expense timerange is not available.
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public ExpenseTimerange getExpenseTimerangeById(Long id) {
        return this.expenseTimerangeRepository.findByTimerangeId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Timerange Does Not Exist"));
    }

    /**
     * Get certain expense timerange with the passed title. Throws
     * DataValueNotFoundException if expense timerange is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public ExpenseTimerange getExpenseTimerangeByTitle(String title) {
        return this.expenseTimerangeRepository.findByTimerangeTitle(title)
                .orElseThrow(() -> new DataValueNotFoundException("Expense Timerange Does Not Exist"));
    }

    /**
     * Get a List of all saved expense timeranges
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExpenseTimerange> getAllExpenseTimerange() {
        return this.expenseTimerangeRepository.getAllExpenseTimerangeList().orElseThrow(() -> new DataValueNotFoundException("Expense Timerange List could not be loaded!"));
    }

    /**
     * Delete an existing expense timerange
     *
     * @param expenseTimerangeId
     * @return true if successful
     */
    public boolean deleteById(Long expenseTimerangeId) {
        if (expenseTimerangeId == null || expenseTimerangeId == 0) {
            return false;
        }

        ExpenseTimerange expenseTimerange = this.getExpenseTimerangeById(expenseTimerangeId);

        try {
            this.expenseTimerangeRepository.delete(expenseTimerange);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Delete an existing expense timerange
     *
     * @param timerangeTitle
     * @return true if successful
     */
    public boolean deleteByTitle(String timerangeTitle) {
        if (timerangeTitle == null || timerangeTitle.trim().isBlank()) {
            return false;
        }

        ExpenseTimerange expenseTimerange = this.getExpenseTimerangeByTitle(timerangeTitle);

        try {
            this.expenseTimerangeRepository.delete(expenseTimerange);
            return false;
        } catch (Exception e) {
            return false;
        }

    }

}
